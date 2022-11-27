package com.ukang.baiyu.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.entity.Notice;

/**
 * Created by zzd on 2016/7/5.
 * 
 * 项目公告列表适配器
 */
public class Announcement_Adapter extends BaseAdapter {

	Context content;
	List<Notice> list;

	public Announcement_Adapter(Context content, List<Notice> list) {
		this.content = content;
		this.list = list;
	}

	public void setList(List<Notice> list) {
		this.list = list;
	}

	public List<Notice> getList() {
		return list;
	}

	@Override
	public int getCount() {
		if (list != null) {
			return list.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int i) {
		return null;
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public View getView(final int i, View view, ViewGroup viewGroup) {
		ViewHodler hodler;
		if (view == null) {
			hodler = new ViewHodler();
			view = LayoutInflater.from(content).inflate(
					R.layout.announcement_item, null);
			hodler.title_name = (TextView) view.findViewById(R.id.title_name);
			hodler.status = (ImageView) view.findViewById(R.id.status);
			view.setTag(hodler);
		} else {
			hodler = (ViewHodler) view.getTag();
		}
//		Notice notice = list.get(i);
//		hodler.title_name.setText(notice.getTitle() + " [ "
//				+ notice.getNickname() + "-" + notice.getAdd_time() + " ]");
//		view.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View view) {
//				// Intent intent = new Intent(content, DetailsActivity.class);
//				// intent.putExtra("id", list.get(i).getId());
//				// content.startActivity(intent);
//			}
//		});
		return view;
	}

	class ViewHodler {
		ImageView status;
		TextView title_name;
	}
}
