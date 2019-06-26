package com.xywy.askforexpert.model.im.group;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bobby on 16/6/17.
 * 群对象
 */
public class GroupBean {

    @PrimaryKey
    @SerializedName("groupid")
    private String groupId;
    @SerializedName("is_disturb")
    private int isDisturb;

    @SerializedName("groupname")
    private String contactName;

    @SerializedName("affiliations_count")
    private int count;

    @SerializedName("img")
    private String headUrl;

    @SerializedName("public")
    private int isPublic;
    private int membersonly;
    private int allowinvites;

    private int maxusers;
    @SerializedName("owner")
    private String owner;

    @SerializedName("owner_name")
    private String owner_name;

    private int status;

    @SerializedName("members")
    private List<ContactModel> memberList;

    @SerializedName("memberlist")
    @Ignore
    private List<ContactModel> memberListCopy;

    public void updateMemberList() {
        memberList = memberListCopy;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public int getMembersonly() {
        return membersonly;
    }

    public void setMembersonly(int membersonly) {
        this.membersonly = membersonly;
    }

    public int getAllowinvites() {
        return allowinvites;
    }

    public void setAllowinvites(int allowinvites) {
        this.allowinvites = allowinvites;
    }

    public int getMaxusers() {
        return maxusers;
    }

    public void setMaxusers(int maxusers) {
        this.maxusers = maxusers;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ContactModel> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<ContactModel> memberList) {
        if (this.memberList == null) {
            this.memberList = new RealmList<>();
        }
        this.memberList.clear();
        if (memberList != null && !memberList.isEmpty()) {
            this.memberList.addAll(memberList);
        }
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getIsDisturb() {
        return isDisturb;
    }

    public void setIsDisturb(int isDisturb) {
        this.isDisturb = isDisturb;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String img) {
        this.headUrl = img;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }
}
