package com.xywy.askforexpert.model.answer.api.set;

import com.xywy.askforexpert.model.answer.api.ShareBaseBean;

import java.util.List;

/**
 * Created by bailiangjin on 16/5/5.
 */
public class SetPageBean extends ShareBaseBean {


    /**
     * userid : 10
     * data : [{"class_id":"2","class_name":"护士执业资格考试","pid":"1","class_level":"2","isdel":"51","image":"http://static.img.xywy.com/doc_connection/images/ym_test03.png"},{"class_id":"3","class_name":"护师资格考试","pid":"1","class_level":"2","isdel":"51","image":"http://static.img.xywy.com/doc_connection/images/ym_test04.png"},{"class_id":"5","class_name":"主管护师（内科护理学）专业技术资格考试","pid":"1","class_level":"2","isdel":"51","image":"http://static.img.xywy.com/doc_connection/images/ym_test01.png"},{"class_id":"7","class_name":"主管护师（妇产科护理学）专业技术资格考试","pid":"1","class_level":"2","isdel":"51","image":"http://static.img.xywy.com/doc_connection/images/ym_test02.png"},{"class_id":"4","class_name":"主管护师（护理学）专业技术资格考试","pid":"1","class_level":"2","isdel":"51","image":"http://static.img.xywy.com/doc_connection/images/ym_test05.png"},{"class_id":"6","class_name":"主管护师（外科护理学）专业技术资格考试","pid":"1","class_level":"2","isdel":"51","image":"http://static.img.xywy.com/doc_connection/images/ym_test06.png"},{"class_id":"8","class_name":"主管护师（儿科护理学）专业技术资格考试","pid":"1","class_level":"2","isdel":"51","image":"http://static.img.xywy.com/doc_connection/images/ym_test07.png"},{"class_id":"9","class_name":"主管护师（社区护理学）专业技术资格考试","pid":"1","class_level":"2","isdel":"51","image":"http://static.img.xywy.com/doc_connection/images/ym_test08.png"}]
     */

    private String userId;
    /**
     * class_id : 2
     * class_name : 护士执业资格考试
     * pid : 1
     * class_level : 2
     * isdel : 51
     * image : http://static.img.xywy.com/doc_connection/images/ym_test03.png
     */

    private List<SetBean> data;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<SetBean> getData() {
        return data;
    }

    public void setData(List<SetBean> data) {
        this.data = data;
    }


}
