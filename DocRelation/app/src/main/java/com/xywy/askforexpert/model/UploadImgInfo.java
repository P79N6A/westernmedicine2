package com.xywy.askforexpert.model;

import java.util.List;

/**
 * 图片上传返回地址 信息
 *
 * @author 王鹏
 * @2015-5-5下午4:52:50
 */
public class UploadImgInfo {

    private String code;
    private String msg;
    private List<UploadImgInfo> data;
    private String url;

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

    public List<UploadImgInfo> getData() {
        return data;
    }

    public void setData(List<UploadImgInfo> data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
