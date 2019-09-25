package com.wl.util.config;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wl.util.dao.ConfigurationDao;

@Component("smsConfig")
public class SmsConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(SmsConfig.class);
	
	private static ConcurrentHashMap<String, ConcurrentHashMap<String, String>> map = new ConcurrentHashMap<String, ConcurrentHashMap<String, String>>();
	
	@Autowired
	private ConfigurationDao applicationConfigDao;

	@PostConstruct
	public void init()
	{
		map.clear();
		List<Map<String,Object>> dbRows =  applicationConfigDao.getSmsConfigBankwise();
		String tempBankCode = "";
		ConcurrentHashMap<String, String> configMap = null;
		for(Map<String,Object> row : dbRows)
		{
				Integer bcode  = 	(Integer) row.get("bank_code");
				String bankcode = String.format("%05d", bcode.intValue());
				String configKey  = 	(String) row.get("sms_property");
				String configValue   = 	(String) row.get("value");
				if(!tempBankCode.equals(bankcode))
				{
					tempBankCode = bankcode;
					configMap = new  ConcurrentHashMap<String, String>();
					map.put(bankcode, configMap);
				}
				configMap.put(configKey, configValue);
		}
		logger.debug("SMS Config loaded " + map);
	}
	
	public static String get(String bankCode , String key)
	{
		return map.get(bankCode).get(key);
	}
}
