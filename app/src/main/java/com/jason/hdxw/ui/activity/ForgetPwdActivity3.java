package com.jason.hdxw.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jason.hdxw.R;
import com.jason.hdxw.base.WhiteBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 找回密码3
 * created by wang on 2018/11/10
 */
public class ForgetPwdActivity3 extends WhiteBarActivity implements View.OnClickListener {
    @BindView(R.id.btn_forget3_sure)
    Button mBtnForget3Sure;
    @BindView(R.id.iv_forget3_back)
    ImageView mIvForget3Back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd3);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_forget3_sure, R.id.iv_forget3_back})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //关闭
            case R.id.iv_forget3_back:
                finish();
                break;
            //确定
            case R.id.btn_forget3_sure:

                break;
        }
    }
}
