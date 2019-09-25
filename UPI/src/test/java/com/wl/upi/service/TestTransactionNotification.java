/*package com.wl.upi.service;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wl.util.HttpsClient;

public class TestTransactionNotification {
	
	private static final Logger logger = LoggerFactory.getLogger(TestTransactionNotification.class);
	
	ApplicationContext context;
	PSPTransactionService upiTransactionService;
	UPIQRService upiQRService;

	@Before
	public void setUp() {
		//context = new ClassPathXmlApplicationContext("upi-spring.xml");
		//upiTransactionService = (PSPTransactionService) context.getBean("upiTransactionService");
		//upiQRService = (UPIQRService) context.getBean("upiQRService");

	}
	@Test
	public void TesttransactionNotification() {
		String data = "{\"merchId\":\"075126027804259\",\"merchVpa\":\"xyza@bank\",\"qrCodeType\":\"dynamic\",\"unqTxnId\":\"PFAH01230681562\","
		+"\"trId\":\"40164610101220604701498737756\",\"txnRefNo\":\"7856039304840\",\"txnDate\":\"20161110154605\",\"txnCurrency\":\"356\","
		+"\"amount\":\"100.00\",\"bankCode\":\"00031\",\"customerVpa\":\"xyz@bank\",\"customerName\":\"Rajesh Mehta\",\"status\":\"SUCCESS\", \"message\":\"InSufficient Amount\"}";
		upiTransactionService.transactionNotification("PSP", data);
		
	}
	@Ignore
	@Test
	public void TeststatusCheck() {
		String data = "{\"MID\":\"075126027804259\",\"txnId\":\"9223372036854775807\",\"bankCode\":\"00031\",\"TID\":\"11122244\"}";
		upiQRService.checkStatus("PSP","", data);
		
	}
	
	@Ignore
	@Test
	public void TestCancelQR() {
		String data = "{\"MID\":\"075126027804259\",\"txnId\":\"9223372036854775807\",\"bankCode\":\"00031\",\"TID\":\"1011\"}";
		upiQRService.cancelQR("PSP", "",data);
	}
	
	
	@Test
	@Ignore
	public void testCheckAggregatorBharatQR(){
		String data="{\"TID\": \"22060471\",\"amount\": \"10.00\",\"txnId\": \"723014053347268780\",\"trId\": \"719519273055705102721304414190\"}";
		upiQRService.checkAggregatorTransStatus("Demo", "", data);
	}
	 
	
	@Test
	@Ignore
	public void testCheckAggregatorUPI(){
		String data="{\"TID\": \"22060471\",\"amount\": \"10.00\",\"txnId\": \"723014053347268780123123123\",\"trId\": \"000022600018181719818352678362475\"}";
		upiQRService.checkAggregatorTransStatus("Demo", "", data);
	}
	
	@Test
	@Ignore
	public void testCheckAggregatorWitoutQRAndUPI(){
		String data="{\"TID\": \"22060471\",\"amount\": \"10.00\",\"txnId\": \"7230140539872487482387432874872\",\"trId\": \"0000226000\"}";
		upiQRService.checkAggregatorTransStatus("Demo", "", data);
	}
	
	@Test
	public void testGetQR() {
		String json = "parm={\"fromEntity\":\"TCH\",\"bankCode\":\"00075\", \"data\": {\"MID\":\"075030951100014\",\"TID\":\"QA027889\",\"qrType\":\"2\", \"prgType\":\"5\", \"amount\":\"10.00\"}}";
		String url = "https://172.16.27.65:8443/bharatqr/qr/getQR";
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		String resp = HttpsClient.send(url, json, headers);
		logger.debug("Response from url"+resp);
		Assert.assertNotNull(resp);
	}
	
	@Test
	public void testGetQR1() {
		String json = "parm={\"fromEntity\":\"TCH\",\"bankCode\":\"00075\", \"data\": {\"MID\":\"075120465002827\",\"TID\":\"27002107\",\"qrType\":\"2\", \"prgType\":\"5\", \"amount\":\"10.00\"}}";
		String url = "https://172.16.27.65:8443/bharatqr/qr/getQR";
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		String resp = HttpsClient.send(url, json, headers);
		logger.debug("Response from url"+resp);
		Assert.assertNotNull(resp);
	}
	
	@Test
	public void testCheckStatus() {
		String data = "parm={\"fromEntity\":\"TCH\",\"bankCode\":\"00075\", \"data\":{\"txnId\":\"726816061515282489\",\"trId\":\"726816061515282489\",\"amount\":\"10.00\", \"TID\":\"27002107\"}}";
		String url = "https://172.16.27.65:8443/bharatqr/aggr/checkStatus";
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		String resp = HttpsClient.send(url, data, headers);
		logger.debug("Response from url"+resp);
		Assert.assertNotNull(resp);
	}
	
	@Test
	public void testCheckStatus1() {
		String data = "parm={\"fromEntity\":\"TCH\",\"bankCode\":\"00075\", \"data\":{\"txnId\":\"723319195749048680\",\"trId\":\"723319195749048680\",\"amount\":\"10.00\", \"TID\":\"52061240\"}}";
		String url = "https://172.16.27.65:8443/bharatqr/aggr/checkStatus";
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		String resp = HttpsClient.send(url, data, headers);
		logger.debug("Response from url"+resp);
		Assert.assertNotNull(resp);
	}
	
	@Test
	public void testCheckStatus2() {
		String data = "parm={\"fromEntity\":\"TCH\",\"bankCode\":\"00075\", \"data\":{\"txnId\":\"075126031302788720111583756428528\",\"trId\":\"075126031302788720111583756428528\",\"amount\":\"10.00\", \"TID\":\"52061240\"}}";
		String url = "https://172.16.27.65:8443/bharatqr/aggr/checkStatus";
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		String resp = HttpsClient.send(url, data, headers);
		logger.debug("Response from url"+resp);
		Assert.assertNotNull(resp);
	}
}
*/