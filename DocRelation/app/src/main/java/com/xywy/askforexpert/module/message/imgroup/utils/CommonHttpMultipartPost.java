package com.xywy.askforexpert.module.message.imgroup.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.xywy.askforexpert.appcommon.net.utils.CustomMultipartEntity;
import com.xywy.askforexpert.appcommon.net.utils.CustomMultipartEntity.ProgressListener;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.im.group.UploadGroupImageResponse;

import net.bither.util.NativeUtil;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class CommonHttpMultipartPost extends AsyncTask<Map<String, String>, Integer, String> {

    private static final String TAG = "CommonHttpMultipartPost";
    private Context context;
    private List<String> filePathList;
    private ProgressDialog pd;
    private long totalSize;
    private String url;
    private Handler handler;
    private int tag;

    public CommonHttpMultipartPost(Context context, List<String> filePathList,
                                   String url, Handler handler, int tag) {
        this.context = context;
        this.filePathList = filePathList;
        this.url = url;
        this.handler = handler;
        this.tag = tag;
    }

    @Override
    protected void onPreExecute() {
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在上传图片...");
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    protected String doInBackground(Map<String, String>... params) {
        String serverResponse = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext httpContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(url);

        try {
            CustomMultipartEntity multipartContent = new CustomMultipartEntity(
                    new ProgressListener() {
                        @Override
                        public void transferred(long num) {
                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });

            filePathList = CompressImg(filePathList);
            for (int i = 0; i < filePathList.size(); i++) {
                multipartContent.addPart("Filedata" + (i + 1), new FileBody(
                        new File(filePathList.get(i)), "image/jpeg"));
            }
            Map paramMap = params[0];
            Iterator iterator = paramMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                if (key.equals("sign")) {
                    value = MD5Util.MD5(value + Constants.MD5_KEY);
                }
                addParam(multipartContent, key, value);
            }
            totalSize = multipartContent.getContentLength();
            httpPost.setEntity(multipartContent);
            HttpResponse response = httpClient.execute(httpPost, httpContext);
            serverResponse = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    private void addParam(CustomMultipartEntity multipartContent, String userId, String userIV) throws UnsupportedEncodingException {
        multipartContent.addPart(userId, new StringBody(userIV));
    }

    public List<String> CompressImg(final List<String> list) {
        final List<String> newFileList = new ArrayList<String>();
        final Long filesize = 2 * 1024 * 1024l;
        for (int i = 0; i < list.size(); i++) {

            int quality = 80;
            FileInputStream in = null;
            Bitmap bit;
            WeakReference<Bitmap> bmp = null;
            try {
                File img_path = new File(list.get(i));
                if (img_path.length() > filesize) {

                    in = new FileInputStream(img_path);
                    bit = BitmapFactory.decodeStream(in);
                    bmp = new WeakReference<>(bit);
                    String toPath = Environment.getExternalStorageDirectory()
                            + "/XYWY_COM_IMG/";
                    File dirFile = new File(toPath, "");
                    if (!dirFile.exists()) {
                        dirFile.mkdirs();
                    }
                    File jpegTrueFile = new File(dirFile, img_path.getName());
                    String model = android.os.Build.MODEL;

                    DLog.i(TAG, "手机型号" + model);
                    if (model.contains("SM")) {
                        FileOutputStream fileOutputStream = new FileOutputStream(
                                jpegTrueFile);
                        bmp.get().compress(CompressFormat.JPEG, 50,
                                fileOutputStream);
                    } else {
                        NativeUtil.compressBitmap(bmp.get(), quality,
                                jpegTrueFile.getAbsolutePath(), true);
                    }

                    DLog.i(TAG, "地址。。。"
                            + jpegTrueFile.getAbsolutePath());
                } else {
                    newFileList.add(list.get(i));
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (bmp != null) {
                    bmp.get().recycle();
                }

                bit = null;
            }

        }
        return newFileList;
    }

    public String CompressOne_Img(String str) {


        try {
            int quality = 90;
            FileInputStream in;
            File img_path = new File(str);
            in = new FileInputStream(img_path);
            Bitmap bit = BitmapFactory.decodeStream(in);
            WeakReference<Bitmap> bmp = new WeakReference<>(bit);
            String toPath = Environment.getExternalStorageDirectory()
                    + "/XYWY_COM_IMG/";
            File dirFile = new File(toPath, "");
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            File jpegTrueFile = new File(dirFile, img_path.getName());
            String model = android.os.Build.MODEL;
            DLog.i(TAG, "手机型号" + model);
            if (model.contains("SM")) {
                FileOutputStream fileOutputStream = new FileOutputStream(
                        jpegTrueFile);
                bmp.get().compress(CompressFormat.JPEG, 50,
                        fileOutputStream);
            } else {
                NativeUtil.compressBitmap(bmp.get(), quality,
                        jpegTrueFile.getAbsolutePath(), true);
            }

            return jpegTrueFile.getAbsolutePath();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return str;
        }

    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        pd.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        LogUtils.e("上传图片返回数据 " + result);
        if (!TextUtils.isEmpty(result)) {
            UploadGroupImageResponse uploadGroupImageResponse = GsonUtils.toObj(result,UploadGroupImageResponse.class);
            Message msg = new Message();
            msg.obj = uploadGroupImageResponse;
            msg.what = tag;
            handler.sendMessage(msg);
            pd.dismiss();
        } else {
            pd.dismiss();
        }
    }

    @Override
    protected void onCancelled() {
    }

}
