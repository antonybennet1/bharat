package com.wl.bharatqr.interceptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;

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

import com.wl.instamer.model.Request;
import com.wl.instamer.model.Response;
import com.wl.util.AesUtil;
import com.wl.util.EncryptionCache;
import com.wl.util.JsonUtility;
import com.wl.util.constants.Constants;

/**
 * Servlet Filter implementation class EncryptionFilter
 */
@WebFilter("/merchantOnboard")
public class EncryptionFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(EncryptionFilter.class);

	/**
	 * Default constructor. 
	 */
	public EncryptionFilter() {
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
		if(receivedData == null)
		{
			Response resp = new Response();
			resp.setStatus(Constants.FAILED.name());
			resp.setMessage("parm paramter is mandatory");
			String content = JsonUtility.convertToJson(resp) ;
			sendResponse(aes, response,isEncrypted,content);
			return;
		}
		receivedData = URLDecoder.decode(receivedData,"UTF-8");
		receivedData = receivedData.replaceAll(" ","+");

		String data = null;
		try {
			Request req = (Request) JsonUtility.parseJson(receivedData, Request.class);
			if(req!=null)
			{
				data = (String) req.getData();
				String bankCode = req.getBankCode();
				aes = EncryptionCache.getEncryptionUtility(bankCode);
				logger.debug("data sent to service is:"+data);
				if(aes!=null)
					receivedData =  aes.decrypt(data);
				isEncrypted = true;
			}
			else
			{
				Response resp = new Response();
				resp.setStatus(Constants.FAILED.name());
				resp.setMessage("Json Parsing error");
				ArrayList<String> a = new ArrayList<String>(1);
				a.add("E-101");
				resp.setResponseObject(a);
				String content = JsonUtility.convertToJson(resp) ;
				sendResponse(aes, response,isEncrypted,content);
				return;
			}
		}
		catch(Exception e)
		{
			logger.debug("Exception while decrypting data in DoFilter of EncryptionFilter class",e);
			receivedData = receivedData.substring(receivedData.indexOf("data")+6, receivedData.length()-1);
		}
		logger.debug("data sent to service is:"+receivedData);
		//HashMap<String, String> parm = new HashMap<String, String>();
		//parm.put("data", data);
		MyHttpServletRequestWrapper requestWrapper = new MyHttpServletRequestWrapper( (HttpServletRequest)request,receivedData);
		MyHttpServletResponseWrapper responseWrapper = new MyHttpServletResponseWrapper((HttpServletResponse) response,ByteArrayOutputStream.class);
		// pass the request along the filter chain
		chain.doFilter(requestWrapper, responseWrapper);
		ByteArrayOutputStream baos = (ByteArrayOutputStream) responseWrapper.getRealOutputStream();
		// and make use of this
		if(baos!=null)
		{
			String content = baos.toString();
			sendResponse(aes,response,isEncrypted,content);
		}
	}
	
	private void sendResponse(AesUtil aes ,  ServletResponse response, boolean isEncrypted, String content) throws IOException
	{
		PrintWriter pw =  response.getWriter();
		logger.info("response for returning to web service request is ----|"+content);
		if(isEncrypted)
		{
			String encryptedContent = aes.encrypt(content);
			logger.info("Encrypted response ----|"+encryptedContent);
			response.setContentLength(encryptedContent.length());			
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
