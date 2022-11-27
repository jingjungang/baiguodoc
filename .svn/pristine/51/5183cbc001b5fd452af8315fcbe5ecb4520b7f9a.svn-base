package com.ukang.baiyu.activity.science;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.fragments.tools.SearchNewsFragment;
import com.ukang.baiyu.systembartint.SystemBarTintManager;
import com.ukang.baiyu.thread.XThread;

public class SearchActivity extends FragmentActivity {

	public static final Integer SEARCH_NEWS = 1;//文献搜索
	public static final Integer SEARCH_DATABASE = 2;//数据库搜索
	
	private int searchKind;
	private static SystemBarTintManager tintManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.index_title_color);
		ViewUtils.inject(this);
		searchKind = getIntent().getIntExtra("searchKind", 1);
//		if(searchKind == 1){//文献搜索
//			initNewsLayout();
//		}else{//数据库搜索
			initDatabaseLayout();
//		}
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
	
	private void initNewsLayout(){
		Fragment mContent = new SearchNewsFragment();	
		FragmentTransaction transaction =  this.getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.main_frame, mContent);
		transaction.commit();
	}
	
	private void initDatabaseLayout(){
		Fragment mContent = new SearchFragment(searchKind);	
		FragmentTransaction transaction =  this.getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.main_frame, mContent);
		transaction.commit();
	}
	
	private void goRequest(CharSequence s){
		RequestParams params = new RequestParams();
		params.addHeader("Cookie", Constant.sessionId);
		params.addBodyParameter("token", Constant.token);
		params.addBodyParameter("q", s.toString());
		params.addBodyParameter("page", "1");
		XThread thread = new XThread(0, params, Constant.NEWS_SEARCH_URL, newsHandler);
		thread.start();
	}
	
	private Handler newsHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
		}
	};
}
