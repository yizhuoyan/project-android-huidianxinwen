package com.jason.hdxw.ui.activity;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.jason.hdxw.R;
import com.jason.hdxw.adapter.NoviceAdapter;
import com.jason.hdxw.base.WhiteBarActivity;
import com.jason.hdxw.bean.NoviceCourseBean;
import com.jason.hdxw.utils.UserCache;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jason.hdxw.api.API.BASICS_NOVICE_COURSE;

/**
 * 新手教程页面
 * created by wang on 2018/11/14
 */
public class NoviceActivity extends WhiteBarActivity implements View.OnClickListener, NoviceAdapter.NoviceClick {
    @BindView(R.id.iv_novice_back)
    ImageView mIvNoviceBack;
    @BindView(R.id.recycler_novice)
    RecyclerView mRecyclerNovice;
    @BindView(R.id.web_novice)
    WebView mWebNovice;

    private NoviceAdapter mAdapter = new NoviceAdapter(this, new ArrayList<NoviceCourseBean.SelectBean>());
    private NoviceCourseBean mCourseBean;
    private Gson mGson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novice);
        ButterKnife.bind(this);
        initView();
//        initData();
        initWeb();
        initUrl();
    }

    private void initUrl() {
        OkGo.<String>post(BASICS_NOVICE_COURSE)
                .params("token", UserCache.getToken())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.e("新手教程返回数据：" + response.body());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.getString("status").equals("y")) {
                                if (jsonObject.getString("url") != null) {
                                    mWebNovice.loadUrl(jsonObject.getString("url"));
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
        //初始化webview
        mWebNovice.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            // 开始加载网页时要做的工作
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
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
        });
        //设置支持脚本
        WebSettings settings = mWebNovice.getSettings();
        //支持通过JS打开新窗口
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //支持自动加载图片
        settings.setLoadsImagesAutomatically(true);
        settings.setJavaScriptEnabled(true);
        // 需要开启webview开启DOM storage的问题
        settings.setDomStorageEnabled(true);
        // 设置允许访问文件数据
        settings.setAllowFileAccess(true);
        //关闭webview中缓存
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //设置支持各种不同的设备
//        settings.setUserAgentString("Mozilla/5.0 (iPad; U; CPU OS 3_2 like Mac OS X;en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B334bSafari/531.21.10");
        //TODO 解决不能弹窗的问题
        mWebNovice.setWebChromeClient(new WebChromeClient());
        //开启硬件加速
        mWebNovice.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        //交给js调用的类
        mWebNovice.addJavascriptInterface(new JavaScriptInterface(), "android");
    }

    private void initData() {
        OkGo.<String>post(BASICS_NOVICE_COURSE)
                .params("token", UserCache.getToken())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.e("新手教程接口返回数据：" + response.body());
                        try {
                            mCourseBean = mGson.fromJson(response.body(), NoviceCourseBean.class);
                        } catch (Exception e) {
                            ToastUtils.show(getString(R.string.analysis_error));
                            e.printStackTrace();
                            return;
                        }
                        if (mCourseBean.getStatus().equals("y")) {
                            if (mCourseBean.getSelect() != null && mCourseBean.getSelect().size() > 0) {
                                mAdapter.setList(mCourseBean.getSelect());
                            }
                        } else {
                            ToastUtils.show(mCourseBean.getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.show(getString(R.string.network_error));
                    }
                });
    }

    private void initView() {
        mRecyclerNovice.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerNovice.setAdapter(mAdapter);
        mAdapter.setClick(this);
    }

    @OnClick(R.id.iv_novice_back)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_novice_back:
                mWebNovice.loadUrl("javascript:fanhui()");
                break;
        }
    }

    @Override
    public void noviceClick(int i) {
        mCourseBean.getSelect().get(i).setSelect(!mCourseBean.getSelect().get(i).isSelect());
        mAdapter.setList(mCourseBean.getSelect());
    }

    /**
     * 返回键交由服务端处理
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            mWebNovice.loadUrl("javascript:fanhui()");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class JavaScriptInterface {
        @JavascriptInterface()
        public void close() {
            finish();
        }
    }
}
