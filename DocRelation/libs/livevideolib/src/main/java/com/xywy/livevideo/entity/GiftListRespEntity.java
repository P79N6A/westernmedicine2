package com.xywy.livevideo.entity;

import java.util.List;

/**
 * Created by zhangzheng on 2017/3/6.
 */
public class GiftListRespEntity {

    /**
     * code : 10000
     * msg : 成功
     * data : [{"id":1,"name":"掌声\r\n","price":1,"createtime":1,"state":1,"img":"http://www.qqtu8.com/f/20100402165615.gif"},{"id":2,"name":"鲜花\r\n","price":10,"createtime":0,"state":1,"img":"http://www.qqtu8.com/f/20100402165615.gif"},{"id":3,"name":"赞","price":20,"createtime":0,"state":1,"img":"http://www.qqtu8.com/f/20100402165615.gif"},{"id":4,"name":"么么哒","price":50,"createtime":0,"state":1,"img":"http://www.qqtu8.com/f/20100402165615.gif"},{"id":5,"name":"游艇","price":100,"createtime":0,"state":1,"img":"http://www.qqtu8.com/f/20100402165615.gif"},{"id":6,"name":"飞机","price":200,"createtime":0,"state":1,"img":"http://www.qqtu8.com/f/20100402165615.gif"}]
     */

    private int code;
    private String msg;
    /**
     * id : 1
     * name : 掌声

     * price : 1
     * createtime : 1
     * state : 1
     * img : http://www.qqtu8.com/f/20100402165615.gif
     */

    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String id;
        private String name;
        private int price;
        private int createtime;
        private int state;
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

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getCreatetime() {
            return createtime;
        }

        public void setCreatetime(int createtime) {
            this.createtime = createtime;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}
