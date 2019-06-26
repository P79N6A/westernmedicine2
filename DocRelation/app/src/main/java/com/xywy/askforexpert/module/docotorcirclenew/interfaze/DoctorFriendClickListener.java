package com.xywy.askforexpert.module.docotorcirclenew.interfaze;

import android.app.Activity;

import com.xywy.askforexpert.module.docotorcirclenew.requestbean.DoctorInfoParam;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.InterestPersonParamBean;

/**
 *
 * 医生朋友 相关点击监听接口
 * Created by bailiangjin on 2016/11/3.
 */
public interface DoctorFriendClickListener {

    /**
     * 用户名点击事件
     */
    void onNameClick(Activity activity, DoctorInfoParam doctorInfoParam);

    /**
     * 用户头像点击事件
     */
    void onHeadImgClick(Activity activity, DoctorInfoParam doctorInfoParam);


    /**
     * +好友 按钮
     *
     */
    public void onAddFriendClick(Activity activity, String userId);

    /**
     * 查看更多 按钮 点击事件
     */
    public void onMoreFriendClick(Activity activity, InterestPersonParamBean paramBean);


    /**
     * 查看了我的人 更多点击数事件
     */
    public void onPeopleSeeMeClick(Activity activity);



}
