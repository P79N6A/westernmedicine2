package com.xywy.askforexpert.module.discovery.medicine.module.account.model;

import android.text.TextUtils;

import com.xywy.askforexpert.module.discovery.medicine.common.MyRxBus;
import com.xywy.askforexpert.module.discovery.medicine.module.account.UserManager;
import com.xywy.askforexpert.module.discovery.medicine.module.account.beans.LoginServerBean;
import com.xywy.askforexpert.module.discovery.medicine.module.account.beans.UserBean;
import com.xywy.askforexpert.module.discovery.medicine.module.account.data.UserRequest;
import com.xywy.askforexpert.module.discovery.medicine.module.chat.HxUserHelper;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.PersonCard;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.request.PersonCardRequest;
import com.xywy.askforexpert.module.discovery.medicine.util.ToastUtils;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/22 10:42
 */

public class LoginModel {
    public static final String USE_CACHE_PASSWORD = "!@&%#(&#)$*^!*#";

    public LoginModel() {

    }

    public String getLastLoginPassword() {
        String pwd = UserManager.getInstance().getCurrentLoginUser().getPassword();
        if (!TextUtils.isEmpty(pwd)) {
            pwd = USE_CACHE_PASSWORD;
        }
        return pwd;
    }

    public String getLastLoginHead() {
        return UserManager.getInstance().getCurrentLoginUser().getPhoto();
    }

    public static void login(final String userName, String password, final Subscriber subscriber) {

        if (TextUtils.isEmpty(userName)) {
            ToastUtils.shortToast("请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.shortToast("请输入密码");
            return;
        }

        if (USE_CACHE_PASSWORD.equals(password)) {
            password = UserManager.getInstance().getCurrentLoginUser().getPassword();
        }
        subscriber.onStart();
        final String finalPassword = password;
        UserRequest.getInstance().login(userName, password)
                .flatMap(new Func1<BaseData<LoginServerBean>, Observable<LoginServerBean>>() {
                    @Override
                    public Observable<LoginServerBean> call(BaseData<LoginServerBean> loginServerBeanBaseData) {
                        //保存登陆成功用户名和密码
                        UserBean currentLoginUser = UserManager.getInstance().getCurrentLoginUser();
                        currentLoginUser.setUserAccount(userName);
                        currentLoginUser.setPassword(finalPassword);
                        return UserRequest.getInstance().hxLogin(loginServerBeanBaseData);
                    }
                })
                .flatMap(new Func1<LoginServerBean, Observable<BaseData<PersonCard>>>() {
                    @Override
                    public Observable<BaseData<PersonCard>> call(LoginServerBean loginServerBean) {
                        HxUserHelper.addUser(loginServerBean.getHx_username(), loginServerBean.getReal_name(), loginServerBean.getPhoto());
                        return PersonCardRequest.getInstance().getDoctorCard(loginServerBean.getUser_id());
                    }
                })
                .subscribe(new BaseRetrofitResponse<BaseData<PersonCard>>(subscriber) {
                    @Override
                    public void onNext(BaseData<PersonCard> personInfo) {
                        saveUserData(personInfo);
                        super.onNext(null);
                    }
                });

    }
    public static void loginByRandomCode(final String phone, String code, final Subscriber subscriber) {

        if (TextUtils.isEmpty(phone)) {
            ToastUtils.shortToast("请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtils.shortToast("请输入验证码");
            return;
        }

        subscriber.onStart();
        UserRequest.getInstance().loginByRandomCode(phone, code)
                .flatMap(new Func1<BaseData<LoginServerBean>, Observable<LoginServerBean>>() {
                    @Override
                    public Observable<LoginServerBean> call(BaseData<LoginServerBean> loginServerBeanBaseData) {
                        //保存登陆成功用户名和密码
                        UserBean currentLoginUser = UserManager.getInstance().getCurrentLoginUser();
                        currentLoginUser.setUserAccount(phone);
                        return UserRequest.getInstance().hxLogin(loginServerBeanBaseData);
                    }
                })
                .flatMap(new Func1<LoginServerBean, Observable<BaseData<PersonCard>>>() {
                    @Override
                    public Observable<BaseData<PersonCard>> call(LoginServerBean loginServerBean) {
                        HxUserHelper.addUser(loginServerBean.getHx_username(), loginServerBean.getReal_name(), loginServerBean.getPhoto());
                        return PersonCardRequest.getInstance().getDoctorCard(loginServerBean.getUser_id());
                    }
                })
                .subscribe(new BaseRetrofitResponse<BaseData<PersonCard>>(subscriber) {
                    @Override
                    public void onNext(BaseData<PersonCard> personInfo) {
                        saveUserData(personInfo);
                        super.onNext(null);
                    }
                });

    }

    private static void saveUserData(BaseData<PersonCard> personInfo) {
        UserBean currentLoginUser = UserManager.getInstance().getCurrentLoginUser();
        currentLoginUser.setPersonServerBean(personInfo.getData());
        UserManager.getInstance().saveUser();
        currentLoginUser.parse();
        MyRxBus.notifyUserInfoChanged();
    }

    public static void refreshPersonalInfo(final Subscriber subscriber) {
        String docId = UserManager.getInstance().getCurrentLoginUser().getLoginServerBean().getUser_id();
        PersonCardRequest.getInstance().getDoctorCard(docId)
                .subscribe(new BaseRetrofitResponse<BaseData<PersonCard>>(subscriber) {
                    @Override
                    public void onNext(BaseData<PersonCard> personCardBaseData) {
                        saveUserData(personCardBaseData);
                        super.onNext(personCardBaseData);
                    }
                });
    }



    public String getLastLoginAccount() {
        return UserManager.getInstance().getCurrentLoginUser().getUserAccount();
    }


}
