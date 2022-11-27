package com.ukang.baiyu.fragments.tools;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.http.RequestParams;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.activity.science.MedChartListActivity;
import com.ukang.baiyu.activity.science.NewsDetailActivity;
import com.ukang.baiyu.activity.science.PathGuideActivity;
import com.ukang.baiyu.activity.science.ReadPicListActivity;
import com.ukang.baiyu.activity.science.DbsearchActivity;
import com.ukang.baiyu.activity.science.SearchActivity;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.common.DataParser;
import com.ukang.baiyu.common.LocalDisplay;
import com.ukang.baiyu.entity.DocNews;
import com.ukang.baiyu.entity.NewsDetail;
import com.ukang.baiyu.entity.Response;
import com.ukang.baiyu.thread.XThread;
import com.ukang.baiyu.view.pulltorefresh.library.PullToRefreshBase;
import com.ukang.baiyu.view.pulltorefresh.library.PullToRefreshListView;
import com.ukang.baiyu.view.pulltorefresh.library.PullToRefreshBase.Mode;
import com.ukang.baiyu.views.PullToRefreshLayout;
import com.ukang.baiyu.views.PullToRefreshLayout.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 客户资料
 * 
 * @author SAN
 * 
 */
@SuppressLint("ValidFragment")
public class NewsListFragment extends BaseFragment {

	// private PullToRefreshLayout pullLayout;
	private ListView listView;
	private PullToRefreshListView pullListView;

	private Activity mContext;
	private NewsAdapter adapter;
	private List<DocNews> list = new ArrayList<DocNews>();
	private LinearLayout viewContainer;
	private int channalId;
	private int page = 1;
	private String nameEn;
	private Response response = null;

	public NewsListFragment() {
		super();
	}

	public NewsListFragment(int channalId) {
		super();
		this.channalId = channalId;
	}

	public NewsListFragment(int channalId, String nameEn) {
		super();
		this.channalId = channalId;
		this.nameEn = nameEn;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_progress_container,
				null);
		viewContainer = (LinearLayout) rootView.findViewById(R.id.container);
		LocalDisplay.init(mContext);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		new ShowDataThread().start();
	}

	class ShowDataThread extends Thread {
		public void run() {
			while (true) {
				switch (channalId) {
				case 1:
					response = Constant.docNewsResp;
					break;
				case 2:
					response = Constant.reportNewsResp;
					break;
				default:
					response = Constant.newsMap.get(nameEn);
					break;
				}
				if (response == null) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						System.out.println("wocao...");
						e.printStackTrace();
					}
				} else {
					initHandler.sendEmptyMessageDelayed(0, 0);
					break;
				}
			}
		}
	}

	Handler initHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			initData();
		}
	};

	@SuppressLint("InflateParams")
	@SuppressWarnings({ "unchecked", "rawtypes" })
	void initData() {
		if (mContext == null)
			return;
		try {
			View v = LayoutInflater.from(mContext).inflate(R.layout.news_list,
					null);
			pullListView = (PullToRefreshListView) v
					.findViewById(R.id.list_view);
			pullListView.setMode(Mode.PULL_FROM_END);

			View headView = LayoutInflater.from(mContext).inflate(R.layout.top,
					null);
			LinearLayout centerButton = (LinearLayout) headView
					.findViewById(R.id.center_button);
			View lunbo = headView.findViewById(R.id.lunbo_fragment);
			// 2016年5月4日 17:07:38 景俊钢 修改 原在if里边
			lunbo.setVisibility(View.VISIBLE);
			if (channalId == 1) {
				lunbo.setVisibility(View.VISIBLE);
				centerButton.setVisibility(View.GONE);
				LinearLayout tvTab1 = (LinearLayout) centerButton
						.findViewById(R.id.line_btn_1);
				LinearLayout tvTab2 = (LinearLayout) centerButton
						.findViewById(R.id.line_btn_2);
				LinearLayout tvTab3 = (LinearLayout) centerButton
						.findViewById(R.id.line_btn_3);
				LinearLayout tvTab4 = (LinearLayout) centerButton
						.findViewById(R.id.line_btn_4);
				tvTab1.setOnClickListener(clickListener);
				tvTab2.setOnClickListener(clickListener);
				tvTab3.setOnClickListener(clickListener);
				tvTab4.setOnClickListener(clickListener);
			}
			listView = pullListView.getRefreshableView();
			listView.addHeaderView(headView);

			pullListView
					.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
						@Override
						public void onPullDownToRefresh(
								PullToRefreshBase refreshView) {
							if (Constant.ISDEBUG)
								Log.i("---", "onPullDownToRefresh");
							try {
								pullListView.setRefreshing();
								pullHandler.sendEmptyMessageDelayed(1, 2000);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						@Override
						public void onPullUpToRefresh(
								PullToRefreshBase refreshView) {
							if (Constant.ISDEBUG)
								Log.i("---", "onPullUpToRefresh");
							try {
								pullListView.setRefreshing();
								// pullHandler.sendEmptyMessageDelayed(1, 2000);
								Handler h = new Handler() {
									@Override
									public void handleMessage(Message msg) {
										String result = (String) msg.obj;
										Message message = Message.obtain();
										message.what = msg.what;
										message.obj = result;
										refreshComHandler.sendMessage(message);
										// 千万别忘了告诉控件加载完毕了哦！
										pullListView.onRefreshComplete();
									}
								};
								RequestParams p = new RequestParams();
								p.addHeader("Cookie", Constant.sessionId);
								p.addHeader("User-Agent", Constant.USER_AGENT);
								p.addHeader("Cookie", Constant.sessionId);
								p.addBodyParameter("token", Constant.token);
								p.addBodyParameter("page",
										String.valueOf(++page));
								if (channalId != 1)
									p.addBodyParameter("office", nameEn);
								String url = "";
								if (channalId == 1) {
									url = Constant.DOCTOR_NEWS_URL;
								} else {
									url = Constant.REPORT_NEWS_URL;
								}
								XThread thread = new XThread(0, p, url, h);
								thread.start();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});

			list = (List<DocNews>) response.getList();
			adapter = new NewsAdapter();
			listView.setAdapter(adapter);

			// 设置在滚动时不加载图片
			boolean pauseOnFling = true;
			boolean pauseOnScroll = true;
			listView.setOnScrollListener(new PauseOnScrollListener(imageLoader,
					pauseOnScroll, pauseOnFling));

			viewContainer.removeAllViews();
			viewContainer.addView(v);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Handler pullHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			pullListView.onRefreshComplete();
		}
	};

	private Handler newsDetailHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String result = msg.obj.toString();
				Response response = DataParser.parseDocNewsDetail(result);
				Constant.curtNews = (NewsDetail) response.getObj();
				Intent intent = new Intent(getActivity(),
						NewsDetailActivity.class);
				intent.putExtra("id", Constant.curtNews.getId());
				if (channalId == 1) {
					intent.putExtra("NEWS_KIND", 1);
				} else {
					intent.putExtra("NEWS_KIND", 2);
				}
				getActivity().startActivity(intent);
				// getActivity().overridePendingTransition(R.anim.abc_fade_in,
				// R.anim.abc_fade_out);
				break;
			case -1:
				Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		}
	};

	private Handler medHandler = new Handler() {

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
				Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();// msg.obj.toString()
				break;
			default:
				break;
			}
		}
	};

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.line_btn_1:
				MobclickAgent.onEvent(mContext, "SEARCH");
				Intent i1 = new Intent();
				i1.setClass(mContext, DbsearchActivity.class);
				i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i1.putExtra("searchKind", SearchActivity.SEARCH_DATABASE);
				startActivity(i1);
				break;
			case R.id.line_btn_2:
				MobclickAgent.onEvent(mContext, "PATH_WAY");
				Intent i2 = new Intent();
				i2.setClass(mContext, PathGuideActivity.class);
				i2.putExtra("NEWS_KIND", 1);
				startActivity(i2);
				break;
			case R.id.line_btn_3:
				MobclickAgent.onEvent(mContext, "READ_PIC_LIST");
				Intent i3 = new Intent();
				i3.setClass(mContext, ReadPicListActivity.class);
				i3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i3);
				break;
			case R.id.line_btn_4:
				MobclickAgent.onEvent(mContext, "MED_CHART");
				loadMedChart();
				break;
			default:
				break;
			}
		}
	};

	private void loadMedChart() {
		RequestParams params = new RequestParams();
		params.addHeader("Cookie", Constant.sessionId);
		params.addBodyParameter("token", Constant.token);
		params.addBodyParameter("page", "1");
		XThread thread = new XThread(mContext, 0, params,
				Constant.MEDCHART_LIST_URL, medHandler);
		thread.setShowDia(true);
		thread.start();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	class NewsAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return list.size();
		}

		@Override
		public Object getItem(int position) {

			return list.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			NewsItemHolder newsHolder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.news_item, null);
				newsHolder = new NewsItemHolder();
				newsHolder.imgNewsPic = (ImageView) convertView
						.findViewById(R.id.iv_news_pic);
				newsHolder.title = (TextView) convertView
						.findViewById(R.id.tv_news_title);
				newsHolder.description = (TextView) convertView
						.findViewById(R.id.tv_news_desc);
				newsHolder.date_text = (TextView) convertView
						.findViewById(R.id.tv_news_time);
				convertView.setTag(newsHolder);
			} else {
				newsHolder = (NewsItemHolder) convertView.getTag();
			}
			imageLoader.displayImage(list.get(position).getThumb(),
					newsHolder.imgNewsPic, options, animateFirstListener);
			try {
				newsHolder.title.setText(list.get(position).getTitle());
				newsHolder.date_text.setText(list.get(position).getInputtime());
				newsHolder.description.setText(list.get(position)
						.getDescription());
				newsHolder.description.setVisibility(View.GONE);
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),
								NewsDetailActivity.class);
						intent.putExtra("id", list.get(position).getId() + "");
						if (channalId == 1) {
							intent.putExtra("NEWS_KIND", 1);
						} else {
							intent.putExtra("NEWS_KIND", 2);
						}
						getActivity().startActivity(intent);
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;
		}
	}

	private Handler refreshComHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				try {
					String result = msg.obj.toString();
					Response response = DataParser.parseDocNews(result);
					if (response != null && response.getList() != null
							&& !response.getList().isEmpty()) {
						List<DocNews> tmpList = (List<DocNews>) response
								.getList();
						if (channalId == 1) {//
							((List<DocNews>) Constant.docNewsResp.getList())
									.addAll(tmpList);
						} else {
							((List<DocNews>) Constant.newsMap.get(nameEn)
									.getList()).addAll(tmpList);
						}
						list.addAll(tmpList);
						adapter.notifyDataSetChanged();
					}
				} catch (Exception e) {

				}
				break;
			case -1:
				Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT)
						.show();//msg.obj.toString()
				break;
			default:
				break;
			}
		}
	};

	class NewsItemHolder {
		ImageView imgNewsPic;
		TextView title;
		TextView date_text;
		TextView description;
	}

	class MyListener implements OnRefreshListener {
		private Handler backHandler;

		public MyListener(Handler backHandler) {
			this.backHandler = backHandler;
		}

		@Override
		public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
			// 下拉刷新操作
			Handler h = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					String result = (String) msg.obj;
					Message message = Message.obtain();
					message.what = msg.what;
					message.obj = result;
					refreshComHandler.sendMessage(message);
					// 千万别忘了告诉控件加载完毕了哦！
					pullToRefreshLayout
							.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				}
			};
			RequestParams p = new RequestParams();
			p.addHeader("Cookie", Constant.sessionId);
			p.addHeader("User-Agent", Constant.USER_AGENT);
			p.addHeader("Cookie", Constant.sessionId);
			p.addBodyParameter("token", Constant.token);
			p.addBodyParameter("page", String.valueOf(++page));
			p.addBodyParameter("office", nameEn);
			String url = "";
			if (channalId == 1) {
				url = Constant.DOCTOR_NEWS_URL;
			} else {
				url = Constant.REPORT_NEWS_URL;
			}
			XThread thread = new XThread(0, p, url, h);
			thread.start();
			System.out.println("news list onrefresh...");
		}

		@Override
		public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
			// 加载操作
			Handler h = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					pullToRefreshLayout
							.refreshFinish(PullToRefreshLayout.SUCCEED);
				}
			};
			h.sendEmptyMessageDelayed(0, 2000);
		}
	}

}
