package com.wl.instamer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MerchantOnboard {
	@JsonIgnore
	private String requestId;
	@JsonProperty(required=true)
	private Integer merIdentification;
	@JsonProperty(required=true)
	private String bankCode;
	@JsonProperty(required=true)
	private String merCreationDate;
	@JsonProperty(required=true)
	private Long appNo;
	@JsonProperty(required=true)
	private String customerId;
	@JsonProperty(required=true)
	private String merType;
	@JsonProperty(required=true)
	private String merSegment;
	@JsonProperty(required=true)
	private Integer mcc;
	@JsonProperty(required=true)
	private Integer tcc;
	@JsonProperty(required=true)
	private Long panNo;
	@JsonProperty(required=true)
	private String legalName;
	@JsonProperty(required=true)
	private String dbaName;
	@JsonProperty(required=true)
	private Integer zoneCode;
	@JsonProperty(required=true)
	private Integer branchCode;
	@JsonProperty(required=true)
	private String grossMdrType;
	@JsonProperty(required=true)
	private String dob;
	@JsonProperty(required=true)
	private String businessEntity;
	@JsonProperty(required=true)
	private String instAddContactPerson;
	@JsonProperty(required=true)
	private String instAddressLine1;
	@JsonProperty(required=true)
	private String instAddressLine2;
	@JsonProperty(required=true)
	private Integer instAddDistrict;
	@JsonProperty(required=true)
	private Integer instCity;
	@JsonProperty(required=true)
	private Integer instState;
	@JsonProperty(required=true)
	private Integer instLocation;
	@JsonProperty(required=true)
	private Integer instAddPincode;
	@JsonProperty(required=true)
	private Long instAddTelephone;
	@JsonProperty(required=true)
	private Long instAddMobileNo;
	@JsonProperty(required=true)
	private String instAddEmail;
	@JsonProperty(required=true)
	private String raAddressLine1;
	@JsonProperty(required=true)
	private String raAddressLine2;
	@JsonProperty(required=true)
	private Integer raPinCode;
	@JsonProperty(required=true)
	private Integer raCity;
	@JsonProperty(required=true)
	private Long raMobile1;
	@JsonProperty(required=true)
	private Long raMobile2;
	@JsonProperty(required=true)
	private Integer raState;
	@JsonProperty(required=true)
	private Integer raDistrict;
	@JsonProperty(required=true)
	private String paymentBy;
	@JsonProperty(required=true)
	private Long accountNo;
	@JsonProperty(required=true)
	private String accountLabel;
	@JsonProperty(required=true)
	private String bnfIfsc;
	@JsonProperty(required=true)
	private String bnfName;
	@JsonProperty(required=true)
	private String bnfAccountNo;
	@JsonProperty(required=true)
	private String bnfBankName;
	@JsonProperty(required=true)
	private Integer paySoldId;
	@JsonProperty(required=true)
	private String stmtReqType;
	@JsonProperty(required=true)
	private String stmtFrequency;
	@JsonProperty(required=true)
	private String merDocumentName;
	@JsonProperty(required=true)
	private String terminalType;
	@JsonProperty(required=true)
	private String edcModel;
	@JsonProperty(required=true)
	private Double domUpto1000Onus;
	@JsonProperty(required=true)
	private Double domUpto1000Offus;
	@JsonProperty(required=true)
	private Double domOffusLess2000;
	@JsonProperty(required=true)
	private Double domOnusLess2000;
	@JsonProperty(required=true)
	private Double domOffusGret2000;
	@JsonProperty(required=true)
	private Double domOnusGret2000;
	@JsonProperty(required=true)
	private Integer bankSubRate;
	@JsonProperty(required=true)
	private Integer leadGenId;
	@JsonProperty(required=true)
	private String seCode;
	@JsonProperty(required=true)
	private String approvedBy;
	@JsonProperty(required=true)
	private String profitabilityStatus;
	@JsonProperty(required=true)
	private String postFacto;
	@JsonProperty(required=true)
	private String nonOperCANo;
	@JsonProperty(required=true)
	private String riskApproval;
	@JsonProperty(required=true)
	private String tipFlag;
	@JsonProperty(required=true)
	private String conFeeFlag;
	@JsonProperty(required=true)
	private Double conFeeAmount;
	@JsonProperty(required=true)
	private Integer conFeePercentage;
	@JsonProperty(required=true)
	private Long merMobileNumber;
	@JsonProperty(required=true)
	private String merEmailId;
	@JsonProperty(required=true)
	private String referralCode;
	@JsonProperty(required=true)
	private Integer numberOfTerminal;
	@JsonProperty(required=true)
	private Integer joiningFee;
	@JsonProperty(required=true)
	private Integer rentalFee;
	@JsonProperty(required=true)
	private Integer setupFee;
	@JsonProperty(required=true)
	private Integer otherCharges;
	@JsonProperty(required=true)
	private String identificationType;
	@JsonProperty(required=true)
	private String isRefundAllowed;
	@JsonProperty(required=true)
	private String pInstallationAddFlag;
	@JsonProperty(required=true)
	private Integer creditCardPremium;
	@JsonProperty(required=true)
	private Integer creditCardNonPremium;
	
	
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public Integer getMerIdentification() {
		return merIdentification;
	}
	public void setMerIdentification(Integer merIdentification) {
		this.merIdentification = merIdentification;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getMerCreationDate() {
		return merCreationDate;
	}
	public void setMerCreationDate(String merchantCreationDate) {
		this.merCreationDate = merchantCreationDate;
	}
	public Long getAppNo() {
		return appNo;
	}
	public void setAppNo(Long appNo) {
		this.appNo = appNo;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getMerType() {
		return merType;
	}
	public void setMerType(String merType) {
		this.merType = merType;
	}
	public String getMerSegment() {
		return merSegment;
	}
	public void setMerSegment(String merSegment) {
		this.merSegment = merSegment;
	}
	public Integer getMcc() {
		return mcc;
	}
	public void setMcc(Integer mcc) {
		this.mcc = mcc;
	}
	public Integer getTcc() {
		return tcc;
	}
	public void setTcc(Integer tcc) {
		this.tcc = tcc;
	}
	public Long getPanNo() {
		return panNo;
	}
	public void setPanNo(Long panNo) {
		this.panNo = panNo;
	}
	public String getLegalName() {
		return legalName;
	}
	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}
	public String getDbaName() {
		return dbaName;
	}
	public void setDbaName(String dbaName) {
		this.dbaName = dbaName;
	}
	public Integer getZoneCode() {
		return zoneCode;
	}
	public void setZoneCode(Integer zoneCode) {
		this.zoneCode = zoneCode;
	}
	public Integer getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(Integer branchCode) {
		this.branchCode = branchCode;
	}
	public String getGrossMdrType() {
		return grossMdrType;
	}
	public void setGrossMdrType(String grossMdrType) {
		this.grossMdrType = grossMdrType;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getBusinessEntity() {
		return businessEntity;
	}
	public void setBusinessEntity(String businessEntity) {
		this.businessEntity = businessEntity;
	}
	public String getInstAddContactPerson() {
		return instAddContactPerson;
	}
	public void setInstAddContactPerson(String instAddContactPerson) {
		this.instAddContactPerson = instAddContactPerson;
	}
	public String getInstAddressLine1() {
		return instAddressLine1;
	}
	public void setInstAddressLine1(String instAddressLine1) {
		this.instAddressLine1 = instAddressLine1;
	}
	public String getInstAddressLine2() {
		return instAddressLine2;
	}
	public void setInstAddressLine2(String instAddressLine2) {
		this.instAddressLine2 = instAddressLine2;
	}
	public Integer getInstCity() {
		return instCity;
	}
	public void setInstCity(Integer instCity) {
		this.instCity = instCity;
	}
	
	public Integer getInstLocation() {
		return instLocation;
	}
	public void setInstLocation(Integer instLocation) {
		this.instLocation = instLocation;
	}
	public Integer getInstAddPincode() {
		return instAddPincode;
	}
	public void setInstAddPincode(Integer instAddPincode) {
		this.instAddPincode = instAddPincode;
	}
	public Long getInstAddTelephone() {
		return instAddTelephone;
	}
	public void setInstAddTelephone(Long instAddTelephone) {
		this.instAddTelephone = instAddTelephone;
	}
	public Long getInstAddMobileNo() {
		return instAddMobileNo;
	}
	public void setInstAddMobileNo(Long instAddMobileNo) {
		this.instAddMobileNo = instAddMobileNo;
	}
	public String getInstAddEmail() {
		return instAddEmail;
	}
	public void setInstAddEmail(String instAddEmail) {
		this.instAddEmail = instAddEmail;
	}
	public String getRaAddressLine1() {
		return raAddressLine1;
	}
	public void setRaAddressLine1(String raAddressLine1) {
		this.raAddressLine1 = raAddressLine1;
	}
	public String getRaAddressLine2() {
		return raAddressLine2;
	}
	public void setRaAddressLine2(String raAddressLine2) {
		this.raAddressLine2 = raAddressLine2;
	}
	public Integer getRaPinCode() {
		return raPinCode;
	}
	public void setRaPinCode(Integer raPinCode) {
		this.raPinCode = raPinCode;
	}
	public Integer getRaCity() {
		return raCity;
	}
	public void setRaCity(Integer raCity) {
		this.raCity = raCity;
	}
	public Long getRaMobile1() {
		return raMobile1;
	}
	public void setRaMobile1(Long raMobile1) {
		this.raMobile1 = raMobile1;
	}
	public Long getRaMobile2() {
		return raMobile2;
	}
	public void setRaMobile2(Long raMobile2) {
		this.raMobile2 = raMobile2;
	}
	public String getPaymentBy() {
		return paymentBy;
	}
	public void setPaymentBy(String paymentBy) {
		this.paymentBy = paymentBy;
	}
	public Long getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(Long accountNo) {
		this.accountNo = accountNo;
	}
	public String getAccountLabel() {
		return accountLabel;
	}
	public void setAccountLabel(String accountLabel) {
		this.accountLabel = accountLabel;
	}
	public String getBnfIfsc() {
		return bnfIfsc;
	}
	public void setBnfIfsc(String bnfIfsc) {
		this.bnfIfsc = bnfIfsc;
	}
	public String getBnfName() {
		return bnfName;
	}
	public void setBnfName(String bnfName) {
		this.bnfName = bnfName;
	}
	public String getBnfAccountNo() {
		return bnfAccountNo;
	}
	public void setBnfAccountNo(String bnfAccountNo) {
		this.bnfAccountNo = bnfAccountNo;
	}
	public String getBnfBankName() {
		return bnfBankName;
	}
	public void setBnfBankName(String bnfBankName) {
		this.bnfBankName = bnfBankName;
	}
	public Integer getPaySoldId() {
		return paySoldId;
	}
	public void setPaySoldId(Integer paySoldId) {
		this.paySoldId = paySoldId;
	}
	public String getStmtReqType() {
		return stmtReqType;
	}
	public void setStmtReqType(String stmtReqType) {
		this.stmtReqType = stmtReqType;
	}
	public String getStmtFrequency() {
		return stmtFrequency;
	}
	public void setStmtFrequency(String stmtFrequency) {
		this.stmtFrequency = stmtFrequency;
	}
	public String getMerDocumentName() {
		return merDocumentName;
	}
	public void setMerDocumentName(String merDocumentName) {
		this.merDocumentName = merDocumentName;
	}
	public String getTerminalType() {
		return terminalType;
	}
	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}
	public String getEdcModel() {
		return edcModel;
	}
	public void setEdcModel(String edcModel) {
		this.edcModel = edcModel;
	}
	public Double getDomUpto1000Onus() {
		return domUpto1000Onus;
	}
	public void setDomUpto1000Onus(Double domUpto1000Onus) {
		this.domUpto1000Onus = domUpto1000Onus;
	}
	public Double getDomUpto1000Offus() {
		return domUpto1000Offus;
	}
	public void setDomUpto1000Offus(Double domUpto1000Offus) {
		this.domUpto1000Offus = domUpto1000Offus;
	}
	public Double getDomOffusLess2000() {
		return domOffusLess2000;
	}
	public void setDomOffusLess2000(Double domOffusLess2000) {
		this.domOffusLess2000 = domOffusLess2000;
	}
	public Double getDomOnusLess2000() {
		return domOnusLess2000;
	}
	public void setDomOnusLess2000(Double domOnusLess2000) {
		this.domOnusLess2000 = domOnusLess2000;
	}
	public Double getDomOffusGret2000() {
		return domOffusGret2000;
	}
	public void setDomOffusGret2000(Double domOffusGret2000) {
		this.domOffusGret2000 = domOffusGret2000;
	}
	public Double getDomOnusGret2000() {
		return domOnusGret2000;
	}
	public void setDomOnusGret2000(Double domOnusGret2000) {
		this.domOnusGret2000 = domOnusGret2000;
	}
	public Integer getBankSubRate() {
		return bankSubRate;
	}
	public void setBankSubRate(Integer bankSubRate) {
		this.bankSubRate = bankSubRate;
	}
	public Integer getLeadGenId() {
		return leadGenId;
	}
	public void setLeadGenId(Integer leadGenId) {
		this.leadGenId = leadGenId;
	}
	public String getSeCode() {
		return seCode;
	}
	public void setSeCode(String seCode) {
		this.seCode = seCode;
	}
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	public String getProfitabilityStatus() {
		return profitabilityStatus;
	}
	public void setProfitabilityStatus(String profitabilityStatus) {
		this.profitabilityStatus = profitabilityStatus;
	}
	public String getPostFacto() {
		return postFacto;
	}
	public void setPostFacto(String postFacto) {
		this.postFacto = postFacto;
	}
	public String getNonOperCANo() {
		return nonOperCANo;
	}
	public void setNonOperCANo(String nonOperCANo) {
		this.nonOperCANo = nonOperCANo;
	}
	public String getRiskApproval() {
		return riskApproval;
	}
	public void setRiskApproval(String riskApproval) {
		this.riskApproval = riskApproval;
	}
	public String getTipFlag() {
		return tipFlag;
	}
	public void setTipFlag(String tipFlag) {
		this.tipFlag = tipFlag;
	}
	public String getConFeeFlag() {
		return conFeeFlag;
	}
	public void setConFeeFlag(String conFeeFlag) {
		this.conFeeFlag = conFeeFlag;
	}
	public Double getConFeeAmount() {
		return conFeeAmount;
	}
	public void setConFeeAmount(Double conFeeAmount) {
		this.conFeeAmount = conFeeAmount;
	}
	public Integer getConFeePercentage() {
		return conFeePercentage;
	}
	public void setConFeePercentage(Integer conFeePercentage) {
		this.conFeePercentage = conFeePercentage;
	}
	public Long getMerMobileNumber() {
		return merMobileNumber;
	}
	public void setMerMobileNumber(Long merMobileNumber) {
		this.merMobileNumber = merMobileNumber;
	}
	public String getMerEmailId() {
		return merEmailId;
	}
	public void setMerEmailId(String merEmailId) {
		this.merEmailId = merEmailId;
	}
	public String getReferralCode() {
		return referralCode;
	}
	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}
	public Integer getNumberOfTerminal() {
		return numberOfTerminal;
	}
	public void setNumberOfTerminal(Integer numberOfTerminal) {
		this.numberOfTerminal = numberOfTerminal;
	}
	public Integer getJoiningFee() {
		return joiningFee;
	}
	public void setJoiningFee(Integer joiningFee) {
		this.joiningFee = joiningFee;
	}
	public Integer getRentalFee() {
		return rentalFee;
	}
	public void setRentalFee(Integer rentalFee) {
		this.rentalFee = rentalFee;
	}
	public Integer getSetupFee() {
		return setupFee;
	}
	public void setSetupFee(Integer setupFee) {
		this.setupFee = setupFee;
	}
	public Integer getOtherCharges() {
		return otherCharges;
	}
	public void setOtherCharges(Integer otherCharges) {
		this.otherCharges = otherCharges;
	}
	public String getIdentificationType() {
		return identificationType;
	}
	public void setIdentificationType(String identificationType) {
		this.identificationType = identificationType;
	}
	public String getIsRefundAllowed() {
		return isRefundAllowed;
	}
	public void setIsRefundAllowed(String isRefundAllowed) {
		this.isRefundAllowed = isRefundAllowed;
	}
	public String getpInstallationAddFlag() {
		return pInstallationAddFlag;
	}
	public void setpInstallationAddFlag(String pInstallationAddFlag) {
		this.pInstallationAddFlag = pInstallationAddFlag;
	}
	public Integer getCreditCardPremium() {
		return creditCardPremium;
	}
	public void setCreditCardPremium(Integer creditCardPremium) {
		this.creditCardPremium = creditCardPremium;
	}
	public Integer getCreditCardNonPremium() {
		return creditCardNonPremium;
	}
	public void setCreditCardNonPremium(Integer creditCardNonPremium) {
		this.creditCardNonPremium = creditCardNonPremium;
	}
	
	/**
	 * @return the raState
	 */
	public Integer getRaState() {
		return raState;
	}
	/**
	 * @param raState the raState to set
	 */
	public void setRaState(Integer raState) {
		this.raState = raState;
	}
	
	
	/**
	 * @return the raDistrict
	 */
	public Integer getRaDistrict() {
		return raDistrict;
	}
	/**
	 * @param raDistrict the raDistrict to set
	 */
	public void setRaDistrict(Integer raDistrict) {
		this.raDistrict = raDistrict;
	}
	
	
	/**
	 * @return the instAddDistrict
	 */
	public Integer getInstAddDistrict() {
		return instAddDistrict;
	}
	/**
	 * @param instAddDistrict the instAddDistrict to set
	 */
	public void setInstAddDistrict(Integer instAddDistrict) {
		this.instAddDistrict = instAddDistrict;
	}
	
	/**
	 * @return the instState
	 */
	public Integer getInstState() {
		return instState;
	}
	/**
	 * @param instState the instState to set
	 */
	public void setInstState(Integer instState) {
		this.instState = instState;
	}
	
	
	@Override
	public String toString() {
		return "MerchantOnboard [merIdentification=" + merIdentification
				+ ", bankCode=" + bankCode + ", merCreationDate="
				+ merCreationDate + ", appNo=" + appNo + ", customerId="
				+ customerId + ", merType=" + merType + ", merSegment="
				+ merSegment + ", mcc=" + mcc + ", tcc=" + tcc + ", panNo="
				+ panNo + ", legalName=" + legalName + ", dbaName=" + dbaName
				+ ", zoneCode=" + zoneCode + ", branchCode=" + branchCode
				+ ", grossMdrType=" + grossMdrType + ", dob=" + dob
				+ ", businessEntity=" + businessEntity
				+ ", instAddContactPerson=" + instAddContactPerson
				+ ", instAddressLine1=" + instAddressLine1
				+ ", instAddressLine2=" + instAddressLine2 + ", instCity="
				+ instCity + ", instLocation=" + instLocation
				+ ", instAddPincode=" + instAddPincode + ", instAddTelephone="
				+ instAddTelephone + ", instAddMobileNo=" + instAddMobileNo
				+ ", instAddEmail=" + instAddEmail + ", raAddressLine1="
				+ raAddressLine1 + ", raAddressLine2=" + raAddressLine2
				+ ", raPinCode=" + raPinCode + ", raCity=" + raCity
				+ ", raMobile1=" + raMobile1 + ", raMobile2=" + raMobile2
				+ ", paymentBy=" + paymentBy + ", accountNo=" + accountNo
				+ ", accountLabel=" + accountLabel + ", bnfIfsc=" + bnfIfsc
				+ ", bnfName=" + bnfName + ", bnfAccountNo=" + bnfAccountNo
				+ ", bnfBankName=" + bnfBankName + ", paySoldId=" + paySoldId
				+ ", stmtReqType=" + stmtReqType + ", stmtFrequency="
				+ stmtFrequency + ", merDocumentName=" + merDocumentName
				+ ", terminalType=" + terminalType + ", edcModel=" + edcModel
				+ ", domUpto1000Onus=" + domUpto1000Onus
				+ ", domUpto1000Offus=" + domUpto1000Offus
				+ ", domOffusLess2000=" + domOffusLess2000
				+ ", domOnusLess2000=" + domOnusLess2000
				+ ", domOffusGret2000=" + domOffusGret2000
				+ ", domOnusGret2000=" + domOnusGret2000 + ", bankSubRate="
				+ bankSubRate + ", leadGenId=" + leadGenId + ", seCode="
				+ seCode + ", approvedBy=" + approvedBy
				+ ", profitabilityStatus=" + profitabilityStatus
				+ ", postFacto=" + postFacto + ", nonOperCANo=" + nonOperCANo
				+ ", riskApproval=" + riskApproval + ", tipFlag=" + tipFlag
				+ ", conFeeFlag=" + conFeeFlag + ", conFeeAmount="
				+ conFeeAmount + ", conFeePercentage=" + conFeePercentage
				+ ", merMobileNumber=" + merMobileNumber + ", merEmailId="
				+ merEmailId + ", referralCode=" + referralCode
				+ ", numberOfTerminal=" + numberOfTerminal + ", joiningFee="
				+ joiningFee + ", rentalFee=" + rentalFee + ", setupFee="
				+ setupFee + ", otherCharges=" + otherCharges
				+ ", identificationType=" + identificationType
				+ ", isRefundAllowed=" + isRefundAllowed
				+ ", pInstallationAddFlag=" + pInstallationAddFlag
				+ ", creditCardPremium=" + creditCardPremium
				+ ", creditCardNonPremium=" + creditCardNonPremium + "]";
	}
	
	
	
}
