package com.xywy.askforexpert.widget.homeWidget.specialview;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.widget.homeWidget.MagicHeaderUtils;
import com.xywy.askforexpert.widget.homeWidget.OuterScroller;

/**
 * Helper of special views in InnerScroller.
 *
 * @author Xavier-S
 * @date 2015.11.17 15:07
 */
public class InnerSpecialViewHelper {

    private static final int ORIGIN_AUTO_COMPLETION_COLOR = Color.TRANSPARENT;
    /*************
     * Content Auto Completion View
     *****************/

    public View mContentAutoCompletionView;
    View mInnerEmptyView;
    Context mContext;
    OuterScroller mOuterScroller;
    int mContentAutoCompletionColor = ORIGIN_AUTO_COMPLETION_COLOR;
    private View mCustomEmptyView;
    private int mCustomEmptyViewHeight = ViewGroup.LayoutParams.MATCH_PARENT;
    private int mCustomEmptyViewHeightOffset;
    private int mContentAutoCompletionViewOffset;

    public InnerSpecialViewHelper(Context context) {
        mContext = context;
    }

    public View getCustomEmptyView() {
        return mCustomEmptyView;
    }

    /********
     * Inner Empty View Customization
     *************/

    public void setCustomEmptyView(View customEmptyView) {
        mCustomEmptyView = customEmptyView;
        mInnerEmptyView = mCustomEmptyView;
    }

    public View getInnerEmptyView() {
        if (mCustomEmptyView != null) {
            return mCustomEmptyView;
        }
        return mInnerEmptyView;
    }

    public void setInnerEmptyView(View innerEmptyView) {
        mInnerEmptyView = innerEmptyView;
    }

    public View getInnerEmptyViewSafely() {
        if (mCustomEmptyView != null) {
            return mCustomEmptyView;
        }
        if (mInnerEmptyView != null) {
            return mInnerEmptyView;
        }
        mInnerEmptyView = new View(mContext);
        return mInnerEmptyView;
    }

    public int getInnerEmptyViewHeightSafely() {
        int result;
        switch (mCustomEmptyViewHeight) {
            case ViewGroup.LayoutParams.WRAP_CONTENT:
                result = getInnerEmptyViewSafely().getMeasuredHeight();
                break;
            case ViewGroup.LayoutParams.MATCH_PARENT:
                if (mOuterScroller != null) {
                    result = mOuterScroller.getContentAreaMaxVisibleHeight();
                } else {
                    result = MagicHeaderUtils.getScreenHeight(mContext);
                }
                break;
            default:
                result = mCustomEmptyViewHeight;
        }
        result += mCustomEmptyViewHeightOffset;

        // height result may not be larger than outers ContentAreaMaxVisibleHeight.
        // in case of listview position error after notifyDataSetChanged().
        result = Math.min(result, mOuterScroller.getContentAreaMaxVisibleHeight());

        return result;
    }

    public void setOuterScroller(OuterScroller outerScroller) {
        mOuterScroller = outerScroller;
    }

    public void setCustomEmptyViewHeight(int height, int offset) {
        mCustomEmptyViewHeight = height;
        mCustomEmptyViewHeightOffset = offset;
    }

    public View getContentAutoCompletionView() {
        return mContentAutoCompletionView;
    }

    public void setContentAutoCompletionView(View contentAutoCompletionView) {
        mContentAutoCompletionView = contentAutoCompletionView;
    }

    public View getContentAutoCompletionViewSafely() {
        if (mContentAutoCompletionView == null) {
            generateContentAutoCompletionView();
        }
        return mContentAutoCompletionView;
    }

    public void setContentAutoCompletionColor(int color) {
        getContentAutoCompletionViewSafely().setBackgroundColor(mContentAutoCompletionColor = color);
    }

    public View generateContentAutoCompletionView() {
        mContentAutoCompletionView = new View(mContext);
        mContentAutoCompletionView.setTag(R.id.id_for_auto_completion_content, "");
        if (mContentAutoCompletionColor != ORIGIN_AUTO_COMPLETION_COLOR) {
            mContentAutoCompletionView.setBackgroundColor(mContentAutoCompletionColor);
        }
        return mContentAutoCompletionView;
    }

    public int getContentAutoCompletionViewOffset() {
        return mContentAutoCompletionViewOffset;
    }

    public void setContentAutoCompletionViewOffset(int offset) {
        mContentAutoCompletionViewOffset = offset;
    }
}
