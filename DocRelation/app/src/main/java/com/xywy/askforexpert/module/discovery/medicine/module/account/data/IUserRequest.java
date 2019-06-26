package com.xywy.askforexpert.module.discovery.medicine.module.account.data;

import android.support.annotation.NonNull;

import com.xywy.askforexpert.module.discovery.medicine.module.account.SendTextMsgType;
import com.xywy.askforexpert.module.discovery.medicine.module.account.beans.LoginServerBean;
import com.xywy.askforexpert.module.discovery.medicine.module.account.beans.PicCodeBean;
import com.xywy.askforexpert.module.discovery.medicine.module.account.beans.UserInfoBean;
import com.xywy.retrofit.net.BaseData;

import rx.Observable;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/30 10:06
 */
public interface IUserRequest {
    @NonNull
    Observable<LoginServerBean> hxLogin(BaseData<LoginServerBean> baseData);

    Observable<BaseData<LoginServerBean>> register(String userName, String password, String verifyCode);

    Observable<BaseData> getVerifiCode(String phone, PicCodeBean picCodeBean, @SendTextMsgType String sendMsgType);

    Observable<BaseData<LoginServerBean>> login(String userName, String password);

    Observable<BaseData> findPassword(String userName, String pwd, String verifyCode);

    Observable<PicCodeBean> requestPicVerify();

    Observable<BaseData> checkPicCode(String picKey, String picCode);

    Observable<BaseData<UserInfoBean>> getUserInfo(String uid);

    Observable<BaseData<LoginServerBean>> loginByRandomCode(String phone, String code);
}

