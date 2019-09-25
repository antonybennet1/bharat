package com.wl.util.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("configurationDao")
public class ConfigurationDaoImpl implements  ConfigurationDao{

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> getConfiguration() {
		// TODO Auto-generated method stub
		String query = "select config_key , config_value from application_config";
		return jdbcTemplate.queryForList(query);
	}  
	
	@Override
	public List<Map<String, Object>> getBankwiseConfiguration() {
		// TODO Auto-generated method stub
		String query = "select bank_code , config_key , config_value from bank_config order by bank_code";
		return jdbcTemplate.queryForList(query);
	}

	@Override
	public List<Map<String, Object>> getSmsConfigBankwise() {
		// TODO Auto-generated method stub
		String query = "SELECT m.bank_code, d.sms_property , d.`value` FROM detail_provider d join master_provider m on d.master_id = m.master_id order by m.bank_code";
		return jdbcTemplate.queryForList(query);
	}  
	

	@Override
	public List<Map<String, Object>> getAggregatorConfigMIDwise() {
		//String query = "select m.mvisa_merchant_id, a.aggregator_id , a.name , a.url from aggregator_details a join merchant_aggregator_link m on a.aggregator_id = m.aggregator_id order by m.mvisa_merchant_id";
		String query = "select m.mvisa_merchant_id, a.aggregator_id , a.name , a.url from aggregator_details a join merchant_aggregator_link m on a.aggregator_id = m.aggregator_id order by m.mvisa_merchant_id ";
		return jdbcTemplate.queryForList(query);
	}  
	
	
}
