package com.xywy.askforexpert.appcommon.utils.tv;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Handler;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.TextAppearanceSpan;
import android.widget.TextView;

import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.html.LinkMovementMethodExt;
import com.xywy.askforexpert.appcommon.utils.html.MTagHandler;
import com.xywy.askforexpert.appcommon.utils.html.TvLoadImageAst;
import com.xywy.askforexpert.appcommon.utils.tv.bean.ColorText;
import com.xywy.askforexpert.appcommon.utils.tv.bean.StyleText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by bailiangjin on 16/4/28.
 */
public class TVUtils {

    /**
     * 给TextView设置 包含HTML样式的内容的 无点击事件
     *
     * @param textView
     * @param content
     */
    public static void setTvWithHtmlContent(TextView textView, String content, int loadFailedImgResId) {
        content = convertToMyFormat(content);
        textView.setText(getHtmlSpanned(textView, content, loadFailedImgResId));
    }

    /**
     * 给TextView设置 包含HTML样式的内容的 有图片点击事件
     *
     * @param textView
     * @param content
     * @param handler
     */
    public static void setTvWithHtmlContent(TextView textView, String content, int loadFailedImgResId, Handler handler) {
        content = convertToMyFormat(content);
        textView.setText(getHtmlSpanned(textView, content, loadFailedImgResId));
        textView.setMovementMethod(new LinkMovementMethodExt(handler, ImageSpan.class));
    }


    /**
     * 讲TextView样式转换为可解析事件的模式的的
     *
     * @param content
     * @return
     */
    public static String convertToMyFormat(String content) {
        content = content.replaceAll("<head>([\\s\\S]*)<\\/head>", "");
        if (content.contains("><p>")) {
            String regularExpression1 = "(<[^\\/]\\w><p>)";
            Pattern pat1 = Pattern.compile(regularExpression1);
            Matcher mat1 = pat1.matcher(content);
            if (mat1.find()) {
                for (int i = 0; i < mat1.groupCount(); i++) {
                    System.out.println(mat1.group(i));
                    String temp = mat1.group(i).replace("<p>", "");
                    content = content.replace(mat1.group(i), temp);
                    String tail = temp.replace("<", "</");
                    content = content.replace("</p>" + tail, tail);
                    System.out.println(content);
                }
            }

        }
        return content;
    }

    public static Spanned getHtmlSpanned(final TextView textView, String content, final int loadFailedImgResId) {
        return Html.fromHtml(content, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                LevelListDrawable drawable = new LevelListDrawable();
                drawable.addLevel(0, 0, null);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                new TvLoadImageAst().execute(source, drawable, textView, loadFailedImgResId);
                return drawable;
            }
        }, new MTagHandler());
    }

    /**
     * 给文字设置多种颜色
     *
     * @param textView
     * @param args
     */
    public static void setContentWithColor(TextView textView, ColorText... args) {

        String allContent = "";
        StringBuffer sb = new StringBuffer();
        for (ColorText colorText : args) {
            if (colorText.getLength() > 0) {
                sb = sb.append(colorText.getContent());
            }
        }
        allContent = sb.toString();
        if (TextUtils.isEmpty(allContent)) {
            return;
        }

        SpannableStringBuilder builder = new SpannableStringBuilder(allContent);
        int startPosition = 0;
        int endPosition = 0;
        for (ColorText colorText : args) {
            if (colorText.getLength() > 0) {
                int textColor=AppUtils.getAppContext().getResources().getColor(colorText.getColor());
                ForegroundColorSpan span = new ForegroundColorSpan(textColor);
                endPosition = startPosition + colorText.getLength();
                builder.setSpan(span, startPosition, endPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                startPosition = endPosition;
            }
        }


        textView.setText(builder);
    }

    /**
     * 给文字设置多种Style混排
     *
     * @param textView
     * @param args
     */
    public static void setContentWithStyle(TextView textView, StyleText... args) {

        String allContent = "";
        StringBuffer sb = new StringBuffer();
        for (StyleText styleText : args) {
            if (styleText.getLength() > 0) {
                sb = sb.append(styleText.getContent());
            }
        }
        allContent = sb.toString();
        if (TextUtils.isEmpty(allContent)) {
            return;
        }

        SpannableStringBuilder builder = new SpannableStringBuilder(allContent);
        int startPosition = 0;
        int endPosition = 0;
        for (StyleText styleText : args) {
            if (styleText.getLength() > 0) {
                TextAppearanceSpan span = new TextAppearanceSpan(YMApplication.getAppContext(), styleText.getStyle());
                endPosition = startPosition + styleText.getLength();
                builder.setSpan(span, startPosition, endPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                startPosition = endPosition;
            }
        }
        textView.setText(builder);
    }


}


