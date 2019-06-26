package com.xywy.askforexpert.appcommon.net.retrofitWrapper;

/**
 * Created by bailiangjin on 16/7/14.
 */

import com.xywy.retrofit.net.ApiException;
import com.xywy.retrofit.net.BaseData;

import rx.functions.Func1;

/**
 * 用来统一处理Http的resultCode
 */
public class CommonErrors<T> implements Func1<BaseData<T>, BaseData<T>> {

    @Override
    public BaseData<T> call(BaseData<T> data) {
        if (data.getCode() != 0 && data.getCode() != 10000 && data.getCode() != 20000) {
            throw new ApiException(data.getCode(), data.getMsg());
        }
        return data;
    }
}
