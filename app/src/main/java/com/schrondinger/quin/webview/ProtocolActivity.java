package com.schrondinger.quin.webview;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.schrondinger.quin.R;
import com.schrondinger.quin.base.activity.ActivityInject;
import com.schrondinger.quin.base.activity.BaseActivity;

import butterknife.BindView;

@ActivityInject(rootViewId = R.layout.activity_protocol)
public class ProtocolActivity extends BaseActivity {
    @BindView(R.id.view_toolbar)
    Toolbar mToolBar;
    @BindView(R.id.rl_toolbar)
    RelativeLayout mRl_toolBar;
    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.tv_title)
    TextView mTv_title;
    @BindView(R.id.mProgressBar)
    ProgressBar mProgressBar;

    private String url="";
    private String title="";

    @Override
    public void initView() {
        super.initView();
        setToolBar(mToolBar);
    }

    @SuppressLint("JavascriptInterface")
    @Override
    public void initLoad() {
        super.initLoad();
        // 配置webview
        title=getIntent().getStringExtra("title");
        url=getIntent().getStringExtra("url");
        mTv_title.setText(title);


        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);// 支持js
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.setWebViewClient(new myWebViewClient());

        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);

        settings.setBuiltInZoomControls(true);

        mWebView.addJavascriptInterface(new MyProtocol(),"shcrodinger");

        // 设置WebChromeClient
        mWebView.setWebChromeClient(new MyChromeClient());
        mWebView.loadUrl(url);
    }

    private  class MyProtocol {
        //Html调用此方法传递数据
        @JavascriptInterface
        public void function_name() {
            // 调用JS中的方法
            mWebView.post(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:loaddate('" + title + "')");
                }
            });
        }
    }

    private class myWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }


    }

    public class MyChromeClient extends android.webkit.WebChromeClient
    {
        @Override
        public void onProgressChanged(WebView view, int newProgress)
        {
            mProgressBar.setProgress(newProgress);
            if (newProgress == 100)
            {
                mProgressBar.setVisibility(View.GONE);
            }
            else
            {
                mProgressBar.setVisibility(View.VISIBLE);

            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title)
        {
            super.onReceivedTitle(view, title);
            // mTv_title.setText(title);
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
