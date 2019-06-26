package com.xywy.askforexpert.module.message.friend;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.module.message.share.ShareUtil;

/**
 * 邀请好友 stone
 *
 * @author 王鹏
 * @2015-6-12下午7:34:41
 */
public class InviteNewFriendMainActivity extends YMBaseActivity {

//    private UMSocialService mController;

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            DLog.d("plat", "platform" + platform);
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(InviteNewFriendMainActivity.this, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(InviteNewFriendMainActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(InviteNewFriendMainActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(InviteNewFriendMainActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
    private Tencent mTencent;
    private MyIUiListener mIUiListener;
    private Bundle params;


    public void onClick_back(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;

            case R.id.re_weixin:
                StatisticalTools.eventCount(InviteNewFriendMainActivity.this, "WeChat");
//                String appID = "wx7adc9ead4e1de7ef";
//                String appSecret = "c2958650c92241d801f105a2d1eaa4d9";

                new ShareAction(this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                        .withMedia(new UMImage(InviteNewFriendMainActivity.this, BitmapFactory.decodeResource(getResources(), R.drawable.dp_icon)))

                        .withTitle("快来跟我一起使用医脉吧！")
                        .withText("我正在使用医脉，它不仅可以让我在线坐诊，还可以与医界好友共同学习交流！")
//                        .withTargetUrl("http://fir.im/txyf")
                        .withTargetUrl("http://app.xywy.com/code.php?app=expert")
                        .share();
                break;

            case R.id.re_qq:
                StatisticalTools.eventCount(InviteNewFriendMainActivity.this, "QQ");
                shareToQQ();
//                new ShareAction(InviteNewFriendMainActivity.this).setPlatform(SHARE_MEDIA.TENCENT).setCallback(umShareListener)
//                        .withTitle("快来跟我一起使用医脉吧！")
//                        .withText("我正在使用医脉，它不仅可以让我在线坐诊，还可以与医界好友共同学习交流！")
//                        .withMedia(new UMImage(InviteNewFriendMainActivity.this, ShareUtil.DEFAULT_SHARE_IMG_ULR))
////                        .withTargetUrl("http://app.xywy.com/code.php?app=expert")
//                        .withTargetUrl("http://app.xywy.com/code.php?app=expert")
//                        .share();
                break;
            case R.id.re_phone:
                StatisticalTools.eventCount(InviteNewFriendMainActivity.this, "telenumber");
                intent = new Intent(InviteNewFriendMainActivity.this,
                        InviteNewFriendActivity.class);
                intent.putExtra("type", "invite");
                startActivity(intent);
                break;

            case R.id.person_interested:

                intent = new Intent(InviteNewFriendMainActivity.this,
                        AddNewCardHolderActivity.class);

                startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.invite_new_fiend_main;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_QQ_SHARE || resultCode == Constants.REQUEST_QZONE_SHARE || resultCode == Constants.REQUEST_OLD_SHARE) {
                Tencent.handleResultData(data, mIUiListener);
            }
        }
    }

    @Override
    protected void initView() {
//        if (!UMShareAPI.get(this).isAuthorize(this, SHARE_MEDIA.TENCENT)){
//            UMShareAPI.get(this).doOauthVerify(this, SHARE_MEDIA.TENCENT, authListener);
//        }
        titleBarBuilder.setTitleText("邀请好友");
        mTencent = Tencent.createInstance("1101752729",getApplicationContext());
        mIUiListener = new MyIUiListener();
    }


    @Override
    protected void initData() {

    }

   class MyIUiListener implements IUiListener{

       @Override
       public void onComplete(Object o) {
           // 操作成功
           Log.i("QQ分享","成功");
       }

       @Override
       public void onError(UiError uiError) {
           // 分享异常
           Log.i("QQ分享",uiError.errorMessage);
       }

       @Override
       public void onCancel() {
           // 取消分享
       }
   }

    private void shareToQQ() {
        params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "快来跟我一起使用医脉吧！");// 标题
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "我正在使用医脉，它不仅可以让我在线坐诊，还可以与医界好友共同学习交流！");// 摘要
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,"http://app.xywy.com/code.php?app=expert");// 内容地址
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,ShareUtil.DEFAULT_SHARE_IMG_ULR);// 网络图片地址　　params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "应用名称");// 应用名称
//        params.putString(QQShare.SHARE_TO_QQ_EXT_INT, "其它附加功能");
        ThreadManager.getMainHandler().post(new Runnable() {

            @Override
            public void run() {
                mTencent.shareToQQ(InviteNewFriendMainActivity.this, params, mIUiListener);
            }
        });
    }
}
