package com.xywy.askforexpert.module.message.imgroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.message.imgroup.view.FrontViewToMove;
import com.xywy.askforexpert.module.message.imgroup.view.pinyin.PinyinKeyMapList;
import com.xywy.askforexpert.module.message.imgroup.view.pinyin.callback.FrontViewMovedListener;

import java.util.List;


/**
 * @param <T>
 * @author bailiangjin
 */
public abstract class PinyinBaseAdapter<T> extends BaseExpandableListAdapter {

    public static FrontViewToMove lastMovedView;
    protected boolean isCanScroll;
    private Context context;
    private ExpandableListView listView;
    private PinyinKeyMapList<T> keyMapList;
    private LayoutInflater inflater;


    public PinyinBaseAdapter(ExpandableListView listView, List<T> list, boolean isCanScroll) {
        this.context = listView.getContext();
        this.listView = listView;
        this.isCanScroll = isCanScroll;
        inflater = LayoutInflater.from(context);
        setListData(list);
    }

    /**
     * 清空View引用
     */
    public static void clearMovedView() {
        lastMovedView = null;
        //System.gc();//findbugs 建议去掉 gc
    }

    public void setListData(final List<T> list) {
        keyMapList = new PinyinKeyMapList<T>(list) {

            @Override
            public String getField(T t) {
                return getItemName(t);
            }
        };
    }

    public void expandGroup() {
        listView.setAdapter(this);
        for (int i = 0, length = this.getGroupCount(); i < length; i++) {
            listView.expandGroup(i);
        }
    }

    @Override
    public Object getChild(int groupId, int childId) {
        return keyMapList.getIndexList(groupId).get(childId);
    }

    @Override
    public long getChildId(int groupPosition, int childPositon) {
        return childPositon;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return keyMapList.getIndexList(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return keyMapList.getIndexList(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return keyMapList.getTypes().size();
    }

    @Override
    public long getGroupId(int groupPosition) {

        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(getFatherLayoutResId(), null);
            convertView.setClickable(true);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.tv_index);
        textView.setText(keyMapList.getTypes().get(groupPosition));
        return convertView;
    }

    @Override
    public boolean hasStableIds() {

        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = View.inflate(context, getChildLayoutResId(), null);
        } else {
            view = convertView;
        }
        T itemData = keyMapList.getIndexList(groupPosition).get(childPosition);
        if (canItemScroll(itemData)) {
            //添加滑动事件处理
            View frontView = view.findViewById(R.id.id_front);
            final FrontViewToMove frontViewToMove = new FrontViewToMove(frontView, listView, new FrontViewMovedListener() {
                @Override
                public void moved(FrontViewToMove view) {
                    lastMovedView = view;
                }
            });
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = getViewHolder(view, itemData);
            holder.getHolder(view);
            view.setTag(holder);
        }
        holder.setData(itemData);
        holder.show();
        return view;
    }

    public Context getContext() {
        return context;
    }

    public PinyinKeyMapList<T> getKeyMapList() {
        return keyMapList;
    }

    public PinyinBaseAdapter setKeyMapList(PinyinKeyMapList<T> keyMapList) {
        this.keyMapList = keyMapList;
        return this;
    }

    protected boolean canItemScroll(T itemData) {
        return isCanScroll;
    }

    protected abstract String getItemName(T itemData);

    protected abstract ViewHolder getViewHolder(View rootView, T itemData);

    protected abstract int getFatherLayoutResId();

    protected abstract int getChildLayoutResId();


    /**
     * ViewHolder基类
     */
    public abstract class ViewHolder {
        public T data;

        public ViewHolder(T itemData) {
            this.data = itemData;
        }

        public abstract ViewHolder getHolder(View view);

        public abstract void show();

        public T getData() {
            return data;
        }

        public ViewHolder setData(T data) {
            this.data = data;
            return this;
        }

    }


}
