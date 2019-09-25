/**
 * 
 */
package com.wl.upi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author kunal.surana
 *
 */
public class UPITransactionDTO  {

	@JsonProperty("customerVpa")
	private String customerVpa;
	@JsonProperty("merchantId")
	private String pspMerchantId; // wil get from input
	@JsonProperty("merchantChannelId")
	private String merchantChannelId;
	@JsonProperty("merchantTransactionId")
	private String merchantTransactionId;  //as tr_id
	@JsonProperty("transactionTimestamp")
	private String transactionTimestamp;          // as trans date
	@JsonProperty("transactionAmount")
	private String transactionAmount;          // as amount
	@JsonProperty("gatewayTransactionId")
	private String gatewayTransactionId;
	@JsonProperty("gatewayResponseCode")
	private String gatewayResponseCode;   //as status
	@JsonProperty("gatewayResponseMessage")
	private String gatewayResponseMessage;     // as message
	@JsonProperty("checksum")
	private String checksum;
	@JsonIgnore
	private String merchantId;      //  field is missing in input
	@JsonIgnore
	private String merchantVpa;    // field is missing in input 
	private String rrn;                //as reference  txn_ref_no
	@JsonIgnore
	private String txnCurrency;    
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
	@JsonIgnore
	private String authCode;
	@JsonProperty("creditVpa")
	private String creditVpa;

	public String getCreditVpa() {
		return creditVpa;
	}
	public void setCreditVpa(String creditVpa) {
		this.creditVpa = creditVpa;
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
	 * @return the pspMerchantId
	 */
	public String getPspMerchantId() {
		return pspMerchantId;
	}
	/**
	 * @param pspMerchantId the pspMerchantId to set
	 */
	public void setPspMerchantId(String pspMerchantId) {
		this.pspMerchantId = pspMerchantId;
	}
	/**
	 * @return the merchantChannelId
	 */
	public String getMerchantChannelId() {
		return merchantChannelId;
	}
	/**
	 * @param merchantChannelId the merchantChannelId to set
	 */
	public void setMerchantChannelId(String merchantChannelId) {
		this.merchantChannelId = merchantChannelId;
	}
	/**
	 * @return the merchantTransactionId
	 */
	public String getMerchantTransactionId() {
		return merchantTransactionId;
	}
	/**
	 * @param merchantTransactionId the merchantTransactionId to set
	 */
	public void setMerchantTransactionId(String merchantTransactionId) {
		this.merchantTransactionId = merchantTransactionId;
	}
	/**
	 * @return the transactionTimestamp
	 */
	public String getTransactionTimestamp() {
		return transactionTimestamp;
	}
	/**
	 * @param transactionTimestamp the transactionTimestamp to set
	 */
	public void setTransactionTimestamp(String transactionTimestamp) {
		this.transactionTimestamp = transactionTimestamp;
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
	 * @return the gatewayTransactionId
	 */
	public String getGatewayTransactionId() {
		return gatewayTransactionId;
	}
	/**
	 * @param gatewayTransactionId the gatewayTransactionId to set
	 */
	public void setGatewayTransactionId(String gatewayTransactionId) {
		this.gatewayTransactionId = gatewayTransactionId;
	}
	/**
	 * @return the gatewayResponseCode
	 */
	public String getGatewayResponseCode() {
		return gatewayResponseCode;
	}
	/**
	 * @param gatewayResponseCode the gatewayResponseCode to set
	 */
	public void setGatewayResponseCode(String gatewayResponseCode) {
		this.gatewayResponseCode = gatewayResponseCode;
	}
	/**
	 * @return the gatewayResponseMessage
	 */
	public String getGatewayResponseMessage() {
		return gatewayResponseMessage;
	}
	/**
	 * @param gatewayResponseMessage the gatewayResponseMessage to set
	 */
	public void setGatewayResponseMessage(String gatewayResponseMessage) {
		this.gatewayResponseMessage = gatewayResponseMessage;
	}
	/**
	 * @return the merchId
	 */
	public String getMerchantId() {
		return merchantId;
	}
	/**
	 * @param merchId the merchId to set
	 */
	public void setMerchantId(String merchId) {
		this.merchantId = merchId;
	}
	/**
	 * @return the merchVpa
	 */
	public String getMerchantVpa() {
		return merchantVpa;
	}
	/**
	 * @param merchVpa the merchVpa to set
	 */
	public void setMerchantVpa(String merchVpa) {
		this.merchantVpa = merchVpa;
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
	 * @return the txnCurrency
	 */
	public String getTxnCurrency() {
		return txnCurrency;
	}
	/**
	 * @param txnCurrency the txnCurrency to set
	 */
	public void setTxnCurrency(String txnCurrency) {
		this.txnCurrency = txnCurrency;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UPITransactionDTO [customerVpa=" + customerVpa + ", pspMerchantId=" + pspMerchantId
				+ ", merchantChannelId=" + merchantChannelId + ", merchantTransactionId=" + merchantTransactionId
				+ ", transactionTimestamp=" + transactionTimestamp + ", transactionAmount=" + transactionAmount
				+ ", gatewayTransactionId=" + gatewayTransactionId + ", gatewayResponseCode=" + gatewayResponseCode
				+ ", gatewayResponseMessage=" + gatewayResponseMessage + ", checksum=" + checksum + ", merchantId="
				+ merchantId + ", merchantVpa=" + merchantVpa + ", rrn=" + rrn + ", txnCurrency=" + txnCurrency
				+ ", bankCode=" + bankCode + ", fromEntity=" + fromEntity + ", txnId=" + txnId + ", qrCodeType="
				+ qrCodeType + ", terminalId=" + terminalId + ", authCode=" + authCode  + ", creditVpa="+ creditVpa+ "]";
	}
}
