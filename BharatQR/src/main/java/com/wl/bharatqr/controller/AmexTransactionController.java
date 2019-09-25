package com.wl.bharatqr.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wl.bharatqr.dao.AmexTransactionDao;
import com.wl.bharatqr.model.AmexFields;
import com.wl.bharatqr.model.AmexRefundFields;
import com.wl.bharatqr.model.AmexResponseStatus;
import com.wl.bharatqr.model.AmexReversalFields;
import com.wl.bharatqr.model.GooglePayAggregatorDto;
import com.wl.bharatqr.model.HmacFields;
import com.wl.bharatqr.model.MerchantQRStatus;
import com.wl.bharatqr.service.AmexTransactionService;
import com.wl.bharatqr.serviceimpl.GooglePayAggregator;
import com.wl.bharatqr.util.AmexConstants;
import com.wl.bharatqr.util.AmexFieldsValidation;
import com.wl.util.model.Response;

@RestController
public class AmexTransactionController {
	private static final Logger logger = LoggerFactory.getLogger(AmexTransactionController.class);

	@Autowired
	AmexTransactionService amexTransactionService;
	
	@Autowired
	AmexTransactionDao amexTransactionDao;
	
	
	
	@PostMapping(value = AmexConstants.HMAC_CLIENTID_SECRET, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<String> getClientIdAndSecretForHMAC(@RequestBody HmacFields hmacFields) {
		AmexResponseStatus amexResponseStatus=new AmexResponseStatus();
		AmexFieldsValidation amexFieldsValidation = new AmexFieldsValidation();
		try{
		String status=amexFieldsValidation.emailValidation(hmacFields);
		if(!amexFieldsValidation.getPropValues("success").equalsIgnoreCase(status)){
			logger.error("EmailId Field Validation Failed.");
			amexResponseStatus.setStatus_code(status);
			amexResponseStatus.setStatus_code_type(amexFieldsValidation.getPropValues("mandatory_data"));
			amexResponseStatus.setDescription(amexFieldsValidation.getPropValues("validation_failed"));
			return new ResponseEntity<String>(new Gson().toJson(amexResponseStatus), HttpStatus.CONFLICT);
		}
		String response=amexTransactionService.getClientIdAndSecret(hmacFields);
		if(!amexFieldsValidation.getPropValues("success").equalsIgnoreCase(response)){
			amexResponseStatus.setStatus_code(amexFieldsValidation.getPropValues("failed"));
			amexResponseStatus.setStatus_code_type(amexFieldsValidation.getPropValues("api_error"));
			amexResponseStatus.setDescription(amexFieldsValidation.getPropValues("generating_failed"));
			return new ResponseEntity<String>(new Gson().toJson(amexResponseStatus), HttpStatus.CONFLICT);
		}
		}catch(Exception e){
			amexResponseStatus.setStatus_code(amexFieldsValidation.getPropValues("failed"));
			amexResponseStatus.setStatus_code_type(amexFieldsValidation.getPropValues("api_error"));
			amexResponseStatus.setDescription(amexFieldsValidation.getPropValues("system_error"));
			return new ResponseEntity<String>(new Gson().toJson(amexResponseStatus), HttpStatus.CONFLICT);
		}
		amexResponseStatus.setStatus_code(amexFieldsValidation.getPropValues("success"));
		amexResponseStatus.setStatus_code_type(amexFieldsValidation.getPropValues("mail_sent"));
		amexResponseStatus.setDescription(amexFieldsValidation.getPropValues("check_mail"));
		return new ResponseEntity<String>(new Gson().toJson(amexResponseStatus), HttpStatus.OK);
	}
	
	

	@PostMapping(value = AmexConstants.AMEX_NOTIFICATION, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<String> amexNotificationAPI(@RequestHeader HttpHeaders headers, @RequestBody AmexFields amexFields) throws NoSuchAlgorithmException {
		logger.info("Calling Amex Notification API..");
		AmexResponseStatus amexResponseStatus = new AmexResponseStatus();
		AmexFieldsValidation amexFieldsValidation = new AmexFieldsValidation();
		String object=new Gson().toJson(amexFields);
		try{
		List<String> authorization=headers.get("Authorization");
		String hamcDetails=authorization.get(0);
		String[] usernameAndHmac=hamcDetails.split(":");
		String clientId=usernameAndHmac[0];
		String hamcFromHeader=usernameAndHmac[1];
		List<String> dateArray=headers.get("date");
		String date=dateArray.get(0);
		
		String status = amexFieldsValidation.fieldsValidation(amexFields);
		if (!amexFieldsValidation.getPropValues("success").equalsIgnoreCase(status)) {
			logger.error("Amex Notification Fields Validation Failed.");
			amexResponseStatus.setStatus_code(status);
			amexResponseStatus.setStatus_code_type(amexFieldsValidation.getPropValues("mandatory_data"));
			amexResponseStatus.setDescription(amexFieldsValidation.getPropValues("validation_failed"));
			return new ResponseEntity<String>(new Gson().toJson(amexResponseStatus), HttpStatus.CONFLICT);
		}
		
		List<Map<String, Object>> authorizationDetails=amexTransactionDao.getAuthorizationDetails(clientId);
		String secret=null;
		if(authorizationDetails.size()>0){
		for(Map<String, Object> authorizationDetail:authorizationDetails){
				secret=authorizationDetail.get("secret").toString();
		}
		}else{
			logger.error("Invalid ClientID..");
			amexResponseStatus.setStatus_code(amexFieldsValidation.getPropValues("failed"));
			amexResponseStatus.setStatus_code_type(amexFieldsValidation.getPropValues("failed"));
			amexResponseStatus.setDescription(amexFieldsValidation.getPropValues("invalid_clientid"));
			return new ResponseEntity<String>(new Gson().toJson(amexResponseStatus), HttpStatus.CONFLICT);
		}
		
		HttpPost post = new HttpPost(amexFieldsValidation.getPropValues("notification_url"));
		StringEntity data = new StringEntity(object,ContentType.create(amexFieldsValidation.getPropValues("conten_type"),"UTF-8"));
		post.setEntity(data);
		String verb = post.getMethod();
		HMACClient hmacClient=new HMACClient();
		String contentMd5 = hmacClient.calculateMD5(object);
			
		String toSign = verb + "\n" + contentMd5 + "\n"
				+ data.getContentType().getValue() + "\n" + date + "\n"
				+ post.getURI().getPath();
		
		String hmac = hmacClient.calculateHMAC(secret, toSign);

		if(!hmac.equals(hamcFromHeader)){
			logger.error("Unauthorized Access..");
			amexResponseStatus.setStatus_code(amexFieldsValidation.getPropValues("failed"));
			amexResponseStatus.setStatus_code_type(amexFieldsValidation.getPropValues("failed"));
			amexResponseStatus.setDescription(amexFieldsValidation.getPropValues("unauthorized_access"));
			return new ResponseEntity<String>(new Gson().toJson(amexResponseStatus), HttpStatus.CONFLICT);
		}
		}catch(Exception e){
			logger.error("Unauthorized Access..");
			amexResponseStatus.setStatus_code(amexFieldsValidation.getPropValues("failed"));
			amexResponseStatus.setStatus_code_type(amexFieldsValidation.getPropValues("failed"));
			amexResponseStatus.setDescription(amexFieldsValidation.getPropValues("unauthorized_access"));
			return new ResponseEntity<String>(new Gson().toJson(amexResponseStatus), HttpStatus.CONFLICT);
		}
		amexResponseStatus = (AmexResponseStatus) amexTransactionService.amexNotification(amexFields);
		return new ResponseEntity<String>(new Gson().toJson(amexResponseStatus), HttpStatus.OK);
	}

	@PostMapping(value = AmexConstants.AMEX_REVERSAL, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<String> amexReversalAPI(@RequestHeader HttpHeaders headers,
			@RequestBody AmexReversalFields amexReversalFields) {
		logger.info("Calling Amex Reversal API..");
		AmexFieldsValidation amexFieldsValidation = new AmexFieldsValidation();
		String status = amexFieldsValidation.amexReversalValidation(amexReversalFields);
		AmexResponseStatus amexResponseStatus = new AmexResponseStatus();
		JsonObject jsonresponse = null;
		try {
			if (!amexFieldsValidation.getPropValues("success").equalsIgnoreCase(status)) {
				logger.error("Amex Reversal Fields Validation Failed.");
				amexResponseStatus.setStatus_code(status);
				amexResponseStatus.setStatus_code_type(amexFieldsValidation.getPropValues("mandatory_data"));
				amexResponseStatus.setDescription(amexFieldsValidation.getPropValues("validation_failed"));
				return new ResponseEntity<String>(new Gson().toJson(amexResponseStatus), HttpStatus.CONFLICT);
			}
			jsonresponse = amexTransactionService.amexReversal(amexReversalFields);
			if (!amexFieldsValidation.getPropValues("success").equals(jsonresponse.get("status"))) {
				logger.error("Amex Reversal Response Failed..");
				amexResponseStatus.setStatus_code(amexFieldsValidation.getPropValues("failed"));
				amexResponseStatus.setStatus_code_type(amexFieldsValidation.getPropValues("reversal_failed"));
				amexResponseStatus.setDescription(jsonresponse.toString());
				return new ResponseEntity<String>(new Gson().toJson(amexResponseStatus), HttpStatus.CONFLICT);
			}
		} catch (Exception ex) {
			logger.error("Amex Reversal Failed:" + ex);
			amexResponseStatus.setStatus_code(amexFieldsValidation.getPropValues("failed"));
			amexResponseStatus.setStatus_code_type(amexFieldsValidation.getPropValues("api_error"));
			amexResponseStatus.setDescription(amexFieldsValidation.getPropValues("reversal_failed"));
			return new ResponseEntity<String>(new Gson().toJson(amexResponseStatus), HttpStatus.CONFLICT);
		}
		logger.info("Amex Reversal API Executed Successfully..");
		amexResponseStatus.setStatus_code(amexFieldsValidation.getPropValues("success"));
		amexResponseStatus.setStatus_code_type(amexFieldsValidation.getPropValues("reversal_success_message"));
		amexResponseStatus.setDescription(jsonresponse.toString());
		return new ResponseEntity<String>(new Gson().toJson(amexResponseStatus), HttpStatus.OK);
	}

	@PostMapping(value = AmexConstants.AMEX_REFUND, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<String> amexRefundAPI(@RequestHeader HttpHeaders headers,
			@RequestBody AmexRefundFields amexRefundFields) {
		logger.info("Calling Amex Refund API..");
		AmexFieldsValidation amexFieldsValidation = new AmexFieldsValidation();
		String status = amexFieldsValidation.amexRefundValidation(amexRefundFields);
		AmexResponseStatus amexResponseStatus = new AmexResponseStatus();
		JsonObject jsonresponse = null;
		try {
			if (!amexFieldsValidation.getPropValues("success").equalsIgnoreCase(status)) {
				logger.error("Amex Refund Fields Validation Failed.");
				amexResponseStatus.setStatus_code(status);
				amexResponseStatus.setStatus_code_type(amexFieldsValidation.getPropValues("mandatory_data"));
				amexResponseStatus.setDescription(amexFieldsValidation.getPropValues("validation_failed"));
				return new ResponseEntity<String>(new Gson().toJson(amexResponseStatus), HttpStatus.CONFLICT);
			}
			jsonresponse = amexTransactionService.amexRefund(amexRefundFields);
			if (!amexFieldsValidation.getPropValues("success").equals(jsonresponse.get("status"))) {
				logger.error("Amex Refund Response Failed..");
				amexResponseStatus.setStatus_code(amexFieldsValidation.getPropValues("failed"));
				amexResponseStatus.setStatus_code_type(amexFieldsValidation.getPropValues("refund_failed"));
				amexResponseStatus.setDescription(jsonresponse.toString());
				return new ResponseEntity<String>(new Gson().toJson(amexResponseStatus), HttpStatus.CONFLICT);
			}
		} catch (Exception ex) {
			logger.error("Amex Refund Failed:" + ex);
			amexResponseStatus.setStatus_code(amexFieldsValidation.getPropValues("failed"));
			amexResponseStatus.setStatus_code_type(amexFieldsValidation.getPropValues("api_error"));
			amexResponseStatus.setDescription(amexFieldsValidation.getPropValues("refund_failed"));
			return new ResponseEntity<String>(new Gson().toJson(amexResponseStatus), HttpStatus.CONFLICT);
		}
		logger.info("Amex Refund API Executed Successfully..");
		amexResponseStatus.setStatus_code(amexFieldsValidation.getPropValues("success"));
		amexResponseStatus.setStatus_code_type(amexFieldsValidation.getPropValues("refund_success_message"));
		amexResponseStatus.setDescription(jsonresponse.toString());
		return new ResponseEntity<String>(new Gson().toJson(amexResponseStatus), HttpStatus.OK);
	}

	@PostMapping(value = "/googlepay", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public String googleApi(@RequestBody GooglePayAggregatorDto googlePayAggregatorDto) throws IOException {
		GooglePayAggregator googlePayAggregator = new GooglePayAggregator();
		JsonObject googlePayStatus = googlePayAggregator.googlePayNotification(googlePayAggregatorDto);
		return googlePayStatus.toString();
	}
	
	@RequestMapping(value = "/merchant/characteristics/v1/qr/status", method = RequestMethod.GET)
	@ResponseBody
	public Response amexQRStatusAPI(@RequestBody MerchantQRStatus merchantQRStatus)
			throws IOException {
		logger.info("calling AMEX QA STATUS API.. ");
		Response response = new Response();
		
		
		return null;
		}

}
