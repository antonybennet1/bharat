package com.wl.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UtilityHelper {

	private static final Logger logger = LoggerFactory
			.getLogger(UtilityHelper.class);
	
	private UtilityHelper(){}

	private static final ThreadLocal<SimpleDateFormat> sdfDate = new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("ddMMyyyy");
		}
	};

	private static final ThreadLocal<SimpleDateFormat> sdfHour = new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("HHmmss");
		}
	};
	
	private static final ThreadLocal<SimpleDateFormat> sdfMMYY = new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("MMYY");
		}
	};

	private static final ThreadLocal<SimpleDateFormat> sdfMMddyyyy = new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("MMddyyyy");
		}
	};
	
	public static String dateInMMddYYYY(){
		Date d = new Date();
		String date = sdfMMddyyyy.get().format(d);
		return date;
	}
	
	public static String dateInddMMyyyy(){
		Date d = new Date();
		String date = sdfDate.get().format(d);
		return date;
	}

	public static String hourInHHmmss(){
		Date d = new Date();
		String date = sdfHour.get().format(d);
		return date;
	}

	public static String MMYY(){
		Date d = new Date();
		String date = sdfMMYY.get().format(d);
		return date;
	}
	/**
	 * Get the payment dto list fetched from DB
	 * @author ashish.bhavsar
	 * @param rows
	 * @return List<PaymentDTO>
	 */
	
	public static String leftPad(String str, int length){

		if(str.length() == length)
			return str;
		if(length <= 0){
			return str;
		}
		if(str.length() < length){
			StringBuilder sb = new StringBuilder();
			for(int i=0 ; i < (length - str.length()) ; i++){
				sb.append("0");
			}
			sb.append(str);
			return sb.toString();
		}
		else
		{
			return str;
		}

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

	static public String byteToHex(byte b) {
		// Returns hex String representation of byte b
		String hexDigit[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
		 return  (hexDigit[(b >>> 4) & 0x0f] + hexDigit[b & 0x0f]);
	}
	
	public static String unHex(String arg) {        

		String str = "";
		for(int i=0;i<arg.length();i+=2)
		{
			String s = arg.substring(i, (i + 2));
			int decimal = Integer.parseInt(s, 16);
			str = str + (char) decimal;
		}       
		return str;
	}
	
	public static int getLength(byte[] len) {        
		String lenStr = byteArrayToHexString(len);   
		int length = Integer.parseInt(lenStr, 16);
		return length;
	}
	
	public static int getLength(String lenStr) {        
		int length = Integer.parseInt(lenStr, 16);
		return length;
	}

	public static java.sql.Date getCurrentDate() {
		java.util.Date today = new java.util.Date();
		return new java.sql.Date(today.getTime());
	}

	/**
	 * Applies the specified mask to the card number.
	 *
	 * @param cardNumber The card number in plain format
	 * @param mask The number mask pattern. Use # to include a digit from the
	 * card number at that position, use x to skip the digit at that position
	 *
	 * @return The masked card number
	 */
	public static String maskCardNumber(String cardNumber, String mask) {

	    // format the number
	    int index = 0;
	    StringBuilder maskedNumber = new StringBuilder();
	    for (int i = 0; i < mask.length(); i++) {
	        char c = mask.charAt(i);
	        if (c == '#') {
	            maskedNumber.append(cardNumber.charAt(index));
	            index++;
	        } else if (c == 'x') {
	            maskedNumber.append(c);
	            index++;
	        } else {
	            maskedNumber.append(c);
	        }
	    }

	    // return the masked number
	    return maskedNumber.toString();
	}
	public static Map<String, String> parseTLV(String tlv) {
	    /*if (tlv == null || tlv.length()%2!=0) {
	        throw new RuntimeException("Invalid tlv, null or odd length");
	    }*/
	    HashMap<String, String> hashMap = new HashMap<String, String>();
	    for (int i=0; i<tlv.length();) {
	        try {
	            String key = tlv.substring(i, i=i+3);
	            logger.debug(key);
	            
	            /*if ((Integer.parseInt(key,16) & 0x1F) == 0x1F) {
	                // extra byte for TAG field
	                key += tlv.substring(i, i=i+2);
	            }*/
	            String len = tlv.substring(i, i=i+3);
	            //int length = Integer.parseInt(len,16);
	            int length = Integer.parseInt(len);
	            /*if (length > 127) {
	                // more than 1 byte for lenth
	                int bytesLength = length-128;
	                len = tlv.substring(i, i=i+(bytesLength*2));
	                length = Integer.parseInt(len,16);
	            }
	            length*=2;*/

	            String value = tlv.substring(i, i=i+length);
	            //logger.debug(key+" = "+value);
	            hashMap.put(key, value);
	        } catch (NumberFormatException e) {
	            throw new RuntimeException("Error parsing number",e);
	        } catch (IndexOutOfBoundsException e) {
	            throw new RuntimeException("Error processing field",e);
	        }
	    }

	    return hashMap;
	}
	
	
}
