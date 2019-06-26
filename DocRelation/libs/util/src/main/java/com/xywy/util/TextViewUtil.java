package com.xywy.util;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/23 13:52
 */

public class TextViewUtil {
    public static void setTextViewLink(TextView tvContact, String s, int start, int end, ClickableSpan what) {
        SpannableStringBuilder builder = new SpannableStringBuilder(s);
//ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.parseColor("#999999"));
        ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.parseColor("#0dc3ce"));
        builder.setSpan(blackSpan, 0, start, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setSpan(blueSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(blackSpan, end, s.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        builder.setSpan(what, start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvContact.setMovementMethod(LinkMovementMethod.getInstance());
        tvContact.setText(builder);
    }
}
