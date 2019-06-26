package com.xywy.askforexpert.module.main.news.newpart.bean;

import java.util.List;

/**
 * Created by bailiangjin on 2016/12/28.
 */

public class MainTabListItemBean {

    public static final int TYPE_NEWS = 1;
    public static final int TYPE_NEWS_MULTI_PICTURE = 2;
    public static final int TYPE_MEDIA_ENTER = 3;
    public static final int TYPE_MEDIA_RECOMMEND = 4;

    private int type;

    private NewsItemBean newsItemBean;
    private MediaEnterItemBean mediaEnterItemBean;
    private MediaRecommendItemBean mediaRecommendItemBean;

    public MainTabListItemBean(NewsItemBean newsItemBean) {
        List<String> pics = newsItemBean.getCommonNewsItemBean().getImgs();
        if ("1".equals(newsItemBean.getCommonNewsItemBean().getModel())&& null != pics && !pics.isEmpty()) {
            this.type = TYPE_NEWS_MULTI_PICTURE;
        } else {
            this.type = TYPE_NEWS;
        }
        this.newsItemBean = newsItemBean;
    }

    public MainTabListItemBean(MediaEnterItemBean mediaEnterItemBean) {
        this.type = TYPE_MEDIA_ENTER;
        this.mediaEnterItemBean = mediaEnterItemBean;
    }

    public MainTabListItemBean(MediaRecommendItemBean mediaRecommendItemBean) {
        this.type = TYPE_MEDIA_RECOMMEND;
        this.mediaRecommendItemBean = mediaRecommendItemBean;
    }

    public NewsItemBean getNewsItemBean() {
        return newsItemBean;
    }

    public MediaEnterItemBean getMediaEnterItemBean() {
        return mediaEnterItemBean;
    }

    public MediaRecommendItemBean getMediaRecommendItemBean() {
        return mediaRecommendItemBean;
    }

    public int getType() {
        return type;
    }


}
