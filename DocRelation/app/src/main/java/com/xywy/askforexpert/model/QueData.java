package com.xywy.askforexpert.model;

import java.io.Serializable;

public class QueData implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String url;
    private int num;

    private String status;

    public QueData() {
    }

    public QueData(String name) {
        this.name = name;
    }

    public QueData(String name, String url, int num) {
        this.name = name;
        this.url = url;
        this.num = num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
