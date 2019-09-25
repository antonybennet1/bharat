package com.wl.upi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AxisUserTxnModel {

	private String bankCode;
	@JsonIgnore
	private String merchId;
	@JsonIgnore
	private String merchChanId;
	@JsonIgnore
	private String unqTxnId;
	private String unqCustId;
	private String amount;
	private String txnDtl;
	@JsonIgnore
	private String currency;
	private String orderId;
	private String customerVpa;
	@JsonIgnore
	private String expiry;
	private String sId;
	@JsonIgnore
	private String checkSum;
	
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getMerchId() {
		return merchId;
	}
	public void setMerchId(String merchId) {
		this.merchId = merchId;
	}
	public String getMerchChanId() {
		return merchChanId;
	}
	public void setMerchChanId(String merchChanId) {
		this.merchChanId = merchChanId;
	}
	public String getUnqTxnId() {
		return unqTxnId;
	}
	public void setUnqTxnId(String unqTxnId) {
		this.unqTxnId = unqTxnId;
	}
	public String getUnqCustId() {
		return unqCustId;
	}
	public void setUnqCustId(String unqCustId) {
		this.unqCustId = unqCustId;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getTxnDtl() {
		return txnDtl;
	}
	public void setTxnDtl(String txnDtl) {
		this.txnDtl = txnDtl;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getCustomerVpa() {
		return customerVpa;
	}
	public void setCustomerVpa(String customerVpa) {
		this.customerVpa = customerVpa;
	}
	public String getExpiry() {
		return expiry;
	}
	public void setExpiry(String expiry) {
		this.expiry = expiry;
	}
	public String getsId() {
		return sId;
	}
	public void setsId(String sId) {
		this.sId = sId;
	}
	public String getCheckSum() {
		return checkSum;
	}
	public void setCheckSum(String checkSum) {
		this.checkSum = checkSum;
	}
	
	
}
