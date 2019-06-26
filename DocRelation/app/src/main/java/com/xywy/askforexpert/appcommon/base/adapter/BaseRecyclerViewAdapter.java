package com.xywy.askforexpert.appcommon.base.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xywy.askforexpert.appcommon.interfaces.IRecyclerView;

import java.util.List;

/**
 * Compiler: Android Studio
 * Project: RecyclerViewWithHeader
 * Author: 方琪
 * Email: fangqi@xywy.com
 * Date: 2015/11/9 11:29
 */

/**
 * BaseRecyclerViewAdapter，可添加头布局和脚布局以及无数据页面
 *
 * @param <T>
 */
@Deprecated
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected static final int TYPE_HEADER = 0;
    protected static final int TYPE_NORMAL = 1;
    protected static final int TYPE_FOOTER = 2;
    private static final String TAG = "BaseRecyclerViewAdapter";
    protected Context mContext;

    protected LayoutInflater mLayoutInflater;

    protected int mItemLayoutId;

    protected RecyclerView mRecyclerView;
    protected List<T> mDatas;
    protected boolean isLoading;
    private View mHeaderView;
    private View mFooterView;
    private View mEmptyView;
    private IRecyclerView.OnItemClickListener onItemClickListener;

    private IRecyclerView.OnLoadMoreListener onLoadMoreListener;

    public BaseRecyclerViewAdapter(Context context, List<T> datas, RecyclerView recyclerView) {
        this(context, datas, -1, recyclerView);
    }

    public BaseRecyclerViewAdapter(Context context, List<T> datas, int itemLayoutId, RecyclerView recyclerView) {
        this.mContext = context;
        this.mDatas = datas;
        this.mItemLayoutId = itemLayoutId;

        if (context != null) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        if (recyclerView != null) {
            this.mRecyclerView = recyclerView;

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    if (dy > 0) {
                        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                        int totalItemCount = layoutManager.getItemCount();
                        int visibleItemCount = layoutManager.getChildCount();
                        int firstVisibleItem = 0;
                        if (layoutManager instanceof LinearLayoutManager) {
                            firstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                        }
                        if (layoutManager instanceof StaggeredGridLayoutManager) {
                            StaggeredGridLayoutManager sglm = (StaggeredGridLayoutManager) layoutManager;
                            int spanCount = sglm.getSpanCount();
                            int[] firstVisiblePositions = sglm.findFirstVisibleItemPositions(new int[spanCount]);
                            firstVisibleItem = firstVisiblePositions[0];
                        }
                        if (!isLoading && (firstVisibleItem + visibleItemCount) >= totalItemCount) {
                            if (onLoadMoreListener != null) {
                                onLoadMoreListener.onLoadMore();
                            }
                            isLoading = true;
                        }
                    }
                }
            });
        }

        RecyclerView.AdapterDataObserver mObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkIfEmpty();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                checkIfEmpty();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                super.onItemRangeChanged(positionStart, itemCount, payload);
                checkIfEmpty();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                checkIfEmpty();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkIfEmpty();
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                checkIfEmpty();
            }
        };
        this.registerAdapterDataObserver(mObserver);
    }

    protected abstract RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType);

    protected abstract void onBind(RecyclerView.ViewHolder holder, int pos, T data);

    public void setOnItemClickListener(IRecyclerView.OnItemClickListener mOnItemClickListener) {
        this.onItemClickListener = mOnItemClickListener;
    }

    public void setOnLoadMoreListener(IRecyclerView.OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setIsLoading(boolean loading) {
        isLoading = loading;
    }

    public void addHeaderView(View mHeaderView) {
        this.mHeaderView = mHeaderView;
        this.notifyItemInserted(0);
    }

    public void addFooterView(View mFooterView) {
        this.mFooterView = mFooterView;
        this.notifyItemInserted(getItemCount());
    }

    public void removeHeaderView(View headerView) {
        if (this.mHeaderView != null && headerView != null && this.mHeaderView == headerView) {
            this.notifyItemRemoved(0);
            this.mHeaderView = null;
            notifyDataSetChanged();
        }
    }

    public void removeFooterView(View footerView) {
        if (this.mFooterView != null && footerView != null && this.mFooterView == footerView) {
            Log.d(TAG, "item count = " + getItemCount());
            this.notifyItemRemoved(getItemCount());
        }
    }

    public boolean hasFooterView(View footerView) {
        return this.mFooterView == footerView;
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public void setEmptyView(View emptyView) {
        if (emptyView != null) {
            emptyView.setVisibility(View.GONE);
        }
        this.mEmptyView = emptyView;
    }

    @Override
    public int getItemViewType(int position) {

        if (this.mHeaderView != null && position == 0) {
            return TYPE_HEADER;
        }

        if (this.mFooterView != null && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }

        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new Holder(this.mHeaderView);
        }

        if (viewType == TYPE_FOOTER) {
            return new Holder(this.mFooterView);
        }

        if (viewType == TYPE_NORMAL) {
            return onCreate(parent, viewType);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (this.getItemViewType(position) == TYPE_HEADER) {
            return;
        }

        if (this.getItemViewType(position) == TYPE_FOOTER) {
            return;
        }

        final int pos = getRealPosition(holder);
        if (holder != null && pos < getItemCount() - 1) {
            final T data = mDatas.get(pos);
            onBind(holder, pos, data);

            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.OnItemClick(v);
                    }
                });
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (getItemViewType(position) == TYPE_HEADER
                            || getItemViewType(position) == TYPE_FOOTER) ?
                            gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams
                && (holder.getLayoutPosition() == 0 || holder.getLayoutPosition() == getItemCount() - 1)) {
            ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
        }
    }

    protected int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();

        return this.mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        if (mDatas != null) {
            return mDatas.size() + getHeaderViewCount() + getFooterViewCount();
        }

        return 0;
    }

    public int getHeaderViewCount() {
        return this.mHeaderView == null ? 0 : 1;
    }

    public int getFooterViewCount() {
        return this.mFooterView == null ? 0 : 1;
    }

    private void checkIfEmpty() {
        if (this.mEmptyView == null) {
            return;
        }

        if (getItemCount() - getFooterViewCount() - getHeaderViewCount() > 0) {
            this.mEmptyView.setVisibility(View.GONE);
        } else {
            this.mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    class Holder extends RecyclerView.ViewHolder {
        public Holder(View itemView) {
            super(itemView);
        }
    }
}
