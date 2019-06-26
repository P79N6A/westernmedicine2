package com.xywy.askforexpert.model.consultentity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zhangzheng on 2017/4/28.
 */

public class OnlineConsultChatEntity {



    public static final int TYPE_ONLY_TEXT = 0;//文本
    public static final int TYPE_ONLY_IMG = TYPE_ONLY_TEXT + 1;//图片
    public static final int TYPE_QUESTION_DESC = TYPE_ONLY_IMG + 1;//详情
    public static final int TYPE_TIP = TYPE_QUESTION_DESC + 1;//意见
    public static final int TYPE_PATIENT_TITLE = TYPE_TIP + 1;//患者管理
    public static final int TYPE_REWARD = 5;//送心意 打赏 stone
    public static final int TYPE_CLOSE_NOTICE = 100;//关闭提示 新添加的  客户端自定义  stone

    public static final int MSG_TYPE_RECV = 0;
    public static final int MSG_TYPE_SEND = 1;
    public static final String RECV_TYPE = "2";
    public static final String SEND_TYPE = "1";

    public static final int SEND_STATE_IN_SENDING = 0;
    public static final int SEND_STATE_SUCCESS = 1;
    public static final int SEND_STATE_FAILED = 2;

    public static final String CONTENT_TYPE_REWARD = "reward";//打赏
    public static final String CONTENT_TYPE_TEXT = "text";
    public static final String CONTENT_TYPE_MEDIA = "media";


    private int msg_type;  //提问或回答
    private OnlineQuestionMsgListRspEntity.DataBean.ListBean dataBean;

    private String tipText;

    private int type;

    private String text;

    private List<String> imgUrls;

    private String time;

    private String imgUrl;

    private String title;

    private String desc;

    private String patientInfo;

    private int sendState = SEND_STATE_SUCCESS;

    private String sendId;

    private String patient_name; //患者姓名

    private String patient_sex; //患者性别

    private String patient_age; //患者年龄

    private String hospital; //是否就诊过 0未就诊 1就诊过

    private String top_title; //问题标题

    private String intent; //问题描述

    private String content; //询问目的

    private ArrayList<String> img; //患者问诊照片

    private String advice;
    private String revisit_time;
    private String visit_time;

    public String getRevisit_time() {
        return revisit_time;
    }

    public void setRevisit_time(String revisit_time) {
        this.revisit_time = revisit_time;
    }

    public String getVisit_time() {
        return visit_time;
    }

    public void setVisit_time(String visit_time) {
        this.visit_time = visit_time;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public String getAmount() {
        return amount;
    }


    public void setAmount(String amount) {
        this.amount = amount;
    }

    private String amount; //价格

    public double getSatisfied() {
        return satisfied;
    }

    public void setSatisfied(double satisfied) {
        this.satisfied = satisfied;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    private double satisfied = 0.0;
    private String comment;

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getPatient_sex() {
        return patient_sex;
    }

    public void setPatient_sex(String patient_sex) {
        this.patient_sex = patient_sex;
    }

    public String getPatient_age() {
        return patient_age;
    }

    public void setPatient_age(String patient_age) {
        this.patient_age = patient_age;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getTop_title() {
        return top_title;
    }

    public void setTop_title(String top_title) {
        this.top_title = top_title;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getImg() {
        return img;
    }

    public void setImg(ArrayList<String> img) {
        this.img = img;
    }

    public static OnlineConsultChatEntity newInstanceText(String text, int msgType) {
        OnlineConsultChatEntity consultChatEntity = new OnlineConsultChatEntity();
        consultChatEntity.setType(TYPE_ONLY_TEXT);
        consultChatEntity.setText(text);
        consultChatEntity.setTime(System.currentTimeMillis() + "");
        consultChatEntity.setMsg_type(msgType);
        return consultChatEntity;
    }

    public static OnlineConsultChatEntity newInstanceTip(String tip) {
        OnlineConsultChatEntity entity = new OnlineConsultChatEntity();
        entity.setType(TYPE_TIP);
        entity.setTipText(tip);
        entity.setTime(System.currentTimeMillis() + "");
        return entity;
    }

    public static OnlineConsultChatEntity newInstanceImgs(List<String> imgUrls) {
        OnlineConsultChatEntity entity = new OnlineConsultChatEntity();
        entity.setType(TYPE_ONLY_IMG);
        entity.setImgUrls(imgUrls);
        entity.setTime(System.currentTimeMillis() + "");
        return entity;
    }

    public static OnlineConsultChatEntity newInstaceDesc(String headImgUrl, String title, String patientInfo, String desc) {
        OnlineConsultChatEntity entity = new OnlineConsultChatEntity();
        entity.setType(TYPE_QUESTION_DESC);
        entity.setImgUrl(headImgUrl);
        entity.setTitle(title);
        entity.setPatientInfo(patientInfo);
        entity.setDesc(desc);
        return entity;
    }

    public void setTitleData(OnlineQuestionMsgListRspEntity.DataBean titleDataBean) {
        type = TYPE_QUESTION_DESC;
        patient_name = titleDataBean.getPatient_name();
        patient_sex = titleDataBean.getPatient_sex();
        StringBuffer age = new StringBuffer();
        if (!titleDataBean.getPatient_age_year().equals("0")){
            age.append(titleDataBean.getPatient_age_year()+"岁");
        }
        if (!titleDataBean.getPatient_age_month().equals("0")){
            age.append(titleDataBean.getPatient_age_month()+"月");
        }
        if (!titleDataBean.getPatient_age_day().equals("0")){
            age.append(titleDataBean.getPatient_age_day()+"天");
        }
        patient_age = age.toString();
        hospital = titleDataBean.getHospital();
        top_title = titleDataBean.getTitle();
        intent = titleDataBean.getIntent();
        content = titleDataBean.getContent();
        img = titleDataBean.getImg();
        amount = titleDataBean.getAmount();
    }

    public void setPatientTitleData(OnlineQuestionMsgListRspEntity.DataBean titleDataBean) {
        type = TYPE_PATIENT_TITLE;
        patient_name = titleDataBean.getPatient_name();
        patient_sex = titleDataBean.getPatient_sex();
        patient_age = titleDataBean.getPatient_age_year()+"岁";
        hospital = titleDataBean.getHospital();
        top_title = titleDataBean.getTitle();
        intent = titleDataBean.getIntent();
        content = titleDataBean.getContent();
        img = titleDataBean.getImg();
        amount = titleDataBean.getAmount();
        advice = titleDataBean.getAdvice();
        revisit_time = titleDataBean.getRevisit_time();
        visit_time = titleDataBean.getVisit_time();
    }

    public void setClosedData(OnlineQuestionMsgListRspEntity.DataBean closedData) {
        comment = closedData.getComment().getContent();
        satisfied = closedData.getComment().getSatisfied();
        type = TYPE_TIP;
    }

    public void setDataBean(OnlineQuestionMsgListRspEntity.DataBean.ListBean dataBean) {
        this.dataBean = dataBean;
        init();
    }

    private void init() {
        if (dataBean != null && dataBean.getContent_type() != null && !"".equals(dataBean.getContent_type())) {
            //设置消息类型 添加打赏功能/送心意 stone
            if (CONTENT_TYPE_REWARD.equals(dataBean.getContent_type())) {
                this.type = TYPE_REWARD;
                text = dataBean.getContent().get(0).getAmount();
            } else if (CONTENT_TYPE_TEXT.equals(dataBean.getContent_type())) {
                this.type = TYPE_ONLY_TEXT;
                text = dataBean.getContent().get(0).getText();
            } else if (CONTENT_TYPE_MEDIA.equals(dataBean.getContent_type())) {
                this.type = TYPE_ONLY_IMG;
                if (imgUrls == null) {
                    imgUrls = new ArrayList<>();
                }
                imgUrls.clear();
                for (int i = 0; i < dataBean.getContent().size(); i++) {
                    imgUrls.add(dataBean.getContent().get(i).getUrl());
                }
            }
            if (dataBean.getQ_a() != null && !"".equals(dataBean.getQ_a())) {
                //发送或接受类型
                if (SEND_TYPE.equals(dataBean.getQ_a())) {
                    this.msg_type = MSG_TYPE_SEND;
                } else if (RECV_TYPE.equals(dataBean.getQ_a()))
                    this.msg_type = MSG_TYPE_RECV;
            }

            this.time = dataBean.getCreated_time();
        }
    }

    public static void sortList(List<OnlineConsultChatEntity> list) {
        Collections.sort(list, new SortComparator());
    }




    public static class SortComparator implements Comparator<OnlineConsultChatEntity> {

        @Override
        public int compare(OnlineConsultChatEntity lhs, OnlineConsultChatEntity rhs) {
            if (lhs == null || rhs == null || lhs.getTime() == null || rhs.getTime() == null) {
                return 0;
            } else {
                Long lhsTime = Long.valueOf(lhs.getTime());
                Long rhsTime = Long.valueOf(rhs.getTime());
                return lhsTime.compareTo(rhsTime);
            }
        }
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }


    public int getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public OnlineQuestionMsgListRspEntity.DataBean.ListBean getDataBean() {
        return dataBean;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public String getTipText() {
        return tipText;
    }

    public void setTipText(String tipText) {
        this.tipText = tipText;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(String patientInfo) {
        this.patientInfo = patientInfo;
    }

    public int getSendState() {
        return sendState;
    }

    public void setSendState(int sendState) {
        this.sendState = sendState;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }
}
