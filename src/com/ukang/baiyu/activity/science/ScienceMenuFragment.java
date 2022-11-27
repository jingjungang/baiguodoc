package com.ukang.baiyu.activity.science;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.activity.conference.ConferenceActivity;
import com.ukang.baiyu.activity.conference.ConferenceFragment;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.common.DataParser;
import com.ukang.baiyu.entity.GridIcon;
import com.ukang.baiyu.entity.Response;
import com.ukang.baiyu.fragments.tools.BaseFragment;
import com.ukang.baiyu.thread.XThread;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment-目录总纲
 * 
 * @author SAN
 * 
 */
public class ScienceMenuFragment extends BaseFragment {
	private final int CHOOSE_AIRPORT = 1001;
	private Activity mContext;

	@ViewInject(R.id.iv_back)
	private ImageButton btnBack;
	@ViewInject(R.id.tv_title)
	private TextView tvTitle;
	@ViewInject(R.id.grid_view)
	private GridView gridView;

	private GridIconAdapter adapter;
	private List<GridIcon> appLinkList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.science_page, null);
		ViewUtils.inject(this, rootView);
		btnBack.setVisibility(View.INVISIBLE);
		tvTitle.setText(R.string.science_art);
		appLinkList = getListData();
		adapter = new GridIconAdapter(appLinkList);
		gridView.setAdapter(adapter);
		return rootView;
	}

	private List<GridIcon> getListData() {
		List<GridIcon> list = new ArrayList<GridIcon>();
		// 2016年5月3日 11:18:05 景俊钢 隐藏
		// String[] names = {"数据库", "路径指南", "读片",
		// "病历夹", "对照试验", "科研课题",
		// "论文润色", "学术翻译", "发表支持",
		// "统计分析", "科研病历" ,"会议咨询"};
		// int[] resIds = {R.drawable.shujuku, R.drawable.lujingzhinan,
		// R.drawable.dupian,
		// R.drawable.binglijia, R.drawable.duizhaoshiyan, R.drawable.keyanketi,
		// R.drawable.lunwenrunse, R.drawable.xueshufanyi,
		// R.drawable.fabiaozhichi,
		// R.drawable.tongjifenxi,
		// R.drawable.keyanbingli,R.drawable.keyanbingli};
		String[] names = { "路径指南", "病例夹", "科研课题", "论文润色", "学术翻译", "发表支持",
				"统计分析", "科研病历", "会议资讯" };
		int[] resIds = { R.drawable.lujingzhinan, R.drawable.binglijia,
				R.drawable.keyanketi, R.drawable.lunwenrunse,
				R.drawable.xueshufanyi, R.drawable.fabiaozhichi,
				R.drawable.tongjifenxi, R.drawable.keyanbingli,
				R.drawable.nav_btn02_nor };
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
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						switch (position) {
//						case 0://数据库
//							MobclickAgent.onEvent(mContext, "SEARCH");
//							intent.setClass(mContext, DbsearchActivity.class);
//							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//							intent.putExtra("searchKind",
//									SearchActivity.SEARCH_DATABASE);
//							startActivity(intent);
//							return;
						case 0://路径指南
							MobclickAgent.onEvent(mContext, "PATH_WAY");
							intent.setClass(mContext, PathGuideActivity.class);
							intent.putExtra("NEWS_KIND", 1);
							startActivity(intent);
							return;
//						case 2://读片
//							MobclickAgent.onEvent(mContext, "READ_PIC_LIST");
//							intent.setClass(mContext, ReadPicListActivity.class);
//							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//							startActivity(intent);
//							return;
						case 1://病历夹
							MobclickAgent.onEvent(mContext, "MED_CHART");
							loadMedChart();
							return;
//						case 4://对照试验
//							intent.setClass(mContext, CompareExamActivity.class);
//							intent.putExtra("TITLE", appList.get(position)
//									.getName());
//							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//							startActivity(intent);
//							return;
						case 2://科研课题
							intent.setClass(mContext, NewsDetailActivity.class);
							intent.putExtra("id", "7");
							intent.putExtra("NEWS_KIND", 4);
							startActivity(intent);
							return;
						case 3://论文润色
							intent.setClass(mContext, NewsDetailActivity.class);
							intent.putExtra("id", "1");
							intent.putExtra("NEWS_KIND", 4);
							startActivity(intent);
							return;
						case 4://学术翻译
							intent.setClass(mContext, NewsDetailActivity.class);
							intent.putExtra("id", "2");
							intent.putExtra("NEWS_KIND", 4);
							startActivity(intent);
							return;
						case 5://发表支持
							intent.setClass(mContext, NewsDetailActivity.class);
							intent.putExtra("id", "3");
							intent.putExtra("NEWS_KIND", 4);
							startActivity(intent);
							return;
						case 6://统计分析
							intent.setClass(mContext, NewsDetailActivity.class);
							intent.putExtra("id", "4");
							intent.putExtra("NEWS_KIND", 4);
							startActivity(intent);
							return;
						case 7://科研病历
							intent.setClass(mContext, NewsDetailActivity.class);
							intent.putExtra("id", "6");
							intent.putExtra("NEWS_KIND", 4);
							startActivity(intent);
							return;
						case 8://会议咨询
							intent.setClass(mContext, ConferenceActivity.class);
							startActivity(intent);
							return;
						default:
							break;
						}
					}
				});
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

}
