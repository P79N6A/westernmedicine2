package com.xywy.retrofit.rxbus;

import android.util.Log;

import com.xywy.util.L;

import rx.Subscriber;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/11/2 13:10
 */
public abstract class EventSubscriber<T> extends Subscriber<Event<T>> {
    @Override
    public void onCompleted() {
        Log.e("onCompleted","onCompleted");
    }

    @Override
    public void onError(Throwable e) {
        L.ex(e);
    }
}
