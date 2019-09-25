package com.wl.upi.dao;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.wl.upi.model.BharatQRTransDTO;
import com.wl.upi.model.MMSTransDTO;
import com.wl.upi.model.TxnDTO;
import com.wl.upi.util.QueryConstant;
import com.wl.util.AesUtil;
import com.wl.util.EncryptionCache;
import com.wl.util.HelperUtil;


@Repository
public class BharatQRTransactionDaoImpl implements BharatQRTransactionDao{

	@Autowired
	private JdbcTemplate jdbcTemplate;

	Logger logger=LoggerFactory.getLogger(BharatQRTransactionDaoImpl.class);

	@Override
	public TxnDTO checkBharatQRTrans(String txnId,Integer id) {
		logger.debug("BharatQRTransDTO checkBharatQRTrans(TCHRequest tchRequest) execution starts");
		String query = "";
		List<Map<String, Object>> rows = null;
		if(txnId!=null)
		{
			logger.info("txn id :"+txnId);
			query = QueryConstant.CHECK_BHARATQR_TRANSACTION ;
			rows = jdbcTemplate.queryForList(query,  new Object[]{txnId , txnId});
		}
		if(id!=null)
		{
			logger.info("id :"+id);
			query = QueryConstant.CHECK_BHARATQR_TRANSACTION_ON_ID ;
			rows = jdbcTemplate.queryForList(query,  new Object[]{id});
		}
		if(rows.size() == 1)
		{
			Map<String,Object> row = rows.get(0);
			TxnDTO transDTO = new TxnDTO();
			transDTO.setId((int)row.get("id"));
			transDTO.setMpan((String)row.get("mvisa_merchant_id"));
			transDTO.setCustPan((String)row.get("customer_pan"));
			transDTO.setCustName((String)row.get("customer_name"));
			int curr=(int)row.get("currency_code");
			transDTO.setCurrency(String.valueOf(curr));
			double amt=(double)row.get("amount");
			transDTO.setTxnAmount(String.valueOf(amt));
			transDTO.setAuthCode((String)row.get("auth_code"));
			transDTO.setRrn((String)row.get("ref_no"));
			transDTO.setPrimaryId((String)row.get("primary_id"));
			transDTO.setSecondaryId((String)row.get("secondary_id"));
			double sAmt=(double)row.get("settlement_amount");
			transDTO.setSettlementAmount(String.valueOf(sAmt));
			Timestamp date=(Timestamp) row.get("date");
			transDTO.setTxnDate(date);
			int transType=(int)row.get("transaction_type");
			transDTO.setTransactionType(String.valueOf(transType));			
			transDTO.setProgramType(transType);
			String bCode=(String)row.get("bank_code");
			transDTO.setBankCode(bCode);
			transDTO.setMerchantId((String)row.get("merchant_id"));
			transDTO.setRefundedAmount((double)row.get("refunded_amount"));
			transDTO.setTid((String)row.get("tid"));
			logger.debug("BharatQRTransDTO checkBharatQRTrans(TCHRequest tchRequest) execution ends");
			logger.info("checkBharatQRTrans Transaction Type ::::"+transType);
			return transDTO;	
		}
		else 
			return null;
	}
	
	@Override
	public TxnDTO checkBharatQRTrans(String rrn	,String authCode, String tid) {
		logger.debug("BharatQRTransDTO checkBharatQRTrans(TCHRequest tchRequest) execution starts");
		String query =  QueryConstant.CHECK_BHARATQR_TXN_RRN;
		List<Map<String, Object>> rows = null;
		logger.debug("Query :"+query);
		logger.info("Query Parameter :"+rrn+"|"+authCode+"|"+tid);
		rows = jdbcTemplate.queryForList(query,  new Object[]{rrn ,authCode , tid});
		if(rows.size() == 1)
		{
			Map<String,Object> row = rows.get(0);
			TxnDTO transDTO = new TxnDTO();
			transDTO.setId((int)row.get("id"));
			transDTO.setMpan((String)row.get("mvisa_merchant_id"));
			transDTO.setCustPan((String)row.get("customer_pan"));
			transDTO.setCustName((String)row.get("customer_name"));
			int curr=(int)row.get("currency_code");
			transDTO.setCurrency(String.valueOf(curr));
			double amt=(double)row.get("amount");
			transDTO.setTxnAmount(String.valueOf(amt));
			transDTO.setAuthCode((String)row.get("auth_code"));
			transDTO.setRrn((String)row.get("ref_no"));
			transDTO.setPrimaryId((String)row.get("primary_id"));
			transDTO.setSecondaryId((String)row.get("secondary_id"));
			double sAmt=(double)row.get("settlement_amount");
			transDTO.setSettlementAmount(String.valueOf(sAmt));
			Timestamp date=(Timestamp) row.get("date");
			transDTO.setTxnDate(date);
			int transType=(int)row.get("transaction_type");
			transDTO.setProgramType(transType);
			String bCode=(String)row.get("bank_code");
			transDTO.setBankCode(bCode);
			transDTO.setMerchantId((String)row.get("merchant_id"));
			transDTO.setRefundedAmount((double)row.get("refunded_amount"));
			transDTO.setTid((String)row.get("tid"));
			logger.debug("BharatQRTransDTO checkBharatQRTrans(TCHRequest tchRequest) execution ends");
			return transDTO;	
		}
		else 
			return null;
	}

	@Override
	public void saveBharatQRRefundTxn(Object[] dataArr) {
		logger.debug("BharatQRTransDTO checkBharatQRTrans(TCHRequest tchRequest) execution starts");
		String query = QueryConstant.INSERT_BHARATQR_REFUND_TRANS;
		int count = jdbcTemplate.update(query,dataArr);
		logger.debug("saveBharatQRRefundTxn returns :"+count);
	}

	@Override
	public void updateRefundedAmount(int id, double amount) {
		String query = QueryConstant.UPDATE_REFUNDED_AMOUNT;
		logger.debug("query :"+query);
		logger.info("id :"+id);
		logger.info("refunded amount :"+amount);
		int count = jdbcTemplate.update(query,new Double(amount), new Integer(id));
		logger.debug("updateRefundedAmount returns :"+count);
	}

	@Override
	public void saveBharatQRTxn(final BharatQRTransDTO bharatQRTxn) {
		String query = QueryConstant.INSERT_BHARATQR_TXN;
		final Timestamp timeStamp = HelperUtil.parseTimestamp(bharatQRTxn.getTimeStamp(), "yyyyMMddHHmmss");
		AesUtil aesApp = EncryptionCache.getEncryptionUtility("APP");
		String encrypted_cardNumber=aesApp.encrypt(bharatQRTxn.getCustPan());

		Object[] parmArr = new Object[16];
		parmArr[0] =bharatQRTxn.getMpan();
		parmArr[1] =timeStamp;
		parmArr[2] =Integer.valueOf(bharatQRTxn.getCurrency());
		parmArr[3] =Double.valueOf(bharatQRTxn.getAmount());
		parmArr[4] =bharatQRTxn.getCustName();
		parmArr[5] =encrypted_cardNumber;
		parmArr[6] =bharatQRTxn.getAuthCode();
		parmArr[7] =bharatQRTxn.getRefNo();
		parmArr[8] = bharatQRTxn.getPrimaryId().trim();
		parmArr[9] =bharatQRTxn.getSecondaryId().trim();
		parmArr[10] =Double.valueOf(bharatQRTxn.getSettlementAmt());
		parmArr[11] =Integer.valueOf(bharatQRTxn.getTransactionType());
		parmArr[12] =bharatQRTxn.getTid();
		parmArr[13] =bharatQRTxn.getBankCode();
		parmArr[14] =bharatQRTxn.getAdditionalData().trim();
		parmArr[15] =bharatQRTxn.getMerchantId();
		PreparedStatementCreatorFactory psc = new PreparedStatementCreatorFactory(query, 
				new int[] {
						Types.VARCHAR , 
						Types.TIMESTAMP,
						Types.INTEGER,
						Types.DOUBLE,
						Types.VARCHAR,
						Types.VARCHAR,
						Types.VARCHAR,
						Types.VARCHAR,
						Types.VARCHAR,
						Types.VARCHAR,
						Types.DOUBLE,
						Types.INTEGER,
						Types.VARCHAR,
						Types.VARCHAR,
						Types.VARCHAR,
						Types.VARCHAR
		});
		psc.setReturnGeneratedKeys(true);
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		int count = jdbcTemplate.update(psc.newPreparedStatementCreator(parmArr),holder);
		bharatQRTxn.setId(holder.getKey().intValue());
		logger.debug("saveBharatQRTxn returns :"+count);
	}

	@Override
	public int updateBQRMMSFlag(String RRN, String Flag) {
		String query = QueryConstant.UPDATE_MMS_BQR_FLAG;
		logger.debug("query :"+query);
		logger.info("RRN :"+RRN);
		logger.info("MMS Flag :"+Flag);
		int count = jdbcTemplate.update(query,Flag,RRN);
		logger.debug("update MMS Flag returns :"+count);
		return count;
	}

	@Override
	public int updateBQRMRLFlag(String RRN,String Flag){
		String query = QueryConstant.UPDATE_MRL_BQR_FLAG;
		logger.debug("query :"+query);
		logger.info("RRN :"+RRN);
		logger.info("MRL Flag fro BQR :"+Flag);
		int count = jdbcTemplate.update(query,Flag,RRN);
		logger.debug("update MRL Flag returns :"+count);
		return count;
		
	}

	
	@Override
	public int updateUPIMRLFlag(String RRN,String Flag){
		String query = QueryConstant.UPDATE_MRL_UPI_FLAG;
		logger.debug("query :"+query);
		logger.info("RRN :"+RRN);
		logger.info("MRL Flag for UPI :"+Flag);
		int count = jdbcTemplate.update(query,Flag,RRN);
		logger.debug("update MRL Flag returns :"+count);
		return count;
		
	}

	@Override
	public int insertIpgRequest(String jsonRequest) {
		logger.debug("inserting Ipg request in db starts");
		String query = QueryConstant.INSERT_IPG_REQUEST;
		int count = jdbcTemplate.update(query,jsonRequest);
		logger.debug("IPg request insert in db count : "+count);
		return count;
	}

	@Override
	public void saveMMSTxn(MMSTransDTO bharatQRTxn) {
		String query = QueryConstant.INSERT_MMS_TXN;
		final Timestamp timeStamp = HelperUtil.parseTimestamp(bharatQRTxn.getTimeStamp(), "yyyyMMddHHmmss");
		AesUtil aesApp = EncryptionCache.getEncryptionUtility("APP");
		String encrypted_cardNumber=aesApp.encrypt(bharatQRTxn.getCustPan());

		Object[] parmArr = new Object[18];
		parmArr[0] =bharatQRTxn.getMpan();
		parmArr[1] =timeStamp;
		parmArr[2] =Integer.valueOf(bharatQRTxn.getCurrency());
		parmArr[3] =Double.valueOf(bharatQRTxn.getAmount());
		parmArr[4] =bharatQRTxn.getCustName();
		parmArr[5] =encrypted_cardNumber;
		parmArr[6] =bharatQRTxn.getAuthCode();
		parmArr[7] =bharatQRTxn.getRefNo();
		parmArr[8] = bharatQRTxn.getPrimaryId().trim();
		parmArr[9] =bharatQRTxn.getSecondaryId().trim();
		parmArr[10] =Double.valueOf(bharatQRTxn.getSettlementAmt());
		parmArr[11] =Integer.valueOf(bharatQRTxn.getTransactionType());
		parmArr[12] =bharatQRTxn.getTid();
		parmArr[13] =bharatQRTxn.getBankCode();
		parmArr[14] =bharatQRTxn.getAdditionalData().trim();
		parmArr[15] =bharatQRTxn.getMerchantId();
		parmArr[16] =bharatQRTxn.getBatchNumber();
		parmArr[17] =bharatQRTxn.getSchemeTransactionId();
		PreparedStatementCreatorFactory psc = new PreparedStatementCreatorFactory(query, 
				new int[] {
						Types.VARCHAR , 
						Types.TIMESTAMP,
						Types.INTEGER,
						Types.DOUBLE,
						Types.VARCHAR,
						Types.VARCHAR,
						Types.VARCHAR,
						Types.VARCHAR,
						Types.VARCHAR,
						Types.VARCHAR,
						Types.DOUBLE,
						Types.INTEGER,
						Types.VARCHAR,
						Types.VARCHAR,
						Types.VARCHAR,
						Types.VARCHAR,
						Types.VARCHAR,
						Types.VARCHAR
		});
		psc.setReturnGeneratedKeys(true);
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		int count = jdbcTemplate.update(psc.newPreparedStatementCreator(parmArr),holder);
		bharatQRTxn.setId(holder.getKey().intValue());
		logger.debug("saveBharatQRTxn returns :"+count);
	}

	@Override
	public boolean getRefundId(String rrn, String authCode, String tid,String refundId) {
		
		String query= QueryConstant.CHECK_REFUND_ID_BQR;
		int count= jdbcTemplate.queryForObject(query,new Object[]{rrn ,authCode , tid, refundId}, Integer.class);
		logger.info("Count refund for this reqRefundId : " + count);
		if(count > 0)
			return true;
		else
			return false;
	}

	@Override
	public Long getUPITransactionCount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getSchemeTransactionCount() {
		// TODO Auto-generated method stub
		return null;
	}


}
