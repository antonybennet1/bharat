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
import com.wl.util.model.AggregatorDetails;

@Component("aggregatorConfig")
public class AggregatorConfig {

	private static final Logger logger = LoggerFactory.getLogger(AggregatorConfig.class);

	private static ConcurrentHashMap<String, AggregatorDetails> map = new ConcurrentHashMap<String, AggregatorDetails>();

	@Autowired
	private ConfigurationDao applicationConfigDao;

	@PostConstruct
	public void init()
	{
		map.clear();
		List<Map<String,Object>> dbRows =  applicationConfigDao.getAggregatorConfigMIDwise();
		for(Map<String,Object> row : dbRows)
		{
			String mid = (String)row.get("mvisa_merchant_id");
			AggregatorDetails ag =new AggregatorDetails();
			ag.setAggrId((String)row.get("aggregator_id"));
			ag.setAggrName((String)row.get("name"));
			ag.setUrl((String)row.get("url"));
			ag.setMerchantId(mid);
			map.put(mid, ag);
		}
		logger.debug("Midwise Aggregator Config loaded " + map);
	}

	public static AggregatorDetails get(String mid)
	{
		return map.get(mid);
	}

}
