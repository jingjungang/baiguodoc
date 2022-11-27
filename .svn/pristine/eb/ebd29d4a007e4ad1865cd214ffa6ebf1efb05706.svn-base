package com.ukang.baiyu.activity.patientevent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.systembartint.SystemBarTintManager;
import com.ukang.baiyu.utils.URLConnectionUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/***
 * 患者基本信息
 * 
 * @author AAA
 *
 */
public class PatientInfosActivity extends Activity implements OnClickListener{

	private TextView tv_title;
	private TextView tv_title_right;
	private ImageButton btnBack;
	/** 姓名，年龄，身高，体重，就诊卡号，出生日期，婚姻状况,，身份证号，联系电话，电子邮箱，通讯地址，所在地区，更新时间 */
	private EditText user_name, user_age, height, weight, tvCard, birthday, marriage, id_card, telephone, email, site,
			area;
	private TextView update_time;
	private ImageView user_sex_iv;
	private Button patient_commit_btn;

	private String Url = Constant.USER_INFOS_URL;
	private static SystemBarTintManager tintManager;
	String result;
	String id;
	private int i;
	private boolean isNew = false;
	ProgressDialog dia;
	String xingbie = "1";
	Handler commit_handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.index_title_color);
		setContentView(R.layout.activity_user_infos);

		init();
		Intent intent = getIntent();
		i = intent.getIntExtra("num", 0);
		if(i == 1){
			patient_commit_btn.setText("保存");
			id = intent.getStringExtra("id");
			dia = new ProgressDialog(PatientInfosActivity.this);
	        dia.setMessage("请稍候...");
	        dia.setCanceledOnTouchOutside(false);
	        getUserInfos();
		}else{
			patient_commit_btn.setText("提交");
			isNew = intent.getBooleanExtra("isNew", false);
		}
		setClick(i);
		
		commit_handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				try {
					JSONObject json = new JSONObject(result);
					int id = json.getInt("status");
					switch(id){
					case -1:
						if(i == 1){
							Toast.makeText(PatientInfosActivity.this, "数据出错 ", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(PatientInfosActivity.this, "电话号码错误 ", Toast.LENGTH_SHORT).show();
						}
						break;
					case -2:
						if(i == 1){
							Toast.makeText(PatientInfosActivity.this, "修改失败 ", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(PatientInfosActivity.this, "电话号码已存在", Toast.LENGTH_SHORT).show();
						}
						break;
					case -3:
						Toast.makeText(PatientInfosActivity.this, "数据错误请从新添加", Toast.LENGTH_SHORT).show();
						break;
					case 1:
						if(i == 1){
							Toast.makeText(PatientInfosActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(PatientInfosActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
						}
						finish();
						break;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.handleMessage(msg);
			}
		};
	}
	
	private void init() {
		user_name = (EditText) findViewById(R.id.user_name);
		user_age = (EditText) findViewById(R.id.user_age);
		height = (EditText) findViewById(R.id.height);
		weight = (EditText) findViewById(R.id.weight);
		tvCard = (EditText) findViewById(R.id.tvCard);
		birthday = (EditText) findViewById(R.id.birthday);
		marriage = (EditText) findViewById(R.id.marriage);
		id_card = (EditText) findViewById(R.id.id_card);
		telephone = (EditText) findViewById(R.id.telephone);
		email = (EditText) findViewById(R.id.email);
		site = (EditText) findViewById(R.id.site);
		area = (EditText) findViewById(R.id.area);
		update_time = (TextView) findViewById(R.id.update_time);
		user_sex_iv = (ImageView) findViewById(R.id.user_sex_iv);
		patient_commit_btn = (Button) findViewById(R.id.patient_commit_btn);

		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title_right = (TextView) findViewById(R.id.tv_title_right);
		tv_title.setText("患者详情");
		tv_title_right.setText("编辑");
		tv_title_right.setVisibility(View.VISIBLE);
		btnBack = (ImageButton) findViewById(R.id.iv_back);
		patient_commit_btn.setOnClickListener(this);
		tv_title_right.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.tv_title_right:
			setClick(2);
			break;
		case R.id.iv_back:
			finish();
			break;
		case R.id.patient_commit_btn:
			dia = new ProgressDialog(this);
	        dia.setMessage("请稍候...");
	        dia.setCanceledOnTouchOutside(false);
	        dia.show();
			new Thread(){
				public void run() {
					List<NameValuePair> parameters = new ArrayList<NameValuePair>();
					parameters.add(new BasicNameValuePair("token", Constant.token));
					parameters.add(new BasicNameValuePair("id", id));
					parameters.add(new BasicNameValuePair("rname", user_name.getText().toString().trim()));
					parameters.add(new BasicNameValuePair("age", user_age.getText().toString().trim()));
					parameters.add(new BasicNameValuePair("height", height.getText().toString().trim()));
					parameters.add(new BasicNameValuePair("weight", weight.getText().toString().trim()));
					parameters.add(new BasicNameValuePair("card", tvCard.getText().toString().trim()));
					parameters.add(new BasicNameValuePair("birthdate", birthday.getText().toString().trim()));
					String mar;
					if(marriage.getText().toString().trim().equals("已婚")){
						mar = "1";
					}else if(marriage.getText().toString().trim().equals("未婚")){
						mar = "2";
					}else{
						mar = "0";
					}
					parameters.add(new BasicNameValuePair("marriage", mar));
					parameters.add(new BasicNameValuePair("birthday", id_card.getText().toString().trim()));
					parameters.add(new BasicNameValuePair("phone", telephone.getText().toString().trim()));
					parameters.add(new BasicNameValuePair("email", email.getText().toString().trim()));
					parameters.add(new BasicNameValuePair("address", site.getText().toString().trim()));
					parameters.add(new BasicNameValuePair("area", area.getText().toString().trim()));
					parameters.add(new BasicNameValuePair("sex", xingbie));
					if(i == 1){
						result = URLConnectionUtil.HttpClientPost(Constant.UPDATE_PATIENT_URL, parameters);
					}else{
						result = URLConnectionUtil.HttpClientPost(Constant.ADD_PATIENT_URL, parameters);
					}
					Message m = new Message();
					m.what = 1;
					commit_handler.sendMessage(m);
				};
			}.start();
			finish();
			break;
		}
	}

	public void getUserInfos() {

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					// LoginActivity.stopProgressDialog();
					try {
						JSONObject jo = new JSONObject(result);
						int status = jo.getInt("status");
						switch (status) {
						case -1:
							Toast.makeText(PatientInfosActivity.this, "userid不存在", Toast.LENGTH_SHORT).show();
							dia.dismiss();
							break;
						case -2:
							Toast.makeText(PatientInfosActivity.this, "未获取到用户信息", Toast.LENGTH_SHORT).show();
							dia.dismiss();
							break;
						case -3:
							Toast.makeText(PatientInfosActivity.this, "用户对应病例信息不存在", Toast.LENGTH_SHORT).show();
							dia.dismiss();
							break;
						case 1:
							dia.dismiss();
							JSONObject joi = jo.getJSONObject("info");
							String rname = joi.has("rname")?joi.getString("rname"):"";
							String age = joi.has("age")?joi.getString("age"):"";
							String h = joi.has("height")?joi.getString("height"):"";
							String w = joi.has("weight")?joi.getString("weight"):"";
							String card = joi.has("card")?joi.getString("card"):"";
							String birthdate = joi.has("birthdate")?joi.getString("birthdate"):"";
							int mar = joi.has("marriage")?joi.getInt("marriage"):2;
							String bir_id = joi.has("birthday")?joi.getString("birthday"):"";
							String phone = joi.has("phone")?joi.getString("phone"):"";
							String ema = joi.has("email")?joi.getString("email"):"";
							String address = joi.has("address")?joi.getString("address"):"";
							String ar = joi.has("area")?joi.getString("area"):"";
							String sex = joi.has("sex")?joi.getString("sex"):"";
							user_name.setText(rname);
							user_age.setText(age);
							height.setText(h);
							weight.setText(w);
							tvCard.setText(card);
							birthday.setText(birthdate);
							if (mar == 1) {
								marriage.setText("已婚");
							} else {
								marriage.setText("未婚");
							}
							id_card.setText(bir_id);
							telephone.setText(phone);
							email.setText(ema);
							site.setText(address);
							area.setText(ar);
							if (sex.equals("1")) {
								user_sex_iv.setImageResource(R.drawable.man);
							} else if (sex.equals("2")) {
								user_sex_iv.setImageResource(R.drawable.woman);
							} else if (sex.equals("0")) {
								user_sex_iv.setVisibility(View.GONE);
							}
							xingbie = sex;
//							update_time.setText("更新时间：" + new SimpleDateFormat("yyyy年MM月dd日")
//									.format(new Date(joi.getLong("update_time") * 1000l)));
							break;
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		new Thread() {
			public void run() {
				result = Postmessage(Url, Constant.token,id);
				Message m = new Message();
				m.what = 1;
				handler.sendMessage(m);
			}
		}.start();
	}

	public String Postmessage(String Url, String Token,String id) {

		// String value="";
		try {
			URL url = new URL(Url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Charset", "utf-8");
			String data = "token=" + URLEncoder.encode(Token, "UTF-8")+"&id=" +id;
			conn.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
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

	private void setClick(int i){
		switch(i){
		case 1:
			user_name.setEnabled(false);
			user_age.setEnabled(false);
			height.setEnabled(false);
			weight.setEnabled(false);
			tvCard.setEnabled(false);
			birthday.setEnabled(false);
			marriage.setEnabled(false);
			id_card.setEnabled(false);
			telephone.setEnabled(false);
			email.setEnabled(false);
			site.setEnabled(false);
			area.setEnabled(false);
			update_time.setEnabled(false);
			break;
		case 2:
			user_name.setEnabled(true);
			user_age.setEnabled(true);
			height.setEnabled(true);
			weight.setEnabled(true);
			tvCard.setEnabled(true);
			birthday.setEnabled(true);
			marriage.setEnabled(true);
			id_card.setEnabled(true);
			telephone.setEnabled(true);
			email.setEnabled(true);
			site.setEnabled(true);
			area.setEnabled(true);
			update_time.setEnabled(true);
			if(isNew){
				user_name.setHint("输入内容");
				user_age.setHint("输入内容");
				height.setHint("输入内容");
				weight.setHint("输入内容");
				tvCard.setHint("输入内容");
				birthday.setHint("输入内容");
				marriage.setHint("输入内容");
				id_card.setHint("输入内容");
				telephone.setHint("输入内容");
				email.setHint("输入内容");
				site.setHint("输入内容");
				area.setHint("输入内容");
			}
			break;
		}
	}

}
