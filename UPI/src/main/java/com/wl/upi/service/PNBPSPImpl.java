/**
 * 
 */
package com.wl.upi.service;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wl.upi.dao.UPITransactionDAO;
import com.wl.upi.model.KotakUPITransactionDTO;
import com.wl.upi.model.TxnDTO;
import com.wl.upi.pnb.Body;
import com.wl.upi.pnb.Envelope;
import com.wl.upi.pnb.MerchantStatusUpdateResponse;
import com.wl.upi.pnb.Req;
import com.wl.upi.pnb.Resp;
import com.wl.upi.pnb.Return;
import com.wl.util.HelperUtil;
import com.wl.util.PSPBankCodeMap;
import com.wl.util.XMLUtility;
import com.wl.util.constants.ErrorMessages;
import com.wl.util.exceptions.ApplicationException;

/**
 * @author ritesh.patil
 *
 *Mapping for XML request classes to database Table 
 *
 * class Field          DB column
 * merchantID           merch_id
 * payeeVirAddr         merch_vpa
 * txnRefId             tr_id
 * orgTxnRefId          txn_ref_no
 * timeStamp            txn_date
 * amount               amount 
 * payerVirAddr         customer_vpa
 * resCode              response_code
 * resDesc              response_message
 * txnId                gateway_trans_id
 * orgTxnId             additional_info1
 * payerCode            additional_info2
 * payeeCode             additional_info3
 * 
 */

@Service("pNBPSPImpl")
public class PNBPSPImpl implements  PSPTransactionService{
	
	
	@Autowired
	private UPITransactionDAO upiTransactionDAO;

	private static Logger logger = LoggerFactory.getLogger(PNBPSPImpl.class);

	@Override
	public Object upiTransactionCallback(String fromEntity, HttpServletRequest req) {
		
		logger.info("upiTransactionCallback(String fromEntity, HttpServletRequest req) method execution ends");
		String requestBody=null;
		Req reqObject=null;
		try {
			requestBody = IOUtils.toString(req.getReader());
			logger.info("Request with xml data :"+requestBody);
			Envelope envelope=(Envelope) XMLUtility.unMarshal(requestBody, Envelope.class);
			reqObject=envelope.getBody().getMerchantStatusUpdateReq().getReq();
			
		} catch (IOException e) {

			/*Object excepArr[] ={
					fromEntity,
					"",requestBody,
					"Marshaling Exception for Request","2","Exception while unmarshaling request "
			};
			upiTransactionDAO.saveTransactionException(excepArr);*/
			logger.error("Exception while unmarshaling request ",e);
			//throw new ApplicationException("Marshaling Exception for Request");
			Req reqObject1 = new Req();
			reqObject1.setResDesc("Marshaling Exception for Request");
			String xmlResponse=generateXMLExceptionResponse(reqObject1);
			/*logger.info("xml marshling generate response"+xmlResponse);
			Map<String, String> responseMap = new LinkedHashMap<>();
			responseMap.put("xmlResponse",xmlResponse);
			return responseMap;*/
			return createResponseMap(xmlResponse);
		}
		
		String validationString=validate(reqObject);
		if(!validationString.isEmpty()){
			/*Object excepArr[] ={
					fromEntity,
					reqObject.getOrgTxnRefId(),requestBody,
					ErrorMessages.JSON_FORMAT_ERROR.toString(),"2","Some Mandatory fields are not present"
			};
			upiTransactionDAO.saveTransactionException(excepArr);*/
			//throw new ApplicationException(ErrorMessages.JSON_FORMAT_ERROR.toString());
				reqObject.setResDesc(validationString);
				String xmlResponse=generateXMLExceptionResponse(reqObject);
				return createResponseMap(xmlResponse);
			}
		
		Map<String, Object> row = upiTransactionDAO.getTxnQRDetails(reqObject.getUPI().getTxnRefId());
		String qrCode=null,txnId=null,bankCode=null,terminalId=null;
		bankCode=PSPBankCodeMap.getBankCode(fromEntity);
		/**
		 * 
		 * Changes done for customer who does not have trid 
		 * contains only VPA so we are checking if transaction not found by trId
		 * we are finding transaction by VPA. 
		 * 
		 * Case 1 : if TrId present in request then get first record (one or multiple)
		 * CAse 2 : if vpa present & has only one record in detail table so return record
		 * Case 3 : If find multiple records for same VPA it will set terminalid as null  
		 */
		if(row!=null)
		{	
			qrCode=(String)row.get("qr_type");
			txnId=(String)row.get("txn_id");
			bankCode=(String)row.get("bank_code");
			terminalId=(String)row.get("terminal_id");
		}else{
			Map<String, Object> row2 = upiTransactionDAO.getTxnQRDetailsFromVpa(reqObject.getPayeeVirAddr());
			if(row2!=null){
				logger.debug("Getting TID from VPA ==> " + (String)row2.get("TID"));
				qrCode="0";
				txnId=reqObject.getUPI().getTxnId();
				bankCode=(String)row2.get("bank_code");
				terminalId=(String)row2.get("TID");
			}else{
				logger.info("UPI QR Transaction details not found");
				Object excepArr[] ={
						fromEntity,
						reqObject.getOrgTxnRefId(),requestBody,
						ErrorMessages.TXN_QR_DETAIL_NOT_FOUND.toString(),"2","Details not found through vpa in detail table"
				};
				upiTransactionDAO.saveTransactionException(excepArr);
				reqObject.setResDesc(ErrorMessages.TXN_QR_DETAIL_NOT_FOUND.toString());
				String xmlResponse=generateXMLExceptionResponse(reqObject);
				return createResponseMap(xmlResponse);
			}
		}
		logger.debug("UPI txn details:"+reqObject.getUPI());
		//Date timeStamp=new Date(reqObject.getUPI().getTimeStamp());
		//As per discussed with Shayam just changing gateway Transaction id  reqObject.getOrgTxnId() to reqObject.getUPI().getTxnId()
		Object dataArr[] = {
				reqObject.getUPI().getMerchantID(),
				reqObject.getPayeeVirAddr(), 
				qrCode,
				txnId,
				reqObject.getUPI().getTxnRefId(), 
				reqObject.getOrgTxnRefId(),
				HelperUtil.parseTimestamp(reqObject.getUPI().getTimeStamp(), "ddMMyyHHmmss"),
				reqObject.getAmount(),
				bankCode,
				reqObject.getPayerVirAddr(),
				reqObject.getResCode(),
				reqObject.getResDesc(),							
				fromEntity,
				terminalId,
				reqObject.getOrgTxnId(),
				null,reqObject.getPayerCode(),reqObject.getPayeeCode(),null};
		try {
			 upiTransactionDAO.saveUpiTxn(dataArr);
		} catch (Exception e) {
			/*Object excepArr[] ={
					fromEntity,
					reqObject.getOrgTxnRefId(),requestBody,
					ErrorMessages.SERVER_ERROR.toString(),"2","Exception while inserting in upi_transaction table:"
			};
			upiTransactionDAO.saveTransactionException(excepArr);*/
			logger.error(reqObject.getUPI().getTxnRefId()+"|Exception while inserting in upi_transaction table:",e);
			reqObject.setResDesc(ErrorMessages.JSON_FORMAT_ERROR.toString());
			String xmlResponse=generateXMLExceptionResponse(reqObject);
			return createResponseMap(xmlResponse);
		}
		
		//responseMap.put("status", Constants.SUCCESS.name());
		//responseMap.put("callBackstatusCode", upiTxn.getGatewayResCode());
		//responseMap.put("callBackstatusDescription", upiTxn.getGatewayReseMsg());
		//responseMap.put("callBacktxnId", upiTxn.getMerchTransId());
		String xmlResponse=generateXMLResponse(reqObject);
		logger.info("xml marshling generate response"+xmlResponse);
		Map<String, String> responseMap = new LinkedHashMap<>();
		responseMap.put("trId", reqObject.getUPI().getTxnRefId());
		responseMap.put("mid", reqObject.getUPI().getMerchantID());
		responseMap.put("bankCode", bankCode);
		responseMap.put("xmlResponse",xmlResponse);
		//Added parameter merchVPA for checking transaction by VPA if fail to get transaction BY TrId 
		responseMap.put("merchVPA",reqObject.getPayeeVirAddr());
		responseMap.put("rrn",reqObject.getOrgTxnRefId());
		responseMap.put("callBackstatusCode",reqObject.getResCode());
		logger.info("upiTransactionCallback(String fromEntity, HttpServletRequest req) method execution ends");
		return responseMap;
	}
	
	
	/**
	 * This function 
	 * @param reqObject Req object getting from request
	 * 
	 */
	private String generateXMLResponse(Req reqObject)
	{
		Envelope envelope =new Envelope();
		Body body=new Body();
		MerchantStatusUpdateResponse merchantStatusUpdateResponse=new MerchantStatusUpdateResponse();
		Return return1=new Return();
		Resp resp=new Resp();
		resp.setOrgTxnId(reqObject.getOrgTxnId());
		resp.setOrgTxnRefId(reqObject.getOrgTxnRefId());
		resp.setResCode(reqObject.getResCode());
		resp.setResDesc(reqObject.getResDesc());
		resp.setTimeStamp(HelperUtil.parseTimestamp(reqObject.getUPI().getTimeStamp(), "ddMMyyHHmmss").toString());
		resp.setTxnId(reqObject.getUPI().getTxnId());
		return1.setResp(resp);
		merchantStatusUpdateResponse.setReturn(return1);
		body.setMerchantStatusUpdateResponse(merchantStatusUpdateResponse);
		envelope.setBody(body);
		return XMLUtility.marshal(envelope, Envelope.class);
		
	}
	
	private String validate( Req reqObject){

		StringBuffer buffer=new StringBuffer();
		if(reqObject.getUPI().getMerchantID() == null || reqObject.getUPI().getMerchantID().isEmpty())
			buffer.append(" Merchant code is empty,");
		if(reqObject.getUPI().getTxnRefId() == null || reqObject.getUPI().getTxnRefId().isEmpty())
			buffer.append(" Transaction Id is empty, ");
		if(reqObject.getAmount() == null || reqObject.getAmount().isEmpty())
			buffer.append(" Transaction amount is empty, ");
		 if(reqObject.getOrgTxnRefId() == null || reqObject.getOrgTxnRefId().isEmpty())
			buffer.append(" Rrn is empty, ");
		 if(reqObject.getUPI().getTimeStamp() == null){
			 buffer.append(" Transaction Date is null ");
		 }else if(reqObject.getUPI().getTimeStamp() != null){
			 java.sql.Timestamp t = HelperUtil.parseTimestamp(reqObject.getUPI().getTimeStamp(), "ddMMyyHHmmss");
			 if(t ==null){
				 buffer.append(" Invalid date format");
			 }
		 }
		return buffer.toString();
		
	}
	
	/**
	 * This function 
	 * @param reqObject Req object getting from request
	 * 
	 */
	private String generateXMLExceptionResponse(Req reqObject){
		Envelope envelope =new Envelope();
		Body body=new Body();
		MerchantStatusUpdateResponse merchantStatusUpdateResponse=new MerchantStatusUpdateResponse();
		Return return1=new Return();
		Resp resp=new Resp();
		resp.setOrgTxnId(reqObject.getOrgTxnId());
		resp.setOrgTxnRefId(reqObject.getOrgTxnRefId());
		resp.setResCode("111");
		resp.setResDesc(reqObject.getResDesc());
		//if(HelperUtil.parseTimestamp(reqObject.getUPI().getTimeStamp(), "ddMMyyHHmmssS") == null)
		resp.setTimeStamp(reqObject.getUPI().getTimeStamp());
		resp.setTxnId(reqObject.getUPI().getTxnId());
		return1.setResp(resp);
		merchantStatusUpdateResponse.setReturn(return1);
		body.setMerchantStatusUpdateResponse(merchantStatusUpdateResponse);
		envelope.setBody(body);
		return XMLUtility.marshal(envelope, Envelope.class);
		
	}
	
	private Map<String,String> createResponseMap(String xmlResponse){
		logger.info("xml marshling generate response"+xmlResponse);
		Map<String, String> responseMap = new LinkedHashMap<>();
		responseMap.put("xmlResponse",xmlResponse);
		return responseMap;
	}
}
