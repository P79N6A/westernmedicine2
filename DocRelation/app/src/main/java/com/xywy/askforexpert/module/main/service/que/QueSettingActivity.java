package com.xywy.askforexpert.module.main.service.que;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

/**
 * 项目名称：D_Platform
 * 类名称：QueSettingActivity
 * 类描述：问题广场设置
 * 创建人：shihao
 * 创建时间：2015-5-27 上午10:10:59
 * 修改备注：
 */
public class QueSettingActivity extends Activity {

    private ImageButton ivBtn;

    private SharedPreferences mSharedPreferences;

    /**
     * 开关状态 1 开启  2 关闭
     */
    private int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);

        setContentView(R.layout.activity_que_setting);

        ivBtn = (ImageButton) findViewById(R.id.iv_switch_btn);

        mSharedPreferences = getSharedPreferences("msg_manager", MODE_PRIVATE);
        if (mSharedPreferences.getBoolean("close_que", true)) {
            ivBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_off));
            state = 1;
        } else {
            ivBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_on));
            state = 2;
        }

        ivBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("msg_manager",
                        MODE_PRIVATE).edit();

                SharedPreferences.Editor edit = getSharedPreferences(
                        "isskip", MODE_PRIVATE).edit();
                edit.putBoolean("skip", true);
                edit.commit();
                if (mSharedPreferences.getBoolean("close_que", true)) {
                    editor.putBoolean("close_que", false);
                    ivBtn.setBackgroundDrawable(getResources().getDrawable(
                            R.drawable.radio_on));
                    state = 2;
                } else {
                    editor.putBoolean("close_que", true);
                    ivBtn.setBackgroundDrawable(getResources().getDrawable(
                            R.drawable.radio_off));
                    state = 1;
                }

                editor.commit();
            }
        });
    }

    public void onQueSettingListener(View v) {
        switch (v.getId()) {
            case R.id.rl_answer:

                break;

            case R.id.btn_setting_back:

                if (YMApplication.getLoginInfo().getData()
                        .getIsjob().equals("2")) {
                    openOrClose(state);
                }
                finish();
                break;

//		case R.id.rl_time:
//
//			break;
        }
    }

    /**
     * 开关
     */
    private void openOrClose(int state) {
        String uid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(uid + state + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("userid", uid);
        params.put("state", state + "");
        params.put("sign", sign);
        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.QUE_EXPERT_OPEN, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);


                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        // TODO Auto-generated method stub
                        super.onFailure(t, errorNo, strMsg);
                        finish();
                    }
                });
    }

    public void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
    }

    public void onPause() {
        StatisticalTools.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
