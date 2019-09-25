package com.wl.qr.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wl.qr.dao.MerchantDetailDao;
import com.wl.qr.dao.QrDao;
import com.wl.qr.model.MerchantDetails;
import com.wl.qr.model.QRTags;
import com.wl.qr.model.TagModel;
import com.wl.qr.utils.Utils;
import com.wl.util.HelperUtil;
import com.wl.util.config.BankConfig;
import com.wl.util.constants.ErrorMessages;
import com.wl.util.exceptions.ApplicationException;
import com.wl.util.model.Response;

@Service("qrService")
public class QRService {

	@Autowired
	@Qualifier("qrDao")
	private QrDao qrDao;

	@Autowired
	@Qualifier("merchantInfoDao")
	private MerchantDetailDao merchantInfoDao;

	private static final Logger logger = LoggerFactory.getLogger(QRService.class);

	public Response getQR(String fromEntity,String jsonRequest)
	{
		Response response = new Response();
		List<String> error_tags = new ArrayList<>();
		try {

			JSONObject obj = new JSONObject(jsonRequest);
			logger.info("Obj received " + obj.toString());
			
			if(!isAmountValid(obj.getString("amount"),obj.optString("mam")))
				throw new ApplicationException(ErrorMessages.INVALID_AMOUNT.toString());

			/* following done to fix tipConvIndicator coming as one digit for mobile app */
			String tipConvIndicator = obj.optString("tipConvIndicator");
			if(tipConvIndicator.length() == 1)
				obj.put("tipConvIndicator", "0"+tipConvIndicator);
			/* following done to fix tipConvIndicator coming as one digit for mobile app */

			String bank_code = obj.optString("bankCode");
			String merchant_id = obj.optString("merchantId");
			String terminalId = obj.optString("terminalId");
			//String purpose = obj.optString("purpose");
			//String fromEntity = obj.optString("fromEntity"); // this will come in method parameter not in JSON

			ObjectMapper mapper = new ObjectMapper();
			InputStream in = QRService.class.getResourceAsStream("/qr_tags.json");
			QRTags tags = mapper.readValue(in,QRTags.class);
			ArrayList<TagModel> all_tags = tags.getQr_data();

			String refNo = HelperUtil.getReferenceNumber();

			/*String bin = qrDao.getBankBin(bank_code);
			String trId = bin + merchant_id + refNo.substring(refNo.length() - 5);*/
			String trId = "";
			/* Following code added to implement QR code generation as per specification of aggregator v1.8 */
			String addData = obj.optString("addData");
			if(addData!=null && !addData.isEmpty())
			{
				if("KLI".equalsIgnoreCase(fromEntity)){
					trId=addData;
				}else{
					trId =refNo;
					logger.debug("Additional Data in TR:"+addData);
					trId = trId + addData;
				}
			}else{
				trId =refNo;
			}
			/* Following code added to implement QR code generation as per specification of aggregator v1.8 */

			StringBuilder qr_string = new StringBuilder();
			for (TagModel p : all_tags) {
				if(null != p.getChild_tags()){
					StringBuilder sub_tag_string = new StringBuilder();
					ArrayList<TagModel> child_tags = p.getChild_tags();
					for (TagModel c : child_tags) {
						sub_tag_string.append(addTag(obj, c, trId,error_tags,fromEntity));
					}
					String subStr = sub_tag_string.toString();
					qr_string.append(p.getTag() + Utils.getFormatedLength(subStr) + subStr);
				}
				else
					qr_string.append(addTag(obj, p, trId,error_tags,fromEntity));
			}
			//qr_string.toString();
			qr_string.append("6304");
			logger.debug("qrStr : "+qr_string.toString());

			String crc = Utils.CalcCRC16(qr_string.toString());
			logger.info("crc is "+ crc.substring(crc.length() - 4).toUpperCase());

			String  final_qr_string = qr_string.toString() + crc.substring(crc.length() - 4).toUpperCase();
			logger.info("final qr string with crc is "+ final_qr_string);

			/*int programType = 1;
			if(obj.has("vpa")){
				if(Utils.hasValue(obj.getString("vpa"))){
					programType  = 2;
				}
			} commented as QR Type to taken from json object
			 */
			//changes done to know the purpose of QR code generation. for example - 5 for qr generated on POS
			String programType = obj.getString("programType");

			String merchVPA = obj.optString("vpa");
			if(obj.has("amount")){
				qrDao.insertQRtring(final_qr_string, 2, bank_code, Integer.valueOf(programType), refNo , "G", terminalId, merchant_id, obj.getString("amount"), fromEntity,trId,merchVPA); //dynamic
			}else{
				qrDao.insertQRtring(final_qr_string, 1, bank_code, Integer.valueOf(programType), refNo, "G", terminalId, merchant_id, "", fromEntity,trId,merchVPA); //static
			}

			if(error_tags.size() > 0){
				logger.info("Error found " + error_tags);
				response.setStatus("FAILED");
				response.setMessage("MISSING TAG");
				HashMap<String,  List<String>> j = new HashMap<String,   List<String>>();
				j.put("fieldList", error_tags);
				response.setResponseObject(j);
				//response.setResponseCode("91"); //as not required
			}else{
				logger.info("final QR : " + final_qr_string);
				response.setStatus("SUCCESS");
				HashMap<String,  String> j = new HashMap<String,  String>();
				j.put("qrString", final_qr_string);
				j.put("trId", trId);
				if("KLI".equalsIgnoreCase(fromEntity)){
					j.put("txnId", trId.substring(0, 12));
				}else{
					j.put("txnId", refNo);
				}
				
				
				response.setResponseObject(j);
			}
		} catch (Exception e) {
			logger.error("Exception in getQR",e);
			response.setStatus("FAILED");
			response.setMessage("Exception Occured");
			//response.setResponseCode("91"); //as not required
		}
		return response;
	}


	private String addTag(JSONObject req_obj, TagModel p, String trId,List<String> error_tags,String fromEntity) throws JSONException {
		String str = "";
		if("tr".equals(p.getJson_mapping())){
			str = str + p.getTag() + Utils.getFormatedLength(trId) + trId;
		}else if("referenceId".equals(p.getJson_mapping())){
			/* Following fix done to restrict additional data sent from aggregator in referenceId */
			if("KLI".equalsIgnoreCase(fromEntity)){
				trId = trId.substring(0,12);
				str = str + p.getTag() + Utils.getFormatedLength(trId) + trId;
			}else{
				if(trId.length() > 18){
					trId = trId.substring(0, 18);
					str = str + p.getTag() + Utils.getFormatedLength(trId) + trId;
				}else{
					str = str + p.getTag() + Utils.getFormatedLength(trId) + trId;
				}
			}
			//str = str + p.getTag() + Utils.getFormatedLength(trId) + trId;
		}
		else	if(req_obj.has(p.getJson_mapping())){
			String value = req_obj.getString(p.getJson_mapping());
			if(Utils.hasValue(value)){
				/* Following changes added to solve prod issue of mcc which has length as 3 */
				if("mcc".equals(p.getJson_mapping()))
					value = String.format("%04d", Integer.parseInt(value));
				/* Following changes added to solve prod issue of mcc which has length as 3 */
				value = Utils.lengthChecker(value, p.getTag_length());
				str = str + p.getTag() + Utils.getFormatedLength(value) + value;
			}
		}else if(p.getDefault_value() != null){
			String value = Utils.lengthChecker(p.getDefault_value(), p.getTag_length());
			if(Utils.hasValue(value)){
				str = str + p.getTag() + Utils.getFormatedLength(value) + value;
			}
		}else if(p.getMendatory()){
			error_tags.add(p.getJson_mapping());
		}
		return str;
	}

	public Response getUpiQR(String fromEntity, String jsonRequest)
	{
		Response response = new Response();
		List<String> error_tags = new ArrayList<>();
		try {
			JSONObject obj = new JSONObject(jsonRequest);
			logger.info("Obj received " + obj.toString());

			if(!isAmountValid(obj.getString("amount"), obj.optString("mam")))
				throw new ApplicationException(ErrorMessages.INVALID_AMOUNT.toString());

			String bank_code = obj.optString("bankCode");
			String merchant_id = obj.optString("merchantId");
			String terminalId = obj.optString("terminalId");
			//String fromEntity = obj.optString("fromEntity"); // this will come in method parameter not in JSON

			//visa specific optional field 
			String addData = obj.optString("consumerId");

			ObjectMapper mapper = new ObjectMapper();
			InputStream in = QRService.class.getResourceAsStream("/upi_qr_tags.json");
			QRTags tags = mapper.readValue(in,QRTags.class);
			ArrayList<TagModel> all_tags = tags.getQr_data();
			//add URL and remove from tags
			StringBuilder qr_string = new StringBuilder();
			qr_string.append(all_tags.get(0).getDefault_value());
			all_tags.remove(0);

			String refNo = HelperUtil.getReferenceNumber();

			/*String bin = qrDao.getBankBin(bank_code);
			String trId = bin + merchant_id + refNo.substring(refNo.length() - 5);*/
			String trId = refNo;

			for (TagModel p : all_tags) {
				String tg = addUpiTag(obj, p, trId+addData, error_tags);
				if(!tg.isEmpty())
				{
					qr_string.append(tg);
					qr_string.append('&');
				}
				if(null != p.getChild_tags()){
					StringBuilder sub_tag_string = new StringBuilder();
					ArrayList<TagModel> child_tags = p.getChild_tags();
					for (TagModel c : child_tags) {
						String subtg =addUpiTag(obj, c, trId+addData, error_tags);
						if(!subtg.isEmpty())
						{
							sub_tag_string.append(subtg);
							sub_tag_string.append('&');
						}
					}
					String subStr = sub_tag_string.deleteCharAt(qr_string.length()-1).toString();
					qr_string.append(p.getTag() + Utils.getFormatedLength(subStr) + subStr);
					qr_string.append('&');
				}
			}

			int programType = obj.optInt("programType");
			qr_string.deleteCharAt(qr_string.length()-1);
			String merchVPA = obj.optString("vpa");
			if(obj.has("amount")){
				qrDao.insertQRtring(qr_string.toString(), 2, bank_code, programType, refNo, "G", terminalId, merchant_id, obj.getString("amount"), fromEntity,trId,merchVPA); //dynamic
			}else{
				qrDao.insertQRtring(qr_string.toString(), 1, bank_code, programType, refNo, "G", terminalId, merchant_id, "", fromEntity,trId,merchVPA); //static
			}
			logger.debug("qrStr : "+qr_string.toString());

			if(error_tags.size() > 0){
				logger.info("Error found " + error_tags);
				response.setStatus("FAILED");
				response.setMessage("MISSING TAG");
				HashMap<String,  List<String>> j = new HashMap<String,   List<String>>();
				j.put("fieldList", error_tags);
				response.setResponseObject(j);
				//response.setResponseCode("91"); //as not required
			}else{
				logger.info("final QR : " + qr_string);
				response.setStatus("SUCCESS");
				HashMap<String,  String> j = new HashMap<String,  String>();
				j.put("qrString", qr_string.toString());
				j.put("txnID", refNo);
				j.put("trId", trId);
				response.setResponseObject(j);
			}
		} catch (Exception e) {
			logger.error("Exception in getUpiQR",e);
			response.setStatus("FAILED");
			response.setMessage(e.getMessage());
			//response.setResponseCode("91");// as not required
		}
		return response;
	}

	private String addUpiTag(JSONObject req_obj, TagModel p, String trId,List<String> error_tags) throws JSONException {
		String str= "";
		if("tr".equals(p.getJson_mapping())){
			str = str + p.getTag() +"=" + trId;
		}else if(req_obj.has(p.getJson_mapping())){
			String value = req_obj.getString(p.getJson_mapping());
			if(Utils.hasValue(value)){
				/* Following changes added to solve prod issue of mcc which has length as 3 */
				if("mcc".equals(p.getJson_mapping()))
					value = String.format("%04d", Integer.parseInt(value));
				/* Following changes added to solve prod issue of mcc which has length as 3 */
				str = p.getTag() +"="+ value;
			}
		}else if(p.getDefault_value() != null){
			String value = p.getDefault_value();
			if(Utils.hasValue(value)){
				str = p.getTag() +"="+ value;
			}
		}else if(p.getMendatory()){
			error_tags.add(p.getJson_mapping());
		}		
		return str;
	}

	public Response entityQRGen(String fromEntity,String bankCode, String jsonRequest) throws ApplicationException{
		JSONObject reqData = new JSONObject();
		Response resp =null;
		try {
			JSONObject obj = new JSONObject(jsonRequest);
			 resp=validateRequest(obj,bankCode,fromEntity);
			if(!resp.getStatus().equalsIgnoreCase("Failed")){
				String qrType = obj.optString("qrType");
				if(qrType.isEmpty())
					qrType = "2";  // considering qrType as option.. if qrtype is not send, it will treated as dynamic QR.
				String programType = obj.optString("prgType");
				int prgType = 5;
				// changed as per code review comment
				try {
					prgType = Integer.valueOf(programType);
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					prgType = 5;
					programType = "5";
				} 
				// changed as per code review comment

				// get merchant info
				MerchantDetails merchantDetails=null;
				try {
					merchantDetails = merchantInfoDao.getMerchantInfo(obj.getString("MID"),
							obj.getString("TID"), bankCode);
				} catch (EmptyResultDataAccessException e) {
					// TODO Auto-generated catch block
					logger.error("MID and TID not found at mobile server",e);
					throw new ApplicationException("Merchant mapping not found");
				}
				if (merchantDetails != null) {
					logger.info("program type from DB"+merchantDetails.getProgramType());

					/* A new field added for aggregator APIs specially for VISA. VISA considers only first 2 tags. 
					As per discussion, reference number is mendatory tag which would be considered as primary id. 
					For secondary id, bank suggested to consider consumerId as secondary id in additional tags(Tag 62). 
					This tag is named as "addData" in getQR API document*/
					String addData = obj.optString("addData");
					if(!addData.isEmpty()){
						if("KLI".equalsIgnoreCase(fromEntity)){
							if(addData.length() < 35){
								reqData.put("consumerId", addData.substring(12));
							}
						}else if(addData.length() > 17){
							throw new ApplicationException(ErrorMessages.STRING_LENGTH_EXCEEDED.toString());
						}else{
							reqData.put("consumerId", addData);
						}
						
						reqData.put("addData", addData);
					}else{
						if("KLI".equalsIgnoreCase(fromEntity)){
							throw new ApplicationException(ErrorMessages.ADDITIONAL_EMPTY_KLIF.toString());
						}
					}
					if(bankCode.equals("00058")){
						logger.info("Purpose : -----> " +obj.optString("purpose"));
						if(obj.optString("purpose") != null || !obj.optString("purpose").equals("")){
							String purpose = obj.optString("purpose");
							reqData.put("purpose", purpose);
						}
					}
					// (prgType == 2) condition added to support QR code generation from APP
					if (prgType == 5 || prgType == 2) {
						reqData.put("bankCode", bankCode);
						if ("1".equals(qrType)) { // changes by ashish
							reqData.put("pointOfInitiation", "11"); 	// changes by ashish
						} else if ("2".equals(qrType)){
							reqData.put("pointOfInitiation", "12"); 		// changes by ashish
							reqData.put("amount", obj.getString("amount"));
						}

						setMpan(reqData,obj.getString("TID"),bankCode); // setting mpan details

						if (merchantDetails.getTipConvIndicator() == 1) {
							reqData.put("tipConvIndicator", "01");  
						} else if (merchantDetails.getTipConvIndicator() == 2) {
							if (merchantDetails.getConvenienceFlag() == 0) {
								reqData.put("tipConvIndicator", "02");
								reqData.put("convAmount", merchantDetails.getConvenienceValue());
							} else if (merchantDetails.getConvenienceFlag() == 1) {
								reqData.put("tipConvIndicator", "03");
								reqData.put("conPerc", merchantDetails.getConvenienceValue());
							}
						}

						reqData.put("ifscAccountNumber", merchantDetails.getIfscAccountNumber());
						reqData.put("mcc", String.valueOf(merchantDetails.getMerchantCategoryCode())); // changes by ashish
						reqData.put("currencyCode", String.valueOf(merchantDetails.getCurrencyCode()));// changes by ashish
						reqData.put("countryCode", merchantDetails.getCountryCode());
						reqData.put("merchantName", merchantDetails.getName());
						reqData.put("cityName", merchantDetails.getCityName());
						reqData.put("postalCode", merchantDetails.getPostalCode());
						reqData.put("terminalId", obj.getString("TID"));
						reqData.put("merchantId", obj.getString("MID"));
						reqData.put("programType", ""+merchantDetails.getProgramType());
						if(merchantDetails.getUpiVpa()!=null && !merchantDetails.getUpiVpa().isEmpty())
						{
							reqData.put("vpa", merchantDetails.getUpiVpa());
							reqData.put("mam", String.valueOf(merchantDetails.getUpiMam())); // changes by ashish
							reqData.put("upiUrl", merchantDetails.getUpiUrl());
							reqData.put("aadharNumber", merchantDetails.getAadharNumber());
						}

						if (obj.has("amount")) {
							reqData.put("amount", obj.getString("amount"));
						}
						resp = getQR(fromEntity, reqData.toString());
						Map<String, String> map =  (Map<String, String>) resp.getResponseObject();
						if(map!=null)
							map.put("prgType", programType);
					} else if (prgType == 3) {// upi txn
						reqData.put("upiUrl", merchantDetails.getUpiUrl());
						reqData.put("vpa", merchantDetails.getUpiVpa());
						reqData.put("merchantName", merchantDetails.getName());
						reqData.put("mcc", String.valueOf(merchantDetails.getMerchantCategoryCode()));
						if (obj.has("amount")) {
							reqData.put("amount", obj.getString("amount"));
						}
						reqData.put("mam", String.valueOf(merchantDetails.getUpiMam()));
						//reqData.put("cu", String.valueOf(merchantDetails.getCurrencyCode()));
						reqData.put("terminalId", obj.getString("TID"));
						reqData.put("merchantId", obj.getString("MID"));
						reqData.put("bankCode", merchantDetails.getBankCode());
						reqData.put("programType", merchantDetails.getProgramType());
						resp = getUpiQR(fromEntity, reqData.toString());
						Map<String, String> map =  (Map<String, String>) resp.getResponseObject();
						if(map!=null)
							map.put("prgType", programType);
					}
					return resp;
				} else {
					logger.info("merchant not present in db, TID: " + obj.getString("TID"));
					throw new ApplicationException("No record found");
				}
			}
			
		}catch (JSONException  e ) {
			logger.info("Exception",e);
			throw new ApplicationException(ErrorMessages.SERVER_ERROR.toString());
		}
		//return getUpiQR(fromEntity, reqData.toString());		
		return resp;
	}	



	private Response validateRequest(JSONObject obj,String bankCode,String fromEntity) throws JSONException {
		
		Response resp = new Response();
		boolean flag = true;
		boolean purposeFlag= true;
		String mid = obj.optString("MID");
		String tid = obj.optString("TID");
		String addData = obj.optString("addData");
		String amount = obj.optString("amount");
		String qrType = obj.optString("qrType");
		String prgType = obj.optString("prgType");
		String purpose=obj.optString("purpose");
		
		if(!mid.isEmpty()){
			if(!HelperUtil.allowAlphaNumericRegex(mid)){
				resp.setMessage("Special Characters is not allowed in MID");
				flag = false;
				return getRes(resp, flag);
			}
		}else{
			resp.setMessage("MID is mandatory");
			flag = false;
			return getRes(resp, flag);
		}
		if(!tid.isEmpty()){
			if(!HelperUtil.allowAlphaNumericRegex(tid)){
				resp.setMessage("Special Characters is not allowed in TID");
				flag = false;
				return getRes(resp, flag);
				}
		}else{
			resp.setMessage("TID is mandatory");
			flag = false;
			return getRes(resp, flag);
		}
		if(!bankCode.isEmpty()) {
			if(!HelperUtil.allowAlphaNumericRegex(bankCode)){
				resp.setMessage("Special Characters is not allowed in bankCode");
				flag = false;
				return getRes(resp, flag);
			}
		}else{
			resp.setMessage(" BankCode is mandatory");
			flag = false;
			return getRes(resp, flag);
		}
		if(!addData.isEmpty()) {
			if(!HelperUtil.allowAlphaNumericRegex(obj.getString("addData"))){
				resp.setMessage("Special Characters is not allowed in addData");
				flag = false;
				return getRes(resp, flag);
			}
		}
		if(!fromEntity.isEmpty()) {
			if(!HelperUtil.allowAlphaNumericRegex(fromEntity)){
				resp.setMessage("Special Characters is not allowed in fromEntity");
				flag = false;
				return getRes(resp, flag);
			}
		}else{
			resp.setMessage("FromEntity is mandatory ");
			flag = false;
			return getRes(resp, flag);
		}
		if(!amount.isEmpty()) {
			if(!HelperUtil.isAmountRegex(amount)){
				resp.setMessage("Amount is not proper");
				flag = false;
				return getRes(resp, flag);
			}
		}else{
			resp.setMessage("Amount is mandatory ");
			flag = false;
			return getRes(resp, flag);
		}
		
		if(!qrType.isEmpty()) {
			if(!HelperUtil.isIntegerRegex(qrType)){
				resp.setMessage("qrType should be integer");
				flag = false;
				return getRes(resp, flag);
				}
		}
		
		if(!prgType.isEmpty()) {
			if(!HelperUtil.isIntegerRegex(prgType)){
				resp.setMessage("prgType should be integer");
				flag = false;
				return getRes(resp, flag);
				}
		}
		
		if(bankCode.equals("00058")){
			try {
				if(!purpose.isEmpty()){
					if(!HelperUtil.allowAlphaNumericRegex(purpose)){
						resp.setMessage("Special Characters is not allowed in purpose");
						purposeFlag = false;
						return getRes(resp, purposeFlag);
					}else{
						purposeFlag=true;
						return getRes(resp, purposeFlag);
					}	
				}else{
					purposeFlag=true;
					return getRes(resp, purposeFlag);
				}
				
			} catch (Exception e) {
				purposeFlag=true;


			}
		}
		
		
		
		/*if(!HelperUtil.allowAlphaNumericRegex(obj.getString("MID"))){
			resp.setMessage("Special Characters is not allowed in MID");
			flag = false;
		}else if(!HelperUtil.allowAlphaNumericRegex(obj.getString("TID"))){
			resp.setMessage("Special Characters is not allowed in TID");
			flag = false;
		}else if(!HelperUtil.allowAlphaNumericRegex(obj.getString("addData"))){
			resp.setMessage("Special Characters is not allowed in addData");
			flag = false;
		}else if(!HelperUtil.allowAlphaNumericRegex(bankCode)){
			resp.setMessage("Special Characters is not allowed in bankCode");
			flag = false;
		}else if(!HelperUtil.allowAlphaNumericRegex(fromEntity)){
			resp.setMessage("Special Characters is not allowed in fromEntity");
			flag = false;
		}else if(!HelperUtil.isAmountRegex(obj.getString("amount") )){
			resp.setMessage("Amount is not proper");
			flag = false;
		}else if(!HelperUtil.isIntegerRegex(obj.getString("qrType") )){
			resp.setMessage("qrType should be integer");
			flag = false;
		}else if(!HelperUtil.isIntegerRegex(obj.getString("prgType") )){
			resp.setMessage("prgType should be integer");
			flag = false;
		}else if(bankCode.equals("00058")){
			try {
				if(!HelperUtil.allowAlphaNumericRegex(obj.getString("purpose"))){
					resp.setMessage("Special Characters is not allowed in purpose");
					purposeFlag = false;
				}else{
					purposeFlag=true;
				}
			} catch (Exception e) {
				purposeFlag=true;
			}
		}*/
			
			
		//System.out.println(obj.get("purpose"));
		
		/*if(obj.get("purpose").equals("abc")){
			try {
				if(!HelperUtil.allowAlphaNumericRegex(obj.getString("purpose"))){
					resp.setMessage("Special Characters is not allowed in purpose");
					flag = false;
				}else{
					flag=true;
				}
			} catch (Exception e) {
				flag=true;
			}
		}*/
			
		/*if(bankCode.equals("00058")){
			try {
				
			} catch (JSONException e) {
				flag = true;
			}
			
		}*/
		
		
		
		//System.out.println("response");
		return  getRes(resp, purposeFlag);
	}

	public Response getRes(Response resp , boolean flag ){
		/*if(!flag || !purposeFlag){
			resp.setStatus("Failed");
			resp.setResponseObject("");
		}else{
			resp.setStatus("success");
		}*/
		
		
		if(!flag){
			resp.setStatus("Failed");
			resp.setResponseObject("");
		}else{
			resp.setStatus("success");
		}
		return resp;
		
	}
	
	
	private void setMpan(JSONObject reqData, String tid, String bankCode) throws JSONException {
		// TODO Auto-generated method stub
		List<Map<String,Object>> mapList = merchantInfoDao.getMpanList(tid, bankCode);
		for(Map<String,Object> row : mapList)
		{
			String scheme = (String) row.get("scheme_id");
			String  mpan = (String) row.get("merchant_pan");
			logger.debug("MPAN :"+scheme+"|"+mpan);
			switch(scheme)
			{
			case "02" :
				reqData.put("visaMpan1",mpan );
				break;
			case "03" : 
				reqData.put("visaMpan2",mpan );
				break;
			case "04" :
				reqData.put("masterMpan1",mpan );
				break;
			case "05" : 
				reqData.put("masterMpan2",mpan );
				break;
			case "06" :
				reqData.put("rupayMpan1",mpan );
				break;
			case "07" : 
				reqData.put("rupayMpan2",mpan );
				break;
			}
		}
	}

	private boolean isAmountValid(String amount,String mamStr)
	{
		if(amount==null || amount.isEmpty())
			return false;
		if(amount.length()>13)
			return false;
		double amt = 0;
		try {
			amt = Double.valueOf(amount);
		} catch (NumberFormatException e) {
			return false;
		}
		double mam  = 0;
		if(mamStr!=null && !mamStr.isEmpty())
		{
			try {
				mam = Double.valueOf(mamStr);
			} catch (NumberFormatException e) {
				return false;
			}
		}
		if(amt < 1)
			return false;
		else if(amt < mam)
			return false;
		else
			return true;
	}
	/**
	 * Called by Magnus for <b>PSTN QR code</b>.
	 * This method is added for giving merchant information for creation of PSTN QR code using below parameters
	 * 
	 * @param fromEntity
	 * @param bankCode
	 * @param jsonRequest
	 * @return  json object, txn id ,tr id inside Response object
	 * @throws ApplicationException
	 */
	public Response entityQRGenPSTN(String fromEntity,String bankCode, String jsonRequest) throws ApplicationException{
		JSONObject reqData = new JSONObject();
		Response response = null;
		try {
			JSONObject obj = new JSONObject(jsonRequest);
			MerchantDetails merchantDetails=null;
			try {
				merchantDetails = merchantInfoDao.getMerchantInfo(obj.getString("MID"),
						obj.getString("TID"), bankCode);
			} catch (EmptyResultDataAccessException e) {
				 logger.error("MID and TID not found at mobile server",e);
				 throw new ApplicationException("Merchant mapping not found");
			}
			if (merchantDetails != null) {
				logger.info("program type from DB"+merchantDetails.getProgramType());
				String trId=null;
				String refNo = null;
				//if PSTN call getQR using magnus as fromEntity & program type will be 7
				if("Magnus".equalsIgnoreCase(fromEntity) && "7".equalsIgnoreCase(obj.optString("prgType"))){
					String visaMapn=merchantInfoDao.getVisaMPAN(obj.getString("TID"));
					refNo=HelperUtil.getReferenceNumberForPSTN(visaMapn);
					trId = refNo;
				}else{
					refNo= HelperUtil.getReferenceNumber();
	                trId = refNo;
				}
				
			
			     reqData.put("bankCode", bankCode);
				 setMpan(reqData,obj.getString("TID"),bankCode); // setting mpan details

				 if (merchantDetails.getTipConvIndicator() == 1) {
					 reqData.put("tipConvIndicator", "01");  
					 reqData.put("convAmount", "");
					 reqData.put("conPerc", "");
				} else if (merchantDetails.getTipConvIndicator() == 2) {
					if (merchantDetails.getConvenienceFlag() == 0) {
						reqData.put("tipConvIndicator", "02");
						reqData.put("conPerc", "");
						reqData.put("convAmount", merchantDetails.getConvenienceValue());
					} else if (merchantDetails.getConvenienceFlag() == 1) {
						reqData.put("tipConvIndicator", "03");
						reqData.put("conPerc", merchantDetails.getConvenienceValue());
						reqData.put("convAmount", "");
					}
				}
				 reqData.put("amexMpan1","" );
				reqData.put("ifscAccountNumber", merchantDetails.getIfscAccountNumber());
				reqData.put("mcc", String.valueOf(merchantDetails.getMerchantCategoryCode())); // changes by ashish
					reqData.put("currencyCode", String.valueOf(merchantDetails.getCurrencyCode()));// changes by ashish
					reqData.put("countryCode", merchantDetails.getCountryCode());
					reqData.put("merchantName", merchantDetails.getName());
					reqData.put("cityName", merchantDetails.getCityName());
					reqData.put("postalCode", merchantDetails.getPostalCode());
					reqData.put("terminalId", obj.getString("TID"));
					reqData.put("merchantId", obj.getString("MID"));
					reqData.put("refUrl", merchantDetails.getUpiUrl());
					reqData.put("vpa", merchantDetails.getUpiVpa());
					reqData.put("mam", String.valueOf(merchantDetails.getUpiMam()));
					reqData.put("aadharNumber", merchantDetails.getAadharNumber());
					reqData.put("rupayRid", "A000000524");
				if(obj.has("amount")){
					qrDao.insertQRtring(reqData.toString(), 2, bankCode, Integer.parseInt(obj.optString("prgType")), refNo, "G", obj.getString("TID"), obj.getString("MID"), obj.getString("amount"), fromEntity,trId,merchantDetails.getUpiVpa()); //dynamic
				}else{
					qrDao.insertQRtring(reqData.toString(), 1, bankCode, Integer.parseInt(obj.optString("prgType")), refNo, "G", obj.getString("TID"), obj.getString("MID"), "", fromEntity,trId,merchantDetails.getUpiVpa()); //static
				}
				response=new Response();
				response.setStatus("SUCCESS");
				HashMap<String,  String> j = new HashMap<String,  String>();
				j.put("qrString", reqData.toString());
				j.put("txnID", refNo);
				j.put("trId", trId);
				response.setResponseObject(j);
				return response;
			} else {
				logger.info("merchant not present in db, TID: " + obj.getString("TID"));
				throw new ApplicationException("No record found");
			}

		} catch (JSONException  e ) {
			logger.info("Exception",e);
			throw new ApplicationException(ErrorMessages.SERVER_ERROR.toString());
		}
	}
	
	/**
	 * This method is added for getting existing <b>static QR code string</b> from database using below parameters
	 * 
	 * @param fromEntity
	 * @param bankCode
	 * @param jsonRequest
	 * @return QR string, txn id ,tr id inside Response object
	 * @throws ApplicationException
	 */
     public Response getStaticQRString(String fromEntity,String bankCode,String jsonRequest) throws ApplicationException{
		
		Response response = null;
		try {
			JSONObject obj = new JSONObject(jsonRequest);
			String mId=obj.getString("MID");
			String tId=obj.getString("TID");
			
			if(mId.length()==0 || mId == null || mId.equals("null")){
				response=new Response();
				response.setStatus("Failed");
				response.setMessage("MID is not present");
				response.setResponseObject(jsonRequest);
				return response;
			}else if(tId.length() == 0 || tId == null || tId.equals("null")){
				response=new Response();
				response.setStatus("Failed");
				response.setMessage("TID is not present");
				response.setResponseObject(jsonRequest);
				return response;
			}else{
				Map<String, Object> row =qrDao.fetchQRString(mId, tId, "1");
				if (row == null) {
					  logger.info("Record not present in db, TID: " + tId);
					  throw new ApplicationException("No record found");
				} else {
					response=new Response();
					response.setStatus("SUCCESS");
					HashMap<String,  String> j = new HashMap<String,  String>();
					j.put("qrString", (String)row.get("data_string"));
					j.put("txnId", (String)row.get("txn_id"));
					j.put("trId", (String)row.get("tr_id"));
					response.setResponseObject(j);
					return response;
				}
			}
			//String qrType=obj.getString("qrType");
			
		} catch (JSONException  e ) {
			logger.info("Exception",e);
			throw new ApplicationException(e.getMessage());
		}		
	}	
     
     
     
     public Response entityQRGenIPG(String fromEntity,String bankCode, String jsonRequest) throws ApplicationException{
 		JSONObject reqData = new JSONObject();
 		Response resp = null;
 		try {
 			JSONObject obj = new JSONObject(jsonRequest);
 			String qrType = obj.getString("qrType");

 			//if(qrType.isEmpty())
 			//	qrType = "2";  // considering qrType as option.. if qrtype is not send, it will treated as dynamic QR.
 			String programType = obj.optString("prgType");
 			//int prgType = 6;
 			// changed as per code review comment
 			/*try {
 				prgType = Integer.valueOf(programType);
 			} catch (NumberFormatException e1) {
 				// TODO Auto-generated catch block
 				prgType = 6;
 				programType = "6";
 			} */
 			//if(obj.optString("prgType").isEmpty())
 			//	programType = "9";
 			// changed as per code review comment

 			// get merchant info
 			MerchantDetails merchantDetails=null;
 			try {
 				merchantDetails = merchantInfoDao.getMerchantInfo(obj.getString("MID"),
 						obj.getString("TID"), bankCode);
 			} catch (EmptyResultDataAccessException e) {
 				// TODO Auto-generated catch block
 				logger.error("MID and TID not found at mobile server",e);
 				throw new ApplicationException("Merchant mapping not found");
 			}
 			if (merchantDetails != null) {
 				logger.info("program type from DB"+merchantDetails.getProgramType());

 				/* A new field added for aggregator APIs specially for VISA. VISA considers only first 2 tags. 
 				As per discussion, reference number is mendatory tag which would be considered as primary id. 
 				For secondary id, bank suggested to consider consumerId as secondary id in additional tags(Tag 62). 
 				This tag is named as "addData" in getQR API document*/
 				String addData = obj.optString("addData");
 				if(!addData.isEmpty()){
 					if(addData.length() > 17){
 						throw new ApplicationException(ErrorMessages.STRING_LENGTH_EXCEEDED.toString());
 					}
 					reqData.put("consumerId", addData);
 					reqData.put("addData", addData);
 				}

 				 reqData.put("bankCode", bankCode);
 					if ("1".equals(qrType)) { 
 						reqData.put("pointOfInitiation", "11"); 	
 					} else if ("2".equals(qrType)){
 						reqData.put("pointOfInitiation", "12"); 		
 						reqData.put("amount", obj.getString("amount"));
 					}

 					setMpan(reqData,obj.getString("TID"),bankCode); // setting mpan details

 					if (merchantDetails.getTipConvIndicator() == 1) {
 						reqData.put("tipConvIndicator", "01");  
 					} else if (merchantDetails.getTipConvIndicator() == 2) {
 						if (merchantDetails.getConvenienceFlag() == 0) {
 							reqData.put("tipConvIndicator", "02");
 							reqData.put("convAmount", merchantDetails.getConvenienceValue());
 						} else if (merchantDetails.getConvenienceFlag() == 1) {
 							reqData.put("tipConvIndicator", "03");
 							reqData.put("conPerc", merchantDetails.getConvenienceValue());
 						}
 					}

 					reqData.put("ifscAccountNumber", merchantDetails.getIfscAccountNumber());
 					reqData.put("mcc", String.valueOf(merchantDetails.getMerchantCategoryCode())); // changes by ashish
 					reqData.put("currencyCode", String.valueOf(merchantDetails.getCurrencyCode()));// changes by ashish
 					reqData.put("countryCode", merchantDetails.getCountryCode());
 					reqData.put("merchantName", merchantDetails.getName());
 					reqData.put("cityName", merchantDetails.getCityName());
 					reqData.put("postalCode", merchantDetails.getPostalCode());
 					reqData.put("terminalId", obj.getString("TID"));
 					reqData.put("merchantId", obj.getString("MID"));
 					reqData.put("vpa", obj.getString("VPA"));
 					reqData.put("programType", ""+programType);
 					if(merchantDetails.getUpiVpa()!=null && !merchantDetails.getUpiVpa().isEmpty())
 					{
 						//reqData.put("vpa", merchantDetails.getUpiVpa());
 						reqData.put("mam", String.valueOf(merchantDetails.getUpiMam())); // changes by ashish
 						reqData.put("upiUrl", merchantDetails.getUpiUrl());
 						reqData.put("aadharNumber", merchantDetails.getAadharNumber());
 					}

 					/*if (obj.has("amount")) {
 						reqData.put("amount", obj.getString("amount"));
 					}*/
 					resp = getQRIPG(fromEntity, reqData.toString());
 					Map<String, String> map =  (Map<String, String>) resp.getResponseObject();
 					if(map!=null)
 						map.put("prgType", programType);
 				
 				return resp;
 			} else {
 				logger.info("merchant not present in db, TID: " + obj.getString("TID"));
 				throw new ApplicationException("No record found");
 			}

 		} catch (JSONException  e ) {
 			logger.info("Exception",e);
 			throw new ApplicationException(ErrorMessages.SERVER_ERROR.toString());
 		}
 		//return getUpiQR(fromEntity, reqData.toString());		
 	}
     
     
      
      public Response getQRIPG(String fromEntity,String jsonRequest)
  	  {
  		Response response = new Response();
  		List<String> error_tags = new ArrayList<>();
  		try {

  			JSONObject obj = new JSONObject(jsonRequest);
  			logger.info("Obj received " + obj.toString());
  			if(obj.getString("pointOfInitiation").equals("12")){
  			     if(!isAmountValid(obj.getString("amount"),obj.optString("mam")))
  				     throw new ApplicationException(ErrorMessages.INVALID_AMOUNT.toString());
  			}

  			/* following done to fix tipConvIndicator coming as one digit for mobile app */
  			String tipConvIndicator = obj.optString("tipConvIndicator");
  			if(tipConvIndicator.length() == 1)
  				obj.put("tipConvIndicator", "0"+tipConvIndicator);
  			/* following done to fix tipConvIndicator coming as one digit for mobile app */

  			String bank_code = obj.optString("bankCode");
  			String merchant_id = obj.optString("merchantId");
  			String terminalId = obj.optString("terminalId");
  			//String fromEntity = obj.optString("fromEntity"); // this will come in method parameter not in JSON

  			ObjectMapper mapper = new ObjectMapper();
  			InputStream in = QRService.class.getResourceAsStream("/qr_tags.json");
  			QRTags tags = mapper.readValue(in,QRTags.class);
  			ArrayList<TagModel> all_tags = tags.getQr_data();

  			String refNo = HelperUtil.getReferenceNumber();

  			/*String bin = qrDao.getBankBin(bank_code);
  			String trId = bin + merchant_id + refNo.substring(refNo.length() - 5);*/
  			String trId = refNo;
  			/* Following code added to implement QR code generation as per specification of aggregator v1.8 */
  			String addData = obj.optString("addData");
  			if(addData!=null && !addData.isEmpty())
  			{
  				logger.debug("Additional Data in TR:"+addData);
  				trId = trId + addData;
  			}
  			/* Following code added to implement QR code generation as per specification of aggregator v1.8 */

  			StringBuilder qr_string = new StringBuilder();
  			for (TagModel p : all_tags) {
  				if(null != p.getChild_tags()){
  					StringBuilder sub_tag_string = new StringBuilder();
  					ArrayList<TagModel> child_tags = p.getChild_tags();
  					for (TagModel c : child_tags) {
  						sub_tag_string.append(addTag(obj, c, trId,error_tags,fromEntity));
  					}
  					String subStr = sub_tag_string.toString();
  					qr_string.append(p.getTag() + Utils.getFormatedLength(subStr) + subStr);
  				}
  				else
  					qr_string.append(addTag(obj, p, trId,error_tags,fromEntity));
  			}
  			//qr_string.toString();
  			qr_string.append("6304");
  			logger.debug("qrStr : "+qr_string.toString());

  			String crc = Utils.CalcCRC16(qr_string.toString());
  			logger.info("crc is ", crc.substring(crc.length() - 4).toUpperCase());

  			String  final_qr_string = qr_string.toString() + crc.substring(crc.length() - 4).toUpperCase();
  			logger.info("final qr string with crc is ", final_qr_string);

  			/*int programType = 1;
  			if(obj.has("vpa")){
  				if(Utils.hasValue(obj.getString("vpa"))){
  					programType  = 2;
  				}
  			} commented as QR Type to taken from json object
  			 */
  			//changes done to know the purpose of QR code generation. for example - 5 for qr generated on POS
  			String programType = obj.getString("programType");

  			String merchVPA = obj.optString("vpa");
  			logger.info("pointOfInitiation qr type "+obj.getString("pointOfInitiation").equals("12"));
  			if(obj.getString("pointOfInitiation").equals("12")){
  				qrDao.insertQRtring(final_qr_string, 2, bank_code, Integer.valueOf(programType), refNo , "G", terminalId, merchant_id, obj.getString("amount"), fromEntity,trId,merchVPA); //dynamic
  			}else{
  				qrDao.insertQRtring(final_qr_string, 1, bank_code, Integer.valueOf(programType), refNo, "G", terminalId, merchant_id, "0.0", fromEntity,trId,merchVPA); //static
  			}

  			if(error_tags.size() > 0){
  				logger.info("Error found " + error_tags);
  				response.setStatus("FAILED");
  				response.setMessage("MISSING TAG");
  				HashMap<String,  List<String>> j = new HashMap<String,   List<String>>();
  				j.put("fieldList", error_tags);
  				response.setResponseObject(j);
  				//response.setResponseCode("91"); //as not required
  			}else{
  				logger.info("final QR : " + final_qr_string);
  				response.setStatus("SUCCESS");
  				HashMap<String,  String> j = new HashMap<String,  String>();
  				j.put("qrString", final_qr_string);
  				j.put("txnId", refNo);
  				j.put("trId", trId);
  				response.setResponseObject(j);
  			}
  		} catch (Exception e) {
  			logger.error("Exception in getQR",e);
  			response.setStatus("FAILED");
  			response.setMessage(e.getMessage());
  			//response.setResponseCode("91"); //as not required
  		}
  		return response;
  	}
     
}
