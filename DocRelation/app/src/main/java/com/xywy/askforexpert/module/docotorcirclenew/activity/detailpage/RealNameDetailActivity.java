package com.xywy.askforexpert.module.docotorcirclenew.activity.detailpage;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.model.api.BaseResultBean;
import com.xywy.askforexpert.model.doctor.Data;
import com.xywy.askforexpert.model.doctor.PraiseBean;
import com.xywy.askforexpert.model.doctor.User;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.DoctorInfoParam;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.MoreParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.rxevent.DCDeleteMsgEventBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.type.DCMsgType;
import com.xywy.askforexpert.module.docotorcirclenew.service.DoctorCircleService;
import com.xywy.askforexpert.module.doctorcircle.adapter.PraiseHeadAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 *  实名动态详情页
 * Created by bailiangjin on 2016/11/25.
 */

public class RealNameDetailActivity extends SuperDynamicDetailActivity {


    public static void start(Activity activity, String msgId) {
        Intent intent = new Intent(activity, RealNameDetailActivity.class);
        intent.putExtra("msgId", msgId);
        activity.startActivity(intent);
    }

    @Override
    public void setTitle() {
        titleBarBuilder.setTitleText("实名动态");
    }

    @Override
    public void setHeaderViewVisibility() {
        tv_hospital.setText(mDynamicDtaile.getData().getUser().getHospital());
        tv_post.setText(mDynamicDtaile.getData().getUser().getSubject());
        rl_delete.setVisibility(View.GONE);

    }

    @Override
    public void setHeaderViewData() {

        final Data data = mDynamicDtaile.getData();
        final User user = data.getUser();
        final List<PraiseBean> praiseListBean = mDynamicDtaile.getData().getPraiselist();

        if (null != praiseListBean && !praiseListBean.isEmpty()) {
            final List<PraiseBean> praiseListBeanNew = new ArrayList<>();
            if (praiseListBean.size() > 6) {
                praiseListBeanNew.addAll(praiseListBean.subList(0, 6));
            } else {
                praiseListBeanNew.addAll(praiseListBean);
            }

            gv_praise_head.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PraiseBean item = praiseListBeanNew.get(position);
                    clickService.onHeadImgClick(RealNameDetailActivity.this, new DoctorInfoParam("", item.getUserid(), ""));
                }
            });
            if (null == praiseListAdapter) {
                praiseListAdapter = new PraiseHeadAdapter(this, praiseListBeanNew);
                gv_praise_head.setAdapter(praiseListAdapter);
            } else {
                praiseListAdapter.setData(praiseListBeanNew);
                praiseListAdapter.notifyDataSetChanged();
            }
        } else {
            if (null != praiseListAdapter) {
                praiseListAdapter.clearData();
            }
        }

        //实名界面独有
        iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                clickService.onMoreClick(RealNameDetailActivity.this, new MoreParamBean(0, msgId, data.getUserid(), data.getRelation(), DCMsgType.REAL_NAME), new CommonResponse<BaseResultBean>() {
//                    @Override
//                    public void onNext(BaseResultBean baseResultBean) {
//                        //删除完成 关闭界面
//                        YmRxBus.notifyDeleteDoctorCircleMsg(new DCDeleteMsgEventBean(msgId, DCMsgType.REAL_NAME));
//                        finish();
//                    }
//                });

                //添加验证认证状态
                DialogUtil.showUserCenterCertifyDialog(RealNameDetailActivity.this, new MyCallBack() {
                    @Override
                    public void onClick(Object object) {
                        clickService.onMoreClick(RealNameDetailActivity.this, new MoreParamBean(0, msgId, data.getUserid(), data.getRelation(), DCMsgType.REAL_NAME), new CommonResponse<BaseResultBean>() {
                            @Override
                            public void onNext(BaseResultBean baseResultBean) {
                                //删除完成 关闭界面
                                YmRxBus.notifyDeleteDoctorCircleMsg(new DCDeleteMsgEventBean(msgId, DCMsgType.REAL_NAME));
                                finish();
                            }
                        });
                    }
                }, null, null);
            }
        });

        tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickService.onNameClick(RealNameDetailActivity.this, new DoctorInfoParam(user.getType(), user.getUserid(), "" + user.getRelation()));
            }
        });

        iv_head_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickService.onNameClick(RealNameDetailActivity.this, new DoctorInfoParam(user.getUserType(), user.getUserid(), "" + user.getRelation()));
            }
        });

    }

    @Override
    public void comment(String curUserId, String commentId, String toUserId, String content, Subscriber subscriber) {
        DoctorCircleService.commentRealName(msgId, commentId, toUserId, content, subscriber);
    }

    @Override
    public DCMsgType getMSGType() {
        return DCMsgType.REAL_NAME;
    }
}
