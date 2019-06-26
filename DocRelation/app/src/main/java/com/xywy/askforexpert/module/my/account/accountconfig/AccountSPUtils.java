package com.xywy.askforexpert.module.my.account.accountconfig;

import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.SPUtils;

/**
 * Created by bailiangjin on 2017/1/11.
 */

public class AccountSPUtils {

    public static  final String IS_LOGINED="IS_LOGINED";
    public static  final String INPUT_ACCOUNT_IDKEY="IS_LOGINED";


    public static  final String NOT_FIRST_TIME_LOGIN="NOT_FIRST_TIME_LOGIN";

    public static SPUtils  getAccountSP(){
       return SPUtils.createInstance("ym_account");
    }

    public static boolean isAccountLogined(String inputUserId){
        LogUtils.d("userName:"+inputUserId);
        return getAccountSP().getBoolean(IS_LOGINED+inputUserId);
    }

    public static void setNotFirstLoginState(String inputUserId){
        getAccountSP().putBoolean(NOT_FIRST_TIME_LOGIN+inputUserId,true);
    }

    public static boolean getNotFirstLoginState(String inputUserId){
       return getAccountSP().getBoolean(NOT_FIRST_TIME_LOGIN+inputUserId);
    }

    public static void setLoginState(String inputUserId,boolean isLogin){
        getAccountSP().putBoolean(IS_LOGINED+inputUserId,isLogin);
    }


    public static void loginSuccess(String inputUserId){
        LogUtils.d("userName:"+inputUserId);
        saveInputUserId(inputUserId);
        setNotFirstLoginState(inputUserId);
        setLoginState(inputUserId,true);
    }

    public static void logout(){
        String userId=getInputUserId();
        LogUtils.d("userName:"+userId);
        setLoginState(userId,false);
        clearInputUserId();
    }

    public static void saveInputUserId(String inputUserId){
        getAccountSP().putString(INPUT_ACCOUNT_IDKEY,inputUserId);
    }

    public static String getInputUserId(){
       return getAccountSP().getString(INPUT_ACCOUNT_IDKEY);
    }

    public static void clearInputUserId(){
        getAccountSP().remove(INPUT_ACCOUNT_IDKEY);
    }


    public static  final String last_account="last_account";
    public static void saveLastInputAccount(String account){
         getAccountSP().putString(last_account,account);
    }

    public static String getLastInputAccount(){
       return getAccountSP().getString(last_account);
    }
    public static void saveLastInputPassword(String password){
        getAccountSP().putString(INPUT_ACCOUNT_IDKEY,password);
    }

    public static String getLastInputPassword(){
        return getAccountSP().getString(last_account);
    }
}
