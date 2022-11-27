package com.ukang.baiyu.activity.conference;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.activity.science.NewsDetailActivity;
import com.ukang.baiyu.activity.tools.AppLinkActivity;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.common.DataParser;
import com.ukang.baiyu.entity.Conference;
import com.ukang.baiyu.entity.NewsDetail;
import com.ukang.baiyu.entity.Response;
import com.ukang.baiyu.fragments.tools.BaseFragment;
import com.ukang.baiyu.thread.XThread;
import com.ukang.baiyu.view.pulltorefresh.library.PullToRefreshBase;
import com.ukang.baiyu.view.pulltorefresh.library.PullToRefreshBase.Mode;
import com.ukang.baiyu.view.pulltorefresh.library.PullToRefreshListView;

/**
 * 客户资料
 * @author SAN
 *
 */
@SuppressLint("ValidFragment")
public class ConferenceListFragment extends BaseFragment {
	
//	private PullToRefreshLayout pullLayout;
	private ListView listView;
	private PullToRefreshListView pullListView;
	
	private Activity mContext;
	private NewsAdapter adapter;
	private List<Conference> list = new ArrayList<Conference>();
	private LinearLayout viewContainer;
	private int channalId;
	private int page = 1;
	//page备份，当加载出错时，回退到加载前的page
	private int pageBackUp = 1;
	private String conferenceId;
	private Response response = null;
	
	public ConferenceListFragment(){
		super();
	}
	
	public ConferenceListFragment(int channalId){
		super();
		this.channalId = channalId;
	}
	
	public ConferenceListFragment(int channalId, String conferenceId){
		super();
		this.channalId = channalId;
		this.conferenceId = conferenceId;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_progress_container,
				null);
		viewContainer = (LinearLayout) rootView.findViewById(R.id.container);
//		LocalDisplay.init(mContext);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		new ShowDataThread().start();
	}
	
	class ShowDataThread extends Thread {
		public void run(){
			while(true){
				response = Constant.conferenceMap.get(conferenceId);
				System.out.println("conferenceId: " + conferenceId + " response: " + response); 
				if(response == null){
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					initHandler.sendEmptyMessageDelayed(0, 0);
					break;
				}
			}
		}
	}
	
	Handler initHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			initData();
		}
	};
	
	@SuppressLint("InflateParams")
	@SuppressWarnings({ "unchecked", "rawtypes" })
	void initData(){
		if (mContext == null)
			return;
		try{
			View v = LayoutInflater.from(mContext).inflate(R.layout.news_list, null);
			pullListView = (PullToRefreshListView) v.findViewById(R.id.list_view);
			pullListView.setMode(Mode.PULL_FROM_END);
			
			View headView = LayoutInflater.from(mContext).inflate(R.layout.top, null);
			LinearLayout centerButton = (LinearLayout) headView.findViewById(R.id.center_button);
			if(channalId == 1){
				//第一页才显示轮播
				headView.findViewById(R.id.lunbo_fragment).setVisibility(View.VISIBLE);
				centerButton.setVisibility(View.VISIBLE);
			}
			listView = pullListView.getRefreshableView();
			listView.addHeaderView(headView);
			
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
//						pullHandler.sendEmptyMessageDelayed(1, 2000);
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
								pullListView.onRefreshComplete(); 
							}
						};
						RequestParams p = new RequestParams();
						p.addHeader("Cookie", Constant.sessionId);
						p.addHeader("User-Agent", Constant.USER_AGENT);
						p.addHeader("Cookie", Constant.sessionId);
						p.addBodyParameter("token", Constant.token);
						p.addBodyParameter("page", String.valueOf(++page));
						p.addBodyParameter("id", conferenceId);
						String url = Constant.CONFERENCE_LIST_URL;
						XThread thread = new XThread(0, p, url, h);
						thread.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			list = (List<Conference>) response.getList();
//			list = getListData();
//			System.out.println("conference list: " + list.size());
			adapter = new NewsAdapter();
			listView.setAdapter(adapter);
			
			//设置在滚动时不加载图片
			boolean pauseOnFling = true;
			boolean pauseOnScroll = true;
			listView.setOnScrollListener(new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
			
			viewContainer.removeAllViews();
			viewContainer.addView(v);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private List<Conference> getListData(){
		List<Conference> cList = new ArrayList<Conference>();
		Conference c = new Conference();
		c.setID("30336");
		c.setNAME("第十一次全国基因功能与表观遗传调控学术研讨会");
		c.setADDRESS("上海市");
		c.setDATE_STR("2015-10-13 到 10-16");
		c.setURL("http://c.doctorpda.cn/con/30336");
		c.setTYPE(2);
		Conference c2 = new Conference();
		c2.setID("31350");
		c2.setNAME("第九届中国肿瘤学术大会暨第十五届海峡两岸肿瘤学术大会");
		c2.setADDRESS("湖北省 武汉市");
		c2.setDATE_STR("2015-10-13 到 10-16");
		c2.setURL("http://c.doctorpda.cn/con/31350");
		c2.setTYPE(2);
		Conference c3 = new Conference();
		c3.setID("31857");
		c3.setNAME("西班牙CPHI");
		c3.setADDRESS("国外 西班牙");
		c3.setDATE_STR("2015-10-13 到 10-16");
		c3.setURL("http://c.doctorpda.cn/con/31857");
		c3.setTYPE(2);
		Conference c4 = new Conference();
		c4.setID("31861");
		c4.setNAME("西班牙CPHI");
		c4.setADDRESS("国外 西班牙");
		c4.setDATE_STR("2015-10-13 到 10-16");
		c4.setURL("http://c.doctorpda.cn/con/31861");
		c4.setTYPE(2);
		Conference c5 = new Conference();
		c5.setID("31878");
		c5.setNAME("2015年世界制药原料欧洲展CPhI Worldwide|韩国CPHI|欧洲CPHI|巴西CPHI|俄罗斯CPHI|土耳其CPHI|日本CPHI|印尼CPHI|杭州鼎忻俞小姐15258879265");
		c5.setADDRESS("国外 西班牙");
		c5.setDATE_STR("2015-10-13 到 10-15");
		c5.setURL("http://c.doctorpda.cn/con/31878");
		c5.setTYPE(2);
		Conference c6 = new Conference();
		c6.setID("26150");
		c6.setNAME("2015年第14届欧洲内科联合会（EFIM）");
		c6.setADDRESS("国外 俄罗斯");
		c6.setDATE_STR("2015-10-13 到 10-16");
		c6.setURL("http://c.doctorpda.cn/con/26150");
		c6.setTYPE(2);
		Conference c7 = new Conference();
		c7.setID("31765");
		c7.setNAME("全国第六届“助产技术与管理”培训班暨2015年助产年会通知");
		c7.setADDRESS("重庆市");
		c7.setDATE_STR("2015-10-13 到 10-18");
		c7.setURL("http://c.doctorpda.cn/con/31765");
		c7.setTYPE(2);
		Conference c8 = new Conference();
		c8.setID("31811");
		c8.setNAME("全国第六届“助产技术与管理”培训班暨2015年助产年会");
		c8.setADDRESS("重庆市");
		c8.setDATE_STR("2015-10-13 到 10-18");
		c8.setURL("http://c.doctorpda.cn/con/31811");
		c8.setTYPE(2);
		
		cList.add(c);
		cList.add(c2);
		cList.add(c3);
		cList.add(c4);
		cList.add(c5);
		cList.add(c6);
		cList.add(c7);
		cList.add(c8);
		
		return cList;
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
					Response response = DataParser.parseConferenceList(result);
					if(response != null && response.getList() != null && !response.getList().isEmpty()){
						List<Conference> tmpList = (List<Conference>) response.getList();
						((List<Conference>)Constant.conferenceMap.get(conferenceId).getList()).addAll(tmpList);
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
				getActivity().overridePendingTransition(R.anim.abc_fade_in,
						R.anim.abc_fade_out); 
				break;
			case -1:
				Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};
	
	private Handler appslHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String result = msg.obj.toString();
				Response response = DataParser.parseAppLink(result);
				Constant.appsResp = response;
				Intent intent = new Intent(getActivity(),
						AppLinkActivity.class);
				getActivity().startActivity(intent);
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
						R.layout.conference_item, null);
				newsHolder = new NewsItemHolder();
				newsHolder.title = (TextView) convertView
						.findViewById(R.id.tv_news_title);
				newsHolder.date_text = (TextView) convertView
						.findViewById(R.id.tv_news_time);
				convertView.setTag(newsHolder);
			} else {
				newsHolder = (NewsItemHolder) convertView.getTag();
			}
			try{
				newsHolder.title.setText(list.get(position).getNAME());
				newsHolder.date_text.setText(list.get(position).getDATE_STR());
				convertView.setOnClickListener(new OnClickListener() {
	
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),
								NewsDetailActivity.class);
						intent.putExtra("id", list.get(position).getID());
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
		TextView title;
		TextView date_text;
	}
	
}
