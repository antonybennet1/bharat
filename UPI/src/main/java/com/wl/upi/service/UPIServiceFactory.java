/**
 * 
 */
package com.wl.upi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.wl.util.constants.ErrorMessages;
import com.wl.util.exceptions.ApplicationException;

/**
 * @author faizul.mahammad
 *
 */
@Service("upiServiceFactory")
public class UPIServiceFactory {

	private static Logger logger = LoggerFactory.getLogger(UPIServiceFactory.class);

	@Autowired
	private ApplicationContext applicationContext; 

	public UPIBankService getUPIService(String bankcode) {
		UPIBankService upiBankService = null; 
		switch (bankcode) {
		case "00031":
			upiBankService = applicationContext.getBean(AxisBankUPIService.class);
			break;
			
		case "00004":
		case "00006":
			System.out.println("BAnkCode inside CbiBankUPIService  -------------------> " + bankcode);
			upiBankService = applicationContext.getBean(CbiBankUPIService.class);
			break;
		case "00058":
			upiBankService = applicationContext.getBean(IndusIndService.class);
		break;
		default:
			System.out.println("BAnkCode inside MultiBankUPIService  -------------------> " + bankcode);
			upiBankService = applicationContext.getBean(MultiBankUPIService.class);
		}		
		return upiBankService;

	}
}
