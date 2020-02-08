package com.jason.hdxw.utils;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.hjq.toast.ToastUtils;
import com.jason.hdxw.app.App;
import com.orhanobut.logger.Logger;

/**
 * 管理用户
 * Created by wang on 2018/8/14 0014
 */
public class UserCache {

    /**
     * 保存我的钱包连接
     * @param url
     */
    public static void setWalletURL(String url){
        url=Strings.trim(url);
        if(url!=null) {
            SPUtil.put(App.getApplication(), "mine_wallet", url);
        }
    }

    /**
     * 我的钱包网页连接
     * @return
     */
    public static String getWalletURL(){
        return (String) SPUtil.get(App.getApplication(), "mine_wallet", "");
    }

    /**
     * 保存商城跳转网页连接
     * @param url
     */
    public static void setIndexShopURL(String url){
        url=Strings.trim(url);
        if(url!=null) {
            SPUtil.put(App.getApplication(), "index_url_shop", url);
        }
    }

    /**
     * 商城跳转网页连接
     * @return
     */
    public static String getIndexShopURL(){
        return (String) SPUtil.get(App.getApplication(), "index_url_shop", "");
    }

    /**
     * 保存商城入驻跳转网页连接
     * @param url
     */
    public static void setIndexShopInURL(String url){
        url=Strings.trim(url);
        if(url!=null) {
            SPUtil.put(App.getApplication(), "index_url_shop_in", url);
        }
    }
    /**
     * 商城入驻跳转网页连接
     * @return
     */
    public static String getIndexShopInURL(){
        return (String) SPUtil.get(App.getApplication(), "index_url_shop_in", "");
    }
    /**
     * 设置用户token
     *
     * @param token
     */
    public static void setToken(String token) {
        SPUtil.put(App.getApplication(), "token", token);
    }

    /**
     * 获取用户Token
     *
     * @return
     */
    public static String getToken() {
        return (String) SPUtil.get(App.getApplication(), "token", "");
    }

    /**
     * 设置用户推荐码
     *
     * @param inviteNum
     */
    public static void setInviteNum(String inviteNum) {
        SPUtil.put(App.getApplication(), "inviteNum", inviteNum);
    }

    /**
     * 获取用户推荐码
     *
     * @return
     */
    public static String getInviteNum() {
        return (String) SPUtil.get(App.getApplication(), "inviteNum", "");
    }

    /**
     * 设置账户余额
     *
     * @param balance
     */
    public static void setBalance(String balance) {
        SPUtil.put(App.getApplication(), "balance", balance);
    }

    /**
     * 获取账户余额
     *
     * @return
     */
    public static String getBalance() {
        return (String) SPUtil.get(App.getApplication(), "balance", "");
    }


    /**
     * 设置第一次打开应用的标识
     *
     * @param ctx
     * @param id
     */
    public static void setFirstLlogin(Context ctx, String id) {
        SPUtil.put(ctx, "first_login", id);
    }

    /**
     * 获取第一次打开应用的标识
     *
     * @param ctx
     * @return
     */
    public static String getFirstLlogin(Context ctx) {
        return (String) SPUtil.get(ctx, "first_login", "");
    }

    /**
     * 获取设备号
     *
     * @return
     */
    public static String getMac() {
        return Settings.System.getString(App.getApplication().getContentResolver(), Settings.System.ANDROID_ID);
    }


    /**
     * 清除所有数据
     */
    public static void clearData() {
        SPUtil.put(App.getApplication(), "balance", "");
        SPUtil.put(App.getApplication(), "inviteNum", "");
        SPUtil.put(App.getApplication(), "token", "");
    }
}
