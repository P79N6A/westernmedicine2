package com.xywy.retrofit.demo;

import android.os.Handler;
import android.util.Log;

import com.xywy.retrofit.RetrofitClient;
import com.xywy.retrofit.net.BaseData;
import com.xywy.retrofit.rxbus.Event;
import com.xywy.retrofit.rxbus.RxBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/9/1 9:37
 * this is a demo ,show usage of  okhttp、retrofit、rxjava.
 */
public class RetrofitDemo {
    private ApiDemo api;

    private  Handler h;
    public RetrofitDemo(){
        api = RetrofitClient.getRetrofit().create(ApiDemo.class);
            h=new Handler();
//            String BASE_URL = "http://yimai.api.xywy.com";
//            RetrofitClient.init(BASE_URL);
    }

    public static String okHttp() throws IOException {
        RequestBody body=new FormBody.Builder()
                .add("password", "D/x7q2CzOdPgU5OMfR3sh5jZI2SunASx0v8a4icrdtGaz0qXl2VN/u2aVhW7AshiDkzgeHxt/+XPWaQG/lTRByA30S77txhvuc/xAN3u0/xMfKbIM8U6a4M8EWAUO2JfiwMvoP7gFdttaqLyprGFYHCZjfiStfFd8t7LtGq+lKU=")
                .add("phone", "15901121698")
        .add("sign", "f8b355ba3c447b9f2d7b7ad38c79050b")
        .add("command", "login")
        .add("reg_id", "").build();
        Request request = new Request.Builder().url(RetrofitClient.BASE_URL).post(body).build();
        Response response = RetrofitClient.getClient().newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    public  void rxJava() throws IOException {

        final Map<String, String> bundle = new HashMap<>();
        bundle.put("password", "D/x7q2CzOdPgU5OMfR3sh5jZI2SunASx0v8a4icrdtGaz0qXl2VN/u2aVhW7AshiDkzgeHxt/+XPWaQG/lTRByA30S77txhvuc/xAN3u0/xMfKbIM8U6a4M8EWAUO2JfiwMvoP7gFdttaqLyprGFYHCZjfiStfFd8t7LtGq+lKU=");
        bundle.put("phone", "15901121698");
        bundle.put("sign", "f8b355ba3c447b9f2d7b7ad38c79050b");
        bundle.put("command", "login");
        bundle.put("reg_id", "");
        api.noCacheDemo(bundle)
                .subscribe(new BaseRetrofitResponse<BaseData<UserData>>() {
                    int count=0;
                    @Override
                    public void onNext(BaseData<UserData> userDataBaseData) {
                        Log.e("onNext", count+++"");
                    }
                });

        api.rxCacheDemo(bundle)
                .subscribe(new BaseRetrofitResponse<BaseData<UserData>>() {
                    int count=0;
                    @Override
                    public void onNext(BaseData<UserData> userDataBaseData) {
                        Log.e("onNext", count+++"");
                    }
                });
    }

    public  void pureRxJava() throws IOException {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("abc");
                subscriber.onCompleted();
            }
        })
                .map(new Func1<String, byte[]>() {
                    @Override
                    public byte[] call(String s) {
                        return s.getBytes();
                    }
                })
                .flatMap(new Func1<byte[], Observable<String>>() {
                    @Override
                    public Observable<String> call(byte[] bytes) {
                        char[] chars = bytes.toString().toCharArray();
                        List<String> strings = new ArrayList<String>();
                        for (char c : chars) {
                            strings.add(String.valueOf(c));
                        }
                        return Observable.from(strings);
                    }

                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Log.e("doOnSubscribe", "doOnSubscribe"); // 需要在主线程执行
                    }
                })
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {

                    }
                });
    }

    public  Observable<BaseData<UserData>>  retrofit() {

        final Map<String, String> bundle = new HashMap<>();
        bundle.put("password", "D/x7q2CzOdPgU5OMfR3sh5jZI2SunASx0v8a4icrdtGaz0qXl2VN/u2aVhW7AshiDkzgeHxt/+XPWaQG/lTRByA30S77txhvuc/xAN3u0/xMfKbIM8U6a4M8EWAUO2JfiwMvoP7gFdttaqLyprGFYHCZjfiStfFd8t7LtGq+lKU=");
        bundle.put("phone", "15901121698");
        bundle.put("sign", "f8b355ba3c447b9f2d7b7ad38c79050b");
        bundle.put("command", "login");
        bundle.put("reg_id", "");
       return api.retrofitDemo(bundle)
               ;
    }

    public  void rxbus(){
        RxBus.getDefault().toObservable("type").subscribe(new Subscriber<Event>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Event event) {
                Log.e("RxBus1",event.getData().toString());
            }
        });
        RxBus.getDefault().toObservable("type").subscribe(new Subscriber<Event>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Event event) {
                Log.e("RxBus2",event.getData().toString());
            }
        });
        repeate();

    }

    private void repeate() {
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                RxBus.getDefault().post(new Event<>("type","data"));
                repeate();
            }
        },5000);
    }

    public static void volley() throws IOException {

    }
}
