package com.xywy.askforexpert.module.docotorcirclenew.utils;


import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.activity.WebViewActivity;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.view.richtext.RichTextWrapper;
import com.xywy.askforexpert.appcommon.view.richtext.RichTexts;
import com.xywy.askforexpert.appcommon.view.richtext.YMTopicResolver;
import com.xywy.askforexpert.appcommon.view.richtext.YMUrlResolver;
import com.xywy.askforexpert.module.docotorcirclenew.activity.NewTopicDetailActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by bailiangjin on 2016/12/5.
 */

public class RichTextUtils {

    /**
     * 服务器返回的带有id的话题整体匹配
     */
    public static final String TOPIC_FULL_REGEX = "#([^#]+?)\\$([0-9]+)\\$ #";

    /**
     * 话题id匹配
     */
    public static final String TOPIC_ID_REGEX = "\\$([0-9]+)\\$\\s";

    /**
     * 去掉话题id的话题标签 正则格式
     */
    public static final String TOPIC_TEXT_REGEX = "#([^#]+?)#";




    public static int TOPIC_COLOR = AppUtils.getAppContext().getResources().getColor(R.color.c_00c8aa);


    /**
     * 解析 医圈内容详情文本 匹配话题标签和 url
     * @param activity
     * @param textView
     * @param orgContentStr
     */
    public static String parseCircleMsgContent(Activity activity, TextView textView, String orgContentStr) {

        Map<String, String> topicMap = RichTextUtils.getTopicMap(orgContentStr);
        String textNoId="";
        if (!TextUtils.isEmpty(orgContentStr)) {
            //替换 双引号
            orgContentStr = orgContentStr.replace("&quot;", "\"");

            textView.setVisibility(View.VISIBLE);
            RichTextWrapper richTextWrapper = getCircleMsgTextWrapper(activity,textView, topicMap);
             textNoId = orgContentStr.replaceAll(RichTextUtils.TOPIC_ID_REGEX, "");
            richTextWrapper.setText(textNoId);
        } else {
            textView.setVisibility(View.GONE);
        }
        return textNoId;
    }

    /**
     * 获取 医圈内容文字 富文本包装器
     * @param activity
     * @param textView
     * @param topicMap
     * @return
     */
    private  static RichTextWrapper getCircleMsgTextWrapper(final Activity activity, TextView textView, final Map<String, String> topicMap) {
        RichTextWrapper richTextWrapper = new RichTextWrapper(textView);

        if (null == topicMap || topicMap.isEmpty()) {
            //无话题文本 不需要添加话题解析器

        } else {
            //有话题内容 需解析话题

            richTextWrapper.addResolver(YMTopicResolver.class);
            richTextWrapper.setOnRichTextListener(YMTopicResolver.class, new RichTexts.RichTextClickListener() {
                @Override
                public void onRichTextClick(TextView v, String textTopic) {
                    if (topicMap.containsKey(textTopic)) {
                        String topicId = topicMap.get(textTopic);
                        LogUtils.d("话题id:" + topicId);
                        //ToastUtils.shortToast("话题id:" + topicId);
                        //跳转到话题详情页
                        NewTopicDetailActivity.startActivity(activity, Integer.valueOf(topicId));
                    }

                }
            });

        }

        richTextWrapper.addResolver(YMUrlResolver.class);
        richTextWrapper.setOnRichTextListener(YMUrlResolver.class, new RichTexts.RichTextClickListener() {
            @Override
            public void onRichTextClick(TextView v, String webUrl) {
                LogUtils.d("url:" + webUrl);
                //ToastUtils.shortToast(content);
                //Intent intent = new Intent(Intent.ACTION_VIEW);
                //intent.setData(Uri.parse(url));
                //activity.startActivity(intent);

                WebViewActivity.start(activity,null,webUrl);
            }
        });
        return richTextWrapper;
    }


    /**
     *
     * @param content 匹配的 文字
     * @return map key 为显示的带#号的话题标签 value 为话题id
     */
    public static Map<String, String> getTopicMap(String content) {

        Map<String, String> topicsMap = new HashMap<String,String>();

        Pattern fullPattern = Pattern.compile(TOPIC_FULL_REGEX);
        Matcher fullMatcher = fullPattern.matcher(content);

        while (fullMatcher.find()) {
            //带id的标签
            String fullLabel = fullMatcher.group();
            // 设置正则匹配所有话题的id，并取出保存
            Pattern idPattern = Pattern.compile(TOPIC_ID_REGEX);
            Matcher idMatcher = idPattern.matcher(fullLabel);
            while (idMatcher.find()) {
                String idStr = idMatcher.group();
                //去掉id的文本标签
                String textLabel = fullLabel.replaceAll(TOPIC_ID_REGEX, "");
                //话题Id
                String topicId = idStr.trim().replaceAll("\\$", "");
                topicsMap.put(textLabel, topicId);
                fullLabel = fullLabel.replace(fullLabel, textLabel);
            }
        }
        return topicsMap;

    }
}
