package com.xywy.askforexpert.module.main.prelaunch.follow;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.uilibrary.titlebar.ItemClickListener;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.uilibrary.dialog.base.AbsTitleDialog;
import com.xywy.askforexpert.module.main.guangchang.PromotionNewActivity;
import com.xywy.askforexpert.widget.module.follow.FollowViewPager;

/**
 * Created by bailiangjin on 2016/10/26.
 */

public class FollowDialog  extends AbsTitleDialog {



    FollowViewPager followViewPager;

    FollowPagerAdapter followPagerAdapter;

    Handler handler;
    public FollowDialog(Context context) {
        super(context);
    }

    public FollowDialog(Context context, Handler handler) {
        super(context);
        this.handler=handler;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_follw_college;
    }

    @Override
    protected boolean isNoTitle() {
        return false;
    }

    @Override
    protected void bindSubView(View view) {
        followViewPager = (FollowViewPager) view.findViewById(R.id.vp_follow);
        followPagerAdapter= new FollowPagerAdapter(getContext(), handler,new ItemClickListener() {
            @Override
            public void onClick() {
                ToastUtils.shortToast("点击了下一步");
                followViewPager.setCurrentItem(1);

            }
        }, new ItemClickListener() {
            @Override
            public void onClick() {
                ToastUtils.shortToast("点击了加入");
                Message msg= new Message();
                msg.what=PromotionNewActivity.JOIN_GROUP_TAG;
                handler.sendMessage(msg);
            }
        });
        followViewPager.setAdapter(followPagerAdapter);
        followViewPager.setScrollable(false);
    }
}
