package com.ukang.baiyu.fragments.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.common.DataParser;
import com.ukang.baiyu.common.LocalDisplay;
import com.ukang.baiyu.common.MWDUtils;
import com.ukang.baiyu.entity.DocNews;
import com.ukang.baiyu.entity.News;
import com.ukang.baiyu.entity.NewsDetail;
import com.ukang.baiyu.entity.Response;
import com.ukang.baiyu.entity.SearchDB;
import com.ukang.baiyu.entity.SearchNews;
import com.ukang.baiyu.fragments.tools.SearchNewsFragment.NewsAdapter;
import com.ukang.baiyu.thread.XThread;
import com.ukang.baiyu.view.pulltorefresh.library.PullToRefreshBase;
import com.ukang.baiyu.view.pulltorefresh.library.PullToRefreshListView;
import com.ukang.baiyu.view.pulltorefresh.library.PullToRefreshBase.Mode;
import com.ukang.baiyu.views.PullToRefreshLayout;
import com.ukang.baiyu.views.PullableListView;
import com.ukang.baiyu.views.PullToRefreshLayout.OnRefreshListener;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.activity.science.NewsDetailActivity;
import com.ukang.baiyu.activity.science.ScienceActivity;
import com.ukang.baiyu.activity.science.SearchActivity;

/**
 * 搜索列表
 * @author SAN
 *
 */
public class SearchListFragment extends BaseFragment {
	
	private ListView listView;
	private PullToRefreshListView pullListView;
	
	private Activity mContext;
	private NewsAdapter adapter;
	private List<SearchDB> list = new ArrayList<SearchDB>();
	private List<SearchNews> newsList = new ArrayList<SearchNews>();
	private LinearLayout viewContainer;
	private int channalId;
	private Response response = null;
	
	public SearchListFragment(){
		super();
	}
	
	public SearchListFragment(int channalId){
		super();
		this.channalId = channalId;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.search_news_list,
				null);
		ViewUtils.inject(this, rootView);
		pullListView = (PullToRefreshListView) rootView.findViewById(R.id.list_view);
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
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		System.out.println("onActivityCreated()..." + channalId);
		if (channalId == 1 || channalId == 2 || channalId == 3) {
			response = Constant.searchAllResp;
			if(response == null || response.getList() == null){
				list = new ArrayList<SearchDB>();
			}else{
				list = (List<SearchDB>) response.getList();
			}
			if(adapter == null){
				adapter = new NewsAdapter(list);
				listView.setAdapter(adapter);
			}else{
				adapter.list = list;
				adapter.notifyDataSetChanged();
			}
		}else if(channalId == 4){
			response = Constant.searchArticleResp;
			if(response == null || response.getList() == null){
				newsList = new ArrayList<SearchNews>();
			}else{
				newsList = (List<SearchNews>) response.getList();
			}
			if(adapter == null){
				adapter = new NewsAdapter(newsList);
				listView.setAdapter(adapter);
			}else{
				adapter.list = newsList;
				adapter.notifyDataSetChanged();
			}
		}
	}
	
	Handler initHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
//			initData();
		}
	};
	
	private Handler pullHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			pullListView.onRefreshComplete(); 
		}
	};
	
	private Handler newsDetailHandler = new Handler(){

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
				getActivity().startActivity(intent);
//				getActivity().overridePendingTransition(R.anim.slide_left_in,
//						R.anim.slide_left_out);
				break;
			case -1:
				Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
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
		}
	};
	
	@Override  
	public void onResume() {  
	    super.onResume();  
	    System.out.println("onResume..." + channalId);
	}  
	  
	@Override  
	public void onPause() {  
	    super.onPause();  
	}  

	class NewsAdapter extends BaseAdapter {
		private List list;
		public NewsAdapter(List list){
			this.list = list;
		}
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
				newsHolder = new NewsItemHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.search_list_item, null);
				newsHolder.linNewsPic = (LinearLayout) convertView.findViewById(R.id.linNewsPic);
				newsHolder.imgNewsPic = (ImageView) convertView
						.findViewById(R.id.iv_news_pic);
				newsHolder.title = (TextView) convertView
						.findViewById(R.id.tv_news_title);
				newsHolder.description = (TextView) convertView
						.findViewById(R.id.tv_news_desc);
				newsHolder.author = (TextView) convertView.findViewById(R.id.tv_news_author);
				newsHolder.date_text = (TextView) convertView
						.findViewById(R.id.tv_news_time);
				convertView.setTag(newsHolder);
			} else {
				newsHolder = (NewsItemHolder) convertView.getTag();
			}
//			if(list.get(position).getThumb() == null || list.get(position).getThumb().equals("")){
				newsHolder.linNewsPic.setVisibility(View.GONE);
//			}else{
//				newsHolder.linNewsPic.setVisibility(View.VISIBLE);
//				imageLoader.displayImage(list.get(position).getThumb(),
//						newsHolder.imgNewsPic, options, animateFirstListener);
//			}
			newsHolder.date_text.setVisibility(View.GONE);
			try{
				String title = "";
				String desc = "";
				String id = "";
				if(channalId == 1 || channalId == 2 || channalId == 3){
					title = ((SearchDB)list.get(position)).getTitle();
					desc = ((SearchDB)list.get(position)).getDescription();
					newsHolder.author.setVisibility(View.GONE);
					newsHolder.date_text.setVisibility(View.GONE);
				}else{
					title = ((SearchNews)list.get(position)).getTitle();
					desc = "卷标：";
					newsHolder.author.setText(((SearchNews)list.get(position)).getAuthorstr());
					newsHolder.author.setVisibility(View.VISIBLE);
					newsHolder.date_text.setText("最后更新日期：" + ((SearchNews)list.get(position)).getPubdate());
					newsHolder.date_text.setVisibility(View.VISIBLE);
				}
				newsHolder.title.setText(title);
				newsHolder.description.setText(desc);
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),
								NewsDetailActivity.class);
						if(channalId == 1 || channalId == 2 || channalId == 3){
							intent.putExtra("id", ((SearchDB)list.get(position)).getId());
						}else{
							intent.putExtra("id", ((SearchNews)list.get(position)).getPmid());
						}
						intent.putExtra("NEWS_KIND", 3);
						getActivity().startActivity(intent);
					}
				});
			}catch(Exception e){
				e.printStackTrace();
			}
			return convertView;
		}
	}

	class NewsItemHolder {
		LinearLayout linNewsPic;
		ImageView imgNewsPic;
		TextView title;
		TextView description;
		TextView author;
		TextView date_text;
	}
}
