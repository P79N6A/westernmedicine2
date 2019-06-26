package com.xywy.askforexpert.model;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 描述：家庭医生用户评论json对象描述
 * </p>
 *
 * @author liuxuejiao
 * @2015-05-14 下午17:46:20
 */
public class HomeDoctorUserComBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8092078855707790600L;
    /**
     * code: 返回状态码
     */
    private int code;
    /**
     * msg: 返回说明
     */
    private String msg;
    /**
     * is_more: 是否加载更多
     */
    private boolean is_more;
    /**
     * list: 返回数据
     */
    private List<UserCommentBean> list;
    /**
     * rank: 总排名
     */
    private String rank;
    /**
     * totalscore: 每页平均分
     */
    private float totalscore;

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

    public boolean isIs_more() {
        return is_more;
    }

    public void setIs_more(boolean is_more) {
        this.is_more = is_more;
    }

    public List<UserCommentBean> getList() {
        return list;
    }

    public void setList(List<UserCommentBean> list) {
        this.list = list;
    }

    public float getTotalscore() {
        return totalscore;
    }

    public void setTotalscore(float totalscore) {
        this.totalscore = totalscore;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

}
