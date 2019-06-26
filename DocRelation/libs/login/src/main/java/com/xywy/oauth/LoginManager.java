package com.xywy.oauth;

import android.content.Context;

import com.xywy.oauth.ui.BaseActivity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class LoginManager {

    private static Context mContext;

    private static LoginManager loginManager;
    private static List<BaseActivity> mBaseActivitys = Collections
            .synchronizedList(new LinkedList<BaseActivity>());

    private LoginManager() {
    }

    /**
     * @return 作用说明 ：获取context
     */
    public static Context getContext() {
        return mContext;
    }

    public static LoginManager getInstance() {
        if (loginManager == null) {
            synchronized (LoginManager.class) {
                if (loginManager == null) {
                    loginManager = new LoginManager();
                }
            }
        }
        return loginManager;
    }

    public static void init(Context context) {

        mContext = context;

    }

    /**
     * @param baseActivity 作用说明 ：添加一个activity到管理里
     */
    public void pushActivity(BaseActivity baseActivity) {
        mBaseActivitys.add(baseActivity);
    }

    /**
     * @param baseActivity 作用说明 ：删除一个activity在管理里
     */
    public void popActivity(BaseActivity baseActivity) {
        mBaseActivitys.remove(baseActivity);
    }

    /**
     * 清除全部activity,退出程序
     */
    public void finshAllActivity() {
        for (BaseActivity activity : mBaseActivitys) {
            activity.finish();
        }
        System.exit(0);
    }

    /**
     * @return 作用说明 ：获取当前最顶部activity的实例
     */
    public BaseActivity getTopBaseActivity() {
        BaseActivity mBaseActivity = null;
        synchronized (mBaseActivitys) {
            final int size = mBaseActivitys.size() - 1;
            if (size < 0) {
                return null;
            }
            mBaseActivity = mBaseActivitys.get(size);
        }
        return mBaseActivity;
    }

}
