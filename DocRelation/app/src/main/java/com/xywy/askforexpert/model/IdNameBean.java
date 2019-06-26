package com.xywy.askforexpert.model;

import java.io.Serializable;

/**
 * id name
 * stone
 */
public class IdNameBean implements Serializable {

    public String id;
    public String name;

    public IdNameBean(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
