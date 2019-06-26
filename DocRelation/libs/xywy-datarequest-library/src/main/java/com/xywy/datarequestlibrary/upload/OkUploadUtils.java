package com.xywy.datarequestlibrary.upload;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.xywy.datarequestlibrary.XywyDataRequestApi;
import com.xywy.datarequestlibrary.downloadandupload.ProgressHelper;
import com.xywy.datarequestlibrary.downloadandupload.listener.UIProgressListener;
import com.xywy.datarequestlibrary.tools.XywyOkHttpClient;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by bailiangjin on 2017/5/18.
 */

public class OkUploadUtils {

    private String TAG = "OkUploadUtils";
    private String uploadUrl = "http://121.41.119.107:81/test/result.php";
    private String filePath = "/sdcard/1.doc";

    //这个是ui线程回调，可直接操作UI
    final UIProgressListener uiProgressRequestListener = new UIProgressListener() {
        @Override
        public void onUIProgress(long bytesWrite, long contentLength, boolean done) {
            Log.e("TAG", "bytesWrite:" + bytesWrite);
            Log.e("TAG", "contentLength" + contentLength);
            Log.e("TAG", (100 * bytesWrite) / contentLength + " % done ");
            Log.e("TAG", "done:" + done);
            Log.e("TAG", "================================");
            //ui层回调
            //uploadProgress.setProgress((int) ((100 * bytesWrite) / contentLength));
            //Toast.makeText(getApplicationContext(), bytesWrite + " " + contentLength + " " + done, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onUIStart(long bytesWrite, long contentLength, boolean done) {
            super.onUIStart(bytesWrite, contentLength, done);
            Toast.makeText(XywyDataRequestApi.getInstance().getAppContext(), "start", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onUIFinish(long bytesWrite, long contentLength, boolean done) {
            super.onUIFinish(bytesWrite, contentLength, done);
            Toast.makeText(XywyDataRequestApi.getInstance().getAppContext(), "end", Toast.LENGTH_SHORT).show();
        }
    };


    private void upload(String uploadUrl, String filePath, Map<String, String> paramMap, final UIProgressListener uiProgressRequestListener) {
        if (TextUtils.isEmpty(uploadUrl)) {
            throw new RuntimeException("uploadUrl can not be empty");
        }

        if (TextUtils.isEmpty(filePath)) {
            throw new RuntimeException("upload filepath can not be empty");
        }


        File file = new File(filePath);

        if (file.isDirectory()) {
            throw new RuntimeException("upload file can not be file directory");
        }

        if (!file.exists()) {
            throw new RuntimeException("upload file not exist");
        }
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (null != paramMap && !paramMap.isEmpty()) {
            String key;
            String value;

            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                key = entry.getKey();
                value = entry.getValue();
                if (!TextUtils.isEmpty(key)) {
                    builder.addFormDataPart(key, TextUtils.isEmpty(value) ? "" : value);
                }
            }
        }



        //构造上传请求，类似web表单
        RequestBody requestBody = builder
                .addFormDataPart("file", file.getName(), RequestBody.create(null, file))
                .addPart(Headers.of("Content-Disposition", "form-data; name=\"uploadFile\";filename=\""+file.getName()+"\""), RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .build();

        //进行包装，使其支持进度回调
        final Request request = new Request.Builder().url(uploadUrl).post(ProgressHelper.addProgressRequestListener(requestBody, uiProgressRequestListener)).build();
        //开始请求
        XywyOkHttpClient.INSTANCE.getOkHttpClient().newCall(request).enqueue(new Callback() {


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("TAG", response.body().string());
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "error ", e);
                e.printStackTrace();
                uiProgressRequestListener.onFailure(e);
            }


        });

    }

}
