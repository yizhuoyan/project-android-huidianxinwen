package com.jason.hdxw.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jason.hdxw.R;
import com.jason.hdxw.base.ClearEditText;
import com.jason.hdxw.base.WhiteBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 找回密码1
 * created by wang on 2018/11/10
 */
public class ForgetPwdActivity1 extends WhiteBarActivity implements View.OnClickListener {
    @BindView(R.id.et_forget1_phone)
    ClearEditText mEtForget1Phone;
    @BindView(R.id.et_forget1_verifyimg)
    ClearEditText mEtForget1Verifyimg;
    @BindView(R.id.et_forget1_verifynum)
    ClearEditText mEtForget1Verifynum;
    @BindView(R.id.tv_forget1_getverifynum)
    TextView mTvForget1Getverifynum;
    @BindView(R.id.btn_forget1_next)
    Button mBtnForget1Next;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd1);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_forget1_next, R.id.tv_forget1_getverifynum, R.id.iv_forgetpwd1_back})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //关闭
            case R.id.iv_forgetpwd1_back:
                finish();
                break;
            //下一步
            case R.id.btn_forget1_next:
                startActivity(ForgetPwdActivity2.class);
                break;
            //获取验证码
            case R.id.tv_forget1_getverifynum:

                break;

        }
    }
}
