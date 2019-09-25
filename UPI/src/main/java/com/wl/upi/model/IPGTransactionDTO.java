package com.wl.upi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IPGTransactionDTO {
	
	private String mid;
	private String mpan;
	@JsonProperty("customer_name")
	private String custName;
	@JsonProperty("txn_currency")
	private String txnCurr;
	@JsonProperty("txn_amount")
	private String txnAmt;
	@JsonProperty("auth_code")
	private String authCode;
	@JsonProperty("ref_no")
	private String refNo;
	@JsonProperty("primary_id")
	private String primaryId;
	@JsonProperty("secondary_id")
	private String secondaryId; 
	@JsonProperty("settlement_amount")
	private String settleAmt;
	@JsonProperty("time_stamp")
	private String timeStamp;
	@JsonProperty("transaction_type")
	private String transType;
	@JsonProperty("bank_code")
	private String bankCode;
	@JsonProperty("aggregator_id")
	private String aggrId;
	@JsonProperty("consumer_pan")
	private String consumerPan;
	@JsonProperty("customer_vpa")
	private String customerVpa;
	@JsonProperty("merchant_vpa")
	private String merchantVpa;
	@JsonProperty("response_code")
	private String responseCode;
	@JsonProperty("tid")
	private String tid;
	
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getCustomerVpa() {
		return customerVpa;
	}
	public void setCustomerVpa(String customerVpa) {
		this.customerVpa = customerVpa;
	}
	public String getMerchantVpa() {
		return merchantVpa;
	}
	public void setMerchantVpa(String merchantVpa) {
		this.merchantVpa = merchantVpa;
	}
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
	 * @return the mpan
	 */
	public String getMpan() {
		return mpan;
	}
	/**
	 * @param mpan the mpan to set
	 */
	public void setMpan(String mpan) {
		this.mpan = mpan;
	}
	/**
	 * @return the custName
	 */
	public String getCustName() {
		return custName;
	}
	/**
	 * @param custName the custName to set
	 */
	public void setCustName(String custName) {
		this.custName = custName;
	}
	/**
	 * @return the txnCurr
	 */
	public String getTxnCurr() {
		return txnCurr;
	}
	/**
	 * @param txnCurr the txnCurr to set
	 */
	public void setTxnCurr(String txnCurr) {
		this.txnCurr = txnCurr;
	}
	/**
	 * @return the txnAmt
	 */
	public String getTxnAmt() {
		return txnAmt;
	}
	/**
	 * @param txnAmt the txnAmt to set
	 */
	public void setTxnAmt(String txnAmt) {
		this.txnAmt = txnAmt;
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
	 * @return the refNo
	 */
	public String getRefNo() {
		return refNo;
	}
	/**
	 * @param refNo the refNo to set
	 */
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	/**
	 * @return the primaryId
	 */
	public String getPrimaryId() {
		return primaryId;
	}
	/**
	 * @param primaryId the primaryId to set
	 */
	public void setPrimaryId(String primaryId) {
		this.primaryId = primaryId;
	}
	/**
	 * @return the secondaryId
	 */
	public String getSecondaryId() {
		return secondaryId;
	}
	/**
	 * @param secondaryId the secondaryId to set
	 */
	public void setSecondaryId(String secondaryId) {
		this.secondaryId = secondaryId;
	}
	/**
	 * @return the settleAmt
	 */
	public String getSettleAmt() {
		return settleAmt;
	}
	/**
	 * @param settleAmt the settleAmt to set
	 */
	public void setSettleAmt(String settleAmt) {
		this.settleAmt = settleAmt;
	}
	/**
	 * @return the timeStamp
	 */
	public String getTimeStamp() {
		return timeStamp;
	}
	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	/**
	 * @return the transType
	 */
	public String getTransType() {
		return transType;
	}
	/**
	 * @param transType the transType to set
	 */
	public void setTransType(String transType) {
		this.transType = transType;
	}
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
	 * @return the aggrId
	 */
	public String getAggrId() {
		return aggrId;
	}
	/**
	 * @param aggrId the aggrId to set
	 */
	public void setAggrId(String aggrId) {
		this.aggrId = aggrId;
	}
	/**
	 * @return the consumerPan
	 */
	public String getConsumerPan() {
		return consumerPan;
	}
	/**
	 * @param consumerPan the consumerPan to set
	 */
	public void setConsumerPan(String consumerPan) {
		this.consumerPan = consumerPan;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	

	public String getResponseCode() {
		return responseCode;
	}
	@Override
	public String toString() {
		return "IPGTransactionDTO [mid=" + mid + ", mpan=" + mpan + ", custName=" + custName + ", txnCurr=" + txnCurr
				+ ", txnAmt=" + txnAmt + ", authCode=" + authCode + ", refNo=" + refNo + ", primaryId=" + primaryId
				+ ", secondaryId=" + secondaryId + ", settleAmt=" + settleAmt + ", timeStamp=" + timeStamp
				+ ", transType=" + transType + ", bankCode=" + bankCode + ", aggrId=" + aggrId + ", consumerPan="
				+ consumerPan + ", customerVpa=" + customerVpa + ", merchantVpa=" + merchantVpa + ", responseCode="
				+ responseCode + ", tid=" + tid + "]";
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	
}
