package com.wl.bharatqr.interceptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.wl.util.AesUtil;
import com.wl.util.EncryptionCache;
import com.wl.util.JsonUtility;
import com.wl.util.SHA256CheckSum;
import com.wl.util.constants.Constants;
import com.wl.util.constants.ErrorMessages;
import com.wl.util.exceptions.ApplicationException;
import com.wl.util.model.Request;
import com.wl.util.model.Response;

/**
 * Servlet Filter implementation class EncryptionFilter
 */
@WebFilter(urlPatterns={"/qrGen/*" , "/upi/collect/*", "/app/*"})
public class EncryptDecryptFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(EncryptDecryptFilter.class);

	/**
	 * Default constructor. 
	 */
	public EncryptDecryptFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	//Decrypt Data
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// Getting Param parameter data into decrypt method
		AesUtil aes = null;
		boolean isEncrypted = false;
		String receivedData=request.getParameter("parm");
		String checksum=request.getParameter("checksum");
		logger.debug("incoming request:"+receivedData);
		
		if(checksum!=null && !checksum.isEmpty())
		{
			if(!SHA256CheckSum.verCheckSum(receivedData, checksum))
			{
				PrintWriter pw =  response.getWriter();
				Response resp = new Response();
				resp.setStatus(Constants.FAILED.name());
				resp.setMessage(ErrorMessages.CHECKSUM_NOT_MATCH.toString());
				String content = JsonUtility.convertToJson(resp) ;
				sendResponse(aes, pw,isEncrypted,content);
				return;
			}
		}
		
		if(receivedData == null)
		{
			PrintWriter pw =  response.getWriter();
			Response resp = new Response();
			resp.setStatus(Constants.FAILED.name());
			resp.setMessage("parm paramter is mandatory");
			String content = JsonUtility.convertToJson(resp) ;
			sendResponse(aes, pw,isEncrypted,content);
			return;
		}
		receivedData = URLDecoder.decode(receivedData,"UTF-8");
		receivedData = receivedData.replaceAll(" ","+");
	
		String data = null;
		Request req = null;
		try {
			req = (Request) JsonUtility.parseJson(receivedData, Request.class);
			if(req!=null)
			{
				data = (String) req.getData();
				String fromEntity = req.getFromEntity();
				aes = EncryptionCache.getEncryptionUtility(fromEntity);
				logger.debug("data sent to service is:"+data);
				if(aes!=null)
					receivedData =  aes.decrypt(data);
				isEncrypted = true;
			}
			else
			{
				PrintWriter pw =  response.getWriter();
				Response resp = new Response();
				resp.setStatus(Constants.FAILED.name());
				resp.setMessage("Json Parsing error");
				ArrayList<String> a = new ArrayList<String>(1);
				a.add("E-101");
				resp.setResponseObject(a);
				String content = JsonUtility.convertToJson(resp) ;
				sendResponse(aes, pw,isEncrypted,content);
				return;
			}
		}
		catch(Exception e)
		{
			logger.debug("Exception while decrypting data in DoFilter of EncryptionFilter class",e);
			receivedData = receivedData.substring(receivedData.indexOf("data")+6, receivedData.length()-1);
		}
		logger.debug("data sent to service is:"+receivedData);
		
		HashMap<String, String> requestParam = new HashMap<String, String>();
		requestParam.put("data", receivedData);
		requestParam.put("fromEntity", req.getFromEntity());
		if(req.getBankCode()!=null)
			requestParam.put("bankCode", req.getBankCode());
		
		MyHttpServletRequestWrapper requestWrapper = new MyHttpServletRequestWrapper( (HttpServletRequest)request,requestParam);
		MyHttpServletResponseWrapper responseWrapper = new MyHttpServletResponseWrapper((HttpServletResponse) response,ByteArrayOutputStream.class);
		// pass the request along the filter chain
		chain.doFilter(requestWrapper, responseWrapper);
		ByteArrayOutputStream baos = (ByteArrayOutputStream) responseWrapper.getRealOutputStream();
		// and make use of this
		if(baos!=null)
		{
			String content = baos.toString();
			PrintWriter pw =  response.getWriter();
			sendResponse(aes,pw,isEncrypted,content);
		}
	}
	
	private void sendResponse(AesUtil aes ,  PrintWriter pw, boolean isEncrypted, String content)
	{
		logger.info("response for returning to web service request is ----|"+content);
		if(isEncrypted)
		{
			String encryptedContent = aes.encrypt(content);
			logger.info("Encrypted response ----|"+encryptedContent);
			pw.print(encryptedContent);
			pw.flush();
		}
		else
		{
			pw.print(content);
			pw.flush();
		}
	}

	/**	
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
