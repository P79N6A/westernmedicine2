package com.xywy.retrofit.rxbus;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/9/18 10:46
 */
public class Event<T> {
    private String name;
    private T data;

    public Event(String name, T data) {
        this.name = name;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
