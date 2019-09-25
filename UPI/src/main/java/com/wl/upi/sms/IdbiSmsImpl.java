package com.wl.upi.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wl.upi.dao.SmsDAO;
import com.wl.util.HelperUtil;
import com.wl.util.HttpsClient;
import com.wl.util.config.ApplicationConfig;
import com.wl.util.config.SmsConfig;

@Component
public class IdbiSmsImpl implements SMSVendor {

	@Autowired
	private SmsDAO smsDao; 

	private static String SEPARATOR = "&";
	private static Logger logger = LoggerFactory.getLogger(IdbiSmsImpl.class);

	@Override
	public boolean sendSms(String mobileNumber, String message, String bankCode) {

		String URL = SmsConfig.get(bankCode, "url");
		String queryString =createQueryString(mobileNumber,message,bankCode);
		String response = HttpsClient.send(URL,queryString,null);
		logger.debug("cbi sms URL : "+URL);
		logger.debug("cbi sms queryString : "+queryString);
		if(response!=null && !response.isEmpty() && response.contains("code="))
		{
			StringBuilder sb = new StringBuilder(response);
			int index = sb.indexOf("~");
			int len = "code=".length();
			int startIndex = index+len+1;
			String responseCode = sb.substring(startIndex,sb.indexOf("&"));
			sb = null; 
			logger.debug("****" + responseCode.trim() + "****");
			boolean flag  = false;

			if("API000".equals(responseCode.trim()))
				flag = true;
			else
				flag =  false;

			try{
				smsDao.saveSmsLogs(SmsConfig.get(bankCode, "aid"), mobileNumber, message, responseCode, response.length() > 40 ? response.substring(0,40) : response, bankCode);
			}catch(Exception e)
			{
				logger.error(mobileNumber+"|error while inserting sms_logs table",e);
			}
			return flag;
		}
		else
			return false;
	}

	private static  String createQueryString(String phoneNumber, String message,String bankCode)
	{
		StringBuilder sb = new StringBuilder();
		//String queryString = "aid=8451&pin=ae4ry&mnumber=919987208561&message=Welcome to IDBI QR PoS  Your User Name is test_user and Initial Pswd is pass@1234. Download app (Andriod/IOS/Windows) from app store&signature=IDBIBK&custref=123456789";
		sb.append("aid=");
		sb.append(SmsConfig.get(bankCode, "aid"));
		sb.append(SEPARATOR);
		sb.append("pin=");
		sb.append(SmsConfig.get(bankCode, "pin"));
		sb.append(SEPARATOR);
		sb.append("mnumber=");
		if(phoneNumber.length() == 10){
			sb.append("91" + phoneNumber);
		}else{
			sb.append(phoneNumber);
		}
		
		sb.append(SEPARATOR);
		sb.append("message=");
		sb.append(message);
		sb.append(SEPARATOR);
		sb.append("signature=");
		sb.append(SmsConfig.get(bankCode, "signature"));
		sb.append(SEPARATOR);
		sb.append("custref=");
		sb.append(HelperUtil.getReferenceNumber());
		return sb.toString();
	}

}
