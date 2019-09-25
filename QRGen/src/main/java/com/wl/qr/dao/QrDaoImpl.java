package com.wl.qr.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

@Repository("qrDao")
public class QrDaoImpl implements QrDao{

	private static final Logger logger = LoggerFactory.getLogger(QrDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;  

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {  
		this.jdbcTemplate = jdbcTemplate;  
	}  

	public String getBankBin(String bankCode){
		String query = "select config_value from bank_config where config_key = 'bin' and bank_code=?";
		return jdbcTemplate.queryForObject(query,String.class,bankCode);
	}

	@Override
	public void insertQRtring(final String qr_string, final int qrType, final String bank_code, final int programType, final String txn_id, final String status,
			final String terminalId, final String merchant_id, final String amount, final String fromEntity, final String trId,final String merchVPA) {
		// TODO Auto-generated method stub
		String query = "INSERT INTO qr_history (data_string, qr_type, bank_code, program_type, "
				+ "txn_id, status, terminal_id, merch_id, amount, from_entity,tr_id,merch_vpa) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
		
		logger.debug("qr_string:"+qr_string);
		logger.debug("qrType:"+qrType);
		logger.debug("bank_code:"+bank_code);
		logger.debug("programType:"+programType);
		logger.debug("txn_id:"+txn_id);
		logger.debug("status:"+status);
		logger.debug("terminalId:"+terminalId);
		logger.debug("merchant_id:"+merchant_id);
		logger.debug("amount:"+amount);
		logger.debug("fromEntity:"+fromEntity);
		logger.debug("trId:"+trId);
		
		jdbcTemplate.execute(query,new PreparedStatementCallback<Boolean>(){
			@Override
			public Boolean doInPreparedStatement(PreparedStatement statement)
					throws SQLException, DataAccessException {

				statement.setString(1, qr_string);  
				statement.setInt(2, qrType);
				statement.setString(3,bank_code);
				statement.setInt(4,programType);
				statement.setString(5,txn_id);
				statement.setString(6,status);
				statement.setString(7, terminalId);
				statement.setString(8, merchant_id);
				statement.setString(9, amount);
				statement.setString(10, fromEntity);
				statement.setString(11, trId);
				statement.setString(12, merchVPA);
				return statement.execute();

			}
		});
	}

	@Override
	public Map<String, Object> fetchQRString(String mid, String tid, String qrType) {
		
		String query  = "select data_string,txn_id,tr_id from qr_history where terminal_id = ? and merch_id=? and qr_type =?";
		logger.debug("Query:"+query + " "+mid +" "+tid +" "+qrType);
		List<Map<String,Object>> rows = jdbcTemplate.queryForList(query, tid,mid,qrType);
		if(rows!=null && rows.size() == 1)
		{
			return rows.get(0);
		}
		else 
			return null;
		/*String query = ;
		return jdbcTemplate.queryForObject(query,String.class,new Object[]{tid,mid,qrType});*/
	}
}
