package com.wl.bharatqr.serviceimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Charsets;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wl.bharatqr.model.GooglePayAggregatorDto;
@Service
public class GooglePayAggregator {
	private static final String JWT_PATH = "G:/SVN_Project/BharatQr_UAT/bharatqr-application/BharatQR/src/main/resources/service-account-key.json";
	private static final String API_SCOPE = "https://www.googleapis.com/auth/nbupayments";
	private static final String UAT_API_URL = "https://staging-nbupayments.googleapis.com/v1/upi/merchantcardtransactioninfo:update";
	public static final Charset UTF8 = Charset.forName("UTF-8");
	public static final int HTTP_TIMEOUT_MS = 10000;
	
	public static final Logger logger=LoggerFactory.getLogger(GooglePayAggregator.class);
	
	public JsonObject googlePayNotification(GooglePayAggregatorDto googlePayAggregatorDto) throws IOException{
		logger.info("Inside googlePayNotifcation method..");
		HttpTransport httpTransport;
		HttpRequestFactory httpRequestFactory;
		HttpResponse httpResponse=null;
		JsonObject response=null;
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			GoogleCredential credential = getGoogleCredential(httpTransport);
			httpRequestFactory = httpTransport.createRequestFactory(credential);
			GenericUrl url = new GenericUrl(UAT_API_URL);
			HttpRequest httpRequest = httpRequestFactory.buildPostRequest(url, getContent(googlePayAggregatorDto));
			
			httpRequest.setReadTimeout(HTTP_TIMEOUT_MS).setThrowExceptionOnExecuteError(false);
			httpResponse = httpRequest.execute();
			String responseContent =CharStreams.toString(new InputStreamReader(httpResponse.getContent(), UTF8)); 
			response = new JsonParser().parse(responseContent).getAsJsonObject();
			
		} catch (GeneralSecurityException e) {
			logger.error("Error in posting data:"+e);
			return response;
		}catch(IOException ex){
			logger.error("Error in posting data:"+ex);
			return response;
		}
		return response;
	}
	
	/** Creates a {@link HttpContent} from the json file. */
	private static HttpContent getContent(GooglePayAggregatorDto googlePayAggregatorDto) {
		HttpContent httpContent = new ByteArrayContent("application/json",
			      new Gson().toJson(googlePayAggregatorDto).getBytes(Charsets.UTF_8));
		return httpContent;
	}
	
	/**
	 * Creates a new credential using the service account json file and Oauth scope.
	 */
	private static GoogleCredential getGoogleCredential(HttpTransport httpTransport)
			throws FileNotFoundException, IOException {
		return GoogleCredential
				.fromStream(new FileInputStream(new File(JWT_PATH)), httpTransport, JacksonFactory.getDefaultInstance())
				.createScoped(Collections.singleton(API_SCOPE));
	}
	
	

}
