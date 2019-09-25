package com.wl.recon.service;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wl.recon.dao.ReconDao;
import com.wl.recon.model.FileDataDTO;
import com.wl.recon.util.ReconUtils;
import com.wl.util.config.ApplicationConfig;
import com.wl.util.config.BankConfig;




@Service("reconservice")
public class ReconService {

	@Autowired
	private ReconDao reconDao;

	
	private static final Logger logger = LoggerFactory.getLogger(ReconService.class);
	
	public String generateFile(String bankCode, String jobParm) throws Exception {
		
		FileDataDTO fileDto=null;
		String resp="";
		boolean b = false;
		logger.info("inside the service method" + bankCode); 
		String fromDate = ReconUtils.genReconFromDate(jobParm);
		String toDate = ReconUtils.genReconToDate(jobParm);
		logger.info("fromDate : " + fromDate);
		logger.info("toDate : " + toDate);
		
		List<FileDataDTO> dataList = reconDao.getFileData(bankCode, fromDate, toDate);
		logger.info("Size of list " + dataList.size());

		if(dataList.size() != 0){
			String localpath=ApplicationConfig.get("localReconfilepath")+ReconUtils.getBankName(bankCode)+"\\";
			String bankName = ReconUtils.getBankName(bankCode.trim());
			String ddMMyy=ReconUtils.getDateInddMMyy(toDate);
			String fileName= bankName+"Recon"+ddMMyy+".txt";
			String filepath=localpath+fileName;
			logger.info("localpath ---> " + localpath);
			logger.info("FileName :" + filepath );
			
			b= createPNBCsvFile(dataList,filepath,ddMMyy);
			if(b){
				if(SFTPFileUpload(fileName,localpath,bankCode)){
					resp="Success";
				}else{
					resp="SFTP File Upload Failed";
				}
			}else{
				logger.info("Problem in generating CSV file ");
				resp="Csv generation failed";
			}
		}else{
			ReconUtils.send(bankCode, BankConfig.get(bankCode, "mailFrom"), BankConfig.get(bankCode, "mailTo"),
					BankConfig.get(bankCode, "mailCC"), BankConfig.get(bankCode, "mailSubject"),
					BankConfig.get(bankCode, "mailBody"), null);
			logger.info(" data not found, Send mail ");
			resp="Data not found for the day";
		}
		return resp;
	}

	private boolean SFTPFileUpload(String fileName, String localpath,String bankCode) {
		logger.info("Inside SFTPFileUpload method ");
		String sftpPath=BankConfig.get(bankCode, "ReconSftpPath");
		String sftpHost=BankConfig.get(bankCode, "ReconSftpIp");
		String sftpPort=BankConfig.get(bankCode, "ReconSftpPort");
		String sftpuserName=BankConfig.get(bankCode, "ReconSftpUserName");
		String sftpPass=BankConfig.get(bankCode, "ReconSftpPass");
		
		if(ReconUtils.uploadFileToServer(fileName,localpath,sftpPath,sftpHost,sftpuserName,sftpPass,sftpPort)){
			logger.info("File uploaded successful on sftp");
			return true;
		}
		return false;
	}

	private boolean createPNBCsvFile(List<FileDataDTO> fildeDto , String filepath,String ddMMyy) {
		logger.info("Inside the csv writer method");
		boolean success = false;
		try {
			PrintWriter pw = new PrintWriter(new File(filepath));
			StringBuilder sb = new StringBuilder();
			String csvSeparator = "|";
			sb.append("HDR"+ddMMyy);
			sb.append('\n');
			for (FileDataDTO data : fildeDto) {
				sb.append(data.getMid());
				sb.append(csvSeparator);
				sb.append(data.getTid());
				sb.append(csvSeparator);
				sb.append(data.getRrn());
				sb.append(csvSeparator);
				sb.append(data.getTxnId());
				sb.append(csvSeparator);
				sb.append(data.getTxnDate());
				sb.append(csvSeparator);
				sb.append(data.getBusDate());
				sb.append(csvSeparator);
				sb.append(data.getTxnAmount());
				sb.append(csvSeparator);
				sb.append(data.getSetlAmount());
				sb.append(csvSeparator);
				sb.append(data.getChannelFlag());
				sb.append(csvSeparator);
				sb.append(data.getMcc());
				sb.append(csvSeparator);
				sb.append(data.getRespCode());
				sb.append('\n');
			}
			sb.append("FT"+String.format("%09d", fildeDto.size()));
			pw.write(sb.toString());
			pw.close();
			success = true;
			logger.info("Refund csv file generated successfully");
			return true;
	}catch(Exception e){
		e.printStackTrace();
		}
		return false;
	}
	
}
