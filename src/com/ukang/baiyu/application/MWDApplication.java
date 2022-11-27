package com.ukang.baiyu.application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.RequestParams;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.common.DataParser;
import com.ukang.baiyu.common.SecurityEncode;
import com.ukang.baiyu.entity.PushNotification;
import com.ukang.baiyu.entity.Response;
import com.ukang.baiyu.entity.Users;
import com.ukang.baiyu.table.ConferenceCategory;
import com.ukang.baiyu.table.IndexCategory;
import com.ukang.baiyu.table.SearchHistory;
import com.ukang.baiyu.thread.XThread;
import com.ukang.baiyu.widget.MyToast;
import com.umeng.socialize.PlatformConfig;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import dalvik.system.DexClassLoader;

public class MWDApplication extends Application {
	public static final String LLKCKIO = "KCKLKKCKCMM,C101203132KDMMO1233.DIDSIKLDZZZ'DKSKDLKL123982983,dksdljfkj.2kjkdk";
	private final String TAG = "MWDApplication";
	private String userName, userPwd;
	private boolean firstLoad;
	Boolean pushFlag;
	public PagerObservered PageNotificationer;
	public String DEPT_NAME = "";
	// 云旺OpenIM的DEMO用到的Application上下文实例
	private static Context sContext;
	public static Context getContext() {
		return sContext;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public Boolean getPushState() {
		return pushFlag;
	}

	public void setPushState(Boolean pushFlag) {
		this.pushFlag = pushFlag;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		// todo
		// Application.onCreate中，首先执行这部分代码，以下代码固定在此处，不要改动，这里return是为了退出Application.onCreate！！！
		// if(mustRunFirstInsideApplicationOnCreate()){
		// //todo 如果在":TCMSSevice"进程中，无需进行openIM和app业务的初始化，以节省内存
		// return;
		// }

		// 初始化云旺SDK
		// InitHelper.initYWSDK(this);
		// OpenIMAgent im = OpenIMAgent.getInstance(this);
		// im.init();
		super.onCreate();
		Log.d(TAG, "MWDApplication");
		Log.d(TAG, "---------onCreate...");
		ReadUser();// 读取
		ReadPushState();
		ReadSetting();
		// 初始化数据库
		initTables();
		initImageLoader(getApplicationContext());
		ThirdThread mThread = new ThirdThread();
		mThread.start();
		Log.d(TAG, "---------onCreate end...");
		new DataBaseThread().start();
		new InitThread().start();
		// dexTool();
	}

	/**
	 * Copy the following code and call dexTool() after super.onCreate() in
	 * Application.onCreate()
	 * <p>
	 * This method hacks the default PathClassLoader and load the secondary dex
	 * file as it's parent.
	 */
	@SuppressLint("NewApi")
	private void dexTool() {

		File dexDir = new File(getFilesDir(), "dlibs");
		dexDir.mkdir();
		File dexFile = new File(dexDir, "libs.apk");
		File dexOpt = new File(dexDir, "opt");
		dexOpt.mkdir();
		try {
			InputStream ins = getAssets().open("libs.apk");
			if (dexFile.length() != ins.available()) {
				FileOutputStream fos = new FileOutputStream(dexFile);
				byte[] buf = new byte[4096];
				int l;
				while ((l = ins.read(buf)) != -1) {
					fos.write(buf, 0, l);
				}
				fos.close();
			}
			ins.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		ClassLoader cl = getClassLoader();
		ApplicationInfo ai = getApplicationInfo();
		String nativeLibraryDir = null;
		if (Build.VERSION.SDK_INT > 8) {
			nativeLibraryDir = ai.nativeLibraryDir;
		} else {
			nativeLibraryDir = "/data/data/" + ai.packageName + "/lib/";
		}
		DexClassLoader dcl = new DexClassLoader(dexFile.getAbsolutePath(),
				dexOpt.getAbsolutePath(), nativeLibraryDir, cl.getParent());

		try {
			Field f = ClassLoader.class.getDeclaredField("parent");
			f.setAccessible(true);
			f.set(cl, dcl);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// .writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);

	}

	// private boolean mustRunFirstInsideApplicationOnCreate() {
	// 必须的初始化
	// SysUtil.setApplication(this);
	// sContext = getApplicationContext();
	// return SysUtil.isTCMSServiceProcess(sContext);
	// }

	void ReadUser() {
		SharedPreferences user = getSharedPreferences("user_info", MODE_PRIVATE);
		userName = user.getString("username", null);
		userPwd = user.getString("password", null);
		if (Constant.ISDEBUG) {
			// Log.d("------", userName + " " + userPwd);
		}
		if (userName != null) {
			userName = SecurityEncode.decoderByDES(userName, LLKCKIO);
		}
		if (userPwd != null && !userPwd.equals(""))
			userPwd = SecurityEncode.decoderByDES(userPwd, LLKCKIO);
		if (userName != null) {
			Users users = new Users();
			users.setUsername(userName);
			users.setPassword(userPwd);
			Constant.users = users;
		}
	}

	public void WriteUser(String strName, String strPassword, boolean isSavePwd) {
		SharedPreferences user = getSharedPreferences("user_info", MODE_PRIVATE);
		SharedPreferences.Editor sharedata = user.edit();
		sharedata.putString("username",
				SecurityEncode.encoderByDES(strName, LLKCKIO));
		if (isSavePwd)
			sharedata.putString("password",
					SecurityEncode.encoderByDES(strPassword, LLKCKIO));
		else
			sharedata.putString("password", "");
		sharedata.commit();
	}

	void ReadPushState() {
		SharedPreferences push = getSharedPreferences("push_state",
				MODE_PRIVATE);
		pushFlag = push.getBoolean("push_flag", true);
		if (Constant.ISDEBUG)
			Log.d(TAG, "ReadPushState. pushFlag: " + pushFlag);
		Constant.pushFlag = pushFlag;
	}

	public void WritePush(boolean flag) {
		if (Constant.ISDEBUG)
			Log.d(TAG, "WritePush. pushFlag: " + flag);
		SharedPreferences user = getSharedPreferences("push_state",
				MODE_PRIVATE);
		SharedPreferences.Editor sharedata = user.edit();
		sharedata.putBoolean("push_flag", flag);
		sharedata.commit();
	}

	public void ReadSetting() {
		SharedPreferences pref = getSharedPreferences("setting_info",
				MODE_PRIVATE);
		firstLoad = pref.getBoolean("first_load", true);
		boolean newVersion = pref.getBoolean("new_version", true);
		Constant.firstLoad = firstLoad;
		String versionCode = pref.getString("version_code", "");
		if (Constant.VERSION_CODE.compareTo(versionCode) > 0) {
			newVersion = true;
		}
		Constant.isNewVersion = newVersion;
	}

	public void WriteSetting(boolean flag, boolean isNewVersion) {
		SharedPreferences pref = getSharedPreferences("setting_info",
				MODE_PRIVATE);
		SharedPreferences.Editor sharedata = pref.edit();
		sharedata.putBoolean("first_load", flag);
		sharedata.putBoolean("new_version", isNewVersion);
		sharedata.putString("version_code", Constant.VERSION_CODE);
		sharedata.commit();
	}

	private void initTables() {
		try {
			// 资讯栏目表
			DbUtils.create(getApplicationContext()).createTableIfNotExist(
					IndexCategory.class);
			// 会议栏目表
			DbUtils.create(getApplicationContext()).createTableIfNotExist(
					ConferenceCategory.class);
			DbUtils.create(getApplicationContext()).createTableIfNotExist(
					PushNotification.class);
			DbUtils.create(getApplicationContext()).createTableIfNotExist(
					SearchHistory.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class ThirdThread extends Thread {
		public void run() {
			PlatformConfig.setWeixin("wxfadfe3f5158e5458",
					"f65aad6405339ea261d81252ce371dbe");
			// 微信 appid appsecret
			PlatformConfig.setSinaWeibo("3921700954",
					"04b48b094faeb16683c32669824ebdad");
			// 新浪微博 appkey appsecret
			PlatformConfig.setQQZone("100424468",
					"c7394704798a158208a74ab60104f0ba");
			// QQ和Qzone appid appkey

		}
	}

	class DataBaseThread extends Thread {
		public void run() {
			DbUtils dbUtils = DbUtils.create(getApplicationContext());
			try {
				List<IndexCategory> categoryList = dbUtils
						.findAll(Selector.from(IndexCategory.class).orderBy(
								"update_date", true));
				List<String> tempList = new ArrayList<String>();
				for (int i = 0; i < categoryList.size(); i++) {
					if (!tempList.contains(categoryList.get(i).getNameEn())) {
						tempList.add(categoryList.get(i).getNameEn());
					} else {
						dbUtils.delete(categoryList.get(i));
					}
				}
				categoryList.clear();
				tempList.clear();
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				List<ConferenceCategory> conferenceList = dbUtils
						.findAll(Selector.from(ConferenceCategory.class)
								.orderBy("update_date", true));
				List<String> tempList = new ArrayList<String>();
				for (int i = 0; i < conferenceList.size(); i++) {
					if (!tempList.contains(conferenceList.get(i).getNameEn())) {
						tempList.add(conferenceList.get(i).getNameEn());
					} else {
						dbUtils.delete(conferenceList.get(i));
					}
				}
				conferenceList.clear();
				tempList.clear();
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class InitThread extends Thread {
		@Override
		public void run() {
			loadStore();
		}

		private void loadStore() {
			Looper.prepare();
			Handler storeHandler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					switch (msg.what) {
					case 0:
						try {
							String result = msg.obj.toString();
							Response response = DataParser
									.parseStoreList(result);
							if (response.getRet() == 1) {
								Constant.storeListResp = response;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					case -1:
						break;
					default:
						break;
					}
				}
			};
			Looper.loop();
			RequestParams p = new RequestParams();
			p.addHeader("Cookie", Constant.sessionId);
			p.addHeader("User-Agent", Constant.USER_AGENT);
			p.addBodyParameter("token", Constant.token);
			p.addBodyParameter("page", "1");
			XThread thread = new XThread(0, p, Constant.STORE_LIST_URL,
					storeHandler);
			thread.start();
		}
	}

}
