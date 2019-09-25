/**
 * 
 */
package com.wl.upi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author kunal.surana
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SbiUpiTransaction  {

	@JsonProperty("payerVPA")
	private String customerVpa;
	@JsonProperty("pspRefNo")
	private String merchantTransactionId;  //as tr_id
	@JsonProperty("txnAuthDate")
	private String transactionTimestamp;          // as trans date
	@JsonProperty("amount")
	private String transactionAmount;          // as amount
	@JsonProperty("npciTransId")
	private String gatewayTransactionId;
	@JsonProperty("responseCode")
	private String gatewayResponseCode;   //as status
	@JsonProperty("statusDesc")
	private String gatewayResponseMessage;     // as message
	@JsonProperty("payeeVPA")
	private String merchantVpa;    // field is missing in input 
	@JsonProperty("custRefNo")
	private String rrn;                //as reference  txn_ref_no
	@JsonProperty("approvalNumber")
	private String authCode;
	@JsonProperty("upiTransRefNo")
	private String upiTransRefNo;
	@JsonProperty("status")
	private String status;
	@JsonProperty(value="addInfo",required=false)
	private Object addInfo;
	@JsonProperty("ref_url")
	private String referenceUrl;
	@JsonProperty("txn_type")
	private String txnType;
	@JsonProperty("errCode")
	private String errorCode;
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
	private String merchantId;
	@JsonIgnore
	private String txn_note;
	
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
	 * @return the upiTransRefNo
	 */
	public String getUpiTransRefNo() {
		return upiTransRefNo;
	}
	/**
	 * @param upiTransRefNo the upiTransRefNo to set
	 */
	public void setUpiTransRefNo(String upiTransRefNo) {
		this.upiTransRefNo = upiTransRefNo;
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
	 * @return the addInfo
	 */
	public Object getAddInfo() {
		return addInfo;
	}
	/**
	 * @param addInfo the addInfo to set
	 */
	public void setAddInfo(Object addInfo) {
		this.addInfo = addInfo;
	}
	/**
	 * @return the referenceUrl
	 */
	public String getReferenceUrl() {
		return referenceUrl;
	}
	/**
	 * @param referenceUrl the referenceUrl to set
	 */
	public void setReferenceUrl(String referenceUrl) {
		this.referenceUrl = referenceUrl;
	}
	/**
	 * @return the txnType
	 */
	public String getTxnType() {
		return txnType;
	}
	/**
	 * @param txnType the txnType to set
	 */
	public void setTxnType(String txnType) {
		this.txnType = txnType;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SbiUpiTransaction [customerVpa=" + customerVpa + ", merchantTransactionId=" + merchantTransactionId
				+ ", transactionTimestamp=" + transactionTimestamp + ", transactionAmount=" + transactionAmount
				+ ", gatewayTransactionId=" + gatewayTransactionId + ", gatewayResponseCode=" + gatewayResponseCode
				+ ", gatewayResponseMessage=" + gatewayResponseMessage + ", merchantVpa=" + merchantVpa + ", rrn=" + rrn
				+ ", authCode=" + authCode + ", upiTransRefNo=" + upiTransRefNo + ", status=" + status + ", addInfo="
				+ addInfo + ", referenceUrl=" + referenceUrl + ", txnType=" + txnType + ", errorCode=" + errorCode
				+ ", txnCurrency=" + txnCurrency + ", bankCode=" + bankCode + ", fromEntity=" + fromEntity + ", txnId="
				+ txnId + ", qrCodeType=" + qrCodeType + ", terminalId=" + terminalId + ", merchantId=" + merchantId
				+ "]";
	}
	/**
	 * @return the txn_note
	 */
	public String getTxn_note() {
		return txn_note;
	}
	/**
	 * @param txn_note the txn_note to set
	 */
	public void setTxn_note(String txn_note) {
		this.txn_note = txn_note;
	}
	
	
}
