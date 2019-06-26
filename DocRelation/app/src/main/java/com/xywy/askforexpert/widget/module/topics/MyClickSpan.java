package com.xywy.askforexpert.widget.module.topics;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.module.docotorcirclenew.utils.RichTextUtils;

/**
 * Compiler: Android Studio
 * Project: TopicEditText
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/4/18 17:46
 */
public class MyClickSpan extends ClickableSpan {

    private static final String TAG = "MyClickSpan";

    private boolean isPressed;

    public MyClickSpan() {
    }

    public void setPressed(boolean pressed) {
        isPressed = pressed;
    }

    @Override
    public void onClick(View widget) {

    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);

        ds.setColor(RichTextUtils.TOPIC_COLOR);
        DLog.d(TAG, "isPressed = " + isPressed);
        ds.bgColor = isPressed ? RichTextUtils.TOPIC_COLOR : Color.TRANSPARENT;
        ds.setUnderlineText(false);
        ds.clearShadowLayer();
    }
}
