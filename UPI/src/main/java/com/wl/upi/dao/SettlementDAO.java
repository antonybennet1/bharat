package com.wl.upi.dao;

import com.wl.upi.model.SettlementDTO;

public interface SettlementDAO {
	
	/**
	 * Save the record in settlement table of Thinclient.
	 * @param bean save the instance of {@link SettlementDTO} into Thinclient Database settlement table. 
	 */
	public void save(SettlementDTO bean);
	
	/**
	 * Update the upi batch number in upi_batch_number table
	 */
	public void updateBatchNumber();
	
	/**
	 * get the upi batch number from upi_batch_number table
	 */
	public int getBatchNumber(String tid);

	public String getMRLBatchNumber();
	
}
