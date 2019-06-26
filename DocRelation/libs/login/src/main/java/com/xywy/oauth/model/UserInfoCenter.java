package com.xywy.oauth.model;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.xywy.oauth.LoginManager;
import com.xywy.oauth.utils.SerializeTools;

import java.io.File;

/**
 * Created by shijiazi on 16/1/7.
 * 用于存放个人信息
 */
public class UserInfoCenter {

    public final static String LoginModelFileName = "/userinfo";

    private static UserInfoCenter instance;

    private LoginModel loginModel;

    private String checkMsg = "";

    private Context mContext;

    private UserInfoCenter() {
        initialData();
    }

    public static synchronized UserInfoCenter getInstance() {
        if (instance == null) {
            instance = new UserInfoCenter();
        }
        return instance;
    }

    public boolean isLogin() {
        if (loginModel == null) {
            return false;
        }
        return true;
    }

    public LoginModel getLoginModel() {
        return loginModel;
    }

    public void setLoginModel(LoginModel loginModel) {
        this.loginModel = loginModel;
        if (loginModel != null) {
            saveLoginModel(loginModel);
        }
    }

    public void reset() {
        //删除保存信息的文件
        String fileDir = getFileDir();
        File file = new File(fileDir);
        file.delete();
        loginModel = null;
        checkMsg = "";
    }

    public void initialData() {
        loadLoginModel();
    }

    /**
     * 快捷方法获取用户id
     *
     * @return
     */
    public String getUserId() {
        String id = "";
        if (loginModel != null) {
            id = loginModel.getUserid();
        }
        return id;
    }

    private void saveLoginModel(LoginModel model) {
        String fileName = getFileDir();
        if (!TextUtils.isEmpty(fileName)) {
            SerializeTools.serialization(fileName, model);
        } else {
            Log.d("userinfo", "登录初始化数据失败");
        }
    }

    private void loadLoginModel() {
        String fileName = getFileDir();
        if (!TextUtils.isEmpty(fileName)) {
            try {
                Object obj = SerializeTools.deserialization(fileName);
                if (obj != null && obj instanceof LoginModel) {
                    loginModel = (LoginModel) obj;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getFileDir() {
        return LoginManager.getContext().getFilesDir().toString() + LoginModelFileName;
    }

}
