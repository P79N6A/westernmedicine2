package com.xywy.askforexpert.appcommon.net.retrofitTools;

import com.xywy.askforexpert.model.base.BaseBean;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/6/20 10:26
 */
@Deprecated
public interface HttpRequestCallBack<D> {

    void onSuccess(BaseBean<D> baseBean);

    void onFailure(RequestState state, String msg);

    enum RequestState {
        /**
         * 服务器返回code非0或10000
         */
        STATE_ERROR_CODE,

        /**
         * 服务器返回数据null
         */
        STATE_NULL_BASEBEAN,

        /**
         * 服务器返回data为空
         */
        STATE_NULL_DATA,

        /**
         * 请求失败
         */
        STATE_REQUEST_FAILED,

        /**
         * 失败
         */
        STATE_ON_FAILURE
    }
}
