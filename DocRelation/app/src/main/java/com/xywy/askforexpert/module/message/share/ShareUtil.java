package com.xywy.askforexpert.module.message.share;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.askforexpert.module.doctorcircle.DoctorCircleSendMessageActivty;

/**
 * 分享工具类 stone
 * 作者：shihao
 * 时间：16/5/31 上午10:39
 * 文件：D_Platform
 * 描述：分享类
 * <p>
 * bailiangjin 2016/10/14 整体使用建造者模式重构
 */
public class ShareUtil {

    /**
     * 医脉应用图片Url
     */
    public static final String DEFAULT_SHARE_IMG_ULR = Constants.COMMON_SHARE_IMAGE_URL;
    /**
     * 医脉当前用户头像图片Url
     */
    public static final String DEFAULT_SHARE_USER_HEAD_IMG_ULR = YMApplication.getLoginInfo().getData().getPhoto();


    private Activity activity;

    private String title = "";
    private String text = "";
    private String targetUrl = "";
    private String imageUrl = "";
    private String shareId = "";
    /**
     * 5 视频原生首页  6 试题首页 7试题列表页 8 试题答题页
     */
    private String shareSource = "";
    private String from = "";

    private UMImage image;

    private String uu;
    private String vu;
    private String answertype;//试题分析那个类型 首页 列表页 答题页
    private String answerversion;

    final SHARE_MEDIA[] displayList = new SHARE_MEDIA[]{
            SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA
    };

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            LogUtils.d("platform" + platform);
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(activity, "收藏成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "分享成功", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(activity, "分享失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            LogUtils.d("platform" + platform);
            Toast.makeText(activity, "分享取消", Toast.LENGTH_SHORT).show();
        }
    };


    public ShareUtil(Builder builder) {
        this.activity = builder.mContext;
        this.title = builder.title;
        this.text = builder.text;
        this.targetUrl = builder.targetUrl;
        String imgUrlTemp = TextUtils.isEmpty(builder.imageUrl) ? DEFAULT_SHARE_IMG_ULR : builder.imageUrl;
        this.imageUrl = imgUrlTemp;
        this.image = new UMImage(activity, imgUrlTemp);
        this.shareId = builder.shareId;
        this.shareSource = builder.shareSource;
        this.from = builder.from;
        this.uu = builder.uu;
        this.vu = builder.vu;
        this.answertype = builder.answertype;
        this.answerversion = builder.answerversion;
    }


    public void outerShare() {
        ShareAction shareAction = null;
        if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
            SHARE_MEDIA[] displayList = new SHARE_MEDIA[]{
                    SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA
            };
            shareAction = new ShareAction(activity).setDisplayList(displayList);
        } else {
            shareAction = new ShareAction(activity).setDisplayList(displayList);
        }

        shareAction.setShareboardclickCallback(new ShareBoardlistener() {
            @Override
            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
                    new ShareAction(activity)
                            .withText(text)
                            .withTitle(title.equals(text) ? text : title + "\n" + text)
                            .withTargetUrl(targetUrl)
                            .withMedia(image)
                            .setPlatform(share_media)
                            .setCallback(umShareListener)
                            .share();
                } else {
                    new ShareAction(activity)
                            .withText(text)
                            .withTitle(title)
                            .withTargetUrl(targetUrl)
                            .withMedia(image)
                            .setPlatform(share_media)
                            .setCallback(umShareListener)
                            .share();
                }
            }
        })
                .open();
    }

    public void innerShare() {
        ShareAction shareAction = null;
        if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
            SHARE_MEDIA[] displayList = new SHARE_MEDIA[]{
                    SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA
            };
            shareAction = new ShareAction(activity).setDisplayList(displayList);
        } else {
            shareAction = new ShareAction(activity).setDisplayList(displayList);
            shareAction.addButton("share_name_card", "share_name_card", "icon_mpj_nor", "icon_mpj_nor")
                    .addButton("share_doc_circle", "share_doc_circle", "icon_yquan_nor", "icon_yquan_nor");
        }
        shareAction.setShareboardclickCallback(new ShareBoardlistener() {
            @Override
            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                if (snsPlatform.mShowWord.equals("share_name_card")) {
                    StatisticalTools.eventCount(activity, "shareCard");

                    //判断认证 stone
                    DialogUtil.showUserCenterCertifyDialog(activity, new MyCallBack() {
                        @Override
                        public void onClick(Object data) {


//                    if (YMUserService.isGuest()) {
//                        DialogUtil.LoginDialog(new YMOtherUtils(activity).context);
//                    } else {
                            Intent intent = new Intent(activity, ShareCardActivity.class);
                            intent.putExtra("id", shareId);
                            if (from.equals("media")) {
                                intent.putExtra("type", "shareNameCardMedia");
                                intent.putExtra("fCardTitle", "");
                                intent.putExtra("fCardHospital", "");
                                intent.putExtra("fCardDpart", text);
                                intent.putExtra("fCardName", title);
                            } else {
                                intent.putExtra("type", "consult");
                            }
                            //// TODO: 16/8/25 原生视频分享
                            if ("5".equals(shareSource)) {
                                intent.putExtra("uu", uu);
                                intent.putExtra("vu", vu);
                            }
                            //// TODO: 16/8/25  试题 首页 列表页 答题页
                            else if ("6".equals(shareSource)) {
                                intent.putExtra("shareAnswerTitle", uu);
                                intent.putExtra("shareAnswerId", vu);
                                intent.putExtra("shareAnswerType", answertype);
                                intent.putExtra("answerversion", answerversion);

                            }

                            if (TextUtils.isEmpty(title)) {
                                intent.putExtra("title", text);
                            } else {
                                if (text != null && !text.equals("")) {
                                    intent.putExtra("title", title + "\n" + text);
                                } else {
                                    intent.putExtra("title", title);
                                }
                            }
                            if (!TextUtils.isEmpty(targetUrl) && targetUrl.contains(CommonUrl.H5_EXAM)) {
                                targetUrl = CommonUrl.H5_EXAM;
                            }
                            intent.putExtra("url", targetUrl);
                            intent.putExtra("imageUrl", imageUrl);
                            intent.putExtra("shareSource", shareSource);
                            activity.startActivity(intent);

                        }
                    }, null, null);
                } else if (snsPlatform.mShowWord.equals("share_doc_circle")) {
                    //                        MobclickAgent.onEvent(context, "shareYq");
                    StatisticalTools.eventCount(activity, "shareYq");

                    //判断认证 stone
                    DialogUtil.showUserCenterCertifyDialog(activity, new MyCallBack() {
                        @Override
                        public void onClick(Object data) {


//                            if (YMUserService.isGuest()) {
//                                DialogUtil.LoginDialog(new YMOtherUtils(activity).context);
//                            } else {
                            Intent intent = new Intent(activity, DoctorCircleSendMessageActivty.class);
                            LogUtils.d("yq_title = " + title);
                            if (TextUtils.isEmpty(title)) {
                                intent.putExtra("title", text);
                            } else {
                                if (text != null && !text.equals("")) {
                                    intent.putExtra("title", title + "\n" + text);
                                } else {
                                    intent.putExtra("title", title);
                                }
                            }

                            if (from.equals("media")) {
                                intent.putExtra("tagURL", shareId);
                            } else {
                                intent.putExtra("tagURL", targetUrl);
                            }

                            if ("6".equals(shareSource)) {

                                intent.putExtra("shareAnswerTitle", uu);
                                intent.putExtra("shareAnswerId", vu);
                                intent.putExtra("shareAnswerType", answertype);
                                intent.putExtra("answerversion", answerversion);

                            }
                            intent.putExtra("id", shareId);
                            intent.putExtra("imgURL", imageUrl);
                            intent.putExtra("share_source", shareSource);// 一天2 资讯1
                            intent.putExtra("shareToDoctorCircle", "shareToDoctorCircle");
                            activity.startActivity(intent);
//                            }
                        }
                    }, null, null);
                } else if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
                    new ShareAction(activity)
                            .withText(text)
                            .withTitle(title.equals(text) ? text : title + "\n" + text)
                            .withTargetUrl(targetUrl)
                            .withMedia(image)
                            .setPlatform(share_media)
                            .setCallback(umShareListener)
                            .share();
                } else {
                    LogUtils.e("ShareAction:param:text:" + text);
                    LogUtils.e("ShareAction:param:title:" + title);
                    LogUtils.e("ShareAction:param:targetUrl:" + targetUrl);
                    LogUtils.e("ShareAction:param:share_media:" + share_media.toString());
                    new ShareAction(activity)
                            .withText(text)
                            .withTitle(title)
                            .withTargetUrl(targetUrl)
                            .withMedia(image)
                            .setPlatform(share_media)
                            .setCallback(umShareListener)
                            .share();
                }
            }
        }).open();
    }


    public static class Builder {
        Activity mContext;
        private String title = "";
        private String text = "";
        private String targetUrl = "";
        private String imageUrl = "";

        private String shareId = "";
        /**
         * 5 视频原生首页  6 试题首页 7试题列表页 8 试题答题页
         */
        private String shareSource = "";

        private String from = "";

        private String uu;
        private String vu;
        private String answertype;//试题分析那个类型 首页 列表页 答题页
        private String answerversion;


        public Builder() {
            this.title = YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID) ? "闻康医生助手分享" : "医脉分享";
            this.text = YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID) ? "闻康医生助手分享" : "医脉分享";
            ;
            this.targetUrl = "";
            this.imageUrl = "";
            this.shareId = "";
            this.shareSource = "";
            this.from = "";
            this.uu = "";
            this.vu = "";
            this.answertype = "";
            this.answerversion = "";
        }

        public ShareUtil build(Activity context) {
            this.mContext = context;
            return new ShareUtil(this);
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setText(String text) {
            this.text = TextUtils.isEmpty(text)?"  ":text;//解决分享，微博无法调起的bug
            return this;
        }

        public Builder setTargetUrl(String targetUrl) {
            this.targetUrl = targetUrl;
            return this;
        }

        public Builder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder setShareId(String shareId) {
            this.shareId = shareId;
            return this;
        }

        public Builder setShareSource(String shareSource) {
            this.shareSource = shareSource;
            return this;
        }

        public Builder setFrom(String from) {
            this.from = from;
            return this;
        }

        public Builder setVideoData(String uu, String vu) {
            this.uu = uu;
            this.vu = vu;
            return this;
        }

        public Builder setAnswerData(String answertitle, String answerid, String answertype, String answerversion) {
            this.uu = answertitle;
            this.vu = answerid;
            this.answertype = answertype;
            this.answerversion = answerversion;
            return this;
        }

    }


}
