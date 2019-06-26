package com.xywy.askforexpert.model;

/**
 * 账单列表
 *
 * @author SHR
 * @2015-5-5上午9:58:26
 */
public class BillItem {
    /**
     * 日期
     */
    private String htime;
    /**
     * 详细日期
     */
    private String dtime;
    /**
     * 绩效收入
     */
    private String hnum;
    /**
     * 来源
     */
    private String hreason;

    public String getHtime() {
        return htime;
    }

    public void setHtime(String htime) {
        this.htime = htime;
    }

    public String getHnum() {
        return hnum;
    }

    public void setHnum(String hnum) {
        this.hnum = hnum;
    }

    public String getHreason() {
        return hreason;
    }

    public void setHreason(String hreason) {
        this.hreason = hreason;
    }

    public String getDtime() {
        return dtime;
    }

    public void setDtime(String dtime) {
        this.dtime = dtime;
    }

}
