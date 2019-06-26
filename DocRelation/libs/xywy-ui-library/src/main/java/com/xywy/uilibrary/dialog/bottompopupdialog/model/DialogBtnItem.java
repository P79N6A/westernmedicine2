package com.xywy.uilibrary.dialog.bottompopupdialog.model;

import com.xywy.uilibrary.dialog.bottompopupdialog.listener.BtnClickListener;

/**
 * Created by bailiangjin on 2017/5/6.
 */

public class DialogBtnItem {
    private String name;
    private StyleType styleType;
    private NumberType numberType;
    private BtnClickListener btnClickListener;
    boolean isCancleItem;

    public DialogBtnItem(String name, BtnClickListener btnClickListener) {
        this.name = name;
        this.numberType = NumberType.GROUP;
        this.btnClickListener = btnClickListener;
    }

    public DialogBtnItem(String name, NumberType numberType, BtnClickListener btnClickListener) {
        this.name = name;
        this.numberType = numberType;
        this.btnClickListener = btnClickListener;
    }

    public boolean isCancleItem() {
        return isCancleItem;
    }

    public DialogBtnItem setCancleItem(boolean cancleItem) {
        isCancleItem = cancleItem;
        return this;
    }

    public NumberType getNumberType() {
        return numberType;
    }

    public void setNumberType(NumberType numberType) {
        this.numberType = numberType;
    }

    public StyleType getStyleType() {
        return styleType;
    }

    public void setStyleType(StyleType styleType) {
        this.styleType = styleType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public BtnClickListener getBtnClickListener() {
        return btnClickListener;
    }

    public void setBtnClickListener(BtnClickListener btnClickListener) {
        this.btnClickListener = btnClickListener;
    }

    public enum StyleType {
        TOP,
        MIDDLE,
        BOTTOM,
        FULL
    }

    public enum NumberType {
        GROUP,
        SINGLE
    }

}
