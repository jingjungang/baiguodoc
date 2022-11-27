package com.ukang.baiyu.activity.tools;


import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.common.DataParser;
import com.ukang.baiyu.entity.Response;
import com.ukang.baiyu.systembartint.SystemBarTintManager;
import com.ukang.baiyu.thread.XThread;
import com.ukang.baiyu.view.swipebacklayout.lib.SwipeBackLayout;
import com.ukang.baiyu.view.swipebacklayout.lib.app.SwipeBackActivity;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.activity.consult.CmtListActivity;
import com.ukang.baiyu.activity.login.LoginActivity;
import com.ukang.baiyu.activity.science.ReadPicDetailActivity;
import com.umeng.analytics.MobclickAgent;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class NewsDetailActivity3 extends SwipeBackActivity {

	private static SystemBarTintManager tintManager;
	private Context mContext;
	@ViewInject(R.id.iv_back)
	private ImageButton btnBack;
	@ViewInject(R.id.line_cmt)
	private LinearLayout lineCmt;
	@ViewInject(R.id.line_share)
	private LinearLayout lineShare;
	@ViewInject(R.id.tv_zan)
	private TextView tvZanTotal;
	
	@ViewInject(R.id.et_comment)
	private EditText etComment;
	@ViewInject(R.id.btn_comment)
	private Button btnComment;
	@ViewInject(R.id.tv_more)
	private TextView tvMore;
	@ViewInject(R.id.btn_zan)
	private ImageButton btnZan;
	
	@ViewInject(R.id.web_view)
	private WebView webView;
	
	@ViewInject(R.id.line_content)
	private LinearLayout lineContent;
	@ViewInject(R.id.line_progress)
	private LinearLayout lineProgress;
	
	private SwipeBackLayout mSwipeBackLayout;
	private final String READ_PIC_SHOW_URL = Constant.HOST_ADDR + "/about/show/";
	private String browserUrl = "";
	private String id;
	private int newsKind;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_detail);
		mContext = this;
		ViewUtils.inject(this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.index_title_color);
		mSwipeBackLayout = getSwipeBackLayout();
		mSwipeBackLayout.setScrimColor(Color.TRANSPARENT);
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		addClickListener();
		id = getIntent().getStringExtra("id");
//		browserUrl = id;
		newsKind = getIntent().getIntExtra("NEWS_KIND", 1);
		switch (newsKind) {
		case 1:
			browserUrl = READ_PIC_SHOW_URL + id;
			break;
		default:
			browserUrl = READ_PIC_SHOW_URL + id;
			break;
		}
		System.out.println(browserUrl);
		initWebView();
//		showHandler.sendEmptyMessage(0);
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
	    MobclickAgent.onResume(this);       //统计时长
		loadZanTotal();
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		webView.reload();
	}
	
	private void addClickListener(){
		btnBack.setOnClickListener(btnClick);
		lineShare.setOnClickListener(lineClick);
//		lineShoucan.setOnClickListener(lineClick);
		btnComment.setOnClickListener(btnClick);
		lineCmt.setVisibility(View.INVISIBLE);
		lineCmt.setOnClickListener(lineClick);
		lineShare.setVisibility(View.GONE);
		tvMore.setText("编辑");
		tvMore.setOnClickListener(btnClick);
		btnZan.setOnClickListener(btnClick);
	}

	/**
	 * 初始化webview
	 */
	private void initWebView() {
		// 支持缩放
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setSaveFormData(true);
		webView.clearCache(false);
        // 联网载入
		webView.loadUrl(browserUrl);
		// 设置
		webView.setWebViewClient(new WebViewClient() {

			/** 开始载入页面 */
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				setProgressBarIndeterminateVisibility(true);// 设置标题栏的滚动条开始
				Log.d("YM", "setProgressBarIndeterminateVisibility");
				browserUrl = url;
				super.onPageStarted(view, url, favicon);
			}

			/** 捕获点击事件*/
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				webView.loadUrl(url);
				return true;
			}

			/** 错误返回 */
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

			/** 页面载入完毕 */
			@Override
			public void onPageFinished(WebView view, String url) {
				setProgressBarIndeterminateVisibility(false);//ֹ
				super.onPageFinished(view, url);
				lineContent.setVisibility(View.VISIBLE);
				lineProgress.setVisibility(View.GONE);
			}

		});

		webView.setWebChromeClient(new WebChromeClient() {
			/** 进度条变化 */
			public void onProgressChanged(WebView view, int newProgress) {
				// 设置标题栏的滚动条停止
				getWindow().setFeatureInt(
						Window.FEATURE_PROGRESS, newProgress * 100);
				super.onProgressChanged(view, newProgress);
			}

			/** 设置标题 */
			public void onReceivedTitle(WebView view, String title) {
//				WebViewApp.this.setTitle(title);
				super.onReceivedTitle(view, title);
			}
		});
	}
	
	private void loadReadPicDetail(String id){
		RequestParams params = new RequestParams();
		params.addHeader("Cookie", Constant.sessionId);
		params.addBodyParameter("token", Constant.token);
		params.addBodyParameter("id", id);
		XThread thread = new XThread(this, 0, params, Constant.READ_PIC_DETAIL_URL, loadHandler);
		thread.setShowDia(true);
		thread.start();
	}
	
	private Handler loadHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String result = msg.obj.toString();
				Response response = DataParser.parseReadPicDetail(result);
				Constant.readpicResp = response ;
				if(response.getRet() == 1){
					Intent intent = new Intent(mContext, ReadPicDetailActivity.class);
					startActivity(intent);
				}else{
					Toast.makeText(mContext, "未查找到数据", Toast.LENGTH_LONG).show();
				}
				break;
			case -1:
				Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};
	
	private OnClickListener btnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == btnBack){
				finish();
			}else if(v == btnComment){
				if(Constant.users.getUsername().equals(LoginActivity.GUEST_NAME)){
					Toast.makeText(NewsDetailActivity3.this, "您现在是游客模式，请使用正式账号登录", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(NewsDetailActivity3.this, LoginActivity.class);
					intent.putExtra("actionType", LoginActivity.ACTION_TYPE_NEED_LOGIN);
					startActivity(intent);
				}else
					confirmComments();
			}else if(v == tvMore){
				loadReadPicDetail(id);
			} else if (v == btnZan) {
				pointZan();
			}
		}
	};
	
	private OnClickListener lineClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == lineCmt){
				if(Constant.users.getUsername().equals(LoginActivity.GUEST_NAME)){
					Toast.makeText(NewsDetailActivity3.this, "您现在是游客模式，请使用正式账号登录", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(NewsDetailActivity3.this, LoginActivity.class);
					intent.putExtra("actionType", LoginActivity.ACTION_TYPE_NEED_LOGIN);
					startActivity(intent);
				}else{
					Intent intent = new Intent(NewsDetailActivity3.this,
							CmtListActivity.class);
					intent.putExtra("id", id);
					intent.putExtra("NEWS_KIND", newsKind);
					startActivity(intent);
				}
			}
		}
	};

	private void loadZanTotal(){
		Handler zanHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
					case 0:
						String result = msg.obj.toString();
						Log.e(">>>>>>>>>", result);
						try {
							JSONObject obj = new JSONObject(result);
							int ret = obj.getInt("status");
							int number = obj.getInt("number");
							tvZanTotal.setText(String.valueOf(number));
						} catch (JSONException e) {
							e.printStackTrace();
						}
						break;
					case -1:
						break;
					default:
						break;
				}
			}
		};
		RequestParams params = new RequestParams();
		params.addHeader("Cookie", Constant.sessionId);
		params.addBodyParameter("token", Constant.token);
		params.addBodyParameter("id", id);
		int type = newsKind;
		switch (newsKind) {
			case 1:
				type = 1;
				break;
			case 2:
				type = 1;
				break;
			case 3:
				type = 2;
				break;
		}
		params.addBodyParameter("type", type + "");
		XThread thread = new XThread(this, 0, params, Constant.READ_PIC_DETAIL_URL, zanHandler);
		thread.start();
	}

	private void pointZan(){
		Handler zanHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
					case 0:
						String result = msg.obj.toString();
						Response response = DataParser.parseAddComment(result);
						if(response.getRet() == 1){
							Toast.makeText(mContext, R.string.zan_success, Toast.LENGTH_LONG).show();
						}else if(response.getRet() == 2){
							Toast.makeText(mContext, R.string.zan_cancel_success, Toast.LENGTH_LONG).show();
						}else{
							Toast.makeText(mContext, "未获取到数据", Toast.LENGTH_LONG).show();
						}
						break;
					case -1:
						Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
						break;
					default:
						break;
				}
			}
		};
		RequestParams params = new RequestParams();
		params.addHeader("Cookie", Constant.sessionId);
		params.addBodyParameter("token", Constant.token);
		params.addBodyParameter("id", id);
		int type = newsKind;
		switch (newsKind) {
			case 1:
				type = 1;
				break;
			case 2:
				type = 1;
				break;
			case 3:
				type = 2;
				break;
			case 4:
				type = 3;
				break;
		}
		params.addBodyParameter("type", type + "");
		XThread thread = new XThread(this, 0, params, Constant.DIAN_ZAN_URL, zanHandler);
		thread.setShowDia(true);
		thread.start();
	}
	
	private void confirmComments(){
		String comment = etComment.getText().toString();
		if(comment.trim().equals("")){
			return;
		}else{
			confirm(comment);
		}
	}
	
	private void confirm(String comment){
		RequestParams params = new RequestParams();
		params.addHeader("Cookie", Constant.sessionId);
		params.addBodyParameter("token", Constant.token);
		params.addBodyParameter("id", id);
		int type = 1;
		switch (newsKind) {
		case 1:
			type = 3;
			break;
		case 2:
			type = 2;
			break;
		case 3:
			type = 1;
			break;
		default:
			break;
		}
		params.addBodyParameter("content", comment);
		XThread thread = new XThread(this, 0, params, Constant.HOST_ADDR + "/filmreading/addcomment", addHandler);
		thread.setShowDia(true);
		thread.start();
	}
	
	private Handler addHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String result = msg.obj.toString();
				Response response = DataParser.parseAddComment(result);
				if(response.getRet() == 1){
					Toast.makeText(NewsDetailActivity3.this, R.string.comment_success, Toast.LENGTH_LONG).show();
					webView.reload();
				}else{
					Toast.makeText(NewsDetailActivity3.this, response.getMsg(), Toast.LENGTH_LONG).show();
				}
				break;
			case -1:
				Toast.makeText(NewsDetailActivity3.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if(webView.canGoBack()){
				webView.goBack();
			}else{
				webView.clearCache(true);
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
