package com.jason.hdxw.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.jason.hdxw.R;
import com.jason.hdxw.base.WhiteBarActivity;
import com.jason.hdxw.utils.UserCache;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jason.hdxw.api.API.BASICS_JOURNALISM;
import static com.jason.hdxw.api.API.MEMBER_AD_MUSIC;

/**
 * 新闻页
 * created by wang on 2018/11/14
 */
public class JournalismActivity extends WhiteBarActivity implements View.OnClickListener {
    @BindView(R.id.iv_journalism_back)
    ImageView mIvJournalismBack;
    @BindView(R.id.web_journalism)
    WebView mWebJournalism;
    @BindView(R.id.refreshlayout_journalism)
    SmartRefreshLayout mRefreshlayoutJournalism;
    @BindView(R.id.iv_journalism_music)
    ImageView mIvJournalismMusic;

    private boolean isSelect = true;
    private String mRefreshUrl;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Logger.e("新闻延时任务执行刷新操作---------------------------------");
            if (mWebJournalism != null) {
                mWebJournalism.loadUrl(mRefreshUrl);
            }
            return true;
        }
    });


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journalism);
        ButterKnife.bind(this);
        initWeb();
        initData();
        initListener();
    }

    private void initListener() {
        //是否启用列表惯性滑动到底部时自动加载更多
        mRefreshlayoutJournalism.setEnableAutoLoadMore(false);
        mRefreshlayoutJournalism.setDisableContentWhenRefresh(false);//是否在刷新的时候禁止列表的操作
        mRefreshlayoutJournalism.setDisableContentWhenLoading(false);//是否在加载的时候禁止列表的操作
//        mRefreshlayoutJournalism.autoRefresh();//自动刷新
        mRefreshlayoutJournalism.setEnableRefresh(false);//是否启用下拉刷新功能
        mRefreshlayoutJournalism.setEnableLoadMore(false);//是否启用上拉加载功能
        mRefreshlayoutJournalism.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(1000);//传入false表示刷新失败
//                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                Logger.e("下拉刷新页面url：" + mRefreshUrl);
                mWebJournalism.loadUrl(mRefreshUrl);
                refreshlayout.finishRefresh(500);
            }
        });
    }

    private void initData() {
        OkGo.<String>post(BASICS_JOURNALISM)
                .params("token", UserCache.getToken())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.e("浏览新闻返回新闻地址：" + response.body());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.getString("status").equals("y")) {
                                mWebJournalism.loadUrl(jsonObject.getString("url"));
                            } else {
                                ToastUtils.show(jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            ToastUtils.show(getString(R.string.analysis_error));
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.show(getString(R.string.network_error));
                    }
                });
        adMusic();
    }

    /**
     * 检查音乐是否开启
     */
    private void adMusic() {
        OkGo.<String>post(MEMBER_AD_MUSIC)
                .params("token", UserCache.getToken())
                .execute(new StringCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.e("广告音乐是否开启接口：" + response.body());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.getString("status").equals("y")) {
                                if (jsonObject.getString("checkinfo").equals("1")) {
                                    isSelect = true;
                                    mIvJournalismMusic.setImageDrawable(getDrawable(R.drawable.s_yybf));
                                } else {
                                    isSelect = false;
                                    mIvJournalismMusic.setImageDrawable(getDrawable(R.drawable.s_yyzt));
                                }
                            } else {
                                ToastUtils.show(jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            ToastUtils.show(getString(R.string.analysis_error));
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.show(getString(R.string.network_error));
                    }
                });
    }

    private void initWeb() {
        mWebJournalism.setClickable(false);
        mWebJournalism.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        //初始化webview
        mWebJournalism.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            // 开始加载网页时要做的工作
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mHandler.removeMessages(0);
                mHandler.sendEmptyMessageDelayed(0, 60 * 3 * 1000);
                Logger.e("开始加载网页：" + url);
                if (!url.equals("file:///android_asset/error.html")) {
                    mRefreshUrl = url;
                    mRefreshlayoutJournalism.setEnableRefresh(false);
                    mWebJournalism.setClickable(false);
                    mWebJournalism.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return true;
                        }
                    });
                } else {
                    mRefreshlayoutJournalism.setEnableRefresh(true);
                    mWebJournalism.setClickable(false);
                    mWebJournalism.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });
                }
                super.onPageStarted(view, url, favicon);
            }

            // 加载完成时要做的工作
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();    //来忽略错误继续加载页面
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Logger.e("加载网页发生错误。。。。。");
                mWebJournalism.loadUrl("file:///android_asset/error.html");
                if (isNetworkConnected(JournalismActivity.this)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mWebJournalism.loadUrl(mRefreshUrl);
                        }
                    });
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Logger.e("加载网页发生错误。。。。。");
                mWebJournalism.loadUrl("file:///android_asset/error.html");
                if (isNetworkConnected(JournalismActivity.this)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mWebJournalism.loadUrl(mRefreshUrl);
                        }
                    });
                }
            }
        });
        //设置支持脚本
        WebSettings settings = mWebJournalism.getSettings();
        //支持通过JS打开新窗口
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //支持自动加载图片
        settings.setLoadsImagesAutomatically(true);
        settings.setJavaScriptEnabled(true);
        // 需要开启webview开启DOM storage的问题
        settings.setDomStorageEnabled(true);
        // 设置允许访问文件数据
        settings.setAllowFileAccess(true);
        //自动播放音乐
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.setMediaPlaybackRequiresUserGesture(false);
        }
        //关闭webview中缓存
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //提供给js调用方法
        mWebJournalism.addJavascriptInterface(new MyJavascriptInterface(), "android");
        //设置支持各种不同的设备
//        settings.setUserAgentString("Mozilla/5.0 (iPad; U; CPU OS 3_2 like Mac OS X;en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B334bSafari/531.21.10");

        //TODO 解决不能弹窗的问题
        mWebJournalism.setWebChromeClient(new WebChromeClient());
        //开启硬件加速
        mWebJournalism.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        mWebJournalism.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mWebJournalism.loadUrl("http://zilianbao.kunmao365.com/");
//            }
//        }, 500);
//        mWebJournalism.loadUrl("http://zilianbao.kunmao365.com/");
    }

    @SuppressLint("NewApi")
    @OnClick({R.id.iv_journalism_back, R.id.iv_journalism_music})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //关闭
            case R.id.iv_journalism_back:
                finish();
                break;
            //开启or关闭音乐
            case R.id.iv_journalism_music:
                isSelect = !isSelect;
                if (isSelect) {
                    mIvJournalismMusic.setImageDrawable(getDrawable(R.drawable.s_yybf));
                    mWebJournalism.evaluateJavascript("javascript:tishiyin('" + 1 + "')", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            //此处为 js 返回的结果
                        }
                    });
                } else {
                    mIvJournalismMusic.setImageDrawable(getDrawable(R.drawable.s_yyzt));
                    mWebJournalism.evaluateJavascript("javascript:tishiyin('" + 2 + "')", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            //此处为 js 返回的结果
                        }
                    });
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        mHandler.removeMessages(0);
        if (mWebJournalism != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            ViewParent parent = mWebJournalism.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWebJournalism);
            }
            mWebJournalism.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mWebJournalism.getSettings().setJavaScriptEnabled(false);
            mWebJournalism.clearHistory();
            mWebJournalism.clearView();
            mWebJournalism.removeAllViews();
            try {
                mWebJournalism.destroy();
            } catch (Throwable ex) {
            }
        }
        super.onDestroy();
    }

    public class MyJavascriptInterface {
        @JavascriptInterface
        public void refresh() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebJournalism.loadUrl(mRefreshUrl);
                    Logger.e("js刷新方法执行  刷新url：" + mRefreshUrl);
                }
            });
        }
    }

    /**
     * 判断网络状态
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            // 获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //判断NetworkInfo对象是否为空
            if (networkInfo != null)
                return networkInfo.isAvailable();
        }
        return false;
    }
}
