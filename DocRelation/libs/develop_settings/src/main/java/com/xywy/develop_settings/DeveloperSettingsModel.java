package com.xywy.develop_settings;

import android.app.Application;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;

import com.codemonkeylabs.fpslibrary.TinyDancer;
import com.facebook.stetho.Stetho;
import com.github.moduth.blockcanary.BlockCanary;
import com.squareup.leakcanary.LeakCanary;

import java.util.concurrent.atomic.AtomicBoolean;

import static android.view.Gravity.START;
import static android.view.Gravity.TOP;

public class DeveloperSettingsModel {

    private static final String TAG = "DeveloperSettingsModel";
    @NonNull
    private final Application application;

    @NonNull
    private final DeveloperSettings developerSettings;

    @NonNull
    private AtomicBoolean stethoAlreadyEnabled = new AtomicBoolean();

    @NonNull
    private AtomicBoolean leakCanaryAlreadyEnabled = new AtomicBoolean();

    @NonNull
    private AtomicBoolean tinyDancerDisplayed = new AtomicBoolean();

    @NonNull
    private AtomicBoolean blockCanaryAlreadyEnabled = new AtomicBoolean();

    public DeveloperSettingsModel(@NonNull Application application,
                                  @NonNull DeveloperSettings developerSettings) {
        this.application = application;
        this.developerSettings = developerSettings;
    }

    @NonNull
    public String getBuildDate() {
        return BuildConfig.build_time;
    }

    @NonNull
    public int getBuildVersionCode() {
        try {
            return application.getPackageManager().getPackageInfo(application.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    @NonNull
    public String getBuildVersionName() {
        try {
            return application.getPackageManager().getPackageInfo(application.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "NotFound";
        }
    }

    public boolean isStethoEnabled() {
        return developerSettings.isStethoEnabled();
    }

    public void changeStethoState(boolean enabled) {
        developerSettings.saveIsStethoEnabled(enabled);
        apply();
    }

    public boolean isLeakCanaryEnabled() {
        return developerSettings.isLeakCanaryEnabled();
    }

    public boolean isBlockCanaryEnabled() {
        return developerSettings.isBlockCanaryEnabled();
    }
    public void changeBlockCanaryState(boolean enabled) {
        developerSettings.saveIsBlockCanaryEnabled(enabled);
        apply();
    }
    public void changeLeakCanaryState(boolean enabled) {
        developerSettings.saveIsLeakCanaryEnabled(enabled);
        apply();
    }

    public boolean isTinyDancerEnabled() {
        return developerSettings.isTinyDancerEnabled();
    }

    public void changeTinyDancerState(boolean enabled) {
        developerSettings.saveIsTinyDancerEnabled(enabled);
        apply();
    }

    public void apply() {
        // Stetho can not be enabled twice.
        if (stethoAlreadyEnabled.compareAndSet(false, true)) {
            if (isStethoEnabled()) {
                Log.e("DeveloperSettingsModel", "Stetho init");
                Stetho.initializeWithDefaults(application);
            }
        }
        // LeakCanary can not be enabled twice.
        if (blockCanaryAlreadyEnabled.compareAndSet(false, true)) {
            if (isLeakCanaryEnabled()) {
                Log.e("DeveloperSettingsModel", "blockCanary init");
                BlockCanary.install(application, new AppBlockCanaryContext()).start();
            }
        }

        // LeakCanary can not be enabled twice.
        if (leakCanaryAlreadyEnabled.compareAndSet(false, true)) {
            if (isLeakCanaryEnabled()) {
                Log.e("DeveloperSettingsModel", "LeakCanary init");
                LeakCanary.install(application);
            }
        }

        if (isTinyDancerEnabled()&& tinyDancerDisplayed.compareAndSet(false, true)) {
            //try disappear first
            try {
                TinyDancer.hide(application);
            } catch (Exception e) {
                // In some cases TinyDancer can not be hidden without exception: for example when you start it first time on Android 6.
            }
            StackTraceElement stackTrace = new Throwable().getStackTrace()[1];
            Log.e(TAG,
                    stackTrace.getClassName() + stackTrace.getLineNumber() + stackTrace.getMethodName()
                            + "TinyDancer");
            final DisplayMetrics displayMetrics = application.getResources().getDisplayMetrics();
            TinyDancer.create()
                    .redFlagPercentage(0.2f)
                    .yellowFlagPercentage(0.05f)
                    .startingGravity(TOP | START)
                    .startingXPosition(displayMetrics.widthPixels / 10)
                    .startingYPosition(displayMetrics.heightPixels / 4)
                    .show(application);
        } else if(tinyDancerDisplayed.compareAndSet( true,false)){
            try {
                TinyDancer.hide(application);
            } catch (Exception e) {
                // In some cases TinyDancer can not be hidden without exception: for example when you start it first time on Android 6.
            }
        }

    }
}
