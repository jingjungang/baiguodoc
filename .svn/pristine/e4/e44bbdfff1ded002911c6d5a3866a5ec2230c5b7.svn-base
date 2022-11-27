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
import android.widget.AutoCompleteTextView;
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
 * 客户资料
 * @author SAN
 *
 */
public class SearchNewsFragment extends BaseFragment {
	
	@ViewInject(R.id.filter_edit)
	private AutoCompleteTextView cEdit;
	@ViewInject(R.id.line_container)
	private LinearLayout lineContainer;
	
	private ListView listView;
	private PullToRefreshListView pullListView;
	
	private Activity mContext;
	private NewsAdapter adapter;
	private List<DocNews> list = new ArrayList<DocNews>();
	
	
	public SearchNewsFragment(){
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.search_news_list,
				null);
		ViewUtils.inject(this, rootView);
		cEdit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
//				goRequest(s);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		pullListView = (PullToRefreshListView) rootView.findViewById(R.id.list_view);
		pullListView.setMode(Mode.PULL_FROM_START);
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
		
//		list = (List<DocNews>) response.getList();
		adapter = new NewsAdapter();
		listView.setAdapter(adapter);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
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
			try{
				newsHolder.title.setText(list.get(position).getTitle());
				newsHolder.date_text.setText(list.get(position).getInputtime());
				newsHolder.description.setText(list.get(position).getDescription());
				convertView.setOnClickListener(new OnClickListener() {
	
					@Override
					public void onClick(View v) {
						RequestParams params = new RequestParams();
						params.addHeader("Cookie", Constant.sessionId);
						params.addBodyParameter("token", Constant.token);
						params.addBodyParameter("id", "5272");
						XThread thread = new XThread(mContext, 0, params, Constant.DOCTOR_NEWS_DETAIL_URL, newsDetailHandler);
						thread.setShowDia(true);
						thread.start();
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
