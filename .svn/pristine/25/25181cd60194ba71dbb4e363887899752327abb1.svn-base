package com.ukang.baiyu.fragments.tools;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.entity.AppLink;
import com.ukang.baiyu.activity.R;

/**
 * 应用推荐
 * @author SAN
 *
 */
public class AppLinkFragment extends BaseFragment {
	
	private Activity mContext;
	
	@ViewInject(R.id.iv_back)
	private ImageButton btnBack;
	@ViewInject(R.id.tv_title)
	private TextView tvTitle;
	@ViewInject(R.id.ib_title_right)
	private ImageButton btnSearch;
	
	@ViewInject(R.id.grid_view)
	private GridView gridView;
	
	private AppLinkAdapter adapter;
	private List<AppLink> appLinkList;
	
	public AppLinkFragment(){
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.app_link,
				null);
		ViewUtils.inject(this, rootView);
		btnBack.setOnClickListener(btnClick);
		tvTitle.setText(R.string.app_vd);
		btnSearch.setVisibility(View.VISIBLE);
		btnSearch.setOnClickListener(btnClick);
		appLinkList = new ArrayList<AppLink>();
		adapter = new AppLinkAdapter(appLinkList);
		gridView.setAdapter(adapter);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		new ShowDataThread().start();
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
	
	private Handler showAppsHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			appLinkList = (List<AppLink>) Constant.appsResp.getList();
			adapter.appList = appLinkList;
			adapter.notifyDataSetChanged();
		}
	};
	
	class ShowDataThread extends Thread{
		public void run(){
			while (true) {
				if(Constant.appsResp == null){
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					showAppsHandler.sendEmptyMessage(0);
					break;
				}
			}
		}
	}
	
	class AppLinkAdapter extends BaseAdapter {
		private List<AppLink> appList;
		public AppLinkAdapter(List<AppLink> appList){
			this.appList = appList;
		}
		@Override
		public int getCount() {
			return appList.size();
		}

		@Override
		public Object getItem(int position) {
			return appList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			AppLinkHolder holder = null;
			if (convertView == null) {
				holder = new AppLinkHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.app_link_item, null);
				holder.tvAppName = (TextView) convertView.findViewById(R.id.tv_app_name);
				holder.ivAppIcon = (ImageView) convertView.findViewById(R.id.iv_app_pic);
				convertView.setTag(holder);
			} else {
				holder = (AppLinkHolder) convertView.getTag();
			}
			try{
				holder.tvAppName.setText(appList.get(position).getDiylink_name());
				imageLoader.displayImage(appList.get(position).getDiylink_img(),
						holder.ivAppIcon, options, animateFirstListener);
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

	class AppLinkHolder {
		ImageView ivAppIcon;
		TextView tvAppName;
	}
}
