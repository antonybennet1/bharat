package com.wl.upi.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import org.eclipse.persistence.queries.StoredProcedureCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.wl.upi.model.SettlementDTO;
import com.wl.upi.util.QueryConstant;

@Repository("settlementDao")
public class SettlementDAOImpl implements SettlementDAO {

	private static Logger logger = LoggerFactory.getLogger(SettlementDAOImpl.class);
	
	@Autowired
	@Qualifier("orclJdbcTemplate")
	private JdbcTemplate orclJdbcTemplate;
	
	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	/* (non-Javadoc)
	 * @see com.wl.upi.dao.SettlementDAO#save(com.wl.upi.model.SettlementDTO)
	 */
	@Override
	public void save(final SettlementDTO bean) {
		String query=null;
		if("00051".equals(bean.getBankCode())||"00011".equals(bean.getBankCode())||"00045".equals(bean.getBankCode()))
		query = QueryConstant.INSERT_SETTLEMENT_TXN_P7;
		else
		query = QueryConstant.INSERT_SETTLEMENT_TXN;
		logger.debug("Query:"+query);
		logger.debug("Parameter:"+bean);
		
		int count = orclJdbcTemplate.update(query,new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, bean.getTxnChannel());
				ps.setString(2, bean.getMid());
				ps.setString(3, bean.getTid());
				ps.setInt(4,bean.getBatchNumber());
				ps.setString(5, bean.getRrn()); // this will store the new RRN for sale and refund
				ps.setString(6, bean.getRequestType());
				ps.setString(7, bean.getTxnTimestamp());
				ps.setString(8, bean.getStanNumber());
				ps.setString(9, bean.getAuthCode());
				ps.setString(10, bean.getResponceCode());
				ps.setString(11, bean.getInvoiceNumber());
				ps.setString(12, bean.getBranchCode());
				ps.setString(13, bean.getTrId());
				ps.setString(14, bean.getOriginalAmount());
				ps.setString(15, bean.getCurrencyCode());
				ps.setString(16, bean.getAdditionalAmount());
				ps.setString(17, bean.getTipApproved());
				ps.setString(18, bean.getExpiryDate());
				ps.setString(19, bean.getCardEntryMode());
				ps.setString(20, bean.getProcessingCode());
				ps.setString(21, bean.getBankCode());
				ps.setString(22, bean.getMti());
				ps.setString(23, bean.getSecondryId());
				ps.setString(24, bean.getMerchantVpa());
				ps.setString(25, bean.getCustomerVpa());
				ps.setString(26, bean.getNewRrn()); // storing old rrn(sale rrn) in P_FIELDF63 for refund
				
			}
		});
		logger.info("Inserting Settlement transaction:" + count);
	}
	
	@Override
	public void updateBatchNumber() {
		String query = QueryConstant.UPDATE_BATCH_NUMBER;
		int result = jdbcTemplate.update(query);
		logger.debug("batch number update status:"+result);
	}

	@Override
	public int getBatchNumber(String tid) {
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("proc_batch_number");
		SqlParameterSource in = new MapSqlParameterSource().addValue("TID", tid);
		Map<String, Object> out = jdbcCall.execute(in);
		int batch_number = (int) out.get("BATCH_NUMBER");
		return batch_number;
	}

	
	
	@Override
	public String getMRLBatchNumber() {
		String query = QueryConstant.GET_BATCH_NUMBER;
		String result = jdbcTemplate.queryForObject(query, String.class);
		String padedBatchNumber=String.format("%04d", Integer.parseInt(result));
		logger.debug("batch number return:"+padedBatchNumber);
		return padedBatchNumber;
	}
	

}
