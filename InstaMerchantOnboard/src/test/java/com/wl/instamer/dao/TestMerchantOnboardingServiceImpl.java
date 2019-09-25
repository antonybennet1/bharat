/*package com.wl.instamer.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wl.instamer.service.MerchantOnboardService;
import com.wl.util.DesUtil;

public class TestMerchantOnboardingServiceImpl {

	static{
		final String encryptedKey = "NlBxhN6ADhuBhLD+vcxwyuNwr6bEP+9Sh1TwV6XaMLV/+gVVlu/B8C3VVlb67v/oB3VFJ2yj+bQOoFc6ZJiIITmMZKnWcjfCDSpo+ylXQ35rRPVRECcJl60ouyZw9011+tyo8YaAp+dD4K/KlJqdP6mWVKIx/n9e4avy7LAx73M4iYxre10mq+7fWwkpaICP2Iw7UXLIs6Mo017fnbAy+CHLg9pxK/5sPlh2u8rwr7FullZbZrInmGJ+juh7b6RPHLCdRcRiYCFc3LTCBloLK7COCKCpWWzIFfy0DRDxVtJV7p8wXhOrnt47orXa6rBRmRlwQLs6f/ptPOmYJJFkHg==";
		final String encryptedIv = "h6gjnwh0xJzwTvAeQZ5RrD2jkKjaA9xEJRGacnlR63c2bE+vAMujX+0FNHJZNuR68dqpZCyEIhpmWQ7vVMjv/yZF4f4Dz1NoMCyCrhhnZeALuVe0Nto2c4/BnOsfxnuk5RGCham8BZpJV+bcn2gfMizC63r3m0qo3YW62biz+/mZD6P6AfET97PaqpFjZ0Zo9xUjsETjztnNf3NGLCYCLEEeESZCCO3hcDdzPNFMNPWfY95JkLNkhTujzhBkGSjITD/qx472AYlwiGIZxY32v2WMEVdaTxrGJu2fv2wjqnrJoE1PmnG8BBl031g1yZQnTQhgzUKXmiE1tmRynfMOGQ==";
		try {
			DesUtil.init(encryptedKey, encryptedIv);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private ApplicationContext context;

    @Before
    public void setUp()
    {
    	System.out.println("inside setup before calling");
        context = new ClassPathXmlApplicationContext("/instamer-servlet.xml");

    }
	
	@Test
	public void testProcessRequest() {
		
		MerchantOnboardService service =context.getBean(MerchantOnboardService.class);
		String str = "{\"merIdentification\":1,\"bankCode\":\"00031\",\"merCreationDate\":\"27-APR-17\",\"appNo\":129930598234,\"customerId\":\"46234\",\"merType\":\"NRM\",\"merSegment\":\"Corporate\",\"mcc\":48,\"tcc\":21,\"panNo\":45678344234,\"legalName\":\"BIKANER SWEET AND SNACKS\",\"dbaName\":\"BIKANER SWEET AND SNACKS\",\"zoneCode\":81,\"branchCode\":21,\"grossMdrType\":\"Y\",\"dob\":\"03-APR-17\",\"businessEntity\":\"Sole Proprietorship Concern\",\"instAddContactPerson\":\"SATISH\",\"instAddressLine1\":\"BIKANER SWEET AND SNACKS , G1 \",\"instAddressLine2\":\"Mumbai\",\"instCity\":35,\"instLocation\":3245,\"instAddPincode\":411033,\"instAddTelephone\":7564423443,\"instAddMobileNo\":9545822223,\"instAddEmail\":\"ganesh@gmail.com\",\"raAddressLine1\":\"BIKANER SWEET AND SNACKS , G1 \",\"raAddressLine2\":\"Pune\",\"raPinCode\":411033,\"raCity\":27,\"raMobile1\":91234567894,\"raMobile2\":0,\"paymentBy\":\"N\",\"accountNo\":1234567345,\"accountLabel\":\"XYZ\",\"bnfIfsc\":\"AXIS8734\",\"bnfName\":\"Ganesh\",\"bnfAccountNo\":\"345567564353\",\"bnfBankName\":\"AXIS\",\"paySoldId\":1,\"stmtReqType\":\"E\",\"stmtFrequency\":\"D\",\"merDocumentName\":\"Y\",\"terminalType\":\"E\",\"edcModel\":\"Bharat QR\",\"domUpto1000Onus\":1.0,\"domUpto1000Offus\":1.0,\"domOffusLess2000\":1.0,\"domOnusLess2000\":2.0,\"domOffusGret2000\":1.0,\"domOnusGret2000\":3.0,\"bankSubRate\":3,\"leadGenId\":3456,\"seCode\":\"3456\",\"approvedBy\":\"Ganesh\",\"profitabilityStatus\":\"Other\",\"postFacto\":\"Y\",\"nonOperCANo\":\"Y\",\"riskApproval\":\"Y\",\"tipFlag\":\"Y\",\"conFeeFlag\":\"F\",\"conFeeAmount\":11.5,\"conFeePercentage\":0.0,\"merMobileNumber\":1234567889,\"merEmailId\":\"bss@GMAIL.COM\",\"referralCode\":12345,\"numberOfTerminal\":1,\"joiningFee\":4.0,\"rentalFee\":3.0,\"setupFee\":4.0,\"otherCharges\":3.0,\"identificationType\":\"PAN\",\"isRefundAllowed\":\"Y\",\"pInstallationAddFlag\":\"Y\",\"creditCardPremium\":2,\"creditCardNonPremium\":2}";
		service.processRequest(str);
		
	}

}
*/