package com.xywy.askforexpert.module.my.smallstation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.umeng.analytics.social.UMSocialService;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.module.message.share.ShareUtil;

/**
 * 右边菜单
 *
 * @author 王鹏
 * @2015-5-11上午9:19:58
 */
public class Menu_right_fragment extends Fragment {

    private LinearLayout lin_share;

    private UMSocialService mController;
    private String userid;
    private String dpart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userid = YMApplication.getLoginInfo().getData().getPid();
        dpart = YMApplication.getLoginInfo().getData().getSubjectName();
        if (dpart == null) {
            dpart = "未填写";
        }
        // 初始化分享平台
//        initSocialSDK(YMApplication.getLoginInfo().getData().getRealname() + "的医脉名片",
//                "医院：" + YMApplication.getLoginInfo().getData().getHospital() + "\n" + "科室：" + dpart,
//                CommonUrl.DP_COMMON + "command=getInfo&template=1&userid=" +
//                        userid + "&sign=" + MD5Util.MD5(userid + "9ab41cc1bbef27fa4b5b7d4cbe17a75a"),
//                YMApplication.getLoginInfo().getData().getPhoto());
        return inflater.inflate(R.layout.fragment_right, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lin_share = (LinearLayout) getActivity().findViewById(R.id.lin_share);
        lin_share.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                new ShareUtil.Builder()
                        .setTitle(YMApplication.getLoginInfo().getData().getRealname() + "的医脉名片")
                        .setText( "医院：" + YMApplication.getLoginInfo().getData().getHospital() + "\n" + "科室：" + dpart)
                        .setTargetUrl(CommonUrl.DP_COMMON + "command=getInfo&template=1&userid=" +
                                userid + "&sign=" + MD5Util.MD5(userid + Constants.MD5_KEY))
                        .setImageUrl(YMApplication.getLoginInfo().getData().getPhoto())
                        .build(getActivity()).outerShare();


            }
        });

    }

    // 初始化分享平台
//    private void initSocialSDK(String shareTitle, String shareContent, String webUrl, String imageUrl) {
//        // 首先在您的Activity中添加如下成员变量
//        mController = UMServiceFactory.getUMSocialService("com.umeng.share");
//        mController.getConfig().removePlatform(SHARE_MEDIA.TENCENT,
//                SHARE_MEDIA.DOUBAN, SHARE_MEDIA.RENREN);
//        // wx7adc9ead4e1de7ef是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID ,
//        // http://dev.umeng.com/social/android/operation 分享平台不显示友盟
//        // 注册时候签名c2958650c92241d801f105a2d1eaa4d9
//        String appID = "wx7adc9ead4e1de7ef";
//        String appSecret = "c2958650c92241d801f105a2d1eaa4d9";
//        // 添加微信平台
//        UMWXHandler wxHandler = new UMWXHandler(getActivity(), appID,
//                appSecret);
//        wxHandler.addToSocialSDK();
//        WeiXinShareContent weixinContent = new WeiXinShareContent();
//        // 设置分享文字
//        weixinContent.setShareContent(shareContent);
//        // 设置title
//        weixinContent.setTitle(shareTitle);
//        // 设置分享内容跳转URL
//        weixinContent.setTargetUrl(webUrl);
//        // 设置分享图片
//        weixinContent.setShareImage(new UMImage(getActivity(), imageUrl));
//        mController.setShareMedia(weixinContent);
//
//        // 支持微信朋友圈
//        UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(),
//                appID, appSecret);
//        wxCircleHandler.setToCircle(true);
//        wxCircleHandler.addToSocialSDK();
//
//        // 设置微信朋友圈分享内容
//        CircleShareContent circleMedia = new CircleShareContent();
//        circleMedia.setShareContent(shareContent);
//        // 设置朋友圈title
//        circleMedia.setTitle(shareTitle);
//        circleMedia.setShareImage(new UMImage(getActivity(), imageUrl));
//        circleMedia.setTargetUrl(webUrl);
//        mController.setShareMedia(circleMedia);
//        // QQ平台
//        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(),
//                "1101752729", "LgrXeXE27j0PFDlU");
//        qqSsoHandler.addToSocialSDK();
//
//        QQShareContent qqShareContent = new QQShareContent();
//        // 设置分享文字
//        qqShareContent.setShareContent(shareContent);
//        // 设置分享title
//        qqShareContent.setTitle(shareTitle);
//        // 设置分享图片
//        qqShareContent
//                .setShareImage(new UMImage(getActivity(), imageUrl));
//        // 设置点击分享内容的跳转链接
//        qqShareContent.setTargetUrl(webUrl);
//        mController.setShareMedia(qqShareContent);
//
//        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
//                getActivity(), "1101752729",
//                "LgrXeXE27j0PFDlU");
//        qZoneSsoHandler.addToSocialSDK();
//
//        // qq空间
//        QZoneShareContent qzone = new QZoneShareContent();
//        // 设置分享文字
//        qzone.setShareContent(shareContent);
//        // 设置点击消息的跳转URL
//        qzone.setTargetUrl(webUrl);
//        // 设置分享内容的标题
//        qzone.setTitle(shareTitle);
//        // 设置分享图片
//        qzone.setShareImage(new UMImage(getActivity(), imageUrl));
//        mController.setShareMedia(qzone);
//
//        // // 新浪微博平台
////			mController.getConfig().setSsoHandler(new SinaSsoHandler());
//        SinaShareContent sinaContent = new SinaShareContent();
//        sinaContent.setTitle(shareTitle);
//        sinaContent.setShareContent(shareContent + webUrl);
//        sinaContent.setTargetUrl(webUrl);
//        sinaContent.setShareImage(new UMImage(getActivity(), imageUrl));
//        mController.setShareMedia(sinaContent);
//        mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ,
//                SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA);
//
//    }

    public void onResume() {
        super.onResume();
        StatisticalTools.fragmentOnResume("Menu_right_fragment");
    }

    public void onPause() {
        super.onPause();
        StatisticalTools.fragmentOnPause("Menu_right_fragment");
    }

}
