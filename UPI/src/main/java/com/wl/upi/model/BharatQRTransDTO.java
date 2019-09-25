package com.wl.upi.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BharatQRTransDTO implements Serializable{

	/**
	 * 
	 */
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
	@JsonProperty(value="refundId")
	private String refundId;
	
	@JsonProperty("additional_data")
	private String additionalData;
	@JsonProperty("new_rrn")
	private String newRRN;
	@JsonIgnore
	private String merchantId;
	@JsonIgnore
	private String tid;
	@JsonIgnore
	private double refundedAmount;
	@JsonIgnore
	private int id;
	
	
	public String getNewRRN() {
		return newRRN;
	}

	public void setNewRRN(String newRRN) {
		this.newRRN = newRRN;
	}
	
	/**
	 * @return the refundId
	 */
	public String getRefundId() {
		return refundId;
	}
	/**
	 * @param mti the refundId to set
	 */
	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}
	
	/**
	 * @return the mti
	 */
	public String getMti() {
		return mti;
	}
	/**
	 * @param mti the mti to set
	 */
	public void setMti(String mti) {
		this.mti = mti;
	}
	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return the functionName
	 */
	public String getFunctionName() {
		return functionName;
	}
	/**
	 * @param functionName the functionName to set
	 */
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	/**
	 * @return the requestType
	 */
	public String getRequestType() {
		return requestType;
	}
	/**
	 * @param requestType the requestType to set
	 */
	public void setRequestType(String requestType) {
		this.requestType = requestType;
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
	 * @return the custPan
	 */
	public String getCustPan() {
		return custPan;
	}
	/**
	 * @param custPan the custPan to set
	 */
	public void setCustPan(String custPan) {
		this.custPan = custPan;
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
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}
	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	/**
	 * @return the amount
	 */
	public String getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		this.amount = amount;
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
	 * @return the primaryid
	 */
	public String getPrimaryId() {
		return primaryId;
	}
	/**
	 * @param primaryid the primaryid to set
	 */
	public void setPrimaryId(String primaryid) {
		this.primaryId = primaryid;
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
	 * @return the settlementAmt
	 */
	public String getSettlementAmt() {
		return settlementAmt;
	}
	/**
	 * @param settlementAmt the settlementAmt to set
	 */
	public void setSettlementAmt(String settlementAmt) {
		this.settlementAmt = settlementAmt;
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
	 * @return the transactinType
	 */
	public String getTransactionType() {
		return transactionType;
	}
	/**
	 * @param transactinType the transactinType to set
	 */
	public void setTransactionType(String transactinType) {
		this.transactionType = transactinType;
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
	 * @return the responseType
	 */
	public String getResponseType() {
		return responseType;
	}
	/**
	 * @param responseType the responseType to set
	 */
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}
	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the respRefNo
	 */
	public String getRespRefNo() {
		return respRefNo;
	}
	/**
	 * @param respRefNo the respRefNo to set
	 */
	public void setRespRefNo(String respRefNo) {
		this.respRefNo = respRefNo;
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
	 * @return the refundedAmount
	 */
	public double getRefundedAmount() {
		return refundedAmount;
	}
	/**
	 * @param refundedAmount the refundedAmount to set
	 */
	public void setRefundedAmount(double refundedAmount) {
		this.refundedAmount = refundedAmount;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the additionalData
	 */
	public String getAdditionalData() {
		return additionalData;
	}
	/**
	 * @param additionalData the additionalData to set
	 */
	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
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
	
	
	
}
