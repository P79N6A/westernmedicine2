package com.xywy.askforexpert.model.MyPurse;

import java.util.List;

/**
 * Created by xugan on 2018/6/25.
 */

public class MyPurseBean {
    public int current_month;   //当前月份
    public String month_income_total;   //当月总收入
    public String club;
    public String immediate;
    public String familydoc;
    public String dhysdoc;
    public String other;
    public String onlinedrug;
    public int card;    //是否绑定银行卡 1-已绑 0-未绑
    public List<BillMonthInfo> months;
    public float club_percent;
    public float immediate_percent;
    public float familydoc_percent;
    public float dhysdoc_percent;
    public float other_percent;
    public List<BillMonthInfo> atrend;

}
