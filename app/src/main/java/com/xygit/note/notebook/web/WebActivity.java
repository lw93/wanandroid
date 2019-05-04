package com.xygit.note.notebook.web;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.xygit.note.notebook.R;
import com.xygit.note.notebook.api.CommResponse;
import com.xygit.note.notebook.api.vo.CommonData;
import com.xygit.note.notebook.base.BaseActivity;
import com.xygit.note.notebook.constant.NoteBookConst;
import com.xygit.note.notebook.manager.evenbus.CollectAction;
import com.xygit.note.notebook.manager.net.HttpManager;
import com.xygit.note.notebook.manager.subscriber.CommSubscriber;
import com.xygit.note.notebook.util.ActivityUtil;
import com.xygit.note.notebook.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

public class WebActivity extends BaseActivity {

    private BridgeWebView bvActivityWeb;
    private Toolbar toolbar;
    private TextView tvTitle;
    private ProgressBar progressBar;
    private CommonData commonData;

    @Override
    public int layoutId() {
        return R.layout.activity_web;
    }

    @Override
    public void initView() {
        toolbar = findViewById(R.id.toolbar);
        tvTitle = findViewById(R.id.tv_title);
        bvActivityWeb = findViewById(R.id.bv_activity_web);
        progressBar = findViewById(R.id.progressBar_web);
    }

    @Override
    public void initAction() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setTitle("");
        tvTitle.setText("详情");
        tvTitle.setLines(1);
        tvTitle.setSingleLine(true);
        tvTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvTitle.setFocusable(true);
        tvTitle.setFocusableInTouchMode(true);
        bvActivityWeb.goBack();
        WebSettings webSettings = bvActivityWeb.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可

        //支持插件
        webSettings.setPluginState(WebSettings.PluginState.ON);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        //优先使用缓存:
        //webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //缓存模式如下：
        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        //不使用缓存:
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        bvActivityWeb.setWebChromeClient(new WebChromeClient());
        bvActivityWeb.setWebViewClient(new JsWebViewClient(bvActivityWeb, tvTitle, progressBar));
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        String url = "";
        if (null != intent) {
            url = intent.getStringExtra(NoteBookConst.INETENT_PARAM_URL);
            commonData = (CommonData) intent.getSerializableExtra(NoteBookConst.INETENT_PARAM_DATA);
        }
        bvActivityWeb.loadUrl(url);
    }

    @Override
    public void clearData() {
        bvActivityWeb.stopLoading();
        bvActivityWeb.clearCache(true);
        bvActivityWeb.clearHistory();
        bvActivityWeb.clearFormData();
        bvActivityWeb.clearSslPreferences();
        bvActivityWeb.destroyDrawingCache();
        bvActivityWeb.destroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_tab_web, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.share_tab_web) {
            shareToOthers();
        } else if (id == R.id.collect_tab_web) {
            addToCollections();
        } else {
            openByBrowser();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openByBrowser() {
        Uri uri = Uri.parse(bvActivityWeb.getUrl());
        ActivityUtil.startActivityBySchemeURL(this, uri, new ActivityUtil.OnActivityException() {
            @Override
            public void onActivityException(Throwable throwable) {
                if (throwable instanceof NullPointerException) {
                    ToastUtil.showToast(throwable.getMessage());
                } else {
                    ToastUtil.showToast("打开失败！");
                }
            }
        });
    }

    private void addToCollections() {
        String id = null != commonData ? String.valueOf(commonData.getId()) : "";
        if (TextUtils.isEmpty(id)) {
            ToastUtil.showToast("收藏失败！");
            return;
        } else if (commonData.isCollect()) {
            ToastUtil.showToast("已收藏！");
            return;
        }
        HttpManager.getInstance().collectInnerArticle(null, id, new CommSubscriber<Object>(this) {

            @Override
            protected void onSucess(CommResponse<Object> objectCommResponse) {
                ToastUtil.showToast("收藏成功！");
                CollectAction collectAction = new CollectAction();
                commonData.setCollect(true);
                collectAction.setData(commonData);
                EventBus.getDefault().post(collectAction);
            }

            @Override
            protected void onFail(Throwable e) {

            }
        });
    }

    private void shareToOthers() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        // 比如发送文本形式的数据内容
        // 指定发送的内容
        sendIntent.putExtra(Intent.EXTRA_TEXT, bvActivityWeb.getUrl());
        // 指定发送内容的类型
        sendIntent.setType("text/plain");
        ActivityUtil.startActivityWithThird(this, sendIntent, new ActivityUtil.OnActivityException() {
            @Override
            public void onActivityException(Throwable throwable) {
                if (throwable instanceof NullPointerException) {
                    ToastUtil.showToast(throwable.getMessage());
                } else {
                    ToastUtil.showToast("分享失败！");
                }
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && bvActivityWeb.canGoBack()) {
            bvActivityWeb.goBack();//返回上个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);//退出H5界面
    }

//    @Subscribe
//    public void collectionAction() {
//        addToCollections();
//    }
}
