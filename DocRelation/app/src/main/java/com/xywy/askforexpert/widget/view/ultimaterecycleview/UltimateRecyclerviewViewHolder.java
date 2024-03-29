package com.xywy.askforexpert.widget.view.ultimaterecycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;


/**
 * Created by MarshalChen on 15-6-2.
 * adding by Hesk 2016-2-29
 */
public class UltimateRecyclerviewViewHolder<T> extends RecyclerView.ViewHolder
         {

    private SparseArray<SparseArray<View>> mSparseSparseArrayView
            = new SparseArray<SparseArray<View>>();
    private T mObject;
    public int position = -1;

    public UltimateRecyclerviewViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * Method called when we need to update the view hold by this class.
     *
     * @param object the object subject of this update
     */
    public void onBindView(Object object) {
        mObject = (T) object;
        updateView(itemView.getContext(), mObject);
    }


    /**
     * Get the last object set to this viewholder
     *
     * @return  the type
     */
    public T getObject() {
        return mObject;
    }


    /**
     * Method called when we need to update the view hold by this class.
     *  @param context context of the root view
     * @param object  the object subject of this update
     */
    public void updateView(Context context, T object) {
    }


    /**
     * Called when a view created by the adapter has been recycled.
     */
    public void onViewRecycled() {
    }

    /**
     * Called when a view created by the adapter has been attached to a window.
     */
    public void onViewAttachedToWindow() {
    }

    /**
     * Called when a view created by the adapter has been detached from its window.
     */
    public void onViewDetachedFromWindow() {
    }

    /**
     * Determine if a click listener should be automatically added to the view of this view holder
     *
     * @return true you want to have this view clickable
     */
    public boolean isClickable() {
        return false;
    }

    /**
     * Determine if a long click listener should be automatically added to the view of this view holder
     *
     * @return true you want to have this view clickable
     */
    public boolean isLongClickable() {
        return false;
    }

    /**
     * Look for a child view with the given id.  If this view has the given
     * id, return this view.
     * The method is efficient, if you had already called it, it will be faster than a normal
     * "findViewById" thanks to a cache system
     * @param id   The id to search for.
     * @param <T>  the type of view
     * @return    The view that has the given id in the hierarchy or null
     */
    public <T extends View> T findViewByIdEfficient(int id) {
        return (T) findViewByIdEfficient(0, id);
    }

    /**
     * Look for a child view of the parent view id with the given id.  If this view has the given
     * id, return this view.
     * The method is efficient, if you had already called it, it will be faster than a normal
     * "findViewById" thanks to a cache system
     * @param parentId  the parent id
     * @param id   The id to search for.
     * @param <T>                    type of object
     * @return  The view that has the given id in the hierarchy or null
     */
    public <T extends View> T findViewByIdEfficient(int parentId, int id) {
        View viewRetrieve = retrieveFromCache(parentId, id);
        if (viewRetrieve == null) {
            viewRetrieve = findViewById(parentId, id);
            if (viewRetrieve != null) {
                storeView(parentId, id, viewRetrieve);
            }
        }
        return (T) viewRetrieve;
    }

    private void storeView(int parentId, int id, View viewRetrieve) {
        SparseArray<View> sparseArrayViewsParent = mSparseSparseArrayView.get(parentId);
        if (sparseArrayViewsParent == null) {
            sparseArrayViewsParent = new SparseArray<View>();
            mSparseSparseArrayView.put(parentId, sparseArrayViewsParent);
        }
        sparseArrayViewsParent.put(id, viewRetrieve);
    }

    private View findViewById(int parentId, int id) {
        if (parentId == 0) {
            return itemView.findViewById(id);
        } else {
            View parent = findViewByIdEfficient(parentId);
            if (parent != null) {
                return parent.findViewById(id);
            } else {
                return null;
            }
        }
    }

    private View retrieveFromCache(int parentId, int id) {
        SparseArray<View> sparseArrayViewsParent = mSparseSparseArrayView.get(parentId);
        if (sparseArrayViewsParent != null) {
            View viewRetrieve = sparseArrayViewsParent.get(id);
            if (viewRetrieve == null) {
                // dead reference
                sparseArrayViewsParent.remove(id);
            } else {
                return viewRetrieve;
            }
        }
        if (parentId == 0) {
            return retrieveFromCache(id);
        } else {
            return null;
        }
    }

    private View retrieveFromCache(int id) {
        for (int i = 0; i < mSparseSparseArrayView.size(); i++) {
            int parentId = mSparseSparseArrayView.keyAt(i);
            if (parentId != 0) {
                View viewRetrieve = retrieveFromCache(parentId, id);
                if (viewRetrieve != null) {
                    return viewRetrieve;
                }
            }
        }
        return null;
    }

}