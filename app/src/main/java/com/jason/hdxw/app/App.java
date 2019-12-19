package com.jason.hdxw.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.provider.Settings;

import com.hjq.toast.ToastUtils;
import com.jason.hdxw.utils.CrashHandler;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.BuildConfig;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * created by wang on 2018/11/10
 */
public class App extends Application {
    public static Application app;
    public static Activity mActivity;

    public App() {
        super();
        app = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "458f36a318", true);
        CrashReport.setUserId("2020836");
        CrashHandler.getInstance().init(this);//初始化全局异常管理
        // 初始化吐司工具类
        ToastUtils.init(getApplicationContext());
        //初始化okgo
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //配置日志
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        //log打印级别，决定了log显示的详细程度
//        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);//全部打印数据
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.NONE);//关闭日志打印
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);
        //全局的读取超时时间
        builder.readTimeout(10000, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(10000, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(10000, TimeUnit.MILLISECONDS);
        OkGo.getInstance().init(this)
                .setOkHttpClient(builder.build());//建议设置OkHttpClient，不设置将使用默认的
        //开启日志打印框架
        Logger.addLogAdapter(new AndroidLogAdapter());
        //关闭打印日志
//        Logger.addLogAdapter(new AndroidLogAdapter() {
//            @Override
//            public boolean isLoggable(int priority, String tag) {
//                return BuildConfig.DEBUG;
//            }
//        });
        //获取当前活动的activity
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                                                    @Override
                                                    public void onActivityCreated(Activity activity, Bundle bundle) {

                                                    }

                                                    @Override
                                                    public void onActivityStarted(Activity activity) {
                                                        mActivity = activity;
                                                    }

                                                    @Override
                                                    public void onActivityResumed(Activity activity) {

                                                    }

                                                    @Override
                                                    public void onActivityPaused(Activity activity) {

                                                    }

                                                    @Override
                                                    public void onActivityStopped(Activity activity) {

                                                    }

                                                    @Override
                                                    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

                                                    }

                                                    @Override
                                                    public void onActivityDestroyed(Activity activity) {

                                                    }
                                                }
        );
    }

    public static Application getApplication() {
        return app;
    }

    public static Activity getActivity() {
        return mActivity;
    }

    /**
     * 获取设备号 ANDROID_ID
     */
    public String getDeviceNo() {
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
