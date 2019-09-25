package com.wl.upi.model;

public class DeviceDetails {
	
	private String mobileNumber;
	private String platform;
	private String deviceId;
	private boolean primaryUser;
	private boolean inAppUser;
	
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}
	/**
	 * @param mobileNumber the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	/**
	 * @return the primaryUser
	 */
	public boolean isPrimaryUser() {
		return primaryUser;
	}
	/**
	 * @param primaryUser the primaryUser to set
	 */
	public void setPrimaryUser(boolean primaryUser) {
		this.primaryUser = primaryUser;
	}
	/**
	 * @return the inAppUser
	 */
	public boolean isInAppUser() {
		return inAppUser;
	}
	/**
	 * @param inAppUser the inAppUser to set
	 */
	public void setInAppUser(boolean inAppUser) {
		this.inAppUser = inAppUser;
	}
	
	
	

	
}
