/*package com.wl.upi.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wl.util.HttpsClient;

public class TestUPITransaction {

	private static final Logger logger = LoggerFactory.getLogger(TestUPITransaction.class);
	
	@Test
	//@Ignore
	public void testMultibankPspSuccess()
	{
		//RestTemplateUtil.setProxyEnabled(true);
		String url = "https://172.16.26.116:8443/bharatqr/upi/callback/BOBPSP";
		String json = "{\"data\":\"zQQu7cxH292+bxuizXlkYWA312z5kABaKcBtLeTv+wUqBf3xBJ3IFU+9CzTTBrfX9K9MmuwvDQ+A0umYbLqRT7cjIyPtNmk3f3xEHX2UAHuGYt6QU0q/eFBV2XKWAjgAWarKjUSiX6faG0Nq/fe5/gRh73ewZKrI1JBXB0NMKFzyTT1NEHrDJkZQ0Db5PwuYirKBRcR8Rdbe9mJVmk2d7pwOT+4QSqj/FkbcyrsOvRcx0DCjDjSLi0lZCPpcl1ypwp7V1xREQWNHQPQhGDJmGnJnkc1XKkKN9sOmrT9nSFEvVNTemPlpSe6VPjDsvDUXpPgorGYEkVW3Gvd+jE9rYT+9HubeBgomouh02nr+9QWYGuBnTOCLiqhcmDX3QyHHJKEH2ySUsDOon8Pg76JGuYaf0NBPGFWtDuQU/i/idGdmpzJ3pqLzl1jHxR3+yNliaEgMwjPpogAiWxvwV1VVh8OhxAKeLZyEoMPnNXDqncEWX47I2NYTBeqbwvHyfJwHsN0m22JOpA4cVn9P+DcQKh+c8Ts4X+joxshPy+MtXISKvQimMnfW6uJLV11+wOLMKWQ98D4B+tr96TpfuFfgRw==\"}";
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		String resp = HttpsClient.send(url, json, headers);
		logger.debug("Response from url"+resp);
		Assert.assertNotNull(resp);
	}
	
	
	@Test
	@Ignore
	public void testMultibankPspFailure()
	{
		//RestTemplateUtil.setProxyEnabled(true);
		String url = "https://172.16.27.65:8443/bharatqr/upi/callback/BOBPSP";
		String json = "{\"data\":\"zQQu7cxH292+bxuizXlkYWA312z5kABaKcBtLeTv+wUqBf3xBJ3IFU+9CzTTBrfX9K9MmuwvDQ+A0umYbLqRT7cjIyPtNmk3f3xEHX2UAHuGYt6QU0q/eFBV2XKWAjgAWarKjUSiX6faG0Nq/fe5/gRh73ewZKrI1JBXB0NMKFzyTT1NEHrDJkZQ0Db5PwuYirKBRcR8Rdbe9mJVmk2d7pwOT+4QSqj/FkbcyrsOvRcx0DCjDjSLi0lZCPpcl1ypwp7V1xREQWNHQPQhGDJmGnJnkc1XKkKN9sOmrT9nSFEvVNTemPlpSe6VPjDsvDUXpPgorGYEkVW3Gvd+jE9rYT+9HubeBgomouh02nr+9QWYGuBnTOCLiqhcmDX3QyHHJKEH2ySUsDOon8Pg76JGuYaf0NBPGFWtDuQU/i/idGdmpzJ3pqLzl1jHxR3+yNliaEgMwjPpogAiWxvwV1VVh8OhxAKeLZyEoMPnNXDqncEWX47I2NYTBeqbwvHyfJwHsN0m22JOpA4cVn9P+DcQKh+c8Ts4X+joxshPy+MtXISKvQimMnfW6adasduJLV11+wOLMKWQ98D4B+tr96TpfuFfgRw==\"}";
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		String resp = HttpsClient.send(url, json, headers);
		logger.debug("Response from url"+resp);
		Assert.assertNotNull(resp);
	}
	
	*//**
	 * SBI failure
	 *//*
	@Test
	@Ignore
	public void testSBIPsp()
	{
		String json = "msg={\"resp\":\"5771081f3d85c59492cc3a3151e69d8cf44e33367fbf94ea2977defa5b0268cfe934f28cb7a3c71eb00f5f2f669a30a068e70761b033c5349d6a37e836968d7df9f1f52b5cbd9c588789d1c43983c59dfd5d3d7f6ad8e768ad1c42b1b7575dbdb2532951084225f59be06da2bda8572f6193998ac6692e16c1c63707edd3ade27bca3960f91953dfd88610e10f9bbea7dc8dc08f3e147cafbb8c67d7a398297f105b35ce9ecff4dcac9bab29e24382d521fdd20c0603954528238324295ec18e865f633f840bf63685a89fe29b9e2f006ba33cc80b58dc6dcbabbfaa9aa83466b4ba6ab26cb574458c99b3a0a1e12330cc00acc93a381852c6335cbe11915c6a127f432d2edad4060a4b72bed085d7de2257ce26313a7244a7648349b5b8833cdee03cec50a090b2699223470ae0a70bcce42b482b1761c9036d88b597fb38a5f4fb61001644ca97ac79d1ed06ab9f30956b1e807e84b89938792cff9ceb1fbff4b50bf4f9d78967c23d32837eebbff0b52dbc8fb8b60cccc2d971bdfdeed6fe\",\"pgMerchantId\":\"SBI0000000000110\"}";
		String url = "https://172.16.27.65:8443/bharatqr/upi/callback/SBIPSP?"+json;
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		String resp = HttpsClient.send(url, json, headers);
		logger.debug("Response from url"+resp);
		Assert.assertNotNull(resp);
	}
	
	*//**
	 * SBI Success
	 * @throws UnsupportedEncodingException 
	 *//*
	@Test
	@Ignore
	public void testSBIPsp1() throws UnsupportedEncodingException
	{
		String parm = "msg=";
		String json = "{\"resp\":\"5771081F3D85C59492CC3A3151E69D8CC27D501469FE0C575262860B2E17E81FB6E25429A97AE10390B8386CE57C7F22BE87EAE8E53472C5E8E20D389CDC1215F50853D43BE4B6A69C9A2C0023FA7E403AF12C180B1B6D56F12FF55B2E108D0CE07CA8C81CCD895C778739391E3E9CA41F17CEAFB15101098C535DD8D167E5A8A1EBF5E3EB7222A29D32B01DE96EBB8E9268290278C00A0469742ED9850951269B8E56658F4C9664E70F990F6CB45425865C328FD84E1871C7EAB9899B9B386647A314768C6ED88C5D197B4320042FC526A277886B2A13C15A9046BD7E0310989DCC118EF1FB4EDB48E99CB388B2245D5310939F7EBB40A6C9CABA8D5E0F964366C078FE938A60226B3D79F21D277877A237852732ED794E84ACD894033252F766B3CD4C8FBC887E2AEC3248EC912EDCAC40F9B5186C58CDCC0D7A2B6C1632EE091F6EEF4AC841C8B5303D6A98A37FA5F1F2913268CCA6B412A52B93D1917A16647EFC6BBAAA9FEE7745F77A6BF945C1345E339740CE5DBDB93A45B679F88B9E63CFC068583782D606152A3D8AD6D222E2157329F1F3CB796B91EB584F765876D6208DDA1C6BB51B3DFEF6A26B0CE2D4CBEE2770F5238BD7433D02FCF6342290FA879B1AC948C8E33D59F9C9002E9C74D032CD2BC03C97CDB2783C73A5576F48\",\"pgMerchantId\":\"SBI0000000000110\"}";
		String url = "https://te1.in.worldline.com:8443/bharatqr/upi/callback/SBIPSP?"+parm+URLEncoder.encode(json,"utf-8");
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		String resp = HttpsClient.send(url, parm+json, headers);
		logger.debug("Response from url"+resp);
		Assert.assertNotNull(resp);
	}
	
	*//**
	 * YES FAILED
	 * @throws UnsupportedEncodingException 
	 *//*
	@Test
	@Ignore
	public void testYesPSP() throws UnsupportedEncodingException
	{
		String json = "{\"data\":\"TiOH6Rn3VHKAQNuppKqXew3sGXNy5GbanyutYn4NfcEwImt9ehkJcRL/R6vVeQk1tq0SCGjhw/il4xlTSKa3LMKbDweiAVAUUXIVqfYcmLVf+8wxzrzvIvBlVjvUEvlC+/onqbCPTZHtgnoNKj6+q0NSlKVQ38CwXokzu3+9swn1Mi5540yRHQfjW+7WEdPY4BRzcNIKMEz8AmCjaNUGu5QA1D9SiD/Oh2N3/SK00wbGSMpWevoWfRNyrenqHB0heoauUUSbj+FnVJkeHHjdN/Sm0/7sP1A8z8s1H+lvBLvuAHmAdNX4vCnYqtgL3Mz2bIE5NUZy+BMDgbGGTRrDkvOeBXRMsZ71PoXVKndbH3uMmCvKP29oB2zktXJbNJ6106UO6Vyu/2QzCcRxFQKNEgfmjZzH0rUYWQc1IZG4JFEGJz5xt8XCSY0c4gKfy+V9dEkbHn0YYsSJUkJ2wW/4lFeOOGx4/5M+98eK6pcMdotcAaaXnJlOkOpbJjalYCH1vQyAZIRJFGcFZMZAguVVn+rvgJFlxBGkxSEfBlQTRUKS/ke306WUdKE9qzziBNsI\"}";
		String url = "https://te1.in.worldline.com:8443/bharatqr/upi/callback/YESPSP";
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		String resp = HttpsClient.send(url, json, headers);
		logger.debug("Response from url"+resp);
		Assert.assertNotNull(resp);
	}
	
	*//**
	 * YES SUCCESS
	 * @throws UnsupportedEncodingException 
	 *//*
	@Test
	@Ignore
	public void testYesPSP1() throws UnsupportedEncodingException
	{
		String json = "{\"data\":\"TiOH6Rn3VHKAQNuppKqXew3sGXNy5GbanyutYn4NfcEwImt9ehkJcRL/R6vVeQk1tq0SCGjhw/il4xlTSKa3LCWmNS0a49RFv2GKDxib8zuNT3XdmbiAECgq/gbop4feKaXn8Ef1CJ69h727ZfeKX8PcsKGORlDxwgNesUmff3H8oeQ/PcVAvCyLyNV+o6IL5q5claJh1RTZqpkFYmE1yKPjCbQD5lN59RefdvG/bXsMETIAd30QsNe1UvkifkdFvD37iP/Yf2AnHwm77TQZQqQKpL0gdr0A2vsWacuWcd+HtMZ6ufO8EPT5hnVhoUQlXX5Ypx6xs84wQoky7QxEMORs3nvPyMaC0FESkcnP+mSyDKv0jrcMXTxQLt/iWuKKZ1rkvBCFqnqnN/CHLg9jyGjEm0gc97qM+zEa7V0Vvc2f7OXi4kWTN9lzKyYAe5Nj4xyQaxEbnulRh/skEhi4vpgeOSVXaqkCPhU3Cgsg6ilSmSiaskUHNmi/S2ToSkYiSk8SX4QR1xge4PofcaQpISSVbDDHceQH85g/Ab0Mq8CgTxlfCwobWnMuvLCkATyU\"}";
		String url = "https://te1.in.worldline.com:8443/bharatqr/upi/callback/YESPSP";
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		String resp = HttpsClient.send(url, json, headers);
		logger.debug("Response from url"+resp);
		Assert.assertNotNull(resp);
	}
	
	*//**
	 * BharatQR Success
	 * @throws UnsupportedEncodingException 
	 *//*
	@Test
	@Ignore
	public void testBharatQRTxn() throws UnsupportedEncodingException
	{
		//String json = "{ 	'class_name': 'TransactionResponse', 	'function': 'populateTransaction', 	'mpan': '4541234000000017', 	'customer_pan': '1234567890123445', 	'customer_name': 'SANTHOSH', 	'txn_currency': '356', 	'txn_amount': '91.00', 	'auth_code': '522046', 	'ref_no': '790190200026', 	'primary_id': '723011222007414771', 	'secondary_id': '', 	'settlement_amount': '91.00', 	'time_stamp': '20170818112755', 	'transaction_type': '1', 	'bank_code': '00001' ,'additional_data':'~~~~~~~' }";
		//String json="{\"class_name\":\"TransactionResponse\",\"function\":\"populateTransaction\",\"mpan\":\"4604901002704071\",\"customer_pan\":\"4336620008851463\",\"customer_name\":\"                              \",\"txn_currency\":\"356\",\"txn_amount\":\"        1.01\",\"auth_code\":\"473124\",\"ref_no\":\"733106720328\",\"primary_id\":\"0127331120819487479       \",\"secondary_id\":\"49375376                    \",\"settlement_amount\":\"          1.01\",\"time_stamp\":\"20171127120920\",\"transaction_type\":\"1\",\"bank_code\":\"00031\",\"additional_data\":\"~~~~~~~\"}";
		//String json = "{\"txn_currency\":\"356\",\"transaction_type\":\"1\",\"customer_name\":\"Somya                         \",\"secondary_id\":\"                            \",\"bank_code\":\"00031\",\"class_name\":\"TransactionResponse\",\"function\":\"populateTransaction\",\"primary_id\":\"0AX2298025351345751       \",\"customer_pan\":\"4386450000000060\",\"mpan\":\"4604901002889666\",\"auth_code\":\"446159\",\"ref_no\":\"700215724828\",\"settlement_amount\":\"          1.00\",\"txn_amount\":\"        1.00\",\"time_stamp\":\"20171102154818\",\"additional_data\":\"~~~~~~~\"}";
		//String json="{\"class_name\":\"TransactionResponse\",\"function\":\"populateTransaction\",\"mpan\":\"5122600002871654\",\"customer_pan\":\"5326760300709057\",\"customer_name\":\"Bhatnagar                     \",\"txn_currency\":\"356\",\"txn_amount\":\"        1.00\",\"auth_code\":\"480938\",\"ref_no\":\"733506060972\",\"primary_id\":\"123456789000431           \",\"secondary_id\":\"                            \",\"settlement_amount\":\"          1.00\",\"time_stamp\":\"20171201115739\",\"transaction_type\":\"1\",\"bank_code\":\"00031\",\"additional_data\":\"~~~~123456789000431~~27373062\"}";
		
		String json ="{\"class_name\":\"TransactionResponse\",\"function\":\"populateTransaction\",\"mpan\":\"4604901003287936\",\"customer_pan\":\"4838340009001579\",\"customer_name\":\"RAMKUMAR S                    \",\"txn_currency\":\"356\",\"txn_amount\":\"        1.05\",\"auth_code\":\"462944\",\"ref_no\":\"734513670907\",\"primary_id\":\"0ph7345192449918190       \",\"secondary_id\":\"101675                      \",\"settlement_amount\":\"          1.05\",\"time_stamp\":\"20171211192242\",\"transaction_type\":\"1\",\"bank_code\":\"00031\",\"additional_data\":\"~~~~~~~\"}";
		String url = "https://172.16.26.116:8443/bharatqr/callback?parm="+URLEncoder.encode(json,"utf-8");
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		String resp = HttpsClient.send(url, null, headers);
		logger.debug("Response from url"+resp);
		Assert.assertNotNull(resp);
	}
	
	*//**
	 * BharatQR Success
	 * @throws UnsupportedEncodingException 
	 *//*
	@Test
	@Ignore
	public void testBharatQRRefund() throws UnsupportedEncodingException
	{
		//String json = "parm={\"fromEntity\":\"TCH\", \"bankCode\":\"00001\", \"data\":{ \"TID\":\"22040409\", \"txnType\":\"1\", \"bankCode\":\"00001\",  \"refundAmount\":\"1.00\", \"rrn\":\"720839373838\", \"authCode\":\"473952\" }}";
		//String json="parm={\"fromEntity\":\"YES\", \"bankCode\":\"00045\", \"data\":{ \"TID\":\"87450221\", \"txnType\":\"1\", \"bankCode\":\"00045\",  \"refundAmount\":\"1.00\", \"rrn\":\"733206000072\", \"authCode\":\"463382\" }}";
		String json="parm={\"fromEntity\":\"YESPSP\", \"bankCode\":\"00045\", \"data\":{ \"TID\":\"47453033\", \"txnType\":\"1\", \"bankCode\":\"00045\",  \"refundAmount\":\"1.00\", \"rrn\":\"733810668905\"}}";
		
		String url = "https://172.16.26.116:8443/bharatqr/qr/refund";
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		String resp = HttpsClient.send(url, json, headers);
		logger.debug("Response from url"+resp);
		Assert.assertNotNull(resp);
	}
	
	@Test
	@Ignore
	public void testTXNStatus() throws UnsupportedEncodingException
	{
		String json = "parm = {\"fromEntity\":\"YES\",\"bankCode\":\"00045\",\"data\":{\"TID\":\"87450221\",\"amount\":\"45.00\",\"txnId\":\"10101010000000000000000000\",\"trId\":\"00100100\"}}";
		String url = "https://172.16.26.116:8443/bharatqr/qr/txnEnquiry";
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		String resp = HttpsClient.send(url, json, headers);
		logger.debug("Response from url"+resp);
		Assert.assertNotNull(resp);
	}
	
	@Test
	@Ignore
	public void testUPIRefund() throws UnsupportedEncodingException
	{
		//String json = "parm={\"fromEntity\":\"TCH\", \"bankCode\":\"00004\", \"data\":{ \"TID\":\"26311052\", \"txnType\":\"2\", \"bankCode\":\"00001\",  \"refundAmount\":\"1.00\", \"rrn\":\"723317372478\", \"authCode\":\"473952\" }}";
		String json = "parm={\"fromEntity\":\"CBIPSP\", \"bankCode\":\"00004\", \"data\":{ \"TID\":\"22040467\", \"txnType\":\"1\", \"bankCode\":\"00004\",  \"refundAmount\":\"5.00\", \"rrn\":\"732512032038\" }}";
		String url = "https://172.16.27.65:8443/bharatqr/qr/refund";
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		String resp = HttpsClient.send(url, json, headers);
		logger.debug("Response from url"+resp);
		Assert.assertNotNull(resp);
	}
	
	*//**
	 * BOB Failed
	 * @throws UnsupportedEncodingException 
	 *//*
	@Test
	@Ignore
	public void testBOBPSP1() throws UnsupportedEncodingException
	{
		String json = "{\"data\":\"2+I+azVedC1z1h8qo7gJcDBbNCG/749QoNukYEvfoZG1JJ2wkr/WhtFo9BehuRHjHoR0MVO4lY6l\n"+
				"svI3VZVBZACG7vlZPbakaBxVm3ZNM701hLqL0F618E2kdNcDLL8o6YexxmuFrAb6E4oBqhdSTFPK\n"+
				"1zjP7qiRBzrtQhTjr/2OZTnxfVdMTDzgoWpnDkL77my0XbXd2CLdHvGFG/Fm8h9t0nA/yoMMutNj\n"+
				"QR2m1j/Cx0yhKJv65td7CrvhPUp3HwmDa7EWVZXXyU/C7UymtotJKnlxs+zwuBAhbsTEKLr++zGW\n"+
				"y6sxGDqs46lyraXjLD6736lWckyQkwNnLvRf6a+XASV1TBOPbgnalO7YeKiRa/ENgBY3pwzsnaxf\n"+
				"LZ5Y/MkpJCPBPfAqSaCudZZA0TMpvp+sb0zmk7tIZo0JE4jzq5oNN29RFn8fITHWG6tMrOv6bCRG\n"+
				"OifUqdiEQfaTxhQukcll6+NwM76t0SUAoH9t7B6AL0OtR/ZFh/xrXwYd7XhL9CO8p58PMzY712Px\n"+
				"j4eNLJbR65qccTcQqyXuDTnLiPxGMx7KT9dH2Ado7HxUJnCcHyeT8kkF95O8luIvcA==\"}";
		String url = "https://te1.in.worldline.com:8443/bharatqr/upi/callback/BOBPSP";
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		String resp = HttpsClient.send(url, json, headers);
		logger.debug("Response from url"+resp);
		Assert.assertNotNull(resp);
	}
	
	
}
*/