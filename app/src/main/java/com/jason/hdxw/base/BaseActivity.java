package com.jason.hdxw.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.jason.hdxw.R;
import com.jason.hdxw.utils.KeyboardUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * activity 基类
 * created by wang on 2018/11/10
 */
public class BaseActivity extends AppCompatActivity {
    private PermissionListener mlistener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //屏幕常亮标识
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //取消常亮
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //设置强制竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //设置强制横屏
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void finish() {
        // 隐藏软键盘，避免软键盘引发的内存泄露
        KeyboardUtils.hideKeyboard(getCurrentFocus());
        super.finish();
    }

    /**
     * 跳转到其他Activity
     *
     * @param cls 目标Activity的Class
     */
    public void startActivity(Class<? extends Activity> cls) {
        startActivity(new Intent(this, cls));
    }

    /**
     * 权限申请
     *
     * @param permissions 待申请的权限集合
     * @param listener    申请结果监听事件
     */
    protected void requestRunTimePermission(String[] permissions, PermissionListener listener) {
        this.mlistener = listener;

        //用于存放为授权的权限
        List<String> permissionList = new ArrayList<>();
        //遍历传递过来的权限集合
        for (String permission : permissions) {
            //判断是否已经授权
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                //未授权，则加入待授权的权限集合中
                permissionList.add(permission);
            }
        }

        //判断集合
        if (!permissionList.isEmpty()) {  //如果集合不为空，则需要去授权
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {  //为空，则已经全部授权
            listener.onGranted();
        }
    }

    /**
     * 权限申请结果
     *
     * @param requestCode  请求码
     * @param permissions  所有的权限集合
     * @param grantResults 授权结果集合
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    //被用户拒绝的权限集合
                    List<String> deniedPermissions = new ArrayList<>();
                    //用户通过的权限集合
                    List<String> grantedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        //获取授权结果，这是一个int类型的值
                        int grantResult = grantResults[i];

                        if (grantResult != PackageManager.PERMISSION_GRANTED) { //用户拒绝授权的权限
                            String permission = permissions[i];
                            deniedPermissions.add(permission);
                        } else {  //用户同意的权限
                            String permission = permissions[i];
                            grantedPermissions.add(permission);
                        }
                    }

                    if (deniedPermissions.isEmpty()) {  //用户拒绝权限为空
                        mlistener.onGranted();
                    } else {  //不为空
                        //回调授权成功的接口
                        mlistener.onDenied(deniedPermissions);
                        //回调授权失败的接口
                        mlistener.onGranted(grantedPermissions);
                    }
                    for (int i = 0; i < grantResults.length; ++i) {
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            //在用户已经拒绝授权的情况下，如果shouldShowRequestPermissionRationale返回false则
                            // 可以推断出用户选择了“不在提示”选项，在这种情况下需要引导用户至设置页手动授权
                            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                                new AlertDialog.Builder(BaseActivity.this).setTitle(getString(R.string.app_name))//设置对话框标题
                                        .setMessage("获取相关权限失败将导致部分功能无法正常使用，需要到设置页面手动授权")//设置显示的内容
                                        .setPositiveButton("去授权", new DialogInterface.OnClickListener() {//添加确定按钮
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                //TODO Auto-generated method stub
                                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                                                intent.setData(uri);
                                                startActivityForResult(intent, 77);
                                                dialog.dismiss();
                                            }
                                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {//响应事件
                                        // TODO Auto-generated method stub
                                        dialog.dismiss();
                                        finish();
                                    }
                                }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                    }
                                }).show();//在按键响应事件中显示此对话框
                            } else {
                                //用户拒绝权限请求，但未选中“不再提示”选项
                                mlistener.onReject();
                            }
                            break;
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 延迟执行某个任务
     *
     * @param action Runnable对象
     */
    public boolean post(Runnable action) {
        return getWindow().getDecorView().post(action);
    }

    /**
     * 延迟某个时间执行某个任务
     *
     * @param action      Runnable对象
     * @param delayMillis 延迟的时间
     */
    public boolean postDelayed(Runnable action, long delayMillis) {
        return getWindow().getDecorView().postDelayed(action, delayMillis);
    }

    /**
     * 删除某个延迟任务
     *
     * @param action Runnable对象
     */
    public boolean removeCallbacks(Runnable action) {
        if (getWindow().getDecorView() != null) {
            return getWindow().getDecorView().removeCallbacks(action);
        } else {
            return true;
        }
    }
}
