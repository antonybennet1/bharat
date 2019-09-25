package com.wl.upi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TCHRequest {
	
	@JsonProperty("MID")
	private String mid;
	@JsonProperty("TID")
	private String tid;
	private String bankCode;
	@JsonIgnore
	private String fromEntity;
	private String txnId;
	@JsonProperty("prgType")
	private String programType;
	@JsonProperty("refundAmount")
	private String refundAmount;
	private String refundReason;
	private String mobileNumber="919999999999";
	
	/**
	 * @return the mid
	 */
	public String getMid() {
		return mid;
	}
	/**
	 * @param mid the mid to set
	 */
	public void setMid(String mid) {
		this.mid = mid;
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
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getProgramType() {
		return programType;
	}
	public void setProgramType(String programType) {
		this.programType = programType;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TCHRequest [mid=" + mid + ", tid=" + tid + ", bankCode=" + bankCode + ", fromEntity=" + fromEntity
				+ ", txnId=" + txnId + ", programType=" + programType + ", refundAmount=" + refundAmount + "]";
	}
	
	
	
	
}
