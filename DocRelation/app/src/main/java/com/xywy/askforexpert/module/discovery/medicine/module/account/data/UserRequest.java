package com.xywy.askforexpert.module.discovery.medicine.module.account.data;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/22 16:14
 */

public class UserRequest{

    public static IUserRequest instance = new UserRequestServerImpl();
    public static IUserRequest getInstance() {
        return instance;
    }
}
