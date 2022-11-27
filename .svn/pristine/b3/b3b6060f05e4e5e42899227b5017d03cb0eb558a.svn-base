package com.ukang.baiyu.views;

import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.ukang.baiyu.views.PullToLoadMoreLayout.OnRefreshListener;

/**
 * 刷新监听器
 * 可监听上拉刷新，以及上拉刷新加载更多
 * @author SAN
 *
 */
public class LoadMoreListener implements OnRefreshListener
{
	private Context mContext;
	private List<NameValuePair> params;
	private Handler handler;
	public LoadMoreListener(Context context, List<NameValuePair> params){
		this.mContext = context;
		this.params = params;
	}
	
	public LoadMoreListener(Handler handler){
		this.handler = handler;
	}

	@Override
	public void onRefresh(final PullToLoadMoreLayout pullToRefreshLayout)
	{
		// 下拉刷新操作
		Handler h = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				// 千万别忘了告诉控件刷新完毕了哦！
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}
		};
//		h.sendEmptyMessageDelayed(0, 3000);
//		if(requestFinish()){
//			Message msg = Message.obtain();
//			msg.obj = "";
//			h.sendMessageDelayed(msg, 2000);
//		}else{
//			h.sendEmptyMessageDelayed(0, 3000);
//		}
//		RequestThread thread = new RequestThread(params, "http", "get", Constant.GET_USERINFO, h);
//		thread.start();
	}
	
	@Override
	public void onLoadMore(final PullToLoadMoreLayout pullToRefreshLayout)
	{
		// 加载操作
		Handler h = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				handler.sendEmptyMessage(0);
				// 千万别忘了告诉控件加载完毕了哦！
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
		};
		h.sendEmptyMessageDelayed(0, 3000);
//		RequestThread thread = new RequestThread(params, "http", "get", Constant.GET_USERINFO, h);
//		thread.start();
	}

}
