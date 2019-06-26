package com.xywy.askforexpert.model;

/**
 * 类描述:
 * 创建人: shihao on 15/12/22 19:26.
 */
public class DcUser {

    /**
     * "userid": "87170554",
     "nickname": "内科病历研讨班",
     "realname": "内科病历研讨班",
     "subject": "医脉官方账号",
     "job": "副主任医师",
     "hospital": "骨科病历",
     "usertype": "seminar",
     "synopsis": "病历研讨班描述",
     "photo": "http://static.img.xywy.com/club/niming/xlzxs.png"
     */

    /**
     * 用户id
     */
    public String id;
    /**
     * 用户昵称
     */
    public String nickname;
    /**
     * 用户真实名字
     */
    public String realname;
    /**
     * 头像
     */
    public String photo;
    /**
     * 所属
     */
    public String subject;
    /**
     * 工作
     */
    public String job;
    /**
     * 所在医院
     */
    public String hospital;
    /**
     * 用户类型
     */
    public String usertype;
    /**
     * 用户动态
     */
    public String synopsis;
}
