package com.ukang.baiyu.activity.consult;

import java.net.URL;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.common.DataParser;
import com.ukang.baiyu.entity.Response;
import com.ukang.baiyu.systembartint.SystemBarTintManager;
import com.ukang.baiyu.thread.XThread;
import com.ukang.baiyu.view.photo.util.Bimp;
import com.ukang.baiyu.view.slidingmenu.SlidingMenu;
import com.ukang.baiyu.view.swipebacklayout.lib.SwipeBackLayout;
import com.ukang.baiyu.view.swipebacklayout.lib.app.SwipeBackActivity;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.activity.login.LoginActivity;
import com.umeng.analytics.MobclickAgent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.util.Log;
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

/**
 * 评论列表
 * @author SAN
 */
public class CmtListActivity extends SwipeBackActivity {

	private static SystemBarTintManager tintManager;
	@ViewInject(R.id.iv_back)
	private ImageButton btnBack;
	@ViewInject(R.id.line_cmt)
	private LinearLayout lineCmt;
	@ViewInject(R.id.line_store)
	private LinearLayout lineStore;
	@ViewInject(R.id.sep_store)
	private View sepStore;
	@ViewInject(R.id.line_share)
	private LinearLayout lineShare;
	
	@ViewInject(R.id.et_comment)
	private EditText etComment;
	@ViewInject(R.id.btn_comment)
	private Button btnComment;
	@ViewInject(R.id.line_add_comment)
	private LinearLayout lineAddCmt;
	
	@ViewInject(R.id.web_view)
	private WebView webView;
	
	@ViewInject(R.id.line_content)
	private LinearLayout lineContent;
	@ViewInject(R.id.line_progress)
	private LinearLayout lineProgress;
	
	private SwipeBackLayout mSwipeBackLayout;
	private final String DOC_CMT_LIST_URL = Constant.HOST_ADDR + "/news/newscomment/";
	private final String REPORT_CMT_LIST_URL = Constant.HOST_ADDR + "/news/reportscomment/";
	private final String CONFERENCE_CMT_LIST_URL = Constant.HOST_ADDR + "/conference/meetingreport/";
	private final String LITERS_CMT_LIST_URL =  Constant.HOST_ADDR + "/pubmed/showliterscomment/";//文献评论列表
	private String browserUrl = "";
	private String id;
	private int newsKind;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_detail);
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
			browserUrl = DOC_CMT_LIST_URL + id;
			break;
		case 2:
			browserUrl = REPORT_CMT_LIST_URL + id;
			break;
		case 3:
			browserUrl = CONFERENCE_CMT_LIST_URL + id;
			break;
		default:
			browserUrl = DOC_CMT_LIST_URL + id;
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
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(this);
	}
	
	private void addClickListener(){
		btnBack.setOnClickListener(btnClick);
		lineShare.setOnClickListener(lineClick);
		lineStore.setVisibility(View.GONE);
		sepStore.setVisibility(View.GONE);
		btnComment.setOnClickListener(btnClick);
		lineCmt.setVisibility(View.GONE);
		lineShare.setVisibility(View.GONE);
		lineAddCmt.setVisibility(View.GONE);
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
	
	private OnClickListener btnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == btnBack){
				finish();
			}else if(v == btnComment){
				if(Constant.users.getUsername().equals(LoginActivity.GUEST_NAME)){
					Toast.makeText(CmtListActivity.this, "您现在是游客模式，请使用正式账号登录", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(CmtListActivity.this, LoginActivity.class);
					intent.putExtra("actionType", LoginActivity.ACTION_TYPE_NEED_LOGIN);
					startActivity(intent);
				}else
					confirmComments();
			}
		}
	};
	
	private OnClickListener lineClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		}
	};
	
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
		params.addBodyParameter("type", type + "");
		params.addBodyParameter("content", comment);
		XThread thread = new XThread(this, 0, params, Constant.ADD_COMMENT_URL, addHandler);
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
					Toast.makeText(CmtListActivity.this, R.string.comment_success, Toast.LENGTH_LONG).show();
//					finish();
				}else{
					Toast.makeText(CmtListActivity.this, response.getMsg(), Toast.LENGTH_LONG).show();
				}
				break;
			case -1:
				Toast.makeText(CmtListActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};
}
