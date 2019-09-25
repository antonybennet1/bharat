/*package com.wl.qr.service;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wl.util.JsonUtility;
import com.wl.util.model.Response;

public class TestQRService {

	private ApplicationContext context;
	private QRService qrService;

	@Before
	public void setUp() throws Exception {
		context = new ClassPathXmlApplicationContext("qr-spring.xml");
		qrService = (QRService) context.getBean("qrService");
	}

	@Test
	@Ignore
	public void testGetQR() {
		//fail("Not yet implemented");
		String data = "{'bankCode':'00001','pointOfInitiation':'12','visaMpan1':'4604905058123456','visaMpan2':'4604905058123456','masterMpan1':'560490505812345','masterMpan2':'560490505812345','rupayMpan1':'560490505812345','rupayMpan2':'560490505812345','ifscAccountNumber':'123456789012345678901234567','vpa':'a@a','mam':'12.00','upiUrl':'upi://abc','aadharNumber':'abcd','mcc':'1111','currencyCode':'356','amount':'10.00','tipConvIndicator':'01','convAmount':'10.00','conPerc':'','countryCode':'IN','merchantName':'TEST MVISA','cityName':'MUMBAI','postalCode':'400067','billNumber':'123456','mobileNumber':'9876543210','storeID':'4007','consumerId':'','terminalId':'87400067','merchantId':'1010122060470'}";
		Response resp = qrService.getQR("APP", data);
		System.out.println(JsonUtility.convertToJson(resp));
	}

	@Test
	public void testGetUpiQR() {
		//fail("Not yet implemented");
		String data = "{'bankCode':'00001','upiUrl':'upi://pay?','vpa':'abc@bank','merchantName':'Test UPI','mcc':'1111','amount':'10.00','mam':'2.00','merchantId':'10010101010','terminalId':'3499453'}";
		Response resp = qrService.getUpiQR("APP", data);
		System.out.println(JsonUtility.convertToJson(resp));
	}

}
*/