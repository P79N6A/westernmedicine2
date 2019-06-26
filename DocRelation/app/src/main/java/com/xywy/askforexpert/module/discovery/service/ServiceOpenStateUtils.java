package com.xywy.askforexpert.module.discovery.service;

import com.xywy.askforexpert.YMApplication;

/**
 * Created by bailiangjin on 2016/12/30.
 */

public class ServiceOpenStateUtils {

    public static String getQuestionSquareOpenState() {
        return YMApplication.dati_map().get(YMApplication.getLoginInfo().getData().getXiaozhan().getDati());
    }

    //问题广场，指定付费
    public static String getQuestionSquareOpenStateAssign() {
        return YMApplication.dati_map().get(YMApplication.getLoginInfo().getData().getXiaozhan().getClub_assign()+"");
    }

    public static String getImwdOpenState() {
        return YMApplication.imwd_map().get(YMApplication.getLoginInfo().getData().getImwd()+"");
    }

    //即时问答(指定付费)
    public static String getImwdOpenStateAssign() {
        return YMApplication.imwd_map().get(YMApplication.getLoginInfo().getData().getXiaozhan().getImwd_assign()+"");
    }

    //即时问答(悬赏)
    public static String getImwdOpenStateReward() {
        return YMApplication.imwd_map().get(YMApplication.getLoginInfo().getData().getXiaozhan().getImwd_reward()+"");
    }

    public static String getCallDoctorOpenState() {
        return YMApplication.phone_map().get(
                YMApplication.getLoginInfo().getData().getXiaozhan().getPhone());
    }

    public static String getTransferTreatmentOpenState() {
        return YMApplication.yuyue_map().get(
                YMApplication.getLoginInfo().getData().getXiaozhan().getYuyue());
    }

    public static String getHomeDoctorOpenState() {
        return YMApplication.fam_map().get(
                YMApplication.getLoginInfo().getData().getXiaozhan().getFamily());
    }


}
