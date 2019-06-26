package com.xywy.askforexpert.module.main.subscribe.adapter;

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
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.ChannelItem;

import java.util.List;

/**
 * 类描述:
 * 创建人: shihao on 16/1/5 15:05.
 *
 * @modified by Jack Fang
 */
public class DragAdapter extends BaseAdapter {
    /**
     * TAG
     */
    private final static String TAG = "DragAdapter";
    private final Context context;
    /**
     * 可以拖动的列表（即用户选择的频道列表）
     */
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
    /**
     * 是否显示底部的ITEM
     */
    private boolean isItemShow = false;
    /**
     * 控制的postion
     */
    private int holdPosition;
    /**
     * 是否改变
     */
    private boolean isChanged = false;
    /**
     * 列表数据是否改变
     */
    private boolean isListChanged = false;
    /**
     * TextView 频道内容
     */
    private TextView item_text;
    private int maxItemCount = Integer.MAX_VALUE;

    private GridView gridView;

    public DragAdapter(Context context, List<ChannelItem> channelList, GridView gridView) {
        this.context = context;
        this.channelList = channelList;
        this.gridView = gridView;
    }

    public DragAdapter(Context context, List<ChannelItem> channelList, int maxItemCount) {
        this.context = context;
        this.channelList = channelList;
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

    public int getMaxItemCount() {
        return maxItemCount;
    }

    public void setMaxItemCount(int maxItemCount) {
        this.maxItemCount = maxItemCount;
    }

    public void setMy_category_tip_text(TextView my_category_tip_text) {
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DLog.d(TAG, "user grid view position = " + position + ", maxItemCount = " + maxItemCount);
        View view = LayoutInflater.from(context).inflate(R.layout.channel_item, parent, false);

        width = AppUtils.getScreenWidth((Activity) context);
        widthItem = (width - DensityUtils.dp2px(40)) / 3;
        item_text = (TextView) view.findViewById(R.id.text_item);
        item_text.setMinWidth(widthItem);
        ChannelItem channel = getItem(position);
        if (channel != null) {
            item_text.setText(channel.getName());
            if (channel.getSelected() == 2) {
                item_text.setEnabled(false);
            }
            if (isChanged && (position == holdPosition) && !isItemShow) {
                item_text.setText("");
                item_text.setSelected(true);
                item_text.setEnabled(true);
                isChanged = false;
            }
            if (!isVisible && (position == -1 + channelList.size())) {
                item_text.setText("");
                item_text.setSelected(true);
                item_text.setEnabled(true);
            }
            if (remove_position == position) {
                item_text.setText("");
            }
        }

//        // 媒体号最多显示50个，即17行，重新计算GridView的高度
//        if (pagePos == 1 && channelList.size() > maxItemCount) {
//            DLog.d(TAG, "channelList size = " + channelList.size());
//            int rowNum = channelList.size() % this.gridView.getNumColumns() == 0
//                    ? channelList.size() / this.gridView.getNumColumns()
//                    : channelList.size() / this.gridView.getNumColumns() + 1;
//            ViewGroup.LayoutParams layoutParams = this.gridView.getLayoutParams();
//            DLog.d(TAG, "gridview layoutparams = " + (layoutParams == null));
//            if (layoutParams != null) {
//                DLog.d(TAG, "view getHeight = " + view.getHeight());
//                layoutParams.height = DensityUtils.dp2px(38) * rowNum + DensityUtils.dp2px(10 * (rowNum - 1));
//                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                this.gridView.setLayoutParams(layoutParams);
//            }
//        }

        return view;
    }

    /**
     * 添加频道列表
     */
    public void addItem(ChannelItem channel) {
        channelList.add(channel);
        isListChanged = true;
        notifyDataSetChanged();
    }

    /**
     * 拖动变更频道排序
     */
    public void exchange(int dragPostion, int dropPostion) {
        holdPosition = dropPostion;
        ChannelItem dragItem = getItem(dragPostion);
        DLog.d(TAG, "startPostion=" + dragPostion + ";endPosition=" + dropPostion);
        if (dragPostion < dropPostion) {
            channelList.add(dropPostion + 1, dragItem);
            channelList.remove(dragPostion);
        } else {
            channelList.add(dropPostion, dragItem);
            channelList.remove(dragPostion + 1);
        }
        isChanged = true;
        isListChanged = true;
        notifyDataSetChanged();
    }

    /**
     * 获取频道列表
     */
    public List<ChannelItem> getChannnelLst() {
        return channelList;
    }

    /**
     * 设置删除的position
     */
    public void setRemove(int position) {
        remove_position = position;
        notifyDataSetChanged();
    }

    /**
     * 删除频道列表
     */
    public void remove() {
        channelList.remove(remove_position);
        remove_position = -1;
        isListChanged = true;
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

    /**
     * 排序是否发生改变
     */
    public boolean isListChanged() {
        return isListChanged;
    }

    /**
     * 显示放下的ITEM
     */
    public void setShowDropItem(boolean show) {
        isItemShow = show;
    }

}

