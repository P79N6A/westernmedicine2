package com.xywy.component.test;

/**
 * Created by shijiazi on 15/12/26.
 * 测试数据结构
 */
public class TestModel {

    private long time;

    private long uid;


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "time=" + time + ",uid=" + uid;
    }
}
