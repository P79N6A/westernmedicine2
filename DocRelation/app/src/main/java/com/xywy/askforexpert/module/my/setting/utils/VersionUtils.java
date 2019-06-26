package com.xywy.askforexpert.module.my.setting.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.umeng.analytics.AnalyticsConfig;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.retrofitTools.HttpRequestCallBackImpl;
import com.xywy.askforexpert.appcommon.net.retrofitTools.RetrofitServices;
import com.xywy.askforexpert.appcommon.net.retrofitTools.RetrofitUtil;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.appcommon.utils.update.UpdateAppUtils;
import com.xywy.askforexpert.model.base.BaseBean;
import com.xywy.askforexpert.model.update.VersionUpdateData;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;

/**
 * 版本更新
 * <p/>
 * modified by Jack Fang @Time 2016/07/27
 */
public final class VersionUtils {

    private static VersionUtils mVersionUtils;
    private static boolean showToast;
    private final SharedPreferences sp;
    int total = 0;
    java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
    NotificationCompat.Builder mBuilder;
    private Activity activity;
    //    private ProgressDialog pd;
    private String downAPKname = "D_Platform.apk";
    private File filePath;
    private VersionUpdateData upDate;
    private NotificationManager notificationManager;
    private AlertDialog.Builder dialog;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mBuilder.setProgress(100, 100, false);
                    mBuilder.setContentTitle("医脉下载完成");
                    mBuilder.setContentText(df.format(total * 1.0 / 1024 / 1024) + "M" + "/" + df.format(total * 1.0 / 1024 / 1024) + "M");
                    notificationManager.notify(1, mBuilder.build());

                    new AsyncTask<String,Void,Boolean>(){

                        @Override
                        protected Boolean doInBackground(String... filePath) {
                            LogUtils.d("apkFilePath:"+filePath[0]);
                            boolean isSafe = UpdateAppUtils.checkApk(activity, filePath[0], upDate.getAndroid().getFileMD5());
                            return isSafe;
                        }

                        @Override
                        protected void onPostExecute(Boolean isSafe) {
                            super.onPostExecute(isSafe);

                            if(isSafe){
                                installApk();
                                if (notificationManager != null) {
                                    notificationManager = null;
                                    mVersionUtils = null;
                                } else {
                                    ToastUtils.shortToast("请重新升级");
                                }
                            }
                        }
                    }.execute(filePath.getAbsolutePath());

                    break;
                case 1:
                    if (notificationManager != null) {
                        notificationManager.cancel(1);
                        notificationManager = null;
                        mVersionUtils = null;
                    }
                    Toast.makeText(activity, "文件下载错误！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    isDownload = false;
                    break;
            }

        }
    };
    private String downPath;
    public boolean isDownload = false;

    private VersionUtils(Activity activity) {
        this.activity = activity;
        sp = activity.getSharedPreferences("save_user", Context.MODE_PRIVATE);
    }

    public static void setShowToast(boolean showToast) {
        VersionUtils.showToast = showToast;
    }

    public static VersionUtils getInstance(Activity context) {
        if (mVersionUtils == null) {
            synchronized (VersionUtils.class) {
                if (mVersionUtils == null) {
                    mVersionUtils = new VersionUtils(context);
                }
            }
        }
        showToast = true;
        return mVersionUtils;
    }

    public void checkVersionUpdate() {
        RetrofitServices.VersionUpdateService service = RetrofitUtil.createService(RetrofitServices.VersionUpdateService.class);
        Call<BaseBean<VersionUpdateData>> call = service.getData(CommonUtils.computeSign("1"));
        RetrofitUtil.getInstance().enqueueCall(call, new HttpRequestCallBackImpl<VersionUpdateData>() {
            @Override
            public void onSuccess(BaseBean<VersionUpdateData> baseBean) {
                upDate = baseBean.getData();
                checkUpdate();
                if (sp != null) {
                    sp.edit().putString("channel", AnalyticsConfig.getChannel(YMApplication.getAppContext()))
                            .putString("updateUrl", upDate.getAndroid().getApkurl()).apply();
                }
            }

            @Override
            public void onFailure(RequestState state, String msg) {
                LogUtils.e("请求更新失败："+msg);
//                super.onFailure(state, msg);

            }
        });
    }


    public void checkUpdate() {
        try {
            if (upDate.getAndroid() == null) {
                return;
            }
            if (upDate.getAndroid().getIsStopUpdate() != null && upDate.getAndroid().getIsStopUpdate().equals("1")) {
                return;
            }
            int currentVer = getVersionCode();//当前app code
            if (upDate.getAndroid().getVersioncode() != null && !"".equals(upDate.getAndroid().getVersioncode())
                    && currentVer < Integer.parseInt(upDate.getAndroid().getVersioncode())) {//versionCode 最新code
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    downPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/";
                    File file = new File(downPath);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    downPath = downPath + downAPKname;

                    filePath = new File(downPath);
                    if (filePath.exists()) {
                        filePath.delete();
                    }
                } else {
                    Toast.makeText(activity, "没有SD卡，不能下载更新！", Toast.LENGTH_SHORT).show();
                    return;
                }
                showUpdataDialog();
            } else {
                DLog.d("version control", "version control " + showToast);
                if (showToast) {
                    ToastUtils.shortToast("当前已是最新版本");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            DLog.d("version control", "version control " + e.getMessage());
        }

    }


    /*
     *
     * 弹出对话框通知用户更新程序
     *
     * 弹出对话框的步骤： 1.创建alertDialog的builder. 2.要给builder设置属性, 对话框的内容,样式,按钮
     * 3.通过builder 创建一个对话框 4.对话框show()出来
     */
    private void showUpdataDialog() {
        dialog = new AlertDialog.Builder(YMApplication.getInstance().getTopActivity());
        dialog.setMessage(upDate.getAndroid().getDescription());
        final String isupdate = upDate.getAndroid().getIsupdate() == null ? "0" : upDate.getAndroid().getIsupdate();
        if (isupdate.equals("1")) {
            dialog.setCancelable(false);
        } else {
            dialog.setCancelable(true);

        }
        dialog.setTitle("版本更新");

        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (!isupdate.equals("1")) {
                    arg0.dismiss();
                }

                downLoadApk();
            }
        });
        if (!isupdate.equals("1")) {
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    if (isupdate.equals("1")) {
                        arg0.dismiss();
                    } else {
                        arg0.dismiss();
                    }
                }
            });
        }

        dialog.setCancelable(false);
        dialog.create().show();
    }


    /*
     * 从服务器中下载APK
     */
    private void downLoadApk() {
        isDownload = true;

        new Thread() {
            @Override
            public void run() {
                try {
                    getFileFromServer();
                } catch (Exception e) {
                    LogUtils.e("下载升级包异常:" + e.getMessage());
                    e.printStackTrace();
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
    }


    private void getFileFromServer() throws IOException {
        InputStream is = null;
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        LogUtils.d("version update " + upDate.toString());
        try {
            // TODO: 2018/3/28 测试stone apk地址
//            String apkDownloadUrl = "http://appdl.xywy.com/android/yimai/test/yimai.apk";
            String apkDownloadUrl = upDate.getAndroid().getApkurl();

            //校验 apk升级包有效性
            if (!UpdateAppUtils.isApkDownloadUrlSafe(apkDownloadUrl)) {
                isDownload = false;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.shortToast("无效的APK下载地址");
                    }
                });
                return;
            }

            URL url = new URL(apkDownloadUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
//			 获取到文件的大小
            long maxleng = conn.getContentLength();
//            pd.setMax((int) (maxleng / maxleng) * 100);
            is = conn.getInputStream();
            fos = new FileOutputStream(filePath);
            bis = new BufferedInputStream(is);

            notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(activity);
            mBuilder.setContentIntent(PendingIntent.getActivity(activity, 1, new Intent(), 0));
            mBuilder.setSmallIcon(R.drawable.dp_icon);
            mBuilder.setContentTitle("医脉正在下载")
                    .setContentText("0M/" + df.format(maxleng * 1.0 / 1024 / 1024) + "M")
                    .setTicker("医脉正在下载");
            mBuilder.setAutoCancel(false);
            mBuilder.setProgress(100, 0, false);
            notificationManager.notify(1, mBuilder.build());

            byte[] buffer = new byte[1024];
            int len;

            Long time = System.currentTimeMillis();
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                // 获取当前下载量
                if (System.currentTimeMillis() - time > 1000) {//   一秒刷新通知栏

                    mBuilder.setProgress(100, (int) ((total * 1.0 / maxleng * 100)), false);
                    mBuilder.setContentText(df.format(total * 1.0 / 1024 / 1024) + "M/" + df.format(maxleng * 1.0 / 1024 / 1024) + "M");
                    notificationManager.notify(1, mBuilder.build());

//                    pd.setProgress((int) ((total * 1.0 / maxleng * 100)));
                    time = System.currentTimeMillis();

                }
            }

            handler.sendEmptyMessage(0);
        } finally {

            if (fos != null) {
                fos.close();
            }
            if (bis != null) {
                bis.close();
            }
            if (is != null) {
                is.close();
            }

//            pd.dismiss();

        }
    }

    // 安装apk
    private void installApk() {
        if (upDate.getAndroid() != null && upDate.getAndroid().getIsupdate() != null && upDate.getAndroid().getIsupdate().equals("1")) {
            dialog.show();
        }
        // 执行动作
        Intent installIntent = new Intent();
        installIntent.setAction(Intent.ACTION_VIEW);
        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // 执行的数据类型
        Uri mImageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果是7.0android系统
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            installIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            mImageUri = getFileExternalContentUri(activity,filePath);

        }else{
            mImageUri = Uri.fromFile(filePath);
        }
        installIntent.setDataAndType(mImageUri, "application/vnd.android.package-archive");
//        installIntent.setDataAndType(Uri.fromFile(filePath),
//                "application/vnd.android.package-archive");
        activity.startActivity(installIntent);


        notificationManager.notify(1, mBuilder.build());
    }

    public static Uri getFileExternalContentUri(Context context, File externalFile) {
        String filePath = externalFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Files.getContentUri("external"),
                new String[]{MediaStore.Files.FileColumns._ID},
                MediaStore.Files.FileColumns.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Files.getContentUri("external"), "" + id);
        } else {
            if (externalFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Files.FileColumns.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
            } else {
                return null;
            }
        }
    }

    /*
     * 获取当前程序的版本号
     */
    private int getVersionCode() throws PackageManager.NameNotFoundException {
        // 获取packagemanager的实例
        PackageManager packageManager = activity.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(activity.getPackageName(), 0);
        return packInfo.versionCode;
    }
}
