package com.xywy.askforexpert.appcommon.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.util.TypedValue;
import android.view.Display;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.utils.enctools.RSATools;
import com.xywy.askforexpert.appcommon.utils.others.DLog;

import java.io.File;

/**
 * 获取app 信息
 */
public class AppUtils {


    private AppUtils() {
        throw new UnsupportedOperationException("AppUtils cannot instantiated");
    }


    public  static Context getAppContext(){
        return YMApplication.getAppContext();
    }

    /**
     * 获取app版本名
     */
    public static String getAppVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取应用程序版本名称信息
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAppVersionCodeStr() {
        return ""+getAppVersionCode();
    }

    /**
     * 获取app版本号
     */
    public static int getAppVersionCode() {


        Context context= getAppContext();
        PackageManager pm = context.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * SD卡是否存在
     *
     * @return
     */
    public static boolean isExistSDCard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

    }

    /**
     * 获取 drawable
     *
     * @param bitmap
     * @return
     */
    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        if (bitmap != null) {
            return new BitmapDrawable(bitmap);
        }
        return null;
    }

    public static int getScreenWidth(Activity context) {
        int[] array = getScreenWidthAndHeight(context);
        if (array.length > 1) {
            return array[0];
        }
        return 0;
    }

    public static int getScreenHeight(Activity context) {
        int[] array = getScreenWidthAndHeight(context);
        if (array.length > 1) {
            return array[1];
        }
        return 0;
    }

    /**
     * 得到设备屏幕的宽度
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 得到设备屏幕的高度
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 得到设备的密度
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int[] getScreenWidthAndHeight(Activity context) {
        int[] window = new int[2];
        window[0] = 0;
        window[1] = 1;
        if (context == null) {
            return window;
        }
        Display display = context.getWindowManager().getDefaultDisplay();

        window[0] = display.getWidth();
        window[1] = display.getHeight();
        return window;
    }

    public static boolean delFolder(Context context, String folderPath) {
        boolean result = false;

        delAllFile(context, folderPath);
        String filePath = folderPath;
        filePath = filePath.toString();
        File myFilePath = new File(filePath);
        myFilePath.delete();
        cleanInternalCache(context);
        cleanExternalCache(context);
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();// 清除ImageLoader的缓存数据
//        cleanDatabases(context);
//        cleanSharedPreference(context);
//        cleaSDfiles(context);
//        cleaSDcach(context);
//        cleanFiles(context);
        result = true;
        return result;
    }

    public static void delAllFile(Context context, String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(context, path + "/" + tempList[i]);
                delFolder(context, path + "/" + tempList[i]);
            }
        }
    }

    /**
     * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * * @param context
     */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /**
     * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * * @param context
     */
    public static void cleanDatabases(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/databases"));
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) * * @param
     * context
     */
    public static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/shared_prefs"));
    }

    /**
     * 按名字清除本应用数据库 * * @param context * @param dbName
     */
    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    /**
     * 清除/data/data/com.xxx.xxx/files下的内容 * * @param context
     */
    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * SDCard/Android/data/com.xxx.xxx/cache/
     *
     * @param context
     */
    public static void cleaSDcach(Context context) {
        deleteFilesByDirectory(context.getExternalCacheDir());
    }

    /**
     * SDCard/Android/data/你的应用的包名/files/
     *
     * @param context
     */
    public static void cleaSDfiles(Context context) {
        deleteFilesByDirectory(context.getExternalFilesDir(null));
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache) * * @param
     * context
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /**
     * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * * @param filePath
     */
    public static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    /**
     * 清除本应用所有的数据 * * @param context * @param filepath
     */
    public static void cleanApplicationData(Context context, String... filepath) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        cleanFiles(context);
        cleaSDfiles(context);
        cleaSDcach(context);
        for (String filePath : filepath) {
            cleanCustomCache(filePath);
        }
    }

    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory
     */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        } else if (directory != null && directory.exists() && !directory.isDirectory()) {
            directory.delete();
        }
    }

    /**
     * Convert Dp to Pixel
     */
    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }


    public static String getAPPInfo() {
        String userID = "0";
        String versionName = "";
        if (!YMUserService.isGuest()) {
            userID = YMApplication.getPID();
        }

        final String model = Build.MODEL;
        final String version = Build.VERSION.RELEASE;
        try {
            versionName = YMApplication.getAppContext().getPackageManager()
                    .getPackageInfo(YMApplication.getAppContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        final String deviceId = YMApplication.getAppContext().getSharedPreferences("storeDeviceID", Context.MODE_PRIVATE)
                .getString("deviceID", "");
        DLog.d("FinalHttp", userID + ", " + model + ", " + version + ", " + versionName + ", " + deviceId);
        return RSATools.strRSA(userID + "|" + model + "|" + version + "|" + versionName + "|" + deviceId + "|" + YMApplication.JPUSH_ID);
    }


}