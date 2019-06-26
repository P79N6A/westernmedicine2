package com.xywy.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/24 16:35
 */

public class CamaraUtil {
    /**
     * 本地图片选取标志
     */
    public static final int FLAG_CHOOSE_IMG = 0x11;
    /**
     * 相机标志
     */
    public static final int FLAG_CHOOSE_CAMERA = 0x17;

    /**
     * 启动相机
     *
     * @param context
     * @return
     */
    public static File startCamera(Context context) {
        View view = null;
        if (context instanceof AppCompatActivity) {
            view = ((AppCompatActivity) context).getCurrentFocus();
        } else if (context instanceof Activity) {
            view = ((Activity) context).getCurrentFocus();
        }
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        File origUri = null;
        if (!PermissionUtils.checkPermission(context, Manifest.permission.CAMERA)) {
            T.showShort(context, "无法打开相机，请授予照相机(Camera)权限");
        } else {
            // 照片命名
            origUri = new File(Environment.getExternalStorageDirectory(), "osc_" + System.currentTimeMillis() + ".jpg");
            origUri.getParentFile().mkdirs();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(origUri));
            if (context instanceof AppCompatActivity) {
                ((AppCompatActivity) context).startActivityForResult(intent, FLAG_CHOOSE_CAMERA);
            } else if (context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, FLAG_CHOOSE_CAMERA);
            }
        }
        return origUri;
    }
    /**
     * 启动相册
     *
     * @param context
     * @return
     */
    public static void startPhoto(Context context) {
        if (!PermissionUtils.checkPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            T.showShort(context, "无法打开相册，请授予内存(Storage)权限");
            return;
        }
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        intent.putExtra(Intent.EXTRA_TITLE, "选择图片");
        ((Activity) context).startActivityForResult(intent, FLAG_CHOOSE_IMG);
    }

    public static String handleResult(Context mContext, File origUri, int requestCode, int resultCode, Intent data) {
        if (requestCode == FLAG_CHOOSE_IMG&&resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri uri = data.getData();
                    if (!TextUtils.isEmpty(uri.getAuthority())) {
                        Cursor cursor = mContext.getContentResolver().query(uri,
                                new String[]{MediaStore.Images.Media.DATA},
                                null, null, null);
                        if (null != cursor) {
                        cursor.moveToFirst();
                        String path = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Images.Media.DATA));
                        cursor.close();
                        return  path;
                        }
                    }else {
                       return uri.getPath();
                    }
            }
            T.showShort( "图片没找到");
        }else if (requestCode == FLAG_CHOOSE_CAMERA&&resultCode == Activity.RESULT_OK) {
            if (origUri !=null){
                return origUri.getPath();
            }
        }
        return null;
    }
}
