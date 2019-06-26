package com.xywy.askforexpert.model;

import java.io.Serializable;

/**
 * <p>
 * 描述：家庭医生签单列表item对象描述
 * </p>
 *
 * @author liuxuejiao
 * @2015-05-14 下午15:26:20
 */
public class SignOrderBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4234647666117154149L;
    /**
     * docname: 医生的姓名
     */
    private String docname;
    /**
     * docid: 医生ID
     */
    private String docid;
    /**
     * amount: 订单金额
     */
    private String amount;
    /**
     * photo: 医生头像
     */
    private String photo;
    /**
     * score: 医生积分
     */
    private String score;
    /**
     * category: 服务类型【1 包周，2 包月，3 免费，4 折扣】
     */
    private String category;
    /**
     * 签约订单时间
     */
    private String orederTime;

    public SignOrderBean() {
        super();
    }

    public SignOrderBean(String docname, String docid, String amount,
                         String photo, String score, String category) {
        super();
        this.docname = docname;
        this.docid = docid;
        this.amount = amount;
        this.photo = photo;
        this.score = score;
        this.category = category;
    }

    public String getDocname() {
        return docname;
    }

    public void setDocname(String docname) {
        this.docname = docname;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOrederTime() {
        return orederTime;
    }

    public void setOrederTime(String orederTime) {
        this.orederTime = orederTime;
    }


}
