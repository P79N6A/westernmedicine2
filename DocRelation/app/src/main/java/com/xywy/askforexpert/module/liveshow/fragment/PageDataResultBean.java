package com.xywy.askforexpert.module.liveshow.fragment;

import java.util.List;

/**
 * Created by bailiangjin on 2017/3/15.
 */

public class PageDataResultBean<T> {

    private boolean hasMoreData;

    private List<T> dataList;



    public PageDataResultBean(boolean hasMoreData, List<T> dataList) {
        this.hasMoreData = hasMoreData;
        this.dataList = dataList;
    }

    public boolean isHasMoreData() {
        return hasMoreData;
    }

    public void setHasMoreData(boolean hasMoreData) {
        this.hasMoreData = hasMoreData;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
