package com.xywy.askforexpert.module.main.media.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.model.media.MediaNumberBean;
import com.xywy.askforexpert.module.docotorcirclenew.adapter.BaseUltimateRecycleAdapter;
import com.xywy.askforexpert.module.docotorcirclenew.adapter.viewholder.BaseRecycleViewHolder;
import com.xywy.askforexpert.module.main.media.MediaDetailActivity;
import com.xywy.askforexpert.widget.view.ultimaterecycleview.UltimateRecyclerviewViewHolder;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/12/21 14:32
 */

public class MediaTodayRecommendAdapter extends BaseUltimateRecycleAdapter<MediaNumberBean> {

    public MediaTodayRecommendAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_today_recommend, parent, false);
        RecyclerView.ViewHolder vh = new ItemViewHolder(v);
        return vh;
    }

    @Override
    public RecyclerView.ViewHolder newFooterHolder(View view) {
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
//        params.height= DensityUtils.dp2px(64);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(64));
//        view.setLayoutParams(params);
        return new OrderMoreViewHolder(view);
    }

    public  int getCustomLoadMoreViewId() {
        return R.layout.order_more;
    }
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder mViewHolder, int position) {
        ((UltimateRecyclerviewViewHolder) mViewHolder).onBindView(getItem(position));
    }

    class ItemViewHolder extends BaseRecycleViewHolder<MediaNumberBean> {
        ImageView ivUsrerPic;
        TextView tvMedianame;
        TextView tvOrderNum;
        TextView tvDescpription;

        public ItemViewHolder(View convertView) {
            super(convertView);
            //话题列表
            ivUsrerPic = (ImageView) convertView.findViewById(R.id.iv_usrer_pic);
            tvMedianame = (TextView) convertView.findViewById(R.id.tv_medianame);
            tvOrderNum = (TextView) convertView.findViewById(R.id.tv_order_num);
            tvDescpription = (TextView) convertView.findViewById(R.id.tv_descpription);

        }

        public void updateView(final Context context, final MediaNumberBean item) {
            super.updateView(context,item);
            ImageLoadUtils.INSTANCE.loadImageView(ivUsrerPic, item.getImg());
            tvMedianame.setText(item.getName());
            tvOrderNum.setText(item.getScore()+" 订阅");
            tvDescpription.setText(item.getIntroduce());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaDetailActivity.startActivity(v.getContext(),item.getMid());
                }
            });
        }
    }
}
