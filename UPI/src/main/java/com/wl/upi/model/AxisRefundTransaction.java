package com.wl.upi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AxisRefundTransaction {

	@JsonIgnore
	private String bankCode;
	private String merchId;
	private String merchChanId;
	@JsonIgnore
	private String unqTxnId;
	private String mobNo;
	private String txnRefundAmount;
	private String txnRefundId;
	private String refundReason;
	private String sId;
	private String checkSum;
	private String gateWayTxnId;
	@JsonIgnore
	private String newRRN;
	
	
	public String getNewRRN() {
		return newRRN;
	}
	public void setNewRRN(String newRRN) {
		this.newRRN = newRRN;
	}
	
	public String getGateWayTxnId() {
		return gateWayTxnId;
	}
	public void setGateWayTxnId(String gateWayTxnId) {
		this.gateWayTxnId = gateWayTxnId;
	}
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
	public String getMobNo() {
		return mobNo;
	}
	public void setMobNo(String mobNo) {
		this.mobNo = mobNo;
	}
	public String getTxnRefundAmount() {
		return txnRefundAmount;
	}
	public void setTxnRefundAmount(String txnRefundAmount) {
		this.txnRefundAmount = txnRefundAmount;
	}
	public String getTxnRefundId() {
		return txnRefundId;
	}
	public void setTxnRefundId(String txnRefundId) {
		this.txnRefundId = txnRefundId;
	}
	public String getRefundReason() {
		return refundReason;
	}
	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
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
