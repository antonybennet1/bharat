package com.wl.bharatqr.model;

import java.util.Map;

public class MerchantCardTransactionInfo {
	private String merchantId;
	private String payeePan;
	private String payerName;
	private String authCode;
	private String transactionReferenceNumber;
	private String merchant_reference_id;
	private String transaction_timestamp;
	
	private Amount amount;
	
	

	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getPayeePan() {
		return payeePan;
	}
	public void setPayeePan(String payeePan) {
		this.payeePan = payeePan;
	}
	public String getPayerName() {
		return payerName;
	}
	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getTransactionReferenceNumber() {
		return transactionReferenceNumber;
	}
	public void setTransactionReferenceNumber(String transactionReferenceNumber) {
		this.transactionReferenceNumber = transactionReferenceNumber;
	}
	public String getMerchant_reference_id() {
		return merchant_reference_id;
	}
	public void setMerchant_reference_id(String merchant_reference_id) {
		this.merchant_reference_id = merchant_reference_id;
	}
	public String getTransaction_timestamp() {
		return transaction_timestamp;
	}
	public void setTransaction_timestamp(String transaction_timestamp) {
		this.transaction_timestamp = transaction_timestamp;
	}
	public Amount getAmount() {
		return amount;
	}
	public void setAmount(Amount amount) {
		this.amount = amount;
	}
	
	
	
}
