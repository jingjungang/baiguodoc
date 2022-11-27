package com.ukang.baiyu.activity.science;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.common.DataParser;
import com.ukang.baiyu.entity.Response;
import com.ukang.baiyu.fragments.tools.SearchListFragment;
import com.ukang.baiyu.table.ConferenceCategory;
import com.ukang.baiyu.table.SearchHistory;
import com.ukang.baiyu.thread.XThread;
import com.ukang.baiyu.util.animation.ZoomOutPageTransformer;
import com.ukang.baiyu.widget.PagerSlidingTabStrip;
import com.ukang.baiyu.widget.PagerSlidingTabStrip.OnTabSelectListener;
import com.ukang.baiyu.activity.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchFragment extends Fragment {

	@ViewInject(R.id.filter_edit)
	private AutoCompleteTextView cEdit;
	@ViewInject(R.id.tv_search)
	private TextView tvSearch;
	
	private ViewPager contentPager;
	private mPagerAdapter adapter;
	private PagerSlidingTabStrip tabs;
	private Activity mContext;
	private int searchKind;
	
	private DbUtils dbUtils;
	
	public SearchFragment(){
		super();
	}
	
	public SearchFragment(int searchKind){
		this.searchKind = searchKind;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.search_frag, null);
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
		setPager(rootView);
		return rootView;
	}
	
	private void setPager(View rootView) {
		contentPager = (ViewPager) rootView.findViewById(R.id.content_pager);
		adapter = new mPagerAdapter(getActivity().getSupportFragmentManager());
		contentPager.setAdapter(adapter);
		contentPager.setOffscreenPageLimit(5);
		contentPager.setPageTransformer(true, new ZoomOutPageTransformer());
		tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs);
		tabs.setTextColorResource(R.color.black_color);
		tabs.setDividerColorResource(R.color.search_tab_bg_color);
		// tabs.setUnderlineColorResource(R.color.common_list_divider);
		tabs.setIndicatorColorResource(R.color.search_tab_bg_color);
		tabs.setUnderlineColorResource(R.color.search_tab_bg_color);
		tabs.setSelectedTextColorResource(R.color.index_title_color);
		tabs.setIndicatorHeight(5);
		tabs.setViewPager(contentPager);
//		contentPager.setPrepareNumber(0);
//		contentPager.setPrepareNumber(1);
//		contentPager.setPrepareNumber(2);
//		contentPager.setPrepareNumber(3);
//		contentPager.setPrepareNumber(4);
		if(searchKind == SearchActivity.SEARCH_NEWS){
			contentPager.setCurrentItem(3);
			tabs.setCurrentTab(3);
		}
		tabs.setTabSelctListener(tabSelctListener);
	}
	
	private OnTabSelectListener tabSelctListener = new OnTabSelectListener() {
		
		@Override
		public void onTabSelected(int position) {
			// TODO Auto-generated method stub
			
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		dbUtils = DbUtils.create(mContext);
	}
	
	
	private void searchDB(){
		String keyword = cEdit.getText().toString();
		SearchHistory searchHis = null;
		try {
			searchHis = dbUtils.findFirst(Selector.from(SearchHistory.class).where("title", "=", keyword));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(searchHis == null){
			searchHis = new SearchHistory();
			searchHis.setTitle(keyword);
			searchHis.setUpdate_date(new Date());
		}
		try {
			dbUtils.saveOrUpdate(searchHis);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RequestParams params = new RequestParams();
		params.addHeader("Cookie", Constant.sessionId);
		params.addBodyParameter("token", Constant.token);
		params.addBodyParameter("q", keyword);
		params.addBodyParameter("page", "1");
		String url = "";
		if(searchKind == SearchActivity.SEARCH_NEWS){
			url = Constant.NEWS_SEARCH_URL;
		}else{
			url = Constant.DATABASE_SEARCH_URL;
		}
		new XThread(0, params, url, searchDBHandler).start();
	}
	
	private Handler searchDBHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String result = msg.obj.toString();
				if(searchKind == SearchActivity.SEARCH_NEWS){
					Response response = DataParser.parseSearchNewsList(result);
					Constant.searchArticleResp = response;
				}else{
					Response response = DataParser.parseSearchDBList(result);
					Constant.searchAllResp = response;
				}
				adapter.notifyChildDataChange();
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
	
	private class mPagerAdapter extends FragmentStatePagerAdapter {

		ArrayList<String> titles = new ArrayList<String>();
		ArrayList<Fragment> fList = new ArrayList<Fragment>();
		public mPagerAdapter(FragmentManager fm) {
			super(fm);
			initData();
		}
		
		private void initData(){
			titles.add("全部");
			titles.add("药品");
			titles.add("疾病");
			titles.add("文献");
			titles.add("文档");
			fList.add(new SearchListFragment(1));
			fList.add(new SearchListFragment(2));
			fList.add(new SearchListFragment(3));
			fList.add(new SearchListFragment(4));
			fList.add(new SearchListFragment(5));
		}

		@Override
		public Fragment getItem(int position) {
			System.out.println("Search getitem..." + position);
			return fList.get(position);
		}

		@Override
		public int getCount() {
			return titles.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles.get(position);
		}

		public void notifyChildDataChange(){
			for(int i = 0; i < fList.size(); i++){
				fList.get(i).onActivityCreated(null);
			}
		}
	}
}
