package com.wl.upi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RefundRequest {
	

	@JsonProperty(value="TID",required=true)
	private String tid;
	@JsonIgnore
	private String bankCode;
	@JsonIgnore
	private String fromEntity;
	@JsonProperty(value="txnType",required=true)
	private String txnType;
	@JsonProperty(value="refundAmount",required=true)
	private String refundAmount;
	private String refundReason;
	private String mobileNumber;
	@JsonProperty(value="rrn",required=false)
	private String rrn;
	@JsonProperty(value="authCode",required=false)
	private String authCode;
	@JsonProperty(value="refundId",required=false)
	private String refundId;
	
	
	public String getRefundId() {
		return refundId;
	}
	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}
	/**
	 * @return the tid
	 */
	public String getTid() {
		return tid;
	}
	/**
	 * @param tid the tid to set
	 */
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getFromEntity() {
		return fromEntity;
	}
	public void setFromEntity(String fromEntity) {
		this.fromEntity = fromEntity;
	}
	/**
	 * @return the refundAmount
	 */
	public String getRefundAmount() {
		return refundAmount;
	}
	/**
	 * @param refundAmount the refundAmount to set
	 */
	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}
	
	/**
	 * @return the refundReason
	 */
	public String getRefundReason() {
		return refundReason;
	}
	/**
	 * @param refundReason the refundReason to set
	 */
	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}
	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}
	/**
	 * @param mobileNumber the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	/**
	 * @return the rrn
	 */
	public String getRrn() {
		return rrn;
	}
	/**
	 * @param rrn the rrn to set
	 */
	public void setRrn(String rrn) {
		this.rrn = rrn;
	}
	
	/**
	 * @return the authCode
	 */
	public String getAuthCode() {
		return authCode;
	}
	/**
	 * @param authCode the authCode to set
	 */
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	
	/**
	 * @return the txnType
	 */
	public String getTxnType() {
		return txnType;
	}
	/**
	 * @param txnType the txnType to set
	 */
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RefundRequest [tid=" + tid + ", bankCode=" + bankCode + ", fromEntity=" + fromEntity + ", txnType="
				+ txnType + ", refundAmount=" + refundAmount + ", refundReason=" + refundReason + ", mobileNumber="
				+ mobileNumber + ", rrn=" + rrn + ", authCode=" + authCode +  ", refundId="+ refundId + "]";
	}
		
}
