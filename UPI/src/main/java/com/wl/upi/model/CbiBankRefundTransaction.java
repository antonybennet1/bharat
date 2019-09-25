package com.wl.upi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CbiBankRefundTransaction {

	@JsonIgnore
	private String bankCode;
	private String entityId ;//DEFAULT AS PER DOCUMEN T
	@JsonProperty(value="merchantId",required=true)
	private String merchantId;
	@JsonProperty(value="txnRefundId",required=true)
	private String txnRefundId;
	@JsonProperty(value="mobNo",required=false,defaultValue="")
	private String mobileNo;
	@JsonProperty(value="rrn",required=true)
	private String rrn;
	@JsonProperty(value="txnRefundAmount",required=true)
	private String txnRefundAmount;
	@JsonProperty(value="originalTxnId",required=true)
	private String originalTxnId;
	@JsonProperty(value="refundReason",required=false,defaultValue="")
	private String refundReason;
	private String payerAddr;
	private String payeeAddr;
	private String txnType;
	private String orgTxnType;
	private String channelId;
	private String aggregatorCode;
	@JsonProperty(value="mCC",required=true)
	private String mcc;
	private String mPin;
	@JsonProperty(value="checksum",required=true)
	private String checksum;
	
	
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getTxnRefundId() {
		return txnRefundId;
	}
	public void setTxnRefundId(String txnRefundId) {
		this.txnRefundId = txnRefundId;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getRrn() {
		return rrn;
	}
	public void setRrn(String rrn) {
		this.rrn = rrn;
	}
	public String getTxnRefundAmount() {
		return txnRefundAmount;
	}
	public void setTxnRefundAmount(String txnRefundAmount) {
		this.txnRefundAmount = txnRefundAmount;
	}
	public String getOriginalTxnId() {
		return originalTxnId;
	}
	public void setOriginalTxnId(String originalTxnId) {
		this.originalTxnId = originalTxnId;
	}
	public String getRefundReason() {
		return refundReason;
	}
	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}
	public String getPayerAddr() {
		return payerAddr;
	}
	public void setPayerAddr(String payerAddr) {
		this.payerAddr = payerAddr;
	}
	public String getPayeeAddr() {
		return payeeAddr;
	}
	public void setPayeeAddr(String payeeAddr) {
		this.payeeAddr = payeeAddr;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getOrgTxnType() {
		return orgTxnType;
	}
	public void setOrgTxnType(String orgTxnType) {
		this.orgTxnType = orgTxnType;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getAggregatorCode() {
		return aggregatorCode;
	}
	public void setAggregatorCode(String aggregatorCode) {
		this.aggregatorCode = aggregatorCode;
	}
	public String getMcc() {
		return mcc;
	}
	public void setMcc(String string) {
		this.mcc = string;
	}
	public String getmPin() {
		return mPin;
	}
	public void setmPin(String mPin) {
		this.mPin = mPin;
	}
	public String getChecksum() {
		return checksum;
	}
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}
	
}
