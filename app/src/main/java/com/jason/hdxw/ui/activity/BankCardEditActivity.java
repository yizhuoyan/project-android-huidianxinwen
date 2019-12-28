package com.jason.hdxw.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjq.toast.ToastUtils;
import com.jason.hdxw.utils.OTPSendUtil;
import com.jason.hdxw.utils.Strings;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.jason.hdxw.R;
import com.jason.hdxw.base.ClearEditText;
import com.jason.hdxw.base.WhiteBarActivity;
import com.jason.hdxw.bean.BankCardMsg;
import com.jason.hdxw.utils.UserCache;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jason.hdxw.api.API.MEMBER_BANK_ADD;

/**
 * 编辑银行卡页面
 * created by wang on 2018/11/22
 */
public class BankCardEditActivity extends WhiteBarActivity {
    @BindView(R.id.iv_bankcardedit_back)
    ImageView mIvBankcardeditBack;
    @BindView(R.id.et_bankcardedit_cardnum)
    ClearEditText mEtBankcardeditCardnum;
    @BindView(R.id.et_bankcardedit_cardtype)
    ClearEditText mEtBankcardeditCardtype;
    @BindView(R.id.et_bankcardedit_cardname)
    ClearEditText mEtBankcardeditCardname;
    @BindView(R.id.tv_bankcardedit_sure)
    TextView mTvBankcardeditSure;
    @BindView(R.id.et_otp)
    EditText mEtOtp;
    @BindView(R.id.btn_otp)
    Button mBtnOtp;

    private BankCardMsg mBankCardMsg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankcardedit);
        ButterKnife.bind(this);
        initData();
        initListener();
    }

    private void initListener() {
        mIvBankcardeditBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvBankcardeditSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBankCard();
            }
        });
        mBtnOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOTP();
            }
        });
    }

    private void initData() {
        mBankCardMsg = (BankCardMsg) getIntent().getSerializableExtra("cardMsg");
        if (mBankCardMsg.getBank().getBankaddress() != null) {
            mEtBankcardeditCardtype.setText(mBankCardMsg.getBank().getBankaddress());
        }
        if (mBankCardMsg.getBank().getBanknum() != null) {
            mEtBankcardeditCardnum.setText(mBankCardMsg.getBank().getBanknum());
        }
        if (mBankCardMsg.getBank().getIdcard() != null) {
            mEtBankcardeditCardname.setText(mBankCardMsg.getBank().getIdcard());
        }

    }
    private void sendOTP(){
        OTPSendUtil.send(this,"edit_bank_card");
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
     * 添加银行卡
     */
    private void addBankCard() {
        if (mEtBankcardeditCardnum.getText() == null || mEtBankcardeditCardnum.getText().toString().trim().length() == 0) {
            ToastUtils.show(getString(R.string.hint_bankcardNum));
            return;
        }
        if (mEtBankcardeditCardnum.getText().toString().trim().length() < 10) {
            ToastUtils.show(getString(R.string.hint_bankcardVerify));
            return;
        }
        if (mEtBankcardeditCardtype.getText() == null || mEtBankcardeditCardtype.getText().toString().trim().length() == 0) {
            ToastUtils.show(getString(R.string.hint_bankcardType));
            return;
        }
        if (mEtBankcardeditCardname.getText() == null || mEtBankcardeditCardname.getText().toString().trim().length() == 0) {
            ToastUtils.show(getString(R.string.hint_bankcardName));
            return;
        }
        String otp= Strings.trim(mEtOtp.getText());
        if(otp==null){
            ToastUtils.show(getString(R.string.changepwd_otp_hint));
            return;
        }
        OkGo.<String>post(MEMBER_BANK_ADD)
                .params("token", UserCache.getToken())
                .params("bankaddress", mEtBankcardeditCardtype.getText().toString().trim())
                .params("banknum", mEtBankcardeditCardnum.getText().toString().trim())
                .params("idcard", mEtBankcardeditCardname.getText().toString().trim())
                .params("code",otp)
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
