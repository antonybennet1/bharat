package com.wl.qr.model;

import java.util.ArrayList;
import java.util.HashMap;

public class QRTags{
	private String url;
	private ArrayList<TagModel> qr_data;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public ArrayList<TagModel> getQr_data() {
		return qr_data;
	}
	public void setQr_data(ArrayList<TagModel> qr_data) {
		this.qr_data = qr_data;
	}
	
}
