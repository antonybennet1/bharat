package com.wl.recon.util;


import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.Map.Entry;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
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
import com.jcraft.jsch.Session;
import com.wl.recon.service.ReconService;
import com.wl.util.UtilityHelper;
import com.wl.util.config.BankConfig;

//import worldline.mvisa.utilities.PropertyFileHandler;



public class ReconUtils {
	private static final Logger logger = LoggerFactory.getLogger(ReconUtils.class);
	
	private static HashMap<String, String> map = new HashMap<>();  
	static{
		map.put("00011","IDBI");
		map.put("00004","CBI");
		map.put("00031","AXIS");
		map.put("00006","BOI");
		map.put("00075","SBI");
		map.put("00045","YES");
		map.put("00050","UBI");
		map.put("00079","KOTAK");
		map.put("00058","INDUS");
		map.put("00003","CANARA");
		map.put("00051","PNB");
		map.put("00074","SIB");
		map.put("00049","OBC");
		map.put("00041","DBC");
		map.put("00001","BOB");
	}
	
	public static String getBankName(String bankCode)
	{
		return map.get(bankCode);
	}
	
	
	public static String genReconFromDate(String jobParam) throws Exception{
		int num = Integer.parseInt(jobParam)+1;
			Date d = new Date();
			Calendar cal = Calendar.getInstance();
			//cal.setTime(d);
			cal.add(Calendar.DATE, -num);
			Date dateBefore30Days = cal.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String fDate =  sdf.format(dateBefore30Days).toString() +" 23:45:00";
			/*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
			Date d1 = dateFormat.parse(fDate);
			
			System.out.println(" TO DATE " + dateFormat.format(d1));*/
		return fDate;
	}
	
	
	
	public static String genReconToDate(String jobParam) throws Exception{
		int num = Integer.parseInt(jobParam);
			Date d = new Date();
			Calendar cal = Calendar.getInstance();
			//cal.setTime(d);
			cal.add(Calendar.DATE, -num);
			Date dateBefore30Days = cal.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String tDate =  sdf.format(dateBefore30Days).toString() +" 23:45:00";
			/*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
			Date d1 = dateFormat.parse(fDate);
			
			System.out.println(" TO DATE " + dateFormat.format(d1));*/
		return tDate;
	}
	
	public static String getDateInddMMyy(String todate) throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date d1 = dateFormat.parse(todate);
		SimpleDateFormat sdf2 = new SimpleDateFormat("ddMMyy");
		//System.out.println(sdf2.format(d1).toString());
		return sdf2.format(d1).toString();
	}
	
	
	
	public static boolean uploadFileToServer(String... args)
	{
		Session     session     = null;
		Channel     channel     = null;
		ChannelSftp channelSftp = null;
		try{
			String user_name,password,serverIp,localFileLoc,remoteFileLoc,fileName;
			int port;
			fileName=args[0];
			localFileLoc=args[1];
			remoteFileLoc=args[2];
			serverIp=args[3];
			user_name=args[4];
			password=args[5];
			port=Integer.parseInt(args[6]);

			JSch jsch = new JSch();
			session = jsch.getSession(user_name,serverIp,port);
			session.setPassword(password);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			channel = (Channel)session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp)channel;
			////Changing the directory to the remote directory where the file needs to be uploadd
			channelSftp.cd(remoteFileLoc);

			File f = new File(localFileLoc+"/"+fileName);
			channelSftp.put(new FileInputStream(f), f.getName());

		}catch(Exception ex){
			ex.printStackTrace();
			//logger.error(ex.getMessage());
			return false;
		}
		return true;
	}
	
	public static void send(String bankCode ,String from, String to, String cc, String subject, String body, HashMap<String, String> attachment) {
		final String user = BankConfig.get(bankCode, "mailUser");
		final String password = BankConfig.get(bankCode, "mailPass");

		//Get the session object
		Properties props = new Properties();
		props.put("mail.smtp.host",BankConfig.get(bankCode, "mailHost"));
		props.put("mail.smtp.port",BankConfig.get(bankCode, "mailPort"));
		props.put("mail.smtp.auth", "true");
		logger.info("user " + user + "   password  " + password + "  host " + BankConfig.get(bankCode, "mailHost")
				+ "  port " + BankConfig.get(bankCode, "mailPort") + " senderID" + from);
		javax.mail.Session session = javax.mail.Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user,password);
			}
		});

		//Compose the message
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
			if(cc!=null && !cc.isEmpty())
			{
				message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
				message.setSubject(subject);
			}

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Now set the actual message
			//messageBodyPart.setText(body);
			messageBodyPart.setContent(body, "text/html; charset=utf-8");

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			if (attachment != null) {
				for (Entry<String, String> entry : attachment.entrySet()){
					messageBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(entry.getValue());
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(entry.getKey());
					multipart.addBodyPart(messageBodyPart);
				}
			}

			// Send the complete message parts
			message.setContent(multipart);

			//send the message
			Transport.send(message);

			logger.debug("Email sent successfully...");
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		/*System.out.println(genReconFromDate("0"));
		System.out.println(genReconToDate("0"));
		
		String tDate="2018-03-14 23:45:00";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date d1 = dateFormat.parse(tDate);
		SimpleDateFormat sdf2 = new SimpleDateFormat("ddMMyy");
		System.out.println(sdf2.format(d1));*/
		
		//System.out.println(UtilityHelper.dateInddMMyyyy());
		
		
		boolean flag =uploadFileToServer("UBIRecon150318.txt","D:/","pranav/recon","172.16.26.116","appladm","jan@2018","22");
		System.out.println(flag);
		
	}
}
