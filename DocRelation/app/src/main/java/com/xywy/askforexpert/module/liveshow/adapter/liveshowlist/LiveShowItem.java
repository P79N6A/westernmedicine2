package com.xywy.askforexpert.module.liveshow.adapter.liveshowlist;

import com.xywy.askforexpert.model.liveshow.LiveShowListPageBean;

import java.util.List;

/**
 * Created by bailiangjin on 2017/3/4.
 */

public class LiveShowItem {

    private Type type;

    LiveShowListPageBean.DataBean item;

    List<LiveShowListPageBean.DataBean> multiList;

    public LiveShowItem(LiveShowListPageBean.DataBean item) {
        this.item = item;
        this.type=Type.SINGLE;
    }

    public LiveShowItem(List<LiveShowListPageBean.DataBean> multiList) {
        this.multiList = multiList;
        this.type=Type.MULTI;
    }


    public Type getType() {
        return type;
    }

    public LiveShowListPageBean.DataBean getItem() {
        return item;
    }

    public void setItem(LiveShowListPageBean.DataBean item) {
        this.item = item;
    }

    public List<LiveShowListPageBean.DataBean> getMultiList() {
        return multiList;
    }

    public void setMultiList(List<LiveShowListPageBean.DataBean> multiList) {
        this.multiList = multiList;
    }

    public enum Type{

        SINGLE,MULTI;
    }
}
