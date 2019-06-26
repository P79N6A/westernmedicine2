package com.xywy.datarequestlibrary;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.xywy.datarequestlibrary.downloadandupload.OkDownloadUtils;
import com.xywy.datarequestlibrary.downloadandupload.listener.OkDownloadListener;
import com.xywy.datarequestlibrary.downloadandupload.listener.UIProgressListener;
import com.xywy.datarequestlibrary.model.BaseData;
import com.xywy.datarequestlibrary.model.CommonResponse;
import com.xywy.datarequestlibrary.paramtools.CommonParamUtils;
import com.xywy.datarequestlibrary.paramtools.CommonRequestParam;
import com.xywy.datarequestlibrary.paramtools.XywyHeaderHelper;
import com.xywy.datarequestlibrary.paramtools.XywyParamUtils;
import com.xywy.datarequestlibrary.service.XywyApiService;
import com.xywy.datarequestlibrary.service.XywyApiServiceProvider;
import com.xywy.datarequestlibrary.utils.GsonUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by bailiangjin on 2017/4/24.
 */
public class XywyDataRequestApi {

    /**
     * Application Context
     */
    private Context appContext;

    /**
     * 用于请求统一添加header
     */
    private XywyHeaderHelper helper;
    /**
     * 中间层公有参数
     */
    private CommonRequestParam commonRequestParam;


    /**
     * 中间层参数是否已经初始化
     */
    private boolean isAppParamInit;

    public boolean isDownLoad = false;


    /**
     * 单例实例
     */
    private static volatile XywyDataRequestApi instance = null;

    public static XywyDataRequestApi getInstance() {
        if (instance == null) {
            synchronized (XywyDataRequestApi.class) {
                if (instance == null) {
                    instance = new XywyDataRequestApi();
                }
            }
        }
        return instance;
    }


    /**
     * 初始化方法  使用本库请求中间层接口 之前必须初始化
     *
     * @param appContext         Application Context
     * @param commonRequestParam 公有参数
     * @param helper
     */
    public void init(Context appContext, CommonRequestParam commonRequestParam, XywyHeaderHelper helper) {
        this.appContext = appContext;
        this.helper = helper;
        this.commonRequestParam = commonRequestParam;
        this.isAppParamInit = true;
    }

    public XywyHeaderHelper getHelper() {
        return helper;
    }

    public CommonRequestParam getCommonRequestParam() {
        return commonRequestParam;
    }

    /**
     * 中间层参数是否已经初始化
     *
     * @return
     */
    public boolean isAppParamInit() {
        return isAppParamInit;
    }

    /**
     * 检测是否已经初始化 在请求
     */
    public static void checkAppParamInit() {
        if (!XywyDataRequestApi.getInstance().isAppParamInit()) {
            throw new RuntimeException("please init  data request library first");
        }
    }

    public Context getAppContext() {
        return appContext;
    }


    /**
     * get请求 方法
     *
     * @param baseUrl     基准url
     * @param portUrl     具体接口url后缀
     * @param getParamMap get参数 map
     * @param apiCode     api接口👌
     * @param apiVersion  api版本号
     * @param clazz       返回数据类型
     * @param subscriber  返回数据回调
     * @param <T>
     */
    public <T> void getRequest(String baseUrl, String portUrl, Map<String, String> getParamMap, String apiCode, String apiVersion, final Class<T> clazz, final Subscriber<T> subscriber) {
        getRequest(baseUrl + "/" + portUrl, getParamMap, apiCode, apiVersion, clazz, subscriber);
    }

    /**
     * post请求 方法 调用中间层接口使用
     *
     * @param baseUrl      基准url
     * @param portUrl      具体接口url后缀
     * @param getParamMap  get参数 map
     * @param postParamMap post参数 map
     * @param apiCode      api接口👌
     * @param apiVersion   api版本号
     * @param clazz        返回数据类型
     * @param subscriber   返回数据回调
     * @param <T>
     */
    public <T> void postRequest(String baseUrl, String portUrl, Map<String, String> getParamMap, Map<String, String> postParamMap, String apiCode, String apiVersion, final Class<T> clazz, final Subscriber<T> subscriber) {
        postRequest(baseUrl + "/" + portUrl, getParamMap, postParamMap, apiCode, apiVersion, clazz, subscriber);
    }

    /**
     * get请求 方法
     *
     * @param url         完整url
     * @param getParamMap get参数 map
     * @param apiCode     api接口👌
     * @param apiVersion  api版本号
     * @param clazz       返回数据类型
     * @param subscriber  返回数据回调
     * @param <T>
     */
    public <T> void getRequest(String url, Map<String, String> getParamMap, String apiCode, String apiVersion, final Class<T> clazz, final Subscriber<T> subscriber) {
        XywyDataRequestApi.getInstance().checkAppParamInit();
        if (null == getParamMap) {
            getParamMap = new HashMap<>();
        }
        getParamMap = XywyParamUtils.addMustParam(getParamMap, apiCode, apiVersion);
        String sign = XywyParamUtils.getSign(getParamMap);
        getParamMap.put("sign", sign);
        getRequestCommon(url, getParamMap, clazz, subscriber);

    }

    /**
     * post请求 方法 调用中间层接口使用
     *
     * @param url          完整url
     * @param getParamMap  get参数 map
     * @param postParamMap post参数 map
     * @param apiCode      api接口👌
     * @param apiVersion   api版本号
     * @param clazz        返回数据类型
     * @param subscriber   返回数据回调
     * @param <T>
     */
    private <T> void postRequest(String url, Map<String, String> getParamMap, Map<String, String> postParamMap, String apiCode, String apiVersion, final Class<T> clazz, final Subscriber<T> subscriber) {
        XywyDataRequestApi.getInstance().checkAppParamInit();
        if (null == getParamMap) {
            getParamMap = new HashMap<>();
        }
        if (null == postParamMap || postParamMap.isEmpty()) {
            // 转向调用get请求方法
            getRequest(url, getParamMap, apiCode, apiVersion, clazz, subscriber);
            return;
        }
        getParamMap = XywyParamUtils.addMustParam(getParamMap, apiCode, apiVersion);
        String sign = XywyParamUtils.getSign(getParamMap, postParamMap);
        getParamMap.put("sign", sign);

        postRequestCommon(url, getParamMap, postParamMap, clazz, subscriber);

    }


    /**
     * get请求 通用方法 未添加应用公用参数
     *
     * @param baseUrl     基准url
     * @param portUrl     具体接口url后缀
     * @param getParamMap get参数 map
     * @param clazz       返回数据类型
     * @param subscriber  返回数据回调
     * @param <T>
     */
    public <T> void getRequestCommon(String baseUrl, String portUrl, Map<String, String> getParamMap, final Class<T> clazz, final Subscriber<T> subscriber) {
        getRequestCommon(baseUrl + "/" + portUrl, getParamMap, clazz, subscriber);
    }

    /**
     * post请求 通用方法 未添加应用公用参数
     *
     * @param baseUrl      基准url
     * @param portUrl      具体接口url后缀
     * @param getParamMap  get参数 map
     * @param postParamMap post参数 map
     * @param clazz        返回数据类型
     * @param subscriber   返回数据回调
     * @param <T>
     */
    public <T> void postRequestCommon(String baseUrl, String portUrl, Map<String, String> getParamMap, Map<String, String> postParamMap, final Class<T> clazz, final Subscriber<T> subscriber) {
        postRequestCommon(baseUrl + "/" + portUrl, getParamMap, postParamMap, clazz, subscriber);
    }

    /**
     * post请求 通用方法 未添加应用公用参数
     *
     * @param url          完整url
     * @param getParamMap  get参数 map
     * @param postParamMap post参数 map
     * @param clazz        返回数据类型
     * @param subscriber   返回数据回调
     * @param <T>
     */
    public <T> void postRequestCommon(String url, Map<String, String> getParamMap, Map<String, String> postParamMap, final Class<T> clazz, final Subscriber<T> subscriber) {
        String urlGetPostFix = CommonParamUtils.generateGetUrlString(getParamMap);
        String fullUrl = url + urlGetPostFix;
        RxRequestHelper.requestNotDealCommonError(XywyApiServiceProvider.getApiService(XywyApiService.class).postRequest(fullUrl, postParamMap), new CommonResponse<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                onResponse(responseBody, clazz, subscriber);
            }
        });
    }


    /**
     * 任意GET请求方法 未添加应用相关参数
     *
     * @param url         完整url
     * @param getParamMap get参数 map
     * @param clazz       返回数据类型
     * @param subscriber  返回数据回调
     * @param <T>
     */
    public <T> void getRequestCommon(String url, Map<String, String> getParamMap, final Class<T> clazz, final Subscriber<T> subscriber) {
        String urlGetPostFix = CommonParamUtils.generateGetUrlString(getParamMap);
        String fullUrl = url + urlGetPostFix;

        RxRequestHelper.requestNotDealCommonError(XywyApiServiceProvider.getApiService(XywyApiService.class).getRequest(fullUrl), new CommonResponse<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                onResponse(responseBody, clazz, subscriber);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                subscriber.onError(e);
            }

        });
    }

    /**
     * 请求回调统一处理
     *
     * @param responseBody
     * @param clazz        返回数据Class 类型
     * @param subscriber   返回数据回调
     * @param <T>
     */
    private <T> void onResponse(ResponseBody responseBody, final Class<T> clazz, final Subscriber<T> subscriber) {
        if (null == responseBody) {
            subscriber.onError(new RuntimeException("服务端返回数据为空"));
            return;
        }
        String jsonString = null;
        try {
            jsonString = responseBody.string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(jsonString)) {
//            subscriber.onNext(null);
        } else {
            try {
                Object data = GsonUtils.toObj(jsonString, clazz);
                if (null == data) {
                    Toast.makeText(XywyDataRequestApi.getInstance().getAppContext().getApplicationContext(), "服务端返回数据格式异常", Toast.LENGTH_SHORT).show();
                    subscriber.onError(new RuntimeException("服务端返回数据 json 格式异常"));
                    return;
                }
                if (data instanceof BaseData) {
                    if ((10000 == ((BaseData) data).getCode())) {
                        subscriber.onNext(GsonUtils.toObj(jsonString, clazz));
                    } else {
                        Toast.makeText(XywyDataRequestApi.getInstance().getAppContext().getApplicationContext(), "服务端返回数据异常:" + ((BaseData) data).getMsg(), Toast.LENGTH_SHORT).show();
                        subscriber.onError(new RuntimeException("服务端返回数据异常:" + ((BaseData) data).getMsg()));
                    }
                } else {
                    subscriber.onNext(GsonUtils.toObj(jsonString, clazz));
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("异常", "服务端返回数据 json 格式异常:" + e.getMessage());
                subscriber.onError(new RuntimeException("服务端返回数据 json 格式异常:" + e.getMessage()));
            }

        }
    }

    /**
     * 下载文件方法
     * @param downloadFileUrl 下载文件 url
     * @param destFileDir 文件将要放置到的目录
     * @param okDownloadListener 文件下载监听 onSuccess中回调传 下载文件的具体位置
     */
    public void downloadFile(String downloadFileUrl, String destFileDir, final OkDownloadListener okDownloadListener){
        isDownLoad = true;
        OkDownloadUtils.downloadFile(downloadFileUrl, destFileDir, new UIProgressListener() {
            @Override
            public void onUIProgress(long currentBytes, long contentLength, boolean done) {
                okDownloadListener.onProgress(contentLength,currentBytes);
            }

            @Override
            public void onSuccess(String filePath) {
                //onSuccess中回调传 下载文件的具体位置
                isDownLoad = false;
                okDownloadListener.onSuccess(filePath);
            }

            @Override
            public void onFailure(Exception exception) {
                isDownLoad = false;
                okDownloadListener.onFailure(exception);
            }
        });

    }
}
