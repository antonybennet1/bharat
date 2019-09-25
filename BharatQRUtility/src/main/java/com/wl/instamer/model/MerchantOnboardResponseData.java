package com.wl.instamer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MerchantOnboardResponseData {

	@JsonProperty(value="MERCHANTCODE")
	public String merchantCode;
	@JsonProperty(value="BANKCODE")
	public String bankCode;
	@JsonProperty(value="MASTER_QR_MID")
	public String masterMpan; 
	@JsonProperty(value="RUPAY_QR_MID")
	public String rupayMpan;
	@JsonProperty(value="MVISAPAN") 
	public String visaMpan;
	@JsonProperty(value="APPLICATIONUMBER")
	public String applicationNumber;
	@JsonProperty(value="TID")
	public String tid;
	@JsonProperty(value="MOBILENUMBER")
	public String mobileNumber;
	@JsonIgnore
	public String amexMpan;
	@JsonIgnore
	public String magnusResponseCode;
	@JsonIgnore
	public String rrn;
	@JsonIgnore
	public String approvalCode;
	public String getMerchantCode() {
		return merchantCode;
	}
	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getMasterMpan() {
		return masterMpan;
	}
	public void setMasterMpan(String masterMpan) {
		this.masterMpan = masterMpan;
	}
	public String getRupayMpan() {
		return rupayMpan;
	}
	public void setRupayMpan(String rupayMpan) {
		this.rupayMpan = rupayMpan;
	}
	public String getVisaMpan() {
		return visaMpan;
	}
	public void setVisaMpan(String visaMpan) {
		this.visaMpan = visaMpan;
	}
	public String getApplicationNumber() {
		return applicationNumber;
	}
	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getAmexMpan() {
		return amexMpan;
	}
	public void setAmexMpan(String amexMpan) {
		this.amexMpan = amexMpan;
	}
	
}
