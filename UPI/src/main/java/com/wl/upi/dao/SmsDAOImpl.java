package com.wl.upi.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wl.upi.util.QueryConstant;

@Repository("smsDao")
public class SmsDAOImpl implements SmsDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	private static Logger logger = LoggerFactory.getLogger(SmsDAOImpl.class);
	
	/*@Override
	public Map<String,String> getSmsConfiguration(String bankCode) {
		String query = QueryConstant.GET_SMS_CONFIG;
		logger.debug("query for sms config :"+query);
		logger.info("query parameter :"+bankCode);
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query,bankCode);
		Map<String,String> smsConfig = new HashMap<>(rows.size());
		String masterId = null;
		for(Map<String, Object> row :rows)
		{
			String key = (String) row.get("sms_property");
			String value = (String) row.get("value");
			masterId = (String) row.get("master_id");
			smsConfig.put(key, value);
		}
		logger.info(masterId+"|SMS config:"+smsConfig);
		return smsConfig;
	}*/

	@Override
	public void saveSmsLogs(String feedId, String to, String smsText, String responseCode, String responseMessage,
			String bankCode) {
		String query = QueryConstant.SAVE_SMS_LOGS;
		if(responseCode==null)
			responseCode="NA";
		if(responseMessage==null)
			responseMessage="NA";
		logger.debug("query parameter feed id:"+feedId);
		logger.debug("query parameter to:"+to);
		logger.debug("query parameter smsText:"+smsText);
		logger.debug("query parameter responseCode:"+responseCode);
		logger.debug("query parameter responseMessage:"+responseMessage);
		logger.debug("query parameter bankCode:"+bankCode);
		
		int count = jdbcTemplate.update(query, feedId, to, smsText, responseCode, responseMessage, bankCode);
		logger.info("insert in sms logs count:"+count);
	}

}
