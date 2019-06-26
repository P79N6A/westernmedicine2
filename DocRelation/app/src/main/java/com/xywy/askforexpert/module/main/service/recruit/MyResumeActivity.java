package com.xywy.askforexpert.module.main.service.recruit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.module.main.service.codex.BookWebActivity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.Map;

/**
 * 我的简历
 *
 * @author 王鹏
 * @2015-5-16下午3:06:34
 */
public class MyResumeActivity extends Activity {

    private Map<String, String> map;

    private ImageView radio;
    private TextView tv_show;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 100:
                    if (map == null) {
                        return;
                    }
                    if (map != null && map.get("code").equals("0")) {

                        if ("yes".equals(map.get("show"))) {
                            radio.setBackgroundResource(R.drawable.radio_off);
                            tv_show.setText("企业可见");
                        } else {
                            radio.setBackgroundResource(R.drawable.radio_on);
                            tv_show.setText("企业不可见");

                        }
                    }
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);

        setContentView(R.layout.my_resume);
        ((TextView) findViewById(R.id.tv_title)).setText("我的简历");
        tv_show = (TextView) findViewById(R.id.tv_show);
        radio = (ImageView) findViewById(R.id.radio);
        if (YMUserService.isGuest()) {
            DialogUtil.LoginDialog(new YMOtherUtils(MyResumeActivity.this).context);
        } else {
            if (NetworkUtil.isNetWorkConnected()) {
                getData();
            } else {
                ToastUtils.shortToast("网络连接失败");
            }

        }

    }

    public void onClick_back(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.re_send:
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(MyResumeActivity.this).context);
                } else {
                    intent = new Intent(MyResumeActivity.this,
                            DeliverActivity.class);
                    // intent.putExtra("type", CommonUrl.MyResume_Send_Url);
                    // intent.putExtra("title", "投递的职位");
                    startActivity(intent);
                }
                break;
            case R.id.re_save:
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(MyResumeActivity.this).context);
                } else {
                    intent = new Intent(MyResumeActivity.this,
                            MyResumeSecondActivity.class);
                    intent.putExtra("type", CommonUrl.MyResume_Save_Url);
                    intent.putExtra("title", "收藏的职位");
                    startActivity(intent);
                }

                break;
            case R.id.myresume:
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(MyResumeActivity.this).context);
                } else {
                    if (map != null) {
                        if (!map.get("url").equals("")) {
                            intent = new Intent(this, BookWebActivity.class);
                            if (!TextUtils.isEmpty(map.get("url"))) {
                                intent.putExtra("url", map.get("url"));
                            } else {
                                intent.putExtra("url", "");
                            }
                            intent.putExtra("msg", "");
                            intent.putExtra("title", "我的简历");
                            startActivity(intent);
                        } else {
                            ToastUtils.shortToast(
                                    map.get("msg"));
                        }
                    } else {
                        ToastUtils.shortToast(
                                "数据获取失败");
                    }
                }

                break;
            case R.id.radio:
                if (map != null) {
                    if ("yes".equals(map.get("show"))) {
                        RadioType("close");
                    } else {
                        RadioType("show");
                    }
                }
                break;
            default:
                break;
        }
    }

    public void RadioType(final String show) {
        String id = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(id + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("uid", id);
        params.put("type", show);
        params.put("sign", sign);
        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.ModleUrl + "zhaopin/show_resume.interface.php",
                params, new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        Map<String, String> maps = ResolveJson.R_MyResume(t.toString());
                        // handler.sendEmptyMessage(100);
                        if ("0".equals(maps.get("code"))) {
                            if ("show".equals(show)) {
                                radio.setBackgroundResource(R.drawable.radio_off);
                                map.put("show", "yes");
                                tv_show.setText("企业可见");
                            } else {
                                radio.setBackgroundResource(R.drawable.radio_on);
                                map.put("show", "no");
                                tv_show.setText("企业不可见");
                            }
                        } else {
                            ToastUtils.shortToast(maps.get("msg"));
                        }
                        super.onSuccess(t);
                    }

                    @Override
                    public void onLoading(long count, long current) {
                        super.onLoading(count, current);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                    }
                });
    }

    // public void getData()
    // {
    // AjaxParams params =new AjaxParams();
    // params.put("uid","18732252");
    // // zhaopin/resumeU.interface.php
    //
    //
    // }

    public void getData() {

        String id = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(id + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("uid", id);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.ModleUrl + "zhaopin/resumeU.interface.php", params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        map = ResolveJson.R_MyResume(t.toString());
                        handler.sendEmptyMessage(100);
                        super.onSuccess(t);
                    }

                    @Override
                    public void onLoading(long count, long current) {
                        // TODO Auto-generated method stub
                        super.onLoading(count, current);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        // TODO Auto-generated method stub
                        super.onFailure(t, errorNo, strMsg);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
