package com.wl.upi.dao;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wl.upi.util.QueryConstant;
import com.wl.util.exceptions.DaoException;

/**
 * @author kunal.surana
 *
 */
@Repository("upiRefundDAO")
public class UpiRefundDAOImpl implements UpiRefundDAO {

	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private static Logger logger = LoggerFactory.getLogger(UpiRefundDAOImpl.class);
	/* (non-Javadoc)
	 * @see com.wl.upi.dao.UpiRefundDAO#saveRefundTransaction(java.lang.String[])
	 */
	@Override
	public void saveRefundTransaction(Object[] args) {
		String query = QueryConstant.INSERT_UPI_REFUND;
		logger.debug("Query :"+query);
		logger.debug("Query parameter :"+Arrays.toString(args));
		int count = jdbcTemplate.update(query, args);
		if(count!=1)
			throw new DaoException("UPI Refund insertion failed");
	}
	
	@Override
	public boolean checkRefundExist(String trId,String bankCode) {
		
		String query = QueryConstant.CHECK_UPI_REFUND;
		logger.debug("Query :"+query);
		logger.debug("Query parameter :"+trId + "|" + bankCode);
		int count = jdbcTemplate.queryForObject(query, Integer.class , trId,bankCode);
		if(count == 1)
			return true;
		else
			return false;
	}

	@Override
	public double getSumForRefundedAmount(String rrn, String bankCode) {
		// TODO Auto-generated method stub
		
		String query = QueryConstant.SUM_OF_REFUNDED_AMT;
		logger.debug("Query :"+query);
		logger.debug("Query parameter rrn :"+rrn + "|" + bankCode);
		Double sumRefund = jdbcTemplate.queryForObject(query, Double.class , rrn,bankCode);
		System.out.println(sumRefund);
		if(sumRefund == null)
			return 0.0d;
		else
		return Double.valueOf(sumRefund);
	}
}
