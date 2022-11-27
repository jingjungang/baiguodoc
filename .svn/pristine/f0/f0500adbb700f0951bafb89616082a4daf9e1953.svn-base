package com.ukang.baiyu.activity.main;

/**
 * 指示动画
 * 景俊钢
 * 2016年5月13日 11:02:16
 */
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.systembartint.SystemBarTintManager;

public class IndicatorActivity extends Activity {
	int click_count = 0; // 点击次数
	private static SystemBarTintManager tintManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		setTheme(R.style.Transparent);
		tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.transparent_background);

		setContentView(R.layout.activity_indicator);
		final TextView image = (TextView) findViewById(R.id.viewstub_iamgeview1);
		image.setBackgroundResource(R.drawable.indicator01);
		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (click_count == 0) {
					image.setBackgroundResource(R.drawable.indicator02);
				} else if (click_count == 1) {
					image.setBackgroundResource(R.drawable.indicator03);
				} else {
					IndicatorActivity.this.finish();
				}
				click_count++;
			}
		});
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
