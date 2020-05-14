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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.jason.hdxw.R;
import com.jason.hdxw.base.ClearEditText;
import com.jason.hdxw.base.WhiteBarActivity;
import com.jason.hdxw.bean.ZfbMsgBean;
import com.jason.hdxw.utils.OTPSendUtil;
import com.jason.hdxw.utils.Strings;
import com.jason.hdxw.utils.UserCache;
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

import static com.jason.hdxw.api.API.MEMBER_ZFB_MSG;
import static com.jason.hdxw.api.API.MEMBER_ZFB_SEND;
import static com.jason.hdxw.api.API.MEMBER_ZFB_UPDATA;


/**
 * 绑定支付宝页面
 * created by wang on 2018/11/15
 */
public class BindAlipayActivity extends WhiteBarActivity implements View.OnClickListener {

    @BindView(R.id.iv_bind_back)
    ImageView mIvBindBack;
    @BindView(R.id.et_bind_num)
    ClearEditText mEtBindNum;
    @BindView(R.id.iv_bind_show)
    ImageView mIvBindShow;
    @BindView(R.id.iv_bind_add)
    ImageButton mIvBindAdd;
    @BindView(R.id.tv_bind_sure)
    Button mTvBindSure;
    @BindView(R.id.et_bind_name)
    ClearEditText mEtBindName;
    @BindView(R.id.et_otp)
    EditText mEtOtp;
    @BindView(R.id.btn_otp)
    Button mBtnOtp;
    //之前的收款码图片        ;
    private String mServerImagePath;
    //刚选择的收款码图片
    private String mSelectedImagePath;

    private Gson mGson = new Gson();

    private ZfbMsgBean mZfbMsgBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_alipay);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        getBindMsg();
    }

    /**
     * 获取支付宝信息
     */
    private void getBindMsg() {

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
                            ZfbMsgBean.AlipayBean info = mZfbMsgBean.getAlipay();
                            if (info == null) {
                                return;
                            }

                            mEtBindName.setText(info.getAlipay_name());
                            mEtBindNum.setText(info.getAlipay());
                            mServerImagePath = Strings.trim(info.getAlipay_qr());
                            if (mServerImagePath != null) {
                                Picasso.get()
                                        .load(mServerImagePath)
                                        .error(R.mipmap.icon)
                                        .fit()
                                        .centerCrop()
                                        .into(mIvBindShow);
                            }
                        } else {
                            ToastUtils.show(mZfbMsgBean.getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.show(getString(R.string.network_error));
                    }
                });
    }

    @OnClick({R.id.iv_bind_back, R.id.iv_bind_add, R.id.tv_bind_sure, R.id.btn_otp})
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
                handleConfirmBtnClick();
                break;
        }
    }

    /**
     * 发送验证码
     */
    private void sendOTP() {
        OTPSendUtil.send(this, "edit_alipay");
        mBtnOtp.setEnabled(false);
        new Runnable() {
            int leftSeconds = 60;

            @Override
            public void run() {
                mBtnOtp.setText(leftSeconds + "s后可再次发送");
                leftSeconds--;
                if (leftSeconds > 0) {
                    postDelayed(this, 1000);
                } else {
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
            mSelectedImagePath = imagePath;
            Bitmap bm = BitmapFactory.decodeFile(mSelectedImagePath);
            mIvBindShow.setImageBitmap(bm);
        }
    }

    private void handleImageBeforeKitkat(Intent data) {
        if (data != null) {
            Uri uri = data.getData();
            String imagePath = getImagePath(uri, null);
            mSelectedImagePath = imagePath;
            Bitmap bm = BitmapFactory.decodeFile(mSelectedImagePath);
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


    private void uploadSelectedImage(final Runnable next) {
        OkGo.<String>post(MEMBER_ZFB_SEND)
                .params("img_url", new File(mSelectedImagePath))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.getString("status").equals("y")) {
                                //更新服务端图片路径
                                mSelectedImagePath=jsonObject.getString("zhifubao");
                                //完成后续任务
                                next.run();
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

    private void saveModify() {
        OkGo.<String>post(MEMBER_ZFB_UPDATA)
                .params("token", UserCache.getToken())
                .params("alipay", Strings.trim(mEtBindNum.getText()))
                .params("alipay_qr", mServerImagePath)
                .params("alipay_name", Strings.trim(mEtBindName.getText()))
                .params("code", Strings.trim(mEtOtp.getText()))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.e("更新支付宝信息接口返回数据：" + response.body());
                        try {
                            JSONObject resp = new JSONObject(response.body());
                            if (resp.getString("status").equals("y")) {
                                if (resp.getString("msg") != null) {
                                    ToastUtils.show(resp.getString("msg"));
                                }
                                finish();
                            } else {
                                ToastUtils.show(resp.getString("msg"));
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
     * 处理确定绑定按钮点击
     */
    private void handleConfirmBtnClick() {
        if (mEtBindNum.getText() == null || mEtBindNum.getText().toString().trim().length() == 0) {
            ToastUtils.show("请输入支付宝账号");
            return;
        }
        if (mEtBindName.getText() == null || mEtBindName.getText().toString().trim().length() == 0) {
            ToastUtils.show("请输入收款人姓名");
            return;
        }
        String otp = Strings.trim(mEtOtp.getText());
        if (otp == null) {
            ToastUtils.show(getString(R.string.changepwd_otp_hint));
            return;
        }
        //之前没有，也没有选择
        if (mSelectedImagePath == null && mServerImagePath == null) {
            ToastUtils.show("请选择收款码图片");
            return;
        }
        //选择了图片
        if (mSelectedImagePath != null) {
            uploadSelectedImage(new Runnable() {
                @Override
                public void run() {
                    saveModify();
                }
            });
            return;
        } else {
            //之前的图片，更新其他数据
            saveModify();
        }

    }
}
