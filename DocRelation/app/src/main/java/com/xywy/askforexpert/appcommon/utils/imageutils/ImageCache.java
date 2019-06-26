package com.xywy.askforexpert.appcommon.utils.imageutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.util.LruCache;

public final class ImageCache {

    private static ImageCache imageCache = null;
    private LruCache<String, Bitmap> cache = null;

    private ImageCache() {
        // use 1/8 of available heap size
        cache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 8)) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    public static synchronized ImageCache getInstance() {
        if (imageCache == null) {
            imageCache = new ImageCache();
        }
        return imageCache;

    }

    /**
     * put bitmap to image cache
     *
     * @param key
     * @param value
     * @return the puts bitmap
     */
    public Bitmap put(String key, Bitmap value) {
        return cache.put(key, value);
    }

    /**
     * return the bitmap
     *
     * @param key
     * @return
     */
    public Bitmap get(String key) {
        return cache.get(key);
    }

    /**
     * check if network avalable
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable() && mNetworkInfo.isConnected();
            }
        }

        return false;
    }
}
