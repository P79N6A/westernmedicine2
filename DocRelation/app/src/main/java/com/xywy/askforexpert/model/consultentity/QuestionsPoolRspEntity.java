package com.xywy.askforexpert.model.consultentity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangzheng on 2017/4/27. 问题库问题列表数据 stone
 */

public class QuestionsPoolRspEntity implements Serializable {


    /**
     * code : 10000
     * msg : success
     * data : {"list":[{"qid":"893","uid":"68258070","user_photo":"/im-static/images/18-40-1.png","amount":"0.00","type":"1","content":"神经衰弱怎么办6","patient_name":"李四","patient_sex":"男","patient_age":"20","created_time":"05-04 09:05:18","add_time":"1493863880"},{"qid":"894","uid":"68258070","user_photo":"/im-static/images/18-40-1.png","amount":"0.00","type":"1","content":"神经衰弱怎么办5","patient_name":"李四","patient_sex":"男","patient_age":"20","created_time":"05-04 09:15:32","add_time":"1493863928"},{"qid":"896","uid":"68258070","user_photo":"/im-static/images/18-40-1.png","amount":"0.00","type":"1","content":"头疼","patient_name":"李四","patient_sex":"男","patient_age":"20","created_time":"05-04 09:21:19","add_time":"1493863999"},{"qid":"895","uid":"68258070","user_photo":"/im-static/images/18-40-1.png","amount":"0.00","type":"1","content":"头疼","patient_name":"李四","patient_sex":"男","patient_age":"20","created_time":"05-04 09:19:39","add_time":"1493864000"}],"total":"4"}
     */

    private int code;
    private String msg;
    /**
     * list : [{"qid":"893","uid":"68258070","user_photo":"/im-static/images/18-40-1.png","amount":"0.00","type":"1","content":"神经衰弱怎么办6","patient_name":"李四","patient_sex":"男","patient_age":"20","created_time":"05-04 09:05:18","add_time":"1493863880"},{"qid":"894","uid":"68258070","user_photo":"/im-static/images/18-40-1.png","amount":"0.00","type":"1","content":"神经衰弱怎么办5","patient_name":"李四","patient_sex":"男","patient_age":"20","created_time":"05-04 09:15:32","add_time":"1493863928"},{"qid":"896","uid":"68258070","user_photo":"/im-static/images/18-40-1.png","amount":"0.00","type":"1","content":"头疼","patient_name":"李四","patient_sex":"男","patient_age":"20","created_time":"05-04 09:21:19","add_time":"1493863999"},{"qid":"895","uid":"68258070","user_photo":"/im-static/images/18-40-1.png","amount":"0.00","type":"1","content":"头疼","patient_name":"李四","patient_sex":"男","patient_age":"20","created_time":"05-04 09:19:39","add_time":"1493864000"}]
     * total : 4
     */

    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        private String total;
        /**
         * qid : 893
         * uid : 68258070
         * user_photo : /im-static/images/18-40-1.png
         * amount : 0.00
         * type : 1
         * content : 神经衰弱怎么办6
         * patient_name : 李四
         * patient_sex : 男
         * patient_age : 20
         * created_time : 05-04 09:05:18
         * add_time : 1493863880
         */

        private List<IMQuestionBean> list;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public List<IMQuestionBean> getList() {
            return list;
        }

        public void setList(List<IMQuestionBean> list) {
            this.list = list;
        }

    }
}
