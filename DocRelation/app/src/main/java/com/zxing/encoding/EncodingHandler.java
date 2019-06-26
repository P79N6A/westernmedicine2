package com.zxing.encoding;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.Hashtable;

public final class EncodingHandler {
	private static final int BLACK = 0xff000000;
	
	public static Bitmap createQRCode(String str,int widthAndHeight ,Bitmap be) throws WriterException {
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();  
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); 
		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = BLACK;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		
		createQRCodeBitmapWithPortrait(bitmap,be);
		return bitmap;
	}
	
	
	//生成条形码...
	/**
	 * 在二维码上绘制头像
	 */
	private static void createQRCodeBitmapWithPortrait(Bitmap qr, Bitmap portrait) {
		// 头像图片的大小
		int portrait_W = portrait.getWidth();
		int portrait_H = portrait.getHeight();

		// 设置头像要显示的位置，即居中显示
		int left = (qr.getWidth()- portrait_W) / 2;
		int top = (qr.getHeight() - portrait_H) / 2;
		int right = left + portrait_W;
		int bottom = top + portrait_H;			
		
		Rect rect1 = new Rect(left, top, right, bottom);
		// 取得qr二维码图片上的画笔，即要在二维码图片上绘制我们的头像
		Canvas canvas = new Canvas(qr);
		
		
		// 设置我们要绘制的范围大小，也就是头像的大小范围
		Rect rect2 = new Rect(0, 0, portrait_W, portrait_H);
		
		// 开始绘制
		canvas.drawBitmap(portrait, rect2, rect1, null);
	}
	
	public static Bitmap createOneQRCode(String content) throws Exception {

		BitMatrix matrix = new MultiFormatWriter().encode(content,
				BarcodeFormat.CODE_128, 500, 200);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = 0xff000000;
				}
			}
		}

		Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bm.setPixels(pixels, 0, width, 0, 0, width, height);
		return bm;
	}
}
