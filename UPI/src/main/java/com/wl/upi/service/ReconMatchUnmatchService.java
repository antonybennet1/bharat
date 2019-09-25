package com.wl.upi.service;



import java.util.List;

import com.wl.upi.model.TxnDTO;

public interface ReconMatchUnmatchService {
	
  List<TxnDTO> listOfLastThreeDaysTransaction(String bankCode);	

}
