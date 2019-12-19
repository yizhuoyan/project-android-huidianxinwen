package com.jason.hdxw.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
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

import static com.jason.hdxw.api.API.MEMBER_BANK_ADD;

/**
 * 添加银行卡页面
 * created by wang on 2018/11/15
 */
public class BankCardAddActivity extends WhiteBarActivity implements View.OnClickListener {
    @BindView(R.id.iv_bankcardadd_back)
    ImageView mIvBankcardaddBack;
    @BindView(R.id.et_bancardadd_cardnum)
    ClearEditText mEtBancardaddCardnum;
    @BindView(R.id.et_bancardadd_cardtype)
    ClearEditText mEtBancardaddCardtype;
    @BindView(R.id.tv_bankcardadd_sure)
    TextView mTvBankcardaddSure;
    @BindView(R.id.et_bancardadd_cardname)
    ClearEditText mEtBancardaddCardname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankcardadd);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_bankcardadd_back, R.id.tv_bankcardadd_sure})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //关闭
            case R.id.iv_bankcardadd_back:
                finish();
                break;
            //确认添加
            case R.id.tv_bankcardadd_sure:
                addBankCard();
                break;
        }
    }

    /**
     * 添加银行卡
     */
    private void addBankCard() {
        if (mEtBancardaddCardnum.getText() == null || mEtBancardaddCardnum.getText().toString().trim().length() == 0) {
            ToastUtils.show(getString(R.string.hint_bankcardNum));
            return;
        }
        if (mEtBancardaddCardnum.getText().toString().trim().length() < 10) {
            ToastUtils.show(getString(R.string.hint_bankcardVerify));
            return;
        }
        if (mEtBancardaddCardtype.getText() == null || mEtBancardaddCardtype.getText().toString().trim().length() == 0) {
            ToastUtils.show(getString(R.string.hint_bankcardType));
            return;
        }
        if (mEtBancardaddCardname.getText() == null || mEtBancardaddCardname.getText().toString().trim().length() == 0) {
            ToastUtils.show(getString(R.string.hint_bankcardName));
            return;
        }
        OkGo.<String>post(MEMBER_BANK_ADD)
                .params("token", UserCache.getToken())
                .params("bankaddress", mEtBancardaddCardtype.getText().toString().trim())
                .params("banknum", mEtBancardaddCardnum.getText().toString().trim())
                .params("idcard", mEtBancardaddCardname.getText().toString().trim())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.e("添加银行卡返回数据：" + response.body());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.getString("status").equals("y")) {
                                if (jsonObject.getString("msg") != null) {
                                    ToastUtils.show(jsonObject.getString("msg"));
                                } else {
                                    ToastUtils.show(getString(R.string.hint_addbankSucceed));
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
}
