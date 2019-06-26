package com.xywy.oauth.login.qq;

import android.content.Context;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.xywy.oauth.login.LoginCallBack;
import com.xywy.oauth.service.ApiParams;
import com.xywy.oauth.service.NetworkConstants;

import org.json.JSONObject;

/**
 * Created by DongJr on 2016/3/16.
 */
public class BaseUiListener implements IUiListener {

    private Context mContext;
    private LoginCallBack mCallBack;
    private Tencent mTencent;
    private String openId;
    private String ERROR_MSG = "授权失败，请稍后重试";
    private boolean firstComplete = true;

    public BaseUiListener(Context context, Tencent tencent, LoginCallBack callBack) {
        mContext = context;
        mCallBack = callBack;
        mTencent = tencent;
    }

    @Override
    public void onComplete(Object response) {
        doComplete(response);
    }

    private void doComplete(Object response) {
        if (response == null) {
            mCallBack.fail(ERROR_MSG);
        } else {
            if (firstComplete) {
                initToken(response);
            } else {
                initApi(response);
            }

        }
    }

    private void initApi(Object response) {
        try {

            JSONObject jsonObject = (JSONObject) response;
            String nickname = jsonObject.getString("nickname").trim();
            String photoUrl = jsonObject.getString("figureurl_2");

            ApiParams apiParams = new ApiParams()
                    .with(NetworkConstants.act_key, NetworkConstants.act_third_value)
                    .with(NetworkConstants.openid_key, openId)
                    .with(NetworkConstants.channelnum_key, "qq")
                    .with(NetworkConstants.usersource_key, NetworkConstants.source_value)
                    .with(NetworkConstants.nickname_key, nickname)
                    .with(NetworkConstants.photo_key, photoUrl)
                    .with("phone", "");
            mCallBack.success(apiParams);

        } catch (Exception e) {
            mCallBack.fail(ERROR_MSG);
        }

    }

    private void initToken(Object response) {
        JSONObject jsonObject = (JSONObject) response;
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            openId = jsonObject.getString(Constants.PARAM_OPEN_ID);

            mTencent.setAccessToken(token, expires);
            mTencent.setOpenId(openId);

            UserInfo userInfo = new UserInfo(mContext, mTencent.getQQToken());
            //调用此方法会重新调用onComplete(),response返回用户信息
            userInfo.getUserInfo(this);
            firstComplete = false;
        } catch (Exception e) {
            mCallBack.fail(ERROR_MSG);
        }

    }

    @Override
    public void onError(UiError e) {
        mCallBack.fail(e.errorMessage);
    }

    @Override
    public void onCancel() {
        mCallBack.fail("取消授权");
    }
}
