package com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/26 15:43
 */
//@StringDef({EditType.userName, EditType.good})
@Retention(RetentionPolicy.SOURCE)
public @interface EditType {
    public static final String userName = "userName";
    public static final String good = "good";
    public static final String intro = "intro";
    public static final String headImage = "imageUrl";
    public static final String birthday = "birthday";
    public static final String sex = "sex";
    public static final String hosp = "hosp";
    public static final String jobTitle = "jobTitle";
    public static final String depart = "depart";

}
