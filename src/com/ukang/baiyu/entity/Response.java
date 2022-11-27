package com.ukang.baiyu.entity;

import java.util.List;
import java.util.Observable;

/**
 * 返回数据实体类
 * @author SAN
 *
 */
public class Response extends Observable{
	private int ret;
	private String msg;
	private List<?> list;
	private Object obj;
	public int getRet() {
		return ret;
	}
	public void setRet(int ret) {
		this.ret = ret;
	}
	public List<?> getList() {
		return list;
	}
	public void setList(List<?> list) {
		this.list = list;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
