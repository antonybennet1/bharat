package com.wl.util.model;

/**
 * Model for AggregatorDetails. Table involved are aggregator_details and merchant_aggregator_link.
 * @author kunal.surana
 *
 */
public class AggregatorDetails {
	private String aggrId;
	private String aggrName;
	private String url;
	private String merchantId;
	
	public String getAggrId() {
		return aggrId;
	}
	public void setAggrId(String aggrId) {
		this.aggrId = aggrId;
	}
	public String getAggrName() {
		return aggrName;
	}
	public void setAggrName(String aggrName) {
		this.aggrName = aggrName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the merchantId
	 */
	public String getMerchantId() {
		return merchantId;
	}
	/**
	 * @param merchantId the merchantId to set
	 */
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AggregatorDetails [aggrId=" + aggrId + ", aggrName=" + aggrName + ", url=" + url + ", merchantId="
				+ merchantId + "]";
	}
	
	
}
