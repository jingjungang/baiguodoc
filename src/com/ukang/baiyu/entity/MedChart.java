package com.ukang.baiyu.entity;

import java.util.List;

public class MedChart {
	private String id;

	private String hcardnum;

	private String bnum;

	private String pname;

	private String snum;

	private String znum;

	private String sex;

	private String age;

	private String ftime;

	private String btime;

	private String zs;

	private String xdiagnosis;

	private String zdiagnosis;

	private String addtime;

	private String userid;

	private String status;

	private List<ImgInfo> imgss;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setHcardnum(String hcardnum) {
		this.hcardnum = hcardnum;
	}

	public String getHcardnum() {
		return this.hcardnum;
	}

	public void setBnum(String bnum) {
		this.bnum = bnum;
	}

	public String getBnum() {
		return this.bnum;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getPname() {
		return this.pname;
	}

	public void setSnum(String snum) {
		this.snum = snum;
	}

	public String getSnum() {
		return this.snum;
	}

	public void setZnum(String znum) {
		this.znum = znum;
	}

	public String getZnum() {
		return this.znum;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSex() {
		return this.sex;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getAge() {
		return this.age;
	}

	public void setFtime(String ftime) {
		this.ftime = ftime;
	}

	public String getFtime() {
		return this.ftime;
	}

	public void setBtime(String btime) {
		this.btime = btime;
	}

	public String getBtime() {
		return this.btime;
	}

	public void setZs(String zs) {
		this.zs = zs;
	}

	public String getZs() {
		return this.zs;
	}

	public void setXdiagnosis(String xdiagnosis) {
		this.xdiagnosis = xdiagnosis;
	}

	public String getXdiagnosis() {
		return this.xdiagnosis;
	}

	public void setZdiagnosis(String zdiagnosis) {
		this.zdiagnosis = zdiagnosis;
	}

	public String getZdiagnosis() {
		return this.zdiagnosis;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getAddtime() {
		return this.addtime;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserid() {
		return this.userid;
	}

	public void setImgs(List<ImgInfo> imgs) {
		this.imgss = imgs;
	}

	public List<ImgInfo> getImgs() {
		return this.imgss;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
}
