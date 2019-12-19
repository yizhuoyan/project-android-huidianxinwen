package com.jason.hdxw.base;

import java.util.List;

/**
 * 动态权限回调接口
 * created by wang on 2018/11/15
 */
public interface PermissionListener {

    //授权成功
    void onGranted();

    //授权部分
    void onGranted(List<String> grantedPermission);

    //拒绝授权
    void onDenied(List<String> deniedPermission);

    //拒绝授权但未点击不再提示
    void onReject();
}
