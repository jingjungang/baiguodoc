package com.ukang.baiyu.activity.me;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.activity.R.color;
import com.ukang.baiyu.activity.R.id;
import com.ukang.baiyu.activity.R.layout;
import com.ukang.baiyu.activity.R.string;
import com.ukang.baiyu.activity.login.LoginActivity;
import com.ukang.baiyu.application.MWDApplication;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.common.DataParser;
import com.ukang.baiyu.common.MWDUtils;
import com.ukang.baiyu.entity.Response;
import com.ukang.baiyu.systembartint.SystemBarTintManager;
import com.ukang.baiyu.thread.XThread;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
/**
 * 注册
 * @author SAN
 *
 */
public class UpdatePwdActivity  extends Activity {
	private final String TAG = "ModiPwdActivity";
	
	@ViewInject(R.id.line_back)
	private LinearLayout lineBack;
	@ViewInject(R.id.tv_title)
	private TextView tvTitle;
	
	@ViewInject(R.id.btn_login)
	private Button btnLogin;//登陆按钮
	
	@ViewInject(R.id.old_password)
	private EditText etOldPwd;
	@ViewInject(R.id.password)
	private EditText etPwd;
	@ViewInject(R.id.re_password)
	private EditText etRePwd;//用户名、密码
	
	private Context mContext;
	
	private static SystemBarTintManager tintManager;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modi_pwd);
        ViewUtils.inject(this);
        mContext = this;
        initview();
        addClickListener();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		tintManager = new SystemBarTintManager(this);
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
    
    private void initview(){
    }
    
    private void addClickListener(){
    	lineBack.setVisibility(View.INVISIBLE);
    	tvTitle.setText(R.string.modi_password);
    	btnLogin.setOnClickListener(loginClick);
    }
    
    private OnClickListener loginClick = new OnClickListener() {
		
		public void onClick(View v) {
			final String oldPwd = etOldPwd.getText().toString();
			final String pwd = etPwd.getText().toString();
			final String rePwd = etRePwd.getText().toString();
			if(oldPwd.equals(pwd)){
				Toast.makeText(mContext, "新密码和旧密码不能一致！", Toast.LENGTH_SHORT).show();
				return;
			}
			if(oldPwd.trim().equals("")){
				Toast.makeText(mContext, "当前密码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if("".equals(pwd.trim())||"".equals(rePwd.trim()))
			{
				Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}else if(!pwd.equals(rePwd)){
				Toast.makeText(mContext, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
				return;
			}
			if(pwd.length() < 6){
				Toast.makeText(mContext, "新密码长度必须大于等于6位", Toast.LENGTH_SHORT).show();
				return;
			}
			if(MWDUtils.isNetworkConnected(mContext)){
				RequestParams params = new RequestParams();
				params.addHeader("User-Agent", Constant.USER_AGENT);
				params.addHeader("Cookie", Constant.sessionId);
				params.addBodyParameter("token", Constant.token);
				params.addBodyParameter("oldpassword", oldPwd);
				params.addBodyParameter("password", pwd);
				params.addBodyParameter("type", "1");
				XThread cThread = new XThread(UpdatePwdActivity.this, 0, params, Constant.UPDATE_PWD_URL, loginHandler);
				cThread.setShowDia(true);
				cThread.start();
			}
		}
	};
	
	String toJson(String pwd, String repwd){
		JSONObject obj = new JSONObject();
		try {
			obj.put("password", pwd);
			obj.put("re_password", repwd);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj.toString();
	}
	
	private Handler loginHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what == -1){
				failHandler.sendEmptyMessage(0);
				return;
			}else{
				try{
					String result = msg.obj.toString();
					Response response = DataParser.parseAccountInfo(result);
					if(response.getRet() == 1){
						Toast.makeText(mContext, "修改密码成功,请重新登录！", Toast.LENGTH_LONG).show();
						Constant.users.setPassword("");
						MWDApplication myapp = (MWDApplication) mContext.getApplicationContext();
						myapp.WriteUser(Constant.users.getUsername(), "", false);
						Intent intent = new Intent(mContext, LoginActivity.class);
						startActivity(intent);
						finish();
					}else if(response.getRet() == -1){
						Toast.makeText(mContext, "新旧密码一致！", Toast.LENGTH_LONG).show();
					}else{
						failHandler.sendEmptyMessage(0);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
    };
    
    private Handler loginFailHandler = new Handler(){
    	@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Toast.makeText(mContext, "登录失败，请重试", Toast.LENGTH_SHORT).show();
    	}
    };
    
    public Handler failHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Toast.makeText(mContext, "修改密码失败，请重试", Toast.LENGTH_SHORT).show();
		}
    };
	
}
