package com.xywy.askforexpert.model.MyPurse;

import java.util.List;

/**
 * Created by xugan on 2018/6/20.
 */

public class MyPurseItemBean {
    public static final int TYPE_LIST = 1;
    public static final int TYPE_GRID = 2;
    public static final int TYPE_MORE = 3;
    private int type;
    public int card;//是否绑定银行卡 1-已绑 0-未绑
    public int current_month;
    private List<MyPurseGVItemBean> mHomeGVItemDatas;
    private List<BillMonthInfo> mMonthInfos;
    public MyPurseItemBean(List<MyPurseGVItemBean> homeGVItemDatas,int current_month, int type){
        this.type = type;
        this.mHomeGVItemDatas = homeGVItemDatas;
        this.current_month = current_month;
    }


    public MyPurseItemBean(int card,List<BillMonthInfo> monthInfos){
        this.type = TYPE_MORE;
        this.mMonthInfos = monthInfos;
        this.card = card;
    }

    public List<BillMonthInfo> getMonthInfoDatas(){
        return mMonthInfos;
    }

    public int getType() {
        return type;
    }

    public List<MyPurseGVItemBean> getHomeGVItemDatas(){
        return mHomeGVItemDatas;
    }
    public void setHomeGVItemDatas(List<MyPurseGVItemBean> homeGVItemDatas){
        this.type = TYPE_GRID;
        this.mHomeGVItemDatas = homeGVItemDatas;
    }

    public float club_percent;
    public float immediate_percent;
    public float familydoc_percent;
    public float dhysdoc_percent;
    public float other_percent;
    public List<BillMonthInfo> atrend;
    public MyPurseItemBean(float club_percent, float immediate_percent, float familydoc_percent,
                           float dhysdoc_percent, float other_percent,
                           List<BillMonthInfo> atrend){
        this.type = TYPE_LIST;
        this.club_percent = club_percent;
        this.immediate_percent = immediate_percent;
        this.familydoc_percent = familydoc_percent;
        this.dhysdoc_percent = dhysdoc_percent;
        this.other_percent = other_percent;
        this.atrend = atrend;
    }
}
