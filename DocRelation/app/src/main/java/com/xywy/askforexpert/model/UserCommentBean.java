package com.xywy.askforexpert.model;

import java.io.Serializable;

/**
 * <p>
 * 描述：家庭医生评论列表item对象描述
 * </p>
 *
 * @author liuxuejiao
 * @2015-05-14 下午17:56:20
 */
public class UserCommentBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4234647666117154149L;
    /**
     * g_uname: 评价人名称
     */
    private String g_uname;
    /**
     * g_ruid: 医生ID
     */
    private String g_ruid;
    /**
     * g_stat: 医生总评分
     */
    private String g_stat;
    /**
     * g_date: 评价时间
     */
    private String g_date;
    /**
     * g_cons: 评价内容
     */
    private String g_cons;

    public UserCommentBean() {
        super();
    }

    public String getG_uname() {
        return g_uname;
    }

    public void setG_uname(String g_uname) {
        this.g_uname = g_uname;
    }

    public String getG_ruid() {
        return g_ruid;
    }

    public void setG_ruid(String g_ruid) {
        this.g_ruid = g_ruid;
    }

    public String getG_stat() {
        return g_stat;
    }

    public void setG_stat(String g_stat) {
        this.g_stat = g_stat;
    }

    public String getG_date() {
        return g_date;
    }

    public void setG_date(String g_date) {
        this.g_date = g_date;
    }

    public String getG_cons() {
        return g_cons;
    }

    public void setG_cons(String g_cons) {
        this.g_cons = g_cons;
    }

}
