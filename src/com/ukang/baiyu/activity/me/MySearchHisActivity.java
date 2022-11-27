package com.ukang.baiyu.activity.me;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.common.DateUtil;
import com.ukang.baiyu.systembartint.SystemBarTintManager;
import com.ukang.baiyu.table.SearchHistory;
import com.ukang.baiyu.view.pulltorefresh.library.PullToRefreshBase;
import com.ukang.baiyu.view.pulltorefresh.library.PullToRefreshListView;
import com.ukang.baiyu.view.pulltorefresh.library.PullToRefreshBase.Mode;
import com.ukang.baiyu.activity.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 我的收藏
 * @author SAN
 *
 */
public class MySearchHisActivity extends FragmentActivity {
	
	private Activity mContext;
	private static SystemBarTintManager tintManager;
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
	private SearchHisAdapter adapter;
	private List<SearchHistory> mySearchList;
	
	private DbUtils dbUtils;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mystore);
		mContext = this;
		dbUtils = DbUtils.create(mContext);
		ViewUtils.inject(this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.index_title_color);
		initview();
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initview(){
		lineBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(btnClick);
		tvTitle.setText(R.string.my_search_his);
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
		
		mySearchList = getListData();
		adapter = new SearchHisAdapter(mySearchList);
		listView.setAdapter(adapter);
	}
	
	private Handler pullHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			pullListView.onRefreshComplete(); 
		}
	};
	
	private List<SearchHistory> getListData(){
		List<SearchHistory> list = null;
		try {
			list = dbUtils.findAll(Selector.from(SearchHistory.class).orderBy("update_date", true));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		if(list == null){
			list = new ArrayList<SearchHistory>();
		}
//		for(int i = 0; i < 15; i++){
//			SearchHistory his = new SearchHistory();
//			his.setTitle("快打开打开塑料袋里卡凉快凉快放到拉斯地方快快贷款贷款贷款的" + i);
//			his.setUpdate_time(new Date());
//			list.add(his);
//		}
		return list;
	}
	
	private OnClickListener btnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == btnBack){
				finish();
			}else if(v == btnSearch){
				
			}
		}
	};
	
	class SearchHisAdapter extends BaseAdapter {
		private List<SearchHistory> searchList;
		public SearchHisAdapter(List<SearchHistory> searchList){
			this.searchList = searchList;
		}
		@Override
		public int getCount() {
			return searchList.size();
		}

		@Override
		public Object getItem(int position) {
			return searchList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			SearchHolder holder = null;
			if (convertView == null) {
				holder = new SearchHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.search_his_item, null);
				holder.title = (TextView) convertView
						.findViewById(R.id.tv_search_title);
				holder.date_text = (TextView) convertView
						.findViewById(R.id.tv_search_time);
				holder.delete = (TextView) convertView.findViewById(R.id.tv_del);
				convertView.setTag(holder);
			} else {
				holder = (SearchHolder) convertView.getTag();
			}
			try{
				holder.title.setText(searchList.get(position).getTitle());
				holder.date_text.setText(DateUtil.getYearDate(searchList.get(position).getUpdate_date()));
				holder.delete.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						try {
							dbUtils.delete(searchList.get(position));
						} catch (DbException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						searchList.remove(position);
						adapter.notifyDataSetChanged();
					}
				});
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

	class SearchHolder {
		TextView title;
		TextView date_text;
		TextView delete;
	}
}
