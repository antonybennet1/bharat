package com.wl.util;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
public class AesEncryDecrypt
{
	private static final String ALGO = "AES";
	private static String key;
	public static String encrypt(String Data, String keyStr) throws Exception 
	{
		Key key = generateKey(keyStr);
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(Data.getBytes());
		String encryptedValue =HelperUtil.base64(encVal);
		System.out.println("Encrypted value :" +encryptedValue); 
		return encryptedValue;
	}
	public static String decrypt(String encryptedData, String keyStr)
			throws Exception 
	{
		Key key = generateKey(keyStr);
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = HelperUtil.base64(encryptedData);
		byte[] decValue = c.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}
	private static Key generateKey(String keyStr) throws Exception {
		byte[] keyValue = keyStr.getBytes();
		Key key = new SecretKeySpec(keyValue, ALGO);
		return key;
	}
	
	public static void main(String[] args) throws Exception {
		String key="IiCXSTIdyJh3Kv13";
		String data="{'merchId':'037022000021100','merchantVpa':'MAB.037022000021100@AXISBANK','customerVpa':'mab.037022000609160@axisbank','merchantTransactionId':'20171006131221','transactionTimestamp':'2017:10:06 01:13:05','transactionAmount':'5.00','gatewayTransactionId':'YESB5ADCEDA78CA54FD8E05400144FFA530','gatewayResponseCode':'00','gatewayResponseMessage':'Success','rrn':'727913035741','checksum':'2f11fc95c609223e2f5e33065b7f7c729c1fe3f207f6a35b2807d9eb0786a878'}";
		
		/*String enc="+zeeLiKkR4aTUczEQvsjJuJIjx2I/7eVt1dhFMT93RsBu3K5MvPjDGE/wx6DMuXt+O29ISAERjXd"+
"EYQ085+pehLA+qdbtZ+VelJSbbVRPJiwb3b4iz0Z6Dx474PAlqmS7iJkwYcYPcBRrsJ9xCqb3bWz"+
"jGD7PQs6UImyV+A/qR1w2P2COcfQakLG/lC7CY846wjphGotOuJUPzxU51p1n+7aWtziSvwl7Y9C"+
"qeU5ew37ABB2KgLBmsruqlMV3w4HUE6oiQFPc39go2KWsHQ89GAfPfpYEDbtJFGWhaOKgfxsDeLU"+
"+vD5IWH8AdvSYLsdT/+VjlB21V5tvKI+AIc7g45aBaYToNgWbMvO6K9liPjy03hYllGUdHqHFqpD"+
"k6uWXrnhDaLOEzdyQjK5s71jun9AnnCetD1DFeLWkbhE4hx/fheDmAkPbxAKI6QWeWH0a5qaNRwr"+
"91BFb9eWp3cQGlJL7WkoraDND2qRKAFICccvlgqMYQam/ryOwJOXIRXrcv6y8N4Q9t0uKtEVW4yz"+
"qT+73D5a5Q7XnIeKc0EN6j/0YoVv+xNq/8hw59tWtgHI/7IYwHi5J/mrOU8v9DVXuWbhTKYTYNHq"+
"0cC70aTJ4fq3lQgby2PdilfDHRDkVimc9U0aUzD/wRl2infgu92rMsLP7nvbRtahlInJl17PupbE"+
"YuP8+Qm9qIxktwT66oPBh3rOc7OaXS4m2hl+KSME/sq5vAKmf4RW5Y+1yKXzW0oq2vJk7VLsQRKj"+
"ha8Gyh3/3YdDYSbAmVvxtxZswRVKup2kLlJRHsTmXha6s/qeuq2RA5qzqlxgTFiS7Jk6xUD6U6uZ"+
"aBQ6mifGVJ6nBJEtSKFbsoI9UV5mW10jq3a9OD02UNbWGwTHTv+XwRACNr7nPGcnj3z8Ysm2QXGN"+
"g+SWSh1/6kB8L5Sk2H5TlOZKy2NHPutsoIqHaFBglzJI12iwFzLQN9+alYiMzl4d+NYYt42IpIM9"+
"OI+E48Dr7iZDqJdz6Tp9vhL3kDZYUks+UOH7hwhFTSNYg5ilhg8dDksHpj/jB+XHw/VzR211skdz"+
"4uvmt+/HVDfQAm0PALs65Nim+9XgzYvVPSVUrg/inEhQlDBIkn/BYE32+L6XJ6LlwngI+mw4GFtf"+
"SORZiH5e2LkmVpQQbMx7oPTN3X0FL0rxnIaU1n0EKGDeAF9FAD9bnuP2W8yT67hUe+DP/mISYo1R"+
"Nnlli8qPp3vK/LxqzpO8XAdsH6iGGdYJiR+r5TIYx2Y8l9cG+YpISeEvF6VhwDcF";
*/
		String encryptedData=encrypt(data, key);
		System.out.println("Encrypted Data : "+encryptedData); 
		String decryptedData=decrypt(encryptedData, key);
		System.out.println("Decrypted Data : "+decryptedData); 
		/*System.out.println("Plain text : "+encryptedData);
System.out.println("Encrypted Data : "+data);
System.out.println("Plain text : "+data);*/
	}
}
