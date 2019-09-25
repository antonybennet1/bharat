package com.wl.upi.model;

public class AxisTransactionStatus {
	
	private String bankCode;
	private String merchId;
	private String merchChanId;
	private String unqTxnId;
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
	public String getCheckSum() {
		return checkSum;
	}
	public void setCheckSum(String checkSum) {
		this.checkSum = checkSum;
	}
	
	

}
