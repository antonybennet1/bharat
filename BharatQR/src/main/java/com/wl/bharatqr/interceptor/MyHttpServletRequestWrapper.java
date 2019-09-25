package com.wl.bharatqr.interceptor;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private final HashMap<String, String> additionalParameter;

	private static final Logger logger = LoggerFactory.getLogger(MyHttpServletRequestWrapper.class);

	public MyHttpServletRequestWrapper(HttpServletRequest request, String decryptedData) { 
		super(request);
		additionalParameter = new HashMap<String, String>(1);
		additionalParameter.put("data", decryptedData);
		//additionalParameter.put("fromEntity", request.getParameter("fromEntity"));
		logger.debug("data in wrapper "+decryptedData);
		
		//logger.debug("data in wrapper fromEntity 333333 "+request.getParameter("fromEntity"));
	}

	public MyHttpServletRequestWrapper(HttpServletRequest request, HashMap<String, String> requestParameter) { 
		super(request);
		additionalParameter = requestParameter;
		logger.info("Iside the hashmap costructor-------------------->" + requestParameter );
	}




	@Override
	public String getParameter(String name) {
		logger.debug("get Parameter called with name :"+name);
		if("data".equals(name) || "fromEntity".equals(name))
		{
			logger.info("Sending ---> " + name );
			return additionalParameter.get(name);
		}
		return super.getParameter(name);
	}


}