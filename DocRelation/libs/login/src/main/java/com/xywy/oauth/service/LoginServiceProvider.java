package com.xywy.oauth.service;


import com.xywy.component.datarequest.neworkWrapper.BaseData;
import com.xywy.component.datarequest.neworkWrapper.IDataResponse;
import com.xywy.oauth.BuildConfig;
import com.xywy.oauth.model.UserInfoCenter;
import com.xywy.oauth.model.entity.LoginEntity;
import com.xywy.oauth.model.entity.RegisterEntity;
import com.xywy.oauth.model.entity.UserThird;


/**
 * Created by wuhai on 2016/1/4.
 * 网络请求  所有的方法
 */
public class LoginServiceProvider {
    public static boolean mIsDevelopEnv = BuildConfig.DEBUG;
    public static String REQUEST_URL_ZONGHE = "";
    public static String REQUEST_URL_CLUB = "";
    public static String REQUEST_HTML = "";

    //中间层 封装的都是Club的接口
    public static String REQUEST_URL_CLUB_ONLINE
            = "http://api.wws.xywy.com/api.php";
    public static String REQUEST_URL_CLUB_DEVELOP
            = "http://test.api.wws.xywy.com/api.php";


    public static void setDevEnv(Boolean mIsDevelopEnv) {
        if (mIsDevelopEnv) {
            REQUEST_URL_CLUB = REQUEST_URL_CLUB_DEVELOP;
        } else {
            REQUEST_URL_CLUB = REQUEST_URL_CLUB_ONLINE;
        }
    }
    /**
     * LoginActivity
     */

    /**
     * 785 . 用户登录 v 1.3
     *
     * @param userName 账号
     * @param password 密码
     */
    public static void login(String userName, String password, IDataResponse iHttpResponse, Object flag) {
        ApiParams apiParams = new ApiParams().with(NetworkConstants.version_value3);
        apiParams.with(NetworkConstants.UserName, userName);
        apiParams.with(NetworkConstants.PassWord, password);
        apiParams.with(NetworkConstants.api_key, "785");
        apiParams.with(NetworkConstants.SIGN,
                DataRequestTool.getSig(apiParams, NetworkConstants.basekey));
        DataRequestTool.get(REQUEST_URL_CLUB, Namespace.Login, apiParams, iHttpResponse, LoginEntity.class, flag,
                false);
    }


    /**
     * 789 . 第三方登录 v 1.1
     *
     * @param postApiParams 第三方获取的用户信息map集合
     */
    public static void loginThird(ApiParams postApiParams, IDataResponse iHttpResponse, Object flag) {
        ApiParams getApiParams = new ApiParams().with(NetworkConstants.version_value1)
                .with(NetworkConstants.api_key, 789)
                .with(NetworkConstants.os_key, NetworkConstants.os_value)
                .with(NetworkConstants.pro_key, NetworkConstants.pro_value)
                .with(NetworkConstants.source_key, NetworkConstants.source_value);
        getApiParams.with(NetworkConstants.SIGN,
                DataRequestTool.getSig(getApiParams, postApiParams,
                        NetworkConstants.basekey));
        DataRequestTool.post(REQUEST_URL_CLUB, Namespace.Userthird, getApiParams, postApiParams, iHttpResponse,
                UserThird.class, flag, false);
    }

    /**
     * 784 . 手机验证码相关功能接口 v 1.2
     *
     * @param phone        手机号
     * @param projectValue 区分注册，忘记密码的参数
     */
    public static void getCode(String phone, IDataResponse iHttpResponse, String projectValue, Object flag) {
        ApiParams getApiParams = new ApiParams().with(NetworkConstants.version_value2)
                .with(NetworkConstants.api_key, "784");
        ApiParams postApiParams = new ApiParams().with(NetworkConstants.project_key, projectValue)
                .with(NetworkConstants.act_key,
                        NetworkConstants.act_value)
                .with(NetworkConstants.phone_key, phone)
                .with(NetworkConstants.code_key, "");
        getApiParams.with(NetworkConstants.SIGN,
                DataRequestTool.getSig(getApiParams, postApiParams,
                        NetworkConstants.basekey));
        DataRequestTool.post(REQUEST_URL_CLUB, Namespace.CODE, getApiParams, postApiParams, iHttpResponse, BaseData
                .class, flag, false);
    }


    /**
     * 786 . 短信验证帐号注册接口 v 1.1
     *
     * @param phone 手机号
     * @param code  验证码
     * @param pwd   密码
     */
    public static void register(String phone, String code, String pwd, IDataResponse iHttpResponse, Object flag) {
        ApiParams getApiParams = new ApiParams().with(NetworkConstants.version_value1)
                .with(NetworkConstants.api_key, "786");
        ApiParams postApiParams = new ApiParams().with(NetworkConstants.devid_key,
                NetworkConstants.devid_vaule)
                .with(NetworkConstants.PassWord,
                        pwd)
                .with(NetworkConstants.nickname_key,
                        NetworkConstants.nickname_vaule)
                .with(NetworkConstants.usersource_key,
                        NetworkConstants.source_value)
                .with(NetworkConstants.usign_key, NetworkConstants.usign_vaule)
                .with(NetworkConstants.project_key, NetworkConstants.regist_project_value)
                .with(NetworkConstants.code_key,
                        code)
                .with(NetworkConstants.phone_key,
                        phone);
        getApiParams.with(NetworkConstants.SIGN,
                DataRequestTool.getSig(getApiParams, postApiParams,
                        NetworkConstants.basekey));
        DataRequestTool.post(REQUEST_URL_CLUB, Namespace.Register, getApiParams, postApiParams, iHttpResponse,
                RegisterEntity.class, flag, false);
    }

    /**
     * ChangePasswordActivity
     * code获取验证码 用getCode
     */

    /**
     * 900 . 短信验证修改密码 v 1.1
     *
     * @param phone  手机号
     * @param code   验证码
     * @param newpwd 新密码
     */
    public static void findPassword(String phone, String code, String newpwd, IDataResponse iHttpResponse, Object flag) {
        ApiParams getApiParams = new ApiParams().with(NetworkConstants.version_value1)
                .with(NetworkConstants.api_key, "900");
        ApiParams postApiParams = new ApiParams().with(NetworkConstants.newpwd_key, newpwd)
                .with(NetworkConstants.project_key,
                        NetworkConstants.forget_project_value)
                .with(NetworkConstants.code_key, code)
                .with(NetworkConstants.phone_key,
                        phone);
        getApiParams.with(NetworkConstants.SIGN,
                DataRequestTool.getSig(getApiParams, postApiParams,
                        NetworkConstants.basekey));
        DataRequestTool.post(REQUEST_URL_CLUB, Namespace.Find_password, getApiParams, postApiParams, iHttpResponse,
                BaseData.class, flag, false);
    }

    /**
     * @param userId      用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    public static void changePassword(int userId, String oldPassword, String newPassword, IDataResponse iDataResponse, Object flag) {
        ApiParams getParams = new ApiParams().with(NetworkConstants.version_value)
                .with(NetworkConstants.api_key, "793")
                .with(NetworkConstants.os_key, NetworkConstants.os_value)
                .with(NetworkConstants.source_key, NetworkConstants.source_value)
                .with(NetworkConstants.pro_key, NetworkConstants.pro_value)
                .with(NetworkConstants.act_key, NetworkConstants.act_pwd_key)
                .with(NetworkConstants.uid_key, userId)
                .with(NetworkConstants.PassWord, oldPassword)
                .with(NetworkConstants.newpasswd_key, newPassword);
        getParams.with(NetworkConstants.SIGN,
                DataRequestTool.getSig(getParams, NetworkConstants.basekey));

        DataRequestTool.get(REQUEST_URL_CLUB, Namespace.Change_password, getParams, iDataResponse,
                BaseData.class, flag, false);
    }
    /**
     * BindingPhoneActivity
     */

    /**
     * 787 . 短信验证绑定用户手机帐号接口 v 1.1
     * 注意  当MineFragment绑定手机显示时候，绑定手机layout能否点击被设置成了false
     *
     * @param phone 手机
     * @param code  验证码
     */
    public static void bind(String phone, String code, IDataResponse iHttpResponse, Object flag) {
        ApiParams getApiParams = new ApiParams().with(NetworkConstants.version_value1)
                .with(NetworkConstants.api_key, "787");
        ApiParams postApiParams = new ApiParams().with(NetworkConstants.phone_key,
                phone)
                .with(NetworkConstants.userid_key, UserInfoCenter.getInstance()
                        .getUserId())
                .with(NetworkConstants.project_key, "APP_XYZTC_REG")
                .with(NetworkConstants.code_key, code);
        getApiParams.with(NetworkConstants.SIGN,
                DataRequestTool.getSig(getApiParams, postApiParams,
                        NetworkConstants.basekey));
        DataRequestTool.post(REQUEST_URL_CLUB, Namespace.Bind, getApiParams, postApiParams, iHttpResponse, BaseData
                .class, flag, false);
    }

    /**
     * 更换昵称接口
     *
     * @param userId   用户id
     * @param nickName 昵称
     */
    public static void changeUserName(int userId, String nickName, IDataResponse iDataResponse, Object flag) {
        ApiParams getParams = new ApiParams().with(NetworkConstants.version_value1)
                .with(NetworkConstants.api_key, "897")
                .with(NetworkConstants.os_key, NetworkConstants.os_value)
                .with(NetworkConstants.source_key, NetworkConstants.source_value)
                .with(NetworkConstants.pro_key, NetworkConstants.pro_value);

        ApiParams postParms = new ApiParams().with(NetworkConstants.userid_key, userId)
                .with(NetworkConstants.nickname_key, nickName);
        getParams.with(NetworkConstants.SIGN,
                DataRequestTool.getSig(getParams, postParms,
                        NetworkConstants.basekey));
        DataRequestTool.post(REQUEST_URL_CLUB, Namespace.userInfoEdit, getParams, postParms, iDataResponse
                , BaseData.class, flag, false);
    }


}































































