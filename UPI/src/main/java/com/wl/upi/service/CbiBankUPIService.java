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

import com.wl.upi.dao.MerchantDAO;
import com.wl.upi.dao.UPITransactionDAO;
import com.wl.upi.dao.UpiRefundDAO;
import com.wl.upi.model.CbiBankRefundTransaction;
import com.wl.upi.model.MerchantDetail;
import com.wl.upi.model.RefundRequest;
import com.wl.upi.model.TxnDTO;
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


@Service("cbiBankUpiService")
public class CbiBankUPIService implements UPIBankService {
	
	@Autowired
	private UpiRefundDAO upiRefundDao;
	
	@Autowired
	private UPITransactionDAO upiTransactionDAO;
	
	@Autowired
	private MerchantDAO merchantDAO;
	
	@Autowired
	@Qualifier("settlementService")
	private SettlementService settlementService;
	
	private static Logger logger = LoggerFactory.getLogger(CbiBankUPIService.class);

	@Override
	public Response upiCollectService(String fromEntity, String jsonRequest) {
		throw new ApplicationException("Not Supported");
	}
	
	

	@Override
	public Response upiRefund(String fromEntity, String bankCode, RefundRequest jsonRequest) {
		
		logger.info("FormEntity -----------------------> ",fromEntity );
		logger.info("bankCode -----------------------> ", bankCode );
		logger.info("jsonRequest -----------------------> " , jsonRequest );
		
		// TODO Auto-generated method stub
		logger.debug("Inside the cbi upi refund");
		logger.debug("request to upi refund :" ,jsonRequest);
		String refundTxnId = HelperUtil.getRRN(); //generating new RRN
		jsonRequest.setBankCode(bankCode);
		
	
		TxnDTO upiTxn=null;
		//TxnDTO upiRefundTxn=null;
		MerchantDetail md = null;
		try {
			upiTxn = upiTransactionDAO.getUpiTxnDetails(jsonRequest.getTid(), jsonRequest.getRrn());
			md = merchantDAO.getMerchantDetails(jsonRequest.getRrn(),jsonRequest.getTid());
		} catch (DaoException e2) {
			// TODO Auto-generated catch block
			logger.error("Exception in checkStatus",e2.getMessage());
			throw new ApplicationException(ErrorMessages.ORG_TXN_NOT_FOUND.toString());
		}
		logger.debug("UPITransactionDTO :",upiTxn);
		if(upiTxn==null)
			throw new ApplicationException(ErrorMessages.ORG_TXN_NOT_FOUND.toString());
		
		/**@pranav checking if refund has already done 
		 * */
		/*upiRefundTxn = upiTransactionDAO.getUpiRefundTxnDetails(jsonRequest.getTid(), jsonRequest.getRrn());
		if (upiRefundTxn != null) {
			if (upiRefundTxn.getResponseCode().equals("00") || upiRefundTxn.getResponseCode().equals("000")
					|| upiRefundTxn.getResponseCode().equalsIgnoreCase("S")) {
				throw new ApplicationException(ErrorMessages.REFUND_ALREADY.toString());
			}
		}*/
		
		logger.debug("merchant_category_code :",md);
		if(md==null)
			throw new ApplicationException(ErrorMessages.ORG_TXN_NOT_FOUND.toString());
		
		
		CbiBankRefundTransaction cbiRefundTxn = new CbiBankRefundTransaction();
		cbiRefundTxn.setBankCode(bankCode);
		if("00006".equals(bankCode)) cbiRefundTxn.setEntityId("boi");
		else cbiRefundTxn.setEntityId("centralbank");
		cbiRefundTxn.setMerchantId(upiTxn.getMerchantId());
		cbiRefundTxn.setTxnRefundId(refundTxnId);
	    cbiRefundTxn.setMobileNo(jsonRequest.getMobileNumber());
		cbiRefundTxn.setRrn(upiTxn.getRrn());
		cbiRefundTxn.setTxnRefundAmount(jsonRequest.getRefundAmount());
		cbiRefundTxn.setOriginalTxnId(upiTxn.getOriginalTrID());
		upiTxn.setNewRRn(refundTxnId);// setting new RRN
		logger.debug("Gateway_Trans_id(an original txn id) :"+upiTxn.getOriginalTrID());
		
		cbiRefundTxn.setRefundReason(jsonRequest.getRefundReason());
		cbiRefundTxn.setBankCode(bankCode);
		
		/*RSAUtil rsa = null;
		try {
			String publicKeyStr = BankConfig.get(bankCode, "rsaPublicKey");
			rsa = new RSAUtil(publicKeyStr.getBytes());
			rsa.setEncoding(Encoding.HEX);
		} catch (Exception e1) {
			logger.error("Unable to initialize RSA ",e1);
			throw new ApplicationException("Server Internal Error");
		}*/
		//payer=merchandAdd and payee= customerAdd during refund
		/*int x =md.getMcc();
		String s = Integer.toString(x);*/
		cbiRefundTxn.setPayerAddr(upiTxn.getMerchantVpa());
		cbiRefundTxn.setPayeeAddr(upiTxn.getCustomerVpa());
		cbiRefundTxn.setTxnType("REFUND");
		cbiRefundTxn.setOrgTxnType("PAY");
		cbiRefundTxn.setChannelId("UPI");
		cbiRefundTxn.setAggregatorCode("WORLDLINE");
		cbiRefundTxn.setMcc(String.valueOf(md.getMcc()));
		
		logger.debug("mcc in double quotes ",String.valueOf(md.getMcc()));
		logger.debug("Gateway_Trans_id(an original txn id) :",upiTxn.getCustomerVpa());
		//logger.info("mcc " , String.valueOf(md.getMcc()));
		cbiRefundTxn.setmPin("orntDlgm9RuPg+dWnwtWXg==");
		//cbiRefundTxn.setmPin("0FtpyOUuq2Okvl0BPumutg==");
		
		
		
		
		String checkSum=null;
		try{
			String chksumData = cbiRefundTxn.getEntityId() + cbiRefundTxn.getMerchantId() + cbiRefundTxn.getTxnRefundId() + cbiRefundTxn.getMobileNo()
			+ cbiRefundTxn.getRrn() + cbiRefundTxn.getTxnRefundAmount() + cbiRefundTxn.getOriginalTxnId() + cbiRefundTxn.getRefundReason() + cbiRefundTxn.getPayerAddr()
			+ cbiRefundTxn.getPayeeAddr() + cbiRefundTxn.getTxnType() + cbiRefundTxn.getOrgTxnType() + cbiRefundTxn.getChannelId() + cbiRefundTxn.getAggregatorCode()
			+ cbiRefundTxn.getMcc() + cbiRefundTxn.getmPin();
			logger.debug("Checksum generated values are " + chksumData);
			checkSum = SHA256CheckSum.genChecksum(chksumData);
		}catch(Exception e){
			logger.error("Problem at the time of generating Checksum ",e);
			throw new ApplicationException(ErrorMessages.SERVER_ERROR.toString());
		}
		checkSum = checkSum.toUpperCase();
		logger.debug("checksum :"+checkSum);
		cbiRefundTxn.setChecksum(checkSum);
		
		//Encryption using psp name
		
		
		String newJsonRequest  = JsonUtility.convertToJson(cbiRefundTxn, true);
		logger.info("Refund Request JSON :"+newJsonRequest);
		
		String pspName = PSPBankCodeMap.getPspName(bankCode);
		logger.debug("PSP name:"+pspName);
		
		
		String encJson=null;
		try {
			encJson = EncryptionCache.getEncryptionUtility(pspName).encrypt(newJsonRequest);
		} catch (Exception e1) {
			logger.error("Error while encrytion of refund json",e1);
			throw new ApplicationException(ErrorMessages.SERVER_ERROR.toString());
		}
		
		
		Map<String,String> req = new LinkedHashMap<>();
		req.put("data", encJson);
		String encJsonRequest  = JsonUtility.convertToJson(req);
		logger.info("Encrypted Request JSON :"+encJsonRequest);
		
		
		HashMap<String, String> headers = new HashMap<>(1);
		headers.put("Content-Type", "application/json");
		
		String response = HttpsClient.send(BankConfig.get(bankCode, "upi_refund_url"), encJsonRequest, headers);
		logger.info("Response from psp:"+response);
		
		//CbiRefundResponse resp = (CbiRefundResponse) JsonUtility.parseJson(response, CbiRefundResponse.class);
		
		//UpiRefundResponse resp = (UpiRefundResponse) JsonUtility.parseJson(response, UpiRefundResponse.class);
		UpiRefundResponse resp = (UpiRefundResponse) JsonUtility.parseJson(response, UpiRefundResponse.class);
		Response respObj = null;
		logger.info(" outside if  resp.getResult() : " + resp.getResult());
		logger.info(" outside if  resp.getCode() : " + resp.getCode());
		
		try {
			if (upiTxn != null) {
				List<String> arg = new ArrayList<>(14);
				arg.add(upiTxn.getTrId());
				arg.add(cbiRefundTxn.getMobileNo()); // mobile Number
				arg.add(cbiRefundTxn.getTxnRefundAmount()); // refund amount
				arg.add(cbiRefundTxn.getTxnRefundId()); // refund txn id
				arg.add(cbiRefundTxn.getRefundReason()); // refund reason
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
				arg.add(upiTxn.getMerchantVpa()); // merchant VPA
				// arg.add(cbiRefundTxn.getTxnRefundId());
				logger.info("upiTxn.getTrId() :" + upiTxn.getTrId());
				logger.info("cbiRefundTxn.getMobileNo() :" + cbiRefundTxn.getMobileNo());
				logger.info("cbiRefundTxn.getTxnRefundAmount() :" + cbiRefundTxn.getTxnRefundAmount());
				logger.info("cbiRefundTxn.getTxnRefundId() :" + cbiRefundTxn.getTxnRefundId());
				logger.info("cbiRefundTxn.getRefundReason() :" + cbiRefundTxn.getRefundReason());
				logger.info("upiTxn.getRrn() :" + upiTxn.getRrn());
				logger.info("upiTxn.getTxnAmount() :" + upiTxn.getTxnAmount());
				logger.info("bankCode :" + bankCode);
				logger.info("upiTxn.getMerchantId() :" + upiTxn.getMerchantId());
				logger.info("upiTxn.getTid() :" + upiTxn.getTid());
				logger.info("upiTxn.getCustomerVpa():" + upiTxn.getCustomerVpa());
				logger.info("fromEntity:" + fromEntity);
				logger.info("resp.getCode() :" + resp.getCode());
				logger.info("resp.getResult() :" + resp.getResult());
				logger.info("upiTxn.getMerchantVpa():" + upiTxn.getMerchantVpa());
				upiRefundDao.saveRefundTransaction(arg.toArray());
			}
		} catch (Exception e) {
			logger.error("Exception in CbiRefund", e);
		}
		if (resp != null && ("00".equals(resp.getCode()) || "000".equals(resp.getCode()))) {
			logger.info(" Inside if  resp.getResult() : " + resp.getResult());
			Map<String, String> map = new LinkedHashMap<String, String>(4);
			map.put("rrn", cbiRefundTxn.getTxnRefundId());
			map.put("refundAmount", cbiRefundTxn.getTxnRefundAmount());
			map.put("txnAmount", upiTxn.getTxnAmount());
			map.put("TID", upiTxn.getTid());
			logger.info("getting details of response before updating in DB" + map);
			respObj = new Response(Constants.SUCCESS.name(), resp.getResult(), map);
			upiTransactionDAO.updateRefundStatus(upiTxn.getTrId(), "S");

			/*
			 * @pranav if the response is 00 or 000 i.e success from psp then
			 * only the transaction will be entered into MOB table
			 */
				logger.info(" Ready for the settelment ");
				settlementService.settleRefundTxn(upiTxn, jsonRequest);
		} else {
			respObj = new Response(Constants.FAILED.name(), resp.getResult(), null);
			logger.info(" Inside else  resp.getResult() : " + resp.getResult());
		}
		return respObj;
	}

	@Override
	public Response doRefundJob(String string, String bankCode) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Response upiRefund(String fromEntity, String bankCode, String jsonRequest) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Response upiRefundRecon(String fromEntity, String bankCode, RefundRequest jsonRequest, TxnDTO upiTxn) {
		
    	logger.info("FormEntity -----------------------> ",fromEntity );
		logger.info("bankCode -----------------------> ", bankCode );
		logger.info("jsonRequest -----------------------> " , jsonRequest );
		
		
		double refundAmountRequest = Double.parseDouble(jsonRequest.getRefundAmount());
        double actualTxnAmount = Double.parseDouble(upiTxn.getTxnAmount());
        if(refundAmountRequest < 0.0 || refundAmountRequest > actualTxnAmount)
        	  throw new ApplicationException(ErrorMessages.INVALID_AMOUNT.toString());
        double totalRefundedAmt = upiRefundDao.getSumForRefundedAmount(upiTxn.getRrn(), bankCode);
        double currentRefundAmtCheck = totalRefundedAmt+refundAmountRequest;
        if(currentRefundAmtCheck > actualTxnAmount)
        	throw new ApplicationException(ErrorMessages.REFUND_EXCEEDED.toString());
		
		// TODO Auto-generated method stub
		logger.debug("Inside the cbi upi refund");
		logger.debug("request to upi refund :" ,jsonRequest);
		String refundTxnId = HelperUtil.getRRN(); //generating new RRN
		jsonRequest.setBankCode(bankCode);
    	
    	
    	// TODO Auto-generated method stub
    	CbiBankRefundTransaction cbiRefundTxn = new CbiBankRefundTransaction();
		cbiRefundTxn.setBankCode(bankCode);
		if("00006".equals(bankCode)) cbiRefundTxn.setEntityId("boi");
		else cbiRefundTxn.setEntityId("centralbank");
		cbiRefundTxn.setMerchantId(upiTxn.getMerchantId());
		cbiRefundTxn.setTxnRefundId(refundTxnId);
	    cbiRefundTxn.setMobileNo(jsonRequest.getMobileNumber());
		cbiRefundTxn.setRrn(upiTxn.getRrn());
		cbiRefundTxn.setTxnRefundAmount(jsonRequest.getRefundAmount());
		cbiRefundTxn.setOriginalTxnId(upiTxn.getOriginalTrID());
		upiTxn.setNewRRn(refundTxnId);// setting new RRN
		logger.debug("Gateway_Trans_id(an original txn id) :"+upiTxn.getOriginalTrID());
		
		cbiRefundTxn.setRefundReason(jsonRequest.getRefundReason());
		cbiRefundTxn.setBankCode(bankCode);
		
		/*RSAUtil rsa = null;
		try {
			String publicKeyStr = BankConfig.get(bankCode, "rsaPublicKey");
			rsa = new RSAUtil(publicKeyStr.getBytes());
			rsa.setEncoding(Encoding.HEX);
		} catch (Exception e1) {
			logger.error("Unable to initialize RSA ",e1);
			throw new ApplicationException("Server Internal Error");
		}*/
		//payer=merchandAdd and payee= customerAdd during refund
		/*int x =md.getMcc();
		String s = Integer.toString(x);*/
		cbiRefundTxn.setPayerAddr(upiTxn.getMerchantVpa());
		cbiRefundTxn.setPayeeAddr(upiTxn.getCustomerVpa());
		cbiRefundTxn.setTxnType("PAY");
		cbiRefundTxn.setOrgTxnType("PAY");
		cbiRefundTxn.setChannelId("UPI");
		cbiRefundTxn.setAggregatorCode("WORLDLINE");
		cbiRefundTxn.setMcc(jsonRequest.getAuthCode());   //For MCC taken from xls file
		
		logger.debug("mcc in double quotes ",cbiRefundTxn.getMcc()); //For MCC taken from xls file
		logger.debug("Gateway_Trans_id(an original txn id) :",upiTxn.getCustomerVpa());
		//logger.info("mcc " , String.valueOf(md.getMcc()));
		cbiRefundTxn.setmPin("orntDlgm9RuPg+dWnwtWXg==");
		
		
		
		String checkSum=null;
		try{
			String chksumData = cbiRefundTxn.getEntityId() + cbiRefundTxn.getMerchantId() + cbiRefundTxn.getTxnRefundId() + cbiRefundTxn.getMobileNo()
			+ cbiRefundTxn.getRrn() + cbiRefundTxn.getTxnRefundAmount() + cbiRefundTxn.getOriginalTxnId() + cbiRefundTxn.getRefundReason() + cbiRefundTxn.getPayerAddr()
			+ cbiRefundTxn.getPayeeAddr() + cbiRefundTxn.getTxnType() + cbiRefundTxn.getOrgTxnType() + cbiRefundTxn.getChannelId() + cbiRefundTxn.getAggregatorCode()
			+ cbiRefundTxn.getMcc() + cbiRefundTxn.getmPin();
			logger.debug("Checksum generated values are " + chksumData);
			checkSum = SHA256CheckSum.genChecksum(chksumData);
		}catch(Exception e){
			logger.error("Problem at the time of generating Checksum ",e);
			throw new ApplicationException(ErrorMessages.SERVER_ERROR.toString());
		}
		checkSum = checkSum.toUpperCase();
		logger.debug("checksum :"+checkSum);
		cbiRefundTxn.setChecksum(checkSum);
		
		//Encryption using psp name
		
		
		String newJsonRequest  = JsonUtility.convertToJson(cbiRefundTxn, true);
		logger.info("Refund Request JSON :"+newJsonRequest);
		
		String pspName = PSPBankCodeMap.getPspName(bankCode);
		logger.debug("PSP name:"+pspName);
		
		
		String encJson=null;
		try {
			encJson = EncryptionCache.getEncryptionUtility(pspName).encrypt(newJsonRequest);
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
		
		//CbiRefundResponse resp = (CbiRefundResponse) JsonUtility.parseJson(response, CbiRefundResponse.class);
		
		//UpiRefundResponse resp = (UpiRefundResponse) JsonUtility.parseJson(response, UpiRefundResponse.class);
		UpiRefundResponse resp = (UpiRefundResponse) JsonUtility.parseJson(response, UpiRefundResponse.class);
		Response respObj = null;
		logger.info(" outside if  resp.getResult() : " + resp.getResult());
		logger.info(" outside if  resp.getCode() : " + resp.getCode());
		
		try {
			if (upiTxn != null) {
				List<String> arg = new ArrayList<>(14);
				arg.add(upiTxn.getTrId());
				arg.add(cbiRefundTxn.getMobileNo()); // mobile Number
				arg.add(cbiRefundTxn.getTxnRefundAmount()); // refund amount
				arg.add(cbiRefundTxn.getTxnRefundId()); // refund txn id
				arg.add(cbiRefundTxn.getRefundReason()); // refund reason
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
				arg.add(upiTxn.getMerchantVpa()); // merchant VPA
				// arg.add(cbiRefundTxn.getTxnRefundId());
				logger.info("upiTxn.getTrId() :" + upiTxn.getTrId());
				logger.info("cbiRefundTxn.getMobileNo() :" + cbiRefundTxn.getMobileNo());
				logger.info("cbiRefundTxn.getTxnRefundAmount() :" + cbiRefundTxn.getTxnRefundAmount());
				logger.info("cbiRefundTxn.getTxnRefundId() :" + cbiRefundTxn.getTxnRefundId());
				logger.info("cbiRefundTxn.getRefundReason() :" + cbiRefundTxn.getRefundReason());
				logger.info("upiTxn.getRrn() :" + upiTxn.getRrn());
				logger.info("upiTxn.getTxnAmount() :" + upiTxn.getTxnAmount());
				logger.info("bankCode :" + bankCode);
				logger.info("upiTxn.getMerchantId() :" + upiTxn.getMerchantId());
				logger.info("upiTxn.getTid() :" + upiTxn.getTid());
				logger.info("upiTxn.getCustomerVpa():" + upiTxn.getCustomerVpa());
				logger.info("fromEntity:" + fromEntity);
				logger.info("resp.getCode() :" + resp.getCode());
				logger.info("resp.getResult() :" + resp.getResult());
				logger.info("upiTxn.getMerchantVpa():" + upiTxn.getMerchantVpa());
				upiRefundDao.saveRefundTransaction(arg.toArray());
			}
		} catch (Exception e) {
			logger.error("Exception in CbiRefund", e);
		}
		if (resp != null && ("00".equals(resp.getCode()) || "000".equals(resp.getCode()))) {
			logger.info(" Inside if  resp.getResult() : " + resp.getResult());
			Map<String, String> map = new LinkedHashMap<String, String>(4);
			map.put("rrn", cbiRefundTxn.getTxnRefundId());
			map.put("refundAmount", cbiRefundTxn.getTxnRefundAmount());
			map.put("txnAmount", upiTxn.getTxnAmount());
			map.put("TID", upiTxn.getTid());
			logger.info("getting details of response before updating in DB" + map);
			respObj = new Response(Constants.SUCCESS.name(), resp.getResult(), map);
			upiTransactionDAO.updateRefundStatus(upiTxn.getTrId(), "S");

			/*
			 * @pranav if the response is 00 or 000 i.e success from psp then
			 * only the transaction will be entered into MOB table
			 */
				/*logger.info(" Ready for the settelment ");
				settlementService.settleRefundTxn(upiTxn, jsonRequest);*/
		} else {
			respObj = new Response(Constants.FAILED.name(), resp.getResult(), null);
			logger.info(" Inside else  resp.getResult() : " + resp.getResult());
		}
		return respObj;
	}
}
