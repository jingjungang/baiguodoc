package com.ukang.baiyu.entity;

public class Comment {
	private String id;

	private String linkid;

	private String content;

	private String add_time;

	private String type;

	private String title;

	private String username;

	private String avatar;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setLinkid(String linkid) {
		this.linkid = linkid;
	}

	public String getLinkid() {
		return this.linkid;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

	public String getAdd_time() {
		return this.add_time;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAvatar() {
		return avatar;
	}
}
