package com.wl.upi.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * @author ritesh.patil
 *
 */
public class MultiBankUPITransaction implements Serializable {

	/**
	 * 
	 * DTO for all bank which will be use common PSP 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String merchId="";
	private String merchantVpa;
	private String customerVpa; 
	@JsonProperty("merchantTransactionId")
	private String merchTransId;
	@JsonProperty("transactionTimestamp")
	private String transTimestamp;
	private String transactionAmount;
	@JsonProperty("gatewayTransactionId")
	private String gatewayTransId;
	@JsonProperty("gatewayResponseCode")
	private String gatewayResCode;
	@JsonProperty("gatewayResponseMessage")
	private String gatewayReseMsg;
	private String rrn;
	private String checksum;
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
	 * @return the transactionAmount
	 */
	public String getTransactionAmount() {
		return transactionAmount;
	}
	/**
	 * @param transactionAmount the transactionAmount to set
	 */
	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
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
	 * @return the gatewayResCode
	 */
	public String getGatewayResCode() {
		return gatewayResCode;
	}
	/**
	 * @param gatewayResCode the gatewayResCode to set
	 */
	public void setGatewayResCode(String gatewayResCode) {
		this.gatewayResCode = gatewayResCode;
	}
	/**
	 * @return the gatewayReseMsg
	 */
	public String getGatewayReseMsg() {
		return gatewayReseMsg;
	}
	/**
	 * @param gatewayReseMsg the gatewayReseMsg to set
	 */
	public void setGatewayReseMsg(String gatewayReseMsg) {
		this.gatewayReseMsg = gatewayReseMsg;
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
	@Override
	public String toString() {
		return "MultiBankUPITransaction [merchId=" + merchId + ", merchantVpa=" + merchantVpa + ", customerVpa="
				+ customerVpa + ", merchTransId=" + merchTransId + ", transTimestamp=" + transTimestamp
				+ ", transactionAmount=" + transactionAmount + ", gatewayTransId=" + gatewayTransId
				+ ", gatewayResCode=" + gatewayResCode + ", gatewayReseMsg=" + gatewayReseMsg + ", rrn=" + rrn + "]";
	}	

}
