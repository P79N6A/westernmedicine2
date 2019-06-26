package com.xywy.livevideo.debug.requestservice.interfaze;

/**
 * Created by bailiangjin on 2017/2/24.
 */
public interface RequestCallback<T> {

    void onSuccess(T t);


    void onError(String errorStr);
}
