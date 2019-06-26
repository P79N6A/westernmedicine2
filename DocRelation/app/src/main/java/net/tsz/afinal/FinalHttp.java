/**
 * Copyright (c) 2012-2013, Michael Yang 杨福海 (www.yangfuhai.com).
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.tsz.afinal;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.xywy.askforexpert.appcommon.net.retrofitWrapper.ApiService4Final;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.model.api.BaseResultBean;
import com.xywy.askforexpert.module.my.account.utils.ForceLogoutDialogUtils;
import com.xywy.retrofit.RetrofitClient;
import com.xywy.retrofit.net.ApiException;
import com.xywy.retrofit.net.RetrofitCache;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 请求工具类 以后改成单例的 stone
 * stone
 * 2017/10/26 下午6:04
 */
public class FinalHttp {
    private final ApiService4Final api;
    private OkHttpClient client;
    private static final MediaType MEDIA_OBJECT_STREAM = MediaType.parse("application/octet-stream");

    public FinalHttp() {
        client = RetrofitClient.getClient();
        api = RetrofitClient.getRetrofit().create(ApiService4Final.class);
    }


    /**
     * 设置网络连接超时时间，默认为10秒钟
     *
     * @param timeout
     */
    public void configTimeout(int timeout) {
        // Copy to customize OkHttp for this request.
        client = client.newBuilder()
                .readTimeout(timeout, TimeUnit.MILLISECONDS)
                .build();
    }

    class FinalCommonResponseTransformer implements Observable.Transformer<ResponseBody, String> {
        @Override
        public Observable<String> call(Observable<ResponseBody> responseBodyObservable) {
            return responseBodyObservable
                    .map(new Func1<ResponseBody, String>() {
                        @Override
                        public String call(ResponseBody responseBody) {
                            try {
                                return responseBody.string();
                            } catch (IOException e) {
                                Log.e(TAG, e.toString());
                            }
                            return null;
                        }
                    }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    private static class CommonFinalResponse extends Subscriber<String> {
        private AjaxCallBack callBack;

        public CommonFinalResponse(AjaxCallBack callBack) {
            this.callBack = callBack;
        }

        @Override
        public void onStart() {
            super.onStart();
            LogUtils.i("currentThreadId="+Thread.currentThread().getId());
            callBack.onStart();
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
//            if (e instanceof ApiException) {
//                callBack.onSuccess(e.getMessage());
//            }
            //如果出现e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof HttpException
            //这些异常，将无法回调
            if (e instanceof ApiException) {
                callBack.onSuccess(e.getMessage());
            }else {
                 if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof HttpException) {
                        com.xywy.util.T.showShort(RetrofitClient.getContext(),"网络中断，请检查您的网络状态");
                 }
                callBack.onFailure(e,-100,e.getMessage());
            }
        }

        @Override
        public void onNext(String t) {
            postResp(callBack, t);
        }
    }

    //------------------get 请求-----------------------
    public void get(String url, AjaxCallBack callBack) {
        get(url, new AjaxParams(), callBack);
    }

    public void get(String url, AjaxParams params, final AjaxCallBack callBack) {

        api.get(url, params.getUrlParams())
                .compose(new FinalCommonResponseTransformer())
                .subscribe(new CommonFinalResponse(callBack));
//        Request request = new Request.Builder().url(getUrlWithQueryString(url,params)).get().build();
//        RetrofitClient.getClient().newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                LogUtils.ex(e);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    postResp(callBack,response.body().string());
//                }
//            }
//        });
    }

    private static Handler h = new Handler(Looper.getMainLooper());

    private static <T> void postResp(final AjaxCallBack callBack, final String resultStr) {

        BaseResultBean result = GsonUtils.toObj(resultStr, BaseResultBean.class);
        int code = result.getCode();
        if (!RetrofitCache.isResponseFromCache(result.getMsg())&&(40000 == code || 40001 == code)) {
            //弹出强制退出提示框
            ForceLogoutDialogUtils.showForceDialogWithLog(code, result.getMsg());
        } else {
            h.post(new Runnable() {
                @Override
                public void run() {
                    callBack.onSuccess(resultStr);
                }
            });
        }

    }

    /**
     * 上传文件
     *
     * @param requestUrl 接口地址
     * @param params     参数
     * @param callBack   回调
     */
    public void upLoadFile(String requestUrl, AjaxParams params, final AjaxCallBack callBack) {
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            //设置类型
            builder.setType(MultipartBody.FORM);
            //追加参数
            ConcurrentHashMap<String, File> fileParams = params.getFileParams();
            for (String key : fileParams.keySet()) {
                File file = fileParams.get(key);
                builder.addFormDataPart(key, file.getName(), createProgressRequestBody(MEDIA_OBJECT_STREAM, file, callBack));
            }

            ConcurrentHashMap<String, String> paramsMap = params.getUrlParams();
            for (String key : paramsMap.keySet()) {
                builder.addFormDataPart(key, paramsMap.get(key));
            }

            //创建RequestBody
            RequestBody body = builder.build();
            //创建Request
            final Request request = new Request.Builder().url(requestUrl).post(body).build();
            //单独设置参数 比如读取超时时间
            final Call call = client.newBuilder().writeTimeout(50, TimeUnit.SECONDS).build().newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    h.post(new Runnable() {
                        @Override
                        public void run() {

                            callBack.onFailure(e, 0, "上传失败");

                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        postResp(callBack, string);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * 创建带进度的RequestBody
     *
     * @param contentType MediaType
     * @param file        准备上传的文件
     * @param callBack    回调
     * @return
     */
    public RequestBody createProgressRequestBody(final MediaType contentType, final File file, final AjaxCallBack callBack) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return file.length();
            }

            long current = 0;

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                try {
                    source = Okio.source(file);
                    Buffer buf = new Buffer();
                    final long remaining = contentLength();

                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        current += readCount;
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onLoading(remaining, current);
                            }
                        });

                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        };
    }
    public Subscription postMap(String url, Map<String,String> params, final AjaxCallBack callBack) {
       return api.post(url, params)
                .compose(new FinalCommonResponseTransformer())
                .subscribe(new CommonFinalResponse(callBack));
    }
    //------------------post 请求-----------------------

    public void post(String url, AjaxParams params, final AjaxCallBack callBack) {
        postMap(url, params.getUrlParams(),callBack);
    }

    static final String TAG = "finalhttp";
    //---------------------下载---------------------------------------

    public Call download(String url, String target, boolean isResume, final AjaxCallBack callback) {

        final File file = new File(target);
        //stone 暂时去掉
//        if (file.exists()) {
//            callback.onSuccess(file);
//            return null;
//        }
        final Request request = new Request.Builder().url(url).build();
        final Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    final long total = response.body().contentLength();
                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onStart();
                        }
                    });
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
                        final long finalCurrent = current;
                        h.post(new Runnable() {
                            @Override
                            public void run() {

                                callback.onLoading(total, finalCurrent);

                            }
                        });
                    }
                    fos.flush();
                    h.post(new Runnable() {
                        @Override
                        public void run() {

                            callback.onSuccess(file);

                        }
                    });
                } catch (final IOException e) {
                    Log.e(TAG, e.toString());
                    h.post(new Runnable() {
                        @Override
                        public void run() {

                            callback.onFailure(e, 0, "下载失败");

                        }
                    });
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
                }
            }
        });
        return call;
    }


    public static String getUrlWithQueryString(String url, Map<String,String> params) {
        if (params != null) {
            String paramString ="";
            for(String s:params.keySet()){
                paramString=paramString+s+"="+params.get(s)+"&";
            }
            url += "?" + paramString;
        }
        return url;
    }
}
