package com.jason.hdxw.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjq.toast.ToastUtils;
import com.jason.hdxw.R;
import com.jason.hdxw.base.ClearEditText;
import com.jason.hdxw.base.WhiteBarActivity;
import com.jason.hdxw.utils.OTPSendUtil;
import com.jason.hdxw.utils.Strings;
import com.jason.hdxw.utils.UserCache;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jason.hdxw.api.API.MEMBER_CHANGE_LOGIN;


/**
 * 修改登录密码
 * created by wang on 2018/11/15
 */
public class ChangeLoginPasswordActivity extends WhiteBarActivity implements View.OnClickListener {
    @BindView(R.id.iv_changeloginorpay_back)
    ImageView mIvChangeloginorpayBack;
    @BindView(R.id.tv_changeloginorpay_title)
    TextView mTvChangeloginorpayTitle;
    @BindView(R.id.et_changeloginorpay_newpwd)
    ClearEditText mEtChangeloginorpayNewpwd;
    @BindView(R.id.tv_changeloginorpay_sure)
    TextView mTvChangeloginorpaySure;
    @BindView(R.id.et_changeloginorpay_newpwd2)
    ClearEditText mEtChangeloginorpayNewpwd2;
    @BindView(R.id.et_otp)
    EditText mEtOtp;
    @BindView(R.id.btn_otp)
    Button mBtnOtp;
    @BindView(R.id.container_otp)
    View containerOtp;

    private JSONObject jsonObject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeloginpassword);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
    }

    private void initView() {
            mTvChangeloginorpayTitle.setText(getString(R.string.changepwd_login));
            mEtChangeloginorpayNewpwd.setHint(getString(R.string.changepwd_login_new));
            mEtChangeloginorpayNewpwd2.setHint(getString(R.string.changepwd_login_new2));
    }

    @OnClick({R.id.iv_changeloginorpay_back, R.id.tv_changeloginorpay_sure,R.id.btn_otp})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //关闭
            case R.id.iv_changeloginorpay_back:
                finish();
                break;
            //确认修改
            case R.id.tv_changeloginorpay_sure:
                changePwd();
                break;
            //发送验证码
            case R.id.btn_otp:
                sendOTP();
                break;
        }
    }

    /**
     * 发送验证码
     */
    private void sendOTP(){
        OTPSendUtil.send(this,"edit_login_pass");
        mBtnOtp.setEnabled(false);
        new Runnable() {
            int leftSeconds=60;
            @Override
            public void run() {
                mBtnOtp.setText(leftSeconds+"s后可再次发送");
                leftSeconds--;
                if(leftSeconds>0) {
                    postDelayed(this, 1000);
                }else{
                    mBtnOtp.setText(R.string.changepwd_otp_btn_txt);
                    mBtnOtp.setEnabled(true);
                }
            }
        }.run();

    }

    /**
     * 修改密码
     */
    private void changePwd() {

        String newPassword=Strings.trim(mEtChangeloginorpayNewpwd.getText());
        if (newPassword==null) {
            ToastUtils.show(getString(R.string.changepwd_newpwd));
            return;
        }
        String newPasswordConfirm=Strings.trim(mEtChangeloginorpayNewpwd2.getText());
        if (newPasswordConfirm==null) {
            ToastUtils.show(getString(R.string.changepwd_newpwd2));
            return;
        }
            String otp=Strings.trim(mEtOtp.getText());
            if (otp==null) {
                ToastUtils.show(getString(R.string.changepwd_otp_hint));
                return;
            }
            OkGo.<String>post(MEMBER_CHANGE_LOGIN)
                    .params("token", UserCache.getToken())
                   // .params("yuan_password",oldPassword)
                    .params("new_password", newPassword)
                    .params("zainew_password",newPasswordConfirm)
                    .params("code",otp)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            Logger.e("修改登录密码接口返回数据：" + response.body());
                            try {
                                jsonObject = new JSONObject(response.body());
                                if (jsonObject.getString("status").equals("y")) {
                                    finish();
                                    if (jsonObject.getString("msg") != null) {
                                        ToastUtils.show(jsonObject.getString("msg"));
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
}
