package com.wl.upi.sms;

public interface SMSVendor {
	public boolean sendSms(String mobileNumber,String message,String bankCode);
}
