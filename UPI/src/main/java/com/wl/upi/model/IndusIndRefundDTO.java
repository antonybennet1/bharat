package com.wl.upi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IndusIndRefundDTO {

	
	@JsonProperty(value="REQTYPE")
	private String reqType;
	
	@JsonProperty(value="RRN")
	private String rrn;
	
	@JsonProperty(value="REQUESTDTTM")
	private String reqDateTime;
	
	@JsonProperty(value="BANKID")
	private String bankCode;
	
	@JsonProperty(value="MID")
	private String mid;
	
	@JsonProperty(value="TID")
	private String tid;
	
	@JsonProperty(value="CUSTVPA")
	private String custVpa;
	
	@JsonProperty(value="MEVPA")
	private String merchantVpa;
	
	@JsonProperty(value="REFAMT")
	private String refAmount;
	
	@JsonProperty(value="ORGRRN")
	private String orgRRN;
	
	@JsonProperty(value="REFRRN")
	private String refundRRN;
	
	@JsonProperty(value="REFTXNDT")
	private String refundDateTime;

	public String getReqType() {
		return reqType;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	public String getRrn() {
		return rrn;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

	public String getReqDateTime() {
		return reqDateTime;
	}

	public void setReqDateTime(String reqDateTime) {
		this.reqDateTime = reqDateTime;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getCustVpa() {
		return custVpa;
	}

	public void setCustVpa(String custVpa) {
		this.custVpa = custVpa;
	}

	public String getMerchantVpa() {
		return merchantVpa;
	}

	public void setMerchantVpa(String merchantVpa) {
		this.merchantVpa = merchantVpa;
	}

	public String getRefAmount() {
		return refAmount;
	}

	public void setRefAmount(String refAmount) {
		this.refAmount = refAmount;
	}

	public String getOrgRRN() {
		return orgRRN;
	}

	public void setOrgRRN(String orgRRN) {
		this.orgRRN = orgRRN;
	}

	public String getRefundRRN() {
		return refundRRN;
	}

	public void setRefundRRN(String refundRRN) {
		this.refundRRN = refundRRN;
	}

	public String getRefundDateTime() {
		return refundDateTime;
	}

	public void setRefundDateTime(String refundDateTime) {
		this.refundDateTime = refundDateTime;
	}
	
	
	
	
}
