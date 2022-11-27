package com.ukang.baiyu.fragments.lunbo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ukang.baiyu.activity.science.NewsDetailActivity;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.entity.BeautyMainVo.Imgs;
import com.ukang.baiyu.utils.LoadingImgUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * 轮播适配
 */
public class LunBoAdapter extends PagerAdapter {

	private Context context;
	private ArrayList<Imgs> lunboList;

	public LunBoAdapter(Context context, ArrayList<Imgs> lunboList) {
		this.lunboList = lunboList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((ImageView) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {

		final int park = position % lunboList.size();
		final Imgs vo = lunboList.get(park);
		final ImageView img = new ImageView(context);
		img.setTag(position);
		img.setScaleType(ScaleType.CENTER_CROP);
		LoadingImgUtil.loadimgAnimate(vo.getImageUrl(), img);
		((ViewPager) container).addView(img, 0);
		final List<Map<String,String>> list = Constant.getLunBoList();
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, NewsDetailActivity.class);
				switch(position%3){
				case 0:
					intent.putExtra("id", list.get(0).get("id"));
					intent.putExtra("NEWS_KIND", 5);
					context.startActivity(intent);
					break;
				case 1:
					intent.putExtra("id", list.get(1).get("id"));
					intent.putExtra("NEWS_KIND", 5);
					context.startActivity(intent);
					break;
				case 2:
					intent.putExtra("id", list.get(2).get("id"));
					intent.putExtra("NEWS_KIND", 5);
					context.startActivity(intent);
					break;
				}
			}
		});
		return img;
	}

}
