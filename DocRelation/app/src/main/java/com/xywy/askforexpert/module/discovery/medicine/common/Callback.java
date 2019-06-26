package com.xywy.askforexpert.module.discovery.medicine.common;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/23 15:03
 */
public interface Callback<T> {
    void onStart();

    void onProgress(int progress);

    void onSuccess(T data);

    void onFail(Throwable e);
}
