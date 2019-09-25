package com.wl.instamer.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wl.instamer.dao.DetailDAO;
import com.wl.instamer.iso.ISO8583;
import com.wl.instamer.model.MerchantOnboard;
import com.wl.instamer.model.MerchantOnboardInsight;
import com.wl.instamer.model.MerchantOnboardResponse;
import com.wl.instamer.model.MerchantOnboardResponseData;
import com.wl.instamer.model.MerchantOnboardResponseDataWrap;
import com.wl.instamer.model.Response;
import com.wl.instamer.util.TcpMagnusClient;
import com.wl.instamer.validation.ValidationFactory;
import com.wl.instamer.validation.Validator;
import com.wl.util.DesUtil;
import com.wl.util.HashGeneration;
import com.wl.util.HelperUtil;
import com.wl.util.HttpsClient;
import com.wl.util.JsonUtility;
import com.wl.util.config.ApplicationConfig;
import com.wl.util.constants.Constants;
import com.wl.util.constants.ErrorMessages;
import com.wl.util.exceptions.ApplicationException;


@Service
public class MerchantOnboardServiceImpl implements MerchantOnboardService {

	private static final Logger logger = LoggerFactory.getLogger(MerchantOnboardServiceImpl.class);
	private static volatile Socket socketObj = null;

	@Autowired
	@Qualifier("detailDAO")
	private DetailDAO details ;


	@Autowired
	@Qualifier("magnusClient")
	private TcpMagnusClient magnusClient ;

	@Autowired
	@Qualifier("iso8583")
	private ISO8583 iso;

	@Override
	public Response processRequest(String jsonRequest) {
		// TODO Auto-generated method stub
		MerchantOnboard merchantDetails = (MerchantOnboard) JsonUtility.parseJson(jsonRequest, MerchantOnboard.class);
		if(merchantDetails==null)
			throw new ApplicationException(ErrorMessages.JSON_FORMAT_ERROR.toString());
		String requestId = HelperUtil.getRequestId();
		merchantDetails.setRequestId(requestId);

		logger.debug(merchantDetails.getRequestId()+"|Incoming Merchant onboard request for bankcode :"+ merchantDetails.getBankCode() +"| request is:"+ jsonRequest);
		logger.debug(merchantDetails.getRequestId()+"|MerchantOnboard|"+merchantDetails);

		Validator<MerchantOnboard> validator =  ValidationFactory.getMerchantOnboardValidator(merchantDetails.getBankCode());
		List<String> errorCodes = validator.validate(merchantDetails);
		//List<String> errorCodes = Collections.emptyList();
		Response response = new Response();
		if(errorCodes!=null && errorCodes.isEmpty())
		{	
			MerchantOnboardResponse resp = getResponseFromInsight(merchantDetails);
			//resp.setResponseCode("1");

			if(resp!=null && "1".equals(resp.getResponseCode()))
			{
				String[] data = resp.getData().split("\\|");
				String sealValue = HashGeneration.convertToMD5(data[0]+new String(DesUtil.getKeyStr()));
				logger.debug(merchantDetails.getRequestId()+"|Generated Response Seal Value :"+sealValue);
				logger.debug(merchantDetails.getRequestId()+"|Response Seal Value :"+data[1]);
				String decData =  DesUtil.decrypt(data[0]);
				logger.debug(merchantDetails.getRequestId()+"|Decrypted Response data:"+decData);
				if(decData == null)
				{
					logger.error(merchantDetails.getRequestId()+"|Unable to decrypt response data element");
					response.setStatus(Constants.FAILED.name());
					response.setMessage("There seems to be technical error! Please try again later");
					return response;
				}
				MerchantOnboardResponseDataWrap  dataObjWrap  = (MerchantOnboardResponseDataWrap) JsonUtility.parseJson(decData, MerchantOnboardResponseDataWrap.class);
				MerchantOnboardResponseData dataObj = dataObjWrap.getSuccess();
				// code to be removed latter:Start
				/*MerchantOnboardResponseData  dataObj  = new MerchantOnboardResponseData();
				dataObj.setAmexMpan("121212121");
				dataObj.setApplicationNumber("1212");
				dataObj.setBankCode("11");
				dataObj.setMasterMpan("4554555");
				dataObj.setMerchantCode("010");
				dataObj.setMobileNumber("1234567894");
				dataObj.setRupayMpan("1545454");
				dataObj.setTid("1050");
				dataObj.setVisaMpan("425454");*/
				// code to be removed latter:Start

				if(dataObj!=null)
				{
					//Added By ashwin : START

					String msgForMagnus = iso.createMerchantOnboardISO(merchantDetails, dataObj);
					//String msgForMagnus = "0610203800000A8000009900002434421153541706237174062300019699999999";
					String magnusRes = null;
					try {
						magnusRes = magnusClient.send(msgForMagnus);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					System.out.println("Response from Magnus : " + magnusRes);
					String[] msgArray= new String[1];
					try {
						 msgArray= iso.parseIsoMessage(magnusRes, 0);
					} catch (ParseException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}


				//	msgArray [2] = "00";// have to remove
					if(msgArray[2] == "00")
					{
						try{
							saveInDB(merchantDetails, dataObj);
						}catch(Exception e)
						{
							logger.error("exception while saving in db",e);
						}
					}
					else
					{
						response.setStatus(Constants.FAILED.name());
						response.setMessage("There seems to be error from Magnus!");
						return response;
					}

					//Added By ashwin : END
					response.setStatus(Constants.SUCCESS.name());
					response.setResponseObject(dataObj);
					return response;
				}
				else
				{
					logger.error(merchantDetails.getRequestId()+"|Unable to parse response data json");
					response.setStatus(Constants.FAILED.name());
					response.setMessage("There seems to be technical error! Please try again later");
					return response;
				}
			}	
			else
			{
				if(resp!=null)
				{
					String[] data = resp.getData().split("\\|");
					String sealValue = HashGeneration.convertToMD5(data[0]+new String(DesUtil.getKeyStr()));
					logger.debug(merchantDetails.getRequestId()+"|Generated Response Seal Value :"+sealValue);
					logger.debug(merchantDetails.getRequestId()+"|Response Seal Value :"+data[1]);
					String decData =  DesUtil.decrypt(data[0]);
					logger.debug(merchantDetails.getRequestId()+"|Decrypted Response data:"+decData);

					errorCodes  = JsonUtility.getErrorCodes(decData);

					logger.error(merchantDetails.getRequestId()+"|Fail response from Insight|"+resp.getData());
					response.setStatus(Constants.FAILED.name());
					response.setMessage("Request Validation Failed");
					response.setResponseObject(errorCodes);
					return response;
				}
				else
				{
					logger.error(merchantDetails.getRequestId()+"|Response from Insight improper. Json parsing error");
					response.setStatus(Constants.FAILED.name());
					response.setMessage("There seems to be technical error! Please try again later");
					return response;
				}
			}
		}
		else
		{
			logger.info(merchantDetails.getRequestId()+"|errorCodes"+errorCodes.size() +"errorCodes values"+errorCodes.toString());
			response.setStatus(Constants.FAILED.name());
			response.setMessage("Request Validation Failed");
			response.setResponseObject(errorCodes);
		}
		return response;
	}

	private MerchantOnboardResponse getResponseFromInsight(MerchantOnboard merchantDetails)
	{
		String url = "http://10.10.11.102/InstaOnBoard/Create";
		MerchantOnboardResponse resp = null;
		String requestId = merchantDetails.getRequestId();

		MerchantOnboardInsight insightDetails = new MerchantOnboardInsight();
		BeanUtils.copyProperties(merchantDetails, insightDetails);

		String jsonStr = JsonUtility.convertToJson(insightDetails);

		logger.debug(requestId+"|Insight clear Json:"+jsonStr);

		String encData = DesUtil.encrypt(jsonStr);
		
		logger.debug(requestId+"|Insight encrypted Json:"+encData);

		String sealValue = HashGeneration.convertToMD5(encData+new String(DesUtil.getKeyStr()));
		logger.debug(requestId+"|Seal Value :"+sealValue);

		HashMap<String,String> headerMap = new HashMap<String,String>(4);
		headerMap.put("RequestId", requestId);
		headerMap.put("ClientId", merchantDetails.getBankCode());
		headerMap.put("SealValue", sealValue);
		headerMap.put("Content-Type","application/x-www-form-urlencoded");
		/*HttpHeaders headerMap = new HttpHeaders();
		headerMap.add("RequestId", requestId);
		headerMap.add("ClientId", merchantDetails.getBankCode());
		headerMap.add("SealValue", sealValue);
		headerMap.add("Content-Type","application/x-www-form-urlencoded");*/
		logger.debug(requestId+"|Request Header :"+headerMap);

		
		try {
			encData = "Data="+URLEncoder.encode(encData,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String responseJson = HttpsClient.send(url, encData, headerMap);
		//String responseJson = RestTemplateUtil.sendPostRequest(url, encData, headerMap);
		logger.debug(requestId+"|Insight response:"+responseJson);
		resp = (MerchantOnboardResponse) JsonUtility.parseJson(responseJson, MerchantOnboardResponse.class);
		return resp;
	}

	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void saveInDB(MerchantOnboard merchantDetails, MerchantOnboardResponseData dataObj)
	{
		long merchPhone = merchantDetails.getMerMobileNumber();
		String bankCode = merchantDetails.getBankCode();
		String tid = dataObj.getTid();
		details.saveDetail(merchantDetails, dataObj);
		details.saveContactDetail(merchantDetails, dataObj);
		details.saveMobileDetail(merchantDetails, dataObj);
		details.saveMerchantGroup(merchantDetails, dataObj);
		details.saveMvisa(merchantDetails, dataObj);
		
		
		if(!details.checkIfPhoneNumberAlreadyPresentInLoginTable(merchPhone,bankCode))
		{
			String noSmsBankcodes = ApplicationConfig.get("noSmsBankcodes");
			List<String> bankCodeList = Arrays.asList(noSmsBankcodes.split(","));
			String	password = null;
			if(bankCodeList.contains(bankCode))
			{
				password = details.createLogInInfo(merchPhone, tid, bankCode);  // BankCode New Parameter added //populating login credentials in the database
			}
			else
			{
				password = details.createLogInInfo(merchPhone, null, bankCode);  
				/////Send login Details through SMS
				//sendLoginDetails(merchPhone,password,getBankCode()); // BankCode Parameter added 
			}
			password = null;
			logger.debug("New Merchant added. SMS sent");
		}
		else
		{
			logger.debug("Merchant alredy present, Therefore No SMS sent");
		}
		
		}

	}



