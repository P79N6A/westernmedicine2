package com.xywy.askforexpert.module.message.msgchat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.imageutils.ImageCache;
import com.xywy.askforexpert.appcommon.utils.imageutils.ImageUtils;
import com.xywy.easeWrapper.EMChatManager;
import com.xywy.easeWrapper.EaseImageUtils;
import com.xywy.easeWrapper.utils.EaseCommonUtils;

import java.io.File;

public class LoadImageTask extends AsyncTask<Object, Void, Bitmap> {
    private ImageView iv = null;
    String localFullSizePath = null;
    String thumbnailPath = null;
    String remotePath = null;
    EMMessage message = null;
    ChatType chatType;
    Activity activity;

    @Override
    protected Bitmap doInBackground(Object... args) {
        thumbnailPath = (String) args[0];
        localFullSizePath = (String) args[1];
        remotePath = (String) args[2];
        chatType = (ChatType) args[3];
        iv = (ImageView) args[4];
        // if(args[2] != null) {
        activity = (Activity) args[5];
        // }
        message = (EMMessage) args[6];
        EMImageMessageBody imgBody = (EMImageMessageBody) message.getBody();
        File file = new File(thumbnailPath);
        if (file.exists()) {
            return EaseImageUtils.decodeScaleImage(thumbnailPath, 160, 160);
        } else if (new File(imgBody.thumbnailLocalPath()).exists()) {
            return EaseImageUtils.decodeScaleImage(imgBody.thumbnailLocalPath(), 160, 160);
        }
        else {
            if (message.direct() == EMMessage.Direct.SEND) {
                if (localFullSizePath != null && new File(localFullSizePath).exists()) {
                    return EaseImageUtils.decodeScaleImage(localFullSizePath, 160, 160);
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            EMClient.getInstance().chatManager().downloadThumbnail(message);
                            LogUtils.d("接收图片 重新下载");
                        }
                    }).start();
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    protected void onPostExecute(Bitmap image) {
        if (image != null) {
            iv.setImageBitmap(image);
            ImageCache.getInstance().put(thumbnailPath, image);
            iv.setClickable(true);
            iv.setTag(thumbnailPath);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (thumbnailPath != null) {
                        Intent intent = new Intent(activity, ShowBigImage.class);
                        File file = new File(localFullSizePath);
                        if (file.exists()) {
                            Uri uri = Uri.fromFile(file);
                            intent.putExtra("uri", uri);
                        } else {
                            // The local full size pic does not exist yet.
                            // ShowBigImage needs to download it from the server
                            // first
                            intent.putExtra("remotepath", remotePath);
                        }
                        if (message.getChatType() != EMMessage.ChatType.Chat) {
                            // delete the image from server after download
                        }
                        if (message != null && message.direct() == EMMessage.Direct.RECEIVE && !message.isAcked()) {
                            message.setAcked(true);
                            try {
                                // 看了大图后发个已读回执给对方
                                EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        activity.startActivity(intent);
                    }
                }
            });
        } else {
            if (message.status() == EMMessage.Status.FAIL) {
                if (EaseCommonUtils.isNetWorkConnected(activity)) {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            EMChatManager.getInstance().asyncFetchMessage(message);

                        }
                    }).start();
                }
            }else {
                if (null!=ImageUtils.getImage(thumbnailPath)){
                    iv.setImageBitmap(ImageUtils.getImage(thumbnailPath));
                }else {

                    EMImageMessageBody imgBody = (EMImageMessageBody) message.getBody();
                    LogUtils.d("ImageLoadUtils 加载图片:"+imgBody.getThumbnailUrl());
                    ImageLoadUtils.INSTANCE.loadImageView(iv,imgBody.getThumbnailUrl());
                }

            }

        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
