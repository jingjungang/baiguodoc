package com.ukang.baiyu.activity.me;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.activity.R.id;
import com.ukang.baiyu.activity.R.layout;
import com.ukang.baiyu.activity.R.string;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.common.DataParser;
import com.ukang.baiyu.entity.Response;
import com.ukang.baiyu.entity.UserInfo;
import com.ukang.baiyu.thread.XThread;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FeedBackActivity extends Activity{
	
	private Context mContext;
	
	@ViewInject(R.id.iv_back)
	private ImageButton btnBack;
	@ViewInject(R.id.tv_title)
	private TextView tvTitle;
	@ViewInject(R.id.feedback_content)
	private EditText etFeedBack;
	@ViewInject(R.id.confirm)
	private Button confirm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed_back);
		mContext = this;
		ViewUtils.inject(this);
		initview();
		addclicklistener();
	}
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onResume(this);       //统计时长
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(this);
	}

	private void initview(){
		tvTitle.setText(getString(R.string.feed_back));
	}
	
	/** 添加点击监听事件 */
	private void addclicklistener(){
		btnBack.setOnClickListener(backclick);
		confirm.setOnClickListener(confirmclick);
	}
	
	private OnClickListener confirmclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String str = etFeedBack.getText().toString();
			if(str != null && str.trim().length() > 0){
				feedBack(str);
			}
		}
	};
	
	void feedBack(String message){
		RequestParams p = new RequestParams();
		p.addHeader("Cookie", Constant.sessionId);
		p.addHeader("User-Agent", Constant.USER_AGENT);
		p.addBodyParameter("token", Constant.token);
		p.addBodyParameter("message", message);
		XThread thread = new XThread(this, 0, p, Constant.ADD_FEED_BACK_URL, feedBackHandler);
		thread.setShowDia(true);
		thread.start();
	}
	
	private Handler feedBackHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String result = msg.obj.toString();
				Response response = DataParser.parseFeedBack(result);
				if(response.getRet() == 1){
					Toast.makeText(mContext, "提交成功", Toast.LENGTH_SHORT).show();
					finish();
				}
				break;
			case -1:
				Toast.makeText(mContext, "提交失败", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};
	
	/** 返回按钮点击  */
	private OnClickListener backclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};
	
}


