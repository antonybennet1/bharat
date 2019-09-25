package com.wl.bharatqr.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.wl.util.AesUtil;
import com.wl.util.EncryptionCache;
import com.wl.util.config.AggregatorConfig;
import com.wl.util.config.ApplicationConfig;
import com.wl.util.config.BankConfig;
import com.wl.util.config.SmsConfig;

@Component("contextRefreshListener")
public class ContextRefreshListener implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger logger = LoggerFactory.getLogger(ContextRefreshListener.class);
	
	@Autowired
	private ApplicationConfig applicationConfig;
	
	@Autowired
	private BankConfig bankConfig;
	
	@Autowired
	private SmsConfig smsConfig;
	
	@Autowired
	private AggregatorConfig aggregatorConfig;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		// TODO Auto-generated method stub
		reloadConfig();
	}
	
	public void reloadConfig()
	{
		applicationConfig.init();
		bankConfig.init();
		smsConfig.init();
		aggregatorConfig.init();
//	    AesUtil.setPublicKey(ApplicationConfig.get("server_public_key_path"));
//	    AesUtil.setPublicKey(ApplicationConfig.get("C:/Users/velkumar/Desktop/te1.cer"));
	    AesUtil.setPublicKey("C:/Users/antonybennet/workspace/bharatqr-application/BharatQR/src/main/resources/te1.cer");
		EncryptionCache.load();
		
		//final String encryptedKey = "NlBxhN6ADhuBhLD+vcxwyuNwr6bEP+9Sh1TwV6XaMLV/+gVVlu/B8C3VVlb67v/oB3VFJ2yj+bQOoFc6ZJiIITmMZKnWcjfCDSpo+ylXQ35rRPVRECcJl60ouyZw9011+tyo8YaAp+dD4K/KlJqdP6mWVKIx/n9e4avy7LAx73M4iYxre10mq+7fWwkpaICP2Iw7UXLIs6Mo017fnbAy+CHLg9pxK/5sPlh2u8rwr7FullZbZrInmGJ+juh7b6RPHLCdRcRiYCFc3LTCBloLK7COCKCpWWzIFfy0DRDxVtJV7p8wXhOrnt47orXa6rBRmRlwQLs6f/ptPOmYJJFkHg==";
		//final String encryptedIv = "h6gjnwh0xJzwTvAeQZ5RrD2jkKjaA9xEJRGacnlR63c2bE+vAMujX+0FNHJZNuR68dqpZCyEIhpmWQ7vVMjv/yZF4f4Dz1NoMCyCrhhnZeALuVe0Nto2c4/BnOsfxnuk5RGCham8BZpJV+bcn2gfMizC63r3m0qo3YW62biz+/mZD6P6AfET97PaqpFjZ0Zo9xUjsETjztnNf3NGLCYCLEEeESZCCO3hcDdzPNFMNPWfY95JkLNkhTujzhBkGSjITD/qx472AYlwiGIZxY32v2WMEVdaTxrGJu2fv2wjqnrJoE1PmnG8BBl031g1yZQnTQhgzUKXmiE1tmRynfMOGQ==";\
		//final String encryptedKey = "WCM+vvjgl7SGfhW43nDHQmuWi+B2bv0W8UgxPOUpRY2XBnQkXU7uhaGxTnT+09oVFo/ao6YTeg1BWf6gGyyKuHhjD09YLIM2cLGWjXr4WUSD1Pbm52rslEE/q43fg3TsvvilScR5SrJsOjTREPGw9J9ZuqA2V5N7oj0UWofUCOn32zrzEL06GDHmifcwDXduzZ8DSjHXa00m16wLu1Jh2StjqfiGm3trJg6IncCl7kBjOUeEct9+0TJflqK72WHwhMf1RTp/hgZAIuFHBh1E8Yh3GLYnjADLFz+zANHwtjC6ZDlX+Ml/B9Q/CGxO7nRWVSg/SRRlZ131zub2EmpJjg==";
		//final String encryptedIv = "MTr8jPeTRtn8VkASOqgv3JoQaK60J7NYwjXIlsgsO93qb8itn7WhKGuZFb0Eoc92ALVgvYOJOlIcvM0uxIFwVTNSKsXdNVruz07Z3Du7JCFRV56np5C4CQ81wKz7mp7QjM2gjeVWRB0oBW4/A4b9WrmORU7TTNnMzyKtu+8rSM11yPQf7TF4AHZXPxjHJqDAhi8VdrF2IxoKnYqnkevwXm2wtt396nnNXa/BVhee3ejR+11VgDqarNdh+ytMHDtZqtiyJDf8aPpegidELia52pF4SC+F1fmFzTUl4YISPyIr554OUVGN/STtYR03EP0DmekhGQiSjLmM10BWA8QGLQ==";
		
		
		/*  //Uncomment code for Insta Merchant Onboard
		final String encryptedKey = ApplicationConfig.get("instaDesEncryptedKey");
		final String encryptedIv = ApplicationConfig.get("instaDesEncryptedIv");
		
		try {
			DesUtil.init(encryptedKey, encryptedIv);
		} catch (Exception e) {
			logger.error("Error Initializing DES util",e);
		}
		*/
		logger.debug("Application Context refresh event is done");
	}

}
