package com.xywy.askforexpert.module.docotorcirclenew.adapter;

/**
 * Created by Marshal Chen on 3/8/2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.model.doctor.InterestePersonItemBean;
import com.xywy.askforexpert.model.doctor.RealNameItem;
import com.xywy.askforexpert.model.topics.MoreTopicItem;
import com.xywy.askforexpert.module.docotorcirclenew.adapter.viewholder.BaseRecycleViewHolder;
import com.xywy.askforexpert.module.docotorcirclenew.adapter.viewholder.ContentRecycleViewHolder;
import com.xywy.askforexpert.module.doctorcircle.adapter.InterestAdapter;
import com.xywy.askforexpert.widget.view.MyListView;
import com.xywy.askforexpert.widget.view.ultimaterecycleview.UltimateRecyclerviewViewHolder;

import java.util.List;

//// TODO: 2016/11/15  卡顿问题待解决
public class CircleRealNameAdapter extends BaseUltimateRecycleAdapter<RealNameItem> {
    public CircleRealNameAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent) {
        View v=LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_circle_content, parent, false);
        RecyclerView.ViewHolder vh=new ContentRecycleViewHolder(uiListener,v);
        return vh;
    }
    static final int RecommondType =11;
    static final int TopicType =12;
    protected int getCustomViewType(final int pos) {
        RealNameItem item=getItem(pos);
        //推荐好友
        if ("userlist".equals(item.listtype)) {
            return RecommondType;
        }  //话题
        else if ("themelist".equals(item.listtype)) {
            return TopicType;
        } //医圈内容
        else if ("dynamic".equals(item.listtype)) {
            return VIEW_TYPES.NORMAL;
        }else {
            return VIEW_TYPES.NORMAL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateCustomViewHolder(ViewGroup parent, int type) {
        if (type== RecommondType){
            View v=LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_circle_recommondfriend, parent, false);
            RecyclerView.ViewHolder vh=new RecommondRecycleViewHolder(v);
            return vh;
        }else if (type== TopicType){
            View v=LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_circle_topic, parent, false);
            v.findViewById(R.id.iv_topic).setOnClickListener(uiListener);
            v.findViewById(R.id.iv_one_day).setOnClickListener(uiListener);
            RecyclerView.ViewHolder vh=new TopicRecycleViewHolder(v);
            return vh;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder mViewHolder, int position) {
        ((UltimateRecyclerviewViewHolder)mViewHolder).onBindView(getItem(position));
    }


    class RecommondRecycleViewHolder extends BaseRecycleViewHolder<RealNameItem> {

        //感兴趣的人
        LinearLayout ll_readdetaile;    //普通列表
        RelativeLayout interest_rl;     //整体布局
        MyListView interest_list;      //感兴趣人列表
        TextView find_more_tv;         //查看更多
        public RecommondRecycleViewHolder(View rootView) {
            super(rootView);
            //新增推荐好友列表
            ll_readdetaile = (LinearLayout) rootView.findViewById(R.id.ll_userdetaile);
            interest_rl = (RelativeLayout) rootView.findViewById(R.id.interest_rl);
            interest_list = (MyListView) rootView.findViewById(R.id.interest_list);
            find_more_tv = (TextView) rootView.findViewById(R.id.find_more_tv);
        }
        public void updateView(Context context, RealNameItem item) {
            super.updateView(context,item);
            interest_rl.setVisibility(View.VISIBLE);
            List<InterestePersonItemBean> interestePersonItemBeans = item.list;
            InterestAdapter interestAdapter = new InterestAdapter(context, interestePersonItemBeans, "1");

            interest_list.setAdapter(interestAdapter);

            find_more_tv.setOnClickListener(uiListener);

            itemView.setOnClickListener(uiListener);
        }

    }

    class TopicRecycleViewHolder extends BaseRecycleViewHolder<RealNameItem> {
        //话题
        RelativeLayout topic_rl_1, topic_rl_2, topic_rl_3, topic_rl_4;
        TextView topic_num_1, topic_num_2, topic_num_3;
        TextView topic_name_1, topic_name_2, topic_name_3;
        public TopicRecycleViewHolder(View rootView) {
            super(rootView);
            //话题列表
            topic_rl_1 = (RelativeLayout) rootView.findViewById(R.id.topic_rl_1);
            topic_rl_2 = (RelativeLayout) rootView.findViewById(R.id.topic_rl_2);
            topic_rl_3 = (RelativeLayout) rootView.findViewById(R.id.topic_rl_3);
            topic_rl_4 = (RelativeLayout) rootView.findViewById(R.id.topic_rl_4);
            topic_name_1 = (TextView) rootView.findViewById(R.id.topic_name_1);
            topic_name_2 = (TextView) rootView.findViewById(R.id.topic_name_2);
            topic_name_3 = (TextView) rootView.findViewById(R.id.topic_name_3);
            topic_num_1 = (TextView) rootView.findViewById(R.id.topic_num_1);
            topic_num_2 = (TextView) rootView.findViewById(R.id.topic_num_2);
            topic_num_3 = (TextView) rootView.findViewById(R.id.topic_num_3);
        }
        public void updateView(final Context context, RealNameItem mRealNameItem) {
            super.updateView(context,mRealNameItem);
            final List<MoreTopicItem.ListEntity> itemList = mRealNameItem.themelist;
            LogUtils.i("话题的个数" + (itemList == null ? 0 : itemList.size()));
            if (itemList != null && itemList.size() == 3) {
                topic_name_1.setText("#" + itemList.get(0).getTheme() + "#");
                topic_name_2.setText("#" + itemList.get(1).getTheme() + "#");
                topic_name_3.setText("#" + itemList.get(2).getTheme() + "#");
                topic_num_1.setText(itemList.get(0).getDynamicNum() == 0 ? "" : itemList.get(0).getDynamicNum() + "");
                topic_num_2.setText(itemList.get(1).getDynamicNum() == 0 ? "" : itemList.get(1).getDynamicNum() + "");
                topic_num_3.setText(itemList.get(2).getDynamicNum() == 0 ? "" : itemList.get(2).getDynamicNum() + "");
                topic_rl_1.setTag(itemList.get((0)));
                topic_rl_2.setTag(itemList.get((1)));
                topic_rl_3.setTag(itemList.get((2)));
                topic_rl_1.setOnClickListener(uiListener);
                topic_rl_2.setOnClickListener(uiListener);
                topic_rl_3.setOnClickListener(uiListener);
                topic_rl_4.setOnClickListener(uiListener);
            }
            itemView.setOnClickListener(uiListener);
        }
    }

//    class ItemViewHolder extends UltimateRecyclerviewViewHolder<RealNameItem>{
//        BaseRecycleViewHolder topic;
//        BaseRecycleViewHolder content;
//        BaseRecycleViewHolder recommond;
//        public ItemViewHolder(View rootView) {
//            super(rootView);
//
//            topic=new TopicRecycleViewHolder(rootView.findViewById(R.id.topic_layout));
//            content=new ContentRecycleViewHolder(uiListener, rootView.findViewById(R.id.ll_userdetaile));
//            recommond=new RecommondRecycleViewHolder(rootView.findViewById(R.id.interest_rl));
//        }
//        public void updateView(Context context, RealNameItem mRealNameItem) {
//            topic.dismiss();
//            content.dismiss();
//            recommond.dismiss();
//
//            BaseRecycleViewHolder show=getDataBinder(mRealNameItem);
//            show.updateView(context,mRealNameItem);
//        }
//        public BaseRecycleViewHolder getDataBinder(RealNameItem mRealNameItem) {//"listtype": "dynamic" "listtype": "userlist","themelist"
//            //推荐好友
//            if ("userlist".equals(mRealNameItem.listtype)) {
//                return recommond;
//            }  //话题
//            else if ("themelist".equals(mRealNameItem.listtype)) {
//                return topic;
//            } //医圈内容
//            else if ("dynamic".equals(mRealNameItem.listtype)) {
//                return content;
//            }
//            throw new RuntimeException();
//        }
//    }

}