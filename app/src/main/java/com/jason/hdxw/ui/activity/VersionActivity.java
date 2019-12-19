package com.jason.hdxw.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.jason.hdxw.R;
import com.jason.hdxw.base.WhiteBarActivity;
import com.jason.hdxw.bean.VersionMsgBean;
import com.jason.hdxw.utils.UserCache;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jason.hdxw.api.API.BASICS_VERSION_MSG;

/**
 * 版本信息页面
 * created by wang on 2018/11/15
 */
public class VersionActivity extends WhiteBarActivity implements View.OnClickListener {

    @BindView(R.id.iv_version_back)
    ImageView mIvVersionBack;
    @BindView(R.id.tv_version_email)
    TextView mTvVersionEmail;
    @BindView(R.id.tv_version_phone)
    TextView mTvVersionPhone;
    @BindView(R.id.tv_version_code)
    TextView mTvVersionCode;

    private VersionMsgBean mVersionMsgBean;
    private Gson mGson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
//        mTvVersionCode.setText(getLocalVersionName(this));
        OkGo.<String>post(BASICS_VERSION_MSG)
                .params("token", UserCache.getToken())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.e("版本信息接口返回数据：" + response.body());
                        try {
                            mVersionMsgBean = mGson.fromJson(response.body(), VersionMsgBean.class);
                        } catch (Exception e) {
                            ToastUtils.show(getString(R.string.analysis_error));
                            e.printStackTrace();
                            return;
                        }

                        if (mVersionMsgBean.getStatus().equals("y")) {
                            mTvVersionEmail.setText(mVersionMsgBean.getAbout().getEmail());
                            mTvVersionPhone.setText(mVersionMsgBean.getAbout().getTel());
                            mTvVersionCode.setText(mVersionMsgBean.getAbout().getBanben());
                        } else {
                            ToastUtils.show(mVersionMsgBean.getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.show(getString(R.string.network_error));
                    }
                });
    }

    @OnClick(R.id.iv_version_back)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_version_back:
                finish();
                break;
        }
    }
}
