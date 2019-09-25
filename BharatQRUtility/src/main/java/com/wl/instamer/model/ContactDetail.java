package com.wl.instamer.model;

public class ContactDetail {
	private int detailid;
	private long marchantId;
	private String name;
	private String cityName;
	private String address;
	private String DBAname;
	private String contactPerson;
	private long contactNumber;
	public int getDetailid() {
		return detailid;
	}
	public void setDetailid(int detailid) {
		this.detailid = detailid;
	}
	public long getMarchantId() {
		return marchantId;
	}
	public void setMarchantId(long marchantId) {
		this.marchantId = marchantId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDBAname() {
		return DBAname;
	}
	public void setDBAname(String dBAname) {
		DBAname = dBAname;
	}
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public long getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(long contactNumber) {
		this.contactNumber = contactNumber;
	}
	
	
	

}
