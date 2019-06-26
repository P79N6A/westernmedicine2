package com.xywy.askforexpert.model.healthy;

import java.util.List;

import javax.annotation.Generated;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/6/8 17:07
 */

/**
 * code	|code	integer	返回码
 * msg	|msg	string	返回信息
 * data	|data	array	数据体
 * id	|data|id	string	患者id
 * xm	|data|xm	string	患者姓名
 * xb	|data|xb	string	患者性别
 * txdz	|data|txdz	string	患者头像
 * nl	|data|nl	integer	患者年龄
 */
@Generated("org.jsonschema2pojo")
public class HealthyUserListBean {
    /**
     * code : 10000
     * msg : 成功
     * data : [{"id":"1","xm":"小方","xb":"女","txdz":"3g.xtjk.wenkang.cn/aa.png","nl":29},{"id":"12","xm":"","xb":"男","txdz":"","nl":46}]
     */

    private int code;
    private String msg;
    /**
     * id : 1
     * xm : 小方
     * xb : 女
     * txdz : 3g.xtjk.wenkang.cn/aa.png
     * nl : 29
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
        private String xm;
        private String xb;
        private String txdz;
        private int nl;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getXm() {
            return xm;
        }

        public void setXm(String xm) {
            this.xm = xm;
        }

        public String getXb() {
            return xb;
        }

        public void setXb(String xb) {
            this.xb = xb;
        }

        public String getTxdz() {
            return txdz;
        }

        public void setTxdz(String txdz) {
            this.txdz = txdz;
        }

        public int getNl() {
            return nl;
        }

        public void setNl(int nl) {
            this.nl = nl;
        }
    }
}
