package com.wl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.wl.util.config.ApplicationConfig;
import com.wl.util.config.BankConfig;

public class ReconUtils
{
  private static final Logger logger = LoggerFactory.getLogger(ReconUtils.class);
  private static HashMap<String, String> map = new HashMap();
  
  static
  {
    map.put("00011", "IDBI");
    map.put("00004", "CBI");
    map.put("00031", "AXIS");
    map.put("00006", "BOI");
    map.put("00075", "SBI");
    map.put("00045", "YES");
    map.put("00050", "UBI");
    map.put("00079", "KOTAK");
    map.put("00058", "INDUS");
    map.put("00003", "CANARA");
    map.put("00051", "PNB");
    map.put("00074", "SIB");
    map.put("00049", "OBC");
    map.put("00041", "DBC");
    map.put("00001", "BOB");
  }
  
  public static String getBankName(String bankCode)
  {
    return (String)map.get(bankCode);
  }
  
  public static String genReconFromDate(String jobParam)
    throws Exception
  {
    int num = Integer.parseInt(jobParam) + 1;
    Date d = new Date();
    Calendar cal = Calendar.getInstance();
    
    cal.add(5, -num);
    Date dateBefore30Days = cal.getTime();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String fDate = sdf.format(dateBefore30Days).toString() + " 23:45:00";
    
    return fDate;
  }
  
  public static String genReconToDate(String jobParam)
    throws Exception
  {
    int num = Integer.parseInt(jobParam);
    Date d = new Date();
    Calendar cal = Calendar.getInstance();
    
    cal.add(5, -num);
    Date dateBefore30Days = cal.getTime();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String tDate = sdf.format(dateBefore30Days).toString() + " 23:45:00";
    
    return tDate;
  }
  
  public static String getDateInddMMyy(String todate)
    throws ParseException
  {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date d1 = dateFormat.parse(todate);
    SimpleDateFormat sdf2 = new SimpleDateFormat("ddMMyy");
    
    return sdf2.format(d1).toString();
  }
  
  public static boolean uploadFileToServer(String... args)
  {
    com.jcraft.jsch.Session session = null;
    Channel channel = null;
    ChannelSftp channelSftp = null;
    try
    {
      String fileName = args[0];
      String localFileLoc = args[1];
      String remoteFileLoc = args[2];
      String serverIp = args[3];
      String user_name = args[4];
      String password = args[5];
      int port = Integer.parseInt(args[6]);
      
      JSch jsch = new JSch();
      session = jsch.getSession(user_name, serverIp, port);
      session.setPassword(password);
      Properties config = new Properties();
      config.put("StrictHostKeyChecking", "no");
      session.setConfig(config);
      session.connect();
      channel = session.openChannel("sftp");
      channel.connect();
      channelSftp = (ChannelSftp)channel;
      
      channelSftp.cd(remoteFileLoc);
      
      File f = new File(localFileLoc + "/" + fileName);
      channelSftp.put(new FileInputStream(f), f.getName());
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      
      return false;
    }
    return true;
  }
  
  public static void send(String bankCode, String from, String to, String cc, String subject, String body, HashMap<String, String> attachment)
  {
    final String user = BankConfig.get(bankCode, "mailUser");
    final String password = BankConfig.get(bankCode, "mailPass");
    
    Properties props = new Properties();
    props.put("mail.smtp.host", BankConfig.get(bankCode, "mailHost"));
    props.put("mail.smtp.port", BankConfig.get(bankCode, "mailPort"));
    props.put("mail.smtp.auth", "true");
    logger.info("user " + user + "   password  " + password + "  host " + BankConfig.get(bankCode, "mailHost") + 
      "  port " + BankConfig.get(bankCode, "mailPort") + " senderID" + from);
    Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(user,password);
		}
	});
    try
    {
      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(from));
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
      if ((cc != null) && (!cc.isEmpty()))
      {
        message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
        message.setSubject(subject);
      }
      BodyPart messageBodyPart = new MimeBodyPart();
      
      messageBodyPart.setContent(body, "text/html; charset=utf-8");
      
      Multipart multipart = new MimeMultipart();
      
      multipart.addBodyPart(messageBodyPart);
      if (attachment != null) {
        for (Map.Entry<String, String> entry : attachment.entrySet())
        {
          messageBodyPart = new MimeBodyPart();
          DataSource source = new FileDataSource((String)entry.getValue());
          messageBodyPart.setDataHandler(new DataHandler(source));
          messageBodyPart.setFileName((String)entry.getKey());
          multipart.addBodyPart(messageBodyPart);
        }
      }
      message.setContent(multipart);
      
      Transport.send(message);
      
      logger.debug("Email sent successfully...");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  /**
   * 
   * Method used for download files from SFTP server
   * 
   * @param bankCode   bank specific code
   * @param prefix     File starting name
   * @param extension  Extension of file which will download from SFTP 
   * @return
   * @throws IOException
   */
  public static boolean downLoadFile(String bankCode,String prefix,String extension) throws IOException{
		
		JSch jsch = new JSch();
		com.jcraft.jsch.Session session = null;
		boolean isFileDownloaded=false;
		String filePrefix=null;
		String patternNameRegex=null;
		ChannelSftp sftpChannel=null;
		try {
			String curDate = new SimpleDateFormat("ddMMyyyy").format(new Date());
			if("00031".equals(bankCode))
			{
				if(ReconUtils.getBankName(bankCode).equalsIgnoreCase("COMMON")){
					filePrefix=prefix+"_*_"+curDate;
		            patternNameRegex = prefix+"_"+"\\w{5}\\_"+curDate+"_\\d+"+extension;
				}else{
					filePrefix=prefix+"_"+bankCode+"_"+curDate;
					patternNameRegex = filePrefix+"_\\d+"+extension;
				}
			}
			else if("00075".equals(bankCode))
			{
				filePrefix=prefix+"_"+curDate;
		        patternNameRegex = prefix+"_"+curDate+"_\\d+"+extension;
			}
			// String fileNameRegex = filePrefix+"_"+"\\w{6}\\_\\d+"+".txt";
			Pattern pattern = Pattern.compile(patternNameRegex);
			
			String search_file_name= filePrefix+"_*"+extension;
			logger.debug(search_file_name);
				
			String SFTPHost = BankConfig.get(bankCode, "ReconSftpIp");//"172.16.26.116";//BankConfig.get(bankCode, key)// "10.10.11.64";
			int SFTPPort = Integer.parseInt(BankConfig.get(bankCode, "ReconSftpPort"));//22;//Integer.parseInt(Parameters.para.get("SFTPPORT").toString());
			String SFTPUser = BankConfig.get(bankCode, "ReconSftpUserName");//"appladm";//Parameters.para.get("SFTPUSER"); // "mxpv5";
			String SFTPPass =  BankConfig.get(bankCode, "ReconSftpPass");//"jan@2018";//Parameters.para.get("SFTPPASSWORD"); //"d3vel0pment";
		
			//For Production & SIT
			//String SFTPWorkingDir = BankConfig.get(bankCode, "ReconSftpPass")+File.separator+ReconUtils.getBankName(bankCode)+File.separator;//"/home/appladm/SFTP/"+fromApp+"/";//Parameters.para.get("SFTPWORKINGDIR"); //  "/mxpv5/data/edp";
			//FOr Local Test
			String SFTPWorkingDir = BankConfig.get(bankCode, "ReconSftpPath")+"/"+ReconUtils.getBankName(bankCode)+"/";
			logger.info(SFTPUser + " " + SFTPHost + " " + SFTPPass + " " + SFTPPort); 
			
			session = jsch.getSession(SFTPUser, SFTPHost, SFTPPort);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(SFTPPass);
			logger.info("conneting to server.....");
			session.connect();
			logger.info("connedted to server");

			Channel channel = (Channel) session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;
			logger.info("channel connected");
			String fileName=null;
			logger.info("Find the files for current date in SFTP location "+SFTPWorkingDir+search_file_name);
			Vector<ChannelSftp.LsEntry> list = sftpChannel.ls(SFTPWorkingDir+search_file_name);
			logger.info(list.size() + "");
			if (list.size() != 0) {
					for(ChannelSftp.LsEntry entry : list) {
						fileName=entry.getFilename();
						Matcher m = pattern.matcher(fileName);
						if (m.matches()) {
							logger.debug("File getting downloaded.... "+fileName);
							String srcPath = SFTPWorkingDir+fileName;
							logger.info(srcPath);
							//Production & SIT release
							String destinationFile = BankConfig.get(bankCode, "SFTPDOWNLOADFOLDER")+ReconUtils.getBankName(bankCode)+File.separator+"Input"+File.separator+entry.getFilename();
							//Local use
							//String destinationFile = BankConfig.get(bankCode, "SFTPDOWNLOADFOLDER")+ReconUtils.getBankName(bankCode)+"\\Input\\"+entry.getFilename();
							logger.info("destinationFile "+destinationFile + "Source "+srcPath);
							sftpChannel.get(srcPath, destinationFile);
							//currentSequence.add(Integer.valueOf(fileName.substring(fileName.lastIndexOf('_')+1,fileName.length()-4)));
							sftpChannel.rm(srcPath);
							isFileDownloaded=true;
						}
					}
			} else {
				ReconUtils.send(bankCode, ApplicationConfig.get("ReconMailFrom"), ApplicationConfig.get("ReconMailTo"), 
			    		   ApplicationConfig.get("ReconMailCC"), "No File found", "Hi SPOC ,<br/>No files found in SFTP location for Download.", null);
				isFileDownloaded=false;
			}
			
		} catch (JSchException e) {
				//isFileDownloaded=false;
				e.printStackTrace();  
				logger.error(e.getMessage());
		} catch (SftpException e) {
				//isFileDownloaded=false;
				e.printStackTrace();
				logger.error(e.getMessage());
		}finally{
			if(sftpChannel !=null)
				sftpChannel.exit();
			if(session !=null)
			     session.disconnect();
			logger.info("session closed");
		}
		return isFileDownloaded;
		
	}

public static void moveFile(Path src,Path destination){
		try {
			Files.move(src, destination, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Unable to move file"+e.getMessage());
		}
	}

/**
 * Moving file from src location to destination location
 * 
 * @param fileName --> moving file name
 * @param srcFileLoc
 * @param destFileLoc
 * @return
 * @throws IOException
 */
public static int moveFileSrctoDestLocation(Path fileName, String srcFileLoc, String destFileLoc) throws IOException
	{
	    logger.debug("Moving File name :"+fileName.getFileName());
		Path srcPath=FileSystems.getDefault().getPath(srcFileLoc+fileName);
		Path desPath=FileSystems.getDefault().getPath(destFileLoc+fileName);
		Files.move(srcPath,desPath,StandardCopyOption.REPLACE_EXISTING);
		return 0;
	}
  
  public static void main(String[] args)
    throws Exception
  {
    boolean flag = uploadFileToServer(new String[] { "UBIRecon150318.txt", "D:/", "pranav/recon", "172.16.26.116", "appladm", "jan@2018", "22" });
    System.out.println(flag);
  }
}
