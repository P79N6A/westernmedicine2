package com.xywy.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限相关工具类
 * Created by bailiangjin on 2016/11/22.
 */

public class PermissionUtils {



    /**
     * 检查权限
     *
     * @param context    上下文
     * @param permission 权限名称。eg：Manifest.permission.CAMERA
     * @return 是否有权限。true 有，false 没有
     */
    public static boolean checkPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 检查一组权限
     *
     * @param context     上下文
     * @param permissions 权限
     * @return 未获取的权限List
     */
    public static List<String> checkPermissions(Context context, List<String> permissions) {
        List<String> permissionDenied = new ArrayList<>();
        for (String permission : permissions) {
            if (!checkPermission(context, permission)) {
                permissionDenied.add(permission);
            }
        }
        return permissionDenied;
    }


}
