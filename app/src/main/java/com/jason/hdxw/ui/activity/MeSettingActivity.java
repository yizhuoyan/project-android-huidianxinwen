package com.jason.hdxw.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jason.hdxw.R;
import com.jason.hdxw.base.WhiteBarActivity;
import com.jason.hdxw.bean.UserInfoBean;
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
import de.hdodenhof.circleimageview.CircleImageView;

import static com.jason.hdxw.api.API.MEMBER_CHANGE_HEADIMG;
import static com.jason.hdxw.api.API.MEMBER_USERINFO;

/**
 * 我的设置页面
 * created by wang on 2018/11/15
 */
public class MeSettingActivity extends WhiteBarActivity implements View.OnClickListener {
    @BindView(R.id.iv_mesetting_back)
    ImageView mIvMesettingBack;
    @BindView(R.id.iv_mesetting_head)
    CircleImageView mIvMesettingHead;
    @BindView(R.id.linear_mesetting_head)
    LinearLayout mLinearMesettingHead;
    @BindView(R.id.relative_mesetting_bankcard)
    RelativeLayout mRelativeMesettingBankcard;
    @BindView(R.id.tv_mesetting_exit)
    TextView mTvMesettingExit;
    @BindView(R.id.tv_mesetting_phone)
    TextView mTvMesettingPhone;
    @BindView(R.id.tv_mesetting_trueName)
    TextView mTvMesettingTrueName;
    @BindView(R.id.relative_mesetting_bind_wx)
    RelativeLayout mRelativeMesettingBindWx;
    @BindView(R.id.relative_mesetting_bind_zfb)
    RelativeLayout mRelativeMesettingBindZfb;

    private UserInfoBean mInfoBean;
    private Gson mGson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesetting);
        ButterKnife.bind(this);
        mInfoBean = (UserInfoBean) getIntent().getSerializableExtra("userInfo");
        initData();
    }

    private void initData() {
        if (mInfoBean != null) {
            if (mInfoBean.getUser_find().getIco() != null) {
                Picasso.get()
                        .load(mInfoBean.getUser_find().getIco())
                        .error(R.drawable.ico_head)
                        .fit()
                        .centerCrop()
                        .into(mIvMesettingHead);
            }
            if (mInfoBean.getUser_find().getTrue_name() != null) {
                mTvMesettingTrueName.setText(mInfoBean.getUser_find().getTrue_name());
            }
            if (mInfoBean.getUser_find().getMobile_phone() != null) {
                mTvMesettingPhone.setText(mInfoBean.getUser_find().getMobile_phone());
            }
        }
    }

    @OnClick({R.id.iv_mesetting_back, R.id.linear_mesetting_head, R.id.relative_mesetting_bankcard, R.id.tv_mesetting_exit,
            R.id.relative_mesetting_bind_wx, R.id.relative_mesetting_bind_zfb})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //关闭
            case R.id.iv_mesetting_back:
                finish();
                break;
            //换头像
            case R.id.linear_mesetting_head:
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
            //银行卡管理
            case R.id.relative_mesetting_bankcard:
                startActivity(BankCardManageActivity.class);
                break;
            //绑定微信
            case R.id.relative_mesetting_bind_wx:
                Intent intent_wx = new Intent(this, BindActivity.class);
                intent_wx.putExtra("bindType", "wx");
                startActivity(intent_wx);
                break;
            //绑定支付宝
            case R.id.relative_mesetting_bind_zfb:
                Intent intent_zfb = new Intent(this, BindActivity.class);
                intent_zfb.putExtra("bindType", "zfb");
                startActivity(intent_zfb);
                break;
            //退出登录
            case R.id.tv_mesetting_exit:
                final AlertDialog dlg = new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.hint_logout_app))
                        .setNegativeButton(getString(R.string.hint_logout_no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton(getString(R.string.hint_logout_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //给返回值 执行退出操作
                                //数据是使用Intent返回
                                Intent intent = new Intent();
                                setResult(56, intent);
                                finish();
                            }
                        }).create();
                dlg.show();
                break;
        }
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
            sendFile(imagePath);//上传图片
        }
    }

    private void handleImageBeforeKitkat(Intent data) {
        if (data != null) {
            Uri uri = data.getData();
            String imagePath = getImagePath(uri, null);
            sendFile(imagePath);
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
            OkGo.<String>post(MEMBER_CHANGE_HEADIMG)
                    .params("token", UserCache.getToken())
                    .params("img_url", new File(imagePath))
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            Logger.e("更换头像接口返回数据：" + response.body());
                            try {
                                JSONObject jsonObject = new JSONObject(response.body());
                                if (jsonObject.getString("status").equals("y")) {
                                    if (jsonObject.getString("msg") != null) {
                                        ToastUtils.show(jsonObject.getString("msg"));
                                    } else {
                                        ToastUtils.show(getString(R.string.hint_changeHead_Succeed));
                                    }
                                    //刷新数据
                                    refreshData();
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
            ToastUtils.show(getString(R.string.hint_changeHead_Failure));
        }
    }

    /**
     * 刷新个人数据
     */
    private void refreshData() {
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
                        initData();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.show(getString(R.string.network_error));
                    }
                });
    }
}
