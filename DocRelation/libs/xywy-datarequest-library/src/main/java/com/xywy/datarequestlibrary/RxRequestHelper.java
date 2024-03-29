package com.xywy.datarequestlibrary;


import com.xywy.datarequestlibrary.model.BaseData;
import com.xywy.datarequestlibrary.model.CommonErrors;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by bailiangjin on 2017/2/16.
 */

public class RxRequestHelper {

    /**
     * 绑定回调到mainthread 并统一处理异常
     *
     * @param observable
     * @param subscriber
     * @param <T>
     * @return
     */
    public static <T> Observable requestDealCommonError(Observable<BaseData<T>> observable, Subscriber<BaseData<T>> subscriber) {
        XywyDataRequestApi.getInstance().checkAppParamInit();
        mapCommonErrors(observable);
        setSubscribeToAndroidMainThread(observable, subscriber);
        return observable;
    }


    /**
     * 绑定回调到mainthread 不统一处理异常
     *
     * @param observable
     * @param subscriber
     * @param <T>
     */
    public static <T> void requestNotDealCommonError(Observable<T> observable, Subscriber<T> subscriber) {
        setSubscribeToAndroidMainThread(observable, subscriber);
    }


    /**
     * 将回调切换到MainThread
     *
     * @param observable
     * @param subscriber
     * @param <T>
     */
    private static <T> void setSubscribeToAndroidMainThread(Observable<T> observable, Subscriber<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 异常统一处理
     *
     * @param observable
     * @param <T>
     */
    private static <T> void mapCommonErrors(Observable<BaseData<T>> observable) {
        observable.map(new CommonErrors<T>());
    }




}
