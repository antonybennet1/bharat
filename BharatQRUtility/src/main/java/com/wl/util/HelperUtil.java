package com.wl.util;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelperUtil {
	static int count=0;
	  //static int randomNum; 
	private static Logger logger = LoggerFactory.getLogger(HelperUtil.class);

	public static String base64(byte[] bytes) {
		return Base64.encodeBase64String(bytes);
	}

	public static byte[] base64(String str) {
		return Base64.decodeBase64(str);
	}

	public static String hex(byte[] bytes) {
		return Hex.encodeHexString(bytes);
	}

	public static byte[] hex(String str) throws DecoderException {
			return Hex.decodeHex(str.toCharArray());
	}
	
	public static String random(int length) {
		byte[] salt = new byte[length];
		new SecureRandom().nextBytes(salt);
		return HelperUtil.hex(salt);
	}
	
	public static String getRequestId()
	{
		Calendar calendar = Calendar.getInstance();
		int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR); 
		int year = calendar.get(Calendar.YEAR);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		int millis = calendar.get(Calendar.MILLISECOND);
		Random r = new Random();
		int random  = r.nextInt();
		random = Math.abs(random%1000);
		StringBuilder sb  = new StringBuilder();
		sb.append(year%10); //1
		sb.append(String.format("%03d",dayOfYear)); // 3
		sb.append(String.format("%02d",hour));//2
		sb.append(String.format("%02d",minute));//2
		sb.append(String.format("%02d",second));//2
		sb.append(String.format("%03d",millis));//3
		sb.append(String.format("%03d",random));//3
		return sb.toString();
	}
	
	public static String byteArrayToHexString(byte[] byteArray){
		if(byteArray == null || byteArray.length == 0)
        {
            return ("");
        }
		StringBuilder hexString = new StringBuilder(byteArray.length * 2);

		for(int i = 0; i < byteArray.length; i++){
			hexString.append(byteToHex(byteArray[i]));
		}

		return (hexString.toString());
	}

	private static String byteToHex(byte b) {
		// Returns hex String representation of byte b
		String hexDigit[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
		 return  (hexDigit[(b >>> 4) & 0x0f] + hexDigit[b & 0x0f]);
	}
	
	/**
	 * generates unique reference number
	 * @return String which is unique reference number based on timestamp and random
	 */
	public static String getReferenceNumber()
	{
		Calendar calendar = Calendar.getInstance();
		int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR); 
		int year = calendar.get(Calendar.YEAR);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		int millis = calendar.get(Calendar.MILLISECOND);
		Random r = new Random();
		int random  = r.nextInt();
		random = Math.abs(random%100000);
		StringBuilder sb  = new StringBuilder();
		sb.append(year%10); //1
		sb.append(String.format("%03d",dayOfYear)); // 3
		sb.append(String.format("%02d",hour));//2
		sb.append(String.format("%02d",minute));//2
		sb.append(String.format("%02d",second));//2
		sb.append(String.format("%03d",millis));//3
		sb.append(String.format("%05d",random));//5
		return sb.toString();
	}
	
	/*public static int getCounterList(){
		 int  size=999;
		 
		ArrayList<Integer> al = new ArrayList<>();
		if(al.isEmpty()){
			for (int i = 0; i < size; i++) {
				al.add(i);
			}	
		}else{
			Random random = new Random(size);
	        while(al.size() > 0) {
	            int index = random.nextInt(al.size());
	            randomNum= al.remove(index);
	        }
		}
		return randomNum;
		
	}*/
	
	public static  String getRRN() 
	{
		count++;
		StringBuilder sb  = new StringBuilder();
		try {
			Calendar calendar = Calendar.getInstance();
			int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR); 
			int year = calendar.get(Calendar.YEAR);
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			int second = calendar.get(Calendar.SECOND);
			int millis = calendar.get(Calendar.MILLISECOND);
				int totalSeconds = 	( hour * 60 * 60 ) + ( minute * 60 ) + second; 
				sb.append(year%10); //1
				sb.append(String.format("%03d",dayOfYear)); // 3
				sb.append(String.format("%05d",totalSeconds)); // 5
				sb.append(String.format("%03d",count));//3
				Thread.sleep(1);
				if(count == 999){
					count=0;
				}
		} catch (Exception e) {
			logger.debug("Errro Occures during RRN generation :"+ e.getMessage());
		}
		return sb.toString();
	}
	
	public static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	/**
	 * Generates randomString of specified length
	 * @param len the length of random string that needs to be generated
	 * @return random string which can be used for password or some other purpose. 
	 */
	public static String randomString(int len) {
		Random rnd = new Random();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}
	
	/**
	 * This function converts data in particular format to SQL timstamp
	 * @param format format like yyyymmddhhmmss
	 * @param date string with format  yyyymmddhhmmss
	 * @return Timestamp sql Timestamp
	 */
	public static Timestamp parseTimestamp(String date,String format)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date d=null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			logger.error(date+"|Error while parsing date:"+e.getMessage());
			return null;
		}
		Timestamp timeStamp = new Timestamp(d.getTime());
		return timeStamp;
	}
	
	/**
	 * This function converts SQL timstamp to  yyyyMMddHHmmss string
	 * @param Timestamp sql Timestamp 
	 * @return string with format  yyyyMMddHHmmss. Returns null if input is null
	 */
	public static String toString(Timestamp date,String format)
	{
		if(date!=null)
		{
			Date d = new Date(date.getTime());
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			String str  = sdf.format(d);
			return str;
		}
		else
			return null;
	}
	
	public static void main(String[] args) throws Exception {
		//HashSet<String> set = new HashSet<String>();
		ArrayList<String> al = new ArrayList<>();
		int dup=0;
		/*for(int i=1;i<=100000;i++)
		{*/
			/*String s = "if(m.getField("+i+")!=null && !m.getField("+i+").toString().isEmpty())"+
					"\n{"+
					"\n\tlogger.info(\"Field 0"+i+" :[\"+m.getField("+i+") +\"]\");"+
					"\n}";
			System.out.println(s);*/
			// synchronized  (HelperUtil.class) {
			
			
				/*String str = getRRN();
				if(al.contains(str)){
					System.out.println("Duplicate rrn" + str);
					//System.out.println("List : " + al);
					dup++;
					//return;
				}else{
					al.add(str);
					System.out.println("Added :"+i+"|" + str);
				}*/
				
				//System.out.println(i+"|"+str);
				//Thread.sleep(1);
				
				
			// }
		/*	try {
				//Thread.sleep(05L);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		//}
			int size = 999;
	        ArrayList<Integer> list = new ArrayList<Integer>(size);
	        for(int i = 1; i <= size; i++) {
	            list.add(i);
	        }

	        Random rand = new Random();
	        int j=0;
	        while(list.size() > 0) {
	            int index = rand.nextInt(list.size());
	         //   System.out.println("j --> " + j++ + "-->" + list.remove(index));
	            System.out.println(list.remove(index));
	        }
			
	        for(int i=1;i<=100000;i++){
	        	String rrn=getRRN();
	        	System.out.println(rrn);
	        }
			
		System.out.println("dup RRN : " +dup);*/
		//System.out.println(al);
		//System.out.println(getReferenceNumberForPSTN("4547191000859487"));
		
		
		System.out.println(isIntegerRegex("12456"));
		
	}
    /**
     * 
     *   TRID   -->  45471910008594878141153926
        
          4547191000859487     8141                 15    39   26
          MPAN (14)           (Julian Date)         HH    MM   SS

     * 
     * 
     * @param visaMapn from database
     * @return
     */
	public static String getReferenceNumberForPSTN(String visaMapn) {
		Calendar calendar = Calendar.getInstance();
		String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
		String minute = String.valueOf(calendar.get(Calendar.MINUTE));
		String second = String.valueOf(calendar.get(Calendar.SECOND));
		String day=String.valueOf(calendar.get(Calendar.DAY_OF_YEAR));
		StringBuffer buffer=new StringBuffer();
		buffer.append(visaMapn); //14
		if(day.length() == 1) day ="00"+day;
		else if(day.length() == 2) day="0"+day;
		buffer.append(String.valueOf(calendar.get(Calendar.YEAR)).substring(3)+day);
		if(hour.length() == 1) buffer.append("0"+hour);
		else buffer.append(hour);
		if(minute.length() == 1) buffer.append("0"+minute);
		else buffer.append(minute);
		if(second.length() == 1) buffer.append("0"+second);
		else buffer.append(second);
		return buffer.toString();
	}

	public static boolean allowAlphaNumericRegex(String input){
		String regex = "^[a-zA-Z0-9]+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		return m.matches();
	}
	
	public static boolean isAmountRegex(String input){
		String regex = "^[1-9]\\d*(\\.\\d+)?$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		return m.matches();
	}
	
	public static boolean isIntegerRegex(String input){
		String regex = "^[0-9]+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		return m.matches();
	}
	
	
}
