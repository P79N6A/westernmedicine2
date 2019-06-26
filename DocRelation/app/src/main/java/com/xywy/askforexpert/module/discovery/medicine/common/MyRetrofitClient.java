package com.xywy.askforexpert.module.discovery.medicine.common;

import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.retrofit.RetrofitClient;
import com.xywy.retrofit.net.XywyCallAdapterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xgxg on 2017/10/26.
 * 由于使用了两个baseurl，为了区分，这里将用药助手的baseurl单独在传一次
 */

public class MyRetrofitClient {
    public static String s = "/api.php/";
    //由于app.build中配置的url后缀多了/api.php/，这里将其去掉
    public static  String BASE_URL = CommonUrl.WWS_XYWY_BASE_URL.replace(s,"");
    private static OkHttpClient client = RetrofitClient.getClient();
    private static Retrofit retrofit ;


    public static Retrofit getMyRetrofit(){
        if(null == retrofit){
            retrofit = new Retrofit.Builder()
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(XywyCallAdapterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(BASE_URL)
                    .build();
        }
        return retrofit;
    }
}
