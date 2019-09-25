package com.wl.upi.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.wl.util.constants.ErrorMessages;
import com.wl.util.exceptions.ApplicationException;

@Component("smsVendorFactory")
public class SMSVendorFactory {
	
	private static Logger logger = LoggerFactory.getLogger(SMSVendorFactory.class);

	@Autowired
	private ApplicationContext applicationContext; 

	public SMSVendor getSMSVendor(String bankcode) {
		
		SMSVendor smsVendor = null; 
		switch (bankcode) {
		case "00011":
			smsVendor = applicationContext.getBean(IdbiSmsImpl.class);
			break;
		case "00004":
		case "00074":
			smsVendor = applicationContext.getBean(CbiSmsImpl.class);
			break;
		case "00031":
		case "00006":
		case "00075":
		case "00045":
		case "00050":
		case "00079":
		case "00058":
		case "00003":
		case "00051":
		case "00001":
		case "00055":	
			smsVendor = applicationContext.getBean(MultibankNetcoreSmsImpl.class);
			break;
		default:
			throw new ApplicationException(ErrorMessages.SMS_UNSUPPORTED.toString());
		}		
		return smsVendor;

	}

}
