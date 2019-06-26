package com.xywy.component.datarequest.uploadLog.bean;

public  class LogBean<T> {
    private String time;
    private String tag;
    private T logContent;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    public T getLogContent() {
        return logContent;
    }

    public void setLogContent(T logContent) {
        this.logContent = logContent;
    }
}
