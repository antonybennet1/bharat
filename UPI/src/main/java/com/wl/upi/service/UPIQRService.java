package com.wl.upi.service;

import com.wl.util.model.Response;

public interface UPIQRService {

	public Response checkStatus(String fromEntity,  String bankCode, String data);

	public Response cancelQR(String fromEntity,  String bankCode,  String data);
	
	public Response checkAggregatorTransStatus(String fromEntity,  String bankCode, String data);
	
	
}
