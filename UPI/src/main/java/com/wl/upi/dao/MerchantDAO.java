package com.wl.upi.dao;

import java.util.List;

import com.wl.upi.model.AggregatorDetails;
import com.wl.upi.model.DeviceDetails;
import com.wl.upi.model.MerchantDetail;

public interface MerchantDAO {
	/**
	 * Get Aggregator Details based on mid.
	 * @param mid mid as input
	 * @return the instance of {@link AggregatorDetails}. Returns null if no record found.
	 */
	public AggregatorDetails getAggregatorDetails(String mid);
	
	/**
	 * Get the list of device and its details from tid of merchant
	 * @param tid tid of merchant
	 * @return the list of {@link DeviceDetails}. Return empy list if no record found
	 */
	public List<DeviceDetails> getDeviceDetails(String tid);
	
	/**
	 * Gets merchant details from detail table based on MPAN or TID. Only one argument needs to be present and other should be null. Dynamically creates query based on input.
	 * @param mpan
	 * @param tid
	 * @return returns {@link MerchantDetail}
	 */
	public MerchantDetail getMerchantDetails(String mpan,String tid);
	
	/**
	 * Get the list of device and its details from tid of merchant
	 * @param tid tid of merchant
	 * @return the list of {@link DeviceDetails}. Return empy list if no record found
	 */
	public List<DeviceDetails> getAxisDeviceDetails(String tid);
}
