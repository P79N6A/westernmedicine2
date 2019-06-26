package com.xywy.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 程序拦截异常
 *
 * @author 王鹏
 * @2015-5-27下午5:09:56
 */
public class CrashHandler implements UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";
    private static CrashHandler instance;
    private UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;
    private Map<String, String> infos = new HashMap<String, String>();
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);

    private Thread mThread = null;
    private Throwable mEx = null;

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    /**
     * 初始化
     */
    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        LogUtils.e(ex.toString());
        if (AppUtils.isDebug(mContext)){
            Log.e(TAG, Log.getStackTraceString(ex));
        }
        if (!handleException(ex) && mDefaultHandler != null) {

            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            AppUtils.restart(mContext);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
        collectDeviceInfo(mContext);

//        new Thread() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                YMApplication.getInstance().appExit();
////				YMApplication.Trace("错误日志"+ex);
//                YMUserService.ymLogout();
//
//                Looper.loop();
//            }
//        }.start();
        saveCatchInfo2File(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
//        try {
//            PackageManager pm = ctx.getPackageManager();
//            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
//                    PackageManager.GET_ACTIVITIES);
//            if (pi != null) {
//                String versionName = pi.versionName == null ? "null"
//                        : pi.versionName;
//                String versionCode = pi.versionCode + "";
//                infos.put("versionName", versionName);
//                infos.put("versionCode", versionCode);
//            }
//        } catch (NameNotFoundException e) {
//            Log.e(TAG, "an error occured when collect package info", e);
//        }catch (Exception e){
//            Log.e(TAG, "an error occured", e);
//        }

        infos.put("versionName", BuildConfig.VERSION_NAME);
        infos.put("versionCode", BuildConfig.VERSION_CODE+"");
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }
    public static String fileDir = Environment.getExternalStorageDirectory()+"/crash/";
    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCatchInfo2File(Throwable ex) {

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=").append(value).append("\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "XYWYcrash-" + time + "-" + timestamp + ".txt";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                String path = "/mnt/sdcard/crash/";
                File dir = new File(fileDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(fileDir + fileName);
                fos.write(sb.toString().getBytes());
                // 发送给开发人员
                sendCrashLog2PM(fileDir + fileName);
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occurred while writing file...", e);
        }
        return null;
    }

    /**
     * 将捕获的导致崩溃的错误信息发送给开发人员
     * <p>
     * 目前只将log日志保存在sdcard 和输出到LogCat中，并未发送给后台。
     */
    private void sendCrashLog2PM(String fileName) {
        if (!new File(fileName).exists()) {
            Toast.makeText(mContext, "日志文件不存在！", Toast.LENGTH_SHORT).show();
            return;
        }

    }

}