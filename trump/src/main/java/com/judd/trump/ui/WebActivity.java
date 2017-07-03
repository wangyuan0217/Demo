package com.judd.trump.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.judd.trump.R;
import com.judd.trump.app.TConstant;
import com.judd.trump.base.TActivity;

public class WebActivity extends TActivity {

    private static int MAX_TITLE_LENGTH = 12;
    private static String DEFAULT_TITLE = "Loading";

    private WebView mWebview;
    private String url;

    public static void open(Context mContext, String url) {
        Intent intent = new Intent(mContext, WebActivity.class);
        intent.putExtra(TConstant.PARAMS, url);
        mContext.startActivity(intent);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_web);
    }

    @Override
    protected void bindView() {
        mWebview = (WebView) findViewById(R.id.webview);
    }

    @Override
    protected void initData(Bundle savedInstance) {
        initTitle(DEFAULT_TITLE);
        url = getIntent().getStringExtra(TConstant.PARAMS);

        init();
    }

    public void init() {
        WebSettings mWebSettings = mWebview.getSettings();

        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setSupportZoom(false);
        mWebSettings.setBuiltInZoomControls(false);
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setSaveFormData(true);
        mWebSettings.setJavaScriptEnabled(true);
        // enable navigator.geolocation
        mWebSettings.setGeolocationEnabled(true);
        // enable Web Storage: localStorage, sessionStorage
        mWebSettings.setDomStorageEnabled(true);
        //不显示webview缩放按钮
        mWebSettings.setDisplayZoomControls(false);

        mWebview.requestFocus();


        // 帮助WebView处理各种通知、请求事件的
        mWebview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                view.loadUrl(url);
                return true;
            }
        });

        // 主要处理解析，渲染网页等浏览器做的事情,WebChromeClient是辅助WebView处理Javascript的对话框，网站图标，网站title，加载进度等
        mWebview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                initTitle("Loading...");
                if (progress == 100) {
//                    desc = view.getContentDescription();
                    initTitle(view.getTitle().length() <= MAX_TITLE_LENGTH ?
                            view.getTitle() : view.getTitle().substring(0, MAX_TITLE_LENGTH));
                }
                super.onProgressChanged(view, progress);
            }
        });
        mWebview.loadUrl(url);
    }

    // 屏蔽返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebview.canGoBack()) {
            mWebview.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        mWebview.destroy();
        return super.onKeyDown(keyCode, event);
    }
}
