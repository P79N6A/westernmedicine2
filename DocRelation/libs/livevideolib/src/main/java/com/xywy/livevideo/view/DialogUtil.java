package com.xywy.livevideo.view;

import android.app.Activity;

import com.xywy.base.view.MessageDialog;
import com.xywy.util.AppUtils;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/2 10:24
 */
public class DialogUtil {
    public static void showFinishPublish(final Activity c,MessageDialog.OnClickListener confirmListener) {
        final MessageDialog dialog = new MessageDialog(c);
        dialog.setMessage("确定结束直播");
        dialog.setOkButton("确定", confirmListener).setCancelButton("取消", new MessageDialog.OnClickListener() {
            @Override
            public void onClick(MessageDialog messageDialog) {
                dialog.dismiss();
            }
        }).show();
    }

    public static void showConflictDialog(final Activity c, String msg) {
        new MessageDialog(c)
                .setMessage(msg)
                .setOkButton("确定", new MessageDialog.OnClickListener() {

            @Override
            public void onClick(MessageDialog messageDialog) {
                messageDialog.dismiss();
                AppUtils.restart(c);
            }
        }).show();
    }
}
