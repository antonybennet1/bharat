package com.wl.upi.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wl.upi.model.AggregatorDetails;
import com.wl.upi.model.DeviceDetails;
import com.wl.upi.model.MerchantDetail;
import com.wl.upi.util.QueryConstant;
import com.wl.util.exceptions.DaoException;


@Repository("merchantDao")
public class MerchantDAOImpl implements MerchantDAO {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private static Logger logger = LoggerFactory.getLogger(MerchantDAOImpl.class);

	/* (non-Javadoc)
	 * @see com.wl.upi.dao.MerchantDAO#getAggregatorDetails(java.lang.String)
	 */
	@Override
	public AggregatorDetails getAggregatorDetails(String mid) {
		// TODO Auto-generated method stub
		String query  = QueryConstant.AGGR_DETAILS;
		logger.debug("Query:"+query);
		logger.debug("Query Parameters:"+mid);
		List<Map<String,Object>> rows = jdbcTemplate.queryForList(query, mid);
		if(rows!=null && rows.size() == 1)
		{
			Map<String,Object> row = rows.get(0);
			AggregatorDetails aggDetails = new AggregatorDetails();
			aggDetails.setAggrId((String)row.get("aggregator_id"));
			aggDetails.setAggrName((String)row.get("name"));
			aggDetails.setUrl((String)row.get("url"));
			aggDetails.setMerchantId(mid);
			return aggDetails;
		}
		else 
			return null;
	}

	/* (non-Javadoc)
	 * @see com.wl.upi.dao.MerchantDAO#getDeviceDetails(java.lang.String)
	 */
	@Override
	public List<DeviceDetails> getDeviceDetails(String tid) {
		String query  = QueryConstant.DEVICE_DETAILS;
		logger.debug("Query:"+query);
		logger.debug("Query Parameters:"+tid);
		List<Map<String,Object>> rows = jdbcTemplate.queryForList(query, tid);
		
		List<DeviceDetails>  list= new ArrayList<>();
		for(Map<String,Object> row : rows)
		{
			DeviceDetails deviceDetails = new DeviceDetails();
			deviceDetails.setMobileNumber((String)row.get("mobile_number"));
			deviceDetails.setDeviceId((String)row.get("device_id"));
			deviceDetails.setPlatform((String)row.get("platform"));
			int is_inapp_activated = (Integer)row.get("is_inapp_activated");
			if(is_inapp_activated == 1)
				deviceDetails.setInAppUser(true);
			
			int is_primary = (Integer)row.get("is_primary");
			if(is_primary == 1)
				deviceDetails.setPrimaryUser(true);
			
			list.add(deviceDetails);
		}
		return list;
	}
	

	/* (non-Javadoc)
	 * @see com.wl.upi.dao.MerchantDAO#getMerchantDetails(java.lang.String, java.lang.String)
	 */
	@Override
	public MerchantDetail getMerchantDetails(String mpan, String tid) {
		// TODO Auto-generated method stub
		String query = null;
		String data = null;
		if(mpan!=null)
		{
			query  = QueryConstant.GET_MERCHANT_DETAIL_MPAN;
			data = mpan;
		}
		if(tid!=null)
		{
			query  = QueryConstant.GET_MERCHANT_DETAIL_TID;
			data = tid;
		}
		logger.debug("Query used :"+query);
		logger.info("Query Parameter :"+data);
		List<Map<String,Object>> rows = jdbcTemplate.queryForList(query, data);
		if(rows!=null && rows.size() == 1)
		{
			Map<String,Object> row = rows.get(0);
			MerchantDetail md = new MerchantDetail();
			md.setMpan(mpan);
			md.setMerchantId((String)row.get("merchant_id"));
			md.setProgramType((int)row.get("program_type")); 
			md.setTid((String)row.get("tid"));
			md.setRefundAllowed((boolean)row.get("can_refund"));
			md.setMcc((int)row.get("merchant_category_code")); //Edited
			return md;
		}
		else
			return null;
	}

	@Override
	public List<DeviceDetails> getAxisDeviceDetails(String tid) {
		String query  = QueryConstant.GET_DIRECT_SMS_PRIMARY_MERCHANT;
		logger.debug("Query:"+query);
		logger.debug("Query Parameters:"+tid);
		List<Map<String,Object>> rows = jdbcTemplate.queryForList(query, tid);
		
		List<DeviceDetails>  list= new ArrayList<>();
		for(Map<String,Object> row : rows)
		{
			DeviceDetails deviceDetails = new DeviceDetails();
			deviceDetails.setMobileNumber((String)row.get("mobile_number"));
			deviceDetails.setDeviceId((String)row.get("device_id"));
			deviceDetails.setPlatform((String)row.get("platform"));
			int is_inapp_activated = (Integer)row.get("is_inapp_activated");
			
			int is_primary = (Integer)row.get("is_primary");
			if(is_primary == 1)
				deviceDetails.setPrimaryUser(true);
			
			list.add(deviceDetails);
		}
		return list;
	}
}
