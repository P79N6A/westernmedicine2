package com.xywy.askforexpert.module.docotorcirclenew.adapter;

/**
 * Created by Marshal Chen on 3/8/2016.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.model.doctor.RealNameItem;
import com.xywy.askforexpert.model.topics.TopicDetailData;
import com.xywy.askforexpert.module.docotorcirclenew.adapter.viewholder.ContentRecycleViewHolder;
import com.xywy.askforexpert.widget.ExpandableTextView;
import com.xywy.askforexpert.widget.view.ultimaterecycleview.UltimateRecyclerviewViewHolder;


public class CircleTopicAdapter extends BaseUltimateRecycleAdapter<RealNameItem> {

    public CircleTopicAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder newHeaderHolder(View view) {
        return new HeadViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_circle_content, parent, false);
        RecyclerView.ViewHolder vh = new ContentRecycleViewHolder(uiListener,v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder mViewHolder, int position) {
        ((UltimateRecyclerviewViewHolder) mViewHolder).onBindView(getItem(position));
    }


//    class ItemViewHolder extends UltimateRecyclerviewViewHolder<RealNameItem> {
//        ContentRecycleViewHolder topic;
//
//        public ItemViewHolder(View rootView) {
//            super(rootView);
//            topic = new ContentRecycleViewHolder(uiListener, rootView);
//        }
//
//        protected void updateView(Context context, RealNameItem mTopicDetailData) {
//            topic.updateView(context, mTopicDetailData);
//        }
//    }

    class HeadViewHolder extends UltimateRecyclerviewViewHolder<RealNameItem> {
        private static final String HOST = "主持人：";
        private static final String POSTS_NUM = "篇动态";
        private static final String FANS_NUM = "人关注";
        TopicDetailData.ListBean headerData;
        private ImageView topicAvatar;
        private TextView topicName;
        private TextView topicHost;
        private TextView topicPosts;
        private TextView topicFans;
        private AppCompatButton topicDetailFollow;
        private ExpandableTextView topicIntro;
        private LinearLayout headerView;

        public HeadViewHolder(View rootView) {
            super(rootView);
            headerView = (LinearLayout) rootView.findViewById(R.id.header_layout_top_half);
            topicAvatar = (ImageView) headerView.findViewById(R.id.topic_detail_avatar);
            topicName = (TextView) headerView.findViewById(R.id.topic_detail_topic_name);
            topicHost = (TextView) headerView.findViewById(R.id.topic_detail_topic_host);
            topicPosts = (TextView) headerView.findViewById(R.id.topic_detail_posts_count);
            topicFans = (TextView) headerView.findViewById(R.id.topic_detail_fans_count);
            topicDetailFollow = (AppCompatButton) headerView.findViewById(R.id.topic_detail_follow);

            topicIntro = (ExpandableTextView) headerView.findViewById(R.id.topic_detail_intro);
        }

        private void setFollowButton(boolean isFocus) {
            topicDetailFollow.setText(isFocus ? "已关注" : "+ 关注");
            topicDetailFollow.setBackgroundResource(isFocus ? R.drawable.transparent_rectangle_bg : R.drawable.white_rectangle_bg);
            topicDetailFollow.setTextColor(isFocus ? Color.WHITE : itemView.getResources().getColor(R.color.accentColor));
        }

        public void updateView(final Context context, RealNameItem mTopicDetailData) {
            headerData = (TopicDetailData.ListBean) headerView.getTag();
            if (headerData == null)
                return;
            ImageLoadUtils.INSTANCE.loadImageView(topicAvatar, headerData.getImage(), R.drawable.default_avatar);
            topicName.setText("#" + headerData.getTheme() + "#");
            if (headerData.getLevel() == 1 && headerData.getName() != null) {
                topicHost.setText(HOST + headerData.getName());
            } else {
                topicHost.setVisibility(View.GONE);
            }
            topicPosts.setText(headerData.getDynamicNum() + POSTS_NUM);
            topicFans.setText(headerData.getFansNum() + FANS_NUM);
            if (headerData.getDescription() != null && !headerData.getDescription().equals("")) {
                topicIntro.setText(headerData.getDescription());
            }
            topicDetailFollow.setOnClickListener(uiListener);
            topicDetailFollow.setVisibility(View.VISIBLE);
            setFollowButton(headerData.isFocused());
            topicHost.setOnClickListener(uiListener);
        }
    }
}