package com.xywy.askforexpert.model;

import java.io.Serializable;
import java.util.List;

/**
 * 问答消息bean
 *
 * @author LG
 */
public class QuestionBean implements Serializable {

    public String code;
    public String msg;
    public Data data;

    public class Data implements Serializable {

        public String more;
        public List<DataList> data;

        public class DataList implements Serializable {
            public String title;
            public String content;
            public String createtime;
            public String id;
        }
    }
}
