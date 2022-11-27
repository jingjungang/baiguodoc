package com.ukang.baiyu.activity.consult;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.activity.main.TabMainActivity;
import com.ukang.baiyu.activity.science.DbsearchActivity;
import com.ukang.baiyu.application.MWDApplication;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.common.DataParser;
import com.ukang.baiyu.entity.Department;
import com.ukang.baiyu.entity.Response;
import com.ukang.baiyu.fragments.tools.NewsListFragment;
import com.ukang.baiyu.table.IndexCategory;
import com.ukang.baiyu.thread.XThread;
import com.ukang.baiyu.util.animation.AnimationFactory;
import com.ukang.baiyu.widget.PagerSlidingTabStrip;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("ValidFragment")
public class MainListFragment extends Fragment implements Observer {

	private static final String tag = "MainListFragment";
	@ViewInject(R.id.ib_search)
	private ImageButton ibSearch;
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

	private ViewPager contentPager;
	private mPagerAdapter adapter;
	private PagerSlidingTabStrip tabs;
	private Activity mContext;
	private final long PERIOD = 5 * 60 * 1000;// 间隔时间2分钟
	private final long DELAY = 1 * 60 * 1000;// 延迟一分钟
	private int channelId;
	private boolean isNewSelectBtnDown = false;
	private String[] departNames = { "全部", "内科", "外科", "其他" };

	private DbUtils dbUtils;
	/** 初始化标题栏的list */
	private List<IndexCategory> categoryList = null;
	/** 初始化标题栏的英文名集合 */
	private List<String> nameList = null;

	public MainListFragment() {
		super();
	}

	public MainListFragment(int channelId) {
		this.channelId = channelId;
	}

	View root;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("oncreateview...");
		root = inflater.inflate(R.layout.main_list, null);
		ViewUtils.inject(this, root);
		// MWDApplication ma = ((MWDApplication)
		// getActivity().getApplication());
		// ma.PageNotificationer.addObserver(this);
		ibSearch.setOnClickListener(btnClick);
		ibShowGrids.setOnClickListener(btnClick);
		ibShowTabs.setOnClickListener(btnClick);
		return root;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		dbUtils = DbUtils.create(mContext);
		try {
			categoryList = dbUtils.findAll(Selector.from(IndexCategory.class)
					.orderBy("update_date", false));
			nameList = new ArrayList<String>();
			if (categoryList != null && !categoryList.isEmpty()) {
				for (int i = 0; i < categoryList.size(); i++) {
					nameList.add(categoryList.get(i).getNameEn());
				}
			}
			System.out.println("NAME_LIST: " + nameList);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getDocNews();
		// getJournal();
		getDepartment();
		new ShowDepThread().start();
	}

	@SuppressWarnings("unchecked")
	private void initView2() {
		for (int i = 1; i < departNames.length; i++) {
			LinearLayout lineDep = (LinearLayout) LayoutInflater.from(mContext)
					.inflate(R.layout.department, null);
			TextView tvName = (TextView) lineDep.findViewById(R.id.tv_name);
			tvName.setText(departNames[i]);
			GridView gridView = (GridView) lineDep.findViewById(R.id.grid_view);
			if (Constant.depListResp != null
					&& Constant.depListResp.getList() != null) {
				DepartmentAdapter depAdapter = new DepartmentAdapter(
						(List<Department>) Constant.depListResp.getList()
								.get(i));
				gridView.setAdapter(depAdapter);
			}
			depContainer.addView(lineDep);
		}
		setPager(root);
	}

	private void setPager(View rootView) {
		contentPager = (ViewPager) rootView
				.findViewById(R.id.content_pager_detail);
		adapter = new mPagerAdapter(getChildFragmentManager());
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
		MobclickAgent.onPageStart("NewsListFragment"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("NewsListFragment");
	}

	RequestParams params = new RequestParams();

	private void getDocNews() {
		params.addHeader("Cookie", Constant.sessionId);
		params.addBodyParameter("token", Constant.token);
		params.addBodyParameter("page", "1");
		new XThread(0, params, Constant.DOCTOR_NEWS_URL, docNewsHandler)
				.start();
	}

	private void loadAddNews(List<String> keyList) {
		if (keyList == null || keyList.isEmpty())
			return;
		for (int i = 0; i < keyList.size(); i++) {
			getReportNews(keyList.get(i));
		}
	}

	private void getReportNews(final String office) {
		RequestParams params = new RequestParams();
		params.addHeader("Cookie", Constant.sessionId);
		params.addBodyParameter("token", Constant.token);
		params.addBodyParameter("page", "1");
		params.addBodyParameter("office", office);
		Handler reportNewsHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					try {
						String result = msg.obj.toString();
						Response response = DataParser.parseDocNews(result);
						Constant.newsMap.put(office, response);
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
		XThread thread = new XThread(0, params, Constant.REPORT_NEWS_URL,
				reportNewsHandler);
		thread.setKeyword(office);
		thread.start();
	}

	private void getJournal() {
		RequestParams params = new RequestParams();
		params.addHeader("Cookie", Constant.sessionId);
		params.addBodyParameter("token", Constant.token);
		new XThread(0, params, Constant.MEDICAL_LIST_URL, medicalNewsHandler)
				.start();
	}

	private void getDepartment() {
		RequestParams params = new RequestParams();
		params.addHeader("Cookie", Constant.sessionId);
		params.addBodyParameter("token", Constant.token);
		new XThread(0, params, Constant.REPORT_DEPARTMENTS_URL,
				departmentHandler).start();
	}

	private Handler docNewsHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				try {
					String result = msg.obj.toString();
					Response response = DataParser.parseDocNews(result);
					Constant.docNewsResp = response;
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

	private Handler medicalNewsHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				try {
					String result = msg.obj.toString();
					Response response = DataParser.parseMedicalList(result);
					Constant.medicalResp = response;
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

	private Handler departmentHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String result = msg.obj.toString();
				System.out.println("DEPT1: " + result);
				Response response = DataParser.parseDeplList(result);
				Constant.depListResp = response;
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
			if (v == ibSearch) {
				Intent i = new Intent(mContext, DbsearchActivity.class);
				// i.putExtra("searchKind", SearchActivity.SEARCH_NEWS);
				startActivity(i);
			} else if (v == ibShowGrids) {
				isNewSelectBtnDown = true;
				btnNewsSelectClick();
			} else if (v == ibShowTabs) {
				btnNewsSelectClick();
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
		((TabMainActivity) mContext).navSelectClick();
	}

	/**
	 * 隐藏部门列表布局
	 */
	public void hidDeptLayout() {
		if (isNewSelectBtnDown) {
			view1.setVisibility(View.VISIBLE);
			TranslateAnimation translateAnimation = AnimationFactory
					.getTranslateAnimation(0, 0, 0, -view2.getHeight(), 500);
			view2.startAnimation(translateAnimation);
			view2.setVisibility(View.GONE);
			isNewSelectBtnDown = false;
		}
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
			Map<String, String> newsKey = Constant.newsAddKey;
			for (Map.Entry<String, String> entry : newsKey.entrySet()) {
				System.out.println("add key: " + entry.getValue());
				if (nameList.contains(entry.getKey())) {
					continue;
				}
				keyList.add(entry.getKey());
				IndexCategory ci = dbUtils.findFirst(Selector.from(
						IndexCategory.class).where("name_en", "=",
						entry.getKey()));
				if (ci == null) {
					ci = new IndexCategory(entry.getKey(), entry.getValue(), 1);
				} else {
					ci.setStatus(1);
					ci.setUpdateDate(new Date(System.currentTimeMillis()));
				}
				dbUtils.saveOrUpdate(ci);
				adapter.titles.add(entry.getValue());
				adapter.fList.add(new NewsListFragment(3, entry.getKey()));
				nameList.add(entry.getKey());
			}
			for (Map.Entry<String, String> entry : Constant.newsDelKey
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
					IndexCategory ci = dbUtils.findFirst(Selector.from(
							IndexCategory.class).where("name_en", "=",
							entry.getKey()));
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
			Constant.newsAddKey.clear();
			Constant.newsDelKey.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class mPagerAdapter extends FragmentStatePagerAdapter {

		ArrayList<String> titles = new ArrayList<String>();
		// 心血管：cardiovascular
		// 神经科：neurology
		// 内分泌：endocrine
		ArrayList<Fragment> fList = new ArrayList<Fragment>();

		public mPagerAdapter(FragmentManager fm) {
			super(fm);
			initData();
		}

		private void initData() {
			/*
			 * titles.add("业内新闻"); titles.add("神经科"); fList.add(new
			 * NewsListFragment(1)); fList.add(new NewsListFragment(1,
			 * "neurology")); // 3 IndexCategory ci2 = new
			 * IndexCategory("neurology", "神经科", 1); List<String> keyList = new
			 * ArrayList<String>(); keyList.add("neurology"); if (categoryList
			 * == null || categoryList.size() < 2) { nameList = new
			 * ArrayList<String>(); nameList.add("ynxw");
			 * nameList.add("neurology"); nameList.add("cardiovascular");
			 * nameList.add("endocrine"); titles.add("心血管"); titles.add("内分泌");
			 * fList.add(new NewsListFragment(3, "cardiovascular"));
			 * fList.add(new NewsListFragment(3, "endocrine"));
			 * keyList.add("cardiovascular"); keyList.add("endocrine");
			 * IndexCategory ci = new IndexCategory("cardiovascular", "心血管", 1);
			 * IndexCategory ci3 = new IndexCategory("endocrine", "内分泌", 1); try
			 * { // dbUtils.saveOrUpdate(ci2); dbUtils.saveOrUpdate(ci);
			 * dbUtils.saveOrUpdate(ci3); } catch (DbException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); } } else {
			 * nameList.add(0, "ynxw"); nameList.add(1, "neurology"); for (int i
			 * = 0; i < categoryList.size(); i++) {
			 * titles.add(categoryList.get(i).getNameCn()); fList.add(new
			 * NewsListFragment(3, categoryList.get(i) .getNameEn()));
			 * keyList.add(categoryList.get(i).getNameEn()); } }
			 */
			List<String> keyList = new ArrayList<String>();
			nameList = new ArrayList<String>();

			try {
				IndexCategory test = dbUtils.findFirst(Selector
						.from(IndexCategory.class)
						.where("id", "=", "neurology")
						.orderBy("update_date", false));
				// System.out.println(test.getId());
				// List<IndexCategory> mlist = dbUtils.findAll(Selector.from(
				// IndexCategory.class).orderBy("update_date", false));
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MWDApplication myapp = (MWDApplication) mContext
					.getApplicationContext();
			if (!TextUtils.isEmpty(myapp.DEPT_NAME)) {
				String dept_code = getDeptNameAndCode(myapp.DEPT_NAME);
				if (!TextUtils.isEmpty(dept_code)) { // 是否有科室code
					titles.add(myapp.DEPT_NAME);
					fList.add(new NewsListFragment(1, dept_code));
					keyList.add(dept_code);
					nameList.add(dept_code);
				} else {
					titles.add("神经科");
					fList.add(new NewsListFragment(1, "neurology"));
					keyList.add("neurology");
					nameList.add("neurology");
					Toast.makeText(getActivity(), "您的科室信息有误，请修正!",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				titles.add("神经科");
				fList.add(new NewsListFragment(1, "neurology"));
				keyList.add("neurology");
				nameList.add("neurology");
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
				if (Constant.depListResp == null) {
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

	@SuppressWarnings("unchecked")
	private String getDeptNameAndCode(String mname) {
		String temp = "";
		for (int i = 1; i < departNames.length; i++) {
			try {
				List<Department> list = (List<Department>) Constant.depListResp
						.getList().get(i);
				for (Department t : list) {
					if (mname.equals(t.getDepNameCn())) {
						temp = t.getDepNameEn();
						break;
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return temp;
	}

	class DepartmentAdapter extends BaseAdapter {

		private List<Department> depList;

		public DepartmentAdapter(List<Department> depList) {
			this.depList = depList;
		}

		@Override
		public int getCount() {
			return depList.size();
		}

		@Override
		public Object getItem(int position) {
			return depList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			MedicalHolder holder = null;
			if (convertView == null) {
				holder = new MedicalHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.department_item, null);
				holder.tvDepName = (TextView) convertView
						.findViewById(R.id.tv_dep_name);
				holder.ivOptPic = (ImageView) convertView
						.findViewById(R.id.iv_opt_pic);
				convertView.setTag(holder);
			} else {
				holder = (MedicalHolder) convertView.getTag();
			}
			try {
				holder.tvDepName.setText(depList.get(position).getDepNameCn());
				holder.ivOptPic.setContentDescription("0");
				if (nameList != null && !nameList.isEmpty()) {
					if (nameList.contains(depList.get(position).getDepNameEn())) {
						holder.ivOptPic.setContentDescription("1");
						holder.ivOptPic
								.setBackgroundResource(R.drawable.selected);
						if ("neurology".equals(depList.get(position)
								.getDepNameEn())) {
							convertView.setEnabled(false);
						} else {
							convertView.setEnabled(true);
						}
					}
				}
				final ImageView ivPic = holder.ivOptPic;
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (ivPic.getContentDescription().equals("0")) {
							ivPic.setBackgroundResource(R.drawable.selected);
							ivPic.setContentDescription("1");
							Constant.newsAddKey.put(depList.get(position)
									.getDepNameEn(), depList.get(position)
									.getDepNameCn());
							Constant.newsDelKey.remove(depList.get(position)
									.getDepNameEn());
						} else {
							ivPic.setBackgroundResource(R.drawable.add);
							ivPic.setContentDescription("0");
							Constant.newsAddKey.remove(depList.get(position)
									.getDepNameEn());
							Constant.newsDelKey.put(depList.get(position)
									.getDepNameEn(), depList.get(position)
									.getDepNameCn());
						}
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;
		}
	}

	class MedicalHolder {

		TextView tvDepName;
		ImageView ivOptPic;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		getDocNews();
		getDepartment();
		new ShowDepThread().start();
	}

}
