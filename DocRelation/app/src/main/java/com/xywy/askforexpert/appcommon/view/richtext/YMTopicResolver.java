package com.xywy.askforexpert.appcommon.view.richtext;

import android.text.Spannable;
import android.util.SparseArray;
import android.widget.TextView;

import com.xywy.askforexpert.module.docotorcirclenew.utils.RichTextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 话题富文本解析器
 * Created by bailiangjin on 16/12/05
 */
public class YMTopicResolver implements Resolver {

    @Override
    public void resolve(final TextView textView, final Spannable spannable, SparseArray<Object> extra, final RichTexts.RichTextClickListener richTextClickListener) {
        Pattern textPattern = Pattern.compile(RichTextUtils.TOPIC_TEXT_REGEX);
        Matcher matcher = textPattern.matcher(spannable);
        while (matcher.find()) {
            //带id的标签
            String fullLabel = matcher.group();
            final RichTexts.TaggedInfo taggedInfo = new RichTexts.TaggedInfo(matcher.start(), matcher.end(), fullLabel);
            if (richTextClickListener != null) {
                RichTexts.TopicTextClickSpan topicTextClickSpan = new RichTexts.TopicTextClickSpan(richTextClickListener, taggedInfo.content);
                spannable.setSpan(topicTextClickSpan, taggedInfo.start, taggedInfo.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            textView.postInvalidate();
        }
    }

}
