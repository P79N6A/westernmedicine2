package com.xywy.askforexpert.model.consultentity;

/**
 * Created by jason on 2018/10/15.
 */

public class JSDHBean {
    private int code;
    private String msg;
    private JSDHData data;

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

    public JSDHData getData() {
        return data;
    }

    public void setData(JSDHData data) {
        this.data = data;
    }

    public class JSDHData{
        int total;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }
}
