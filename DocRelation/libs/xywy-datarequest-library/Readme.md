# xywy-datarequest-library使用说明

## 1 初始化
### 1.1 初始化方法
```
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
           this.isInit = true;
       }
```


### 1.2 参数说明
```
public class CommonRequestParam {

    private String source;
    private String pro;
    private String signKey;
    private String os = "android";

    public CommonRequestParam(String source, String pro, String signKey) {
        this.source = source;
        this.pro = pro;
        this.signKey = signKey;
    }
}

```
### 1.3 调用方法
```
 XywyDataRequestApi.getInstance().init(appContext,new CommonRequestParam(Constants.SOURCE_VALUE,Constants.PRO_VALUE,Constants.MD5_SIGN_KEY_VAULE),new XywyHeaderHelper() {
            @Override
            public void addHeader(Request.Builder requestBuilder) {
                //如果请求需要统一添加header 在此处添加 不需要请空实现
            }
        });

```

## 2 使用网络请求

### 2.1 说明

#### 2.1.1 调用中间层接口
```
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
```

#### 2.1.2 调用任意接口

```
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
```


#### 2.1.3 下载文件
```
/**
     * 下载文件方法
     * @param downloadFileUrl 下载文件 url
     * @param destFileDir 文件将要放置到的目录
     * @param okDownloadListener 文件下载监听 onSuccess中回调传 下载文件的具体位置
     */
    public void downloadFile(String downloadFileUrl, String destFileDir, final OkDownloadListener okDownloadListener){

        OkDownloadUtils.downloadFile(downloadFileUrl, destFileDir, new UIProgressListener() {
            @Override
            public void onUIProgress(long currentBytes, long contentLength, boolean done) {
                okDownloadListener.onProgress(contentLength,currentBytes);
            }

            @Override
            public void onSuccess(String filePath) {
                super.onSuccess(filePath);
                //onSuccess中回调传 下载文件的具体位置
                okDownloadListener.onSuccess(filePath);
            }

            @Override
            public void onFailure(Exception exception) {
                super.onFailure(exception);
                okDownloadListener.onFailure(exception);
            }
        });

    }
```

### 2.2 使用示例

#### 2.2.1 GET请求示例
```
    public static void getHotWord(int top, Subscriber<HotWrodRspEntity> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("top", top + "");
        XywyDataRequestApi.getInstance().getRequest(Constant.PRIMARY_URL,Constant.METHOD_GET_HOT_WORD, getParams, "1584", "1.0", HotWrodRspEntity.class,subscriber);
    }

```


#### 2.2.2 POST请求示例

与get类似 区别为 多以 postParam Map

#### 2.2.3 文件下载 使用示例

```
 XywyDataRequestApi.getInstance().downloadFile(downloadFileUrl, FilePathUtil.getSdcardPath(), new OkDownloadListener() {
                    @Override
                    public void onProgress(long totalBytes, long currentBytes) {
                        if(0!=totalBytes){
                            shortToast("下载进度："+currentBytes*100/totalBytes);
                        }
                    }

                    @Override
                    public void onSuccess(String absoluteFilePath) {

                        shortToast("下载成功："+absoluteFilePath);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        shortToast("下载异常："+e.getMessage());
                    }
                });

```


#### 2.2.4 其他使用方式说明

以上示例为调用中间层接口时的调用方式;
如果调用任意接口时调用 getRequestCommon()和postRequestCommon();
区别在于调用任意接口时位添加 应用请求中间层使用的 source pro sign os 等参数，需要使用者自己处理相关参数并按属性分别放到getparam和postparam中

#### 2.2.5 注意事项
调用时所用的url需为纯url，不能出现？和get参数 get参数请放到 getParam Map中，实际请求时，数据请求库会根据传入的getParam计算生成带？和get参数的url




