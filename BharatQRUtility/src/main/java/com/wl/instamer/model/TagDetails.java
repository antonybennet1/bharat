package com.wl.instamer.model;

import java.util.HashMap;

public class TagDetails
{
	String tagId;
	String tagVal;
	String subTagYN;
	String tagDesc;
	HashMap<String,TagDetails> subTagDetailsMap;
	public String getTagId() {
		return tagId;
	}
	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
	public String getTagVal() {
		return tagVal;
	}
	public void setTagVal(String tagVal) {
		this.tagVal = tagVal;
	}
	public String getSubTagYN() {
		return subTagYN;
	}
	public void setSubTagYN(String subTagYN) {
		this.subTagYN = subTagYN;
	}
	public String getTagDesc() {
		return tagDesc;
	}
	public void setTagDesc(String tagDesc) {
		this.tagDesc = tagDesc;
	}
	public HashMap<String, TagDetails> getSubTagDetailsMap() {
		return subTagDetailsMap;
	}
	public void setSubTagDetailsMap(HashMap<String, TagDetails> subTagDetailsMap) {
		this.subTagDetailsMap = subTagDetailsMap;
	}
}