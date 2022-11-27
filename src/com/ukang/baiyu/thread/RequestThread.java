package com.ukang.baiyu.thread;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.ukang.baiyu.service.RequestService;
import com.ukang.baiyu.service.impl.RequestServiceImpl;
/**
 * 从哪来，回哪去
 * 通用网络请求线程，该线程负责发起网络请求
 * 统一返回值以Object方式
 * 通过handler将返回数据传回至调用方
 * @author SAN
 *
 */
@SuppressLint("DefaultLocale")
public class RequestThread extends Thread {
	private final String TAG = "RequestThread";
	private List<NameValuePair> params;
	private HttpClient hc;
	private Handler handler;
	private String httpKind;//请求类型：http/https
	private String httpMethod;//请求方法：get/post
	private String url;//url
	
	private boolean cancel = false;
	
	/**
	 * 用于post请求的构造方法
	 * @param context
	 * @param params
	 * @param httpKind http请求方式：http/https
	 * @param httpMethod 请求方法：get/post
	 * @param url
	 * @param handler
	 */
	public RequestThread(List<NameValuePair> params,String httpKind, String httpMethod, 
			String url, Handler handler){
		this.params = params;
		this.httpKind = httpKind;
		this.httpMethod = httpMethod;
		this.url = url;
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
	public RequestThread(String httpKind, String httpMethod, 
			String url, Handler handler){
		this.httpKind = httpKind;
		this.httpMethod = httpMethod;
		this.url = url;
		this.handler = handler;
	}

	public void run(){
		//请求服务类
		RequestService service = new RequestServiceImpl();
		Object obj = null;
		//调用请求方法并获得返回值
		if(httpKind.toLowerCase().equals("http")){
			if(httpMethod.toLowerCase().equals("get")){
				obj = service.doHttpGetRequest(hc, url);
			}else{
				obj = service.doHttpPostRequest(hc, params, url);
			}
		}else{
			if(httpMethod.toLowerCase().equals("get")){
				obj = service.doHttpsGetRequest(hc, url);
			}else{
				obj = service.doHttpsPostRequest(hc, params, url);
			}
		}
//		if(Constant.ISDEBUG) System.out.println("url: " + url);
		if(cancel) return;
		Message msg = Message.obtain();
		msg.obj = obj;
		if(obj == null) msg.what = -1;
		handler.sendMessageDelayed(msg, 200);
	}
	
	public void cannelRequest(){
//		System.out.println("RThread cannelRequest...");
//		Log.d(TAG, "Thread cannelRequest...");
		cancel = true;
		try{
			this.currentThread().interrupt();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
