package com.jason.hdxw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jason.hdxw.R;
import com.jason.hdxw.base.WhiteBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改密码页面
 * created by wang on 2018/11/15
 */
public class ChangePwdActivity extends WhiteBarActivity implements View.OnClickListener {

    @BindView(R.id.iv_changepwd_back)
    ImageView mIvChangepwdBack;
    @BindView(R.id.tv_changepwd_login)
    TextView mTvChangepwdLogin;
    @BindView(R.id.tv_changepwd_pay)
    TextView mTvChangepwdPay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepwd);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_changepwd_back, R.id.tv_changepwd_login, R.id.tv_changepwd_pay})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //关闭
            case R.id.iv_changepwd_back:
                finish();
                break;
            //修改登录密码
            case R.id.tv_changepwd_login:
                Intent intent_login = new Intent(this, ChangeLoginOrPayActivity.class);
                intent_login.putExtra("type", "login");
                startActivity(intent_login);
                break;
            //修改支付密码
            case R.id.tv_changepwd_pay:
                Intent intent_pay = new Intent(this, ChangeLoginOrPayActivity.class);
                intent_pay.putExtra("type", "pay");
                startActivity(intent_pay);
                break;
        }
    }
}
