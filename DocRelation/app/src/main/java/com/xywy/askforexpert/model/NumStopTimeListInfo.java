package com.xywy.askforexpert.model;

import java.util.List;

/**
 * 停诊服务 列表
 *
 * @author 王鹏
 * @2015-5-28下午9:10:38
 */
public class NumStopTimeListInfo {

    private String code;
    private String msg;
    private NumStopTimeListInfo data;
    private String total;
    private List<NumStopTimeListInfo> data_list;
    private String startdate;
    private String enddate;
    private String reason;
    private String addtime;
    private String state;
    private String state_name;

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

    public NumStopTimeListInfo getData() {
        return data;
    }

    public void setData(NumStopTimeListInfo data) {
        this.data = data;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<NumStopTimeListInfo> getData_list() {
        return data_list;
    }

    public void setData_list(List<NumStopTimeListInfo> data_list) {
        this.data_list = data_list;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }


}
