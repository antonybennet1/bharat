package com.wl.upi.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wl.upi.dao.BharatQRTransactionDao;
import com.wl.upi.dao.BharatQRTransactionDaoImpl;
import com.wl.upi.dao.MerchantDAO;
import com.wl.upi.model.TxnDTO;
import com.wl.upi.util.Test;
import com.wl.util.EncryptionCache;
import com.wl.util.HelperUtil;
import com.wl.util.HttpsClient;
import com.wl.util.config.ApplicationConfig;
import com.wl.util.config.BankConfig;
import com.wl.util.model.AggregatorDetails;

@Service("aggregatorService")
public class AggregatorService {

	private static Logger logger = LoggerFactory.getLogger(AggregatorService.class);
	
	private static ExecutorService aggregatorThreadPool = Executors.newFixedThreadPool(10); 

	@Autowired
	@Qualifier("merchantDao")
	private  MerchantDAO merchantDao;
	
	/*@Autowired
	private UPITransactionDAO upiTransactionDAO;*/
	
	@Autowired
	private BharatQRTransactionDao bharatQRTransactionDao;

	/**
	 * @param txnDetails
	 */
	public void sendNotification(TxnDTO txnDetails,AggregatorDetails aggDetails,int programTypeStr){
		
		boolean ipgFlag = false;
		String mid = txnDetails.getMerchantId();
		
			JSONObject notificationJson = new JSONObject();
			try {
				if(mid!=null)
					notificationJson.put("mid", mid);
				if(txnDetails.getMpan()!=null)
					notificationJson.put("mpan", txnDetails.getMpan());
				if(txnDetails.getCustName()!=null)
					notificationJson.put("customer_name", txnDetails.getCustName());
				if(txnDetails.getCurrency()!=null)
					notificationJson.put("txn_currency", "356");
				else
					notificationJson.put("txn_currency", "356");
				notificationJson.put("txn_amount", String.format("%.2f", Double.parseDouble(txnDetails.getTxnAmount())));
				notificationJson.put("auth_code", txnDetails.getAuthCode());
				notificationJson.put("ref_no", txnDetails.getRrn());
				if("KLI".equalsIgnoreCase(aggDetails.getAggrName())){
					if(txnDetails.getTrId() != null){
						notificationJson.put("primary_id", txnDetails.getTrId().substring(0, 12));
					}else{
						notificationJson.put("primary_id", txnDetails.getPrimaryId());
					}
				}else{
					notificationJson.put("primary_id", txnDetails.getPrimaryId());
				}
				
			if ("00031".equals(txnDetails.getBankCode()) || "00079".equals(txnDetails.getBankCode())
					|| "IPG".equalsIgnoreCase(aggDetails.getAggrName())) {
				/***
				 * Bank Code added for kotak and Axis bank
				 */
					if(txnDetails.getCustomerVpa() != null){
						notificationJson.put("customer_vpa", txnDetails.getCustomerVpa());
					}
					if(txnDetails.getMerchantVpa() != null){
						notificationJson.put("merchant_vpa", txnDetails.getMerchantVpa());
					}
					if(txnDetails.getTrId() != null){
						notificationJson.put("tr_id", txnDetails.getTrId());
					}
				}
			
						
				if(txnDetails.getSecondaryId()!=null){
					notificationJson.put("secondary_id", txnDetails.getSecondaryId());
				}else{
					//get secondary id from tr id for AXis, IPG, KOTAK
				if ("00031".equals(txnDetails.getBankCode()) || "IPG".equalsIgnoreCase(aggDetails.getAggrName())
						|| "00079".equals(txnDetails.getBankCode())) {
						String secondary_id;
						if("KLI".equalsIgnoreCase(aggDetails.getAggrName())){
							if(txnDetails.getTrId() != null){
								notificationJson.put("secondary_id", txnDetails.getTrId().substring(12));
							}
						}else{
							//String secondary_id;
							if (txnDetails.getSecondaryId() != null) {
								notificationJson.put("secondary_id", txnDetails.getSecondaryId());
							}else{
								if(txnDetails.getTrId() != null){
									if(txnDetails.getTrId().length() >= 17){
										secondary_id = txnDetails.getTrId().substring(txnDetails.getTrId().length() - 17);
										notificationJson.put("secondary_id", secondary_id);
									}else {
										notificationJson.put("secondary_id", txnDetails.getTrId());
									}	
								}
								
								/*if(txnDetails.getTrId().length() >= 17){
									secondary_id = txnDetails.getTrId().substring(txnDetails.getTrId().length() - 17);
									notificationJson.put("secondary_id", secondary_id);
								}else {
									notificationJson.put("secondary_id", txnDetails.getTrId());
								}*/
							}

							/*if(txnDetails.getTrId().length() >= 17){
								secondary_id = txnDetails.getTrId().substring(txnDetails.getTrId().length() - 17);
								notificationJson.put("secondary_id", secondary_id);
							}else{
								notificationJson.put("secondary_id", txnDetails.getTrId());
							}*/
						}
						
					}
				}
				// Condition added for tid which is required for IPG
				if("00079".equals(txnDetails.getBankCode()) || "IPG".equalsIgnoreCase(aggDetails.getAggrName())){
					if(txnDetails.getTid()!=null){
						notificationJson.put("tid", txnDetails.getTid());
					}
				}
				if(txnDetails.getSettlementAmount()!=null)
					notificationJson.put("settlement_amount",String.format("%.2f", Double.parseDouble(txnDetails.getSettlementAmount())));
				else
					notificationJson.put("settlement_amount", String.format("%.2f", Double.parseDouble(txnDetails.getTxnAmount())));
				notificationJson.put("time_stamp", HelperUtil.toString(txnDetails.getTxnDate(),"yyyyMMddHHmmss"));
				
				if(txnDetails.getTransactionType() != null) {
					logger.info("sendNotification() Transaction Type if():::: " + txnDetails.getTransactionType());
					notificationJson.put("transaction_type", txnDetails.getTransactionType());
				}
				else {
					logger.info("sendNotification() Transaction Type  else:::: " + txnDetails.getTransactionType());
					notificationJson.put("transaction_type", "2");
				}
				/*if(txnDetails.getTransactionType() != null)
					notificationJson.put("transaction_type", txnDetails.getTransactionType());
				else
					notificationJson.put("transaction_type", "1");*/
				//notificationJson.put("transaction_type", txnDetails.getProgramType());
				
				notificationJson.put("bank_code", txnDetails.getBankCode());
				
				if(txnDetails.getCustPan()!=null)
				{
					String clearPan = EncryptionCache.getEncryptionUtility("APP").decrypt(txnDetails.getCustPan());
					String encPan = EncryptionCache.getEncryptionUtility("PAN").encrypt(clearPan);
					
					logger.info(txnDetails.getRrn()+"Encrypted PAN :"+encPan);
					notificationJson.put("consumer_pan",encPan);
				}
				String notJson = null;
				String URL = null;
				
				
			if (aggDetails != null) {
				
				notificationJson.put("aggregator_id", aggDetails.getAggrId());
				logger.info(txnDetails.getRrn() + "|Notification json :" + notificationJson.toString());
				URL = aggDetails.getUrl();
				notJson = notificationJson.toString();
				logger.info("Notification Json sending to aggregator : " + notJson);	
				
				if("IPG".equalsIgnoreCase(aggDetails.getAggrName())){
					ipgFlag =true;
					System.out.println("Enter in to ipg insert method");
					int ipgRecordCount=bharatQRTransactionDao.insertIpgRequest(notJson);
					System.out.println("Record ipg count ends : " + ipgRecordCount);
				}
				
				
				//send MRL notification only if program type is 9
				/*if (programTypeStr == 9) {
					URL = ApplicationConfig.get("mrlpay_url");
					logger.info(txnDetails.getRrn() + "|Merchant is a MRL merchant");
				}*/
			} /*else {
				if ("00031".equals(txnDetails.getBankCode())) {
					notificationJson.put("aggregator_id", "PAYC");
					URL = ApplicationConfig.get("paycraft_url");
					logger.info(txnDetails.getRrn() + "|Merchant is direct merchant of axis");

				} else {
					logger.info(txnDetails.getRrn() + "|Merchant is not aggregator merchant");
					return;
				}

			}*/

					
			if (ipgFlag == false) {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Content-Type", "application/json");
				AggregatorTask notiTask = new AggregatorTask(txnDetails.getRrn(), URL, notJson, headers);

				int i = 1;
				for (; i <= 3; i++) {
					try {
						aggregatorThreadPool.execute(notiTask);
						break;
					} catch (Exception e) {
						i++;
						logger.error("Connection error from aggregator , Resending request : " + " Count : " + i
								+ e.getMessage());
						logger.error(txnDetails.getRrn() + "|Aggregator Task has failed to execute", e);

					}
				}

			}
								
				
			} catch (JSONException e) {
				logger.error(txnDetails.getRrn()+"|Exception while creating notification json : ",e);
			}
			catch(Exception e)
			{
				logger.error(txnDetails.getRrn()+"|Exception while sending aggregator notification : ",e);
			}
		
		
	}
	
	
	public boolean sendBQRMrlNotification(TxnDTO txnDetails , int programType) {
		boolean flag =true;
		logger.info("Inside the sendBQRMrlNotification() ---> ");
		logger.info("Program type inside sendBQRMrlNotification ::::"+programType);
		String mid = txnDetails.getMerchantId();
		
			JSONObject notificationJson = new JSONObject();
			try {
				if(mid!=null) {
					notificationJson.put("mid", mid);
				}
				if(txnDetails.getMpan()!=null) {
					notificationJson.put("mpan", txnDetails.getMpan());
				}
				if(txnDetails.getCustName()!=null) {
					notificationJson.put("customer_name", txnDetails.getCustName());
				}
				if(txnDetails.getCurrency()!=null) {
					notificationJson.put("txn_currency", txnDetails.getCurrency());
				}else {
					notificationJson.put("txn_currency", "356");
				}				
				notificationJson.put("txn_amount", String.format("%.2f", Double.parseDouble(txnDetails.getTxnAmount())));
				notificationJson.put("auth_code", txnDetails.getAuthCode());
				notificationJson.put("ref_no", txnDetails.getRrn());
				
				/***
				 * Bank Code added for kotak and Axis bank
				 */
				
				if (txnDetails.getCustomerVpa() != null) {
					notificationJson.put("customer_vpa", txnDetails.getCustomerVpa());
				}
				if (txnDetails.getMerchantVpa() != null) {
					notificationJson.put("merchant_vpa", txnDetails.getMerchantVpa());
				}
				/*if (txnDetails.getTrId() != null) {
					notificationJson.put("tr_id", txnDetails.getTrId());
				}*/
				
				//get secondary id from tr id for AXis, IPG, KOTAK & for MRL Pay
	
				if(txnDetails.getTrId() != null) {
					String secondary_id;
					if (txnDetails.getTrId().length() >= 17) {
						secondary_id = txnDetails.getTrId().substring(txnDetails.getTrId().length() - 17);
						notificationJson.put("secondary_id", secondary_id);
						notificationJson.put("primary_id", txnDetails.getTrId());
					} else {
						notificationJson.put("secondary_id", txnDetails.getTrId());
						notificationJson.put("primary_id", txnDetails.getTrId());
					}
				}else {
					if(txnDetails.getSecondaryId()!=null){
						notificationJson.put("secondary_id", txnDetails.getSecondaryId());
					}
					if (txnDetails.getPrimaryId() != null) {
						notificationJson.put("primary_id", txnDetails.getPrimaryId());
					}
				}
				
				
				
				// Condition added for tid which is required for IPG
			//	if("00079".equals(txnDetails.getBankCode())){
					if(txnDetails.getTid()!=null){
						notificationJson.put("tid", txnDetails.getTid());
					}
				//}
				if(txnDetails.getSettlementAmount()!=null)
					notificationJson.put("settlement_amount",String.format("%.2f", Double.parseDouble(txnDetails.getSettlementAmount())));
				else
					notificationJson.put("settlement_amount", String.format("%.2f", Double.parseDouble(txnDetails.getTxnAmount())));
				notificationJson.put("time_stamp", HelperUtil.toString(txnDetails.getTxnDate(),"yyyyMMddHHmmss"));
				if(txnDetails.getTransactionType() != null) {
					logger.info("sendBQRMrlNotification() Transaction Type if():::: " + txnDetails.getTransactionType());
					notificationJson.put("transaction_type", txnDetails.getTransactionType());
				}
				else {
					logger.info("sendBQRMrlNotification() Transaction Type  else:::: " + txnDetails.getTransactionType());
					notificationJson.put("transaction_type", "2");
				}
				//notificationJson.put("transaction_type", txnDetails.getProgramType());
				
				notificationJson.put("bank_code", txnDetails.getBankCode());
				
				if(txnDetails.getCustPan()!=null)
				{
					String clearPan = EncryptionCache.getEncryptionUtility("APP").decrypt(txnDetails.getCustPan());
					//String encPan = EncryptionCache.getEncryptionUtility("PAN").encrypt(clearPan);
					
					logger.info(txnDetails.getRrn()+"Encrypted PAN :"+clearPan);
					notificationJson.put("consumer_pan",clearPan);
				}else {					
					String acqBankIdentification = BankConfig.get(txnDetails.getBankCode(),"acq_bank_no");
					String reverseRrn = new StringBuffer(txnDetails.getRrn()).reverse().toString();			
					String cardNumber = acqBankIdentification + reverseRrn;
					notificationJson.put("consumer_pan",cardNumber);					
				}
				
				String notJson = null;
				String URL = null;
				
				//send MRL notification only if program type is 9
				if (programType ==9 || programType ==11) {
					URL = ApplicationConfig.get("mrlpay_url");
					logger.info(txnDetails.getRrn() + "|Merchant is a MRL merchant");
				}
				
				notJson = notificationJson.toString();
				logger.info("Notification Json sending to MRL : " + notJson);		
				
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Content-Type", "application/json");
				AggregatorTask notiTask = new AggregatorTask(txnDetails.getRrn(), URL , notJson, headers);
				
			int i = 1;
			for (; i <= 3; i++) {
				try {
					aggregatorThreadPool.execute(notiTask);
					flag=true;
					break;
					//throw new Exception();
				} catch (Exception e) {
					i++;
					flag=false;
					logger.error("Connection error from MRL , Resending request : " + " Count : " + i
							+ e.getMessage());
					logger.error(txnDetails.getRrn() + "|MRL notification Task has failed to execute", e.getMessage());
				}
			}
		
				
			} catch (JSONException e) {
				flag=false;
				logger.error(txnDetails.getRrn()+"|Exception while creating notification json : ",e.getMessage());
			}
			catch(Exception e)
			{
				flag=false;
				logger.error(txnDetails.getRrn()+"|Exception while sending MRL notification : ",e.getMessage());
			}
			return flag;
		
		
	}
	
	/**
	 * Inner class which create aggregator task and sends the notification to aggregator with details passed.
	 * @author kunal.surana
	 */
	private class AggregatorTask implements Runnable
	{
		private String rrn;
		private String url;
		private String json;
		private Map<String, String> headers;
		
		public AggregatorTask(String rrn, String url, String json , Map<String, String> headers) {
			super();
			this.rrn=rrn;
			this.url = url;
			this.json = json;
			this.headers = headers;
		}
		
		@Override
		public void run() {
			String resp = HttpsClient.send(url, json, headers);
			//String resp = Test.send(url, json, headers);
			logger.info(rrn + "|Response from aggregator :" + resp);
		}
		
	}
}
