package com.ukang.baiyu.activity.patientevent;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.activity.login.LoginActivity;
import com.ukang.baiyu.activity.science.MedChartListActivity;
import com.ukang.baiyu.adapter.Announcement_Adapter;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.common.DataParser;
import com.ukang.baiyu.entity.GridIcon;
import com.ukang.baiyu.entity.Notice;
import com.ukang.baiyu.entity.Response;
import com.ukang.baiyu.fragments.tools.BaseFragment;
import com.ukang.baiyu.thread.XThread;

/**
 * 患者事物Fragment
 * 
 * @author SAN
 * 
 */
public class PatientMenuFragment extends BaseFragment implements
		OnItemClickListener {
	private final int CHOOSE_AIRPORT = 1001;
	private Activity mContext;
	@ViewInject(R.id.pull_to_refresh_listview)
	private PullToRefreshListView lv;

	@ViewInject(R.id.iv_back)
	private ImageButton btnBack;
	@ViewInject(R.id.tv_title)
	private TextView tvTitle;
	@ViewInject(R.id.grid_view)
	private GridView gridView;

	@ViewInject(R.id.total)
	private TextView tv_total;

	private GridIconAdapter adapter;
	private Announcement_Adapter adapter1;
	private List<GridIcon> appLinkList;
	private Intent intent;

	private PullToRefreshListView mPullToRefreshListView;
	private List<Notice> list;
	int page = 1;
	int spare_page;
	boolean isLoad = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.patient, null);
		ViewUtils.inject(this, rootView);
		btnBack.setVisibility(View.INVISIBLE);
		tvTitle.setText(R.string.patient_trans);
		appLinkList = getListData();
		adapter = new GridIconAdapter(appLinkList);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
		init(rootView);
		return rootView;
	}

	private void init(View rootView) {
		// TODO Auto-generated method stub
		list = new ArrayList<Notice>();
		onLoad();
		mPullToRefreshListView = (PullToRefreshListView) rootView
				.findViewById(R.id.pull_to_refresh_listview);
		// 设置同时开启上拉加载，下拉刷新
		mPullToRefreshListView.setMode(Mode.BOTH);
		// 更改上拉加载的文字内容（默认使用下拉刷新的文字内容）
		mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel(
				getString(R.string.pullup_to_load));
		mPullToRefreshListView.getLoadingLayoutProxy(false, true)
				.setRefreshingLabel(getString(R.string.loading));
		mPullToRefreshListView.getLoadingLayoutProxy(false, true)
				.setReleaseLabel(getString(R.string.release_to_load));
		// 设置PullToRefreshListView的上拉、下拉监听
		mPullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						refreshLoad();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						onLoad();
					}
				});
		// 得到ListView
		ListView mListView = mPullToRefreshListView.getRefreshableView();
		adapter1 = new Announcement_Adapter(getActivity(), list);
		mListView.setAdapter(adapter);
	}

	private List<GridIcon> getListData() {
		List<GridIcon> list = new ArrayList<GridIcon>();
		// String[] names = {"我的患者", "随访记录", "患者咨询",
		// "医生互动", "我的医助", "我的订单",
		// "我的收入", "患者援助"};

		// 2016年5月3日 11:13:26 景俊钢 隐藏
		// String[] names = {"我的患者", "随访记录", "患者咨询",
		// "医生互动", "我的医助", "我的订单", "患者援助"};
		// int[] resIds = {R.drawable.wodehuanzhe, R.drawable.suifangjilu,
		// R.drawable.huanzhezixun,
		// R.drawable.yishenghudong, R.drawable.wodeyizhu,
		// R.drawable.wodedingdan,
		// R.drawable.wodeshouru, R.drawable.huanzheyuanzhu};
		// 2016年5月3日 11:13:26 景俊钢 隐藏
		// int[] resIds = {R.drawable.wodehuanzhe, R.drawable.suifangjilu,
		// R.drawable.huanzhezixun,
		// R.drawable.yishenghudong, R.drawable.wodeyizhu,
		// R.drawable.wodedingdan,
		// R.drawable.huanzheyuanzhu};
		String[] names = { "我的患者", "患者咨询" };
		int[] resIds = { R.drawable.mypatient_selector,
				R.drawable.patientconsolut_selector };
		for (int i = 0; i < names.length; i++) {
			GridIcon ic = new GridIcon();
			ic.setName(names[i]);
			ic.setImg(resIds[i]);
			list.add(ic);
		}
		return list;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:
			if (Constant.users.getUsername().equals(LoginActivity.GUEST_NAME)) {
				Toast.makeText(getActivity(), "您现在是游客模式，请使用正式账号登录",
						Toast.LENGTH_LONG).show();
				Intent intent = new Intent(mContext, LoginActivity.class);
				intent.putExtra("actionType",
						LoginActivity.ACTION_TYPE_NEED_LOGIN);
				startActivity(intent);
			} else {
				Intent intent = new Intent(mContext, MyPatientActivity.class);
				intent.putExtra("TITLE", appLinkList.get(position).getName());
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			break;
		case 1:
			Toast.makeText(getActivity(), "敬请期待！", Toast.LENGTH_SHORT).show();
			break;
		case 2:
			Toast.makeText(getActivity(), "敬请期待！", Toast.LENGTH_SHORT).show();
			break;
		case 3:
			Toast.makeText(getActivity(), "敬请期待！", Toast.LENGTH_SHORT).show();
			break;
		case 4:
			Toast.makeText(getActivity(), "敬请期待！", Toast.LENGTH_SHORT).show();
			break;
		case 5:
			Toast.makeText(getActivity(), "敬请期待！", Toast.LENGTH_SHORT).show();
			break;
		case 6:
			Toast.makeText(getActivity(), "敬请期待！", Toast.LENGTH_SHORT).show();
			break;
		case 7:
			Toast.makeText(getActivity(), "敬请期待！", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	private OnClickListener btnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		}
	};

	private void loadMedChart() {
		Handler medHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					String result = msg.obj.toString();
					Response response = DataParser.parseMedChartList(result);
					Constant.medChartListResp = response;
					Intent intent = new Intent(getActivity(),
							MedChartListActivity.class);
					getActivity().startActivity(intent);
					break;
				case -1:
					Toast.makeText(mContext, msg.obj.toString(),
							Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}
		};
		RequestParams params = new RequestParams();
		params.addHeader("Cookie", Constant.sessionId);
		params.addBodyParameter("token", Constant.token);
		params.addBodyParameter("page", "1");
		XThread thread = new XThread(mContext, 0, params,
				Constant.MEDCHART_LIST_URL, medHandler);
		thread.setShowDia(true);
		thread.start();
	}

	class GridIconAdapter extends BaseAdapter {
		private List<GridIcon> appList;

		public GridIconAdapter(List<GridIcon> appList) {
			this.appList = appList;
		}

		@Override
		public int getCount() {
			return appList.size();
		}

		@Override
		public Object getItem(int position) {
			return appList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			AppLinkHolder holder = null;
			if (convertView == null) {
				holder = new AppLinkHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.science_grid_item, null);
				holder.tvAppName = (TextView) convertView
						.findViewById(R.id.tv_app_name);
				holder.ivAppIcon = (ImageView) convertView
						.findViewById(R.id.iv_app_pic);
				convertView.setTag(holder);
			} else {
				holder = (AppLinkHolder) convertView.getTag();
			}
			try {
				holder.tvAppName.setText(appList.get(position).getName());
				holder.ivAppIcon.setImageResource(appList.get(position)
						.getImg());
				// convertView.setOnClickListener(new OnClickListener() {
				// @Override
				// public void onClick(View v) {
				// // TODO Auto-generated method stub
				// Intent intent = new Intent();
				// intent.setClass(mContext, ComeSoonActivity.class);
				// intent.putExtra("TITLE", appList.get(position).getName());
				// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivity(intent);
				// }
				// });
			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;
		}
	}

	class AppLinkHolder {
		ImageView ivAppIcon;
		TextView tvAppName;
	}

	private void onLoad() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", Constant.token);
		params.addBodyParameter("page", String.valueOf(page));
		params.addBodyParameter("pagesize", "10");
		XThread thread = new XThread(getActivity(), 0, params,
				Constant.messagenoread, mhandler);
		if (isLoad) {
			thread.setShowDia(true);
		}
		thread.start();
	}

	Handler mhandler = new Handler() {
		public void handleMessage(Message msg) {
			String result = msg.obj.toString();
			try {
				JSONObject json = new JSONObject(result);
				switch (json.getInt("status")) {
				case 1:
					tv_total.setText(json.getString("alltotal"));
					setJson(json);
					page++;
					spare_page = page;
					// 告诉控件，加载完成
					mPullToRefreshListView.onRefreshComplete();
					break;
				case -1:
					page = spare_page;
					// 告诉控件，加载完成
					Toast.makeText(getActivity(), "没有了...", Toast.LENGTH_SHORT)
							.show();
					mPullToRefreshListView.onRefreshComplete();
					break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	};

	private void setJson(JSONObject json) {
		try {
			JSONArray array = json.getJSONArray("info");
			if (array.length() > 0) {
				for (int i = 0; i < array.length(); i++) {
					Notice notice = new Notice();
					JSONObject item = array.getJSONObject(i);
					notice.setId(item.has("id") ? item.getString("id") : "");
//					notice.setAdd_time(item.has("add_time") ? item
//							.getString("add_time") : "");
//					notice.setUpdate_time(item.has("update_time") ? item
//							.getString("update_time") : "");
//					notice.setType(item.has("type") ? item.getInt("type") : 1);
//					notice.setTitle(item.has("title") ? item.getString("title")
//							: "");
//					notice.setState(item.has("state") ? item.getInt("state")
//							: 1);
//					notice.setThumb(item.has("thumb") ? item.getString("thumb")
//							: "");
//					notice.setUsername(item.has("username") ? item
//							.getString("username") : "");
//					notice.setNickname(item.has("nickname") ? item
//							.getString("nickname") : "");
//					if (notice.getState() == 3) {
//						list.add(0, notice);
//					} else {
//						list.add(notice);
//					}
					adapter.notifyDataSetChanged();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 刷新数据请求
	private void refreshLoad() {
		isLoad = false;
		list.clear();
		for (int i = 1; i < spare_page; i++) {
			page = i;
			onLoad();
		}
		isLoad = true;
	}
}
