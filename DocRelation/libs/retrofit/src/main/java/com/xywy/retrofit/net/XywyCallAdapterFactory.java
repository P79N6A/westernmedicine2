package com.xywy.retrofit.net;

import com.google.gson.Gson;
import com.xywy.retrofit.RetrofitClient;
import com.xywy.retrofit.cache.DiskCacheClient;
import com.xywy.retrofit.cache.ICache;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.FormBody;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Producer;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

//import com.xywy.askforexpert.YMApplication;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/9/7 15:15
 * a modified copy  of RxJavaCallAdapterFactory
 */
public final class XywyCallAdapterFactory extends CallAdapter.Factory {
    private final Scheduler scheduler;

    private XywyCallAdapterFactory(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * Returns an instance which creates synchronous observables that do not operate on any scheduler
     * by default.
     */
    public static XywyCallAdapterFactory create() {
        return new XywyCallAdapterFactory(null);
    }

    @Override
    public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        Class<?> rawType = getRawType(returnType);
        if (rawType != Observable.class) {
            return null;
        }
        return getCallAdapter(returnType, scheduler);
    }

    private CallAdapter<Observable<?>> getCallAdapter(Type returnType, Scheduler scheduler) {
        Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
        return new BaseDataCallAdapter(observableType, scheduler);
//        Class<?> rawObservableType = getRawType(observableType);
//        if (rawObservableType == BaseData.class) {
//            return new BaseDataCallAdapter(observableType, scheduler);
//        }
//        return null;
    }

    static final class CallOnSubscribe<T> implements Observable.OnSubscribe<Response<T>> {
        private final Call<T> originalCall;
        private final Type responseType;

        public CallOnSubscribe(Type responseType, Call<T> call) {
            this.originalCall = call;
            this.responseType = responseType;
        }

        @Override
        public void call(final Subscriber<? super Response<T>> subscriber) {
            // Since Call is a one-shot type, clone it for each new subscriber.
            Call<T> call = originalCall.clone();

            // Wrap the call in a helper which handles both unsubscription and backpressure.
            RequestProducer<T> requestProducer = new RequestProducer<>(responseType, call, subscriber);
            subscriber.add(requestProducer);
            subscriber.setProducer(requestProducer);
        }
    }

    static final class RequestProducer<M> implements Subscription, Producer {
        private final Call<M> call;
        private final Subscriber<? super Response<M>> subscriber;
        private static ICache cache;

        static {
            //-------------------------------设置http缓存，提升用户体验-----------------------------------
            File httpCacheDirectory = new File(RetrofitClient.getContext().getCacheDir(), "retrofitCache");
            try {
                cache = new DiskCacheClient(httpCacheDirectory, 10 * 1024 * 1024);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private final Type responseType;

        RequestProducer(Type responseType, Call<M> call, Subscriber<? super Response<M>> subscriber) {
            this.responseType = responseType;
            this.call = call;
            this.subscriber = subscriber;
        }

        @Override
        public void request(long n) {
            if (n < 0) throw new IllegalArgumentException("n < 0: " + n);
            if (n == 0) return; // Nothing to do when requesting 0.
            try {
                //1、先从缓存中读
                Request request = call.request();
                boolean cacheEnable = cacheEnabled(request);
                if (cacheEnable) {
                    String cacheStr = cache.get(request);
                    if (cacheStr != null && !subscriber.isUnsubscribed()) {
                        System.out.println(responseType + "============== getGenericInterfaces");
                        try {
                            //// TODO: 2016/11/18  此处使用msg字段代表数据来源于缓存，后续需要注意
                            Object cacheData = new Gson().fromJson(cacheStr, responseType);
                            Field msg = cacheData.getClass().getField("msg");
                            msg.setAccessible(true);
                            msg.set(cacheData, RetrofitCache.CacheStr);
                            Response<M> cacheResp = Response.success((M) cacheData);
                            subscriber.onNext(cacheResp);
                        } catch (Exception e) {
                            //do nothing
                        }
                    }
                }
                //2、发请求获取数据,改写intercept，设置request为不允许使用okhttp缓存
                Response<M> netResp = call.execute();

                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(netResp);
                }
                //3、如果onnext不出异常写缓存
                if (netResp.isSuccessful() && cacheEnable) {
                    cache.put(netResp);
                }

            } catch (Throwable t) {
                Exceptions.throwIfFatal(t);
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(t);
                }
                return;
            }

            if (!subscriber.isUnsubscribed()) {
                subscriber.onCompleted();
            }
        }

        //是否允许缓存
        public boolean cacheEnabled(Request request) {
            boolean headerAllow = !request.cacheControl().noCache();
            boolean methodAllow = request.method().equalsIgnoreCase("GET") ||
                    (request.method().equalsIgnoreCase("post")
                            && (request.body() == null || request.body() instanceof FormBody)
                    );
            return headerAllow && methodAllow;
        }

        @Override
        public void unsubscribe() {
            call.cancel();
        }

        @Override
        public boolean isUnsubscribed() {
            return call.isCanceled();
        }
    }


    static final class BaseDataCallAdapter implements CallAdapter<Observable<?>> {
        private final Type responseType;
        private final Scheduler scheduler;

        BaseDataCallAdapter(Type responseType, Scheduler scheduler) {
            this.responseType = responseType;
            this.scheduler = scheduler;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public <R> Observable<R> adapt(Call<R> call) {
            Observable<R> observable = Observable.create(new CallOnSubscribe<>(responseType, call)) //
                    .lift(OperatorMapResponseToBodyOrError.<R>instance())
                    .compose(new CommonResponseTransformer<R>());
            if (scheduler != null) {
                return observable.subscribeOn(scheduler);
            }
            return observable;
        }
    }

    /**
     * A version of {@link Observable#map(Func1)} which lets us trigger {@code onError} without having
     * to use {@link Observable#flatMap(Func1)} which breaks producer requests from propagating.
     */
    static final class OperatorMapResponseToBodyOrError<T> implements Observable.Operator<T, Response<T>> {
        private static final OperatorMapResponseToBodyOrError<Object> INSTANCE =
                new OperatorMapResponseToBodyOrError<>();

        @SuppressWarnings("unchecked") // Safe because of erasure.
        static <R> OperatorMapResponseToBodyOrError<R> instance() {
            return (OperatorMapResponseToBodyOrError<R>) INSTANCE;
        }

        @Override
        public Subscriber<? super Response<T>> call(final Subscriber<? super T> child) {
            return new Subscriber<Response<T>>(child) {
                @Override
                public void onNext(Response<T> response) {
                    if (response.isSuccessful()) {
                        child.onNext(response.body());
                    } else {
                        onError(new HttpException(response));
                    }
                }

                @Override
                public void onCompleted() {
                    child.onCompleted();
                }

                @Override
                public void onError(Throwable e) {
//                    if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof HttpException) {
//                        L.e("网络异常:"+e.getMessage());
//                        com.xywy.util.T.showShort(RetrofitClient.getContext(),"网络中断，请检查您的网络状态");
//                    }
                    child.onError(e);
                }
            };
        }
    }
}
