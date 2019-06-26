package com.xywy.askforexpert.module.message.imgroup.constants;

/**
 * Created by bailiangjin on 16/7/6.
 */
public enum UserPageShowType {

    SHOW("SHOW"),//展示群成员
    SELECT_NEW_MASTER("SELECT_NEW_MASTER"),//选择新群主
    ADD_MEMBER("ADD_MEMBER"),//添加用户
    CREATE_GROUP("CREATE_GROUP");//创建群组

    private String value;

    private UserPageShowType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}