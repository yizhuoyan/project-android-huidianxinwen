package com.jason.hdxw.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.jason.hdxw.R;
import com.jason.hdxw.base.WhiteBarActivity;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebViewActivity extends WhiteBarActivity {
    String title;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    String url;
    @BindView(R.id.webview)
    WebView webview;

    @SuppressLint({"JavascriptInterface"})
    private void initView() {
        this.webview.getSettings().setJavaScriptEnabled(true);
        this.webview.getSettings().setSupportZoom(true);
        this.webview.getSettings().setBuiltInZoomControls(true);
        this.webview.getSettings().setUseWideViewPort(true);
        this.webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        this.webview.getSettings().setLoadWithOverviewMode(true);
        this.webview.addJavascriptInterface(new MyObject(), "android");
        this.webview.loadUrl(this.url);
        this.webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView paramAnonymousWebView, String paramAnonymousString) {
                if ((paramAnonymousString.startsWith("http:")) || (paramAnonymousString.startsWith("https:"))) {
                    paramAnonymousWebView.loadUrl(paramAnonymousString);
                }
                return true;
            }
        });
    }

    @SuppressLint({"NewApi", "JavascriptInterface"})
    private void initWeb() {
        this.webview.canGoBack();
        this.webview.requestFocus();
        this.webview.getSettings().setJavaScriptEnabled(true);
        this.webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.webview.getSettings().setUseWideViewPort(true);
        this.webview.getSettings().setLoadWithOverviewMode(true);
        this.webview.getSettings().setAllowFileAccessFromFileURLs(true);
        this.webview.getSettings().setDefaultTextEncodingName("UTF-8");
        this.webview.getSettings().setDomStorageEnabled(true);
        this.webview.getSettings().setDatabaseEnabled(true);
        this.webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
        this.webview.getSettings().setAppCacheEnabled(true);
        this.webview.getSettings().setAllowFileAccess(true);
        this.webview.getSettings().setAllowContentAccess(true);
        this.webview.getSettings().setMediaPlaybackRequiresUserGesture(true);
        this.webview.getSettings().setSupportZoom(true);
        String str = getApplicationContext().getCacheDir().getAbsolutePath();
        this.webview.getSettings().setAppCachePath(str);
        this.webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView paramAnonymousWebView, String paramAnonymousString) {
                super.onPageFinished(paramAnonymousWebView, paramAnonymousString);
            }

            @Override
            public void onPageStarted(WebView paramAnonymousWebView, String paramAnonymousString, Bitmap paramAnonymousBitmap) {
                super.onPageStarted(paramAnonymousWebView, paramAnonymousString, paramAnonymousBitmap);
            }

            @Override
            public void onReceivedError(WebView paramAnonymousWebView, int paramAnonymousInt, String paramAnonymousString1, String paramAnonymousString2) {
                super.onReceivedError(paramAnonymousWebView, paramAnonymousInt, paramAnonymousString1, paramAnonymousString2);
            }

            @Override
            public void onReceivedError(WebView paramAnonymousWebView, WebResourceRequest paramAnonymousWebResourceRequest, WebResourceError paramAnonymousWebResourceError) {
                super.onReceivedError(paramAnonymousWebView, paramAnonymousWebResourceRequest, paramAnonymousWebResourceError);
            }

            @Override
            public void onReceivedSslError(WebView paramAnonymousWebView, SslErrorHandler paramAnonymousSslErrorHandler, SslError paramAnonymousSslError) {
                paramAnonymousSslErrorHandler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView paramAnonymousWebView, String paramAnonymousString) {
                paramAnonymousWebView.loadUrl(paramAnonymousString);
                return true;
            }
        });
        this.webview.setWebChromeClient(new WebChromeClient());
        this.webview.addJavascriptInterface(new MyObject(), "android");
        this.webview.loadUrl(this.url);
    }

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        this.title = getIntent().getStringExtra("title");
        this.url = getIntent().getStringExtra("url");
        this.tvTitle.setText(this.title);
        StringBuilder logs = new StringBuilder();
        logs.append("加载的链接为:");
        logs.append(this.url);
        Logger.e(logs.toString(), new Object[0]);
        initWeb();
    }

    @OnClick({R.id.iv_novice_back})
    public void onViewClicked() {
        finish();
    }

    public class MyObject {
        public MyObject() {
        }

        @JavascriptInterface
        public void close() {
            WebViewActivity.this.finish();
        }
    }
}
