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
import static com.jason.hdxw.api.API.MEMBER_CHANGE_PAY;


/**
 * 修改支付密码
 * created by wang on 2018/11/15
 */
public class ChangePayPasswordActivity extends WhiteBarActivity implements View.OnClickListener {
    @BindView(R.id.iv_changeloginorpay_back)
    ImageView mIvChangeloginorpayBack;
    @BindView(R.id.tv_changeloginorpay_title)
    TextView mTvChangeloginorpayTitle;
    @BindView(R.id.et_changeloginorpay_newpwd)
    ClearEditText mEtChangeloginorpayNewpwd;
    @BindView(R.id.tv_changeloginorpay_sure)
    TextView mTvChangeloginorpaySure;
    @BindView(R.id.et_changeloginorpay_oldpwd)
    ClearEditText mEtChangeloginorpayOldpwd;
    @BindView(R.id.et_changeloginorpay_newpwd2)
    ClearEditText mEtChangeloginorpayNewpwd2;

    private JSONObject jsonObject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepaypassword);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
    }

    private void initView() {

        mTvChangeloginorpayTitle.setText(getString(R.string.changepwd_pay));
        mEtChangeloginorpayOldpwd.setHint(getString(R.string.changepwd_pay_old));
        mEtChangeloginorpayNewpwd.setHint(getString(R.string.changepwd_pay_new));
        mEtChangeloginorpayNewpwd2.setHint(getString(R.string.changepwd_pay_new2));
    }

    @OnClick({R.id.iv_changeloginorpay_back, R.id.tv_changeloginorpay_sure})
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
        }
    }



    /**
     * 修改密码
     */
    private void changePwd() {

        String newPassword = Strings.trim(mEtChangeloginorpayNewpwd.getText());
        if (newPassword == null) {
            ToastUtils.show(getString(R.string.changepwd_newpwd));
            return;
        }
        String newPasswordConfirm = Strings.trim(mEtChangeloginorpayNewpwd2.getText());
        if (newPasswordConfirm == null) {
            ToastUtils.show(getString(R.string.changepwd_newpwd2));
            return;
        }
        String oldPassword = Strings.trim(mEtChangeloginorpayOldpwd.getText());
        if (oldPassword == null) {
            ToastUtils.show(getString(R.string.changepwd_oldpwd));
            return;
        }


        OkGo.<String>post(MEMBER_CHANGE_PAY)
                .params("token", UserCache.getToken())
                .params("yuan_password", oldPassword)
                .params("new_password", newPassword)
                .params("zainew_password", newPasswordConfirm)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.e("修改提现密码接口返回数据：" + response.body());
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
