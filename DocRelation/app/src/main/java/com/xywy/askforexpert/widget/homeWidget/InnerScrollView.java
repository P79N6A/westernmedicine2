package com.xywy.askforexpert.widget.homeWidget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.xywy.askforexpert.widget.homeWidget.layout.SizeSensitiveLinearLayout;
import com.xywy.askforexpert.widget.homeWidget.specialview.InnerSpecialViewHelper;

/**
 * （ScrollView in Fragment in ViewPager of MagicHeaderViewPager）
 *
 * @author Xavier-S
 * @date 2015.11.09 14:04
 */
public class InnerScrollView extends ScrollView implements InnerScroller {

    public static final String TAG = "szlc[InnerListView]";
    protected View mEmptyHeader;
    /********************
     * Outer Settings
     ******************/

    protected OuterScroller mOuterScroller;
    private final OnScrollStateChangedListener mOnScrollStateChangedListener
            = new OnScrollStateChangedListener() {
        @Override
        public void onScrollStart() {
        }

        @Override
        public void onStop() {
            if (mOuterScroller != null) {
                mOuterScroller.onInnerScrollerStop();
            }
        }
    };
    /************************************************************
     * ***     save and restore position related to mhvp     ****
     ************************************************************/

    // item's marginTop to magic header
    protected int ORIGIN_ITEM_MARGIN_TOP_2_HEADER = 0x10;
    protected int mItemMariginTop2Header = ORIGIN_ITEM_MARGIN_TOP_2_HEADER;
    protected int mLastHeaderVisibleHeight = 0;
    /**
     * Listener set by others
     **/
    OnScrollChangedListener mOnScrollChangedListener;
    /************************************************************
     * ********              View Lifecycle             ********
     ************************************************************/
    boolean mAttached = false;
    boolean mHasDetached = false;
    /**
     * (as its name)
     */
    SizeSensitiveLinearLayout mTheOnlyChild;
    View mContentView;
    int mContentHeight;
    private int mCustomHeaderCount;
    private int mIndex = -1;
    /************************************************************
     * *******         Interaction with Outer           ********
     ************************************************************/
    private boolean mBlockMeasure;
    private boolean mGettingScrollY = false;
    private boolean mRendered;
    /**********
     * Control scroll
     ****************/
    private boolean mHasAdjustedFirstScrollPosition = false;
    private int mScrollRange;
    /************************************************************
     * ********               Scroll Helper              ********
     ************************************************************/
    private ScrollStateHelper mScrollStateHelper;
    private View mReceiveView;
    /*********************************************************************
     * ***         Customize empty content view and its height        ****
     *********************************************************************/
    private InnerSpecialViewHelper mInnerSpecialViewHelper;

    public InnerScrollView(Context context) {
        super(context);
        initView();
    }

    public InnerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        initTheOnlyLinearLayout();
    }

    private void initEmptyHeader() {
        if (mEmptyHeader == null) {
            mEmptyHeader = new FrameLayout(getContext());
        }

        if (mEmptyHeader.getParent() != null) {
            ((ViewGroup) mEmptyHeader.getParent()).removeView(mEmptyHeader);
        }
        mTheOnlyChild.addView(mEmptyHeader, 0);
    }

    /**
     * （as its name）
     *
     * @param firstVisibleItem no use for ScrollView
     *                         {@hide}
     */
    @Override
    public final void recordScrollPosition(int firstVisibleItem) {
        if (!mAttached || mOuterScroller == null || mIndex != mOuterScroller.getCurrentInnerScrollerIndex()) {
            return;
        }
        mLastHeaderVisibleHeight = mOuterScroller.getHeaderVisibleHeight();

        int itemMariginTop = -getScrollY();
        mItemMariginTop2Header = itemMariginTop - mLastHeaderVisibleHeight;
    }

    /**
     * (as its name)
     * {@hide}
     */
    @Override
    public final void adjustEmptyHeaderHeight() {
        if (mEmptyHeader == null || mOuterScroller == null || mOuterScroller.getHeaderHeight() == 0) {
            return;
        }
        if (mEmptyHeader.getPaddingTop() != mOuterScroller.getHeaderHeight()) {
            post(new Runnable() {
                @Override
                public void run() {
                    mEmptyHeader.setPadding(0, mOuterScroller.getHeaderHeight(), 0, 0);
                    resetRocordedValues();
                }
            });
        }
    }

    /**
     * InnerScrollView has 2 states：
     * 1. EmptyHeader + content + auto completion,
     * 2. EmptyHeader + emptyContent + autoCompletion.
     * This method called on OnlyChild's height changed, to ensure in right state.
     */
    private void updateViewState() {

        checkEmptyContent();

        checkAutoCompletion();
    }

    private void resetRocordedValues() {
        mHasAdjustedFirstScrollPosition = false;
        mItemMariginTop2Header = ORIGIN_ITEM_MARGIN_TOP_2_HEADER;
        performScroll(mItemMariginTop2Header);
    }

    @Override
    public int getInnerScrollY() {
        return getScrollY();
    }

    @Override
    public void scrollToTop() {
        scrollTo(getScrollX(), 0);
    }

    @Override
    public void scrollToInnerTop() {
        int innerTopY = mOuterScroller.getHeaderHeight() - mOuterScroller.getHeaderVisibleHeight();
        scrollTo(getScrollX(), innerTopY);
    }

    @Override
    public void addHeaderView(View headerView) {
        if (headerView != null) {
            int insertIndex = getContentInsertIndex();
            mTheOnlyChild.addView(headerView, insertIndex);
            mCustomHeaderCount++;
        }
    }

    public boolean removeHeaderView(View headerView) {

        int index = mTheOnlyChild.indexOfChild(headerView);

        if (index == -1) {
            return false;
        }

        if (index <= getEmptyHeaderIndex() || (getContentIndex() > 0 && index >= getContentIndex())) {
            throw new IndexOutOfBoundsException("Sorry, the view parameter of removeView() is not in header!");
        }

        mTheOnlyChild.removeView(headerView);
        mCustomHeaderCount--;
        return true;
    }

    private int getContentInsertIndex() {
        return getEmptyHeaderIndex() + 1 + mCustomHeaderCount;
    }

    private int getEmptyHeaderIndex() {
        if (mEmptyHeader != null) {
            return mTheOnlyChild.indexOfChild(mEmptyHeader);
        }
        return -1;
    }

    private int getContentIndex() {
        if (mContentView != null) {
            return mTheOnlyChild.indexOfChild(mContentView);
        }
        return -1;
    }

    public void onRefresh(boolean isRefreshing) {
        if (mOuterScroller != null) {
            mOuterScroller.updateRefreshState(isRefreshing);
        }
    }

    @Override
    public OuterScroller getOuterScroller() {
        return mOuterScroller;
    }

    @Override
    public void register2Outer(OuterScroller outerScroller, int index) {
        if (outerScroller != null && (outerScroller != mOuterScroller || mIndex != index)) {
            mIndex = index;
            mOuterScroller = outerScroller;
            mOuterScroller.registerInnerScroller(index, this);
            getInnerViewHelper().setOuterScroller(mOuterScroller);
            adjustEmptyHeaderHeight();
        }
    }

    /**
     * Remove content view
     */
    public void clearContent() {
        if (mContentView != null && mTheOnlyChild.indexOfChild(mContentView) != -1) {
            mTheOnlyChild.removeView(mContentView);
            mContentView = null;
        }
    }

    /**
     * Remove content view
     */
    public void removeAllContentViews() {
        clearContent();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        onScroll(l, t, oldl, oldt);

        if (mOnScrollChangedListener != null) {
            mOnScrollChangedListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        mOnScrollChangedListener = listener;
    }

    public boolean isBlockMeasure() {
        return mBlockMeasure;
    }

    private void setBlockMeasure(boolean blockMeasure) {
        this.mBlockMeasure = blockMeasure;
    }

    public final void onScroll(int l, int t, int oldl, int oldt) {
        if (!mAttached || mBlockMeasure) {
            return;
        }
        if (mOuterScroller != null && mIndex == mOuterScroller.getCurrentInnerScrollerIndex()) {
            triggerOuterScroll();
            getScrollStateHelper().onVerticalScroll(t, oldt);
            recordScrollPosition(0);
        }
    }

    /**
     * （as its name）
     * <p>
     * {@hide}
     */
    @Override
    public final void triggerOuterScroll() {
        if (!mGettingScrollY && mOuterScroller != null) {
            mGettingScrollY = true;
            mOuterScroller.onInnerScroll(mIndex, getInnerScrollY());
            mGettingScrollY = false;
        }
    }


    /************************************************************
     ****                  Control content                 *****
     ************************************************************/

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        mAttached = true;
        if (mHasDetached) {
            onReAttached();
        }
    }

    private void onReAttached() {
        // Directly call won't go into effect, so call it in the next loop.
        post(new Runnable() {
            @Override
            public void run() {
                performScroll(mItemMariginTop2Header);
                if (isBlockMeasure()) {
                    setBlockMeasure(false);
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        mHasDetached = true;
        mAttached = false;
        super.onDetachedFromWindow();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (oldw == 0 && oldh == 0 && !mRendered) {
            mRendered = true;
            onRender();
        }
        updateScrollRange();
    }

    /**
     * Based on onSizeChanged() at the first time.
     */
    private void onRender() {
        initEmptyHeader();
        adjustEmptyHeaderHeight();
    }

    /**
     * Scroll only responding to Magic Header's change.
     * {@hide}
     */
    public final void performScroll(int itemMariginTop2Header) {
        if (!mAttached || mOuterScroller == null || mTheOnlyChild == null || mTheOnlyChild.getHeight() == 0) {
            return;
        }

        mLastHeaderVisibleHeight = mOuterScroller.getHeaderVisibleHeight();

        if (!mHasAdjustedFirstScrollPosition) {
            mHasAdjustedFirstScrollPosition = true;
            scrollToInnerTop();
            return;
        }

        if (itemMariginTop2Header > ORIGIN_ITEM_MARGIN_TOP_2_HEADER - 1 || -itemMariginTop2Header < mOuterScroller.getHeaderHeight()) {
            scrollToInnerTop();
        } else {
            scrollTo(getScrollX(), -(itemMariginTop2Header + mLastHeaderVisibleHeight));
        }
    }

    /**
     * Method performScroll() usually produce substantial scroll action, unable to be frequently called.
     * While this method syncScroll() has verification inside, will not directly produce scrolling,
     * and can be frequently called.
     * {@hide}
     */
    @Override
    public final void syncScroll() {
        if (!mAttached || mOuterScroller == null) {
            return;
        }
        if (mOuterScroller.getHeaderVisibleHeight() != mLastHeaderVisibleHeight) {
            performScroll(mItemMariginTop2Header);
        }
    }

    /**
     * please use {@link #setContentView(View)} instead
     */
    @Deprecated
    @Override
    public void addView(View child) {
        super.addView(child);
    }

    /**
     * please use {@link #setContentView(View)} instead
     */
    @Deprecated
    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
    }

    /**
     * please use {@link #setContentView(View)} instead
     */
    @Deprecated
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
    }

    /**
     * please use {@link #setContentView(View)} instead
     */
    @Deprecated
    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
    }

    /**
     * please use {@link #setContentView(View)} instead
     */
    @Deprecated
    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
    }

    /**
     * @param contentView
     * @param layoutParams
     */
    private void setContentView(View contentView, LinearLayout.LayoutParams layoutParams) {

        if (contentView == null) {
            clearContent();
            return;
        }

        if (contentView == mContentView) {
            return;
        }

        clearContent();

        mContentView = contentView;

        if (getInnerEmptyView() != null) {
            mTheOnlyChild.removeView(getInnerEmptyView());
            setInnerEmptyView(null);
        }

        mTheOnlyChild.addView(mContentView, getContentInsertIndex(), layoutParams);
    }

    private void updateScrollRange() {
        int scrollViewHeight = getHeight();
        int contentHeight = 0;
        if (getChildCount() > 0) {
            contentHeight = getChildAt(0).getHeight();
        }
        mScrollRange = Math.max(0, contentHeight - (scrollViewHeight - getPaddingTop() - getPaddingBottom()));
    }

    public View getContentView() {
        return mContentView;
    }

    public void setContentView(View contentView) {
        setContentView(contentView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void initTheOnlyLinearLayout() {
        mTheOnlyChild = new SizeSensitiveLinearLayout(getContext());
        mTheOnlyChild.setOrientation(LinearLayout.VERTICAL);
        mTheOnlyChild.setOnSizeChangedListener(new SizeSensitiveLinearLayout.SizeChangedListener() {

            @Override
            public void onSizeChanged(int w, int h, int oldw, int oldh) {
                onContentSizeChanged();
            }
        });
        super.addView(mTheOnlyChild);
    }

    private void onContentSizeChanged() {
        updateScrollRange();

        updateViewState();

        if (!mHasAdjustedFirstScrollPosition) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    performScroll(mItemMariginTop2Header);
                }
            }, 10);
        }
    }

    public ScrollStateHelper getScrollStateHelper() {
        if (mScrollStateHelper == null) {
            mScrollStateHelper = new ScrollStateHelper(getOnScrollStateChangedListenerWrapper(null));
        }
        return mScrollStateHelper;
    }

    public void setScrollStateHelper(ScrollStateHelper scrollStateHelper) {
        mScrollStateHelper = scrollStateHelper;
    }

    public void setOnScrollStateChangedListener(OnScrollStateChangedListener onScrollStateChangedListener) {
        setScrollStateHelper(
                new ScrollStateHelper(
                        getOnScrollStateChangedListenerWrapper(onScrollStateChangedListener)));
    }

    private OnScrollStateChangedListener getOnScrollStateChangedListenerWrapper(final OnScrollStateChangedListener onScrollStateChangedListener) {
        if (onScrollStateChangedListener == null) {
            return mOnScrollStateChangedListener;
        }
        return new OnScrollStateChangedListener() {
            @Override
            public void onScrollStart() {
                mOnScrollStateChangedListener.onScrollStart();
                onScrollStateChangedListener.onScrollStart();
            }

            @Override
            public void onStop() {
                mOnScrollStateChangedListener.onStop();
                onScrollStateChangedListener.onStop();
            }
        };
    }

    @Override
    public boolean isScrolling() {
        return getScrollStateHelper().mScrollState == ScrollState.SCROLL_STATE_IDLE;
    }

    /*************************************************************
     * ****               Touch event receiver              *****
     *************************************************************/
    public View getReceiveView() {
        return mReceiveView == null ? this : mReceiveView;
    }

    public void setReceiveView(View receiveView) {
        this.mReceiveView = receiveView;
    }

    /*************************************************************
     * ***********       Drawing optimization       *************
     *************************************************************/
    @Override
    public void draw(Canvas canvas) {
        final int restoreCount = canvas.save();
        if (mOuterScroller != null) {
            canvas.clipRect(0, getScrollY() + mOuterScroller.getHeaderVisibleHeight(), getWidth(), getScrollY() + getHeight());
        }
        super.draw(canvas);
        canvas.restoreToCount(restoreCount);
    }

    private InnerSpecialViewHelper getInnerViewHelper() {
        if (mInnerSpecialViewHelper == null) {
            mInnerSpecialViewHelper = new InnerSpecialViewHelper(getContext());
        }
        return mInnerSpecialViewHelper;
    }

    public void setCustomEmptyView(View emptyView) {
        getInnerViewHelper().setCustomEmptyView(emptyView);
    }

    /**
     * @param height You may pass wrap_content, match_parent or a figure. Defaults to match_parent.
     * @param offset Height offset, positive larger and negative smaller. Defaults to 0.
     * @return
     */
    public void setCustomEmptyViewHeight(int height, int offset) {
        getInnerViewHelper().setCustomEmptyViewHeight(height, offset);
    }

    public int getInnerEmptyViewHeightSafely() {
        return getInnerViewHelper().getInnerEmptyViewHeightSafely();
    }

    private View getInnerEmptyViewSafely() {
        return getInnerViewHelper().getInnerEmptyViewSafely();
    }

    private View getInnerEmptyView() {
        return getInnerViewHelper().getInnerEmptyView();
    }

    public void setInnerEmptyView(View emptyView) {
        getInnerViewHelper().setInnerEmptyView(emptyView);
    }

    /**
     * EmptyContent should change according to header. This is a relatively better way(ListView creates
     * EmptyView when items are render, so has no need to do this.
     */
    public void checkEmptyContent() {
        if (!mAttached || mTheOnlyChild == null || mContentView != null) {
            return;
        }

        final int itemHeight = getInnerEmptyViewHeightSafely();

        if (getInnerEmptyViewSafely().getParent() != mTheOnlyChild) {

            if (getInnerEmptyView().getParent() != null) {
                ((ViewGroup) getInnerEmptyView().getParent()).removeView(getInnerEmptyView());
            }

            mTheOnlyChild.addView(getInnerEmptyView(), getContentInsertIndex(), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight));
        } else {

            ViewGroup.LayoutParams lp = getInnerEmptyView().getLayoutParams();

            if (lp.height == itemHeight) {
                return;
            } else {
                lp.height = itemHeight;
            }
        }

        post(new Runnable() {
            @Override
            public void run() {
                if (getInnerEmptyView() != null) {
                    getInnerEmptyView().requestLayout();
                }
            }
        });
    }

    /*********************************************************************
     * **          Content Auto Completion View and its height         ***
     *********************************************************************/
    private void checkAutoCompletion() {
        if (mOuterScroller == null || !mAttached) {
            return;
        }

        int completionHeight = 0;

        if (getAutoCompletionView() != null) {
            completionHeight = getAutoCompletionView().getMeasuredHeight();
        }
        final int contentHeight = mTheOnlyChild.getMeasuredHeight() - mEmptyHeader.getMeasuredHeight() - completionHeight;

        if (contentHeight == mContentHeight) {
            return;
        }

        setBlockMeasure(true);
        final int tempY = getScrollY();

        mContentHeight = contentHeight;


        if (mContentHeight >= mOuterScroller.getContentAreaMaxVisibleHeight()) {
            if (getAutoCompletionView() == null) {
            } else {
                removeAutoCompletionView();
            }
        } else {
            int targetHeight = mOuterScroller.getContentAreaMaxVisibleHeight() - mContentHeight;

            final View autoCompletion = getContentAutoCompletionViewSafely();

            ViewGroup parent = (ViewGroup) autoCompletion.getParent();
            if (parent != mTheOnlyChild) {
                if (parent != null) {
                    ((ViewGroup) autoCompletion.getParent()).removeView(autoCompletion);
                }
                mTheOnlyChild.addView(autoCompletion, ViewGroup.LayoutParams.MATCH_PARENT, targetHeight);
            }

            ViewGroup.LayoutParams lp = autoCompletion.getLayoutParams();
            if (lp.height != targetHeight) {
                lp.height = targetHeight;
            }
        }

        post(new Runnable() {
            @Override
            public void run() {
                if (getAutoCompletionView() != null) {
                    getAutoCompletionView().requestLayout();
                } else {
                    mTheOnlyChild.requestLayout();
                }
                post(new Runnable() {
                    @Override
                    public void run() {
                        if (getScrollY() != tempY) {
                            performScroll(mItemMariginTop2Header);
                        }
                        setBlockMeasure(false);
                    }
                });
            }
        });
    }

    private void removeAutoCompletionView() {
        if (getAutoCompletionView().getParent() != null) {
            final ViewGroup parent = (ViewGroup) getAutoCompletionView().getParent();
            parent.removeView(getAutoCompletionView());
            post(new Runnable() {
                @Override
                public void run() {
                    parent.requestLayout();
                }
            });
            setAutoCompletionView(null);
        }
    }

    private View getAutoCompletionView() {
        return getInnerViewHelper().getContentAutoCompletionView();
    }

    private void setAutoCompletionView(View view) {
        getInnerViewHelper().setContentAutoCompletionView(view);
    }

    public void setContentAutoCompletionColor(int color) {
        getInnerViewHelper().setContentAutoCompletionColor(color);
    }

    private void generateContentAutoCompletionView() {
        getInnerViewHelper().generateContentAutoCompletionView();
    }

    private View getContentAutoCompletionViewSafely() {
        return getInnerViewHelper().getContentAutoCompletionViewSafely();
    }

    /************************************************************
     * ******         ScrollState like ListView          ********
     ************************************************************/
    enum ScrollState {
        SCROLL_STATE_IDLE, SCROLL_STATE_TOUCH_SCROLL
    }

    /*************************************************************
     * ***     called by self, so no need for decorator       ****
     *************************************************************/
    public interface OnScrollChangedListener {
        void onScrollChanged(ScrollView scrollView, int l, int t, int oldl, int oldt);
    }

    /**
     * （as its name）
     */
    public interface OnScrollStateChangedListener {
        /**
         * On Scroll start event. Touch_Scroll and Fling are regarded as the same.
         */
        void onScrollStart();

        /**
         * （as its name）
         */
        void onStop();
    }

    public class ScrollStateHelper {
        ScrollState mScrollState = ScrollState.SCROLL_STATE_IDLE;
        private OnScrollStateChangedListener mOnScrollStateChangedListener;

        ScrollStateHelper(OnScrollStateChangedListener onScrollStateChangedListener) {
            mOnScrollStateChangedListener = onScrollStateChangedListener;
        }

        private void onVerticalScroll(int t, int oldt) {
            if (t == 0 || t == mScrollRange) {
                setScrollState(ScrollState.SCROLL_STATE_IDLE);
            } else if (Math.abs(t - oldt) < 2) {
                setScrollState(ScrollState.SCROLL_STATE_IDLE);
            } else {
                setScrollState(ScrollState.SCROLL_STATE_TOUCH_SCROLL);
            }
        }

        public ScrollState getScrollState() {
            return mScrollState;
        }

        public void setScrollState(ScrollState scrollState) {
            if (scrollState != mScrollState) {
                mScrollState = scrollState;
                if (mOnScrollStateChangedListener != null) {
                    switch (mScrollState) {
                        case SCROLL_STATE_IDLE:
                            mOnScrollStateChangedListener.onStop();
                            break;
                        case SCROLL_STATE_TOUCH_SCROLL:
                            mOnScrollStateChangedListener.onScrollStart();
                            break;
                    }
                }
            } else {
                /* Even if they are equal, still report onStop, cuz when stopping, it
                   may move 1px several times.
                 */
                if (scrollState == ScrollState.SCROLL_STATE_IDLE) {
                    mOnScrollStateChangedListener.onStop();
                }
            }
        }
    }
}
