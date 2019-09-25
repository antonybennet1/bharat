/*package com.wl.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestAesUtil {

	private static final Logger logger = LoggerFactory.getLogger(TestAesUtil.class);


	ApplicationContext context ;

	@Before
	public void setUp()
	{
		//context = new ClassPathXmlApplicationContext("util-spring.xml");
	}

	static{
		AesUtil.setPublicKey("D:\\WL-DATA\\documents\\PROJECTS\\mvisa_merchant_workspace\\bharatqr-application\\BharatQR\\src\\main\\resources\\paymod115.cer");
		//EncryptionCache.load();
	}
	@Test
	@Ignore
	public final void testEncryptPayCraft() {
		EncryptionCache.load();
		AesUtil aes = EncryptionCache.getEncryptionUtility("00031");
		String str = aes.encrypt("{\"merIdentification\":1,\"bankCode\":\"00031\",\"merCreationDate\":\"27-APR-17\",\"appNo\":129930598234,\"customerId\":\"46234\",\"merType\":\"NRM\",\"merSegment\":\"Corporate\",\"mcc\":48,\"tcc\":21,\"panNo\":45678344234,\"legalName\":\"BIKANER SWEET AND SNACKS\",\"dbaName\":\"BIKANER SWEET AND SNACKS\",\"zoneCode\":81,\"branchCode\":21,\"grossMdrType\":\"Y\",\"dob\":\"03-APR-17\",\"businessEntity\":\"Sole Proprietorship Concern\",\"instAddContactPerson\":\"SATISH\",\"instAddressLine1\":\"BIKANER SWEET AND SNACKS , G1 \",\"instAddressLine2\":\"Mumbai\",\"instCity\":35,\"instLocation\":3245,\"instAddPincode\":411033,\"instAddTelephone\":7564423443,\"instAddMobileNo\":9545822223,\"instAddEmail\":\"ganesh@gmail.com\",\"raAddressLine1\":\"BIKANER SWEET AND SNACKS , G1 \",\"raAddressLine2\":\"Pune\",\"raPinCode\":411033,\"raCity\":27,\"raMobile1\":9545234234,\"raMobile2\":0,\"paymentBy\":\"N\",\"accountNo\":1234567345,\"accountLabel\":\"XYZ\",\"bnfIfsc\":\"AXIS8734\",\"bnfName\":\"Ganesh\",\"bnfAccountNo\":\"345567564353\",\"bnfBankName\":\"AXIS\",\"paySoldId\":1,\"stmtReqType\":\"E\",\"stmtFrequency\":\"D\",\"merDocumentName\":\"Y\",\"terminalType\":\"E\",\"edcModel\":\"Bharat QR\",\"domUpto1000Onus\":1.0,\"domUpto1000Offus\":1.0,\"domOffusLess2000\":1.0,\"domOnusLess2000\":2.0,\"domOffusGret2000\":1.0,\"domOnusGret2000\":3.0,\"bankSubRate\":3,\"leadGenId\":3456,\"seCode\":\"3456\",\"approvedBy\":\"Ganesh\",\"profitabilityStatus\":\"Other\",\"postFacto\":\"Y\",\"nonOperCANo\":\"Y\",\"riskApproval\":\"Y\",\"tipFlag\":\"Y\",\"conFeeFlag\":\"F\",\"conFeeAmount\":11.5,\"conFeePercentage\":0.0,\"merMobileNumber\":1234567889,\"merEmailId\":\"bss@GMAIL.COM\",\"referralCode\":12345,\"numberOfTerminal\":1,\"joiningFee\":4.0,\"rentalFee\":3.0,\"setupFee\":4.0,\"otherCharges\":3.0,\"identificationType\":\"PAN\",\"isRefundAllowed\":\"Y\",\"pInstallationAddFlag\":\"Y\",\"creditCardPremium\":2,\"creditCardNonPremium\":2}");
		logger.debug("Enc value |"+str);
		Assert.assertNotNull(str);
	}
	@Test
	@Ignore
	public final void testEncrypt() {
		AesUtil.setPublicKey("D:\\WL-DATA\\documents\\PROJECTS\\mvisa_merchant_workspace\\bharatqr-application\\BharatQR\\src\\main\\resources\\paymod223.cer");
		EncryptionCache.load();
		AesUtil aes = EncryptionCache.getEncryptionUtility("00031");
		String str = aes.encrypt("{\"merIdentification\":1,\"bankCode\":\"00031\",\"merCreationDate\":\"02-July-17\",\"appNo\":867446,\"customerId\":\"123468\",\"merType\":\"NRM\",\"merSegment\":\"Corporate\",\"mcc\":267,\"tcc\":21,\"panNo\":45678344234,\"legalName\":\"BIKANER\",\"dbaName\":\"BIKANER\",\"zoneCode\":81,\"branchCode\":32,\"grossMdrType\":\"Y\",\"dob\":\"03-APR-17\",\"businessEntity\":\"Sole Proprietorship Concern\",\"instAddContactPerson\":\"SATISH\",\"instAddressLine1\":\"BIKANER SWEET AND SNACKS , G1 \",\"instAddressLine2\":\"Mumbai\",\"instAddDistrict\":2,\"instCity\":35,\"instState\":123,\"instLocation\":12,\"instAddPincode\":411033,\"instAddTelephone\":8149718852,\"instAddMobileNo\":1234567890,\"instAddEmail\":\"Rahul@gmail.com\",\"raAddressLine1\":\"Dadar\",\"raAddressLine2\":\"Thane\",\"raPinCode\":411033,\"raCity\":1067,\"raMobile1\":9545234234,\"raMobile2\":9545234437,\"raState\":14,\"raDistrict\":154842,\"paymentBy\":\"C\",\"accountNo\":1234567345,\"accountLabel\":\"NME\",\"bnfIfsc\":\"AXIS8734\",\"bnfName\":\"asdf\",\"bnfAccountNo\":\"12345678976465\",\"bnfBankName\":\"sadwq\",\"paySoldId\":1,\"stmtReqType\":\"E\",\"stmtFrequency\":\"D\",\"merDocumentName\":\"Y\",\"terminalType\":\"E\",\"edcModel\":\"BHARAT QR CODE\",\"domUpto1000Onus\":0.24,\"domUpto1000Offus\":0.24,\"domOffusLess2000\":0.46,\"domOnusLess2000\":0.45,\"domOffusGret2000\":1.0,\"domOnusGret2000\":1.0,\"bankSubRate\":0,\"leadGenId\":1,\"seCode\":\"45345\",\"approvedBy\":\"G\",\"profitabilityStatus\":\"Branch Prefer Price\",\"postFacto\":\"Y\",\"nonOperCANo\":\"Y\",\"riskApproval\":\"Y\",\"tipFlag\":\"Y\",\"conFeeFlag\":\"P\",\"conFeeAmount\":1.0,\"conFeePercentage\":1,\"merMobileNumber\":1234567889,\"merEmailId\":\"bss@GMAIL.COM\",\"referralCode\":\"123456\",\"numberOfTerminal\":12,\"joiningFee\":10,\"rentalFee\":100,\"setupFee\":100,\"otherCharges\":123456,\"identificationType\":\"AADHAR\",\"isRefundAllowed\":\"Y\",\"pInstallationAddFlag\":\"Y\",\"creditCardPremium\":146,\"creditCardNonPremium\":12}");
		logger.debug("testEncrypt Enc value |"+str);
		Assert.assertNotNull(str);
	}

	@Test
	@Ignore
	public final void testDecrypt() throws Exception {
		AesUtil.setPublicKey("D:\\WL-DATA\\documents\\PROJECTS\\mvisa_merchant_workspace\\bharatqr-application\\BharatQR\\src\\main\\resources\\paymod223.cer");
		EncryptionCache.load();
		AesUtil aes = EncryptionCache.getEncryptionUtility("00031");
		String str = aes.decrypt("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");
		logger.debug(" testDecrypt Dec value |"+str);
		Assert.assertNotNull(str);
	}

	@Test
	@Ignore
	public final void testDecryptPayCraft() throws Exception {
		AesUtil aes = EncryptionCache.getEncryptionUtility("00031");
		//String str = AesUtil.decrypt("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");
		//String str = aes.decrypt("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");
		String str =     aes.decrypt("xuaLRSnOReAZSbP4Q4osgyhIl/mqqHCiK3QwtXKLdxVP/HjXaLDbhts0CsnaH6z3GPGydb/uEsSphGeweX+fMAKKMoal/Uvzj+n9Tbqqa7e1X/elsCAC7R+jNSjMC1qy1w6kMcuzWsdt2RV5m8PJGn5URDeuY3c7B9lhstQiq4oyOhceFtoRdlj8wx3a8DlesOb+GDKw4S9loSVp9fUVCiF+A6aHPHuZl0l5HDMIHoM=");
		logger.debug("Dec value |"+str);
		Assert.assertNotNull(str);
	}

	@Test
	public final void testEncryptSecretKey() throws Exception {
		//AesUtil.setPrivateKey("D:\\WL-DATA\\documents\\PROJECTS\\mvisa_merchant_workspace\\bharatqr-application\\BharatQR\\src\\main\\resources\\testenvironment.jks", "JKS", "venture@123" , "te1.in.worldline.com");
		//String encStr = AesUtil.encryptSecretKey("f4gSEGChrx1qyBVGTgHcVk10");

		*//******* paymod 223 jks dev environment *********//*
		//Following key for 172.16.27.65 server - DEV
		//AesUtil.setPrivateKey("D:\\WL-DATA\\documents\\PROJECTS\\mvisa_merchant_workspace\\bharatqr-application\\BharatQR\\src\\main\\resources\\paymod223.keystore", "JKS", "pass@1234" , "paymod223");
		//Following key for 172.16.26.116 server - SIT
		//AesUtil.setPrivateKey("D:\\UAT_Production_Realease\\bharatqr-application\\BharatQR\\src\\main\\resources\\testenvironment.jks", "JKS", "venture@123" , "te1.in.worldline.com");
		//Following key for 172.16.27.175 server - PROD
		//AesUtil.setPrivateKey("D:\\Keys\\Production\\172.16.27.175\\paymod_ssl.keystore", "JKS", "atos@1234" , "paymod");

		String encStr = AesUtil.encryptSecretKey("48C3B4286FF421A4");
		logger.debug("instaMer key |"+encStr);
		encStr = AesUtil.encryptSecretKey("0000000000000000");
		logger.debug("instaMer iv |"+encStr);
		encStr = AesUtil.encryptSecretKey("f4gSEGChrx1qyBVGTgHcVk10");
		logger.debug("instaMer des key |"+encStr);
		encStr = AesUtil.encryptSecretKey("12345678");
		logger.debug("instaMer des iv |"+encStr);


		encStr = AesUtil.encryptSecretKey("F27D5C9927726BCEFE7510B1BDD3D137");
		logger.debug("mobile iv |"+encStr);
		encStr = AesUtil.encryptSecretKey("3FF2EC019C627B945225DEBAD71A01B6985FE84C95A70EB132882F88C0A59A55");
		logger.debug("mobile salt |"+encStr);
		encStr = AesUtil.encryptSecretKey("MZygpewJsCpRrfOr");
		logger.debug("mobile key |"+encStr);

		encStr = AesUtil.encryptSecretKey("48C3B4286FF421A4A328E68AD9E542A4");
		logger.debug("mobile paycraft key |"+encStr);
		encStr = AesUtil.encryptSecretKey("00000000000000000000000000000000");
		logger.debug("mobile paycraft iv |"+encStr);
		
		
		encStr = AesUtil.encryptSecretKey("yHMjhzRCMNcKpfaq");
		logger.debug("customer pan encrption key |"+encStr);

		encStr = AesUtil.encryptSecretKey("IiCXSTIdyJh3Kv13");
		logger.debug("AXIS PSP encryption key|"+encStr);

		encStr = AesUtil.encryptSecretKey("5fe77685c12a58559cd0857d21f6faaa");
		logger.debug("SBI PSP encryption key|"+encStr);
		
		encStr = AesUtil.encryptSecretKey("8s3WpLrHTC8VQXvG");
		logger.debug("YES PSP encryption key|"+encStr);
		
		encStr = AesUtil.encryptSecretKey("3vgxEzJXDtRkHpU7");
		logger.debug("BOB PSP encryption key|"+encStr);
		
		encStr = AesUtil.encryptSecretKey("AsvZuKuyXqUY7jCS");
		logger.debug("SIB PSP encryption key|"+encStr);
		
		encStr = AesUtil.encryptSecretKey("NgRffLSPneRzNMSs");
		logger.debug("IDBI PSP encryption key|"+encStr);
		
		encStr = AesUtil.encryptSecretKey("EvJjCVWcZUeApULP");
		logger.debug("CBI PSP encryption key|"+encStr);
		
		encStr = AesUtil.encryptSecretKey("SPBahdzEXefS23AQ");
		logger.debug("KOTAK PSP encryption key|"+encStr);
		

		*//******* paymod 223 jks dev environment *********//*

		*//******* irctc key prod  environment *********//*
		AesUtil.setPrivateKey("D:\\WL-DATA\\documents\\PROJECTS\\mvisa_merchant_workspace\\bharatqr-application\\BharatQR\\src\\main\\resources\\irctc.jks", "JKS", "W0r1dl!ne@018#" , "irctc_wl");

		String encStr = AesUtil.encryptSecretKey("48C3B4286FF421A4");
		logger.debug("instaMer key |"+encStr);
		encStr = AesUtil.encryptSecretKey("0000000000000000");
		logger.debug("instaMer iv |"+encStr);
		encStr = AesUtil.encryptSecretKey("f4gSEGChrx1qyBVGTgHcVk10");
		logger.debug("instaMer des key |"+encStr);
		encStr = AesUtil.encryptSecretKey("12345678");
		logger.debug("instaMer des iv |"+encStr);



		encStr = AesUtil.encryptSecretKey("F27D5C9927726BCEFE7510B1BDD3D137");
		logger.debug("mobile iv |"+encStr);
		encStr = AesUtil.encryptSecretKey("3FF2EC019C627B945225DEBAD71A01B6985FE84C95A70EB132882F88C0A59A55");
		logger.debug("mobile salt |"+encStr);
		encStr = AesUtil.encryptSecretKey("MZygpewJsCpRrfOr");
		logger.debug("mobile key |"+encStr);

		encStr = AesUtil.encryptSecretKey("48C3B4286FF421A4A328E68AD9E542A4");
		logger.debug("mobile paycraft key |"+encStr);
		encStr = AesUtil.encryptSecretKey("00000000000000000000000000000000");
		logger.debug("mobile paycraft iv |"+encStr);


		encStr = AesUtil.encryptSecretKey("bLRoRRipkrayODoe");
		logger.debug("AXIS PSP encrption key|"+encStr);

		encStr = AesUtil.encryptSecretKey("<to be provided at the time of production release>");
		logger.debug("SBI PSP encrption key|"+encStr);
		
		encStr = AesUtil.encryptSecretKey("AsvZuKuyXqUY7jCS");
		logger.debug("SIB PSP encrption key|"+encStr);

		 

		*//******* paymod 223 jks dev environment *********//*



		//f4gSEGChrx1qyBVGTgHcVk10|NlBxhN6ADhuBhLD+vcxwyuNwr6bEP+9Sh1TwV6XaMLV/+gVVlu/B8C3VVlb67v/oB3VFJ2yj+bQOoFc6ZJiIITmMZKnWcjfCDSpo+ylXQ35rRPVRECcJl60ouyZw9011+tyo8YaAp+dD4K/KlJqdP6mWVKIx/n9e4avy7LAx73M4iYxre10mq+7fWwkpaICP2Iw7UXLIs6Mo017fnbAy+CHLg9pxK/5sPlh2u8rwr7FullZbZrInmGJ+juh7b6RPHLCdRcRiYCFc3LTCBloLK7COCKCpWWzIFfy0DRDxVtJV7p8wXhOrnt47orXa6rBRmRlwQLs6f/ptPOmYJJFkHg==
		//12345678|h6gjnwh0xJzwTvAeQZ5RrD2jkKjaA9xEJRGacnlR63c2bE+vAMujX+0FNHJZNuR68dqpZCyEIhpmWQ7vVMjv/yZF4f4Dz1NoMCyCrhhnZeALuVe0Nto2c4/BnOsfxnuk5RGCham8BZpJV+bcn2gfMizC63r3m0qo3YW62biz+/mZD6P6AfET97PaqpFjZ0Zo9xUjsETjztnNf3NGLCYCLEEeESZCCO3hcDdzPNFMNPWfY95JkLNkhTujzhBkGSjITD/qx472AYlwiGIZxY32v2WMEVdaTxrGJu2fv2wjqnrJoE1PmnG8BBl031g1yZQnTQhgzUKXmiE1tmRynfMOGQ==
		//logger.debug("Enc value |"+encStr);
		Assert.assertNotNull(encStr);
	}

	@Test
	@Ignore
	public final void testDecryptSecretKey() throws Exception {
		AesUtil.setPublicKey("D:\\WL-DATA\\documents\\PROJECTS\\mvisa_merchant_workspace\\bharatqr-application\\BharatQR\\src\\main\\resources\\paymod115.cer");
		String decStr =  AesUtil.decryptSecretKey("gXr0gt4QJAS7s2USCrB34e32u/dpkSemfzOs7PKi9EIVtGv6c0/tTx8OcKZbBSJk1p8jcnfNo86bZOizkXkJJ73NtnYWZVNIcDUHKbnohkL7jl0dhNXydUAv42+CljD04Chb4S7zt/2YgW9GQd5N3+cHirs7NnqfVhFu9p6jFl1qIKO8Cc2tHoG+Vmf1zJIAH3UzHAx9UsiLVVdxuB8KtXGvDlJbSwfGShKPHs62mohwxN/pWwRxdxoimruiqqa71WOjLhVs5AF1QSFZ2GbeEiO+/HHqf7UaHWFNtiiGQ1ogCnMlSapw70Af2Ff2iQzUvVigOSqc8IYbEpsV8LJP+g==");
		//String decStr = AesUtil.decryptSecretKey("NlBxhN6ADhuBhLD+vcxwyuNwr6bEP+9Sh1TwV6XaMLV/+gVVlu/B8C3VVlb67v/oB3VFJ2yj+bQOoFc6ZJiIITmMZKnWcjfCDSpo+ylXQ35rRPVRECcJl60ouyZw9011+tyo8YaAp+dD4K/KlJqdP6mWVKIx/n9e4avy7LAx73M4iYxre10mq+7fWwkpaICP2Iw7UXLIs6Mo017fnbAy+CHLg9pxK/5sPlh2u8rwr7FullZbZrInmGJ+juh7b6RPHLCdRcRiYCFc3LTCBloLK7COCKCpWWzIFfy0DRDxVtJV7p8wXhOrnt47orXa6rBRmRlwQLs6f/ptPOmYJJFkHg==");
		//f4gSEGChrx1qyBVGTgHcVk10|NlBxhN6ADhuBhLD+vcxwyuNwr6bEP+9Sh1TwV6XaMLV/+gVVlu/B8C3VVlb67v/oB3VFJ2yj+bQOoFc6ZJiIITmMZKnWcjfCDSpo+ylXQ35rRPVRECcJl60ouyZw9011+tyo8YaAp+dD4K/KlJqdP6mWVKIx/n9e4avy7LAx73M4iYxre10mq+7fWwkpaICP2Iw7UXLIs6Mo017fnbAy+CHLg9pxK/5sPlh2u8rwr7FullZbZrInmGJ+juh7b6RPHLCdRcRiYCFc3LTCBloLK7COCKCpWWzIFfy0DRDxVtJV7p8wXhOrnt47orXa6rBRmRlwQLs6f/ptPOmYJJFkHg==
		//12345678|h6gjnwh0xJzwTvAeQZ5RrD2jkKjaA9xEJRGacnlR63c2bE+vAMujX+0FNHJZNuR68dqpZCyEIhpmWQ7vVMjv/yZF4f4Dz1NoMCyCrhhnZeALuVe0Nto2c4/BnOsfxnuk5RGCham8BZpJV+bcn2gfMizC63r3m0qo3YW62biz+/mZD6P6AfET97PaqpFjZ0Zo9xUjsETjztnNf3NGLCYCLEEeESZCCO3hcDdzPNFMNPWfY95JkLNkhTujzhBkGSjITD/qx472AYlwiGIZxY32v2WMEVdaTxrGJu2fv2wjqnrJoE1PmnG8BBl031g1yZQnTQhgzUKXmiE1tmRynfMOGQ==
		logger.debug("Dec value |"+decStr);
		Assert.assertNotNull(decStr);
	}

	@Test
	//@Ignore
	public final  void testAxisPSPEncryption() throws Exception
	{
		AesUtil.setPublicKey("D:\\UAT_Production_Realease\\bharatqr-application\\BharatQR\\src\\main\\resources\\te1.cer");
		String encryptedKey = "Bvo0gjI18RtfI/QzDA/ngtbzhaZu0mCtUStz2AQV3yDeLKKlGcQBnkwnRCyD5tlzH5I0nmBDnGqdUcH7hIwXGmVvydq5SLCGTY3CJldCH3kabQKj1i4H7CmshvL3+VDaZoVuSnKPJrdFHQ9flyV0e4epa6/gJcw4Ja79JL5zcKQu29E7HPZKJALGMHVvPAjIoNzDljDGEZ+H++Kl8nAAR2nAhlmEfTg7vv0Zxx20IWBcnaCraqybtcMjlSTTwvl6dRrHjnJG9CGLClfYlh8OE6vMzkdoE76B8D1b+KrmzfwJx01Hl9FquVdrUvTZJynGHVzQ0ZKdPfg5DXRNvNP2ig==";
		AesUtil aes = new AesUtil();
		aes.init(encryptedKey, null, null);
		//aes.decrypt("wxp3XXcqmY9JD4f+onyy1z2wze98b09h5d7BQFvjQfIig4JRqKXEnapaD+tDwbl2Mnp/JJ4yzHRjs9t9yT9wy9nuN2VI+rElSyv2iS1BD0VymFpcY8tLPD3zE7vmPyd949R6sesZ+fqa3cxcigdJqi4Qt2l7trbZxwZUe0shFGAVeyRAa0eaiOfEYF/CxCUq41OVksYVXvjkGBuZ1TH54ZtTPCHQ9Reb6ojJCQOL1uqs1KJAPKQCIesaK7vXQi0MTnwrzxY70ICS7tFLGcMIthHh9PH5KK0FPqbrJerVjT4/sEALV6JUbJaTqLm2PXFS/6gyZ/r9ZwOSQtS+xcb5o99Pz8Gbg1AIMRrjWYVXqmbZt4++NX8IbwAH3WFSMe8BhEZ8QTi4DS79+ldjbGpIcDkFatrrZPwR9gLiiCNRevMozjvlEhMlCf5G5PXs/qQVb/wQ/vRLeyee/02rNEtb0JByU6kcSoGkpt84Q9XEJzF8NlwozNxvOWb12if9lZVBljNUHBPA/W1+ONHEduF+3mG0o4TaxoFtX+Cl11d/D873yu9Fth2OyKS7giweC8ulMIuVI/sNZyjgEjO/LKPrG1cZQqpDGkyHkvD33mVaqeDAIxlfBQn+9y+HjtFjtBQripN2Zqq6Y3ffjspgue/rRzqTzW4KimgAA6xnYZhheTjwGrIyH1u6NTyisSDRUd1Abls0m69cLRk42kY6hnvw3Mif1NtIB+kXgahUpdsmfrGtOcjr0FFWFLX5EtmHNeKrz8s9Y4n2xM472IcmC/Hw+kaPJifA2hAq65IpkVkwHMmm29H7SoMgNTxgKsSQsNV/VlAxXHLCYcUTeC1QIuVmH36o+MxOEK4xnHbHlwgdUDqRUT/NHPwxgFL6DWNIwxwyhrrCc0tJyhGCmDp+7XHUze5siX9ItrEMcPSfbEU6U3xmzlOiF7QTe1N2DoGlbNldD9xapjNvVKon/dj48XMWQKDoe6RqLVW1BVebGixFgYCCvl1QNu8Kq4KtRkylJeQh/N1dc5Dp2XClfA7QigRZQhpv8dFysiomEgWwXIuyA1zHIiLX/OQVkBEXPEL5dvA+dVoI2HZgOsw3TSab47qoNBMBB13AiDliayL1rwBlf3qpqqq8FuzRgQ41U8sMBKrUVtVVkbr1cswZalo7IH+I8A==");
		String encData = aes.encrypt("{\"merchId\":\"075010184200367\",\"merchantVpa\":\"wsbimab.075010184200367-12345678@sbi\",\"customerVpa\":\"bandhan@bandhan\",\"merchantTransactionId\":\"0740010010029036100260000000949\",\"transactionTimestamp\":\"2018-03-06 13:06:08\",\"transactionAmount\":\"1.00\",\"gatewayTransactionId\":\"SIB42708262702E4639A0DD75977B62AFC1\",\"gatewayResponseCode\":\"00\",\"gatewayResponseMessage\":\"Success\",\"rrn\":\"806513346800\",\"checksum\":\"6069fe14b088e38b781c054f10a3e0401ec81c26baf7595843e4b3b9df11cfa0\"}");
		logger.debug("enc data :"+encData);
		encData=new String("qeKP62icn7G4Bzq+bMmT3nh/2lb98kkOQ4GivBHUGOkKhTfN1I0UMgD3qWwlrkfUY8gt80HLv7+FHn6bNftdijUlFcqiOZxNpdXIjgpkGfge0r30s6Q1/hRK7aFgUkKGrV8x8tP53LY6odUYUROT06KMIYNJqrf7lffSn6Z+Y+7ujG5p88cWCEbMK7dMdq/QQs0SurK6e9dF/rFNrbcV++Z2meeiEi7MN31DbT9nUjCVu30iaycMsHvhFCLKmheGdxQFEpLZqQmVvkKHrh7+k1z5G7Dlto+XhXVOfIuCYBa1+338xVx8lYvbRAGxc8pCa0Of0mqM6FfiVsjJtOVFaD6oxNku0f/jDTrwglhfNsKo4qpqj9jtqjsoGWvC6Z+bv0ufjqW5mlaEmDEdAn4jZgQHcZhagoXpX/qdB1G+NUWKfEUbHhu3QFHiSKkXV28b3BQmDAcKiQt4OvJ0RHu+TiHFXecIUhCiikyZV8Hjo99fWksBgiGaZOfQqUI7IQ1OPmMsAmTFZ0sv3f2jDWBhBxQQwPAMkOk9MuKJM9IV6XvS8QX02L87Qe5+UpTkugdli+inNTOQ5EFOJnfpNK8uCZuKbVwqzC6j9cynhgtxPRBE/VsHkjTNxW1DMdWeCT3V");
		String decData = aes.decrypt(encData);
		logger.debug("dec data :"+decData);
	}

	@Test
	@Ignore
	public final  void testAxisPSPEncryption1() throws Exception
	{
		AesUtil.setPublicKey("D:\\WL-DATA\\documents\\PROJECT\S\\mvisa_merchant_workspace\\bharatqr-application\\BharatQR\\src\\main\\resources\\irctc.cer");
		String encryptedKey = "jHgxt4bEQnu7Az4x7dJZM4kdjKe1A2Z3ZUFH8aQxUCf6FkyEt8vFg8KTOA6JE60S4Ei9O2x5UCJVmMphbU7OSus20Qmy4PZEu6OlJZTwjoZTIsaG2ZubO3URoErRyFZ3tWW3AyXzx25uIjmxrumo5YIgQKph3CEOlcToOH/k5Wfi6fpWYzrtQ06oPDtvqDQ10e3u2eGCsvJwpqeyexw2ccLkkwuXS5QZ4OuwbzYop/iJbE0UQOIePX1IGFcL1Yndcmqvw6U7BLoTg86DmVbgd/Sq4Zs40BV1BdyiHmBqvYRb6pcEXg//58llLswqhStD7CZV7NuNrkXusXZlMhnWQA==";
		AesUtil aes = new AesUtil();
		aes.init(encryptedKey, null, null);
		//aes.decrypt("wxp3XXcqmY9JD4f+onyy1z2wze98b09h5d7BQFvjQfIig4JRqKXEnapaD+tDwbl2Mnp/JJ4yzHRjs9t9yT9wy9nuN2VI+rElSyv2iS1BD0VymFpcY8tLPD3zE7vmPyd949R6sesZ+fqa3cxcigdJqi4Qt2l7trbZxwZUe0shFGAVeyRAa0eaiOfEYF/CxCUq41OVksYVXvjkGBuZ1TH54ZtTPCHQ9Reb6ojJCQOL1uqs1KJAPKQCIesaK7vXQi0MTnwrzxY70ICS7tFLGcMIthHh9PH5KK0FPqbrJerVjT4/sEALV6JUbJaTqLm2PXFS/6gyZ/r9ZwOSQtS+xcb5o99Pz8Gbg1AIMRrjWYVXqmbZt4++NX8IbwAH3WFSMe8BhEZ8QTi4DS79+ldjbGpIcDkFatrrZPwR9gLiiCNRevMozjvlEhMlCf5G5PXs/qQVb/wQ/vRLeyee/02rNEtb0JByU6kcSoGkpt84Q9XEJzF8NlwozNxvOWb12if9lZVBljNUHBPA/W1+ONHEduF+3mG0o4TaxoFtX+Cl11d/D873yu9Fth2OyKS7giweC8ulMIuVI/sNZyjgEjO/LKPrG1cZQqpDGkyHkvD33mVaqeDAIxlfBQn+9y+HjtFjtBQripN2Zqq6Y3ffjspgue/rRzqTzW4KimgAA6xnYZhheTjwGrIyH1u6NTyisSDRUd1Abls0m69cLRk42kY6hnvw3Mif1NtIB+kXgahUpdsmfrGtOcjr0FFWFLX5EtmHNeKrz8s9Y4n2xM472IcmC/Hw+kaPJifA2hAq65IpkVkwHMmm29H7SoMgNTxgKsSQsNV/VlAxXHLCYcUTeC1QIuVmH36o+MxOEK4xnHbHlwgdUDqRUT/NHPwxgFL6DWNIwxwyhrrCc0tJyhGCmDp+7XHUze5siX9ItrEMcPSfbEU6U3xmzlOiF7QTe1N2DoGlbNldD9xapjNvVKon/dj48XMWQKDoe6RqLVW1BVebGixFgYCCvl1QNu8Kq4KtRkylJeQh/N1dc5Dp2XClfA7QigRZQhpv8dFysiomEgWwXIuyA1zHIiLX/OQVkBEXPEL5dvA+dVoI2HZgOsw3TSab47qoNBMBB13AiDliayL1rwBlf3qpqqq8FuzRgQ41U8sMBKrUVtVVkbr1cswZalo7IH+I8A==");
		String encData = aes.encrypt("kunal");
		logger.debug("enc data :"+encData);
		encData=new String("+zeeLiKkR4aTUczEQvsjJsfss/QWSvelL13VODQvPBbwODDTvYwFJ2G6BNmQIkBG8hHnOafVlvwutjQGGi+YjIadUaTJUvZ6PpeM9iob15t0U83ZPGEmFKt9N1yVe7ss4UsHKNxFTHm8ce7C5s6743/JQhHyYIvk+oX2IODpZWgJvfbaQjhaZ9MC0aMaFX46LlX4Jhc1DnRCTZCUpgkBE9yleH5wDy+WhFQ4W+h5SZQhQaJZn4poXRF8NLiO0h+a9PU1XHNbSxUC+eW7vjWO9JOY/4Jwfv/9HNanU3/sVWRi97xskNDxDraq/B/KwL5J591lYZgeZddWt/RRKlDpyTpEN+VAYxhaeJiTd7ci82MeI4KrRABGbSruigJjHEGM1XbTPmeMOHOOhQCpNHAzsj6ym5dkCfbGfexTSBtvrtg8WkG+hMAanr4yxz/mApVs7oz/uWjW5sVv/rTPN6QImDpzRQzeij2YZ7B2KI1V023AR5psEct7p12ZC6TNHkM2Hq3PpxltxNB2Ghzj89cV4S6EOqlNlVlCX5Fm3bSjDRGWU4APbiVQ8U8wQYu8tM1NaNPNlAX5Oi289acvJxh5nsnhOYHteXrlaMrQqSb69EYe37j6xxcldT1ivCDBNz1Vw/hTztCQSvvZLO5U8DflcJBkSJtmvRPy5DS+N8Gb6+xCsn3t9r3mBcF6Dh5A6owk0fqy+h79vyI/fY97g50xZChJmO2kCDRnCXMUgOdl+mIMp4lIM7g57vTiDvtdZGpFCIuPInAMg6VM1sbReE08W22MIMIL01Qjv6b6MzlHuHtWJZUAyg5YbJbDGaAgXSLRWxcNsM5UH2h80THZLfPHAQmbVUBe/R07bgHCz9GrYlswZlzQ4HdP5kt0aKF9AhKY8mMnj0GeLPFEwxKMd6BJpw+HAURJp7d4DGIUS9Eu/jA4fSa0PyIhUXp8UHGF4xgeVOqbr5LCHkHHca0GXUHT5KxgRrpN7U1dlcdhB0NU6WiRENsoW8vf/kT64AydPN4x+d0F0DsRTQdOQHSGIctAhwexFDHCMfxfBxDve5f4U/Ir8JepfNBab+IG7cH+w5vO6te58kBcm4rmC+z20rrHYFU3KqM4FZCCEekEWi/xm8WbGiHcRDiwI1y5TSC6zkIlchzdxUsRQTy1KxwA==");
		String decData = aes.decrypt(encData);
		logger.debug("dec data :"+decData);
	}


	@Test
	@Ignore
	public final void testMultiBankPSPEncryption() throws Exception{

		AesUtil.setPublicKey("D:\\QR_CODE_WS\\bharatqr-application\\BharatQR\\src\\main\\resources\\paymod223.cer");
		String encryptedKey ="Y2sxMwW1o9fhMSvUU1LA6YmPACXvobh4ruG7456bdEMLzdALyftB0DTA3E0Bc8MSWO7F0NCBORrUIKa4nDkJg3+plznl2VRZ9/hbfWtlxa0/PCgWiN82EfnC6yB5/KQPcQOpsegBp7xUkIf4ZKDYjUQaKQMHeYmPmaAe0O/wEF/LDaURdb4ZwoReo4374gvWSEgpvke7v0DVx66GZY8rtDD4jvPGF97HKkBmofG3W+CvgIvTbTZhvLrGt22TNYe4Q/W9LFjbHY/JpHQkL0RDOBHw/UWUpgHNf7z5bzXM+wdhMbuz4Z9ctizUnR7EiTNv+ghx5Jr8plKJ8kl+p0MdlA==";
		AesUtil aes = new AesUtil();
		aes.init(encryptedKey, null, null);
		String data="{\"merchId\":\"037033001161026\",\"merchantVpa\":\"abcmerchant@xbank\",\"customerVpa\":\"padma@xbank\","
				+"\"merchantTransactionId\":\"7193192447592261234\",\"transactionTimestamp\":\"24-MAR-17\","
				+"\"transactionAmount\":\"72\",\"gatewayTransactionId\":\"XXI91123456789032025071490711853337\","
				+"\"gatewayResponseCode\":\"00\",\"gatewayResponseMessage\":\"Success\",\"rrn\":\"703118109867\","
				+"\"checksum\":\"b345c7d41e1d4a2819ceaaba009f046d74119ac8e49e8dcd94d8319458da566d\"}";
		String encData=aes.encrypt( data);
		logger.debug("enc data :"+encData);
		encData=new String("wFut4DpxGPD4zpRECR66CughvKjucQET0wEobCYjp7bK6CiHTG7nghXDSH/YmqV5zZXV7dCILQRFb7kOlYY3Mj/KIY0FJGGecne7pnpCTL8h0YIGwmfn9UaVNoi6kaFf67mWdBN3iV1v9JRdF8VJOgpU+tKnfxOZXnr3V1/2C8WnexsIzrd4VC5Si3SXj/HhaHEJ2gvEF+sGEZ7tc6ZBHXg9reAqicl4LdIbHummtuDfT3RIxiUGtQuYo590tNCaHqMmIpDyFe4PotTh1wZg9bIQa6fKbSRvsfuh0hiwYrMq0ICewqP4dTHk8lMGi7BncBHz0de3UInyW+b2Ct9xorZUxTv9b/+CQk/dgrFun/K/LyKVpfms/39zKVQJVX/FkFUaEHG1NVxZp/ixtKvaxSAyovhYSUaC/STGaremw/cydWHSN8k+i5OvXvCKhcnnnxaaJ8v2KPLT3WO+RiaVEkqOKueE9w8buXM/LeT1TS782a6jowxAbhZBJbSZx5PG+zHVPNNjVOrlxOTD2BBp1RbJBo5Bylnfj3WpKIi1j1y6bm/zTRAxsfat1RipBoKH");
		String decData = aes.decrypt(encData);
		logger.debug("dec data :"+decData);
		
		
	}



}
*/