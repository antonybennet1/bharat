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
import com.wl.qr.dao.QrDao;
import com.wl.qr.service.QRService;
import com.wl.util.model.Response;

@RestController
public class QRCodeController {

	private static final Logger logger = LoggerFactory.getLogger(QRCodeController.class);

	@Autowired
	private QRService qrService;

	@Autowired
	@Qualifier("qrDao")
	private QrDao qrDao;

	@ResponseBody
	@RequestMapping(value = URIConstant.BHARATQR_GEN, method = { RequestMethod.POST, RequestMethod.GET }, consumes = {
			MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public Response bharatQRCode(HttpServletRequest request) {
		String jsonRequest = request.getParameter("data");
		String fromEntity = request.getParameter("fromEntity");
		logger.info("json request to qrservice :" + jsonRequest);
		return qrService.getQR(fromEntity, jsonRequest);
	}

	@ResponseBody
	@RequestMapping(value = URIConstant.UPIQR_GEN, method = { RequestMethod.POST, RequestMethod.GET }, consumes = {
			MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public Response upiQRCode(HttpServletRequest request) {
		String jsonRequest = request.getParameter("data");
		logger.info("json request to qrservice :" + jsonRequest);
		String fromEntity = request.getParameter("fromEntity");
		return qrService.getUpiQR(fromEntity, jsonRequest);
	}

}
