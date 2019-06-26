package com.xywy.askforexpert.module.main.service.downFile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;

import java.util.Map;

/**
 * 文件下载 基类
 *
 * @author 王鹏
 * @2015-6-5下午4:27:06
 */
public class BaseDownFragment extends Fragment implements ContentValue {

    private SharedPreferences sp;
    private Context mContext;
    private YMApplication myApp;
    private Editor edit;

    /**
     * @param mContext   上下文环境
     * @param title      标题
     * @param msg        消息
     * @param cancelable 是否可取消
     * @return 返回ProgressDialog这个对象
     */
    public ProgressDialog showProgressDialog(Context mContext, String title,
                                             String msg, boolean cancelable) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.setCancelable(cancelable);
        dialog.show();
        return dialog;

    }

    /**
     * @param
     * @return Intent
     * @Description: 获得服务的意图
     */
    public Intent getServerIntent() {
        Intent i = new Intent(getActivity(), DownloadService.class);
        i.putExtra(CACHE_DIR, getStringValueByConfigFile(CACHE_DIR)); // 设置缓存路径
        return i;
    }

    /**
     * @param key
     * @param value
     * @Description: 想配置文件中增加一个字段
     **/
    public boolean putStringValueToConfigFile(String key, String value) {
        Editor e = getSp().edit();
        e.putString(key, value);
        return e.commit();
    }

    public boolean putStringValueToConfigFile(String key, int value) {
        Editor e = getSp().edit();
        e.putInt(key, value);
        return e.commit();
    }

    public boolean putBooleanValueToConfigFile(String key, boolean value) {
        Editor e = getSp().edit();
        e.putBoolean(key, value);
        return e.commit();
    }

    public String getStringValueByConfigFile(String key) {
        return sp.getString(key, "");
    }

    public int getIntegerValueByConfigFile(String key) {
        return getSp().getInt(key, -1);
    }

    public boolean getBooleanValueByConfigFile(String key) {
        return getSp().getBoolean(key, false);
    }

    /**
     * @param view 系统回调的view
     * @return void
     */
    public void onClick(View view) {

    }

    /**
     * @param msg      需要显示的消息
     * @param mContext 接收一个环境
     * @return void
     */
    public void showTost(String msg, Context mContext) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 根据参数,和类型返回一个URL
     *
     * @param param 接收一个封装了参数的Map对象
     * @param type  指定返回url 类型
     * @return 返回指定类型的url
     */
    public String getUrlByParameter(Map<String, String> param, int type) {
        String url = "";
        switch (type) {
            default:
                break;
        }
        return url;
    }

    /**
     * 初始化控件
     *
     * @return void
     */
    public void initView() {
        YMApplication app = (YMApplication) getActivity().getApplication();
        setSp(getActivity().getSharedPreferences("config",
                getActivity().MODE_PRIVATE));
        this.edit = getSp().edit();
        setMyApp(app);
    }

    public YMApplication getMyApp() {
        return myApp;
    }

    public void setMyApp(YMApplication myApp) {
        this.myApp = myApp;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public SharedPreferences getSp() {
        return sp;
    }

    public void setSp(SharedPreferences sp) {
        this.sp = sp;
    }

    public Editor getEdit() {
        return edit;
    }

    public void setEdit(Editor edit) {
        this.edit = edit;
    }

    public void onResume() {
        super.onResume();
        StatisticalTools.fragmentOnResume("BaseDownFragment");
    }

    public void onPause() {
        super.onPause();
        StatisticalTools.fragmentOnPause("BaseDownFragment");
    }

}
