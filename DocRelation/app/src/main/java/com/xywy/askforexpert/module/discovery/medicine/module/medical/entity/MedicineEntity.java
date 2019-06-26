package com.xywy.askforexpert.module.discovery.medicine.module.medical.entity;

import java.io.Serializable;

/**
 * Created by bobby on 17/3/22.
 */

public class MedicineEntity implements Serializable {
    protected int id;
    private String name;
    protected String image;
    protected String gyssku;
    protected String spec;
    protected String company;
    protected int fczs;
    protected float scj;
    protected int stock;//库存数量
    protected int productId;
    private String specification;//规格
    private String wksmj;

    private String remark;//备注
    private int dayCount;//每天的数量
    private String dayCountDesc;//每天服用时间说明
    private int timeCount;//每次的数量
    private String timeCountDesc;//每次服用时间说明
    private int count; //总数量

    public String getWksmj() {
        return wksmj;
    }

    public void setWksmj(String wksmj) {
        this.wksmj = wksmj;
    }

    public String getUseage() {
        return useage;
    }

    public void setUseage(String useage) {
        this.useage = useage;
    }

    private String useage;//常用用法

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTakeMethod() {
        return this.takeMethod ;
    }

    public void setTakeMethod(String takeMethod) {
        this.takeMethod = takeMethod;
    }

    private String takeMethod;//服用方法

    public void setTimeCountDesc(String timeCountDesc) {
        this.timeCountDesc = timeCountDesc;
    }

    public String getTimeCountDesc(){
        return this.timeCountDesc;
    }

    public void setDayCountDesc(String dayCountDesc) {
        this.dayCountDesc = dayCountDesc;
    }

    public String getDayCountDesc(){
        return this.dayCountDesc == null ?"":this.dayCountDesc;
    }

    public void setCount(int count){
        this.count = count;
    }

    public int getCount(){
        return this.count;
    }

    public void setTimeCount(int timeCount){
        this.timeCount = timeCount;
    }

    public int getTimeCount(){
        return timeCount;
    }

    public void setDayCount(int dayCount){
        this.dayCount = dayCount;
    }

    public int getDayCount(){
        return dayCount;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }



    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGyssku() {
        return gyssku;
    }

    public void setGyssku(String gyssku) {
        this.gyssku = gyssku;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getWeight() {
        return fczs;
    }

    public void setWeight(int weight) {
        this.fczs = weight;
    }

    public float getPrice() {
        return scj;
    }

    public void setPrice(float price) {
        this.scj = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }


}
