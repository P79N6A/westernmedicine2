package com.xywy.askforexpert.model.doctor;

import com.xywy.askforexpert.model.api.BaseStringResultBean;

import java.io.Serializable;
import java.util.List;

public class MoreMessage extends BaseStringResultBean implements Serializable {

    public List<CommentBean> list;
    public Messages message;
    public User user;

    public List<CommentBean> getList() {
        return list;
    }

    public void setList(List<CommentBean> list) {
        this.list = list;
    }

    public Messages getMessage() {
        return message;
    }

    public void setMessage(Messages message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
