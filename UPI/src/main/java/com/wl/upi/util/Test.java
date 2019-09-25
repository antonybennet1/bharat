package com.wl.upi.util;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Test {

	public static void main(String[] args) {

		Map<String, String> map = new HashMap<>();
		map.put("Content-type", "application/json");
		System.out.println("Content-Type : application/json");

		System.out.println("url : http://172.16.27.223:7004/bharatQrResponse");
		System.out.println("Sending data");
		System.out.println(
				"{\"txn_currency\":\"356\",\"customer_vpa\":\"9152992797@upi\",\"transaction_type\":\"2\",\"merchant_vpa\":\"051378247760000.mab@pnb\",\"secondary_id\":\"63795797519218242\",\"bank_code\":\"00051\",\"aggregator_id\":\"01061991\",\"primary_id\":\"90511422163795797519218111\",\"ref_no\":\"111111111111\",\"settlement_amount\":\"1.00\",\"mid\":\"051378247760002\",\"tr_id\":\"90511422163795797519218111\",\"txn_amount\":\"1.00\",\"time_stamp\":\"20190220142257\",\"tid\":\"12345678\"}");
		
		send("http://172.16.27.223:7004/bharatQrResponse",
				"{\"txn_currency\":\"356\",\"customer_vpa\":\"9152992797@upi\",\"transaction_type\":\"2\",\"merchant_vpa\":\"051378247760000.mab@pnb\",\"secondary_id\":\"63795797519218242\",\"bank_code\":\"00051\",\"aggregator_id\":\"01061991\",\"primary_id\":\"90511422163795797519218111\",\"ref_no\":\"111111111111\",\"settlement_amount\":\"1.00\",\"mid\":\"051378247760002\",\"tr_id\":\"90511422163795797519218111\",\"txn_amount\":\"1.00\",\"time_stamp\":\"20190220142257\",\"tid\":\"12345678\"}",
				map);
	}

	public static String send(String uri, String queryString, Map<String, String> headers) {
		System.out.println("Starting send method");
		StringBuffer response = new StringBuffer();
		URL url;
		try {
			boolean isHttps = uri.startsWith("https");
			url = new URL(uri);
			HttpURLConnection con = null;
			// Install the all-trusting host verifier
			if (isHttps) {
				// System.setProperty("jsse.enableSNIExtension", "false");
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

				// HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
				// //comment this line to check hostname and uncomment to BYPASS
				// SAME
				// SSLContext sc = SSLContext.getInstance("TLSv1.2");
				// sc.init(null, trustAllCerts, new
				// java.security.SecureRandom());
				// HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				// //comment this line for doing certificate checking and
				// uncomment to bypass certificate checking
				con = (HttpsURLConnection) url.openConnection();

				// System.setProperty("jsse.enableSNIExtension", "false");
			} else {
				con = (HttpURLConnection) url.openConnection();
			}
			if (headers != null) {
				for (Entry<String, String> ent : headers.entrySet()) {
					con.setRequestProperty(ent.getKey(), ent.getValue());
				}
			}

			if (queryString != null) {
				con.setRequestMethod("POST");
				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				System.out.println("Request : " + queryString);
				wr.writeBytes(queryString);
				wr.flush();
				wr.close();
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print_https_cert(con);

			// System.out.println("Http status for request :"+responseCode);
			// response = getResponse(con);
			// System.out.println(response.toString());
			System.out.println("Response : " + response.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Ending send method");
		return response.toString();
	}

	private static TrustManager[] get_trust_mgr() {
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

}
