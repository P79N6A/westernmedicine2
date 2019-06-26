package com.xywy.askforexpert.widget.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * @author xhl
 */
public class SwipeListViewRight extends ListView {

    private final int mDuration = 100;
    private final int mDurationStep = 10;
    private Boolean mIsHorizontal;
    private View mPreItemView;
    private View mCurrentItemView;
    private float mFirstX;
    private float mFirstY;
    private int mRightViewWidth = 200;
    private boolean mIsShown;
    private boolean mIsFooterCanSwipe = false;
    private boolean mIsHeaderCanSwipe = false;

    public SwipeListViewRight(Context context) {
        super(context);
//		mRightViewWidth = (YMApplication.SCR_W - 2 * (int) dip2px(context, 10)) * 386 / 541 / 3;
    }

    public SwipeListViewRight(Context context, AttributeSet attrs) {
        super(context, attrs);
//		mRightViewWidth = (YMApplication.SCR_W - 2 * (int) dip2px(context, 10)) * 386 / 541 / 3;
    }

    public SwipeListViewRight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//		mRightViewWidth = (YMApplication.SCR_W - 2 * (int) dip2px(context, 10)) * 386 / 541 / 3;
    }

    /**
     * dip转px
     */
    public static float dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (dipValue * scale + 0.5f);
    }

    /**
     * 处理ListView的焦点拦截事件
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float lastX = ev.getX();
        float lastY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsHorizontal = null;
                mFirstX = lastX;
                mFirstY = lastY;
                int motionPosition = pointToPosition((int) mFirstX, (int) mFirstY);

                if (motionPosition >= 0) {
                    View currentItemView = getChildAt(motionPosition
                            - getFirstVisiblePosition());
                    mPreItemView = mCurrentItemView;
                    mCurrentItemView = currentItemView;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = lastX - mFirstX;
                float dy = lastY - mFirstY;

                if (Math.abs(dx) >= 5 && Math.abs(dy) >= 5) {
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mIsShown
                        && (mPreItemView != mCurrentItemView || isHitCurItemLeft(lastX))
                        && mPreItemView != null) {
                    /**
                     * 情况一： 一个Item的右边布局已经显示， 这时候点击任意一个item, 那么那个右边布局显示的item隐藏其右边布局
                     */
                    hiddenRight(mPreItemView);

                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    private boolean isHitCurItemLeft(float x) {
        return x < getWidth() - mRightViewWidth;
    }

    /**
     * 判断滑动方向
     *
     * @param dx
     * @param dy
     * @return true是横向滑动的，可以滑动；false不是横向滑动的，不可以滑动；
     */
    private boolean judgeScrollDirection(float dx, float dy) {
        boolean canJudge = true;

        if (Math.abs(dx) > 30 && Math.abs(dx) > 2 * Math.abs(dy)) {
            mIsHorizontal = true;
        } else if (Math.abs(dy) > 30 && Math.abs(dy) > 2 * Math.abs(dx)) {
            mIsHorizontal = false;
        } else {
            canJudge = false;
        }

        return canJudge;
    }

    /**
     * 判断底部view是否可以滑动
     *
     * @param posX
     * @param posY
     * @return
     */
    private boolean judgeFooterView(float posX, float posY) {
        if (mIsFooterCanSwipe) {
            return true;
        }
        int selectPos = pointToPosition((int) posX, (int) posY);
        return selectPos < (getCount() - getFooterViewsCount());
    }

    /**
     * 判断顶部view是否可以滑动
     *
     * @param posX
     * @param posY
     * @return
     */
    private boolean judgeHeaderView(float posX, float posY) {
        if (mIsHeaderCanSwipe) {
            return true;
        }
        int selectPos = pointToPosition((int) posX, (int) posY);
        return !(selectPos >= 0 && selectPos < getHeaderViewsCount());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        float lastX = ev.getX();
        float lastY = ev.getY();

        if (!judgeFooterView(mFirstX, mFirstY)
                || !judgeHeaderView(mFirstX, mFirstY)) {
            return super.onTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = lastX - mFirstX;
                float dy = lastY - mFirstY;

                if (mIsHorizontal == null) {
                    if (!judgeScrollDirection(dx, dy)) {
                        break;
                    }
                }
                if (null != mPreItemView) {
                    if (mIsHorizontal) {
                        if (mIsShown && mPreItemView != mCurrentItemView) {
                            /**
                             * 情况二： 一个Item的右边布局已经显示，
                             * 这时候左右滑动另外一个item,那个右边布局显示的item隐藏其右边布局
                             * 向左滑动只触发该情况，向右滑动还会触发情况五
                             */
                            hiddenRight(mPreItemView);
                        }

                        if (mIsShown && mPreItemView == mCurrentItemView) {
                            dx = dx - mRightViewWidth;
                        }

                        if (dx < 0 && dx > -mRightViewWidth
                                && null != mCurrentItemView) {
                            mCurrentItemView.scrollTo((int) (-dx), 0);
                        }

                        return true;
                    } else {
                        if (mIsShown) {
                            /**
                             * 情况三： 一个Item的右边布局已经显示，
                             * 这时候上下滚动ListView,那么那个右边布局显示的item隐藏其右边布局
                             */
                            hiddenRight(mPreItemView);
                        }
                    }
                }

                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                clearPressedState();
                if (mIsShown && null != mPreItemView) {
                    /**
                     * 情况四： 一个Item的右边布局已经显示， 这时候左右滑动当前一个item,那个右边布局显示的item隐藏其右边布局
                     */
                    hiddenRight(mPreItemView);
                }

                if (mIsHorizontal != null && mIsHorizontal) {
                    if (null != mCurrentItemView) {
                        if (mFirstX - lastX > mRightViewWidth / 2) {
                            showRight(mCurrentItemView);
                        } else {
                            /**
                             * 情况五： 向右滑动一个item,且滑动的距离超过了右边View的宽度的一半，隐藏之。
                             */
                            hiddenRight(mCurrentItemView);
                        }
                    }

                    return true;
                }

                break;
        }
        clearPressedState();
        return super.onTouchEvent(ev);
    }

    private void clearPressedState() {
        if (null != mCurrentItemView) {
            mCurrentItemView.setPressed(false);
            mCurrentItemView.setFocusableInTouchMode(false);
            mCurrentItemView.setSelected(false);
            setPressed(false);
            refreshDrawableState();
        }
    }

    /**
     * 显示View对象右侧的删除的View对象
     *
     * @param view ：ListView当前显示的对象
     */
    private void showRight(View view) {
        Message msg = new MoveHandler().obtainMessage();
        msg.obj = view;
        msg.arg1 = view.getScrollX();
        msg.arg2 = mRightViewWidth;
        msg.sendToTarget();

        mIsShown = true;
    }

    /**
     * 隐藏View对象右侧的删除的view对象
     *
     * @param view ：Listview当前显示的对象
     */
    private void hiddenRight(View view) {
        if (mCurrentItemView == null) {
            return;
        }
        Message msg = new MoveHandler().obtainMessage();//
        msg.obj = view;
        msg.arg1 = view.getScrollX();
        msg.arg2 = 0;

        msg.sendToTarget();
        mIsShown = false;
    }

    public int getRightViewWidth() {
        return mRightViewWidth;
    }

    public void setRightViewWidth(int mRightViewWidth) {
        this.mRightViewWidth = mRightViewWidth;
    }

    /**
     * 设置list的脚是否能够swipe
     *
     * @param canSwipe
     */
    public void setFooterViewCanSwipe(boolean canSwipe) {
        mIsFooterCanSwipe = canSwipe;
    }

    /**
     * 设置list的头是否能够swipe
     *
     * @param canSwipe
     */
    public void setHeaderViewCanSwipe(boolean canSwipe) {
        mIsHeaderCanSwipe = canSwipe;
    }

    /**
     * 设置 footer and header can swipe
     *
     * @param footer
     * @param header
     */
    public void setFooterAndHeaderCanSwipe(boolean footer, boolean header) {
        mIsHeaderCanSwipe = header;
        mIsFooterCanSwipe = footer;
    }

    /**
     * 隐藏ListView中所有的Item的右侧的view对象
     */
    public void closeOpenedItems() {
        int start = getFirstVisiblePosition();
        int end = getLastVisiblePosition();
        for (int i = start; i <= end; i++) {
            hiddenRight(getChildAt(i - start));
        }
    }

    /**
     * 右侧布局滑动动画处理
     */
    @SuppressLint("HandlerLeak")
    class MoveHandler extends Handler {
        int stepX = 0;

        int fromX;

        int toX;

        View view;

        private boolean mIsInAnimation = false;

        private void animatioOver() {
            clearPressedState();
            mIsInAnimation = false;
            stepX = 0;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (stepX == 0) {
                if (mIsInAnimation) {
                    return;
                }
                mIsInAnimation = true;
                view = (View) msg.obj;
                fromX = msg.arg1;// 滑动后的x的位置
                toX = msg.arg2;// 滑动后x的位置
                stepX = (int) ((toX - fromX) * mDurationStep * 1.0 / mDuration);
                if (stepX < 0 && stepX > -1) {
                    stepX = -1;
                } else if (stepX > 0 && stepX < 1) {
                    stepX = 1;
                }
                // view向左边滑动
                if (Math.abs(toX - fromX) < 10) {
                    view.scrollTo(toX, 0);
                    animatioOver();
                    return;
                }
            }

            fromX += stepX;
            boolean isLastStep = (stepX > 0 && fromX > toX)
                    || (stepX < 0 && fromX < toX);
            if (isLastStep) {
                fromX = toX;
            }

            view.scrollTo(fromX, 0);
            clearPressedState();
            invalidate();

            if (!isLastStep) {
                this.sendEmptyMessageDelayed(0, mDurationStep);
            } else {
                animatioOver();
            }
        }

    }
}
