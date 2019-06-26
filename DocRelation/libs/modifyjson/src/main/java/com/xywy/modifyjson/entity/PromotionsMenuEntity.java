package com.xywy.modifyjson.entity;


import java.util.List;

/**
* 限时促销 菜单项  包括
 *
 * 省-市   1级科室-2级科室
* @author pihaishen
* create at 2016/09/05
*/
public class PromotionsMenuEntity {

    /**
     * code : 10000
     * data: []
     * msg : 请求成功
     */
    private int code;

    private String msg;

    private List<SubsEntity> data;

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

    public List<SubsEntity> getData() {
        return data;
    }

    public void setData(List<SubsEntity> data) {
        this.data = data;
    }

    public static class SubsEntity extends SubEntity{
        private List<SubEntity> subs;

        public List<SubEntity> getSubs() {
            return subs;
        }

        public void setSubs(List<SubEntity> subs) {
            this.subs = subs;
        }
    }

    public static class SubEntity{
        private int id;

        private String name;

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
    }

}
