package com.xywy.livevideo.player;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xywy.livevideolib.R;

/**
 * Created by zhangzheng on 2017/2/23.
 */
public class CommonLoadingView extends RelativeLayout {

    private Context context;

    private ImageView ivLoading;

    public CommonLoadingView(Context context) {
        super(context);
        init(context);
    }

    public CommonLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommonLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.live_loading_view, this);
        ivLoading = (ImageView) findViewById(R.id.iv_loading);
        Animation animation = new RotateAnimation(0f, 360f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1500);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setInterpolator(new LinearInterpolator());
        ivLoading.startAnimation(animation);
    }


}
