package com.xywy.askforexpert.model.subscribe;

/**
 * 类描述:
 * 创建人: shihao on 16/1/4 18:51.
 */
public class ServeEntity {

    /**
     * "id": 1,
     * "name": "问题广场",
     * "type": 2
     */

    private int id;

    private String name;

    private int type;


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

    @Override
    public String toString() {
        return "ServeEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
