package com.jason.hdxw.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.gyf.barlibrary.ImmersionBar;
import com.jason.hdxw.R;

/**
 * 透明状态栏基类
 * created by wang on 2018/11/10
 */
public class TransparencyBarActivity extends BaseActivity implements ViewTreeObserver.OnGlobalLayoutListener {
    private ImmersionBar mImmersionBar;//状态栏沉浸

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarConfig().init();
    }

    /**
     * 初始化沉浸式状态栏
     */
    private ImmersionBar statusBarConfig() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)
                .transparentStatusBar()
                .keyboardEnable(true)
//                .fitsSystemWindows(true)
                .statusBarDarkFont(false); //状态栏字体颜色为白色
//                .keyboardEnable(false, WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
//                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);  //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
        //必须设置View树布局变化监听，否则软键盘无法顶上去，还有模式必须是SOFT_INPUT_ADJUST_PAN
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(this);
        return mImmersionBar;
    }

    public static void marginTopStatusBarHeightForView(View view){
        int result = 0;
        int resourceId = view.getContext().getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = view.getContext().getResources().getDimensionPixelSize(resourceId);
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            //append margin-top
            lp.setMargins(lp.leftMargin, lp.topMargin+result, lp.rightMargin, lp.bottomMargin);
        }
    }
    @Override
    public void onGlobalLayout() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null) mImmersionBar.destroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }
}
