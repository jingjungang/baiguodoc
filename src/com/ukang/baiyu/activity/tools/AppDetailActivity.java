package com.ukang.baiyu.activity.tools;

import java.net.URL;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.view.slidingmenu.SlidingMenu;
import com.ukang.baiyu.view.swipebacklayout.lib.SwipeBackLayout;
import com.ukang.baiyu.view.swipebacklayout.lib.app.SwipeBackActivity;
import com.ukang.baiyu.activity.R;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.util.Log;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class AppDetailActivity extends SwipeBackActivity {

	@ViewInject(R.id.web_view)
	private WebView webView;
	private SwipeBackLayout mSwipeBackLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_detail);
		ViewUtils.inject(this);
//		showHandler.sendEmptyMessage(0);
		mSwipeBackLayout = getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
	}
	
	ImageGetter imgGetter = new Html.ImageGetter() { 
        @Override 
        public Drawable getDrawable(String source) { 
              Drawable drawable = null; 
              System.out.println("source: " + source);
              URL url;
              try {
                  url = new URL(source);
                  drawable = Drawable.createFromStream(url.openStream(), "temp.jpg");
              } catch (Exception e) {
                  e.printStackTrace();
                  return null;
              }
//              drawable = Drawable.createFromPath(source);  // Or fetch it from the URL 
              // Important 
              drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable 
                            .getIntrinsicHeight()); 
              return drawable; 
        } 
	}; 

	private Handler showHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
		}
	};
}
