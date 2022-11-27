package com.ukang.baiyu.activity.tools;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.baiyu.fragments.tools.AppLinkFragment;
import com.ukang.baiyu.systembartint.SystemBarTintManager;
import com.ukang.baiyu.activity.R;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

/**
 * 应用推荐
 * @author SAN
 *
 */
public class AppLinkActivity extends FragmentActivity {
	
	private static SystemBarTintManager tintManager;
//	private SwipeBackLayout mSwipeBackLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		ViewUtils.inject(this);
//		mSwipeBackLayout = getSwipeBackLayout();
//		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.index_title_color);
		Fragment mContent = new AppLinkFragment();	
		FragmentTransaction transaction =  this.getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.main_frame, mContent);
		transaction.commit();
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
	
}
