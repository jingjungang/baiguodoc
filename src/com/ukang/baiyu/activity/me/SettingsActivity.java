package com.ukang.baiyu.activity.me;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.activity.science.SearchActivity;
import com.ukang.baiyu.systembartint.SystemBarTintManager;

/**
 * 工具箱
 * @author SAN
 *
 */
public class SettingsActivity extends FragmentActivity {
	
	@ViewInject(R.id.line_back)
	private LinearLayout lineBack;
	@ViewInject(R.id.iv_back)
	private ImageButton btnBack;
	@ViewInject(R.id.tv_title)
	private TextView tvTitle;
	
	@ViewInject(R.id.line_store)
	private LinearLayout lineStore;
	@ViewInject(R.id.line_comment)
	private LinearLayout lineComment;
	@ViewInject(R.id.line_search_his)
	private LinearLayout lineSearchHis;
//	@ViewInject(R.id.line_settings)
//	private LinearLayout lineSettings;
	
	private SystemBarTintManager tintManager;
//	private SwipeBackLayout mSwipeBackLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tools);
		ViewUtils.inject(this);
		initview();
		initViewData();
		addClickListener();
//		mSwipeBackLayout = getSwipeBackLayout();
//		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
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
		lineBack.setVisibility(View.VISIBLE);
	}
	
	private void initViewData(){
		tvTitle.setText(R.string.tools);
	}
	
	private void addClickListener(){
		btnBack.setOnClickListener(btnClick);
	}
	
	private OnClickListener btnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == btnBack){
				finish();
			}
		}
	};
	
	private OnClickListener lineClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent();
			if(v == lineStore){
				i.setClass(SettingsActivity.this, MyStoreActivity.class);
			}else if(v == lineComment){
				i.setClass(SettingsActivity.this, MyCommentActivity.class);
			}else if(v == lineSearchHis){
				i.setClass(SettingsActivity.this, SearchActivity.class);
//			}else if(v == lineSettings){
//				i.setClass(SettingsActivity.this, SettingsActivity.class);
			}
			startActivity(i);
		}
	};
}
