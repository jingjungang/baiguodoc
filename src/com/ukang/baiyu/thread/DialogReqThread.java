package com.ukang.baiyu.thread;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;

import com.ukang.baiyu.service.RequestService;
import com.ukang.baiyu.service.impl.RequestServiceImpl;
import com.ukang.baiyu.activity.R;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
/**
 * 从哪来，回哪去，自带正在加载进度界面
 * 通用网络请求线程，该线程负责发起网络请求
 * 统一返回值以Object方式
 * 通过handler将返回数据传回至调用方
 * @author SAN
 *
 */
@SuppressLint("DefaultLocale")
public class DialogReqThread extends Thread {
	private final String TAG = "RequestThread";
	private List<NameValuePair> params;
	private HttpClient hc;
	private Handler handler;
	private String httpKind;//请求类型：http/https
	private String httpMethod;//请求方法：get/post
	private String url;//url
	private Context mContext;
	
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
	public DialogReqThread(Context context, List<NameValuePair> params,String httpKind, String httpMethod, 
			String url, Handler handler){
		this.mContext = context;
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
	public DialogReqThread(Context context, String httpKind, String httpMethod, 
			String url, Handler handler){
		this.mContext = context;
		this.httpKind = httpKind;
		this.httpMethod = httpMethod;
		this.url = url;
		this.handler = handler;
	}

	public void run(){
		Looper.prepare();
		showHandler.sendEmptyMessage(0);
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
		if(cancel) return;
		Message msg = Message.obtain();
		msg.obj = obj;
		if(obj == null) msg.what = -1;
		handler.sendMessageDelayed(msg, 200);
		dismissHandler.sendEmptyMessage(0);
		Looper.loop();
	}
	
	@SuppressWarnings("static-access")
	public void cannelRequest(){
		try{
			this.currentThread().interrupt();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private ProgressDialog dialog;
    private void showDialog(){
    	dialog = new ProgressDialog(mContext);
		dialog.setOnKeyListener(new OnKeyListener() {
			
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					System.out.println("onkey ： BACK");
					cannelRequest();
		            return true;
		        }
				return false;
			}
		});
		dialog.setMessage(mContext.getString(R.string.loading));
		dialog.setCancelable(false);
		dialog.show();
    }
	
	private void cancelDialog(){
    	if(dialog != null){
    		dialog.dismiss();
    	}
    }
	
	private Handler showHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			showDialog();
		}
	};
	
	private Handler dismissHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			cancelDialog();
		}
	};
}
