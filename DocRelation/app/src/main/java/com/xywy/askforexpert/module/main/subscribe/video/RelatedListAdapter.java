package com.xywy.askforexpert.module.main.subscribe.video;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.CommonBaseAdapter;
import com.xywy.askforexpert.appcommon.base.adapter.CommonViewHolder;
import com.xywy.askforexpert.model.videoNews.VideoNewsBean;

import java.util.List;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/6/23 14:26
 */
public class RelatedListAdapter extends CommonBaseAdapter<VideoNewsBean.RelatedBean> {
    public RelatedListAdapter(Context mContext, List<VideoNewsBean.RelatedBean> mDatas, int mItemLayoutId) {
        super(mContext, mDatas, mItemLayoutId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mItemLayoutId, parent, false);
        }

        TextView relatedNewsTitle = CommonViewHolder.getView(convertView, R.id.related_news_title);
        TextView relatedNewsClickCount = CommonViewHolder.getView(convertView, R.id.related_news_click_count);

        if (mDatas != null && !mDatas.isEmpty() && position >= 0 && position < mDatas.size()) {
            VideoNewsBean.RelatedBean relatedBean = mDatas.get(position);
            relatedNewsTitle.setText(relatedBean.getTitle() == null ? "" : relatedBean.getTitle());
            relatedNewsClickCount.setText(String.valueOf(relatedBean.getClick()));
        }

        return convertView;
    }
}
