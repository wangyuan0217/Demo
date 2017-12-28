package com.judd.trump.ui.common_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.judd.trump.R;
import com.judd.trump.R2;
import com.judd.trump.base.TActivity;

import java.util.ArrayList;

import butterknife.BindView;

public class WebActivity extends TActivity {

    @BindView(R2.id.title_tv)
    TextView mTitle;
    @BindView(R2.id.progress)
    ProgressBar progressbar;
    @BindView(R2.id.webview)
    WebView webview;

    private String url;

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_web);
    }

    @Override
    protected void initData(Bundle savedInstance) {
        initTitle("Loading...");
        url = getIntent().getStringExtra("url");

        initWebViewSettings();

        // 帮助WebView处理各种通知、请求事件的
        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                view.loadUrl(url);
                return true;
            }
        });

        // 主要处理解析，渲染网页等浏览器做的事情,WebChromeClient是辅助WebView处理Javascript的对话框，网站图标，网站title，加载进度等
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progressbar.getVisibility() == View.GONE)
                    progressbar.setVisibility(View.VISIBLE);
                progressbar.setProgress(progress);
                if (progress == 100) {
                    progressbar.setVisibility(View.GONE);
//                    desc = view.getContentDescription();
                    if (!TextUtils.isEmpty(view.getTitle()))
                        mTitle.setText(view.getTitle());
                }
                super.onProgressChanged(view, progress);
            }
        });
        webview.loadUrl(url);
    }

    private void initWebViewSettings() {
        WebSettings s = webview.getSettings();
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setJavaScriptEnabled(true);
        //不显示webview缩放按钮
        s.setDisplayZoomControls(false);
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webview.destroy();
    }
}
