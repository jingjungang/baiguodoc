package com.ukang.baiyu.table;

import java.util.Date;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 检索记录
 * @author SAN
 *
 */
@Table(name = "search_history")
public class SearchHistory {
	@Id
	@Column(column = "id")
	private Integer id;
	@Column(column = "title")
	private String title;
	@Column(column = "update_date")
	private Date update_date;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

}
