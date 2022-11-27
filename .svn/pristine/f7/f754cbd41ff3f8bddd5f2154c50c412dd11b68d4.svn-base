package com.ukang.baiyu.activity.me;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.entity.AppLink;
import com.ukang.baiyu.entity.SearchNews;
import com.ukang.baiyu.fragments.tools.BaseFragment;
import com.ukang.baiyu.view.pulltorefresh.library.PullToRefreshBase;
import com.ukang.baiyu.view.pulltorefresh.library.PullToRefreshListView;
import com.ukang.baiyu.view.pulltorefresh.library.PullToRefreshBase.Mode;
import com.ukang.baiyu.activity.R;

/**
 * 我的收藏
 * @author SAN
 *
 */
public class MyAllArtFragment extends BaseFragment {
	
	private Activity mContext;
	
	@ViewInject(R.id.line_back)
	private LinearLayout lineBack;
	@ViewInject(R.id.iv_back)
	private ImageButton btnBack;
	@ViewInject(R.id.tv_title)
	private TextView tvTitle;
	@ViewInject(R.id.ib_search)
	private ImageButton btnSearch;
	@ViewInject(R.id.ib_modi_user)
	private ImageButton btnModiUser;
	
	private ListView listView;
	@ViewInject(R.id.list_view)
	private PullToRefreshListView pullListView;
	private StoreAdapter adapter;
	private List<SearchNews> myStoreList;
	
	public MyAllArtFragment(){
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.mystore,
				null);
		ViewUtils.inject(this, rootView);
		lineBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(btnClick);
		tvTitle.setText(R.string.my_store);
		btnSearch.setVisibility(View.VISIBLE);
		btnSearch.setOnClickListener(btnClick);
//		myStoreList = new ArrayList<SearchNews>();
		
		pullListView.setMode(Mode.PULL_FROM_END);
		listView = pullListView.getRefreshableView();
		pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				if(Constant.ISDEBUG)
					Log.i("---", "onPullDownToRefresh");
				try {
					pullListView.setRefreshing();
					pullHandler.sendEmptyMessageDelayed(1, 2000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				if(Constant.ISDEBUG)
					Log.i("---", "onPullUpToRefresh");
				try {
					pullListView.setRefreshing();
					pullHandler.sendEmptyMessageDelayed(1, 2000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		myStoreList = getListData();
		adapter = new StoreAdapter(myStoreList);
		listView.setAdapter(adapter);
		
		return rootView;
	}
	
	private Handler pullHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			pullListView.onRefreshComplete(); 
		}
	};
	
	private List<SearchNews> getListData(){
		List<SearchNews> list = new ArrayList<SearchNews>();
		for(int i = 0; i < 10; i++){
			SearchNews news = new SearchNews();
			news.setTitle("SCI论文角色	" + i);
			news.setAuthorstr("全面的拉克丝的客流量，卡卡两三点了卡洛斯的两款车库存量，可参考参考尺寸");
			news.setIssn("0022-095" + i);
			news.setPubdate("2015-02-1" + i);
			list.add(news);
		}
		return list;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		new ShowDataThread().start();
	}
	
	private OnClickListener btnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == btnBack){
				getActivity().finish();
			}else if(v == btnSearch){
				
			}
		}
	};
	
	class StoreAdapter extends BaseAdapter {
		private List<SearchNews> storeList;
		public StoreAdapter(List<SearchNews> storeList){
			this.storeList = storeList;
		}
		@Override
		public int getCount() {
			return storeList.size();
		}

		@Override
		public Object getItem(int position) {
			return storeList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			StoreHolder holder = null;
			if (convertView == null) {
				holder = new StoreHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.allart_list_item, null);
				holder.linNewsPic = (LinearLayout) convertView.findViewById(R.id.linNewsPic);
				holder.imgNewsPic = (ImageView) convertView
						.findViewById(R.id.iv_news_pic);
				holder.title = (TextView) convertView
						.findViewById(R.id.tv_news_title);
				holder.description = (TextView) convertView
						.findViewById(R.id.tv_news_desc);
				holder.author = (TextView) convertView.findViewById(R.id.tv_news_author);
				holder.date_text = (TextView) convertView
						.findViewById(R.id.tv_news_time);
				convertView.setTag(holder);
			} else {
				holder = (StoreHolder) convertView.getTag();
			}
			try{
				holder.title.setText(storeList.get(position).getTitle());
				holder.author.setText(((SearchNews)storeList.get(position)).getAuthorstr());
				holder.author.setVisibility(View.VISIBLE);
				holder.description.setText("ISSN：" + storeList.get(position).getIssn());
				holder.date_text.setText("最后更新日期：" + storeList.get(position).getPubdate());
				holder.date_text.setVisibility(View.VISIBLE);
				convertView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
					}
				});
			}catch(Exception e){
				e.printStackTrace();
			}
			return convertView;
		}
	}

	class StoreHolder {
		LinearLayout linNewsPic;
		ImageView imgNewsPic;
		TextView title;
		TextView description;
		TextView author;
		TextView date_text;
	}
}
