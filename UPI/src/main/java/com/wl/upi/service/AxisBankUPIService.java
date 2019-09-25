package com.wl.upi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wl.upi.dao.UPITransactionDAO;
import com.wl.upi.dao.UpiRefundDAO;
import com.wl.upi.model.AxisRefundTransaction;
import com.wl.upi.model.AxisTransactionStatus;
import com.wl.upi.model.AxisUserTxnModel;
import com.wl.upi.model.RefundRequest;
import com.wl.upi.model.TCHRequest;
import com.wl.upi.model.TxnDTO;
import com.wl.upi.model.UPITransactionDTO;
import com.wl.upi.model.UpiRefundResponse;
import com.wl.util.HelperUtil;
import com.wl.util.HttpsClient;
import com.wl.util.JsonUtility;
import com.wl.util.RSAUtil;
import com.wl.util.RSAUtil.Encoding;
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
@Service("axisBankUPIService")
public class AxisBankUPIService implements UPIBankService {

	@Autowired
	private UpiRefundDAO upiRefundDAO;

	@Autowired
	private UPITransactionDAO upiTransactionDAO;
	
	@Autowired
	@Qualifier("settlementService")
	private SettlementService settlementService;

	private static Logger logger = LoggerFactory.getLogger(AxisBankUPIService.class);

	private UpiRefundResponse tokenGeneration(AxisUserTxnModel axisUserTxnModel) {

		String baseURL = BankConfig.get(axisUserTxnModel.getBankCode(), "collectAPIBaseURL");
		String url = baseURL+"/MerchantToken";
		String jsonInput = JsonUtility.convertToJson(axisUserTxnModel);
		//sending the post request to Axis url
		HashMap<String, String> map = new HashMap<>(1);
		map.put("Content-Type", "application/json");
		String resp = HttpsClient.send(url, jsonInput, map);
		UpiRefundResponse response = (UpiRefundResponse) JsonUtility.parseJson(resp, UpiRefundResponse.class);
		logger.debug("Generated Token -->"+response.getData());
		return response;
	}
	public UpiRefundResponse collectRequest(String token,String bankCode){

		String baseURL = BankConfig.get(bankCode, "collectAPIBaseURL");
		String url = baseURL+"/requestCollect/"+token;			
		//sending the post request to Axis url
		HashMap<String, String> map = new HashMap<>(1);
		map.put("Content-Type", "application/json");
		String resp = HttpsClient.send(url, token, map);
		UpiRefundResponse response = (UpiRefundResponse) JsonUtility.parseJson(resp, UpiRefundResponse.class);
		logger.debug("Response data -->"+response.getData());
		return response;

	}
	
	private UpiRefundResponse TransactionStatusCheck(AxisTransactionStatus transactionStatus){

		String baseURL = BankConfig.get(transactionStatus.getBankCode(), "collectAPIBaseURL");
		String url = baseURL+"/status";
		String jsonInput = JsonUtility.convertToJson(transactionStatus);
		//sending the post request to Axis url
		HashMap<String, String> map = new HashMap<>(1);
		map.put("Content-Type", "application/json");
		String resp = HttpsClient.send(url, jsonInput, map);
		UpiRefundResponse response = (UpiRefundResponse) JsonUtility.parseJson(resp, UpiRefundResponse.class);
		logger.debug("Transaction status result-->"+response.getResult());
		return response;

	}
	public UpiRefundResponse refundTransaction(AxisRefundTransaction refundTransaction){

		String baseURL = BankConfig.get(refundTransaction.getBankCode(), "collectAPIBaseURL");
		String url = baseURL;//+"/refund";		
		String jsonInput = JsonUtility.convertToJson(refundTransaction);       
		//sending the post request to Axis url
		HashMap<String, String> map = new HashMap<>(1);
		map.put("Content-Type", "application/json");
		String resp = HttpsClient.send(url, jsonInput, map);
		logger.debug("Json Request to Axis :"+jsonInput);
		//String resp = RestTemplateUtil.sendPostRequest(url, jsonInput);
		UpiRefundResponse response = (UpiRefundResponse) JsonUtility.parseJson(resp, UpiRefundResponse.class);
		logger.debug("Transaction refund status-->"+response.getResult());
		return response;		
	}
	
	

	@Override
	public Response upiCollectService(String fromEntity, String jsonRequest) {
		// TODO Auto-generated method stub
		logger.debug("request to upiCollectService :"+jsonRequest);
		AxisUserTxnModel model = (AxisUserTxnModel) JsonUtility.parseJson(jsonRequest, AxisUserTxnModel.class) ;
		if(model==null)
			throw new ApplicationException(ErrorMessages.JSON_FORMAT_ERROR.toString());
		String unqTxnId = HelperUtil.getReferenceNumber();
		model.setUnqTxnId(unqTxnId);
		model.setExpiry(BankConfig.get(model.getBankCode(), "collectApiExpiry"));
		model.setMerchId(BankConfig.get(model.getBankCode(), "axisWSMerchId"));
		model.setMerchChanId(BankConfig.get(model.getBankCode(), "axisWSChannelId"));
		model.setCurrency("INR");

		RSAUtil rsa = null;
		try {
			String publicKeyStr = BankConfig.get("00031", "rsaPublicKey");
			rsa = new RSAUtil(publicKeyStr.getBytes());
			rsa.setEncoding(Encoding.HEX);
		} catch (Exception e1) {
			logger.error("Unable to initialize RSA ",e1);
		}

		String checkSum = model.getMerchId()  + model.getMerchChanId() + model.getUnqTxnId() + model.getUnqCustId() + model.getAmount() + 
				model.getTxnDtl() + model.getCurrency() +  model.getOrderId() + model.getCustomerVpa() +  model.getExpiry()  + model.getsId();
		logger.debug("checksum :"+checkSum);
		if(rsa!=null)
		{
			String encryptedChecksum = rsa.encrypt(checkSum);
			model.setCheckSum(encryptedChecksum);
		}

		UpiRefundResponse resp =   tokenGeneration(model);
		if(resp!=null && "000".equals(resp.getCode()))
		{
			String token = (String) resp.getData();
			resp = collectRequest(token,model.getBankCode());
			if(resp!=null && "00".equals(resp.getCode()))
			{
				Response respObj = new Response(Constants.SUCCESS.name() , resp.getResult(), resp.getData());
				return respObj;
			}
			else
			{
				if(resp!=null)
				{
					Response respObj = new Response(Constants.FAILED.name() , resp.getResult(), resp.getData());
					return respObj;
				}
				else
					throw new ApplicationException("Collect Request failed");
			}
		}
		else 
			throw new ApplicationException("Token Generation failed");
	}

	@Override
	public Response upiRefund(String fromEntity, String bankCode, RefundRequest jsonRequest) {
		// TODO Auto-generated method stub
		logger.debug("request to upiRefund :"+jsonRequest);	
		String refundTxnId = HelperUtil.getRRN(); // generating new RRN
		jsonRequest.setBankCode(bankCode);
		if(jsonRequest.getRefundId().length() < 4){
			logger.debug(ErrorMessages.REFUNDID_LENGTH.toString());
			throw new ApplicationException(ErrorMessages.REFUNDID_LENGTH.toString());
		}
		
		Map<String, Double> refMap = upiTransactionDAO.checkRefundIdExist(jsonRequest.getRrn(),
				jsonRequest.getTid());
		String refund_id=""; 
		Double refund_amountd=0.0;
		boolean refExistFlag=false;
		if (refMap.size() > 0) {
			for (Map.Entry<String, Double> entry : refMap.entrySet()) {
				logger.debug("Key = " + entry.getKey() + ", Value = " + entry.getValue());
				refund_id=entry.getKey();
				if(refund_id.equals(jsonRequest.getRefundId())){
					refExistFlag=true;
					break;
				 }
			}

		}
		 if(refExistFlag){
			 logger.debug(ErrorMessages.REFUND_ALREADY.toString());
				throw new ApplicationException(ErrorMessages.REFUND_ALREADY.toString());
		 }
		// changes comment based on code review
		/*List<String> args = new ArrayList<>(2);
		args.add(tchRequest.getTxnId());
		args.add(bankCode);*/
		

		TxnDTO upiTxn=null;
		try {
			upiTxn = upiTransactionDAO.getUpiTxnDetails(jsonRequest.getTid(), jsonRequest.getRrn());
		} catch (DaoException e2) {
			// TODO Auto-generated catch block
			logger.error("Exception in checkStatus"+e2.getMessage());
			throw new ApplicationException(ErrorMessages.ORG_TXN_NOT_FOUND.toString());
		}
		logger.debug("UPITransactionDTO :"+upiTxn);
		if(upiTxn==null)
			throw new ApplicationException(ErrorMessages.ORG_TXN_NOT_FOUND.toString());
		
		AxisRefundTransaction refund = new AxisRefundTransaction();
		//refund.setTxnRefundId(refundTxnId);
		refund.setTxnRefundId(jsonRequest.getRrn());
		refund.setMerchId(BankConfig.get(bankCode, "axisWSMerchId"));
		refund.setMerchChanId(BankConfig.get(bankCode, "axisWSChannelId"));
		refund.setTxnRefundAmount(jsonRequest.getRefundAmount());
		refund.setUnqTxnId(upiTxn.getTrId());
		refund.setRefundReason(jsonRequest.getRefundReason());
		refund.setMobNo(jsonRequest.getMobileNumber());
		refund.setsId(upiTxn.getMerchantId());
		refund.setBankCode(bankCode);	
		upiTxn.setNewRRn(refundTxnId); // new RRn generated for refund
		
		RSAUtil rsa = null;
		try {
			String publicKeyStr = BankConfig.get(bankCode, "rsaPublicKey");
			rsa = new RSAUtil(publicKeyStr.getBytes());
			rsa.setEncoding(Encoding.HEX);
		} catch (Exception e1) {
			logger.error("Unable to initialize RSA ",e1);
			throw new ApplicationException("Server Internal Error");
		}

		String checkSum = refund.getMerchId()  + refund.getMerchChanId() + refund.getTxnRefundId() + refund.getMobNo() + refund.getTxnRefundAmount() + refund.getUnqTxnId() + refund.getRefundReason() + refund.getsId();
		logger.debug("checksum :"+checkSum);
		if(rsa!=null)
		{
			String encryptedChecksum = rsa.encrypt(checkSum);
			refund.setCheckSum(encryptedChecksum);
		}
		UpiRefundResponse resp =   refundTransaction(refund);
		/*AxisResponse resp = new AxisResponse();
		resp.setCode("000");
		resp.setResult("Success");
		resp.setData("123456");*/
		Response respObj = null;
		
		try {
			if (upiTxn != null) {
				List<String> arg = new ArrayList<>(14);
				arg.add(upiTxn.getTrId());
				arg.add(refund.getMobNo()); // mobile Number
				arg.add(jsonRequest.getRefundAmount()); // refund amount
				arg.add(refund.getTxnRefundId()); // refund txn id
				arg.add(refund.getRefundReason()); // refund reason
				arg.add(upiTxn.getRrn()); // RRN old RRN (sale RRN)
				arg.add(upiTxn.getTxnAmount()); // txn amount
				arg.add(bankCode); // bankcode
				arg.add(upiTxn.getMerchantId()); // MID
				arg.add(upiTxn.getTid()); // TID
				arg.add(upiTxn.getCustomerVpa()); // customer VPA
				arg.add(fromEntity);
				arg.add(resp.getCode()); // resp code
				arg.add(resp.getResult()); // resp message
				arg.add(jsonRequest.getRefundId() == null ? "" : jsonRequest.getRefundId()); // storing refundId coming from
																								// request
				arg.add(upiTxn.getMerchantVpa());
				upiRefundDAO.saveRefundTransaction(arg.toArray());
			}
		} catch (Exception e) {
			logger.error("Exception in upiRefund", e);
		}
		
		if (resp != null && "000".equals(resp.getCode())) {
			//Sending response 
			Map<String, String> map = new LinkedHashMap<String, String>(4);
			map.put("rrn", refund.getTxnRefundId());
			map.put("refundAmount", refund.getTxnRefundAmount());
			map.put("txnAmount", upiTxn.getTxnAmount());
			map.put("TID", upiTxn.getTid());
			map.put("refundId", jsonRequest.getRefundId());
			map.put("merchantId", upiTxn.getMerchantId());
			respObj = new Response(Constants.SUCCESS.name(), resp.getResult(), map);
			upiTransactionDAO.updateRefundStatus(upiTxn.getTrId(), "S");
			
			// Refund successful transaction for settlement
			logger.info(" Outside the settelment ");
			settlementService.settleRefundTxn(upiTxn, jsonRequest);
			
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
	
	
	public Response upiRefund(String fromEntity, String bankCode, String jsonRequest) {
		// TODO Auto-generated method stub
		logger.debug("request to upiRefund :"+jsonRequest);
		TCHRequest tchRequest = (TCHRequest) JsonUtility.parseJson(jsonRequest, TCHRequest.class) ;
		if(tchRequest==null)
			throw new ApplicationException(ErrorMessages.JSON_FORMAT_ERROR.toString());
		tchRequest.setBankCode(bankCode);
		String refundTxnId = HelperUtil.getRRN(); 

		// changes comment based on code review
		/*List<String> args = new ArrayList<>(2);
		args.add(tchRequest.getTxnId());
		args.add(bankCode);*/
		
		String[] args = new String[2];
		args[0] = tchRequest.getTxnId();
		args[1] = bankCode;

		UPITransactionDTO upiTxn=null;
		try {
			upiTxn = upiTransactionDAO.checkUpiTxn(args);
		} catch (DaoException e2) {
			// TODO Auto-generated catch block
			logger.error("Exception in checkStatus"+e2.getMessage());
			throw new ApplicationException("Transaction not found for transaction id");
		}
		logger.debug("UPITransactionDTO :"+upiTxn);

		AxisRefundTransaction refund = new AxisRefundTransaction();
		refund.setTxnRefundId(refundTxnId);
		refund.setMerchId(BankConfig.get(tchRequest.getBankCode(), "axisWSMerchId"));
		refund.setMerchChanId(BankConfig.get(tchRequest.getBankCode(), "axisWSChannelId"));
		refund.setTxnRefundAmount(tchRequest.getRefundAmount());
		refund.setUnqTxnId(upiTxn.getMerchantTransactionId());
		refund.setRefundReason(tchRequest.getRefundReason());
		refund.setMobNo(tchRequest.getMobileNumber());
		refund.setsId(tchRequest.getMid());
		refund.setBankCode(bankCode);
		RSAUtil rsa = null;
		try {
			String publicKeyStr = BankConfig.get(bankCode, "rsaPublicKey");
			rsa = new RSAUtil(publicKeyStr.getBytes());
			rsa.setEncoding(Encoding.HEX);
		} catch (Exception e1) {
			logger.error("Unable to initialize RSA ",e1);
			throw new ApplicationException("Server Internal Error");
		}

		String checkSum = refund.getMerchId()  + refund.getMerchChanId() + refund.getTxnRefundId() + refund.getMobNo() + refund.getTxnRefundAmount() + refund.getUnqTxnId() + refund.getRefundReason() + refund.getsId();
		logger.debug("checksum :"+checkSum);
		if(rsa!=null)
		{
			String encryptedChecksum = rsa.encrypt(checkSum);
			refund.setCheckSum(encryptedChecksum);
		}
		UpiRefundResponse resp =   refundTransaction(refund);
		/*AxisResponse resp = new AxisResponse();
		resp.setCode("000");
		resp.setResult("Success");
		resp.setData("123456");*/
		if(resp!=null && "000".equals(resp.getCode()))
		{
			//UPITransactionDTO upiTxn=null;
			try {
				if(upiTxn!=null)
				{
					List<String> arg = new ArrayList<>(14);
					arg.add(upiTxn.getMerchantTransactionId());
					arg.add(refund.getMobNo()) ; // mobile Number
					arg.add(refund.getTxnRefundAmount()); // refund amount
					arg.add(refund.getTxnRefundId()); // refund txn id
					arg.add(refund.getRefundReason()); // refund reason
					arg.add(upiTxn.getRrn()); // RRN
					arg.add(upiTxn.getTransactionAmount()); // txn amount
					arg.add(bankCode); // bankcode
					arg.add(upiTxn.getMerchantId()); // MID
					arg.add(upiTxn.getTerminalId()); // TID
					arg.add(upiTxn.getCustomerVpa()); // customer VPA
					arg.add(fromEntity);
					upiRefundDAO.saveRefundTransaction(arg.toArray());
				}
			} catch (Exception e) {
				logger.error("Exception in upiRefund",e);
			}
			HashMap<String,String> map = new HashMap<String,String>(1);
			map.put("rrn", refund.getTxnRefundId());
			Response respObj = new Response(Constants.SUCCESS.name() , resp.getResult(), map);
			upiTransactionDAO.updateRefundStatus(upiTxn.getMerchantTransactionId(), "S");
			return respObj;
		}
		else 
		{
			Response respObj = new Response(Constants.FAILED.name() , resp.getResult(), null);
			return respObj;
		}
	}
	
	@Override
	public Response upiRefundRecon(String fromEntity, String bankCode, RefundRequest refRequest, TxnDTO upiTxn) {
		
		
		double refundAmountRequest = Double.parseDouble(refRequest.getRefundAmount());
        double actualTxnAmount = Double.parseDouble(upiTxn.getTxnAmount());
        if(refundAmountRequest < 0.0 || refundAmountRequest > actualTxnAmount)
        	  throw new ApplicationException(ErrorMessages.INVALID_AMOUNT.toString());
        double totalRefundedAmt = upiRefundDAO.getSumForRefundedAmount(upiTxn.getRrn(), bankCode);
        double currentRefundAmtCheck = totalRefundedAmt+refundAmountRequest;
        if(currentRefundAmtCheck > actualTxnAmount)
        	throw new ApplicationException(ErrorMessages.REFUND_EXCEEDED.toString());
		
		
		logger.debug("request to upiRefund :"+refRequest);	
		String refundTxnId = HelperUtil.getRRN(); // new RRN generated an send as refundId in response
		refRequest.setBankCode(bankCode);
		logger.info("Refund id value :"+refRequest.getRefundId());
		if(refRequest.getRefundId() != null){
				if(refRequest.getRefundId().length() < 4){
					logger.debug(ErrorMessages.REFUNDID_LENGTH.toString());
					throw new ApplicationException(ErrorMessages.REFUNDID_LENGTH.toString());
				}
		}
		
		AxisRefundTransaction refund = new AxisRefundTransaction();
		//refund.setTxnRefundId(refundTxnId);
		//refund.setTxnRefundId(refRequest.getRrn());
		//refund.setTxnRefundId(upiTxn.getOriginalTrID());
		refund.setGateWayTxnId(upiTxn.getOriginalTrID());
		refund.setMerchId(BankConfig.get(bankCode, "axisWSMerchId"));
		refund.setMerchChanId(BankConfig.get(bankCode, "axisWSChannelId"));
		refund.setTxnRefundAmount(refRequest.getRefundAmount());
		refund.setUnqTxnId(upiTxn.getTrId());
		//refund.setTxnRefundId(upiTxn.getTrId());
		refund.setTxnRefundId(refundTxnId);
		refund.setRefundReason(refRequest.getRefundReason());
		refund.setMobNo(refRequest.getMobileNumber());
		refund.setsId(upiTxn.getMerchantId());
		refund.setBankCode(bankCode);	
		//refund.setNewRRN(refundTxnId);// new RRn generated for refund
		//upiTxn.setNewRRn(refundTxnId); 
		RSAUtil rsa = null;
		try {
			String publicKeyStr = BankConfig.get(bankCode, "rsaPublicKey");
			rsa = new RSAUtil(publicKeyStr.getBytes());
			rsa.setEncoding(Encoding.HEX);
		} catch (Exception e1) {
			logger.error("Unable to initialize RSA ",e1);
			throw new ApplicationException("Server Internal Error");
		}

		String checkSum = refund.getMerchId()  + refund.getMerchChanId() + refund.getUnqTxnId() + refund.getMobNo() + refund.getTxnRefundAmount() + refund.getGateWayTxnId() + refund.getRefundReason() + refund.getsId();
		logger.debug("checksum :"+checkSum);
		if(rsa!=null)
		{
			String encryptedChecksum = rsa.encrypt(checkSum);
			refund.setCheckSum(encryptedChecksum);
		}
		UpiRefundResponse resp =   refundTransaction(refund);
		/*AxisResponse resp = new AxisResponse();
		resp.setCode("000");
		resp.setResult("Success");
		resp.setData("123456");*/
		Response respObj = null;
		
		try {
			if (upiTxn != null) {
				List<String> arg = new ArrayList<>(14);
				arg.add(upiTxn.getTrId());
				arg.add(refund.getMobNo()); // mobile Number
				arg.add(refRequest.getRefundAmount()); // refund amount
				arg.add(refund.getTxnRefundId()); // refund txn id
				arg.add(refund.getRefundReason()); // refund reason
				arg.add(upiTxn.getRrn()); // RRN old RRN (sale RRN)
				arg.add(upiTxn.getTxnAmount()); // txn amount
				arg.add(bankCode); // bankcode
				arg.add(upiTxn.getMerchantId()); // MID
				arg.add(upiTxn.getTid()); // TID
				arg.add(upiTxn.getCustomerVpa()); // customer VPA
				arg.add(fromEntity);
				arg.add(resp.getCode()); // resp code
				arg.add(resp.getResult()); // resp message
				arg.add(refRequest.getRefundId() == null ? "" : refRequest.getRefundId()); // storing refundId coming from
																								// request
				arg.add(upiTxn.getMerchantVpa());
				upiRefundDAO.saveRefundTransaction(arg.toArray());
			}
		} catch (Exception e) {
			logger.error("Exception in upiRefund", e);
		}
		
		if (resp != null && "000".equals(resp.getCode())) {
			//Sending response 
			Map<String, String> map = new LinkedHashMap<String, String>(4);
			map.put("rrn", refund.getTxnRefundId());
			map.put("refundAmount", refund.getTxnRefundAmount());
			map.put("txnAmount", upiTxn.getTxnAmount());
			map.put("TID", upiTxn.getTid());
			map.put("refundId",refundTxnId);
			respObj = new Response(Constants.SUCCESS.name(), resp.getResult(), map);
			upiTransactionDAO.updateRefundStatus(upiTxn.getTrId(), "S");
			// Refund successful transaction for settlement
			/*logger.info(" Outside the settelment ");
			settlementService.settleRefundTxn(upiTxn, refRequest);*/
			
		} else {
			respObj = new Response(Constants.FAILED.name(), resp.getResult(), null);
		}
		return respObj;
	}
}
