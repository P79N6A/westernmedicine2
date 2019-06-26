package com.xywy.askforexpert.model.answer.api.paperlist;

import com.google.gson.annotations.SerializedName;
import com.xywy.askforexpert.model.answer.api.ShareBaseBean;

import java.util.List;

/**
 * 试卷列表页 API Bean
 * Created by bailiangjin on 16/5/5.
 */
public class PaperListPageBean extends ShareBaseBean {


    /**
     * code : 10000
     * msg : 成功
     * userid : 10
     * class : [{"class_id":"22","class_name":"专业务实","pid":"2","class_level":"3","isdel":"51","data":[{"paper_id":"65","paper_name":"2015年护士执业资格考试（专业实务）真题及详解","class_id":"22","pass_score":0,"total_score":120,"create_time":"1461294198","update_time":"0","paper_status":"1","isdel":"51","audit_man_id":"0"},{"paper_id":"67","paper_name":"2014年护士执业资格考试（专业实务）真题及详解","class_id":"22","pass_score":0,"total_score":135,"create_time":"1461294198","update_time":"1461744839","paper_status":"1","isdel":"51","audit_man_id":"0"},{"paper_id":"69","paper_name":"2013年护士执业资格考试（专业实务）真题及详解","class_id":"22","pass_score":91,"total_score":135,"create_time":"1461294198","update_time":"1462237481","paper_status":"1","isdel":"51","audit_man_id":"0"},{"paper_id":"71","paper_name":"2012年护士执业资格考试（专业实务）真题及详解","class_id":"22","pass_score":0,"total_score":136,"create_time":"1461294198","update_time":"0","paper_status":"1","isdel":"51","audit_man_id":"0"},{"paper_id":"73","paper_name":"2011年护士执业资格考试（专业实务）真题及详解","class_id":"22","pass_score":0,"total_score":135,"create_time":"1461294198","update_time":"1461820342","paper_status":"1","isdel":"51","audit_man_id":"0"},{"paper_id":"75","paper_name":"2016年护士执业资格考试（专业实务）模拟试题及答案","class_id":"22","pass_score":0,"total_score":141,"create_time":"1461294198","update_time":"0","paper_status":"1","isdel":"51","audit_man_id":"0"}]},{"class_id":"23","class_name":"实践能力","pid":"2","class_level":"3","isdel":"51","data":[{"paper_id":"66","paper_name":"2015年护士执业资格考试（实践能力）真题及详解","class_id":"23","pass_score":0,"total_score":120,"create_time":"1461294198","update_time":"0","paper_status":"1","isdel":"51","audit_man_id":"0"},{"paper_id":"68","paper_name":"2014年护士执业资格考试（实践能力）真题及详解","class_id":"23","pass_score":0,"total_score":135,"create_time":"1461294198","update_time":"0","paper_status":"1","isdel":"51","audit_man_id":"0"},{"paper_id":"70","paper_name":"2013年护士执业资格考试（实践能力）真题及详解","class_id":"23","pass_score":0,"total_score":135,"create_time":"1461294198","update_time":"0","paper_status":"1","isdel":"51","audit_man_id":"0"},{"paper_id":"72","paper_name":"2012年护士执业资格考试（实践能力）真题及详解","class_id":"23","pass_score":0,"total_score":136,"create_time":"1461294198","update_time":"0","paper_status":"1","isdel":"51","audit_man_id":"0"},{"paper_id":"74","paper_name":"2011年护士执业资格考试（实践能力）真题及详解","class_id":"23","pass_score":0,"total_score":135,"create_time":"1461294198","update_time":"0","paper_status":"1","isdel":"51","audit_man_id":"0"},{"paper_id":"76","paper_name":"2016年护士执业资格考试（实践能力）模拟试题及答案","class_id":"23","pass_score":0,"total_score":150,"create_time":"1461294198","update_time":"0","paper_status":"1","isdel":"51","audit_man_id":"0"}]}]
     */
    private String userid;
    /**
     * class_id : 22
     * class_name : 专业务实
     * pid : 2
     * class_level : 3
     * isdel : 51
     * data : [{"paper_id":"65","paper_name":"2015年护士执业资格考试（专业实务）真题及详解","class_id":"22","pass_score":0,"total_score":120,"create_time":"1461294198","update_time":"0","paper_status":"1","isdel":"51","audit_man_id":"0"},{"paper_id":"67","paper_name":"2014年护士执业资格考试（专业实务）真题及详解","class_id":"22","pass_score":0,"total_score":135,"create_time":"1461294198","update_time":"1461744839","paper_status":"1","isdel":"51","audit_man_id":"0"},{"paper_id":"69","paper_name":"2013年护士执业资格考试（专业实务）真题及详解","class_id":"22","pass_score":91,"total_score":135,"create_time":"1461294198","update_time":"1462237481","paper_status":"1","isdel":"51","audit_man_id":"0"},{"paper_id":"71","paper_name":"2012年护士执业资格考试（专业实务）真题及详解","class_id":"22","pass_score":0,"total_score":136,"create_time":"1461294198","update_time":"0","paper_status":"1","isdel":"51","audit_man_id":"0"},{"paper_id":"73","paper_name":"2011年护士执业资格考试（专业实务）真题及详解","class_id":"22","pass_score":0,"total_score":135,"create_time":"1461294198","update_time":"1461820342","paper_status":"1","isdel":"51","audit_man_id":"0"},{"paper_id":"75","paper_name":"2016年护士执业资格考试（专业实务）模拟试题及答案","class_id":"22","pass_score":0,"total_score":141,"create_time":"1461294198","update_time":"0","paper_status":"1","isdel":"51","audit_man_id":"0"}]
     */

    @SerializedName("class")
    private List<PaperListGroupBean> classX;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public List<PaperListGroupBean> getClassX() {
        return classX;
    }

    public void setClassX(List<PaperListGroupBean> classX) {
        this.classX = classX;
    }


}
