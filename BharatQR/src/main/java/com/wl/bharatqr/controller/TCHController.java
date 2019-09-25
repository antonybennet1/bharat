package com.wl.bharatqr.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wl.bharatqr.util.URIConstant;
import com.wl.qr.service.QRService;
import com.wl.upi.service.UPIQRService;
import com.wl.upi.service.UPIServiceFactory;
import com.wl.util.JsonUtility;
import com.wl.util.constants.ErrorMessages;
import com.wl.util.exceptions.ApplicationException;
import com.wl.util.model.Request;
import com.wl.util.model.Response;

@RestController
public class TCHController {
	private static final Logger logger = LoggerFactory.getLogger(TCHController.class);

	@Autowired
	@Qualifier("upiQRService")
	private UPIQRService upiQRService;

	@Autowired
	private QRService qrService;
	
	@Autowired
	@Qualifier("upiServiceFactory")
	private UPIServiceFactory upiServiceFactory;
	

	@ResponseBody
	@RequestMapping(value = URIConstant.CHECK_TXN_STATUS, method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = {
					MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public Response checkTransactionStatus(HttpServletRequest request) {
		String jsonInput = request.getParameter("parm");
		logger.info("json request to checkTransactionStatus :" + jsonInput);
		Request reqObject = (Request) JsonUtility.parseJson(jsonInput, Request.class);
		logger.debug(reqObject.getFromEntity() + "|checkTransactionStatus - data :" + reqObject.getData());
		return upiQRService.checkStatus(reqObject.getFromEntity(),reqObject.getBankCode(), JsonUtility.convertToJson(reqObject.getData()));
	}

	@ResponseBody
	@RequestMapping(value = URIConstant.CANCEL_QR, method = { RequestMethod.POST, RequestMethod.GET }, consumes = {
			MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public Response cancelQR(HttpServletRequest request) {
		String jsonInput = request.getParameter("parm");
		logger.info("json request to cancelQR :" + jsonInput);
		Request reqObject = (Request) JsonUtility.parseJson(jsonInput, Request.class);
		if (reqObject == null)
			throw new ApplicationException(ErrorMessages.JSON_FORMAT_ERROR.toString());
		logger.debug(reqObject.getFromEntity() + "|cancelQR - data :" + reqObject.getData());
		return upiQRService.cancelQR(reqObject.getFromEntity(),reqObject.getBankCode(), JsonUtility.convertToJson(reqObject.getData()));
	}

	@ResponseBody
	@RequestMapping(value = URIConstant.EntityQR_GEN, method = { RequestMethod.POST, RequestMethod.GET }, consumes = {
			MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public Response entityQRCode(HttpServletRequest request) {
		String jsonRequest = request.getParameter("parm");
		logger.info("json request to entityQRCode :" + jsonRequest);
		Request reqObject = (Request) JsonUtility.parseJson(jsonRequest, Request.class);
		if (reqObject == null)
			throw new ApplicationException(ErrorMessages.JSON_FORMAT_ERROR.toString());
		logger.debug(reqObject.getFromEntity() + "|entityQRCode - data :" + reqObject.getData());
		return qrService.entityQRGen(reqObject.getFromEntity(),reqObject.getBankCode(), JsonUtility.convertToJson(reqObject.getData()));
	}
	
	@RequestMapping(value = URIConstant.CHECK_AGGTXN_STATUS, method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = {
					MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public Response checkAggregatorTransStatus(HttpServletRequest request) {
		String jsonInput = request.getParameter("parm");
		logger.info("json request to checkAggregatorTransStatus :" + jsonInput);
		Request reqObject = (Request) JsonUtility.parseJson(jsonInput, Request.class);
		logger.debug(reqObject.getFromEntity() + "|checkAggregatorTransStatus - data :" + reqObject.getData());
		return upiQRService.checkAggregatorTransStatus(reqObject.getFromEntity(),reqObject.getBankCode(), JsonUtility.convertToJson(reqObject.getData()));
	}
	@ResponseBody
	@RequestMapping(value = URIConstant.EntityQR_GEN_PSTN, method = { RequestMethod.POST, RequestMethod.GET }, consumes = {
			MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public Response entityQRCodePSTN(HttpServletRequest request) {
		String jsonRequest = request.getParameter("parm");
		logger.info("json request to entityQRCode :" + jsonRequest);
		Request reqObject = (Request) JsonUtility.parseJson(jsonRequest, Request.class);
		if (reqObject == null)
			throw new ApplicationException(ErrorMessages.JSON_FORMAT_ERROR.toString());
		logger.debug(reqObject.getFromEntity() + "|entityQRCode - data :" + reqObject.getData());
		return qrService.entityQRGenPSTN(reqObject.getFromEntity(),reqObject.getBankCode(), JsonUtility.convertToJson(reqObject.getData()));
	}
	
	@ResponseBody
	@RequestMapping(value = URIConstant.STATICQR_GEN, method = { RequestMethod.POST, RequestMethod.GET }, consumes = {
			MediaType.APPLICATION_FORM_URLENCODED_VALUE,MediaType.APPLICATION_JSON_VALUE })
	public Response entityStaticQRCode(HttpServletRequest request) {
		String jsonRequest = request.getParameter("parm");
		logger.info("json request to entityStaticQRCode :" + jsonRequest);
		Request reqObject = (Request) JsonUtility.parseJson(jsonRequest, Request.class);
		if (reqObject == null)
			throw new ApplicationException(ErrorMessages.JSON_FORMAT_ERROR.toString());
		logger.debug(reqObject.getFromEntity() + "|entityStaticQRCode - data :" + reqObject.getData());
		return qrService.getStaticQRString(reqObject.getFromEntity(),reqObject.getBankCode(), JsonUtility.convertToJson(reqObject.getData()));
	}
	
	
	@ResponseBody
	@RequestMapping(value = URIConstant.EntityQR_GEN_IPG, method = { RequestMethod.POST, RequestMethod.GET }, consumes = {
			MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public Response entityQRCodeFo(HttpServletRequest request) {
		String jsonRequest = request.getParameter("parm");
		logger.info("json request to entityQRCode :" + jsonRequest);
		Request reqObject = (Request) JsonUtility.parseJson(jsonRequest, Request.class);
		if (reqObject == null)
			throw new ApplicationException(ErrorMessages.JSON_FORMAT_ERROR.toString());
		logger.debug(reqObject.getFromEntity() + "|entityQRCode - data :" + reqObject.getData());
		return qrService.entityQRGenIPG(reqObject.getFromEntity(),reqObject.getBankCode(), JsonUtility.convertToJson(reqObject.getData()));
	}
}
