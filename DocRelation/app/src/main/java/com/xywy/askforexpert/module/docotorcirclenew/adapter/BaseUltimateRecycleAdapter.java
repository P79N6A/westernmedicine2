package com.xywy.askforexpert.module.docotorcirclenew.adapter;

/**
 * Created by Marshal Chen on 3/8/2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.widget.view.ultimaterecycleview.UltimateRecyclerviewViewHolder;
import com.xywy.askforexpert.widget.view.ultimaterecycleview.UltimateViewAdapter;

import java.util.List;

/**
 * 带默认加载更多的adapter
 * @param <T>
 */
public abstract class BaseUltimateRecycleAdapter<T> extends UltimateViewAdapter {
    protected final Context context;
    protected List<T> dataList;

    public View.OnClickListener getUiListener() {
        return uiListener;
    }

    public void setUiListener(View.OnClickListener uiListener) {
        this.uiListener = uiListener;
    }

    protected View.OnClickListener uiListener ;

    public BaseUltimateRecycleAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<T> list) {
        this.dataList = list;
    }

    @Override
    public RecyclerView.ViewHolder newFooterHolder(View view) {
        return new DefaultFootViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder newHeaderHolder(View view) {
        return new UltimateRecyclerviewViewHolder<>(view);
    }

    public  int getCustomLoadMoreViewId() {
        return R.layout.item_loading_more;
    }

    public class DefaultFootViewHolder<T> extends UltimateRecyclerviewViewHolder<T>{
        protected View mLoadingView,mNomore;
        public DefaultFootViewHolder(View itemView) {
            super(itemView);
            mLoadingView=itemView.findViewById(R.id.rl_load_more);
            mNomore=itemView.findViewById(R.id.no_more_data);
        }
        public void updateView(final Context context, T item) {
            super.updateView(context, item);
                if (state.hasMore()&&state.isLoadingData()){
                    mLoadingView.setVisibility(View.VISIBLE);
                    mNomore.setVisibility(View.GONE);
                }else{
                    mLoadingView.setVisibility(View.GONE);
                    mNomore.setVisibility(View.VISIBLE);
                }
        }
    }


    @Override
    public int getAdapterItemCount() {
        return null == dataList ? 0 : dataList.size();
    }

    public T getItem(int position) {
        if (customHeaderView != null)
            position--;
        // URLogs.d("position----"+position);
        if (position >= 0 && position < dataList.size())
            return dataList.get(position);
        else return null;
    }
}