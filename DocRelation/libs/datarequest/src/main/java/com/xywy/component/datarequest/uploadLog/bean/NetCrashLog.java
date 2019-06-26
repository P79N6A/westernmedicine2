package com.xywy.component.datarequest.uploadLog.bean;

public  class NetCrashLog {
    private String netType;
    private String request;
    private String response;
    private String stackStrace;
    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getStackStrace() {
        return stackStrace;
    }

    public void setStackStrace(String stackStrace) {
        this.stackStrace = stackStrace;
    }
}
