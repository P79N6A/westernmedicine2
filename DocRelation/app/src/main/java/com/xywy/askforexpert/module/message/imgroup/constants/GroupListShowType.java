package com.xywy.askforexpert.module.message.imgroup.constants;

/**
 * Created by bailiangjin on 16/7/6.
 */
public enum GroupListShowType {

    SHOW("SHOW"),//展示群列表
    SELECT_TO_SHARE("SELECT_TO_SHARE");//选择群组 分享消息

    private String value;

    private GroupListShowType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}