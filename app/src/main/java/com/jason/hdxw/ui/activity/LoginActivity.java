package com.jason.hdxw.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.jason.hdxw.R;
import com.jason.hdxw.base.ClearEditText;
import com.jason.hdxw.base.WhiteBarActivity;
import com.jason.hdxw.utils.UserCache;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jason.hdxw.api.API.BASICS_LOGIN;

/**
 * 登录页
 * created by wang on 2018/11/10
 */
public class LoginActivity extends WhiteBarActivity implements View.OnClickListener {

    @BindView(R.id.et_login_account)
    ClearEditText mEtLoginAccount;
    @BindView(R.id.et_login_pwd)
    ClearEditText mEtLoginPwd;
    @BindView(R.id.btn_login_login)
    Button mBtnLoginLogin;
    @BindView(R.id.tv_login_forgetpwd)
    TextView mTvLoginForgetpwd;
    @BindView(R.id.tv_login_register)
    TextView mTvLoginRegister;
    @BindView(R.id.iv_login_close)
    ImageView mIvLoginClose;

    //退出程序标识
    private boolean isQuit = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isQuit = false;
        }
    };

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (UserCache.getToken() != null && UserCache.getToken().length() > 0) {
            startActivity(MainActivity.class);
            finish();
        }
//        handler.sendEmptyMessageDelayed(0, 10000);
    }

    @OnClick({R.id.btn_login_login, R.id.tv_login_forgetpwd, R.id.tv_login_register, R.id.iv_login_close})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //登录
            case R.id.btn_login_login:
                login();
                break;
            //忘记密码
            case R.id.tv_login_forgetpwd:
                ToastUtils.show(getString(R.string.hint_service));
//                startActivity(ForgetPwdActivity1.class);
                break;
            //注册
            case R.id.tv_login_register:
                startActivity(RegisterActivity.class);
                break;
            //关闭
            case R.id.iv_login_close:
                finish();
                System.exit(0);
                break;
        }
    }

    /**
     * 登录
     */
    private void login() {
        if (mEtLoginAccount.getText() == null || mEtLoginAccount.getText().toString().trim().length() == 0) {
            ToastUtils.show(getText(R.string.hint_account));
            return;
        }
        if (mEtLoginPwd.getText() == null || mEtLoginPwd.getText().toString().trim().length() == 0) {
            ToastUtils.show(getText(R.string.hint_pwd));
            return;
        }
        OkGo.<String>post(BASICS_LOGIN)
                .params("username", mEtLoginAccount.getText().toString().trim())
                .params("password", mEtLoginPwd.getText().toString().trim())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.e("登录接口返回信息：" + response.body());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.getString("status").equals("y")) {
                                //保存 token
                                UserCache.setToken(jsonObject.getString("token"));
                                //保存商城跳转url
                                UserCache.setIndexShopURL(jsonObject.getString("index_url_shop"));
                                //保存商城入驻跳转url
                                UserCache.setIndexShopInURL(jsonObject.getString("index_url_shop_in"));
                                if (jsonObject.getString("msg") != null) {
                                    ToastUtils.show(jsonObject.getString("msg"));
                                }
                                startActivity(MainActivity.class);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (!isQuit) {
                isQuit = true;
                ToastUtils.show(getString(R.string.hint_exit));
                // 利用handler延迟发送更改状态信息
                mHandler.sendEmptyMessageDelayed(0, 1500);
            } else {
                finish();
                System.exit(0);
            }
        }
        return false;
    }
}
