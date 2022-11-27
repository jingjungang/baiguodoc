package com.ukang.baiyu.activity.conference;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.activity.main.TabMainActivity;
import com.ukang.baiyu.activity.science.DbsearchActivity;
import com.ukang.baiyu.activity.science.SearchActivity;
import com.ukang.baiyu.adapter.DepartmentAdapter;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.common.DataParser;
import com.ukang.baiyu.entity.CommonEntity;
import com.ukang.baiyu.entity.Response;
import com.ukang.baiyu.systembartint.SystemBarTintManager;
import com.ukang.baiyu.table.ConferenceCategory;
import com.ukang.baiyu.thread.XThread;
import com.ukang.baiyu.util.animation.AnimationFactory;
import com.ukang.baiyu.widget.PagerSlidingTabStrip;
import com.umeng.analytics.MobclickAgent;

public class ConferenceActivity extends FragmentActivity {

	@ViewInject(R.id.tv_title)
	private TextView tvTitle;
	// @ViewInject(R.id.ib_search)
	// private ImageButton ibSearch;
	@ViewInject(R.id.ib_show_grids)
	private ImageButton ibShowGrids;
	@ViewInject(R.id.ib_show_tabs)
	private ImageButton ibShowTabs;

	@ViewInject(R.id.view1)
	private View view1;
	@ViewInject(R.id.view2)
	private View view2;

	// view2 childs
	@ViewInject(R.id.line_container)
	private LinearLayout depContainer;
	@ViewInject(R.id.iv_back)
	private ImageButton iv_back;

	private ViewPager contentPager;
	private CPagerAdapter adapter;
	private PagerSlidingTabStrip tabs;
	private Activity mContext;
	private final long PERIOD = 5 * 60 * 1000;// 间隔时间2分钟
	private final long DELAY = 1 * 60 * 1000;// 延迟一分钟
	private int channelId;
	private boolean isNewSelectBtnDown = false;
	private String[] departNames = { "全部", "内科", "外科", "临床专科" };
	private static SystemBarTintManager tintManager;
	private DbUtils dbUtils;
	/** 初始化标题栏的list */
	private List<ConferenceCategory> categoryList = null;
	/** 初始化标题栏的英文名集合 */
	private List<String> nameList = null;

	public ConferenceActivity() {
		super();
	}

	public ConferenceActivity(int channelId) {
		this.channelId = channelId;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View rootView = View.inflate(this, R.layout.conference_main, null);
		setContentView(rootView);
		ViewUtils.inject(this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.index_title_color);

		tvTitle.setText(getString(R.string.tab_2));
		// ibSearch.setOnClickListener(btnClick);
		ibShowGrids.setOnClickListener(btnClick);
		ibShowTabs.setOnClickListener(btnClick);
		iv_back.setOnClickListener(btnClick);
		// *****
		mContext = ConferenceActivity.this;
		dbUtils = DbUtils.create(mContext);
		setPager(rootView);
		try {
			categoryList = dbUtils.findAll(Selector.from(
					ConferenceCategory.class).orderBy("update_date", false));
			nameList = new ArrayList<String>();
			if (categoryList != null && !categoryList.isEmpty()) {
				for (int i = 0; i < categoryList.size(); i++) {
					nameList.add(categoryList.get(i).getNameCn());
				}
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getDepartment();
		new ShowDepThread().start();
	}

	@SuppressWarnings("unchecked")
	private void initView2() {
		if (Constant.conferenceCategoryResp == null
				|| Constant.conferenceCategoryResp.getList() == null)
			return;
		List<CommonEntity> pList = (List<CommonEntity>) Constant.conferenceCategoryResp
				.getList();
		Collections.sort(pList, new ABCComparator());
		HashMap<String, List<CommonEntity>> cmap = (HashMap<String, List<CommonEntity>>) Constant.conferenceCategoryResp
				.getObj();
		for (int i = 0; i < pList.size(); i++) {
			LinearLayout lineDep = (LinearLayout) LayoutInflater.from(mContext)
					.inflate(R.layout.department, null);
			TextView tvName = (TextView) lineDep.findViewById(R.id.tv_name);
			tvName.setText(pList.get(i).getStr2());
			GridView gridView = (GridView) lineDep.findViewById(R.id.grid_view);
			List<CommonEntity> childList = cmap.get(pList.get(i).getStr1());
			// System.out.println("childList: " + childList);
			if (childList != null) {
				DepartmentAdapter depAdapter = new DepartmentAdapter(
						(List<CommonEntity>) childList,
						ConferenceActivity.this, nameList);
				gridView.setAdapter(depAdapter);
			}
			depContainer.addView(lineDep);
		}
	}

	class ABCComparator implements Comparator<CommonEntity> {

		public int compare(CommonEntity c1, CommonEntity c2) {
			return String.valueOf(c1.getStr1()).compareTo(
					String.valueOf(c2.getStr1()));
		}
	}

	private void setPager(View rootView) {
		contentPager = (ViewPager) rootView
				.findViewById(R.id.content_pager_detail);
		adapter = new CPagerAdapter(getSupportFragmentManager());
		contentPager.setAdapter(adapter);
		contentPager.setOffscreenPageLimit(10);
		// contentPager.setPageTransformer(true, new ZoomOutPageTransformer());
		tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs_detail);
		tabs.setTextColorResource(R.color.black_color);
		tabs.setDividerColorResource(R.color.gray_line);
		// tabs.setUnderlineColorResource(R.color.common_list_divider);
		tabs.setIndicatorColorResource(R.color.indicator_color);
		tabs.setUnderlineColorResource(R.color.indicator_color);
		tabs.setSelectedTextColorResource(R.color.index_title_color);
		tabs.setIndicatorHeight(5);
		tabs.setViewPager(contentPager);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("ConferenceFragment"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("ConferenceFragment");
	}

	private void loadAddNews(List<String> keyList) {
		if (keyList == null || keyList.isEmpty())
			return;
		for (int i = 0; i < keyList.size(); i++) {
			getConferenceList(keyList.get(i));
		}
	}

	private void getConferenceList(final String id) {
		RequestParams params = new RequestParams();
		params.addHeader("Cookie", Constant.sessionId);
		params.addBodyParameter("token", Constant.token);
		params.addBodyParameter("page", "1");
		params.addBodyParameter("id", id);
		Handler reportNewsHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					try {
						String result = msg.obj.toString();
						Response response = DataParser
								.parseConferenceList(result);
						System.out.println("map put id: " + id);
						Constant.conferenceMap.put(id, response);
					} catch (Exception e) {

					}
					break;
				case -1:
					// Toast.makeText(mContext, msg.obj.toString(),
					// Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}
		};
		XThread thread = new XThread(0, params, Constant.CONFERENCE_LIST_URL,
				reportNewsHandler);
		thread.start();
	}

	private void getDepartment() {
		RequestParams params = new RequestParams();
		params.addHeader("Cookie", Constant.sessionId);
		params.addBodyParameter("token", Constant.token);
		new XThread(0, params, Constant.CONFERENCE_CATEGORY_URL,
				departmentHandler).start();
		// Message msg = Message.obtain();
		// msg.what = 0;
		// msg.obj =
		// "{\"status\": 1,\"info\":{\"pinfo\":{\"p1\":\"内科\",\"p2\":\"外科\",\"p3\": \"专科\",\"p5\": \"药学\",\"p6\": \"中医/中药\",\"p7\": \"基础医学\",\"p8\": \"公共卫生\",\"p9\": \"教育/科研\",\"p10\":\"医药营销\",\"p11\":\"其他\"},\"cinfo\": {\"p1\": {\"03-2\": \"呼吸\",\"03-7\": \"神内\",\"03-4\": \"血液\",\"030805\": \"传染\",\"0311\": \"肝病\",\"030105\": \"房颤\",\"030103\": \"高血压\"},\"p2\": {\"04-1\": \"普外\",\"040401\": \"脑血管病\",\"0404991\": \"神外其他\"},\"p3\": {\"18\": \"麻醉疼痛\",\"25\": \"生殖医学\"},\"p5\": {\"13-1\": \"药理学\",\"13-2\": \"药剂学\"},\"p6\": {\"1901\": \"中医学\",\"1902\": \"中药学\"},\"p7\": {\"2003\": \"生理生化\",\"2005\": \"解剖/组织胚胎\",\"2006\": \"病理/病生理\"},\"p8\": {\"1208\": \"预防保健\",\"12-1\": \"卫生环境\",\"12-2\": \"食品卫生\"},\"p9\": {\"24\": \"法学巡讲\",\"1503\": \"论文写作\",\"15-1\": \"医学教育\",\"15-2\": \"卫生管理\"},\"p10\": {\"2101\": \"思享咖啡馆\",\"2102\": \"思齐俱乐部\"},\"p11\": {\"1701\": \"其他\"}}}}";
		// // msg.obj =
		// "{\"status\": 1,\"info\": [{\"all\": \"全部\"},{\"hematology\": \"血液科\",\"neurology\": \"神经科\",\"eye\": \"眼科\", \"gynecology\": \"妇产科\"},{ \"ent\": \"耳鼻喉科\",\"urology\": \"泌尿外科\",\"vascular\": \"血管外科\",\"rheumatology\": \"风湿科\"},{\"pediatrics\": \"儿科\",\"emergency\": \"急诊科\",\"anesthesiology\": \"麻醉科\",\"nursing\": \"护理科\"}]}";
		// departmentHandler.sendMessageDelayed(msg, 2500);
	}

	private Handler departmentHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String result = msg.obj.toString();
				Response response = DataParser.parseConferenceCategory(result);
				Constant.conferenceCategoryResp = response;
				break;
			case -1:
				// Toast.makeText(mContext, msg.obj.toString(),
				// Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};

	String toJson(String quickTime, String optionalTime) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("quick_time", quickTime);
			obj.put("optional_time", optionalTime);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj.toString();
	}

	private OnClickListener btnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// if (v == ibSearch) {
			// Intent i = new Intent(mContext, DbsearchActivity.class);
			// i.putExtra("searchKind", SearchActivity.SEARCH_NEWS);
			// startActivity(i);
			// } else
			if (v == ibShowGrids) {
				isNewSelectBtnDown = true;
				btnNewsSelectClick();
			} else if (v == ibShowTabs) {
				btnNewsSelectClick();
			} else if (v == iv_back) {
				finish();
			}
		}
	};

	/**
	 * 用户点击定制新闻的箭头按钮的监听函数
	 */
	public void btnNewsSelectClick() {
		if (isNewSelectBtnDown) {
			view2.setVisibility(View.VISIBLE);
			TranslateAnimation translateAnimation = AnimationFactory
					.getTranslateAnimation(0, 0, -view1.getHeight(), 0, 500);
			view2.startAnimation(translateAnimation);
			translateAnimation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					view1.setVisibility(View.GONE);
				}
			});
		} else {
			view1.setVisibility(View.VISIBLE);
			TranslateAnimation translateAnimation = AnimationFactory
					.getTranslateAnimation(0, 0, 0, -view2.getHeight(), 500);
			view2.startAnimation(translateAnimation);
			view2.setVisibility(View.GONE);
			categoryHandler.sendEmptyMessage(0);
		}
		isNewSelectBtnDown = !isNewSelectBtnDown;
		// ((TabMainActivity) mContext).navSelectClick();
	}

	private Handler categoryHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			addCategoryTitle();
		}
	};

	private void addCategoryTitle() {
		try {
			List<String> keyList = new ArrayList<String>();
			Map<String, String> newsKey = Constant.conferenceAddKey;
			for (Map.Entry<String, String> entry : newsKey.entrySet()) {
				System.out.println("add key: " + entry.getValue());
				if (nameList.contains(entry.getKey())) {
					continue;
				}
				keyList.add(entry.getKey());
				ConferenceCategory ci = dbUtils.findFirst(Selector.from(
						ConferenceCategory.class).where("conference_id", "=",
						entry.getKey()));
				if (ci == null) {
					ci = new ConferenceCategory(entry.getKey(),
							entry.getValue(), 1);
				} else {
					ci.setStatus(1);
					ci.setUpdateDate(new Date(System.currentTimeMillis()));
				}
				dbUtils.saveOrUpdate(ci);
				adapter.titles.add(entry.getValue());
				adapter.fList
						.add(new ConferenceListFragment(3, entry.getKey()));
				nameList.add(entry.getKey());
			}
			for (Map.Entry<String, String> entry : Constant.conferenceDelKey
					.entrySet()) {
				System.out.println("del key: " + entry.getValue());
				int index = nameList.indexOf(entry.getKey());
				System.out.println("index --> " + index);
				if (index < 0)
					continue;
				try {
					adapter.titles.remove(index);
					adapter.fList.remove(index);
					nameList.remove(index);
					ConferenceCategory ci = dbUtils.findFirst(Selector.from(
							ConferenceCategory.class).where("conference_id",
							"=", entry.getKey()));
					if (ci != null) {
						dbUtils.delete(ci);
					}
				} catch (Exception ee) {
					ee.printStackTrace();
				}
			}
			adapter.notifyDataSetChanged();
			tabs.notifyDataSetChanged();
			loadAddNews(keyList);
			Constant.conferenceAddKey.clear();
			Constant.conferenceDelKey.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class CPagerAdapter extends FragmentStatePagerAdapter {

		ArrayList<String> titles = new ArrayList<String>();
		ArrayList<Fragment> fList = new ArrayList<Fragment>();

		public CPagerAdapter(FragmentManager fm) {
			super(fm);
			initData();
		}

		private void initData() {
			List<String> keyList = new ArrayList<String>();
			titles.add("神内");
			keyList.add("03-2");
			if (categoryList == null || categoryList.size() < 4) {
				nameList = new ArrayList<String>();
				nameList.add("03-7");
				nameList.add("03-2");
				nameList.add("03-4");
				nameList.add("030805");
				titles.add("呼吸");
				titles.add("血液");
				titles.add("传染");
				fList.add(new ConferenceListFragment(3, "03-7"));
				fList.add(new ConferenceListFragment(3, "03-2"));
				fList.add(new ConferenceListFragment(3, "03-4"));
				fList.add(new ConferenceListFragment(3, "030805"));
				keyList.add("03-7");
				keyList.add("03-4");
				keyList.add("030805");
				ConferenceCategory cc1 = new ConferenceCategory("03-2", "呼吸", 1);
				ConferenceCategory cc3 = new ConferenceCategory("03-4", "血液", 1);
				ConferenceCategory cc4 = new ConferenceCategory("030805", "传染",
						1);
				try {
					// dbUtils.saveOrUpdate(cc2);
					dbUtils.saveOrUpdate(cc1);
					dbUtils.saveOrUpdate(cc3);
					dbUtils.saveOrUpdate(cc4);
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				nameList.add(0, "03-7");
				for (int i = 0; i < categoryList.size(); i++) {
					titles.add(categoryList.get(i).getNameCn());
					fList.add(new ConferenceListFragment(3, categoryList.get(i)
							.getConferenceId()));
					keyList.add(categoryList.get(i).getConferenceId());
				}
			}
			loadAddNews(keyList);
		}

		@Override
		public Fragment getItem(int position) {
			return fList.get(position);
		}

		@Override
		public int getCount() {
			return titles.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles.get(position);
		}

	}

	private Handler initView2Handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			initView2();
		}
	};

	class ShowDepThread extends Thread {

		public void run() {
			while (true) {
				if (Constant.conferenceCategoryResp == null) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						System.out.println("wocao...");
						e.printStackTrace();
					}
				} else {
					initView2Handler.sendEmptyMessage(0);
					break;
				}
			}
		}
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
