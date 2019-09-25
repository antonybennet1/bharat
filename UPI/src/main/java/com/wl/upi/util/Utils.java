package com.wl.upi.util;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.text.DecimalFormat;
import java.util.Random;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONArray;
import org.json.JSONObject;

public class Utils {
	
	public Object populateHttpOrHttpsJsonObject(JSONObject requestObject, String url, String methodType,
			JSONArray requestArray) {

		JSONObject responseObject = new JSONObject();

		if (url.contains("https")) {
			HttpsURLConnection con = null;

			try {
				SSLContext ssl_ctx = SSLContext.getInstance("TLS");
				TrustManager[] trust_mgr = get_trust_mgr();
				ssl_ctx.init(null, // key manager
						trust_mgr, // trust manager
						new SecureRandom()); // random number generator
				HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());

				HostnameVerifier allHostsValid = new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) {
						return false;
					}
				};

				// Install the all-trusting host verifier
				HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

				URL urlObj = new URL(url);
				con = (HttpsURLConnection) urlObj.openConnection();
				con.setRequestMethod(methodType);
				if ("POST".equalsIgnoreCase(methodType)) {
					con.setDoOutput(true);
					con.setDoInput(true);
					con.setRequestProperty("Content-Type", "application/json");
					con.setRequestProperty("Accept", "application/json");
					BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
					if (null != requestObject)
						wr.write(requestObject.toString());
					else if (null != requestArray)
						wr.write(requestArray.toString());
					wr.flush();
				}
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				String responseString = response.toString();
				System.out.println(responseString);
			}
			 catch (Exception e) {
				//logger.error("[Utils][getHttpsConnection] method :  URL : " + url, e);
			}
		} else {
			try {
				URL urlObj = new URL(url);
				HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
				con.setRequestMethod(methodType);
				if ("POST".equalsIgnoreCase(methodType)) {
					con.setDoOutput(true);
					con.setDoInput(true);
					con.setRequestProperty("Content-Type", "application/json");
					con.setRequestProperty("Accept", "application/json");
					BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
					if (null != requestObject)
						wr.write(requestObject.toString());
					else if (null != requestArray)
						wr.write(requestArray.toString());
					wr.flush();
				}
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				String responseString = response.toString();
				System.out.println(responseString);
			} catch (Exception e) {
				
			}
		}
		return responseObject;
   }
	private TrustManager[] get_trust_mgr() {
		TrustManager[] certs = new TrustManager[] { new X509TrustManager() {
			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
					throws CertificateException {
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
					throws CertificateException {
			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		} };
		return certs;
	}
	public String sendSMS(String mobile_number, String text) {
		//String returnObject = new ReturnObject();
		Object jsonObject = null;
		System.out.println("Mbl :" + mobile_number);
		System.out.println("text :" + text);
		String url = "http://182.18.180.27:8080/esbsample/json/sendSMS?mobile_number=" + mobile_number + "&content="
				+ text;
		try {
			System.out.println("Url :" + url);
			jsonObject = new Utils().populateHttpOrHttpsJsonObject(null, url, "GET", null);
			System.out.println("Response :" + jsonObject.toString());
			/*returnObject.setReturnCode(0);
			returnObject.setReturnMessage("Success");
			returnObject.setObject(jsonObject);*/
			return "Success";
		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		}
		//return returnObject;
	}
	
	public String generateRandomNumber(int size) {

        Random random = new Random();

        StringBuilder PIN = new StringBuilder("");

        for (int index = 0; index < size; index++) {

                int randomInteger = random.nextInt(size);

                PIN.append(randomInteger);

        }

        return PIN.toString();

}




}