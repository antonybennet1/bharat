package com.wl.bharatqr.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.util.Base64;
import com.wl.bharatqr.util.AmexFieldsValidation;

public class HMACClient {

	private final static String DATE_FORMAT = "EEE, d MMM yyyy HH:mm:ss z";
	private final static String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	
	private final static String SECRET = "secretsecret";
	private final static String USERNAME = "jos";
	
	private static final Logger LOG = LoggerFactory.getLogger(HMACClient.class);

	public static void main(String[] args) throws HttpException, IOException, NoSuchAlgorithmException {
		HMACClient client = new HMACClient();
		client.makeHTTPCallUsingHMAC(USERNAME);
	}

	public void makeHTTPCallUsingHMAC(String username) throws HttpException, IOException, NoSuchAlgorithmException {
		String contentToEncode = "{\"type\":\"type1\",\"primary_account_number\":\"1234\",\"full_pan\":\"123\",\"expiry_date\":\"1212\",\"processing_code\":\"321\",\"transaction_amount\":\"000000000010\",\"tip_amount\":\"000000000012\",\"transaction_time\":\"123450\",\"system_trace_number\":\"78890\",\"transaction_time_local\":\"12233\",\"reconciliation_date\":\"121212\",\"acquirer_reference_data\":\"acquirer data\",\"retrieval_reference_number\":\"123\",\"acquirer_id\":\"123\",\"approval_code\":\"234\",\"action_code\":\"111\",\"card_acceptor_id\":\"123456789012345\",\"card_acceptor_id_type\":\"SE\",\"card_acceptor_name\":\"vel\",\"currency_code\":\"234\",\"pos_dc\":\"324\",\"additional_data\":{\"name\":\"vel\",\"value\":\"10\"}}";
		String contentType = "application/json";
//		String contentType = "application/vnd.geo.comment+json";
		//String contentType = "text/plain";
		String currentDate = new SimpleDateFormat(DATE_FORMAT).format(new Date());
		AmexFieldsValidation amexFieldsValidation = new AmexFieldsValidation();
		HttpPost post = new HttpPost(amexFieldsValidation.getPropValues("notification_url"));
//		StringEntity data = new StringEntity(contentToEncode,"UTF-8");//,"UTF-8"
		StringEntity data = new StringEntity(contentToEncode,ContentType.create(contentType,"UTF-8"));
		post.setEntity(data);
		
		String verb = post.getMethod();
		String contentMd5 = calculateMD5(contentToEncode);
		String toSign = verb + "\n" + contentMd5 + "\n"
				+ data.getContentType().getValue() + "\n" + currentDate + "\n"
				+ post.getURI().getPath();
		
		String hmac = calculateHMAC(SECRET, toSign);

		post.addHeader("Authorization", username + ":" + hmac);
		post.addHeader("Date", currentDate);
		post.addHeader("content-type", "application/json");
		post.addHeader("accept-language", "en-us");
		post.addHeader("content-encoding", "UTF-8");
		

		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(post);
		
		System.out.println("client response:" + response.getStatusLine().getStatusCode());
		System.out.println("response:" + response.toString());
	}

	public String calculateHMAC(String secret, String data) {
		try {
			SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(),	HMAC_SHA1_ALGORITHM);
			Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(data.getBytes());
			String result = new String(Base64.encodeBase64(rawHmac));
			return result;
		} catch (GeneralSecurityException e) {
			LOG.warn("Unexpected error while creating hash: " + e.getMessage(),	e);
			throw new IllegalArgumentException();
		}
	}
	
	public String calculateMD5(String contentToEncode) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		digest.update(contentToEncode.getBytes());
		String result = new String(Base64.encodeBase64(digest.digest()));
		return result;
	}
}

