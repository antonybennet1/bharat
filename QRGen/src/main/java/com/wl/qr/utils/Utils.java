package com.wl.qr.utils;

public class Utils {
	
	public static String lengthChecker(String str, int len) {
		if(str.length() > len){
			str = str.substring(0, len);
		}
		return str;
	}

	public static boolean hasValue(String str) {
		if (null == str || "".equals(str) || str.length() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	//calculateCrcMsb will return decimal value of crc. Convert the hex value in crc tag to decimal and compare to validate.
	public static String CalcCRC16(String strInput) {
		int crc = 0xFFFF;
		byte[] data = stringToBytesASCII(strInput);
		for (int i = 0; i < data.length; i++) {
			crc ^= (int) (data[i] << 8);
			for (int j = 0; j < 8; j++) {
				if ((crc & 0x8000) > 0)
					crc = (int) ((crc << 1) ^ 0x1021);
				else
					crc <<= 1;
			}
		}
		String s = Integer.toHexString(crc);
		return s;
	}

	public static byte[] stringToBytesASCII(String str) {
		byte[] b = new byte[str.length()];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) str.charAt(i);
		}
		return b;
	}

	public static String getFormatedLength(String str) {
		String res = "";

		if (str.length() < 10) {
			res = "0" + str.length();
		} else {
			res = "" + str.length();
		}

		return res;
	}
	
	public static void main(String[] args) {
		String strInput="000201010212021646049010048752830415512260000487528061661000200048753020826UTIB000015315301020001459926460010A0000005240128mab.037133027800306@axisbank27530010A00000052401350083021539000206850800049404109180Y52044900530335654044240.005802IN5908CESC LTD6007KOLKATA6106700019625505180083021539000206850800049404109180Y0708030120186304";
		System.out.println(CalcCRC16(strInput));
	}
}
