package com.xywy.askforexpert.model.followList;

import java.util.List;

/**
 * Compiler: Android Studio
 * Project: D_Platform
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2015/12/22 9:12
 */
public class FollowListRootData {
    private String code;
    private String msg;
    private List<FollowListData> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<FollowListData> getData() {
        return data;
    }

    public void setData(List<FollowListData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FollowListRootData{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
