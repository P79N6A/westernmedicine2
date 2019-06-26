package com.xywy.askforexpert.model.media;

import com.xywy.askforexpert.model.api.BaseResultBean;

import java.util.List;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/1/10 11:17
 */

public class TodayRecommendBean extends BaseResultBean {
    private List<MediaNumberBean> data;

    public List<MediaNumberBean> getData() {
        return data;
    }

    public void setData(List<MediaNumberBean> data) {
        this.data = data;
    }

}
