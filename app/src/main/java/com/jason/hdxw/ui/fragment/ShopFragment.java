package com.jason.hdxw.ui.fragment;

import com.jason.hdxw.R;
import com.jason.hdxw.base.UILazyFragment;

/**
 * 商城Fragment
 * created by wang on 2018/11/10
 */
public class ShopFragment extends UILazyFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shop;
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
     * 是否开启状态栏暗色字体
     *
     * @return
     */
    @Override
    public boolean statusBarDarkFont() {
        return true;
    }
}