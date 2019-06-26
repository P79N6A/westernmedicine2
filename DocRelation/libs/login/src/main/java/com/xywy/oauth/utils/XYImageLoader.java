package com.xywy.oauth.utils;


import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xywy.component.datarequest.imageWrapper.ImageLoaderUtils;
import com.xywy.component.datarequest.imageWrapper.RoundedBitmapWithBorderDisplayer;
import com.xywy.oauth.R;

/**
 * Created by shijiazi on 16/1/7.
 */
public class XYImageLoader {

    private static XYImageLoader instance;
    private DisplayImageOptions mUserHeadImageOption;
    private DisplayImageOptions mHospitalImageOption;
    private DisplayImageOptions mMainSlideShowOption;
    private RoundedBitmapWithBorderDisplayer mDoctorRoundDisplayer;

    private XYImageLoader() {
        mDoctorRoundDisplayer = new RoundedBitmapWithBorderDisplayer(1, 0xffe0e0e0);

        mUserHeadImageOption = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(mDoctorRoundDisplayer)
                .showImageOnLoading(R.drawable.default_user_head)
                .showImageOnFail(R.drawable.default_user_head)
                .showImageForEmptyUri(R.drawable.default_user_head)
                .build();

        mHospitalImageOption = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .showImageOnLoading(R.drawable.default_hospital)
                .showImageOnFail(R.drawable.default_hospital)
                .showImageForEmptyUri(R.drawable.default_hospital)
                .build();

        mMainSlideShowOption = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .showImageOnLoading(R.drawable.default_main_slide_show)
                .showImageOnFail(R.drawable.default_main_slide_show)
                .showImageForEmptyUri(R.drawable.default_main_slide_show)
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
        ImageLoaderUtils.getInstance()
                .displayImage(url, view, mHospitalImageOption);
    }

    public void displayImage(String url, ImageView view) {
        ImageLoaderUtils.getInstance()
                .displayImage(url, view);
    }

    public void displayImage(String url, ImageView view, ImageLoadingListener listener) {
        ImageLoaderUtils.getInstance()
                .displayImage(url, view, listener);
    }

}
