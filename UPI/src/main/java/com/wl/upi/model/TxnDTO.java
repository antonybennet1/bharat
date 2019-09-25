package com.wl.upi.model;

import java.sql.Timestamp;

public class TxnDTO {
	
	private int id;
	private String custPan; 
	private String custName; 
	private String currency;
	private String primaryId;
	private String secondaryId;
	private String merchantId; 
	private String merchantVpa;
	private String bankCode; 
	private String tid;
	private String txnAmount;
	private String settlementAmount;
	private String fromEntity;
	private String rrn;
	private String authCode;
	private Timestamp txnDate;
	private String customerVpa;
	private String trId;
	private String responseCode;
	private String responseMessage;
	private int programType;
	private String qrType;
	private Timestamp created;
	private String mpan; 
	private String transactionType;
	private double refundedAmount;
	private String status;
	private String errorCode;
	private String respRefNo;
	private String originalTrID;
	private String newRRn;
	
	//Added for Sbi Refund Request
	private String transRefNo;
	private String customerRefNo;
	private String orderNo;
	private String refundRemark;	
	
	public String getNewRRn() {
		return newRRn;
	}
	public void setNewRRn(String newRRn) {
		this.newRRn = newRRn;
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
	 * @return the transactionType
	 */
	public String getTransactionType() {
		return transactionType;
	}
	/**
	 * @param transactionType the transactionType to set
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
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
	 * @return the settlementAmount
	 */
	public String getSettlementAmount() {
		return settlementAmount;
	}
	/**
	 * @param settlementAmount the settlementAmount to set
	 */
	public void setSettlementAmount(String settlementAmount) {
		this.settlementAmount = settlementAmount;
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
	 * @return the primaryId
	 */
	public String getPrimaryId() {
		return primaryId;
	}
	/**
	 * @param primaryId the primaryId to set
	 */
	public void setPrimaryId(String primaryId) {
		this.primaryId = primaryId;
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
	 * @return the txnAmount
	 */
	public String getTxnAmount() {
		return txnAmount;
	}
	/**
	 * @param txnAmount the txnAmount to set
	 */
	public void setTxnAmount(String txnAmount) {
		this.txnAmount = txnAmount;
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
	 * @return the txnDate
	 */
	public Timestamp getTxnDate() {
		return txnDate;
	}
	/**
	 * @param txnDate the txnDate to set
	 */
	public void setTxnDate(Timestamp txnDate) {
		this.txnDate = txnDate;
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
	 * @return the responseCode
	 */
	public String getResponseCode() {
		return responseCode;
	}
	/**
	 * @param responseCode the responseCode to set
	 */
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	/**
	 * @return the responseMessage
	 */
	public String getResponseMessage() {
		return responseMessage;
	}
	/**
	 * @param responseMessage the responseMessage to set
	 */
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	/**
	 * @return the programType
	 */
	public int getProgramType() {
		return programType;
	}
	/**
	 * @param programType the programType to set
	 */
	public void setProgramType(int programType) {
		this.programType = programType;
	}
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
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
	 * @return the qrType
	 */
	public String getQrType() {
		return qrType;
	}
	/**
	 * @param qrType the qrType to set
	 */
	public void setQrType(String qrType) {
		this.qrType = qrType;
	}
	
	
	/**
	 * @return the originalTrID
	 */
	public String getOriginalTrID() {
		return originalTrID;
	}
	/**
	 * @param originalTrID the originalTrID to set
	 */
	
	public void setOriginalTrID(String originalTrID) {
		this.originalTrID = originalTrID;
	}
		
	public String getCustomerRefNo() {
		return customerRefNo;
	}
	public void setCustomerRefNo(String customerRefNo) {
		this.customerRefNo = customerRefNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getRefundRemark() {
		return refundRemark;
	}
	public void setRefundRemark(String refundRemark) {
		this.refundRemark = refundRemark;
	}
	
	public String getTransRefNo() {
		return transRefNo;
	}
	public void setTransRefNo(String transRefNo) {
		this.transRefNo = transRefNo;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TxnDTO [id=" + id + ", custPan=" + custPan + ", custName=" + custName + ", currency=" + currency
				+ ", primaryId=" + primaryId + ", secondaryId=" + secondaryId + ", merchantId=" + merchantId
				+ ", merchantVpa=" + merchantVpa + ", bankCode=" + bankCode + ", tid=" + tid + ", txnAmount="
				+ txnAmount + ", settlementAmount=" + settlementAmount + ", fromEntity=" + fromEntity + ", rrn=" + rrn
				+ ", authCode=" + authCode + ", txnDate=" + txnDate + ", customerVpa=" + customerVpa + ", trId=" + trId
				+ ", responseCode=" + responseCode + ", responseMessage=" + responseMessage + ", programType="
				+ programType + ", qrType=" + qrType + ", created=" + created + ", mpan=" + mpan + ", transactionType="
				+ transactionType + ", refundedAmount=" + refundedAmount + ", status=" + status + ", errorCode="
				+ errorCode + ",originalTrID =" + originalTrID + ", respRefNo=" + respRefNo + ",customerRefNo=" + customerRefNo + ", "
				+ "orderNo=" + orderNo + ", refundRemark=" + refundRemark + ", transRefNo=" + transRefNo + ","
				+ "getStatus()=" + getStatus() + ", getErrorCode()="+ getErrorCode() + ", getRespRefNo()=" + getRespRefNo() + ", getTransactionType()="
				+ getTransactionType() + ", getRefundedAmount()=" + getRefundedAmount() + ", getSettlementAmount()="
				+ getSettlementAmount() + ", getCurrency()=" + getCurrency() + ", getPrimaryId()=" + getPrimaryId()
				+ ", getSecondaryId()=" + getSecondaryId() + ", getCustPan()=" + getCustPan() + ", getCustName()="
				+ getCustName() + ", getMerchantId()=" + getMerchantId() + ", getMerchantVpa()=" + getMerchantVpa()
				+ ", getBankCode()=" + getBankCode() + ", getTid()=" + getTid() + ", getTxnAmount()=" + getTxnAmount()
				+ ", getFromEntity()=" + getFromEntity() + ", getRrn()=" + getRrn() + ", getAuthCode()=" + getAuthCode()
				+ ", getTxnDate()=" + getTxnDate() + ", getCustomerVpa()=" + getCustomerVpa() + ", getTrId()="
				+ getTrId() + ", getResponseCode()=" + getResponseCode() + ", getResponseMessage()="
				+ getResponseMessage() + ", getProgramType()=" + getProgramType() + ", getCreated()=" + getCreated()
				+ ", getMpan()=" + getMpan() + ", getId()=" + getId() + ",getOriginalTrID()=" +getOriginalTrID() +", getQrType()=" + getQrType() +","
				+ "getCustomerRefNo()=" + getCustomerRefNo() + ", getOrderNo()=" + getOrderNo() +", getRefundRemark()=" + getRefundRemark() 
				+ "getClass()="	+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}
