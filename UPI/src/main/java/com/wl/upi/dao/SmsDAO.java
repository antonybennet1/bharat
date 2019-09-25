package com.wl.upi.dao;

public interface SmsDAO {
	public void saveSmsLogs(String feedId, String to, String smsText, String responseCode,
			String responseMessage,String bankCode);
}
