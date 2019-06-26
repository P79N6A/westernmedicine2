package com.xywy.askforexpert.model.liveshow;

/**
 * Created by bailiangjin on 2017/3/8.
 */

public class LiveShowStateInfoBean {


    /**
     * code : 10000
     * msg : 成功
     * data : {"is_Anchor":1,"cover":"http://img2.cache.netease.com/photo/0010/2017-02-27/600x450_CE9TA26L50CB0010.png","balance":10000}
     */

    private int code;
    private String msg;
    private LiveShowStateInfoEntity data;

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

    public LiveShowStateInfoEntity getData() {
        return data;
    }

    public void setData(LiveShowStateInfoEntity data) {
        this.data = data;
    }


}
