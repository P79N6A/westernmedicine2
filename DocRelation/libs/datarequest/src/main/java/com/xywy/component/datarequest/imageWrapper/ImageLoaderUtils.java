package com.xywy.component.datarequest.imageWrapper;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.xywy.component.datarequest.tool.FileUtils;

import java.io.File;
import java.text.DecimalFormat;

/**
 * 图片加载工具类
 */
public class ImageLoaderUtils {

    private static final int DEFAULT_DISC_CACHE_SIZE = 50 * 1024 * 1024;//文件缓存最大容量为50M
    private static ImageLoaderUtils instance = null;
    private ImageLoader mInnerImageLoader;
    private DisplayImageOptions mDefaultImageOpions;

    protected ImageLoaderUtils() {

    }

    public static synchronized ImageLoaderUtils getInstance() {
        if (instance == null) {
            instance = new ImageLoaderUtils();
        }
        return instance;
    }

    /**
     * 用来初始化缓存系统。
     * 建议在Application的Oncreate中调用此方法
     *
     * @param context
     */
    public void init(Context context) {

        mDefaultImageOpions = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        File defaultCacheDir = StorageUtils.getOwnCacheDirectory(context.getApplicationContext(),
                "");
        //设置为0会启用默认的memory大小，为1/8的可用内存控件
        MemoryCache normalMemoryCache = DefaultConfigurationFactory.createMemoryCache(context,0);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(DEFAULT_DISC_CACHE_SIZE)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(mDefaultImageOpions)
                .memoryCache(normalMemoryCache)
                //TODO stone 测试与线上要修改
//                .writeDebugLogs()// Remove for release app
                .build();

        // Initialize ImageLoader with configuration.
        mInnerImageLoader = ImageLoader.getInstance();
        mInnerImageLoader.init(config);

    }

    public void displayImage(String url, ImageView view) {
        mInnerImageLoader.displayImage(url, view, mDefaultImageOpions);
    }

    public void displayImage(String url, ImageView view, ImageLoadingListener listener) {
        mInnerImageLoader.displayImage(url, view, mDefaultImageOpions, listener);
    }

    public void displayImage(String url, ImageView view, DisplayImageOptions options) {
        mInnerImageLoader.displayImage(url, view, options);
    }

    //// TODO: 15/12/26  需要深入测试
    public void clearCache() {
        mInnerImageLoader.clearMemoryCache();
        mInnerImageLoader.clearDiskCache();
    }

    //获取缓存大小,以M为单位
    public String getCacheSize() {
        try {
            File file = mInnerImageLoader.getDiskCache().getDirectory();
            float size = (float) FileUtils.getTotalSizeOfFilesInDir(file) / (1024 * 1024);
            DecimalFormat df = new DecimalFormat("0.0");//格式化小数，不足的补0
            return df.format(size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }
}
