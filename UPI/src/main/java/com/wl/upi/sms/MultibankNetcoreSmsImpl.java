package com.wl.upi.sms;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wl.upi.dao.SmsDAO;
import com.wl.util.HttpsClient;
import com.wl.util.config.SmsConfig;


@Component
public class MultibankNetcoreSmsImpl implements SMSVendor {

	@Autowired
	private SmsDAO smsDao; 

	private static Logger logger = LoggerFactory.getLogger(MultibankNetcoreSmsImpl.class);

	@Override
	public boolean sendSms(String mobileNumber, String message, String bankCode) {
		boolean flag= false;
		String URL = SmsConfig.get(bankCode,"url");
		String feedId = SmsConfig.get(bankCode,"feedid"); 
		String userName = SmsConfig.get(bankCode,"username");
		String password = SmsConfig.get(bankCode,"password"); 
		
		String finalUrl="";
		try {
			String queryString = createQueryString(mobileNumber, message, feedId, userName, password);
			finalUrl = URL + queryString;
		} catch (UnsupportedEncodingException e1) {
			logger.error("Error in URL encoding",e1);
		}
		String response = HttpsClient.send(finalUrl, "", null);
		logger.info(mobileNumber+"|Response from netcore :"+response);

		String responseCode = null;
		String responseMessage = null;
		int error_code_startIndex = response.indexOf("<CODE>");
		int error_code_endIndex = response.indexOf("</CODE>");
		if(error_code_startIndex>0)
		{
			int error_message_startIndex = response.indexOf("<DESC>");
			int error_message_endIndex = response.indexOf("</DESC>");
			if(error_code_startIndex>0)
			{
				responseCode = response.substring(error_code_startIndex+6,error_code_endIndex);
				responseMessage=response.substring(error_message_startIndex+6,error_message_endIndex);
			}
		}
		logger.debug(mobileNumber+"|response_code:"+responseCode );
		logger.debug(mobileNumber+"|response_message:"+responseMessage);
		try{
			smsDao.saveSmsLogs(feedId, mobileNumber, message, responseCode, responseMessage, bankCode);
			flag=true;
		}catch(Exception e)
		{
			logger.error(mobileNumber+"|error while inserting sms_logs table",e);
			flag=false;
		}
		return flag;
	}

	private String createQueryString(String phoneNumber, String message,String feedId, String  userName, String password) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		sb.append("feedid=" +feedId);
		if(userName!=null && !userName.isEmpty())
		{
			sb.append("&username=" +userName);
		}
		if(password!=null && !password.isEmpty())
		{
			sb.append("&password=" +password);
		}
		sb.append("&To=" + phoneNumber);
		sb.append("&Text="+URLEncoder.encode(message,"UTF-8"));
		return sb.toString();
	}
}
