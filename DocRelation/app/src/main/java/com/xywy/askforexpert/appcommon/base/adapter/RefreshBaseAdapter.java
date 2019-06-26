package com.xywy.askforexpert.appcommon.base.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.base.view.CircleProgressBar;

/**
 * 项目名称：D_Platform
 * 类名称：RefreshBaseAdapter
 * 类描述：刷新Adapter 底部自动加载更多 最底部显示加载完成
 * 创建人：shihao
 * 创建时间：2015-8-7 上午9:56:52
 * 修改备注：
 */
@Deprecated
public abstract class RefreshBaseAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static final int TYPE_FOOTER = Integer.MIN_VALUE;
    private static final int TYPE_ADAPTEE_OFFSET = 2;
    private boolean canLoadMore = false;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            return onCreateFooterViewHolder(parent, viewType);
        }
        return onCreateContentItemViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == getContentItemCount() && holder.getItemViewType() == TYPE_FOOTER) {
            onBindFooterView(holder, position);
        } else {
            onBindContentItemView(holder, position);
        }
    }

    public boolean isCanLoadMore() {
        return canLoadMore;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
//        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        int itemCount = getContentItemCount();
        if (useFooter()) {
            itemCount += 1;
        }
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getContentItemCount() && useFooter()) {
            return TYPE_FOOTER;
        }
        return getContentItemType(position) + TYPE_ADAPTEE_OFFSET;
    }

    public ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.refresh_footerview, parent, false);
        ViewHolderFooter vh = new ViewHolderFooter(v);
        return vh;
    }

    public void onBindFooterView(ViewHolder holder, int position) {
        ViewHolderFooter footerHolder = (ViewHolderFooter) holder;
        if (isCanLoadMore()) {
            footerHolder.pro.setVisibility(View.VISIBLE);
            footerHolder.pro.setmProgressColor(0x16A5D7);
            footerHolder.title.setText("正在加载中...");
        } else {
            footerHolder.pro.setVisibility(View.GONE);
            footerHolder.title.setText("没有数据啦");
        }
    }

    public abstract boolean useFooter();

    public abstract ViewHolder onCreateContentItemViewHolder(ViewGroup parent, int viewType);//创建你要的普通item

    public abstract void onBindContentItemView(ViewHolder holder, int position);//绑定数据

    public abstract int getContentItemCount();

    public abstract int getContentItemType(int position);

    private static class ViewHolderFooter extends ViewHolder {
        public CircleProgressBar pro;
        public TextView title;

        public ViewHolderFooter(View v) {
            super(v);
            pro = (CircleProgressBar) v.findViewById(R.id.pro);
            title = (TextView) v.findViewById(R.id.footerTitle);
        }
    }

}
