package com.ukang.baiyu.entity;

/**
 * 数据库搜索实体类
 * 
 * @author SAN
 * 
 */
public class SearchDB {
	private int type;
	private String id;
	private String title;
	private String description;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
