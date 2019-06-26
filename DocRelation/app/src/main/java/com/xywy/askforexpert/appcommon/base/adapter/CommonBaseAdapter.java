package com.xywy.askforexpert.appcommon.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2015/11/27 10:15
 */
@Deprecated
public abstract class CommonBaseAdapter<T> extends BaseAdapter {
    protected final ImageLoader mImageLoader;
    protected LayoutInflater mLayoutInflater;
    protected Context mContext;
    protected List<T> mDatas;
    protected int mItemLayoutId;

    public CommonBaseAdapter(Context mContext, List<T> mDatas) {
        this(mContext, mDatas, -1);
    }

    public CommonBaseAdapter(Context mContext, List<T> mDatas, int mItemLayoutId) {
        if (mDatas != null) {
            this.mDatas = mDatas;
        } else {
            this.mDatas = new ArrayList<>();
        }

        if (mItemLayoutId != -1) {
            this.mItemLayoutId = mItemLayoutId;
        }

        mImageLoader = ImageLoader.getInstance();

        if (mContext != null) {
            this.mContext = mContext;
            mLayoutInflater = LayoutInflater.from(mContext);
        }
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return (mDatas != null && position >= 0 && position < getCount()) ? mDatas.get(position) : new Object();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 设置Adapter数据
     *
     * @param mDatas 需要设置到Adapter的数据
     */
    public void setDatas(List<T> mDatas) {
        if (mDatas != null) {
            this.mDatas = mDatas;
        }
    }

    /**
     * 更新Adapter数据
     *
     * @param mDatas 新增的数据
     */
    public void addDatas(List<T> mDatas) {
        if (mDatas != null) {
            if (this.mDatas == null) {
                this.mDatas = new ArrayList<>();
            }
            this.mDatas.addAll(mDatas);
            notifyDataSetChanged();
        }
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
}
