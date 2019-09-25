package com.wl.upi.model;
/**
 * ritesh.patil
 */
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class KotakUPITransactionDTO implements Serializable{

	/**
	 * 
	 * ritesh.patil
	 */
	
	private static final long serialVersionUID = 1L;
	
	
	@JsonProperty("merchantcode")
	private String merchId;
	@JsonProperty("payeevpa")
	private String merchantVpa;
	@JsonProperty("payervpa")
	private String customerVpa; 
	@JsonProperty("transactionid")
	private String merchTransId;
	@JsonProperty("transactionTimestamp")
	private String transTimestamp;
	private String amount;
	@JsonProperty("refid")
	private String gatewayTransId;
	private String rrn;
    private String status;
    private String statusCode;
    private String aggregatorcode;
    private String type;
    private String description;
    private String remarks;
    private String transactionreferencenumber;
    private String refurl;
    
	@JsonIgnore
	private String bankCode;       // field is missing in input  
	@JsonIgnore
	private String fromEntity;
	@JsonIgnore
	private String txnId;
	@JsonIgnore
	private String qrCodeType;
	@JsonIgnore
	private String terminalId;
	private String checksum;

    /**
	 * @return the merchId
	 */
	public String getMerchId() {
		return merchId;
	}
	/**
	 * @param merchId the merchId to set
	 */
	public void setMerchId(String merchId) {
		this.merchId = merchId;
	}
	/**
	 * @return the merchantVpa
	 */
	public String getMerchantVpa() {
		return merchantVpa;
	}
	/**
	 * @param merchantVpa the merchantVpa to set
	 */
	public void setMerchantVpa(String merchantVpa) {
		this.merchantVpa = merchantVpa;
	}
	/**
	 * @return the customerVpa
	 */
	public String getCustomerVpa() {
		return customerVpa;
	}
	/**
	 * @param customerVpa the customerVpa to set
	 */
	public void setCustomerVpa(String customerVpa) {
		this.customerVpa = customerVpa;
	}
	/**
	 * @return the merchTransId
	 */
	public String getMerchTransId() {
		return merchTransId;
	}
	/**
	 * @param merchTransId the merchTransId to set
	 */
	public void setMerchTransId(String merchTransId) {
		this.merchTransId = merchTransId;
	}
	/**
	 * @return the transTimestamp
	 */
	public String getTransTimestamp() {
		return transTimestamp;
	}
	/**
	 * @param transTimestamp the transTimestamp to set
	 */
	public void setTransTimestamp(String transTimestamp) {
		this.transTimestamp = transTimestamp;
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
	 * @return the gatewayTransId
	 */
	public String getGatewayTransId() {
		return gatewayTransId;
	}
	/**
	 * @param gatewayTransId the gatewayTransId to set
	 */
	public void setGatewayTransId(String gatewayTransId) {
		this.gatewayTransId = gatewayTransId;
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
	 * @return the fromEntity
	 */
	public String getFromEntity() {
		return fromEntity;
	}
	/**
	 * @param fromEntity the fromEntity to set
	 */
	public void setFromEntity(String fromEntity) {
		this.fromEntity = fromEntity;
	}
	/**
	 * @return the txnId
	 */
	public String getTxnId() {
		return txnId;
	}
	/**
	 * @param txnId the txnId to set
	 */
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	/**
	 * @return the qrCodeType
	 */
	public String getQrCodeType() {
		return qrCodeType;
	}
	/**
	 * @param qrCodeType the qrCodeType to set
	 */
	public void setQrCodeType(String qrCodeType) {
		this.qrCodeType = qrCodeType;
	}
	/**
	 * @return the terminalId
	 */
	public String getTerminalId() {
		return terminalId;
	}
	/**
	 * @param terminalId the terminalId to set
	 */
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
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
	 * @return the statusCode
	 */
	public String getStatusCode() {
		return statusCode;
	}
	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	/**
	 * @return the aggregatorcode
	 */
	public String getAggregatorcode() {
		return aggregatorcode;
	}
	/**
	 * @param aggregatorcode the aggregatorcode to set
	 */
	public void setAggregatorcode(String aggregatorcode) {
		this.aggregatorcode = aggregatorcode;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}
	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * @return the transactionreferencenumber
	 */
	public String getTransactionreferencenumber() {
		return transactionreferencenumber;
	}
	/**
	 * @param transactionreferencenumber the transactionreferencenumber to set
	 */
	public void setTransactionreferencenumber(String transactionreferencenumber) {
		this.transactionreferencenumber = transactionreferencenumber;
	}
	/**
	 * @return the refurl
	 */
	public String getRefurl() {
		return refurl;
	}
	/**
	 * @param refurl the refurl to set
	 */
	public void setRefurl(String refurl) {
		this.refurl = refurl;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "KotakUPITransactionDTO [merchId=" + merchId + ", merchantVpa=" + merchantVpa + ", customerVpa="
				+ customerVpa + ", merchTransId=" + merchTransId + ", transTimestamp=" + transTimestamp + ", amount="
				+ amount + ", gatewayTransId=" + gatewayTransId + ", rrn=" + rrn + ", status=" + status
				+ ", statusCode=" + statusCode + ", aggregatorcode=" + aggregatorcode + ", type=" + type
				+ ", description=" + description + ", remarks=" + remarks + ", transactionreferencenumber="
				+ transactionreferencenumber + ", refurl=" + refurl + ", bankCode=" + bankCode + ", fromEntity="
				+ fromEntity + ", txnId=" + txnId + ", qrCodeType=" + qrCodeType + ", terminalId=" + terminalId + "]";
	}
	
	/**
	 * @return the checksum
	 */
	public String getChecksum() {
		return checksum;
	}
	/**
	 * @param checksum the checksum to set
	 */
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}
	
	
	
   	

	
	
	
	
	
	

}
