package com.wl.upi.service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.wl.upi.dao.BharatQRTransactionDao;
import com.wl.upi.dao.MerchantDAO;
import com.wl.upi.dao.SettlementDAO;
import com.wl.upi.dao.SettlementDAOImpl;
import com.wl.upi.dao.UPITransactionDAO;
import com.wl.upi.model.BharatQRTransDTO;
import com.wl.upi.model.MMSTransDTO;
import com.wl.upi.model.MerchantDetail;
import com.wl.upi.model.MrlDTO;
import com.wl.upi.model.RefundRequest;
import com.wl.upi.model.TtmsTransactiosDto;
import com.wl.upi.model.TxnDTO;
import com.wl.util.EncryptionCache;
import com.wl.util.HelperUtil;
import com.wl.util.JsonUtility;
import com.wl.util.TcpClient;
import com.wl.util.config.AggregatorConfig;
import com.wl.util.config.BankConfig;
import com.wl.util.constants.Constants;
import com.wl.util.constants.ErrorMessages;
import com.wl.util.exceptions.ApplicationException;
import com.wl.util.model.AggregatorDetails;
import com.wl.util.model.Response;

//import oracle.net.resolver.TNSNamesNamingAdapter;
@Service
public class BharatQRServiceImpl implements BharatQRService{

	private static Logger logger = LoggerFactory.getLogger(BharatQRServiceImpl.class);

	@Autowired
	private BharatQRTransactionDao bharatQRTransactionDao;

	@Autowired
	@Qualifier("upiTransactionDAO")
	private  UPITransactionDAO upiTransactionDao;

	@Autowired
	@Qualifier("upiServiceFactory")
	private UPIServiceFactory upiServiceFactory;

	@Autowired
	private MerchantDAO merchantDao;

	@Autowired
	@Qualifier("notificationService")
	private NotificationService notificationService;

	@Autowired
	@Qualifier("aggregatorService")
	private AggregatorService aggregatorService;

	@Autowired
	@Qualifier("settlementService")
	private SettlementService settlementService;

	
	@Autowired
	private SettlementDAO settlementDao;

	@Override
	public Response bhartQRRefund(String fromEntity, String bankCode, RefundRequest request) {

		TxnDTO transDTO=bharatQRTransactionDao.checkBharatQRTrans(request.getRrn(),request.getAuthCode(),request.getTid());
		
		if(transDTO==null)
			throw new ApplicationException(ErrorMessages.ORG_TXN_NOT_FOUND.toString());
			
		BigDecimal bd = new BigDecimal(transDTO.getRefundedAmount());
		logger.info("Refund Amount till date:"+bd.toPlainString());
		bd = bd.add(new BigDecimal(request.getRefundAmount()));
		logger.info("Refund Amount:"+request.getRefundAmount());
		double  total_refund = bd.doubleValue();
		logger.info("Total Amount:"+total_refund);
		double amt = Double.parseDouble(transDTO.getTxnAmount());
		logger.info("Transaction Amount:"+amt);
		transDTO.setRefundedAmount(new BigDecimal(request.getRefundAmount()).doubleValue());
		logger.info("REfundAmount :" +transDTO.getRefundedAmount());
		if(bankCode.equals("00031")){ 
			// RefundId check is only foe Axis bank BharatQr 
			logger.info("RefundId For BharatQr transaction in Request : " + request.getRefundId());
			if(request.getRefundId().length() < 4){
				logger.debug(ErrorMessages.REFUNDID_LENGTH.toString());
				throw new ApplicationException(ErrorMessages.REFUNDID_LENGTH.toString());
			}
			boolean refExistFlag=bharatQRTransactionDao.getRefundId(request.getRrn(),request.getAuthCode(),request.getTid(),request.getRefundId());
			if(refExistFlag){
				throw new ApplicationException(ErrorMessages.REFUND_ALREADY.toString());
			}
			
		}
	
		if(amt < 0.0)
			throw new ApplicationException(ErrorMessages.INVALID_AMOUNT.toString());
		if(total_refund > amt)
			throw new ApplicationException(ErrorMessages.REFUND_EXCEEDED.toString());
		Response resp = new Response();
		try{
			BharatQRTransDTO txnRequest = new BharatQRTransDTO();
			createBharatQRRefundModel(transDTO, txnRequest,request);
			
			String magnusReqStr = JsonUtility.convertToJson(txnRequest,true);
			logger.info("JSON Request sending to magnus:"+magnusReqStr);
			if(magnusReqStr != null && ! magnusReqStr.isEmpty()){
				
				 //Commented Magnus connectivity for refund 
				
				/*String magnusResponse=TcpClient.send(magnusReqStr);
				logger.info("JSON Response sending to magnus:"+magnusResponse);
				JSONObject response = new JSONObject(magnusResponse);
				transDTO.setStatus(response.getString("status"));
				transDTO.setErrorCode(response.optString("error_code"));
				transDTO.setRespRefNo(response.optString("resp_ref_no"));*/
				
				// comment setStatus code for magnus connectivity 
				transDTO.setStatus("S");
				
				String txnId = "";
				transDTO.setNewRRn(txnRequest.getNewRRN());
				if(transDTO.getPrimaryId()!=null && transDTO.getPrimaryId().length() >=18)
					txnId = transDTO.getPrimaryId();
				else 	if(transDTO.getSecondaryId()!=null && transDTO.getSecondaryId().length() >=18)
					txnId = transDTO.getSecondaryId();
				else
					txnId = "NA";

				bharatQRTransactionDao.saveBharatQRRefundTxn(
						new Object[]{
								transDTO.getId(),
								transDTO.getMpan(),
								transDTO.getTxnDate(),
								transDTO.getCurrency(),
								//transDTO.getRefundedAmount(),
								request.getRefundAmount(),
								transDTO.getTxnAmount(),
								transDTO.getCustName(),
								transDTO.getCustPan(),
								transDTO.getStatus(),
								transDTO.getAuthCode(),
								transDTO.getRrn(),
								transDTO.getPrimaryId(),
								transDTO.getSecondaryId(),
								transDTO.getTid(),
								transDTO.getBankCode(),
								request.getRefundId() != null ? request.getRefundId() : "",
								transDTO.getNewRRn()	
								});

				if("S".equals(transDTO.getStatus()))
				{
					logger.info("Refund Success. Updating refund amount in transaction table :"+total_refund);
					bharatQRTransactionDao.updateRefundedAmount(transDTO.getId(), total_refund);
					resp.setStatus(Constants.SUCCESS.name());
					Map<String, String> map = new LinkedHashMap<>(4);
					map.put("rrn", transDTO.getRrn());
					map.put("refundAmount", request.getRefundAmount());
					map.put("txnAmount",transDTO.getTxnAmount());
					map.put("TID",transDTO.getTid());
					map.put("refundRRN",transDTO.getNewRRn());
					if(bankCode.equals("00031")){
						/*RefundId which we are getting in refund request the same refundId should 
						go in response only for Axis bank*/ 
						map.put("refundId",request.getRefundId() != null ? request.getRefundId() : "");
					}
					resp.setResponseObject(map);
					
					//uncomment this code for new refund api when you will not call magnus api.
					logger.info(" Outside the settelment ");
					if (fromEntity.equalsIgnoreCase("mrl") || fromEntity.equalsIgnoreCase("mms")) {
						int count=0;
						logger.info("Inserting MRL refund in MMS DB ");
						String batchNumber = settlementDao.getMRLBatchNumber();
						logger.info("MRL Batch Number : " + batchNumber);
						MrlDTO mrl = upiTransactionDao.getDataForMrlBQR(transDTO.getRrn(), transDTO.getMpan(), 9,
								batchNumber, transDTO.getCurrency());
						mrl.setMessageType("230");
						mrl.setProcessingCode("200000");
						mrl.setRefundRrn(transDTO.getNewRRn());
						int insert_count = upiTransactionDao.sendDataToMRLDB(mrl);
						if (insert_count == 1) {
							logger.info("MMS refund inserted : " + count + " for RRN " + transDTO.getRrn());
							
							//count = bharatQRTransactionDao.updateBQRMMSFlag(transDTO.getRrn(), "Y");
							//logger.info("MMS Flag update Y : " + count + " for RRN " + transDTO.getRrn());
						} else {
							logger.info("MMS refund not inserted : " + count + " for RRN " + transDTO.getRrn());
							//count = bharatQRTransactionDao.updateBQRMMSFlag(transDTO.getRrn(), "N");
							//logger.info("Else MMS Flag update N : " + count + " for RRN " + transDTO.getRrn());
						}
					}
//					TCH Commented by velkumar
					/*else {
						settlementService.settleRefundTxn(transDTO, request);
					}*/
					
					
				}
				else
				{
					resp.setStatus(Constants.FAILED.name());
					resp.setMessage(ErrorMessages.valueOf(transDTO.getErrorCode()).toString());
				}
				
				logger.debug("bhartQRRefund(String fromEntity, String bankCode, String jsonRequest) method execution ends :");	
			}
		}
		catch(DataAccessException e)
		{
			resp.setStatus(Constants.FAILED.name());
			resp.setMessage(ErrorMessages.SERVER_ERROR.toString());
			logger.error("Exception in refund :",e);
		}
		catch(Exception e)
		{
			resp.setStatus(Constants.FAILED.name());
			resp.setMessage(ErrorMessages.SERVER_ERROR.toString());
			logger.error("Exception in refund :",e);
		}
		logger.info("Response returned for refund is :"+resp);
		return resp;
	}

	@Override
	public Response refund(String fromEntity, String bankCode, String jsonRequest) {
		// TODO Auto-generated method stub
		logger.info("Refund execution starts json is :"+jsonRequest);
		logger.info("Refund execution starts fromEntity is :"+fromEntity);
		logger.info("Refund execution starts bankCode is :"+bankCode);
		RefundRequest refRequest = (RefundRequest) JsonUtility.parseJson(jsonRequest, RefundRequest.class) ;
		if(refRequest==null)
			throw new ApplicationException(ErrorMessages.JSON_FORMAT_ERROR.toString());

		MerchantDetail md = merchantDao.getMerchantDetails(null, refRequest.getTid());
		if(md==null)
			throw new ApplicationException(ErrorMessages.MERCHANT_MAPPING_NOT_FOUND.toString());

		logger.info(refRequest.getTid()+"|Refund allowed for tid:"+md.isRefundAllowed());
		if(!md.isRefundAllowed())
			throw new ApplicationException(ErrorMessages.REFUND_UNSUPPORTED.toString());
		if("1".equals(refRequest.getTxnType()))
		{	
			return bhartQRRefund(fromEntity, bankCode, refRequest);
		}
		else if("2".equals(refRequest.getTxnType()))
		{
			UPIBankService upiBankService =  upiServiceFactory.getUPIService(bankCode);
			return  upiBankService.upiRefund(fromEntity, bankCode , refRequest);
		}
		else
			throw new ApplicationException("Invalid Transaction Type");
	}

	@Override
	public Map<String, String> populateBharatQRTxn(String jsonRequest) {

		BharatQRTransDTO bharatqrReq = (BharatQRTransDTO) JsonUtility.parseJson(jsonRequest, BharatQRTransDTO.class) ;
		if(bharatqrReq==null)
			throw new ApplicationException(ErrorMessages.JSON_FORMAT_ERROR.toString());
		String mpan = bharatqrReq.getMpan();
		if(mpan.startsWith("5")){
			mpan = mpan.substring(0,mpan.length() -1);
		}
		MerchantDetail md =  merchantDao.getMerchantDetails(mpan, null);
		if(md==null)
			throw new ApplicationException(ErrorMessages.MERCHANT_MAPPING_NOT_FOUND.toString());
		
		//check if transaction present
		boolean present = upiTransactionDao.checkBharatQRTxnExists(bharatqrReq.getRefNo(), bharatqrReq.getAuthCode());
		if(present){
			logger.info("txn already present for : " + bharatqrReq.getRefNo());
			throw new ApplicationException(ErrorMessages.DUPLICATE_TXN.toString());
		}
		
		logger.info("tid:"+md.getTid());
		logger.info("mid:"+md.getMerchantId());
		logger.info("Program Type:"+md.getProgramType());
		bharatqrReq.setTid(md.getTid());
		bharatqrReq.setMerchantId(md.getMerchantId());
		
		if(bharatqrReq.getSecondaryId()== null){
			logger.info("inside secondary id is null for Bharat QR Service");
			if(bharatqrReq.getAdditionalData()!=null)
			{
				Map<String,String> map =  parseAdditionalField(bharatqrReq.getPrimaryId(),bharatqrReq.getAdditionalData());

				String pId = map.get("primary_id");
				String sId = map.get("secondary_id");
				if(pId!=null)
				{
					logger.debug("setting primary id from additional data:"+pId);
					bharatqrReq.setPrimaryId(pId);
				}
				if(sId!=null)
				{
					logger.debug("setting secondary id from additional data:"+sId);
					bharatqrReq.setSecondaryId(sId);
				}
			}
		}
		
		try {
			bharatQRTransactionDao.saveBharatQRTxn(bharatqrReq);
			//String batchNumber =bharatqrReq.getTimeStamp().substring(8);
			String batchNumber= settlementDao.getMRLBatchNumber();
			System.out.println("Batch Number : " + batchNumber);
			if(md.getProgramType()==7 || md.getProgramType() ==9 || md.getProgramType() ==13){
				MrlDTO mrl = upiTransactionDao.getDataForMrlBQR(bharatqrReq.getRefNo(),mpan,md.getProgramType(),batchNumber,bharatqrReq.getCurrency());
				mrl.setCardNumber(bharatqrReq.getCustPan());
				int count=0;
				try{
					int insert_count=upiTransactionDao.sendDataToMRLDB(mrl);
					if(insert_count==1){
						count =bharatQRTransactionDao.updateBQRMMSFlag(bharatqrReq.getRefNo(), "Y");
						logger.info("MMS Flag update Y : " + count + " for RRN "+bharatqrReq.getRefNo());
					}else{
						count =bharatQRTransactionDao.updateBQRMMSFlag(bharatqrReq.getRefNo(), "N");
						logger.info("Else MMS Flag update N : " + count + " for RRN "+bharatqrReq.getRefNo());
					}
				}catch(Exception e){
					count =bharatQRTransactionDao.updateBQRMMSFlag(bharatqrReq.getRefNo(), "N");
					logger.info(" Exceptio nMMS Flag update N : " + count + " for RRN "+bharatqrReq.getRefNo());
					logger.error(e.getMessage());
				}
				
				logger.info("BQR data inserted successfully in MRL DB");
				
			}
		} catch (Exception e) {
			logger.error("Exception in populateBharatQRTxn",e);
			throw new ApplicationException(ErrorMessages.SERVER_ERROR.toString());
		}
		Map<String, String> responseMap = new LinkedHashMap<>();
		responseMap.put("id", Integer.toString(bharatqrReq.getId()));
		responseMap.put("mid", bharatqrReq.getMerchantId());
		responseMap.put("bankCode", bharatqrReq.getBankCode());
		responseMap.put("programType", Integer.toString(md.getProgramType()));
		responseMap.put("status", Constants.SUCCESS.name());
		responseMap.put("rrn", bharatqrReq.getRefNo());
		responseMap.put("message",Constants.SUCCESS.name());
		return responseMap;
	}

	private void createBharatQRRefundModel(TxnDTO from, BharatQRTransDTO to, RefundRequest request) throws Exception
	{
		try{
			String newRRn = HelperUtil.getRRN();
			logger.debug(" insdie the bharatQr refund module ");
			
			BigDecimal refbd = new BigDecimal(request.getRefundAmount()).movePointRight(2);
			BigDecimal setbd = new BigDecimal(from.getSettlementAmount()).movePointRight(2);
			logger.debug(" refund amount set :"+refbd.doubleValue());
			to.setMti("0220");
			to.setRequestType("refund");
			to.setMpan(from.getMpan());
			to.setCustPan(EncryptionCache.getEncryptionUtility("APP").decrypt(from.getCustPan()));
			to.setCustName(from.getCustName());
			to.setCurrency(from.getCurrency());
			to.setAmount(refbd.toPlainString());
			from.setRefundedAmount(refbd.doubleValue());
			to.setAuthCode(from.getAuthCode());
			to.setRefNo(from.getRrn());
			to.setPrimaryId(from.getPrimaryId());
			to.setSecondaryId(from.getSecondaryId());
			to.setSettlementAmt(setbd.toPlainString());
			to.setTransactionType(from.getTransactionType());
			to.setBankCode(from.getBankCode());
			to.setTimeStamp(HelperUtil.toString(from.getTxnDate(),"yyyyMMddHHmmss"));
			to.setNewRRN(newRRn);
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
	}

	/**
	 * parse additional field data from json object received from Magnus and set it into linkedhashmap
	 * @return linkedhashmap converted key/value pair after parsing.
	 */
	private LinkedHashMap<String,String> parseAdditionalField(String primary_id, String additional_data) {

		String bill_number,reference_no,mobile_number,store_id,loyalty_number,consumer_id,terminal_id,purpose;
		LinkedHashMap<String,String> add_data=new LinkedHashMap<String,String>(2);
		try
		{
			String[] fields=additional_data.split("\\~");
			for(int i=0;i<fields.length;i++)
			{
				if(fields[i]!=null && !fields[i].trim().isEmpty())
				{	
					switch (i) {
					case 0:
						bill_number=fields[0];
						logger.debug("setting field 0 :"+bill_number);
						add_data.put("secondary_id",bill_number);
						break;
					case 1:
						mobile_number=fields[1];
						logger.debug("setting field 1 :"+mobile_number);
						add_data.put("secondary_id",mobile_number);
						break;
					case 2:
						store_id=fields[2];
						logger.debug("setting field 2 :"+store_id);
						add_data.put("secondary_id",store_id);
						break;
					case 3:
						loyalty_number=fields[3];
						logger.debug("setting field 3 :"+loyalty_number);
						add_data.put("secondary_id",loyalty_number);
						break;
					case 4:
						reference_no=fields[4];
						logger.debug("setting field 4 :"+reference_no);
						if(!reference_no.equals(primary_id))
							add_data.put("primary_id",reference_no);
						else
							add_data.put("primary_id",primary_id);
						break;
					case 5:
						consumer_id=fields[5];
						//if(!consumer_id.equals("") && consumer_id.length() > 0 && consumer_id != null){
							logger.debug("setting field 5 :"+consumer_id);
							add_data.put("secondary_id",consumer_id);
						//}
						break;
					case 6:
						terminal_id=fields[6];
						logger.debug("setting field 6 :"+terminal_id);
						add_data.put("terminal_id",terminal_id);
						break;
					case 7:
						purpose=fields[7];
						logger.debug("setting field 7 :"+purpose);
						add_data.put("purpose",purpose);
						break;
					default:
						break;
					}
				}
			}
		}
		catch(Exception e)
		{
			logger.error("Exception in parsing additional_data:",e);
		}

		return add_data;
	}

	@Override
	public void notifyForBharatQRTxn(Map<String, String> resp) {

		String mid = resp.get("mid");
		String id = resp.get("id");
		int programTypeStr = Integer.parseInt(resp.get("programType"));
		logger.debug("Checking if merchant is aggregator merchant for mid :"+mid);
		AggregatorDetails aggDetail  = AggregatorConfig.get(mid);
		TxnDTO txnDetails  = bharatQRTransactionDao.checkBharatQRTrans(null,Integer.valueOf(id));
		
		if (programTypeStr == 9 || programTypeStr == 11) {
			logger.info("Program type for Comming request : " + programTypeStr);
			logger.info("notifyForBharatQRTxn() Transaction Type : " + txnDetails.getTransactionType());
			boolean flag=aggregatorService.sendBQRMrlNotification(txnDetails,programTypeStr);
			if(flag==true){
				int count =bharatQRTransactionDao.updateBQRMRLFlag(txnDetails.getRrn(), "Y");
				logger.info(" MMRL notification flag updated to Y for  RRN : " + txnDetails.getRrn() +" count :" +count );
			}else{
				int count =bharatQRTransactionDao.updateBQRMRLFlag(txnDetails.getRrn(), "N");
				logger.info(" MMRL notification flag updated to N for  RRN : " + txnDetails.getRrn() +" count :" +count );
			}
		}
		
		if(txnDetails!=null && !"00031".equals(txnDetails.getBankCode()) && resp!=null && aggDetail==null)
		{
			logger.info("This request is for : " + txnDetails.getBankCode() + " and  program type : " + programTypeStr);
			//txnDetails.setProgramType(programTypeStr);
			//send MRL notification only if program type is 9
			logger.debug(mid+"|Not an aggregator merchant");
			txnDetails  =  notificationService.sendBharatQRPushNotification(resp,txnDetails);			
		}
		
		if(txnDetails!=null && (("00031".equals(txnDetails.getBankCode()) && aggDetail!=null) || aggDetail!=null))
		{
			logger.debug(mid+"|It is aggregator merchant");
			//added primary id of transaction
			//Sending programType separatly bcz the value of getProgramType() in txnDetails is Transaction Type (for program Type 7 and 9)
			aggregatorService.sendNotification(txnDetails,aggDetail,programTypeStr);
		}

	}

	@Override
	public void notifyForUpiTxn(Map<String, String> resp) {
		// TODO Auto-generated method stub
		String trId = resp.get("trId");
		String bankCode = resp.get("bankCode");
		String mid = resp.get("mid");
		String rrn=resp.get("rrn");
		logger.debug("Checking if merchant is aggregator merchant for mid :"+mid);
		AggregatorDetails aggDetail  =null;
		if(mid != null){
			aggDetail = AggregatorConfig.get(mid);
			logger.info(" aggDetail : " + aggDetail);
		 }
		TxnDTO txnDetails=null;
		try {
			/*if("00079".equals(bankCode) || "00031".equals(bankCode) || "00011".equals(bankCode) || "00051".equals(bankCode)){
				logger.debug( bankCode +" : txn found, searching txn by VPA");
				txnDetails = upiTransactionDao.getUpiTxnDetails(trId, true);
			}else{
				txnDetails = upiTransactionDao.getUpiTxnDetails(trId, false);
			}*/
			
			// Enabling code for all the banks first it will check transaction by trId follows by VPA
			txnDetails = upiTransactionDao.getUpiTxnDetails(trId, false,rrn);
			if(txnDetails == null){
				logger.debug( bankCode +" : txn not found using trid with QR history, searching txn by RRN & tr_id");
				txnDetails = upiTransactionDao.getUpiTxnDetails(trId, true,rrn);
			}
			if(txnDetails == null)
				throw new ApplicationException(ErrorMessages.JSON_FORMAT_ERROR.toString());
			
		} catch (Exception e1) {
			logger.error(trId+"|Transaction details not found",e1);
		}
		
		//int batchNumber = Integer.parseInt(HelperUtil.toString(txnDetails.getTxnDate(), "MMddHH"));
		//String batchNumber = HelperUtil.toString(txnDetails.getTxnDate(), "MMddHH");
			String batchNumber= settlementDao.getMRLBatchNumber();
			
//			added by velkumar
//			begin
			String cardNo="";
			if(bankCode.length()==5&&rrn.length()==12){
				StringBuffer sBuffer=new StringBuffer(rrn);
				sBuffer.reverse();
				cardNo="0".concat(bankCode).concat(sBuffer.substring(sBuffer.length()-10));
				
			}
//			end
		
		if (txnDetails != null && trId != null && !trId.isEmpty() && bankCode != null && !bankCode.isEmpty()
				&& aggDetail == null && !"00031".equals(txnDetails.getBankCode())) {
			logger.debug(mid + "|Not an aggregator merchant");
			txnDetails = notificationService.sendUPIPushNotification(bankCode, trId, txnDetails);
			

			if (txnDetails.getProgramType()== 7 || txnDetails.getProgramType()== 9 || txnDetails.getProgramType()== 13) {
				logger.debug(" fetching details for  MRL 1 ");
				if ("00".equals(txnDetails.getResponseCode()) || "000".equals(txnDetails.getResponseCode())) {
					MrlDTO mrl = upiTransactionDao.getDataForMRLUPI(txnDetails.getRrn(),txnDetails.getProgramType(),batchNumber);
					try{
//						added by velkumar
//						begin
						if(cardNo.length()==16)
						mrl.setCardNumber(cardNo);
//						end
						int i= upiTransactionDao.sendDataToMRLDB(mrl);	
						logger.info(" data inserted in MMS DB --> " + i + "RRN : " + txnDetails.getRrn());
						if(i==0){
							int count = upiTransactionDao.updateMrlSettlementFlag(txnDetails.getRrn());
							logger.info(count + " IF statement Transaction not inserted in MRL DB due to some exception while inserting settlement flag is N: " + txnDetails.getRrn() );
						}
					}catch(Exception e){
						int count = upiTransactionDao.updateMrlSettlementFlag(txnDetails.getRrn());
						logger.info(count + " Transaction not inserted in MRL DB due to some exception while inserting settlement flag is N: " + txnDetails.getRrn() );
						logger.error("RRN : txnDetails.getRrn() --> " + e.getMessage());
					}
					
					//send MRL notification only if program type is 9
					if (txnDetails.getProgramType()== 9) {
						boolean flag = aggregatorService.sendBQRMrlNotification(txnDetails,txnDetails.getProgramType());
						if(flag==true){
							int count =bharatQRTransactionDao.updateUPIMRLFlag(txnDetails.getRrn(), "Y");
							logger.info(" MMRL notification flag updated to Y for  RRN : " + txnDetails.getRrn() +" count :" +count );
						}else{
							int count =bharatQRTransactionDao.updateUPIMRLFlag(txnDetails.getRrn(), "N");
							logger.info(" MMRL notification flag updated to N for  RRN : " + txnDetails.getRrn() +" count :" +count );
						}
					}
				} else {
					logger.debug("Cannot insert into Mrl Db as Response Code is not equal to 00 :::");
				}
			}else if (txnDetails.getProgramType()== 11) {
				boolean flag = aggregatorService.sendBQRMrlNotification(txnDetails,txnDetails.getProgramType());
				if(flag==true){
					int count =bharatQRTransactionDao.updateUPIMRLFlag(txnDetails.getRrn(), "Y");
					logger.info(" MMRL notification flag updated to Y for  RRN : " + txnDetails.getRrn() +" count :" +count );
				}else{
					int count =bharatQRTransactionDao.updateUPIMRLFlag(txnDetails.getRrn(), "N");
					logger.info(" MMRL notification flag updated to N for  RRN : " + txnDetails.getRrn() +" count :" +count );
				}
			}
			
		}
		//9 has been  used for sending notification to MRL which follows the same notification as of axis bank
		if (txnDetails != null && ("00031".equals(txnDetails.getBankCode()) && aggDetail!=null) || aggDetail!=null ) {
			logger.debug(mid + "|It is aggregator merchant");
			//Sending programType separatly bcz the same method has been called in BharatQr notification (for program Type 7 and 9)
			aggregatorService.sendNotification(txnDetails,aggDetail,txnDetails.getProgramType());
			

			if (txnDetails.getProgramType()== 7 || txnDetails.getProgramType()== 9 || txnDetails.getProgramType()== 13) {
				logger.debug(" fetching details for  MRL 2");
				if ("00".equals(txnDetails.getResponseCode()) || "000".equals(txnDetails.getResponseCode())) {
					MrlDTO mrl = upiTransactionDao.getDataForMRLUPI(txnDetails.getRrn(),txnDetails.getProgramType(),batchNumber);
					try{
//						added by velkumar
//						begin
						if(cardNo.length()==16)
						mrl.setCardNumber(cardNo);
//						end
						
						int i= upiTransactionDao.sendDataToMRLDB(mrl);	
						logger.info(" data inserted in MRL DB --> " + i + "RRN : " + txnDetails.getRrn());
					}catch(Exception e){
						int count = upiTransactionDao.updateMrlSettlementFlag(txnDetails.getRrn());
						logger.info(count + " Transaction not inserted in MRL DB due to some exception while inserting : " + txnDetails.getRrn() );
					}
					//send MRL notification only if program type is 9
					if (txnDetails.getProgramType()== 9) {
						boolean flag = aggregatorService.sendBQRMrlNotification(txnDetails,txnDetails.getProgramType());
						if(flag==true){
							int count =bharatQRTransactionDao.updateUPIMRLFlag(txnDetails.getRrn(), "Y");
							logger.info(" MMRL notification flag updated to Y for  RRN : " + txnDetails.getRrn() +" count :" +count );
						}else{
							int count =bharatQRTransactionDao.updateUPIMRLFlag(txnDetails.getRrn(), "N");
							logger.info(" MMRL notification flag updated to N for  RRN : " + txnDetails.getRrn() +" count :" +count );
						}
					}
				} else {
					logger.debug("Cannot insert into Mrl Db as Response Code is not equal to 00 :::");
				}
			}else if (txnDetails.getProgramType()== 11) {
				boolean flag = aggregatorService.sendBQRMrlNotification(txnDetails,txnDetails.getProgramType());
				if(flag==true){
					int count =bharatQRTransactionDao.updateUPIMRLFlag(txnDetails.getRrn(), "Y");
					logger.info(" MMRL notification flag updated to Y for  RRN : " + txnDetails.getRrn() +" count :" +count );
				}else{
					int count =bharatQRTransactionDao.updateUPIMRLFlag(txnDetails.getRrn(), "N");
					logger.info(" MMRL notification flag updated to N for  RRN : " + txnDetails.getRrn() +" count :" +count );
				}
			}
			
			
		}
		/*else if(txnDetails != null && bankCode != null && !bankCode.isEmpty()
				&& aggDetail == null && "00031".equals(txnDetails.getBankCode())){
			logger.info("This is direct merchant for Axis Bank");
			txnDetails = notificationService.sendUPIPushNotification(bankCode, trId, txnDetails);
		}
		
		
		
		/**@pranav
		 * IF --> If transaction Details and aggregtor details is not null and 
		 * 			(other than IPG all agregators are allowed for settlement)
		 * ELSE IF--> This is for normal merchant which is not aggregator merchant 
		 * 			will enter in the else if statement   
		 * */
		
		if(txnDetails.getProgramType() != 7 && txnDetails.getProgramType()!= 9
				&& txnDetails.getProgramType() != 11 && txnDetails.getProgramType() != 13){
					if (txnDetails != null && aggDetail != null && !"IPG".equalsIgnoreCase(aggDetail.getAggrName())
							&& !"tch".equalsIgnoreCase(txnDetails.getFromEntity())) {
						
						logger.info(" Aggregator is not null block  and  program type|" + txnDetails.getProgramType() );
						settlementService.settleTxn(txnDetails);
					} else if (txnDetails != null && aggDetail == null && !"tch".equalsIgnoreCase(txnDetails.getFromEntity())) {
						logger.info(" Aggregator is null block  and  program type|" + txnDetails.getProgramType() );
						settlementService.settleTxn(txnDetails);
						notificationService.sendUPIPushNotification(bankCode, trId, txnDetails);
					}
				}
		
		
		
	}

	@Override
	public Map<String, String> populateTTMS(String jsonRequest) {


		MMSTransDTO bharatqrReq = (MMSTransDTO) JsonUtility.parseJson(jsonRequest, MMSTransDTO.class) ;
		if(bharatqrReq==null)
			throw new ApplicationException(ErrorMessages.JSON_FORMAT_ERROR.toString());
		String mpan = bharatqrReq.getMpan();
		if(mpan.startsWith("5")){
			mpan = mpan.substring(0,mpan.length() -1);
		}
		MerchantDetail md =  merchantDao.getMerchantDetails(mpan, null);
		if(md==null)
			throw new ApplicationException(ErrorMessages.MERCHANT_MAPPING_NOT_FOUND.toString());
		
		//check if transaction present
		boolean present = upiTransactionDao.checkBharatQRTxnExists(bharatqrReq.getRefNo(), bharatqrReq.getAuthCode());
		if(present){
			logger.info("txn already present for : " + bharatqrReq.getRefNo());
			throw new ApplicationException(ErrorMessages.DUPLICATE_TXN.toString());
		}
		
		logger.info("tid:"+md.getTid());
		logger.info("mid:"+md.getMerchantId());
		logger.info("Program Type:"+md.getProgramType());
		bharatqrReq.setTid(md.getTid());
		bharatqrReq.setMerchantId(md.getMerchantId());
		
		if(bharatqrReq.getSecondaryId()== null){
			logger.info("inside secondary id is null for Bharat QR Service");
			if(bharatqrReq.getAdditionalData()!=null)
			{
				Map<String,String> map =  parseAdditionalField(bharatqrReq.getPrimaryId(),bharatqrReq.getAdditionalData());

				String pId = map.get("primary_id");
				String sId = map.get("secondary_id");
				if(pId!=null)
				{
					logger.debug("setting primary id from additional data:"+pId);
					bharatqrReq.setPrimaryId(pId);
				}
				if(sId!=null)
				{
					logger.debug("setting secondary id from additional data:"+sId);
					bharatqrReq.setSecondaryId(sId);
				}
			}
		}
		
		try {
			bharatQRTransactionDao.saveMMSTxn(bharatqrReq);
			//String batchNumber =bharatqrReq.getTimeStamp().substring(8);
			String batchNumber= bharatqrReq.getBatchNumber();
			System.out.println("Batch Number : " + batchNumber);
			if(md.getProgramType()==7 || md.getProgramType() ==9 || md.getProgramType() ==13){
				MrlDTO mrl = upiTransactionDao.getDataForMrlBQR(bharatqrReq.getRefNo(),mpan,md.getProgramType(),batchNumber,bharatqrReq.getCurrency());
				mrl.setCardNumber(bharatqrReq.getCustPan());
				int count=0;
				try{
					int insert_count=upiTransactionDao.sendDataToMRLDB(mrl);
					if(insert_count==1){
						count =bharatQRTransactionDao.updateBQRMMSFlag(bharatqrReq.getRefNo(), "Y");
						logger.info("MMS Flag update Y : " + count + " for RRN "+bharatqrReq.getRefNo());
					}else{
						count =bharatQRTransactionDao.updateBQRMMSFlag(bharatqrReq.getRefNo(), "N");
						logger.info("Else MMS Flag update N : " + count + " for RRN "+bharatqrReq.getRefNo());
					}
				}catch(Exception e){
					count =bharatQRTransactionDao.updateBQRMMSFlag(bharatqrReq.getRefNo(), "N");
					logger.info(" Exceptio nMMS Flag update N : " + count + " for RRN "+bharatqrReq.getRefNo());
					logger.error(e.getMessage());
				}
				
				logger.info("MMS data inserted successfully in MMS DB");
				
			}
		} catch (Exception e) {
			logger.error("Exception in populateMMSTxn",e);
			throw new ApplicationException(ErrorMessages.SERVER_ERROR.toString());
		}
		Map<String, String> responseMap = new LinkedHashMap<>();
		responseMap.put("id", Integer.toString(bharatqrReq.getId()));
		responseMap.put("mid", bharatqrReq.getMerchantId());
		responseMap.put("bankCode", bharatqrReq.getBankCode());
		responseMap.put("programType", Integer.toString(md.getProgramType()));
		responseMap.put("status", Constants.SUCCESS.name());
		responseMap.put("rrn", bharatqrReq.getRefNo());
		responseMap.put("message",Constants.SUCCESS.name());
		return responseMap;
	
	}

	@Override
	public void notifyForMMSTxn(Map<String, String> resp) {

		String mid = resp.get("mid");
		String id = resp.get("id");
		int programTypeStr = Integer.parseInt(resp.get("programType"));
		logger.debug("Checking if MMS merchant is aggregator merchant for mid :"+mid);
		AggregatorDetails aggDetail  = AggregatorConfig.get(mid);
		TxnDTO txnDetails  = bharatQRTransactionDao.checkBharatQRTrans(null,Integer.valueOf(id));
		
		/**we dont have to send the transaction to MRL as the transaction is coming from MRL to mobile server 
		 * */
		
		/*if (programTypeStr == 9 || programTypeStr == 11) {
			logger.info("Program type for Comming request : " + programTypeStr);
			logger.info("notifyForBharatQRTxn() Transaction Type : " + txnDetails.getTransactionType());
			boolean flag=aggregatorService.sendBQRMrlNotification(txnDetails,programTypeStr);
			if(flag==true){
				int count =bharatQRTransactionDao.updateBQRMRLFlag(txnDetails.getRrn(), "Y");
				logger.info(" MMRL notification flag updated to Y for  RRN : " + txnDetails.getRrn() +" count :" +count );
			}else{
				int count =bharatQRTransactionDao.updateBQRMRLFlag(txnDetails.getRrn(), "N");
				logger.info(" MMRL notification flag updated to N for  RRN : " + txnDetails.getRrn() +" count :" +count );
			}
		}*/
		
		if(txnDetails!=null && !"00031".equals(txnDetails.getBankCode()) && resp!=null && aggDetail==null)
		{
			logger.info("This request is for : " + txnDetails.getBankCode() + " and  program type : " + programTypeStr);
			//txnDetails.setProgramType(programTypeStr);
			//send MRL notification only if program type is 9
			logger.debug(mid+"|Not an aggregator merchant");
			txnDetails  =  notificationService.sendBharatQRPushNotification(resp,txnDetails);			
		}
		
		if(txnDetails!=null && (("00031".equals(txnDetails.getBankCode()) && aggDetail!=null) || aggDetail!=null))
		{
			logger.debug(mid+"|It is aggregator merchant");
			//added primary id of transaction
			//Sending programType separatly bcz the value of getProgramType() in txnDetails is Transaction Type (for program Type 7 and 9)
			aggregatorService.sendNotification(txnDetails,aggDetail,programTypeStr);
		}

	}

	@Override
	public Map<String, String> populateTTMSRequestBOdy(MMSTransDTO bharatqrReq) {

		logger.info("Inside the populateTTMSRequestBOdy method | " + bharatqrReq.getRefNo());
		//MMSTransDTO bharatqrReq = (MMSTransDTO) JsonUtility.parseJson(jsonRequest, MMSTransDTO.class) ;
		if(bharatqrReq==null)
			throw new ApplicationException(ErrorMessages.JSON_FORMAT_ERROR.toString());
		String mpan = bharatqrReq.getMpan();
		if(mpan.startsWith("5")){
			mpan = mpan.substring(0,mpan.length() -1);
		}
		MerchantDetail md =  merchantDao.getMerchantDetails(mpan, null);
		if(md==null)
			throw new ApplicationException(ErrorMessages.MERCHANT_MAPPING_NOT_FOUND.toString());
		
		//check if transaction present
		boolean present = upiTransactionDao.checkBharatQRTxnExists(bharatqrReq.getRefNo(), bharatqrReq.getAuthCode());
		if(present){
			logger.info("txn already present for : " + bharatqrReq.getRefNo());
			throw new ApplicationException(ErrorMessages.DUPLICATE_TXN.toString());
		}
		
		logger.info("tid:"+md.getTid());
		logger.info("mid:"+md.getMerchantId());
		logger.info("Program Type:"+md.getProgramType());
		bharatqrReq.setTid(md.getTid());
		bharatqrReq.setMerchantId(md.getMerchantId());
		
		if(bharatqrReq.getSecondaryId()== null){
			logger.info("inside secondary id is null for Bharat QR Service");
			if(bharatqrReq.getAdditionalData()!=null)
			{
				Map<String,String> map =  parseAdditionalField(bharatqrReq.getPrimaryId(),bharatqrReq.getAdditionalData());

				String pId = map.get("primary_id");
				String sId = map.get("secondary_id");
				if(pId!=null)
				{
					logger.debug("setting primary id from additional data:"+pId);
					bharatqrReq.setPrimaryId(pId);
				}
				if(sId!=null)
				{
					logger.debug("setting secondary id from additional data:"+sId);
					bharatqrReq.setSecondaryId(sId);
				}
			}
		}
		
		try {
			bharatQRTransactionDao.saveMMSTxn(bharatqrReq);
			//String batchNumber =bharatqrReq.getTimeStamp().substring(8);
			String batchNumber= bharatqrReq.getBatchNumber();
			System.out.println("Batch Number : " + batchNumber);
			if(md.getProgramType()==7 || md.getProgramType() ==9 || md.getProgramType() ==13){
				MrlDTO mrl = upiTransactionDao.getDataForMrlBQR(bharatqrReq.getRefNo(),mpan,md.getProgramType(),batchNumber,bharatqrReq.getCurrency());
				mrl.setCardNumber(bharatqrReq.getCustPan());
				int count=0;
				try{
					int insert_count=upiTransactionDao.sendDataToMRLDB(mrl);
					if(insert_count==1){
						count =bharatQRTransactionDao.updateBQRMMSFlag(bharatqrReq.getRefNo(), "Y");
						logger.info("MMS Flag update Y : " + count + " for RRN "+bharatqrReq.getRefNo());
					}else{
						count =bharatQRTransactionDao.updateBQRMMSFlag(bharatqrReq.getRefNo(), "N");
						logger.info("Else MMS Flag update N : " + count + " for RRN "+bharatqrReq.getRefNo());
					}
				}catch(Exception e){
					count =bharatQRTransactionDao.updateBQRMMSFlag(bharatqrReq.getRefNo(), "N");
					logger.info(" Exceptio nMMS Flag update N : " + count + " for RRN "+bharatqrReq.getRefNo());
					logger.error(e.getMessage());
				}
				
				logger.info("MMS data inserted successfully in MMS DB");
				
			}
		} catch (Exception e) {
			logger.error("Exception in populateMMSTxn",e);
			throw new ApplicationException(ErrorMessages.SERVER_ERROR.toString());
		}
		Map<String, String> responseMap = new LinkedHashMap<>();
		responseMap.put("id", Integer.toString(bharatqrReq.getId()));
		responseMap.put("mid", bharatqrReq.getMerchantId());
		responseMap.put("bankCode", bharatqrReq.getBankCode());
		responseMap.put("programType", Integer.toString(md.getProgramType()));
		responseMap.put("status", Constants.SUCCESS.name());
		responseMap.put("rrn", bharatqrReq.getRefNo());
		responseMap.put("message",Constants.SUCCESS.name());
		return responseMap;
	
	}

	@Override
	public Response getCount(String bank_code, String date) {
		Response response=new Response();
		Long upi_transaction_count=0L;
		Long scheme_transaction_count=0L;
		response.setStatus("Failed");
		response.setMessage("No Records");
		response.setScheme_transaction_count(scheme_transaction_count);
		response.setUpi_transaction_count(upi_transaction_count);
		if(bank_code.isEmpty()||bank_code==null||date.isEmpty()||date==null){
			response.setMessage("Bank code and Date should not be empty");
			return response;
		}
		/*Date date = new Date();  
	    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
	    String strDate= formatter.format(date);  
		2019-04-21*/
		String startDate=date.concat(" 00:00:01");
		String endDate=date.concat(" 23:59:59");
		System.out.println("startdate="+startDate);
		System.out.println("enddate="+endDate);
		upi_transaction_count=bharatQRTransactionDao.getUPITransactionCount();
		scheme_transaction_count=bharatQRTransactionDao.getSchemeTransactionCount();
		response.setStatus("Success");
		response.setMessage("Transaction count fetched Successfully");
		response.setScheme_transaction_count(scheme_transaction_count);
		response.setUpi_transaction_count(upi_transaction_count);
		return response;
	}

}
