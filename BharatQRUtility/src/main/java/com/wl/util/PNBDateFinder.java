package com.wl.util;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PNBDateFinder {

	private static Date getDate(String date) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("Second Date : " + date);
		return df.parse(date);
	}

	private static long daysBetween(Date one, Date two) {
		long difference = (one.getTime() - two.getTime()) / 86400000;
		return Math.abs(difference);
	}

	public static void main(String[] args) {
		try {
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			System.out.println("First Date : " + sdf.format(d));
			Date d1 = sdf.parse(sdf.format(d));
			Date d2 = getDate("2018-12-19");
			
			Date d3 = sdf.parse(sdf.format(d2));
			System.out.println("Second Date : " + d3);
			
			

			System.out.println(daysBetween(d1, d2));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}