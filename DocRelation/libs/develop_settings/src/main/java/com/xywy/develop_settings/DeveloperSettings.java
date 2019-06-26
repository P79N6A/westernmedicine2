package com.xywy.develop_settings;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * Wrapper over {@link SharedPreferences} to store developer settings.
 */
public class DeveloperSettings {

    private static final String KEY_IS_STETHO_ENABLED = "is_stetho_enabled";
    private static final String KEY_IS_LEAK_CANARY_ENABLED = "is_leak_canary_enabled";
    private static final String KEY_IS_TINY_DANCER_ENABLED = "is_tiny_dancer_enabled";
    private static final String KEY_IS_BLOCK_CANARY_ENABLED = "is_block_canary_enabled";
    private static final String KEY_HTTP_LOGGING_LEVEL = "key_http_logging_level";

    @NonNull
    private final SharedPreferences sharedPreferences;

    public DeveloperSettings(@NonNull SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public boolean isStethoEnabled() {
        return sharedPreferences.getBoolean(KEY_IS_STETHO_ENABLED, false);
    }

    public void saveIsStethoEnabled(boolean isStethoEnabled) {
        sharedPreferences.edit().putBoolean(KEY_IS_STETHO_ENABLED, isStethoEnabled).apply();
    }

    public boolean isLeakCanaryEnabled() {
        return sharedPreferences.getBoolean(KEY_IS_LEAK_CANARY_ENABLED, true);
    }

    public void saveIsLeakCanaryEnabled(boolean isLeakCanaryEnabled) {
        sharedPreferences.edit().putBoolean(KEY_IS_LEAK_CANARY_ENABLED, isLeakCanaryEnabled).apply();
    }

    public boolean isTinyDancerEnabled() {
        return sharedPreferences.getBoolean(KEY_IS_TINY_DANCER_ENABLED, false);
    }

    public void saveIsTinyDancerEnabled(boolean isTinyDancerEnabled) {
        sharedPreferences.edit().putBoolean(KEY_IS_TINY_DANCER_ENABLED, isTinyDancerEnabled).apply();
    }

    public boolean isBlockCanaryEnabled() {
        return sharedPreferences.getBoolean(KEY_IS_BLOCK_CANARY_ENABLED, true);
    }
    public void saveIsBlockCanaryEnabled(boolean isBlockCanaryEnabled) {
        sharedPreferences.edit().putBoolean(KEY_IS_BLOCK_CANARY_ENABLED, isBlockCanaryEnabled).apply();
    }
}
