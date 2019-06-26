package com.xywy.askforexpert.widget.module.doctorcircle;

/**
 * 底部弹框类型
 * Created by bailiangjin on 2016/11/7.
 */

public class PopupWindowType {

    /**
     * 展示类型
     */
    private String showType;

    private  boolean isDeleteShow;

    public PopupWindowType(String showType, boolean isDeleteShow) {
        this.showType = showType;
        this.isDeleteShow = isDeleteShow;
    }



    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public boolean isDeleteShow() {
        return isDeleteShow;
    }

    public void setDeleteShow(boolean deleteShow) {
        isDeleteShow = deleteShow;
    }
}
