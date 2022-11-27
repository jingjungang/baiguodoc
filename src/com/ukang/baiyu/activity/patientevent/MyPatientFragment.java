package com.ukang.baiyu.activity.patientevent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.activity.login.LoginActivity;
import com.ukang.baiyu.adapter.MyPatienAdapter;
import com.ukang.baiyu.application.MWDApplication;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.fragments.tools.BaseFragment;
import com.ukang.baiyu.systembartint.SystemBarTintManager;
import com.ukang.xlistview.XListView;
import com.ukang.xlistview.XListView.IXListViewListener;

public class MyPatientFragment extends BaseFragment implements
		IXListViewListener, OnClickListener , Observer {

	private XListView patient;
	private Handler handler, mHandler;
	private String result;
	private String token;
	private ImageButton iv_back;
	private TextView tv_title;
	private ImageButton line_add_patient;
	private int page = 1;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private boolean isFirst = true;
	private boolean isRefresh = false;
	private MyPatienAdapter adapter;
	private SystemBarTintManager tintManager;
	ProgressDialog dia;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.my_patient1, null);
		ViewUtils.inject(this, rootView);
		
		tv_title = (TextView) rootView.findViewById(R.id.tv_title);
		tv_title.setText("我的患者");
		patient = (XListView) rootView.findViewById(R.id.lv_patient);
		patient.setPullLoadEnable(true);
		patient.setXListViewListener(this);
		patient.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						PatientInfosActivity.class);
				intent.putExtra("num", 1);
				intent.putExtra("id", (String) list.get(arg2 - 1).get("userid"));
				startActivity(intent);
			}
		});
		line_add_patient = (ImageButton) rootView
				.findViewById(R.id.line_add_patient);
		iv_back = (ImageButton) rootView.findViewById(R.id.iv_back);
		if(Constant.users.getUsername().equals(LoginActivity.GUEST_NAME)){
			line_add_patient.setVisibility(View.GONE);
		}
		iv_back.setVisibility(View.GONE);
		line_add_patient.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		dia = new ProgressDialog(getActivity());
		dia.setMessage("请稍候...");
		dia.setCanceledOnTouchOutside(false);
		dia.show();
		showList();
		return rootView;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.line_add_patient:
			Intent intent = new Intent(getActivity(),
					PatientInfosActivity.class);
			intent.putExtra("num", 2);
			intent.putExtra("isNew", true);
			startActivity(intent);
			break;
		}
	}

	/**
	 * 开启线程访问网络得到数据更新UI
	 */
	private void showList() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					if (isFirst) {
						dia.dismiss();
					} else {
						onLoad();
					}
					showListByResulttg(result);
					if (!isRefresh) {
						page++;
					}
					isFirst = false;
					break;
				}
			}
		};
		// 启动线程来执行任务
		new Thread() {
			public void run() {
				// 请求网络
				token = Constant.token;
				Message m = new Message();
				if (token != null) {
					Postmessage(Constant.MY_PATIENT_URL, String.valueOf(page),
							token);
					m.what = 1;
					// 发送消息到Handler
					handler.sendMessage(m);
				}
			}
		}.start();
	}

	/**
	 * 获取列表
	 */
	public String Postmessage(String Url, String page, String token) {

		try {
			URL url = new URL(Url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("Charset", "utf-8");
			String data = "page=" + URLEncoder.encode(page, "UTF-8")
					+ "&token=" + token;
			conn.setRequestProperty("Content-Length",
					String.valueOf(data.getBytes().length));
			OutputStream os = conn.getOutputStream();
			os.write(data.getBytes());
			os.flush();
			InputStreamReader is = new InputStreamReader(conn.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(is);
			StringBuffer strBuffer = new StringBuffer();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				strBuffer.append(line);
			}
			result = strBuffer.toString();
			is.close();
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 根据返回结果json解析展示list
	 * 
	 * @param re
	 */
	public void showListByResulttg(String json) {
		JSONObject o = null;
		try {
			o = new JSONObject(json);
			// 构建JSON数组对象
			JSONObject oo = o.getJSONObject("info");
			JSONArray array = oo.getJSONArray("info");
			// 从JSON数组对象读取数据
			for (int i = 0; i < array.length(); i++) {
				// 获取各个属性的值
				JSONObject item = array.getJSONObject(i);
				String userid = item.has("id") ? item.getString("id") : "";
				String avatar = item.has("avatar") ? item.getString("avatar")
						: "";
				String nickname = item.has("rname") ? item.getString("rname")
						: "";
				String username = item.has("phone") ? item.getString("phone")
						: "";
				String age = item.has("age") ? item.getString("age") : "";
				String sex = item.has("sex") ? item.getString("sex") : "";

				// 构建数据源
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userid", userid);
				map.put("avatar", avatar);
				map.put("nickname", nickname);
				map.put("username", username);
				map.put("age", age);
				map.put("sex", sex);
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (isFirst) {
			adapter = new MyPatienAdapter(getActivity(), list);
			patient.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		isRefresh = true;
		mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			public void run() {
				list.clear();
				for (int i = 0; i < page; i++) {
					showList();
				}
				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		isRefresh = false;
		showList();
	}

	/** 加载完成 */
	private void onLoad() {
		patient.stopRefresh();
		patient.stopLoadMore();
		patient.setRefreshTime("刚刚");
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		Postmessage(Constant.MY_PATIENT_URL, String.valueOf(page),
				token);
	}

}
