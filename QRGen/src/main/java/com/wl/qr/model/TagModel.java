package com.wl.qr.model;
import java.util.ArrayList;

public class TagModel {
	private String tag;
	private int tag_length;
	private String json_mapping;
	private boolean mendatory;
	private String default_value;
	private ArrayList<TagModel> child_tags;
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int getTag_length() {
		return tag_length;
	}
	public void setTag_length(int tag_length) {
		this.tag_length = tag_length;
	}
	public String getJson_mapping() {
		return json_mapping;
	}
	public void setJson_mapping(String json_mapping) {
		this.json_mapping = json_mapping;
	}
	public boolean getMendatory() {
		return mendatory;
	}
	public void setMendatory(boolean mendatory) {
		this.mendatory = mendatory;
	}
	public String getDefault_value() {
		return default_value;
	}
	public void setDefault_value(String default_value) {
		this.default_value = default_value;
	}
	public ArrayList<TagModel> getChild_tags() {
		return child_tags;
	}
	public void setChild_tags(ArrayList<TagModel> child_tags) {
		this.child_tags = child_tags;
	}
}
