package com.ukang.baiyu.activity.tools;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;

import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.view.slidingmenu.SlidingMenu;
import com.ukang.baiyu.view.slidingmenu.app.SlidingFragmentActivity;

@SuppressLint("Instantiatable")
public class BaseActivity extends SlidingFragmentActivity {

	private int mTitleRes;
	protected Fragment mFrag;
	private Context mContext;

	public BaseActivity(int titleRes) {
		mTitleRes = titleRes;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setTitle(mTitleRes);

		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
//		if (savedInstanceState == null) {
//			FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
//			mFrag = new SampleListFragment(mContext, getSlidingMenu());
//			t.replace(R.id.menu_frame, mFrag);
//			t.commit();
//		} else {
//			mFrag = this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
//		}

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
//		sm.setShadowWidthRes(R.dimen.shadow_width);
//		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//		sm.setFadeEnabled(true);
		
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case android.R.id.home:
//			toggle();
//			return true;
//		case R.id.github:
//			Util.goToGitHub(this);
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getSupportMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
}
