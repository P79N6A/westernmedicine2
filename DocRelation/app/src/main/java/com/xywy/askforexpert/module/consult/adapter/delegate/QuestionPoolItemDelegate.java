package com.xywy.askforexpert.module.consult.adapter.delegate;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.consultentity.QuestionsListEntity;
import com.xywy.askforexpert.module.consult.activity.ConsultChatActivity;
import com.xywy.askforexpert.module.consult.adapter.ConsultListAdapter;
import com.xywy.util.XYImageLoader;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by wkteam on 2017/4/27. stone 移植yimai
 */

public class QuestionPoolItemDelegate implements ItemViewDelegate<QuestionsListEntity> {
    private boolean mIsFromWTKOrHistory;
    private ConsultListAdapter.ImDetailCall imDetailCall;

    public QuestionPoolItemDelegate(boolean isFromWTKOrHistory,ConsultListAdapter.ImDetailCall imDetailCall) {
        this.mIsFromWTKOrHistory = isFromWTKOrHistory;
        this.imDetailCall = imDetailCall;
    }

    public QuestionPoolItemDelegate(boolean isFromWTKOrHistory) {
        this.mIsFromWTKOrHistory = isFromWTKOrHistory;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_consult_question;
    }

    @Override
    public boolean isForViewType(QuestionsListEntity item, int position) {
        return item.getType() == QuestionsListEntity.TYPE_POOL_ENTITY;
    }

    @Override
    public void convert(ViewHolder holder, final QuestionsListEntity dataBean, int position) {
        XYImageLoader.getInstance().displayUserHeadImage(
                dataBean.getQuestionPoolEntity().getUser_photo(), (ImageView) holder.getView(R.id.iv_patient_head));
        holder.setText(R.id.tv_patient_name, dataBean.getQuestionPoolEntity().getPatient_name());
        holder.setText(R.id.tv_patient_sex, QuestionsListEntity.sexMap.get(dataBean.getQuestionPoolEntity().getPatient_sex()));
        //stone 新增月 天
        holder.setText(R.id.tv_patient_age, dataBean.getQuestionPoolEntity().getPatient_age() + "岁"
                + ((!TextUtils.isEmpty(dataBean.getQuestionPoolEntity().getPatient_age_month()) && !dataBean.getQuestionPoolEntity().getPatient_age_month().equals("0")) ? (dataBean.getQuestionPoolEntity().getPatient_age_month() + "月") : "")
                + ((!TextUtils.isEmpty(dataBean.getQuestionPoolEntity().getPatient_age_day()) && !dataBean.getQuestionPoolEntity().getPatient_age_day().equals("0")) ? (dataBean.getQuestionPoolEntity().getPatient_age_day() + "天") : ""));
        //创建时间
        holder.setText(R.id.tv_time, dataBean.getQuestionPoolEntity().getCreated_time());
        holder.setText(R.id.tv_question_desc, dataBean.getQuestionPoolEntity().getContent());
        //问题金额 stone 5.4.0新添加字段
        holder.setText(R.id.tv_question_pay, dataBean.getQuestionPoolEntity().getType_tag());
//        if (dataBean.getQuestionPoolEntity().getType().equals(QuestionsListEntity.PAY_TYPE_FREE)) {
//            holder.setText(R.id.tv_question_pay, "免费");
//        } else if (dataBean.getQuestionPoolEntity().getType().equals(QuestionsListEntity.PAY_TYPE_RMB)) {
//            holder.setText(R.id.tv_question_pay, dataBean.getQuestionPoolEntity().getAmount() + "元");
//        } else if (dataBean.getQuestionPoolEntity().getType().equals(QuestionsListEntity.PAY_TYPE_RMB_SPECIFY)) {
//            holder.setText(R.id.tv_question_pay, "指定" + dataBean.getQuestionPoolEntity().getAmount() + "元");
//        } else if (dataBean.getQuestionPoolEntity().getType().equals(QuestionsListEntity.PAY_TYPE_POINTS)) {
//            holder.setText(R.id.tv_question_pay, dataBean.getQuestionPoolEntity().getAmount() + "积分");
//        }
        final Context context = holder.getConvertView().getContext();
        holder.getView(R.id.ll_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stone 去除无用参数
//                String patientInfo = QuestionsListEntity.sexMap.get(dataBean.getQuestionPoolEntity().getPatient_sex()) + " " +
//                        dataBean.getQuestionPoolEntity().getPatient_age() + "岁";
                imDetailCall.imDetailCall();
                ConsultChatActivity.startActivity(mIsFromWTKOrHistory, context,
                        dataBean.getQuestionPoolEntity().getQid(), dataBean.getQuestionPoolEntity().getUid(),
                        dataBean.getQuestionPoolEntity().getPatient_name(), dataBean.getQuestionPoolEntity().getType().equals(QuestionsListEntity.PAY_TYPE_RMB_SPECIFY));
            }
        });

    }

}
