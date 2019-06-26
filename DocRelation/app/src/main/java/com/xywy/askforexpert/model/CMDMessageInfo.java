package com.xywy.askforexpert.model;

import java.util.List;

/**
 * Created by wangpeng on 16/7/20.
 * describ：
 * revise：
 */
public class CMDMessageInfo {


    /**
     * groupName : 测试群
     * groupid : 215639961376915900
     * owner : 56008997
     * headUrl :
     * ownername : 张三
     * inviteNumber : [{"username":"name1","userid":1122},{"username":"name2","userid":1123}]
     * deleteNumber : [{"username":"name1","userid":1122},{"username":"name2","userid":1123}]
     */

    private String groupName;
    private String groupid;
    private String owner;
    private String headUrl;
    private String ownername;
    /**
     * username : name1
     * userid : 1122
     */

    private List<NumberEntity> inviteNumber;
    /**
     * username : name1
     * userid : 1122
     */

    private List<NumberEntity> deleteNumber;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public List<NumberEntity> getInviteNumber() {
        return inviteNumber;
    }

    public void setInviteNumber(List<NumberEntity> inviteNumber) {
        this.inviteNumber = inviteNumber;
    }

    public List<NumberEntity> getDeleteNumber() {
        return deleteNumber;
    }

    public void setDeleteNumber(List<NumberEntity> deleteNumber) {
        this.deleteNumber = deleteNumber;
    }

    public static class NumberEntity {
        private String username;
        private int userid;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }
    }

}
