package com.jason.hdxw.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jason.hdxw.R;
import com.jason.hdxw.base.BaseActivity;
import com.jason.hdxw.base.ClearEditText;
import com.jason.hdxw.bean.HomeEarningsBean;
import com.jason.hdxw.bean.KitingTypeBean;
import com.jason.hdxw.utils.UserCache;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jason.hdxw.api.API.BASICS_HOME_EARNINGS;
import static com.jason.hdxw.api.API.MEMBER_KITING;
import static com.jason.hdxw.api.API.MEMBER_KITING_TIME;
import static com.jason.hdxw.api.API.MEMBER_KITING_TYPE;

/**
 * 提现页面
 * created by wang on 2018/11/12
 */
public class KitingActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.iv_kiting_back)
    ImageView mIvKitingBack;
    @BindView(R.id.tv_kiting_detail)
    TextView mTvKitingDetail;
    @BindView(R.id.tv_kiting_time)
    TextView mTvKitingTime;
    @BindView(R.id.tv_kiting_balance)
    TextView mTvKitingBalance;
    @BindView(R.id.tv_kiting_banknum)
    TextView mTvKitingBanknum;
    @BindView(R.id.et_kiting_money)
    ClearEditText mEtKitingMoney;
    @BindView(R.id.et_kiting_pwd)
    ClearEditText mEtKitingPwd;
    @BindView(R.id.relative_kiting_bindcard)
    RelativeLayout mRelativeKitingBindcard;
    @BindView(R.id.btn_kiting_sure)
    Button mBtnKitingSure;
    @BindView(R.id.tv_kiting_explain)
    TextView mTvKitingExplain;

    private KitingTypeBean mKitingTypeBean;
    public HomeEarningsBean mEarningsBean;
    private Gson mGson = new Gson();
    private int kitingType = -1;
    private String selectItems[];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kiting);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true)
                .init();
        getBalance();
        getBankMsg();
        getKitingTime();
    }

    /**
     * 获取账户余额
     */
    private void getBalance() {
        OkGo.<String>post(BASICS_HOME_EARNINGS)
                .params("token", UserCache.getToken())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.e("提现收益接口返回数据：" + response.body());
                        try {
                            mEarningsBean = mGson.fromJson(response.body(), HomeEarningsBean.class);
                        } catch (Exception e) {
                            ToastUtils.show(getString(R.string.analysis_error));
                            e.printStackTrace();
                            return;
                        }
                        if (mEarningsBean.getStatus().equals("y")) {
                            if (mEarningsBean.getMoney().getUser_money() != null && mEarningsBean.getMoney().getUser_money().length() > 0) {
                                mTvKitingBalance.setText(mEarningsBean.getMoney().getUser_money());
                                //余额存到本地
                                UserCache.setBalance(mEarningsBean.getMoney().getUser_money());
                            }
                        } else {
                            ToastUtils.show(mEarningsBean.getMsg());
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
     * 获取提现时间说明
     */
    private void getKitingTime() {
        OkGo.<String>post(MEMBER_KITING_TIME)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.e("提现时间接口返回数据：" + response.body());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.getString("status").equals("y")) {
                                if (jsonObject.getString("tis") != null) {
                                    mTvKitingTime.setText(jsonObject.getString("tis"));
                                }
                                if (jsonObject.getString("xiamtis") != null) {
                                    mTvKitingExplain.setText(jsonObject.getString("xiamtis"));
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

    /**
     * 获取提现类型
     */
    private void getBankMsg() {
        OkGo.<String>post(MEMBER_KITING_TYPE)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.e("获取银行卡信息返回数据：" + response.body());
                        try {
                            mKitingTypeBean = mGson.fromJson(response.body(), KitingTypeBean.class);
                        } catch (Exception e) {
                            ToastUtils.show(getString(R.string.analysis_error));
                            e.printStackTrace();
                            return;
                        }
                        if (mKitingTypeBean.getStatus().equals("y")) {
                            if (mKitingTypeBean.getList() != null && mKitingTypeBean.getList().size() > 0) {
                                selectItems = new String[mKitingTypeBean.getList().size()];
                                for (int i = 0; i < mKitingTypeBean.getList().size(); i++) {
                                    selectItems[i] = mKitingTypeBean.getList().get(i).getName();
                                }
                                kitingType = 0;
                                mTvKitingBanknum.setText(mKitingTypeBean.getList().get(0).getName());
                            }
                        } else {
                            ToastUtils.show(mKitingTypeBean.getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.show(getString(R.string.network_error));
                    }
                });
    }

    @OnClick({R.id.iv_kiting_back, R.id.tv_kiting_detail, R.id.relative_kiting_bindcard, R.id.btn_kiting_sure})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //关闭
            case R.id.iv_kiting_back:
                finish();
                break;
            //提现明细
            case R.id.tv_kiting_detail:
                startActivity(KitingDetailsActivity.class);
                break;
            //选择提现方式
            case R.id.relative_kiting_bindcard:
                if (kitingType != -1) {
                    dialogChoice();
                } else {
                    ToastUtils.show("未获取提现方式");
                }
                break;
            //确认提现
            case R.id.btn_kiting_sure:
                if (kitingType != -1) {
                    kiting();
                } else {
                    ToastUtils.show("未获取提现方式");
                }
                break;
        }
    }

    /**
     * 选择提现类型
     */
    private void dialogChoice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, 3);
        builder.setTitle("选择提现方式");
        builder.setSingleChoiceItems(selectItems, kitingType,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Logger.e("选中：" + selectItems[which]);
                        kitingType = which;
                        mTvKitingBanknum.setText(mKitingTypeBean.getList().get(which).getName());
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    /**
     * 提现方法
     */
    private void kiting() {
        if (mEtKitingMoney.getText() == null || mEtKitingMoney.getText().toString().trim().length() == 0) {
            ToastUtils.show(getString(R.string.kiting_money));
            return;
        }
        if (mEtKitingPwd.getText() == null || mEtKitingPwd.getText().toString().trim().length() == 0) {
            ToastUtils.show(getString(R.string.kiting_pwd));
            return;
        }
        Logger.e("提现参数：token: " + UserCache.getToken() + "  price:  " + mEtKitingMoney.getText().toString().trim().length() + "  password" + mEtKitingPwd.getText().toString().trim().length() + "   提现方式：" + mKitingTypeBean.getList().get(kitingType).getType());
        OkGo.<String>post(MEMBER_KITING)
                .params("token", UserCache.getToken())
                .params("price", mEtKitingMoney.getText().toString().trim())
                .params("password", mEtKitingPwd.getText().toString().trim())
                .params("type", mKitingTypeBean.getList().get(kitingType).getType())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.e("立即提现接口返回数据：" + response.body());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.getString("status").equals("y")) {
                                if (jsonObject.getString("msg") != null) {
                                    ToastUtils.show(jsonObject.getString("msg"));
                                } else {
                                    ToastUtils.show(getString(R.string.hint_kiting_succeed));
                                }
                                getBalance();
                                mEtKitingMoney.setText("");
                                mEtKitingPwd.setText("");
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
