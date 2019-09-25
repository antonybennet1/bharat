package com.wl.upi.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
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

import com.wl.upi.dao.MerchantDAO;
import com.wl.upi.model.DeviceDetails;
import com.wl.upi.model.TxnDTO;
import com.wl.upi.sms.SMSVendor;
import com.wl.upi.sms.SMSVendorFactory;
import com.wl.util.EncryptionCache;
import com.wl.util.HelperUtil;
import com.wl.util.HttpsClient;
import com.wl.util.UtilityHelper;
import com.wl.util.config.ApplicationConfig;
import com.wl.util.config.BankConfig;
import com.wl.util.exceptions.ApplicationException;

import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;

/**
 * NotificationService is the service class that does the push notification. Detail of notification is to be passed in JSON format in constructor 
 * @author kunal.surana
 */
@Service("notificationService")
public class NotificationService {

	private static Logger logger = LoggerFactory.getLogger(NotificationService.class);

	private static ExecutorService notificationThreadPool = Executors.newFixedThreadPool(20); 

	@Autowired
	@Qualifier("merchantDao")
	private  MerchantDAO merchantDao;


	@Autowired
	@Qualifier("smsVendorFactory")
	private SMSVendorFactory smsVendorFactory;

	/**
	 * This function send push notification to mobile APP based on deviceId. If merchant is Aggregator merchant then notification will be sent to Aggregator
	 * @param bankCode bankcode
	 * @param trId trId as received from PSP.
	 */
	public TxnDTO sendUPIPushNotification(String bankCode,String trId,TxnDTO txnDetails)
	{

		logger.info(trId+"|Transaction details :"+txnDetails);
		Integer programType  = txnDetails.getProgramType();
		logger.debug(trId+"|Program type:"+programType);
		//Added programType!=9 in if condition bcz the decel will be displayed on MRL POS so we can't send notification 
		if(programType!=5 && programType!=9 )
		{
			String amount = ""+txnDetails.getTxnAmount();
			String customerVPA = txnDetails.getCustomerVpa();
			String rrn = txnDetails.getRrn();
			String responseCode = txnDetails.getResponseCode();
			String tid =  txnDetails.getTid();

			JSONObject notificationJson = null;
			List<DeviceDetails> deviceList = null;
			if("00".equals(responseCode) ||"000".equals(responseCode))
			{
				/*String title = "Transaction Sucessful";
				String message = "Rs. %s received from %s VPA on %s. Transaction Reference - %s, RRN - %s";*/
				String title = "Transaction Successful";
				String message = BankConfig.get(bankCode,"upi_notification_message");
				logger.info("message ---> " +message);
				String strDate = HelperUtil.toString(txnDetails.getTxnDate(), "dd-MM-yyyy HH:mm:ss");
				message = String.format(message, amount,strDate, trId ,customerVPA, rrn);
				logger.info(trId+"|Notification Message is :"+message);
				notificationJson = new JSONObject();
				try {
					notificationJson.put("title", title);
					notificationJson.put("message", message);
				} catch (JSONException e1) {
					logger.error(trId+"|unable to create notification json object");
					throw new ApplicationException("Unable to create notification json object");
				}	
				deviceList = merchantDao.getDeviceDetails(tid);

				if(notificationJson!=null && deviceList!=null)
				{
					try {
						if(!"00031".equals(bankCode)){
					NotificationTask notiTask = new NotificationTask(bankCode, notificationJson, deviceList);
					notificationThreadPool.execute(notiTask);
					}
					SMSTask smsTask = new SMSTask(bankCode, message, deviceList);
					notificationThreadPool.execute(smsTask);
					} catch (Exception e) {
						logger.error("Notification Task has failed to execute",e);
					}
				}
			}
		}
		else
			logger.debug(trId+"|Push Notification not sent as merchant is of POS merchant");
		return txnDetails;
	}

	public TxnDTO sendBharatQRPushNotification(Map<String,String> map,TxnDTO txnDetails)
	{
		String id = map.get("id");
		String mid = map.get("mid");
		String bankCode = map.get("bankCode");
		String programTypeStr = map.get("programType");
		logger.info(id+"|mid:"+mid);
		if(txnDetails!=null)
		{
			logger.info(id+"|Transaction details :"+txnDetails);
			int programType  = Integer.parseInt(programTypeStr);
			logger.info(id+"|programType:"+programType);
			if(programType!=5)
			{
				String amount = ""+txnDetails.getTxnAmount();
				String customerName = txnDetails.getCustName().trim();
				String customerPan = txnDetails.getCustPan();
				String mpan = txnDetails.getMpan();
				try {
					customerPan = EncryptionCache.getEncryptionUtility("APP").decrypt(customerPan);
					customerPan = UtilityHelper.maskCardNumber(customerPan, "####-xxxx-xxxx-####");
				} catch (Exception e2) {
					logger.error(id+"|error decrypting customer pan",e2);
				}
				String authCode = txnDetails.getAuthCode();
				String tid =  txnDetails.getTid();

				JSONObject notificationJson = null;
				List<DeviceDetails> deviceList = null;

				String title = "Transaction Successful";
				String message = BankConfig.get(bankCode,"brq_notification_message");
				String strDate = HelperUtil.toString(txnDetails.getTxnDate(), "dd-MM-yyyy HH:mm:ss");
				message = String.format(message, amount,customerName, strDate, customerPan, authCode , mpan);
				logger.info(id+"|Notification title is :"+title);
				logger.info(id+"|Notification Message is :"+message);
				notificationJson = new JSONObject();
				try {
					notificationJson.put("title", title);
					notificationJson.put("message", message);
				} catch (JSONException e1) {
					logger.error(id+"|unable to create notification json object");
					throw new ApplicationException("Unable to create notification json object");
				}	
				deviceList = merchantDao.getDeviceDetails(tid);

				if(notificationJson!=null && deviceList!=null)
				{
					NotificationTask notiTask = new NotificationTask(bankCode, notificationJson, deviceList);
					try {
						notificationThreadPool.execute(notiTask);
					} catch (Exception e) {
						logger.error(id+"|Notification Task has failed to execute",e);
					}
				}
			}
			else
				logger.debug(id+"|Push Notification not sent as merchant is os POS merchant");
		}
		else
			logger.error(id+"|Txn not found");
		return txnDetails;
	}


	/**
	 * Inner class which create notification task and sends the notification to device list passed.
	 * @author kunal.surana
	 */
	private class NotificationTask implements Runnable
	{
		private String bankCode;
		private JSONObject  notificationObj;
		private List<DeviceDetails> deviceList;

		/**
		 * Inner class which create notification task and sends the notification to device list passed.
		 * @param bankCode bankcode 
		 * @param notificationObj  notification details which is JSON object with fields like title and message
		 * @param deviceList the list of device id and platform for user logged in should be passed
		 */
		public NotificationTask(String bankCode, JSONObject notificationObj, List<DeviceDetails> deviceList) {
			super();
			this.bankCode = bankCode;
			this.notificationObj = notificationObj;
			this.deviceList = deviceList;
		}

		/**
		 *  Function to send pushNotification
		 */
		private void pushNotification()
		{
			for (DeviceDetails dd : deviceList)
			{
				if(!dd.getPlatform().isEmpty() && !dd.getDeviceId().isEmpty() && dd.isInAppUser())
				{
					logger.debug("Starting notification for Platform   :"+dd.getPlatform()+"|deviceId:"+dd.getDeviceId());
					switch (dd.getPlatform().toLowerCase()) {
					case "android":
						sendAndroidNotification(dd.getDeviceId());
						break;
					case "ios":
						sendIosNotification(dd.getDeviceId());					
						break;
					case "windows":
						sendWindowsNotification(dd.getDeviceId());
						break;
					default:
						break;
					}
				}
			}
		}

		/**
		 * This function sends notification to windows device based on deviceID. 
		 * @param deviceId
		 */
		private void sendWindowsNotification(String deviceId) {
			throw new UnsupportedOperationException("Windows push notification not yet implemented");
		}

		/**
		 * This function sends notification to ios device based on deviceID. 
		 * @param deviceId device id of mobile
		 */
		private void sendIosNotification(String deviceId) {
			String password=BankConfig.get(bankCode, "iosNotificationP12Pwd");
			String sound = "default";
			boolean production=true; //change to false while testing
			int badge = 1;

			//BasicConfigurator.configure();
			String path=BankConfig.get(bankCode, "iosNotificationP12Path"); //BankwiseProperty.getp12path(getBankCode());
			logger.info("p12path is "+path);
			logger.info("p12password is "+password);
			File keystore = new File(path);

			try {
				String title = notificationObj.getString("title");
				String message = notificationObj.getString("message");
				String iosMessage = title+"\n"+message;
				//ProxyManager.setProxy(PropertyFileHandler.getProxyIp(), PropertyFileHandler.getProxyPort());
				Push.combined(iosMessage, badge, sound, keystore, password, production, deviceId);
			} catch (CommunicationException | KeystoreException e) {
				logger.error("Exception while sending ios notification",e);
			} catch (JSONException e) {
				logger.error("Exception while sending ios notification",e);
			}
		}

		/**
		 * This function sends notification to android device based on deviceID. 
		 * @param deviceId device id of mobile
		 */
		private void sendAndroidNotification(String deviceId) {

			final String apiKey = BankConfig.get(bankCode, "androidNotificationApiKey");
			logger.info("sending notificarion to " + deviceId);
			logger.info(bankCode+"|api key" + apiKey);
			String fcmResponse = null;

			try {
				JSONObject notification = new JSONObject();
				notification.put("body", notificationObj.getString("message"));
				notification.put("sound", "default");
				notification.put("click_action", "MENUActivity");

				JSONObject jsonNotification = new JSONObject();
				jsonNotification.put("to", deviceId);
				jsonNotification.put("notification", notification);
				String queryString = jsonNotification.toString();

				String firebaseUrl = ApplicationConfig.get("firebaseUrl");
				HashMap<String, String> headers = new HashMap<>();
				headers.put("Authorization", "KEY="+ apiKey);
				headers.put("Content-Type","application/json");
				fcmResponse = HttpsClient.send(firebaseUrl, queryString, headers);
				logger.debug("Response from firebaseUrl:"+fcmResponse);
			} catch (Exception e) {
				logger.error("Exception while sending android notification",e);
			}
		}

		@Override
		public void run() {
			logger.debug("starting pushNotification");
			pushNotification();
		}
	}
	
	
	/**
	 * Inner class which create SMS sending task and sends the SMS to mobile number passed in device list.
	 * @author kunal.surana
	 */
	private class SMSTask implements Runnable
	{
		private String bankCode;
		private String  smsText;
		private List<DeviceDetails> deviceList;

		/**
		 * Inner class which create notification task and sends the notification to device list passed.
		 * @param bankCode bankcode 
		 * @param smsText  SMS text to be snet
		 * @param deviceList the list of Mobile Numbers for sending SMS.
		 */
		public SMSTask(String bankCode, String smsText, List<DeviceDetails> deviceList) {
			super();
			this.bankCode = bankCode;
			this.smsText = smsText;
			this.deviceList = deviceList;
		}

		/**
		 *  Function to push SMS to mobile numbers
		 */
		private void pushSMS()
		{
			for (DeviceDetails dd : deviceList)
			{
				/*if(!dd.getPlatform().isEmpty() && !dd.getDeviceId().isEmpty())
				{
					logger.debug(dd.getMobileNumber()+"|Starting SMS sending for Mobile Number");
					SMSVendor smsVendor = smsVendorFactory.getSMSVendor(this.bankCode);
					boolean flag = smsVendor.sendSms(dd.getMobileNumber(), this.smsText, this.bankCode);
					if(flag)
						logger.info(dd.getMobileNumber()+"|SMS sent");
					else
						logger.info(dd.getMobileNumber()+"|SMS sending failed");
				}*/
				if ((dd.isInAppUser() == false && dd.isPrimaryUser() == true)
						|| (dd.isInAppUser() == false && dd.isPrimaryUser() == false)
						|| (dd.isInAppUser() == true && dd.isPrimaryUser() == true)) {
                           logger.debug("Mobile no is "+dd.getMobileNumber() + "  Status of app user "+  dd.isInAppUser() );
                           logger.debug(dd.getMobileNumber()+"|Starting SMS sending for Mobile Number");
                           SMSVendor smsVendor = smsVendorFactory.getSMSVendor(this.bankCode);
                           boolean flag = smsVendor.sendSms(dd.getMobileNumber(), this.smsText, this.bankCode);
                           if(flag)
                                  logger.info(dd.getMobileNumber()+"|SMS sent");
                           else
                                  logger.info(dd.getMobileNumber()+"|SMS sending failed");
                      }
             }
		}
		@Override
		public void run() {
			pushSMS();
		}
	}

}
