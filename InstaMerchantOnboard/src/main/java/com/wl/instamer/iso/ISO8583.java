package com.wl.instamer.iso;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.MessageFactory;
import com.wl.instamer.model.MerchantOnboard;
import com.wl.instamer.model.MerchantOnboardResponseData;
import com.wl.util.UtilityHelper;
import com.wl.util.FieldGenerator;
import com.wl.util.HelperUtil;

@Component("iso8583")
public class ISO8583 {

	private static final Logger logger = LoggerFactory
			.getLogger(ISO8583.class);
	
	private boolean isIsoMessageLengthRequired = true;
	
	public void setIsoMessageLengthRequired(boolean isIsoMessageLengthRequired) {
		this.isIsoMessageLengthRequired = isIsoMessageLengthRequired;
	}

	private static MessageFactory<IsoMessage> config(String path) throws IOException{
		MessageFactory<IsoMessage> mfact = new MessageFactory<IsoMessage>();
		mfact.setConfigPath(path);
		return mfact;
	}

	/**Create iso message based on request type.
	 * set MTI and Header globally for ISO message.
	 * @param isoBean
	 * @param requestType
	 * @return String ISO converted message in string format
	 */
	public String createMerchantOnboardISO(MerchantOnboard request, MerchantOnboardResponseData response){
		MessageFactory<IsoMessage> msgFact = new MessageFactory<IsoMessage>();
		IsoMessage iso = msgFact.newMessage(0x600);
		FieldGenerator fg = new FieldGenerator();
		iso.setCharacterEncoding("ISO-8859-1");
		iso.setBinaryBitmap(false);
		iso.setValue(3, "990000" , IsoType.NUMERIC, 6);
		iso.setValue(11, Integer.valueOf(fg.getField11()), IsoType.NUMERIC, 6); // stan number
		//iso.setValue(11, Integer.valueOf("100476"), IsoType.NUMERIC, 6); // stan number
		iso.setValue(12,fg.getField12(), IsoType.TIME,6);  //Trans Time -  Format 'hhmiss'
		//iso.setValue(12,134000, IsoType.TIME,6);
		
		
		iso.setValue(13, UtilityHelper.dateInddMMyyyy(), IsoType.ALPHA,6); //Trans Date  - Format 'mmdd'
		//iso.setValue(13,000001, IsoType.DATE4,4);
		
		iso.setValue(41,response.getTid(), IsoType.ALPHA,8);  //Trans Date  - Format 'mmdd'
		iso.setValue(42,response.getMerchantCode(), IsoType.ALPHA,15);  //Trans Date  - Format 'mmdd'
		String field43; 
		field43 = request.getDbaName() + request.getInstLocation();
		
		//String field43 ="JAMI SANTHOSH INSTA MID BANGALORE IN";
		if(!field43.trim().endsWith("IN"))
		{
			field43 = field43 + " IN"; 
		}
		field43 = String.format("%1$40s", field43);
		
		iso.setValue(43, field43 , IsoType.ALPHA, 40); //Card Acceptor Name/Location
		String field49 ="356";
		iso.setValue(49, field49 , IsoType.ALPHA, 3);
		String field60 = buildField60(request, response);
		//String field60 = "22222222222222600031JAMI SANTHOSH INSTA MIDBANGALORE MG ROAD 5441";
		iso.setValue(60,field60, IsoType.LLLVAR, 6);
		
		String field61 = buildField61(request, response);
		//String field61 = "2222222222222269999999935605052017";
		iso.setValue(62,field61, IsoType.LLLVAR, 6);
		
		String field63 = buildField63(request, response);
		//String field62 = "0003122222222222222699999999410010123456789451234511111111126100032121212122 74187768807418776880990000100476134000";
		iso.setValue(63,field63, IsoType.LLLVAR, 6);
		
		
		byte[] isoMsg = iso.writeData();
		/*ByteArrayOutputStream byteOuts = new ByteArrayOutputStream();
		try {
			iso.write(byteOuts, 2);
			isoMsg = byteOuts.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Exception in creating iso message:",e);
		}finally{	
			try {
				byteOuts.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block				
				e.printStackTrace();
			}
		}*/
		logger.info("Transaction packet :"+HelperUtil.byteArrayToHexString(isoMsg));
		String str ="";
		try {
			str = new String(isoMsg,"ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			logger.error("Exception in sale trancation :",e);
		}
		return str;
	}
	
	private static String buildField63(MerchantOnboard request, MerchantOnboardResponseData response) {
		StringBuilder field63 = new StringBuilder();
		//field63.append(String.format("%1$5s", request.getBankCode()!=null?request.getBankCode():""));
		field63.append(String.format("%1$" + 5 + "s", request.getBankCode()!=null?request.getBankCode():"").replace(' ', '0'));
		//field63.append(String.format("%1$15s", response.getMerchantCode()!=null?response.getMerchantCode():""));
		field63.append(String.format("%1$" + 15 + "s", response.getMerchantCode()!=null?response.getMerchantCode():"").replace(' ', '0'));
		//field63.append(String.format("%1$8s",  response.getTid()!=null?response.getTid():""));
		field63.append(String.format("%1$" + 8 + "s", response.getTid()!=null?response.getTid():"").replace(' ', '0'));
		field63.append(String.format("%1$16s", response.getVisaMpan()!=null?response.getVisaMpan():""));
		field63.append(String.format("%1$16s", response.getMasterMpan()!=null?response.getMasterMpan():""));
		field63.append(String.format("%1$16s", response.getRupayMpan()!=null?response.getRupayMpan():""));
		field63.append(String.format("%1$10s", request.getMerMobileNumber()!=null?request.getMerMobileNumber():""));
		field63.append(String.format("%1$10s", request.getInstAddMobileNo()!=null?request.getInstAddMobileNo():""));
		System.out.println("Filed 63 : " + field63.toString());
		return field63.toString();
	}
	
	private static String buildField61(MerchantOnboard request, MerchantOnboardResponseData response) {
		StringBuilder field61 = new StringBuilder();
		/*field61.append(String.format("%1$15s", response.getMerchantCode()!=null?response.getMerchantCode():""));
		field61.append(String.format("%1$8s",  response.getTid()!=null?response.getTid():""));
		*/
		field61.append(String.format("%1$" + 15 + "s", response.getMerchantCode()!=null?response.getMerchantCode():"").replace(' ', '0'));
		/*field61.append(String.format("%0"+ (15 - (response.getMerchantCode()!=null?response.getMerchantCode():"").length() )+"d%s",0 ,(response.getMerchantCode()!=null?response.getMerchantCode():"")));
		field61.append(String.format("%0"+ (8 - (response.getTid()!=null?response.getTid():"").length() )+"d%s",0 ,(response.getTid()!=null?response.getTid():"")));
		*/
		field61.append(String.format("%1$" + 8 + "s", response.getTid()!=null?response.getTid():"").replace(' ', '0'));
		field61.append(String.format("%1$3s", "356"));
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyy");
		field61.append(String.format("%1$8s", sdf.format(new Date())));
		System.out.println("Filed 61 : " + field61.toString());
		return field61.toString();
	}
	
	private static String buildField60(MerchantOnboard request, MerchantOnboardResponseData response) {
		StringBuilder field60 = new StringBuilder();
		/*field60.append(String.format("%0"+ (15 - (response.getMerchantCode()!=null?response.getMerchantCode():"").length() )+"d%s",0 ,(response.getMerchantCode()!=null?response.getMerchantCode():"")));
		*/
		field60.append(String.format("%1$" + 15 + "s", response.getMerchantCode()!=null?response.getMerchantCode():"").replace(' ', '0'));
		/*field60.append(String.format("%0"+ (5 - (request.getBankCode()!=null?request.getBankCode():"").length() )+"d%s",0 ,(request.getBankCode()!=null?request.getBankCode():"")));
		*/
		field60.append(String.format("%1$" + 5 + "s", request.getBankCode()!=null?request.getBankCode():"").replace(' ', '0'));
		field60.append(String.format("%1$-" + 25 + "s", request.getDbaName()!=null?request.getDbaName():""));
		field60.append(String.format("%1$-" + 25 + "s", request.getInstLocation()!=null?request.getInstLocation():""));
		field60.append(String.format("%1$-" + 4 + "s", request.getMcc()!=null?request.getMcc():""));
		field60.append(String.format("%1$12s", "000001000000"));
		System.out.println("Filed 60 : " + field60.toString());
		return field60.toString();
	}
	
	public String[] parseIsoMessage(String responseData,int headerLength) throws ParseException, IOException{
		
			byte responseByteArray[] = responseData.getBytes("ISO-8859-1");
			String hexDumpResponse = HelperUtil.byteArrayToHexString(responseByteArray);
			logger.info("Response packet :"+hexDumpResponse);
			final MessageFactory<IsoMessage> mfact = config("config.xml");
			byte binArray[]  = null;
			/*if(isIsoMessageLengthRequired)
			{
				binArray = Arrays.copyOfRange(responseByteArray, 2, responseByteArray.length);
			}*/
			IsoMessage m = mfact.parseMessage(responseByteArray, headerLength) ;
			String[] msgArray = parse(m);	
			
			return msgArray;
	}

	private String[] parse(IsoMessage m) {
		 String[] msgArray = new String[3];
		
		if(m.getField(2)!=null && !m.getField(2).toString().isEmpty())
		{
			logger.info("Field 02 :["+m.getField(2) +"]");
		}
		if(m.getField(3)!=null && !m.getField(3).toString().isEmpty())
		{
			logger.info("Field 03 :["+m.getField(3) +"]");
		}
		if(m.getField(4)!=null && !m.getField(4).toString().isEmpty())
		{
			logger.info("Field 04 :["+m.getField(4) +"]");
		}
		if(m.getField(5)!=null && !m.getField(5).toString().isEmpty())
		{
			logger.info("Field 05 :["+m.getField(5) +"]");
		}
		if(m.getField(6)!=null && !m.getField(6).toString().isEmpty())
		{
			logger.info("Field 06 :["+m.getField(6) +"]");
		}
		if(m.getField(7)!=null && !m.getField(7).toString().isEmpty())
		{
			logger.info("Field 07 :["+m.getField(7) +"]");
		}
		if(m.getField(8)!=null && !m.getField(8).toString().isEmpty())
		{
			logger.info("Field 08 :["+m.getField(8) +"]");
		}
		if(m.getField(9)!=null && !m.getField(9).toString().isEmpty())
		{
			logger.info("Field 09 :["+m.getField(9) +"]");
		}
		if(m.getField(10)!=null && !m.getField(10).toString().isEmpty())
		{
			logger.info("Field 010 :["+m.getField(10) +"]");
		}
		if(m.getField(11)!=null && !m.getField(11).toString().isEmpty())
		{
			logger.info("Field 011 :["+m.getField(11) +"]");
		}
		if(m.getField(12)!=null && !m.getField(12).toString().isEmpty())
		{
			logger.info("Field 012 :["+m.getField(12) +"]");
		}
		if(m.getField(13)!=null && !m.getField(13).toString().isEmpty())
		{
			logger.info("Field 013 :["+m.getField(13) +"]");
		}
		if(m.getField(14)!=null && !m.getField(14).toString().isEmpty())
		{
			logger.info("Field 014 :["+m.getField(14) +"]");
		}
		if(m.getField(15)!=null && !m.getField(15).toString().isEmpty())
		{
			logger.info("Field 015 :["+m.getField(15) +"]");
		}
		if(m.getField(16)!=null && !m.getField(16).toString().isEmpty())
		{
			logger.info("Field 016 :["+m.getField(16) +"]");
		}
		if(m.getField(17)!=null && !m.getField(17).toString().isEmpty())
		{
			logger.info("Field 017 :["+m.getField(17) +"]");
		}
		if(m.getField(18)!=null && !m.getField(18).toString().isEmpty())
		{
			logger.info("Field 018 :["+m.getField(18) +"]");
		}
		if(m.getField(19)!=null && !m.getField(19).toString().isEmpty())
		{
			logger.info("Field 019 :["+m.getField(19) +"]");
		}
		if(m.getField(20)!=null && !m.getField(20).toString().isEmpty())
		{
			logger.info("Field 020 :["+m.getField(20) +"]");
		}
		if(m.getField(21)!=null && !m.getField(21).toString().isEmpty())
		{
			logger.info("Field 021 :["+m.getField(21) +"]");
		}
		if(m.getField(22)!=null && !m.getField(22).toString().isEmpty())
		{
			logger.info("Field 022 :["+m.getField(22) +"]");
		}
		if(m.getField(23)!=null && !m.getField(23).toString().isEmpty())
		{
			logger.info("Field 023 :["+m.getField(23) +"]");
		}
		if(m.getField(24)!=null && !m.getField(24).toString().isEmpty())
		{
			logger.info("Field 024 :["+m.getField(24) +"]");
		}
		if(m.getField(25)!=null && !m.getField(25).toString().isEmpty())
		{
			logger.info("Field 025 :["+m.getField(25) +"]");
		}
		if(m.getField(26)!=null && !m.getField(26).toString().isEmpty())
		{
			logger.info("Field 026 :["+m.getField(26) +"]");
		}
		if(m.getField(27)!=null && !m.getField(27).toString().isEmpty())
		{
			logger.info("Field 027 :["+m.getField(27) +"]");
		}
		if(m.getField(28)!=null && !m.getField(28).toString().isEmpty())
		{
			logger.info("Field 028 :["+m.getField(28) +"]");
		}
		if(m.getField(29)!=null && !m.getField(29).toString().isEmpty())
		{
			logger.info("Field 029 :["+m.getField(29) +"]");
		}
		if(m.getField(30)!=null && !m.getField(30).toString().isEmpty())
		{
			logger.info("Field 030 :["+m.getField(30) +"]");
		}
		if(m.getField(31)!=null && !m.getField(31).toString().isEmpty())
		{
			logger.info("Field 031 :["+m.getField(31) +"]");
		}
		if(m.getField(32)!=null && !m.getField(32).toString().isEmpty())
		{
			logger.info("Field 032 :["+m.getField(32) +"]");
		}
		if(m.getField(33)!=null && !m.getField(33).toString().isEmpty())
		{
			logger.info("Field 033 :["+m.getField(33) +"]");
		}
		if(m.getField(34)!=null && !m.getField(34).toString().isEmpty())
		{
			logger.info("Field 034 :["+m.getField(34) +"]");
		}
		if(m.getField(35)!=null && !m.getField(35).toString().isEmpty())
		{
			logger.info("Field 035 :["+m.getField(35) +"]");
		}
		if(m.getField(36)!=null && !m.getField(36).toString().isEmpty())
		{
			logger.info("Field 036 :["+m.getField(36) +"]");
		}
		if(m.getField(37)!=null && !m.getField(37).toString().isEmpty())
		{
			logger.info("Field 037 :["+m.getField(37) +"]");
			msgArray[0] = m.getField(37).toString();
		}
		if(m.getField(38)!=null && !m.getField(38).toString().isEmpty())
		{
			logger.info("Field 038 :["+m.getField(38) +"]");
			msgArray[1] = m.getField(38).toString();
		}
		if(m.getField(39)!=null && !m.getField(39).toString().isEmpty())
		{
			logger.info("Field 039 :["+m.getField(39) +"]");
			msgArray[2] = m.getField(39).toString();
		}
		if(m.getField(40)!=null && !m.getField(40).toString().isEmpty())
		{
			logger.info("Field 040 :["+m.getField(40) +"]");
		}
		if(m.getField(41)!=null && !m.getField(41).toString().isEmpty())
		{
			logger.info("Field 041 :["+m.getField(41) +"]");
		}
		if(m.getField(42)!=null && !m.getField(42).toString().isEmpty())
		{
			logger.info("Field 042 :["+m.getField(42) +"]");
		}
		if(m.getField(43)!=null && !m.getField(43).toString().isEmpty())
		{
			logger.info("Field 043 :["+m.getField(43) +"]");
		}
		if(m.getField(44)!=null && !m.getField(44).toString().isEmpty())
		{
			logger.info("Field 044 :["+m.getField(44) +"]");
		}
		if(m.getField(45)!=null && !m.getField(45).toString().isEmpty())
		{
			logger.info("Field 045 :["+m.getField(45) +"]");
		}
		if(m.getField(46)!=null && !m.getField(46).toString().isEmpty())
		{
			logger.info("Field 046 :["+m.getField(46) +"]");
		}
		if(m.getField(47)!=null && !m.getField(47).toString().isEmpty())
		{
			logger.info("Field 047 :["+m.getField(47) +"]");
		}
		if(m.getField(48)!=null && !m.getField(48).toString().isEmpty())
		{
			logger.info("Field 048 :["+m.getField(48) +"]");
		}
		if(m.getField(49)!=null && !m.getField(49).toString().isEmpty())
		{
			logger.info("Field 049 :["+m.getField(49) +"]");
		}
		if(m.getField(50)!=null && !m.getField(50).toString().isEmpty())
		{
			logger.info("Field 050 :["+m.getField(50) +"]");
		}
		if(m.getField(51)!=null && !m.getField(51).toString().isEmpty())
		{
			logger.info("Field 051 :["+m.getField(51) +"]");
		}
		if(m.getField(52)!=null && !m.getField(52).toString().isEmpty())
		{
			logger.info("Field 052 :["+m.getField(52) +"]");
		}
		if(m.getField(53)!=null && !m.getField(53).toString().isEmpty())
		{
			logger.info("Field 053 :["+m.getField(53) +"]");
		}
		if(m.getField(54)!=null && !m.getField(54).toString().isEmpty())
		{
			logger.info("Field 054 :["+m.getField(54) +"]");
		}
		if(m.getField(55)!=null && !m.getField(55).toString().isEmpty())
		{
			logger.info("Field 055 :["+m.getField(55) +"]");
		}
		if(m.getField(56)!=null && !m.getField(56).toString().isEmpty())
		{
			logger.info("Field 056 :["+m.getField(56) +"]");
		}
		if(m.getField(57)!=null && !m.getField(57).toString().isEmpty())
		{
			logger.info("Field 057 :["+m.getField(57) +"]");
		}
		if(m.getField(58)!=null && !m.getField(58).toString().isEmpty())
		{
			logger.info("Field 058 :["+m.getField(58) +"]");
		}
		if(m.getField(59)!=null && !m.getField(59).toString().isEmpty())
		{
			logger.info("Field 059 :["+m.getField(59) +"]");
		}
		if(m.getField(60)!=null && !m.getField(60).toString().isEmpty())
		{
			logger.info("Field 060 :["+m.getField(60) +"]");
		}
		if(m.getField(61)!=null && !m.getField(61).toString().isEmpty())
		{
			logger.info("Field 061 :["+m.getField(61) +"]");
		}
		if(m.getField(62)!=null && !m.getField(62).toString().isEmpty())
		{
			logger.info("Field 062 :["+m.getField(62) +"]");
		}
		if(m.getField(63)!=null && !m.getField(63).toString().isEmpty())
		{
			logger.info("Field 063 :["+m.getField(63) +"]");
		}
		if(m.getField(64)!=null && !m.getField(64).toString().isEmpty())
		{
			logger.info("Field 064 :["+m.getField(64) +"]");
		}
		
		return msgArray;
	}

}
