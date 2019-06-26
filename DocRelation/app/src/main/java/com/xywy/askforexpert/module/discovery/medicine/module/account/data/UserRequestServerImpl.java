package com.xywy.askforexpert.module.discovery.medicine.module.account.data;

import android.support.annotation.NonNull;

import com.xywy.askforexpert.module.discovery.medicine.common.ApiConstants;
import com.xywy.askforexpert.module.discovery.medicine.common.MyRetrofitClient;
import com.xywy.askforexpert.module.discovery.medicine.common.RequestTool;
import com.xywy.askforexpert.module.discovery.medicine.module.account.SendTextMsgType;
import com.xywy.askforexpert.module.discovery.medicine.module.account.beans.LoginServerBean;
import com.xywy.askforexpert.module.discovery.medicine.module.account.beans.PicCodeBean;
import com.xywy.askforexpert.module.discovery.medicine.module.account.beans.UserInfoBean;
import com.xywy.askforexpert.module.discovery.medicine.module.chat.ChatHelper;
import com.xywy.retrofit.RetrofitClient;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.util.DeviceUtil;
import com.xywy.util.MD5Util;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/30 10:19
 */

 class UserRequestServerImpl implements IUserRequest {
//    UserApi api = RetrofitClient.getRetrofit().create(UserApi.class);
    UserApi api = MyRetrofitClient.getMyRetrofit().create(UserApi.class);
    @Override
    public Observable<BaseData<LoginServerBean>> login(String userName, String password) {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("password", password);
        postParams.put("username", userName);
        postParams = RequestTool.addServiceId(postParams);
        Map<String, String> getParams = RequestTool.getCommonParams("1599");
        getParams.put("sign",RequestTool.getSign(getParams,postParams));
        return api.login(getParams,postParams);
    }

    @Override
    public Observable<BaseData<LoginServerBean>> register(String userName, String password, String verifyCode) {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("password", password);
        postParams.put("phone", userName);
        postParams.put("code", verifyCode);
        postParams.put("project", SendTextMsgType.project_value);
        Map<String, String> getParams = RequestTool.getCommonParams("1600");
        getParams.put("sign",RequestTool.getSign(getParams,postParams));
        return api.register(getParams,postParams);
    }

    @Override
    public Observable<PicCodeBean> requestPicVerify() {
        Map<String, String> getParams = RequestTool.getCommonParams("1423");
        final String time = System.currentTimeMillis() +"";
        final String picKey = DeviceUtil.getSerialNumber() + time;
        getParams.put("timestamp", time);
        getParams.put("flag", picKey);
        getParams.put("sign",RequestTool.getSign(getParams,null));
        final String imageUrl = RequestTool.getUrlWithQueryString(RetrofitClient.BASE_URL+ ApiConstants.GET_PICTURE_CODE, getParams);
        return Observable.create(new Observable.OnSubscribe<PicCodeBean>() {
            @Override
            public void call(final Subscriber<? super PicCodeBean> subscriber) {
                PicCodeBean picBean=new PicCodeBean();
                picBean.setTimestamp(time);
                picBean.picIdentify =picKey;
                picBean.imageUrl=imageUrl;
                        subscriber.onNext(picBean);
            }
        });
    }

    @Override
    public Observable<BaseData> checkPicCode(String picKey, String picCode) {
        return null;
    }

    @Override
    public Observable<BaseData<UserInfoBean>> getUserInfo(String uid) {
        Map<String, String> getApiParams = RequestTool.getCommonParams("791");
        getApiParams.put("version", "1.0");
        getApiParams.put("uid", uid);
        getApiParams.put("sign",RequestTool.getSign(getApiParams,null));
        return api.getUserInfo(getApiParams);
    }


    /**
     * 784 . 手机验证码相关功能接口 v 1.2
     * code       计算方式   code = md5('api_wws_'+pro+timestamp+flag+code)   code为图片验证码图片上的内容
     */
    @Override
    public Observable<BaseData> getVerifiCode(String phone, PicCodeBean picCodeBean, String sendMsgType) {
        Map<String, String> getApiParams = RequestTool.getCommonParams("1501");
        getApiParams.put("version", "1.3");
        Map<String, String> postApiParams = new HashMap<>();
        postApiParams.put("act","userSMS_send");
        postApiParams.put("phone", phone);
        postApiParams.put("code", MD5Util.MD5("api_wws_" + RequestTool.REQUEST_PRO +
                picCodeBean.getTimestamp() + picCodeBean.picIdentify + picCodeBean.userInput));
        postApiParams.put("flag", picCodeBean.picIdentify);
        postApiParams.put("project", sendMsgType);
        postApiParams.put("timestamp", picCodeBean.getTimestamp());
        getApiParams.put("sign",RequestTool.getSign(getApiParams,postApiParams));
        return api.getTextMsgCode(getApiParams,postApiParams);
    }

    @Override
    public Observable<BaseData> findPassword(String userName, String pwd, String verifyCode) {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("password", pwd);
        postParams.put("phone", userName);
        postParams.put("code", verifyCode);
        postParams.put("project", SendTextMsgType.project_value_getback_pwd);
        Map<String, String> getParams = RequestTool.getCommonParams("1609");
        getParams.put("sign",RequestTool.getSign(getParams,postParams));
        return api.findPassWord(getParams,postParams);
    }
    @NonNull
    @Override
    public Observable<LoginServerBean> hxLogin(final BaseData<LoginServerBean> baseData) {
        return Observable.create(new Observable.OnSubscribe<LoginServerBean>() {
            @Override
            public void call(final Subscriber<? super LoginServerBean> subscriber) {
                ChatHelper.getInstance().Login(baseData.getData().getHx_username(), baseData.getData().getHx_password(), new BaseRetrofitResponse(subscriber) {
                            @Override
                            public void onNext(Object o) {
                                subscriber.onNext(baseData.getData());
                            }
                        }
                );
            }
        });
    }

    @Override
    public Observable<BaseData<LoginServerBean>> loginByRandomCode(String phone, String code) {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("phone", phone);
        postParams.put("code", code);
        postParams.put("project", SendTextMsgType.project_value_login_code);
        Map<String, String> getParams = RequestTool.getCommonParams("1665");
        getParams.put("sign",RequestTool.getSign(getParams,postParams));
        return api.loginByRandomCode(getParams,postParams);
    }


}
