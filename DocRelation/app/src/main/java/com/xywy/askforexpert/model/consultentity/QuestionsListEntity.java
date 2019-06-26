package com.xywy.askforexpert.model.consultentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhangzheng on 2017/4/27.
 */

public class QuestionsListEntity implements Serializable {

    public static final HashMap<String, String> sexMap;

    static {
        sexMap = new HashMap<>();
        sexMap.put("1", "男");
        sexMap.put("2", "女");
    }

    public static final String PAY_TYPE_FREE = "1";
    public static final String PAY_TYPE_RMB = "2";
    public static final String PAY_TYPE_RMB_SPECIFY = "3";
    public static final String PAY_TYPE_POINTS = "4";

    public static final String POINT_UNIT = "积分";
    public static final String RMB_UNIT = "元";
    public static final String FREE_TEXT = "免费";

    public static final int TYPE_CONSULT_ENTITY = 0xf0;
    public static final int TYPE_POOL_ENTITY = TYPE_CONSULT_ENTITY + 1;
    public static final int TYPE_ANSWERED_ENTITY = TYPE_POOL_ENTITY + 1;

    private int type = TYPE_CONSULT_ENTITY;

    private IMQuestionBean consultEntity;
    private IMQuestionBean questionPoolEntity;
    private IMQuestionBean answeredEntity;

    private String time;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public IMQuestionBean getConsultEntity() {
        return consultEntity;
    }

    public void setConsultEntity(IMQuestionBean consultEntity) {
        this.consultEntity = consultEntity;
    }

    public IMQuestionBean getQuestionPoolEntity() {
        return questionPoolEntity;
    }

    public void setQuestionPoolEntity(IMQuestionBean questionPoolEntity) {
        this.questionPoolEntity = questionPoolEntity;
    }

    public IMQuestionBean getAnsweredEntity() {
        return answeredEntity;
    }

    public void setAnsweredEntity(IMQuestionBean answeredEntity) {
        this.answeredEntity = answeredEntity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static List<QuestionsListEntity> getList(QuestionInHandleRspEntity data) {
        if (data == null || data.getData() == null || data.getData().getList() == null) {
            return new ArrayList<>();
        } else {
            List<IMQuestionBean> beanList = data.getData().getList();
            int size = beanList.size();
            List<QuestionsListEntity> list = new ArrayList<>();
            QuestionsListEntity entity;
            for (int i = 0; i < size; i++) {
                entity = new QuestionsListEntity();
                entity.setConsultEntity(beanList.get(i));
                entity.setTime(beanList.get(i).getCreated_time());
                entity.setType(QuestionsListEntity.TYPE_CONSULT_ENTITY);
                list.add(entity);
            }
            // Collections.sort(list, new sortByTime());
            Collections.reverse(list);
            return list;
        }
    }

    public static List<QuestionsListEntity> getList(QuestionsPoolRspEntity data) {
        if (data == null || data.getData() == null) {
            return new ArrayList<>();
        } else {
            List<IMQuestionBean> beanList = data.getData().getList();
            int size = beanList.size();
            List<QuestionsListEntity> list = new ArrayList<>();
            QuestionsListEntity entity;
            for (int i = 0; i < size; i++) {
                entity = new QuestionsListEntity();
                entity.setQuestionPoolEntity(beanList.get(i));
                entity.setTime(beanList.get(i).getCreated_time());
                entity.setType(QuestionsListEntity.TYPE_POOL_ENTITY);
                list.add(entity);
            }
            //Collections.sort(list, new sortByTime());
            return list;
        }
    }

    public static List<QuestionsListEntity> getList(QuestionAnsweredLIstRspEntity data) {
        if (data == null || data.getData() == null) {
            return new ArrayList<>();
        } else {
            List<IMQuestionBean> beanList = data.getData();
            int size = beanList.size();
            List<QuestionsListEntity> list = new ArrayList<>();
            QuestionsListEntity entity;
            for (int i = 0; i < size; i++) {
                entity = new QuestionsListEntity();
                entity.setAnsweredEntity(beanList.get(i));
                entity.setTime(beanList.get(i).getLast_time());
                entity.setType(QuestionsListEntity.TYPE_ANSWERED_ENTITY);
                list.add(entity);
            }
            // Collections.sort(list, new sortByTime());
            return list;
        }
    }

    public static class sortByTime implements Comparator<QuestionsListEntity> {

        @Override
        public int compare(QuestionsListEntity lhs, QuestionsListEntity rhs) {
            if (lhs == null || lhs.getTime() == null || rhs == null || rhs.getTime() == null) {
                return 0;
            } else {
                return -lhs.getTime().compareTo(rhs.getTime());
            }
        }
    }
}
