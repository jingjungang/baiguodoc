package com.ukang.baiyu.fragments.tools;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.activity.science.NewsDetailActivity;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.common.DataParser;
import com.ukang.baiyu.entity.AppLink;
import com.ukang.baiyu.entity.Response;
import com.ukang.baiyu.thread.XThread;
import com.ukang.baiyu.view.pulltorefresh.library.PullToRefreshBase;
import com.ukang.baiyu.view.pulltorefresh.library.PullToRefreshBase.Mode;
import com.ukang.baiyu.view.pulltorefresh.library.PullToRefreshListView;

public class SearchAppFragment extends BaseFragment {

	private ListView listView;
	private PullToRefreshListView pullListView;
	private AppsAdapter adapter;
	
	
	@ViewInject(R.id.filter_edit)
	private AutoCompleteTextView cEdit;
	@ViewInject(R.id.tv_search)
	private TextView tvSearch;
	
	private Activity mContext;
	
	private DbUtils dbUtils;
	
	public SearchAppFragment(){
		super();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.search_app, null);
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
		tvSearch.setOnClickListener(btnClick);
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
	
	private Handler pullHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			pullListView.onRefreshComplete(); 
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		dbUtils = DbUtils.create(mContext);
	}
	
	
	private void searchDB(){
		RequestParams params = new RequestParams();
		params.addHeader("Cookie", Constant.sessionId);
		params.addBodyParameter("token", Constant.token);
		params.addBodyParameter("q", cEdit.getText().toString());
		params.addBodyParameter("page", "1");
		new XThread(0, params, Constant.SEARCH_APP_LIST, searchHandler).start();
	}
	
	private Handler searchHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String result = msg.obj.toString();
				Response response = DataParser.parseSearchNewsList(result);
				Constant.searchArticleResp = response;
				if(response != null && response.getList() != null){
					ArrayList<AppLink> list = (ArrayList<AppLink>) response.getList();
					if(adapter == null){
						adapter = new AppsAdapter(list);
						listView.setAdapter(adapter);
					}else{
						adapter.list = list;
						adapter.notifyDataSetChanged();
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
	
	String toJson(String quickTime, String optionalTime){
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
			if(v == tvSearch){
				searchDB();
			}
		}
	};
	
	class AppsAdapter extends BaseAdapter {
		private List<AppLink> list;
		public AppsAdapter(List<AppLink> list){
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
			newsHolder.date_text.setVisibility(View.GONE);
			imageLoader.displayImage(list.get(position).getDiylink_img(),
					newsHolder.imgNewsPic, options, animateFirstListener);
			try{
				newsHolder.title.setText(list.get(position).getDiylink_name());
				newsHolder.description.setText(list.get(position).getDiylink_content());
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),
								NewsDetailActivity.class);
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
