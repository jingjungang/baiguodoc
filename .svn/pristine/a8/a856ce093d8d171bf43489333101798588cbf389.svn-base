package com.ukang.baiyu.activity.science;

/**
 * "科研课题" 7
 * "论文润色" 1
 * "学术翻译" 2
 * "发表支持" 3
 * "统计分析" 4
 * "科研病历" 6
 */
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.baiyu.common.Constant;
import com.ukang.baiyu.common.DataParser;
import com.ukang.baiyu.entity.Response;
import com.ukang.baiyu.systembartint.SystemBarTintManager;
import com.ukang.baiyu.thread.XThread;
import com.ukang.baiyu.view.swipebacklayout.lib.SwipeBackLayout;
import com.ukang.baiyu.view.swipebacklayout.lib.app.SwipeBackActivity;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.activity.login.LoginActivity;
import com.ukang.baiyu.activity.me.CommentListActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewsDetailActivity extends SwipeBackActivity {

    private static SystemBarTintManager tintManager;
    @ViewInject(R.id.iv_back)
    private ImageButton btnBack;
    @ViewInject(R.id.line_cmt)
    private LinearLayout lineCmt;
    @ViewInject(R.id.line_store)
    private LinearLayout lineStore;
    @ViewInject(R.id.line_share)
    private LinearLayout lineShare;
    @ViewInject(R.id.tv_zan)
    private TextView tvZanTotal;

    @ViewInject(R.id.et_comment)
    private EditText etComment;
    @ViewInject(R.id.btn_comment)
    private Button btnComment;
    @ViewInject(R.id.btn_zan)
    private ImageButton btnZan;

    @ViewInject(R.id.web_view)
    private WebView webView;

    @ViewInject(R.id.line_content)
    private LinearLayout lineContent;
    @ViewInject(R.id.line_progress)
    private LinearLayout lineProgress;

    private SwipeBackLayout mSwipeBackLayout;
    private final String DOC_NEWS_SHOW_URL = Constant.HOST_ADDR + "/news/newsshow/";
    private final String REPORT_NEWS_SHOW_URL = Constant.HOST_ADDR + "/news/reportsshow/";
    private final String CONFERENCE_NEWS_SHOW_URL = Constant.HOST_ADDR + "/conference/meetingshow/";
    private final String LITERS_NEWS_SHOW_URL = Constant.HOST_ADDR + "/pubmed/apishowliters/";
    private final String SCIENCE_NEWS_SHOW_URL = Constant.HOST_ADDR + "/scientific/index/";
    private final String CAROUSEL_NEWS_SHOW_URL = Constant.HOST_ADDR + "/consultation/listsshow/";
    private String browserUrl = "";
    private String id;
    private int newsKind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);
        ViewUtils.inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.index_title_color);
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setScrimColor(Color.TRANSPARENT);
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        initView();
        addClickListener();
        id = getIntent().getStringExtra("id");
        // browserUrl = id;
        newsKind = getIntent().getIntExtra("NEWS_KIND", 1);
        switch (newsKind) {
        case 1://业内新闻
            browserUrl = DOC_NEWS_SHOW_URL + id;
            break;
        case 2:
            browserUrl = REPORT_NEWS_SHOW_URL + id;
            break;
        case 3://资讯科室
            browserUrl = CONFERENCE_NEWS_SHOW_URL + id;
            break;
        case 4:// 科研
            browserUrl = SCIENCE_NEWS_SHOW_URL + id;
            break;
        case 5:// 轮播详情
            browserUrl = CAROUSEL_NEWS_SHOW_URL + id;
            break;
        default:
            browserUrl = DOC_NEWS_SHOW_URL + id;
            break;
        }
        System.out.println(browserUrl);
        initWebView();
        // showHandler.sendEmptyMessage(0);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); // 统计时长
        loadZanTotal();
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        webView.reload();
    }

    private void initView() {
        lineCmt.setVisibility(View.VISIBLE);
        lineShare.setVisibility(View.VISIBLE);
    }

    private void addClickListener() {
        btnBack.setOnClickListener(btnClick);
        lineShare.setOnClickListener(lineClick);
        lineStore.setOnClickListener(lineClick);
        btnComment.setOnClickListener(btnClick);
        lineCmt.setOnClickListener(lineClick);
        btnZan.setOnClickListener(btnClick);
    }

    /**
     * 初始化webview
     */
    private void initWebView() {
        // 支持缩放
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSaveFormData(true);
        webView.clearCache(false);
        // 联网载入
        webView.loadUrl(browserUrl);
        // 设置
        webView.setWebViewClient(new WebViewClient() {

            /** 开始载入页面 */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                setProgressBarIndeterminateVisibility(true);// 设置标题栏的滚动条开始
                Log.d("YM", "setProgressBarIndeterminateVisibility");
                browserUrl = url;
                super.onPageStarted(view, url, favicon);
            }

            /** 捕获点击事件 */
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

            /** 错误返回 */
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            /** 页面载入完毕 */
            @Override
            public void onPageFinished(WebView view, String url) {
                setProgressBarIndeterminateVisibility(false);// ֹ
                super.onPageFinished(view, url);
                lineContent.setVisibility(View.VISIBLE);
                lineProgress.setVisibility(View.GONE);
            }

        });

        webView.setWebChromeClient(new WebChromeClient() {

            /** 进度条变化 */
            public void onProgressChanged(WebView view, int newProgress) {
                // 设置标题栏的滚动条停止
                getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress * 100);
                super.onProgressChanged(view, newProgress);
            }

            /** 设置标题 */
            public void onReceivedTitle(WebView view, String title) {
                // WebViewApp.this.setTitle(title);
                super.onReceivedTitle(view, title);
            }
        });
    }

    private OnClickListener btnClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (v == btnBack) {
                finish();
            } else if (v == btnComment) {
                if (Constant.users.getUsername().equals(LoginActivity.GUEST_NAME)) {
                    Toast.makeText(NewsDetailActivity.this, "您现在是游客模式，请使用正式账号登录", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(NewsDetailActivity.this, LoginActivity.class);
                    intent.putExtra("actionType", LoginActivity.ACTION_TYPE_NEED_LOGIN);
                    startActivity(intent);
                } else
                    confirmComments();
            } else if (v == btnZan) {
                pointZan();
            }
        }
    };

    private UMShareListener umShareListener = new UMShareListener() {

        @Override
        public void onResult(SHARE_MEDIA platform) {
            // Toast.makeText(NewsDetailActivity.this, platform + " 分享成功啦", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            // Toast.makeText(NewsDetailActivity.this,platform + " 分享失败啦", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            // Toast.makeText(NewsDetailActivity.this,platform + " 分享取消了", Toast.LENGTH_LONG).show();
        }
    };

    private OnClickListener lineClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (Constant.users.getUsername().equals(LoginActivity.GUEST_NAME)) {
                Toast.makeText(NewsDetailActivity.this, "您现在是游客模式，请使用正式账号登录", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(NewsDetailActivity.this, LoginActivity.class);
                intent.putExtra("actionType", LoginActivity.ACTION_TYPE_NEED_LOGIN);
                startActivity(intent);
                return;
            }
            if (v == lineCmt) {
                MobclickAgent.onEvent(NewsDetailActivity.this, "COMMENT_LIST");
                Intent intent = new Intent(NewsDetailActivity.this,
                // CmtListActivity.class);
                        CommentListActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("NEWS_KIND", newsKind);
                startActivity(intent);
            } else if (v == lineStore) {
                Map<String, String> map = Constant.storeMap.get(newsKind);
                if (map != null && map.containsKey(id)) {
                    delStore();
                } else {
                    addStore();
                }
            } else if (v == lineShare) {
            	Toast.makeText(NewsDetailActivity.this, "敬请期待", Toast.LENGTH_SHORT).show();
//                 share();
            }
        }
    };

    private void share() {
        String targetUrl = browserUrl;
        // Log.i("targetUrl", targetUrl);
        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[] {SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE };
        new ShareAction(this)
                .setDisplayList(displaylist)
                // .withText( "呵呵" )
                // .withTitle("title")
                .withTargetUrl(targetUrl).setListenerList(umShareListener, umShareListener)
                .setShareboardclickCallback(shareBoardlistener).open();
    }

    private ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {

        @Override
        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
            String targetUrl = browserUrl;
            if (share_media == SHARE_MEDIA.WEIXIN) {
                new ShareAction(NewsDetailActivity.this).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
                        .withText("我说").withTargetUrl(targetUrl)
                        // .withMedia(image)
                        .share();

            }
        }

    };

    private void confirmComments() {
        String comment = etComment.getText().toString();
        if (comment.trim().equals("")) {
            return;
        } else {
            MobclickAgent.onEvent(NewsDetailActivity.this, "ADD_COMMENT");
            confirm(comment);
        }
    }

    private void loadZanTotal() {
        Handler zanHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                switch (msg.what) {
                case 0:
                    String result = msg.obj.toString();
                    try {
                        JSONObject obj = new JSONObject(result);
                        int ret = obj.getInt("status");
                        int number = obj.getInt("number");
                        tvZanTotal.setText(String.valueOf(number));
                    } catch (JSONException e) {
                        e.printStackTrace();
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
        params.addBodyParameter("id", id);
        int type = newsKind;
        switch (newsKind) {
        case 1:
            type = 1;
            break;
        case 2:
            type = 1;
            break;
        case 3:
            type = 2;
            break;
        }
        params.addBodyParameter("type", type + "");
        XThread thread = new XThread(this, 0, params, Constant.ZAN_TOTAL_URL, zanHandler);
        thread.start();
    }

    private void pointZan() {
        Handler zanHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                switch (msg.what) {
                case 0:
                    String result = msg.obj.toString();
                    Response response = DataParser.parseAddComment(result);
                    if (response.getRet() == 1) {
                        Toast.makeText(NewsDetailActivity.this, R.string.zan_success, Toast.LENGTH_LONG).show();
                    } else if (response.getRet() == 2) {
                        Toast.makeText(NewsDetailActivity.this, R.string.zan_cancel_success, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(NewsDetailActivity.this, "未获取到数据", Toast.LENGTH_LONG).show();
                    }
                    break;
                case -1:
                    Toast.makeText(NewsDetailActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
                }
            }
        };
        RequestParams params = new RequestParams();
        params.addHeader("Cookie", Constant.sessionId);
        params.addBodyParameter("token", Constant.token);
        params.addBodyParameter("id", id);
        int type = newsKind;
        switch (newsKind) {
        case 1:
            type = 1;
            break;
        case 2:
            type = 1;
            break;
        case 3:
            type = 2;
            break;
        case 4:
            type = 3;
            break;
        }
        params.addBodyParameter("type", type + "");
        XThread thread = new XThread(this, 0, params, Constant.DIAN_ZAN_URL, zanHandler);
        thread.setShowDia(true);
        thread.start();
    }

    private void confirm(String comment) {
        RequestParams params = new RequestParams();
        params.addHeader("Cookie", Constant.sessionId);
        params.addBodyParameter("token", Constant.token);
        params.addBodyParameter("id", id);
        int type = 1;
        switch (newsKind) {
        case 1:
            type = 3;
            break;
        case 2:
            type = 2;
            break;
        case 3:
            type = 1;
            break;
        default:
            break;
        }
        params.addBodyParameter("type", type + "");
        params.addBodyParameter("content", comment);
        XThread thread = new XThread(this, 0, params, Constant.ADD_COMMENT_URL, addHandler);
        thread.setShowDia(true);
        thread.start();
    }

    private Handler addHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
            case 0:
                String result = msg.obj.toString();
                Response response = DataParser.parseAddComment(result);
                if (response.getRet() == 1) {
                	etComment.setText("");
                    Toast.makeText(NewsDetailActivity.this, R.string.comment_success, Toast.LENGTH_LONG).show();
                    // finish();
                } else {
                    Toast.makeText(NewsDetailActivity.this, response.getMsg(), Toast.LENGTH_LONG).show();
                }
                break;
            case -1:
                Toast.makeText(NewsDetailActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
            }
        }
    };

    private void addStore() {
        RequestParams params = new RequestParams();
        params.addHeader("Cookie", Constant.sessionId);
        params.addBodyParameter("token", Constant.token);
        params.addBodyParameter("id", id);
        int type = 1;
        switch (newsKind) {
        case 1:
            type = 3;
            break;
        case 2:
            type = 2;
            break;
        case 3:
            type = 1;
            break;
        default:
            break;
        }
        params.addBodyParameter("type", type + "");
        XThread thread = new XThread(this, 0, params, Constant.ADD_STORE_URL, addStoreHandler);
        thread.setShowDia(true);
        thread.start();
    }

    private void delStore() {
        Handler delStoreHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                switch (msg.what) {
                case 0:
                    String result = msg.obj.toString();
                    Response response = DataParser.parseAddComment(result);
                    if (response.getRet() == 1) {
                        Toast.makeText(NewsDetailActivity.this, R.string.del_store_success, Toast.LENGTH_LONG).show();
                    } else if (response.getRet() == -1) {
                        Toast.makeText(NewsDetailActivity.this, "唯一标识不存在", Toast.LENGTH_LONG).show();
                    } else if (response.getRet() == -2) {
                        Toast.makeText(NewsDetailActivity.this, "删除失败", Toast.LENGTH_LONG).show();
                    }
                    break;
                case -1:
                    Toast.makeText(NewsDetailActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
                }
            }
        };
        RequestParams params = new RequestParams();
        params.addHeader("Cookie", Constant.sessionId);
        params.addBodyParameter("token", Constant.token);
        params.addBodyParameter("linked", id);
        int type = 1;
        switch (newsKind) {
        case 1:
            type = 3;
            break;
        case 2:
            type = 2;
            break;
        case 3:
            type = 1;
            break;
        default:
            break;
        }
        params.addBodyParameter("type", type + "");
        XThread thread = new XThread(this, 0, params, Constant.DEL_STORE_URL, delStoreHandler);
        thread.setShowDia(true);
        thread.start();
    }

    private Handler addStoreHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
            case 0:
                String result = msg.obj.toString();
                Response response = DataParser.parseAddComment(result);
                if (response.getRet() == 1) {
                    Toast.makeText(NewsDetailActivity.this, R.string.store_success, Toast.LENGTH_LONG).show();
                    Map<String, String> map = Constant.storeMap.get(newsKind);
                    if (map != null) {
                        map.put(id, id);
                    } else {
                        map = new HashMap<String, String>();
                        map.put(id, id);
                        Constant.storeMap.put(newsKind, map);
                    }
                    // finish();
                } else {
                    Toast.makeText(NewsDetailActivity.this, response.getMsg(), Toast.LENGTH_LONG).show();
                }
                break;
            case -1:
                Toast.makeText(NewsDetailActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
            }
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                webView.clearCache(true);
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
