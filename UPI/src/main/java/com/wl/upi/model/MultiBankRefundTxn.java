package com.wl.upi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MultiBankRefundTxn {

	@JsonIgnore
	private String bankCode;
	@JsonProperty(value="merchId",required=true)
	private String merchantId;
	@JsonProperty(value="unqTxnId",required=true)
	private String orginalTxnId;
	@JsonProperty(value="mobNo",required=false,defaultValue="")
	private String mobileNumber;
	@JsonProperty(value="txnRefundAmount",required=true)
	private String refundAmount;
	@JsonProperty(value="txnRefundId",required=true)
	private String txnRefundId;
	@JsonProperty(value="rrn",required=true)
	private String rrn;
	@JsonProperty(value="refundReason",required=false,defaultValue="")
	private String refundReason;
	@JsonProperty(value="checkSum",required=true)
	private String checkSum;
	/**
	 * @return the bankCode
	 */
	public String getBankCode() {
		return bankCode;
	}
	/**
	 * @param bankCode the bankCode to set
	 */
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	/**
	 * @return the merchantId
	 */
	public String getMerchantId() {
		return merchantId;
	}
	/**
	 * @param merchantId the merchantId to set
	 */
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	/**
	 * @return the orginalTxnId
	 */
	public String getOrginalTxnId() {
		return orginalTxnId;
	}
	/**
	 * @param orginalTxnId the orginalTxnId to set
	 */
	public void setOrginalTxnId(String orginalTxnId) {
		this.orginalTxnId = orginalTxnId;
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
	 * @return the txnRefundId
	 */
	public String getTxnRefundId() {
		return txnRefundId;
	}
	/**
	 * @param txnRefundId the txnRefundId to set
	 */
	public void setTxnRefundId(String txnRefundId) {
		this.txnRefundId = txnRefundId;
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
	 * @return the checkSum
	 */
	public String getCheckSum() {
		return checkSum;
	}
	/**
	 * @param checkSum the checkSum to set
	 */
	public void setCheckSum(String checkSum) {
		this.checkSum = checkSum;
	}
	
}
