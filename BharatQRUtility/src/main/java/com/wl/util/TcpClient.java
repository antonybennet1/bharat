package com.wl.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wl.util.config.ApplicationConfig;
import com.wl.util.constants.ErrorMessages;
import com.wl.util.exceptions.ApplicationException;


public class TcpClient {


	private static final Logger logger = LoggerFactory.getLogger(TcpClient.class);

	private static String  IP_ADDRESS = ApplicationConfig.get("magnus_refund_ip");
	private static int PORT =  Integer.parseInt(ApplicationConfig.get("magnus_refund_port"));

	private static InetSocketAddress hostAddress = new InetSocketAddress(IP_ADDRESS,PORT);

	public static String send(String msg)
	{
		long starttime = System.currentTimeMillis();
		SocketChannel clientChannel=null;
		String response="";
		try {
			logger.info("Connecting :"+hostAddress);
			
			clientChannel = SocketChannel.open(hostAddress);
			clientChannel.socket().setSoTimeout(Integer.parseInt(ApplicationConfig.get("magnus_refund_timeout")));
			byte [] message = null;

			ByteBuffer buffer = null;
			message = msg.getBytes("UTF-8");

			buffer = ByteBuffer.wrap(message);
			clientChannel.write(buffer);
			buffer.clear();

			buffer = ByteBuffer.allocate(1024);
			int numRead = -1;

			numRead = clientChannel.read(buffer);

			byte[] data = new byte[numRead];
			System.arraycopy(buffer.array(), 0, data, 0, numRead);


			response = new String(data,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.error("Exception in send of TcpClient :",e);
			throw new ApplicationException(ErrorMessages.SERVER_ERROR.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Exception in send of TcpClient :",e);
			throw new ApplicationException(ErrorMessages.SERVER_ERROR.toString());
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception in send of TcpClient :",e);
			throw new ApplicationException(ErrorMessages.SERVER_ERROR.toString());
		}
		finally{
			try {
				clientChannel.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("Exception while closing connection :",e);
			}

		}

		return response;

	}




	public static void main(String [] a)
	{
		/*String str = MagnusClient.send(ISO8583.keyExchange());

		//String resp0810 = "002e60000001120810203801000280000499049000000117355005310112303330363036303630360006303030383034";
		ISO8583.parseKeyExchangeResponse(str);

		System.out.println("Response"+str);*/

	}


}
