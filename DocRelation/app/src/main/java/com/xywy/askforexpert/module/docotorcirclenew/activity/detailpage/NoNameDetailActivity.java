package com.xywy.askforexpert.module.docotorcirclenew.activity.detailpage;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.model.api.BaseResultBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.rxevent.DCDeleteMsgEventBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.type.DCMsgType;
import com.xywy.askforexpert.module.docotorcirclenew.service.DoctorCircleService;

import rx.Subscriber;

/**
 * 匿名动态详情页
 * Created by bailiangjin on 2016/11/25.
 */

public class NoNameDetailActivity extends SuperDynamicDetailActivity {

    public static void start(Activity activity, String msgId) {
        Intent intent = new Intent(activity, NoNameDetailActivity.class);
        intent.putExtra("msgId", msgId);
        activity.startActivity(intent);
    }


    @Override
    public void setTitle() {
        titleBarBuilder.setTitleText("匿名动态");
    }

    @Override
    public void setHeaderViewVisibility() {

        tv_hospital.setVisibility(View.GONE);
        tv_post.setVisibility(View.GONE);
        v_divider.setVisibility(View.GONE);
        iv_more.setVisibility(View.GONE);

        rl_delete.setVisibility(YMUserService.getCurUserId().equals(mDynamicDtaile.getData().getUser().getUserid()) ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setHeaderViewData() {
        //匿名界面独有
        rl_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickService.onDeleteMsgClick(NoNameDetailActivity.this, msgId, new CommonResponse<BaseResultBean>() {
                    @Override
                    public void onNext(BaseResultBean baseResultBean) {
                        shortToast("删除成功");
                        YmRxBus.notifyDeleteDoctorCircleMsg(new DCDeleteMsgEventBean(msgId, DCMsgType.NO_NAME));
                        //删除完成 关闭界面
                        finish();
                    }
                });
            }
        });
    }



    @Override
    public void comment(String curUserId, String commentId, String toUserId, String content, Subscriber subscriber) {
        DoctorCircleService.commentNoName(curUserId, msgId, commentId, toUserId, content, subscriber);
    }

    @Override
    public DCMsgType getMSGType() {
        return DCMsgType.NO_NAME;
    }
}
