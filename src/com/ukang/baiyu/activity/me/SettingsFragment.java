package com.ukang.baiyu.activity.me;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.activity.login.LoginActivity;
import com.ukang.baiyu.activity.login.StartActivity;
import com.ukang.baiyu.activity.science.DbsearchActivity;
import com.ukang.baiyu.application.MWDApplication;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.common.DataParser;
import com.ukang.baiyu.entity.Response;
import com.ukang.baiyu.entity.UserInfo;
import com.ukang.baiyu.fragments.tools.BaseFragment;
import com.ukang.baiyu.thread.XThread;
import com.umeng.analytics.MobclickAgent;

/**
 * 我
 * 
 * @author SAN
 */
public class SettingsFragment extends BaseFragment implements Observer {

	/** 搜索 */
	@ViewInject(R.id.ib_search)
	private ImageButton btnSearch;
	@ViewInject(R.id.ib_modi_user)
	private ImageButton btnModiUser;

	/** 用户姓名 */
	@ViewInject(R.id.tv_user_name)
	private TextView tvUserName;
	/** 用户头像 */
	@ViewInject(R.id.iv_cus_pic)
	private ImageView ivManPic;

	/** 个人信息 */
	@ViewInject(R.id.line_mod_user)
	private LinearLayout line_mod_user;
	/** 消息中心 */
	@ViewInject(R.id.line_msg)
	private LinearLayout line_msg;
	/** 我的收藏 */
	@ViewInject(R.id.line_store)
	private LinearLayout line_store;
	/** 我的评论 */
	@ViewInject(R.id.line_cmt)
	private LinearLayout line_cmt;
	/** 我的检索记录 */
	@ViewInject(R.id.line_search_his)
	private LinearLayout line_search_his;
	/** 我的全文 */
	@ViewInject(R.id.line_my_all_art)
	private LinearLayout line_my_all_art;
	/** 我的出诊时间 */
	@ViewInject(R.id.line_out_time)
	private LinearLayout line_out_time;
	/** 修改密码 */
	@ViewInject(R.id.line_re_pwd)
	private LinearLayout line_re_pwd;
	/** 意见反馈 */
	@ViewInject(R.id.line_feedback)
	private LinearLayout line_feedback;
	/** 关于我们 */
	@ViewInject(R.id.line_aboutus)
	private LinearLayout line_aboutus;

	/** 退出登录 */
	@ViewInject(R.id.btn_logout)
	private Button btnLogout;

	/** 版本号 */
	@ViewInject(R.id.tv_version_code)
	private TextView tvVerCode;

	private Activity mContext;
	private boolean isEdit;
	Intent intent;

	public SettingsFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		System.out.println("mine create...");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MWDApplication ma = ((MWDApplication) getActivity().getApplication());
		ma.PageNotificationer.addObserver(this);
		View v = inflater.inflate(R.layout.settings, null);
		ViewUtils.inject(this, v);
		btnSearch.setOnClickListener(btnClick);
		btnModiUser.setOnClickListener(btnClick);
		btnModiUser.setVisibility(View.INVISIBLE);
		// btnTools.setOnClickListener(btnClick);

		line_mod_user.setOnClickListener(clickListener);
		line_msg.setOnClickListener(clickListener);
		line_store.setOnClickListener(clickListener);
		line_cmt.setOnClickListener(clickListener);
		line_search_his.setOnClickListener(clickListener);
		line_my_all_art.setOnClickListener(clickListener);
		line_out_time.setOnClickListener(clickListener);
		line_re_pwd.setOnClickListener(clickListener);
		line_feedback.setOnClickListener(clickListener);
		line_aboutus.setOnClickListener(clickListener);

		btnLogout.setOnClickListener(btnClick);

		tvVerCode.setText("当前版本：" + Constant.VERSION_CODE);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loadUserInfo();
		isEdit = false;
	}

	private void loadUserInfo() {
		MobclickAgent.onEvent(mContext, "PERSONAL_CENTER");
		RequestParams p = new RequestParams();
		p.addHeader("Cookie", Constant.sessionId);
		p.addHeader("User-Agent", Constant.USER_AGENT);
		p.addHeader("Cookie", Constant.sessionId);
		p.addBodyParameter("token", Constant.token);
		p.addBodyParameter("type", "1");//1医生2患者3临床实验
		XThread thread = new XThread(mContext, 0, p, Constant.USER_INFO_URL_1,
				userHandler);
		if (isEdit) {
			thread.setShowDia(true);
		}
		thread.start();
	}

	private Handler userHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				try {
					String result = msg.obj.toString();
					Response response = DataParser.parseAccountInfo(result);
					if (response.getRet() == 1) {
						if (isEdit) {
							Intent i = new Intent(mContext, UserActivity.class);
							i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(i);
						} else {
							showViewData();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case -1:
				Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		}
	};

	private void showViewData() {
		UserInfo u = Constant.userinfo;
		String username = null;
		if (Constant.users.getUsername().equals(LoginActivity.GUEST_NAME)) {
			username = "游客";
		} else
			username = (u.getNickname() == null || u.getNickname().equals("")) ? u
					.getMobile() : u.getNickname();
		tvUserName.setText(username);
		if (u.getAvatar() != null && !u.getAvatar().equals(""))
			imageLoader.displayImage(u.getAvatar(), ivManPic, options,
					animateFirstListener);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("ToolsFragment"); // 统计页面
		if (Constant.users.getUsername().equals(LoginActivity.GUEST_NAME)) {
			btnLogout.setText(R.string.login_now); 
		} else {
			btnLogout.setText(R.string.logout);
		}
		UserInfo u = Constant.userinfo;
		if (Constant.bitmap != null) {
			ivManPic.setImageBitmap(Constant.bitmap);
		} else if (u!=null && u.getAvatar() != null && !u.getAvatar().equals("")) {
			imageLoader.displayImage(u.getAvatar(), ivManPic, options,
					animateFirstListener);
		}
		refreshData();
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("ToolsFragment");
	}

	private void refreshData() {
		UserInfo u = Constant.userinfo;
		if (u != null) {
			String username = null;
			if (Constant.users.getUsername().equals(LoginActivity.GUEST_NAME)) {
				username = "游客";
			} else
				username = (u.getNickname() == null || u.getNickname().equals(
						"")) ? u.getMobile() : u.getNickname();
			try {
				tvUserName.setText(username);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.line_mod_user:
				Constant.bitmap = null;
				isEdit = true;
				if (Constant.users.getUsername().equals(
						LoginActivity.GUEST_NAME)) {
					Toast.makeText(getActivity(), "您现在是游客模式，请使用正式账号登录",
							Toast.LENGTH_LONG).show();
					Intent intent = new Intent(mContext, LoginActivity.class);
					intent.putExtra("actionType",
							LoginActivity.ACTION_TYPE_NEED_LOGIN);
					startActivity(intent);
				} else
					loadUserInfo();
				break;
			case R.id.line_msg:
				Toast.makeText(mContext, "敬请期待", Toast.LENGTH_SHORT).show();
				break;
			case R.id.line_store:
				Toast.makeText(mContext, "敬请期待", Toast.LENGTH_SHORT).show();
				// intent = new Intent(mContext, MyStoreActivity.class);
				// startActivity(intent);
				break;
			case R.id.line_cmt:
				Toast.makeText(mContext, "敬请期待", Toast.LENGTH_SHORT).show();
				// intent = new Intent(mContext, MyCommentActivity.class);
				// startActivity(intent);
				break;
			case R.id.line_search_his:
				Toast.makeText(mContext, "敬请期待", Toast.LENGTH_SHORT).show();
				// intent = new Intent(mContext, SearchActivity.class);
				// startActivity(intent);
				break;
			case R.id.line_my_all_art:
				Toast.makeText(mContext, "敬请期待", Toast.LENGTH_SHORT).show();
				// intent = new Intent(mContext, MyAllArtActivity.class);
				// startActivity(intent);
				break;
			case R.id.line_out_time:
				Toast.makeText(mContext, "敬请期待", Toast.LENGTH_SHORT).show();
				break;
			case R.id.line_re_pwd:
				if (Constant.users.getUsername().equals(
						LoginActivity.GUEST_NAME)) {
					Toast.makeText(getActivity(), "您现在是游客模式，请使用正式账号登录",
							Toast.LENGTH_LONG).show();
					Intent intent = new Intent(mContext, LoginActivity.class);
					intent.putExtra("actionType",
							LoginActivity.ACTION_TYPE_NEED_LOGIN);
					startActivity(intent);
				} else {
					intent = new Intent(mContext, UpdatePwdActivity.class);
					startActivity(intent);
				}
				break;
			case R.id.line_feedback:
				MobclickAgent.onEvent(mContext, "FEED_BACK");
				intent = new Intent(mContext, FeedBackActivity.class);
				startActivity(intent);
				break;
			case R.id.line_aboutus:
				intent = new Intent();
				intent.setClass(mContext, NewsDetailActivity2.class);
				intent.putExtra("NEWS_KIND", 2);
				startActivity(intent);
				break;
			}
		}
	};

	private OnClickListener btnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == btnSearch) {
				Intent intent = new Intent(mContext, DbsearchActivity.class);
				startActivity(intent);
			} else if (v == btnModiUser) {
				Constant.bitmap = null;
				isEdit = true;
				if (Constant.users.getUsername().equals(
						LoginActivity.GUEST_NAME)) {
					Toast.makeText(getActivity(), "您现在是游客模式，请使用正式账号登录",
							Toast.LENGTH_LONG).show();
					Intent intent = new Intent(mContext, LoginActivity.class);
					intent.putExtra("actionType",
							LoginActivity.ACTION_TYPE_NEED_LOGIN);
					startActivity(intent);
				} else
					loadUserInfo();
			} else if (v == btnLogout) {
				if (btnLogout.getText().toString().equals("退出登录")) {
					dialog();
				} else {
					MWDApplication myapp = (MWDApplication) mContext
							.getApplicationContext();
					myapp.DEPT_NAME = "";
					Constant.users.setUsername(LoginActivity.GUEST_NAME);
					Constant.userinfo.setAvatar("");

					myapp.WriteUser("", "", false);
					Constant.token = null;
					Constant.users = null;
					getActivity().finish();
					Intent intent = new Intent(mContext, LoginActivity.class);
					getActivity().startActivity(intent);
				}
			}
		}
	};

	private void dialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // 先得到构造器
		builder.setTitle("提示"); // 设置标题
		builder.setMessage("是否确认退出?"); // 设置内容
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // 设置确定按钮

					@Override
					public void onClick(DialogInterface dialog, int which) {
						MWDApplication myapp = (MWDApplication) mContext
								.getApplicationContext();
						myapp.WriteUser("", "", false);
						Constant.token = null;
						Constant.users = null;
						Constant.token = "";
						getActivity().finish();
						Intent intent = new Intent(mContext,
								LoginActivity.class);
						startActivity(intent);
						dialog.dismiss(); // 关闭dialog
					}
				});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // 设置取消按钮

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		// 参数都设置完成了，创建并显示出来
		builder.create().show();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		// ivManPic.setImageBitmap(Constant.bitmap);
		// UserInfo u = Constant.userinfo;
		// imageLoader.displayImage(u.getAvatar(), ivManPic, options,
		// animateFirstListener);
	}
}
