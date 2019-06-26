package com.xywy.component;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.xywy.activityrouter.ExtraTypes;
import com.xywy.activityrouter.MethodInvoker;
import com.xywy.activityrouter.Router;
import com.xywy.routerstub.MainActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by bobby on 16/5/25.
 */
public class XywyApplication extends Application implements MethodInvoker {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            try {
                //反射初始化app develop setting，检测摇晃手机启动设置页面
                Class aClass = Class.forName("com.xywy.develop_settings.DevelopSettingManager");
                Method method = aClass.getMethod("init", Application.class);
                System.out.println(method.invoke(null, this));


            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        ExtraTypes xywymainExtra = new ExtraTypes();
        xywymainExtra.setIntExtra("int1","int2");
        xywymainExtra.setIntExtra("int1,int2".split(","));
        xywymainExtra.setDoubleExtra("double1,double2".split(","));
        xywymainExtra.setBooleanExtra("boolean1,boolean2".split(","));
        xywymainExtra.setByteExtra("byte1,byte2".split(","));
        xywymainExtra.setCharExtra("chat1,chat2".split(","));
        xywymainExtra.setFloatExtra("float1,float2".split(","));
        xywymainExtra.setLongExtra("long1,long2".split(","));
        xywymainExtra.setShortExtra("short1,short2".split(","));
        xywymainExtra.setSerializableExtra("serializable1,serializable2".split(","));
        Router.map("xywymain", com.xywy.component.MainActivity.class, null, xywymainExtra);

        ExtraTypes xywystubExtra = new ExtraTypes();
        xywystubExtra.setIntExtra("DOCTORID,ORDERID".split(","));
        Router.map("stubmain", MainActivity.class, null, xywystubExtra);

        //方法
        Router.map("xywyfun", null, this, new ExtraTypes());
    }

    @Override
    public void invoke(Context context, String target, Bundle bundle) {
        Log.d("invode", "target = " + target);
        Log.d("invode", "bundle = " + bundle.toString());
}    }

