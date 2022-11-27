package com.ukang.baiyu.activity.login;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;




import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.common.DataParser;
import com.ukang.baiyu.common.MWDUtils;
import com.ukang.baiyu.entity.Response;
import com.ukang.baiyu.systembartint.SystemBarTintManager;
import com.ukang.baiyu.thread.XThread;
import com.umeng.analytics.MobclickAgent;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.application.MWDApplication;
/**
 * 找回密码
 * @author SAN
 *
 */
public class GetBackPwdActivity  extends Activity {
	private final String TAG = "RegisterActivity";
	
	@ViewInject(R.id.iv_back)
	private ImageButton btnBack;
	@ViewInject(R.id.tv_title)
	private TextView tvTitle;
	@ViewInject(R.id.btn_register)
	private Button btnRegister;//登陆按钮
	@ViewInject(R.id.et_mobile)
	private EditText etMobile; 
	@ViewInject(R.id.et_yzm)
	private EditText etYzm;
	@ViewInject(R.id.btn_yzm)
	private Button btnYzm;
	@ViewInject(R.id.et_password)
	private EditText etPassword;//用户名、密码
	
	private Context mContext;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_back_pwd);
        ViewUtils.inject(this);
        mContext = this;
        initview();
        addClickListener();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.index_title_color);
    }
    
    @TargetApi(19) 
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}
    
    public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
    
    private void initview(){
    	tvTitle.setText(R.string.get_back_password);
    }
    
    private void addClickListener(){
    	btnRegister.setOnClickListener(loginClick);
    	btnBack.setOnClickListener(btnClick);
    	btnYzm.setOnClickListener(btnClick);
    }
    
    private OnClickListener loginClick = new OnClickListener() {
		
		public void onClick(View v) {
			String mobile = etMobile.getText().toString();
			String yzm = etYzm.getText().toString();
			String password = etPassword.getText().toString();
			if(mobile.trim().length() < 11){
				Toast.makeText(mContext, "手机号格式不正确", Toast.LENGTH_SHORT).show();
				return;
			}
			if(yzm.trim().length() < 6){
				Toast.makeText(mContext, "验证码长度不够", Toast.LENGTH_SHORT).show();
				return;
			}
			if(password.trim().length() < 6){
				Toast.makeText(mContext, "验证码格式", Toast.LENGTH_SHORT).show();
				return;
			}
			if(MWDUtils.isNetworkConnected(mContext)){
				RequestParams params = new RequestParams();
				params.addBodyParameter("phone", mobile);
				params.addBodyParameter("code", yzm);
				params.addBodyParameter("password", password);
				params.addBodyParameter("type", "1");
				XThread thread = new XThread(GetBackPwdActivity.this, 0, params, Constant.YZM_PWD_URL, loginHandler);
//				thread.setShowDia(true);
				thread.start();
			}
		}
	};
	
	private OnClickListener btnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == btnBack){
				onBackPressed();
			}else if(v == btnYzm){
				String mobile = etMobile.getText().toString();
				if(mobile.trim().length() < 11){
					Toast.makeText(mContext, "手机号格式不正确", Toast.LENGTH_SHORT).show();
					return;
				}
				sendRequest();
				startTimer();
			}
		}
	};
	
	private Timer timer;
	private int timeCount = 60;
	
	private void startTimer(){
		if(timer == null){
			timer = new Timer();
		}
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				timeCount--;
				timeHandler.sendEmptyMessage(0);
			}
		}, 0, 1000);
	}
	
	private Handler timeHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			btnYzm.setEnabled(false);
			btnYzm.setText(timeCount + "秒");
			if(timeCount < 0){
				timeCount = 60;
				timer.cancel();
				timer = null;
				btnYzm.setEnabled(true);
				btnYzm.setText("验证码");
			}
		}
	};
	
	private void sendRequest(){
		RequestParams params = new RequestParams();
		params.addBodyParameter("phone", etMobile.getText().toString());
		params.addBodyParameter("type", "1");
		XThread thread = new XThread(GetBackPwdActivity.this, 0, params, Constant.YZM_URL, yzmHandler);
		thread.setShowDia(true);
		thread.start();
	}
	
	private Handler yzmHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what == -1){
				failHandler.sendEmptyMessage(0);
				return;
			}else{
				String str = (String) msg.obj;
				if(Constant.ISDEBUG)
					Log.d(TAG, str);
				if(str != null && !str.equals("")){
					try {
						Response response = DataParser.parseUserInfo(str);
						int ret = response.getRet();
						if(ret == 1){
							MWDApplication myapp = (MWDApplication) mContext.getApplicationContext();
							if(Constant.ISDEBUG)
								Log.d(TAG, "write user to pref...");
							myapp.WriteUser("", "", false);
							Toast.makeText(myapp, "验证码已发送", Toast.LENGTH_LONG).show();
						}else if(ret == 0){
							nameErrorHandler.sendEmptyMessage(0);
							return;
						}else if(ret == -1){
							notExistlHandler.sendEmptyMessage(0);
							return;
						}else if(ret == -2){
							yzmErrorHandler.sendEmptyMessage(0);
							return;
						}else{
							failHandler.sendEmptyMessage(0);
							return;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						failHandler.sendEmptyMessage(0);
						return;
					}
				}else{
					loginFailHandler.sendEmptyMessage(0);
					return;
				}
			}
		}
	};
	
	private Handler loginHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what == -1){
				failHandler.sendEmptyMessage(0);
				return;
			}else{
				String str = (String) msg.obj;
				if(Constant.ISDEBUG)
					Log.d(TAG, str);
				if(str != null && !str.equals("")){
					try {
						Response response = DataParser.parseUserInfo(str);
						int ret = response.getRet();
						if(ret == 1){
							MWDApplication myapp = (MWDApplication) mContext.getApplicationContext();
							if(Constant.ISDEBUG)
								Log.d(TAG, "write user to pref...");
							myapp.WriteUser("", "", false);
							Toast.makeText(myapp, "密码设置成功，请登录", Toast.LENGTH_LONG).show();
							finish();
						}else if(ret == 0){
							nameErrorHandler.sendEmptyMessage(0);
							return;
						}else if(ret == -1){
							notExistlHandler.sendEmptyMessage(0);
							return;
						}else if(ret == -2){
							yzmErrorHandler.sendEmptyMessage(0);
							return;
						}else{
							failHandler.sendEmptyMessage(0);
							return;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						failHandler.sendEmptyMessage(0);
						return;
					}
				}else{
					loginFailHandler.sendEmptyMessage(0);
					return;
				}
			}
//			goRequest();
		}
    };
    
    private Handler loginFailHandler = new Handler(){
    	@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Toast.makeText(mContext, "提交失败，请重试", Toast.LENGTH_SHORT).show();
    	}
    };
    
    private Handler notExistlHandler = new Handler(){
    	@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Toast.makeText(mContext, "手机号未注册", Toast.LENGTH_SHORT).show();
    	}
    };
    
    private Handler yzmErrorHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Toast.makeText(mContext, "验证码错误", Toast.LENGTH_SHORT).show();
		}
    };
    
    private Handler nameErrorHandler = new Handler(){
    	@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Toast.makeText(mContext, "手机号格式错误", Toast.LENGTH_SHORT).show();
    	}
    };
    
    public Handler failHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Toast.makeText(mContext, "手机号未注册", Toast.LENGTH_SHORT).show();
		}
    };
    
}
