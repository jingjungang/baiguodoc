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
import com.ukang.baiyu.activity.R.color;
import com.ukang.baiyu.activity.R.drawable;
import com.ukang.baiyu.activity.R.id;
import com.ukang.baiyu.activity.R.layout;
import com.ukang.baiyu.activity.R.string;
import com.ukang.baiyu.activity.login.LoginActivity;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.common.DataParser;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ukang.baiyu.entity.Conference;
import com.ukang.baiyu.entity.MedChart;
import com.ukang.baiyu.entity.Response;
import com.ukang.baiyu.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

/**
 * 病例夹
 * @author SAN
 *
 */
public class MedChartListFragment extends SwipeBackActivity {
	
	private static SystemBarTintManager tintManager;
	private SwipeBackLayout mSwipeBackLayout;
	private Context mContext;
	
	@ViewInject(R.id.iv_back)
	private ImageButton btnBack;
	@ViewInject(R.id.tv_title)
	private TextView tvTitle;
	@ViewInject(R.id.line_add_medchart)
	private LinearLayout lineAddMedChart;
	
	@ViewInject(R.id.refreshable_view)
	private PullToRefreshLayout refreshableView;//该view包含了向上拉更新
	@ViewInject(R.id.list_view)
	private PullableListView listView;
	private List<MedChart> list;
	private MedChartAdapter adapter;
	
	public DisplayImageOptions options;
	public ImageLoader imageLoader = ImageLoader.getInstance();
	public ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	
	private int page = 1;
	//page备份，当加载出错时，回退到加载前的page
	private int pageBackUp = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.medchart);
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
		
		//设置在滚动时不加载图片
		boolean pauseOnFling = true;
		boolean pauseOnScroll = true;
		listView.setOnScrollListener(new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
//		listView.setOnItemLongClickListener(longClick);
	}
	
	private void initImageLoader(){
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.default_image)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
		.cacheOnDisc(true).considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565).build();
	}
	
	private static class AnimateFirstDisplayListener extends
		SimpleImageLoadingListener {
	
		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());
		
		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
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
	
	private void initview(){
		tvTitle.setText(getString(R.string.medchart));
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loadMedChart();
		Bimp.tempSelectBitmap.clear();
		Bimp.max = 0;
		MobclickAgent.onResume(this);       //统计时长
	}
	
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(this);
	}
	
	private void loadMedChart(){
		RequestParams params = new RequestParams();
		params.addHeader("Cookie", Constant.sessionId);
		params.addBodyParameter("token", Constant.token);
		page = 1;
		params.addBodyParameter("page", String.valueOf(page));
		XThread thread = new XThread(this, 0, params, Constant.MEDCHART_LIST_URL, medHandler);
//		thread.setShowDia(true);
		thread.start();
	}
	
	private Handler medHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String result = msg.obj.toString();
				Response response = DataParser.parseMedChartList(result);
				if(response.getList() != null){
					list = (List<MedChart>) response.getList();
					adapter.notifyDataSetChanged();
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
	
	private void addClickListener(){
		btnBack.setOnClickListener(btnClick);
		lineAddMedChart.setOnClickListener(lineClick);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initViewData(){
		if(Constant.medChartListResp!=null && Constant.medChartListResp.getList()!=null){
			list = (List<MedChart>) Constant.medChartListResp.getList();
		}else{
			list = new ArrayList<MedChart>(); 
		}
		adapter = new MedChartAdapter();
		listView.setAdapter(adapter);
		refreshableView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				// TODO Auto-generated method stub
				// 加载操作
				new Handler()
				{
					@Override
					public void handleMessage(Message msg)
					{
						// 千万别忘了告诉控件加载完毕了哦！
						refreshableView.loadmoreFinish(PullToRefreshLayout.SUCCEED);
					}
				}.sendEmptyMessageDelayed(0, 1000);
			}
			
			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				// TODO Auto-generated method stub
				// 加载操作
				Handler h = new Handler()
				{
					@Override
					public void handleMessage(Message msg)
					{
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
				XThread thread = new XThread(MedChartListFragment.this, 0, params, Constant.MEDCHART_LIST_URL, h);
				thread.start();
			}
		});
	}
	
	private Handler refreshComHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				try{
					String result = msg.obj.toString();
					Response response = DataParser.parseMedChartList(result);
					if(response != null && response.getList() != null && !response.getList().isEmpty()){
						List<MedChart> tmpList = (List<MedChart>) response.getList();
						list.addAll(tmpList);
						adapter.notifyDataSetChanged();
						pageBackUp = page;
					}
				}catch(Exception e){
					
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
	
	private Handler pullHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
//			pullListView.onRefreshComplete(); 
		}
	};
	
	private List<MedChart> getListData(){
		List<MedChart> fList = new ArrayList<MedChart>();
		for(int i = 0; i < 5; i++){
			MedChart f = new MedChart();
			f.setPname("adklkl");
			f.setBtime("2015");
			f.setZs("ckcsldkkl");
			fList.add(f);
		}
		return fList;
	}
	
	private OnClickListener btnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == btnBack){
				finish();
			}
		}
	};
	
	private OnClickListener lineClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == lineAddMedChart){
				if(Constant.users.getUsername().equals(LoginActivity.GUEST_NAME)){
					Toast.makeText(mContext, "您现在是游客模式，请使用正式账号登录", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(mContext, LoginActivity.class);
					intent.putExtra("actionType", LoginActivity.ACTION_TYPE_NEED_LOGIN);
					startActivity(intent);
				}else{
					MobclickAgent.onEvent(mContext, "ADD_MED_CHART");
					Intent intent = new Intent(mContext, AddMedChartActivity.class);
					startActivity(intent);
				}
			}
		}
	};

	private void showDelDia(final int position){
		CharSequence[] items = { "删除"
		};
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
		.setTitle("选择")
		.setItems(items,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
							case 0:
								showConfirm(position);
								dialog.dismiss();
								break;
							default:
								break;
						}
					}
				}
		);
		builder.show();
	}

	private void showConfirm(final int position){
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
				.setTitle("温馨提示")
				.setMessage("是否要删除")
        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				deleteItem(position);
				dialog.dismiss();
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		})
		;
		builder.show();
	}

	private void deleteItem(final int position) {
		Handler delHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
					case 0:
						String result = msg.obj.toString();
						Response response = DataParser.parseMedChartDetail(result);
						Constant.medchartResp = response;
						if(response.getRet() == 1){
							Toast.makeText(mContext, "删除成功", Toast.LENGTH_LONG).show();
							list.remove(position);
							adapter.notifyDataSetChanged();
						}else{
							Toast.makeText(mContext, "不属于本医生的病历", Toast.LENGTH_SHORT).show();
						}
						break;
					case -1:
						Toast.makeText(mContext, "操作失败，请重试", Toast.LENGTH_SHORT).show();
						break;
					default:
						break;
				}
			}
		};
		RequestParams params = new RequestParams();
		params.addHeader("Cookie", Constant.sessionId);
		params.addBodyParameter("token", Constant.token);
		params.addBodyParameter("id", list.get(position).getId());
		XThread thread = new XThread(this, 0, params, Constant.MEDCHART_DELETE_URL, delHandler);
		thread.setShowDia(true);
		thread.start();
	}

	private void loadMedChart(String id){
		RequestParams params = new RequestParams();
		params.addHeader("Cookie", Constant.sessionId);
		params.addBodyParameter("token", Constant.token);
		params.addBodyParameter("id", id);
		XThread thread = new XThread(this, 0, params, Constant.MEDCHART_DETAIL_URL, loadHandler);
		thread.setShowDia(true);
		thread.start();
	}
	
	private Handler loadHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String result = msg.obj.toString();
				Response response = DataParser.parseMedChartDetail(result);
				Constant.medchartResp = response;
				if(response.getRet() == 1){
					Intent intent = new Intent(mContext, MedChartDetailActivity.class);
					startActivity(intent);
				}else{
					Toast.makeText(mContext, response.getMsg(), Toast.LENGTH_LONG).show();
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			NewsItemHolder newsHolder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.medchart_item, null);
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
			String imgUrl = null;
			if(list.get(position).getImgs()!=null && !list.get(position).getImgs().isEmpty()){
				imgUrl = list.get(position).getImgs().get(0).getImgPath();
			}
			imageLoader.displayImage(imgUrl,
					newsHolder.imgNewsPic, options, animateFirstListener);
			try{
//				System.out.println("pname: " + list.get(position).getPname());
				String sex = list.get(position).getSex()==null ? "男" : (list.get(position).getSex().equals("1") ? "男" : "女");
				String age = list.get(position).getAge()==null ? "" : list.get(position).getAge();
				String desc = "西医诊断：" + (list.get(position).getXdiagnosis()==null ? "" : list.get(position).getXdiagnosis());
				System.out.println("CAO: " + desc);
				String date = list.get(position).getBtime()==null ? "" : list.get(position).getBtime();
				newsHolder.title.setText(list.get(position).getPname() + " " + sex + " " + age + "岁");
				newsHolder.description.setText(desc);
				newsHolder.date_text.setText(date);
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						loadMedChart(list.get(position).getId());
					}
				});
				convertView.setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						showDelDia(position);
						return false;
					}
				});
			}catch(Exception e){
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
