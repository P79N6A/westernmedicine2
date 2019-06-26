package com.xywy.askforexpert.module.consult.adapter.delegate;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.utils.ClickUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.model.consultentity.CommonRspEntity;
import com.xywy.askforexpert.model.consultentity.QuestionsListEntity;
import com.xywy.askforexpert.module.consult.ConsultConstants;
import com.xywy.askforexpert.module.consult.ServiceProvider;
import com.xywy.askforexpert.module.consult.activity.ConsultChatActivity;
import com.xywy.askforexpert.module.consult.adapter.ConsultListAdapter;
import com.xywy.util.XYImageLoader;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import rx.Subscriber;

/**
 * Created by zhangzheng on 2017/4/26. stone 移植yimai
 */

public class ConsultItemDelegate implements ItemViewDelegate<QuestionsListEntity> {

    private boolean mIsFromWTKOrHistory;
    private ConsultListAdapter.ImDetailCall imDetailCall;

    public ConsultItemDelegate(boolean isFromWTKOrHistory,ConsultListAdapter.ImDetailCall imDetailCall) {
        this.mIsFromWTKOrHistory = isFromWTKOrHistory;
        this.imDetailCall = imDetailCall;
    }

    public ConsultItemDelegate(boolean isFromWTKOrHistory) {
        this.mIsFromWTKOrHistory = isFromWTKOrHistory;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_consult_question;
    }

    @Override
    public boolean isForViewType(QuestionsListEntity item, int position) {
        return item.getType() == QuestionsListEntity.TYPE_CONSULT_ENTITY;
    }

    @Override
    public void convert(final ViewHolder holder, final QuestionsListEntity dataBean, int position) {
        //设置头像
        XYImageLoader.getInstance().displayUserHeadImage(
                dataBean.getConsultEntity().getUser_photo(), (ImageView) holder.getView(R.id.iv_patient_head));
        //患者名称
        holder.setText(R.id.tv_patient_name, dataBean.getConsultEntity().getPatient_name());
        //性别
        holder.setText(R.id.tv_patient_sex, QuestionsListEntity.sexMap.get(dataBean.getConsultEntity().getPatient_sex()));
        //是否已读
        if (dataBean.getConsultEntity().getIs_read() == 0) {
            holder.getView(R.id.tv_msg_tip).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.tv_msg_tip).setVisibility(View.GONE);
        }
        //stone 新增月 天
        //年龄
        holder.setText(R.id.tv_patient_age, dataBean.getConsultEntity().getPatient_age() + "岁"
                + ((!TextUtils.isEmpty(dataBean.getConsultEntity().getPatient_age_month()) && !dataBean.getConsultEntity().getPatient_age_month().equals("0")) ? (dataBean.getConsultEntity().getPatient_age_month() + "月") : "")
                + ((!TextUtils.isEmpty(dataBean.getConsultEntity().getPatient_age_day()) && !dataBean.getConsultEntity().getPatient_age_day().equals("0")) ? (dataBean.getConsultEntity().getPatient_age_day() + "天") : ""));
        //创建时间
        holder.setText(R.id.tv_time, dataBean.getConsultEntity().getCreated_time());
        //问题描述
        holder.setText(R.id.tv_question_desc, dataBean.getConsultEntity().getContent());
        if (dataBean.getConsultEntity().getReply_accept().equals("0")) {
            ((TextView) holder.getView(R.id.tv_question_desc)).setText(YMOtherUtils.getSpanContent("[新问题] " + dataBean.getConsultEntity().getContent()));
        } else{
            if (dataBean.getConsultEntity().getIs_summary() == 0) {
                ((TextView) holder.getView(R.id.tv_question_desc)).setText(YMOtherUtils.getSpanContent("[未总结] " + dataBean.getConsultEntity().getContent()));
            } else {
                holder.setText(R.id.tv_question_desc, dataBean.getConsultEntity().getContent());
            }
        }

        //问题金额 stone 5.4.0新添加字段
        holder.setText(R.id.tv_question_pay, dataBean.getConsultEntity().getType_tag());
        //old stone
//        if (dataBean.getConsultEntity().getType().equals(QuestionsListEntity.PAY_TYPE_FREE)) {
//            holder.setText(R.id.tv_question_pay, "免费");
//        } else if (dataBean.getConsultEntity().getType().equals(QuestionsListEntity.PAY_TYPE_RMB)) {
//            holder.setText(R.id.tv_question_pay, dataBean.getConsultEntity().getAmount() + "元");
//        } else if (dataBean.getConsultEntity().getType().equals(QuestionsListEntity.PAY_TYPE_RMB_SPECIFY)) {
//            holder.setText(R.id.tv_question_pay, "指定" + dataBean.getConsultEntity().getAmount() + "元");
//        } else if (dataBean.getConsultEntity().getType().equals(QuestionsListEntity.PAY_TYPE_POINTS)) {
//            holder.setText(R.id.tv_question_pay, dataBean.getConsultEntity().getAmount() + "积分");
//        }



        //跳转聊天界面 stone 去除无用参数
        final Context context = holder.getConvertView().getContext();
        holder.getView(R.id.ll_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imDetailCall.imDetailCall();
                //医脉在外面认领完进详情 未认领就认领 stone
                if (ClickUtil.isFastDoubleClick()) {
                    return;
                }
                if (!YMApplication.sIsYSZS) {
                    if (dataBean.getConsultEntity().getStatus() == 0) {
                        StatisticalTools.eventCount(YMApplication.getAppContext(), ConsultConstants.EVENT_QUESTION_CLAIM);
                        ServiceProvider.adoptQuestion(YMApplication.getUUid(), dataBean.getConsultEntity().getQid(), new Subscriber<CommonRspEntity>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                //stone 更换提示
                                ToastUtils.shortToast("认领失败,请重试");
                            }

                            @Override
                            public void onNext(CommonRspEntity commonRspEntity) {
                                if (commonRspEntity != null && commonRspEntity.getCode() == ConsultConstants.CODE_REQUEST_SUCCESS) {
                                    YmRxBus.notifyAdoptQuestionSuccess();
                                    ConsultChatActivity.startActivity(mIsFromWTKOrHistory, context,
                                            dataBean.getConsultEntity().getQid(), dataBean.getConsultEntity().getUid(),
                                            dataBean.getConsultEntity().getPatient_name(), dataBean.getConsultEntity().getType().equals(QuestionsListEntity.PAY_TYPE_RMB_SPECIFY)
                                    );
                                } else {
                                    //stone 更换提示
                                    ToastUtils.shortToast("认领失败,请重试");
                                }
                            }
                        });
                    } else {
                        ConsultChatActivity.startActivity(mIsFromWTKOrHistory, context,
                                dataBean.getConsultEntity().getQid(), dataBean.getConsultEntity().getUid(),
                                dataBean.getConsultEntity().getPatient_name(), dataBean.getConsultEntity().getType().equals(QuestionsListEntity.PAY_TYPE_RMB_SPECIFY)
                        );
                    }
                } else {
                    //医生助手目前不变 在详情中认领
//                String patientInfo = QuestionsListEntity.sexMap.get(dataBean.getConsultEntity().getPatient_sex()) + " " + dataBean.getConsultEntity().getPatient_age() + "岁";
                    ConsultChatActivity.startActivity(mIsFromWTKOrHistory, context,
                            dataBean.getConsultEntity().getQid(), dataBean.getConsultEntity().getUid(),
                            dataBean.getConsultEntity().getPatient_name(), dataBean.getConsultEntity().getType().equals(QuestionsListEntity.PAY_TYPE_RMB_SPECIFY)
                    );
                }
            }
        });

    }

}
