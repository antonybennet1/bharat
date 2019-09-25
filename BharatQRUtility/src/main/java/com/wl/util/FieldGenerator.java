package com.wl.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FieldGenerator {
	
	private static final Logger logger = LoggerFactory
			.getLogger(FieldGenerator.class);
	
	private String field12;
	private String field13;
	private String field07;
	private String field37;
	private String field11;
	private Date now;
	
	
		
	public String getField12() {
		return field12;
	}

	public String getField13() {
		return field13;
	}

	public String getField07() {
		return field07;
	}

	public String getField37() {
		return field37;
	}

	public String getField11() {
		return field11;
	}

	public Date getNow() {
		return now;
	}

	public FieldGenerator() {
		now  = new Date();
		generate();
	}
	
	public void generate(){
		generateField12();
		generateField13();
		generateField07();
		generateField11();
		generateField37();
	}


	/* Time, Local Transaction */
	private void generateField12(){
		SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");
	    field12 =  sdfTime.format(now);
	}
	
	/* Date, Local Transaction */
	private void generateField13(){
		SimpleDateFormat sdfTime = new SimpleDateFormat("MMdd");
	    field13 =  sdfTime.format(now);
	}
	
	/* Transmission date and Time */
	private void generateField07(){

        Calendar c = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
        c.setTime(now);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        int date = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH) + 1;
        
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%02d", month));
        sb.append(String.format("%02d", date));
        sb.append(String.format("%02d", hour) );
        sb.append(String.format("%02d", min));
        sb.append(String.format("%02d", second));
        field07 = sb.toString() ;
        sb = null;


}


	
	/* Systems Trace Audit Number, STAN  */
	private void generateField11(){
		Calendar c = GregorianCalendar.getInstance();
		c.setTime(now);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int min = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		int millis = c.get(Calendar.MILLISECOND);
		int totalSeconds = 	( hour * 60 * 60 ) + ( min * 60 ) + second + millis; 
		String strHour = Integer.toString(hour);
		field11 = strHour.substring(strHour.length()-1) + Integer.toString(totalSeconds);
	}
	
	
	/* Retrieval Reference Number */
	private void generateField37(){
		SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy");
	    String year =  sdfTime.format(now);
		String y = year.substring(year.length()-1);
		
		Calendar c = GregorianCalendar.getInstance();
		c.setTime(now);
		int julianDate = c.get(Calendar.DAY_OF_YEAR);
	
		
		
		/* the yddd equivalent of the field 7 date (y - last digit of year, ddd - Julian Date) */
		String first4 = y + String.format("%03d",julianDate); // julianDate
		/* the hours from the time in field 7 */
		String next2 = field12.substring(0, 2);
		/* the value from field 11 */
		String next6 = field11;
		
		String rrn = first4 + next2 + next6;
		field37 = rrn ;
		
		//logger.debug("RRN-->" + rrn);
	}
	
	public static void main(String[] args) {
		FieldGenerator fg = new FieldGenerator();
		fg.generate();
		logger.info("Field 07 :"+fg.getField07());
		logger.info("Field 11 :"+fg.getField11());
		logger.info("Field 12 :"+fg.getField12());
		logger.info("Field 13 :"+fg.getField13());
		logger.info("Field 37 :"+fg.getField37());
		
	}


}

