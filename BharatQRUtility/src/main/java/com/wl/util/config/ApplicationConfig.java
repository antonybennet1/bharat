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

@Component("applicationConfig")
public class ApplicationConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
	
	private static ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
	
	@Autowired
	private ConfigurationDao applicationConfigDao;

	
	@PostConstruct
	public void init()
	{
		map.clear();
		List<Map<String,Object>> dbRows =  applicationConfigDao.getConfiguration();
		for(Map<String,Object> row : dbRows)
		{
			String key = (String) row.get("config_key");
			String value = (String) row.get("config_value");
			map.put(key, value);
		}
		logger.debug("Application Config loaded " + map);
	}
	
	public static String get(String key)
	{
		return map.get(key);
	}
	
}
