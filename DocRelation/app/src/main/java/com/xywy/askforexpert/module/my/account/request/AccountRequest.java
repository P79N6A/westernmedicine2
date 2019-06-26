package com.xywy.askforexpert.module.my.account.request;

import android.text.TextUtils;

import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.model.LoginInfo_New;
import com.xywy.askforexpert.model.RegisterInfo;
import com.xywy.askforexpert.module.discovery.medicine.common.MyRetrofitClient;
import com.xywy.askforexpert.module.discovery.medicine.common.RequestTool;
import com.xywy.retrofit.net.BaseData;
import com.xywy.util.MD5Util;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import rx.Observable;

/**
 * Created by xugan on 2018/2/2.
 */

public class AccountRequest {
    private AccountApi api;
    private static AccountRequest instance = new AccountRequest();
    private AccountRequest(){
        api = MyRetrofitClient.getMyRetrofit().create(AccountApi.class);
    }
    public static AccountRequest getInstance(){
        return instance;
    }

    public Observable<BaseData> getVerifiCode(String phone, String timestamp, String picCode,String flag,String sendMsgType) {
        Map<String, String> getApiParams = RequestTool.getCommonParams("1501");
        getApiParams.put("version", "1.0");
        Map<String, String> postApiParams = new HashMap<>();
        postApiParams.put("act","userSMS_send");
        postApiParams.put("phone", phone);
        postApiParams.put("code", MD5Util.MD5("api_wws_" + RequestTool.REQUEST_PRO +
                timestamp + flag + picCode));
        postApiParams.put("flag", flag);
        postApiParams.put("project", sendMsgType);
        postApiParams.put("timestamp", timestamp);
        getApiParams.put("sign",RequestTool.getSign(getApiParams,postApiParams));
        return api.getTextMsgCode(getApiParams,postApiParams);
    }

    public Observable<BaseData<RegisterInfo>> register(String phone, String psw, String code) {
        Map<String, String> getApiParams = RequestTool.getCommonParams("1600");
        Map<String, String> postApiParams = new HashMap<>();
        postApiParams.put("phone", phone);
        postApiParams.put("password", psw);
        postApiParams.put("code", code);
        postApiParams.put("project", "APP_YMGYH_REG");
       if(YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)){
           postApiParams.put("bus_source", "13");
       }else {
           postApiParams.put("bus_source", "8");
       }
        getApiParams.put("sign",RequestTool.getSign(getApiParams,postApiParams));
        return api.register(getApiParams,postApiParams);
    }

    public Observable<BaseData<LoginInfo_New>> login(String username,String password){
        Map<String, String> getApiParams = RequestTool.getCommonParams("1599");
        Map<String, String> postApiParams = new HashMap<>();
        if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
            postApiParams.put("bus_source", 13 + "");
        }else {
            postApiParams.put("bus_source", 8 + "");
        }
        postApiParams.put("username",username);
        postApiParams.put("password",password);
        postApiParams.put("reg_id", JPushInterface.getRegistrationID(YMApplication.getInstance()));
        getApiParams.put("sign",RequestTool.getSign(getApiParams,postApiParams));
        return api.login(getApiParams,postApiParams);
    }

    public Observable<BaseData> updatePwd(String phone,String password,String new_password,String code,String uid,String project){
        Map<String, String> getApiParams = RequestTool.getCommonParams("1808");
        Map<String, String> postApiParams = new HashMap<>();
        postApiParams.put("phone",phone);
        postApiParams.put("password",password);
        postApiParams.put("new_password",new_password);
        postApiParams.put("code",code);
        postApiParams.put("uid",uid);
        postApiParams.put("project", project);
        getApiParams.put("sign",RequestTool.getSign(getApiParams,postApiParams));
        return api.updatePwd(getApiParams,postApiParams);
    }

    public Observable<BaseData> resetPwd(String phone,String password,String code,String project){
        Map<String, String> getApiParams = RequestTool.getCommonParams("1609");
        Map<String, String> postApiParams = new HashMap<>();
        postApiParams.put("phone",phone);
        postApiParams.put("password",password);
        postApiParams.put("code",code);
        postApiParams.put("project", project);
        getApiParams.put("sign",RequestTool.getSign(getApiParams,postApiParams));
        return api.resetPwd(getApiParams,postApiParams);
    }

    public Observable<BaseData> bindPhone(String phone,String uid,String code,String project){
        Map<String, String> getApiParams = RequestTool.getCommonParams("1809");
        Map<String, String> postApiParams = new HashMap<>();
        postApiParams.put("phone",phone);
        postApiParams.put("uid",uid);
        postApiParams.put("code",code);
        postApiParams.put("project", project);
        getApiParams.put("sign",RequestTool.getSign(getApiParams,postApiParams));
        return api.bindPhone(getApiParams,postApiParams);
    }
}
