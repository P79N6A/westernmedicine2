package com.xywy.askforexpert.model.consultentity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zhangzheng on 2017/4/28.
 */

public class ConsultChatEntity {

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
    private QuestionMsgListRspEntity.DataBean.ListBean dataBean;

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
    private boolean titmeFlag = false;

    private ArrayList<Integer> width;

    private ArrayList<Integer> height;

    public boolean isTitmeFlag() {
        return titmeFlag;
    }

    public void setTitmeFlag(boolean titmeFlag) {
        this.titmeFlag = titmeFlag;
    }

    public ArrayList<Integer> getWidth() {
        return width;
    }

    public void setWidth(ArrayList<Integer> width) {
        this.width = width;
    }

    public ArrayList<Integer> getHeight() {
        return height;
    }

    public void setHeight(ArrayList<Integer> height) {
        this.height = height;
    }

    public static ConsultChatEntity newInstanceText(String text, int msgType) {
        ConsultChatEntity consultChatEntity = new ConsultChatEntity();
        consultChatEntity.setType(TYPE_ONLY_TEXT);
        consultChatEntity.setText(text);
        consultChatEntity.setTime(System.currentTimeMillis() + "");
        consultChatEntity.setMsg_type(msgType);
        return consultChatEntity;
    }

    public static ConsultChatEntity newInstanceTip(String tip) {
        ConsultChatEntity entity = new ConsultChatEntity();
        entity.setType(TYPE_TIP);
        entity.setTipText(tip);
        entity.setTime(System.currentTimeMillis() + "");
        return entity;
    }

    public static ConsultChatEntity newInstanceImgs(List<String> imgUrls) {
        ConsultChatEntity entity = new ConsultChatEntity();
        entity.setType(TYPE_ONLY_IMG);
        entity.setImgUrls(imgUrls);
        entity.setTime(System.currentTimeMillis() + "");
        return entity;
    }

    public static ConsultChatEntity newInstaceDesc(String headImgUrl, String title, String patientInfo, String desc) {
        ConsultChatEntity entity = new ConsultChatEntity();
        entity.setType(TYPE_QUESTION_DESC);
        entity.setImgUrl(headImgUrl);
        entity.setTitle(title);
        entity.setPatientInfo(patientInfo);
        entity.setDesc(desc);
        return entity;
    }


    public void setDataBean(QuestionMsgListRspEntity.DataBean.ListBean dataBean) {
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
//                    width.add(dataBean.getContent().get(i).getWidth());
//                    height.add(dataBean.getContent().get(i).getHeight());
                }
            }
            if (dataBean.getQ_a() != null && !"".equals(dataBean.getQ_a())) {
                //发送或接受类型
                if (SEND_TYPE.equals(dataBean.getQ_a())) {
                    this.msg_type = MSG_TYPE_SEND;
                } else if (RECV_TYPE.equals(dataBean.getQ_a()))
                    this.msg_type = MSG_TYPE_RECV;
            }

            this.time = Long.parseLong(dataBean.getCreated_time())*1000+"";
        }
    }

    public static void sortList(List<ConsultChatEntity> list) {
        Collections.sort(list, new SortComparator());
    }

    public static class SortComparator implements Comparator<ConsultChatEntity> {

        @Override
        public int compare(ConsultChatEntity lhs, ConsultChatEntity rhs) {
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

    public QuestionMsgListRspEntity.DataBean.ListBean getDataBean() {
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
