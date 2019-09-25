/**
 * 
 */
package com.wl.upi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.wl.upi.service.AxisPSPImpl;
import com.wl.upi.service.KotakPSPImpl;
import com.wl.upi.service.MultiBankPSPImpl;
import com.wl.upi.service.PNBPSPImpl;
import com.wl.upi.service.PSPTransactionService;
import com.wl.upi.service.SbiPSPImpl;
import com.wl.util.constants.ErrorMessages;
import com.wl.util.exceptions.ApplicationException;

/**
 * PSPServiceFactory is factory of Bank PSP implementation of {@link PSPTransactionService}. 
 * @author faizul.mahammad
 */
@Service("pspServiceFactory")
public class PSPServiceFactory {

	private static Logger logger = LoggerFactory.getLogger(PSPServiceFactory.class);

	@Autowired
	private ApplicationContext applicationContext; 

	/**
	 * Factory method for Bank PSP implementation of {@link PSPTransactionService}. 
	 * @param pspName name of bank PSP as present in the URL of worldline given to bank PSP
	 * @return returns the instance of bank wise implementation of {@link PSPTransactionService}
	 */
	public PSPTransactionService getPSPTxnService(String pspName) {
		PSPTransactionService pspService = null; 
		switch (pspName) {
		case "AXISPSP":
			pspService = applicationContext.getBean(AxisPSPImpl.class);
			break;
		case "SBIPSP":
			pspService = applicationContext.getBean(SbiPSPImpl.class);
			break;
		case "KOTAKPSP" : // KOTAK implementing WL specs
			pspService=applicationContext.getBean(KotakPSPImpl.class);
			break;
		case "UBIPSP":
		case "PNBPSP":
			pspService=applicationContext.getBean(PNBPSPImpl.class);
			break;
		case "YESPSP":
		case "BOBPSP":
		case "IDBIPSP":
		case "CBIPSP":
		case "SIBPSP":
		case "BOIPSP":
		case "CANARAPSP":
		case "OBCPSP":
		case "DBCPSP":
		case "INDUSPSP":
		case "BANDHANPSP":
			  pspService= applicationContext.getBean(MultiBankPSPImpl.class);
			  break;
		default:
			logger.debug("PSP name :"+pspName+" is not configured");
			throw new ApplicationException(ErrorMessages.INVALID_PSP.toString());
		}		
		return pspService;
	}
}
