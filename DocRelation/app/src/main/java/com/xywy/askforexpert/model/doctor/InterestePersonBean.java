package com.xywy.askforexpert.model.doctor;

import java.util.List;

/**
 * 感兴趣医生
 *
 * @author apple
 */
public class InterestePersonBean {

    private int code;
    private String msg;
    private List<InterestePersonItemBean> data;

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

    public List<InterestePersonItemBean> getData() {
        return data;
    }

    public void setData(List<InterestePersonItemBean> data) {
        this.data = data;
    }


}
