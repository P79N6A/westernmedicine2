package com.xywy.askforexpert.model;

/**
 * 项目名称：D_Platform 类名称：Message 类描述：消息实体类 创建人：shihao 创建时间：2015-6-10 下午5:21:03
 * 修改备注：
 */
public class Message {

    private int type;
    private String title;
    private String detail;
    private String msgNum;
    private String createTime;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getMsgNum() {
        return msgNum;
    }

    public void setMsgNum(String msgNum) {
        this.msgNum = msgNum;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

}
