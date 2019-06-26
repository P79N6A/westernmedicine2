package com.xywy.askforexpert.widget.view;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.PaintDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;

/**
 * 选择器 pop
 *
 * @author Stone
 */
public class SelectBasePopupWindow extends PopupWindow {

    //列表弹窗的间隔
    protected final float LIST_PADDING = 16;
    //坐标的位置（x、y）
    private final int[] mLocation = new int[2];
    private Context mContext;
    //实例化一个矩形
    private Rect mRect = new Rect();

    private boolean isStyle = true;// 从上往下滑的scaleAnimation 默认为true

    // private int colorRes;
    // private final Context context;

    public SelectBasePopupWindow(Activity context) {
        super(context);
        // this.context = context;
    }

    public SelectBasePopupWindow(boolean isStyle, Activity context) {
        super(context);
        this.isStyle = isStyle;
        // this.context = context;
    }

    public SelectBasePopupWindow(boolean isStyle, int colorRes, Activity context) {
        super(context);
        this.isStyle = isStyle;
        // this.colorRes = colorRes;
        // this.context = context;
    }

    public SelectBasePopupWindow init(View view) {
        this.setContentView(view);
        this.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        if (isStyle) {
            this.setAnimationStyle(R.style.PopupDownAnimation1);
        } else {
            // this.setAnimationStyle(R.style.PopupDownAnimation2);
//            this.setAnimationStyle(R.style.PopupDownAnimation0);
        }
        // if (colorRes != 0) {
        // this.setBackgroundDrawable(new ColorDrawable(context.getResources()
        // .getColor(colorRes)));
        // } else {
        // this.setBackgroundDrawable(new ColorDrawable(context.getResources()
        // .getColor(R.color.transparent)));
        // }
        setBackgroundDrawable(new PaintDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);
        setFocusable(true);
        setTouchable(true);
        return this;
    }

    /**
     * 显示弹窗列表界面
     */
    public void show(View view) {
        //获得点击屏幕的位置坐标
        view.getLocationOnScreen(mLocation);

        //设置矩形的大小
        mRect.set(mLocation[0], mLocation[1], mLocation[0] + view.getWidth(), mLocation[1] + view.getHeight());

        //显示弹窗的位置
        int popupGravity = Gravity.RIGHT | Gravity.TOP;
        showAtLocation(view, popupGravity, DensityUtils.dp2px(LIST_PADDING),
                mRect.bottom + DensityUtils.dp2px(LIST_PADDING) / 3);
    }


}
