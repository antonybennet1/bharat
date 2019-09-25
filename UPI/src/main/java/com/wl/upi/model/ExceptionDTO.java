package com.wl.upi.model;

public class ExceptionDTO {
	private String formEntity;
	private String rrn;
	private String json;
	private String messsage;
	private String exception;
	private String txnType;
	
	public String getFormEntity() {
		return formEntity;
	}
	public void setFormEntity(String formEntity) {
		this.formEntity = formEntity;
	}
	public String getRrn() {
		return rrn;
	}
	public void setRrn(String rrn) {
		this.rrn = rrn;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public String getMesssage() {
		return messsage;
	}
	public void setMesssage(String messsage) {
		this.messsage = messsage;
	}
	public String getException() {
		return exception;
	}
	public void setException(String exception) {
		this.exception = exception;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
}
