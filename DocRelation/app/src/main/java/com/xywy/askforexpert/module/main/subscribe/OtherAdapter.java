package com.xywy.askforexpert.module.main.subscribe;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;
import com.xywy.askforexpert.model.ChannelItem;

import java.util.List;

/**
 * 类描述:
 * 创建人: shihao on 16/1/5 15:22.
 *
 * @modified by Jack Fang
 */
public class OtherAdapter extends BaseAdapter {
    private final Context context;
    public List<ChannelItem> channelList;
    /**
     * 要删除的position
     */
    public int remove_position = -1;
    /**
     * 是否可见
     */
    boolean isVisible = true;
    int width;
    int widthItem;
    private TextView item_text;
    private int maxItemCount = Integer.MAX_VALUE;
    private GridView gridView;

    public OtherAdapter(Context context, List<ChannelItem> channelList, GridView gridView) {
        this.context = context;
        this.channelList = channelList;
        this.gridView = gridView;
    }

    public OtherAdapter(Context context, List<ChannelItem> channelList, int maxItemCount) {
        this.context = context;
        this.channelList = channelList;
        this.maxItemCount = maxItemCount;
    }

    public int getMaxItemCount() {
        return maxItemCount;
    }

    public void setMaxItemCount(int maxItemCount) {
        this.maxItemCount = maxItemCount;
    }

    @Override
    public int getCount() {
        return channelList == null ? 0 : channelList.size();
    }

    @Override
    public ChannelItem getItem(int position) {
        if (channelList != null && channelList.size() != 0) {
            return channelList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.channel_item, parent, false);

        width = AppUtils.getScreenWidth((Activity) context);
        widthItem = (width - DensityUtils.dp2px(40)) / 3;
        item_text = (TextView) view.findViewById(R.id.text_item);
        item_text.setMinWidth(widthItem);
        ChannelItem channel = getItem(position);
        item_text.setText(channel.getName());
        if (!isVisible && (position == -1 + channelList.size())) {
            item_text.setText("");
        }
        if (remove_position == position) {
            item_text.setText("");
        }

//        if (pagePos == 1 && channelList.size() > maxItemCount) {
//            int rowNum = channelList.size() % this.gridView.getNumColumns() == 0
//                    ? channelList.size() / this.gridView.getNumColumns()
//                    : channelList.size() / this.gridView.getNumColumns() + 1;
//            int maxRowNum = maxItemCount % this.gridView.getNumColumns() == 0
//                    ? maxItemCount / this.gridView.getNumColumns()
//                    : maxItemCount / this.gridView.getNumColumns() + 1;
//            if (rowNum > maxRowNum) {
//                rowNum = maxRowNum;
//            }
//            if (rowNum == maxRowNum) {
//                if (position > maxItemCount) {
//                    view.setVisibility(View.GONE);
//                }
//            }
//            ViewGroup.LayoutParams layoutParams = this.gridView.getLayoutParams();
//            if (layoutParams != null) {
//                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                layoutParams.height = DensityUtils.dp2px(38) * rowNum + DensityUtils.dp2px(10 * (rowNum - 1));
//                this.gridView.setLayoutParams(layoutParams);
//            }
//        }

        return view;
    }

    /**
     * 获取频道列表
     */
    public List<ChannelItem> getChannnelLst() {
        return channelList;
    }

    /**
     * 添加频道列表
     */
    public void addItem(ChannelItem channel) {
        channelList.add(channel);
        if (channelList.size() < maxItemCount) {
            notifyDataSetChanged();
        }
    }

    /**
     * 设置删除的position
     */
    public void setRemove(int position) {
        remove_position = position;
        notifyDataSetChanged();
        // notifyDataSetChanged();
    }

    /**
     * 删除频道列表
     */
    public void remove() {
        channelList.remove(remove_position);
        remove_position = -1;
        notifyDataSetChanged();
    }

    /**
     * 设置频道列表
     */
    public void setListDate(List<ChannelItem> list) {
        channelList = list;
    }

    /**
     * 获取是否可见
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * 设置是否可见
     */
    public void setVisible(boolean visible) {
        isVisible = visible;
    }

}

