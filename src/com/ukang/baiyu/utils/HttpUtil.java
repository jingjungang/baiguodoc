package com.ukang.baiyu.utils;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.ukang.baiyu.common.Constant;

import android.util.Log;


/**
 * http网络操作工具类
 * 
 * @author SAN
 * @version 1.0
 */
public class HttpUtil {

	/**
	 * 使用HttpClient完成Get请求，并返回相应结果
	 * 
	 * @param url
	 * @param resultEncoder
	 * @return
	 */
	public static String doGet(HttpClient hc, String url) {

		// HttpGet连接对象
		HttpGet httpRequest = new HttpGet(url);

		if(Constant.sessionId != null && !Constant.sessionId.equals("")){
			httpRequest.setHeader("Cookie", Constant.sessionId);
		}
		httpRequest.setHeader("User-Agent", Constant.USER_AGENT);
		BasicHttpParams httpParams = new BasicHttpParams();  
	    HttpConnectionParams.setConnectionTimeout(httpParams, 10*1000);  
	    HttpConnectionParams.setSoTimeout(httpParams, 10*1000);  
	    
		// 获取默认的HttpClient对象
		hc = new DefaultHttpClient(httpParams);

		try {
			// 请求HttpClient，取得HttpResponse
			HttpResponse httpResponse = hc.execute(httpRequest);
			Header[] hh = httpResponse.getHeaders("Set-Cookie");
            for(int i = 0; i < hh.length; i++){
            	Log.d("Set-Cookie", hh[i].getValue());
            	if(Constant.sessionId == null || Constant.sessionId.equals("")){
            		Constant.sessionId = hh[0].getValue();
            	}
            }
			// 若请求成功
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(httpResponse.getEntity(),
						HTTP.UTF_8);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 使用HttpClient完成Post请求，并返回相应结果
	 * 
	 * @param url
	 * @param charSet
	 * @param params
	 * @param resultEncoder
	 * @return
	 */
	public static String doPost(HttpClient hc, String url, 
			List<NameValuePair> params) {
		// HttpPost连接对象
		HttpPost httpRequest = new HttpPost(url
				);

		if(Constant.sessionId != null && !Constant.sessionId.equals("")){
			httpRequest.setHeader("Cookie", Constant.sessionId);
		}
		httpRequest.setHeader("User-Agent", Constant.USER_AGENT);
		try {
			// 设置字符集
			HttpEntity httpEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);

			// 请求httpRequest
			httpRequest.setEntity(httpEntity);
//			httpRequest.setHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");    

			BasicHttpParams httpParams = new BasicHttpParams();  
		    HttpConnectionParams.setConnectionTimeout(httpParams, 10*1000);  
		    HttpConnectionParams.setSoTimeout(httpParams, 10*1000);  
		    
			// 获取默认的HttpClient对象
			hc = new DefaultHttpClient(httpParams);
			
			// 请求HttpClient，取得HttpResponse
			HttpResponse httpResponse = hc.execute(httpRequest);
			Header[] hh = httpResponse.getHeaders("Set-Cookie");
            for(int i = 0; i < hh.length; i++){
            	Log.d("Set-Cookie", hh[i].getValue());
            	if(Constant.sessionId == null || Constant.sessionId.equals("")){
            		if(hh[i].getValue().contains("PHPSESSID=")){
            			Constant.sessionId = hh[i].getValue();
            		}
            	}
            }
            if(Constant.ISDEBUG){
            	Log.i(TAG, "code: " + httpResponse.getStatusLine().getStatusCode());
            }
			// 若请求成功
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(httpResponse.getEntity(),
						HTTP.UTF_8);
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private static final String TAG = "HttpUtil-LOG";

	/**
	 * 使用HttpClient完成Post请求，并返回相应结果
	 * 
	 * @param url
	 * @param charSet
	 * @param params
	 * @param resultEncoder
	 * @return String[] 由2个值组成 sessionId + json
	 */
	public static String[] doPost2(String url, String charSet,
			List<NameValuePair> params, String resultEncoder) {

		String[] result = { "", "" };

		Log.d("url", url);
		// HttpPost连接对象
		HttpPost httpRequest = new HttpPost(url
//				.replace("aiyoutech.oicp.net", "192.168.2.104")
				);
		

		try {
			// 设置字符集
			HttpEntity httpEntity = new UrlEncodedFormEntity(params, charSet);

			// 请求httpRequest
			httpRequest.setEntity(httpEntity);
			
			BasicHttpParams httpParams = new BasicHttpParams();  
		    HttpConnectionParams.setConnectionTimeout(httpParams, 30*1000);  
		    HttpConnectionParams.setSoTimeout(httpParams, 30*1000);  
			// 获取默认的HttpClient对象
			HttpClient httpClient = new DefaultHttpClient(httpParams);

			// 请求HttpClient，取得HttpResponse
			HttpResponse httpResponse = httpClient.execute(httpRequest);

			Header[] hh = httpResponse.getHeaders("Set-Cookie");
            for(int i = 0; i < hh.length; i++){
            	Log.d("Set-Cookie", hh[i].getValue());
            }
//			String[] sessionId = hh[0].getValue().split(";");
//			result[0] = sessionId[0].substring(11);

			// 若请求成功
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result[0] = EntityUtils.toString(httpResponse.getEntity(),
						resultEncoder);
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "result: " + result.toString());
		Log.d(TAG, "result: " + " \n" + result[0]);
		return result;
	}
	
	/**
	 * 使用HttpClient完成Post请求，并返回相应结果
	 * 
	 * @param url
	 * @param charSet
	 * @param params
	 * @param resultEncoder
	 * @return String[] 由2个值组成 sessionId + json
	 */
	public static String[] doHttpsPost(String url, String charSet,
			List<NameValuePair> params, String resultEncoder) {

		String[] result = { "", "" };

		Log.d("url", url);
		// HttpPost连接对象
		HttpPost httpRequest = new HttpPost(url
//				.replace("aiyoutech.oicp.net", "192.168.2.104")
				);
		

		try {
			// 设置字符集
			HttpEntity httpEntity = new UrlEncodedFormEntity(params, charSet);

			// 请求httpRequest
			httpRequest.setEntity(httpEntity);
			
			BasicHttpParams httpParams = new BasicHttpParams();  
		    HttpConnectionParams.setConnectionTimeout(httpParams, 10*1000);  
		    HttpConnectionParams.setSoTimeout(httpParams, 10*1000);  
			// 获取默认的HttpClient对象
			HttpClient httpClient = new DefaultHttpClient(httpParams);

			// 请求HttpClient，取得HttpResponse
			HttpResponse httpResponse = httpClient.execute(httpRequest);

			// 若请求成功
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result[0] = EntityUtils.toString(httpResponse.getEntity(),
						resultEncoder);
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "result: " + result.toString());
		Log.d(TAG, "result: " + " \n" + result[0]);
		return result;
	}
	
	public static boolean doUploadFile(String httpUrl,File file)
	{
		
		HttpPost request = new HttpPost(httpUrl
//				.replace("aiyoutech.oicp.net", "180.170.156.137")
				);   
		request.setHeader("Cookie", "JSESSIONID=" + Constant.sessionId
				+ ";");
        HttpClient httpClient = new DefaultHttpClient(); 
        FileEntity entity = new FileEntity(file,"binary/octet-stream"); 
        HttpResponse response; 
        request.setEntity(entity); 
        entity.setContentEncoding("binary/octet-stream"); 
        try {
			response = httpClient.execute(request);
			return true;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
        return false;
	}
	
	public static boolean doUploadImg(String httpUrl,byte[] buf)
	{
		
		HttpPost request = new HttpPost(httpUrl
//				.replace("aiyoutech.oicp.net", "180.170.156.137")
				);   
		request.setHeader("Cookie", "JSESSIONID=" + Constant.sessionId
				+ ";");
        HttpClient httpClient = new DefaultHttpClient(); 
        ByteArrayInputStream instream = new ByteArrayInputStream(buf);
        InputStreamEntity entity = new InputStreamEntity(instream, buf.length);
//        FileEntity entity = new FileEntity(file,"binary/octet-stream"); 
        HttpResponse response; 
        request.setEntity(entity); 
        entity.setContentEncoding("binary/octet-stream"); 
        try {
			response = httpClient.execute(request);
			
			return true;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
        return false;
	}
	
	public static boolean doConfirmComment(String httpUrl, String resultEncoder)
	{
		
		HttpPost httpRequest = new HttpPost(httpUrl
//				.replace("aiyoutech.oicp.net", "180.170.156.137")
				);   
		httpRequest.setHeader("Cookie", "JSESSIONID=" + Constant.sessionId
				+ ";");
		HttpClient httpClient = new DefaultHttpClient();

		try {
			
			// 请求HttpClient，取得HttpResponse
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			// 若请求成功
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				httpResponse = httpClient.execute(httpRequest);
				return true;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return false;
	}
//	public static boolean uploadFile(String hosturl,String picname,String uploadFile)
//     {
//       String end = "\r\n";
//       String twoHyphens = "--";
//       String boundary = "*****";
//       try
//       {
//         URL url =new URL(hosturl);
//         HttpURLConnection con=(HttpURLConnection)url.openConnection();
//        
//         con.setDoInput(true);
//         con.setDoOutput(true);
//         con.setUseCaches(false);
//        
//         con.setRequestMethod("POST");
//        
//         con.setRequestProperty("Connection", "Keep-Alive");
//         con.setRequestProperty("Charset", "UTF-8");
//         con.setRequestProperty("Content-Type",
//                            "multipart/form-data;boundary="+boundary);
//        
//         DataOutputStream ds =
//           new DataOutputStream(con.getOutputStream());
//         ds.writeBytes(twoHyphens + boundary + end);
//         ds.writeBytes("Content-Disposition: form-data; " +
//                       "name=\"file1\";filename=\"" +
//                       picname +"\"" + end);
//         ds.writeBytes(end);   
//
//        
//         FileInputStream fStream = new FileInputStream(uploadFile);
//        
//         int bufferSize = 1024;
//         byte[] buffer = new byte[bufferSize];
//
//         int length = -1;
//        
//         while((length = fStream.read(buffer)) != -1)
//         {
//           
//           ds.write(buffer, 0, length);
//         }
//         ds.writeBytes(end);
//         ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
//
//        
//         fStream.close();
//         ds.flush();
//
//        
//         InputStream is = con.getInputStream();
//         int ch;
//         StringBuffer b =new StringBuffer();
//         while( ( ch = is.read() ) != -1 )
//         {
//           b.append( (char)ch );
//         }
//        
//         //showDialog("上传成功"+b.toString().trim());
//        
//         ds.close();
//       }
//       catch(Exception e)
//       {
//         
//       }
//     }


}
