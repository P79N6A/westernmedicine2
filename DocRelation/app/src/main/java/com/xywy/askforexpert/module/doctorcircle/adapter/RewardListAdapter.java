package com.xywy.askforexpert.module.doctorcircle.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.CommonBaseAdapter;
import com.xywy.askforexpert.appcommon.base.adapter.CommonViewHolder;
import com.xywy.askforexpert.model.discussDetail.WinningList;

import java.util.List;

/**
 * 获奖名单 适配器
 * <p>
 * Compiler: Android Studio
 * Project: D_Platform
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2015/12/23 9:28
 */
public class RewardListAdapter extends CommonBaseAdapter<WinningList> {
    private List<WinningList> mDatas;

    public RewardListAdapter(Context mContext, List<WinningList> mDatas, int mItemLayoutId) {
        super(mContext, mDatas, mItemLayoutId);

        this.mDatas = mDatas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mItemLayoutId, parent, false);
        }

        TextView rewardPos = CommonViewHolder.getView(convertView, R.id.reward_list_postion);
        TextView rewardName = CommonViewHolder.getView(convertView, R.id.reward_list_name);
        TextView rewardScore = CommonViewHolder.getView(convertView, R.id.reward_list_money);

        WinningList winningList = mDatas.get(position);
        rewardPos.setText(String.valueOf(position + 1));
        rewardName.setText(winningList.getName());
        rewardScore.setText(winningList.getScore());

        return convertView;
    }
}
