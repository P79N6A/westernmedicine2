package com.xywy.askforexpert.module.discovery.medicine.module.account.beans;

import com.google.gson.Gson;
import com.xywy.askforexpert.module.discovery.medicine.common.MyConstant;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.KeyValueGroup;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.KeyValueNode;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.PersonCard;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.util.DataParserUtil;
import com.xywy.retrofit.demo.BaseRetrofitResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/22 16:59
 * 服务器接口字段未确定，使用本类作为抽象数据层，供程序使用，对服务器返回字段进行包装处理。
 */

public class UserBean {
    //本地字段
    private String password;
    private String userAccount;
    private List<String> idImages = new ArrayList<>();
    private List<String> jobImages = new ArrayList<>();
    private KeyValueNode mMajorSecond = new KeyValueNode();
    private KeyValueNode mMajor = new KeyValueNode();
    private KeyValueNode mHosp = new KeyValueNode();
    private KeyValueNode mGender = new KeyValueNode();
    private KeyValueNode mJobTitle = new KeyValueNode();
    private KeyValueNode mState = new KeyValueNode();
    //服务器字段
//    private LoginServerBean loginServerBean = new LoginServerBean();
    private PersonCard personServerBean = new PersonCard();

    public PersonCard getPersonServerBean() {
        return personServerBean;
    }

    public void setPersonServerBean(PersonCard personServerBean) {
        this.personServerBean = personServerBean;
    }

    public KeyValueNode getHosp() {
        return mHosp;
    }

    public void setHosp(KeyValueNode hosp) {
        this.mHosp = hosp;
        getLoginServerBean().setHos_name(hosp.getName());
    }

    public KeyValueNode getMajorSecond() {
        return mMajorSecond;
    }

    public void setMajorSecond(KeyValueNode majorSecond) {
        this.mMajorSecond = majorSecond;
        getLoginServerBean().setMajor_second(majorSecond.getId());
    }

    public KeyValueNode getMajor() {
        return mMajor;
    }

    public void setMajor(KeyValueNode major) {
        this.mMajor = major;
        getLoginServerBean().setMajor_first(major.getId());
    }

    public KeyValueNode getJobTitle() {
        return mJobTitle;
    }

    public void setJobTitle(KeyValueNode jobTitle) {
        this.mJobTitle = jobTitle;
        getLoginServerBean().setClinic(jobTitle.getId());
    }

    public PersonCard getLoginServerBean() {
        return getPersonServerBean();
    }

//    public void setLoginServerBean(LoginServerBean loginServerBean) {
//        this.loginServerBean = loginServerBean;
//    }

    public void parse() {
        DataParserUtil.parseSex(new BaseRetrofitResponse<List<KeyValueGroup>>() {
            @Override
            public void onNext(List<KeyValueGroup> list) {
                for (KeyValueGroup key : list) {
                    if (key.getName().equals(getLoginServerBean().getGender())) {
                        mGender=key;
                    }
                }
            }
        });
        DataParserUtil.parseState(new BaseRetrofitResponse<List<KeyValueGroup>>() {
            @Override
            public void onNext(List<KeyValueGroup> list) {
                mState = DataParserUtil.find(getLoginServerBean().getWkys(), list);
            }
        });
        DataParserUtil.parseJobTitle(new BaseRetrofitResponse<List<KeyValueGroup>>() {
            @Override
            public void onNext(List<KeyValueGroup> list) {
                mJobTitle = DataParserUtil.find(getLoginServerBean().getClinic(), list);
            }
        });
        mHosp = new KeyValueNode(MyConstant.NOT_DEFINED, getLoginServerBean().getHos_name());
        mMajor = new KeyValueNode(MyConstant.NOT_DEFINED, getLoginServerBean().getMajor_first());
        mMajorSecond = new KeyValueNode(MyConstant.NOT_DEFINED, getLoginServerBean().getMajor_second());
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public KeyValueNode getState() {
//        return  new KeyValueNode(UserBean.checking,"ceshi");
        DataParserUtil.parseState(new BaseRetrofitResponse<List<KeyValueGroup>>() {
            @Override
            public void onNext(List<KeyValueGroup> list) {
                mState = DataParserUtil.find(getLoginServerBean().getWkys(), list);
            }
        });
        return mState;
    }
public static class UserState {

    //认证状态常量
    public static final String passed="1";
    public static final String checking="0";
    public static final String failed="-2";
    public static final String unverified="-1";
}

    public String getBe_good_at() {
        return getLoginServerBean().getBe_good_at();
    }

    public String getId() {
        return getLoginServerBean().getUser_id();
    }

    public KeyValueNode getGender() {
        return mGender;
    }

    public void setGender(KeyValueNode gender) {
        this.mGender = gender;
    }

    public String getPhoto() {
        return personServerBean != null ? getPersonServerBean().getPhoto() : null;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getHx_username() {
        return getLoginServerBean().getHx_username();
    }

    public String getHx_password() {
        return getLoginServerBean().getHx_password();
    }


    public List<String> getIdImages() {
        return idImages;
    }

    public void setIdImages(List<String> idImages) {
        this.idImages = idImages;
    }

    public List<String> getJobImages() {
        return jobImages;
    }

    public void setJobImages(List<String> jobImages) {
        this.jobImages = jobImages;
    }


    public UserBean copy() {
        //克隆对象
        Gson gson = new Gson();
        String temp = gson.toJson(this);
        UserBean bean = gson.fromJson(temp, UserBean.class);
        return bean;
    }
}
