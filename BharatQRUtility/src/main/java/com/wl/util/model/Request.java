package com.wl.util.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Request {
	private String fromEntity;
	@JsonProperty(required=false)
	private String bankCode;
	private Object data;
	public String getFromEntity() {
		return fromEntity;
	}
	public void setFromEntity(String fromEntity) {
		this.fromEntity = fromEntity;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
}
