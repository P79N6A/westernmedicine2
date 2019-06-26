package com.xywy.askforexpert.appcommon.utils.imageutils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hyphenate.util.EMLog;
import com.hyphenate.util.PathUtil;

/**
 * 废弃的图片加载方法 建议 使用ImageLoadUtils
 */
@Deprecated
public class ImageUtils {
//	public static String getThumbnailImagePath(String imagePath) {
//		String path = imagePath.substring(0, imagePath.lastIndexOf("/") + 1);
//		path += "th" + imagePath.substring(imagePath.lastIndexOf("/") + 1, imagePath.length());
//		EMLog.d("msg", "original image path:" + imagePath);
//		EMLog.d("msg", "thum image path:" + path);
//		return path;
//	}

    public static String getImagePath(String remoteUrl) {
        String imageName = remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1, remoteUrl.length());
        String path = PathUtil.getInstance().getImagePath() + "/" + imageName;
        EMLog.d("msg", "image path:" + path);
        return path;

    }


    public static String getThumbnailImagePath(String thumbRemoteUrl) {
        String thumbImageName = thumbRemoteUrl.substring(thumbRemoteUrl.lastIndexOf("/") + 1, thumbRemoteUrl.length());
        String path = PathUtil.getInstance().getImagePath() + "/" + "th" + thumbImageName;
        EMLog.d("msg", "thum image path:" + path);
        return path;
    }

    public static Bitmap getImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap;// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;// 压缩好比例大小后再进行质量压缩
    }
}
