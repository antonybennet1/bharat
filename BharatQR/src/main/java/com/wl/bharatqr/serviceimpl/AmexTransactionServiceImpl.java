package com.wl.bharatqr.serviceimpl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.Charsets;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wl.bharatqr.dao.AmexTransactionDao;
import com.wl.bharatqr.model.AmexFields;
import com.wl.bharatqr.model.AmexRefundFields;
import com.wl.bharatqr.model.AmexResponseStatus;
import com.wl.bharatqr.model.AmexReversalFields;
import com.wl.bharatqr.model.HmacFields;
import com.wl.bharatqr.service.AmexTransactionService;
import com.wl.bharatqr.util.AmexFieldsValidation;

@Service
public class AmexTransactionServiceImpl implements AmexTransactionService {
	private static Logger logger = LoggerFactory.getLogger(AmexTransactionServiceImpl.class);
	@Autowired
	AmexTransactionDao amexTransactionDao;

	AmexFieldsValidation amexFieldsValidation = new AmexFieldsValidation();

	@Override
	public Object amexNotification(AmexFields amexFields) {
		logger.info("inside AmexTransactionServiceImpl>>");
		AmexResponseStatus amexResponseStatus = new AmexResponseStatus();
		amexResponseStatus.setStatus_code(amexFieldsValidation.getPropValues("failed"));
		amexResponseStatus.setStatus_code_type(amexFieldsValidation.getPropValues("failed"));

		try {
			String status = amexTransactionDao.amexNotificationDao(amexFields);
			if (amexFieldsValidation.getPropValues("success").equalsIgnoreCase(status)) {
				logger.info("Amex Notification Inserted Successfully!!");
				amexResponseStatus.setStatus_code(amexFieldsValidation.getPropValues("success"));
				amexResponseStatus.setStatus_code_type(amexFieldsValidation.getPropValues("success"));
				amexResponseStatus.setDescription(amexFieldsValidation.getPropValues("notification_message_success"));
				return amexResponseStatus;
			}
		} catch (Exception e) {
			logger.error("Records inserting Amex Transaction Detail:" + e);
			amexResponseStatus.setStatus_code(amexFieldsValidation.getPropValues("api_error"));
			amexResponseStatus.setStatus_code_type(amexFieldsValidation.getPropValues("system_error"));
			amexResponseStatus.setDescription(amexFieldsValidation.getPropValues("notification_message_error"));
			return amexResponseStatus;
		}
		amexResponseStatus.setDescription(amexFieldsValidation.getPropValues("notification_message_failed"));
		logger.info("Amex Notification Insertion Failed..");
		return amexResponseStatus;
	}

	@Override
	public JsonObject amexReversal(AmexReversalFields amexReversalFields) {
		logger.info("Inside amex Reversal method..");
		HttpTransport httpTransport;
		HttpRequestFactory httpRequestFactory;
		HttpResponse httpResponse = null;
		JsonObject response = null;
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			// GoogleCredential credential = getGoogleCredential(httpTransport);
			httpRequestFactory = httpTransport.createRequestFactory();
			GenericUrl url = new GenericUrl("");
			HttpRequest httpRequest = httpRequestFactory.buildPostRequest(url, getReversalContent(amexReversalFields));

			httpRequest.setReadTimeout(GooglePayAggregator.HTTP_TIMEOUT_MS).setThrowExceptionOnExecuteError(false);
			httpResponse = httpRequest.execute();
			String responseContent = CharStreams
					.toString(new InputStreamReader(httpResponse.getContent(), GooglePayAggregator.UTF8));
			response = new JsonParser().parse(responseContent).getAsJsonObject();

		} catch (GeneralSecurityException e) {
			logger.error("Error in Reversal posting data:" + e);
			return response;
		} catch (IOException ex) {
			logger.error("Error in Reversal posting data:" + ex);
			return response;
		}
		return response;
	}

	private static HttpContent getReversalContent(AmexReversalFields amexReversalFields) {
		HttpContent httpContent = new ByteArrayContent("application/json",
				new Gson().toJson(amexReversalFields).getBytes(Charsets.UTF_8));
		return httpContent;
	}

	@Override
	public JsonObject amexRefund(AmexRefundFields amexRefundFields) {
		logger.info("Inside amex Refund method..");
		HttpTransport httpTransport;
		HttpRequestFactory httpRequestFactory;
		HttpResponse httpResponse = null;
		JsonObject response = null;
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			// GoogleCredential credential = getGoogleCredential(httpTransport);
			httpRequestFactory = httpTransport.createRequestFactory();
			GenericUrl url = new GenericUrl("");
			HttpRequest httpRequest = httpRequestFactory.buildPostRequest(url, getRefundContent(amexRefundFields));

			httpRequest.setReadTimeout(GooglePayAggregator.HTTP_TIMEOUT_MS).setThrowExceptionOnExecuteError(false);
			httpResponse = httpRequest.execute();
			String responseContent = CharStreams
					.toString(new InputStreamReader(httpResponse.getContent(), GooglePayAggregator.UTF8));
			response = new JsonParser().parse(responseContent).getAsJsonObject();

		} catch (GeneralSecurityException e) {
			logger.error("Error in Refund posting data:" + e);
			return response;
		} catch (IOException ex) {
			logger.error("Error in Refund posting data:" + ex);
			return response;
		}
		return response;
	}

	private static HttpContent getRefundContent(AmexRefundFields amexRefundFields) {
		HttpContent httpContent = new ByteArrayContent("application/json",
				new Gson().toJson(amexRefundFields).getBytes(Charsets.UTF_8));
		return httpContent;
	}

	@Override
	public String getClientIdAndSecret(HmacFields hmacFields) {
		List<Map<String, Object>> authorizationDetails=amexTransactionDao.isMailIdAlreadyExist(hmacFields);
		String secret=null;
		String clientId=null;
		String emailId=null;
		if(authorizationDetails.size()>0){
		for(Map<String, Object> authorizationDetail:authorizationDetails){
				secret=authorizationDetail.get("secret").toString();
				clientId=authorizationDetail.get("clientId").toString();
				emailId=authorizationDetail.get("emailId").toString();
		}
		}else{
			String[] emailIdArray=hmacFields.getEmailId().split("@");
			clientId=emailIdArray[0]+RandomStringUtils.random(5,"0123456789abcdef");
			 secret = RandomStringUtils.random(8,"0123456789abcdef");
			 hmacFields.setClientId(clientId);
			 hmacFields.setSecret(secret);
			 String status=amexTransactionDao.insertClientIdAndSecret(hmacFields);
			 if(!amexFieldsValidation.getPropValues("success").equals(status)){
				 return amexFieldsValidation.getPropValues("failed");
			 }
		}
		return amexFieldsValidation.getPropValues("success");
	}
}
