package com.xywy.askforexpert.appcommon.view.richtext;

import android.content.Context;
import android.text.Spannable;
import android.util.SparseArray;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xuerufeng on 16/1/25.
 */
public class YMUrlResolver implements Resolver {

    public static final String IMG_MATCH_REGULAR = "[http|https]+[://]+[0-9A-Za-z:/[-]_#[?][=][.][&]]*";
    public static Pattern PATTERN = Pattern.compile(IMG_MATCH_REGULAR);


    @Override
    public void resolve(final TextView textView, final Spannable sp, SparseArray<Object> extra, final RichTexts.RichTextClickListener listener) {
        Matcher matcher = PATTERN.matcher(sp);
        final Context context = textView.getContext();


        while(matcher.find()){
            String content = matcher.group();
            final RichTexts.TaggedInfo info = new RichTexts.TaggedInfo(matcher.start(),matcher.end(),content);
            if(listener!=null) {
                RichTexts.RichTextClickSpan span = new RichTexts.RichTextClickSpan(listener,info.content);
                sp.setSpan(span, info.start, info.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
//            RichTexts.StickerSpan testSpan = new RichTexts.StickerSpan(context, R.drawable.icon,100,100);
//            RichTexts.setImageSpan(sp, info, testSpan);
            textView.postInvalidate();
        }
    }

}
