package com.wl.bharatqr.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONObject;

import com.wl.upi.util.Utils;

public class MerchantOnBoardAPICall {

	public static void main(String[] args) {
		MerchantOnBoardAPICall merchantOnBoardAPICall = new MerchantOnBoardAPICall();
		merchantOnBoardAPICall.merchantOnBoard(args[0], args[1]);

	}

	public void merchantOnBoard(String bankCode, String vpa) {
		System.out.println("Inside merchantOnBoard method..");
		// logger.info("Inside merchantOnBoard method..");
		Connection conInsight = null;
		Statement stmt = null;
		Statement stmt1 = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conInsight = DriverManager.getConnection("jdbc:oracle:thin:@192.168.153.163:1521:INSIGHT", "INSIGHT",
					"prdins#716");
			// stmt=conInsight.createStatement();
			stmt1 = conInsight.createStatement();
			int tidCount = 0;
			int count = 1;
			int xxx = 0;
			/*
			 * ResultSet rs=stmt.
			 * executeQuery("SELECT mt.tid FROM xp_merchantprofile mp  INNER JOIN INSTALLATIONREQMASTER IRM ON IRM.MERCHANTID = MP.MERCHANTID  INNER JOIN MIDTIDDETAILS MT ON IRM.INSTALLATIONREQID = MT.INSTALLATIONREQID  WHERE IRM.MODELID = 172 AND mt.anchorapplication = 'Y'and lower(mp.vpa) in("
			 * +vpa+")");
			 * 
			 * 
			 * String tid=null; while(rs.next()){ ++count; tid=rs.getString(1);
			 * System.out.println("TID="+tid+"|| bankcode="+bankCode);
			 */
			// logger.info("TID="+tid+"|| bankcode="+bankCode);
			ResultSet rss = null;
			if ("00031".equals(bankCode)) {
				rss = stmt1.executeQuery(
						"SELECT DISTINCT MT.MID \"Merchant Id\",  Mm.Merchantname,  im.instcategorycode \"MCCCode\",  SUBSTR(Cm.Cityname,0,15) \"MerchantCityName\",  trim( TRANSLATE ( Mm.Meraddress1, 'x'  ||CHR(10)  ||CHR(13), 'x')  || ' '  || TRANSLATE ( Mm.Meraddress2, 'x'  ||CHR(10)  ||CHR(13), 'x')) Address,  SUBSTR(Mm.Businessas,0,23) \"DBA Name\",  MM.MERCONTACTPERSON1 \"Contact Person Name\",  MN.MOBILE_NUMBER \"Contact Person Phone Number\",  MN.EMAILID \"Email Id\",  INS.MXPACBANKCODE \"Bank Code\",  CCM.CURRENCYCODE,  NVL(MT.REFUNDNEW,'N') \"Can Refund Flag\",  CASE    WHEN adjustment ='N'    THEN '0'    WHEN adjustment ='Y'    THEN '1'  END tip_conv_indicator,  '' convenience_flag,  '' Convenience_Value,  Mt.Tid,  CASE    WHEN mm.ipg='Y'    THEN 'Y'    WHEN irm.aggregator IS NULL    OR irm.Aggregator    ='0'    THEN 'N'    WHEN (SELECT COUNT(*)      FROM MERCHANTPROFILE ME      INNER JOIN installationreqmaster IR      ON IR.merchantid               = ME.merchantid      WHERE ME.merchantprofileid     = mp.merchantprofileid      AND UPPER(TRIM(IR.aggregator)) = UPPER(TRIM(irm.aggregator))      AND ir.institutionid           = MM.INSTITUTIONID )>0    THEN 'Y'  END aggregatorFLAG ,  CASE    WHEN mm.ipg='Y'    THEN '01061991'    WHEN irm.aggregator IS NULL    OR irm.Aggregator    ='0'    THEN ''    WHEN (SELECT COUNT(*)      FROM MERCHANTPROFILE ME      INNER JOIN installationreqmaster IR      ON IR.merchantid               = ME.merchantid      WHERE ME.merchantprofileid     = mp.merchantprofileid      AND UPPER(TRIM(IR.aggregator)) = UPPER(TRIM(irm.aggregator))      AND ir.institutionid           = MM.INSTITUTIONID )>0    THEN irm.aggregator  END Aggregatorid ,  DECODE(mm.ipg,'Y','IPG', DECODE(irm.aggregator,NULL,'','0','' ,DECODE(  (SELECT COUNT(*) FROM MERCHANTPROFILE ME  INNER JOIN installationreqmaster IR ON IR.merchantid = ME.merchantid WHERE ME.merchantprofileid= mp.merchantprofileid  AND UPPER(TRIM(ir.aggregator))                       = UPPER(TRIM(irm.aggregator))  AND ir.institutionid                                 = MM.INSTITUTIONID  ),0,'',  (SELECT aggregator_name  FROM xp_aggregator_master  WHERE aggregator_id=irm.aggregator  AND institution_id = MM.INSTITUTIONID  )))) AggregatorNAME,  DECODE(mm.ipg,'Y',mp.UPIURL, DECODE(irm.aggregator,NULL,'','0','' ,DECODE(  (SELECT COUNT(*) FROM XP_MERCHANTPROFILE ME  INNER JOIN installationreqmaster IR ON IR.merchantid = ME.merchantid WHERE ME.merchantprofileid= mp.merchantprofileid  AND UPPER(TRIM(ir.aggregator))                       = UPPER(TRIM(irm.aggregator))  AND ir.institutionid                                 = MM.INSTITUTIONID  ),0,'',  (SELECT aggregator_url  FROM xp_aggregator_master  WHERE aggregator_id=irm.aggregator  AND institution_id = MM.INSTITUTIONID  )))) Aggregator_URL,  '' storeid,  (mp.QR_IFSCCODE  ||mp.QR_ACCNO) IFSC_ACCOUNTNO,  MM.MERPINCODE \"postal code\",  MP.VPA,  '' MAM,  MP.UPIURL,  mp.AADHARNO,  irm.qr_code QRCODE,  CASE    WHEN IRM.MODELID  IN (171,172)    AND NVL(MP.IPG,'N')='N'    THEN 2    WHEN IRM.MODELID IN (171,172)    AND MP.ipg        ='Y'    THEN 8    WHEN IRM.MODELID IN      (SELECT modelid FROM assetmodelmaster WHERE gprs='Y'      )    THEN 5  END ENROLLMENT_TYPE,   3 \"Merchant Identifier length\",  (SELECT    CASE      WHEN MVISAPAN IS NOT NULL      THEN '02'    END val  FROM INSTALLATIONREQMASTER  WHERE (modelid IN    (SELECT modelid FROM assetmodelmaster WHERE gprs='Y'    )  OR modelid           ='171')  AND QR_CODE         IS NOT NULL  AND INSTALLATIONREQID=IRM.installationreqid  ) \"Merchant Identifier Visa\",  (SELECT    CASE      WHEN MVISAPAN IS NOT NULL      THEN MVISAPAN      ELSE MVISAPAN    END val  FROM INSTALLATIONREQMASTER  WHERE (modelid IN    (SELECT modelid FROM assetmodelmaster WHERE gprs='Y'    )  OR modelid           ='171')  AND QR_CODE         IS NOT NULL  AND INSTALLATIONREQID=IRM.installationreqid  ) MPAN,  (SELECT    CASE      WHEN MASTER_QR_MID IS NOT NULL      THEN '04'    END val  FROM INSTALLATIONREQMASTER  WHERE (modelid IN    (SELECT modelid FROM assetmodelmaster WHERE gprs='Y'    )  OR modelid           ='171')  AND QR_CODE         IS NOT NULL  AND INSTALLATIONREQID=IRM.installationreqid  ) \"Merchant Identifier master\",  (SELECT    CASE      WHEN MASTER_QR_MID IS NOT NULL      THEN SUBSTR(MASTER_QR_MID,1,15)      ELSE MASTER_QR_MID    END val  FROM INSTALLATIONREQMASTER  WHERE (modelid IN    (SELECT modelid FROM assetmodelmaster WHERE gprs='Y'    )  OR modelid           ='171')  AND QR_CODE         IS NOT NULL  AND INSTALLATIONREQID=IRM.installationreqid  ) MASTER_QR_MID,  (SELECT    CASE      WHEN RUPAY_QR_MID IS NOT NULL      THEN '06'    END val  FROM INSTALLATIONREQMASTER  WHERE (modelid IN    (SELECT modelid FROM assetmodelmaster WHERE gprs='Y'    )  OR modelid           ='171')  AND QR_CODE         IS NOT NULL  AND INSTALLATIONREQID=IRM.installationreqid  ) \"Merchant Identifier RUPAY\",  (SELECT    CASE      WHEN RUPAY_QR_MID IS NOT NULL      THEN RUPAY_QR_MID      ELSE RUPAY_QR_MID    END val  FROM INSTALLATIONREQMASTER  WHERE (modelid IN    (SELECT modelid FROM assetmodelmaster WHERE gprs='Y'    )  OR modelid           ='171')  AND QR_CODE         IS NOT NULL  AND INSTALLATIONREQID=IRM.installationreqid  ) RUPAY FROM MERCHANTMASTER MM INNER JOIN INSTALLATIONREQMASTER IRM ON MM.MERCHANTID=IRM.MERCHANTID INNER JOIN MIDTIDDETAILS MT ON IRM.INSTALLATIONREQID = MT.INSTALLATIONREQID INNER JOIN citymaster CM ON CM.CITYID=MM.MERCITY INNER JOIN COUNTRYMASTER CT ON CT.COUNTRYID=MM.MERCOUNTRY INNER JOIN INSTITUTIONMASTER INS ON Mm.Institutionid=Ins.Institutionid INNER JOIN Currencycodemaster Ccm ON Ccm.Currencycodeid=Mt.Currencycodeid INNER JOIN instcategorymaster im ON im.instcategoryid = mm.instcategoryid INNER JOIN merchantprofile mp ON mp.MERCHANTID    =mm.MERCHANTID AND MP.INSTITUTIONID=MM.INSTITUTIONID INNER JOIN I_BRANCHMASTER bm ON bm.I_branchid = mp.branchid INNER JOIN statemaster sm ON sm.STATEID=mp.MERSTATE INNER JOIN assetmodelmaster am ON am.modelid=irm.modelid INNER JOIN XP_MVISA_MOBILENUMBER MN ON IRM.INSTALLATIONREQID = mn.I_REQUESTID AND mn.I_REQUESTID      IS NOT NULL AND MN.FILE_GEN          ='N' LEFT JOIN regionmaster RG ON RG.regionid=mm.regionid LEFT JOIN subregionmaster SG ON SG.subregionid=mm.subregionid LEFT JOIN locationmaster lm ON lm.locationid=mp.merlocation LEFT JOIN XP_AGGREGATOR_MASTER XAM ON XAM.AGGREGATOR_ID       =MP.aggregator WHERE mt.anchorapplication ='Y' AND IRM.MODELID           IN  ( SELECT modelid FROM assetmodelmaster WHERE gprs='Y'  UNION  SELECT 171 FROM dual  )AND IRM.INSTALLATIONREQID IN  (SELECT TO_NUMBER(MT.INSTALLATIONREQID)  FROM MERCHANTMASTER MM  INNER JOIN INSTALLATIONREQMASTER IRM  ON MM.MERCHANTID=IRM.MERCHANTID  INNER JOIN MIDTIDDETAILS MT  ON IRM.INSTALLATIONREQID  = MT.INSTALLATIONREQID  WHERE MT.ANCHORAPPLICATION='Y'  AND IRM.MODELID          IN    ( SELECT modelid FROM assetmodelmaster WHERE gprs='Y'    UNION    SELECT 171 FROM dual    )  AND IRM.QR_CODE     IS NOT NULL  AND MM.INSTITUTIONID ='20'  AND irm.qr_code     IS NOT NULL  AND MT.TID          IN ("
								+ vpa + "))");
			} else {
				rss = stmt1.executeQuery(
						"  SELECT distinct MT.MID \"Merchant Id\",  Mm.Merchantname,  im.instcategorycode \"MCCCode\",  SUBSTR(Cm.Cityname,0,15)  \"MerchantCityName\",    trim( TRANSLATE ( Mm.Meraddress1, 'x'||CHR(10)||CHR(13), 'x')|| ' ' || TRANSLATE ( Mm.Meraddress2, 'x'||CHR(10)||CHR(13), 'x')) Address,  SUBSTR(Mm.Businessas,0,23) \"DBA Name\",  MM.MERCONTACTPERSON1 \"Contact Person Name\",  MN.MOBILE_NUMBER \"Contact Person Phone Number\",  MN.EMAILID \"Email Id\",  INS.MXPACBANKCODE \"Bank Code\",  CCM.CURRENCYCODE,  NVL(MT.REFUNDNEW,'N') \"Can Refund Flag\",    CASE WHEN adjustment ='N'  THEN   '0'    WHEN adjustment ='Y'  THEN     '1'   END tip_conv_indicator,'' convenience_flag,'' Convenience_Value,Mt.Tid,case  WHEN mm.ipg='Y' then 'Y'       WHEN irm.aggregator IS NULL Or irm.Aggregator ='0' then 'N'      when  ( SELECT COUNT(*)  FROM XP_MERCHANTPROFILE ME                inner join installationreqmaster IR ON IR.merchantid = ME.merchantid                WHERE ME.merchantprofileid= mp.merchantprofileid AND UPPER(TRIM(IR.aggregator)) = UPPER(TRIM(irm.aggregator))                and ir.institutionid = MM.INSTITUTIONID )>0 then 'Y' end aggregatorFLAG ,                  case  WHEN mm.ipg='Y' then '01061991'       WHEN irm.aggregator IS NULL Or irm.Aggregator ='0' then ''      when  ( SELECT COUNT(*)  FROM XP_MERCHANTPROFILE ME                inner join installationreqmaster IR ON IR.merchantid = ME.merchantid                WHERE ME.merchantprofileid= mp.merchantprofileid AND UPPER(TRIM(IR.aggregator)) = UPPER(TRIM(irm.aggregator))                and ir.institutionid = MM.INSTITUTIONID )>0 then  irm.aggregator end Aggregatorid ,                       DECODE(mm.ipg,'Y','IPG',     DECODE(irm.aggregator,null,'','0',''     ,DECODE((SELECT COUNT(*)  FROM XP_MERCHANTPROFILE ME inner join installationreqmaster IR ON IR.merchantid = ME.merchantid     WHERE ME.merchantprofileid= mp.merchantprofileid AND UPPER(TRIM(ir.aggregator)) = UPPER(TRIM(irm.aggregator))     and ir.institutionid = MM.INSTITUTIONID),0,'', (SELEct aggregator_name from xp_aggregator_master                  where aggregator_id=irm.aggregator and institution_id = MM.INSTITUTIONID )))) AggregatorNAME,                      DECODE(mm.ipg,'Y',mp.UPIURL,   DECODE(irm.aggregator,null,'','0',''     ,DECODE((SELECT COUNT(*)  FROM XP_MERCHANTPROFILE ME inner join installationreqmaster IR ON IR.merchantid = ME.merchantid     WHERE ME.merchantprofileid= mp.merchantprofileid AND UPPER(TRIM(ir.aggregator)) = UPPER(TRIM(irm.aggregator))    and ir.institutionid = MM.INSTITUTIONID),0,'', (SELEct aggregator_url from xp_aggregator_master                  where aggregator_id=irm.aggregator and institution_id = MM.INSTITUTIONID )))) Aggregator_URL,        mp.storeid,                 (mp.QR_IFSCCODE||mp.QR_ACCNO) IFSC_ACCOUNTNO,  MM.MERPINCODE \"postal code\",  MP.VPA,   '' MAM,   MP.UPIURL,   mp.AADHARNO,irm.qr_code QRCODE,  CASE WHEN IRM.MODELID IN (171,172) AND NVL(MP.ISIPG,'N')='N' THEN 2   when IRM.MODELID in (171,172) AND MP.isipg='Y' THEN 8  WHEN IRM.MODELID in (select modelid from assetmodelmaster where gprs='Y') THEN 5  END ENROLLMENT_TYPE,    3 \"Merchant Identifier length\",    (Select case when MVISAPAN is not null then '02' end val   from INSTALLATIONREQMASTER     where modelid  IN  (171,172,94,95,159,155) and QR_CODE is not null and INSTALLATIONREQID=IRM.installationreqid ) \"Merchant Identifier Visa\",    (Select case when MVISAPAN is not null then  MVISAPAN else MVISAPAN end val   from INSTALLATIONREQMASTER     where modelid  IN  (171,172,94,95,159,155) and QR_CODE is not null and INSTALLATIONREQID=IRM.installationreqid ) MPAN,       ( Select case when MASTER_QR_MID is not null then '04' end val       from INSTALLATIONREQMASTER where modelid IN (171,172,94,95,159,155) and QR_CODE is not null and INSTALLATIONREQID=IRM.installationreqid ) \"Merchant Identifier master\",          ( Select case when MASTER_QR_MID is not null then  substr(MASTER_QR_MID,1,15) else MASTER_QR_MID end val       from INSTALLATIONREQMASTER where modelid IN (171,172,94,95,159,155) and QR_CODE is not null and INSTALLATIONREQID=IRM.installationreqid  ) MASTER_QR_MID,   ( Select case when RUPAY_QR_MID is not null then '06' end val from INSTALLATIONREQMASTER     where modelid IN (171,172,94,95,159,155)     and QR_CODE is not null and INSTALLATIONREQID=IRM.installationreqid ) \"Merchant Identifier RUPAY\",        (Select case when RUPAY_QR_MID is not null then RUPAY_QR_MID else RUPAY_QR_MID end val from INSTALLATIONREQMASTER     where modelid IN (171,172,94,95,159,155)     and QR_CODE is not null and INSTALLATIONREQID=IRM.installationreqid ) RUPAY      FROM MERCHANTMASTER MM  INNER JOIN INSTALLATIONREQMASTER IRM  ON MM.MERCHANTID=IRM.MERCHANTID  INNER JOIN MIDTIDDETAILS MT ON IRM.INSTALLATIONREQID = MT.INSTALLATIONREQID  Inner Join Citymaster Cm On Cm.Cityid=Mm.Mercity  INNER JOIN COUNTRYMASTER CT  ON CT.COUNTRYID=MM.MERCOUNTRY  Inner Join Institutionmaster Ins On Mm.Institutionid=Ins.Institutionid  Inner Join Currencycodemaster Ccm On Ccm.Currencycodeid=Mt.Currencycodeid  inner join instcategorymaster im on im.INSTCATEGORYID = mm.instcategoryid  Inner Join Xp_Mvisa_Mobilenumber Mn On Irm.Installationreqid = Mn.I_Requestid And Mn.I_Requestid Is Not Null And Mm.Merchantcode=Mn.Mid  and mn.is_primary='Y'   inner join assetmodelmaster am on am.modelid=irm.modelid  INNER JOIN xp_merchantprofile MP  ON mp.MERCHANTID=MM.MERCHANTID    AND MP.INSTITUTIONID=MM.INSTITUTIONID  inner join statemaster sm on sm.STATEID=mp.MERSTATE             left join regionmaster RG on RG.regionid=mm.regionid      left join subregionmaster SG on SG.subregionid=mm.subregionid      inner join xp_branchmaster bm  on bm.branchid = mp.branchid  and bm.INSTITUTIONID=mp.INSTITUTIONID      left join  locationmaster lm  on lm.locationid=mp.merlocation  where mt.anchorapplication='Y' and irm.modelid IN (171,172)  and mt.tid  IN ("
								+ vpa + ")");
			}
			while (rss.next()) {
				++tidCount;
				System.out.println("Inside merchant on board through vpa in BaratQR.....");
				// logger.info("Inside merchant on board through vpa in
				// BaratQR.....");
				JSONObject response = new JSONObject();
				try {
					JSONObject addtionalFields = new JSONObject();
					addtionalFields.put("customerRelationshipNumber", "");
					addtionalFields.put("typeofTerminal", "");
					addtionalFields.put("serialNumberofMachine", "");
					addtionalFields.put("dailyTransactionValueLimit", "9999999.00");
					addtionalFields.put("rupayDomDebitOffus", "");
					addtionalFields.put("rupayDomDebitOnus", "");
					addtionalFields.put("rupayDomInterOffus", "");
					addtionalFields.put("rupayDomInterOnus", "");
					addtionalFields.put("countryCode", "IN");
					addtionalFields.put("address2", "");
					addtionalFields.put("locationName", "");
					addtionalFields.put("state", "");
					addtionalFields.put("awlHelpLineNo", "");
					addtionalFields.put("region", "");
					addtionalFields.put("subRegion", "");
					addtionalFields.put("branch", "");
					addtionalFields.put("creditCards", "");
					addtionalFields.put("billNumber", "N");
					addtionalFields.put("mobileNumber", "N");
					addtionalFields.put("storeId", "");
					addtionalFields.put("loyaltyNumber", "");
					addtionalFields.put("referenceId", "");
					addtionalFields.put("consumerId", "");
					addtionalFields.put("purpose", "");

					Calendar startCalc = Calendar.getInstance();
					startCalc.setTime(new Date());
					String startDate = new SimpleDateFormat("ddMMyyyyHHmmss").format(startCalc.getTime());

					String mobileNumber = rss.getString(8);
					System.out.println("Mobile Number before=" + mobileNumber);
					System.out.println("couNt=" + ++xxx);
					// logger.info("Mobile Number before="+mobileNumber);
					if (mobileNumber.length() == 10) {
						System.out.println("Mobile Number after=" + mobileNumber);
						// logger.info("Mobile Number after="+mobileNumber);
					} else if (mobileNumber.length() > 10) {
						mobileNumber = mobileNumber.substring(mobileNumber.length() - 10);
						System.out.println("Mobile Number after=" + mobileNumber);
						// logger.info("Mobile Number after="+mobileNumber);
					} else {
						int l = mobileNumber.length();
						for (int i = l; i < 10; i++) {
							mobileNumber = mobileNumber + 0;
						}
						System.out.println("Mobile Number after=" + mobileNumber);
						// logger.info("Mobile Number after="+mobileNumber);
					}

					JSONObject jsonInput = new JSONObject();
					jsonInput.put("requestId", startDate + new Utils().generateRandomNumber(6));
					jsonInput.put("requestDateAndTime", startDate);
					jsonInput.put("requestType", "QRC");
					jsonInput.put("merchantId", rss.getString(1));
					jsonInput.put("merchantName", rss.getString(2));
					jsonInput.put("mccCode", rss.getString(3));
					jsonInput.put("merchantCityName", rss.getString(4));
					jsonInput.put("address", rss.getString(5));
					jsonInput.put("dbaName", rss.getString(6));
					jsonInput.put("contactPersonName", rss.getString(7));
					jsonInput.put("contactPersonPhoneNumber", mobileNumber);
					jsonInput.put("emailId", rss.getString(9));
					jsonInput.put("bankCode", rss.getString(10));
					jsonInput.put("currencyCode", rss.getString(11));
					jsonInput.put("canRefundFlag", rss.getString(12));
					jsonInput.put("tipConvIndicator", rss.getString(13));
					jsonInput.put("convenienceFlag", rss.getString(14));
					jsonInput.put("convenienceValue", rss.getString(15));
					jsonInput.put("tid", rss.getString(16));
					jsonInput.put("aggregatorFlag", rss.getString(17));
					jsonInput.put("aggregatorId", rss.getString(18));
					jsonInput.put("aggregatorName", rss.getString(19));
					jsonInput.put("aggregatorURL", rss.getString(20));
					jsonInput.put("aggregatorURL2", "");
					jsonInput.put("storeId", rss.getString(21));
					jsonInput.put("ifscCodeAccountNumber", rss.getString(22));
					jsonInput.put("postalCode", rss.getString(23));
					jsonInput.put("upiVpa", rss.getString(24));
					jsonInput.put("upiMam", rss.getString(25));
					jsonInput.put("upiUrl", rss.getString(26));
					jsonInput.put("aadharNumber", rss.getString(27));
					jsonInput.put("programType", rss.getString(29));
					jsonInput.put("staticQrCode", rss.getString(28));
					jsonInput.put("smsFlag", "Y");
					jsonInput.put("pdfFlag", "Y");
					jsonInput.put("active", "Y");
					jsonInput.put("visaMerchantIdentifierValue", rss.getString(31));
					jsonInput.put("mastercardMerchantIdentifierValue", rss.getString(33));
					jsonInput.put("npciMerchantIdentifierValue", "");
					jsonInput.put("additionalFields", addtionalFields);
					jsonInput.put("primaryNumber", mobileNumber);
					jsonInput.put("version", "1.0");
					jsonInput.put("id", "");
					jsonInput.put("amex_mid", "");
					jsonInput.put("amex_tid", "");
					System.out.println("Merchant Detail in json format=" + jsonInput);
					// logger.info("Merchant Detail in json format="+jsonInput);
					SSLContext ssl_ctx = SSLContext.getInstance("TLS");
					TrustManager[] trust_mgr = get_trust_mgr();
					ssl_ctx.init(null, // key manager
							trust_mgr, // trust manager
							new SecureRandom()); // random number generator
					HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());

					HostnameVerifier allHostsValid = new HostnameVerifier() {
						public boolean verify(String hostname, SSLSession session) {
							return true;
						}
					};
					HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

					URL url = new URL("https://paymod.in.worldline.com:8443/MerchantOnBoardWeb/addNewMerchant");
					System.out.println("Merchant on board URL=" + url);
					// logger.info("Merchant on board URL="+url);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setDoOutput(true);
					connection.setDoInput(true);
					connection.setRequestProperty("Content-Type", "application/json");
					connection.setRequestProperty("Accept", "application/json");
					connection.setRequestMethod("POST");

					OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
					outputStreamWriter.write(jsonInput.toString());
					outputStreamWriter.flush();
					StringBuilder stringBuffer = new StringBuilder();
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(connection.getInputStream(), "utf-8"));
					String line = null;
					while ((line = bufferedReader.readLine()) != null) {
						stringBuffer.append(line + "\n");
					}
					bufferedReader.close();
					response = new JSONObject(stringBuffer.toString());
					System.out.println("tid=" + rss.getString(16) + "|| merchant on board response=" + response);
					// logger.info("tid="+rss.getString(16)+"|| merchant on
					// board response="+response);

				} catch (Exception e) {
					System.out.println("Error, while calling Merchant on Board in BharatQR:" + e);
					// logger.error("Error, while calling Merchant on Board in
					// BharatQR:" + e);
				}
			}

			// }
			if (count == 0 || tidCount == 0) {
				System.out.println("VPA=" + vpa + "||Details not found through vpa or TID in Insight database");
				// logger.info("VPA="+vpa+"||Details not found through vpa or
				// TID in Insight database");
			}
		} catch (Exception e) {
			System.out.println("FETCH TID FROM INSIGHT DATABASE=" + e);
			// logger.error("FETCH TID FROM INSIGHT DATABASE="+e);
		} finally {
			try {
				// stmt.close();
				stmt1.close();
				conInsight.close();
			} catch (Exception e) {
				System.out.println("Connection Exception in Insight Database=" + e);
				// logger.error("Connection Exception in Insight Database="+e);
			}

		}

	}

	public TrustManager[] get_trust_mgr() {
		TrustManager[] certs = new TrustManager[] { new X509TrustManager() {
			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
					throws CertificateException {
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
					throws CertificateException {
			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		} };
		return certs;
	}
}
