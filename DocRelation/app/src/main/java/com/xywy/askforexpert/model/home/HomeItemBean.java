package com.xywy.askforexpert.model.home;

import java.util.List;

/**
 * Created by xugan on 2018/4/8.
 */

public class HomeItemBean {
    public static final int TYPE_LIST = 1;
    public static final int TYPE_GRID = 2;
    public static final int TYPE_MORE = 3;
    private int type;
    private List<HomeGVItemBean> mHomeGVItemDatas;
    public HomeItemBean(List<HomeGVItemBean> homeGVItemDatas,int type){
        this.type = type;
        this.mHomeGVItemDatas = homeGVItemDatas;
    }


    public HomeItemBean(){
        this.type = TYPE_MORE;
    }

    public int getType() {
        return type;
    }

    public List<HomeGVItemBean> getHomeGVItemDatas(){
        return mHomeGVItemDatas;
    }
    public void setHomeGVItemDatas(List<HomeGVItemBean> homeGVItemDatas){
        this.type = TYPE_GRID;
        this.mHomeGVItemDatas = homeGVItemDatas;
    }

    public String image;
    public String username;
    public String doctorname;
    public String depart;
    public String pay_time;
    public String did;
    public String amount;
    public String note;
    public HomeItemBean(String image,String username,String doctorname,String depart,String pay_time,
                        String did,String amount,String note){
        this.type = TYPE_LIST;
        this.image = image;
        this.username = username;
        this.doctorname = doctorname;
        this.depart = depart;
        this.pay_time = pay_time;
        this.did = did;
        this.amount = amount;
        this.note = note;

    }
}
