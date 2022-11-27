package com.ukang.baiyu.views;

import com.ukang.baiyu.activity.R;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CopyClick implements OnClickListener {
	private String copyText;
	private Activity mContext;
	private PopupWindow popupWindow;
	
	public CopyClick(Activity context, String copyText){
		this.mContext = context;
		this.copyText = copyText;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		System.out.println("v.id: " + v.getId());
		ClipboardManager cbm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clip = ClipData.newPlainText(copyText, copyText);
		cbm.setPrimaryClip(clip);
		RelativeLayout layout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.copy_pop_view, null);
		TextView tvShow = (TextView) layout.findViewById(R.id.tvContent);
		tvShow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
			}
		});
		popupWindow = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new ColorDrawable());
		popupWindow.setAnimationStyle(R.style.AnimationPop);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);// 响应回退按钮事件
		
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		popupWindow.showAsDropDown(v, 0, -(v.getTop() * 4 + v.getHeight()));
	}
};
