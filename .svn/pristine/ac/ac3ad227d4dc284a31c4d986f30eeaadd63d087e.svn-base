package com.ukang.baiyu.activity.tools;

import java.io.IOException;  
import java.io.InputStream;  
import java.net.MalformedURLException;  
  
import org.apache.http.HttpResponse;  
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.impl.client.DefaultHttpClient;  

import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.activity.R;

import android.content.Context;  
import android.content.res.Resources.NotFoundException;  
import android.graphics.drawable.BitmapDrawable;  
import android.graphics.drawable.Drawable;  
import android.os.AsyncTask;  
import android.text.Html.ImageGetter;  
import android.util.DisplayMetrics;  
import android.widget.TextView;  
  
public class URLImageParser implements ImageGetter {  
    Context c;  
    TextView tv_image;  
    private Drawable mDefaultDrawable;  
  
    public URLImageParser(TextView t, Context c) {  
        this.tv_image = t;  
        this.c = c;  
  
        try {  
            mDefaultDrawable = c.getResources().getDrawable(  
                    R.drawable.default_image);  
            // Log.i("-->", "执行");  
        } catch (NotFoundException e) {  
            mDefaultDrawable = null;  
            // Log.i("-->", "执行1");  
            e.printStackTrace();  
        }  
    }  
  
    @Override  
    public Drawable getDrawable(String source) {  
        // TODO Auto-generated method stub  
        URLDrawable urlDrawable = new URLDrawable();  
        // main3.b.add(source);  
        try {  
            /* 
             * mDefaultDrawable.setBounds(0, 0, 0 + 
             * mDefaultDrawable.getIntrinsicWidth(), 
             * mDefaultDrawable.getIntrinsicHeight()); 
             */  
            urlDrawable.drawable = mDefaultDrawable;  
            URLImageParser.this.tv_image.invalidate();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        /* 
         * urlDrawable.setBounds(0, 0, 0 + mDefaultDrawable.getIntrinsicWidth(), 
         * mDefaultDrawable.getIntrinsicHeight()); 
         */  
        ImageGetterAsyncTask asyncTask = new ImageGetterAsyncTask(urlDrawable);  
        asyncTask.execute(source);  
        return urlDrawable;  
    }  
  
    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {  
        URLDrawable urlDrawable;  
  
        public ImageGetterAsyncTask(URLDrawable d) {  
            this.urlDrawable = d;  
        }  
  
        @Override  
        protected void onPostExecute(Drawable result) {  
            if (result != null) {  
                urlDrawable.drawable = result;  
                URLImageParser.this.tv_image.invalidate();  
                // Log.i("-->", "执行3");  
            }  
        }  
  
        @Override  
        protected void onPreExecute() {  
            urlDrawable.setBounds(0, 0,  
                    0 + mDefaultDrawable.getIntrinsicWidth(),  
                    0 + mDefaultDrawable.getIntrinsicHeight());  
            urlDrawable.drawable = mDefaultDrawable;  
            URLImageParser.this.tv_image.invalidate();  
            super.onPreExecute();  
        }  
  
        @Override  
        protected Drawable doInBackground(String... params) {  
            // TODO Auto-generated method stub  
            String source = params[0];// 图片URL  
            return fetchDrawable(source);  
        }  
  
        // 获取URL的Drawable对象  
        public Drawable fetchDrawable(String urlString) {  
            BitmapDrawable bitmap = null;  
            Drawable drawable = null;  
            try {  
                InputStream is = fetch(urlString);  
                bitmap = (BitmapDrawable) BitmapDrawable.createFromStream(is,"src");  
                drawable = bitmap;  
                DisplayMetrics metrics = Constant.metrics;  
                if(bitmap.getBitmap().getWidth()>metrics.widthPixels||bitmap.getBitmap().getHeight()>metrics.heightPixels)  
                    //进行等比例缩放程序  
                    drawable.setBounds(0, 0, metrics.widthPixels, ((int)(metrics.widthPixels*bitmap.getBitmap().getHeight()/bitmap.getBitmap().getWidth())));  
                else  
                    drawable.setBounds(0,0,bitmap.getBitmap().getWidth(),bitmap.getBitmap().getHeight());  
            } catch (Exception e) {  
                return null;  
            }  
            return drawable;  
        }  
  
        private InputStream fetch(String urlString)  
                throws MalformedURLException, IOException {  
            DefaultHttpClient httpClient = new DefaultHttpClient();  
            HttpGet request = new HttpGet(urlString);  
            HttpResponse response = httpClient.execute(request);  
            return response.getEntity().getContent();  
        }  
    }  
}
