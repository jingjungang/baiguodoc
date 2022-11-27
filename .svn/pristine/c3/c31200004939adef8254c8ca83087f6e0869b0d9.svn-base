package com.ukang.baiyu.thread;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;

import com.ukang.baiyu.service.RequestService;
import com.ukang.baiyu.service.impl.RequestServiceImpl;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
/**
 * 一次处理多个网络数据请求
 * 用途：在需要预加载或者初始化数据时使用<br/>
 * 统一返回值以Object方式
 * @author SAN
 *
 */
@SuppressLint("DefaultLocale")
public class MultiRequestThread extends Thread {
	private final String TAG = "MultiRequestThread";
	private List<List<NameValuePair>> params;
	private HttpClient hc;
	private Handler[] handlers;
	private Handler handler;
	private String[] httpKind;//请求类型：http/https
	private String[] httpMethod;//请求方法：get/post
	private String[] urls;//url数组
	
	private boolean cancel = false;
	
	/**
	 * 用于post请求的构造方法
	 * @param context
	 * @param params
	 * @param httpKind http请求方式：http/https
	 * @param httpMethod 请求方法：get/post
	 * @param url
	 * @param handler[]
	 */
	public MultiRequestThread(List<List<NameValuePair>> params,String[] httpKind, String[] httpMethod, 
			String[] urls, Handler handlers[], Handler handler){
		this.httpKind = httpKind;
		this.httpMethod = httpMethod;
		this.urls = urls;
		this.params = params;
		this.handlers = handlers;
		this.handler = handler;
	}
	
	/**
	 * 用于get请求的构造方法
	 * @param context
	 * @param httpKind http请求方式：http/https
	 * @param httpMethod 请求方法：get/post
	 * @param url
	 * @param handler
	 */
	public MultiRequestThread(String[] httpKind, String[] httpMethod, 
			String[] urls,List<List<NameValuePair>> params, Handler[] handlers, Handler handler){
		this.httpKind = httpKind;
		this.httpMethod = httpMethod;
		this.urls = urls;
		this.params = params;
		this.handlers = handlers;
		this.handler = handler;
	}

	public void run(){
		//请求服务类
		RequestService service = new RequestServiceImpl();
		Object obj = null;
		for(int i = 0; i < urls.length; i++){
			//调用请求方法并获得返回值
			if(httpKind[i].toLowerCase().equals("http")){
				if(httpMethod[i].toLowerCase().equals("get")){
					obj = service.doHttpGetRequest(hc, urls[i]);
				}else{
					obj = service.doHttpPostRequest(hc, params.get(i), urls[i]);
				}
			}else{
				if(httpMethod[i].toLowerCase().equals("get")){
					obj = service.doHttpsGetRequest(hc, urls[i]);
				}else{
					obj = service.doHttpsPostRequest(hc, params.get(i), urls[i]);
				}
			}
//			System.out.println("m thread return: " + obj);
			if(obj == null){
				break;
			}else{
				Message msg = Message.obtain();
				msg.obj = obj;
				handlers[i].sendMessage(msg);
			}
		}
		if(cancel) return;
		handler.sendEmptyMessage(0);
	}
	
	public void cannelRequest(){
//		System.out.println("MThread cannelRequest...");
//		Log.d(TAG, "Thread cannelRequest...");
		cancel = true;
		try{
			this.currentThread().interrupt();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
