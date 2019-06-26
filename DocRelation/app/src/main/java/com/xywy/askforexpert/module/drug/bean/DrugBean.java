package com.xywy.askforexpert.module.drug.bean;


import java.io.Serializable;

/**
 * 药品bean stone
 */
public class DrugBean implements Serializable {

    /**
     * "id": "782",
     * "stock": "99",
     * "fczs": "15",
     * "productId": "432375",
     * "wksmj": "11.50",
     * "specification": "100包",
     * "image": "http://static.img.xywy.com/online-user/images/no_drug_130x136.jpg",
     * "company": "台湾泛生制药厂股份有限公司",
     * "name": "泛生舒复"
     * "useage":""
     */

    public String id; //商品id
    public String pid;
    public int stock; //库存
    public String fczs;
    public String productId; //规格id
    public String specification; //规格
    public String wksmj; //售卖价
    public String image; //图片
    public String company; //公司
    public String name; //名称
    public String supplier;

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getUseage() {
        return useage;
    }

    public void setUseage(String useage) {
        this.useage = useage;
    }

    private String useage;

    public int isCommon; //是否加入常用药 0未加入 1 已加入

    //处方中药品
    /**
     * gname : 修正 肺宁颗粒
     * num : 1
     * price : 32.00
     * amount : 32.00
     * take_rate : 1
     * take_time : 饭后服用
     * take_num : 1
     * take_unit : 支
     * take_method : 口服
     * take_day : 0
     * drug_unit : 盒
     */


    public String gname;
    public String num;
    public String price;
    public String amount;
    public String take_rate;
    public String take_time;
    public String take_num;
    public String take_unit;
    public String take_method;
    public String take_day;
    public String drug_unit;


    //开处方需要用的字段
//    id：商品ID
//    num：开药数量(int),
//    take_rate：服用频率（每天几次 string）
//    take_time：服用时间（饭前、饭后... string)
//    take_num：服用量（int）
//    take_unit：服用量单位(int),
//    take_method：服用方法(int),
//    remark：备注(string)
//    take_day ：用药天数 (int)
//    drug_unit：开药单位(int)

    public String remark;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getFczs() {
        return fczs;
    }

    public void setFczs(String fczs) {
        this.fczs = fczs;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getWksmj() {
        return wksmj;
    }

    public void setWksmj(String wksmj) {
        this.wksmj = wksmj;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int isCommon() {
        return isCommon;
    }

    public void setCommon(int common) {
        this.isCommon = common;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
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

    public String getTake_unit() {
        return take_unit;
    }

    public void setTake_unit(String take_unit) {
        this.take_unit = take_unit;
    }

    public String getTake_method() {
        return take_method;
    }

    public void setTake_method(String take_method) {
        this.take_method = take_method;
    }

    public String getTake_day() {
        return take_day;
    }

    public void setTake_day(String take_day) {
        this.take_day = take_day;
    }

    public String getDrug_unit() {
        return drug_unit;
    }

    public void setDrug_unit(String drug_unit) {
        this.drug_unit = drug_unit;
    }

    public int getIsCommon() {
        return isCommon;
    }

    public void setIsCommon(int isCommon) {
        this.isCommon = isCommon;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}