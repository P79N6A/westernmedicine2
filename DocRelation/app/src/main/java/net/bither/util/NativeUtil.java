/*
 * 由于要调用本地JNI 所以包名不能改 
 */
package net.bither.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

/**
 * 最新图片压缩方法
 * @author 王鹏
 * @version 
 * @2015-4-8下午4:37:06
 */
public class NativeUtil {
	private static int DEFAULT_QUALITY = 95;

	public static void compressBitmap(Bitmap bit, String fileName,
			boolean optimize) {
		compressBitmap(bit, DEFAULT_QUALITY, fileName, optimize);

	}

	public static void compressBitmap(Bitmap bit, int quality, String fileName,
			boolean optimize) {
		Log.d("native", "compress of native");

		 if (bit.getConfig() != Config.ARGB_8888) {
		Bitmap result = null;

		result = Bitmap.createBitmap(bit.getWidth() / 3, bit.getHeight() / 3,
				Config.ARGB_8888);// 缩小3倍
		Canvas canvas = new Canvas(result);
		Rect rect = new Rect(0, 0, bit.getWidth(), bit.getHeight());// original
		rect = new Rect(0, 0, bit.getWidth() / 3, bit.getHeight() / 3);// 缩小3倍
		canvas.drawBitmap(bit, null, rect, null);
		saveBitmap(result, quality, fileName, optimize);
		result.recycle();
		System.gc();
		 } else {
		 saveBitmap(bit, quality, fileName, optimize);
		 }

	}

	private static void saveBitmap(Bitmap bit, int quality, String fileName,
			boolean optimize) {

		compressBitmap(bit, bit.getWidth(), bit.getHeight(), quality,
				fileName.getBytes(), optimize);

	}

	private static native String compressBitmap(Bitmap bit, int w, int h,
			int quality, byte[] fileNameBytes, boolean optimize);

	static {
		System.loadLibrary("jpegbither");
		System.loadLibrary("bitherjni");

	}

}
