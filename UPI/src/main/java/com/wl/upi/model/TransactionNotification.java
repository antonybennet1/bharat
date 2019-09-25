package com.wl.upi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TransactionNotification {

	@JsonIgnore
	private String fromEntity;
	private String merchId;
	private String merchVpa;
	private String qrCodeType;
	private String unqTxnId;
	private String trId;
	private String txnRefNo;
	private String txnDate;
	private String txnCurrency;
	private String amount;
	private String bankCode;
	private String customerVpa;
	private String customerName;
	private String status;
	private String message;
//	private String tid;
	
	public String getMerchId() {
		return merchId;
	}
	public void setMerchId(String merchId) {
		this.merchId = merchId;
	}
	public String getMerchVpa() {
		return merchVpa;
	}
	public void setMerchVpa(String merchVpa) {
		this.merchVpa = merchVpa;
	}
	public String getQrCodeType() {
		return qrCodeType;
	}
	public void setQrCodeType(String qrCodeType) {
		this.qrCodeType = qrCodeType;
	}
	public String getUnqTxnId() {
		return unqTxnId;
	}
	public void setUnqTxnId(String unqTxnId) {
		this.unqTxnId = unqTxnId;
	}
	public String getTrId() {
		return trId;
	}
	public void setTrId(String trId) {
		this.trId = trId;
	}
	public String getTxnRefNo() {
		return txnRefNo;
	}
	public void setTxnRefNo(String txnRefNo) {
		this.txnRefNo = txnRefNo;
	}
	public String getTxnDate() {
		return txnDate;
	}
	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}
	public String getTxnCurrency() {
		return txnCurrency;
	}
	public void setTxnCurrency(String txnCurrency) {
		this.txnCurrency = txnCurrency;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getCustomerVpa() {
		return customerVpa;
	}
	public String getFromEntity() {
		return fromEntity;
	}
	public void setFromEntity(String fromEntity) {
		this.fromEntity = fromEntity;
	}
	public void setCustomerVpa(String customerVpa) {
		this.customerVpa = customerVpa;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return "TransactionNotification [fromEntity=" + fromEntity + ", merchId=" + merchId + ", merchVpa=" + merchVpa
				+ ", qrCodeType=" + qrCodeType + ", unqTxnId=" + unqTxnId + ", trId=" + trId + ", txnRefNo=" + txnRefNo
				+ ", txnDate=" + txnDate + ", txnCurrency=" + txnCurrency + ", amount=" + amount + ", bankCode="
				+ bankCode + ", customerVpa=" + customerVpa + ", customerName=" + customerName + ", status=" + status
				+ ", message=" + message + "]";
	}
	
}
