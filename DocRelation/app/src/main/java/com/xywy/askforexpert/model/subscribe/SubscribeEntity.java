package com.xywy.askforexpert.model.subscribe;

import java.io.Serializable;

/**
 * 类描述:资讯订阅
 * 创建人: shihao on 16/1/4 18:49.
 */
public class SubscribeEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private int id;

    private String name;

    private int type;

    private int style;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    @Override
    public String toString() {
        return "SubscribeEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", style=" + style +
                '}';
    }
}
