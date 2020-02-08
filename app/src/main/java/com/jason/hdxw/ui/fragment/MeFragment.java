package com.jason.hdxw.ui.fragment;

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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.jason.hdxw.ui.activity.WebViewActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.jason.hdxw.R;
import com.jason.hdxw.base.UILazyFragment;
import com.jason.hdxw.bean.UserInfoBean;
import com.jason.hdxw.ui.activity.AboutActivity;
import com.jason.hdxw.ui.activity.ChangePwdActivity;
import com.jason.hdxw.ui.activity.IdeaActivity;
import com.jason.hdxw.ui.activity.LoginActivity;
import com.jason.hdxw.ui.activity.MeSettingActivity;
import com.jason.hdxw.ui.activity.VersionActivity;
import com.jason.hdxw.utils.UserCache;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.jason.hdxw.api.API.MEMBER_CHANGE_HEADIMG;
import static com.jason.hdxw.api.API.MEMBER_EXIT_APP;
import static com.jason.hdxw.api.API.MEMBER_USERINFO;

/**
 * 个人中心Fragment
 * created by wang on 2018/11/10
 */
public class MeFragment extends UILazyFragment implements View.OnClickListener {
    @BindView(R.id.iv_me_head)
    CircleImageView mIvMeHead;
    @BindView(R.id.tv_me_name)
    TextView mTvMeName;
    @BindView(R.id.tv_me_phone)
    TextView mTvMePhone;
    @BindView(R.id.tv_me_setting)
    TextView mTvMeSetting;
    @BindView(R.id.tv_me_changepwd)
    TextView mTvMeChangepwd;
    @BindView(R.id.tv_me_idea)
    TextView mTvMeIdea;
    @BindView(R.id.tv_me_about)
    TextView mTvMeAbout;
    @BindView(R.id.tv_me_version)
    TextView mTvMeVersion;
    @BindView(R.id.tv_me_exit)
    TextView mTvMeExit;
    Unbinder unbinder;
    @BindView(R.id.iv_me_setting)
    ImageView mIvMeSetting;

    private UserInfoBean mInfoBean;
    private Gson mGson = new Gson();


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected int getTitleBarId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
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
                                UserCache.setInviteNum(mInfoBean.getUser_find().getInvite_code());
                            }
                            if (mInfoBean.getUser_find().getUsername() != null) {
                                mTvMeName.setText(mInfoBean.getUser_find().getUsername());
                            }
                            if (mInfoBean.getUser_find().getMobile_phone() != null) {
                                mTvMePhone.setText(mInfoBean.getUser_find().getMobile_phone());
                            }
                            if (mInfoBean.getUser_find().getIco() != null) {
                                Picasso.get()
                                        .load(mInfoBean.getUser_find().getIco())
                                        .error(R.drawable.ico_head)
                                        .fit()
                                        .centerCrop()
                                        .into(mIvMeHead);
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

    /**
     * 是否启用沉浸式状态栏
     *
     * @return
     */
    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    /**
     * 状态栏暗色字体
     *
     * @return
     */
    @Override
    public boolean statusBarDarkFont() {
        return true;
    }

    @OnClick({R.id.tv_me_setting, R.id.tv_me_wallet, R.id.tv_me_changepwd, R.id.tv_me_idea, R.id.tv_me_about, R.id.tv_me_version, R.id.tv_me_exit,
            R.id.iv_me_setting, R.id.iv_me_head})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //个人设置
            case R.id.iv_me_setting:
                Intent intent = new Intent(getSupportActivity(), MeSettingActivity.class);
                intent.putExtra("userInfo", mInfoBean);
                startActivityForResult(intent, 55);
                break;
            //个人设置
            case R.id.tv_me_setting:
                Intent intent2 = new Intent(getSupportActivity(), MeSettingActivity.class);
                intent2.putExtra("userInfo", mInfoBean);
                startActivityForResult(intent2, 55);
                break;
            // 我的钱包
            case R.id.tv_me_wallet:
                Intent walletIntent = new Intent(getSupportActivity(), WebViewActivity.class);
                walletIntent.putExtra("title", "我的钱包");
                walletIntent.putExtra("url", UserCache.getWalletURL());
                startActivity(walletIntent);
                break;
            //修改密码
            case R.id.tv_me_changepwd:
                startActivity(ChangePwdActivity.class);
                break;
            //意见反馈
            case R.id.tv_me_idea:
                startActivity(IdeaActivity.class);
                break;
            //关于我们
            case R.id.tv_me_about:
                startActivity(AboutActivity.class);
                break;
            //版本信息
            case R.id.tv_me_version:
                startActivity(VersionActivity.class);
                break;
            //安全退出
            case R.id.tv_me_exit:
                inquiryApp();
                break;
            //换头像
            case R.id.iv_me_head:
                if (ContextCompat.checkSelfPermission(getSupportActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getSupportActivity(), new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    //打开系统相册
                    Intent intent8 = new Intent("android.intent.action.GET_CONTENT");
                    intent8.setType("image/*");
                    startActivityForResult(intent8, 8);//打开系统相册
                }
                break;
        }
    }

    /**
     * 确认是否退出登录
     */
    private void inquiryApp() {
        final AlertDialog dlg = new AlertDialog.Builder(getSupportActivity())
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
                        logoutApp();
                    }
                }).create();
        dlg.show();
    }

    /**
     * 退出登录
     */
    public void logoutApp() {
        OkGo.<String>post(MEMBER_EXIT_APP)
                .params("token", UserCache.getToken())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.e("退出程序接口返回数据：" + response.body());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.getString("status").equals("y")) {
                                if (jsonObject.getString("msg") != null) {
                                    ToastUtils.show(jsonObject.getString("msg"));
                                } else {
                                    ToastUtils.show(getString(R.string.hint_logout_succeed));
                                }
                                UserCache.clearData();
                                startActivity(LoginActivity.class);
                                getSupportActivity().finish();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 55) {
            initData();
        }
        if (requestCode == 55 && resultCode == 56) {
            logoutApp();
        }
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
            if (DocumentsContract.isDocumentUri(getSupportActivity(), uri)) {
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
        Cursor cursor = getSupportActivity().getContentResolver().query(uri, null, selection, null, null);
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
                                    initData();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}