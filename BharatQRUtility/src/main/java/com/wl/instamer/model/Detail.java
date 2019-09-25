package com.wl.instamer.model;

public class Detail {

	private int detailid;
	private long marchantId;
	private long marchantCtegoryCode;
	private long counterPhoneNum;
	public String countryCode;
	public String currencyCode;
	public String bankCode;
	private boolean isEnabled;
	private boolean canRefund;
	private int tipConIndicator;
	private boolean ConFlag;
	private float conValue;
	private long tid;
	private String storeId;
	public int getDetailid() {
		return detailid;
	}
	public void setDetailid(int detailid) {
		this.detailid = detailid;
	}
	public long getMarchantId() {
		return marchantId;
	}
	public void setMarchantId(long marchantId) {
		this.marchantId = marchantId;
	}
	public long getMarchantCtegoryCode() {
		return marchantCtegoryCode;
	}
	public void setMarchantCtegoryCode(long marchantCtegoryCode) {
		this.marchantCtegoryCode = marchantCtegoryCode;
	}
	public long getCounterPhoneNum() {
		return counterPhoneNum;
	}
	public void setCounterPhoneNum(long counterPhoneNum) {
		this.counterPhoneNum = counterPhoneNum;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public boolean isEnabled() {
		return isEnabled;
	}
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	public boolean isCanRefund() {
		return canRefund;
	}
	public void setCanRefund(boolean canRefund) {
		this.canRefund = canRefund;
	}
	public int getTipConIndicator() {
		return tipConIndicator;
	}
	public void setTipConIndicator(int tipConIndicator) {
		this.tipConIndicator = tipConIndicator;
	}
	public boolean isConFlag() {
		return ConFlag;
	}
	public void setConFlag(boolean conFlag) {
		ConFlag = conFlag;
	}
	public float getConValue() {
		return conValue;
	}
	public void setConValue(float conValue) {
		this.conValue = conValue;
	}
	public long getTid() {
		return tid;
	}
	public void setTid(long tid) {
		this.tid = tid;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	
	
}
