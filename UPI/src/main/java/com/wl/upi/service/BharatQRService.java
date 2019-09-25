package com.wl.upi.service;

import java.util.Map;

import com.wl.upi.model.MMSTransDTO;
import com.wl.upi.model.MrlDTO;
import com.wl.upi.model.RefundRequest;
import com.wl.upi.model.TtmsTransactiosDto;
import com.wl.util.model.Response;

public interface BharatQRService {
	
	public Response refund(String fromEntity, String bankCode, String jsonRequest);
	
	public Response bhartQRRefund(String fromEntity, String bankCode, RefundRequest request);
	
	public Map<String,String> populateBharatQRTxn(String jsonRequest);
	
	public void notifyForBharatQRTxn(Map<String,String> resp);
	
	public void notifyForUpiTxn(Map<String,String> resp);
	
	public Map<String,String> populateTTMS(String jsonRequest);
	
	public void notifyForMMSTxn(Map<String,String> resp);

	public Map<String,String> populateTTMSRequestBOdy(MMSTransDTO ttmsDto);
	
	public Response getCount(String bank_code,String date);
}
