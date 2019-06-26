package com.xywy.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/4/1 13:42
 * 压缩工具类
 */

public class CompressUtil {
    public static Bitmap createCompressBitmap(String path, int maxWidth, int maxHeight) {
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            // 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
            BitmapFactory.decodeFile(path, opts);
            int srcWidth = opts.outWidth;// 获取图片的原始宽度
            int srcHeight = opts.outHeight;// 获取图片原始高度
            int destWidth;
            int destHeight;
            // 缩放的比例
            double ratio;
            if (srcWidth < maxWidth || srcHeight < maxHeight) {
                ratio = 0.0;
                destWidth = srcWidth;
                destHeight = srcHeight;
            } else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
                ratio = (double) srcWidth / maxWidth;
                destWidth = maxWidth;
                destHeight = (int) (srcHeight / ratio);
            } else {
                ratio = (double) srcHeight / maxHeight;
                destHeight = maxHeight;
                destWidth = (int) (srcWidth / ratio);
            }
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            // 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
            newOpts.inSampleSize = (int) ratio + 1;
            L.e("压缩"+newOpts.inSampleSize+"倍");
            // inJustDecodeBounds设为false表示把图片读进内存中
            newOpts.inJustDecodeBounds = false;
            // 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
            newOpts.outHeight = destHeight;
            newOpts.outWidth = destWidth;
            // 获取缩放后图片
            return BitmapFactory.decodeFile(path, newOpts);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void saveBitmap(Bitmap bitmap,String toFile){
        // save file
        try {
            File myCaptureFile = new File(toFile);
            FileOutputStream out = null;
            out = new FileOutputStream(myCaptureFile);
            if(bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)){
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
