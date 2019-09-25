package com.wl.upi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wl.upi.dao.UPITransactionDAO;
import com.wl.upi.dao.UpiRefundDAO;
import com.wl.upi.model.MultiBankRefundTxn;
import com.wl.upi.model.RefundRequest;
import com.wl.upi.model.TxnDTO;
import com.wl.upi.model.UPITransactionDTO;
import com.wl.upi.model.UpiRefundResponse;
import com.wl.util.EncryptionCache;
import com.wl.util.HelperUtil;
import com.wl.util.HttpsClient;
import com.wl.util.JsonUtility;
import com.wl.util.PSPBankCodeMap;
import com.wl.util.SHA256CheckSum;
import com.wl.util.config.BankConfig;
import com.wl.util.constants.Constants;
import com.wl.util.constants.ErrorMessages;
import com.wl.util.exceptions.ApplicationException;
import com.wl.util.exceptions.DaoException;
import com.wl.util.model.Response;
/**
 * @author faizul.mahammad
 *
 */
@Service("multiBankUPIService")
public class MultiBankUPIService implements UPIBankService {

	@Autowired
	private UpiRefundDAO upiRefundDAO;

	@Autowired
	private UPITransactionDAO upiTransactionDAO;
	
	@Autowired
	@Qualifier("settlementService")
	private SettlementService settlementService;

	private static Logger logger = LoggerFactory.getLogger(MultiBankUPIService.class);


	@Override
	public Response upiCollectService(String fromEntity, String jsonRequest) {
		throw new ApplicationException("Not Supported");
	}

	@Override
	public Response upiRefund(String fromEntity, String bankCode, RefundRequest refRequest) {
		// TODO Auto-generated method stub
		logger.debug("request to upiRefund :"+refRequest);
		String refundTxnId = HelperUtil.getRRN(); // generating new RRN for refund 
		refRequest.setBankCode(bankCode);


		TxnDTO upiTxn=null;
		//TxnDTO upiRefundTxn =null;
		try {
			upiTxn = upiTransactionDAO.getUpiTxnDetails(refRequest.getTid(), refRequest.getRrn());
		} catch (DaoException e2) {
			// TODO Auto-generated catch block
			logger.error("Exception in checkStatus"+e2.getMessage());
			throw new ApplicationException(ErrorMessages.ORG_TXN_NOT_FOUND.toString());
		}
		logger.debug("UPITransactionDTO :"+upiTxn);
		if(upiTxn==null)
			throw new ApplicationException(ErrorMessages.ORG_TXN_NOT_FOUND.toString());
		
		/**@pranav checking if refund has already done 
		 * */
		/*upiRefundTxn = upiTransactionDAO.getUpiRefundTxnDetails(refRequest.getTid(), refRequest.getRrn());
		if (upiRefundTxn != null) {
			if (upiRefundTxn.getResponseCode().equals("00") || upiRefundTxn.getResponseCode().equals("000")
					|| upiRefundTxn.getResponseCode().equalsIgnoreCase("S")) {
				throw new ApplicationException(ErrorMessages.REFUND_ALREADY.toString());
			}
		}*/
		
		MultiBankRefundTxn refund = new MultiBankRefundTxn();
		refund.setMerchantId(upiTxn.getMerchantId());
		refund.setTxnRefundId(refundTxnId);
		refund.setMobileNumber(refRequest.getMobileNumber());
		refund.setRrn(upiTxn.getRrn());
		refund.setRefundAmount(refRequest.getRefundAmount());
		refund.setOrginalTxnId(upiTxn.getTrId());
		refund.setRefundReason(refRequest.getRefundReason());
		refund.setBankCode(bankCode);
		upiTxn.setNewRRn(refundTxnId);
		String checkSum=null;
		try{
			String chksumData = refund.getMerchantId()  + refund.getTxnRefundId() + refund.getMobileNumber() + refund.getRrn() + refund.getRefundAmount() +
					refund.getOrginalTxnId() + refund.getRefundReason();
			logger.debug("Checksum generated values are "+chksumData);
			checkSum = SHA256CheckSum.genChecksum(chksumData);
		}catch(Exception e){
			logger.error("Problem at the time of generating Checksum ",e);
			throw new ApplicationException(ErrorMessages.SERVER_ERROR.toString());
		}
		checkSum = checkSum.toUpperCase();
		logger.debug("checksum :"+checkSum);
		refund.setCheckSum(checkSum);

		String jsonRequest  = JsonUtility.convertToJson(refund, true);
		logger.info("Refund Request JSON :"+jsonRequest);
		String pspName = PSPBankCodeMap.getPspName(bankCode);
		logger.debug("PSP name:"+pspName);

		String encJson=null;
		try {
			encJson = EncryptionCache.getEncryptionUtility(pspName).encrypt(jsonRequest);
		} catch (Exception e1) {
			logger.error("Error while encrytion of refund json",e1);
			throw new ApplicationException(ErrorMessages.SERVER_ERROR.toString());
		}

		Map<String,String> req = new LinkedHashMap<>();
		req.put("data", encJson);
		String encJsonRequest  = JsonUtility.convertToJson(req);
		logger.info("Encrypted Request JSON :"+jsonRequest);

		HashMap<String, String> headers = new HashMap<>(1);
		headers.put("Content-Type", "application/json");

		String response = HttpsClient.send(BankConfig.get(bankCode, "upi_refund_url"), encJsonRequest, headers);
		logger.info("Response from psp:"+response);

		UpiRefundResponse resp = (UpiRefundResponse) JsonUtility.parseJson(response, UpiRefundResponse.class);
		Response respObj = null;
		
		try {
			if (upiTxn != null) {
				List<String> arg = new ArrayList<>(14);
				arg.add(upiTxn.getTrId());
				arg.add(refund.getMobileNumber()); // mobile Number
				arg.add(refund.getRefundAmount()); // refund amount
				arg.add(refund.getTxnRefundId()); // refund txn id
				arg.add(refund.getRefundReason()); // refund reason
				arg.add(upiTxn.getRrn()); // RRN
				arg.add(upiTxn.getTxnAmount()); // txn amount
				arg.add(bankCode); // bankcode
				arg.add(upiTxn.getMerchantId()); // MID
				arg.add(upiTxn.getTid()); // TID
				arg.add(upiTxn.getCustomerVpa()); // customer VPA
				arg.add(fromEntity);
				arg.add(resp.getCode()); // resp code
				arg.add(resp.getResult()); // resp message
				arg.add(""); // req refund Id ; only incase of axis bank
				arg.add(upiTxn.getMerchantVpa()); // merchant vpa
				upiRefundDAO.saveRefundTransaction(arg.toArray());
				logger.info(" Outside the settelment ");
			}
		} catch (Exception e) {
			logger.error("Exception in upiRefund", e);
		}
		
		if (resp != null && ("000".equals(resp.getCode()) || "00".equals(resp.getCode())
				|| "s".equalsIgnoreCase(resp.getCode()))) {
			Map<String, String> map = new LinkedHashMap<String, String>(4);
			map.put("rrn", refund.getTxnRefundId());
			map.put("refundAmount", refund.getRefundAmount());
			map.put("txnAmount", upiTxn.getTxnAmount());
			map.put("TID", upiTxn.getTid());
			respObj = new Response(Constants.SUCCESS.name(), resp.getResult(), map);
			upiTransactionDAO.updateRefundStatus(upiTxn.getTrId(), "S");
			/**
			 * @pranav If the status is '00' or '000 or 'S' i.e success the the
			 *         data will be entered in to the MOB_PAYMENT_TXN
			 */
			logger.info(" inside the if settelment ");
			settlementService.settleRefundTxn(upiTxn, refRequest);

		} else {
			respObj = new Response(Constants.FAILED.name(), resp.getResult(), null);
		}
		
		return respObj;

	}
	
	@Override
	public Response doRefundJob(String fromEntity, String bankCode) {

		List<UPITransactionDTO> txnList = upiTransactionDAO.getQRDetailsForRefund(bankCode);
		if(txnList.size()==0)
		{
			Response response = new Response(Constants.SUCCESS.name() , "No Transaction found for refund");
			return response;
		}
		for(UPITransactionDTO txn :txnList )
		{
			RefundRequest request = new RefundRequest();
			request.setTid(txn.getTerminalId());
			request.setRefundAmount(txn.getTransactionAmount());
			request.setRefundReason("Auto Refund");
			request.setBankCode(bankCode);
			request.setMobileNumber("9999999999");
			request.setRrn(txn.getRrn());

			if(upiRefundDAO.checkRefundExist(txn.getMerchantTransactionId(), bankCode))
			{
				logger.debug("refund already processed for tr id :"+txn.getMerchantTransactionId());
			}
			else
			{
				Response resp  = null;
				try{
					resp =  upiRefund(fromEntity, bankCode, request);
					logger.debug(txn.getTxnId()+ "|Response :"+resp);
				}catch(Exception e)
				{
					resp = new Response();
					resp.setStatus(Constants.FAILED.name());
					logger.error(txn.getTxnId()+"exception found:"+e.getMessage());
				}
				if(!Constants.SUCCESS.name().equals(resp.getStatus()))
				{
					upiTransactionDAO.updateRefundStatus(txn.getMerchantTransactionId(), "C");
				}
			}
		}
		Response response = new Response(Constants.SUCCESS.name() , "All Required Refund Processed");
		return response;
	}

	@Override
	public Response upiRefund(String fromEntity, String bankCode, String jsonRequest) {
		// TODO Auto-generated method stub
		throw new ApplicationException("Functionality will not be implemented");
	}
	
	@Override
	public Response upiRefundRecon(String fromEntity, String bankCode, RefundRequest refRequest,TxnDTO upiTxn) {
				logger.info("upiRefundRecon ::::: ");
		        double refundAmountRequest = Double.parseDouble(refRequest.getRefundAmount());
		        double actualTxnAmount = Double.parseDouble(upiTxn.getTxnAmount());
		        if(refundAmountRequest < 0.0 || refundAmountRequest > actualTxnAmount)
		        	  throw new ApplicationException(ErrorMessages.INVALID_AMOUNT.toString());
		        double totalRefundedAmt = upiRefundDAO.getSumForRefundedAmount(upiTxn.getRrn(), bankCode);
		        double currentRefundAmtCheck = totalRefundedAmt+refundAmountRequest;
		        if(currentRefundAmtCheck > actualTxnAmount)
		        	throw new ApplicationException(ErrorMessages.REFUND_EXCEEDED.toString());
		        
				logger.info("Actual TXN Amt "+actualTxnAmount);
				logger.info("Refund Amount Request TXN Amt "+refundAmountRequest);
				logger.info("Total RefundedAmt Request TXN Amt "+totalRefundedAmt);
				logger.info("Total RefundedAmt + refundAmountRequest "+currentRefundAmtCheck);
		        
		        logger.debug("request to upiRefund :"+refRequest);
				String refundTxnId = HelperUtil.getRRN(); // generating new RRN for refund 
				refRequest.setBankCode(bankCode);

                logger.debug("UPITransactionDTO :"+upiTxn);
				
                /**@pranav checking if refund has already done 
        		 * */
        		/*upiRefundTxn = upiTransactionDAO.getUpiRefundTxnDetails(refRequest.getTid(), refRequest.getRrn());
        		if (upiRefundTxn != null) {
        			if (upiRefundTxn.getResponseCode().equals("00") || upiRefundTxn.getResponseCode().equals("000")
        					|| upiRefundTxn.getResponseCode().equalsIgnoreCase("S")) {
        				throw new ApplicationException(ErrorMessages.REFUND_ALREADY.toString());
        			}
        		}*/
                MultiBankRefundTxn refund = new MultiBankRefundTxn();
        		
                if("00031".equals(bankCode))
                {
	        		refund.setMerchantId(upiTxn.getMerchantId());
	        		refund.setTxnRefundId(refundTxnId);
	        		refund.setMobileNumber(refRequest.getMobileNumber());
	        		refund.setRrn(upiTxn.getRrn());
	        		refund.setRefundAmount(refRequest.getRefundAmount());
	        		refund.setOrginalTxnId(upiTxn.getTrId());
	        		refund.setRefundReason(refRequest.getRefundReason());
	        		refund.setBankCode(bankCode);
	        		upiTxn.setNewRRn(refundTxnId);
                }
                else if("00075".equals(bankCode))
                {
	        		refund.setMerchantId(upiTxn.getMerchantId());
	        		refund.setTxnRefundId(upiTxn.getRrn());
	        		//refund.setMobileNumber(refRequest.getMobileNumber());
	        		refund.setRrn(upiTxn.getRrn());
	        		refund.setRefundAmount(refRequest.getRefundAmount());
	        		refund.setOrginalTxnId(upiTxn.getTrId());
	        		refund.setRefundReason(refRequest.getRefundReason());
	        		refund.setBankCode(bankCode);
	        		upiTxn.setNewRRn(upiTxn.getRrn());
                }
                String response = null;
                if(!"00075".equals(bankCode))
        		{        			
	        		String checkSum=null;
	        		try{
	        			String chksumData = refund.getMerchantId()  + refund.getTxnRefundId() + refund.getMobileNumber() + refund.getRrn() + refund.getRefundAmount() +
	        					refund.getOrginalTxnId() + refund.getRefundReason();
	        			logger.debug("Checksum generated values are "+chksumData);
	        			checkSum = SHA256CheckSum.genChecksum(chksumData);
	        		}catch(Exception e){
	        			logger.error("Problem at the time of generating Checksum ",e);
	        			throw new ApplicationException("Problem at the time of generating Checksum ");
	        		}
	        		checkSum = checkSum.toUpperCase();
	        		logger.debug("checksum :"+checkSum);
	        		refund.setCheckSum(checkSum);
	
	        		String jsonRequest  = JsonUtility.convertToJson(refund, true);
	        		logger.info("Refund Request JSON :"+jsonRequest);
	        		String pspName = PSPBankCodeMap.getPspName(bankCode);
	        		logger.debug("PSP name:"+pspName);
	
	        		String encJson=null;
	        		try {
	        			encJson = EncryptionCache.getEncryptionUtility(pspName).encrypt(jsonRequest);
	        		} catch (Exception e1) {
	        			logger.error("Error while encrytion of refund json",e1);
	        			throw new ApplicationException("Error while encrytion of refund json");
	        		}
	
	        		Map<String,String> req = new LinkedHashMap<>();
	        		req.put("data", encJson);
	        		String encJsonRequest  = JsonUtility.convertToJson(req);
	        		logger.info("Encrypted Request JSON :"+jsonRequest);
	
	        		HashMap<String, String> headers = new HashMap<>(1);
	        		headers.put("Content-Type", "application/json");
	        		
	        			response = HttpsClient.send(BankConfig.get(bankCode, "upi_refund_url"), encJsonRequest, headers);
        		}
        		response ="{\"code\":\"00\",\"result\":\"success\",\"data\":\"{}\"}";
        		//response="{'code':'00','result':'success','data':''}";
        		logger.info("Response from psp:"+response);

        		UpiRefundResponse resp = (UpiRefundResponse) JsonUtility.parseJson(response, UpiRefundResponse.class);
        		Response respObj = null;
        		
        		try {
        			if (upiTxn != null) {
        				List<String> arg = new ArrayList<>(14);
        				if("00031".equals(bankCode))
        				{
	        				arg.add(upiTxn.getTrId());
	        				arg.add(refund.getMobileNumber()); // mobile Number
	        				arg.add(refund.getRefundAmount()); // refund amount
	        				arg.add(refund.getTxnRefundId()); // refund txn id
	        				arg.add(refund.getRefundReason()); // refund reason
	        				arg.add(upiTxn.getRrn()); // RRN
	        				arg.add(upiTxn.getTxnAmount()); // txn amount
	        				arg.add(bankCode); // bankcode
	        				arg.add(upiTxn.getMerchantId()); // MID
	        				arg.add(upiTxn.getTid()); // TID
	        				arg.add(upiTxn.getCustomerVpa()); // customer VPA
	        				arg.add(fromEntity);
	        				arg.add(resp.getCode()); // resp code
	        				arg.add(resp.getResult()); // resp message
	        				arg.add(""); // req refund Id ; only incase of axis bank
	        				arg.add(upiTxn.getMerchantVpa()); // merchant vpa
	        				upiRefundDAO.saveRefundTransaction(arg.toArray());
	        				logger.info(" Outside the settelment "+arg);
        				}
        				else if("00075".equals(bankCode))
        				{
        					arg.add(upiTxn.getTrId());
	        				arg.add(""); // mobile Number
	        				arg.add(refund.getRefundAmount()); // refund amount
	        				arg.add(refund.getTxnRefundId()); // refund txn id
	        				arg.add(refund.getRefundReason()); // refund reason
	        				arg.add(upiTxn.getRrn()); // RRN
	        				arg.add(upiTxn.getTxnAmount()); // txn amount
	        				arg.add(bankCode); // bankcode
	        				arg.add(upiTxn.getMerchantId()); // MID
	        				arg.add(upiTxn.getTid()); // TID
	        				arg.add(upiTxn.getCustomerVpa()); // customer VPA
	        				arg.add(fromEntity);
	        				arg.add(resp.getCode()); // resp code
	        				arg.add(resp.getResult()); // resp message
	        				arg.add(""); // req refund Id ; only incase of axis bank
	        				arg.add(upiTxn.getMerchantVpa()); // merchant vpa
	        				upiRefundDAO.saveRefundTransaction(arg.toArray());
	        				logger.info(" Outside the settelment for sbi :::"+arg);
        				}
        			}
        		} catch (Exception e) {
        			logger.error("Exception in upiRefund", e);
        		}
        		
        		if("00031".equals(bankCode))
        		{  
	        		if (resp != null && ("000".equals(resp.getCode()) || "00".equals(resp.getCode())
	        				|| "s".equalsIgnoreCase(resp.getCode()))) {
	        			Map<String, String> map = new LinkedHashMap<String, String>(4);
	        			map.put("rrn", refund.getTxnRefundId());
	        			/*map.put("refundAmount", refund.getRefundAmount());
	        			map.put("txnAmount", upiTxn.getTxnAmount());
	        			map.put("TID", upiTxn.getTid());*/
	        			map.put("refundId", refund.getTxnRefundId());
	        			map.put("merchId", refund.getTxnRefundId());
	        			respObj = new Response(Constants.SUCCESS.name(), resp.getResult(), map);
	        			upiTransactionDAO.updateRefundStatus(upiTxn.getTrId(), "S");
	        			/**
	        			 * @pranav If the status is '00' or '000 or 'S' i.e success the the
	        			 *         data will be entered in to the MOB_PAYMENT_TXN
	        			 */
	        			//logger.info(" inside the if settelment ");
	        			//settlementService.settleRefundTxn(upiTxn, refRequest);
	
	        		} else {
	        			respObj = new Response(Constants.FAILED.name(), resp.getResult(), null);
	        		}
        		}
        		else if("00075".equals(bankCode))
        		{
        			if (resp != null && ("000".equals(resp.getCode()) || "00".equals(resp.getCode())
	        				|| "s".equalsIgnoreCase(resp.getCode()))) {
	        			respObj = new Response(Constants.SUCCESS.name(), resp.getResult(), null);
	        		logger.info("respObj ::::::"+respObj);
	        		} else {
	        			respObj = new Response(Constants.FAILED.name(), resp.getResult(), null);
	        		}
        		}
        	return respObj;
	}
}
