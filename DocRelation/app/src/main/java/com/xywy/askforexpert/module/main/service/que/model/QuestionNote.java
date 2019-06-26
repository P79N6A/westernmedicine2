package com.xywy.askforexpert.module.main.service.que.model;

import com.xywy.askforexpert.model.consultentity.CommentBean;

import java.io.Serializable;

/**
 * 问题广场，帖子实例 stone
 */

public class QuestionNote implements Serializable {
    /**
     * id : 295915594
     * qid : 146094713
     * uid : 13048046
     * intime : 1523585962
     * rep_tag : waitreply
     * status : 2
     * agree_count : 0
     * title : 体检报告说 谷丙转氨酶高 我经常熬夜会有
     * age : 27岁
     * sex : 男
     * createtime : 10:19:22
     * ques_from : pay_ques
     * givepoint : 36
     * con : 病情描述（发病时间、主要症状、症状变化等）：体检报告说 谷丙转氨酶高 我经常熬夜会有影响吗？是否去医院就诊过：没去过当前服药说明：没有服药既往病史及手术史：没有
     * nickname : xywy134284788
     * level : 8        需要修复的回复，level的值为8时，表示医生的回复需要修改
     * time : 17:46
     * subjectname : 体检
     * q_type : 1
     */

    public String id;
    public String qid;
    public String uid;
    public String intime;
    public String rep_tag;
    public String status;
    public String agree_count;
    public String title;
    public String age;
    public String sex;
    public String createtime;
    public String ques_from;
    public String givepoint;
    public String con;
    public String nickname;
    public String time;
    public String subjectname;
    public String q_type;//问题类别   1 指定付费 2 悬赏  3绩效
    public String level;//需要修复的回复，level的值为8时，表示医生的回复需要修改


    /**
     * 评价 stone 新添加的
     */
    public CommentBean grade;
    /**
     * 问题状态类别  ques 跳到回复 zhuiwen 跳到追问
     */
    public String type;

    public String money;



}
