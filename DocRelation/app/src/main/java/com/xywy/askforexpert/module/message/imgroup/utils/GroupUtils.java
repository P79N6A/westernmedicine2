package com.xywy.askforexpert.module.message.imgroup.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.text.TextUtils;

import com.xywy.askforexpert.appcommon.utils.FileUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.model.im.group.ContactBean;
import com.xywy.askforexpert.model.im.group.GroupModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by bailiangjin on 16/7/4.
 */
public class GroupUtils {

    public static String getGroupName(GroupModel groupModel) {
        if (null == groupModel) {
            return "";
        }
        return TextUtils.isEmpty(groupModel.getContactName()) ? "" : groupModel.getContactName();
    }

    public static String getUserName(ContactBean contactBean) {
        if (null == contactBean) {
            return "";
        }
        return TextUtils.isEmpty(contactBean.getUserName()) ? contactBean.getUserId() : contactBean.getUserName();
    }


    /**
     * 保存方法
     */
    public static String saveGroupHeadPhotoBitmap(Context context, Bitmap bitmap) {
        LogUtils.d("保存图片");

        String filePath=context.getFilesDir()+File.separator+"tempPicDir"+File.separator+"temp.png";
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        } else {
            FileUtils.makeDirs(filePath);
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            LogUtils.d("save Success");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }


    /**
     * 缩放Bitmap图片
     **/
    public static Bitmap zoomBitmap(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int width = w / 2;
        int height = h / 2;
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出
        Bitmap newBitMap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newBitMap;
    }


}
