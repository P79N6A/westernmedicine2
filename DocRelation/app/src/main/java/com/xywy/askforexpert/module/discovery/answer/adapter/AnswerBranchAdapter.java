package com.xywy.askforexpert.module.discovery.answer.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseAdapter;
import com.xywy.askforexpert.appcommon.utils.tv.TVUtils;
import com.xywy.askforexpert.model.answer.show.AnswerItem;
import com.xywy.askforexpert.module.discovery.answer.service.ConvertUtils;

import java.util.List;

/**
 * 选项适配器
 * Created by bailiangjin on 16/4/18.
 */
public class AnswerBranchAdapter extends YMBaseAdapter<AnswerItem> {
    private boolean isAnswered;


    public AnswerBranchAdapter(Activity activity, List<AnswerItem> dataList, boolean isAnswered) {
        super(activity, dataList);
        this.isAnswered = isAnswered;

    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_for_branch_lv;
    }

    @Override
    public BaseViewHolder getViewHolder(View rootView) {
        return new ViewHolder(rootView);
    }


    public class ViewHolder extends BaseViewHolder<AnswerItem> {
        private final TextView tv_index;
        private final TextView tv_branch_desc;

        public ViewHolder(View rootView) {
            super(rootView);
            this.tv_branch_desc = (TextView) rootView.findViewById(R.id.tv_branch_desc);
            this.tv_index = (TextView) rootView.findViewById(R.id.tv_index);
        }

        @Override
        public void show(int position, AnswerItem item) {
            rootView.setBackgroundColor(position % 2 == 1 ? context.getResources().getColor(R.color.white) : context.getResources().getColor(R.color.answer_list_view_grey_bg));
            TVUtils.setTvWithHtmlContent(tv_branch_desc, item.getContent(), R.drawable.img_load_failed_small);
            String indexStr = ConvertUtils.getAnswerString(position);
            //以下两行 使得index显示 恢复初始状态
            tv_index.setText("");
            tv_index.setBackgroundResource(R.drawable.answer_select_circle_selector);

            if (item.isMulti()) {
                if (item.isComplete()) {
                    if (item.isChecked()) {
                        tv_index.setBackgroundResource(item.isRight() ? R.drawable.answer_select_icon_right : R.drawable.answer_select_icon_false);
                    } else if (item.isRight()) {
                        tv_index.setBackgroundResource(R.drawable.answer_select_icon_right);
                    } else {
                        tv_index.setEnabled(false);
                        tv_index.setText(indexStr);
                    }
                } else {
                    tv_index.setText(indexStr);
                    //设置选中效果 是否选中
                    tv_index.setEnabled(item.isChecked());
                }
                //多选题

            } else {
                //单选题
                if (item.isChecked()) {
                    tv_index.setBackgroundResource(item.isRight() ? R.drawable.answer_select_icon_right : R.drawable.answer_select_icon_false);
                } else if (isAnswered && item.isRight()) {
                    tv_index.setBackgroundResource(R.drawable.answer_select_icon_right);
                } else {
                    tv_index.setText(indexStr);
                    tv_index.setEnabled(false);
                }

            }

        }

    }

}
