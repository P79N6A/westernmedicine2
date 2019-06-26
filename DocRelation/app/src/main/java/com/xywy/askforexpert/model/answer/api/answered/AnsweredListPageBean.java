package com.xywy.askforexpert.model.answer.api.answered;

import com.xywy.askforexpert.model.api.BaseResultBean;

import java.util.List;

/**
 * 已答试卷列表页 对应 API bean
 * Created by bailiangjin on 16/5/6.
 */
public class AnsweredListPageBean extends BaseResultBean {

    /**
     * code : 10000
     * msg : 成功
     * userid : 10
     * data : [{"record_id":"14","paper_id":"3","answer_man_id":"10","create_time":"1462766810","ispass":"51","score":30,"paper_name":"2015年护理学（师）专业知识真题及详解","isdel":"51","paper_status":"1","wrong":{"paper_id":"3","answer_man_id":"10","paper_name":"2015年护理学（师）专业知识真题及详解 \u2014 错题集"}},{"record_id":"3","paper_id":"66","answer_man_id":"10","create_time":"1462440139","ispass":"-1","score":0,"paper_name":"2015年护士执业资格考试（实践能力）真题及详解","isdel":"51","paper_status":"1","wrong":{"paper_id":"66","answer_man_id":"10","paper_name":"2015年护士执业资格考试（实践能力）真题及详解 \u2014 错题集"}}]
     */
    private String userId;
    /**
     * record_id : 14
     * paper_id : 3
     * answer_man_id : 10
     * create_time : 1462766810
     * ispass : 51
     * score : 30
     * paper_name : 2015年护理学（师）专业知识真题及详解
     * isdel : 51
     * paper_status : 1
     * wrong : {"paper_id":"3","answer_man_id":"10","paper_name":"2015年护理学（师）专业知识真题及详解 \u2014 错题集"}
     */

    private List<AnsweredPaperBean> data;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<AnsweredPaperBean> getData() {
        return data;
    }

    public void setData(List<AnsweredPaperBean> data) {
        this.data = data;
    }


}
