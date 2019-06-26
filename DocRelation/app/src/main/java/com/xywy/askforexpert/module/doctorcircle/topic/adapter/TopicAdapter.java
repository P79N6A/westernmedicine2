package com.xywy.askforexpert.module.doctorcircle.topic.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.topics.MoreTopicItem;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by shihao on 16/4/20.
 */
public class TopicAdapter extends BaseAdapter implements SectionIndexer {

    private static final String TAG = "TopicAdapter";
    List<MoreTopicItem.ListEntity> mList;
    /**
     * 存放标题信息
     */
    private List<String> list;
    private SparseIntArray positionOfSection;
    private SparseIntArray sectionOfPosition;
    private LayoutInflater layoutInflater;

    private Context mContext;

    private int type;

    private String signKey = "";

    public TopicAdapter(Context context, List<MoreTopicItem.ListEntity> mList) {
        this.mList = mList;
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void bindData(List<MoreTopicItem.ListEntity> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    /**
     * 标记的内容
     */
    public void signText(String signKey) {
        this.signKey = signKey;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public MoreTopicItem.ListEntity getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.topic_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MoreTopicItem.ListEntity topic = getItem(position);
        if (topic == null) {
            DLog.e(TAG, "话题不能为空");
        } else {
            String topicName = topic.getTheme();
            String header = topic.getHeader();
            if (position == 0 || header != null && !header.equals(getItem(position - 1).getHeader())) {
                if ("".equals(header)) {
                    holder.headLayout.setVisibility(View.GONE);
                } else {
                    holder.headLayout.setVisibility(View.VISIBLE);
                    if (header.equals("历史搜索")) {
                        holder.headLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                        holder.headerIv.setVisibility(View.VISIBLE);
                        holder.headerIv.setImageResource(R.drawable.sea_history_icon);
                        type = 2;
                    } else {
                        type = 1;
                        holder.headLayout.setBackgroundColor(mContext.getResources().getColor(R.color.topic_item_bg));
                    }
                    holder.header.setText(header);
                }
            } else {
                holder.headLayout.setVisibility(View.GONE);
            }
            if (!"".equals(signKey)) {
                String content = "#" + topicName + "#";
                SpannableString s = new SpannableString(content);
                if (topicName.equals(signKey)) {
                    s.setSpan(new ForegroundColorSpan(Color.parseColor("#0dc3ce")), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    Pattern p = Pattern.compile(signKey);
                    Matcher m = p.matcher(s);
                    while ((m.find())) {
                        int start = m.start();
                        int end = m.end();
                        s.setSpan(new ForegroundColorSpan(Color.parseColor("#0dc3ce")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }

                holder.searchContent.setText(s);
            } else {
                if (type == 1) {
                    holder.searchContent.setText("#" + topicName + "#");
                } else if (type == 2) {
                    holder.searchContent.setText(topicName);
                } else {
                    holder.searchContent.setText(topicName);
                }
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

            String letter = getItem(i).getHeader();
            DLog.e(TAG, "topicAdapter getsection getHeader:" + letter + " name:" + getItem(i).getTheme());
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
        @Bind(R.id.header_iv)
        ImageView headerIv;
        @Bind(R.id.header)
        TextView header;
        @Bind(R.id.head_layout)
        RelativeLayout headLayout;
        @Bind(R.id.search_content)
        TextView searchContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
