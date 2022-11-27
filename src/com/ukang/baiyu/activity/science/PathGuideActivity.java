package com.ukang.baiyu.activity.science;


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
import com.ukang.baiyu.activity.R.color;
import com.ukang.baiyu.activity.R.id;
import com.ukang.baiyu.activity.R.layout;
import com.ukang.baiyu.activity.R.string;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
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
 * 路径指南
 * @author SAN
 *
 */
public class PathGuideActivity extends SwipeBackActivity {

	private static SystemBarTintManager tintManager;
	
	@ViewInject(R.id.iv_back)
	private ImageButton btnBack;
	@ViewInject(R.id.tv_title)
	private TextView tvTitle;
	
	@ViewInject(R.id.tv_pathway)
	private TextView tvPathWay;
	@ViewInject(R.id.tv_guide)
	private TextView tvGuide;
	
	@ViewInject(R.id.web_view)
	private WebView webView;
	
	private SwipeBackLayout mSwipeBackLayout;
	private final String PATH_WAY_LIST_URL = Constant.HOST_ADDR + "/pathway/lists";
	private final String GUIDE_URL = Constant.HOST_ADDR + "/guide/lists";
	private String browserUrl = "";
	private String id;
	private int newsKind;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pathway_guide);
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
		newsKind = getIntent().getIntExtra("NEWS_KIND", 1);
		switch (newsKind) {
		case 1:
			browserUrl = PATH_WAY_LIST_URL;
			tvTitle.setText(R.string.path_way);
			break;
		case 2:
			browserUrl = GUIDE_URL;
			tvTitle.setText(R.string.about_us);
			break;
		default:
			browserUrl = PATH_WAY_LIST_URL;
			break;
		}
		initWebView();
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
	
	private void addClickListener(){
		btnBack.setOnClickListener(btnClick);
		tvPathWay.setOnClickListener(btnClick);
		tvGuide.setOnClickListener(btnClick);
	}
	
	private OnClickListener btnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == btnBack){
				if(webView.canGoBack()){
					//2016年5月18日 14:44:53 景俊钢 返回改为返回上一页
					//webView.goBack();
					webView.clearCache(true);
					finish();
				}else{
//					webView.clearCache(true);
					finish();
				}
			} else if (v == tvPathWay) {
				browserUrl = PATH_WAY_LIST_URL;
				webView.loadUrl(browserUrl);
//				webView.reload();
				tvPathWay.setTextColor(getResources().getColor(R.color.index_title_color));
				tvGuide.setTextColor(getResources().getColor(R.color.common_black));
			} else if (v == tvGuide) {
				browserUrl = GUIDE_URL;
				webView.loadUrl(browserUrl);
//				webView.reload();
				tvGuide.setTextColor(getResources().getColor(R.color.index_title_color));
				tvPathWay.setTextColor(getResources().getColor(R.color.common_black));
			}
		}
	};

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
