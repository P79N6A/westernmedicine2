package com.xywy.activityrouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by shijiazi on 16/11/22.
 */

/*
    用于activity路由
 */
public class Router {

    public static String KEY_RAW_URL = "com.xywy.activityrouter.KeyRawUrl";

    private static List<Mapping> mappings = new ArrayList<>();

    private static void initIfNeed() {
        if (!mappings.isEmpty()) {
            return;
        }
    }

    public static void map(String target, Class<? extends Activity> activity, MethodInvoker method, ExtraTypes extraTypes) {
        mappings.add(new Mapping(target, activity, method, extraTypes));
    }

    public static boolean open(Context context, String url) {
        return open(context, Uri.parse(url));
    }

    public static boolean open(Context context, String url, RouterCallback callback) {
        return open(context, Uri.parse(url), callback);
    }

    public static boolean open(Context context, Uri uri) {
        return open(context, uri, null);
    }

    public static boolean open(Context context, Uri uri, RouterCallback callback) {
        return open(context, uri, -1, callback);
    }

    public static boolean openForResult(Activity activity, String url, int requestCode) {
        return openForResult(activity, Uri.parse(url), requestCode);
    }

    public static boolean openForResult(Activity activity, String url, int requestCode, RouterCallback callback) {
        return openForResult(activity, Uri.parse(url), requestCode, callback);
    }

    public static boolean openForResult(Activity activity, Uri uri, int requestCode) {
        return openForResult(activity, uri, requestCode, null);
    }

    public static boolean openForResult(Activity activity, Uri uri, int requestCode, RouterCallback callback) {
        return open(activity, uri, requestCode, callback);
    }

    private static boolean open(Context context, Uri uri, int requestCode, RouterCallback callback) {
        boolean success = false;
        try {
            success = doOpen(context, uri, requestCode);
            if (callback != null) {
                if (success) {
                    callback.afterOpen(context, uri);
                } else {
                    callback.notFound(context, uri);
                }
            }
        } catch (Throwable e) {
            if (callback != null) {
                callback.error(context, uri, e);
            }
        }
        return success;
    }

    private static boolean doOpen(Context context, Uri uri, int requestCode) {
        initIfNeed();
        String target = uri.getHost();
        if(!TextUtils.isEmpty(uri.getPath())) {
            target = uri.getHost() + "/" + uri.getPath();
        }
        for (Mapping mapping : mappings) {
            if (target.equals(mapping.getTarget())) {
                if (mapping.getActivity() == null) {
                    mapping.getMethod().invoke(context, target, mapping.parseExtras(uri));
                    return true;
                }
                Intent intent = new Intent(context, mapping.getActivity());
                intent.putExtras(mapping.parseExtras(uri));
                intent.putExtra(KEY_RAW_URL, uri.toString());
                if (!(context instanceof Activity)) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                if (requestCode >= 0) {
                    if (context instanceof Activity) {
                        ((Activity) context).startActivityForResult(intent, requestCode);
                    } else {
                        throw new RuntimeException("can not startActivityForResult context " + context);
                    }
                } else {
                    context.startActivity(intent);
                }
                return true;
            }
        }
        return false;
    }

}
