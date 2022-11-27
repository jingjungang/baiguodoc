package com.ukang.baiyu.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.annotation.SuppressLint;
import android.app.DownloadManager.Query;
import android.util.Log;

/**
 * 采用普通的HttpsConnection来实现https请求
 * @author SAN
 *
 */
public class HttpsUtils2 {
	class MytmArray implements X509TrustManager {
		public X509Certificate[] getAcceptedIssuers() {
			// return null;
			return new X509Certificate[] {};
		}

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			// TODO Auto-generated method stub

		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			// TODO Auto-generated method stub
			// System.out.println("cert: " + chain[0].toString() +
			// ", authType: "
			// + authType);
		}
	};

	static TrustManager[] xtmArray = new TrustManager[] { new X509TrustManager() {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(
				java.security.cert.X509Certificate[] chain, String authType)
				throws java.security.cert.CertificateException {
		}

		public void checkServerTrusted(
				java.security.cert.X509Certificate[] chain, String authType)
				throws java.security.cert.CertificateException {
		}
	} };

	/**
	 * 信任所有主机-对于任何证书都不做检查
	 */
	private static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		// Android 采用X509的证书信息机制
		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, xtmArray, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
			// HttpsURLConnection.setDefaultHostnameVerifier(DO_NOT_VERIFY);//
			// 不进行主机名确认
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			// TODO Auto-generated method stub
			// System.out.println("Warning: URL Host: " + hostname + " vs. "
			// + session.getPeerHost());
			return true;
		}
	};

	public static String doPost(String httpUrl) {
		String result = "";
		HttpURLConnection http = null;
		StringBuffer sb = new StringBuffer();
		URL url;
		try {
			url = new URL(httpUrl);
			// 判断是http请求还是https请求
			if (url.getProtocol().toLowerCase().equals("https")) {
				trustAllHosts();
				http = (HttpsURLConnection) url.openConnection();
				((HttpsURLConnection) http).setHostnameVerifier(DO_NOT_VERIFY);// 不进行主机名确认

			} else {
				http = (HttpURLConnection) url.openConnection();
			}
			http.setConnectTimeout(10000);// 设置超时时间
			http.setReadTimeout(50000);
			http.setRequestMethod("POST");// 设置请求类型为
			http.setDoInput(true);
			http.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			http.connect();
			OutputStream os = http.getOutputStream();
			StringBuffer params = new StringBuffer();
			params.append("username").append("=").append("15202188295")
			.append("&").append("password").append("=").append("san12345");
			os.write(params.toString().getBytes());
			os.flush();
			os.close();
			int code = http.getResponseCode();// http或https返回状态200还是403
			BufferedReader in = null;
			Log.i("LoginServiceImpl", "code" + code);
			if (code == 200) {
				getCookie(http);
				in = new BufferedReader(new InputStreamReader(
						http.getInputStream()));
			} else
				in = new BufferedReader(new InputStreamReader(
						http.getErrorStream()));
			while((result = in.readLine()) != null){
				sb.append(result);
				Log.i("LoginServiceImpl", result);
			}
			in.close();
			http.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("LoginServiceImpl", "sb: " + sb.toString());
		return sb.toString();
	}

	/** 得到cookie */
	private static void getCookie(HttpURLConnection http) {
		String cookieVal = null;
		String key = null;
		// DataDefine.mCookieStore = "";
//		 for (int i = 1; (key = http.getHeaderFieldKey(i)) != null; i++) {
//		 if (key.equalsIgnoreCase("set-cookie")) {
		// cookieVal = http.getHeaderField(i);
		// cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
		// DataDefine.mCookieStore = DataDefine.mCookieStore + cookieVal
		// + ";";
		// }
		// }
	}

	@SuppressLint("NewApi")
	public static Query HttpQueryReturnClass(String httpUrl, String base64) {
		String result = "";
		Log.i("控制", httpUrl);
		Query obj = new Query();
		HttpURLConnection http = null;
		URL url;
		try {
			url = new URL(httpUrl);
			// 判断是http请求还是https请求
			if (url.getProtocol().toLowerCase().equals("https")) {
				trustAllHosts();
				http = (HttpsURLConnection) url.openConnection();
				((HttpsURLConnection) http).setHostnameVerifier(DO_NOT_VERIFY);// 不进行主机名确认
			} else {
				http = (HttpURLConnection) url.openConnection();
			}
			http.setConnectTimeout(10000);// 设置超时时间
			http.setReadTimeout(50000);
			http.setRequestMethod("POST");// 设置请求类型为post
			http.setDoInput(true);
			http.setDoOutput(true);
			http.setRequestProperty("Content-Type", "text/xml");
			// http.setRequestProperty("Cookie", DataDefine.mCookieStore);
			DataOutputStream out = new DataOutputStream(http.getOutputStream());
			out.writeBytes(base64);
			out.flush();
			out.close();
			// obj.setHttpStatus(http.getResponseCode());// 设置http返回状态200还是403
			BufferedReader in = null;
			// if (obj.getHttpStatus() == 200) {
			// getCookie(http);
			// in = new BufferedReader(new InputStreamReader(
			// http.getInputStream()));
			// } else
			// in = new BufferedReader(new InputStreamReader(
			// http.getErrorStream()));
			result = in.readLine();// 得到返回结果
			in.close();
			http.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
}
