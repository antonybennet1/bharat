package com.wl.bharatqr.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wl.bharatqr.util.URIConstant;
import com.wl.upi.service.AxisBankUPIService;
import com.wl.upi.service.UPIBankService;
import com.wl.upi.service.UPIServiceFactory;
import com.wl.util.JsonUtility;
import com.wl.util.config.ApplicationConfig;
import com.wl.util.constants.Constants;
import com.wl.util.constants.ErrorMessages;
import com.wl.util.exceptions.ApplicationException;
import com.wl.util.model.Request;
import com.wl.util.model.Response;

@RestController
public class JobsController {
	
	private static Logger logger = LoggerFactory.getLogger(JobsController.class);
	
	@Autowired
	@Qualifier("upiServiceFactory")
	private UPIServiceFactory upiServiceFactory;

	@ResponseBody
	@RequestMapping(value = URIConstant.REFUND_JOB, method = { RequestMethod.GET})
	public Response refundTransaction(@PathVariable("refundFor") String refundFor, @RequestParam String bankCode, HttpServletRequest request) {
		logger.info("Request came for IP :"+request.getRemoteAddr());
		if(request.getRemoteAddr().equals(ApplicationConfig.get("serverIP")))
		{
			if("UPI".equals(refundFor))
			{
				logger.info("Starting refund for UPI");
				UPIBankService upiBankService =  upiServiceFactory.getUPIService(bankCode);
				return upiBankService.doRefundJob("REFUNDJOB", bankCode);
			}
			else
				return new Response(Constants.FAILED.name(),"Invalid Request");
		}
		return new Response(Constants.FAILED.name(),"Unauthorized Request");
	}
}
