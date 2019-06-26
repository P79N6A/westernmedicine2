package com.xywy.askforexpert.module.doctorcircle.topic.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.topics.MyTopic;
import com.xywy.askforexpert.module.docotorcirclenew.activity.NewTopicDetailActivity;
import com.xywy.askforexpert.module.doctorcircle.topic.CreateEditTopicActivity;
import com.xywy.askforexpert.widget.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.xywy.askforexpert.appcommon.old.Constants.EDIT_TOPIC;

/**
 * 我的话题
 * Created by shihao on 16/4/26.
 */
public class MyTopicAdapter extends BaseAdapter implements SectionIndexer {//

    private static final String TAG = "MyTopicAdapter";
    private Context mContext;
    /**
     * 存放标题信息
     */
    private List<String> list;
    private SparseIntArray positionOfSection;
    private SparseIntArray sectionOfPosition;
    private List<MyTopic.DataEntity> topicList;

    public MyTopicAdapter(Context context, List<MyTopic.DataEntity> topicList) {
        this.mContext = context;
        this.topicList = topicList;
    }

    public void bindData(List<MyTopic.DataEntity> topicList) {
        this.topicList = topicList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return topicList.size();
    }

    @Override
    public MyTopic.DataEntity getItem(int position) {
        return topicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        DLog.i(TAG, "getView");
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_my_topic, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MyTopic.DataEntity topic = getItem(position);
        if (topic != null) {

            final String header = topic.getTitle();
            DLog.i(TAG, "头信息" + header);
            if (position == 0 || header != null && !header.equals(getItem(position - 1).getTitle())) {
                if ("".equals(header)) {
                    holder.headLayout.setVisibility(View.GONE);
                } else {
                    holder.headLayout.setVisibility(View.VISIBLE);
                    holder.header.setText(header);
                }
            } else {
                holder.headLayout.setVisibility(View.GONE);
            }
            if (topic.getThemes() != null) {
                final TopicGridViewAdapter adapter = new TopicGridViewAdapter(mContext, topic.getThemes());
                holder.myTopicGv.setAdapter(adapter);
                holder.myTopicGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Toast.makeText(mContext, "点击了第" + position + "话题", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, NewTopicDetailActivity.class);
                        if (header.equals("我主持的话题")) {
                            StatisticalTools.eventCount(mContext, "yqTopicMoreMineCreate");
                        } else if (header.equals("我关注的话题")) {
                            StatisticalTools.eventCount(mContext, "yqTopicMoreMineFocus");
                        } else if (header.equals("我参与的话题")) {
                            StatisticalTools.eventCount(mContext, "yqTopicMoreMineJoin");
                        }
                        StatisticalTools.eventCount(mContext, "");
                        intent.putExtra(NewTopicDetailActivity.TOPICID_INTENT_PARAM_KEY, adapter.getItem(position).getId());
                        mContext.startActivity(intent);
                    }
                });
            }

        }
        return convertView;
    }

    @Override
    public Object[] getSections() {
        positionOfSection = new SparseIntArray();
        sectionOfPosition = new SparseIntArray();
        int count = getCount();
        list = new ArrayList<String>();
        positionOfSection.put(0, 0);
        sectionOfPosition.put(0, 0);
        for (int i = 1; i < count; i++) {

            String letter = getItem(i).getTitle();
            int section = list.size() - 1;
            if (list.get(section) != null && !list.get(section).equals(letter)) {
                list.add(letter);
                section++;
                positionOfSection.put(section, i);
            }
            sectionOfPosition.put(i, section);
        }
        return list.toArray(new String[list.size()]);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return positionOfSection.get(sectionIndex);
    }

    @Override
    public int getSectionForPosition(int position) {
        return sectionOfPosition.get(position);
    }

    static class ViewHolder {
        @Bind(R.id.header)
        TextView header;
        @Bind(R.id.head_layout)
        RelativeLayout headLayout;
        @Bind(R.id.my_topic_gv)
        MyGridView myTopicGv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class TopicGridViewAdapter extends BaseAdapter {

        private Context context;
        private List<MyTopic.DataEntity.ThemesEntity> topicList;

        public TopicGridViewAdapter(Context context, List<MyTopic.DataEntity.ThemesEntity> topicList) {
            this.context = context;
            this.topicList = topicList;
        }

        @Override
        public int getCount() {
            return topicList.size();
        }

        @Override
        public MyTopic.DataEntity.ThemesEntity getItem(int position) {
            return topicList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_topic_gridview, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final MyTopic.DataEntity.ThemesEntity topic = topicList.get(position);
            if (topic != null) {
                holder.myTopicTv.setText("#" + topic.getTheme() + "#");
                if (topic.getLevel() == 1) {    //1编辑 0 不显示
                    holder.editIv.setVisibility(View.VISIBLE);
                    holder.myTopicNumTv.setVisibility(View.GONE);
                    holder.editIv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, CreateEditTopicActivity.class);
                            intent.putExtra(CreateEditTopicActivity.MODE_KEY, EDIT_TOPIC);
                            intent.putExtra(CreateEditTopicActivity.TOPIC_NAME_KEY, topic.getTheme());
                            intent.putExtra(CreateEditTopicActivity.TOPIC_COVER_KEY, topic.getImage());
                            intent.putExtra(CreateEditTopicActivity.TOPIC_ID_KEY, topic.getId());
                            intent.putExtra(CreateEditTopicActivity.TOPIC_INTRO_KEY, topic.getDescription());
                            intent.putExtra(CreateEditTopicActivity.TOPIC_TYPE_KEY, (ArrayList<? extends Parcelable>) topic.getCat());
                            context.startActivity(intent);
                        }
                    });
                } else {
                    holder.editIv.setVisibility(View.GONE);
                    holder.myTopicNumTv.setText(topic.getCount() == 0 ? "" : topic.getCount() + "");
                }

            }
            return convertView;
        }

        class ViewHolder {
            @Bind(R.id.my_topic_tv)
            TextView myTopicTv;
            @Bind(R.id.my_topic_num_tv)
            TextView myTopicNumTv;
            @Bind(R.id.edit_iv)
            ImageView editIv;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
