package com.xywy.askforexpert.module.discovery.adapter.discovermain.bean;

/**
 * Created by bailiangjin on 2016/12/28.
 */

public class DiscoverListItemBean {

    private DiscoverItemType itemType;
    private String  title;
    private String  disc;
    private int iconResId;

    public DiscoverListItemBean(DiscoverItemType itemType, String title, int iconResId) {
        this.itemType = itemType;
        this.title = title;
        this.iconResId = iconResId;
    }

    public DiscoverItemType getItemType() {
        return itemType;
    }

    public void setItemType(DiscoverItemType itemType) {
        this.itemType = itemType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

}
