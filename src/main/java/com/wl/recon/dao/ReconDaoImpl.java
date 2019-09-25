package com.wl.recon.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wl.recon.model.FileDataDTO;
import com.wl.recon.util.QueryConstants;
import com.wl.recon.util.ReconUtils;

@Repository("reconDao")
public class ReconDaoImpl implements ReconDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final Logger logger = LoggerFactory.getLogger(ReconDaoImpl.class);

	@Override
	public List<FileDataDTO> getFileData(String bankCode, String fromDate, String toDate ) {

		logger.info("Inside the getFileData method");
		
		
		List<FileDataDTO> list = new ArrayList<>();
		try {
			String query = "";
			List<Map<String, Object>> rows = null;
			
			if (bankCode != null) {
				query = QueryConstants.GET_FILE_DETAILS;
				logger.info("query : " + query);
				logger.info("parameter : " + bankCode + "|" + fromDate + "|" + toDate);
				rows = jdbcTemplate.queryForList(query, new Object[] { bankCode, fromDate, toDate });
				if (rows.size() != 0) {
					//Map<String, Object> row = rows.get(0);
					for(Map<String, Object> row : rows){
						FileDataDTO fileDTO = new FileDataDTO();
						fileDTO.setMid(((String) row.get("merch_id")));
						fileDTO.setTid(((String) row.get("TID")));
						fileDTO.setRrn(((String) row.get("txn_ref_no")));
						fileDTO.setTxnId(((String) row.get("gateway_trans_id")));
						fileDTO.setTxnDate(((java.sql.Timestamp) row.get("txn_date")));
						fileDTO.setBusDate(((java.sql.Timestamp) row.get("txn_date")));
						fileDTO.setTxnAmount(((double) row.get("amount")));
						fileDTO.setSetlAmount(((double) row.get("amount")));
						fileDTO.setMcc(((int) row.get("merchant_category_code")));
						fileDTO.setRespCode(((String) row.get("response_code")));
						fileDTO.setChannelFlag("QR");
						list.add(fileDTO) ;
						//System.out.println(" fileDTO.toString() ----> " + fileDTO.toString());
					}
				}
				return list;
			} else {
				logger.info("bankCode in else : " + bankCode);
				return null;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

}
