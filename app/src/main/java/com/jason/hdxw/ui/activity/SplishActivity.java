package com.jason.hdxw.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.jason.hdxw.R;
import com.jason.hdxw.base.PermissionListener;
import com.jason.hdxw.base.TransparencyBarActivity;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * 启动页
 * created by wang on 2018/11/22
 */
public class SplishActivity extends TransparencyBarActivity {
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splish);
        requestPermission();
    }

    /**
     * 请求权限
     */
    private void requestPermission() {
        //Manifest.permission.LOCATION_HARDWARE,android.Manifest.permission.CAMERA,Manifest.permission.CALL_PHONE,
        requestRunTimePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA}, new PermissionListener() {
            @Override
            public void onGranted() {
                Logger.e("所有权限授权成功");
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(LoginActivity.class);
                        finish();
                    }
                }, 1000);
            }

            @Override
            public void onGranted(List<String> grantedPermission) { //授权成功权限集合
                for (String s : grantedPermission) {
                    Logger.e(s + "权限请求成功-----");
                }
            }

            @Override
            public void onDenied(List<String> deniedPermission) { //授权失败权限集合
                for (String s : deniedPermission) {
                    Logger.e(s + "权限请求失败++++++");
                }

            }

            @Override
            public void onReject() {
                Logger.e("用户拒绝但为点击不再提示++++++");
                requestPermission();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 77) {
            requestPermission();
        }
    }
}
