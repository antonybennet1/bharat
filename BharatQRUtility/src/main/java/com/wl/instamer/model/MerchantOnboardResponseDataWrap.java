package com.wl.instamer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MerchantOnboardResponseDataWrap {

	@JsonProperty(value="Error")
	public Object error;
	@JsonProperty(value="Success")
	public MerchantOnboardResponseData success;
	/**
	 * @return the error
	 */
	public Object getError() {
		return error;
	}
	/**
	 * @param error the error to set
	 */
	public void setError(Object error) {
		this.error = error;
	}
	/**
	 * @return the success
	 */
	public MerchantOnboardResponseData getSuccess() {
		return success;
	}
	/**
	 * @param success the success to set
	 */
	public void setSuccess(MerchantOnboardResponseData success) {
		this.success = success;
	}
	
	
	
}
