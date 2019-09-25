package com.wl.bharatqr.controller;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wl.bharatqr.util.ReconConstants;
import com.wl.upi.model.RefundRequest;
import com.wl.upi.model.TxnDTO;
import com.wl.upi.service.ReconMatchUnmatchService;
import com.wl.upi.service.UPIBankService;
import com.wl.upi.service.UPIServiceFactory;
import com.wl.util.HelperUtil;
import com.wl.util.ReconUtils;
import com.wl.util.config.ApplicationConfig;
import com.wl.util.config.BankConfig;
import com.wl.util.model.FileFinder;
import com.wl.util.model.ReconReq;
import com.wl.util.model.Response;

/**
 * 
 * @author ritesh.patil
 * 
 * Recon process For match unmatch transaction
 *
 */
@RestController
public class ReconMatchUnmatchController {
	
	
   private static final Logger logger = LoggerFactory.getLogger(ReconMatchUnmatchController.class);
	
	@Autowired
	private ReconMatchUnmatchService reconService;
	
	@Autowired
	@Qualifier("upiServiceFactory")
	private UPIServiceFactory upiServiceFactory;
	
	
	/**
	 * <p>Getting <b>bankcode</b> from request as a paramter based on code it will get corresponding bank
	 * name using some logic of today's date & matched pattern download the files from configure SFTP location for specific bank & upload to
	 * local server.
	 * 
	 *<p>From local server it reads files ,from file it reads records after reading process records for matched/Unmatched transaction.  
	 *
	 *  	Mobile Server	PSP File	      Match/Unmatch	Action
            Scenario 1	Not present	          Present Unmatch	Refund
            Scenario 2	Present – Fail	      Present Unmatch	Refund
            Scenario 3	Present	Not present	  unMatch	No Action
            Scenario 4	Present	present	      Match	No Action

	 * <p>After calling refund API for specific PSP it will create xlsx file based on failure & success reponse copy both 
	 * files in SFTP location of PSP. 
	 *     
	 * PSP & Other entity will take xlsx  from SFTP location.
	 * 
	 * SFTP Location will be    
	 * 
	 * Dump file path                 SFTP Path + bank name              /home/appladm/SFTP/SBI
	 * Success xlsx file create       SFTP Path + bank name + success    /home/appladm/SFTP/SBI/Success
	 *                                                                   /home/appladm/SFTP/SBI/Failed
	 * Local server path 
	 * for input file                local path + bank name + Input      //D:\SFTP_FILE\SBI\Input
	 * process file                  local path + bank name + Processed  
	 * validation failed file        local path + bank name + Validation_failed
	 * success file                  local path + bank name + Success  
	 * Failed file                   local path + bank name + Failed  
	 * 
	 * @param bankCode
	 * @param httpServletRequest
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/recon/{bankCode}", method = RequestMethod.GET)
	@ResponseBody
	public Response dataSync(@PathVariable("bankCode") String bankCode, HttpServletRequest httpServletRequest)
			throws IOException {

		logger.info("Start dataSync method execution with bankcode " + bankCode);
		Response response = new Response();
	
		String fileName = null;
		String extension = null;
		
		if("00031".equals(bankCode)) //Axis Bank
		{
			fileName = ReconConstants.FILE_NAME_PREFIX;
			extension = ReconConstants.XLSX_EXTENSION;
		}
		else if("00075".equals(bankCode)) // Sbi Bank
		{
			fileName = ReconConstants.FILE_NAME_SBI_PREFIX;
			extension = ReconConstants.CSV_EXTENSION;
		}
		// try {
		//if (ReconUtils.downLoadFile(bankCode, ReconConstants.FILE_NAME_PREFIX, ReconConstants.XLSX_EXTENSION)) {
		if (ReconUtils.downLoadFile(bankCode, fileName, extension)) {
			String curDate = new SimpleDateFormat("ddMMyyyy").format(new Date());
			Calendar calendar = Calendar.getInstance();
			String randomNo = calendar.get(Calendar.HOUR_OF_DAY) + "" + calendar.get(Calendar.MINUTE) + ""
					+ calendar.get(Calendar.SECOND) + "" + calendar.get(Calendar.MILLISECOND);
			HashMap<String, String> attachment = null;
			List<ReconReq> readSuccesslist = new ArrayList<ReconReq>();
			List<ReconReq> validationFieldList = new ArrayList<ReconReq>();
			final List<ReconReq> successList = new ArrayList<ReconReq>();
			final List<ReconReq> failedList = new ArrayList<ReconReq>();
			String localPath = BankConfig.get(bankCode, "SFTPDOWNLOADFOLDER") + ReconUtils.getBankName(bankCode)
					+ File.separator;
			// String localPath=BankConfig.get(bankCode,
			// "SFTPDOWNLOADFOLDER")+ReconUtils.getBankName(bankCode)+"\\";
			final String bCode = bankCode;
			processFiles(readSuccesslist, bankCode, validationFieldList);
			if (!validationFieldList.isEmpty()) {
				// create xlsx for validation failed and send mail to SPOC
				attachment = new HashMap<String, String>();
				String validationFailedFile = "validationFailed_" + curDate + "_" + randomNo
						+ ReconConstants.XLSX_EXTENSION;
				String failedPath = localPath + ReconConstants.VALIDATION_FAILED_DIR + File.separator;
				// String
				// failedPath=localPath+ReconConstants.VALIDATION_FAILED_DIR+"\\";
				generateXLSFIle(validationFieldList, failedPath + validationFailedFile, "fail", true);
				attachment.put(validationFailedFile, failedPath + validationFailedFile);
				if(!"00075".equals(bankCode)) {
					ReconUtils.send(bankCode, ApplicationConfig.get("ReconMailFrom"), ApplicationConfig.get("ReconMailTo"),
							ApplicationConfig.get("ReconMailCC"), ReconConstants.VALIDATION_FAILED_SUBJECT,
							ReconConstants.VALIDATION_FAILED_MSG, attachment);
				}
				logger.info("Validation Failed file created in local server");
			}
			logger.info("success list size " + readSuccesslist.size());
			logger.info("errorList list size " + validationFieldList.size());

			if (!readSuccesslist.isEmpty()) {
				final List<TxnDTO> dtos = reconService.listOfLastThreeDaysTransaction(bankCode);

				if (dtos.isEmpty())
					logger.info("We dont have last three days records for bank " + bankCode);
				else
					logger.info("We have last three days records count " + dtos.size() + " bankcode " + bankCode);

				// RefundRequest refundRequest=null;
				ExecutorService executorService = Executors.newFixedThreadPool(50);
				for (final ReconReq reconReq : readSuccesslist) {
					// logger.info(reconReq.getRrn() + "
					// "+reconReq.getGatewayTransId());
					executorService.execute(new Runnable() {
						@Override
						public void run() {
							logger.info("Name of Thraed " + Thread.currentThread().getName());
							boolean flag = true;
							String terminalId = null;
							for (TxnDTO txnDTO : dtos) {
								logger.info(txnDTO.getRrn() + "         " + txnDTO.getOriginalTrID());
								
								if("00031".equals(bCode)) //Axis Bank
								{
									logger.info("inside AXIS :::::");
									if (reconReq.getRrn().equals(txnDTO.getRrn())
											&& reconReq.getGatewayTransId().equals(txnDTO.getOriginalTrID())) {
										if (txnDTO.getResponseCode().equals("00") || txnDTO.getResponseCode().equals("000")
												|| txnDTO.getResponseCode().equalsIgnoreCase("s")) {											
											// Matched & Success Transaction
											flag = false;
											logger.info("present in both PSP & Mobile with SUCCESS response code rrn is "
													+ reconReq.getRrn() + " GatewayTransactionId :"
													+ reconReq.getGatewayTransId());
											break;
										} else {
											// Matched & Failed Transaction Intiated
											// Refund
											processRefund(bCode, reconReq, txnDTO, successList, failedList);
											flag = false;
											logger.info(
													"present in both PSP & Mobile with FAILED response code Refund initated rrn is "
															+ reconReq.getRrn() + " GatewayTransactionId :"
															+ reconReq.getGatewayTransId());

											break;
										}
									}
									terminalId = txnDTO.getTid();	
								}
								else if("00075".equals(bCode)) // Sbi Bank
								{
									logger.info("inside SBI :::::");
									if (reconReq.getGatewayTransId().equals(txnDTO.getOriginalTrID())) {
										
										if (txnDTO.getResponseCode().equals("00") || txnDTO.getResponseCode().equals("000")
												|| txnDTO.getResponseCode().equalsIgnoreCase("s")) {											
											logger.info("txnDTO response code ::::"+txnDTO.getResponseCode());
											// Matched & Success Transaction
											flag = false;
											logger.info("present in both SBIPSP & Mobile with SUCCESS response code GatewayTransactionId is "+ reconReq.getGatewayTransId());
											break;
										} else {
											// Matched & Failed Transaction Intiated
											// Refund
											logger.info("inside else ::::::::");
											processRefund(bCode, reconReq, txnDTO, successList, failedList);											
											flag = false;
											logger.info(
													"present in both SBIPSP & Mobile with FAILED response code GatewayTransactionId :"
															+ reconReq.getGatewayTransId());

											break;
										}
									}
								}
							}
							if (flag) {
								// Present in PSP but not in Mobile Initiated
								// Refund
								TxnDTO txnDTO = new TxnDTO();
								if("00031".equals(bCode)) //Axis Bank
								{
									txnDTO.setTxnAmount(String.valueOf(reconReq.getAmount()));
									txnDTO.setRrn(reconReq.getRrn());
									txnDTO.setMerchantId(reconReq.getMerchantId());
									txnDTO.setMerchantVpa(reconReq.getPayeeAddr());
									txnDTO.setCustomerVpa(reconReq.getPayerAddr());
									txnDTO.setTrId(reconReq.getTransId());
									txnDTO.setTid(terminalId);
									txnDTO.setOriginalTrID(reconReq.getGatewayTransId());
									processRefund(bCode, reconReq, txnDTO, successList, failedList);
									flag = false;
									logger.info("present in PSP but not in Mobile  Refund initated rrn: "
											+ reconReq.getRrn() + " GatewayTransactionId :" + reconReq.getGatewayTransId());
								}
								else if("00075".equals(bCode)) // Sbi Bank
								{
									String refundTxnId = HelperUtil.getRRN(); // generating new RRN for refund 
									txnDTO.setTxnAmount(String.valueOf(reconReq.getAmount()));
									txnDTO.setRrn(refundTxnId);
									txnDTO.setMerchantId(reconReq.getMerchantId());
									txnDTO.setMerchantVpa(reconReq.getPayeeAddr());
									txnDTO.setCustomerVpa(reconReq.getPayerAddr());
									txnDTO.setTrId(reconReq.getOrderNo()); /**For sbi refund setting order number as trId as trId is not provided by mis*/ 
									txnDTO.setTransRefNo(reconReq.getTransRefNo());
									txnDTO.setTid(terminalId);
									txnDTO.setOriginalTrID(reconReq.getGatewayTransId());
									txnDTO.setCustomerRefNo(reconReq.getCustomerRefNo());
									txnDTO.setOrderNo(reconReq.getOrderNo());
									txnDTO.setRefundRemark(reconReq.getRefundRemark());
									processRefund(bCode, reconReq, txnDTO, successList, failedList);
									
									flag = false;
									logger.info("present in SBIPSP but not in Mobile, GatewayTransactionId :" + reconReq.getGatewayTransId());
								}
							}
							logger.info("End of Thraed " + Thread.currentThread().getName());
						}
					});
				}
				/*
				 * Iterator<ReconReq> iterator = readSuccesslist.iterator();
				 * while(iterator.hasNext()){ ReconReq reconReq=iterator.next(); //After check
				 * reconreq object remove from list }
				 */
				executorService.shutdown();
				while (!executorService.isTerminated()) {
					// logger.info("Inside while loop ");
				}

				String SFTPHost = BankConfig.get(bankCode, "ReconSftpIp");
				String SFTPPort = BankConfig.get(bankCode, "ReconSftpPort");
				String SFTPUser = BankConfig.get(bankCode, "ReconSftpUserName");
				String SFTPPass = BankConfig.get(bankCode, "ReconSftpPass");
				// local
				String SFTPWorkingDir = BankConfig.get(bankCode, "ReconSftpPath") + "/"
						+ ReconUtils.getBankName(bankCode) + "/";
				// SIT / PROD
				// String SFTPWorkingDir = BankConfig.get(bankCode,
				// "ReconSftpPath")+File.separator+ReconUtils.getBankName(bankCode)+File.separator;
				logger.info("Final success list size " + successList.size());
				logger.info("Final failed list size " + failedList.size());
				if (!failedList.isEmpty()) {
					String failedResponseFile = ReconConstants.REFUND_FAILED_FILE + curDate + "_" + randomNo
							+ ReconConstants.XLSX_EXTENSION;
					String failedPath = localPath + ReconConstants.FAILED_DIR + File.separator;
					generateXLSFIle(failedList, failedPath + failedResponseFile, "fail", false); // create file name random
					// SIT /Prod
					ReconUtils.uploadFileToServer(new String[] { failedResponseFile, failedPath,
							SFTPWorkingDir + ReconConstants.FAILED_DIR + File.separator, SFTPHost, SFTPUser, SFTPPass,
							SFTPPort });
					// local
					/*
					 * ReconUtils.uploadFileToServer(new
					 * String[]{failedResponseFile,failedPath,SFTPWorkingDir+
					 * ReconConstants.FAILED_DIR+"/", SFTPHost,SFTPUser,SFTPPass,SFTPPort});
					 */
				}
				if (successList.isEmpty()) {
					logger.info("No records find for refund Matched /Unmatched Process");
					response.setMessage("No records find for refund ");
					response.setStatus("FAILED");
				} else {
					String successResponseFile = ReconConstants.REFUND_SUCCESS_FILE + curDate + "_" + randomNo
							+ ReconConstants.XLSX_EXTENSION;
					String successPath = localPath + ReconConstants.SUCCESS_DIR + File.separator;
					if("00031".equals(bankCode))
					{
						generateXLSFIle(successList, successPath + successResponseFile, "Success", false);	
					}
					else if("00075".equals(bankCode))
					{
						generateXLSFIleForSbi(successList, successPath + successResponseFile, "Success", false);
					}
					// SIT /Prod
					ReconUtils.uploadFileToServer(new String[] { successResponseFile, successPath,
							SFTPWorkingDir + ReconConstants.SUCCESS_DIR + File.separator, SFTPHost, SFTPUser, SFTPPass,
							SFTPPort });
					// local
					/*
					 * ReconUtils.uploadFileToServer(new
					 * String[]{successResponseFile,successPath,SFTPWorkingDir+
					 * ReconConstants.SUCCESS_DIR+"/", SFTPHost,SFTPUser,SFTPPass,SFTPPort});
					 */
					logger.info("Refunded records created in XLS file and placed in SFTP location");
					response.setMessage("Refunded records created in XLS file and placed in SFTP location");
					response.setStatus("SUCCESS");
				}

			} else {
				logger.info("No Records found in XLS file for refund. ");
				response.setMessage("No Records found in XLS file for refund. ");
				response.setStatus("FAILED");
			}
			logger.info("End of dataSync method execution with bankcode " + bankCode);
			if(!"00075".equals(bankCode)) {
				
				ReconUtils.send(bankCode, ApplicationConfig.get("ReconMailFrom"), ApplicationConfig.get("ReconMailTo"),
						ApplicationConfig.get("ReconMailCC"), ReconConstants.SUCCESS_EXCECUTION_SUB,
						ReconConstants.SUCCESS_EXCECUTION_MSG, null);
			}
		} else {
			logger.info("No files found for Download ");
			response.setStatus("FAILED");
			response.setMessage("No files found for Download. ");
		}
		
		return response;
	}
	
	 
   /**
    * Method used to create failed or success xlsx file
    * 
    * @param successList
    * @param location
    * @param status
    * @param check
    * @throws IOException
    */
	private void generateXLSFIle(List<ReconReq> successList,String location,String status,boolean check) throws IOException {
		    Workbook workbook = new XSSFWorkbook();
		    Sheet sheet = workbook.createSheet();
		    String excelFilePath = location;
		    int rowCount = 0;
		    CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
		    Font font = sheet.getWorkbook().createFont();
		    //font.setBold(true);
		    font.setFontHeightInPoints((short) 12);
		    cellStyle.setFont(font);
		    Row row = sheet.createRow(0);
		    Cell cell = row.createCell(0);		    
		    cell.setCellStyle(cellStyle);
		    cell.setCellValue("MID");
		    cell = row.createCell(1);
		    cell.setCellStyle(cellStyle);
		    cell.setCellValue("TRANSACTION_ID");
		    cell = row.createCell(2);
		    cell.setCellStyle(cellStyle);
		    cell.setCellValue("RRN");
		    cell = row.createCell(3);
		    cell.setCellStyle(cellStyle);
		    if(check) cell.setCellValue("TRN_STATUS");
		    else cell.setCellValue("Txn_ID_Refund");
		    cell = row.createCell(4);
		    cell.setCellStyle(cellStyle);
		    cell.setCellValue("payerAddr");
		    cell = row.createCell(5);
		    cell.setCellStyle(cellStyle);
		    cell.setCellValue("payeeAddr");
		    cell = row.createCell(6);
		    cell.setCellStyle(cellStyle);
		    cell.setCellValue("TRN_DATE_TIME");
		    cell = row.createCell(7);
		    cell.setCellStyle(cellStyle);
		    if(check) cell.setCellValue("MCC");
		    else cell.setCellValue("Refund_TXN_Date_Time");
		    cell = row.createCell(8);
		    cell.setCellStyle(cellStyle);
		    cell.setCellValue("Amount");
		    cell = row.createCell(9);
		    cell.setCellStyle(cellStyle);
		    cell.setCellValue("GATEWAYTXN ID");
		    if(check){
		    	cell = row.createCell(10);
			    cell.setCellStyle(cellStyle);
			    cell.setCellValue("Mobile No");
		    }
		    if("fail".equals(status)){
		    	if(check) cell = row.createCell(11);
		    	else cell = row.createCell(10);
		    	cell.setCellStyle(cellStyle);
		    	cell.setCellValue("Error");
		    }
		    for (ReconReq reconReq : successList) {
		    	Row row1 = sheet.createRow(++rowCount);
		    	writeBook(reconReq,row1,status,check); 
		    }
		    FileOutputStream outputStream=null;
		    try  {
		    	outputStream = new FileOutputStream(excelFilePath);
		        workbook.write(outputStream);
		    }catch (Exception e) {
		    	e.printStackTrace();
		    	logger.error("Exception while creating excel sheet status : "+status +"checker type "+ check);
				
			}finally{
				logger.info("The file is closed "+excelFilePath);
				outputStream.flush();
				outputStream.close();
			}
	     //Write code for SFTP location
		 //upload in SFTP location
	  }
		
		/**
	    * Method used to create failed or success xlsx  sbi file
	    * 
	    * @param successList
	    * @param location
	    * @param status
	    * @param check
	    * @throws IOException
	    */
		private void generateXLSFIleForSbi(List<ReconReq> successList,String location,String status,boolean check) throws IOException {
			logger.info("inside generateXLSFIleForSbi ::::::"+successList);
			    Workbook workbook = new XSSFWorkbook();
			    Sheet sheet = workbook.createSheet();
			    String excelFilePath = location;
			    int rowCount = 0;
			    CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
			    Font font = sheet.getWorkbook().createFont();
			    //font.setBold(true);
			    font.setFontHeightInPoints((short) 12);
			    cellStyle.setFont(font);
			    Row row = sheet.createRow(0);
			    Cell cell = row.createCell(0);			    
			    cell.setCellStyle(cellStyle);
			    cell.setCellValue("MID");
			    cell = row.createCell(1);
			    cell.setCellStyle(cellStyle);
			    cell.setCellValue("RRN");
			    cell = row.createCell(2);
			    cell.setCellStyle(cellStyle);
			    cell.setCellValue("TRANSACTION_REF_NUMBER");			    
			    cell = row.createCell(3);
			    cell.setCellStyle(cellStyle);
			    cell.setCellValue("CUSTOMER_REF_NUMBER");
			    cell = row.createCell(4);
			    cell.setCellStyle(cellStyle);
			    cell.setCellValue("ORDER_NO");
			    cell = row.createCell(5);
			    cell.setCellStyle(cellStyle);
			    cell.setCellValue("REFUND_REQUEST_AMT");
			    cell = row.createCell(6);
			    cell.setCellStyle(cellStyle);
			    cell.setCellValue("REFUND_REMARK");
			    if("fail".equals(status)){
			    	if(check) cell = row.createCell(8);
			    	else cell = row.createCell(7);
			    	cell.setCellStyle(cellStyle);
			    	cell.setCellValue("Error");
			    }
			    for (ReconReq reconReq : successList) {
			    	Row row1 = sheet.createRow(++rowCount);
			    	writeBookForSBI(reconReq,row1,status,check); 
			    }
			    FileOutputStream outputStream=null;
			    try  {
			    	outputStream = new FileOutputStream(excelFilePath);
			        workbook.write(outputStream);
			    }catch (Exception e) {
			    	e.printStackTrace();
			    	logger.error("Exception while creating excel sheet status : "+status +"checker type "+ check);
					
				}finally{
					logger.info("The file is closed "+excelFilePath);
					outputStream.flush();
					outputStream.close();
				}
		  }
	
      /**
       * Method used to read one by one all the files belongs to given format
       * read one by one record from file
       * 
       * @param list
       * @param bankCode
       * @param erorrList
       */
	  private void processFiles(List<ReconReq> list,String bankCode,List<ReconReq> erorrList){
			           try {
							
			    	       // Production/SIT
			    	        String commonLoc=BankConfig.get(bankCode, "SFTPDOWNLOADFOLDER")+ReconUtils.getBankName(bankCode)+File.separator;
			    	        String inputloc = commonLoc+ReconConstants.INPUT_DIR+File.separator;
							String failedLoc=commonLoc+ReconConstants.FAILED_DIR+File.separator;
			    	        String processedDir=commonLoc+ReconConstants.PROCESSED_DIR+File.separator;
			    	        String validationFailedloc = commonLoc+ReconConstants.VALIDATION_FAILED_DIR+File.separator;
							String successLoc=commonLoc+ReconConstants.SUCCESS_DIR+File.separator;
			    	   //Local use
			    	       /* String commonLoc=BankConfig.get(bankCode, "SFTPDOWNLOADFOLDER")+ReconUtils.getBankName(bankCode)+"\\";
			    	        String inputloc = commonLoc+ReconConstants.INPUT_DIR+"\\";
							String failedLoc=commonLoc+ReconConstants.FAILED_DIR+"\\";
			    	        String processedDir=commonLoc+ReconConstants.PROCESSED_DIR+"\\";
			    	        String validationFailedloc = commonLoc+ReconConstants.VALIDATION_FAILED_DIR+"\\";
							String successLoc=commonLoc+ReconConstants.SUCCESS_DIR+"\\";*/
			    	        
			    	        File directory = new File(inputloc);
							if (!directory.exists())
							        directory.mkdir();
							directory = new File(failedLoc);
							if (!directory.exists())
							       directory.mkdir();
							directory = new File(processedDir);
							if (!directory.exists())
							       directory.mkdir();
							directory = new File(validationFailedloc);
							if (!directory.exists())
							       directory.mkdir();
							directory = new File(successLoc);
							if (!directory.exists())
							       directory.mkdir();
							
			    	        //Scanner scanner = null;
							String curDate = new SimpleDateFormat("ddMMyyyy").format(new Date());
							String fileName = null;
							if("00031".equals(bankCode)) //Axis Bank
							{
								fileName=ReconConstants.FILE_NAME_PREFIX+"_"+bankCode+"_"+curDate+"_*"+ReconConstants.XLSX_EXTENSION;
							}
							else if("00075".equals(bankCode)) // Sbi Bank
							{
								fileName=ReconConstants.FILE_NAME_SBI_PREFIX+"_"+curDate+"_*"+ReconConstants.CSV_EXTENSION;
								logger.info("filename for sbi :::"+fileName);
							}
							//String fileName=ReconConstants.FILE_NAME_PREFIX+"_"+bankCode+"_"+curDate+"_*"+ReconConstants.XLSX_EXTENSION;
							FileFinder finder = new FileFinder(fileName);
			                // pass the initial directory and the finder to the file tree walker
							try {
								Files.walkFileTree(Paths.get(inputloc), finder);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								logger.error(e1.getMessage());
							}
			
							// get the matched paths
							Collection<Path> matchedFiles = finder.getMatchedPaths();
			                 if (matchedFiles.size() == 0) {
			                	 logger.info("No matchedFiles :::");
								//email if no file received
								/*SendMerchantStatusEmail.send(PropertyFileHandler.getEmail_from()
										, PropertyFileHandler.getEmail_to()
										, PropertyFileHandler.getEmail_cc()
										, PropertyFileHandler.getEmail_subject_failed() + " of " + getBankCode()
										, PropertyFileHandler.getEmail_body_merchant_detail_failed_no_file_found().toString().replace("<date>", utilityObj.getDate(0)).replace("<bank_code>", getBankCode())
										, null);*/
								return;
							} else {
								for (Path path : matchedFiles) {
																		
									//Path srcPath=FileSystems.getDefault().getPath(inputloc+path.getFileName());
									//Path desPath=FileSystems.getDefault().getPath(processedDir+path.getFileName());
			                        logger.debug("File Found :"+path.getFileName());
									String dest_filename = path.getFileName().toString();
									try {
										File text = new File(inputloc + dest_filename);
										if(!text.exists())
										{
											logger.debug("File :"+text.getName()+" does not exist.");
											continue;
										}
										logger.info("inputloc  "+inputloc);
										if("00031".equals(bankCode)) //Axis Bank
										{
											FileInputStream inputStream = new FileInputStream(new File(inputloc+path.getFileName()));
											Workbook workbook = new XSSFWorkbook(inputStream);
										    Sheet firstSheet = workbook.getSheetAt(0);
										    Iterator<org.apache.poi.ss.usermodel.Row> iterator =firstSheet.iterator();
										    ReconReq req=null;
										    while (iterator.hasNext()) {
											        Row nextRow = iterator.next();
											        logger.info("Row cell size "+nextRow.getLastCellNum());
											        if(nextRow.getRowNum()==0 ){
											        	   continue; //just skip the rows if row number is 0 or 1
											         }
											       
											        req = new ReconReq();
											        validateRow(req,nextRow);
											         if(!req.getError().isEmpty()){
						                				logger.error("Error: Validation failed for fields " + req.getError());
						                				erorrList.add(req);
						                			}else{
						                			     list.add(req);
						                			} 
											    }
										    inputStream.close();
										}
										else if("00075".equals(bankCode)) // Sbi Bank
										{	
											FileInputStream inputStream = new FileInputStream(new File(inputloc+path.getFileName()));
											InputStreamReader r = new InputStreamReader(inputStream);
											BufferedReader br = new BufferedReader(r);
											ReconReq req=null;
											String line = "";
								            //Read to skip the header
								            br.readLine();
								            //Reading from the second line
								            while ((line = br.readLine()) != null) 
								            {
								                String[] sbidetails = line.split(",");
								                req = new ReconReq();
										        validateRowForCsv(req,sbidetails);
										         if(!req.getError().isEmpty()){
					                				logger.error("Error: Validation failed for fields " + req.getError());
					                				erorrList.add(req);
					                			}else{
					                			     list.add(req);
					                			} 
								            }
										}
										
									    ReconUtils.moveFileSrctoDestLocation(path.getFileName(),inputloc, processedDir);
									} catch (IOException e) {
										 e.printStackTrace();
										 logger.error(e.getMessage());
								 }
							}
						 }
			
			           } catch (Exception e) {
							e.printStackTrace();
							logger.error(e.getMessage());
						}
					finally{
							
						}
		 }
			    
		
            /**
             * Method used to validate records present in xlsx file
             *       
             * @param reconReq
             * @param nextRow
             */
			private void validateRow(ReconReq reconReq,Row nextRow){
				StringBuffer buffer=new StringBuffer();
				String str ="";
				
				try{
					str =(String)getCellValue(nextRow.getCell(0));
	                if(str.isEmpty() || str.length() != 15)
	                	buffer.append(" Merchant Id is empty or length should be 15 characters,");
	                else
	                	reconReq.setMerchantId(str);
				}catch(Exception e){
					 buffer.append(" Merchant Id is "+e.getMessage());
				}
				try{
					str =(String)getCellValue(nextRow.getCell(1));
	                if(str.isEmpty() || str.length() != 35)
	                	buffer.append(" Transaction Id is empty or length should be 35 characters,");
	                else
	                	reconReq.setTransId(str);
				}catch(Exception e){
					 buffer.append(" Transaction Id is "+e.getMessage());
				}
			
				try{
	            	str =(String)getCellValue(nextRow.getCell(2));
	                if(str.isEmpty() || str.length() != 12)
	                	buffer.append(" RRN is empty or length should be 12 characters,");
	                else
	                	reconReq.setRrn(str);
				}catch(Exception e){
					 buffer.append(" RRN is "+e.getMessage());
				}
				try{	
	                str =(String)getCellValue(nextRow.getCell(3));
	                if(str.isEmpty() ||  str.length() >= 10)
	                	buffer.append(" Transaction status is empty or length is greater than 10,");
	                else
	                	reconReq.setTransStatus(str);
	            }catch(Exception e){
					 buffer.append(" Transaction status is "+e.getMessage());
				}
            	 	
             try{
	            	str =(String)getCellValue(nextRow.getCell(4));
	                if(str.isEmpty() ||  str.length() >= 50 )
	                	buffer.append(" Address of payer is empty or length is greater than 50,");
	                else
	                	reconReq.setPayerAddr(str);
	            }catch(Exception e){
					 buffer.append(" Address of paye is "+e.getMessage());
				}
        
               try{
	            	str =(String)getCellValue(nextRow.getCell(5));
	                if(str.isEmpty() ||  str.length() >= 50)
	                	buffer.append(" Address of payee is empty or length is greater than 50,");
	                else
	                	reconReq.setPayeeAddr(str);
	            }catch(Exception e){
					 buffer.append(" Address of payee is "+e.getMessage());
				}
               try{
                    str =(String)getCellValue(nextRow.getCell(6));
	                if(str.isEmpty())
	                	buffer.append(" Transaction date time is empty or format is wrong,");
	                else
	                	reconReq.setTransDateTime(str);
	            }catch(Exception e){
					 buffer.append(" Transaction date time is "+e.getMessage());
				}
			   
               try{
                  str =(String)getCellValue(nextRow.getCell(7));
	                if(str.isEmpty() ||  str.length() != 4)
	                	buffer.append(" MCC is empty or length is greater than 35,");
	                else
	                	reconReq.setMCC(str);
	            }catch(Exception e){
					 buffer.append(" MCC is "+e.getMessage());
				}
               try{    
            	    double amount =(double) getCellValue(nextRow.getCell(8));
	                if(amount == 0.0 || str.length() >= 12)
	                	buffer.append(" Amount is empty or length is greater than 35,");
	                else
	                	reconReq.setAmount(amount);
	            }catch(Exception e){
					 buffer.append(" Amount is "+e.getMessage());
				}
               try{   
	                str =(String)getCellValue(nextRow.getCell(9));
	                if(str.isEmpty() || str.length() != 35)
	                	buffer.append(" Gateway Transaction Id is empty or length is greater than 35,");
	                else
	                	reconReq.setGatewayTransId(str);
	            }catch(Exception e){
					 buffer.append(" Gateway Transaction Id is "+e.getMessage());
				}
	                
               try{  
	                str =(String)getCellValue(nextRow.getCell(10));
	                if(str.isEmpty())
	                	buffer.append(" Mobile no. is empty,");
	                else
	                	reconReq.setMobileNo(str);
	                if(!str.isEmpty() && !str.equalsIgnoreCase("NA")){
	                	if(str.length() != 10){
	                		buffer.append(" Mobile no. should be 10 digits,");
	                	}
	                }
	            }catch(Exception e){
					 buffer.append(" Mobile no. is "+e.getMessage());
				}
			      
            
				
		        if(nextRow.getLastCellNum() < 11)
		        	buffer.append("Invalid column size coulmn size is : "+nextRow.getLastCellNum());
		        if(!buffer.toString().isEmpty())
	            	reconReq.setError(buffer.toString());
			}
			/**
			 * <p> Method used to check xslx columns data type 
			 * 
			 * @param cell
			 * @return
			 */
			private Object getCellValue(Cell cell) {
					if (cell == null) {
			            return "";
			        }
				    switch (cell.getCellType()) {
				    case Cell.CELL_TYPE_BLANK:
				    	return "";
	                case Cell.CELL_TYPE_STRING:
				        return cell.getStringCellValue();
				 
				    case Cell.CELL_TYPE_BOOLEAN:
				        return cell.getBooleanCellValue();
				 
				    case Cell.CELL_TYPE_NUMERIC:
				    	 if(cell.getColumnIndex() == 8)
				    		 return cell.getNumericCellValue();
				    	 if (DateUtil.isCellDateFormatted(cell)) {
				             SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
				             System.out.print(dateFormat.format(cell.getDateCellValue()) + "\t\t");
				             return dateFormat.format(cell.getDateCellValue());
				         } else {
				             System.out.print(cell.getNumericCellValue() + "\t\t");
				             return String.valueOf((int)cell.getNumericCellValue());
				         }
				    }
					return null;
			 }
			
			/**
			 * 
			 * <p>Method used for calling Refund API to PSP
			 * 
			 * @param bankCode
			 * @param reconReq
			 * @param txnDTO
			 * @param successList contains successfully refund from PSP 
			 * @param failedList  contains failed refunds from PSP or exception raise refunds
			 */
			private void processRefund(String bankCode,ReconReq reconReq,TxnDTO txnDTO,List<ReconReq> successList,List<ReconReq> failedList){
				logger.info("Inside Process Refund Method::::::::::");
				  SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
				  logger.info("Thread Name :"+Thread.currentThread().getName());
				  RefundRequest refundRequest=new RefundRequest();
				  
				  if("00031".equals(bankCode))
				  {
				  ///refundRequest.setAuthCode(authCode);
				  refundRequest.setBankCode(bankCode);
				  refundRequest.setFromEntity(ReconConstants.FILE_NAME_PREFIX);
				  refundRequest.setMobileNumber(reconReq.getMobileNo());
				  refundRequest.setRefundAmount(reconReq.getAmount()+"");   // Check this condition
				 // refundRequest.setRefundId(refundId);
				  refundRequest.setRefundReason("Recon through File Matched/Unmatched Process");
				  //refundRequest.setTxnType("2"); // chekc with sonali
				  refundRequest.setRrn(reconReq.getRrn());
				  }
				  else if("00075".equals(bankCode))
				  {
					  refundRequest.setBankCode(bankCode);
					  refundRequest.setFromEntity(ReconConstants.FILE_NAME_SBI_PREFIX);					 
					  refundRequest.setRefundAmount(reconReq.getAmount()+"");   // Check this condition					 
					  refundRequest.setRefundReason("Customer Refund");					  
					  refundRequest.setRrn(txnDTO.getRrn());		
				  }
				  if("000".equals(bankCode))
					  refundRequest.setAuthCode(reconReq.getMCC());
				  UPIBankService upiBankService =  upiServiceFactory.getUPIService(bankCode);
				  Response response=null;
				  try{
					  if("00031".equals(bankCode))
					  {
						  response=upiBankService.upiRefundRecon("Recon", bankCode , refundRequest,txnDTO);
							if (response.getStatus().equals("SUCCESS")) {
								Map<String, String> responseMap = (Map<String, String>) response.getResponseObject();
								reconReq.setTxnRefundId(responseMap.get("refundId")); // Transaction Refund Id
								// reconReq.setMerchantId(responseMap.get("merchId")); // Merchant Id
								reconReq.setRefundTXNDate(dateFormat.format(new Date()));
								successList.add(reconReq);
							} else {
								reconReq.setError(response.getMessage());
								failedList.add(reconReq);
							}
					  }
					  else if("00075".equals(bankCode))
					  {
						  response=upiBankService.upiRefundRecon("Sbi", bankCode , refundRequest,txnDTO);
						  if (response.getStatus().equals("SUCCESS")) {
								Map<String, String> responseMap = (Map<String, String>) response.getResponseObject();
								reconReq.setTxnRefundId(txnDTO.getRrn()); // Transaction Refund Id
								reconReq.setRrn(txnDTO.getRrn());
								reconReq.setMerchantId(txnDTO.getMerchantId()); // Merchant Id
								reconReq.setRefundTXNDate(dateFormat.format(new Date()));
								successList.add(reconReq);
							} else {
								reconReq.setError(response.getMessage());
								failedList.add(reconReq);
							}
					  }
				     
				  }catch(Exception e){
					     e.printStackTrace();
					     logger.error("Gettting Exception at the time of Processing refund "+e.getStackTrace());
					     reconReq.setError(e.getMessage());
					     failedList.add(reconReq);
				  }
			}
			
			
			
			/**
			 * <p>Write the data on file
			 * 
			 * @param reconReq
			 * @param row
			 * @param status
			 * @param check
			 */
		   private void writeBook(ReconReq reconReq, Row row,String status,boolean check) {
	            Cell cell = row.createCell(0);
			    cell.setCellValue(reconReq.getMerchantId());
			    cell = row.createCell(1);
			    cell.setCellValue(reconReq.getTransId());
			    cell = row.createCell(2);
			    cell.setCellValue(reconReq.getRrn());
			    cell = row.createCell(3);
			    if(check) cell.setCellValue(reconReq.getTransStatus()); 
			    else cell.setCellValue(reconReq.getTxnRefundId());
			    cell = row.createCell(4);
			    cell.setCellValue(reconReq.getPayerAddr());
			    cell = row.createCell(5);
			    cell.setCellValue(reconReq.getPayeeAddr());
			    cell = row.createCell(6);
			    cell.setCellValue(reconReq.getTransDateTime());
			    cell = row.createCell(7);
			    if(check) cell.setCellValue(reconReq.getMCC());
			    else cell.setCellValue(reconReq.getRefundTXNDate());
			    cell = row.createCell(8);
			    cell.setCellValue((double)reconReq.getAmount());
			    cell = row.createCell(9);
			    cell.setCellValue(reconReq.getGatewayTransId());
			    if(check){
			        cell = row.createCell(10);
				    cell.setCellValue(reconReq.getMobileNo());
			    }
			    if("fail".equals(status)){
			    	if(check) cell = row.createCell(11);
			    	else cell = row.createCell(10);
			    	cell.setCellValue(reconReq.getError());
			    }
        }
		   
		   /**
			 * <p>Write the data on file
			 * 
			 * @param reconReq
			 * @param row
			 * @param status
			 * @param check
			 */
		   private void writeBookForSBI(ReconReq txnDto, Row row,String status,boolean check) {
	            Cell cell = row.createCell(0);
			    cell.setCellValue(txnDto.getMerchantId());
			    cell = row.createCell(1);
			    cell.setCellValue(txnDto.getRrn());
			    cell = row.createCell(2);
			    cell.setCellValue(txnDto.getTransRefNo());
			    cell = row.createCell(3);
			    cell.setCellValue(txnDto.getCustomerRefNo());
			    cell = row.createCell(4);
			    cell.setCellValue(txnDto.getOrderNo());
			    cell = row.createCell(5);
			    cell.setCellValue((double)txnDto.getAmount());
			    cell = row.createCell(6);
			    cell.setCellValue(txnDto.getRefundRemark());
			  
			    /*if("fail".equals(status)){
			    	if(check) cell = row.createCell(8);
			    	else cell = row.createCell(7);
			    	cell.setCellValue(txnDto.getError());
			    }*/
       }
		   
			/*private Workbook getWorkbook(String excelFilePath)
			        throws IOException {
			    Workbook workbook = null;
			 
			    if (excelFilePath.endsWith("xlsx")) {
			        workbook = new XSSFWorkbook();
			    } else if (excelFilePath.endsWith("xls")) {
			        workbook = new HSSFWorkbook();
			    } else {
			        throw new IllegalArgumentException("The specified file is not Excel file");
			    }
			 
			    return workbook;
			}*/

			
			/*private void scanDownloadedFile(String merchantDataLine,List<ReconReq> list,FileOutputStream fop) throws IOException, SQLException
			{
						if(merchantDataLine.trim().isEmpty())
								return;
				
							String[] allMerchPhone=null; 
							//validationFailedIndex.clear();
							String[] data=merchantDataLine.trim().split("\\|");
				            logger.info(data.toString());
				            logger.debug("length "+data.length);

				    		for (int i = 0; i < data.length; i++) {
				    			logger.info("field " + i + " :" + data[i]);
				    		}
	                        int validLengthCount=11;
	                        if(data.length == validLengthCount){
	                        	String validationString=validate(data);
	                        	if(!validationString.isEmpty()){
	                				logger.error("Error: Validation failed for fields " + validationString);
	                				//failed_records ++;
	                				writeToFile(merchantDataLine+"|Erroneous validations ="+validationString,fop);
	                				return;
	                			}else{
	                			
	                				list.add(new ReconReq(data[0], data[1], data[2],
	                						data[3], data[4], data[5], data[6], 
	                						data[7], data[8], data[9], data[10]));
	                				
	                			}
	                         }else{
	                 			//failed_records ++;
	                			writeToFile(merchantDataLine+"|Error=Length Incorrect",fop);
	                			return;
	                		}
							try{
							   logger.info("List size "+list.size()+"");
							   // isTransactionSucessful = true;
							}catch(Exception e)
							{
								//isTransactionSucessful=false;
								//failed_records ++;
								writeToFile(merchantDataLine+"|Exception="+e.getMessage(),fop);
								e.printStackTrace();
								logger.error(e.getMessage(),e);
								logger.error(e.getMessage(),e);
								//con.rollback();
								logger.info("Rollback for this transaction has been done.");
							}finally{
								//DatabaseConnection.close(null, statement, null);
							}

				}
			
				public  void writeToFile(String line,FileOutputStream fop) throws IOException {
					String newLine = System.getProperty("line.separator");
					line = line + newLine;
					// get the content in bytes
					byte[] contentInBytes = line.getBytes();
					fop.write(contentInBytes );
				}*/
			
			/*Iterator<Cell> cellIterator = nextRow.cellIterator();
			
			String str ="";
	        while (cellIterator.hasNext()) {
	            Cell nextCell = cellIterator.next();
	            int columnIndex = nextCell.getc getColumnIndex();
	            switch (columnIndex) {
	            case 0:
	                str =(String)getCellValue(nextCell);
	                if(str.isEmpty())
	                	buffer.append(" Transaction Id is empty or length is greater than 35,");
	                else
	                	reconReq.setTRANSACTION_ID(str);
	                break;
	            case 1:
	            	str =(String)getCellValue(nextCell);
	                if(str.isEmpty())
	                	buffer.append(" RRN is empty or length is greater than 35,");
	                else
	                	reconReq.setRrn(str);
	            		
	                break;
	            case 2:
	            	str =(String)getCellValue(nextCell);
	                if(str.isEmpty())
	                	buffer.append(" Transaction Id is empty or length is greater than 35,");
	                else
	                	reconReq.setTRN_STATUS(str);
	            	 	
	                break;
	            case 3:
	            	str =(String)getCellValue(nextCell);
	                if(str.isEmpty())
	                	buffer.append(" Address of payer is empty or length is greater than 35,");
	                else
	                	reconReq.setPayerAddr(str);
	                break;
	            case 4:
	            	str =(String)getCellValue(nextCell);
	                if(str.isEmpty())
	                	buffer.append(" Address of payee is empty or length is greater than 35,");
	                else
	                	reconReq.setPayeeAddr(str);
	                break;
	            case 5:
	            	str =(String)getCellValue(nextCell);
	                if(str.isEmpty())
	                	buffer.append(" IFSC of payer is empty or length is greater than 35,");
	                else
	                	reconReq.setPAYER_IFSC(str);
	                break;
	            case 6:
	            	str =(String)getCellValue(nextCell);
	                if(str.isEmpty())
	                	buffer.append(" IFSC of Payee is empty or length is greater than 35,");
	                else
	                	reconReq.setPAYEE_IFSC(str);
	                break; 
	            case 7:
		            str =(String)getCellValue(nextCell);
	                if(str.isEmpty())
	                	buffer.append(" Customer account from amount debited is empty or length is greater than 35,");
	                else
	                	reconReq.setFROM_ACCOUNT(str);
			                break;
	             case 8:
	            	 str =(String)getCellValue(nextCell);
		                if(str.isEmpty())
		                	buffer.append(" Merchant account, which credited is empty or length is greater than 35,");
		                else
		                	reconReq.setTO_ACCOUNT(str);
		             break;
		         case 9:
		        	 str =(String)getCellValue(nextCell);
		                if(str.isEmpty())
		                	buffer.append(" Transaction date time is empty or length is greater than 35,");
		                else
		                	reconReq.setTRN_DATE_TIME(str);
				     break;
		         case 10:
		        	 str =(String)getCellValue(nextCell);
		                if(str.isEmpty())
		                	buffer.append(" Transaction Amount is empty or length is greater than 35,");
		                else
		                	reconReq.setMCC(str);
				     break;    
	            }
	         }*/
			/*private String validate(String [] reqObject){

				StringBuffer buffer=new StringBuffer();
				if(reqObject[0].equals("") || reqObject[0].length() != 35)
					buffer.append(" Transaction Id is empty or length is greater than 35,");
				if(reqObject[1].equals("") || reqObject[1].length() != 12)
					buffer.append(" RRN is empty or length is greater than 35,");
				if(reqObject[2].equals("") || reqObject[2].length() > 10)
					buffer.append(" Transaction status is empty or length is greater than 35,");
				if(reqObject[3].equals("") || reqObject[3].length() > 50)
					buffer.append(" Address of payer is empty or length is greater than 35,");
				if(reqObject[4].equals("") || reqObject[4].length() > 50)
					buffer.append(" Address of payee is empty or length is greater than 35,");
				if(reqObject[5].equals("") || reqObject[5].length() > 16)
					buffer.append(" IFSC of payer is empty or length is greater than 35,");
				if(reqObject[6].equals("") || reqObject[6].length() > 16)
					buffer.append(" IFSC of Payee is empty or length is greater than 35,");
				if(reqObject[7].equals("") || reqObject[7].length() > 26)
					buffer.append(" Customer account from amount debited is empty or length is greater than 35,");
				if(reqObject[8].equals("") || reqObject[8].length() > 26)
					buffer.append(" Merchant account, which credited is empty or length is greater than 35,");
				//DAte validation is pending
				if(reqObject[9].equals(""))
					buffer.append(" Transaction Id is empty or length is greater than 35,");
				if(reqObject[10].equals("") || reqObject[10].length() != 4)
					buffer.append(" Transaction Amount is empty or length is greater than 35,");

				return buffer.toString();
				
			}*/

		   @RequestMapping(value = "/bharatqrDemo", method = RequestMethod.GET)
		   @ResponseBody
		   public String createQRCode() throws Exception {

			   //System.out.println("BharatQR Request..."+requestQR);
			  // JSONObject requestJson = new JSONObject(requestQR);
			   //JSONObject dataRequestJson = requestJson.getJSONObject("data");https://:8443/bharatqr/qr/getQR
			   String url = "https://te1.in.worldline.com:8443/bharatqr/qr/getQR";
			   System.setProperty("https.protocols","TLSv1.2");
			   URL obj = new URL(url);
			   HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	
			   // optional default is GET
			   con.setRequestMethod("POST");
	
			   //add request header
			   con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			   //con.setRequestProperty("Content-Length", Integer.toString(requestQR.getBytes().length));
			        //  con.setUseCaches(false);
			        //  con.setDoOutput(true);
					//	DataOutputStream wr = new DataOutputStream(con.getOutputStream());
					//	wr.writeBytes("");
					//	wr.flush();
					//	wr.close();
					
					
					//print_https_cert(con);
				 int responseCode = con.getResponseCode();
					
				StringBuilder response = null;
				String resp = null;
				if(con!=null){
					BufferedReader br =
							new BufferedReader(
									new InputStreamReader(con.getInputStream()));
					response = new StringBuilder();
					String input;
					while ((input = br.readLine()) != null){
						response.append(input);
					}
					br.close();
					resp = response.toString();
					logger.info("Response is :"+resp);
					//System.out.println("Response is :"+resp);
				}

			          
			 return "rit bhai";
		          
		  }
		   
		   /**
            * Method used to validate records present in CSV SBI file
            *       
            * @param reconReq
            * @param nextRow
            */
			private void validateRowForCsv(ReconReq reconReq,String[] nextRow){
				StringBuffer buffer=new StringBuffer();
				String str ="";
				
				try{
					//Merchant Id
					str = nextRow[1];
	                if(str.isEmpty() || str.length() != 16)
	                	buffer.append(" Merchant Id is empty or length should be 16 characters,");
	                else
	                	reconReq.setMerchantId(str);
				}catch(Exception e){
					 buffer.append(" Merchant Id is "+e.getMessage());
				}
				try{
					//Transaction Reference Number
					str = nextRow[9];
	                if(str.isEmpty() || str.length() != 9)
	                	buffer.append(" Transaction Reference Number is empty or length should be 35 characters,");
	                else
	                	reconReq.setTransRefNo(str);
				}catch(Exception e){
					 buffer.append(" Transaction Reference Number is "+e.getMessage());
				}
			
				/*try{
	            	str = nextRow[21];
	            	logger.info("RRN :::"+nextRow[21]);
	                if(str.isEmpty() || str.length() != 12)
	                	buffer.append(" RRN is empty or length should be 12 characters,");
	                else
	                	reconReq.setRrn(str);
				}catch(Exception e){
					 buffer.append(" RRN is "+e.getMessage());
				}*/
				try{	
					//Transaction status
	                str = nextRow[5];
	                if(str.isEmpty() ||  str.length() >= 10)
	                	buffer.append(" Transaction status is empty or length is greater than 10,");
	                else
	                	reconReq.setTransStatus(str);
	            }catch(Exception e){
					 buffer.append(" Transaction status is "+e.getMessage());
				}
           	 	
            try{
            		//Address of payer
	            	str = nextRow[29];
	                if(str.isEmpty() ||  str.length() >= 50 )
	                	buffer.append(" Address of payer is empty or length is greater than 50,");
	                else
	                	reconReq.setPayerAddr(str);
	            }catch(Exception e){
					 buffer.append(" Address of paye is "+e.getMessage());
				}
       
              try{
            	  	//Address of payee
	            	str = nextRow[8];
	                if(str.isEmpty() ||  str.length() >= 50)
	                	buffer.append(" Address of payee is empty or length is greater than 50,");
	                else
	                	reconReq.setPayeeAddr(str);
	            }catch(Exception e){
					 buffer.append(" Address of payee is "+e.getMessage());
				}
              try{
            	   //Transaction date
                   str = nextRow[19];
	                if(str.isEmpty())
	                	buffer.append(" Transaction date time is empty or format is wrong,");
	                else
	                	reconReq.setTransDateTime(str);
	            }catch(Exception e){
					 buffer.append(" Transaction date time is "+e.getMessage());
				}
			   
              try{
	            	 //MCC
	                 str = nextRow[4];
		                if(str.isEmpty() ||  str.length() != 4)
		                	buffer.append(" MCC is empty or length is greater than 35,");
		                else
		                	reconReq.setMCC(str);
	            }catch(Exception e){
					 buffer.append(" MCC is "+e.getMessage());
				}
              try{    
            	  	//Amount
	           	    double amount = Double.parseDouble(nextRow[15]) ;
		                if(amount == 0.0 || str.length() >= 12)
		                	buffer.append(" Amount is empty or length is greater than 35,");
		                else
		                	reconReq.setAmount(amount);
	            }catch(Exception e){
					 buffer.append(" Amount is "+e.getMessage());
				}                       
				
              try{   
            	  	//Gateway Transaction Id
	                str =(String)nextRow[10];
	                if(str.isEmpty() || str.length() != 35)
	                	buffer.append(" Gateway Transaction Id is empty or length is greater than 35,");
	                else
	                	reconReq.setGatewayTransId(str);
	            }catch(Exception e){
					 buffer.append(" Gateway Transaction Id is "+e.getMessage());
				}
              
              try{   
            	  	//Customer Reference Number
	                str =(String)nextRow[22];
	                reconReq.setCustomerRefNo(str);
	            }catch(Exception e){
					 buffer.append("Customer Reference Number is "+e.getMessage());
				}
              try{   
            	  	//Order Number
	                str =(String)nextRow[21];
	                reconReq.setOrderNo(str);
	            }catch(Exception e){
					 buffer.append("Order Number is "+e.getMessage());
				}
              try{   
            	  	//Refund Remark
	                str = "Customer Refund";
	                reconReq.setRefundRemark(str);
	            }catch(Exception e){
					 buffer.append(" Refund Remark is "+e.getMessage());
				}
             
		        if(!buffer.toString().isEmpty())
	            	reconReq.setError(buffer.toString());
			}
}
