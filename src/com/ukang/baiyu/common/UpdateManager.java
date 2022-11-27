package com.ukang.baiyu.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.ukang.baiyu.activity.R;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UpdateManager {

	private Context mContext;
	// 提示语123
	private String updateMsg = "检测到新版本，是否下载更新";
	private String content;
	private int size;
	// 返回的安装包url
	private String apkUrl = "";
	private Dialog noticeDialog;
	private Dialog downloadDialog;
	/* 下载包安装路径 */
	private static final String savePath = "/sdcard/BaiGuoDoctor/";
	private static final String saveFileName = savePath + "BaiGuo.apk";
	/* 进度条与通知ui刷新的handler和msg常量 */
	private ProgressBar mProgress;
	private TextView mpercent;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	private int progress;
	private Thread downLoadThread;
	private boolean interceptFlag = false;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				mpercent.setText("已下载：" + progress + "%");
				break;
			case DOWN_OVER:
				downloadDialog.dismiss();
				installApk();
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(Context _context, String _apkUrl, String content,
			int size) {

		this.mContext = _context;
		this.apkUrl = _apkUrl;
		this.content = content;
		this.size = size;
	}

	// 外部接口让主Activity调用

	public void checkUpdateInfo() {
		showNoticeDialog();
	}

	private void showNoticeDialog() {
		String msg = htmlToStr(Html.fromHtml(content) + "");
		msg = msg.replace("*", "\n");
		noticeDialog = new Builder(mContext).setTitle("软件版本更新").setMessage(msg)
				.setPositiveButton("下载", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						showDownloadDialog();
					}
				}).setNegativeButton("以后再说", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).setCancelable(false).show();
	}

	public static String htmlToStr(String htmlStr) {
		String result = "";
		boolean flag = true;
		if (htmlStr == null) {
			return null;
		}
		htmlStr = htmlStr.replace("\"", ""); // 去掉引号

		char[] a = htmlStr.toCharArray();
		int length = a.length;
		for (int i = 0; i < length; i++) {
			if (a[i] == '<') {
				flag = false;
				continue;
			}
			if (a[i] == '>') {
				flag = true;
				continue;
			}
			if (flag == true) {
				result += a[i];
			}
		}
		return result.toString();
	}

	private void cancelDownload() {
		if (downloadDialog != null)
			downloadDialog.dismiss();
		interceptFlag = true;
	}

	private void showDownloadDialog() {
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.progress);
		mpercent = (TextView) v.findViewById(R.id.text_percent);
		downloadDialog = new Builder(mContext).setTitle("软件版本更新").setView(v)
				.setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
						interceptFlag = true;
						File file = new File(savePath);
						if (file.exists()) {
							file.delete();
						}
					}
				}).setOnKeyListener(new OnKeyListener() {

					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						// TODO Auto-generated method stub
						if (keyCode == KeyEvent.KEYCODE_BACK) {
							System.out.println("onkey ： BACK");
							cancelDownload();
							return true;
						}
						return false;
					}
				}).setCancelable(false).show();
		downloadApk();
	}

	private Runnable mdownApkRunnable = new Runnable() {

		@Override
		public void run() {
			try {
				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				// int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdir();
				} else {
					file.delete();
					// System.out.println("删除原来下载的apk文件");
					file.mkdir();
					// System.out.println("新建apk文件");
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);
				int count = 0;
				byte buf[] = new byte[1024];
				do {
					int numread = is.read(buf);
					if (numread == -1) {
						// 下载完成通知安装
						// System.out.println(numread + "  下载完成");
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					count += numread;
					progress = (int) (((float) count / (size)) * 100);
					// 更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载.
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * 
	 * 下载apk
	 * 
	 * @param url
	 */

	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * 
	 * 安装apk
	 * 
	 * @param url
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}

}
