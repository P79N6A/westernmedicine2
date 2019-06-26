package com.xywy.askforexpert.module.discovery.answer.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseAdapter;
import com.xywy.askforexpert.model.answer.show.AnsweredListItem;

import java.util.List;


/**
 * 错题列表 adapter
 * Created by bailiangjin on 16/3/21.
 */
public class AnsweredPaperListAdapter extends YMBaseAdapter<AnsweredListItem> {

    private boolean isMultiDelete = false;

    public AnsweredPaperListAdapter(Activity context, List<AnsweredListItem> list) {
        super(context, list);
    }

    public void setMultiDelete(boolean multiDelete) {
        isMultiDelete = multiDelete;
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_answered_list;
    }

    @Override
    public BaseViewHolder getViewHolder(View rootView) {
        return new ViewHolder(rootView);
    }


    public class ViewHolder extends BaseViewHolder<AnsweredListItem> {

        private final RelativeLayout rl_root;

        /**
         * 试题名
         */
        private final TextView tv_name;
        private final CheckBox cb_checked;

        public ViewHolder(View rootView) {
            super(rootView);
            this.rl_root = (RelativeLayout) this.rootView.findViewById(R.id.rl_root);
            this.tv_name = (TextView) this.rootView.findViewById(R.id.tv_name);
            this.cb_checked = (CheckBox) this.rootView.findViewById(R.id.cb_checked);
        }

        @Override
        public void show(int position, AnsweredListItem answeredListItem) {
            rl_root.setBackgroundColor(position % 2 == 0 ? context.getResources().getColor(R.color.white) : context.getResources().getColor(R.color.answer_list_view_grey_bg));

            if (null != answeredListItem && !TextUtils.isEmpty(answeredListItem.getName())) {
                tv_name.setText(answeredListItem.getName());
            } else {
                tv_name.setText("");
            }

            cb_checked.setVisibility(isMultiDelete ? View.VISIBLE : View.GONE);
            if (isMultiDelete) {
                cb_checked.setChecked(answeredListItem.isChecked());
            }

        }
    }
}
