package com.wl.upi.service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wl.upi.dao.MerchantDAO;
import com.wl.upi.dao.UPITransactionDAO;
import com.wl.upi.model.IndusIndRefundDTO;
//import com.wl.upi.model.IndusIndRefundResponeDTO;
import com.wl.upi.model.MerchantDetail;
import com.wl.upi.model.RefundRequest;
import com.wl.upi.model.TxnDTO;
import com.wl.upi.model.UpiRefundResponse;
import com.wl.util.HelperUtil;
import com.wl.util.HttpsClient;
import com.wl.util.JsonUtility;
import com.wl.util.config.BankConfig;
import com.wl.util.constants.Constants;
import com.wl.util.constants.ErrorMessages;
import com.wl.util.exceptions.ApplicationException;
import com.wl.util.exceptions.DaoException;
import com.wl.util.model.Response;

public class IndusIndService implements UPIBankService{

	private static Logger logger = LoggerFactory.getLogger(IndusIndService.class);
	
	UPITransactionDAO upitransactiondao;
	
	MerchantDAO merchantdao;
	

	@Override
	public Response upiCollectService(String fromEntity, String jsonRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	public Response upiRefund(String fromEntity, String bankCode, RefundRequest jsonRequest) {
		
		String newRefundRRN = HelperUtil.getRRN();
		
		TxnDTO txndetail =  null;
		MerchantDetail merchantdetail = null;
		try{
			txndetail = upitransactiondao.getUpiTxnDetails(jsonRequest.getTid(), jsonRequest.getRrn());
			merchantdetail = merchantdao.getMerchantDetails(jsonRequest.getRrn(), jsonRequest.getTid());
		}catch(DaoException de){
			logger.error("Exception in checkStatus",de.getMessage());
			throw new ApplicationException(ErrorMessages.ORG_TXN_NOT_FOUND.toString());
		}
		
		logger.debug("UPITransactionDTO :",txndetail);
		if(txndetail == null)
			throw new ApplicationException(ErrorMessages.ORG_TXN_NOT_FOUND.toString());
		
		logger.debug("merchant_category_code :",merchantdetail);
		if(merchantdetail == null)
			throw new ApplicationException(ErrorMessages.ORG_TXN_NOT_FOUND.toString());
		
		java.util.Date d =new java.util.Date();
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMYYYYHHmmss");
		
		IndusIndRefundDTO reftxn = new IndusIndRefundDTO();
		reftxn.setReqType("UPIREFUND");
		reftxn.setRrn("");//Pending for review
		reftxn.setReqDateTime(sdf.format(d));
		reftxn.setBankCode(bankCode);
		reftxn.setMid(txndetail.getMerchantId());
		reftxn.setTid(txndetail.getTid());
		reftxn.setCustVpa(txndetail.getCustomerVpa());
		reftxn.setMerchantVpa(txndetail.getMerchantVpa());
		reftxn.setRefAmount(jsonRequest.getRefundAmount());
		reftxn.setOrgRRN(jsonRequest.getRrn());
		reftxn.setRefundRRN(newRefundRRN);
		reftxn.setRefundDateTime(jsonRequest.getTid());
		
		String jsonObject = JsonUtility.convertToJson(reftxn,true);
		logger.error("Refund Json Request "+jsonObject);
		
		Map<String,String> request = new LinkedHashMap<>();
		request.put("data", jsonObject);
		
		HashMap<String, String> headers = new HashMap<>(1);
		headers.put("Content-Type", "application/json");
		
		String respRecieved = HttpsClient.send(BankConfig.get(bankCode, "upi_refund_url"), jsonObject, headers);
		logger.info("Response recieved from psp:"+respRecieved);
		
		UpiRefundResponse resp = (UpiRefundResponse) JsonUtility.parseJson(respRecieved, UpiRefundResponse.class);
		logger.debug("Response : "+resp);
		
		if(resp!=null && "000".equals(resp.getCode()))
		{
			HashMap<String,String> map = new HashMap<String,String>(1);
			map.put("rrn", reftxn.getOrgRRN());
			Response respObj = new Response(Constants.SUCCESS.name() , resp.getResult(), map);
			logger.info("Response from mobile server : " + respObj);
			return respObj;
		}else{
			Response respObj = new Response(Constants.FAILED.name() , resp.getResult(), null);
			logger.info("Response from mobile server : " + respObj);
			return respObj;
		}
		//return respObj;
	}

	@Override
	public Response upiRefund(String fromEntity, String bankCode, String jsonRequest) {
		
		return null;
	}

	@Override
	public Response doRefundJob(String string, String bankCode) {
		
		return null;
	}

	@Override
	public Response upiRefundRecon(String fromEntity, String bankCode, RefundRequest jsonRequest, TxnDTO txnDTO) {
		
		return null;
	}

	
	
	
}
