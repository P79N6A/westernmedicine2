package com.xywy.oauth.login;

/**
 * Created by DongJr on 2016/3/23.
 */
public interface LoginCallBack {

    void success(Object object);

    void fail(String msg);

}
