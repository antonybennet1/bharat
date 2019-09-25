package com.wl.qr.model;

public class MerchantDetails {
	
	private String bankCode;
	private int tipConvIndicator;
	private float convenienceValue;
	private String ifscAccountNumber;
	private String upiVpa;
	private double upiMam;
	private String upiUrl;
	private String aadharNumber;
	private int merchantCategoryCode;
	private int currencyCode;
	private String countryCode;
	private String name;
	private String cityName;
	private String postalCode;
	private int programType;
 	private int convenienceFlag;
 	
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public int getTipConvIndicator() {
		return tipConvIndicator;
	}
	public void setTipConvIndicator(int i) {
		this.tipConvIndicator = i;
	}
	public float getConvenienceValue() {
		return convenienceValue;
	}
	public void setConvenienceValue(float f) {
		this.convenienceValue = f;
	}
	public String getIfscAccountNumber() {
		return ifscAccountNumber;
	}
	public void setIfscAccountNumber(String ifscAccountNumber) {
		this.ifscAccountNumber = ifscAccountNumber;
	}
	public String getUpiVpa() {
		return upiVpa;
	}
	public void setUpiVpa(String upiVpa) {
		this.upiVpa = upiVpa;
	}
	public double getUpiMam() {
		return upiMam;
	}
	public void setUpiMam(double d) {
		this.upiMam = d;
	}
	public String getUpiUrl() {
		return upiUrl;
	}
	public void setUpiUrl(String upiUrl) {
		this.upiUrl = upiUrl;
	}
	public String getAadharNumber() {
		return aadharNumber;
	}
	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}
	public int getMerchantCategoryCode() {
		return merchantCategoryCode;
	}
	public void setMerchantCategoryCode(int i) {
		this.merchantCategoryCode = i;
	}
	public int getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(int i) {
		this.currencyCode = i;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public int getProgramType() {
		return programType;
	}
	public void setProgramType(int i) {
		this.programType = i;
	}
	public int getConvenienceFlag() {
		return convenienceFlag;
	}
	public void setConvenienceFlag(int i) {
		this.convenienceFlag = i;
	}
	@Override
	public String toString() {
		return "MerchantDetails [bankCode=" + bankCode + ", tipConvIndicator=" + tipConvIndicator
				+ ", convenienceValue=" + convenienceValue + ", ifscAccountNumber=" + ifscAccountNumber + ", upiVpa="
				+ upiVpa + ", upiMam=" + upiMam + ", upiUrl=" + upiUrl + ", aadharNumber=" + aadharNumber
				+ ", merchantCategoryCode=" + merchantCategoryCode + ", currencyCode=" + currencyCode + ", countryCode="
				+ countryCode + ", name=" + name + ", cityName=" + cityName + ", postalCode=" + postalCode
				+ ", programType=" + programType + ", convenienceFlag=" + convenienceFlag + "]";
	}
	
}
