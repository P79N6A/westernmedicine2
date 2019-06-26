package com.xywy.askforexpert.appcommon.utils;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

/**
 * Created by bailiangjin on 16/9/1.
 */
public class AnimationUtils {

    /**
     * 旋转动画
     *
     * @param fromDegrees    从多少度
     * @param toDegrees      到多少度
     * @param durationMillis 旋转时间
     * @return
     */
    public static Animation getRotateAnimation(float fromDegrees,
                                               float toDegrees, int durationMillis) {
        RotateAnimation rotate = new RotateAnimation(fromDegrees, toDegrees,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotate.setDuration(durationMillis);
        rotate.setFillAfter(true);

        return rotate;
    }
}
