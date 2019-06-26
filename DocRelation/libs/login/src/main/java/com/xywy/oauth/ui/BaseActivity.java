package com.xywy.oauth.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xywy.oauth.LoginManager;
import com.xywy.oauth.R;
import com.xywy.oauth.utils.BackgroundUtils;


/**
 * Created by shijiazi on 16/3/11.
 */
public class BaseActivity extends FragmentActivity {


    public Dialog loadingDialog;//loading用

    protected void chearData() {

    }

    public <T extends View> T findView(int viewId) {
        return (T) findViewById(viewId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginManager application = LoginManager.getInstance();
        application.popActivity(this);

    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        LoginManager application = LoginManager.getInstance();
        application.pushActivity(this);

        loadingDialog = new Dialog(this, R.style.loading_dialog);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
//        {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        BackgroundUtils.getInstance().dealAppRunState("", true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        BackgroundUtils.getInstance().dealAppRunState();
    }

    @Override
    public void setContentView(final int layoutResID) {
        super.setContentView(layoutResID);

    }


    /**
     * dialog show
     */
    public void showDialog() {
        LayoutInflater inflater2 = LayoutInflater.from(this);
        View v = inflater2.inflate(R.layout.dialog_common_layout, null);// 得到加载view
        RelativeLayout layout2 = (RelativeLayout) v.findViewById(R.id.layout_control);// 加载布局
        loadingDialog.setCancelable(false);//true 点击空白处或返回键消失   false 不消失
        loadingDialog.setContentView(layout2, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
        loadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    loadingDialog.dismiss();
                }
                return false;
            }
        });
        loadingDialog.show();
    }

    public void hideDialog() {
        if (loadingDialog != null) {
            loadingDialog.cancel();
        }
    }


    //将数据绑定到view上
    protected <T> T getIntentData(String key, Class<T> returnClass) {
        if (getIntent().getExtras() != null)
            return returnClass.cast(getIntent().getExtras().get(key));
        return null;
    }
}
