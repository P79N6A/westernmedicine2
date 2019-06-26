package com.xywy.livevideo.entity;

/**
 * Created by zhangzheng on 2017/2/28.
 */
public class VideoListEntity {
    public static final int ITEM_TYPE_NORMAL = 0;
    public static final int ITEM_TYPE_HEAD = ITEM_TYPE_NORMAL + 1;
    private int itemType;
    private Object data;

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
