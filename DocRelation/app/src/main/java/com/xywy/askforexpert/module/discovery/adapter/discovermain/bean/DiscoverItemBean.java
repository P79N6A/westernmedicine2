package com.xywy.askforexpert.module.discovery.adapter.discovermain.bean;

/**
 * Created by bailiangjin on 2016/12/28.
 */

public class DiscoverItemBean {

    public static final int TYPE_LIST = 1;
    public static final int TYPE_SERVICE = 2;
    public static final int TYPE_LIVE_SHOW = 3;
    //药品助手类型
    public static final int TYPE_MEDICINE = 4;

    private int type;

    private DiscoverListItemBean horizonItemBean;
    private DiscoverServiceItem serviceItem;
    private DiscoverLiveShowItemBean liveShowItemBean;

    public DiscoverItemBean(int type) {
        this.type = type;
    }

    public DiscoverItemBean(DiscoverListItemBean horizonItemBean) {
        this.type = TYPE_LIST;
        this.horizonItemBean = horizonItemBean;
    }

    public DiscoverItemBean(DiscoverServiceItem serviceItem) {
        this.type = TYPE_SERVICE;
        this.serviceItem = serviceItem;
    }

    public DiscoverItemBean(DiscoverLiveShowItemBean liveShowItemBean) {
        this.type =TYPE_LIVE_SHOW;
        this.liveShowItemBean = liveShowItemBean;
    }

    public int getType() {
        return type;
    }

    public DiscoverListItemBean getHorizonItemBean() {
        return horizonItemBean;
    }

    public DiscoverServiceItem getServiceItem() {
        return serviceItem;
    }

    public DiscoverLiveShowItemBean getLiveShowItemBean() {
        return liveShowItemBean;
    }
}
