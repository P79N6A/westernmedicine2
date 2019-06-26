package com.xywy.askforexpert.model.media;

import java.util.List;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/1/10 11:17
 */

public class MediaFirstItemBean {
    private List<MediaNumberBean> recommend;
    private List<MediaTypeBean> subject;

    public List<MediaNumberBean> getRecommend() {
        return recommend;
    }

    public void setRecommend(List<MediaNumberBean> recommend) {
        this.recommend = recommend;
    }

    public List<MediaTypeBean> getSubject() {
        return subject;
    }

    public void setSubject(List<MediaTypeBean> subject) {
        this.subject = subject;
    }
}
