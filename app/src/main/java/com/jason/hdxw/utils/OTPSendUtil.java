package com.jason.hdxw.utils;

import android.content.Context;

import com.hjq.toast.ToastUtils;
import com.jason.hdxw.R;
import com.jason.hdxw.api.API;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class OTPSendUtil {

    public static void send(final Context ctx,final String type){

        Logger.e("手机验证码接口参数ｔｏｋｅｎ：" +UserCache.getToken());
        OkGo.<String>post(API.BASIC_OTP)
                .params("token", UserCache.getToken())
                .params("type", type)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.e("手机验证码接口返回数据：" + response.body());
                        try {

                            JSONObject jsonObject = new JSONObject(response.body());
                            if ("y".equals(jsonObject.getString("status"))) {

                                ToastUtils.show(ctx.getString(R.string.changepwd_hint2)+"您手机");

                            } else {
                                ToastUtils.show(jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            ToastUtils.show(ctx.getString(R.string.analysis_error));
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.show(ctx.getString(R.string.network_error));
                    }


                });
    }
}
