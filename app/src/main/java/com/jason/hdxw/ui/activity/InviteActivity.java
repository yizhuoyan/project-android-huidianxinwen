package com.jason.hdxw.ui.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.jason.hdxw.R;
import com.jason.hdxw.base.TransparencyBarActivity;
import com.jason.hdxw.bean.UserInfoBean;
import com.jason.hdxw.utils.DensityUtil;
import com.jason.hdxw.utils.UserCache;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jason.hdxw.api.API.BASICS_QRCODE;
import static com.jason.hdxw.api.API.MEMBER_USERINFO;

/**
 * 邀请页面
 * created by wang on 2018/11/14
 */
public class InviteActivity extends TransparencyBarActivity implements View.OnClickListener {
    @BindView(R.id.iv_invite_back)
    ImageView mIvInviteBack;
    @BindView(R.id.linear_invite_top)
    LinearLayout mLinearInviteTop;
    @BindView(R.id.tv_invite_code)
    TextView mTvInviteCode;

    private UserInfoBean mInfoBean;
    private final Gson mGson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        ButterKnife.bind(this);
        initView();
        initData();

    }

    private void initData() {
        if (UserCache.getInviteNum() != null && UserCache.getInviteNum().length() > 0) {
            mTvInviteCode.setText(UserCache.getInviteNum());
        } else {
            OkGo.<String>post(MEMBER_USERINFO)
                    .params("token", UserCache.getToken())
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            Logger.e("个人信息接口返回数据：" + response.body());
                            try {
                                mInfoBean = mGson.fromJson(response.body(), UserInfoBean.class);
                            } catch (Exception e) {
                                ToastUtils.show(getString(R.string.analysis_error));
                                e.printStackTrace();
                                return;
                            }
                            if (mInfoBean.getStatus().equals("y")) {
                                if (mInfoBean.getUser_find().getInvite_code() != null && mInfoBean.getUser_find().getInvite_code().length() > 0) {
                                    mTvInviteCode.setText(mInfoBean.getUser_find().getInvite_code());
                                    UserCache.setInviteNum(mInfoBean.getUser_find().getInvite_code());
                                }

                            } else {
                                ToastUtils.show(mInfoBean.getMsg());
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


    private void initView() {
        int result = 0;
        int resourceId = getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mLinearInviteTop.getLayoutParams();
        lp.setMargins(DensityUtil.dip2px(this, 10), result, 0, 0);
        mLinearInviteTop.setLayoutParams(lp);
    }

    @OnClick({R.id.iv_invite_back, R.id.btn_copy_invite_code})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //关闭
            case R.id.iv_invite_back:
                finish();
                break;
            //复制邀请码
            case R.id.btn_copy_invite_code:
                if (mTvInviteCode.getText() != null) {
                    // 从API11开始android推荐使用android.content.ClipboardManager
                    // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(mTvInviteCode.getText());
                    ToastUtils.show(getString(R.string.invite_copy_tip_succeed));
                }
                break;
        }
    }

}
