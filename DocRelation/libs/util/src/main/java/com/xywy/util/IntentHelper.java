package com.xywy.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/10 12:27
 */

public class IntentHelper {
    public static  void startImplicitActivity(Context ctx, Intent intent) {
        List<ResolveInfo> resolveInfos =
                ctx.getPackageManager().queryIntentActivities(intent,
                        PackageManager.MATCH_ALL);
        for (ResolveInfo info:resolveInfos){
            if(info.activityInfo!=null&&info.activityInfo.applicationInfo.packageName.equals(ctx.getApplicationInfo().packageName)){
                ComponentName component = new ComponentName(
                        info.activityInfo.applicationInfo.packageName,
                        info.activityInfo.name);
                intent.setComponent(component);
                ctx.startActivity(intent);
                return;
            }
        }
        //不存在
        L.e(intent.getAction()+" activity not exists");
    }
}
