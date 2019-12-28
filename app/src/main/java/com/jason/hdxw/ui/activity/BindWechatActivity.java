package com.jason.hdxw.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jason.hdxw.R;
import com.jason.hdxw.base.ClearEditText;
import com.jason.hdxw.base.WhiteBarActivity;
import com.jason.hdxw.bean.WxMsgBean;
import com.jason.hdxw.bean.ZfbMsgBean;
import com.jason.hdxw.utils.OTPSendUtil;
import com.jason.hdxw.utils.Strings;
import com.jason.hdxw.utils.UserCache;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jason.hdxw.api.API.MEMBER_WX_MSG;
import static com.jason.hdxw.api.API.MEMBER_WX_SEND;
import static com.jason.hdxw.api.API.MEMBER_WX_UPDATA;
import static com.jason.hdxw.api.API.MEMBER_ZFB_MSG;
import static com.jason.hdxw.api.API.MEMBER_ZFB_SEND;
import static com.jason.hdxw.api.API.MEMBER_ZFB_UPDATA;


/**
 * 绑定微信页面(暂停使用)
 * created by wang on 2018/11/15
 */
@Deprecated
public class BindWechatActivity extends WhiteBarActivity implements View.OnClickListener {

    @BindView(R.id.iv_bind_back)
    ImageView mIvBindBack;
    @BindView(R.id.tv_bind_title)
    TextView mTvBindTitle;
    @BindView(R.id.et_bind_num)
    ClearEditText mEtBindNum;
    @BindView(R.id.iv_bind_show)
    ImageView mIvBindShow;
    @BindView(R.id.iv_bind_add)
    ImageView mIvBindAdd;
    @BindView(R.id.tv_bind_hint)
    TextView mTvBindHint;
    @BindView(R.id.tv_bind_sure)
    TextView mTvBindSure;
    @BindView(R.id.et_bind_name)
    ClearEditText mEtBindName;
    @BindView(R.id.et_otp)
    EditText mEtOtp;
    @BindView(R.id.btn_otp)
    Button mBtnOtp;
    @BindView(R.id.container_otp)
    View containerOtp;

    private String mImagePath;
    private Gson mGson = new Gson();
    private WxMsgBean mWxMsgBean;
    private ZfbMsgBean mZfbMsgBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_wechat);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        if (getIntent().getStringExtra("bindType").equals("wx")) {
            mTvBindTitle.setText(getString(R.string.setting_bind_wx));
            mEtBindNum.setHint(getString(R.string.setting_input_wx));
            mTvBindHint.setText(getString(R.string.setting_hint_wx));
            containerOtp.setVisibility(View.GONE);
        } else {
            mTvBindTitle.setText(getString(R.string.setting_bind_zfb));
            mEtBindNum.setHint(getString(R.string.setting_input_zfb));
            mTvBindHint.setText(getString(R.string.setting_hint_zfb));
            containerOtp.setVisibility(View.VISIBLE);
        }
        getBindMsg(getIntent().getStringExtra("bindType"));
    }

    /**
     * 获取支付宝或微信信息
     *
     * @param type
     */
    private void getBindMsg(String type) {
        if (type.equals("wx")) {
            OkGo.<String>post(MEMBER_WX_MSG)
                    .params("token", UserCache.getToken())
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            Logger.e("微信信息接口返回数据：" + response.body());
                            try {
                                mWxMsgBean = mGson.fromJson(response.body(), WxMsgBean.class);
                            } catch (Exception e) {
                                ToastUtils.show(getString(R.string.analysis_error));
                                e.printStackTrace();
                                return;
                            }
                            if (mWxMsgBean.getStatus().equals("y")) {
                                if (mWxMsgBean.getWeixin() != null && mWxMsgBean.getWeixin().getWeixn_name() != null) {
                                    mEtBindName.setText(mWxMsgBean.getWeixin().getWeixn_name());
                                }
                                if (mWxMsgBean.getWeixin() != null && mWxMsgBean.getWeixin().getWeixin() != null) {
                                    mEtBindNum.setText(mWxMsgBean.getWeixin().getWeixin());
                                    mEtBindNum.setSelection(mEtBindNum.getText().toString().trim().length());//将光标移至文字末尾
                                }
                                if (mWxMsgBean.getWeixin() != null && mWxMsgBean.getWeixin().getWeixin_qr() != null && mWxMsgBean.getWeixin().getWeixin_qr().length() > 0) {
                                    Picasso.get()
                                            .load(mWxMsgBean.getWeixin().getWeixin_qr())
                                            .error(R.mipmap.icon)
                                            .fit()
                                            .centerCrop()
                                            .into(mIvBindShow);
                                }
                            } else {
                                ToastUtils.show(mWxMsgBean.getMsg());
                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            ToastUtils.show(getString(R.string.network_error));
                        }
                    });
        } else {
            OkGo.<String>post(MEMBER_ZFB_MSG)
                    .params("token", UserCache.getToken())
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            Logger.e("支付宝信息接口返回数据：" + response.body());
                            try {
                                mZfbMsgBean = mGson.fromJson(response.body(), ZfbMsgBean.class);
                            } catch (Exception e) {
                                ToastUtils.show(getString(R.string.analysis_error));
                                e.printStackTrace();
                                return;
                            }
                            if (mZfbMsgBean.getStatus().equals("y")) {
                                if (mZfbMsgBean.getAlipay() != null && mZfbMsgBean.getAlipay().getAlipay_name() != null) {
                                    mEtBindName.setText(mZfbMsgBean.getAlipay().getAlipay_name());
                                }
                                if (mZfbMsgBean.getAlipay() != null && mZfbMsgBean.getAlipay().getAlipay() != null) {
                                    mEtBindNum.setText(mZfbMsgBean.getAlipay().getAlipay());
                                    mEtBindNum.setSelection(mEtBindNum.getText().toString().trim().length());//将光标移至文字末尾
                                }
                                if (mZfbMsgBean.getAlipay() != null && mZfbMsgBean.getAlipay().getAlipay_qr() != null && mZfbMsgBean.getAlipay().getAlipay_qr().length() > 0) {
                                    Picasso.get()
                                            .load(mZfbMsgBean.getAlipay().getAlipay_qr())
                                            .error(R.mipmap.icon)
                                            .fit()
                                            .centerCrop()
                                            .into(mIvBindShow);
                                }
                            } else {
                                ToastUtils.show(mWxMsgBean.getMsg());
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

    @OnClick({R.id.iv_bind_back, R.id.iv_bind_add, R.id.tv_bind_sure,R.id.btn_otp})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //关闭
            case R.id.iv_bind_back:
                finish();
                break;
            //发送验证码
            case R.id.btn_otp:
                sendOTP();
                break;
            //添加图片
            case R.id.iv_bind_add:
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    //打开系统相册
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    startActivityForResult(intent, 8);//打开系统相册
                }
                break;
            //确定
            case R.id.tv_bind_sure:
                if (getIntent().getStringExtra("bindType").equals("wx")) {
                    if (mImagePath == null && mWxMsgBean.getWeixin().getWeixin_url() == null) {
                        ToastUtils.show("请选择收款码");
                        return;
                    }
                } else {
                    if (mImagePath == null && mZfbMsgBean.getAlipay().getAlipay_url() == null) {
                        ToastUtils.show("请选择收款码");
                        return;
                    }
                }
                if (mEtBindNum.getText() == null || mEtBindNum.getText().toString().trim().length() == 0) {
                    ToastUtils.show("请输入收款账号");
                    return;
                }
                if (mEtBindName.getText() == null || mEtBindName.getText().toString().trim().length() == 0) {
                    ToastUtils.show("请输入收款人姓名");
                    return;
                }
                sendFile(mImagePath);
                break;
        }
    }
    /**
     * 发送验证码
     */
    private void sendOTP(){
        OTPSendUtil.send(this,"edit_alipay");
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 8) {
            if (Build.VERSION.SDK_INT >= 19) {
                handleImageOnKitkat(data);
            } else {
                handleImageBeforeKitkat(data);
            }
        }
    }

    @TargetApi(19)
    private void handleImageOnKitkat(Intent data) {
        if (data != null) {
            String imagePath = null;
            Uri uri = data.getData();
            if (DocumentsContract.isDocumentUri(this, uri)) {
                //如果是document类型的uri，则通过document id处理
                String docId = DocumentsContract.getDocumentId(uri);
                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String id = docId.split(":")[1];
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content:" +
                            "//downloads/public_downloads"), Long.valueOf(docId));
                    imagePath = getImagePath(contentUri, null);
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                //如果是content类型的uri，则使用普通方式处理
                imagePath = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                //如果是File类型的uri，直接获取图片路径即可
                imagePath = uri.getPath();
            }
            mImagePath = imagePath;
            Bitmap bm = BitmapFactory.decodeFile(mImagePath);
            mIvBindShow.setImageBitmap(bm);
        }
    }

    private void handleImageBeforeKitkat(Intent data) {
        if (data != null) {
            Uri uri = data.getData();
            String imagePath = getImagePath(uri, null);
            mImagePath = imagePath;
            Bitmap bm = BitmapFactory.decodeFile(mImagePath);
            mIvBindShow.setImageBitmap(bm);
        }
    }

    /**
     * 获取图片路径
     *
     * @param uri
     * @param selection
     * @return
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 上传文件
     *
     * @param imagePath
     */
    private void sendFile(String imagePath) {
        if (imagePath != null) {
            if (getIntent().getStringExtra("bindType").equals("wx")) {
                OkGo.<String>post(MEMBER_WX_SEND)
                        .params("img_url", new File(imagePath))
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.body());
                                    if (jsonObject.getString("status").equals("y")) {
                                        OkGo.<String>post(MEMBER_WX_UPDATA)
                                                .params("token", UserCache.getToken())
                                                .params("weixin", mEtBindNum.getText().toString().trim())
                                                .params("weixin_qr", jsonObject.getString("wx"))
                                                .params("weixn_name", mEtBindName.getText().toString().trim())
                                                .execute(new StringCallback() {
                                                    @Override
                                                    public void onSuccess(Response<String> response) {
                                                        Logger.e("更新微信信息接口返回数据：" + response.body());
                                                        try {
                                                            JSONObject jsonObject1 = new JSONObject(response.body());
                                                            if (jsonObject1.getString("status").equals("y")) {
                                                                if (jsonObject1.getString("msg") != null) {
                                                                    ToastUtils.show(jsonObject1.getString("msg"));
                                                                }
                                                                finish();
                                                            } else {
                                                                ToastUtils.show(jsonObject1.getString("msg"));
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
            } else {

                final String otp= Strings.trim(mEtOtp.getText());
                if (otp==null) {
                    ToastUtils.show(getString(R.string.changepwd_otp_hint));
                    return;
                }

                OkGo.<String>post(MEMBER_ZFB_SEND)
                        .params("img_url", new File(imagePath))
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.body());
                                    if (jsonObject.getString("status").equals("y")) {
                                        OkGo.<String>post(MEMBER_ZFB_UPDATA)
                                                .params("token", UserCache.getToken())
                                                .params("alipay", mEtBindNum.getText().toString().trim())
                                                .params("alipay_qr", jsonObject.getString("zhifubao"))
                                                .params("alipay_name", mEtBindName.getText().toString().trim())
                                                .params("code",otp)
                                                .execute(new StringCallback() {
                                                    @Override
                                                    public void onSuccess(Response<String> response) {
                                                        Logger.e("更新支付宝信息接口返回数据：" + response.body());
                                                        try {
                                                            JSONObject jsonObject1 = new JSONObject(response.body());
                                                            if (jsonObject1.getString("status").equals("y")) {
                                                                if (jsonObject1.getString("msg") != null) {
                                                                    ToastUtils.show(jsonObject1.getString("msg"));
                                                                }
                                                                finish();
                                                            } else {
                                                                ToastUtils.show(jsonObject1.getString("msg"));
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
        } else {
            if (mWxMsgBean != null && mWxMsgBean.getWeixin() != null && mWxMsgBean.getWeixin().getWeixin_url() != null && mWxMsgBean.getWeixin().getWeixin_url().length() > 0) {
                OkGo.<String>post(MEMBER_WX_UPDATA)
                        .params("token", UserCache.getToken())
                        .params("weixin", mEtBindNum.getText().toString().trim())
                        .params("weixin_qr", mWxMsgBean.getWeixin().getWeixin_url())
                        .params("weixn_name", mEtBindName.getText().toString().trim())
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                Logger.e("更新微信信息接口返回数据：" + response.body());
                                try {
                                    JSONObject jsonObject1 = new JSONObject(response.body());
                                    if (jsonObject1.getString("status").equals("y")) {
                                        if (jsonObject1.getString("msg") != null) {
                                            ToastUtils.show(jsonObject1.getString("msg"));
                                        }
                                        finish();
                                    } else {
                                        ToastUtils.show(jsonObject1.getString("msg"));
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
            } else if (mZfbMsgBean != null && mZfbMsgBean.getAlipay() != null && mZfbMsgBean.getAlipay().getAlipay_url() != null && mZfbMsgBean.getAlipay().getAlipay_url().length() > 0) {
                OkGo.<String>post(MEMBER_ZFB_UPDATA)
                        .params("token", UserCache.getToken())
                        .params("alipay", mEtBindNum.getText().toString().trim())
                        .params("alipay_qr", mZfbMsgBean.getAlipay().getAlipay_url())
                        .params("alipay_name", mEtBindName.getText().toString().trim())
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                Logger.e("更新支付宝信息接口返回数据：" + response.body());
                                try {
                                    JSONObject jsonObject1 = new JSONObject(response.body());
                                    if (jsonObject1.getString("status").equals("y")) {
                                        if (jsonObject1.getString("msg") != null) {
                                            ToastUtils.show(jsonObject1.getString("msg"));
                                        }
                                        finish();
                                    } else {
                                        ToastUtils.show(jsonObject1.getString("msg"));
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
            } else {
                ToastUtils.show(getString(R.string.hint_changeHead_Failure));
            }
        }
    }
}
