package com.xywy.askforexpert.module.discovery.answer.adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseAdapter;
import com.xywy.askforexpert.model.answer.api.paper.Question;
import com.xywy.askforexpert.widget.module.answer.SheetTextView;

import java.util.List;

/**
 * Created by wangpeng on 16/8/23.
 * describ：
 * revise：
 */
public class AnswerSheetAdapter extends YMBaseAdapter<Question> {


    public AnswerSheetAdapter(Activity activity, List dataList) {
        super(activity, dataList);
    }

    /**
     * 获取 item layout ResId
     *
     * @return int item layout resid
     */
    @Override
    public int getItemLayoutResId() {
        return R.layout.answer_sheet_gride_item_view;
    }

    /**
     * 获取 ViewHolder ViewHolder自身实现初始化
     *
     * @param rootView
     * @return
     */
    @Override
    protected BaseViewHolder getViewHolder(View rootView) {
        return new ViewHolder(rootView);
    }

    public class ViewHolder extends BaseViewHolder<Question> {

        private SheetTextView sheetTextView;

        public ViewHolder(View rootView) {
            super(rootView);
            this.sheetTextView = (SheetTextView) rootView.findViewById(R.id.sheetTextView);
        }

        @Override
        public void show(int position, Question data) {
            int value = position + 1;
            sheetTextView.setmTextStr("" + value);
            if (data.isMulti() && data.isComplete() || !data.isMulti() && data.isAnswered()) {
                if (data.isRight()) {
                    sheetTextView.setTextColor(ContextCompat.getColor(context, R.color.color_tv_gree73cb75));
                    sheetTextView.setmCircleBackgroundColor(ContextCompat.getColor(context, R.color.color_in_greed8f6da));
                    sheetTextView.setmOuterCiclerColor(ContextCompat.getColor(context, R.color.color_out_gree7ed184));
                } else {
                    sheetTextView.setTextColor(ContextCompat.getColor(context, R.color.color_tv_redff686c));
                    sheetTextView.setmCircleBackgroundColor(ContextCompat.getColor(context, R.color.color_in_redffd8d9));
                    sheetTextView.setmOuterCiclerColor(ContextCompat.getColor(context, R.color.color_out_redfe9b9e));

                }
            } else {
                sheetTextView.setTextColor(ContextCompat.getColor(context, R.color.answer_tv_color3));
                sheetTextView.setmCircleBackgroundColor(ContextCompat.getColor(context, R.color.white));
                sheetTextView.setmOuterCiclerColor(ContextCompat.getColor(context, R.color.color_out_nodedede));

            }
        }
    }
}
