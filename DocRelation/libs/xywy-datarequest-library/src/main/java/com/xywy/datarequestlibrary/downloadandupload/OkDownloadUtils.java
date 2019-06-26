package com.xywy.datarequestlibrary.downloadandupload;

import android.util.Log;

import com.xywy.datarequestlibrary.downloadandupload.listener.UIProgressListener;
import com.xywy.datarequestlibrary.paramtools.MD5;
import com.xywy.datarequestlibrary.tools.XywyOkHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by bailiangjin on 2017/5/8.
 */

public class OkDownloadUtils {

    private static final String TAG = "OkDownloadUtils";


    public static void downloadFile(String downLoadFileUrl, final String destFileDir, final UIProgressListener uiProgressListener) {

        final String fileName = MD5.generateSign(downLoadFileUrl);
        final File file = new File(destFileDir, fileName);
        if (file.exists()) {
//            uiProgressListener.onSuccess(file.getAbsolutePath());
//            return;
            file.delete();
        }
        //构造请求
        final Request downLoadRequest = new Request.Builder()
                .url(downLoadFileUrl)
                .build();

        //包装Response使其支持进度回调
        ProgressHelper.addProgressResponseListener(XywyOkHttpClient.INSTANCE.getOkHttpClient(), uiProgressListener).newCall(downLoadRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "error ", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.e("TAG", response.body().string());

                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    long total = response.body().contentLength();
                    Log.d(TAG, "total------>" + total);
                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
                        Log.d(TAG, "current------>" + current);
                    }
                    fos.flush();
                    uiProgressListener.onSuccess(file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                    uiProgressListener.onFailure(e);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, e.toString());
                    }
                }
            }

        });
    }
}
