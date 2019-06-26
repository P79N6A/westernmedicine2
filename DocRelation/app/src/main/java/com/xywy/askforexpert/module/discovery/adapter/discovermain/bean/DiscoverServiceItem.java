package com.xywy.askforexpert.module.discovery.adapter.discovermain.bean;

import java.util.List;

/**
 * Created by bailiangjin on 2016/12/29.
 */

public class DiscoverServiceItem {

    private List<DiscoverServiceInnerItem> innerItemList;

    public DiscoverServiceItem(List<DiscoverServiceInnerItem> innerItemList) {
        this.innerItemList = innerItemList;
    }

    public List<DiscoverServiceInnerItem> getInnerItemList() {
        return innerItemList;
    }

    public void setInnerItemList(List<DiscoverServiceInnerItem> innerItemList) {
        this.innerItemList = innerItemList;
    }
}
