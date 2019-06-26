package com.xywy.component.datarequest.uploadLog;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.xywy.component.datarequest.network.RequestManager;
import com.xywy.component.datarequest.neworkWrapper.BaseData;
import com.xywy.component.datarequest.neworkWrapper.IDataResponse;
import com.xywy.component.datarequest.tool.DataRequestTool;
import com.xywy.component.datarequest.uploadLog.bean.LogBean;
import com.xywy.component.datarequest.uploadLog.bean.UploadBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/8/15 15:02
 */
public class LogImpl implements ILog {
    static final int MAX_FILE_LENGTH = 200 * 1024;//超过此长度即应该上传
    final File LOG_FILE;
    private final Handler handler;
    private final Context context;
    private volatile boolean isUpLoading = false;
    private Queue<String> q = new ArrayDeque<>();
    private static String url="";
    public LogImpl(Context ctx, Handler handler) {
        this.context = ctx.getApplicationContext();
        this.handler = handler;
        LOG_FILE = new File(ctx.getFilesDir().getAbsolutePath() + File.separator + "uploadlog.txt");
        if (RequestManager.isDebug()){
            url="http://test.api.wws.xywy.com/api.php/";
        }else {
            url="http://api.wws.xywy.com/api.php/";
        }
    }

    private static byte[] createParams(Context ctx, List<LogBean> logs) {
        UploadBean bean = new UploadBean();
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                bean.setAppName(pi.packageName);
                bean.setAppVersion(pi.versionName);
                bean.setDevice(Build.FINGERPRINT);
                bean.setLogs(logs);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("UploadLog", "an error occured when collect package info", e);
        }
        return new Gson().toJson(bean).getBytes();
    }

    //每条log占一行
    public void writeLog(String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        if (!LOG_FILE.exists()) {
            createFile();
        }
        content.replace("\n", "");
        //如果文件长度超过缓存最大容量，先上传
        if (LOG_FILE.length() > MAX_FILE_LENGTH) {
            uploadLog();
        }
        //当日志正在上传时将日志先缓存到内存队列中
        if (isUpLoading) {
            q.add(content);
            return;
        }
        if (!content.endsWith("\n")) {
            content = content + "\n";
        }
        RandomAccessFile randomFile = null;
        try {
            // 打开一个随机访问文件流，按读写方式
            randomFile = new RandomAccessFile(LOG_FILE, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 将写文件指针移到文件尾
            randomFile.seek(fileLength);
            randomFile.write(content.getBytes("UTF-8"));
            randomFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void uploadLog() {
        if (!LOG_FILE.exists() || LOG_FILE.length() == 0) {
            return;
        }
        isUpLoading = true;
        IDataResponse callback = new IDataResponse() {
            @Override
            public void onResponse(final BaseData obj) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (DataRequestTool.noError(obj)) {
                            LOG_FILE.delete();
                        }
                        isUpLoading = false;
                        //将上传期间缓存在内存中的log写入disk
                        while (q.peek() != null) {
                            String str = q.poll();
                            writeLog(str);
                        }

                    }
                });
            }
        };

        DataRequestTool.gzip(url, "appLogs/index", createParams(context, readUploadLog()),
                callback, null, null);
    }

    private void createFile() {
        if (!LOG_FILE.exists()) {
            try {
                LOG_FILE.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeLog(int start, int end) {
        try {
//Construct the new file that will later be renamed to the original filename.
            File tempFile = new File(LOG_FILE.getAbsolutePath() + ".tmp");

            BufferedReader br = new BufferedReader(new FileReader(LOG_FILE));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

            String line = null;
            int count = 0;
//Read from the original file and write to the new
//unless content matches data to be removed.
            while ((line = br.readLine()) != null) {

                if (count < start || count > end) {
                    pw.println(line);
                    pw.flush();
                }
                count++;
            }
            pw.close();
            br.close();

//Delete the original file
            if (!LOG_FILE.delete()) {
                throw new RuntimeException("Could not delete file");
            }

//Rename the new file to the filename the original file had.
            if (!tempFile.renameTo(LOG_FILE)) {
                throw new RuntimeException("Could not rename file");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private List<LogBean> readUploadLog() {
        List<LogBean> list = new ArrayList<>();
        int start = 0;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(LOG_FILE));
            String line = null;
            int count = 0;
            //StringBuilder builder=new StringBuilder();
            while ((line = br.readLine()) != null) {
                if (count >= start) {
                    //builder.append(line);
                    list.add(new Gson().fromJson(line, LogBean.class));
                }
                count++;
            }
            //return builder.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JsonSyntaxException ex){
            //json格式解析异常，删除log文件
            LOG_FILE.delete();
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //return new Gson().toJson(list);
        return list;
    }

//    @Deprecated
//    private int getUploadLine(){
//       return context.getSharedPreferences("uploadLog",Context.MODE_PRIVATE).getInt("uploadLine",0);
//    }
//    @Deprecated
//    private void writeUploadLine(int uploadLine){
//        context.getSharedPreferences("uploadLog",Context.MODE_PRIVATE).edit()
//                .putInt("uploadLine",uploadLine).commit();
//    }
}
