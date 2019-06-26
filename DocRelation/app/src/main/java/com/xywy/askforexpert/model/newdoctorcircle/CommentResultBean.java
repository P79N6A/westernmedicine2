package com.xywy.askforexpert.model.newdoctorcircle;

import com.google.gson.annotations.SerializedName;
import com.xywy.askforexpert.model.api.BaseStringResultBean;

/**
 *
 * 评论 返回结果Bean
 * Created by bailiangjin on 2016/11/4.
 */
public class CommentResultBean extends BaseStringResultBean {

    @SerializedName("commentid")
    private String commentId;


    private String commentStr;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }


    public String getCommentStr() {
        return commentStr;
    }

    public void setCommentStr(String commentStr) {
        this.commentStr = commentStr;
    }
}
