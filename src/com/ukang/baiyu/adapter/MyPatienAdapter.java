package com.ukang.baiyu.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.utils.AsyncLoadingImg;

public class MyPatienAdapter extends BaseAdapter {

	private Context context;
	private List<Map<String,Object>> list;
	
	public ImageLoader imageLoader;
	
	
	public MyPatienAdapter(Context context, List<Map<String, Object>> list) {
		super();
		this.context = context;
		this.list = list;
		imageLoader = AsyncLoadingImg.getImageLoader(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(list.size() == 0){
			return 0;
		}
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolde holde;
		if(arg1 == null){
			holde = new ViewHolde();
			arg1 = LayoutInflater.from(context).inflate(R.layout.item_patient, null);
			holde.tv_contact_user = (TextView) arg1.findViewById(R.id.tv_contact_user);
			holde.tv_contact_section = (TextView) arg1.findViewById(R.id.tv_contact_section);
			holde.tv_contact_sex = (TextView) arg1.findViewById(R.id.tv_contact_sex);
			holde.img = (ImageView) arg1.findViewById(R.id.iv_contact_thumb);
			arg1.setTag(holde); 
		}else{
			holde = (ViewHolde) arg1.getTag();
		}
		if(!list.get(arg0).get("nickname").toString().equals("")&&list.get(arg0).get("nickname").toString()!=null){
			holde.tv_contact_user.setText("姓名："+list.get(arg0).get("nickname").toString());
		}else{
			holde.tv_contact_user.setText("姓名："+list.get(arg0).get("username").toString());
		}
		if(list.get(arg0).get("sex").equals("0")){
			holde.tv_contact_sex.setText("性别：女");
		}else{
			holde.tv_contact_sex.setText("性别：男");
		}
		holde.tv_contact_section.setText("年龄："+list.get(arg0).get("age"));
		String avatar = (String) list.get(arg0).get("avatar");
		if(avatar != null && !avatar.equals("null")&&!avatar.equals("")){
		imageLoader.displayImage((String) list.get(arg0).get("avatar"), holde.img,AsyncLoadingImg.getDefaultOptions());
		}else{
			holde.img.setImageResource(R.drawable.icon_man);
		}
		return arg1;
	}

	class ViewHolde{
		TextView tv_contact_user,tv_contact_section,tv_contact_sex;
		ImageView img;
	}
}
