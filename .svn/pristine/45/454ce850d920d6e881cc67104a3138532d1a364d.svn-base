package com.ukang.baiyu.activity.me;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.common.DataParser;
import com.ukang.baiyu.entity.AppLink;
import com.ukang.baiyu.entity.Comment;
import com.ukang.baiyu.entity.Response;
import com.ukang.baiyu.entity.SearchNews;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.activity.science.NewsDetailActivity;
import com.ukang.baiyu.thread.XThread;
import com.ukang.baiyu.view.pulltorefresh.library.PullToRefreshBase;
import com.ukang.baiyu.view.pulltorefresh.library.PullToRefreshListView;
import com.ukang.baiyu.view.pulltorefresh.library.PullToRefreshBase.Mode;

/**
 * 我的评论
 *
 * @author SAN
 */
public class CommentListActivity extends Activity {

    private Activity mContext;

    @ViewInject(R.id.line_back)
    private LinearLayout lineBack;
    @ViewInject(R.id.iv_back)
    private ImageButton btnBack;
    @ViewInject(R.id.tv_title)
    private TextView tvTitle;
    @ViewInject(R.id.line_cmt)
    private LinearLayout lineCmt;
    @ViewInject(R.id.line_share)
    private LinearLayout lineShare;

    private ListView listView;
    @ViewInject(R.id.list_view)
    private PullToRefreshListView pullListView;
    private CommentAdapter adapter;
    private List<Comment> commentList;
    private String linkid;
    private int type;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_comment);
        mContext = this;
        ViewUtils.inject(this);
        linkid = getIntent().getStringExtra("id");
        type = getIntent().getIntExtra("NEWS_KIND", 0);
        initview();
    }

    @SuppressWarnings("unchecked")
    private void initview() {
        lineBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(btnClick);
        tvTitle.setText("");
//		btnSearch.setVisibility(View.VISIBLE);
//		btnSearch.setOnClickListener(btnClick);
//		myStoreList = new ArrayList<SearchNews>();
//		lineCmt.setVisibility(View.VISIBLE);
        lineShare.setVisibility(View.GONE);

        pullListView.setMode(Mode.PULL_FROM_END);
        listView = pullListView.getRefreshableView();
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                if (Constant.ISDEBUG)
                    Log.i("---", "onPullDownToRefresh");
                try {
                    pullListView.setRefreshing();
                    pullHandler.sendEmptyMessageDelayed(1, 2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                if (Constant.ISDEBUG)
                    Log.i("---", "onPullUpToRefresh");
                try {
                    pullListView.setRefreshing();
                    pullHandler.sendEmptyMessageDelayed(1, 2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        commentList = new ArrayList<Comment>();
        adapter = new CommentAdapter();
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        Handler loadHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        String result = msg.obj.toString();
                        Response response = DataParser.parseCommentList(result);
                        if (response.getRet() == 1) {
                            if (response.getList() != null && !response.getList().isEmpty()) {
                                commentList = (List<Comment>) response.getList();
                                adapter.notifyDataSetChanged();
                            }
                        } else {

                        }
                        break;
                    case -1:
                        break;
                    default:
                        break;
                }
            }
        };
        RequestParams params = new RequestParams();
        params.addHeader("Cookie", Constant.sessionId);
		params.addBodyParameter("token", Constant.token);
		params.addBodyParameter("id", String.valueOf(linkid));
        params.addBodyParameter("type", String.valueOf(3));
//		params.addBodyParameter("userid", "");
        String cmtUrl = "";
        switch (type) {
            case 1:
                cmtUrl = Constant.NEWS_CMT_LIST_URL;
                break;
            case 2:
                cmtUrl = Constant.NEWS_CMT_LIST_URL;
                break;
            case 3://会议
                cmtUrl = Constant.CONFERENCE_CMT_LIST_URL;
                break;
            case 4://读片
                cmtUrl = Constant.READ_PIC_CMT_LIST_URL;
                break;
            default:
                break;
        }
        XThread thread = new XThread(this, 0, params, cmtUrl, loadHandler);
        thread.start();
    }

    private Handler pullHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            pullListView.onRefreshComplete();
        }
    };

    private OnClickListener btnClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (v == btnBack) {
                finish();
            }
        }
    };

    class CommentAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return commentList.size();
        }

        @Override
        public Object getItem(int position) {
            return commentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            StoreHolder holder = null;
            if (convertView == null) {
                holder = new StoreHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.cmt_list_item, null);
                holder.linNewsPic = (LinearLayout) convertView.findViewById(R.id.linNewsPic);
                holder.imgNewsPic = (ImageView) convertView
                        .findViewById(R.id.iv_news_pic);
                holder.title = (TextView) convertView
                        .findViewById(R.id.tv_news_title);
                holder.description = (TextView) convertView
                        .findViewById(R.id.tv_news_desc);
                holder.date_text = (TextView) convertView
                        .findViewById(R.id.tv_news_time);
                convertView.setTag(holder);
            } else {
                holder = (StoreHolder) convertView.getTag();
            }
//			imageLoader.displayImage("https://wifamily.blob.core.chinacloudapi.cn/wom/2015052712084080.jpg",
//					holder.imgNewsPic, options, animateFirstListener);
            try {
               // holder.title.setText(commentList.get(position).getTitle());
                holder.description.setText(commentList.get(position).getContent());
                holder.date_text.setText(commentList.get(position).getAdd_time());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }

    class StoreHolder {
        LinearLayout linNewsPic;
        ImageView imgNewsPic;
        TextView title;
        TextView description;
        TextView date_text;
    }
}
