package com.xywy.askforexpert.model;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 描述：家庭医生签约订单json对象描述
 * </p>
 *
 * @author liuxuejiao
 * @2015-05-14 下午15:46:20
 */
public class HomeDoctorSignOderBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8092078855707790600L;
    /**
     * code: 返回状态码
     */
    public int code;
    /**
     * msg: 返回说明
     */
    public String msg;
    /**
     * is_more: 是否加载更多
     */
    public boolean is_more;
    /**
     * list: 返回数据
     */
    public List<SignOrderBean> list;

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

    public List<SignOrderBean> getList() {
        return list;
    }

    public void setList(List<SignOrderBean> list) {
        this.list = list;
    }


}
