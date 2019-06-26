package com.xywy.component.datarequest.uploadLog.bean;

import java.util.List;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/8/22 17:18
 */
public  class UploadBean {
    private String appName;
    private String appVersion;
    private String device;
    private List<LogBean> logs;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }


    public List<LogBean> getLogs() {
        return logs;
    }

    public void setLogs(List<LogBean> logs) {
        this.logs = logs;
    }
}

