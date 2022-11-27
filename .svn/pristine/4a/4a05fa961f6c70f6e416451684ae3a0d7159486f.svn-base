package com.ukang.baiyu.activity.science;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.activity.conference.ConferenceActivity;
import com.ukang.baiyu.activity.main.TabMainActivity;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.common.DataParser;
import com.ukang.baiyu.entity.Response;
import com.ukang.baiyu.fragments.tools.BaseFragment;
import com.ukang.baiyu.thread.XThread;
import com.ukang.baiyu.utils.LoadingImgUtil;
import com.ukang.baiyu.views.AutoScrollTextView;
import com.ukang.baiyu.widget.MyToast;
import com.umeng.analytics.MobclickAgent;

/**
 * Fragment-目录总纲
 * 
 * @author SAN
 * 
 */
public class ScienceMenuFragment_new extends BaseFragment {
	private Activity mContext;

	@ViewInject(R.id.iv_back)
	private ImageButton btnBack;
	@ViewInject(R.id.tv_title)
	private TextView tvTitle;
	@ViewInject(R.id.grid_view)
	private GridView gridView;
	@ViewInject(R.id.img)
	private ImageView ivManPic;
	@ViewInject(R.id.v_guide_root)
	private ViewStub vs_guide_root;
	// @ViewInject(R.id.view)
	// private View v_view;

	View rootView;
	AutoScrollTextView autoScrollTextView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.science_page_new, null);
		ViewUtils.inject(this, rootView);
		init();
		noticeMsg();
		// showGuideSlide();
		return rootView;
	}

	private void showGuideSlide() {
		// TODO Auto-generated method stub
		SharedPreferences sp = mContext.getPreferences(0);
		if (!sp.getString("indicator_layer", "").equals("true")) {
			// v_view.setBackgroundResource(R.drawable.indicator01);
			/*
			 * v_view.setOnClickListener(new OnClickListener() {
			 * 
			 * @Override public void onClick(View arg0) { // TODO Auto-generated
			 * method stub v_view.setVisibility(View.GONE); } });
			 */
			try {// ((ViewStub)rootView.findViewById(R.id.guide_root))
				final View guideSlideView = vs_guide_root.inflate();
				RelativeLayout rl = (RelativeLayout) guideSlideView
						.findViewById(R.id.guide_root);
				if (rl != null) {
					rl.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							guideSlideView.setVisibility(View.GONE);
						}
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			/*
			 * startActivity(new Intent(mContext,
			 * com.ukang.baiyu.activity.main.IndicatorActivity.class));
			 */
			// 标记第一次进入App指示完成
			Editor editor = sp.edit();
			editor.putString("indicator_layer", "true");
			editor.commit();
			MyToast ct = new MyToast(mContext, "欢迎来到白果医疗服务中心");
			ct.setDuration(8000);
			ct.show();
		} else {
			return;
		}
	}

	private void init() {
		// TODO Auto-generated method stub

		// 设置滚动条
		autoScrollTextView = (AutoScrollTextView) rootView
				.findViewById(R.id.textView);
		btnBack.setVisibility(View.INVISIBLE);
		tvTitle.setText(R.string.science_art);
		((RelativeLayout) rootView.findViewById(R.id.rl1))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						MobclickAgent.onEvent(mContext, "MED_CHART");
						loadMedChart();
					}
				});
		((RelativeLayout) rootView.findViewById(R.id.rl2))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.setClass(mContext, ConferenceActivity.class);
						startActivity(intent);
					}
				});
		((RelativeLayout) rootView.findViewById(R.id.rl3))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						MobclickAgent.onEvent(mContext, "PATH_WAY");
						intent.setClass(mContext, PathGuideActivity.class);
						intent.putExtra("NEWS_KIND", 1);
						startActivity(intent);
					}
				});
		((RelativeLayout) rootView.findViewById(R.id.rl4))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Dialog();
					}
				});
	}

	/**
	 * 电子病历-查看
	 */
	private void loadMedChart() {
		Handler medHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					String result = msg.obj.toString();
					Response response = DataParser.parseMedChartList(result);
					Constant.medChartListResp = response;
					Intent intent = new Intent(getActivity(),
							MedChartListActivity.class);
					getActivity().startActivity(intent);
					break;
				case -1:
					Toast.makeText(mContext, msg.obj.toString(),
							Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}
		};
		RequestParams params = new RequestParams();
		params.addHeader("Cookie", Constant.sessionId);
		params.addBodyParameter("token", Constant.token);
		params.addBodyParameter("page", "1");
		XThread thread = new XThread(mContext, 0, params,
				Constant.MEDCHART_LIST_URL, medHandler);
		thread.setShowDia(true);
		thread.start();
	}

	/**
	 * 公告
	 */
	private void noticeMsg() {
		Handler medHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					String result = msg.obj.toString();
					if (!TextUtils.isEmpty(result)) {
						try {
							JSONObject jo = new JSONObject(result);
							String status = jo.getString("status");
							String info = "";
							if (status.equals("1")) {
								info = jo.getString("info");
								if (info.equals("")) { // 数据未编辑
									info = "暂无公告";
								}
								final String img_url = jo.getString("logourl");
								if (!TextUtils.isEmpty(img_url)) {
									// LoadingImgUtil.loadimgAnimate(
									// img_url, ivManPic);
									// imageLoader.displayImage(img_url,
									// ivManPic,
									// options, animateFirstListener);
									ivManPic.setScaleType(ScaleType.CENTER_CROP);
									LoadingImgUtil.loadimgAnimate(img_url,
											ivManPic);
									// BitmapUtils bitmapUtils = new
									// BitmapUtils(mContext);
									// bitmapUtils.display(ivManPic, img_url);
								}
							} else if (status.equals("0")) { // token验证失败
								info = "暂无公告";
							} else if (status.equals("-1")) { // 无数据
								info = "暂无公告";
							}
							autoScrollTextView.initScrollTextView(
									mContext.getWindowManager(), info);
							autoScrollTextView.starScroll();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				case -1:
					autoScrollTextView.initScrollTextView(
							mContext.getWindowManager(), "暂无公告");
					autoScrollTextView.starScroll();
					Toast.makeText(mContext, msg.obj.toString(),
							Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}
		};
		RequestParams params = new RequestParams();
		params.addHeader("Cookie", Constant.sessionId);
		params.addBodyParameter("token", Constant.token);
		params.addBodyParameter("type", "1");
		XThread thread = new XThread(mContext, 0, params,
				Constant.SCIENCE_NOTICE, medHandler);
		thread.setShowDia(true);
		thread.start();
	}

	/**
	 * 科研服务Dialog
	 */
	AlertDialog dia = null;

	private void Dialog() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.science_service, null);
		LinearLayout rl1 = (LinearLayout) v.findViewById(R.id.l1);
		LinearLayout rl2 = (LinearLayout) v.findViewById(R.id.l2);
		LinearLayout rl3 = (LinearLayout) v.findViewById(R.id.l3);
		LinearLayout rl4 = (LinearLayout) v.findViewById(R.id.l6);
		LinearLayout rl5 = (LinearLayout) v.findViewById(R.id.l7);
		LinearLayout rl6 = (LinearLayout) v.findViewById(R.id.l8);
		rl1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, NewsDetailActivity.class);
				intent.putExtra("id", "6");
				intent.putExtra("NEWS_KIND", 4);
				startActivity(intent);
				dia.dismiss();
			}
		});
		rl2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, NewsDetailActivity.class);
				intent.putExtra("id", "7");
				intent.putExtra("NEWS_KIND", 4);
				startActivity(intent);
				dia.dismiss();
			}
		});
		rl3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, NewsDetailActivity.class);
				intent.putExtra("id", "1");
				intent.putExtra("NEWS_KIND", 4);
				startActivity(intent);
				dia.dismiss();
			}
		});
		rl4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, NewsDetailActivity.class);
				intent.putExtra("id", "2");
				intent.putExtra("NEWS_KIND", 4);
				startActivity(intent);
				dia.dismiss();
			}
		});
		rl5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, NewsDetailActivity.class);
				intent.putExtra("id", "3");
				intent.putExtra("NEWS_KIND", 4);
				startActivity(intent);
				dia.dismiss();
			}
		});
		rl6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, NewsDetailActivity.class);
				intent.putExtra("id", "4");
				intent.putExtra("NEWS_KIND", 4);
				startActivity(intent);
				dia.dismiss();
			}
		});
		dia = new AlertDialog.Builder(mContext)
		// .setTitle("请选择科研项目")
		// .setIcon(android.R.drawable.ic_dialog_info)
				.setView(v).show();
	}
}
