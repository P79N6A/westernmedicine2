package com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.model;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xywy.askforexpert.R;
import com.xywy.base.view.MessageDialog;
import com.xywy.util.ImageLoaderUtils;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/11/1 11:15
 */

public class DialogModel {

    public static void warn(Context context, String str) {
        new MessageDialog(context)
                .setTitle("温馨提示")
                .setMessage(str)
                .setOkButton("确定", new MessageDialog.OnClickListener() {
                    @Override
                    public void onClick(MessageDialog messageDialog) {
                        messageDialog.dismiss();
                    }
        }).show();
    }

    public static void picDetail(Context context, int resid) {
        @SuppressLint("InflateParams") LinearLayout layout = (LinearLayout) LayoutInflater.from(context)
                .inflate(R.layout.dialog_image, null);
        final Dialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        ImageView head_two = (ImageView) layout
                .findViewById(R.id.img_idcard_show);
        head_two.setBackgroundResource(resid);
    }

    public static void picDetail(Context context, String url) {
        @SuppressLint("InflateParams") LinearLayout layout = (LinearLayout) LayoutInflater.from(context)
                .inflate(R.layout.dialog_image, null);
        final Dialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        ImageView head_two = (ImageView) layout
                .findViewById(R.id.img_idcard_show);
        ImageLoaderUtils.getInstance().displayImage(url,head_two);
    }
}
