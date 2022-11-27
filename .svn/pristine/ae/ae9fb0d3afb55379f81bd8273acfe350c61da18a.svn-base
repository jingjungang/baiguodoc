package com.ukang.baiyu.table;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 新闻实体类
 * @author SAN
 *
 */
@Table(name = "news")
public class News {
	@Id
	@Column(column = "id")
	private Integer id;
	@Column(column = "msg_id")
	private Long msg_id;
	@Column(column = "title")
	private String title;
	@Column(column = "content")
	private String content;
	@Column(column = "activity")
	private String activity;
	//已读未读标识，0：未读；1：已读
	@Column(column = "readState")
	private int readState;
	@Column(column = "update_time")
	private String update_time;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getMsg_id() {
		return msg_id;
	}

	public void setMsg_id(Long msg_id) {
		this.msg_id = msg_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public int getReadState() {
		return readState;
	}

	public void setReadState(int readState) {
		this.readState = readState;
	}

}
