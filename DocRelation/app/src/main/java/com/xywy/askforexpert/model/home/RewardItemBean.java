package com.xywy.askforexpert.model.home;

import java.util.List;

/**
 * Created by xugan on 2018/4/12.
 */

public class RewardItemBean{
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
