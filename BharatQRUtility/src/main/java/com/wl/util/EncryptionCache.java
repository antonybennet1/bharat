package com.wl.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wl.util.config.ApplicationConfig;
import com.wl.util.config.BankConfig;

public class EncryptionCache {
	
	private static final Logger logger = LoggerFactory.getLogger(EncryptionCache.class);
	
	public static ConcurrentHashMap<String, AesUtil> map = new ConcurrentHashMap<String, AesUtil>();
	
	/*static{
		load();
	}*/
	public static void load()
	{
		/*//Uncomment code for Insta Merchant Onboard
		AesUtil aes = new AesUtil();
		//System.out.println("configuring 00031");
		String encryptedIv = BankConfig.get("00031", "instaEncryptedIv");
		String encryptedKey  = BankConfig.get("00031", "instaEncryptedKey");
		//String encryptedIv = "csYOn6gfFbuqdIGI9bkpIovDAxO7mwvcbCadq5XY8f9vRdxPzd85NGLBDZ8ETz6C8ORGg5PFSoF/cP5Csi03wSa1aNu8X43lXxdz4zXNuMdEz5NIGcKy+THxbJkc0JB84AdCMqZmnvhSecA1+ZzjbwPdRsVAQ2gy2oKKu1yhcd6qkv1b1yZBjDTR7pRQKr4ho0CXolD5kX+OPy5J9mxuzbkCc20X9dRwJm76jZlUZgXiX19ZoL6foHwbq1xIE0EAUKUfAQOKUDtP0q3j5yLwEsm6uYxxWNWDdeu0bKxA7NOOTQvKTfTAls0R70ErEO7h9y2NyabE4leO/VTsW80ggQ==";
		//String encryptedKey = "WAERagRGUDC6nQJOcs3S2Q71wMxmId8qTInlsQKhVqgoXfA/MdDBT8Kb6rKOr1ZSOHF2TPYLDskXwssqak8mAgQZJalwiRTQRpW5Md9e2ZlqAUwe9d0h68t0Q/mSKLR78YAMM3NLbYl1y38MhieB2Kbey0E1dg9ZDZhYrGArzTb8NAEEfVMZwTVRk7JDE2gRuTD5/FSC0ctxcAXHVCwGXdw8bAvAYvhLgzG15BeLjjGwfX8jSQZk/dMWuZ92YVdGl93truv2pareR3gfzjPacY3Mmo0cFd4Iy13wLlBXdsNedCIybGbQlE0h77tdWNXhUerXkCeY2q/k82KK8jxCXA==";
		try {
			aes.init(encryptedKey, encryptedIv, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("00031", aes);
		*/
		/*Following added for Consumer PAN encryption*/
		AesUtil custPanEncryption = new AesUtil(); 
		try {
			custPanEncryption.setDataHexed(true);
			custPanEncryption.init(ApplicationConfig.get("customerPanEncKey"), null, null);
		} catch (Exception e) {
			logger.error("Exception in loading PAN encryption key",e);
		}
		map.put("PAN", custPanEncryption);
		logger.debug("PAN encryption key loaded");
		/* Following added for Consumer PAN encryption */
		
		
		AesUtil worldlineEnc = new AesUtil();
		/*encryptedIv = "h3nCoenWQhiutoyc/TUfGmIC2CJMb7KhfmjKoqrpVySAi+jnj0HRgAtEPvaubfgMH/bIMtBfDYwSRcB90VU3elsxtZ1msB5orMLHAh+GdO89SKy5U/vQJBW48qI0x/l+yCv89IHevlDqmvg6s6MzNd1fqwMaT2qfg+aSOFuNPXK9WDSUdqCBNgLt8JF9McWkXHA26q//wuu/CnZMri8zRRmtWLReFxlp4ktlj/eFnSnQ2iKxSrhOF/xwGSNp35Jck9zIuDmWFAzz6guyKyYTC07Zj3Bgl8+4EF66n/BVn6YG9oPXPxy8ILX0dKqlzwQuksl/WgiOvfbi8zbHZO2xeQ==";
		encryptedKey = "DCYlk3h1mYhqdS30l+A5I3dT4ddJ31D+c53JQIigVv8xyMdzKJJbNT3tebaNz+4lX7vEgNv36n5JuYu/T9d4JPH/WIlxQ9XZhOBDHeomCfp0GwLFOMueZIW5/DiemonEtBOhWPC59VdUYgsWhhf2T3ih9B0CW4q7uxI29YOR4A01Jn8V+g1RQll7+QfDnR++EhjUCwPBQXb5CQIep8zPRxvbWB8CKb8lTveINfELVB+oMbx1gkBu8Pib3DY6bcGAlFtztbVbL8g51+ZYRjHwVwl+lClNPDX7B4K27JroSmk7IRS1dd32ZvWYCJWQJdHDEaHjgmGIKZQSVNz9/Wh7Fg==";
		String encryptedSalt = "RT6GEzrybBVWlokXJgF0VTpyVx0hwuMFnYkW6LEK4GlKuoaizM23UrfRvOhUgRVhGVeL0JmNsZsJKdgjI2Yhpk6mC+uHmUDn64EZKpFd5qxcYDAJ+G0fbHzzF3vt50io9Ax6Opi8drElGTPPGKUEePSy66juLcVfAjabulJ47MjMwDigFLAQeTp7VjzX2zT2MJDBnCPN3+AbSjmsbyjGi/zgPGTOQ1IPWPptQoUUJgP5oGqYcSN6ZraHBFv95tO8Ad+iE/aEPoNgq4MKEIaN34ESJW8UI5z/1YM/6Qc8bsRLyrX1OpzK5/TUEr+mU9jEdxg6wVCp5rk2cBRiKm3JmQ==";*/
		String encryptedIv = ApplicationConfig.get("app_iv");
		String encryptedKey = ApplicationConfig.get("app_enc_key");
		String encryptedSalt = ApplicationConfig.get("app_salt");
		try {
			worldlineEnc.init(encryptedKey, encryptedIv, encryptedSalt);
		} catch (Exception e) {
			logger.error("Exception in loading APP encryption key",e);
		}
		map.put("APP", worldlineEnc);
		logger.debug("APP encryption key loaded");
		
		/*Following added for AXISPSP encryption*/
		AesUtil axisPspAes = new AesUtil(); 
		try {
			axisPspAes.init(BankConfig.get("AXISPSP", "pspRequestAESKey"), null, null);
		} catch (Exception e) {
			logger.error("Exception in loading AXIS PSP encryption key",e);
		}
		map.put("AXISPSP", axisPspAes);
		logger.debug("AXISPSP encryption key loaded");
		/* Following added for AXISPSP encryption */
		
		/*Following added for SBIPSP encryption*/
		AesUtil sbiPspAes = new AesUtil(); 
		try {
			sbiPspAes.setKeyHexed(true);
			sbiPspAes.setDataHexed(true);
			sbiPspAes.init(BankConfig.get("SBIPSP", "pspRequestAESKey"), null, null);
		} catch (Exception e) {
			logger.error("Exception in loading SBIPSP encryption key",e);
		}
		map.put("SBIPSP", sbiPspAes);
		logger.debug("SBIPSP encryption key loaded");
		/* Following added for SBIPSP encryption */
		
		/*Following added for YESPSP encryption*/
		AesUtil yesPspAes = new AesUtil(); 
		try {
			yesPspAes.init(BankConfig.get("YESPSP", "pspRequestAESKey"), null, null);
		} catch (Exception e) {
			logger.error("Exception in loading YESPSP encryption key",e);
		}
		map.put("YESPSP", yesPspAes);
		logger.debug("YESPSP encryption key loaded");
		/* Following added for YESPSP encryption */
		
		/*Following added for BOB encryption*/
		AesUtil bobPspAes = new AesUtil(); 
		try {
			bobPspAes.init(BankConfig.get("BOBPSP", "pspRequestAESKey"), null, null);
		} catch (Exception e) {
			logger.error("Exception in loading BOBPSP encryption key",e);
		}
		map.put("BOBPSP", bobPspAes);
		logger.debug("BOBPSP encryption key loaded");
		/* Following added for BOB encryption */
		
		/*Following added for SIB encryption*/
		AesUtil sibPspAes = new AesUtil(); 
		try {
			sibPspAes.init(BankConfig.get("SIBPSP", "pspRequestAESKey"), null, null);
		} catch (Exception e) {
			logger.error("Exception in loading SIBPSP encryption key",e);
		}
		map.put("SIBPSP", sibPspAes);
		logger.debug("SIBPSP encryption key loaded");
		/* Following added for SIB encryption */
		
		/*Following added for IDBI encryption*/
		AesUtil idbiPspAes = new AesUtil(); 
		try {
			idbiPspAes.init(BankConfig.get("IDBIPSP", "pspRequestAESKey"), null, null);
		} catch (Exception e) {
			logger.error("Exception in loading IDBIPSP encryption key",e);
		}
		map.put("IDBIPSP", idbiPspAes);
		logger.debug("IDBIPSP encryption key loaded");
		/* Following added for IDBIPSP encryption */
		
		/*Following added for CBI encryption*/
		AesUtil cbiPspAes = new AesUtil(); 
		try {
			cbiPspAes.init(BankConfig.get("CBIPSP", "pspRequestAESKey"), null, null);
		} catch (Exception e) {
			logger.error("Exception in loading CBIPSP encryption key",e);
		}
		map.put("CBIPSP", cbiPspAes);
		logger.debug("CBIPSP encryption key loaded");
		/* Following added for CBIPSP encryption */
		
		/*Following added for KOTAKPSP encryption*/
		AesUtil kotakPspAes = new AesUtil(); 
		try {
			kotakPspAes.init(BankConfig.get("KOTAKPSP", "pspRequestAESKey"), null, null);
		} catch (Exception e) {
			logger.error("Exception in loading KOTAKPSP encryption key",e);
		}
		map.put("KOTAKPSP", kotakPspAes);
		logger.debug("KOTAKPSP encryption key loaded");
		/* Following added for KOTAKPSP encryption */
		
		
		/*Following added for BOIPSP encryption*/
		AesUtil boiPspAes = new AesUtil(); 
		try {
			boiPspAes.init(BankConfig.get("BOIPSP", "pspRequestAESKey"), null, null);
		} catch (Exception e) {
			logger.error("Exception in loading BOIPSP encryption key",e);
		}
		map.put("BOIPSP", boiPspAes);
		logger.debug("BOIPSP encryption key loaded");
		/* Following added for BOIPSP encryption */
		
		/*Following added for INDUSPSP encryption*/
		AesUtil indusPspAes = new AesUtil(); 
		try {
			indusPspAes.init(BankConfig.get("INDUSPSP", "pspRequestAESKey"), null, null);
		} catch (Exception e) {
			logger.error("Exception in loading INDUSPSP encryption key",e);
		}
		map.put("INDUSPSP", indusPspAes);
		logger.debug("INDUSPSP encryption key loaded");
		/* Following added for INDUSPSP encryption */
		
		
		/*Following added for CANARAPSP encryption*/
		AesUtil canaraPspAes = new AesUtil(); 
		try {
			canaraPspAes.init(BankConfig.get("CANARAPSP", "pspRequestAESKey"), null, null);
		} catch (Exception e) {
			logger.error("Exception in loading CANARAPSP encryption key",e);
		}
		map.put("CANARAPSP", canaraPspAes);
		logger.debug("CANARAPSP encryption key loaded");
		/* Following added for CANARAPSP encryption */
		
		
		/*Following added for OBCPSP encryption*/
		AesUtil obcPspAes = new AesUtil(); 
		try {
			obcPspAes.init(BankConfig.get("OBCPSP", "pspRequestAESKey"), null, null);
		} catch (Exception e) {
			logger.error("Exception in loading OBCPSP encryption key",e);
		}
		map.put("OBCPSP", obcPspAes);
		logger.debug("OBCPSP encryption key loaded");
		/* Following added for OBCPSP encryption */
		
		
		/*Following added for DBCPSP encryption*/
		AesUtil dbcPspAes = new AesUtil(); 
		try {
			dbcPspAes.init(BankConfig.get("DBCPSP", "pspRequestAESKey"), null, null);
		} catch (Exception e) {
			logger.error("Exception in loading DBCPSP encryption key",e);
		}
		map.put("DBCPSP", dbcPspAes);
		logger.debug("DBCPSP encryption key loaded");
		/* Following added for DBCPSP encryption */
		
		/*Following added for UBIPSP encryption*/
		AesUtil ubiPspAes = new AesUtil(); 
		try {
			ubiPspAes.init(BankConfig.get("UBIPSP", "pspRequestAESKey"), null, null);
		} catch (Exception e) {
			logger.error("Exception in loading UBIPSP encryption key",e);
		}
		map.put("UBIPSP", ubiPspAes);
		logger.debug("UBIPSP encryption key loaded");
		/* Following added for UBIPSP encryption */
		
		/*System.out.println(" Concurrent HashMap :"+ map);
		
		Iterator<String> it =map.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			System.out.println(key);
		}*/
		
		/*
          Following key added for BandhanBank
           */
		
		AesUtil bandhanPspAes = new AesUtil();
		try{
			bandhanPspAes.init(BankConfig.get("BANDHANPSP", "pspRequestAESKey"), null, null);
		}catch(Exception e){
			logger.error("Exception in loading BANDHANPSP encryption key", e);
		}
		map.put("BANDHANPSP", bandhanPspAes);
		logger.debug("BANDHANPSP encryption key loaded" +  bandhanPspAes);
		
		
	}
	public static AesUtil getEncryptionUtility(String bankCode)
	{
		
		
		return map.get(bankCode);
	}
}
