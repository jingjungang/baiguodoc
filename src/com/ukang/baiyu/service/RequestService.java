package com.ukang.baiyu.service;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;

/**
 * 通用请求服务
 * @author SAN
 *
 */
public interface RequestService {
	/**
	 * https POST请求
	 * @param hc
	 * @param params
	 * @return
	 */
	public Object doHttpsPostRequest(HttpClient hc, List<NameValuePair> params, String url);
	
	/**
	 * https Get 请求
	 * @param hc
	 * @param params
	 * @return
	 */
	public Object doHttpsGetRequest(HttpClient hc, String url);
	
	/**
	 * http POST请求
	 * @param hc
	 * @param params
	 * @return
	 */
	public Object doHttpPostRequest(HttpClient hc, List<NameValuePair> params, String url);
	
	/**
	 * http Get 请求
	 * @param hc
	 * @param params
	 * @return
	 */
	public Object doHttpGetRequest(HttpClient hc, String url);
}
