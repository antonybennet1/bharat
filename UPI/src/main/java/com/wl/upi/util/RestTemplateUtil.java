package com.wl.upi.util;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


public class RestTemplateUtil {

	private static Logger logger = LoggerFactory.getLogger(RestTemplateUtil.class);
	
	private static boolean proxyEnabled;
	
	/**
	 * @return the proxyEnabled
	 */
	public static boolean isProxyEnabled() {
		return proxyEnabled;
	}
	/**
	 * @param proxyEnabled the proxyEnabled to set
	 */
	public static void setProxyEnabled(boolean proxyEnabled) {
		RestTemplateUtil.proxyEnabled = proxyEnabled;
	}
	public static String sendPostRequest(String url, String jsonInput){
		
		// for Proxy setting 
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		if(proxyEnabled)
		{
			Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress("10.10.15.200", 443));
			requestFactory.setProxy(proxy);
		}
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(requestFactory);  
		//end for proxy		

		//setting content type and input
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);		
		HttpEntity<String> entity = new HttpEntity<String>(jsonInput ,headers);
		String response = restTemplate.postForObject(url, entity, String.class);
		return response;
		
	}
	public static void main(String[] args) {
		String str = RestTemplateUtil.sendPostRequest("https://172.16.26.116:8443/bharatqr/upi/callback/CANARAPSP", "{ \"merchId\":\"CollectTest\", \"merchChanId\":\"CollectApp\", \"unqTxnId\":\"SA95UZdplklsfssdjkklsjfstrurig3yua707k124dyQh29\", \"mobNo\":\"919773187515\", \"txnRefundAmount\":\"1.0\", \"txnRefundId\":\"SA95UZdplkfhhgjlsfssdjslkfstrurig3yua707k124duyQh57\", \"refundReason\":\"not insterested\", \"sId\":\"123\", \"checkSum\": \"84CE785404141FC7412232D6D4D6E80309BA11B2CA909590F72F4405512C9FC8E3B306DA241AFE4D8194AD04ECC4599AF4252473E52DEBCA462D8A43740A55C24EB478269C924C0FEC8883DDCF334F13C7A3AB665FD87B441D5508CC0B8FF3476E09A14D15046727CD5BAD62CB235CB90F17B7335AAB4A69A6DCA75B8DB296A0D2FB086ACA84BCC7ECA15B088F62B82112B7CD6950E4C75CBB0BFC0D20C09AC75A879B2E0553987A8CD708E4BBD014F5F0C3D5B28D8A64A56F578E4F900FDAE0BDE6753C5C4830E07D284B7B9C809B90F7B2DE929426671284D3C41BD9A2BFA67BF78F0F3D17B217D2D764D1EC5FB385EBFE19084A742D3BD7DD6CC072C30BAD\" }");
		System.out.println(str);
		}
}
