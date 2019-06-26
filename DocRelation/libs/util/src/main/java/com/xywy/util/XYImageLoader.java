package com.xywy.util;


import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.lang.reflect.Field;
import java.text.DecimalFormat;

/**
 * Created by shijiazi on 16/1/7.
 */
public class XYImageLoader {

    private static XYImageLoader instance;
    private DisplayImageOptions mUserHeadImageOption;
    private DisplayImageOptions mHospitalImageOption;
    private DisplayImageOptions mMainSlideShowOption;
    private RoundedBitmapWithBorderDisplayer mDoctorRoundDisplayer;

    private int mDefaultUserHead;
    private int mDefaultHospital;
    private int mDefaultMainSlideShow;

    private XYImageLoader() {
        mDoctorRoundDisplayer = new RoundedBitmapWithBorderDisplayer(1, 0xffe0e0e0);
    }

    public void setDefaultUserHead(int value) {
        mDefaultUserHead = value;
        mUserHeadImageOption = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(mDoctorRoundDisplayer)
                .showImageOnLoading(mDefaultUserHead)
                .showImageOnFail(mDefaultUserHead)
                .showImageForEmptyUri(mDefaultUserHead)
                .build();
    }

    public void setDefaultHospital(int value) {
        mDefaultHospital = value;
        mHospitalImageOption = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .showImageOnLoading(mDefaultHospital)
                .showImageOnFail(mDefaultHospital)
                .showImageForEmptyUri(mDefaultHospital)
                .build();
    }

    public void setDefaultMainSlideShow(int value) {
        mDefaultMainSlideShow = value;
        mMainSlideShowOption = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .showImageOnLoading(mDefaultMainSlideShow)
                .showImageOnFail(mDefaultMainSlideShow)
                .showImageForEmptyUri(mDefaultMainSlideShow)
                .build();
    }

    public static synchronized XYImageLoader getInstance() {
        if (instance == null) {
            instance = new XYImageLoader();
        }
        return instance;
    }

    /**
     * 显示用户头像
     *
     * @param url
     * @param view
     */
    public void displayUserHeadImage(String url, ImageView view) {
        if(mUserHeadImageOption == null) {
            mUserHeadImageOption = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .displayer(mDoctorRoundDisplayer)
                    .showImageOnLoading(mDefaultUserHead)
                    .showImageOnFail(mDefaultUserHead)
                    .showImageForEmptyUri(mDefaultUserHead)
                    .build();
        }
        ImageLoaderUtils.getInstance()
                .displayImage(url, view, mUserHeadImageOption);
    }

    /**
     * 显示首页轮播图
     *
     * @param url
     * @param view
     */
    public void displaySlideShowImage(String url, ImageView view) {
        if(mMainSlideShowOption == null) {
            mMainSlideShowOption = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .cacheInMemory(true)
                    .cacheOnDisk(false)
                    .showImageOnLoading(mDefaultMainSlideShow)
                    .showImageOnFail(mDefaultMainSlideShow)
                    .showImageForEmptyUri(mDefaultMainSlideShow)
                    .build();
        }
        ImageLoaderUtils.getInstance()
                .displayImage(url, view, mMainSlideShowOption);
    }

    /**
     * 显示医院首页图片
     *
     * @param url
     * @param view
     */
    public void displayHospitalImage(String url, ImageView view) {
        if(mHospitalImageOption == null) {
            mHospitalImageOption = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .cacheInMemory(true)
                    .cacheOnDisk(false)
                    .showImageOnLoading(mDefaultHospital)
                    .showImageOnFail(mDefaultHospital)
                    .showImageForEmptyUri(mDefaultHospital)
                    .build();
        }
        ImageLoaderUtils.getInstance()
                .displayImage(url, view, mHospitalImageOption);
    }


    /**
     * 正常显示
     *
     * @param url
     * @param view
     */
    public void displayImage(String url, ImageView view) {
        ImageLoaderUtils.getInstance()
                .displayImage(url, view);
    }

    public void displayImage(String url, ImageView view, ImageLoadingListener listener) {
        ImageLoaderUtils.getInstance()
                .displayImage(url, view, listener);
    }

    //获取缓存大小,只计算了imageloader图片缓存的
    public void clearCache() {
        try {
            Field f = ImageLoaderUtils.class.getDeclaredField("mInnerImageLoader");
            f.setAccessible(true);
            ImageLoader loader = (ImageLoader) f.get(ImageLoaderUtils.getInstance());
            loader.clearMemoryCache();
            loader.clearDiskCache();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取缓存大小,只计算了imageloader图片缓存的
    public String getCacheSize() {
        try {
            Field f = ImageLoaderUtils.class.getDeclaredField("mInnerImageLoader");
            f.setAccessible(true);
            ImageLoader loader = (ImageLoader) f.get(ImageLoaderUtils.getInstance());
            File file = loader.getDiskCache().getDirectory();
            float size = (float) FileUtils.getTotalSizeOfFilesInDir(file) / (1024 * 1024);
            DecimalFormat df = new DecimalFormat("0.0");//格式化小数，不足的补0
            return df.format(size) + "M";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0M";
    }
}
