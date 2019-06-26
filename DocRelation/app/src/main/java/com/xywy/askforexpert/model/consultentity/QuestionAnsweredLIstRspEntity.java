package com.xywy.askforexpert.model.consultentity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangzheng on 2017/5/9. 历史回复问题列表数据 stone
 */

public class QuestionAnsweredLIstRspEntity implements Serializable{

    /**
     * code : 10000
     * msg : success
     * data : [{"id":"483","uid":"68258077","created_time":"04-14 14:02:29","type":"3","patient_name":"李四35","patient_sex":"1","patient_age":"20","qid":"483","last_content":"浑身难受35","last_status":"2","last_time":"04-14 14:02:29","chat_id":"3913","is_read":0,"amount":"1.00","user_photo":"/im-static/images/18-40-1.png"},{"id":"482","uid":"68258077","created_time":"04-14 14:02:22","type":"3","patient_name":"李四34","patient_sex":"1","patient_age":"20","qid":"482","last_content":"浑身难受34","last_status":"2","last_time":"04-14 14:02:22","chat_id":"3912","is_read":0,"amount":"1.00","user_photo":"/im-static/images/18-40-1.png"},{"id":"481","uid":"68258077","created_time":"04-14 14:02:12","type":"3","patient_name":"李四33","patient_sex":"1","patient_age":"20","qid":"481","last_content":"浑身难受33","last_status":"2","last_time":"04-14 14:02:12","chat_id":"3911","is_read":0,"amount":"1.00","user_photo":"/im-static/images/18-40-1.png"},{"id":"480","uid":"68258077","created_time":"04-14 14:02:04","type":"3","patient_name":"李四32","patient_sex":"1","patient_age":"20","qid":"480","last_content":"浑身难受32","last_status":"2","last_time":"04-14 14:02:04","chat_id":"3910","is_read":0,"amount":"1.00","user_photo":"/im-static/images/18-40-1.png"},{"id":"479","uid":"68258077","created_time":"04-14 14:01:58","type":"3","patient_name":"李四31","patient_sex":"1","patient_age":"20","qid":"479","last_content":"浑身难受31","last_status":"2","last_time":"04-14 14:01:58","chat_id":"3909","is_read":0,"amount":"1.00","user_photo":"/im-static/images/18-40-1.png"},{"id":"478","uid":"68258077","created_time":"04-14 14:01:50","type":"3","patient_name":"李四30","patient_sex":"1","patient_age":"20","qid":"478","last_content":"浑身难受30","last_status":"2","last_time":"04-14 14:01:50","chat_id":"3908","is_read":0,"amount":"1.00","user_photo":"/im-static/images/18-40-1.png"},{"id":"477","uid":"68258077","created_time":"04-14 14:01:40","type":"3","patient_name":"李四29","patient_sex":"1","patient_age":"20","qid":"477","last_content":"浑身难受29","last_status":"2","last_time":"04-14 14:01:40","chat_id":"3907","is_read":0,"amount":"1.00","user_photo":"/im-static/images/18-40-1.png"},{"id":"476","uid":"68258077","created_time":"04-14 14:01:32","type":"3","patient_name":"李四28","patient_sex":"1","patient_age":"20","qid":"476","last_content":"浑身难受28","last_status":"2","last_time":"04-14 14:01:32","chat_id":"3906","is_read":0,"amount":"1.00","user_photo":"/im-static/images/18-40-1.png"},{"id":"452","uid":"68258077","created_time":"04-14 10:14:36","type":"3","patient_name":"李四5","patient_sex":"1","patient_age":"20","qid":"452","last_content":"浑身难受5","last_status":"2","last_time":"04-14 10:14:36","chat_id":"3882","is_read":0,"amount":"0.00","user_photo":"/im-static/images/18-40-1.png"},{"id":"451","uid":"68258077","created_time":"04-14 10:14:23","type":"3","patient_name":"李四3","patient_sex":"1","patient_age":"20","qid":"451","last_content":"浑身难受4","last_status":"2","last_time":"04-14 10:14:23","chat_id":"3881","is_read":0,"amount":"0.00","user_photo":"/im-static/images/18-40-1.png"}]
     */

    private int code;
    private String msg;
    /**
     * id : 483
     * uid : 68258077
     * created_time : 04-14 14:02:29
     * type : 3
     * patient_name : 李四35
     * patient_sex : 1
     * patient_age : 20
     * qid : 483
     * last_content : 浑身难受35
     * last_status : 2
     * last_time : 04-14 14:02:29
     * chat_id : 3913
     * is_read : 0
     * amount : 1.00
     * user_photo : /im-static/images/18-40-1.png
     */

    private List<IMQuestionBean> data;

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

    public List<IMQuestionBean> getData() {
        return data;
    }

    public void setData(List<IMQuestionBean> data) {
        this.data = data;
    }

}
