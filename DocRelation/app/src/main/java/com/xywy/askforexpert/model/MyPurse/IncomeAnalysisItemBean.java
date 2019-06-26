package com.xywy.askforexpert.model.MyPurse;

import java.util.List;

/**
 * Created by xugan on 2018/6/20.
 */

public class IncomeAnalysisItemBean {
    public String total;
    public List<Reward> list;
    public class Reward{
        public String image;
        public String username;
        public String doctorname;
        public String depart;
        public String pay_time;
        public String did;
        public String amount;
        public String note;
    }

}
