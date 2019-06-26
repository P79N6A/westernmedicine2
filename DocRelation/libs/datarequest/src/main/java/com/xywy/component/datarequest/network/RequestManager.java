package com.xywy.component.datarequest.network;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ResponseDelivery;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.google.gson.Gson;
import com.xywy.component.datarequest.uploadLog.UploadLogClient;

import java.io.File;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

public class RequestManager {

    private static final int DEFAULT_TIMEOUT_TIMER = 10000;
    final static String DEFAULT_CACHE_DIR = "volley";
    private static RequestQueue mRequestQueue;
    private static boolean isDebug=false;//判断app Module的buildtype
    private RequestManager() {
    }


    public static void init(Context context) {
        init(context,false);
    }
    public static void init(Context context,boolean uploadLog) {
        try {
            Class build=Class.forName(context.getPackageName()+".BuildConfig");
            String build_type= (String) build.getDeclaredField("BUILD_TYPE").get(build);
            if (build_type.equalsIgnoreCase("release")){
                isDebug=false;
            }else {
                isDebug=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mRequestQueue == null) {
            //此处使用Delivery替换volley自带的
            mRequestQueue=initRequestQueue(context,new Delivery(new Handler(Looper.getMainLooper())));
        }
        if (uploadLog){
            UploadLogClient.init(context);
        }
    }

    public static void initTest(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = initRequestQueue(context,new Delivery(Executors.newSingleThreadExecutor()));
        }
    }

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    public static void addRequest(Request<?> request, Object tag) {
        if (mRequestQueue == null) {
            throw new IllegalStateException("RequestQueue not initialized");
        }

        //AssertDebug.Assert(request);

        if (tag != null) {
            request.setTag(tag);
        }

        if (request.getRetryPolicy() == null) {
            request.setRetryPolicy(new DefaultRetryPolicy(
                    DEFAULT_TIMEOUT_TIMER,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }

        mRequestQueue.add(request);
    }

    public static void cancelAll(Object tag) {
        if (mRequestQueue == null) {
            throw new IllegalStateException("RequestQueue not initialized");
        }
        mRequestQueue.cancelAll(tag);
    }

    public static void clearCache(Request req) {
        if (mRequestQueue == null) {
            throw new IllegalStateException("RequestQueue not initialized");
        }
        if (req != null && mRequestQueue.getCache() != null) {
            mRequestQueue.getCache().remove(req.getCacheKey());
        }
    }

    public static RequestQueue initRequestQueue(Context context,ResponseDelivery responseDelivery) {
        File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);
        String userAgent = "volley/0";
        try {
            String packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            userAgent = packageName + "/" + info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        //Build.VERSION.SDK_INT >= 9
        HttpStack stack = new HurlStack();
        Network network = new BasicNetwork(stack);
        RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir), network,4,responseDelivery);
        queue.start();
        return queue;
    }

    /**
     *
     * @param req   请求对象
     * @param resp  服务器返回数据
     * @param <T>  resp 的类型
     */
    public static<T> void putCache(Request req,T resp){
        if (mRequestQueue == null) {
            throw new IllegalStateException("RequestQueue not initialized");
        }
        Cache.Entry entry=new Cache.Entry();
        entry.data =new Gson().toJson(resp).getBytes(Charset.forName("UTF-8"));
        entry.ttl = Long.MAX_VALUE;
        entry.softTtl=0;//表示数据需要被刷新
        mRequestQueue.getCache().put(req.getCacheKey(),entry);
    }

    public static boolean isDebug() {
        return isDebug;
    }
}
