/*package com.wl.util;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDesUtil {

	private static final Logger logger = LoggerFactory.getLogger(TestDesUtil.class);
	public static final String encryptedKey = "NlBxhN6ADhuBhLD+vcxwyuNwr6bEP+9Sh1TwV6XaMLV/+gVVlu/B8C3VVlb67v/oB3VFJ2yj+bQOoFc6ZJiIITmMZKnWcjfCDSpo+ylXQ35rRPVRECcJl60ouyZw9011+tyo8YaAp+dD4K/KlJqdP6mWVKIx/n9e4avy7LAx73M4iYxre10mq+7fWwkpaICP2Iw7UXLIs6Mo017fnbAy+CHLg9pxK/5sPlh2u8rwr7FullZbZrInmGJ+juh7b6RPHLCdRcRiYCFc3LTCBloLK7COCKCpWWzIFfy0DRDxVtJV7p8wXhOrnt47orXa6rBRmRlwQLs6f/ptPOmYJJFkHg==";
	public static final String encryptedIv = "h6gjnwh0xJzwTvAeQZ5RrD2jkKjaA9xEJRGacnlR63c2bE+vAMujX+0FNHJZNuR68dqpZCyEIhpmWQ7vVMjv/yZF4f4Dz1NoMCyCrhhnZeALuVe0Nto2c4/BnOsfxnuk5RGCham8BZpJV+bcn2gfMizC63r3m0qo3YW62biz+/mZD6P6AfET97PaqpFjZ0Zo9xUjsETjztnNf3NGLCYCLEEeESZCCO3hcDdzPNFMNPWfY95JkLNkhTujzhBkGSjITD/qx472AYlwiGIZxY32v2WMEVdaTxrGJu2fv2wjqnrJoE1PmnG8BBl031g1yZQnTQhgzUKXmiE1tmRynfMOGQ==";
	@Test
	@Ignore
	public final void testEncrypt() throws Exception {
		
		DesUtil.init(encryptedKey, encryptedIv);
		String str = DesUtil.encrypt("{\"MerIden\":\"1\",\"Mercreationdate\":\"03-APR-17\",\"AppNumber\":\"129930598234\",\"Mercustid\":\"46234\",\"Mertype\":\"NRM\",\"Mersegment\":\"SEGMENT\",\"Mcc\":48,\"Tcc\":21,\"Pan\":\"45678344234\",\"Melegalname\":\"GANESH\",\"Medbaname\":\"DERE\",\"Zonecode\":81,\"Branchcode\":\"32\",\"Grossmdrtype\":\"Y\",\"Merdob\":\"03-APR-17\",\"Merbusentity\":\"PQR\",\"IACPerson\":\"SATISH\",\"IAAdd1\":\"PUNE\",\"IAAdd2\":\"SATARA\",\"IAPincode\":411033,\"IATelephone\":7564423443,\"IAMobileno\":9545822223,\"IAEmail1\":\"GANESH@GMAIL.COM\",\"RAAdd1\":\"HUDCO\",\"RAAdd2\":\"CIDCO\",\"RAPincode\":\"411033\",\"RACity\":27,\"RAMobileone\":\"9545822223\",\"RAMobiletwo\":\"9545234234\",\"Paymentby\":\"AC-ZME\",\"Accno\":1234567345,\"Acclabel\":\"XYZ\",\"Beifsccode\":\"AXIS8734\",\"Beaccname\":\"GANESH\",\"Beaccnumber\":\"345567564353\",\"Paysoldid\":1,\"StmtReqtype\":\"E\",\"Stmtfrq\":\"Y\",\"Merdocname\":\"Y\",\"TerminalType\":\"E\",\"Edcmodel\":\"INDIA QR CODE\",\"domupto1000onus\":\"1\",\"domupto1000offus\":\"1\",\"Domoffusless2000\":\"2\",\"Domonusless2000\":\"1\",\"Domoffusgret2000\":\"1\",\"Domonusgret2000\":\"3\",\"BankSubRate\":\"3\",\"LeadgenId\":\"3456\",\"Secode\":\"3456\",\"ProfitabilityStatus\":\"YES\",\"Approvedby\":\"GANESH\",\"Postfacto\":\"Y\",\"Nonopecano\":\"3455\",\"Riskapp\":\"Y\",\"Tipflag\":\"Y\",\"Convfees\":\"2\",\"Convfeesamt\":\"2\",\"Convfeesamtper\":\"1\",\"MerchatMobileNumber\":\"1234567889\",\"Merchantemailid\":\"AXIS@GMAIL.COM\",\"ReferralCode\":\"12345\",\"BankCode\":\"00031\",\"IACITY\":35,\"IALOCATION\":3245,\"NoOfTerminal\":\"1\",\"Rentalfee\":\"3\",\"Setupfee\":\"4\",\"Joiningfee\":\"4\",\"Othercharges\":\"3\",\"Bebankname\":\"AXIS\",\"IdenType\":\"PAN\",\"Refund\":\"Y\",\"Creditcardpremium\":\"2\",\"Creditcardnonpremium\":\"2\",\"p_InstallationAddressFlag\":\"Y\"}");
		logger.debug("Enc value |"+str);
		Assert.assertNotNull(str);
	}

	@Test
	public final void testDecrypt() throws Exception {
		AesUtil aes = EncryptionCache.getEncryptionUtility("00031");
		//String str = AesUtil.decrypt("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");
		    String str = aes.decrypt("0m9AyjcYJWxah/geiBOFcSL03YyqFEg8KD5u6fBbYwAd+IkGjpVjCTbrxR+Fe5aI");
		logger.debug("Dec value |"+str);
		Assert.assertNotNull(str);
	}

}
*/