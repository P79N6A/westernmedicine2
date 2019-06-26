package com.xywy.oauth.login.WeiBo;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.xywy.oauth.login.LoginCallBack;
import com.xywy.oauth.login.MineConstants;
import com.xywy.oauth.service.ApiParams;
import com.xywy.oauth.service.NetworkConstants;


/**
 * Created by DongJr on 2016/3/25.
 */
public class WBAuthListener implements WeiboAuthListener {

    private LoginCallBack mLoginCallBack;

    private Context mContext;
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                // 调用 User#parse 将JSON串解析成User对象
                User user = User.parse(response);

                ApiParams apiParams = new ApiParams()
                        .with(NetworkConstants.act_key, NetworkConstants.act_third_value)
                        .with(NetworkConstants.openid_key, user.id)
                        .with(NetworkConstants.channelnum_key, "weibo")
                        .with(NetworkConstants.usersource_key, NetworkConstants.source_value)
                        .with(NetworkConstants.nickname_key, user.screen_name)
                        .with(NetworkConstants.photo_key, user.avatar_hd)
                        .with("phone", "");
                mLoginCallBack.success(apiParams);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            mLoginCallBack.fail("授权失败，请稍后重试");
        }
    };

    public WBAuthListener(Context context, LoginCallBack loginCallBack) {

        mLoginCallBack = loginCallBack;
        mContext = context;

    }

    @Override
    public void onComplete(Bundle values) {
        Oauth2AccessToken mAccessToken = Oauth2AccessToken.parseAccessToken(values);// 从 Bundle 中解析 Token
        if (mAccessToken.isSessionValid()) {
            UsersAPI usersAPI = new UsersAPI(mContext, MineConstants.WBAPP_ID, mAccessToken);
            long uid = Long.parseLong(mAccessToken.getUid());
            usersAPI.show(uid, mListener);
        } else {
            // 当您注册的应用程序签名不正确时，就会收到错误Code，请确保签名正确
            String code = values.getString("code");
            if (!TextUtils.isEmpty(code)) {
                mLoginCallBack.fail("签名不正确");
            }
        }
    }

    @Override
    public void onWeiboException(WeiboException e) {
        mLoginCallBack.fail("授权失败，请稍后重试");
    }

    @Override
    public void onCancel() {
        mLoginCallBack.fail("授权取消");
    }
}
