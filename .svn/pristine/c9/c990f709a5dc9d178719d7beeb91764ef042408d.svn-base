package com.ukang.baiyu.thread;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.ukang.baiyu.activity.R;

public class XThread extends Thread {
	private int position;
	private String keyword;
	private RequestParams params;
	private String url;
	private Handler handler;
	private HttpHandler mHttpHelper;
	private Activity mContext;
	private boolean isShowDia = false;
	
	public XThread(int _position, RequestParams _params, String _url, Handler _handler){ //无上下文
		this.position = _position;
		this.params = _params;
		this.url = _url;
		this.handler = _handler;
	}
	
	public XThread(Activity _context, int _position, RequestParams _params, String _url, Handler _handler){//有上下文
		this.mContext = _context;
		this.position = _position;
		this.params = _params;
		this.url = _url;
		this.handler = _handler;
	}
	
	public void run(){
		Looper.prepare();
		if(isShowDia) showDialog();
		mHttpHelper = new HttpUtils().send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

	        @Override
	        public void onStart() {
	        }

	        @Override
	        public void onLoading(final long total, final long current, boolean isUploading) {
	        	if(isShowDia && isUploading){
	        		Message message = Message.obtain();
		        	message.arg1 = (int) total;
		        	message.arg2 = (int) current;
		        	try{
//		        		refreshProgress.sendMessageDelayed(message, 300);
		        	}catch(Exception e){
		        		e.printStackTrace();
		        	}
	        	}
	        }

	        @Override
	        public void onSuccess(ResponseInfo<String> responseInfo) {
	        	if(isShowDia) cancelDialog();
	        	String result = responseInfo.result;
	        	System.out.println(result);
	        	Message message = Message.obtain();
	        	message.what = 0;
	        	message.obj = result;
	        	message.arg1 = position;
	        	handler.sendMessage(message);
	        }

	        @Override
	        public void onFailure(HttpException error, String msg) {
	        	if(isShowDia) cancelDialog();
	        	System.out.println("onFailure --> " + msg);
	        	Message message = Message.obtain();
	        	message.what = -1;
	        	message.obj = msg;
	        	handler.sendMessage(message);
	        }
		});
		Looper.loop();
	}
	
	private Handler refreshProgress = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			try{
				int total = msg.arg1;
				int current = msg.arg2;
				if(dialog != null){
					int progress = (int) (100 * current / total);
					System.out.println("P: " + progress);
					System.out.println(total + " " + current + " WOCAO!!!");
					dialog.setMessage("正在提交  已完成" + progress +"%");
					dialog.setProgress(progress);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	};
	
	private ProgressDialog dialog;
    private void showDialog(){
    	dialog = new ProgressDialog(mContext);
		dialog.setOnKeyListener(new OnKeyListener() {
			
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					System.out.println("onkey ： BACK");
					mHttpHelper.cancel();
					cancelDialog();
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

	public boolean isShowDia() {
		return isShowDia;
	}

	public void setShowDia(boolean isShowDia) {
		this.isShowDia = isShowDia;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
