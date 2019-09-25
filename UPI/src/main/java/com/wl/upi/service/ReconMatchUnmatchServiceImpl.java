package com.wl.upi.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wl.upi.dao.UPITransactionDAO;
import com.wl.upi.model.TxnDTO;



@Service
public class ReconMatchUnmatchServiceImpl implements ReconMatchUnmatchService{
	
	private static Logger logger = LoggerFactory.getLogger(ReconMatchUnmatchServiceImpl.class);
	
	@Autowired
	private UPITransactionDAO transactionDAO;

	@Override
	public List<TxnDTO> listOfLastThreeDaysTransaction(String bankCode) {
		    logger.info("listOfLastThreeDaysTransaction : BANKCODE is "+bankCode);
		    SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
	        Calendar fromDate=Calendar.getInstance();
	        Calendar toDate=Calendar.getInstance();
	        dateFormat.format(fromDate.getTime());
	        System.out.println(dateFormat.format(fromDate.getTime()));
	        //fromDate.add(Calendar.DATE, -1);
	       // System.out.println(dateFormat.format(calendar.getTime()));
	       // String fromDate=dateFormat.format(calendar.getTime());
	        toDate=Calendar.getInstance();
	        toDate.add(Calendar.DATE, -3);
	        dateFormat.format(toDate.getTime());
	        System.out.println(dateFormat.format(toDate.getTime()));
	       // System.out.println(dateFormat.format(calendar.getTime()));
	       // String toDate=dateFormat.format(calendar.getTime());
	       logger.info("To and From dates are respectively  "+toDate.getTime() + "  "+fromDate.getTime());
	       List<TxnDTO> list=transactionDAO.getTransactionBtw(new java.sql.Date(toDate.getTime().getTime()),new java.sql.Date(fromDate.getTime().getTime()) 
	        		,bankCode);
        
		return list;
	}

}
