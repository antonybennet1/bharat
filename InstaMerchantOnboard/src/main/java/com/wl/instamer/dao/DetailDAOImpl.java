package com.wl.instamer.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.wl.instamer.model.Detail;
import com.wl.instamer.model.MerchantOnboard;
import com.wl.instamer.model.MerchantOnboardResponseData;
import com.wl.util.HelperUtil;

@Repository("detailDAO")
public class DetailDAOImpl implements DetailDAO {

	private static final Logger logger = LoggerFactory.getLogger(DetailDAOImpl.class);

	private JdbcTemplate jdbcTemplate;  
	
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
	/*	final org.springframework.jdbc.datasource.DriverManagerDataSource ds = (DriverManagerDataSource) jdbcTemplate.getDataSource();
		jdbcTemplate.setDataSource(new org.springframework.jdbc.datasource.DriverManagerDataSource(){
		  // You'll need to implement all the methods, simply delegating to ds
		  @Override
		  public Connection getConnection() throws SQLException {
		    Connection c = ds.getConnection();
		    c.setAutoCommit(false);
		    return c;
		  }
		});*/
		this.jdbcTemplate = jdbcTemplate;  
		
	}  

	@Override
	public void saveDetail(final MerchantOnboard request, final MerchantOnboardResponseData response) {
		// TODO Auto-generated method stub

		String query1="INSERT INTO detail (merchant_id,merchant_category_code,"
				+ "counter_phone_number,country_code,currency_code,bank_code, is_enabled,can_refund,tip_conv_indicator, convenience_flag , convenience_value, "
				+ "TID, store_id, ifsc_account_number, postal_code) "
				+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		logger.debug("jdbc template :" + jdbcTemplate);
		logger.debug("sql :" + query1);	
		jdbcTemplate.execute(query1,new PreparedStatementCallback<Boolean>(){
			@Override
			public Boolean doInPreparedStatement(PreparedStatement statement)
					throws SQLException, DataAccessException {

				int tip_conv_ind,convenience_flag;
				tip_conv_ind=convenience_flag=0;
				//tip_conv_ind=Integer.parseInt(request.getTipFlag());
				//convenience_flag = Integer.parseInt(request.getConFeeFlag());
				String CountryCode = "IN";

				statement.setString(1, response.getMerchantCode());  ////Merchant Id
				statement.setInt(2, request.getMcc());/// merchant_category_code
				statement.setLong(3,request.getMerMobileNumber());////counter_phone_number
				statement.setString(4,CountryCode);
				statement.setInt(5,0); //Currency code?
				statement.setString(6,request.getBankCode());
				statement.setBoolean(7, true);
				statement.setBoolean(8, "Y".equalsIgnoreCase(request.getIsRefundAllowed()) ? true : false);  
				statement.setInt(9,tip_conv_ind);
				statement.setInt(10,convenience_flag );
				statement.setDouble(11, request.getConFeeAmount());
				statement.setString(12,response.getTid());
				statement.setString(13,"storeId");///storeId?
				statement.setString(14,request.getBnfIfsc());
				statement.setLong(15,request.getInstAddPincode());
				return statement.execute();

			}
		});
		
	
			try {
				jdbcTemplate.getDataSource().getConnection().commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	public void saveContactDetail(final MerchantOnboard request, final MerchantOnboardResponseData response)
	{
		
		String query2="INSERT INTO contact_detail (merchant_id,name,city_name,"
				+ "address,dba_name,contact_person,contact_person_phone,email,TID) "
				+ " VALUES(?,?,?,?,?,?,?,?,?)";
		logger.debug("sql :" + query2);
		jdbcTemplate.execute(query2,new PreparedStatementCallback<Boolean>(){
			@Override
			public Boolean doInPreparedStatement(PreparedStatement statement)
					throws SQLException, DataAccessException {

				statement.setString(1,response.getMerchantCode());  //Merchant Id
				statement.setString(2,request.getLegalName());
				statement.setString(3,""+request.getInstLocation());// cityName??
				statement.setString(4,request.getInstAddressLine1());
				statement.setString(5,request.getDbaName());
				statement.setString(6,request.getInstAddContactPerson());
				statement.setLong(7,request.getMerMobileNumber());
				statement.setString(8,request.getMerEmailId());
				statement.setString(9, response.getTid());

				return statement.execute();

			}
		});
		

		/*try {
			jdbcTemplate.getDataSource().getConnection().commit();
			jdbcTemplate.getDataSource().getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	
	}
	public void saveMobileDetail(final MerchantOnboard request, final MerchantOnboardResponseData response)
	{	
		final List<String> multipleNumbers = new LinkedList<>();
		multipleNumbers.add(request.getMerMobileNumber().toString());
		multipleNumbers.add(request.getRaMobile1().toString());

		String query3="INSERT INTO mobile_details (mobile_number, bank_code) VALUES(?, ?)";
		logger.debug("sql :" + query3);
		try{
			jdbcTemplate.batchUpdate(query3, new BatchPreparedStatementSetter() {
			
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					// TODO Auto-generated method stub
					ps.setString(1,multipleNumbers.get(i) );
					ps.setString(2,request.getBankCode());
				}

				@Override
				public int getBatchSize() {
					// TODO Auto-generated method stub
					return multipleNumbers.size();
				}
			});
		}
		catch(DataAccessException e)
		{
			
			logger.error(e.getMessage(),e);
			
			if(e instanceof DuplicateKeyException){
				logger.error("Primary key violation in insertMobileNumber"+ e);
			}
			else
			{
				throw e;
			}
		}

		/*
		jdbcTemplate.execute(query3,new PreparedStatementCallback<Boolean>(){
			@Override
			public Boolean doInPreparedStatement(PreparedStatement statement)
					throws SQLException, DataAccessException {
				for(int i=0;i<multipleNumbers.size();i++)
				{
					statement.setString(1, multipleNumbers.get(i));
					statement.setString(2, "111");
					statement.addBatch();
				}
				try{
					return statement.execute();
				}
				catch(SQLException e)
				{
					logger.error(e.getMessage());
					int error_code =((SQLException)e).getErrorCode();
					if(error_code==1062){
						logger.error("Primary key violation in insertMobileNumber"+ e);
						return true;
					}
					else
					{
						throw e;
					}
				}
			}
		});
		 */
	}
	public void saveMerchantGroup(final MerchantOnboard request, final MerchantOnboardResponseData response)
	{


		String query4="INSERT INTO merchant_group (TID, scheme_id, merchant_pan, bank_code) VALUES(?,?,?,?)";
		jdbcTemplate.execute(query4,new PreparedStatementCallback<Boolean>(){
			@Override
			public Boolean doInPreparedStatement(PreparedStatement statement)
					throws SQLException, DataAccessException {
				statement.setString(1, response.getTid());
				statement.setString(2, "12314");
				statement.setString(3, "7877");
				statement.setString(4, request.getBankCode());
				return statement.execute();

			}
		});

	}
	public void saveMvisa(final MerchantOnboard request, final MerchantOnboardResponseData response)
	{


		String query5="INSERT INTO mvisa_id_details (mvisa_id) VALUES(?)";
		jdbcTemplate.execute(query5,new PreparedStatementCallback<Boolean>(){
			@Override
			public Boolean doInPreparedStatement(PreparedStatement statement)
					throws SQLException, DataAccessException {
				statement.setString(1, response.getTid());
				return statement.execute();

			}
		});

	}
	
	
	
	@Override
	public void updateDetails(Detail detail) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean checkIfPhoneNumberAlreadyPresentInLoginTable(final Long merMobileNumber, final String bankCode) {

		// TODO Auto-generated method stub
		//		Connection con =DatabaseConnection.getDatabaseInstance();
		String query = "Select count(1) from login_detail where user_name=? and bank_code=?";
		PreparedStatement statement=null;
		// Insert mvisa Id 
		
		int resultCount  = jdbcTemplate.execute(query,new PreparedStatementCallback<Integer>(){
			@Override
			public Integer doInPreparedStatement(PreparedStatement statement)
					throws SQLException, DataAccessException {
				statement.setString(1, merMobileNumber.toString());
				statement.setString(2, bankCode);
				statement.execute();
				ResultSet rs = statement.getResultSet();
				rs.next();
		        return rs.getInt(1);

			}
		});

	if(resultCount==1)
		return true;
	
	return false;
		
	
	}

	@Override
	public String createLogInInfo(long merchPhone, String tid, final String bankCode) {
		// TODO Auto-generated method stub
		final String mobileNumber = Long.toString(merchPhone);
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		
		String password = null;
		if(tid != null)
		{
			password=tid;
			//ecrypted_password = UtilityClass.getMD5(tid);
		}
		else
		{
			password = HelperUtil.randomString(8);
			//ecrypted_password=UtilityClass.getMD5(password);
		}
		String ecrypted_password = bcrypt.encode(password);
		
			final String ecrypted_pass = ecrypted_password;
			//ecrypted_password=UtilityClass.getMD5(password);
			logger.debug("Password set is :"+ecrypted_password);
			
			String query="INSERT INTO login_detail (user_name,password,bank_code,is_first_login,update_time , last_login_time) "
					+ " VALUES(?,?,?,?,?,?)"; 
			
			
			jdbcTemplate.execute(query,new PreparedStatementCallback<Boolean>(){
				@Override
				public Boolean doInPreparedStatement(PreparedStatement statement)
						throws SQLException, DataAccessException {
					statement.setString(1, mobileNumber);  ////user_name
					statement.setString(2, ecrypted_pass);  ////password
					statement.setString(3, bankCode);
					statement.setInt(4, 1);
					java.sql.Timestamp now = new Timestamp(new Date().getTime());
					statement.setTimestamp(5, now);
					statement.setTimestamp(6, now );
					return statement.execute();

				}
			});
	
		return password;
	
	}
}


