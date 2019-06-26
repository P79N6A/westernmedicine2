package com.xywy.askforexpert.model.consultentity;

/**
 * Created by zhangzheng on 2017/5/2.
 */

public class ChatBottomItemEntity {
    private String text;
    private boolean isEnable;
    private int ennabledImgSrc;
    private int disabledImgSrc;

    public ChatBottomItemEntity(){

    }

    public ChatBottomItemEntity(String text,boolean isEnable,int ennabledImgSrc,int disabledImgSrc){
        this.text=text;
        this.isEnable=isEnable;
        this.ennabledImgSrc=ennabledImgSrc;
        this.disabledImgSrc=disabledImgSrc;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public int getEnnabledImgSrc() {
        return ennabledImgSrc;
    }

    public void setEnnabledImgSrc(int ennabledImgSrc) {
        this.ennabledImgSrc = ennabledImgSrc;
    }

    public int getDisabledImgSrc() {
        return disabledImgSrc;
    }

    public void setDisabledImgSrc(int disabledImgSrc) {
        this.disabledImgSrc = disabledImgSrc;
    }

}
