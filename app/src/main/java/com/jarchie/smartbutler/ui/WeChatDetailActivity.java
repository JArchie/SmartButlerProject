package com.jarchie.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ProgressBar;
import com.jarchie.smartbutler.R;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.ui
 * 文件名:   WeChatDetailActivity
 * 创建者:   Jarchie
 * 创建时间: 17/1/23 上午10:23
 * 描述:     微信精选的详情页,这里用到的是WebView技术
 */

public class WeChatDetailActivity extends BaseActivity{
    private String title,url;
    private ProgressBar mProgressbar;
    private WebView mWebview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechar_detail);
        initView();
    }

    //初始化View
    private void initView() {
        mProgressbar = (ProgressBar) findViewById(R.id.mProgressBar);
        mWebview = (WebView) findViewById(R.id.mWebView);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        url = intent.getStringExtra("url");
        //设置标题
        getSupportActionBar().setTitle(title);
        //加载网页的处理逻辑
        mWebview.getSettings().setJavaScriptEnabled(true);
        //支持缩放
        mWebview.getSettings().setSupportZoom(true);
        mWebview.getSettings().setBuiltInZoomControls(true);
        //接口回调
        mWebview.setWebChromeClient(new WebViewClient());
        //加载网页
        mWebview.loadUrl(url);
        //本地显示
        mWebview.setWebViewClient(new android.webkit.WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                //接收这个事件
                return true;
            }
        });
    }

    //自定义WebviewClient
    public class WebViewClient extends WebChromeClient{
        //进度变化的监听
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            //处理进度,加载完成时隐藏进度条
            if (newProgress == 100){
                mProgressbar.setVisibility(View.GONE);
            }
        }
    }

}
