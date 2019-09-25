package com.wl.instamer.util;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.wl.instamer.dao.DetailDAO;
import com.wl.instamer.dao.DetailDAOImpl;
import com.wl.instamer.iso.ISO8583;
import com.wl.instamer.model.MerchantOnboard;
import com.wl.instamer.model.MerchantOnboardResponseData;
import com.wl.util.UtilityHelper;

//import com.awl.cca.constants.Constants;

@Component("magnusClient")
public class TcpMagnusClient {


	private static final Logger logger = LoggerFactory.getLogger(TcpMagnusClient.class);

	private static volatile Socket socketObj = null;
	
	@PostConstruct
	public void initializeBean(){

		/*try {
		logger.info("initializing TcpMagnusClient");
			Properties ep = new Properties();
			String magnusIp = ep.getProperty("magnusIp");
			int magnusPort = Integer.parseInt(ep.getProperty("magnusPort"));
			String magnusIp = "10.10.11.11";
			int magnusPort = 2098;
			if(magnusIp.length() > 0 )
			{
				socketObj = getSocket(magnusIp, magnusPort);
				socketObj.setKeepAlive(true);
				//socketObj.setSoTimeout(Integer.valueOf(Parameters.para.get("timeout")));
			}
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
		} catch (UnknownHostException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}*/
	}

	@PreDestroy
	public void destroyBean(){
		try {
			if(socketObj!=null)
			{
				socketObj.close();
				logger.info("Destroying TcpMagnusClient");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static Socket getSocket(String ip, int port)
			throws NumberFormatException, UnknownHostException, IOException {
		
		if (socketObj == null) {
			synchronized (TcpMagnusClient.class) {
				logger.debug("Connecting BharatQR IP:"+ip+"/"+port);
				socketObj = new Socket(ip, port);
			}
		}
		return socketObj;
	}


	public String send(String reqISOMsg)
	{
		
		PrintWriter printWriterObj = null;
		InputStream in = null;
		String response = "";
		try {
			
			logger.info("bharatQrClient req msg|"+reqISOMsg+"|"+reqISOMsg.length());
			
			if(socketObj!=null)
			{
				logger.info("bharatQrClient isConnected|isClosed|"+socketObj.isConnected()+"|"+socketObj.isClosed());
			}
			if(socketObj == null || !socketObj.isConnected() || socketObj.isClosed()){
				if(socketObj!=null && socketObj.isClosed())
					socketObj = null;
				initializeBean();
				logger.info("bharatQrClient isConnected"+socketObj.isConnected());
			}
			
			
			synchronized (TcpMagnusClient.class) {
				int ret = 0;
				byte[] byte_reponse = null;
				OutputStream os = socketObj.getOutputStream();
				BufferedOutputStream bos = new BufferedOutputStream(os);
				OutputStreamWriter osw = new OutputStreamWriter(bos,"ISO-8859-1");
				printWriterObj = new PrintWriter(osw);
				printWriterObj.print(reqISOMsg);
				printWriterObj.flush();
				logger.info(Thread.currentThread().getName()+"|bharatQrClient bytes written:"+reqISOMsg.getBytes().length);
				in = socketObj.getInputStream();
				DataInputStream din = new DataInputStream(in);
			    
				//ret = din.readShort();
				byte_reponse = new byte[255];
				din.read(byte_reponse);
			    
				logger.info(Thread.currentThread().getName()+"|bharatQrClient bytes received:"+ret);
				if(ret == -1)
				{
					logger.info("bharatQrClient|End of response stream reached. No response to read");
					response = "993";
					destroyBean();
				}
				else
				{
					response = new String(byte_reponse);
				}
				logger.info("bharatQrClient response:"+response);
			}
			return response;
		} catch (java.net.SocketTimeoutException ste) {
			logger.debug("Socket time out exception",ste);
			if(ste.getMessage()!=null && ste.getMessage().contains("Read timed out"))
			{
				response = "990"; 
			}
			else
			{
				response = "993";
			}
		} catch (ConnectException connException) {
			logger.info("Connection Exception:connException: "
					+ connException);
			response = "991" ; 
		} catch (IOException e) {
			
			response = "992"; 
			logger.error("Exception in send of bharatQrClient :"+e.getMessage(),e);
			if(e instanceof EOFException || (e.getMessage()!=null && (e.getMessage().contains("Connection reset") || e.getMessage().contains("Broken pipe")) ))
			{
				destroyBean();
				logger.error("Closing connection as connection reset by peer");
			}
		} catch (Exception exceptionObject) {
			exceptionObject.printStackTrace();
			logger.info("Connection Exception:exceptionObject: "
					+ exceptionObject);
			response = "993"; 
		}
		
		return response;
	}
	
	public static void main(String [] a) throws ParseException, IOException
	{
		/*String str = MagnusClient.send(ISO8583.keyExchange());

		//String resp0810 = "002e60000001120810203801000280000499049000000117355005310112303330363036303630360006303030383034";
		ISO8583.parseKeyExchangeResponse(str);

		System.out.println("Response"+str);*/
		ByteBuffer buf = ByteBuffer.allocate(1000);
		System.out.println("Remaining"+buf.toString());
		String resp0810 = "002e60000001120810203801000280000499049000000117355005310112303330363036303630360006303030383034";
		buf.put(resp0810.getBytes());
		buf.flip();
		System.out.println("Remaining"+buf.toString());
		byte[] len = new byte[4];
		buf.get(len, 0, 4);
		System.out.println(new String(len));
		
		int length = UtilityHelper.getLength(new String(len));
		System.out.println("len|"+length);
		byte[] data = new byte[length*2];
		buf.get(data, 0, length*2);
		System.out.println(new String(data));
		System.out.println("Remaining"+buf.toString());
		
		ISO8583 iso = new ISO8583();
		MerchantOnboard request = new MerchantOnboard();
		MerchantOnboardResponseData response = new MerchantOnboardResponseData();
		request.setDbaName("BIKANER SWEET AND SNACKS");
		request.setInstLocation(12);
		request.setMcc(48);
		request.setBankCode("111");
		request.setMerMobileNumber(9324721649L);
		request.setInstAddMobileNo(8425996444L);
		response.setTid("99999999");
		response.setMerchantCode("222222222222226");
		response.setVisaMpan("4547191000000769");
		response.setMasterMpan("529812100000075");
		response.setRupayMpan("6100441000000770");
		String result = iso.createMerchantOnboardISO(request, response);
		System.out.println(result);
	
		TcpMagnusClient t = new TcpMagnusClient();
		String magnusRes = null;
		try {
			magnusRes = t.send(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Response from Magnus : " + magnusRes);
		iso.parseIsoMessage(magnusRes, 0);
		DetailDAO details = new DetailDAOImpl();
		//details.saveDeail(magnusRes);
		
		buf.flip();
		buf.put("kunal".getBytes());
		
		System.out.println("Remaining"+buf.toString());
	}

	
}
