package com.wl.instamer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MerchantOnboardResponse {

	@JsonProperty(value="RESPCODE")
	public String responseCode;
	@JsonProperty(value="BANK")
	public String bankCode;
	@JsonProperty(value="REQUESTID")
	public String requestId; 
	@JsonProperty(value="DATA")
	public String data;
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	
}
