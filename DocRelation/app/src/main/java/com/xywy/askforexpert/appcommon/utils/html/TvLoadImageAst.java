package com.xywy.askforexpert.appcommon.utils.html;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by bailiangjin on 16/6/16.
 */
public class TvLoadImageAst extends AsyncTask<Object, Void, Bitmap> {


    private LevelListDrawable mDrawable;
    private TextView textView;
    private int loadFailedImgResId;

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    protected Bitmap doInBackground(Object... params) {
        String source = (String) params[0];
        mDrawable = (LevelListDrawable) params[1];
        textView = (TextView) params[2];
        loadFailedImgResId = (int) params[3];
        try {
            InputStream is = new URL(source).openStream();
            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {


        } else {
            bitmap = drawableToBitmap(textView.getContext().getResources().getDrawable(loadFailedImgResId));
            //T.shortToast("网络异常试题图片加载失败");
        }
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
        mDrawable.addLevel(1, 1, bitmapDrawable);
        mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        mDrawable.setLevel(1);
        // i don't know yet a better way to refresh TextView
        // invalidate() doesn't work as expected
        CharSequence content = textView.getText();
        textView.setText(content);
    }
}
