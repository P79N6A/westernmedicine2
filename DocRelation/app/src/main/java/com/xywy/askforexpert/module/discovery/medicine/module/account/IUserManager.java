package com.xywy.askforexpert.module.discovery.medicine.module.account;

import android.content.Context;

import java.util.List;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/28 14:23
 */
public interface IUserManager<T> {
    void startRegister(Context context);

    void startLogin(Context context);

    void startForgetPassword(Context context);

    void startAsGuest(Context context);

    /**
     * 个人信息是否可修改
     * @param context
     * @param showMsg
     * @return
     */
    boolean isPerInfoUpdateable(Context context, boolean showMsg);

    /**
     * 是否是认证医生
     * @param context
     * @param showMsg
     * @return
     */
    boolean isVerifiedDoc(Context context, boolean showMsg);

    void setCurrentLoginUser(Object user);

    T getCurrentLoginUser();

    boolean isLogin();

    void addJobImageUrl(String url);
    List getJobImageUrl();
    void addIdImageUrl(String url);

    /**
     * 获取提交认证的图片列表
     * @return
     */
    List getIdImageUrl();

    void notifyUserDataChanged();

    /**
     * 持久化保存用户信息
     */
    void saveUser();

    boolean checkLogin();
    String getCheckState();

    void logout();

}
