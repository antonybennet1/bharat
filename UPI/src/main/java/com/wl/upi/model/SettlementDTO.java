package com.wl.upi.model;

public class SettlementDTO {
	private String txnChannel; // P_TXN_CHANNEL
	private String mid; //P_MERCHANTID
	private String tid; //P_TERMINALID
	private Integer batchNumber ; //P_BATCH
	private String rrn ; //P_RETRIVAL_REF_NUMBER
	private String requestType ; //P_REQUEST_TYPE
	private String txnTimestamp; //P_DATE (yyyymmddhhmmss)
	private String stanNumber ; //P_STAN_NUMBER
	private String authCode ; //P_AUTH_ID
	private String responceCode ; //P_RESPONSE_CODE
	private String invoiceNumber ; // P_INVOICENUMBER ; 
	private String branchCode; //P_BRANCH_CODE
	private String trId; //P_TRANSACTION_ID  which is trId
	private String originalAmount; //P_ORIGINAL_AMOUNT  
	private String currencyCode; //P_CURRENCY_CODE  
	private String additionalAmount; //P_ADDAMOUNT  
	private String tipApproved; //P_TIP_APPROVED  
	private String expiryDate; //P_EXPIRYDATE 
	private String cardEntryMode; // P_CARD_ENTRYMODE
	private String processingCode; //P_PROCESSING_CODE
	private String bankCode; //P_BANK_CODE
	private String mti; // P_MTI
	private String customerVpa; // P_CUST_VPA
	private String merchantVpa; // P_MERCHANT_VPA
	private String newRrn; // P_FIELF63
	
	public String getNewRrn() {
		return newRrn;
	}
	public void setNewRrn(String newRrn) {
		this.newRrn = newRrn;
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
	/**
	 * @return the txnChannel
	 */
	private String secondryId;
	public String getTxnChannel() {
		return txnChannel;
	}
	/**
	 * @param txnChannel the txnChannel to set
	 */
	public void setTxnChannel(String txnChannel) {
		this.txnChannel = txnChannel;
	}
	/**
	 * @return the mid
	 */
	public String getMid() {
		return mid;
	}
	/**
	 * @param mid the mid to set
	 */
	public void setMid(String mid) {
		this.mid = mid;
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
	/**
	 * @return the batchNumber
	 */
	public Integer getBatchNumber() {
		return batchNumber;
	}
	/**
	 * @param batchNumber the batchNumber to set
	 */
	public void setBatchNumber(Integer batchNumber) {
		this.batchNumber = batchNumber;
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
	 * @return the txnTimestamp
	 */
	public String getTxnTimestamp() {
		return txnTimestamp;
	}
	/**
	 * @param txnTimestamp the txnTimestamp to set
	 */
	public void setTxnTimestamp(String txnTimestamp) {
		this.txnTimestamp = txnTimestamp;
	}
	/**
	 * @return the stanNumber
	 */
	public String getStanNumber() {
		return stanNumber;
	}
	/**
	 * @param stanNumber the stanNumber to set
	 */
	public void setStanNumber(String stanNumber) {
		this.stanNumber = stanNumber;
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
	 * @return the responceCode
	 */
	public String getResponceCode() {
		return responceCode;
	}
	/**
	 * @param responceCode the responceCode to set
	 */
	public void setResponceCode(String responceCode) {
		this.responceCode = responceCode;
	}
	/**
	 * @return the invoiceNumber
	 */
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	/**
	 * @param invoiceNumber the invoiceNumber to set
	 */
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	/**
	 * @return the branchCode
	 */
	public String getBranchCode() {
		return branchCode;
	}
	/**
	 * @param branchCode the branchCode to set
	 */
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	/**
	 * @return the trId
	 */
	public String getTrId() {
		return trId;
	}
	/**
	 * @param trId the trId to set
	 */
	public void setTrId(String trId) {
		this.trId = trId;
	}
	/**
	 * @return the originalAmount
	 */
	public String getOriginalAmount() {
		return originalAmount;
	}
	/**
	 * @param originalAmount the originalAmount to set
	 */
	public void setOriginalAmount(String originalAmount) {
		this.originalAmount = originalAmount;
	}
	/**
	 * @return the currencyCode
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}
	/**
	 * @param currencyCode the currencyCode to set
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	/**
	 * @return the additionalAmount
	 */
	public String getAdditionalAmount() {
		return additionalAmount;
	}
	/**
	 * @param additionalAmount the additionalAmount to set
	 */
	public void setAdditionalAmount(String additionalAmount) {
		this.additionalAmount = additionalAmount;
	}
	/**
	 * @return the tipApproved
	 */
	public String getTipApproved() {
		return tipApproved;
	}
	/**
	 * @param tipApproved the tipApproved to set
	 */
	public void setTipApproved(String tipApproved) {
		this.tipApproved = tipApproved;
	}
	/**
	 * @return the expiryDate
	 */
	public String getExpiryDate() {
		return expiryDate;
	}
	/**
	 * @param expiryDate the expiryDate to set
	 */
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	/**
	 * @return the cardEntryMode
	 */
	public String getCardEntryMode() {
		return cardEntryMode;
	}
	/**
	 * @param cardEntryMode the cardEntryMode to set
	 */
	public void setCardEntryMode(String cardEntryMode) {
		this.cardEntryMode = cardEntryMode;
	}
	/**
	 * @return the processingCode
	 */
	public String getProcessingCode() {
		return processingCode;
	}
	/**
	 * @param processingCode the processingCode to set
	 */
	public void setProcessingCode(String processingCode) {
		this.processingCode = processingCode;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SettlementDTO [txnChannel=" + txnChannel + ", mid=" + mid + ", tid=" + tid + ", batchNumber="
				+ batchNumber + ", rrn=" + rrn + ", requestType=" + requestType + ", txnTimestamp=" + txnTimestamp
				+ ", stanNumber=" + stanNumber + ", authCode=" + authCode + ", responceCode=" + responceCode
				+ ", invoiceNumber=" + invoiceNumber + ", branchCode=" + branchCode + ", trId=" + trId
				+ ", originalAmount=" + originalAmount + ", currencyCode=" + currencyCode + ", additionalAmount="
				+ additionalAmount + ", tipApproved=" + tipApproved + ", expiryDate=" + expiryDate + ", cardEntryMode="
				+ cardEntryMode + ", processingCode=" + processingCode + ", bankCode=" + bankCode + ", mti=" + mti + ", merchantVpa=" + merchantVpa + ", customerVpa=" + customerVpa +  ", newRrn=" + newRrn
				+ "]";
	}
	/**
	 * @return the secondryId
	 */
	public String getSecondryId() {
		return secondryId;
	}
	/**
	 * @param secondryId the secondryId to set
	 */
	public void setSecondryId(String secondryId) {
		this.secondryId = secondryId;
	}
	
	
}

