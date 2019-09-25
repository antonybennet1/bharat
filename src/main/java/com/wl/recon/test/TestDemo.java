package com.wl.recon.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class TestDemo {

	public static void main(String[] args) throws ParseException {
		
		Date d = new Date();
		/*d=d-0;
		System.out.println(d);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
		System.out.println(sdf.format(d));*/
		
	/*	
		Calendar cal = Calendar.getInstance();
		//cal.setTime(d);
		cal.add(Calendar.DATE, -0);
		Date dateBefore30Days = cal.getTime();
		System.out.println(dateBefore30Days);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(sdf.format(dateBefore30Days));
		
		String fDate =  sdf.format(dateBefore30Days).toString() +" 23:45:00";
		System.out.println(" fDate "+fDate );
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
		Date d1 = dateFormat.parse(fDate);
		
		System.out.println(" TO DATE " + dateFormat.format(d1));*/
		
		
	
		
		Scanner s = new Scanner(System.in);
		int i = s.nextInt();
		String st = s.next();
		Double dooo = s.nextDouble();
		
		System.out.println("int " + i);
		System.out.println("double " + dooo);
		System.out.println("String " + st);
		
		
	}

}
