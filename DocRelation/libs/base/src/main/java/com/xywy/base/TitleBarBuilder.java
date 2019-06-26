package com.xywy.base;

import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xywy.util.DensityUtil;

/**
 * 标题栏 构建类
 * @author bailiangjin
 */
public class TitleBarBuilder {

//    private static final int defaultBgResId = R.drawable.base_toolbar_bg_no_alpha;
    private static final int defaultBackIconResId = R.drawable.base_back_btn_selector_new;

    Toolbar toolbar;
    TextView tv_title,tv_right;

    public TitleBarBuilder(final Activity activity, Toolbar toolbar) {
        this.toolbar = toolbar;
        tv_right = (TextView) toolbar.findViewById(R.id.tv_right);
        tv_title = (TextView) toolbar.findViewById(R.id.tv_title_toolbar);
        tv_title.setTextColor(activity.getResources().getColor(R.color.color_333));
//        toolbar.setBackgroundResource(defaultBgResId);

        //默认 返回键按钮
        toolbar.setNavigationIcon(defaultBackIconResId);
        //默认点击事件 finish Activity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }


    public Toolbar build() {
        return toolbar;
    }


    public TitleBarBuilder setTitleText(String title) {
        tv_title.setText(title);
        return this;
    }





    /**
     * 设置 标题栏北京
     * @param backGroundResId
     * @return
     */
    public TitleBarBuilder setBackGround(int backGroundResId) {
        toolbar.setBackgroundResource(backGroundResId);
        return this;
    }

    /**
     * 设置标题文字宽度
     *
     * @param width dp
     */
    public void setTitleTvWidth(int width) {
        ViewGroup.LayoutParams layoutParams = tv_title.getLayoutParams();
        layoutParams.width = DensityUtil.dip2px(width);
        tv_title.setLayoutParams(layoutParams);
        tv_title.invalidate();
    }





    /**
     * 自定义返回键
     *
     * @param listener
     * @return
     */
    public TitleBarBuilder setBackIconClickEvent(final View.OnClickListener listener) {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
        return this;
    }
    public TitleBarBuilder setRignt(String text,final View.OnClickListener listener) {
        tv_right.setText(text);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setOnClickListener(listener);
        return this;
    }

    public TitleBarBuilder setRight(String text,int resId,final View.OnClickListener listener) {
        tv_right.setText(text);
        tv_right.setBackgroundResource(resId);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setOnClickListener(listener);
        return this;
    }
    /**
     * 自定义返回键
     *
     * @param description
     * @param iconResId
     * @param listener
     * @return
     */
    public TitleBarBuilder setBackIcon(String description, int iconResId, final View.OnClickListener listener) {
        toolbar.setNavigationIcon(iconResId);
        toolbar.setNavigationContentDescription(description);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
        return this;
    }

    /**
     * 隐藏返回按钮
     *
     * @return
     */
    public TitleBarBuilder hideBackIcon() {
        toolbar.setNavigationIcon(null);
        return this;
    }

}
