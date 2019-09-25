package com.wl.instamer.service;

import com.wl.instamer.model.Response;


public interface MerchantOnboardService {
	public Response processRequest(String jsonRequest);
}
