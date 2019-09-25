package com.wl.upi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AggregatorRequest {
	
	@JsonProperty("TID")
	private String tid;
	private String amount;
	private String txnId;
	private String trId;
	@JsonIgnore
	private String fromEntity;
	
	
	
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
	
	 
	

}
