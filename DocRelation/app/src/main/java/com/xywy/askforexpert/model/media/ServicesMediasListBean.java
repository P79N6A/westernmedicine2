package com.xywy.askforexpert.model.media;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/6/13 14:58
 */

import javax.annotation.Generated;

/**
 * code	|code	integer	返回码
 * msg	|msg	string	返回信息
 * total	|total	string	总数
 * list	|list	array	数据体
 * id	|list|id	string	媒体号或服务号id
 * name	|list|name	string	媒体号或服务号名称
 * img	|list|img	string	图片
 */
@Generated("org.jsonschema2pojo")
public class ServicesMediasListBean {

    /**
     * id : 97489285
     * name : 测试自动推送账号
     * img : http://xs3.op.xywy.com/club.xywy.com/doc/20160520/150_150_31dfb77bf9138c.jpg
     */

    private String id;
    private String name;
    private String img;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
