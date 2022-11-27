package com.ukang.baiyu.views;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.thread.DialogReqThread;
import com.ukang.baiyu.views.PullToRefreshLayout.OnRefreshListener;

/**
 * 刷新监听器
 * 可监听下拉刷新，以及上拉刷新加载更多
 * @author SAN
 *
 */
public class MyListener implements OnRefreshListener
{
	private Context mContext;
	private List<NameValuePair> params;
	private Handler backHandler;
	public MyListener(Context context, List<NameValuePair> params, Handler backHandler){
		this.mContext = context;
		this.params = params;
		this.backHandler = backHandler;
	}

	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout)
	{
		// 下拉刷新操作
		Handler h = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				String str = (String) msg.obj;
				// 千万别忘了告诉控件刷新完毕了哦！
				Message m = Message.obtain();
				m.obj = str;
				backHandler.sendMessage(m);
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}
		};
		int maxpage = Constant.totalMap.get("") / Constant.PAGE_SIZE + 1;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		Log.d(TAG, "GO DEVICE_LIST shopid: " + Constant.shopList.get(0).getShopId());
		params.add(new BasicNameValuePair("shopid", ""));
		params.add(new BasicNameValuePair("size", String.valueOf(Constant.PAGE_SIZE)));
		DialogReqThread thread = new DialogReqThread(mContext, params, "http", "post", Constant.LOGIN_URL, h);
		thread.start();
	}
	
	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout)
	{
		// 加载操作
		new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				// 千万别忘了告诉控件加载完毕了哦！
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
		}.sendEmptyMessageDelayed(0, 5000);
	}

}
