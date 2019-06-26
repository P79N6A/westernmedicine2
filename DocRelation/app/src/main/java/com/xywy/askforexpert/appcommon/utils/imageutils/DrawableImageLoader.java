package com.xywy.askforexpert.appcommon.utils.imageutils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.others.FileHelper;

import java.io.File;
import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 废弃的图片加载方法 建议使用ImageLoadUtils
 */
@Deprecated
public class DrawableImageLoader {
    public static String fileName = "image";
    private static DrawableImageLoader loader;
    private final Handler handler = new Handler();
    /**
     * 文件的存储目录
     */
    public String filePath = "";
    // 为了加快速度，在内存中开启缓存（主要应用于重复图片较多时，或者同一个图片要多次被访问，比如在ListView时来回滚动）
    public Map<String, MySoftRef> imageCache = new HashMap<String, MySoftRef>();
    // .newCachedThreadPool();
    /**
     * @param imageUrl
     * 图像url地址
     * @param callback
     * 回调接口
     * @return 返回内存中缓存的图像，第一次加载返回null
     */

    Drawable drawable = null;
    private boolean isSoftRefrence = true;
    private ExecutorService executorService = Executors.newFixedThreadPool(5);
    /**
     * 垃圾Reference的队列（所引用的对象已经被回收，则将该引用存入队列中）
     */
    private ReferenceQueue<Drawable> q;
    private Map<String, Drawable> imgCache = new HashMap<String, Drawable>();

    private DrawableImageLoader() {
        filePath = getDire();
        q = new ReferenceQueue<Drawable>();

    }

    public static DrawableImageLoader getInstance() {
        if (loader == null) {
            loader = new DrawableImageLoader();
        }
        return loader;
    }

    public static String getDire() {
        return getDire(CommonUrl.DIRE);
    }

    public static String getDire(String dire) {
        StringBuffer p = new StringBuffer();
        if (AppUtils.isExistSDCard()) {
            p.append(Environment.getExternalStorageDirectory()
                    .getAbsolutePath());
        } else {
            p.append(YMApplication.getAppContext().getFileStreamPath("")
                    .getAbsolutePath());
        }
        p.append(File.separator).append(dire).append(File.separator)
                .append(fileName);
        return p.toString();
    }

    public static String getGifDire() {
        return getDire() + "/gif";
    }

    /**
     * 提供给其它类
     *
     * @return 图片的存储目录
     */
    public static String getFilePath(String id) {
        return getDire() + File.separator + id + CommonUrl.SUFFIX;
    }

    /**
     * 以软引用的方式对一个Bitmap对象的实例进行引用并保存该引用
     */
    private void addCacheBitmap(Drawable drawable, String key) {
        cleanCache();// 清除垃圾引用
        MySoftRef ref = new MySoftRef(drawable, q, key);
        imageCache.put(key, ref);
    }

    private void cleanCache() {
        MySoftRef ref = null;
        while ((ref = (MySoftRef) q.poll()) != null) {
            imageCache.remove(ref._key);
        }
    }

    public Drawable loadDrawable(final String imageUrl, final String productId,
                                 final ImageCallback callback) {
        // 如果缓存过就从缓存中取出数据
        if (imageCache.containsKey(imageUrl + productId) && isSoftRefrence) {
            MySoftRef ref = imageCache.get(imageUrl + productId);
            drawable = ref.get();
            if (drawable != null) {
                // callback.imageLoaded(drawable, imageUrl);
                return drawable;
            }
        }
        if (imgCache.containsKey(imageUrl + productId) && !isSoftRefrence) {
            drawable = imgCache.get(imageUrl + productId);
            if (drawable != null) {
                // callback.imageLoaded(drawable, imageUrl);
                return drawable;
            }
        }

        // 缓存中没有图像，则从网络上取出数据，并将取出的数据缓存到内存中
        executorService.submit(new Runnable() {
            public void run() {
                try {
                    final Drawable loaddrawable = loadImageFromUrl(imageUrl,
                            filePath, productId, callback);
                    if (loaddrawable != null) {
                        if (isSoftRefrence) {
                            addCacheBitmap(loaddrawable, imageUrl + productId);
                        } else {
                            imgCache.put(imageUrl + productId, loaddrawable);
                        }
                        handler.post(new Runnable() {
                            public void run() {
                                callback.imageLoaded(loaddrawable, imageUrl);
                            }
                        });
                    }
                } catch (Exception e) {
                    // Preserve interrupt status
                    Log.e("AsyncPoolImageLoad", e.getMessage());
                }

            }
        });
        return null;
    }

    public Drawable loadLocalDrawable(final String imageUrl,
                                      final String productName, final ImageCallback callback) {

        // 如果缓存过就从缓存中取出数据
        if (imageCache.containsKey(imageUrl) && isSoftRefrence) {
            MySoftRef ref = imageCache.get(imageUrl);
            drawable = ref.get();
            if (drawable != null) {
                return drawable;
            }
        }
        if (imgCache.containsKey(imageUrl) && !isSoftRefrence) {
            drawable = imgCache.get(imageUrl);
            if (drawable != null) {
                return drawable;
            }
        }

        // 缓存中没有图像，则从网络上取出数据，并将取出的数据缓存到内存中
        executorService.submit(new Runnable() {
            public void run() {
                try {

                    final Drawable loaddrawable = loadImageFromLocal(filePath,
                            productName);
                    if (loaddrawable != null) {
                        if (isSoftRefrence) {
                            addCacheBitmap(loaddrawable, imageUrl);
                        } else {
                            imgCache.put(imageUrl, loaddrawable);
                        }
                        handler.post(new Runnable() {
                            public void run() {
                                callback.imageLoaded(loaddrawable, imageUrl);
                            }
                        });
                    }
                } catch (Exception e) {
                    // Preserve interrupt status
                    Log.e("AsyncPoolImageLoad", e.getMessage());
                }

            }
        });
        return null;
    }

    // 从网络上取数据方法
    @SuppressWarnings("static-access")
    protected Drawable loadImageFromUrl(String imageUrl, String path,
                                        String productId, final ImageCallback callback) {
        try {
            Drawable draw = null;

            // 下载图片并存入系统指定目录
            FileHelper fh = new FileHelper();
            String fileName = productId;
            if (!fh.isFileExist(fileName + CommonUrl.SUFFIX, path)) {
                // 创建目录
                fh.creatSDDir(path);
                try {
                    fh.downFile(imageUrl, path, fileName + "", callback);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                draw = Drawable.createFromPath(path + File.separator + fileName
                        + CommonUrl.SUFFIX);
            } catch (Exception e) {
                draw = null;
            }
            return draw;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 从网络上取数据方法
    @SuppressWarnings("static-access")
    protected Drawable loadImageFromLocal(String path, String productName) {
        try {
            Drawable draw = null;
            // 下载图片并存入系统指定目录
            FileHelper fh = new FileHelper();
            String fileName = productName + "";

            if (fh.isFileExist(fileName + CommonUrl.SUFFIX, path)) {
                try {
                    Bitmap bit = FileHelper.decodeSampledBitmapFromDescriptor(
                            path + File.separator + fileName + CommonUrl.SUFFIX,
                            0, 3);
                    draw = AppUtils.bitmapToDrawable(bit);
                } catch (Exception e) {
                    draw = null;
//					MyLog.log("err", "localImgerror==" + e.getMessage());
                }
            }
            return draw;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 直接从网络获取图片流
     *
     * @param url
     * @param callback
     */
    public void loadImageNoCache(final String url,
                                 final ImageStrreamCallback callback) {
        try {
            final byte[] data = FileHelper.downFile(url);
            handler.post(new Runnable() {
                public void run() {
                    callback.imageLoaded(data, url);
                }
            });
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // 对外界开放的回调接口
    public interface ImageCallback {
        // 注意 此方法是用来设置目标对象的图像资源
        void imageLoaded(Drawable imageDrawable, String imageUrl);
        // public void onUpdate(int length, String imageUrl);
    }

    public interface ImageStrreamCallback {
        void imageLoaded(byte[] datas, String imageUrl);
    }

    /**
     * 继承SoftReference，使得每一个实例都具有可识别的标识。
     */
    class MySoftRef extends SoftReference<Drawable> {
        private String _key = "";

        public MySoftRef(Drawable drawable, ReferenceQueue<Drawable> q,
                         String key) {
            super(drawable, q);
            _key = key;
        }
    }
}
