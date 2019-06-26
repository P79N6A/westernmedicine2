package com.xywy.askforexpert.model.push;

import java.io.Serializable;

/**
 * 推送返回数据 stone
 */
public class PushExtraBean implements Serializable{


    private int type;//推送类型
    private int qid;//问题id
    private int uid;//患者id
    private String qtype;//为3位指定付费 qtype   1:免费2：悬赏 3：指定付费
    private String waitType;//是新问题还是追问      值   zhuiwen  ques
    private String rid;//处理完成接口需要传的参数
    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getWaitType() {
        return waitType;
    }

    public void setWaitType(String waitType) {
        this.waitType = waitType;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getQtype() {
        return qtype;
    }

    public void setQtype(String qtype) {
        this.qtype = qtype;
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
