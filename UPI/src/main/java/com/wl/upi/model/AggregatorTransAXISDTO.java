package com.wl.upi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AggregatorTransAXISDTO {
	
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
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getMpan() {
		return mpan;
	}
	public void setMpan(String mpan) {
		this.mpan = mpan;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getTxnCurr() {
		return txnCurr;
	}
	public void setTxnCurr(String txnCurr) {
		this.txnCurr = txnCurr;
	}
	public String getTxnAmt() {
		return txnAmt;
	}
	public void setTxnAmt(String txnAmt) {
		this.txnAmt = txnAmt;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getRefNo() {
		return refNo;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	public String getPrimaryId() {
		return primaryId;
	}
	public void setPrimaryId(String primaryId) {
		this.primaryId = primaryId;
	}
	public String getSecondaryId() {
		return secondaryId;
	}
	public void setSecondaryId(String secondaryId) {
		this.secondaryId = secondaryId;
	}
	public String getSettleAmt() {
		return settleAmt;
	}
	public void setSettleAmt(String settleAmt) {
		this.settleAmt = settleAmt;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getAggrId() {
		return aggrId;
	}
	public void setAggrId(String aggrId) {
		this.aggrId = aggrId;
	}
	public String getConsumerPan() {
		return consumerPan;
	}
	public void setConsumerPan(String consumerPan) {
		this.consumerPan = consumerPan;
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
	@Override
	public String toString() {
		return "AggregatorTransAXISDTO [mid=" + mid + ", mpan=" + mpan + ", custName=" + custName + ", txnCurr="
				+ txnCurr + ", txnAmt=" + txnAmt + ", authCode=" + authCode + ", refNo=" + refNo + ", primaryId="
				+ primaryId + ", secondaryId=" + secondaryId + ", settleAmt=" + settleAmt + ", timeStamp=" + timeStamp
				+ ", transType=" + transType + ", bankCode=" + bankCode + ", aggrId=" + aggrId + ", consumerPan="
				+ consumerPan + ", customerVpa=" + customerVpa + ", merchantVpa=" + merchantVpa + "]";
	}
	
	

}
