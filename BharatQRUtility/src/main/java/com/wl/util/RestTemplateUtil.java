package com.wl.util;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


public class RestTemplateUtil {

	private static Logger logger = LoggerFactory.getLogger(RestTemplateUtil.class);

	
	public static String sendPostRequest(String url, String queryString, HttpHeaders headers){
		
		// for Proxy setting 
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		logger.debug("requestFactory:"+requestFactory);
		/*Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress("10.10.15.200", 8080));
		requestFactory.setProxy(proxy);*/
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(requestFactory);  
		//end for proxy		
		logger.debug("restTemplate:"+restTemplate);

		//setting content type and input
		HttpEntity<String> entity = new HttpEntity<String>(queryString ,headers);
		logger.debug("entity:"+entity);
		String response = restTemplate.postForObject(url, entity, String.class);
		return response;		
	}
	public static void main(String[] args) {
		
		if("rest".equals(args[0]))
		{
			HttpHeaders map = new HttpHeaders();
			map.add("ClientId", "00031");
			map.add("SealValue", "f729e426e70aef5f6cd0b86d0609e5e2");
			map.add("RequestId", "7222104557421994");
			String data = "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";
			String str = RestTemplateUtil.sendPostRequest("http://10.10.11.102/InstaOnBoard/Create", data , map);
			System.out.println(str);
		}
		else
		{
			HashMap<String,String> map = new HashMap<String,String>(4);
			map.put("ClientId", "00031");
			map.put("SealValue", "f729e426e70aef5f6cd0b86d0609e5e2");
			map.put("RequestId", "7222104557421994");
			String data = "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";
			String str = HttpsClient.send("http://10.10.11.102/InstaOnBoard/Create", data , map);
			System.out.println(str);
		}
		
		}
}
