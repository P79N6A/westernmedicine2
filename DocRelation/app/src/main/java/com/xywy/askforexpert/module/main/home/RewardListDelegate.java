package com.xywy.askforexpert.module.main.home;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.model.home.HomeItemBean;
import com.xywy.askforexpert.widget.CircleImageView;
import com.xywy.util.TimeUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by xgxg on 2018/4/4.
 */

public class RewardListDelegate implements ItemViewDelegate<HomeItemBean> {
    Context context;
    private final SpannableStringBuilder spannableStringBuilder;
    private String text;
    private final ForegroundColorSpan span;

    public RewardListDelegate(Context context){
        this.context = context;
        spannableStringBuilder = new SpannableStringBuilder();
        span = new ForegroundColorSpan(context.getResources().getColor(R.color.c_fea116));
    }
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_home_reward;
    }

    @Override
    public boolean isForViewType(HomeItemBean item, int position) {
        return HomeItemBean.TYPE_LIST==item.getType();
    }

    @Override
    public void convert(ViewHolder holder, HomeItemBean homeItemBean, int position) {
        CircleImageView head = holder.getView(R.id.iv_head);
        TextView tv_depart = holder.getView(R.id.tv_depart);
        TextView tv_name = holder.getView(R.id.tv_name);
        TextView tv_reward_start_str = holder.getView(R.id.tv_reward_start_str);
        TextView tv_time = holder.getView(R.id.tv_time);
        ImageLoadUtils.INSTANCE.loadImageView(head, homeItemBean.image,R.drawable.reward_head_default_icon);
        tv_depart.setText(homeItemBean.depart);
        tv_name.setText(homeItemBean.doctorname);
        text = Constants.DOCTORGETREWARD_STR+homeItemBean.amount+"元"+Constants.REWARD_STR;
        spannableStringBuilder.clear();
        spannableStringBuilder.append(text);
        spannableStringBuilder.setSpan(span, Constants.DOCTORGETREWARD_STR.length(), Constants.DOCTORGETREWARD_STR.length()+homeItemBean.amount.length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_reward_start_str.setText(spannableStringBuilder);
        tv_time.setText(TimeUtils.getStrDate(homeItemBean.pay_time));
    }


}
