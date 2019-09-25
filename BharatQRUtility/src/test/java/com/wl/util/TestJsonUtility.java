/*package com.wl.util;

import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wl.instamer.model.MerchantOnboard;
import com.wl.instamer.model.MerchantOnboardResponse;
import com.wl.instamer.model.Request;

public class TestJsonUtility {
	
	private static final Logger logger = LoggerFactory.getLogger(TestJsonUtility.class);

	@Test
	@Ignore
	public final void testParseJson() {
		
		String s = "{\"merIdentification\":1,\"bankCode\":\"00031\",\"merCreationDate\":\"27-APR-17\",\"appNo\":129930598234,\"customerId\":\"46234\",\"merType\":\"NRM\",\"merSegment\":\"Corporate\",\"mcc\":48,\"tcc\":21,\"panNo\":45678344234,\"legalName\":\"BIKANER SWEET AND SNACKS\",\"dbaName\":\"BIKANER SWEET AND SNACKS\",\"zoneCode\":81,\"branchCode\":21,\"grossMdrType\":\"Y\",\"dob\":\"03-APR-17\",\"businessEntity\":\"Sole Proprietorship Concern\",\"instAddContactPerson\":\"SATISH\",\"instAddressLine1\":\"BIKANER SWEET AND SNACKS , G1 \",\"instAddressLine2\":\"Mumbai\",\"instCity\":35,\"instLocation\":3245,\"instAddPincode\":411033,\"instAddTelephone\":7564423443,\"instAddMobileNo\":9545822223,\"instAddEmail\":\"ganesh@gmail.com\",\"raAddressLine1\":\"BIKANER SWEET AND SNACKS , G1 \",\"raAddressLine2\":\"Pune\",\"raPinCode\":411033,\"raCity\":27,\"raMobile1\":9545234234,\"raMobile2\":0,\"paymentBy\":\"N\",\"accountNo\":1234567345,\"accountLabel\":\"XYZ\",\"bnfIfsc\":\"AXIS8734\",\"bnfName\":\"Ganesh\",\"bnfAccountNo\":\"345567564353\",\"bnfBankName\":\"AXIS\",\"paySoldId\":1,\"stmtReqType\":\"E\",\"stmtFrequency\":\"D\",\"merDocumentName\":\"Y\",\"terminalType\":\"E\",\"edcModel\":\"Bharat QR\",\"domUpto1000Onus\":1.0,\"domUpto1000Offus\":1.0,\"domOffusLess2000\":1.0,\"domOnusLess2000\":2.0,\"domOffusGret2000\":1.0,\"domOnusGret2000\":3.0,\"bankSubRate\":3,\"leadGenId\":3456,\"seCode\":\"3456\",\"approvedBy\":\"Ganesh\",\"profitabilityStatus\":\"Other\",\"postFacto\":\"Y\",\"nonOperCANo\":\"Y\",\"riskApproval\":\"Y\",\"tipFlag\":\"Y\",\"conFeeFlag\":\"F\",\"conFeeAmount\":11.5,\"conFeePercentage\":0.0,\"merMobileNumber\":1234567889,\"merEmailId\":\"bss@GMAIL.COM\",\"referralCode\":12345,\"numberOfTerminal\":1,\"joiningFee\":4.0,\"rentalFee\":3.0,\"setupFee\":4.0,\"otherCharges\":3.0,\"identificationType\":\"PAN\",\"isRefundAllowed\":\"Y\",\"pInstallationAddFlag\":\"Y\",\"creditCardPremium\":2,\"creditCardNonPremium\":2}";
		MerchantOnboard merchant =  (MerchantOnboard) JsonUtility.parseJson(s, MerchantOnboard.class);
		logger.debug("Merchant Details|"+merchant);
		
		s = "{\"RESPCODE\":\"0\",\"BANK\":\"00031\",\"REQUESTID\":\"7136181417546554\",\"DATA\":\"0m9AyjcYJWxah/geiBOFcY46jfZS+iNQ2tzgfMmSfnAAsBtqZg++IuqlVxytEJKc|42d89f3b71172dbf70ad808ca24530ca\"}";
		MerchantOnboardResponse merchantResp =  (MerchantOnboardResponse) JsonUtility.parseJson(s, MerchantOnboardResponse.class);
		logger.debug("Merchant Details|"+merchantResp.getRequestId());
		Assert.assertNotNull(merchantResp);
	}
	
	@Test
	@Ignore
	public final void testParseJson1() {
		
		String s = "{\"RESPCODE\":\"0\",\"BANK\":\"00031\",\"REQUESTID\":\"7150164221104924\",\"DATA\":\"0m9AyjcYJWxah/geiBOFcSL03YyqFEg8KD5u6fBbYwAd+IkGjpVjCTbrxR+Fe5aI|cd0a01566b48e3f1c3bc167cc0bc340b\"}";
		MerchantOnboardResponse merchant =  (MerchantOnboardResponse) JsonUtility.parseJson(s, MerchantOnboardResponse.class);
		Assert.assertNotNull(merchant);
		logger.debug("Merchant response|"+merchant);
	}
	
	@Test
	@Ignore
	public final void testGetErrorList() {
		
		String s = "{\"Error\":{\"Ids\":[\"ERWL001\"]},\"Success\":null}";
		List<String> errors = JsonUtility.getErrorCodes(s);
		Assert.assertNotNull(errors);
		logger.debug("Error Codes|"+errors);
	}

	@Test
	public final void testConvertToJson() {
		
		MerchantOnboard merchantDetails = new MerchantOnboard();
		merchantDetails.setMerIdentification(1);
		merchantDetails.setBankCode("00031");
		merchantDetails.setMerCreationDate("02-July-17");
		merchantDetails.setAppNo(867446L);
		merchantDetails.setCustomerId("123468");
		merchantDetails.setMerType("NRM");
		merchantDetails.setMerSegment("Corporate");
		merchantDetails.setMcc(267);
		merchantDetails.setTcc(21);
		merchantDetails.setPanNo(45678344234L);
		merchantDetails.setLegalName("BIKANER");
		merchantDetails.setDbaName("BIKANER");
		merchantDetails.setZoneCode(81);
		merchantDetails.setBranchCode(32);
		merchantDetails.setGrossMdrType("Y");
		merchantDetails.setDob("03-APR-17");
		merchantDetails.setBusinessEntity("Sole Proprietorship Concern");
		merchantDetails.setInstAddContactPerson("SATISH");
		merchantDetails.setInstAddressLine1("BIKANER SWEET AND SNACKS , G1 ");
		merchantDetails.setInstAddressLine2("Mumbai");
		merchantDetails.setInstAddPincode(411033);
		merchantDetails.setInstAddTelephone(8149718852L);
		merchantDetails.setInstAddMobileNo(1234567890L);
		merchantDetails.setInstAddEmail("Rahul@gmail.com");
		merchantDetails.setInstCity(35);
		merchantDetails.setInstState(123);
		merchantDetails.setInstAddDistrict(2);
		merchantDetails.setInstLocation(12);
		merchantDetails.setRaAddressLine1("Dadar");
		merchantDetails.setRaAddressLine2("Thane");
		merchantDetails.setRaPinCode(411033);
		merchantDetails.setRaState(14);
		merchantDetails.setRaDistrict(154842);
		merchantDetails.setRaCity(1067);
		merchantDetails.setRaMobile1(9545234234L);
		merchantDetails.setRaMobile2(9545234437L);
		merchantDetails.setPaymentBy("C");
		merchantDetails.setAccountNo(1234567345L);
		merchantDetails.setAccountLabel("NME");
		merchantDetails.setBnfIfsc("AXIS8734");
		merchantDetails.setBnfName("asdf");
		merchantDetails.setBnfAccountNo("12345678976465");
		merchantDetails.setBnfBankName("sadwq");
		merchantDetails.setPaySoldId(1);
		merchantDetails.setStmtReqType("E");
		merchantDetails.setStmtFrequency("D");
		merchantDetails.setMerDocumentName("Y");
		merchantDetails.setTerminalType("E");
		merchantDetails.setEdcModel("BHARAT QR CODE");
		merchantDetails.setDomUpto1000Onus(0.24);
		merchantDetails.setDomUpto1000Offus(0.24);
		merchantDetails.setDomOnusLess2000(0.45);
		merchantDetails.setDomOffusLess2000(0.46);
		merchantDetails.setDomOnusGret2000(1.0);
		merchantDetails.setDomOffusGret2000(1.0);
		merchantDetails.setBankSubRate(0);
		merchantDetails.setLeadGenId(1);
		merchantDetails.setSeCode("45345");
		merchantDetails.setProfitabilityStatus("Branch Prefer Price");
		merchantDetails.setApprovedBy("G");
		merchantDetails.setPostFacto("Y");
		merchantDetails.setNonOperCANo("Y");
		merchantDetails.setRiskApproval("Y");
		merchantDetails.setTipFlag("Y");
		merchantDetails.setConFeeFlag("P");
		merchantDetails.setConFeeAmount(1.0);
		merchantDetails.setConFeePercentage(1);
		merchantDetails.setMerMobileNumber(1234567889L);
		merchantDetails.setMerEmailId("bss@GMAIL.COM");
		merchantDetails.setReferralCode("123456");
		merchantDetails.setNumberOfTerminal(12);
		merchantDetails.setRentalFee(100);
		merchantDetails.setSetupFee(100);
		merchantDetails.setJoiningFee(10);
		merchantDetails.setOtherCharges(123456);
		merchantDetails.setIdentificationType("AADHAR");
		merchantDetails.setIsRefundAllowed("Y");
		merchantDetails.setpInstallationAddFlag("Y");
		merchantDetails.setCreditCardPremium(146);
		merchantDetails.setCreditCardNonPremium(12);
		
		Request req = new Request();
		String encData = "4503aeab9537dadb47954e49397b7288d540197f3734f8091007d2f7e91638d0fc17aafc531d471e54563511d065f9615e31f44bfb38e847aa89bd34e1da0722995bcd3d219801f5c19805b083cb36bcfdfb011c5794f483b7974d6f394340937a3710e3616e6d36904e74605ac152ccc3426a3f08417034800d2b78d4a67de922a3d657271b357439373e64275189586f4ec825ce4119bdc9452a54a891a80fb6a69d2fbbe0ce6e56d0ebc44f6b92d0e18f51fab0d3ee686cbeb4d254b84864b1cf66c74c7a25f5c9a5969406883f4d7f3cb8cb296d5d04363208cef77ab7534c8f8ecaff1f80fe9563bb049f2bb509b085a30a5475148cf258f884a703150b456567720e48ed5b38014e43a26c67fb5a738853db90bf8c50cbb5faca3cb0f03cedc0470e32218a301bb3ec00a6392fc4a951a94da3bd60c12fdc529e1f8c8e89dd390d2515914bb0f3df2eff5307f683a5a6d73059fc7f88d7197a4eb5b6146f106b9fc41fc9c37580c8181e49ff6e7fb9c5a16caef26a8d6106f4937a9639ed861c09ca44a6a658201d85322fc1e20676b6532039db2b0eb57d86867c32e51ddfd3ba3f24755282cdb33c58ae8b65de9dd3bc5fba34e7f6314cd0ec70cba9398664bef87030d6d978c22143715ff4eaaa478b72d19b9d491965f7b227f52f05b7e0c07ef6e3d2e99d788554b90b64317e96ad7dfac9922536284ad6527083e270f1f64f9f1feb6421853624537791e6ba35ecc1e181993931fe81fe7acfc2aaa78578560ccaa82cb02cbd2651829b48d740ae1be988c9445ff9a15837b5caa96424cb40076502348b5bbefe1afa4cbaefa8ba5fcec8a1bf5e3a17ff2d959b702a4ff0119249cde08158b893613fccc0143885d8dc14b8114346aa964d3b9886eb8c653699667ba8ba7ed30e80fb8006b64f94a1fd594523d454d80cbc4343e6259275af33b65abfa2dba3d8312d3c21def7283ec579c8bafc72e58054056998aa5184864a96542333a17dc83b2f4ee2fda108e15d59e4aa340a1f4efc41eaf65ca5804d763a41362d49b80084a88219ff5bf07175d0cf110d417b8bfafae9dd989d583cf3face7f44440434b792a8485b12b5e569bd3572845d9aed3407fcd5e97e750ffe84a14926e3c484fed538c94310fd718fb767258f07ae9bda1c43af16c8ea6d68671e37b48e2e5d87ece46397fc513d24d78015e6ffd9f613fb5b80309cd6344ca42bb0af3b1f5ce08b47e3df532e568d3663df00201e93f7662684f6428fed18a9ffadcdad92231aafcc6562da22f9cec167bdc29789a4fc7e0fb326913f7a845352e23ef0338cc271fe95b882fc18c338f1db423347bf67f1f2685f6cb27d3492fd57b2b13175f638d69d1b82a4017fdfdcdcc10d8923969f1fb0b50e789edf7df59538a219a5080032d7ff3089908b99de739e94f5e04deaa08d8f353853a6344441b0e1fc7ec2a313b90c32fbbc89b7297286f75994f1b84b61b71480ff081b99f7c2a463cfac3bbef7c900c198a7b2c18c404415e184e8bdc28e448f48e0627b6e2ae21c339ae4d725d33bfe9f5305eb7baafd2cb6a6e673f0279243e2347a706a6a7488fec107e178e59d65181ccc9b32a66424d8305a98f422e79756427e70e7fa32e26254df7c757f25957183291f41369791b4e853755fca48deebab31ec944a82003a185a20b1a7f684248bdf1d1235f33d035f6772184052ed5ca5f71e04db42a54b51dd2e8c4a56a619a0e56bba16d51e6ae381c5bc0f00e249c1565b6e004f62fa8b40057ffd0c7a5ae3da31586836820f346dfb2b7ee314f523e99d9ffe3d6c327f07c204f5abe0abbe027a0ae46b993191f9c7b5bc0173abab28b041a7e853e60282c6b4381a08a3ce1854024c92f9aad22029865530d078c7746bfefb28488c6f81fd6f04f24807c8d280b6e4469f5d387225f8c6ad752b16bb3d752ea3cde583d098386ec85f919241b97d12c1f7c07b5b4482a639290eb0f0a51c1d4548003286afddf35822a4b1fd39fcaa492818a642b8efa50e95c5ce05d18a160d39b28ad65f325e6c5122333e3fab995c8b5a040b175b04435743e478a4d2593216871b458407955fb1099e9bdddcd4e847b93cd5c6e51574401d1c20032c3241ebb083f9c21df006460f7620cbe487e962cd25228ffefb1c67b1f5bfa34f3ee6b0378722cf973fecd5e20a3c0e317af0215f1f1ea6d1283bc62bee33a6828e4524d96820ecb5d06e1cd6b1680081193c6ee649c8f22283fce820d240dae6bf7ae82ec320edec00f96eb0cd82e2f80cbc3f85972590255c7f6074690d0e12ca57861a8149f3d1b477e0c083ffd2b50e2567134a9ac62bf2fdb7f6bcabc691a27854d6d45e027d3a7a35b7cfd2e2053cd7951a180168d22a0f8ee056e9357d86e9337d3ed7c6";
		req.setBankCode("00031");
		req.setData(encData);
		String json = JsonUtility.convertToJson(merchantDetails);
		logger.debug("JSON string is |"+json);
		Assert.assertNotNull(json);
	}
	
	@Test
	public final void testConvertToJson1() {
		
		LinkedHashMap<String,Object> map = new LinkedHashMap<>();
		map.put("integer", 1);
		map.put("String", "String");
		map.put("decimal", 2.56);
		map.put("Boolean", true);
		
		String json = JsonUtility.convertToJson(map);
		logger.debug("JSON string is |"+json);
		Assert.assertNotNull(json);
	}

}
*/