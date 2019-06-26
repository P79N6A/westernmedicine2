package com.xywy.askforexpert.appcommon.net.retrofitWrapper.commonerror;

/**
 * Created by bailiangjin on 16/7/14.
 */

import com.xywy.askforexpert.model.api.BaseStringResultBean;
import com.xywy.retrofit.net.ApiException;

import rx.functions.Func1;

/**
 * 用来统一处理Http的resultCode String格式的
 */
public class CommonStringRstErrors<T extends BaseStringResultBean> implements Func1<T, T> {

    @Override
    public T call(T data) {
        if (!"0".equals(data.getCode())&&!"10000".equals(data.getCode())&&!"20000".equals(data.getCode())) {
            throw new ApiException(Integer.parseInt(data.getCode()), data.getMsg());
        }
        return data;
    }
}
