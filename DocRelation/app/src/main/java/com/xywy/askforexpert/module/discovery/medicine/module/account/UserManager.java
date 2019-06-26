package com.xywy.askforexpert.module.discovery.medicine.module.account;

import com.xywy.askforexpert.module.discovery.medicine.module.account.beans.UserBean;
import com.xywy.util.ContextUtil;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/23 17:54
 */

public class UserManager {
    private static IUserManager userManagerImpl = new UserManagerImpl(ContextUtil.getAppContext());
    public static IUserManager<UserBean> getInstance() {
        return userManagerImpl;
    }
}
