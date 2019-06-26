package com.xywy.activityrouter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Path;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by bobby on 16/11/22.
 */
public class Mapping {
    private final String target;
    private final Class<? extends Activity> activity;
    private final MethodInvoker method;
    private final ExtraTypes extraTypes;

    public Mapping(String target, Class<? extends Activity> activity, MethodInvoker method, ExtraTypes extraTypes) {
        if (target == null) {
            throw new NullPointerException("target can not be null");
        }
        this.target = target;
        this.activity = activity;
        this.method = method;
        this.extraTypes = extraTypes;
        if (target.toLowerCase().startsWith("http://") || target.toLowerCase().startsWith("https://")) {
            //浏览器
        } else {
            //其他
        }
    }

    public MethodInvoker getMethod() {
        return method;
    }

    public Class<? extends Activity> getActivity() {
        return activity;
    }

    @Override
    public String toString() {
        return String.format("%s => %s", target, activity);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Mapping) {
            Mapping that = (Mapping) o;
            return target.equals(that.target);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return target.hashCode();
    }

    public String getTarget() {
        return target;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Bundle parseExtras(Uri uri) {
        Bundle bundle = new Bundle();

        Set<String> params = uri.getQueryParameterNames();
        for (String param: params) {
            String value = uri.getQueryParameter(param);
            if(!TextUtils.isEmpty(value)) {
                put(bundle, param, value);
            }
        }
        return bundle;
    }

    private void put(Bundle bundle, String name, String value) {
        int type = extraTypes.getType(name);
        switch (type) {
            case ExtraTypes.INT:
                bundle.putInt(name, Integer.parseInt(value));
                break;
            case ExtraTypes.LONG:
                bundle.putLong(name, Long.parseLong(value));
                break;
            case ExtraTypes.BOOL:
                bundle.putBoolean(name, Boolean.parseBoolean(value));
                break;
            case ExtraTypes.SHORT:
                bundle.putShort(name, Short.parseShort(value));
                break;
            case ExtraTypes.FLOAT:
                bundle.putFloat(name, Float.parseFloat(value));
                break;
            case ExtraTypes.DOUBLE:
                bundle.putDouble(name, Double.parseDouble(value));
                break;
            case ExtraTypes.BYTE:
                bundle.putByte(name, Byte.parseByte(value));
                break;
            case ExtraTypes.CHAR:
                bundle.putChar(name, value.charAt(0));
                break;
            case ExtraTypes.SERIALIZABLE:
                Serializable obj = (Serializable) ObjectUtils.StringToObject(value);
                if(obj!=null) {
                    bundle.putSerializable(name, obj);
                }
                break;
            default:
                bundle.putString(name, value);
                break;
        }
    }
}
