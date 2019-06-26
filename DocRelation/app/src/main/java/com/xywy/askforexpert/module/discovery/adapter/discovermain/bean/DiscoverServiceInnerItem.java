package com.xywy.askforexpert.module.discovery.adapter.discovermain.bean;

/**
 * Created by bailiangjin on 2017/4/26.
 */

public class DiscoverServiceInnerItem {
    private DiscoverItemType itemType;
    private String name;
    private String desc;
    private int iconResId;
    private int helpedNumber;
    private int unreadMsgCount;
    private boolean isOpened;
    private boolean hasNotice;

    public DiscoverServiceInnerItem(String name, String desc, int iconResId,  boolean isOpened,int unreadMsgCount) {
        this.name = name;
        this.desc = desc;
        this.iconResId = iconResId;
        this.unreadMsgCount = unreadMsgCount;
        this.isOpened = isOpened;
    }

    public DiscoverServiceInnerItem(String name, String desc, int iconResId,  boolean isOpened) {
        this.name = name;
        this.desc = desc;
        this.iconResId = iconResId;
        this.isOpened = isOpened;
    }

    public DiscoverServiceInnerItem(DiscoverItemType type, String name, int iconResId) {
        this.itemType = type;
        this.name = name;
        this.iconResId = iconResId;
    }

    public DiscoverItemType getItemType() {
        return itemType;
    }


    public boolean isHasNotice() {
        return hasNotice;
    }

    public void setHasNotice(boolean hasNotice) {
        this.hasNotice = hasNotice;
    }

    public int getHelpedNumber() {
        return helpedNumber;
    }

    public void setHelpedNumber(int helpedNumber) {
        this.helpedNumber = helpedNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public int getUnreadMsgCount() {
        return unreadMsgCount;
    }

    public void setUnreadMsgCount(int unreadMsgCount) {
        this.unreadMsgCount = unreadMsgCount;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }
}
