package com.xywy.askforexpert.widget.view.ultimaterecycleview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * An abstract adapter which can be extended for Recyclerview
 */
public abstract class UltimateViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements ItemTouchHelperAdapter {
    protected Handler timer = new Handler(Looper.getMainLooper());
    protected UltimateRecyclerView.CustomRelativeWrapper customHeaderView = null;
    protected View footView = null;
    protected View footViewItemView = null;

    /**
     * this watches how many times does this loading more triggered
     */
    private int loadmoresetingswatch = 0;
    protected int mEmptyViewPolicy;
    protected int mEmptyViewOnInitPolicy;

    /**
     * Lock used to modify the content of list. Any write operation
     * performed on the array should be synchronized on this lock.
     */
    protected final Object mLock = new Object();


    /**
     * Set the header view of the adapter.
     *
     * @param customHeaderView na
     */
    public void setCustomHeaderView(UltimateRecyclerView.CustomRelativeWrapper customHeaderView) {
        this.customHeaderView = customHeaderView;
    }

    public UltimateRecyclerView.CustomRelativeWrapper getCustomHeaderView() {
        return customHeaderView;
    }

    public boolean hasHeaderView() {
        return customHeaderView!=null;
    }
    public boolean hasFootView() {
        return getCustomLoadMoreViewId()!=View.NO_ID;
    }


    /**
     * Using a custom LoadMoreView
     *
     */
    public final void setFootView(Context context) {
        if (footView != null) {
            Log.d("", "The loading more layout has already been initiated.");
            return;
        }
        if (hasFootView()){
            footView = LayoutInflater.from(context).inflate(getCustomLoadMoreViewId(), null);
        }
    }

    public  int getCustomLoadMoreViewId(){
        return View.NO_ID;
    }



    protected   UltimateRecyclerView.State state;
    public void setState(UltimateRecyclerView.State state) {
        this.state=state;
    }


    private class DelayUpdateFootView implements Runnable {

        boolean enable;
        public DelayUpdateFootView(boolean enable) {
            this.enable=enable;
        }

        @Override
        public void run() {
            if (!enable && loadmoresetingswatch > 0 && hasFootView()) {
                final int displaySize = getItemCount();
                final int dataSize = getAdapterItemCount();
                if (dataSize > 0 && footViewItemView != null) {
                    notifyItemRemoved(displaySize - 1);
                }
                detectDispatchFootViewDisplay(getAdapterItemCount(), getItemCount());
            }

            if (enable) {
                recoverFootView();
            }
        }
    }

    public DelayUpdateFootView cbloadmore;

    /**
     * as the set function to switching load more feature
     *
     * @param enable
     */
    public final void updateFootView(boolean enable) {
        cbloadmore=new DelayUpdateFootView(enable);
    }

    public final void internalExecuteLoadingView() {
        if (cbloadmore != null) {
            timer.post(cbloadmore);
            loadmoresetingswatch++;
            cbloadmore = null;
        }
    }

    /**
     * Called by RecyclerView when it stops observing this Adapter.
     *
     * @param recyclerView The RecyclerView instance which stopped observing this adapter.
     * @see #onAttachedToRecyclerView(RecyclerView)
     */
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        timer.removeCallbacks(cbloadmore);
    }

    public final void setEmptyViewPolicy(final int policy) {
        mEmptyViewPolicy = policy;
    }

    public final void setEmptyViewOnInitPolicy(final int policy) {
        mEmptyViewOnInitPolicy = policy;
    }

    public final int getEmptyViewPolicy() {
        return mEmptyViewPolicy;
    }

    public final int getEmptyViewInitPolicy() {
        return mEmptyViewOnInitPolicy;
    }


    /**
     * the basic view holder creation
     *
     * @param parent   coming from the bottom api
     * @param viewType coming the bottom api as well
     * @return expected a typed view holder
     */
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPES.FOOTER) {
            VH viewHolder = newFooterHolder(footView);
            /**
             * this is only for the first time rendering of the adapter
             */
            footViewItemView = viewHolder.itemView;
            if (getAdapterItemCount() == 0) {
                removeFootView();
            }
            if (state.isFootViewVisible() && getAdapterItemCount() > 0) {
                recoverFootView();
            }
            return viewHolder;
        } else if (viewType == VIEW_TYPES.HEADER) {
            return newHeaderHolder(customHeaderView);
        } else if (viewType == VIEW_TYPES.NORMAL) {
            return onCreateNormalViewHolder(parent);
        }else{
            return onCreateCustomViewHolder(parent, viewType);
        }
    }





    /**
     * requirement: FOOTER, HEADER. it does not bind and need to do that in the header binding
     *
     * @param view with no binding view of nothing
     * @return v
     */
    public abstract VH newFooterHolder(View view);


    public abstract VH newHeaderHolder(View view);

    /**
     * for all NORMAL type holder
     *
     * @param parent view group parent
     * @return vh
     */
    public abstract VH onCreateNormalViewHolder(ViewGroup parent);

    public  VH onCreateCustomViewHolder(ViewGroup parent, int type) {
        return null;
    };


    @Override
    public int getItemViewType(int position) {
            int last_item = getItemCount() - 1;
        if (position == last_item && hasFootView()&&state.isFootViewVisible()) {
                return VIEW_TYPES.FOOTER;
            } else if (position == 0 && hasHeaderView()) {
                return VIEW_TYPES.HEADER;
            } else if (getCustomViewType(position)!=VIEW_TYPES.NORMAL) {
                return getCustomViewType(position);
            } else {
                return VIEW_TYPES.NORMAL;
            }
    }

    protected int getCustomViewType(final int pos) {
        return VIEW_TYPES.NORMAL;
    }


    /**
     * retrieve the amount of the total items in the urv for display that will be including all data items as well as the decorative items
     *
     * @return the int
     */
    @Override
    public int getItemCount() {
        return getAdapterItemCount() + getAdditionalItems();
    }

    public int getAdditionalItems() {
        int offset = 0;
        if (hasHeaderView()) offset++;
        if (hasFootView()&&state.isFootViewVisible()) offset++;
        return offset;
    }

    /**
     * Returns the number of items in the adapter bound to the parent RecyclerView.
     *
     * @return The number of data items in the bound adapter
     */
    public abstract int getAdapterItemCount();


    public final void toggleSelection(int pos) {
        notifyItemChanged(pos);
    }


    public final void clearSelection(int pos) {
        notifyItemChanged(pos);
    }

    public final void setSelected(int pos) {
        notifyItemChanged(pos);
    }

    /**
     * Swap the item of list
     *
     * @param list data list
     * @param from position from
     * @param to   position to
     */
    public void swapPositions(List<?> list, int from, int to) {
        if (hasHeaderView()) {
            from--;
            to--;
        }
        if (state.isFootViewVisible() && to == getItemCount() - 1) return;
        if (hasHeaderView() && to == 0) return;
        if (hasHeaderView() && from == 0) return;
        if (state.isFootViewVisible() && from == getItemCount() - 1) return;
        Collections.swap(list, from, to);
    }


    /**
     * Insert a item to the list of the adapter
     *
     * @param list     data list
     * @param object   object T
     * @param position position
     * @param <T>      in T
     */
    public final <T> void insertInternal(List<T> list, T object, final int position) {
        list.add(position, object);
        int g = position;
        if (hasHeaderView()) g++;
        notifyItemInserted(g);
    }


    public final <T> void insertFirstInternal(List<T> list, T item) {
        insertInternal(list, item, 0);
    }

    public final <T> void insertLastInternal(List<T> list, T item) {
        insertInternal(list, item, getAdapterItemCount());
    }

    /**
     * insert the new item list after the whole list
     *
     * @param insert_data   new list
     * @param original_list original copy
     * @param <T>           the type
     */
    public final <T> void insertInternal(List<T> insert_data, List<T> original_list) {
        try {
            Iterator<T> id = insert_data.iterator();
            int g = getItemCount();
            //   if (hasHeaderView()) g--;
            if (state.isFootViewVisible()) g--;
            final int start = g;
            synchronized (mLock) {
                while (id.hasNext()) {
                    original_list.add(original_list.size(), id.next());
                }
            }
            if (insert_data.size() == 1) {
                notifyItemInserted(start);
            } else if (insert_data.size() > 1) {
                notifyItemRangeInserted(start, insert_data.size());
            }
            if (state.isFootViewVisible()) {
                recoverFootView();
            }
        } catch (Exception e) {
            String o = e.fillInStackTrace().getCause().getMessage().toString();
            Log.d("fillInStackTrace", o + " : ");
        }
    }

    /**
     * Remove a item of  the list of the adapter
     *
     * @param list     na
     * @param position na
     * @param <T>      na
     */
    public final <T> void removeInternal(List<T> list, int position) {
        if (hasHeaderView() && position == 0) return;
        if (state.isFootViewVisible() && position == getItemCount() - 1) return;
        if (list.size() > 0) {
            synchronized (mLock) {
                list.remove(hasHeaderView() ? position - 1 : position);
            }
            removeNotifyExternal(position);
            notifyItemRemoved(position);
        }
    }

    protected void removeNotifyExternal(final int pos) {

    }

    public final <T> void removeFirstInternal(List<T> list) {
        removeInternal(list, 0);
    }

    public final <T> void removeLastInternal(List<T> list) {
        removeInternal(list, getAdapterItemCount() - 1);
    }

    /**
     * Clear the list of the adapter
     *
     * @param list data list
     * @param <T>  na
     */
    public final <T> void clearInternal(List<T> list) {
        int data_size_before_remove = list.size();
        final int display_size_before_remove = getItemCount();
        synchronized (mLock) {
            list.clear();
        }
        notifyAfterRemoveAllData(data_size_before_remove, display_size_before_remove);
    }

    /**
     * @param data_size_before_remove    data size
     * @param display_size_before_remove display item size
     * @return TRUE for this is done and no more further processing
     */
    protected boolean detectDispatchFootViewDisplay(final int data_size_before_remove, final int display_size_before_remove) {
        if (data_size_before_remove == 0) {
            if (display_size_before_remove == 2) {

                if (mEmptyViewPolicy == UltimateRecyclerView.EMPTY_KEEP_HEADER_AND_LOARMORE) {

                } else if (mEmptyViewPolicy == UltimateRecyclerView.EMPTY_KEEP_HEADER) {
                    removeFootView();
                } else if (mEmptyViewPolicy == UltimateRecyclerView.EMPTY_CLEAR_ALL) {
                    removeFootView();
                }

            } else if (display_size_before_remove == 1) {

                if (mEmptyViewPolicy == UltimateRecyclerView.EMPTY_KEEP_HEADER_AND_LOARMORE) {

                } else if (mEmptyViewPolicy == UltimateRecyclerView.EMPTY_KEEP_HEADER) {
                    removeFootView();
                } else if (mEmptyViewPolicy == UltimateRecyclerView.EMPTY_CLEAR_ALL) {
                    removeFootView();
                }

                return true;

            } else if (display_size_before_remove == 0) {
                if (mEmptyViewPolicy == UltimateRecyclerView.EMPTY_KEEP_HEADER_AND_LOARMORE) {
                    notifyDataSetChanged();
                } else if (mEmptyViewPolicy == UltimateRecyclerView.EMPTY_KEEP_HEADER) {
                    notifyDataSetChanged();
                } else if (mEmptyViewPolicy == UltimateRecyclerView.EMPTY_SHOW_LOADMORE_ONLY) {
                    notifyDataSetChanged();
                }
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    protected void recoverFootView() {
        if (footViewItemView != null) {
            if (footViewItemView.getVisibility() != View.VISIBLE) {
                footViewItemView.setVisibility(View.VISIBLE);
            }
        }
    }

    protected void removeFootView() {
        if (footViewItemView != null) {
            if (footViewItemView.getVisibility() != View.GONE) {
                footViewItemView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * works on API v23
     * there is a high  chance to crash this
     *
     * @param data_size_before_remove    original size before removed
     * @param display_size_before_remove the counts for display items
     *                                   <code>
     *                                   http://stackoverflow.com/questions/30220771/recyclerview-inconsistency-detected-invalid-item-position</code>
     */

    protected void notifyAfterRemoveAllData(final int data_size_before_remove, final int display_size_before_remove) {
        try {
            final int notify_start_item = hasHeaderView() ? 1 : 0;
            final int notifiy_n_plus = hasHeaderView() ? data_size_before_remove + 1 : data_size_before_remove;
            if (detectDispatchFootViewDisplay(data_size_before_remove, display_size_before_remove))
                return;

            if (data_size_before_remove == 0) return;

            if (mEmptyViewPolicy == UltimateRecyclerView.EMPTY_KEEP_HEADER_AND_LOARMORE) {
                if (hasHeaderView())
                    notifyItemRangeChanged(notify_start_item, data_size_before_remove);
                else {
                    notifyDataSetChanged();
                }
            } else if (mEmptyViewPolicy == UltimateRecyclerView.EMPTY_KEEP_HEADER) {
                notifyItemRangeRemoved(notify_start_item, data_size_before_remove);
                removeFootView();
            } else if (mEmptyViewPolicy == UltimateRecyclerView.EMPTY_CLEAR_ALL) {
                notifyItemRangeRemoved(0, notifiy_n_plus);
                removeFootView();
            } else if (mEmptyViewPolicy == UltimateRecyclerView.EMPTY_SHOW_LOADMORE_ONLY) {
                notifyItemRangeRemoved(0, notifiy_n_plus);
                recoverFootView();
            } else {
                notifyItemRangeRemoved(0, notifiy_n_plus);
            }
        } catch (Exception e) {
            String o = e.fillInStackTrace().getCause().getMessage().toString();
            Log.d("fillInStackTrace", o + " : ");
        }
    }


    /**
     * remove all items
     *
     * @param list na
     * @param <T>  na
     */
    public final <T> void removeAllInternal(List<T> list) {
        clearInternal(list);
    }





    public static class VIEW_TYPES {
        public static final int NORMAL = 0;
        public static final int HEADER = 1;
        //this is the default loading footer
        public static final int FOOTER = 2;
    }

    protected enum AdapterAnimationType {
        AlphaIn,
        SlideInBottom,
        ScaleIn,
        SlideInLeft,
        SlideInRight,
    }

    /**
     * Animations when loading the adapter
     *
     * @param view the view
     * @param type the type of the animation
     * @return the animator in array
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected Animator[] getAdapterAnimations(View view, AdapterAnimationType type) {
        if (type == AdapterAnimationType.ScaleIn) {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", .5f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", .5f, 1f);
            return new ObjectAnimator[]{scaleX, scaleY};
        } else if (type == AdapterAnimationType.AlphaIn) {
            return new Animator[]{ObjectAnimator.ofFloat(view, "alpha", .5f, 1f)};
        } else if (type == AdapterAnimationType.SlideInBottom) {
            return new Animator[]{
                    ObjectAnimator.ofFloat(view, "translationY", view.getMeasuredHeight(), 0)
            };
        } else if (type == AdapterAnimationType.SlideInLeft) {
            return new Animator[]{
                    ObjectAnimator.ofFloat(view, "translationX", -view.getRootView().getWidth(), 0)
            };
        } else if (type == AdapterAnimationType.SlideInRight) {
            return new Animator[]{
                    ObjectAnimator.ofFloat(view, "translationX", view.getRootView().getWidth(), 0)
            };
        }
        return null;
    }


/*
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHelper.clear(holder.itemView);

    }
    */
public UltimateRecyclerView.State getState(){
    return  state;
}

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        if (hasHeaderView() && getItemViewType(position) == VIEW_TYPES.HEADER) return;
        if (state.isFootViewVisible() && getItemViewType(position) == VIEW_TYPES.FOOTER) return;
        notifyDataSetChanged();
    }
}
