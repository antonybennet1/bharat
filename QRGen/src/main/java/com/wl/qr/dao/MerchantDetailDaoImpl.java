package com.wl.qr.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.wl.qr.model.MerchantDetails;

@Repository("merchantInfoDao")
public class MerchantDetailDaoImpl implements MerchantDetailDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private static final Logger logger = LoggerFactory.getLogger(MerchantDetailDaoImpl.class);

	public MerchantDetails getMerchantInfo(String mid, String tid, String bankCode) {
		String query = "SELECT d.upi_vpa, d.upi_url, d.upi_mam, d.aadhar_number, "
				+ "d.program_type, cd.name, cd.city_name, d.bank_code, d.merchant_category_code, d.country_code, d.currency_code, "
				+ "d.tip_conv_indicator,d.convenience_flag,d.convenience_value,d.ifsc_account_number,d.postal_code "
				+ "FROM contact_detail cd INNER JOIN detail d ON cd.TID=d.TID WHERE cd.TID =?"
				+ "and d.bank_code = ? and d.merchant_id=?";
		
		logger.debug("Query parmeters : " +tid+ "|"+bankCode+ "|"+ mid);

		MerchantDetails merchantDetails = jdbcTemplate.queryForObject(query, new RowMapper<MerchantDetails>() {
			@Override
			public MerchantDetails mapRow(ResultSet rs, int rownumber) throws SQLException {
				MerchantDetails merchantDetails = new MerchantDetails();
				merchantDetails.setUpiVpa(rs.getString("upi_vpa"));
				merchantDetails.setUpiUrl(rs.getString("upi_url"));
				merchantDetails.setUpiMam(rs.getDouble("upi_mam"));
				merchantDetails.setAadharNumber(rs.getString("aadhar_number"));
				merchantDetails.setProgramType(rs.getInt("program_type"));
				merchantDetails.setName(rs.getString("name"));
				merchantDetails.setCityName(rs.getString("city_name"));
				merchantDetails.setBankCode(rs.getString("bank_code"));
				merchantDetails.setMerchantCategoryCode(rs.getInt("merchant_category_code"));
				merchantDetails.setCountryCode(rs.getString("country_code"));
				merchantDetails.setCurrencyCode(rs.getInt("currency_code"));
				merchantDetails.setTipConvIndicator(rs.getInt("tip_conv_indicator"));
				merchantDetails.setConvenienceFlag(rs.getInt("convenience_flag"));
				merchantDetails.setConvenienceValue(rs.getFloat("convenience_value"));
				merchantDetails.setIfscAccountNumber(rs.getString("ifsc_account_number"));
				merchantDetails.setPostalCode(rs.getString("postal_code"));
				return merchantDetails;
			}
		}, tid, bankCode, mid);

		logger.info("merchant details " + merchantDetails);
		return merchantDetails;
	}
	
	@Override
	public List<Map<String, Object>> getMpanList(String tid, String bankCode) {
		// TODO Auto-generated method stub
		String query  = "select scheme_id , merchant_pan from merchant_group where bank_code = ? and TID = ? order by scheme_id";
		logger.debug("Query :"+query);
		logger.debug("Query Parameters:"+tid+"|" +bankCode);
		return jdbcTemplate.queryForList(query, bankCode , tid);
	}

	@Override
	public String getVisaMPAN(String tId) {
		
		String query = "select mg.merchant_pan from merchant_group mg join detail d on mg.tid=d.tid where mg.tid=? and mg.scheme_id = '02'";
		logger.debug("Query :"+query);
		logger.debug("Query parameter rrn :"+tId);
		String visaMpan = jdbcTemplate.queryForObject(query, String.class , tId);
		logger.debug(visaMpan);
		if(visaMpan == null)
			return null;
		else
		return visaMpan;
	}

}
