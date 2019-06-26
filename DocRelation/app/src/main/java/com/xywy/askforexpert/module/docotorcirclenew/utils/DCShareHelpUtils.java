package com.xywy.askforexpert.module.docotorcirclenew.utils;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.xywy.askforexpert.appcommon.base.webview.CommonWebViewActivity;
import com.xywy.askforexpert.module.discovery.answer.AnswerMainActivity;
import com.xywy.askforexpert.module.discovery.answer.activity.AnswerDetailActivity;
import com.xywy.askforexpert.module.discovery.answer.activity.AnswerMultiLevelListActivity;
import com.xywy.askforexpert.module.discovery.answer.constants.AnswerShareConstants;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.type.ShareSourceType;
import com.xywy.askforexpert.module.main.media.MediaDetailActivity;
import com.xywy.askforexpert.module.main.service.media.InfoDetailActivity;
import com.xywy.askforexpert.module.main.subscribe.video.VideoNewsActivity;
import com.xywy.askforexpert.module.message.share.ShareWebActivity;

/**
 * Created by bailiangjin on 2016/11/10.
 */

public class DCShareHelpUtils {

    public static final String SHARE_SOURCE_INFO = "1";
    public static final String SHARE_SOURCE_COMMON_WEB = "3";
    public static final String SHARE_SOURCE_MEDIA = "4";
    public static final String SHARE_SOURCE_VIDEO = "5";
    public static final String SHARE_SOURCE_ANSWER = "6";
    
    
    public static ShareSourceType getShareType(String typeStr){
        switch (typeStr){
            case SHARE_SOURCE_INFO:
                return ShareSourceType.INFO;
            case SHARE_SOURCE_COMMON_WEB:
                return ShareSourceType.COMMON_WEB;
            case SHARE_SOURCE_MEDIA:
                return ShareSourceType.MEDIA;
            case SHARE_SOURCE_VIDEO:
                return ShareSourceType.VIDEO;
            case SHARE_SOURCE_ANSWER:
                return ShareSourceType.ANSWER;
            default:
                return ShareSourceType.SHARE_WEB;
        }

    }


    public static void toShareWebPage(Activity activity, String shareTitle, String shareUrl, String shareImageUrl) {
        Intent intent = new Intent(activity, ShareWebActivity.class);
        intent.putExtra("url", shareUrl);
        if (shareTitle.contains("\n")) {
            int index = shareTitle.indexOf("\n");
            intent.putExtra("title", shareTitle.substring(0, index));
        } else {
            intent.putExtra("title", shareTitle);
        }
        intent.putExtra("imageUrl", shareImageUrl);
        intent.putExtra("posts_id", "");
        activity.startActivity(intent);
    }

    public static void toShareAnswerPage(Activity activity, String extraData) {
        String share_other = extraData;

        String shareAnswerId = "", shareAnswerTitle = "", answerType = "", shareAnswerVersion = "";
        if (share_other.contains("|")) {
            String[] split = share_other.split("\\|");
            if (split.length > 0) {
                shareAnswerId = split[0];
            }
            if (split.length > 1) {
                shareAnswerTitle = split[1];
            }

            if (split.length > 2) {
                answerType = split[2];
            }

            if (split.length > 3) {
                shareAnswerVersion = split[3];
            }

        }

        if (!TextUtils.isEmpty(answerType) && answerType.equals(AnswerShareConstants.shareSourceType_Home)) {
            AnswerMainActivity.start(activity);
        } else if (!TextUtils.isEmpty(answerType) && answerType.equals(AnswerShareConstants.shareSourceType_List)) {
            AnswerMultiLevelListActivity.start(activity, shareAnswerId, shareAnswerTitle);
        } else if (!TextUtils.isEmpty(answerType) && answerType.equals(AnswerShareConstants.shareSourceType_Detail)) {
            AnswerDetailActivity.start(activity, shareAnswerId, shareAnswerTitle, shareAnswerVersion, -1);
        } else {
            AnswerMainActivity.start(activity);
        }
    }

    public static void toShareVideoPage(Activity activity, String extraData) {
        if (extraData != null && !"".equals(extraData)) {
            String[] ids = extraData.split("\\|");
            if (ids.length >= 3 && ids[0] != null && !ids[0].equals("")) {
                Intent intent = new Intent(activity, VideoNewsActivity.class);
                intent.putExtra(VideoNewsActivity.NEWS_ID_INTENT_KEY, ids[0]);
                intent.putExtra(VideoNewsActivity.VIDEO_UUID_INTENT_KEY, ids[1] == null ? "" : ids[1]);
                intent.putExtra(VideoNewsActivity.VIDEO_VUID_INTENT_KEY, ids[2] == null ? "" : ids[2]);
                activity.startActivity(intent);
            } else {
                Toast.makeText(activity, "id错误", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, "id错误", Toast.LENGTH_SHORT).show();
        }
    }

    public static void toShareMediaPage(Activity activity, String shareUrl) {
        Intent intent = new Intent(activity, MediaDetailActivity.class);
        intent.putExtra("mediaId", shareUrl);
        activity.startActivity(intent);
    }

    public static void toCommonWeb(Activity activity, String shareTitle, String shareUrl, String shareImageUrl, String extraData) {
        Intent intent = new Intent(activity, CommonWebViewActivity.class);
        intent.putExtra("id", extraData);
        intent.putExtra("imageUrl", shareImageUrl);
        if (shareTitle.contains("\n")) {
            shareTitle = shareTitle.substring(0, shareTitle.indexOf("\n"));
        }
        intent.putExtra("isfrom", shareTitle);
        intent.putExtra("content_url", shareUrl);
        intent.putExtra("description", "");
        activity.startActivity(intent);
    }

    public static void toShareInfoPage(Activity activity, String shareTitle, String shareUrl, String shareImageUrl, String extraData) {
        Intent intent = new Intent(activity, InfoDetailActivity.class);
        intent.putExtra("title", shareTitle);
        intent.putExtra("url", shareUrl);
        intent.putExtra("imageurl", shareImageUrl);
        intent.putExtra("ids", extraData);
        activity.startActivity(intent);
    }
}
