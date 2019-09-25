package com.wl.bharatqr.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.wl.util.JsonUtility;
import com.wl.util.constants.Constants;
import com.wl.util.constants.ErrorMessages;
import com.wl.util.exceptions.ApplicationException;
import com.wl.util.model.Response;

@EnableWebMvc 
@ControllerAdvice
@RestController
public class ExceptionController {
	
	private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);
	
	/*@ExceptionHandler(ApplicationException.class)
	public String  handleException(ApplicationException ex) {
		logger.error("handleJsonException: exception is raised is handled and response sent",ex);
		Response resp = new Response();
		resp.setStatus(Constants.FAILED.name());
		resp.setMessage(ex.getMessage());
		return JsonUtility.convertToJson(resp);
	}*/
	
	@ExceptionHandler(Exception.class)
	public String handleGeneralException(Exception ex) {
		logger.error("handleGeneralException: exception is raised is handled and response sent",ex);
		Response resp = new Response();
		resp.setStatus(Constants.FAILED.name());
		if(ex instanceof ApplicationException)
			resp.setMessage(ex.getMessage());
		else if(ex instanceof ServletRequestBindingException)
			resp.setMessage("Request Binding Error");
		else
			resp.setMessage(ErrorMessages.SERVER_ERROR.toString());
		return JsonUtility.convertToJson(resp);
	}
	
/*	@ExceptionHandler(ServletRequestBindingException.class)
	public String handleServletRequestBindingException(ServletRequestBindingException ex)
	{
		//change message
		logger.error("handleServletRequestBindingException: exception is raised is handled and response sent",ex);
		Response resp = new Response();
		resp.setStatus(Constants.FAILED.name());
		resp.setMessage("Server Internal Error");
		return JsonUtility.convertToJson(resp);
	}*/
}
