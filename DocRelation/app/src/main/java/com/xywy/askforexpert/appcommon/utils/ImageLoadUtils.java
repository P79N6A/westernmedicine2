package com.xywy.askforexpert.appcommon.utils;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;

/**
 * 基于Universal imageLoader 封装的 图片加载工具类
 * 枚举形式 实现单例
 * Created by bailiangjin on 16/7/16.
 */
public enum ImageLoadUtils {
    INSTANCE;
    /**
     * 标准配置
     */
    private DisplayImageOptions normalOptions;
    /**
     * 圆形配置
     */
    private DisplayImageOptions circleOptions;
    private DisplayImageOptions callCircleOptions;
    /**
     * 圆角图片配置
     */
    private DisplayImageOptions roundedOptions;


    private BitmapDisplayer simpleBitmapDisplayer;
    private BitmapDisplayer circleBitmapDisplayer;
    private BitmapDisplayer roundedBitmapDisplayer;

    private int onLoadingImageResId;
    private int onEmptyImageResId;
    private int onFailedImageResId;

    /**
     * 呼叫页面 默认头像
     */
    private int callUserHeadDefaultImageResId;


    /**
     * 构造方法 参数初始化 单例形式 只会初始化一次 避免不必要的资源开支
     */
    ImageLoadUtils() {
        //初始化 全局默认图片
        onLoadingImageResId = R.drawable.icon_photo_def;
        onEmptyImageResId = R.drawable.icon_photo_def;
        onFailedImageResId = R.drawable.icon_photo_def;


        simpleBitmapDisplayer = new SimpleBitmapDisplayer();
        normalOptions = getOption(onLoadingImageResId, onEmptyImageResId, onFailedImageResId, simpleBitmapDisplayer);

        circleBitmapDisplayer = new CircleBitmapDisplayer();
        circleOptions = getOption(onLoadingImageResId, onEmptyImageResId, onFailedImageResId, circleBitmapDisplayer);

        //圆角图片 圆角半径dp
        int cornerRadiusDp = 2;
        //圆角大小通过 dp2px转换 使得 不同分辨率设备上呈现一致显示效果
        roundedBitmapDisplayer = new RoundedBitmapDisplayer(dip2px(YMApplication.getAppContext(), cornerRadiusDp));
        roundedOptions = getOption(onLoadingImageResId, onEmptyImageResId, onFailedImageResId, roundedBitmapDisplayer);

    }

    /**
     * dip px 转换工具类 将圆角进行转换 以实现不同分辨率设备上呈现相同效果
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }

    /**
     * 重构 抽取出的通用生成Option方法
     *
     * @param onLoadingImageResId
     * @param onEmptyImageResId
     * @param onFailedImageResId
     * @param bitmapDisplayer     normal 或圆形、圆角 bitmapDisplayer
     * @return
     */
    private DisplayImageOptions getOption(int onLoadingImageResId, int onEmptyImageResId, int onFailedImageResId, BitmapDisplayer bitmapDisplayer) {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(onLoadingImageResId)
                .showImageForEmptyUri(onEmptyImageResId)
                .showImageOnFail(onFailedImageResId)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(bitmapDisplayer)
                .build();
    }

    public void loadImageView(ImageView iv, String url) {
        ImageLoader.getInstance().displayImage(url, iv, normalOptions);
    }

    public void loadImageView(ImageView iv, String url, ImageLoadingListener listener) {
        ImageLoader.getInstance().displayImage(url, iv, normalOptions, listener);
    }

    public void loadCircleImageView(ImageView iv, String url) {
        ImageLoader.getInstance().displayImage(url, iv, circleOptions);
    }

    public void loadRoundedImageView(ImageView iv, String url) {
        ImageLoader.getInstance().displayImage(url, iv, roundedOptions);
    }

    public void loadImageView(ImageView iv, String url, int defaultImageResId) {
        if (null == iv) {
            LogUtils.e("imageView is null");
            return;
        }
        try {
            DisplayImageOptions normalOptions = getOption(defaultImageResId, defaultImageResId, defaultImageResId, simpleBitmapDisplayer);
            ImageLoader.getInstance().displayImage(url, iv, normalOptions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadCircleImageView(ImageView iv, String url, int defaultImageResId) {
        if (null == iv) {
            LogUtils.e("imageView is null");
            return;
        }
        try {
            DisplayImageOptions circleOptions = getOption(defaultImageResId, defaultImageResId, defaultImageResId, circleBitmapDisplayer);
            ImageLoader.getInstance().displayImage(url, iv, circleOptions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadRoundedImageView(ImageView iv, String url, int defaultImageResId, int cornerRadiusDp) {
        if (null == iv) {
            LogUtils.e("imageView is null");
            return;
        }
        try {
            BitmapDisplayer roundedBitmapDisplayer = new RoundedBitmapDisplayer(dip2px(iv.getContext(), cornerRadiusDp));
            DisplayImageOptions roundedOptions = getOption(defaultImageResId, defaultImageResId, defaultImageResId, roundedBitmapDisplayer);
            ImageLoader.getInstance().displayImage(url, iv, roundedOptions);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化方法
     *
     * @param context
     */
    public void init(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        //TODO stone 测试与线上要修改
//        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

}


//    File cacheDir = StorageUtils.getCacheDirectory(context);
//    ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//            .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
//            .diskCacheExtraOptions(480, 800, null)
//            .taskExecutor(...)
//            .taskExecutorForCachedImages(...)
//    .threadPoolSize(3) // default
//    .threadPriority(Thread.NORM_PRIORITY - 2) // default
//    .tasksProcessingOrder(QueueProcessingType.FIFO) // default
//    .denyCacheImageMultipleSizesInMemory()
//    .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
//            .memoryCacheSize(2 * 1024 * 1024)
//    .memoryCacheSizePercentage(13) // default
//    .diskCache(new UnlimitedDiskCache(cacheDir)) // default
//            .diskCacheSize(50 * 1024 * 1024)
//    .diskCacheFileCount(100)
//    .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
//            .imageDownloader(new BaseImageDownloader(context)) // default
//            .imageDecoder(new BaseImageDecoder()) // default
//            .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
//            .writeDebugLogs()
//    .build();



