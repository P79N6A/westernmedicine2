package com.xywy.askforexpert.module.consult.adapter.delegate;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.model.consultentity.QuestionsListEntity;
import com.xywy.askforexpert.module.consult.activity.ConsultChatActivity;
import com.xywy.askforexpert.module.consult.adapter.ConsultListAdapter;
import com.xywy.util.XYImageLoader;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by wkteam on 2017/5/9. stone 移植yimai 历史回复
 */

public class QuestionAnsweredItemDelegate implements ItemViewDelegate<QuestionsListEntity> {
    private boolean isFromQuestionLibrary;

    private final SparseBooleanArray mCollapsedStatus;
    private ConsultListAdapter.ImDetailCall imDetailCall;

    public QuestionAnsweredItemDelegate(boolean isFromQuestionLibrary,ConsultListAdapter.ImDetailCall imDetailCall) {
        this.isFromQuestionLibrary = isFromQuestionLibrary;
        mCollapsedStatus = new SparseBooleanArray();
        this.imDetailCall = imDetailCall;
    }

    public QuestionAnsweredItemDelegate(boolean isFromQuestionLibrary) {
        this.isFromQuestionLibrary = isFromQuestionLibrary;
        mCollapsedStatus = new SparseBooleanArray();
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_consult_question;
    }

    @Override
    public boolean isForViewType(QuestionsListEntity item, int position) {
        return item.getType() == QuestionsListEntity.TYPE_ANSWERED_ENTITY;
    }

    @Override
    public void convert(ViewHolder holder, final QuestionsListEntity dataBean, int position) {

        //stone 添加评价 有评分就有评语
        if (dataBean.getAnsweredEntity().getComment() == null) {
            holder.getView(R.id.line_comment_show).setVisibility(View.VISIBLE);
            holder.getView(R.id.layout_comment).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.line_comment_show).setVisibility(View.GONE);
            holder.getView(R.id.layout_comment).setVisibility(View.VISIBLE);
            com.xywy.askforexpert.widget.expandabletextview.ExpandableTextView expandableTextView = holder.getView(R.id.expand_text_view);

            expandableTextView.setText(dataBean.getAnsweredEntity().getComment().getContent(), mCollapsedStatus, position);

            YMOtherUtils.handleStar((ImageView) holder.getView(R.id.iv_star), Integer.parseInt(dataBean.getAnsweredEntity().getComment().getSatisfied()) * 2);

        }

        //stone 总结与否
        if (dataBean.getAnsweredEntity().getIs_summary() == 1) {
            holder.setText(R.id.tv_question_desc, dataBean.getAnsweredEntity().getLast_content());
        } else {
            ((TextView) holder.getView(R.id.tv_question_desc)).setText(YMOtherUtils.getSpanContent("[未总结] " + dataBean.getAnsweredEntity().getLast_content()));
        }


        //stone 总结与否
//        if (dataBean.getAnsweredEntity().getIs_summary() == 1) {
//            holder.getView(R.id.tv_is_sumup).setVisibility(View.GONE);
//        } else {
//            holder.getView(R.id.tv_is_sumup).setVisibility(View.VISIBLE);
//        }

        XYImageLoader.getInstance().displayUserHeadImage(
                dataBean.getAnsweredEntity().getUser_photo(), (ImageView) holder.getView(R.id.iv_patient_head));
        holder.setText(R.id.tv_patient_name, dataBean.getAnsweredEntity().getPatient_name());
        holder.setText(R.id.tv_patient_sex, QuestionsListEntity.sexMap.get(dataBean.getAnsweredEntity().getPatient_sex()));
        //stone 新增月 天
        holder.setText(R.id.tv_patient_age, dataBean.getAnsweredEntity().getPatient_age() + "岁"
                + ((!TextUtils.isEmpty(dataBean.getAnsweredEntity().getPatient_age_month()) && !dataBean.getAnsweredEntity().getPatient_age_month().equals("0")) ? (dataBean.getAnsweredEntity().getPatient_age_month() + "月") : "")
                + ((!TextUtils.isEmpty(dataBean.getAnsweredEntity().getPatient_age_day()) && !dataBean.getAnsweredEntity().getPatient_age_day().equals("0")) ? (dataBean.getAnsweredEntity().getPatient_age_day() + "天") : ""));
        holder.setText(R.id.tv_time, dataBean.getAnsweredEntity().getCreated_time());
//        holder.setText(R.id.tv_question_desc, dataBean.getAnsweredEntity().getLast_content());
        //问题金额 stone 5.4.0新添加字段
        holder.setText(R.id.tv_question_pay, dataBean.getAnsweredEntity().getType_tag());
//        if (dataBean.getAnsweredEntity().getType().equals(QuestionsListEntity.PAY_TYPE_FREE)) {
//            holder.setText(R.id.tv_question_pay, "免费");
//        } else if (dataBean.getAnsweredEntity().getType().equals(QuestionsListEntity.PAY_TYPE_RMB)) {
//            holder.setText(R.id.tv_question_pay, dataBean.getAnsweredEntity().getAmount() + "元");
//        } else if (dataBean.getAnsweredEntity().getType().equals(QuestionsListEntity.PAY_TYPE_RMB_SPECIFY)) {
//            holder.setText(R.id.tv_question_pay, "指定" + dataBean.getAnsweredEntity().getAmount() + "元");
//        } else if (dataBean.getAnsweredEntity().getType().equals(QuestionsListEntity.PAY_TYPE_POINTS)) {
//            holder.setText(R.id.tv_question_pay, dataBean.getAnsweredEntity().getAmount() + "积分");
//        }
        final Context context = holder.getConvertView().getContext();
        holder.getView(R.id.ll_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stone 去除无用参数
//                String patientInfo = QuestionsListEntity.sexMap.get(dataBean.getAnsweredEntity().getPatient_sex()) + " " +
//                        dataBean.getAnsweredEntity().getPatient_age() + "岁";
                imDetailCall.imDetailCall();
                ConsultChatActivity.startActivity(isFromQuestionLibrary, context,
                        dataBean.getAnsweredEntity().getQid(), dataBean.getAnsweredEntity().getUid(),
                        dataBean.getAnsweredEntity().getPatient_name(), true, dataBean.getAnsweredEntity().getAllow_summary() == 0);
            }
        });
    }


}
