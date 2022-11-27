package com.ukang.baiyu.table;

import java.sql.Date;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 会议顶部栏目表
 * @author SAN
 *
 */
@Table(name = "conference_category")
public class ConferenceCategory {
	@Id
//	@Column(column = "id")
//	private Integer id;
	@Column(column = "conference_id")
	private String conferenceId;
	@Column(column = "name_en")
	private String nameEn;
	@Column(column = "name_cn")
	private String nameCn;
	@Column(column = "status")
	private int status;
	@Column(column = "update_date")
	private Date updateDate;
	
	public ConferenceCategory(){
		
	}
	
	public ConferenceCategory(String conferenceId, String nameCn, int status){
		this.conferenceId = conferenceId;
		this.nameCn = nameCn;
		this.status = status;
		this.updateDate = new Date(System.currentTimeMillis());
	}
	
//	public Integer getId() {
//		return id;
//	}
//	public void setId(Integer id) {
//		this.id = id;
//	}
	public String getNameEn() {
		return nameEn;
	}
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}
	public String getNameCn() {
		return nameCn;
	}
	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getConferenceId() {
		return conferenceId;
	}
	public void setConferenceId(String conferenceId) {
		this.conferenceId = conferenceId;
	}
}
