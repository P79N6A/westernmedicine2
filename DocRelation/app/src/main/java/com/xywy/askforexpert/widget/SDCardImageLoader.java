package com.xywy.askforexpert.widget;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.xywy.askforexpert.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 从SDCard异步加载图片
 *
 * @author 王鹏
 * @2015-4-25下午2:50:57
 */
public class SDCardImageLoader {
    //
    public static List<String> img_path = new ArrayList<String>();
    public static int img_max;
    public static int count;
    public static boolean isMax_check = true;
    // 缓存
    private LruCache<String, Bitmap> imageCache;
    // 固定2个线程来执行任务
    private ExecutorService executorService = Executors.newFixedThreadPool(1);
    private Handler handler = new Handler();

    private int screenW, screenH;

    public SDCardImageLoader(int screenW, int screenH) {
        this.screenW = screenW;
        this.screenH = screenH;

        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;

        // 设置图片缓存大小为程序最大可用内存的1/8
        imageCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    // 直接載入圖片
    public static Bitmap getBitmap(String path) {
        Bitmap bt = BitmapFactory.decodeFile(path);
        return bt;
    }

    private Bitmap loadDrawable(final int smallRate, final String filePath,
                                final ImageCallback callback) {
        // 如果缓存过就从缓存中取出数据
        if (filePath != null && imageCache.get(filePath) != null) {
            return imageCache.get(filePath);
        }

        // 如果缓存没有则读取SD卡
        executorService.submit(new Runnable() {
            public void run() {
                try {
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(filePath, opt);

                    // 获取到这个图片的原始宽度和高度
                    int picWidth = opt.outWidth;
                    int picHeight = opt.outHeight;

                    // 读取图片失败时直接返回
                    if (picWidth == 0 || picHeight == 0) {
                        return;
                    }

                    // 初始压缩比例
                    opt.inSampleSize = smallRate;
                    // 根据屏的大小和图片大小计算出缩放比例
                    if (picWidth > picHeight) {
                        if (picWidth > screenW) {
                            opt.inSampleSize *= picWidth / screenW;
                        }
                    } else {
                        if (picHeight > screenH) {
                            opt.inSampleSize *= picHeight / screenH;
                        }
                    }

                    // 这次再真正地生成一个有像素的，经过缩放了的bitmap
                    opt.inJustDecodeBounds = false;
                    final Bitmap bmp = BitmapFactory.decodeFile(filePath, opt);
                    // 存入map
                    imageCache.put(filePath, bmp);

                    handler.post(new Runnable() {
                        public void run() {
                            callback.imageLoaded(bmp);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return null;
    }

    /**
     * 异步读取SD卡图片，并按指定的比例进行压缩（最大不超过屏幕像素数）
     *
     * @param smallRate 压缩比例，不压缩时输入1，此时将按屏幕像素数进行输出
     * @param filePath  图片在SD卡的全路径
     * @param imageView 组件
     */
    public void loadImage(int smallRate, final String filePath,
                          final ImageView imageView) {

        Bitmap bmp = loadDrawable(smallRate, filePath, new ImageCallback() {

            @Override
            public void imageLoaded(Bitmap bmp) {
                if (imageView.getTag().equals(filePath)) {
                    if (bmp != null) {
                        imageView.setImageBitmap(bmp);
                    } else {
                        imageView.setImageResource(R.drawable.img_default_bg);
                    }
                }
            }
        });

        if (bmp != null) {
            if (imageView.getTag().equals(filePath)) {
                imageView.setImageBitmap(bmp);
            }
        } else {
            imageView.setImageResource(R.drawable.img_default_bg);
        }

    }

    /**
     * 缩放图片
     *
     * @param srcPath
     * @return
     */
    public Bitmap compressImageFromFile(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;// 只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = screenH;//
        float ww = screenW;//
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;// 设置采样率

        newOpts.inPreferredConfig = Config.RGB_565;// 该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        // return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
        // 其实是无效的,大家尽管尝试
        return bitmap;
    }

    public Bitmap createBitmap(String path) {
        int w = screenW;
        int h = screenH;
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            // 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
            BitmapFactory.decodeFile(path, opts);
            int srcWidth = opts.outWidth;// 获取图片的原始宽度
            int srcHeight = opts.outHeight;// 获取图片原始高度
            int destWidth = 0;
            int destHeight = 0;
            // 缩放的比例
            double ratio = 0.0;
            if (srcWidth < w || srcHeight < h) {
                ratio = 0.0;
                destWidth = srcWidth;
                destHeight = srcHeight;
            } else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
                ratio = (double) srcWidth / w;
                destWidth = w;
                destHeight = (int) (srcHeight / ratio);
            } else {
                ratio = (double) srcHeight / h;
                destHeight = h;
                destWidth = (int) (srcWidth / ratio);
            }
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            // 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
            newOpts.inSampleSize = (int) ratio + 1;
            // inJustDecodeBounds设为false表示把图片读进内存中
            newOpts.inJustDecodeBounds = false;
            newOpts.inPreferredConfig = Config.RGB_565;
            // 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
            newOpts.outHeight = destHeight;
            newOpts.outWidth = destWidth;
            // 获取缩放后图片
            return BitmapFactory.decodeFile(path, newOpts);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    // 对外界开放的回调接口
    public interface ImageCallback {
        // 注意 此方法是用来设置目标对象的图像资源
        void imageLoaded(Bitmap imageDrawable);
    }
}
