package com.xywy.askforexpert.appcommon.net.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.DeviceUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by bailiangjin on 16/9/6.
 */
public class CommonNetUtils {

    private static Map<String, String> headerMap = new HashMap<>();


    public static void addHeader(OkHttpClient.Builder builder) {
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();
                final Map<String, String> headerMap = getHeaderMapWhenUse();
                for (String headerKey : headerMap.keySet()) {
                    requestBuilder.header(headerKey, headerMap.get(headerKey));
                }
                requestBuilder.method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
    }

    public static void addHeader(Request.Builder requestBuilder) {


        final Map<String, String> headerMap = getHeaderMapWhenUse();
        for (String headerKey : headerMap.keySet()) {
            requestBuilder.header(headerKey, headerMap.get(headerKey));
        }

    }

    @NonNull
    private static Map<String, String> getHeaderMapWhenUse() {
        headerMap.put("Accept-Encoding", "gzip");
        headerMap.put("Accept-Encoding", "identity");
        headerMap.put("Yimai-Request", AppUtils.getAPPInfo());
        headerMap.put("Yimai-Code", TextUtils.isEmpty(DeviceUtils.getIMei()) ? "" : DeviceUtils.getIMei());
        headerMap.put("Yimai-Token", TextUtils.isEmpty(YMUserService.getUserToken()) ? "" : YMUserService.getUserToken());
        return headerMap;
    }
}