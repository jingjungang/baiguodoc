package com.ukang.baiyu.activity.science;

import java.net.URL;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.activity.R.color;
import com.ukang.baiyu.activity.R.id;
import com.ukang.baiyu.activity.R.layout;
import com.ukang.baiyu.activity.R.string;
import com.ukang.baiyu.systembartint.SystemBarTintManager;
import com.ukang.baiyu.view.swipebacklayout.lib.SwipeBackLayout;
import com.ukang.baiyu.view.swipebacklayout.lib.app.SwipeBackActivity;
import com.umeng.analytics.MobclickAgent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 科研
 * @author SAN
 *
 */
public class ScienceActivity extends Activity {
	
	private Context mContext;

	@ViewInject(R.id.iv_back)
	private ImageButton btnBack;
	@ViewInject(R.id.tv_title)
	private TextView tvTitle;
	@ViewInject(R.id.ib_title_right)
	private ImageButton btnSearch;
	
	@ViewInject(R.id.line_sci1)
	private LinearLayout lineSci1;
	@ViewInject(R.id.line_sci2)
	private LinearLayout lineSci2;
	@ViewInject(R.id.line_sci3)
	private LinearLayout lineSci3;
	@ViewInject(R.id.line_sci4)
	private LinearLayout lineSci4;
	@ViewInject(R.id.line_sci5)
	private LinearLayout lineSci5;
	
	private static SystemBarTintManager tintManager;
//	private SwipeBackLayout mSwipeBackLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.science);
		mContext = this;
//		mSwipeBackLayout = getSwipeBackLayout();
//		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.index_title_color);
		ViewUtils.inject(this);
		btnBack.setOnClickListener(btnClick);
		tvTitle.setText(R.string.keyan);
		btnSearch.setVisibility(View.VISIBLE);
		addClickListener();
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
		lineSci1.setOnClickListener(lineClick);
		lineSci2.setOnClickListener(lineClick);
		lineSci3.setOnClickListener(lineClick);
		lineSci4.setOnClickListener(lineClick);
		lineSci5.setOnClickListener(lineClick);
	}
	
	private OnClickListener btnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == btnBack) {
				finish();
			}
		}
	};
	
	private OnClickListener lineClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(mContext,
					NewsDetailActivity.class);
			String id = "1";
			if(v == lineSci1){
				id = "1";
			}else if(v == lineSci2){
				id = "3";
			}else if(v == lineSci3){
				id = "2";
			}else if(v == lineSci4){
				id = "4";
			}else if(v == lineSci5){
				id = "6";
			}
			intent.putExtra("id", id);
			intent.putExtra("NEWS_KIND", 4);
			startActivity(intent);
		}
	};
}
