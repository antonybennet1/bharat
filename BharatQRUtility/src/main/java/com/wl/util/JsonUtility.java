package com.wl.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;

public class JsonUtility {

	private static final Logger logger = LoggerFactory.getLogger(JsonUtility.class);

	public static Object parseJson(String jsonInput, Class<?> t)
	{
		Object obj= null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.configure(Feature.ALLOW_SINGLE_QUOTES,true);
			mapper.setSerializationInclusion(Include.NON_NULL);
			obj = mapper.readValue(jsonInput, t);
		} catch (IOException e) {
			logger.error("Json Parsing error",e);
		}
		catch (Exception e) {
			logger.error("Json Parsing error",e);
		}
		return obj;
	}

	public static String convertToJson(Object obj)
	{
		ObjectMapper mapper = new ObjectMapper();
		String str = null;
		try {
			str = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			logger.error("Json processing error",e);
		}
		catch (Exception e) {
			logger.error("Json processing error",e);
		}
		return str;
	}
	
	public static String convertToJson(Object obj, boolean excludeNull)
	{
		ObjectMapper mapper = new ObjectMapper();
		String str = null;
		try {
			if(excludeNull)
				mapper.setSerializationInclusion(Include.NON_NULL);
			str = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			logger.error("Json processing error",e);
		}
		catch (Exception e) {
			logger.error("Json processing error",e);
		}
		return str;
	}

	public static List<String> getErrorCodes(String json)
	{
		ObjectMapper mapper = new ObjectMapper();
		try{
			JsonNode rootNode = mapper.readTree(json);
			JsonNode errorNode = rootNode.path("Error");
			
			JsonNode errorIdsNode =  errorNode.path("Ids");
			Iterator<JsonNode> elements = errorIdsNode.elements();
			List<String> errorCodes = new LinkedList<String>();
			while(elements.hasNext()){
				JsonNode errorCode = elements.next();
				errorCodes.add(errorCode.asText());
				//System.out.println("ErrorCode="+errorCode.asText());
			}

			return errorCodes;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			logger.error("Json processing error",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Json processing error",e);
		}
		return null;
	}


}
