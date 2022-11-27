package com.ukang.baiyu.activity.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.activity.consult.MainListFragment;
import com.ukang.baiyu.activity.me.SettingsFragment;
import com.ukang.baiyu.activity.patientevent.PatientMenuFragment;
import com.ukang.baiyu.activity.science.ScienceMenuFragment_new;
import com.ukang.baiyu.application.MWDApplication;
import com.ukang.baiyu.application.PagerObservered;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.common.UpdateManager;
import com.ukang.baiyu.systembartint.SystemBarTintManager;
import com.ukang.baiyu.util.animation.AnimationFactory;
import com.ukang.baiyu.view.indicator.Indicator;
import com.ukang.baiyu.view.indicator.IndicatorViewPager;
import com.ukang.baiyu.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;
import com.ukang.baiyu.widget.NoScrollViewPager;
import com.umeng.analytics.MobclickAgent;

/**
 * MainViewPager(主界面)
 * 
 * @author SAN
 */
public class TabMainActivity extends FragmentActivity {

	private IndicatorViewPager indicatorViewPager;

	private boolean isShowNav = true;// 是否显示顶部栏
	private static SystemBarTintManager tintManager;
	private View bottom;

	String result, result1;
	String Url = Constant.LUNBO_URL;
	String uri = Constant.HOST;
	List<Map<String, String>> list;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_tabmain);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		MWDApplication ma = ((MWDApplication) getApplication());
		ma.PageNotificationer = new PagerObservered();

		tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.index_title_color);

		NoScrollViewPager viewPager = (NoScrollViewPager) findViewById(R.id.tabmain_viewPager);
		Indicator indicator = (Indicator) findViewById(R.id.tabmain_indicator);
		bottom = findViewById(R.id.tabmain_indicator);
		indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
		indicatorViewPager
				.setAdapter(new MyAdapter(getSupportFragmentManager()));
		// 禁止viewpager的滑动事件
		viewPager.setNoScroll(true);
		// 设置viewpager保留界面不重新加载的页面数量
		viewPager.setOffscreenPageLimit(4);
		viewPager.setCurrentItem(1);
		// 默认是1,，自动预加载左右两边的界面。设置viewpager预加载数为0。只加载加载当前界面。
		// viewPager.setPrepareNumber(0);
		// viewPager.setPrepareNumber(1);

		// UmengUpdateAgent.update(this);
		// UmengUpdateAgent.setUpdateOnlyWifi(false);
		apkQingQiu();
		showList();
	}

	/**
	 * 开启线程访问网络得到数据更新UI
	 */
	private void apkQingQiu() {
		// 启动线程来执行任务
		new Thread() {

			public void run() {
				// 请求网络
				PostmessageAPK(Constant.APKurl);
				Message m = new Message();
				m.what = 2;
				// 发送消息到Handler
				handler.sendMessage(m);
			}
		}.start();
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: // 轮播
				try {
					JSONObject json = new JSONObject(result);
					JSONObject js = json.getJSONObject("info");
					JSONArray array = js.getJSONArray("info");
					list = new ArrayList<Map<String, String>>();
					for (int i = 0; i < array.length(); i++) {
						Map<String, String> map = new HashMap<String, String>();
						JSONObject item = array.getJSONObject(i);
						String img = item.has("img") ? item.getString("img")
								: "";
						String name = item.has("NAME") ? item.getString("NAME")
								: "";
						String id = item.has("id") ? item.getString("id") : "";
						map.put("img", uri + img);
						map.put("name", name);
						map.put("id", id);
						list.add(map);
					}
					Constant.setLunBoList(list);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 2: // 更新
				if (!TextUtils.isEmpty(result1)) {
					try {
						JSONObject json = new JSONObject(result1);
						if (json.getInt("vnum") > Constant.CODE) {
							String content = json.has("content") ? json
									.getString("content") : "";
							int size = json.has("size") ? json.getInt("size")
									: 0;
							new UpdateManager(TabMainActivity.this,
									Constant.APK_URL, content, size)
									.checkUpdateInfo();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {

				}

				break;
			}
		}
	};

	/**
	 * 获取列表
	 */
	public String PostmessageAPK(String Url) {

		try {
			URL url = new URL(Url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("Charset", "utf-8");
			String data = "type=" + URLEncoder.encode("1", "UTF-8") + "&test=1";
			conn.setRequestProperty("Content-Length",
					String.valueOf(data.getBytes().length));
			OutputStream os = conn.getOutputStream();
			os.write(data.getBytes());
			os.flush();
			InputStreamReader is = new InputStreamReader(conn.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(is);
			StringBuffer strBuffer = new StringBuffer();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				strBuffer.append(line);
			}
			result1 = strBuffer.toString();
			is.close();
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
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
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	/**
	 * 用户点击定制新闻的箭头按钮的监听函数
	 */
	public void navSelectClick() {
		if (isShowNav) {
			TranslateAnimation translateAnimation = AnimationFactory
					.getTranslateAnimation(0, 0, 0, bottom.getHeight(), 600);
			bottom.startAnimation(translateAnimation);
			translateAnimation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					bottom.setVisibility(View.GONE);
				}
			});
		} else {
			TranslateAnimation translateAnimation = AnimationFactory
					.getTranslateAnimation(0, 0, bottom.getHeight(), 0, 600);
			bottom.startAnimation(translateAnimation);
			bottom.setVisibility(View.VISIBLE);
		}
		isShowNav = !isShowNav;
	}

	/**
	 * 用户点击定制新闻的箭头按钮的监听函数
	 */
	public void hidDeptLayout() {
		if (!isShowNav) {
			TranslateAnimation translateAnimation = AnimationFactory
					.getTranslateAnimation(0, 0, bottom.getHeight(), 0, 600);
			bottom.startAnimation(translateAnimation);
			bottom.setVisibility(View.VISIBLE);
			isShowNav = true;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// if(!isShowNav){
			// hidDeptLayout();
			// return true;
			// }else{
			exitBy2Click();
			// }
		}
		// return super.onKeyDown(keyCode, event);
		return false;
	}

	/**
	 * 双击退出函数
	 */
	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {

				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
		} else {
			finish();
			System.exit(0);
		}
	}

	private class MyAdapter extends IndicatorFragmentPagerAdapter {

		private String[] tabNames = { "资讯", "患者事务", "学术科研", "我" }; // "会议",患者事务
																	// 我的患者
		private int[] tabIcons = { R.drawable.icon_1_selector,
				R.drawable.hzsw_selector, R.drawable.icon_4_selector,// R.drawable.icon_3_selector,
				R.drawable.icon_5_selector };// R.drawable.icon_2_selector,
		private LayoutInflater inflater;

		public MyAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
			inflater = LayoutInflater.from(getApplicationContext());
		}

		@Override
		public int getCount() {
			return tabNames.length;
		}

		@Override
		public View getViewForTab(int position, View convertView,
				ViewGroup container) {
			if (convertView == null) {
				convertView = (TextView) inflater.inflate(R.layout.tab_main,
						container, false);
			}
			TextView textView = (TextView) convertView;
			textView.setText(tabNames[position]);
			textView.setCompoundDrawablesWithIntrinsicBounds(0,
					tabIcons[position], 0, 0);
			// textView.setCompoundDrawables(null,
			// getResources().getDrawable(tabIcons[position]), null, null);
			return textView;
		}

		@Override
		public Fragment getFragmentForPage(int position) {
			Fragment mFragment = null;
			switch (position) {
			case 0:
				mFragment = new MainListFragment();
				break;
			// case 1: // 隐藏到学术研究
			// mFragment = new ConferenceFragment(); 2016年4月25日 景俊钢
			// mFragment = new MyPatientFragment();// PatientMenuFragment();
			// break;
			case 1:
				mFragment = new PatientMenuFragment();
				break;
			case 2:
				mFragment = new ScienceMenuFragment_new();
				break;
			case 3:
				mFragment = new SettingsFragment();
				break;
			default:
				mFragment = new MainListFragment();
				break;
			}
			return mFragment;
		}
	}

	/**
	 * 开启线程访问网络得到数据更新UI
	 */
	private void showList() {
		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					try {
						JSONObject json = new JSONObject(result);
						JSONObject js = json.getJSONObject("info");
						JSONArray array = js.getJSONArray("info");
						list = new ArrayList<Map<String, String>>();
						for (int i = 0; i < array.length(); i++) {
							Map<String, String> map = new HashMap<String, String>();
							JSONObject item = array.getJSONObject(i);
							String img = item.has("img") ? item
									.getString("img") : "";
							String name = item.has("NAME") ? item
									.getString("NAME") : "";
							String id = item.has("id") ? item.getString("id")
									: "";
							map.put("img", uri + img);
							map.put("name", name);
							map.put("id", id);
							list.add(map);
						}
						Constant.setLunBoList(list);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
		};
		// 启动线程来执行任务
		new Thread() {

			public void run() {
				// 请求网络
				Postmessage(Url);
				Message m = new Message();
				m.what = 1;
				// 发送消息到Handler
				handler.sendMessage(m);
			}
		}.start();
	}

	/**
	 * 获取列表
	 */
	public String Postmessage(String Url) {

		try {
			URL url = new URL(Url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("Charset", "utf-8");
			String data = "status=" + URLEncoder.encode("1", "UTF-8");
			conn.setRequestProperty("Content-Length",
					String.valueOf(data.getBytes().length));
			OutputStream os = conn.getOutputStream();
			os.write(data.getBytes());
			os.flush();
			InputStreamReader is = new InputStreamReader(conn.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(is);
			StringBuffer strBuffer = new StringBuffer();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				strBuffer.append(line);
			}
			result = strBuffer.toString();
			is.close();
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
