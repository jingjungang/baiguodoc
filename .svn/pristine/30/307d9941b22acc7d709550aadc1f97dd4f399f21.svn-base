package com.ukang.baiyu.activity.science;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.ukang.baiyu.thread.XThread;
import com.ukang.baiyu.view.photo.util.Bimp;
import com.ukang.baiyu.view.swipebacklayout.lib.SwipeBackLayout;
import com.ukang.baiyu.view.swipebacklayout.lib.app.SwipeBackActivity;
import com.ukang.baiyu.views.PullToRefreshLayout;
import com.ukang.baiyu.views.PullableListView;
import com.ukang.baiyu.views.PullToRefreshLayout.OnRefreshListener;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.activity.login.LoginActivity;
import com.ukang.baiyu.activity.tools.NewsDetailActivity3;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.common.DataParser;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ukang.baiyu.entity.ReadPic;
import com.ukang.baiyu.entity.Response;
import com.ukang.baiyu.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

/**
 * 病例读片列表
 * 
 * @author SAN
 *
 */
public class ReadPicListActivity extends SwipeBackActivity {

	private static SystemBarTintManager tintManager;
	private SwipeBackLayout mSwipeBackLayout;
	private Context mContext;

	@ViewInject(R.id.iv_back)
	private ImageButton btnBack;
	@ViewInject(R.id.tv_title)
	private TextView tvTitle;
	@ViewInject(R.id.line_add_medchart)
	private ImageView lineAddMedChart;

	@ViewInject(R.id.refreshable_view)
	private PullToRefreshLayout refreshableView;// 该view包含了向上拉更新
	@ViewInject(R.id.list_view)
	private PullableListView listView;
	private List<ReadPic> list;
	private MedChartAdapter adapter;
	private boolean isAllRead = true;

	public DisplayImageOptions options;
	public ImageLoader imageLoader = ImageLoader.getInstance();
	public ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	private int page = 1;
	// page备份，当加载出错时，回退到加载前的page
	private int pageBackUp = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read_pic);
		mContext = this;
		ViewUtils.inject(this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.index_title_color);
		mSwipeBackLayout = getSwipeBackLayout();
		mSwipeBackLayout.setScrimColor(Color.TRANSPARENT);
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		initImageLoader();
		initview();
		addClickListener();
		initViewData();

		// 设置在滚动时不加载图片
		boolean pauseOnFling = true;
		boolean pauseOnScroll = true;
		listView.setOnScrollListener(new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
	}

	private void initImageLoader() {
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_image)
				.showImageForEmptyUri(R.drawable.default_image).showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
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

	private void initview() {
		tvTitle.setText(getString(R.string.read_pic));
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (isAllRead) {
			loadReadPic();
		} else {
			loadReadPic();
		}
		Bimp.tempSelectBitmap.clear();
		Bimp.max = 0;
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private void loadReadPic() {
		RequestParams params = new RequestParams();
		params.addHeader("Cookie", Constant.sessionId);
		params.addBodyParameter("token", Constant.token);
		page = 1;
		params.addBodyParameter("page", String.valueOf(page));
		XThread thread = new XThread(this, 0, params, Constant.READ_PIC_LIST_URL, readHandler);
		// thread.setShowDia(true);
		thread.start();
	}

	private Handler readHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String result = msg.obj.toString();
				Response response = DataParser.parseReadPicList(result);
				if (response.getList() != null) {
					list = new ArrayList<ReadPic>();
					list = (List<ReadPic>) response.getList();
					if (adapter != null) {
						adapter.notifyDataSetChanged();
					} else {
						adapter = new MedChartAdapter();
						listView.setAdapter(adapter);
					}
				}
				break;
			case -1:
				Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};

	private void addClickListener() {
		btnBack.setOnClickListener(btnClick);
		lineAddMedChart.setOnClickListener(lineClick);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initViewData() {
		if (Constant.readPicListResp != null && Constant.readPicListResp.getList() != null) {
			list = (List<ReadPic>) Constant.readPicListResp.getList();
		} else {
			list = new ArrayList<ReadPic>();
		}
		adapter = new MedChartAdapter();
		listView.setAdapter(adapter);

		refreshableView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				// TODO Auto-generated method stub
				// 加载操作
				new Handler() {
					@Override
					public void handleMessage(Message msg) {
						// 千万别忘了告诉控件加载完毕了哦！
						refreshableView.loadmoreFinish(PullToRefreshLayout.SUCCEED);
					}
				}.sendEmptyMessageDelayed(0, 1000);
			}

			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				// TODO Auto-generated method stub
				// 加载操作
				Handler h = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						String result = (String) msg.obj;
						Message message = Message.obtain();
						message.what = msg.what;
						message.obj = result;
						refreshComHandler.sendMessage(message);
						// 千万别忘了告诉控件加载完毕了哦！
						refreshableView.loadmoreFinish(PullToRefreshLayout.SUCCEED);
					}
				};
				RequestParams params = new RequestParams();
				params.addHeader("Cookie", Constant.sessionId);
				params.addBodyParameter("token", Constant.token);
				params.addBodyParameter("page", String.valueOf(++page));
				XThread thread = new XThread(ReadPicListActivity.this, 0, params, Constant.MEDCHART_LIST_URL, h);
				thread.start();
			}
		});
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
					Response response = DataParser.parseReadPicList(result);
					if (response != null && response.getList() != null && !response.getList().isEmpty()) {
						List<ReadPic> tmpList = (List<ReadPic>) response.getList();
						list.addAll(tmpList);
						adapter.notifyDataSetChanged();
						pageBackUp = page;
					}
				} catch (Exception e) {

				}
				break;
			case -1:
				Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
				page = pageBackUp;
				break;
			default:
				break;
			}
		}
	};

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
			if (v == lineAddMedChart) {
				if (Constant.users.getUsername().equals(LoginActivity.GUEST_NAME)) {
					Toast.makeText(mContext, "您现在是游客模式，请使用正式账号登录", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(mContext, LoginActivity.class);
					intent.putExtra("actionType", LoginActivity.ACTION_TYPE_NEED_LOGIN);
					startActivity(intent);
				} else {
					MobclickAgent.onEvent(mContext, "ADD_READ_PIC");
					Intent intent = new Intent(mContext, AddReadPicActivity.class);
					startActivity(intent);
				}
			}
		}
	};

	class MedChartAdapter extends BaseAdapter {

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
		public View getView(final int position, View convertView, ViewGroup parent) {
			NewsItemHolder newsHolder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.read_pic_item, null);
				newsHolder = new NewsItemHolder();
				newsHolder.imgNewsPic = (ImageView) convertView.findViewById(R.id.iv_read_pic);
				newsHolder.title = (TextView) convertView.findViewById(R.id.tv_news_title);
				newsHolder.description = (TextView) convertView.findViewById(R.id.tv_news_desc);
				newsHolder.date_text = (TextView) convertView.findViewById(R.id.tv_news_time);
				convertView.setTag(newsHolder);
			} else {
				newsHolder = (NewsItemHolder) convertView.getTag();
			}
			String imgUrl = null;
			if (list.get(position).getImgs() != null && !list.get(position).getImgs().isEmpty()) {
				imgUrl = list.get(position).getImgs().get(0).getImgPath();
			}
			imageLoader.displayImage(imgUrl, newsHolder.imgNewsPic, options, animateFirstListener);
			try {
				newsHolder.title.setText(list.get(position).getTitle());
				newsHolder.description.setText(list.get(position).getPresentation());
				newsHolder.date_text.setVisibility(View.GONE);
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, NewsDetailActivity3.class);
						intent.putExtra("id", list.get(position).getId() + "");
						intent.putExtra("NEWS_KIND", 1);
						startActivity(intent);
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;
		}
	}

	class NewsItemHolder {
		ImageView imgNewsPic;
		TextView title;
		TextView date_text;
		TextView description;
	}
}
