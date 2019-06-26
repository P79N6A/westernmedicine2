package com.xywy.askforexpert.module.doctorcircle.topic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.topics.MoreTopicItem;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by shihao on 16/4/25.
 */
public class MoreItemAdapter extends BaseAdapter {

    List<MoreTopicItem.ListEntity> infoDataList;
    private Context mContext;
    private DisplayImageOptions displayImageOptions;

    private ImageLoader imageLoader;

    public MoreItemAdapter(Context context, List<MoreTopicItem.ListEntity> infoDataList) {
        mContext = context;
        this.infoDataList = infoDataList;
        displayImageOptions = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .showStubImage(R.drawable.img_default_bg)
                .showImageForEmptyUri(R.drawable.img_default_bg)
                .showImageOnFail(R.drawable.img_default_bg).cacheInMemory(true)
                .cacheOnDisc(true).build();

        imageLoader = ImageLoader.getInstance();
    }

    public void bindData(List<MoreTopicItem.ListEntity> infoDataList) {
        this.infoDataList = infoDataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return infoDataList.size();
    }

    @Override
    public MoreTopicItem.ListEntity getItem(int position) {
        return infoDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_more_topic, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (infoDataList != null && infoDataList.size() > 0) {
//            holder.moreItemPic.setImageResource(R.drawable.default_avatar);
            imageLoader.displayImage(infoDataList.get(position).getImage(), holder.moreItemPic, displayImageOptions);
            holder.itemTitle.setText(infoDataList.get(position).getTheme());
            if (!infoDataList.get(position).getDescription().isEmpty()) {
                holder.itemSubTitle.setVisibility(View.VISIBLE);
                holder.itemSubTitle.setText(infoDataList.get(position).getDescription());
            } else {
                holder.itemSubTitle.setVisibility(View.GONE);
            }
            holder.itemDynamic.setText(infoDataList.get(position).getDynamicNum() + "篇动态");
            holder.itemAttention.setText(infoDataList.get(position).getFansNum() + "篇关注");
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.more_item_pic)
        ImageView moreItemPic;
        @Bind(R.id.item_title)
        TextView itemTitle;
        @Bind(R.id.item_sub_title)
        TextView itemSubTitle;
        @Bind(R.id.item_dynamic)
        TextView itemDynamic;
        @Bind(R.id.item_attention)
        TextView itemAttention;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
