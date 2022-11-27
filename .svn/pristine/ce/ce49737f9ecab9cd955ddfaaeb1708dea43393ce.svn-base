package com.ukang.baiyu.widget;

import com.ukang.baiyu.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MyToast extends Toast {
		Context mContext;
		
	public MyToast(Context context,String info) {
		super(context);
		mContext = context;
		LayoutInflater li = LayoutInflater.from(context);
		View v = li.inflate(R.layout.layout_toast, null);
		((TextView) v.findViewById(R.id.txt_tips)).setText(info);
		this.setView(v);
		// TODO Auto-generated constructor stub
	}

}
