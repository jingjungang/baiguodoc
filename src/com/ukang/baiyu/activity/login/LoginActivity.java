package com.ukang.baiyu.activity.login;

import java.util.ArrayList;
import java.util.List;

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
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.common.DataParser;
import com.ukang.baiyu.common.MWDUtils;
import com.ukang.baiyu.common.SecurityEncode;
import com.ukang.baiyu.entity.Response;
import com.ukang.baiyu.entity.Users;
import com.ukang.baiyu.systembartint.SystemBarTintManager;
import com.ukang.baiyu.thread.MultiRequestThread;
import com.ukang.baiyu.thread.RequestThread;
import com.ukang.baiyu.thread.XThread;
import com.umeng.analytics.MobclickAgent;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.activity.main.TabMainActivity;
import com.ukang.baiyu.application.MWDApplication;

/**
 * 登陆
 * 
 * @author SAN
 * 
 */
public class LoginActivity extends Activity {
	private final String TAG = "LoginActivity";
	private final static int SCANNIN_GREQUEST_CODE = 1;

	public static final String GUEST_NAME = Constant.GUEST_MOBILE;
	public static final String GUEST_PWD = Constant.GUEST_PASSWORD;

	public static final int ACTION_TYPE_NORMAL = 0;
	public static final int ACTION_TYPE_NEED_LOGIN = 1;// 需要登录

	@ViewInject(R.id.iv_back)
	private ImageButton btnBack;
	@ViewInject(R.id.btn_login)
	private Button btnLogin;// 登陆按钮
	@ViewInject(R.id.username)
	private EditText etUsername;
	@ViewInject(R.id.password)
	private EditText etPassword;// 用户名、密码
	@ViewInject(R.id.cb_store_pwd)
	private CheckBox cbStorePwd;// 是否记住密码
	@ViewInject(R.id.tv_guest)
	private TextView tvGuest;
	@ViewInject(R.id.tv_register)
	private TextView tvRegister;
	@ViewInject(R.id.tv_get_back_password)
	private TextView tvGetBackPwd;

	private Context mContext;
	private MultiRequestThread mThread;
	private RequestThread rThread;
	private Response response;

	private int actionType;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_login);
		ViewUtils.inject(this);
		mContext = this;
		actionType = getIntent().getIntExtra("actionType", 0);
		initview();
		addClickListener();
		System.out.println("actionType: " + actionType);
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
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private void initview() {
		if (actionType != ACTION_TYPE_NEED_LOGIN) {
			btnBack.setVisibility(View.INVISIBLE);
		}
		etUsername.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				etPassword.setText("");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		System.out.println(actionType == ACTION_TYPE_NEED_LOGIN);
		if (Constant.users != null && actionType != ACTION_TYPE_NEED_LOGIN) {
			etUsername.setText(Constant.users.getUsername());
			etPassword.setText(Constant.users.getPassword());
		}
		if (actionType == ACTION_TYPE_NEED_LOGIN) {
			tvGuest.setVisibility(View.INVISIBLE);
		}
	}

	private void addClickListener() {
		btnBack.setOnClickListener(btnClick);
		btnLogin.setOnClickListener(loginClick);
		tvGuest.setOnClickListener(loginClick);
		tvRegister.setOnClickListener(registerClick);
		tvGetBackPwd.setOnClickListener(btnClick);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				etUsername.setText(bundle.getString("result"));
				// mImageView.setImageBitmap((Bitmap)
				// data.getParcelableExtra("bitmap"));
			}
			break;
		case 0:
			if (resultCode == 9) {
				SharedPreferences user = getSharedPreferences("user_info",
						MODE_PRIVATE);
				MWDApplication ma = (MWDApplication) getApplication();
				try {
					String name = SecurityEncode.decoderByDES(
							user.getString("username", ""), ma.LLKCKIO);
					String pa = SecurityEncode.decoderByDES(
							user.getString("password", ""), ma.LLKCKIO);
					login_after_register(name,pa);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		}
	}

	private ProgressDialog dialog;

	private void showDialog() {
		dialog = new ProgressDialog(mContext);
		dialog.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					System.out.println("onkey ： BACK");
					cancelRequest();
					return true;
				}
				return false;
			}
		});
		dialog.setMessage("正在登录");
		dialog.setCancelable(false);
		dialog.show();
	}

	private void cancelDialog() {
		if (dialog != null)
			dialog.dismiss();
	}

	private void cancelRequest() {
		if (dialog != null)
			dialog.dismiss();
		if (rThread != null)
			rThread.cannelRequest();
		if (mThread != null)
			mThread.cannelRequest();
	}

	private OnClickListener btnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == btnBack) {
				finish();
			} else if (v == tvGetBackPwd) {
				Intent intent = new Intent(mContext, GetBackPwdActivity.class);
				startActivity(intent);
			}
		}
	};

	private OnClickListener loginClick = new OnClickListener() {

		public void onClick(View v) {
			if (v == tvGuest) {
				RequestParams params = new RequestParams();
				params.addBodyParameter("type", "1");
				params.addBodyParameter("mobile", GUEST_NAME);
				params.addBodyParameter("password", GUEST_PWD);
				XThread thread = new XThread(LoginActivity.this, 0, params,
						Constant.LOGIN_URL, guestHandler);
				thread.setShowDia(true);
				thread.start();
			} else if (v == btnLogin) {
				String username = etUsername.getText().toString();
				String password = etPassword.getText().toString();
				if ("".equals(username.trim()) || "".equals(password.trim())) {
					Toast.makeText(mContext, "请输入用户名和密码", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (MWDUtils.isNetworkConnected(mContext)) {
					Constant.token = null;
					showDialog();
					Constant.sessionId = null;
					// guest guest baiyuguest
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("type", "1"));
					params.add(new BasicNameValuePair("mobile", username));
					params.add(new BasicNameValuePair("password", password));
					rThread = new RequestThread(params, "http", "post",
							Constant.LOGIN_URL, loginHandler);
					rThread.start();
				} else {
					Toast.makeText(mContext, "网络连接失败", Toast.LENGTH_SHORT)
							.show();
				}
			}
			// Intent i = new Intent(mContext, MainPageActivity.class);
			// startActivity(i);
			// finish();
		}
	};

	// 注册完后登录
	public void login_after_register(String username, String password) {
		if (MWDUtils.isNetworkConnected(mContext)) {
			Constant.token = null;
			showDialog();
			Constant.sessionId = null;
			// guest guest baiyuguest
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type", "1"));
			params.add(new BasicNameValuePair("mobile", username));
			params.add(new BasicNameValuePair("password", password));
			rThread = new RequestThread(params, "http", "post",
					Constant.LOGIN_URL, loginHandler);
			rThread.start();
		} else {
			Toast.makeText(mContext, "网络连接失败", Toast.LENGTH_SHORT).show();
		}
	}

	private Handler guestHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				try {
					response = DataParser.parseUserInfo((String) msg.obj);
					int ret = response.getRet();
					if (ret == 1) {
						Users user = new Users();
						user.setUsername(GUEST_NAME);
						user.setPassword(GUEST_PWD);
						Constant.users = user;
						Intent i = new Intent(mContext, TabMainActivity.class);
						startActivity(i);
						finish();
					} else { // -4密码错误
						Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT)
								.show();
					}
				} catch (Exception e) {

				}
				break;
			case -1:
				Toast.makeText(mContext, "网络连接失败", Toast.LENGTH_SHORT).show();// msg.obj.toString()
				break;
			default:
				break;
			}
		}
	};

	private OnClickListener registerClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.putExtra("actionType", actionType);
			startActivityForResult(i, 0);
		}
	};

	String toJson(String username, String password) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("username", username);
			obj.put("password", password);
			obj.put("versions", Constant.VERSION_CODE);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj.toString();
	}

	private Handler loginHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			cancelDialog();
			if (msg.what == -1) {
				failHandler.sendEmptyMessage(0);
				return;
			} else {
				String str = (String) msg.obj;
				if (Constant.ISDEBUG)
					Log.d(TAG, str);
				if (str != null && !str.equals("")) {
					try {
						response = DataParser.parseUserInfo(str);
						int ret = response.getRet();
						if (ret == 1) {
							JSONObject jo = new JSONObject(
									new JSONObject(str).getString("into"));
							String subject = jo.getString("subject");
							MWDApplication myapp = (MWDApplication) mContext
									.getApplicationContext();
							myapp.DEPT_NAME = subject;
							Users users = new Users();
							users.setUsername(etUsername.getText().toString());
							users.setPassword(etPassword.getText().toString());
							Constant.users = users;
							// Constant.userinfo = (UserInfo) response.getObj();
							if (Constant.ISDEBUG)
								Log.d(TAG, "write user to pref...");
							myapp.WriteUser(etUsername.getText().toString(),
									etPassword.getText().toString(),
									cbStorePwd.isChecked());
						} else {
							loginFailHandler.sendEmptyMessage(ret);
							return;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						failHandler.sendEmptyMessage(0);
						return;
					}
				} else {
					loginFailHandler.sendEmptyMessage(-5);
					return;
				}
			}
			// goRequest();
			if (actionType != ACTION_TYPE_NEED_LOGIN) {
				Intent i = new Intent(mContext, TabMainActivity.class);
				startActivity(i);
				MWDApplication ma = (MWDApplication) getApplication();
				// ma.PageNotificationer.notifition();
			}
			finish();
		}
	};

	private Handler loginFailHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			cancelDialog();
			String str = "";
			switch (msg.what) {
			case -1:
				str = "手机号格式错误";
				break;
			case -3:
				str = "手机号不存在";
				break;
			case -4:
				str = "密码错误";
				break;
			case -5:
				str = "登录失败";
				break;
			default:
				str = "用户名或密码错误";
				break;
			}
			Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
		}
	};

	public Handler failHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			cancelDialog();
			if (response != null) {
				Toast.makeText(mContext, response.getMsg(), Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(mContext, "登录失败，请重试", Toast.LENGTH_SHORT).show();
			}
		}
	};

	public Handler updateHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			showChooseDateDia();
		}
	};

	void showChooseDateDia() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
				.setTitle(R.string.update_notice)
				.setMessage(response.getMsg())
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Intent intent = new Intent(mContext,
								DownAPKService.class);
						intent.putExtra("apk_url", response.getObj().toString());
						startService(intent);
						Toast.makeText(mContext, "正在下载新版本，请稍后",
								Toast.LENGTH_SHORT).show();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						finish();
					}
				}).setCancelable(false);
		builder.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (actionType == ACTION_TYPE_NEED_LOGIN) {
				finish();
				return true;
			}
			// System.exit(0);
			moveTaskToBack(true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
