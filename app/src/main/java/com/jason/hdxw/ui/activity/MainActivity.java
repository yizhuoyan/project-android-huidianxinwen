package com.jason.hdxw.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jason.hdxw.R;
import com.jason.hdxw.adapter.HomeFragmentAdapter;
import com.jason.hdxw.base.BaseActivity;
import com.jason.hdxw.base.NoScrollViewPager;
import com.jason.hdxw.bean.UpdataBean;
import com.jason.hdxw.ui.fragment.IndexFragment;
import com.jason.hdxw.ui.fragment.MeFragment;
import com.jason.hdxw.ui.fragment.ShopFragment;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jason.hdxw.api.API.BASICS_UPDATE;
import static com.jason.hdxw.api.API.MEMBER_EXAMINE_MONEY;

/**
 * 首页
 * created by wang on 2018/11/10
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.no_vp_main)
    NoScrollViewPager mNoVpMain;
    @BindView(R.id.iv_main_index)
    ImageView mIvMainIndex;
    @BindView(R.id.tv_main_index)
    TextView mTvMainIndex;
    @BindView(R.id.linear_main_index)
    LinearLayout mLinearMainIndex;
    @BindView(R.id.linear_main_shop)
    LinearLayout mLinearMainShop;
    @BindView(R.id.iv_main_me)
    ImageView mIvMainMe;
    @BindView(R.id.tv_main_me)
    TextView mTvMainMe;
    @BindView(R.id.linear_main_me)
    LinearLayout mLinearMainMe;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private UpdataBean mUpdataBean;
    private Gson mGson = new Gson();
    //退出程序标识
    private boolean isQuit = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isQuit = false;
        }
    };
    private IndexFragment mIndexFragment = new IndexFragment();
    private MeFragment mMeFragment = new MeFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //初始化状态栏工具 方便fragment调用
        ImmersionBar.with(this)
//                .statusBarColor(R.color.colorPrimary)
                .fitsSystemWindows(true)
//                .statusBarDarkFont(true)
                .init();
        initView();
    }

    private void initView() {
        //默认首页
        mNoVpMain.setCurrentItem(0);
        mFragments.add(mIndexFragment);
        mFragments.add(new ShopFragment());
        mFragments.add(mMeFragment);
        mNoVpMain.setAdapter(new HomeFragmentAdapter(getSupportFragmentManager(), mFragments));
    }

    @Override
    protected void onResume() {
        super.onResume();
        isUpdate();
    }

    /**
     * 升级接口
     */
    private void isUpdate() {
        OkGo.<String>post(BASICS_UPDATE)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.e("更新接口返回数据：" + response.body());
                        try {
                            mUpdataBean = mGson.fromJson(response.body(), UpdataBean.class);
                        } catch (Exception e) {
                            ToastUtils.show(getString(R.string.analysis_error));
                            e.printStackTrace();
                            return;
                        }
                        if (mUpdataBean.getStatus().equals("y")) {
                            try {
                                if (getLocalVersion(MainActivity.this) < Integer.valueOf(mUpdataBean.getList().getVarsionnum())) {
                                    final AlertDialog dlg = new AlertDialog.Builder(MainActivity.this)
                                            .setMessage(mUpdataBean.getList().getMessage())
                                            .setCancelable(false)
                                            .setNegativeButton(getString(R.string.hint_close), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                    finish();
                                                    System.exit(0);
                                                }
                                            })
                                            .setPositiveButton(getString(R.string.hint_loading), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Uri uri = Uri.parse(mUpdataBean.getList().getDown_url());
                                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                    startActivity(intent);
                                                }
                                            }).create();
                                    dlg.show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                ToastUtils.show(getString(R.string.hint_update));
                            }
                        } else {
                            ToastUtils.show(mUpdataBean.getMsg());
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 88) {
            mIndexFragment.getEarnings(null);
            mIndexFragment.getNoticeList();
        }
    }

    @Override
    protected void onDestroy() {
//        mNoVpMain.removeOnPageChangeListener(this);
        mNoVpMain.setAdapter(null);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (!isQuit) {
                isQuit = true;
                ToastUtils.show(getString(R.string.hint_exit));
                // 利用handler延迟发送更改状态信息
                mHandler.sendEmptyMessageDelayed(0, 1500);
            } else {
                finish();
                System.exit(0);
            }
        }
        return false;
    }

    /**
     * 获取本地软件版本号名称
     */
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * 获取本地软件版本号
     */
    public static int getLocalVersion(Context ctx) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    @SuppressLint("NewApi")
    @OnClick({R.id.linear_main_index, R.id.linear_main_shop, R.id.linear_main_me})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //首页
            case R.id.linear_main_index:
                mNoVpMain.setCurrentItem(0);
                mIvMainIndex.setImageDrawable(getDrawable(R.drawable.ico_home_active));
                mTvMainIndex.setTextColor(getResources().getColor(R.color.but_tintblue));

                mIvMainMe.setImageDrawable(getDrawable(R.drawable.ico_smile_face_normal));
                mTvMainMe.setTextColor(getResources().getColor(R.color.black));
                break;
            //开始赚钱
            case R.id.linear_main_shop:
                examineMoney();
                break;
            //个人中心
            case R.id.linear_main_me:
                mNoVpMain.setCurrentItem(2);
                mIvMainIndex.setImageDrawable(getDrawable(R.drawable.ico_home_normal));
                mTvMainIndex.setTextColor(getResources().getColor(R.color.black));

                mIvMainMe.setImageDrawable(getDrawable(R.drawable.ico_smile_face_active));
                mTvMainMe.setTextColor(getResources().getColor(R.color.but_tintblue));
                break;
        }
    }

    /**
     * 检查是否可以赚钱
     */
    private void examineMoney() {
        OkGo.<String>post(MEMBER_EXAMINE_MONEY)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.e("是否可以赚钱返回数据：" + response.body());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.getString("status").equals("y")) {
                                if (jsonObject.getString("checkinfo").equals("1")) {
                                    Intent intent1 = new Intent(MainActivity.this, JournalismActivity.class);
                                    startActivityForResult(intent1, 88);
                                } else {
                                    ToastUtils.show(jsonObject.getString("msg"));
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
}
