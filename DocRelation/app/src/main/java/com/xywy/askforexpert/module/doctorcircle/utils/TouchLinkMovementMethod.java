package com.xywy.askforexpert.module.doctorcircle.utils;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.widget.TextView;

import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.widget.module.topics.MyClickSpan;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/5/13 11:15
 */
public class TouchLinkMovementMethod extends LinkMovementMethod {
    private static final String TAG = "TouchLinkMovementMethod";

    private MyClickSpan myClickSpan;

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            myClickSpan = getPressedClickSpan(widget, buffer, event);
            if (myClickSpan != null) {
                myClickSpan.setPressed(true);
                DLog.d(TAG, "link start = " + buffer.getSpanStart(myClickSpan) + ", end = " + buffer.getSpanEnd(myClickSpan));
                Selection.setSelection(buffer, buffer.getSpanStart(myClickSpan),
                        buffer.getSpanEnd(myClickSpan));
            }
        } else if (action == MotionEvent.ACTION_MOVE) {
            MyClickSpan pressedClickSpan = getPressedClickSpan(widget, buffer, event);
            if (pressedClickSpan != null && myClickSpan != null && pressedClickSpan != myClickSpan) {
                myClickSpan.setPressed(false);
                myClickSpan = null;
                Selection.removeSelection(buffer);
            }
        } else {
            if (myClickSpan != null) {
                myClickSpan.setPressed(false);
                super.onTouchEvent(widget, buffer, event);
            }
            myClickSpan = null;
            Selection.removeSelection(buffer);
        }

        return true;
    }

    private MyClickSpan getPressedClickSpan(TextView textView, Spannable spannable, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        x -= textView.getTotalPaddingLeft();
        y -= textView.getTotalPaddingTop();

        x += textView.getScrollX();
        y += textView.getScrollY();

        Layout layout = textView.getLayout();
        int line = layout.getLineForVertical(y);
        int offset = layout.getOffsetForHorizontal(line, x);

        MyClickSpan[] link = spannable.getSpans(offset, offset, MyClickSpan.class);
        MyClickSpan pressedClickSpan = null;
        if (link.length > 0) {
            pressedClickSpan = link[0];
        }

        return pressedClickSpan;
    }
}
