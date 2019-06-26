package com.xywy.askforexpert.appcommon.net.retrofitTools;

import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.CommonNetUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.base.BaseBean;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/5/20 14:18
 */
@Deprecated
public class RetrofitUtil {
    private static String apiBaseUrl = CommonUrl.BASE_HOST;

    private static RetrofitUtil mInstance;

    /**
     * Log Interceptor
     */
    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

    private static OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder();

    private static Retrofit.Builder retrofit = new Retrofit.Builder()
            .baseUrl(apiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create());


    public RetrofitUtil() {
    }

    public static RetrofitUtil getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitUtil.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitUtil();
                }
            }
        }

        return mInstance;
    }

    public static <S> S createService(Class<S> serviceClass) {

        CommonNetUtils.addHeader(httpClient);

        //TODO stone 测试与线上要修改
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

        httpClient.addInterceptor(loggingInterceptor);
        return retrofit.client(httpClient.build()).build().create(serviceClass);
    }

    /**
     * Token Authentication
     */
    public static <S> S createService(Class<S> serviceClass, final String authToken) {
        if (authToken != null) {
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", authToken)
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }

        //TODO stone 测试与线上要修改
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

        httpClient.addInterceptor(loggingInterceptor);
        return retrofit.client(httpClient.build()).build().create(serviceClass);
    }

    public static void changeApiBaseUrl(String newBaseUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(newBaseUrl)
                .addConverterFactory(GsonConverterFactory.create());
    }

    /**
     * 检查Retrofit http请求信息
     *
     * @param request Retrofit Request
     */
    public static void checkRetrofitRequest(Request request) {
        if (request != null) {
            String method = request.method();
            Headers headers = request.headers();
            RequestBody body = request.body();
            HttpUrl url = request.url();
            DLog.d("checkRetrofitRequest", "request method = " + method
                    + "\nheaders = " + headers.toString()
                    + "\nrequest body = " + body
                    + "\nrequest url = " + url);
        }
    }

    /**
     * OAuth
     */
//    public static <S> S createService(Class<S> serviceClass, final AccessToken token) {
//        if (token != null) {
//            httpClient.addInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Request original = chain.request();
//
//                    Request.Builder requestBuilder = original.newBuilder()
//                            .header("Accept", "application/json")
//                            .header("Authorization",
//                                    token.getTokenType() + " " + token.getAccessToken())
//                            .method(original.method(), original.body());
//
//                    Request request = requestBuilder.build();
//                    return chain.proceed(request);
//                }
//            });
//        }
//
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        httpClient.addInterceptor(loggingInterceptor);
//        return retrofit.client(httpClient.build()).build().create(serviceClass);
//    }

//    /**
//     * 上传文件
//     *
//     * @param context           上下文
//     * @param bodyName          bodyName
//     * @param descriptionString 描述
//     * @param fileUri           文件uri
//     */
//    public static void uploadFile(final Context context, String bodyName, String descriptionString,
//                                  Uri fileUri) {
//
//        RetrofitServices.FileUploadService service = createService(RetrofitServices.FileUploadService.class);
//
//        File file = new File(fileUri.getPath());
//
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//
//        MultipartBody.Part body = MultipartBody.Part.createFormData(bodyName, file.getName(), requestFile);
//
//        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);
//
//        Call<ResponseBody> call = service.upload(description, body);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                if (response.isSuccessful()) {
//                    Log.d("uploadFile", "success");
//                    Toast.makeText(context, "upload file success", Toast.LENGTH_SHORT).show();
//                } else {
//                    Log.d("uploadFile", "failed");
//                    Toast.makeText(context, "upload file failed", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.d("uploadFile error", t.getMessage());
//                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
    public static void connectTimeOut(long time, TimeUnit timeUnit) {
        httpClient.connectTimeout(time, timeUnit);
    }

    /**
     * 请求数据
     */
    public <D> void enqueueCall(Call<BaseBean<D>> call, final HttpRequestCallBack<D> callBack) {
        call.enqueue(new Callback<BaseBean<D>>() {
            @Override
            public void onResponse(Call<BaseBean<D>> call, retrofit2.Response<BaseBean<D>> response) {
                if (BuildConfig.DEBUG) {
                    checkRetrofitRequest(call.request());
                }

                if (response.isSuccessful()) {
                    BaseBean<D> baseBean = response.body();
                    if (baseBean != null) {
                        if (baseBean.getCode().equals("10000") || baseBean.getCode().equals("0")) {
                            D data = baseBean.getData();
                            D list = baseBean.getList();
                            if (data != null || list != null) {
                                if (callBack != null) {
                                    callBack.onSuccess(baseBean);
                                }
                            } else {
                                if (callBack != null) {
                                    callBack.onFailure(HttpRequestCallBack.RequestState.STATE_NULL_DATA, "返回数据为空");
                                }
                            }
                        } else {
                            if (callBack != null) {
                                callBack.onFailure(HttpRequestCallBack.RequestState.STATE_ERROR_CODE, baseBean.getMsg());
                            }
                        }
                    } else {
                        if (callBack != null) {
                            callBack.onFailure(HttpRequestCallBack.RequestState.STATE_NULL_BASEBEAN, "返回数据为空");
                        }
                    }
                } else {
                    if (callBack != null) {
                        callBack.onFailure(HttpRequestCallBack.RequestState.STATE_REQUEST_FAILED, "请求失败");
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseBean<D>> call, Throwable t) {
                if (BuildConfig.DEBUG) {
                    checkRetrofitRequest(call.request());
                }

                if (callBack != null) {
                    callBack.onFailure(HttpRequestCallBack.RequestState.STATE_ON_FAILURE, t.getMessage());
                }
            }
        });
    }

}
