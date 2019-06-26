package com.xywy.retrofit.net;

import com.google.gson.Gson;
import com.xywy.retrofit.RetrofitClient;
import com.xywy.util.L;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 对于api返回observeable进行统一错误处理和线程控制
 *
 * @param <M>
 */
public class CommonResponseTransformer<M> implements Observable.Transformer<M, M> {

    public CommonResponseTransformer() {
    }

    @Override
    public Observable<M> call(Observable<M> observable) {
        return observable
                //检测业务返回值错误
                .map(new ResultCodeErrors<M>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .lift(new SubscriberInterceptor<M>());
    }

    /**
     * 用来统一处理Http的resultCode
     */
    static class ResultCodeErrors<M> implements Func1<M, M> {
        static Gson gson = new Gson();

        @Override
        public M call(M data) {
            String temp;
            //如果是兼容Final创建的请求不做业务返回值判断
            if (isAfinalRequest(data)) {
                return data;
            } else {
                temp = gson.toJson(data);
            }
            CheckResult.checkNetReturn(temp);
            return data;
        }

    }

    /**
     * 所有请求回调先传递到这里再向上分发到我们的回调
     *
     * @param <M>
     */
    public static final class SubscriberInterceptor<M> implements Observable.Operator<M, M> {

        @Override
        public Subscriber<? super M> call(final Subscriber<? super M> child) {
            return new Subscriber<M>(child) {
                @Override
                public void onCompleted() {
                    child.onCompleted();
                }

                @Override
                public void onError(Throwable e) {
                    L.e("onError" + e.getMessage());
                    if (!RetrofitClient.sHandleCommonResp.handleError(e)){
                        child.onError(e);
                    }
                }

                @Override
                public void onNext(M data) {
                    if (!isAfinalRequest(data)) {
                        L.e("onNext" + new Gson().toJson(data));
                    }
                    if (!RetrofitClient.sHandleCommonResp.handleSuccess(data)){
                        child.onNext(data);
                    }
                }
            };
        }
    }

    /**
     * 判断是否是兼容afinal的请求
     *
     * @param data
     * @param <M>
     * @return
     */
    static <M> boolean isAfinalRequest(M data) {

        return data instanceof ResponseBody;
    }
}


