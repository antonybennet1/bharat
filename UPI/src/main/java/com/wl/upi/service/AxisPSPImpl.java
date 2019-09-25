/**
 * 
 */
package com.wl.upi.service;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wl.upi.dao.UPITransactionDAO;
import com.wl.upi.model.AxisPspRequest;
import com.wl.upi.model.ExceptionDTO;
import com.wl.upi.model.UPITransactionDTO;
import com.wl.util.AesUtil;
import com.wl.util.EncryptionCache;
import com.wl.util.HelperUtil;
import com.wl.util.JsonUtility;
import com.wl.util.PSPBankCodeMap;
import com.wl.util.RSAUtil;
import com.wl.util.constants.Constants;
import com.wl.util.constants.ErrorMessages;
import com.wl.util.exceptions.ApplicationException;


/**
 * @see PSPTransactionService
 * Following is AXIS PSP fields and their database mapping in upi_transaction table
 * Field Name - Database Column Name - Description
 * customerVpa - `customer_vpa`  - VPA of customer
 * merchantId - `additional_info1` - merchantId provided by AXIS PSP for each merchant. WL MID is expected in this field as per sonali.
 * merchantChannelId - `additional_info2` - merchantChannelId provided by AXIS PSP.
 * merchantTransactionId - `tr_id` - tr which is present in Bharat QR specs 4.0 ---> field 27 sub tag 01.
 * transactionTimestamp - `txn_date`  - transactionTimestamp as received from BANK PSP. Need to parse this to store in DB.
 * transactionAmount - `amount` - transactionAmount as received from BANK PSP
 * gatewayTransactionId - `gateway_trans_id` 
 * gatewayResponseCode - `response_code` - response code for transaction
 * gatewayResponseMessage - `response_message` - response message
 * rrn - `txn_ref_no` - This is RRN for transaction. This field can be used for future reference in case of any issue with PSP 
 *  
 * @author kunal.surana
 */
@Service("axisPSPImpl")
public class AxisPSPImpl implements PSPTransactionService {

	@Autowired
	private UPITransactionDAO upiTransactionDAO;

	private static Logger logger = LoggerFactory.getLogger(AxisPSPImpl.class);


	/* (non-Javadoc)
	 * @see com.wl.upi.service.PSPTransactionService#upiTransactionCallback(java.lang.String)
	 */
	@Override
	public Object upiTransactionCallback(String fromEntity, HttpServletRequest req) {
		logger.info("Starts UPITransaction method ");
		ExceptionDTO exce = null;
		String decryptedData = null;
		String data = null;
		String requestBody = "";
		/* Following code moved from Controller to Service as this will be bank specific : Start */
		try {
			 requestBody = IOUtils.toString(req.getReader());
			AxisPspRequest axisRequest =  (AxisPspRequest) JsonUtility.parseJson(requestBody,AxisPspRequest.class);
			if(axisRequest==null){
				/*Object excepArr[] ={
						fromEntity,
						"",requestBody,
						ErrorMessages.JSON_FORMAT_ERROR.toString(),"2","Before decryption"
				};
				upiTransactionDAO.saveTransactionException(excepArr);*/
				throw new ApplicationException(ErrorMessages.JSON_FORMAT_ERROR.toString());
				//return createExceptionMap(axisRequest);
			}
			AesUtil aes = EncryptionCache.getEncryptionUtility(fromEntity);
			data = new String(axisRequest.getData().getBytes("UTF-8"));
			logger.info("data:"+data);
			decryptedData = aes.decrypt(data);
		} catch (Exception e) {
			/*Object excepArr[] ={
					fromEntity,
					"",requestBody,
					ErrorMessages.SERVER_ERROR.toString(),"2","Aes Initialization failed while decryption"
			};
			upiTransactionDAO.saveTransactionException(excepArr);*/
			logger.error("Aes Initialization failed"+e);
			throw new ApplicationException(ErrorMessages.SERVER_ERROR.toString());
		}
		/* Following code moved from Controller to Service as this will be bank specific : End */
		
		//Response response = new Response();
		UPITransactionDTO upiTxn = (UPITransactionDTO) JsonUtility.parseJson(decryptedData,
				UPITransactionDTO.class);
		if(upiTxn==null){
			/*Object excepArr[] ={
					fromEntity,
					"",decryptedData,
					ErrorMessages.JSON_FORMAT_ERROR.toString(),"2","After Decryption"
			};
			upiTransactionDAO.saveTransactionException(excepArr);*/
			return createExceptionMap(upiTxn,ErrorMessages.JSON_FORMAT_ERROR.toString());
			//throw new ApplicationException(ErrorMessages.JSON_FORMAT_ERROR.toString());
		}
		//PITransactionDTO.setCustomerName("   ");             //missing from input string  as per doc
		
		boolean present = upiTransactionDAO.checkUPITxnExists(upiTxn.getMerchantTransactionId(), upiTxn.getRrn());
		if(present){
			/*Object excepArr[] ={
					fromEntity,
					upiTxn.getRrn(),decryptedData,
					ErrorMessages.DUPLICATE_TXN.toString(),"2","Transaction already present"
			};
			upiTransactionDAO.saveTransactionException(excepArr);*/
			logger.info("txn already present for : " + upiTxn.getRrn());
			//throw new ApplicationException(ErrorMessages.DUPLICATE_TXN.toString());
			return createExceptionMap(upiTxn,ErrorMessages.DUPLICATE_TXN.toString());
		}
		
		
		try{
			logger.info("Transaction details:"+upiTxn);
			String checkSum = upiTxn.getCustomerVpa()  + upiTxn.getPspMerchantId()+ upiTxn.getMerchantChannelId() + upiTxn.getMerchantTransactionId() + upiTxn.getTransactionTimestamp()+ 
					upiTxn.getTransactionAmount() + upiTxn.getGatewayTransactionId() +  upiTxn.getGatewayResponseCode()+ upiTxn.getGatewayResponseMessage();
			logger.debug("checksum :"+checkSum);
		//	RSAUtil rsa = new RSAUtil();
			//System.out.println("Our generated checksum :" + rsa.encrypt(checkSum));
			
			logger.debug("Credit Vpa ---> " + upiTxn.getCreditVpa());
			
			
			// Checking if merchant vpa (creditVpa) present before upi transaction 
			int count =  upiTransactionDAO.getMerchantVpaExist(upiTxn.getCreditVpa());
			if(count==0){
				Object excepArr[] ={
						fromEntity,
						upiTxn.getRrn(),decryptedData,
						ErrorMessages.MERCHANT_VPA_NOT_FOUND.toString(),"2","Merchant VPA not found"
				};
				upiTransactionDAO.saveTransactionException(excepArr);
				//throw new ApplicationException(ErrorMessages.MERCHANT_VPA_NOT_FOUND.toString());
				return createExceptionMap(upiTxn,ErrorMessages.MERCHANT_VPA_NOT_FOUND.toString());
				
			}
			
			
			
			/*RSAUtil rsa = null;
			try {
				String publicKeyStr = BankConfig.get("00031", "rsaWlPublicKey");
				InputStream privateKeyStream = AxisPSPImpl.class.getClassLoader().getResourceAsStream("/wl_axis_privkey.pkcs8");
				byte[] keyBytes = new byte[privateKeyStream.available()];
				privateKeyStream.read(keyBytes);
				privateKeyStream.close();
				rsa = new RSAUtil(publicKeyStr.getBytes(),keyBytes);
				rsa.setEncoding(Encoding.HEX);
			} catch (Exception e1) {
				logger.error("Unable to initialize RSA ",e1);
			}
			
			
			if(rsa!=null)
			{
				String encryptedChecksum = rsa.encrypt(checkSum);
				String decryptedChecksum = rsa.decrypt(upiTxn.getChecksum());
				logger.debug("decryptedChecksum:"+decryptedChecksum);
				if(upiTxn.getChecksum()!=null && !upiTxn.getChecksum().equalsIgnoreCase(encryptedChecksum))
					throw new ApplicationException("Checksum failure");
			}*/
			/*TxnDTO txnDetail = upiTransactionDAO.getUpiTxnDetails(upiTxn.getMerchantTransactionId());
			if(txnDetail!=null && "2".equals(txnDetail.getQrType()))
				throw new ApplicationException(ErrorMessages.TXN_ALREADY_DONE.toString());*/
			
			
			
			
			//START commented when tr_id is generating with diff logic and not getting in db 
			Map<String, Object> rows = upiTransactionDAO.getTxnQRDetails(upiTxn.getMerchantTransactionId());
			upiTxn.setBankCode(PSPBankCodeMap.getBankCode(fromEntity));
			if(rows!=null){
				setTxnQRDetails(rows, upiTxn);
			}else{
				/*Map<String, Object> rows2 = upiTransactionDAO.getTxnQRDetailsMerchantIdVpa(upiTxn.getMerchantVpa());
				
				upiTxn.setBankCode(PSPBankCodeMap.getBankCode(fromEntity));
				if(rows2!=null){
					setTxnQRDetailsMerchantIdVPA(rows2, upiTxn);
				}*/
				
				
				Map<String, Object> row2 = upiTransactionDAO.getTxnQRDetailsFromVpa(upiTxn.getCreditVpa());
				upiTxn.setBankCode(PSPBankCodeMap.getBankCode(fromEntity));
				if(row2!=null){
					setTxnQRDetailsVPA(row2, upiTxn);
				}else{
					logger.info("UPI QR Transaction details not found");
					Object excepArr[] ={
							fromEntity,
							upiTxn.getRrn(),decryptedData,
							ErrorMessages.TXN_QR_DETAIL_NOT_FOUND.toString(),"2","Details not found through vpa in detail table"
					};
					upiTransactionDAO.saveTransactionException(excepArr);
					//throw new ApplicationException(ErrorMessages.TXN_QR_DETAIL_NOT_FOUND.toString());
					return createExceptionMap(upiTxn,ErrorMessages.TXN_QR_DETAIL_NOT_FOUND.toString());
				}
			}
			logger.debug("UPI txn details:"+upiTxn);
			//END 

			// calling DAO method
			String []timestamp =  upiTxn.getTransactionTimestamp().split("\\+");
			
			Object dataArr[] = {
			upiTxn.getMerchantId(),
			upiTxn.getCreditVpa(), 
			upiTxn.getQrCodeType(),
			upiTxn.getTxnId(),
			upiTxn.getMerchantTransactionId(), 
			upiTxn.getRrn(),
			//HelperUtil.parseTimestamp(timestamp[0], "yyyy-MM-dd'T'HH:mm:ss.S"),
			HelperUtil.parseTimestamp(timestamp[0], "yyyy-MM-dd'T'HH:mm:ss"),
			upiTxn.getTransactionAmount(), 
			upiTxn.getBankCode(),
			upiTxn.getCustomerVpa(), 
			upiTxn.getGatewayResponseCode(), 
			upiTxn.getGatewayResponseMessage(),							
			fromEntity, 
			upiTxn.getTerminalId(),					
			upiTxn.getGatewayTransactionId(),
			upiTxn.getPspMerchantId(),
			upiTxn.getMerchantChannelId(),
			null,
			null};			
			upiTransactionDAO.saveUpiTxn(dataArr);
		}catch(Exception e){
			/*Object excepArr[] ={
					fromEntity,
					upiTxn.getRrn(),decryptedData,
					ErrorMessages.SERVER_ERROR.toString(),"2","Exception"
			};
			upiTransactionDAO.saveTransactionException(excepArr);*/
				//throw new ApplicationException(ErrorMessages.SERVER_ERROR.toString());
			return createExceptionMap(upiTxn,ErrorMessages.SERVER_ERROR.toString());
			//}
			
		}
		Map<String, String> map = new LinkedHashMap<>();
		map.put("callBackstatusCode", upiTxn.getGatewayResponseCode());
		map.put("callBackstatusDescription", upiTxn.getGatewayResponseMessage());
		map.put("callBacktxnId", upiTxn.getGatewayTransactionId());
		map.put("trId", upiTxn.getMerchantTransactionId());
		map.put("mid", upiTxn.getMerchantId());
		map.put("bankCode", upiTxn.getBankCode());
		//Added parameter merchVPA for checking transaction by VPA if fail to get transaction BY TrId 
		map.put("merchVPA",upiTxn.getCreditVpa());
		map.put("rrn",upiTxn.getRrn());
		return map;
	}
	
	
	private Object createExceptionMap(UPITransactionDTO upiTxn,String errorMessage) {
		
		Map<String, String> map = new LinkedHashMap<>();
		map.put("callBackstatusCode", "111");
		map.put("callBackstatusDescription", errorMessage);
		map.put("status", Constants.FAILED.name());
		if(upiTxn != null){
			map.put("callBacktxnId",upiTxn.getTxnId());
			map.put("trId", upiTxn.getMerchantTransactionId());
			map.put("mid", upiTxn.getMerchantId());
			map.put("bankCode", upiTxn.getBankCode());
			//Added parameter merchVPA for checking transaction by VPA if fail to get transaction BY TrId 
			map.put("merchVPA",upiTxn.getCreditVpa());
			map.put("rrn",upiTxn.getRrn());
		}else{
			map.put("callBacktxnId","");
			map.put("trId", "");
			map.put("mid", "");
			map.put("bankCode", "");
			//Added parameter merchVPA for checking transaction by VPA if fail to get transaction BY TrId 
			map.put("merchVPA","");
			map.put("rrn","");
		}
		
		return  map;
	}


	/**
	 * This function sets the result of qr details in UPITransactionDTO
	 * @param rows database result received
	 * @param upiTxn instance of {@link UPITransactionDTO}
	 */
	private void setTxnQRDetails(Map<String,Object> row, UPITransactionDTO upiTxn)
	{
		if(row!=null)
		{
				upiTxn.setMerchantId((String)row.get("merch_id"));
				upiTxn.setMerchantVpa((String)row.get("merch_vpa"));
				upiTxn.setQrCodeType((String)row.get("qr_type"));
				upiTxn.setTxnId((String)row.get("txn_id"));
				upiTxn.setBankCode((String)row.get("bank_code"));
				upiTxn.setTerminalId((String)row.get("terminal_id"));
		}
		
	}
	
	
	private void setTxnQRDetailsVPA(Map<String,Object> row, UPITransactionDTO upiTxn)
	{
		if(row!=null)
		{
				upiTxn.setMerchantId((String)row.get("merchant_id"));
				upiTxn.setBankCode((String)row.get("bank_code"));
				upiTxn.setTerminalId((String)row.get("TID"));
		}
		
	}
}
