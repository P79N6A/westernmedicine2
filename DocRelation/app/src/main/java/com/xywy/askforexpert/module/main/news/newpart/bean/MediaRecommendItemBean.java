package com.xywy.askforexpert.module.main.news.newpart.bean;

import com.xywy.askforexpert.model.news.MediaRecommendItem;

import java.util.List;

/**
 * Created by bailiangjin on 2017/1/3.
 */

public class MediaRecommendItemBean {
    private List<MediaRecommendItem> mediaRecommendItemList;

    public MediaRecommendItemBean(List<MediaRecommendItem> mediaRecommendItemList) {
        this.mediaRecommendItemList = mediaRecommendItemList;
    }

    public List<MediaRecommendItem> getMediaRecommendItemList() {
        return mediaRecommendItemList;
    }

    public void setMediaRecommendItemList(List<MediaRecommendItem> mediaRecommendItemList) {
        this.mediaRecommendItemList = mediaRecommendItemList;
    }
}
