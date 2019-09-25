package com.wl.util.dao;

import java.util.List;
import java.util.Map;

public interface ConfigurationDao {
	public List<Map<String, Object>> getConfiguration();

	public List<Map<String, Object>> getBankwiseConfiguration();
	
	public List<Map<String, Object>> getSmsConfigBankwise();
	
	public List<Map<String, Object>> getAggregatorConfigMIDwise();
}
