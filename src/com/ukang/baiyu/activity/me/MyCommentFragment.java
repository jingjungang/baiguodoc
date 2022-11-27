package com.ukang.baiyu.activity.me;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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
import com.ukang.baiyu.entity.Comment;
import com.ukang.baiyu.entity.SearchNews;
import com.ukang.baiyu.fragments.tools.BaseFragment;
import com.ukang.baiyu.view.pulltorefresh.library.PullToRefreshBase;
import com.ukang.baiyu.view.pulltorefresh.library.PullToRefreshListView;
import com.ukang.baiyu.view.pulltorefresh.library.PullToRefreshBase.Mode;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.activity.science.NewsDetailActivity;

/**
 * 我的评论
 * @author SAN
 *
 */
public class MyCommentFragment extends BaseFragment {
	
	private Activity mContext;
	
	@ViewInject(R.id.line_back)
	private LinearLayout lineBack;
	@ViewInject(R.id.iv_back)
	private ImageButton btnBack;
	@ViewInject(R.id.tv_title)
	private TextView tvTitle;
//	@ViewInject(R.id.ib_search)
//	private ImageButton btnSearch;
//	@ViewInject(R.id.ib_modi_user)
//	private ImageButton btnModiUser;
	@ViewInject(R.id.line_cmt)
	private LinearLayout lineCmt;
	@ViewInject(R.id.line_share)
	private LinearLayout lineShare;
	
	private ListView listView;
	@ViewInject(R.id.list_view)
	private PullToRefreshListView pullListView;
	private CommentAdapter adapter;
	private List<Comment> commentList;
	
	public MyCommentFragment(){
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
		View rootView = inflater.inflate(R.layout.my_comment,
				null);
		ViewUtils.inject(this, rootView);
		lineBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(btnClick);
		tvTitle.setText(R.string.my_cmt);
//		btnSearch.setVisibility(View.VISIBLE);
//		btnSearch.setOnClickListener(btnClick);
//		myStoreList = new ArrayList<SearchNews>();
//		lineCmt.setVisibility(View.VISIBLE);
		lineShare.setVisibility(View.GONE);

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

		commentList = getListData();
		adapter = new CommentAdapter();
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
	
	private List<Comment> getListData(){
		List<Comment> list = new ArrayList<Comment>();
		if(Constant.cmtListResp!=null && Constant.cmtListResp.getList()!=null){
			list = (List<Comment>) Constant.cmtListResp.getList();
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
			}
//			else if(v == btnSearch){
//				
//			}
		}
	};
	
	class CommentAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return commentList.size();
		}

		@Override
		public Object getItem(int position) {
			return commentList.get(position);
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
				convertView = LayoutInflater.from(mContext).inflate(R.layout.cmt_list_item, null);
				holder.linNewsPic = (LinearLayout) convertView.findViewById(R.id.linNewsPic);
				holder.imgNewsPic = (ImageView) convertView
						.findViewById(R.id.iv_news_pic);
				holder.title = (TextView) convertView
						.findViewById(R.id.tv_news_title);
				holder.description = (TextView) convertView
						.findViewById(R.id.tv_news_desc);
				holder.date_text = (TextView) convertView
						.findViewById(R.id.tv_news_time);
				convertView.setTag(holder);
			} else {
				holder = (StoreHolder) convertView.getTag();
			}
//			imageLoader.displayImage("https://wifamily.blob.core.chinacloudapi.cn/wom/2015052712084080.jpg",
//					holder.imgNewsPic, options, animateFirstListener);
			try{
				holder.title.setText(commentList.get(position).getTitle());
				holder.description.setText(commentList.get(position).getContent());
				holder.date_text.setText(commentList.get(position).getAdd_time());
				convertView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getActivity(),
								NewsDetailActivity.class);
						intent.putExtra("id", commentList.get(position).getLinkid());
						int type = Integer.parseInt(commentList.get(position).getType());
						int kind = 3;
						switch (type) {
							case 1:
								kind = 3;
								break;
							case 2:
								kind = 2;
								break;
							case 3:
								kind = 1;
								break;
						}
						intent.putExtra("NEWS_KIND", kind);
						getActivity().startActivity(intent);
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
		TextView date_text;
	}
}
