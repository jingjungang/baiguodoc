package com.ukang.baiyu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.entity.CommonEntity;

public class DepartmentAdapter extends BaseAdapter {

    private List<CommonEntity> depList;
    Context mContext;
    List<String> nameList ;

    public DepartmentAdapter(List<CommonEntity> depList, Context mContext, List<String> name_List) {
        this.depList = depList;
        this.mContext = mContext;
        this.nameList = name_List;
    }

    @Override
    public int getCount() {
        return depList.size();
    }

    @Override
    public Object getItem(int position) {
        return depList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MedicalHolder holder = null;
        if (convertView == null) {
            holder = new MedicalHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.department_item, null);
            holder.tvDepName = (TextView) convertView.findViewById(R.id.tv_dep_name);
            holder.ivOptPic = (ImageView) convertView.findViewById(R.id.iv_opt_pic);
            convertView.setTag(holder);
        } else {
            holder = (MedicalHolder) convertView.getTag();
        }
        try {
            holder.tvDepName.setText(depList.get(position).getStr2());
            holder.ivOptPic.setContentDescription("0");
            if (nameList != null && !nameList.isEmpty()) {
                if (nameList.contains(depList.get(position).getStr1())) {
                    holder.ivOptPic.setContentDescription("1");
                    holder.ivOptPic.setBackgroundResource(R.drawable.selected);
                    if ("03-7".equals(depList.get(position).getStr1())) {
                        convertView.setEnabled(false);
                    } else {
                        convertView.setEnabled(true);
                    }
                }
            }
            final ImageView ivPic = holder.ivOptPic;
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (ivPic.getContentDescription().equals("0")) {
                        ivPic.setBackgroundResource(R.drawable.selected);
                        ivPic.setContentDescription("1");
                        Constant.conferenceAddKey.put(depList.get(position).getStr1(), depList.get(position).getStr2());
                        Constant.conferenceDelKey.remove(depList.get(position).getStr1());
                    } else {
                        ivPic.setBackgroundResource(R.drawable.add);
                        ivPic.setContentDescription("0");
                        Constant.conferenceAddKey.remove(depList.get(position).getStr1());
                        Constant.conferenceDelKey.put(depList.get(position).getStr1(), depList.get(position).getStr2());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    class MedicalHolder {

        TextView tvDepName;
        ImageView ivOptPic;
    }
}
