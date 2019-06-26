package com.xywy.askforexpert.module.main.diagnose.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseAdapter;
import com.xywy.askforexpert.model.TreatmentListInfo;

import java.util.List;

/**
 * 诊断纪录 详情页面
 *
 * @author 王鹏
 * @2015-5-22下午2:53:42
 */
@SuppressLint("ResourceAsColor")
public class BaseTrenatmentAdapter extends YMBaseAdapter<TreatmentListInfo> {

    public SparseBooleanArray selectionMap;

    public BaseTrenatmentAdapter(Activity activity, List dataList) {
        super(activity, dataList);
    }


    @Override
    public int getItemLayoutResId() {
        return R.layout.treatment_list_item;
    }

    @Override
    protected BaseViewHolder getViewHolder(View rootView) {
        return new ViewHolder(rootView);
    }

    public void init() {
        for (int i = 0; i < getCount(); i++) {
            selectionMap = new SparseBooleanArray();
            selectionMap.put(i, false);
        }
    }

    private class ViewHolder extends BaseViewHolder<TreatmentListInfo> {
        TextView tv_title;
        TextView tv_date;
        TextView tv_img;

        public ViewHolder(View rootView) {
            super(rootView);
            this.tv_title = (TextView) rootView.findViewById(R.id.tv_title);
            this.tv_date = (TextView) rootView.findViewById(R.id.tv_date);
            this.tv_img = (TextView) rootView.findViewById(R.id.tv_img);
        }

        @Override
        public void show(int position, TreatmentListInfo data) {
            tv_date.setText(data.getAddtime());
            tv_title.setText(data.getContent());
            List<String> list2 = data.getImgs();
            if (list2 != null && list2.size() > 0) {
                tv_img.setVisibility(View.VISIBLE);
            } else {
                tv_img.setVisibility(View.GONE);
            }
        }


    }
}
