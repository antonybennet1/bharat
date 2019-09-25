package com.wl.bharatqr.daoimpl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wl.bharatqr.dao.AmexTransactionDao;
import com.wl.bharatqr.model.AmexFields;
import com.wl.bharatqr.model.HmacFields;
import com.wl.bharatqr.util.AmexFieldsValidation;
import com.wl.upi.util.QueryConstant;
@Repository
public class AmexTransactionDaoImpl implements AmexTransactionDao{
	
	private static Logger logger = LoggerFactory.getLogger(AmexTransactionDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	AmexFieldsValidation amexFieldsValidation=new AmexFieldsValidation();
	@Override
	public String amexNotificationDao(AmexFields amexFields) {
		int count=0;
		try{
		logger.info("Inside AmexTransactionDaoImpl-->");
		String query=QueryConstant.INSERT_AMEX_NOTIFICATION;
		Object dataArr[] = {
				amexFields.getType(),
				amexFields.getPrimary_account_number(),
				amexFields.getFull_pan(),
				amexFields.getExpiry_date(),
				amexFields.getProcessing_code(),
				amexFields.getTransaction_amount(),
				amexFields.getTip_amount(),
				amexFields.getTransaction_time(),
				amexFields.getSystem_trace_number(),
				amexFields.getTransaction_time_local(),
				amexFields.getReconciliation_date(),
				amexFields.getAcquirer_reference_data(),
				amexFields.getRetrieval_reference_number(),
				amexFields.getAcquirer_id(),
				amexFields.getApproval_code(),
				amexFields.getAction_code(),
				amexFields.getCard_acceptor_id(),
				amexFields.getCard_acceptor_id_type(),
				amexFields.getCard_acceptor_name(),
				amexFields.getCurrency_code(),
				amexFields.getPos_dc(),
				amexFields.getAdditional_data().toString(),
				};
		logger.debug(" Insert query : " + query );
	    count = jdbcTemplate.update(query,dataArr);
		logger.info("Records inserting Amex Transaction Detail --->" + count);
		if(count>0){
			return amexFieldsValidation.getPropValues("success");
		}
		}catch(Exception e){
			logger.debug(" Insert Amex Details : " + e );
		}
		return amexFieldsValidation.getPropValues("failed");
	}
	@Override
	public List<Map<String, Object>> isMailIdAlreadyExist(HmacFields hmacFields) {
		logger.info("emailId :"+hmacFields.getEmailId());
		String query = QueryConstant.CHECK_HAMC_EMAILID ;
		List<Map<String, Object>> rows= jdbcTemplate.queryForList(query,new Object[]{hmacFields.getEmailId()});
		return rows;
	}
	
	@Override
	public List<Map<String, Object>> getAuthorizationDetails(String clientId) {
		logger.info("clientId :"+clientId);
		String query = QueryConstant.GET_SECRET_FOR_HAMC;
		List<Map<String, Object>> rows= jdbcTemplate.queryForList(query,new Object[]{clientId});
		return rows;
	}
	
	@Override
	public String insertClientIdAndSecret(HmacFields hmacFields) {
		int count=0;
		try{
		logger.info("Inside AmexTransactionDaoImpl-->");
		String query=QueryConstant.INSERT_CLIENTID_SECRET;
		Object dataArr[] = {
				hmacFields.getEmailId(),
				hmacFields.getClientId(),
				hmacFields.getSecret()
				};
		logger.debug(" Insert query : " + query );
	    count = jdbcTemplate.update(query,dataArr);
		logger.info("Generating clientId and secret for Hmac --->" + count);
		if(count>0){
			return amexFieldsValidation.getPropValues("success");
		}
		}catch(Exception e){
			logger.debug("Generating clientId and secret for Hmac : " + e );
		}
		return amexFieldsValidation.getPropValues("failed");
	}

}
