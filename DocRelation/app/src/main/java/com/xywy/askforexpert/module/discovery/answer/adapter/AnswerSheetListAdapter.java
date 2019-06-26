package com.xywy.askforexpert.module.discovery.answer.adapter;

import android.app.Activity;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseAdapter;
import com.xywy.askforexpert.model.answer.api.paper.ChapterBean;
import com.xywy.askforexpert.widget.module.answer.AnswerSheetView;

import java.util.List;

/**
 * Created by wangpeng on 16/8/23.
 * describ：
 * revise：
 */
public class AnswerSheetListAdapter extends YMBaseAdapter<ChapterBean> {
    private Activity activity;
    private AnswerSheetView.SheetOnitemClickCallbackListenner onItemClickListener;

    public AnswerSheetListAdapter(Activity activity, List dataList, AnswerSheetView.SheetOnitemClickCallbackListenner onItemClickListener) {
        super(activity, dataList);
        this.activity = activity;
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 获取 item layout ResId
     *
     * @return int item layout resid
     */
    @Override
    public int getItemLayoutResId() {
        return R.layout.answer_sheet_main_item_view;
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

    public class ViewHolder extends BaseViewHolder<ChapterBean> {

        private AnswerSheetView answerSheetView;

        public ViewHolder(View rootView) {
            super(rootView);
            this.answerSheetView = (AnswerSheetView) rootView.findViewById(R.id.answer_sheet_view);
        }

        @Override
        public void show(int position, ChapterBean data) {
            answerSheetView.initData(activity, data.getFlatQuestionList());

            answerSheetView.setTheetType(data.getName());
            answerSheetView.setOnItemClickListenner(onItemClickListener);
        }
    }
}
