package com.xywy.askforexpert.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ScrollView;

/**
 * 可设置悬浮按钮的ScrollView
 * <p>
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2015/11/30 9:23
 */
public class FloatButtonScrollView extends ScrollView {
    private static final String LOG_TAG = "FloatButtonScrollView";
    /**
     * 决定floatView是否可见的view
     */
    private View primaryView;

    /**
     * floatView
     */
    private View floatView;

    /**
     * 悬浮按钮进入动画
     */
    private Animation enterAnim;

    /**
     * 悬浮按钮退出动画
     */
    private Animation exitAnim;

    private View floatShare2;

    public FloatButtonScrollView(Context context) {
        super(context);
    }

    public FloatButtonScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatButtonScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setFloatShare2(View floatShare2) {
        this.floatShare2 = floatShare2;
    }

    public void setEnterAnim(Animation enterAnim) {
        this.enterAnim = enterAnim;
    }

    public void setExitAnim(Animation exitAnim) {
        this.exitAnim = exitAnim;
    }

    public void setPrimaryView(View primaryView) {
        this.primaryView = primaryView;
    }

    public void setFloatView(View floatView) {
        this.floatView = floatView;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        Rect scrollBounds = new Rect();
        this.getHitRect(scrollBounds);
        if (primaryView != null && floatView != null) {
            if (t < primaryView.getTop()) {
                // 当primaryView位于屏幕下方时不显示悬浮按钮
                if (floatView.getVisibility() == VISIBLE) {
                    if (exitAnim != null) {
                        floatView.startAnimation(exitAnim);
                    }
                    floatView.setVisibility(GONE);
                    if (floatShare2 != null) {
                        if (floatShare2.getVisibility() == INVISIBLE) {
                            floatShare2.setVisibility(GONE);
                        }
                    }
                }
            } else {
                // 当primaryView滑动到屏幕上方时根据primaryView的显示情况决定是否显示悬浮按钮
                if (primaryView.getLocalVisibleRect(scrollBounds)) {
                    // primaryView可见，设置floatView不可见
                    if (floatView.getVisibility() == VISIBLE) {
                        if (exitAnim != null) {
                            floatView.startAnimation(exitAnim);
                        }
                        floatView.setVisibility(GONE);
                        if (floatShare2 != null) {
                            if (floatShare2.getVisibility() == INVISIBLE) {
                                floatShare2.setVisibility(GONE);
                            }
                        }
                    }
                } else {
                    // primaryView不可见，设置floatView可见
                    if (floatView.getVisibility() == GONE) {
                        if (enterAnim != null) {
                            floatView.startAnimation(enterAnim);
                        }
                        floatView.setVisibility(VISIBLE);
                        if (floatShare2 != null) {
                            if (floatShare2.getVisibility() == GONE) {
                                floatShare2.setVisibility(INVISIBLE);
                            }
                        }
                    }
                }
            }
        }
    }
}
