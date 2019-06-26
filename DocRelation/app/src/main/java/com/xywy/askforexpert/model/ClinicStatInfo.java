package com.xywy.askforexpert.model;

import java.util.List;

/**
 * 我的诊所 开通状态
 *
 * @author 王鹏
 * @2015-5-25下午1:59:19
 */
public class ClinicStatInfo {

    private String code;
    private String msg;
    private ClinicStatInfo data;
    private String dati;//问题广场答题
    private String yuyue;//预约转诊
    private String phone;//电话医生
    private String family;//家庭医生

    private int club_assign;         //图文咨询-指定付费  -1未开通  0 待审核 1开通 2 关闭
    private int imwd_reward;         //IM问答-悬赏  -1未开通0 待审核 1开通 2 关闭

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    private List<Service> services;//各个业务的集合

    public int getClub_assign() {
        return club_assign;
    }

    public void setClub_assign(int club_assign) {
        this.club_assign = club_assign;
    }

    public int getImwd_reward() {
        return imwd_reward;
    }

    public void setImwd_reward(int imwd_reward) {
        this.imwd_reward = imwd_reward;
    }

    public int getImwd_assign() {
        return imwd_assign;
    }

    public void setImwd_assign(int imwd_assign) {
        this.imwd_assign = imwd_assign;
    }

    public int getClub_reward() {
        return club_reward;
    }

    public void setClub_reward(int club_reward) {
        this.club_reward = club_reward;
    }

    private int imwd_assign;         //M问答-指定付费  -1未开通0 待审核 1开通 2 关闭
    private int club_reward;         //图文咨询-悬赏    -1未开通   0 待审核 1开通 2 关闭

    private String iszj;


    public String getIszj() {
        return iszj;
    }

    public void setIszj(String iszj) {
        this.iszj = iszj;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ClinicStatInfo getData() {
        return data;
    }

    public void setData(ClinicStatInfo data) {
        this.data = data;
    }

    public String getDati() {
        return dati;
    }

    public void setDati(String dati) {
        this.dati = dati;
    }

    public String getYuyue() {
        return yuyue;
    }

    public void setYuyue(String yuyue) {
        this.yuyue = yuyue;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }
}
