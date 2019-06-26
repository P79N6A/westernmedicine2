package com.xywy.develop_settings;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.squareup.seismic.ShakeDetector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/7/21 10:07
 */
public class DevelopSettingManager {
    private static DeveloperSettingsModel developerSettingModel;

    public static DeveloperSettingsModel getDeveloperSettingModel() {
        return developerSettingModel;
    }

    public static void init(final Application context) {
        ShakeDetector shakeDetector = new ShakeDetector(new ShakeDetector.Listener() {
            @Override
            public void hearShake() {
                Intent lynxActivityIntent = new Intent(context, DeveloperSettingsActivity.class);
                lynxActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(lynxActivityIntent);
            }
        });
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        shakeDetector.start(sensorManager);

        developerSettingModel = new DeveloperSettingsModel(context, new DeveloperSettings(context.getSharedPreferences("developer_settings", Context.MODE_PRIVATE)));
        developerSettingModel.apply();

        logCurrentActivity(context);
        logCurrentFragment(context);

    }

    private static void logCurrentActivity(Application context) {
        //监控activity生命周期，打印日志
        context.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.e("onActivityCreated",activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.e("onActivityStarted",activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.e("onActivityResumed",activity.getClass().getSimpleName());
//                if (activity instanceof  FragmentActivity){
//                    final FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
//                    List<Fragment> fragments = fragmentManager.getFragments();
//                    for (Fragment f : fragments) {
//                        if (f.isVisible()) {
//                            Log.e("onResumeFragment", f.getClass().getSimpleName());
//                        }
//                    }
//                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.e("onActivityPaused",activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.e("onActivityStopped",activity.getClass().getSimpleName());
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.e("onActivitySaveInstance",activity.getClass().getSimpleName());

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.e("onActivityDestroyed",activity.getClass().getSimpleName());

            }
        });
    }

    private static void logCurrentFragment(final Application context) {
        //启动守护线程监听log,显示当前resume的Fragment
        FragmentManager.enableDebugLogging(true);
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                FragmentManager.enableDebugLogging(true);
                Process pro = null;
                BufferedReader br=null;
                try {
                    Runtime.getRuntime().exec("logcat -c").waitFor();
                    pro = Runtime.getRuntime().exec("logcat");
                    br=new BufferedReader(new InputStreamReader(pro.getInputStream()));
                    String line = null;
                    while (context!=null) {
                        while ((line = br.readLine()) != null) {
                            if (line.contains("moveto RESUMED:")){
                                String[] s=line.replace("moveto RESUMED","").split(":");
                                if (s.length==3){
                                    Log.e("onResumeFragment",s[2]);
                                }
                            }
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (br!=null){
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }
}
