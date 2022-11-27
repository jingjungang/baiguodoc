package com.ukang.baiyu.activity.me;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.activity.login.LoginActivity;
import com.ukang.baiyu.activity.science.DbsearchActivity;
import com.ukang.baiyu.activity.science.SearchActivity;
import com.ukang.baiyu.application.MWDApplication;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.common.DataParser;
import com.ukang.baiyu.common.DisplayUtils;
import com.ukang.baiyu.common.FileUtils;
import com.ukang.baiyu.entity.Response;
import com.ukang.baiyu.entity.UserInfo;
import com.ukang.baiyu.fragments.tools.BaseFragment;
import com.ukang.baiyu.thread.XThread;
import com.ukang.baiyu.utils.ContentUtils;
import com.ukang.baiyu.widget.MyDatePickerDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * 修改个人信息
 * 
 * @author SAN
 * 
 */
public class ModifyUserFragment extends BaseFragment {

	private Activity mContext;

	@ViewInject(R.id.iv_back)
	private ImageButton btnBack;
	@ViewInject(R.id.tv_title)
	private TextView tvTitle;
	@ViewInject(R.id.ib_title_right)
	private ImageButton btnSearch;
	@ViewInject(R.id.btn_save)
	private Button btnSave;

	@ViewInject(R.id.tv_user_name)
	private TextView tvUserName;
	@ViewInject(R.id.et_phone)
	private EditText etPhone;
	@ViewInject(R.id.et_nickname)
	private EditText etNickName;
	@ViewInject(R.id.tv_birthday)
	private TextView tvBirthday;
	@ViewInject(R.id.et_email)
	private EditText etEmail;
	@ViewInject(R.id.rg_sex)
	private RadioGroup group;

	// 后面添加字段
	@ViewInject(R.id.et_hospital)
	private EditText etHospital;
	@ViewInject(R.id.et_subject)
	private EditText etSubject;
	// @ViewInject(R.id.et_job)
	// private EditText etJob;
	@ViewInject(R.id.et_edu)
	private EditText etEdu;

	@ViewInject(R.id.spinner)
	private Spinner spin;

	@ViewInject(R.id.iv_cus_pic)
	private ImageView ivManPic;

	private final int CONSULT_DOC_PICTURE = 1000; // 从图库中选择
	private final int CONSULT_DOC_CAMERA = 1001; // 拍照
	private Uri outputFileUri;
	private Bitmap avatorBitmap;

	private String SEX_TYPE;

	public ModifyUserFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.modify_user, null);
		ViewUtils.inject(this, rootView);
		initViewData();
		btnBack.setOnClickListener(btnClick);
		tvTitle.setText(R.string.modi_persional_info);
		btnSearch.setVisibility(View.INVISIBLE);
		btnSearch.setOnClickListener(btnClick);
		ivManPic.setOnClickListener(btnClick);
		tvBirthday.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				createDialog().show();
			}
		});
		btnSave.setOnClickListener(btnClick);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_1:// 30
					SEX_TYPE = "1";
					break;
				case R.id.rb_2:// 60
					SEX_TYPE = "2";
					break;
				}
			}

		});

		return rootView;
	}

	private void initViewData() {
		UserInfo u = Constant.userinfo;
		if (Constant.bitmap != null) {
			ivManPic.setImageBitmap(Constant.bitmap);
		} else if (u.getAvatar() != null && !u.getAvatar().equals("")) {
			imageLoader.displayImage(u.getAvatar(), ivManPic, options,
					animateFirstListener);
		}

		String username = null;
		if (Constant.users.getUsername().equals(LoginActivity.GUEST_NAME)) {
			username = "游客";
		} else
			username = (u.getNickname() == null || u.getNickname().equals("")) ? u
					.getMobile() : u.getNickname();
		tvUserName.setText(username);
		etPhone.setText(u.getMobile());
		etPhone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "电话号码不可修改", Toast.LENGTH_SHORT)
						.show();
			}

		});
		etNickName.setText(u.getNickname());
		tvBirthday.setText(u.getBirthday());
		etEmail.setText(u.getEmail());
		// 后面添加
		etHospital.setText(u.getHospital());
		etSubject.setText(u.getSubject());
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.doc_type_items,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(adapter);
		if (!TextUtils.isEmpty(u.getJob())) {
			int job = Integer.valueOf(u.getJob());
			switch (job) {
			case 0:
				spin.setSelection(0);
				break;
			case 1:
				spin.setSelection(1);
				break;
			case 2:
				spin.setSelection(2);
				break;
			case 3:
				spin.setSelection(3);
				break;
			case 4:
				spin.setSelection(4);
				break;
			}
		}
		etEdu.setText(u.getEdu());

		if (u.getSex() == null || u.getSex().equals("")
				|| u.getSex().equals("1")) {
			((RadioButton) group.getChildAt(0)).setChecked(true);
		} else {
			((RadioButton) group.getChildAt(1)).setChecked(true);
		}
		// SEX_TYPE = u.getSex().equals("0") ? "1" : u.getSex();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	private Calendar c;
	String select_time = "";

	protected Dialog createDialog() {
		Dialog mDialog = null;
		c = Calendar.getInstance();
		c.setTime(new Date());
		mDialog = new MyDatePickerDialog(mContext,
				new DatePickerDialog.OnDateSetListener() {
					boolean fired = false;

					public void onDateSet(DatePicker dp, int year, int month,
							int dayOfMonth) {
						select_time = year + "-" + formatMonthOrDay(month + 1)
								+ "-" + formatMonthOrDay(dayOfMonth);
						try {
							tvBirthday.setText(select_time);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (!fired) {
							fired = true;
						}
					}
				}, c.get(Calendar.YEAR), // 传入年份
				c.get(Calendar.MONTH), // 传入月份
				c.get(Calendar.DAY_OF_MONTH) // 传入天数
		);
		return mDialog;
	}

	private String formatMonthOrDay(int input) {
		String result = "";
		if (input < 10) {
			result = "0" + input;
		} else {
			result = String.valueOf(input);
		}
		return result;
	}

	private OnClickListener btnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == btnBack) {
				getActivity().finish();
			} else if (v == btnSearch) {
				Intent i = new Intent(mContext, DbsearchActivity.class);
				i.putExtra("searchKind", SearchActivity.SEARCH_NEWS);
				startActivity(i);
			} else if (v == ivManPic) {
				showChoosePicDia();
			} else if (v == btnSave) {
				upadateUser();
			}
		}
	};

	private void upadateUser() {
		String nickname = etNickName.getText().toString();
		if (nickname.trim().equals("")) {
			Toast.makeText(mContext, "昵称不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		MobclickAgent.onEvent(mContext, "EDIT_USER");
		RequestParams p = new RequestParams();
		p.addHeader("Cookie", Constant.sessionId);
		p.addHeader("User-Agent", Constant.USER_AGENT);
		p.addHeader("Cookie", Constant.sessionId);
		p.addBodyParameter("token", Constant.token);
		p.addBodyParameter("nickname", nickname);
		p.addBodyParameter("email", etEmail.getText().toString());
		p.addBodyParameter("sex", SEX_TYPE);
		p.addBodyParameter("birthday", tvBirthday.getText().toString());
		p.addBodyParameter("hospital", etHospital.getText().toString());
		p.addBodyParameter("subject", etSubject.getText().toString());
		p.addBodyParameter("job", spin.getSelectedItemPosition() + "");
		p.addBodyParameter("edu", etEdu.getText().toString());
		p.addBodyParameter("version", "2");// 1老版本2新版本 默认1
		XThread thread = new XThread(mContext, 0, p, Constant.UPDATE_USER_URL,
				userHandler);
		thread.setShowDia(true);
		thread.start();
	}

	private Handler userHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String result = msg.obj.toString();
				Response response = DataParser.parseUserInfo(result);
				if (response.getRet() == 1) {
					Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
					UserInfo u = Constant.userinfo;
					u.setMobile(etPhone.getText().toString());
					u.setNickname(etNickName.getText().toString());
					u.setSex(SEX_TYPE);
					u.setEmail(etEmail.getText().toString());
					u.setBirthday(tvBirthday.getText().toString());
					u.setHospital(etHospital.getText().toString());
					u.setSubject(etSubject.getText().toString());
					u.setJob(spin.getSelectedItemPosition() + "");
					mContext.finish();
				} else if (response.getRet() == -2) {
					Toast.makeText(mContext, "邮箱格式错误或者邮箱已经存在",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(mContext, "没有更新项", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case -1:
				Toast.makeText(mContext, "网络异常,稍后再试", Toast.LENGTH_SHORT)
						.show();// msg.obj.toString()
				break;
			default:
				break;
			}
		}
	};

	void showChoosePicDia() {
		CharSequence[] items = { "相册", "相机"
		// , "相册2"
		};
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
				.setTitle("选择").setItems(items,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									chooseImage();
									break;
								case 1:
									goCamera();
									break;
								default:
									break;
								}
							}
						});
		builder.show();
	}

	public void goCamera() {
		String path = Environment.getExternalStorageDirectory()
				+ File.separator + "YuYi" + File.separator + "temp";
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}
		File file = new File(path, System.currentTimeMillis() + ".jpg");
		outputFileUri = Uri.fromFile(file);

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		startActivityForResult(intent, CONSULT_DOC_CAMERA);
	}

	public void chooseImage() {
		Intent intent = new Intent();
		// 由于4.4后面action为ACTION_OPEN_DOCUMENT，由于当前开发的SDK版本不足4.4，为了兼容4.4及以后版本，采用这种方式
		intent.setAction(Intent.ACTION_PICK);
		// intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		// intent.setType("image/*");
		startActivityForResult(Intent.createChooser(intent, "选择图片"),
				CONSULT_DOC_PICTURE);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0)
			return;
		if (resultCode == -1) {
			if (requestCode == CONSULT_DOC_PICTURE) {
				Log.d("LoginServiceImpl", "=-=-=-=data: " + data);
				if (data == null) {
					Log.d("LoginServiceImpl",
							"=-=-=-=onActivityResult data return null");
					return;
				}

				Uri uri = data.getData();
				try {
					Log.d("LoginServiceImpl", "=-=-=-=uri: " + uri);
					// String[] proj = { MediaStore.Images.Media.DATA };
					// Cursor cursor = mContext.getContentResolver().query(uri,
					// proj, // Which
					// // columns
					// // to return
					// null, // WHERE clause; which rows to return (all rows)
					// null, // WHERE clause selection arguments (none)
					// null); // Order-by clause (ascending by name)
					//
					// int column_index =
					// cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					// cursor.moveToFirst();
					//
					// String fileName = cursor.getString(column_index);
					String fileName = ContentUtils.getImageAbsolutePath(
							getActivity(), uri);

					// Uri.fromFile(new File(fileName));
					String path = Environment.getExternalStorageDirectory()
							+ File.separator + "YuYi" + File.separator + "temp";
					File f = new File(path);
					if (!f.exists()) {
						f.mkdirs();
					}
					String newFileName = path + File.separator
							+ System.currentTimeMillis() + ".jpg";
					File sourceFile = new File(fileName);
					File outFile = new File(newFileName);
					FileUtils.fileChannelCopy(sourceFile, outFile);
					FileUtils.copyFile(fileName, newFileName);
					if (outFile.length() < 100) {
						try {
							outFile = sourceFile.createTempFile("temp", "");
							FileUtils.copyFile(fileName,
									outFile.getAbsolutePath());
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					outputFileUri = Uri.fromFile(outFile);
					avatorBitmap = getPicBitmap();
					uploadImg();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT)
							.show();
				}
			} else if (requestCode == CONSULT_DOC_CAMERA) {
				try {
					avatorBitmap = getPicBitmap();
					uploadImg();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private Bitmap getPicBitmap() {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			// 先设置为TRUE不加载到内存中，但可以得到宽和高
			options.inJustDecodeBounds = true;
			bitmap = BitmapFactory.decodeFile(outputFileUri.getPath(), options); // 此时返回bm为空
			options.inJustDecodeBounds = false;
			// 计算缩放比
			int devW = DisplayUtils.getWidthAndHeight(mContext)[0];
			int outW = options.outWidth > options.outHeight ? options.outWidth
					: options.outHeight;
			int be = (int) (outW / (float) (1024));
			if (be <= 0)
				be = 1;
			options.inSampleSize = be;
			// 这样就不会内存溢出了
			bitmap = BitmapFactory.decodeFile(outputFileUri.getPath(), options);
			BufferedOutputStream bos = null;
			bos = new BufferedOutputStream(new FileOutputStream(
					outputFileUri.getPath()));
			bitmap.compress(CompressFormat.JPEG, 50, bos);
			bos.flush();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		return bitmap;
	}

	private void uploadImg() {
		RequestParams params = new RequestParams();
		params.addHeader("Cookie", Constant.sessionId);
		params.addHeader("User-Agent", Constant.USER_AGENT);
		params.addBodyParameter("token", Constant.token);
		File f = new File(outputFileUri.getPath());
		params.addBodyParameter("avatar", f);
		XThread thread = new XThread(mContext, 0, params,
				Constant.UPLOAD_IMG_URL, uploadHandler);
		thread.setShowDia(true);
		thread.start();
	}

	private Handler uploadHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String str = (String) msg.obj;
				ivManPic.setImageBitmap(avatorBitmap);
				Constant.bitmap = avatorBitmap;
				// MWDApplication ma = ((MWDApplication)
				// getActivity().getApplication());
				// ma.PageNotificationer.notifition();
				System.out.println("upload result : " + str);
				// Response response = DataParser.parseAccountInfo(str);
				try {

				} catch (Exception e) {

				}
				break;
			case -1:
				break;
			default:
				break;
			}
		}
	};
}
