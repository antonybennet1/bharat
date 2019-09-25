/*package com.wl.upi.service;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wl.upi.model.AxisRefundTransaction;
import com.wl.upi.model.AxisTransactionStatus;
import com.wl.upi.model.AxisUserTxnModel;
import com.wl.upi.model.RefundRequest;
import com.wl.util.JsonUtility;
import com.wl.util.config.BankConfig;

public class TestAxisBankUPIService {
	
	ApplicationContext context ;
	ApplicationContext utilContext ;
	AxisBankUPIService axisBankUPIService;
	AxisUserTxnModel axisUserTxnModel;
	AxisTransactionStatus axisUserTxnModel2 ;
	AxisRefundTransaction axisRefundTransaction;
	@Before
	@Ignore
	public void setUp()
	{
		context = new ClassPathXmlApplicationContext("upi-spring.xml");
		utilContext = new ClassPathXmlApplicationContext("util-spring.xml");
		axisBankUPIService = (AxisBankUPIService) context.getBean("axisBankUPIService");
		 axisUserTxnModel = new AxisUserTxnModel();
		axisUserTxnModel.setMerchId("CollectTest");
		axisUserTxnModel.setMerchChanId("CollectApp");
		axisUserTxnModel.setUnqTxnId("SA95UZdplklsfssdjkklsjfstrurig3yua707k124dyQh31");
		axisUserTxnModel.setUnqCustId("919773187515");
		axisUserTxnModel.setAmount("2.00");
		axisUserTxnModel.setTxnDtl("GRT");
		axisUserTxnModel.setCurrency("INR");
		axisUserTxnModel.setOrderId("ABGT45");
		axisUserTxnModel.setCustomerVpa("vijay@axis");
		axisUserTxnModel.setExpiry("20");
		axisUserTxnModel.setsId("123");
		axisUserTxnModel.setCheckSum(
				"795C5496901C829A0EE53C427124AA85F821FDA00B51D07832E136FFC0D2BB3E9A67D3DE2513D18EE2EBDAC2E6F2D253FE15640B4F40FBA83EAEF6B684A3ED0DD4AC960E29DCAC91820D541FA0D62C403D55EB5C565D8D82EDD9CC05E2033F08E508EBDBAE216A735082DC7C64"
						+ "1FD1659931F9615A8B0456D206762E23C53944CEE8046145149E482E1A36020C9013F96828D4EA9E3387130546949537968784B22C28F7B3E77EB3ED13E4AA17B4095629348B3DAB137BAEDB87B79A99EC164BFFA6F2577BCA0D2C5B53D8569AD5D8F3DE535722F1FA48C2171A517903AF"
						+ "48F01779BC414E9C47C1B9F6758C14467346BA882F7768B8EE5BEC01BF60500BFA47");
		axisUserTxnModel2 = new AxisTransactionStatus();
		axisUserTxnModel2.setMerchId("CollectTest");
		axisUserTxnModel2.setMerchChanId("CollectApp");
		axisUserTxnModel2.setUnqTxnId("SA95UZdplklsfssdjkklsjfstrurig3yua707k124dyQh31");
		axisUserTxnModel2.setCheckSum(
				"6E00E309B6EBF2BE49A64DE331C4F3030263F8F86AF97CEF8873C4996C9553D55FD51A9A917E1EBB7092E8C063233AC61ECE3D3A775A0B64516A7281B9F1F6F99CAACE3DC14D54ACC1"
						+ "27AFF16DF2639D378F4862C26AD45167113EB64E0C8AE3AC04F310C560FFBA448D67079C9CDBAD0B502AD69156E26F75AEDDDAFDFF7A7E80CEF759F889F16DE33A1F08E54DA2B21A2"
						+ "C543790B1BDC320120A1BB777AD76DF7B89631B6B3D9AC90C072B9E5A9D59D465B9409A8F39BC08E78BE5916ED42DDD3C60676EE7A196ACBA02C3537829A74B1B0B769D0E67B1CD222"
						+ "F626100BAC749CD08C70C72890382C98073C3B41D813D054BBDD3F8F1C2F4F6FB75AE8603D6");
		axisRefundTransaction = new AxisRefundTransaction();
		axisRefundTransaction.setMerchId("CollectTest");
		axisRefundTransaction.setMerchChanId("CollectApp");
		axisRefundTransaction.setUnqTxnId("SA95UZdplklsfssdjkklsjfstrurig3yua707k124dyQh31");
		axisRefundTransaction.setCheckSum(
				"6E00E309B6EBF2BE49A64DE331C4F3030263F8F86AF97CEF8873C4996C9553D55FD51A9A917E1EBB7092E8C063233AC61ECE3D3A775A0B64516A7281B9F1F6F99CAACE3DC14D54ACC1"
						+ "27AFF16DF2639D378F4862C26AD45167113EB64E0C8AE3AC04F310C560FFBA448D67079C9CDBAD0B502AD69156E26F75AEDDDAFDFF7A7E80CEF759F889F16DE33A1F08E54DA2B21A2"
						+ "C543790B1BDC320120A1BB777AD76DF7B89631B6B3D9AC90C072B9E5A9D59D465B9409A8F39BC08E78BE5916ED42DDD3C60676EE7A196ACBA02C3537829A74B1B0B769D0E67B1CD222"
						+ "F626100BAC749CD08C70C72890382C98073C3B41D813D054BBDD3F8F1C2F4F6FB75AE8603D6");
		axisRefundTransaction.setMobNo("919773187515");
		axisRefundTransaction.setRefundReason("not insterested");
		axisRefundTransaction.setsId("123");
		axisRefundTransaction.setTxnRefundAmount("1.0");
		axisRefundTransaction.setTxnRefundId("SA95UZdplkfhhgjlsfssdjslkfstrurig3yua707k124duyQh57");
		axisRefundTransaction.setBankCode("00031");
	}

	@Test
	@Ignore
	public void testTokenGeneration() {
		//axisBankUPIService.tokenGeneration(axisUserTxnModel);
		
	}

	@Test
	@Ignore
	public void testCollectRequest() {
		//axisBankUPIService.collectRequest("919773187515ugitzlmaxse5hwdk4oj9687cy1475681470000");
	}

	@Test
	@Ignore
	public void testTransactionStatusCheck() {
		//axisBankUPIService.TransactionStatusCheck(axisUserTxnModel2);

	}

	@Test
	public void testRefundTransaction() {
		System.setProperty("https.proxyHost", "10.10.15.200");
	    System.setProperty("https.proxyPort", "8080");
		BankConfig bankconfig  = utilContext.getBean(BankConfig.class);
		bankconfig.init();
		String jsonRequest = "{ \"MID\" : \"000022600018181\" , \"TID\" : \"22060471\",  \"txnId\" : \"719820372460638468\",  \"mobileNumber\" : \"9987208561\", \"refundReason\" : \"noReason\", \"refundAmount\" : \"10\" }";
		RefundRequest req = (RefundRequest) JsonUtility.parseJson(jsonRequest,RefundRequest.class);
		axisBankUPIService.upiRefund("TCH", "00031", req);
	}

}
*/