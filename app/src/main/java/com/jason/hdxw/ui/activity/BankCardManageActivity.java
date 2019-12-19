package com.jason.hdxw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.jason.hdxw.R;
import com.jason.hdxw.base.WhiteBarActivity;
import com.jason.hdxw.bean.BankCardMsg;
import com.jason.hdxw.bean.VersionMsgBean;
import com.jason.hdxw.utils.UserCache;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jason.hdxw.api.API.BASICS_VERSION_MSG;
import static com.jason.hdxw.api.API.MEMBER_BANK_MSG;

/**
 * 银行卡管理页面
 * created by wang on 2018/11/15
 */
public class BankCardManageActivity extends WhiteBarActivity implements View.OnClickListener {
    @BindView(R.id.iv_bankcardmanage_back)
    ImageView mIvBankcardmanageBack;
    @BindView(R.id.linear_bankcardmanage_yescard)
    LinearLayout mLinearBankcardmanageYescard;
    @BindView(R.id.tv_bankcardmanage_addcard)
    TextView mTvBankcardmanageAddcard;
    @BindView(R.id.linear_bankcardmanage_nocard)
    LinearLayout mLinearBankcardmanageNocard;
    @BindView(R.id.tv_bankcardmanage_cardname)
    TextView mTvBankcardmanageCardname;
    @BindView(R.id.tv_bankcardmanage_cardnum_front)
    TextView mTvBankcardmanageCardnumFront;
    @BindView(R.id.tv_bankcardmanage_cardnum_end)
    TextView mTvBankcardmanageCardnumEnd;
    @BindView(R.id.tv_bankcardmanage_hint)
    TextView mTvBankcardmanageHint;
    @BindView(R.id.linear_bankcardmanage_edit)
    LinearLayout mLinearBankcardmanageEdit;
    private BankCardMsg mCardMsg;
    private Gson mGson = new Gson();
    private VersionMsgBean mVersionMsgBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankcardmanage);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        OkGo.<String>post(MEMBER_BANK_MSG)
                .params("token", UserCache.getToken())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.e("获取银行卡信息返回数据：" + response.body());
                        try {
                            mCardMsg = mGson.fromJson(response.body(), BankCardMsg.class);
                        } catch (Exception e) {
                            ToastUtils.show(getString(R.string.analysis_error));
                            e.printStackTrace();
                            return;
                        }
                        if (mCardMsg.getBank().getBanknum() != null && mCardMsg.getBank().getBanknum().length() > 0) {
                            mLinearBankcardmanageYescard.setVisibility(View.VISIBLE);
                            mLinearBankcardmanageNocard.setVisibility(View.GONE);
                            mTvBankcardmanageCardname.setText(mCardMsg.getBank().getBankaddress());
                            mTvBankcardmanageCardnumFront.setText(mCardMsg.getBank().getBanknum().substring(0, 4) + "  ");
                            mTvBankcardmanageCardnumEnd.setText(mCardMsg.getBank().getBanknum().substring(mCardMsg.getBank().getBanknum().length() - 4,
                                    mCardMsg.getBank().getBanknum().length()));
                            getTel();
                        } else {
                            mLinearBankcardmanageYescard.setVisibility(View.GONE);
                            mLinearBankcardmanageNocard.setVisibility(View.VISIBLE);
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
     * 获取客服电话
     */
    private void getTel() {
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
                            mTvBankcardmanageHint.setText(getString(R.string.bankcard_hint) + mVersionMsgBean.getAbout().getTel());
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

    @OnClick({R.id.iv_bankcardmanage_back, R.id.tv_bankcardmanage_addcard, R.id.linear_bankcardmanage_edit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //关闭
            case R.id.iv_bankcardmanage_back:
                finish();
                break;
            //添加银行卡
            case R.id.tv_bankcardmanage_addcard:
                startActivityForResult(new Intent(this, BankCardAddActivity.class), 33);
                break;
            //编辑银行卡
            case R.id.linear_bankcardmanage_edit:
                Intent intent = new Intent(this, BankCardEditActivity.class);
                intent.putExtra("cardMsg", mCardMsg);
                startActivityForResult(intent, 33);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 33) {
            initData();
        }
    }
}
