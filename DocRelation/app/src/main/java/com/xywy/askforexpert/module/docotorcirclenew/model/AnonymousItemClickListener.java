package com.xywy.askforexpert.module.docotorcirclenew.model;

import android.app.Activity;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.utils.ClickUtil;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMDebugOnClickListener;
import com.xywy.askforexpert.model.doctor.NotRealNameItem;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.PraiseParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.ShareParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.type.DCMsgType;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.type.ShareDirectionType;
import com.xywy.askforexpert.module.docotorcirclenew.service.DcCommonClickService;
import com.xywy.askforexpert.module.message.share.ShareUtil;

public class AnonymousItemClickListener extends YMDebugOnClickListener {
    DcCommonClickService clickService = new DcCommonClickService();
    Activity context;
    IRecycleViewModel recycleViewModel;

    public AnonymousItemClickListener(Activity context, IRecycleViewModel recycleViewModel) {
        this.context= context;
        this.recycleViewModel = recycleViewModel;
    }

    public void shareTo(Object value) {
        String imageUrl = "";
        NotRealNameItem item = (NotRealNameItem) value;
        if (item.minimgs.size() > 0) {
            imageUrl = item.minimgs.get(0);
        } else {
            imageUrl = ShareUtil.DEFAULT_SHARE_IMG_ULR;
        }
        clickService.onShareBtnClick(context, new ShareParamBean(item.content, "分享自“医脉”", item.url, imageUrl, item.id, ShareDirectionType.OUTER_SHARE));
    }


    public void praise(Object value) {
        final NotRealNameItem item = (NotRealNameItem) value;
        clickService.onPraiseClick(new PraiseParamBean(item.id, "", "", DCMsgType.NO_NAME, new CommonResponse() {
            @Override
            public void onNext(Object o) {
                if ("1".equals(item.is_praise)) {
                    ToastUtils.shortToast("取消点赞成功");
                    item.is_praise = "0";
                    int praiseNum = Integer
                            .parseInt(item.praiseNum) - 1;
                    item.praiseNum = praiseNum + "";
                } else {
                    ToastUtils.shortToast("点赞成功");
                    item.is_praise = "1";
                    int praiseNum = Integer
                            .parseInt(item.praiseNum) + 1;
                    item.praiseNum = praiseNum + "";
                }
                recycleViewModel.notifyDataChanged();
            }
        }));
    }
    public void onClick(final View v) {
        final NotRealNameItem item = (NotRealNameItem) v.getTag();
        switch (v.getId()) {
            case R.id.ll_doctor_share://分享
                if (ClickUtil.isFastDoubleClick()) {
                    return;
                }
                //添加验证认证状态 stone
                DialogUtil.showUserCenterCertifyDialog(context, new MyCallBack() {
                    @Override
                    public void onClick(Object data) {
                        shareTo(item);
                    }
                }, null, null);

                break;
            case R.id.rl_anonymous://点击整个Item
            case R.id.tv_user_content://匿名内容
            case R.id.ll_discuss:// 评论
                if (ClickUtil.isFastDoubleClick()) {
                    return;
                }
//                //添加验证认证状态 stone
//                DialogUtil.showUserCenterCertifyDialog(context, new MyCallBack() {
//                    @Override
//                    public void onClick(Object data) {
//                        clickService.onItemClick(context, item.id, null, PublishType.Anonymous);
//                    }
//                }, null, null);
//                break;
            clickService.onItemClick(context, item.id, null, PublishType.Anonymous);
            case R.id.ll_praise:// 点赞
                if (ClickUtil.isFastDoubleClick()) {
                    return;
                }
                //添加验证认证状态 stone
                DialogUtil.showUserCenterCertifyDialog(context, new MyCallBack() {
                    @Override
                    public void onClick(Object data) {
                        praise(item);
                    }
                }, null, null);

//                clickService.onItemClick(context,item.id,null,PublishType.Anonymous);
                break;

        }
    }
}