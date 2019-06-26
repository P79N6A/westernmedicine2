package com.xywy.askforexpert.model.consultentity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangzheng on 2017/4/26. 处理中问题列表数据 stone
 */

public class QuestionInHandleRspEntity  implements Serializable {


    /**
     * code : 10000
     * msg : success
     * data : {"no_read_total":0,"list":[{"content":"绁炵粡琛板急鎬庝箞鍔�4","created_time":"05-03 11:57:53","uid":"68258070","is_read":1,"patient_name":"鏉庡洓","patient_sex":"1","patient_age":"20","qid":"884","chat_id":"5147","user_photo":"http://test.d.xywy.com/im-static/images/18-40-1.png","type":"1","amount":"0.00"},{"content":"绁炵粡琛板急鎬庝箞鍔�3","created_time":"05-03 11:57:57","uid":"68258070","is_read":1,"patient_name":"鏉庡洓","patient_sex":"1","patient_age":"20","qid":"883","chat_id":"5146","user_photo":"http://test.d.xywy.com/im-static/images/18-40-1.png","type":"1","amount":"0.00"},{"content":"绁炵粡琛板急鎬庝箞鍔�2","created_time":"05-03 11:58:01","uid":"68258070","is_read":1,"patient_name":"鏉庡洓","patient_sex":"1","patient_age":"20","qid":"882","chat_id":"5145","user_photo":"http://test.d.xywy.com/im-static/images/18-40-1.png","type":"1","amount":"0.00"},{"content":"鎰熷啋鎬庝箞鍔�0","created_time":"05-03 11:58:11","uid":"68258070","is_read":1,"patient_name":"鏉庡洓","patient_sex":"1","patient_age":"20","qid":"880","chat_id":"5143","user_photo":"http://test.d.xywy.com/im-static/images/18-40-1.png","type":"1","amount":"0.00"},{"content":"鎰熷啋鎬庝箞鍔�1","created_time":"05-03 11:58:18","uid":"68258070","is_read":1,"patient_name":"鏉庡洓","patient_sex":"1","patient_age":"20","qid":"881","chat_id":"5144","user_photo":"http://test.d.xywy.com/im-static/images/18-40-1.png","type":"1","amount":"0.00"}]}
     */

    private int code;
    private String msg;
    /**
     * no_read_total : 0
     * list : [{"content":"绁炵粡琛板急鎬庝箞鍔�4","created_time":"05-03 11:57:53","uid":"68258070","is_read":1,"patient_name":"鏉庡洓","patient_sex":"1","patient_age":"20","qid":"884","chat_id":"5147","user_photo":"http://test.d.xywy.com/im-static/images/18-40-1.png","type":"1","amount":"0.00"},{"content":"绁炵粡琛板急鎬庝箞鍔�3","created_time":"05-03 11:57:57","uid":"68258070","is_read":1,"patient_name":"鏉庡洓","patient_sex":"1","patient_age":"20","qid":"883","chat_id":"5146","user_photo":"http://test.d.xywy.com/im-static/images/18-40-1.png","type":"1","amount":"0.00"},{"content":"绁炵粡琛板急鎬庝箞鍔�2","created_time":"05-03 11:58:01","uid":"68258070","is_read":1,"patient_name":"鏉庡洓","patient_sex":"1","patient_age":"20","qid":"882","chat_id":"5145","user_photo":"http://test.d.xywy.com/im-static/images/18-40-1.png","type":"1","amount":"0.00"},{"content":"鎰熷啋鎬庝箞鍔�0","created_time":"05-03 11:58:11","uid":"68258070","is_read":1,"patient_name":"鏉庡洓","patient_sex":"1","patient_age":"20","qid":"880","chat_id":"5143","user_photo":"http://test.d.xywy.com/im-static/images/18-40-1.png","type":"1","amount":"0.00"},{"content":"鎰熷啋鎬庝箞鍔�1","created_time":"05-03 11:58:18","uid":"68258070","is_read":1,"patient_name":"鏉庡洓","patient_sex":"1","patient_age":"20","qid":"881","chat_id":"5144","user_photo":"http://test.d.xywy.com/im-static/images/18-40-1.png","type":"1","amount":"0.00"}]
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

    public static class DataBean  implements Serializable{
        private int no_read_total;
        private String total;
        /**
         * content : 绁炵粡琛板急鎬庝箞鍔�4
         * created_time : 05-03 11:57:53
         * uid : 68258070
         * is_read : 1
         * patient_name : 鏉庡洓
         * patient_sex : 1
         * patient_age : 20
         * qid : 884
         * chat_id : 5147
         * user_photo : http://test.d.xywy.com/im-static/images/18-40-1.png
         * type : 1
         * amount : 0.00
         */

        private List<IMQuestionBean> list;

        public int getNo_read_total() {
            return no_read_total;
        }

        public void setNo_read_total(int no_read_total) {
            this.no_read_total = no_read_total;
        }

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
