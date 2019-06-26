package com.xywy.askforexpert.model.consultentity;

/**
 * IM新消息数
 */

public class NewMessageNumEntity {

//    {
//        "code": 10000,
//            "msg": "成功",
//            "data": {
//        "num": 2
//    }
//    }

    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int num;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}
