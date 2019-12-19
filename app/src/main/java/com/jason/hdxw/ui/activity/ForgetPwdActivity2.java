package com.jason.hdxw.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jason.hdxw.R;
import com.jason.hdxw.base.ClearEditText;
import com.jason.hdxw.base.WhiteBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 找回密码2
 * created by wang on 2018/11/10
 */
public class ForgetPwdActivity2 extends WhiteBarActivity implements View.OnClickListener {
    @BindView(R.id.tv_forget2_phonenum)
    TextView mTvForget2Phonenum;
    @BindView(R.id.et_forget2_newpwd)
    ClearEditText mEtForget2Newpwd;
    @BindView(R.id.btn_forget2_sure)
    Button mBtnForget2Sure;
    @BindView(R.id.iv_forget2_back)
    ImageView mIvForget2Back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd2);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.btn_forget2_sure, R.id.iv_forget2_back})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //关闭
            case R.id.iv_forget2_back:
                finish();
                break;
            //确定
            case R.id.btn_forget2_sure:
                startActivity(ForgetPwdActivity3.class);
                break;
        }
    }
}
