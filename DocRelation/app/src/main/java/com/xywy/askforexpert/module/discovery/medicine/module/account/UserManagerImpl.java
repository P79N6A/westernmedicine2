package com.xywy.askforexpert.module.discovery.medicine.module.account;

import android.content.Context;

import com.google.gson.Gson;
import com.xywy.askforexpert.module.discovery.medicine.module.account.beans.UserBean;
import com.xywy.askforexpert.module.discovery.medicine.module.chat.ChatHelper;
import com.xywy.util.AppUtils;
import com.xywy.util.ContextUtil;
import com.xywy.util.SharedPreferencesHelper;
import com.xywy.util.T;

import java.util.List;

public class UserManagerImpl implements IUserManager<UserBean> {
    public final Context context;

    public UserBean current;
//    public UserBean last;

    public UserManagerImpl(Context context) {
        this.context = context;
    }

    @Override
    public void startRegister(Context context) {
        //暂时先注销
//        RegisterActivity.start(context);
    }

    @Override
    public void startLogin(Context context) {
        //暂时先注销
//        LoginActivity.start(context);
    }

    @Override
    public void startForgetPassword(Context context) {
        //暂时先注销
//        FindPasswordActivity.start(context);
    }

    @Override
    public void startAsGuest(Context context) {
        //暂时先注销
//        Intent intent1 = new Intent();
//        intent1.setClass(context, LoginActivity.class);
//        context.startActivity(intent1);
    }

    /**
     * @param context
     * @param showMsg
     * @return 个人信息是否可修改
     */
    @Override
    public boolean isPerInfoUpdateable(Context context, boolean showMsg) {
        if (UserBean.UserState.checking.equals(getCurrentLoginUser().getState().getId())) {
            if (showMsg) {
                T.showShort("认证中，此项不可修改！");
//                DialogModel.warn(context, "认证中，此项不可修改！");
            }
        } else if (UserBean.UserState.passed.equals(getCurrentLoginUser().getState().getId())) {
//            if (showMsg) {
//                DialogModel.warn(context, "您已认证，此项不可修改！");
//            }
        } else if (UserBean.UserState.failed.equals(getCurrentLoginUser().getState().getId())){

        }else if (UserBean.UserState.unverified.equals(getCurrentLoginUser().getState().getId())){
            return true;
        }
        return false;
    }

    @Override
    public boolean isVerifiedDoc(Context context, boolean showMsg) {
        if (!isLogin()){
            return false;
        }
        if (UserBean.UserState.passed.equals(getCurrentLoginUser().getState().getId())) {
            return true;
        }
        if (showMsg) {
            T.showShort("请先认证!");
//                DialogModel.warn(context, "请先认证!");
            }
        return false;
    }

    @Override
    public String getCheckState() {
        return  getCurrentLoginUser().getState().getName();
    }

    @Override
    public void logout() {

        UserBean save=new UserBean();
        save.setPassword(current.getPassword());
        save.setUserAccount(current.getUserAccount());
        setCurrentLoginUser(save);
        ChatHelper.getInstance().logout(true,null);
        ContextUtil.finishAllActivity();
        AppUtils.restart(context);
//        startLogin(context);
    }

    @Override
    public void setCurrentLoginUser(Object user) {
        Gson gson = new Gson();
        current = gson.fromJson(gson.toJson(user), UserBean.class);
        SharedPreferencesHelper.getSystem(context).putString("current_user", new Gson().toJson(user));
    }

    @Override
    public synchronized UserBean getCurrentLoginUser() {
        if (current == null) {
            String lastUser = SharedPreferencesHelper.getSystem(context).getString("current_user");
            current = new Gson().fromJson(lastUser, UserBean.class);
            if (current != null) {
                current.parse();
            }else{
                current = new UserBean();
            }
        }
        return current;
    }

    @Override
    public boolean isLogin() {
        return ChatHelper.getInstance().isLoggedIn() && current != null&&current.getLoginServerBean()!=null;
    }

    @Override
    public void addJobImageUrl(String url) {
        getCurrentLoginUser().getJobImages().add(url);
    }

    @Override
    public List getJobImageUrl() {
        return getCurrentLoginUser().getJobImages();
    }

    @Override
    public void addIdImageUrl(String url) {
        getCurrentLoginUser().getIdImages().add(url);
    }

    @Override
    public List getIdImageUrl() {
        return getCurrentLoginUser().getIdImages();
    }

    @Override
    public void notifyUserDataChanged() {

    }

    @Override
    public void saveUser() {
        SharedPreferencesHelper.getSystem(context).putString("current_user", new Gson().toJson(current));
    }

    @Override
    public boolean checkLogin() {
        if (!isLogin()) {
            startLogin(context);
        }
        return isLogin();
    }

}