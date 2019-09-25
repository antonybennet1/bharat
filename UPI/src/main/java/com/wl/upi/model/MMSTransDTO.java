package com.wl.upi.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MMSTransDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	@JsonProperty(value="mti" , required= false)
	private String mti; 
	@JsonProperty(value="class_name" , required= false)
	private String className; 
	@JsonProperty(value="function" , required= false)
	private String functionName; 
	@JsonProperty(value="request_type" , required= false)
	private String requestType;
	@JsonProperty("mpan")
	private String mpan;
	@JsonProperty("customer_pan")
	private String custPan;
	@JsonProperty("customer_name")
	private String custName;
	@JsonProperty("txn_currency")
	private String currency;
	@JsonProperty("txn_amount")
	private String amount;
	@JsonProperty("auth_code")
	private String authCode;
	@JsonProperty("ref_no")
	private String refNo;
	@JsonProperty("primary_id")
	private String primaryId;
	@JsonProperty("secondary_id")
	private String secondaryId;
	@JsonProperty("settlement_amount")
	private String settlementAmt;
	@JsonProperty("time_stamp")
	private String timeStamp;
	@JsonProperty("transaction_type")
	private String transactionType;
	@JsonProperty("bank_code")
	private String bankCode;
	@JsonProperty("response_type")
	private String responseType;
	@JsonProperty(value="error_code" , required= false)
	private String errorCode;
	@JsonProperty(value="status" , required= false)
	private String status;
	@JsonProperty(value="resp_ref_no" , required= false)
	private String respRefNo;
	@JsonProperty(value="refundId", required= false)
	private String refundId;
	
	@JsonProperty("additional_data")
	private String additionalData;
	@JsonProperty(value="new_rrn", required= false)
	private String newRRN;
	@JsonIgnore
	private String merchantId;
	@JsonIgnore
	private String tid;
	@JsonIgnore
	private double refundedAmount;
	@JsonIgnore
	private int id;
	@JsonProperty(value="batch_number" , required=false)
	private String batchNumber;
	@JsonProperty(value="scheme_transaction_id" , required= false)
	private String schemeTransactionId;
	
	
	
	public String getMti() {
		return mti;
	}
	public void setMti(String mti) {
		this.mti = mti;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getMpan() {
		return mpan;
	}
	public void setMpan(String mpan) {
		this.mpan = mpan;
	}
	public String getCustPan() {
		return custPan;
	}
	public void setCustPan(String custPan) {
		this.custPan = custPan;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
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
	public String getSettlementAmt() {
		return settlementAmt;
	}
	public void setSettlementAmt(String settlementAmt) {
		this.settlementAmt = settlementAmt;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getResponseType() {
		return responseType;
	}
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRespRefNo() {
		return respRefNo;
	}
	public void setRespRefNo(String respRefNo) {
		this.respRefNo = respRefNo;
	}
	public String getRefundId() {
		return refundId;
	}
	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}
	public String getAdditionalData() {
		return additionalData;
	}
	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
	}
	public String getNewRRN() {
		return newRRN;
	}
	public void setNewRRN(String newRRN) {
		this.newRRN = newRRN;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public double getRefundedAmount() {
		return refundedAmount;
	}
	public void setRefundedAmount(double refundedAmount) {
		this.refundedAmount = refundedAmount;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	public String getSchemeTransactionId() {
		return schemeTransactionId;
	}
	public void setSchemeTransactionId(String schemeTransactionId) {
		this.schemeTransactionId = schemeTransactionId;
	}

	
	
}
