package com.jason.hdxw.ui.activity;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.jason.hdxw.R;
import com.jason.hdxw.base.BaseActivity;
import com.jason.hdxw.base.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jason.hdxw.api.API.BASICS_IMG_VERIFY;
import static com.jason.hdxw.api.API.BASICS_REGISTER;

/**
 * 注册页
 * created by wang on 2018/11/10
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.et_register_account)
    ClearEditText mEtRegisterAccount;
    @BindView(R.id.et_register_phone)
    ClearEditText mEtRegisterPhone;
    @BindView(R.id.et_register_name)
    ClearEditText mEtRegisterName;
    @BindView(R.id.et_register_verifyimg)
    ClearEditText mEtRegisterVerifyimg;
    @BindView(R.id.et_register_activate)
    ClearEditText mEtRegisterActivate;
    @BindView(R.id.et_register_recommend)
    ClearEditText mEtRegisterRecommend;
    @BindView(R.id.btn_register_over)
    Button mBtnRegisterOver;
    @BindView(R.id.tv_register_agreement)
    TextView mTvRegisterAgreement;
    @BindView(R.id.iv_register_back)
    ImageView mIvRegisterBack;
    @BindView(R.id.et_register_pwd1)
    ClearEditText mEtRegisterPwd1;
    @BindView(R.id.et_register_pwd2)
    ClearEditText mEtRegisterPwd2;
    @BindView(R.id.web_register_verifyimg)
    WebView mWebRegisterVerifyimg;
    @BindView(R.id.linear_register_verifyimg)
    LinearLayout mLinearRegisterVerifyimg;
    @BindView(R.id.et_register_kitingpwd)
    ClearEditText mEtRegisterKitingpwd;
    @BindView(R.id.et_register_kitingpwd2)
    ClearEditText mEtRegisterKitingpwd2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true)
                .init();
        initWeb();
    }

    private void initWeb() {
        //初始化webview
        mWebRegisterVerifyimg.setWebViewClient(new WebViewClient() {
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
        WebSettings settings = mWebRegisterVerifyimg.getSettings();
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
        mWebRegisterVerifyimg.setWebChromeClient(new WebChromeClient());
        //开启硬件加速
        mWebRegisterVerifyimg.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        mWebRegisterVerifyimg.loadUrl(BASICS_IMG_VERIFY);
    }


    @OnClick({R.id.iv_register_back, R.id.btn_register_over, R.id.tv_register_agreement, R.id.linear_register_verifyimg})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //关闭
            case R.id.iv_register_back:
                finish();
                break;
            //刷新验证码
            case R.id.linear_register_verifyimg:
                mWebRegisterVerifyimg.loadUrl(BASICS_IMG_VERIFY);
                break;
            //完成
            case R.id.btn_register_over:
                register();
                break;
            //使用协议
            case R.id.tv_register_agreement:
                startActivity(RegisterAgreementActivity.class);
                break;
        }
    }

    /**
     * 注册
     */
    private void register() {
        if (mEtRegisterAccount.getText() == null || mEtRegisterAccount.getText().toString().trim().length() == 0) {
            ToastUtils.show(getString(R.string.hint_account));
            return;
        }
        if (isNumeric(mEtRegisterAccount.getText().toString().trim())) {
            ToastUtils.show(getString(R.string.hint_nonumber));
            return;
        }
        if (isChar(mEtRegisterAccount.getText().toString().trim())) {
            ToastUtils.show(getString(R.string.hint_nochar));
            return;
        }
        if (mEtRegisterPhone.getText() == null || mEtRegisterPhone.getText().toString().trim().length() == 0) {
            ToastUtils.show(getString(R.string.hint_phone));
            return;
        }
        if (mEtRegisterName.getText() == null || mEtRegisterName.getText().toString().trim().length() == 0) {
            ToastUtils.show(getString(R.string.hint_name));
            return;
        }
//        if (mEtRegisterVerifyimg.getText() == null || mEtRegisterVerifyimg.getText().toString().trim().length() == 0) {
//            ToastUtils.show(getString(R.string.hint_img_verify));
//            return;
//        }
        if (mEtRegisterPwd1.getText() == null || mEtRegisterPwd1.getText().toString().trim().length() == 0) {
            ToastUtils.show(getString(R.string.hint_pwd));
            return;
        }
        if (mEtRegisterPwd2.getText() == null || mEtRegisterPwd2.getText().toString().trim().length() == 0) {
            ToastUtils.show(getString(R.string.hint_pwd_too));
            return;
        }
        if (mEtRegisterKitingpwd.getText() == null || mEtRegisterKitingpwd.getText().toString().trim().length() == 0) {
            ToastUtils.show(getString(R.string.hint_kitingpwd));
            return;
        }
        if (mEtRegisterKitingpwd2.getText() == null || mEtRegisterKitingpwd2.getText().toString().trim().length() == 0) {
            ToastUtils.show(getString(R.string.hint_kitingpwd_too));
            return;
        }
        if (mEtRegisterActivate.getText() == null || mEtRegisterActivate.getText().toString().trim().length() == 0) {
            ToastUtils.show(getString(R.string.hint_activate_num));
            return;
        }
        if (mEtRegisterRecommend.getText() == null || mEtRegisterRecommend.getText().toString().trim().length() == 0) {
            ToastUtils.show(getString(R.string.hint_recommend_num));
            return;
        }
        OkGo.<String>post(BASICS_REGISTER)
                .params("username", mEtRegisterAccount.getText().toString().trim())
                .params("mobile_phone", mEtRegisterPhone.getText().toString().trim())
                .params("user_nikn", mEtRegisterName.getText().toString().trim())
//                .params("imgcode", mEtRegisterVerifyimg.getText().toString().trim())
                .params("password", mEtRegisterPwd1.getText().toString().trim())
                .params("firmpawd", mEtRegisterPwd2.getText().toString().trim())
                .params("zhipass", mEtRegisterKitingpwd.getText().toString().trim())
                .params("fzhipass", mEtRegisterKitingpwd2.getText().toString().trim())
                .params("code_key", mEtRegisterActivate.getText().toString().trim())
                .params("y_name", mEtRegisterRecommend.getText().toString().trim())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.e("注册账号接口返回信息：" + response.body());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.getString("status").equals("y")) {
                                if (jsonObject.getString("msg") != null) {
                                    ToastUtils.show(jsonObject.getString("msg"));
                                } else {
                                    ToastUtils.show(getString(R.string.hint_register_succeed));
                                }
                                finish();
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

    /**
     * 判断字符串是否为纯汉字
     *
     * @param name
     * @return
     */
    public boolean isChar(String name) {
        int n = 0;
        for (int i = 0; i < name.length(); i++) {
            n = (int) name.charAt(i);
            if (!(19968 <= n && n < 40869)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否为纯数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
