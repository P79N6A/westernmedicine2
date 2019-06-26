package com.xywy.askforexpert.module.discovery.medicine.module.account;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/30 14:36
 */
@Retention(RetentionPolicy.SOURCE)
public @interface SendTextMsgType {
    public static String project_value = "APP_WKYYAPP";
    public static String project_value_update_pwd = "APP_WKYYAPP_UPWD";
    public static String project_value_getback_pwd = "APP_WKYYAPP_GPWD";
    public static String project_value_login = "APP_WKYYAPP_LOGIN";
    String project_value_login_code = "WKYPZS_DYNAMIC_PASS";
}
