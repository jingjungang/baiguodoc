package com.ukang.baiyu.utils;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class URLConnectionUtil {

	static String result = null;
    /**
     * 请求后台，判断登录结果。
     * @param username
     * @param password
     */
    public static String HttpClientPost(String URL,List<NameValuePair> parameters) {
    	//1.创建 HttpClient的实例  
        HttpClient httpClient = new DefaultHttpClient();
        try {
	        //2、创建某种连接方法的实例，在这里是HttpPost。
	        HttpPost httpPost = new HttpPost(URL);  
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "UTF-8");//创建传递参数封装 实体对象，设置传递参数的编码  
            httpPost.setEntity(entity);//把实体对象存入到httpPost对象中 
            //4、调用HttpClient实例的execute方法来执行创建好的method实例
            HttpResponse httpResp = httpClient.execute(httpPost);
            
            //5、判断是否能够请求成功
            if(httpResp.getStatusLine().getStatusCode()==200){
            	//6、获取返回的数据
            	result = EntityUtils.toString(httpResp.getEntity(),"UTF-8");
            	return result;
            } else {
            	Log.i("HttpClientPost", "HttpPost方式请求失败");
            }
        }catch (Exception e) {  
            e.printStackTrace(); 
        }finally {
            // 7、释放连接。无论执行方法是否成功，都必须释放连接  
            httpClient.getConnectionManager().shutdown();//释放链接  
        }
		return result;
    	
    }
    /**
     * 请求后台，判断登录结果。
     * @param username
     * @param password
     * @return 
     */
    public static String HttpClientGet(String strUrl) {
    	//1、创建 HttpClient的实例 
        HttpClient httpClient = new DefaultHttpClient();
        try { 
            //2、创建某种连接方法的实例，在这里是HttpGet。 
            HttpGet httpGet = new HttpGet(strUrl);
            
            //3、获取HttpResponse实例
            HttpResponse httpResp = httpClient.execute(httpGet);
            //4、判断是否能够请求成功
            if(httpResp.getStatusLine().getStatusCode()==200){          
            	//5、获取返回的数据
            	result = EntityUtils.toString(httpResp.getEntity(),"UTF-8");
            	return result; 
            } else {
            	Log.i("HttpClientGet", "HttpGet方式请求失败");
            }
        } catch (Exception e) {  
            e.printStackTrace(); 
        }finally {
            // 6、释放连接。无论执行方法是否成功，都必须释放连接  
        	httpClient.getConnectionManager().shutdown();//释放链接  
        }
		return result;       
    }
}
