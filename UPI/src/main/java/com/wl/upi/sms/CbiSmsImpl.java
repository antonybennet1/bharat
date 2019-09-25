package com.wl.upi.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wl.upi.dao.SmsDAO;
import com.wl.util.HttpsClient;
import com.wl.util.config.SmsConfig;

@Component
public class CbiSmsImpl implements SMSVendor {

	@Autowired
	private SmsDAO smsDao; 
	
	private static String SEPARATOR = "&";
	private static Logger logger = LoggerFactory.getLogger(CbiSmsImpl.class);
	
	@Override
	public boolean sendSms(String mobileNumber, String message, String bankCode) {

		String URL = SmsConfig.get(bankCode, "url");
		String queryString = createQueryString(mobileNumber, message,bankCode);
		String response = "";
		try {
			logger.debug("cbi sms URL : "+URL);
			logger.debug("cbi sms queryString : "+queryString);
			response = HttpsClient.send(URL, queryString, null);
			logger.debug("cbi sms response : "+response);
		} catch (Exception e) {
			logger.error("cbi sms error : "+e);
		}
		boolean flag = false;
		if (response != null && !response.isEmpty() && response.contains("cbindotp")) {
			flag =  true;
		} else
			flag =  false;		
		
		try{
			smsDao.saveSmsLogs(SmsConfig.get(bankCode, "enterpriseid"), mobileNumber, message, "00", response.length() > 40 ? response.substring(0,40) : response, bankCode);
		}catch(Exception e)
		{
			logger.error(mobileNumber+"|error while inserting sms_logs table",e);
		}
		return flag;
	}
	
	private String createQueryString(String phoneNumber, String message,String bankCode) {
		StringBuilder sb = new StringBuilder();
		sb.append("enterpriseid=");
		sb.append(SmsConfig.get(bankCode, "enterpriseid"));
		sb.append(SEPARATOR);
		sb.append("subEnterpriseid=");
		sb.append(SmsConfig.get(bankCode, "subEnterpriseid"));
		sb.append(SEPARATOR);
		sb.append("pusheid=");
		sb.append(SmsConfig.get(bankCode, "pusheid"));
		sb.append(SEPARATOR);
		sb.append("pushepwd=");
		sb.append(SmsConfig.get(bankCode, "pushepwd"));
		sb.append(SEPARATOR);
		sb.append("sender=");
		sb.append(SmsConfig.get(bankCode, "sender"));
		sb.append(SEPARATOR);
		sb.append("msisdn=");
		sb.append(phoneNumber);
		sb.append(SEPARATOR);
		sb.append("msgtext=");
		sb.append(message);
		return sb.toString();
	}


}
