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
import android.app.AlertDialog.Builder;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.common.DataParser;
import com.ukang.baiyu.common.MWDUtils;
import com.ukang.baiyu.entity.Response;
import com.ukang.baiyu.systembartint.SystemBarTintManager;
import com.ukang.baiyu.thread.MultiRequestThread;
import com.ukang.baiyu.thread.RequestThread;
import com.ukang.baiyu.utils.URLConnectionUtil;
import com.umeng.analytics.MobclickAgent;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.application.MWDApplication;

/**
 * 注册
 * 
 * @author SAN
 * 
 */
public class RegisterActivity extends Activity {
	private final String TAG = "RegisterActivity";

	@ViewInject(R.id.iv_back)
	private ImageButton btnBack;
	@ViewInject(R.id.btn_register)
	private Button btnRegister;// 登陆按钮
	@ViewInject(R.id.et_username)
	private EditText etUsername;
	@ViewInject(R.id.register_btn_yzm)
	private Button register_btn_yzm;// 获取验证码
	@ViewInject(R.id.register_et_yzm)
	private EditText register_et_yzm;
	@ViewInject(R.id.et_password)
	private EditText etPassword;// 用户名、密码
	@ViewInject(R.id.et_repassword)
	private EditText etRePassword;// 密码

	private Context mContext;
	private MultiRequestThread mThread;
	private RequestThread rThread;
	private Response response;

	private int actionType;
	Handler handler;
	String result;
	private Timer timer;
	private int timeCount = 180;
	private int mcount = 180;// 预设180秒

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		ViewUtils.inject(this);
		mContext = this;
		actionType = getIntent().getIntExtra("actionType", 0);
		addClickListener();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.index_title_color);
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					// LoginActivity.stopProgressDialog();
					try {
						JSONObject json = new JSONObject(result);
						int status = json.getInt("status");
						switch (status) {
						case 0:
							Toast.makeText(RegisterActivity.this, "手机号格式错误",
									Toast.LENGTH_SHORT).show();
							break;
						case -1:
							Toast.makeText(RegisterActivity.this, "验证码为空",
									Toast.LENGTH_SHORT).show();
							break;
						case -2:
							Toast.makeText(RegisterActivity.this,
									"信息填写与获取验证码时候不一致", Toast.LENGTH_SHORT)
									.show();
							break;
						case -3:
							Toast.makeText(RegisterActivity.this, "密码为空",
									Toast.LENGTH_SHORT).show();
							break;
						case -4:
							Toast.makeText(RegisterActivity.this, "更新失败",
									Toast.LENGTH_SHORT).show();
							break;
						case 1:
							Toast.makeText(RegisterActivity.this, "修改成功",
									Toast.LENGTH_SHORT).show();
							finish();
							break;
						}
					} catch (JSONException e) {
					}
					break;
				case 2:
					// LoginActivity.stopProgressDialog();
					try {
						JSONObject json = new JSONObject(result);
						int status = json.getInt("status");
						switch (status) {
						case 0:
							Toast.makeText(RegisterActivity.this, "手机号格式错误",
									Toast.LENGTH_SHORT).show();
							break;
						case -1:
							Toast.makeText(RegisterActivity.this,
									"手机号不存在（未注册）", Toast.LENGTH_SHORT).show();
							break;
						case -2:
							Toast.makeText(RegisterActivity.this, "三分钟只能发送一次",
									Toast.LENGTH_SHORT).show();
							break;
						case 1:
							Toast.makeText(RegisterActivity.this, "发送成功",
									Toast.LENGTH_SHORT).show();
							break;
						}
					} catch (JSONException e) {
					}
					break;
				}
			};
		};
		// Dialog();
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

	private void addClickListener() {
		btnRegister.setOnClickListener(loginClick);
		btnBack.setOnClickListener(btnClick);
		register_btn_yzm.setOnClickListener(btnClick);
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
		dialog.setMessage("正在注册");
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

	private OnClickListener loginClick = new OnClickListener() {

		public void onClick(View v) {
			String username = etUsername.getText().toString();
			String password = etPassword.getText().toString();
			String rePassword = etRePassword.getText().toString();
			String yzm = register_et_yzm.getText().toString();
			if ("".equals(username.trim()) || "".equals(password.trim())
					|| "".equals(rePassword)) {
				Toast.makeText(mContext, "请输入用户名和密码", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			if (username.trim().length() != 11) {
				Toast.makeText(mContext, "手机格式不正确", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!password.equals(rePassword)) {
				Toast.makeText(mContext, "两次输入密码不一致，请重新输入", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			if (password.trim().length() < 6) {
				Toast.makeText(mContext, "密码长度不能少于6位", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			if (yzm.trim() == null && yzm.trim().equals("")) {
				Toast.makeText(mContext, "验证码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if (MWDUtils.isNetworkConnected(mContext)) {
				showDialog();
				Constant.sessionId = null;
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("mobile", username));
				params.add(new BasicNameValuePair("password", password));
				params.add(new BasicNameValuePair("code", yzm));
				params.add(new BasicNameValuePair("type", "1")); // 1医生2患者3临床实验
				rThread = new RequestThread(params, "http", "post",
						Constant.REGISTER_URL, loginHandler);
				rThread.start();
			}
		}
	};

	private OnClickListener btnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String mobile = etUsername.getText().toString().trim();
			// TODO Auto-generated method stub
			if (v == btnBack) {
				onBackPressed();
			} else if (v == register_btn_yzm) {
				if (mobile.length() < 11) {
					Toast.makeText(RegisterActivity.this, "手机号格式不正确",
							Toast.LENGTH_SHORT).show();
					return;
				}
				// LoginActivity.startProgressDialog(this);
				new Verification(mobile).start();
				startTimer();
			}
		}
	};

	/** 验证码请求 */
	class Verification extends Thread {
		String mobile;

		public Verification(String mobile) {
			super();
			this.mobile = mobile;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("phone", mobile));
			parameters.add(new BasicNameValuePair("type", "1")); // 1医生2患者3临床
			result = URLConnectionUtil.HttpClientPost(
					Constant.REGISTER_YZM_URL, parameters);
			Message msg = new Message();
			msg.what = 2;
			handler.sendMessage(msg);
			super.run();
		}
	}

	/** 验证button计时器 */
	private void startTimer() {
		if (timer == null) {
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

	private Handler timeHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			register_btn_yzm.setEnabled(false);
			register_btn_yzm.setText(timeCount + "秒");
			if (timeCount < 0) {
				timeCount = mcount;
				timer.cancel();
				timer = null;
				register_btn_yzm.setEnabled(true);
				register_btn_yzm.setText("验证码");
			}
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
							MWDApplication myapp = (MWDApplication) mContext
									.getApplicationContext();
							if (Constant.ISDEBUG)
								Log.d(TAG, "write user to pref...");
							myapp.WriteUser(etUsername.getText().toString(),
									"", false);
							Toast.makeText(myapp, "注册成功!", Toast.LENGTH_LONG)
									.show();
							// finish();
							myapp.WriteUser(etUsername.getText().toString(),
									etPassword.getText().toString(), true);
							Dialog();
						} else if (ret == 9) {
							updateHandler.sendEmptyMessage(0);
							return;
						} else if (ret == -1) {
							nameErrorHandler.sendEmptyMessageDelayed(0, 500);
						} else if (ret == -3) {
							alreadyExistlHandler
									.sendEmptyMessageDelayed(0, 500);
						} else if (ret == -5) {
							Toast.makeText(RegisterActivity.this, "验证码错误",
									Toast.LENGTH_LONG).show();
						} else {
							failHandler.sendEmptyMessage(0);
							return;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						failHandler.sendEmptyMessage(0);
						return;
					}
				} else {
					loginFailHandler.sendEmptyMessage(0);
					return;
				}
			}
			// goRequest();
		}
	};

	private Handler loginFailHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			cancelDialog();
			Toast.makeText(mContext, "登录失败，请重试", Toast.LENGTH_SHORT).show();
		}
	};

	private Handler alreadyExistlHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			cancelDialog();
			Toast.makeText(mContext, "该账号已被注册", Toast.LENGTH_SHORT).show();
		}
	};

	private Handler nameErrorHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			cancelDialog();
			Toast.makeText(mContext, "手机号格式错误", Toast.LENGTH_SHORT).show();
		}
	};

	public Handler failHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			cancelDialog();
			if (response != null) {
				Toast.makeText(mContext, "注册失败，请重试", Toast.LENGTH_SHORT).show();// response.getMsg()
			} else {
				Toast.makeText(mContext, "注册失败，请重试", Toast.LENGTH_SHORT).show();
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

	/**
	 * 完善信息
	 */
	AlertDialog mdialog = null;

	private void Dialog() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.dialog_notice, null);
		final Spinner spin = (Spinner) v.findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.doc_type_items,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(adapter);
		final EditText edt_hospital = (EditText) v.findViewById(R.id.hospital);
		final EditText edt_object = (EditText) v.findViewById(R.id.object);
		Builder dia = null;
		dia = new AlertDialog.Builder(mContext);
		dia.setTitle("请完善以下信息：").setIcon(android.R.drawable.ic_dialog_info)
				.setView(v)
				.setNegativeButton("跳过", new AlertDialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						setResult(9);
						RegisterActivity.this.finish();
						// mdialog.dismiss();
					}
				}).setPositiveButton("提交", new AlertDialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						String position = String.valueOf(spin
								.getSelectedItemPosition());
						String hospital = edt_hospital.getText().toString()
								.trim();
						String object = edt_object.getText().toString().trim();
						if (TextUtils.isEmpty(hospital)) {
							myDialogInfo("请填写医院!");
						} else if (TextUtils.isEmpty(object)) {
							myDialogInfo("请填写科室!");
						} else if (position.equals("0")) {
							myDialogInfo("请选择职称!");
						} else {
							saveBaseInfo(hospital, object, position);
						}
					}
				}).create();
		dia.setCancelable(false);
		mdialog = dia.show();
	}

	private void saveBaseInfo(String hospital, String subject, String job) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("mobile", etUsername.getText()
				.toString().trim()));
		// "18782927645"));
		params.add(new BasicNameValuePair("type", "1")); // 1医生端 2患者端 3临床研究
		params.add(new BasicNameValuePair("hospital", hospital));
		params.add(new BasicNameValuePair("subject", subject));
		params.add(new BasicNameValuePair("job", job));
		rThread = new RequestThread(params, "http", "post",
				Constant.SAVE_BASEINFOS_AFTER_REGISET, userHandler);
		rThread.start();
	}

	Handler userHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String result = msg.obj.toString();
				Response response = DataParser.parseUserInfo(result);
				if (response.getRet() == 1) {
					Toast.makeText(RegisterActivity.this, "提交成功",
							Toast.LENGTH_SHORT).show();
					setResult(9);
					RegisterActivity.this.finish();
				} else if (response.getRet() == -4) {
					myDialogInfo("提交失败");
				} else if (response.getRet() == -3) {
					myDialogInfo("提交失败");// 电话号码不存在
				}
				break;
			case -1:
				myDialogInfo("提交异常");// 电话号码不存在
				break;
			default:
				break;
			}
		}
	};

	private void myDialogInfo(String info) {
		new AlertDialog.Builder(mContext).setTitle("提示")
				.setIcon(android.R.drawable.ic_dialog_info).setMessage(info)
				.setNegativeButton("确定", new AlertDialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						mdialog.show();
					}
				}).show();
	}
}
