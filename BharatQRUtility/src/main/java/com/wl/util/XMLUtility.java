package com.wl.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XMLUtility {
	
	private static final Logger logger = LoggerFactory
			.getLogger(XMLUtility.class);

	public static String  marshal(Object obj, Class<?> clas)
	{
		try {
			StringWriter sw = new StringWriter();
			JAXBContext jaxbContext = JAXBContext.newInstance(clas);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(obj, sw);
			return sw.toString();
		} catch (JAXBException e) {
			logger.error("Exception while marshalling",e);
		}
		return null;
	}
	public static Object  unMarshal(String xml,Class<?> clas)
	{
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(clas);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			InputStream stream = new ByteArrayInputStream(xml.getBytes("utf-8"));
			Object obj = jaxbUnmarshaller.unmarshal(stream);
			return obj;
		} catch (JAXBException e) {
			logger.error("Exception while marshalling",e);
		} catch (UnsupportedEncodingException e) {
			logger.error("Exception while marshalling",e);
		}
		return null;
	}
}
