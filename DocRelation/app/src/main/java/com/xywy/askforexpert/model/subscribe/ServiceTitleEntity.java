package com.xywy.askforexpert.model.subscribe;

import java.util.List;

/**
 * 类描述:
 * 创建人: shihao on 16/1/4 18:48.
 */
public class ServiceTitleEntity {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private int code;

    private String msg;

    private int flag;

    private List<SubscribeEntity> subscribe;

    private List<ServeEntity> serve;

    private List<SubscribeMediaBean> media;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public List<ServeEntity> getServe() {
        return serve;
    }

    public void setServe(List<ServeEntity> serve) {
        this.serve = serve;
    }

    public List<SubscribeEntity> getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(List<SubscribeEntity> subscribe) {
        this.subscribe = subscribe;
    }

    public List<SubscribeMediaBean> getMedia() {
        return media;
    }

    public void setMedia(List<SubscribeMediaBean> media) {
        this.media = media;
    }

    @Override
    public String toString() {
        return "ServiceTitleEntity{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", flag=" + flag +
                ", subscribe=" + subscribe +
                ", serve=" + serve +
                ", media=" + media +
                '}';
    }
}
