package com.xywy.retrofit;

import android.content.Context;

import com.xywy.retrofit.net.XywyCallAdapterFactory;
import com.xywy.util.L;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/9/8 14:21
 */
public class RetrofitClient {
    private static OkHttpClient client;
    private static Retrofit retrofit;
    public static  String BASE_URL ;
    private static final TimeUnit timeUnit =TimeUnit.SECONDS;
    public static WeakReference<Context> sContext;
    public static ICommonRespHandler sHandleCommonResp;
    public static final String FORCE_NETWORK = "Cache-Control: no-cache";
    public static Context getContext() {
        return sContext.get();
    }

    public interface ICommonRespHandler{
        boolean handleSuccess(Object data);
        boolean handleError(Throwable t);
    }

    public static void init(String url, Context context, ICommonRespHandler handleCommonResp, Interceptor interceptor){
        if (BASE_URL!=null){
            return;
        }
        sContext=new WeakReference<>(context);
        sHandleCommonResp= handleCommonResp;
        BASE_URL=url;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        //TODO stone 测试与线上要修改
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        logging.setLevel(HttpLoggingInterceptor.Level.NONE);

        client = new OkHttpClient.Builder()
                //.cache(cache)  //禁用okhttp自身的的缓存
                .connectTimeout(10, timeUnit)
                .readTimeout(20, timeUnit)
                .writeTimeout(10, timeUnit)
                .retryOnConnectionFailure(true)
                .addInterceptor(logging)
                .addInterceptor(interceptor)
                .build();
        retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(XywyCallAdapterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

    }

    public static OkHttpClient getClient() {
        if (client==null){
            throw new NullPointerException("Call init() first");
        }
        return client;
    }


    public static Retrofit getRetrofit() {
        if (retrofit==null){
            throw new NullPointerException("Call init() first");
        }
        return retrofit;
    }

    /**
     * 其实也是将File封装成RequestBody，然后再封装成Part，<br>
     * 不同的是使用MultipartBody.Builder来构建MultipartBody
     * @param fileName 文件名
     * @param file 文件
     * @param imageType 文件类型
     */
    public static MultipartBody.Builder addFiles(String fileName,File file,
                                                     MediaType imageType) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        RequestBody requestBody = RequestBody.create(imageType, file);
        builder.addFormDataPart(fileName, file.getName(), requestBody);
        builder.setType(MultipartBody.FORM);
        return builder;
    }
    /**
     * 添加文本类型的Part到的MultipartBody.Builder中
     * @param builder 用于构建MultipartBody的Builder
     * @param key 参数名（name属性）
     * @param value 文本内容
     */
    public static MultipartBody.Builder addTextPart(MultipartBody.Builder builder,
                                                    Map<String,String> map) {

        for (String key:map.keySet()){
            RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), map.get(key));
            // MultipartBody.Builder的addFormDataPart()有一个直接添加key value的重载，但坑的是这个方法
            // 不会设置编码类型，会出乱码，所以可以使用3个参数的，将中间的filename置为null就可以了
            // builder.addFormDataPart(key, value);
            // 还有一个坑就是，后台取数据的时候有可能是有顺序的，比如必须先取文本后取文件，
            // 否则就取不到（真弱啊...），所以还要注意add的顺序
            builder.addFormDataPart(key, null, requestBody);
        }
        return builder;
    }
    /**
     * 创建带进度的RequestBody
     *
     * @param contentType MediaType
     * @param file        准备上传的文件
     * @param callBack    回调
     * @return
     */
    public static RequestBody createProgressRequestBody(final MediaType contentType, final File file, final Subscriber callBack) {
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
                    final long total = contentLength();

                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        current += readCount;
                        callBack.onNext(current*100/total);
                    }
                } catch (Exception e) {
                    L.ex(e);
                }
            }
        };
    }

}

