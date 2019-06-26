package com.xywy.askforexpert.module.docotorcirclenew.interfaze;

import android.app.Activity;

import com.xywy.askforexpert.module.docotorcirclenew.requestbean.TopicParamBean;

/**
 *
 * 话题Cell点击监听接口
 * Created by bailiangjin on 2016/11/3.
 */
public interface TopicClickListener {

    /**
     * 话题点击事件
     * @param activity
     * @param topicParamBean
     */
    void onTopicCLick(Activity activity, TopicParamBean topicParamBean);

    /**
     * 更多话题点击事件
     */
    void onMoreTopicClick(Activity activity);

}
