package com.xywy.askforexpert.model;

import java.util.List;

/***
 * 积分详情
 *
 * @author 王鹏
 * @2015-10-30上午10:01:16
 */
public class MyScoresInfo {

    private String code;
    private String msg;
    private MyScoresInfo data;
    private String total;
    private List<MyScoresInfo> detail;
    private String point;
    private String createtime;
    private String demo;
    private String type;

    private MyScoresInfo points;
    private String gone_score;
    private String total_score;
    private String balance;
    private String endtime;


    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

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

    public MyScoresInfo getData() {
        return data;
    }

    public void setData(MyScoresInfo data) {
        this.data = data;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<MyScoresInfo> getDetail() {
        return detail;
    }

    public void setDetail(List<MyScoresInfo> detail) {
        this.detail = detail;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MyScoresInfo getPoints() {
        return points;
    }

    public void setPoints(MyScoresInfo points) {
        this.points = points;
    }

    public String getGone_score() {
        return gone_score;
    }

    public void setGone_score(String gone_score) {
        this.gone_score = gone_score;
    }

    public String getTotal_score() {
        return total_score;
    }

    public void setTotal_score(String total_score) {
        this.total_score = total_score;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }


}
