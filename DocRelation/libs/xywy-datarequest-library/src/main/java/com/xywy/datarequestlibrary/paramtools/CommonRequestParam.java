package com.xywy.datarequestlibrary.paramtools;

/**
 * Created by bailiangjin on 2017/4/25.
 */

public class CommonRequestParam {

    private String source;
    private String pro;
    private String signKey;
    private String os = "android";

    public CommonRequestParam(String source, String pro, String signKey) {
        this.source = source;
        this.pro = pro;
        this.signKey = signKey;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPro() {
        return pro;
    }

    public void setPro(String pro) {
        this.pro = pro;
    }

    public String getSignKey() {
        return signKey;
    }

    public void setSignKey(String signKey) {
        this.signKey = signKey;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }
}
