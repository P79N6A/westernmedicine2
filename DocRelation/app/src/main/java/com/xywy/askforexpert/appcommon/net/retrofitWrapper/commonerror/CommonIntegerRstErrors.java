package com.xywy.askforexpert.appcommon.net.retrofitWrapper.commonerror;

/**
 * Created by bailiangjin on 16/7/14.
 */

import com.xywy.askforexpert.model.api.BaseResultBean;
import com.xywy.retrofit.net.ApiException;

import rx.functions.Func1;

/**
 * 用来统一处理Http的resultCode int格式的
 */
public class CommonIntegerRstErrors<T extends BaseResultBean> implements Func1<T, T> {

    @Override
    public T call(T data) {
        if (data.getCode() != 0 && data.getCode() != 10000 && data.getCode() != 20000) {
            throw new ApiException(data.getCode(), data.getMsg());
        }
        return data;
    }
}
