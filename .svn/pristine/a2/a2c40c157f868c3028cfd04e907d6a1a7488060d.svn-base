package com.ukang.baiyu.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class MWDUtils {
	/**
	 * 验证邮箱格式是否正确
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isVaildEmail(String email) {
		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public static String getMD5String(byte[] source) {
		String s = null;
		char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
			// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
				// >>> 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 通过经纬度得到城市名称
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public static String GetJsonAddr(String latitude, String longitude) {
//		{"Placemark":[
//		          	{"id":"p1","ExtendedData":{"LatLonBox":{"south":31.244864,"west":121.476771,"east":121.479469,"north":31.247562}},
//		          	"address":"中国上海市闸北区福建北路330号 邮政编码: 200071","Point":{"coordinates":[121.47812,31.246213,0]},
//		          	"AddressDetails":
//		          		{"Country":{
//		          			"CountryNameCode":"CN","CountryName":"中国",
//		          			"AdministrativeArea":
//		          				{"Locality":{
//		          					"LocalityName":"上海市",
//		          					"DependentLocality":
//		          						{"DependentLocalityName":"闸北区","Thoroughfare":{"ThoroughfareName":"福建北路330号"}}},
//		          			"AdministrativeAreaName":"上海市"}},"Accuracy":8}
//		          	}],
//		          "Status":{"request":"geocode","code":200},
//		          "name":"31.246322631835938,121.47833251953125"}
		// 也可以是http://maps.google.cn/maps/geo?output=csv&key=abcdef&q=%s,%s，不过解析出来的是英文地址
		// 密钥可以随便写一个key=abc
		// output=csv,也可以是xml或json，采用默认的方式是output=json
		String url = String.format("http://ditu.google.cn/maps/geo?key=abcdef&q=%s,%s",latitude, longitude);
		HttpGet httpGet = new HttpGet(url);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			response = client.execute(httpGet);
			/*
			 * HttpEntity entity = response.getEntity();
			 * 
			 * InputStream stream = entity.getContent();
			 * 
			 * int b;
			 * 
			 * while ((b = stream.read()) != -1) {
			 * 
			 * stringBuilder.append((char) b);
			 * 
			 * }
			 */
			HttpEntity entity = response.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(entity
			.getContent()));
			String result = br.readLine();
			while (result != null) {
				stringBuilder.append(result);
				result = br.readLine();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject jsonObject = new JSONObject();
		String city = "";
		try {
			jsonObject = new JSONObject(stringBuilder.toString());
			if(jsonObject != null){
				JSONArray jplacearr = jsonObject.getJSONArray("Placemark");
				JSONObject jplaceobj = jplacearr.getJSONObject(0);
				city = jplaceobj.getJSONObject("AddressDetails").getJSONObject("Country")
				    .getJSONObject("AdministrativeArea").getJSONObject("Locality")
				    .getString("LocalityName");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return city;
	}
	
	/**
	 * 判断是否安装了SD卡
	 * @return
	 */
	public static boolean isSdPresent()
    {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }
	
	/**
	 * 字符串类型的数值转换成数值型
	 * @param str
	 * @return
	 */
	public static long parseLongVal(String str){
		long l = 0;
		try{
			l = Long.parseLong(str);
		}catch(Exception e){
			e.printStackTrace();
		}
		return l;
	}
	
	/**
	 * 计算百分比，保留两位小数,格式：15.23%
	 * @param x
	 * @param total
	 * @return
	 */
	public static String getPercent(float x,float total){  
	   String result="";//接受百分比的值  
	   double tempresult=x/total;  
	   //NumberFormat nf   =   NumberFormat.getPercentInstance();     注释掉的也是一种方法  
	   //nf.setMinimumFractionDigits( 2 );        保留到小数点后几位  
	   DecimalFormat df1 = new DecimalFormat("0.00%");    //##.00%   百分比格式，后面不足2位的用0补齐  
	   //result=nf.format(tempresult);     
	   result= df1.format(tempresult);    
	   return result;  
	}  
	
	/**
	 * 判断当前网络是否正常
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) { 
		if (context != null) { 
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context 
					.getSystemService(Context.CONNECTIVITY_SERVICE); 
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
			if (mNetworkInfo != null) { 
				return mNetworkInfo.isAvailable(); 
			} else {
//				Toast.makeText(context, "当前网络不可用，请检查网络后重试", Toast.LENGTH_SHORT).show();
			}
		} 
		return false; 
	}
	
	/**
	 * 设置场所名称，针对ellipse失效不过出现省略号情况
	 * @param name
	 * @return
	 */
	public static String setPlaceName(String name){
		if(name == null) return "";
		if(name.length() > 6){
			return name.substring(0, 6) + "...";
		}else{
			return name;
		}
	}
	
	/**
	 * 格式化数值大小，用KB,MB,GB来表示
	 * 
	 * @param length
	 * @return
	 */
	public static String formatSize(long length) {
		String result = null;
		int sub_string = 0;
		if (length >= 1073741824) {
			sub_string = String.valueOf((float) length / 1073741824).indexOf(
					".");
			result = ((float) length / 1073741824 + "000").substring(0,
					sub_string + 3) + "G";
		} else if (length >= 1048576) {
			sub_string = String.valueOf((float) length / 1048576).indexOf(".");
			result = ((float) length / 1048576 + "000").substring(0,
					sub_string + 3) + "M";
		} else if (length >= 1024) {
			sub_string = String.valueOf((float) length / 1024).indexOf(".");
			result = ((float) length / 1024 + "000").substring(0,
					sub_string + 3) + "K";
		} else if (length < 1024)
			result = Long.toString(length) + "B";
		return result;
	}
	
	/**
	 * 如果字符串是null，则返回双引号
	 * @param str
	 * @return
	 */
	public static String replaceNull(String str){
		if(str == null) return "";
		if(str.equals("null")) return "";
		if(str.trim().equals("")){
			return "";
		}else{
			return str;
		}
	}
	
	/**
	 * 判断当前网络是否是wifi
	 * @param context
	 * @return
	 */
	public static boolean isWIFIConnected(Context context){
		boolean flag = false;
        ConnectivityManager connectMgr = (ConnectivityManager) context
		        .getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectMgr.getActiveNetworkInfo();
		if(info == null) flag = false;
		if(info.getType() == ConnectivityManager.TYPE_WIFI){//WIFI网络
			flag = true;
		}
        return flag;
	}
	
	/**
     * 转换图片成圆形
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float roundPx;
            float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
            if (width <= height) {
                    roundPx = width / 2;
                    top = 0;
                    bottom = width;
                    left = 0;
                    right = width;
                    height = width;
                    dst_left = 0;
                    dst_top = 0;
                    dst_right = width;
                    dst_bottom = width;
            } else {
                    roundPx = height / 2;
                    float clip = (width - height) / 2;
                    left = clip;
                    right = width - clip;
                    top = 0;
                    bottom = height;
                    width = height;
                    dst_left = 0;
                    dst_top = 0;
                    dst_right = height;
                    dst_bottom = height;
            }
            Bitmap output = Bitmap.createBitmap(width,
                            height, Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
            final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
            final RectF rectF = new RectF(dst);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, src, dst, paint);
            return output;
    }
    
    public static String toJson(HashMap<String, Object> map){
		JSONObject obj = new JSONObject();
		try {
			if(map != null && !map.isEmpty()){
				for(Map.Entry<String, Object> entry: map.entrySet()){
					obj.put(entry.getKey(), entry.getValue());
				}
			}else{
				return null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("toJson2: " + obj.toString());
		return obj.toString();
	}
}
