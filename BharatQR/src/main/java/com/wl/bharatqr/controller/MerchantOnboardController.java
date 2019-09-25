package com.wl.bharatqr.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wl.bharatqr.util.URIConstant;
import com.wl.instamer.model.Response;
import com.wl.instamer.service.MerchantOnboardService;

@RestController
public class MerchantOnboardController {
	
	private static final Logger logger = LoggerFactory.getLogger(MerchantOnboardController.class);
	
	@Autowired
	private MerchantOnboardService merchantOnboardService;
	
	@ResponseBody
	@RequestMapping(value = URIConstant.MERCHANT_ONBOARD, method = {RequestMethod.POST,RequestMethod.GET},
	consumes={MediaType.APPLICATION_JSON_VALUE})
	public Response merchantOnboardService(HttpServletRequest request)
	{
		String param = request.getParameter("data");
		logger.debug("incoming request data:"+param);
		Response resp = merchantOnboardService.processRequest(param);
		return resp;
	}
}
