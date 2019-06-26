package com.xywy.askforexpert.module.discovery.medicine.module.medical.entity;

public class PharmacyRecordEntity {
    private String id;//处方id
    private String time;//处方时间
    private String oid;//订单id
    private String uname;

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private int order_status;//订单状态，1-未支付 2-待发货 3-待收货 4-已完成 5-已取消 6-已退款 7-待退款
    private String image;
    private Drug drug_list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public Drug getDrug_list() {
        return drug_list;
    }

    public void setDrug_list(Drug drug_list) {
        this.drug_list = drug_list;
    }

//stone 对应关系
//    @"take_unit":@{
//        @"1":@"片",
//        @"2":@"粒",
//        @"3":@"包",
//        @"4":@"颗",
//        @"5":@"支",
//        @"6":@"克",
//        @"7":@"毫克",
//        @"8":@"微克",
//        @"9":@"毫升"
//    },
//    @"take_method":@{
//        @"1":@"口服",
//        @"2":@"注射",
//        @"3":@"外用",
//        @"4":@"静滴",
//        @"5":@"吸入"
//    }
    public static class Drug{
        private int id;
        private int pid;//处方id
        private String gid;//商品id
        private String gsku;//商品sku
        private String gname;
        private String specification;
        private String num;
        private String price;
        private String amount;//商品总价
        private String take_rate;//服用频率（每天几次）
        private String take_time;//服用时间（饭前、饭后...）
        private String take_num;//服用量
        private int take_unit;//服用量单位（片、粒、包...）
        private int take_method;//服用方法（口服、外用、注射）
        private String remark;//备注

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getGsku() {
            return gsku;
        }

        public void setGsku(String gsku) {
            this.gsku = gsku;
        }

        public String getGname() {
            return gname;
        }

        public void setGname(String gname) {
            this.gname = gname;
        }

        public String getSpecification() {
            return specification;
        }

        public void setSpecification(String specification) {
            this.specification = specification;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getTake_rate() {
            return take_rate;
        }

        public void setTake_rate(String take_rate) {
            this.take_rate = take_rate;
        }

        public String getTake_time() {
            return take_time;
        }

        public void setTake_time(String take_time) {
            this.take_time = take_time;
        }

        public String getTake_num() {
            return take_num;
        }

        public void setTake_num(String take_num) {
            this.take_num = take_num;
        }

        public int getTake_unit() {
            return take_unit;
        }

        public void setTake_unit(int take_unit) {
            this.take_unit = take_unit;
        }

        public int getTake_method() {
            return take_method;
        }

        public void setTake_method(int take_method) {
            this.take_method = take_method;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }




}
