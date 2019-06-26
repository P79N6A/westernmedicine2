package com.xywy.askforexpert.model.doctor;

import com.xywy.askforexpert.model.api.BaseStringResultBean;

import java.io.Serializable;


public class DynamicDtaile extends BaseStringResultBean implements Serializable {
    public Data data;
    public User user;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
