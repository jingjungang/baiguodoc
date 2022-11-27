package com.ukang.baiyu.activity.science;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.activity.login.LoginActivity;
import com.ukang.baiyu.activity.tools.AlbumActivity;
import com.ukang.baiyu.activity.tools.GalleryActivity;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.common.DataParser;
import com.ukang.baiyu.entity.Response;
import com.ukang.baiyu.systembartint.SystemBarTintManager;
import com.ukang.baiyu.view.photo.util.Bimp;
import com.ukang.baiyu.view.photo.util.ImageItem;
import com.ukang.baiyu.view.photo.util.PublicWay;
import com.ukang.baiyu.view.photo.util.Res;
import com.ukang.baiyu.view.swipebacklayout.lib.SwipeBackLayout;
import com.ukang.baiyu.view.swipebacklayout.lib.app.SwipeBackActivity;
import com.ukang.baiyu.widget.MyDatePickerDialog;
import com.ukang.baiyu.widget.WheelProgressDialog;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


/**
 * 添加读片
 * @author SAN
 *
 */
public class AddReadPicActivity extends SwipeBackActivity {
	
	private static SystemBarTintManager tintManager;
	private SwipeBackLayout mSwipeBackLayout;
	private Context mContext;
	
	@ViewInject(R.id.iv_back)
	private ImageButton btnBack;
	@ViewInject(R.id.tv_title)
	private TextView tvTitle;
	@ViewInject(R.id.tv_title_right)
	private TextView tvSave;
	@ViewInject(R.id.ib_add)
	private ImageButton btnAdd;
	
	@ViewInject(R.id.et_title)
	private EditText etTitle;
	@ViewInject(R.id.et_desc)
	private EditText etDesc;
	
	private Uri outputFileUri;
	private final int CONSULT_DOC_PICTURE = 1000; //从图库中选择 
	private final int CONSULT_DOC_CAMERA = 1001;  //拍照
	
	@ViewInject(R.id.first_grid_view)
	private GridView noScrollgridview;
	
	private GridAdapter adapter;
	public static Bitmap bimap ;
	
	private String SEX_TYPE = "1";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_readpic);
		mContext = this;
		getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		ViewUtils.inject(this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.index_title_color);
		mSwipeBackLayout = getSwipeBackLayout();
		mSwipeBackLayout.setScrimColor(Color.TRANSPARENT);
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		Res.init(this);
		initview();
		addClickListener();
		PublicWay.num = PublicWay.MAX_NUM;
	}
	
	@TargetApi(19) 
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onResume(this);       //统计时长
	    adapter.notifyDataSetChanged();
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(this);
	}

	private void initview(){
		tvTitle.setText(R.string.create_readpic);
		tvSave.setVisibility(View.VISIBLE);
		
		noScrollgridview = (GridView) findViewById(R.id.first_grid_view);	
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				System.out.println("itemclick...");
			}
		});
	}
	
	private void addClickListener(){
		tvSave.setOnClickListener(btnClick);
		btnBack.setOnClickListener(btnClick);
		btnAdd.setOnClickListener(btnClick);
	}
	
	private OnClickListener btnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == btnBack){
				finish();
			}else if(v == btnAdd){
				showChoosePicDia();
			}else if(v == tvSave){
				save();
			}
		}
	};
	
	private Calendar c;
	String select_time = "";
	protected Dialog createDialog(final TextView tv) {
		Dialog mDialog = null;
			c = Calendar.getInstance();
			c.setTime(new Date());
			mDialog = new MyDatePickerDialog(mContext,
					new DatePickerDialog.OnDateSetListener() {
						boolean fired = false;
						public void onDateSet(DatePicker dp, int year,
								int month, int dayOfMonth) {
							select_time = year + "-" + formatMonthOrDay(month + 1) + "-"
								+ formatMonthOrDay(dayOfMonth);
							try {
								tv.setText(select_time);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(!fired){
								fired = true;
							}
						}
					}, c.get(Calendar.YEAR), // 传入年份
					c.get(Calendar.MONTH), // 传入月份
					c.get(Calendar.DAY_OF_MONTH) // 传入天数
			);
		return mDialog;
	}
	
	private String formatMonthOrDay(int input){
		String result = "";
		if(input < 10){
			result = "0" + input;
		}else{
			result = String.valueOf(input);
		}
		return result;
	}
	
	private OnClickListener lineClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		}
	};
	
	
	private void save(){
		String title = etTitle.getText().toString();
		if(title.trim().equals("")){
			Toast.makeText(mContext, "标题不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		RequestParams params = new RequestParams();
		params.addHeader("Cookie", Constant.sessionId);
		params.addBodyParameter("token", Constant.token);
		params.addBodyParameter("title", title);
		params.addBodyParameter("presentation", etDesc.getText().toString());
		
		if(Bimp.tempSelectBitmap.size() > 0){
			for(int i = 0; i < Bimp.tempSelectBitmap.size(); i++){
				params.addBodyParameter("attachmendss" + (i+1), new File(Bimp.tempSelectBitmap.get(i).getImagePath()));
			}
			params.addBodyParameter("filenum", Bimp.tempSelectBitmap.size() + "");
		}
		ConfirmThread thread = new ConfirmThread(Constant.ADD_READ_PIC_URL, params);
		thread.start();
	}
	
	@SuppressWarnings("rawtypes")
	private HttpHandler mHttpHelper;
	class ConfirmThread extends Thread{
		private String url;
		private RequestParams params;
		public ConfirmThread(String url, RequestParams params){
			this.url = url;
			this.params = params;
		}
		public void run(){
		mHttpHelper = new HttpUtils().send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
		
		        @Override
		        public void onStart() {
		        	showDialog();
		        }
		
		        @Override
		        public void onLoading(final long total, final long current, boolean isUploading) {
		        	if(isUploading){
		        		Message message = Message.obtain();
			        	message.arg1 = (int) total;
			        	message.arg2 = (int) current;
			        	try{
			        		refreshProgress.sendMessageDelayed(message, 100);
			        	}catch(Exception e){
			        		e.printStackTrace();
			        	}
		        	}
		        }
		
		        @Override
		        public void onSuccess(ResponseInfo<String> responseInfo) {
	        		cancelDialog();
		        	String result = responseInfo.result;
		        	System.out.println(result);
		        	Message message = Message.obtain();
		        	message.what = 0;
		        	message.obj = result;
		        	addMedHandler.sendMessage(message);
		        }
		
		        @Override
		        public void onFailure(HttpException error, String msg) {
		        	cancelDialog();
		        	System.out.println("onFailure --> " + msg);
		        	Message message = Message.obtain();
		        	message.what = -1;
		        	message.obj = msg;
		        	addMedHandler.sendMessage(message);
		        }
			});
		}
	}
	
	private Handler refreshProgress = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			try{
				int total = msg.arg1;
				int current = msg.arg2;
				if(wheelProgressDialog != null){
					int progress = (int) (100 * current / total);
					wheelProgressDialog.progress(progress).message("正在提交  已完成" + progress +"%");
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	};
	
	private WheelProgressDialog wheelProgressDialog;
	private void showDialog(){
		wheelProgressDialog = new WheelProgressDialog(mContext);
		wheelProgressDialog.setOnKeyListener(new OnKeyListener() {
			
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					System.out.println("onkey ： BACK");
					mHttpHelper.cancel();
					cancelDialog();
		            return true;
		        }
				return false;
			}
		});
		wheelProgressDialog.message(getString(R.string.confirming));
		wheelProgressDialog.show();
	}
	
	private void cancelDialog(){
    	if(wheelProgressDialog != null){
    		wheelProgressDialog.dismiss();
    	}
    }
	
	private Handler addMedHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String result = msg.obj.toString();
				Response response = DataParser.parseAddMechart(result);
				if(response.getRet() == 1){
					Toast.makeText(mContext, R.string.confirm_success, Toast.LENGTH_LONG).show();
					finish();
				}else{
					Toast.makeText(mContext, "该账号不存在", Toast.LENGTH_LONG).show();
					Intent inten = new Intent(AddReadPicActivity.this, LoginActivity.class);
					startActivity(inten);
					finish();
				}
				break;
			case -1:
				Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);  
        if(resultCode == 0) return;
        if(resultCode == RESULT_OK){
        	if (requestCode == CONSULT_DOC_PICTURE) {  
        		System.out.println("WOCAO!!!");
        		adapter.update();
        		adapter.notifyDataSetChanged();
	        }else if (requestCode == CONSULT_DOC_CAMERA) { 
	        	if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {
					
	        		Bitmap bm = getPicBitmap();
	        		ImageItem takePhoto = new ImageItem();
	        		takePhoto.setBitmap(bm);
	        		takePhoto.setImagePath(outputFileUri.getPath());
	        		Bimp.tempSelectBitmap.add(takePhoto);
				}
	        }
        }
    } 
	
	private Bitmap getPicBitmap(){
		Bitmap bitmap = null;
		try{
			BitmapFactory.Options options = new BitmapFactory.Options();
	        // 先设置为TRUE不加载到内存中，但可以得到宽和高
	        options.inJustDecodeBounds = true;
	        bitmap = BitmapFactory.decodeFile(outputFileUri.getPath(), options); // 此时返回bm为空
	        options.inJustDecodeBounds = false;
	        // 计算缩放比
	        int outW = options.outWidth > options.outHeight ? options.outWidth : options.outHeight;
	        int be = (int) (outW / (float) (1024));
	        if (be <= 0)
	        	be = 1;
	        options.inSampleSize = be;
	        // 这样就不会内存溢出了
	        bitmap = BitmapFactory.decodeFile(outputFileUri.getPath(), options);
	        BufferedOutputStream bos = null;  
	        bos = new BufferedOutputStream(new FileOutputStream(outputFileUri.getPath()));
	        bitmap.compress(CompressFormat.JPEG, 50, bos);  
	        bos.flush();
	        bos.close();
		}catch(Exception e){
    		e.printStackTrace();
    		Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
    	}
		return bitmap;
	}
	
	public void goCamera(){
		String path = Environment.getExternalStorageDirectory() + File.separator + "YuYi" + File.separator + "temp";
		File f = new File(path);
		if(!f.exists()){
			f.mkdirs();
		}
		File file = new File(path, System.currentTimeMillis() + ".jpg");  
        outputFileUri = Uri.fromFile(file);  

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);  
        startActivityForResult(intent, CONSULT_DOC_CAMERA);  
	}
	
	public void chooseImage(){
		Intent intent = new Intent(AddReadPicActivity.this,
				AlbumActivity.class);
//		startActivity(intent);
		startActivityForResult(intent, CONSULT_DOC_PICTURE);  
	}
	
	void showChoosePicDia(){
		CharSequence[] items = { "相册", "相机"
				};  
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
        .setTitle("选择")
		.setItems(items, 
				new DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialog, int which) {  
            	switch (which) {
				case 0:
					chooseImage();
					break;
				case 1:
					goCamera();
					break;
				default:
					chooseImage();
					break;
				}
            }  
        }
		)
        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	dialog.dismiss();
            }
        })
        ;
		builder.show();
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	Bimp.tempSelectBitmap.clear();
			Bimp.max = 0;
        }
        return super.onKeyDown(keyCode, event);
    }
	
	private Handler delHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			adapter.notifyDataSetChanged();
		}
	};
	
	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			if(Bimp.tempSelectBitmap.size() == 9){
				return 9;
			}
//			return (Bimp.tempSelectBitmap.size() + 1);
			return (Bimp.tempSelectBitmap.size());
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				holder.delete = (ImageButton) convertView.findViewById(R.id.iv_del);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position ==Bimp.tempSelectBitmap.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
			}
			holder.delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Bimp.tempSelectBitmap.remove(position);
					Bimp.max--;
					delHandler.sendEmptyMessage(0);
				}
			});
			holder.image.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(AddReadPicActivity.this,
							GalleryActivity.class);
					intent.putExtra("position", String.valueOf(position));
					intent.putExtra("ID", position);
					startActivity(intent);
				}
			});
			
			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
			public ImageButton delete;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						System.out.println("loading...");
						if (Bimp.max == Bimp.tempSelectBitmap.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				}
			}).start();
		}
	}
}