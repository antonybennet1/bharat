package com.wl.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wl.util.constants.ErrorMessages;
import com.wl.util.exceptions.ApplicationException;

public class HttpsClient {

	private static  Logger logger = LoggerFactory.getLogger(HttpsClient.class);
	
	private static HostnameVerifier allHostsValid = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};
	
	private static TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(X509Certificate[] certs,
				String authType) {
		}

		public void checkServerTrusted(X509Certificate[] certs,
				String authType) {
		}

	} };

	public static String send(String uri, String queryString,Map<String, String> headers)
	{
		String response = "";
		URL url;
		try {
			boolean isHttps = uri.startsWith("https");
			logger.debug("Connecting URL"+uri);
			url = new URL(uri);
			HttpURLConnection con=null;
			// Install the all-trusting host verifier
			if(isHttps)
			{
				//System.setProperty("jsse.enableSNIExtension", "false");
				HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid); //comment this line to check hostname and uncomment to BYPASS SAME
				SSLContext sc = SSLContext.getInstance("TLSv1.2");
				//SSLContext sc = SSLContext.getInstance("TLS");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory()); //comment this line for doing certificate checking and uncomment to bypass certificate checking
				con = (HttpsURLConnection)url.openConnection();
				
				//System.setProperty("jsse.enableSNIExtension", "false");
			}
			else
			{
				//Removed proxy for ipg "01061991" i.e aggregator_id as it is internal server
				if(queryString != null){
					if(queryString.contains("01061991")){
						System.out.println(" inside if () Remove ipg Proxy block ");
						con = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
					}else{
						System.out.println("This is not ipg http url inside first else()");
						con = (HttpURLConnection) url.openConnection();
					}
				}else{
					System.out.println("while sending sms query string is already send nothing");
					con = (HttpURLConnection) url.openConnection();
				}
		}
			if(headers!=null)
			{
				for(Entry<String, String> ent : headers.entrySet())
				{
					con.setRequestProperty(ent.getKey(), ent.getValue());
				}
			}
			
			if(queryString!=null)
			{
				logger.debug("sending data :"+queryString);
				con.setRequestMethod("POST");
				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.writeBytes(queryString);
				wr.flush();
				wr.close();
			}
			
			//print_https_cert(con);
			
			int responseCode = con.getResponseCode();
			logger.debug("Http status for request :"+responseCode);
			//System.out.println("Http status for request :"+responseCode);
			response  = getResponse(con);

		} catch (MalformedURLException e) {
			logger.error("Incorrect URL :"+uri,e);
			throw new ApplicationException("Invalid Url");
		} catch (IOException e) {
			logger.error("Exception  while hitting :"+uri,e);
			throw new ApplicationException(ErrorMessages.SERVER_ERROR.toString());
		} catch (KeyManagementException e) {
			logger.error("Exception  while hitting :"+uri,e);
			throw new ApplicationException(ErrorMessages.SERVER_ERROR.toString());
		} catch (NoSuchAlgorithmException e) {
			logger.error("Exception  while hitting :"+uri,e);
			throw new ApplicationException(ErrorMessages.SERVER_ERROR.toString());
		} catch (Exception e) {
			logger.error("Exception  while hitting :"+uri,e);
			throw new ApplicationException(ErrorMessages.SERVER_ERROR.toString());
		}
		return response;
	}
	
	
	private static String  getResponse(HttpURLConnection con) throws IOException{
		StringBuilder response = null;
		String resp = null;
		if(con!=null){
			BufferedReader br =
					new BufferedReader(
							new InputStreamReader(con.getInputStream()));
			response = new StringBuilder();
			String input;
			while ((input = br.readLine()) != null){
				response.append(input);
			}
			br.close();
			resp = response.toString();
			logger.info("Response is :"+resp);
			//System.out.println("Response is :"+resp);
		}
		return resp;
	}
	
	public static void main(String[] args) {
		/*HashMap<String, String> map = new HashMap<>();
		map.put("ClientId", "00031");
		map.put("SealValue", "fc221f92a417a4be2423a0b699950fdb");
		map.put("RequestId", "7219180021301479");
		String data = "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";
		HttpsClient.send("http://10.10.11.102/InstaOnBoard/Create", data, map);*/
		
		 Map<String, String> map= new HashMap<>(); 
		  map.put("Content-type", "application/json"); 
		  //send("https://ipg.in.worldline.com/bharatQrResponse", "{\"txn_currency\":\"356\",\"customer_vpa\":\"9152992797@upi\",\"transaction_type\":\"2\",\"merchant_vpa\":\"051378247760002.mab@pnb\",\"secondary_id\":\"63795797519218242\",\"bank_code\":\"00051\",\"aggregator_id\":\"01061991\",\"primary_id\":\"90511422163795797519218242\",\"ref_no\":\"905114832923\",\"settlement_amount\":\"1.00\",\"mid\":\"051378247760002\",\"tr_id\":\"90511422163795797519218242\",\"txn_amount\":\"1.00\",\"time_stamp\":\"20190220142257\"}", map);
		  
		  send("http://172.16.27.167:7004/ipg/bharatQrResponse", "{\"txn_currency\":\"356\",\"customer_vpa\":\"9152992797@upi\",\"transaction_type\":\"2\",\"merchant_vpa\":\"051378247760002.mab@pnb\",\"secondary_id\":\"63795797519218242\",\"bank_code\":\"00051\",\"aggregator_id\":\"01061991\",\"primary_id\":\"90511422163795797519218242\",\"ref_no\":\"905114832923\",\"settlement_amount\":\"1.00\",\"mid\":\"051378247760002\",\"tr_id\":\"90511422163795797519218242\",\"txn_amount\":\"1.00\",\"time_stamp\":\"20190220142257\"}", map);
		
	}
}
