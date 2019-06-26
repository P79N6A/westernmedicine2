package com.xywy.askforexpert.module.my.clinic;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.d_platform_n.NumberPicker;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.Map;

/**
 * 停诊时间
 *
 * @author 王鹏
 * @2015-5-20下午1:35:54
 */
public class NumberStopTimeActiviy extends Activity {
    private static final String TAG = "NumberStopTimeActiviy";
    // private int style;

    private NumberPicker np1, np2, np3, np1_stop, np2_stop, np3_stop;
    private static String str1_start;
    private static String str2_start;
    private static String str3_start;

    private static String str1_stop;
    private static String str2_stop;
    private static String str3_stop;
    private TextView tv_date_start, tv_date_stop;
    private TextView save;
    private ImageView imgage_right_start;
    private ImageView imgage_right_stop;
    private RelativeLayout re_start, re_stop;
    private boolean bopen = false;
    private boolean bopen_stop = false;
    private LinearLayout dialog_content;
    private LinearLayout dialog_content_1;
    private EditText edit_reason;
    private LinearLayout lin_stop;
    private LinearLayout lin_start;
    int year;
    int month;
    int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        NumberStopTimeActiviy.this
                .requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.num_stop_time);
        ((TextView) findViewById(R.id.tv_title)).setText("停诊时间设置");
        Time t = new Time();
        t.setToNow();
        year = t.year;
        month = t.month + 1;
        day = t.monthDay;
        str1_start = year + "";
        str2_start = month + "";
        str3_start = day + "";
        str1_stop = year + "";
        str2_stop = month + "";
        str3_stop = day + "";

        initview();
        np_stop();
        np_start();
        tv_date_stop.setText(getData_stop());
        tv_date_start.setText(getData_start());
    }

    public void initview() {
        np1 = (NumberPicker) findViewById(R.id.np1);
        np2 = (NumberPicker) findViewById(R.id.np2);
        np3 = (NumberPicker) findViewById(R.id.np3);
        np1_stop = (NumberPicker) findViewById(R.id.np4);
        np2_stop = (NumberPicker) findViewById(R.id.np5);
        np3_stop = (NumberPicker) findViewById(R.id.np6);
        tv_date_start = (TextView) findViewById(R.id.tv_date_start);
        tv_date_stop = (TextView) findViewById(R.id.tv_date_top);
        re_start = (RelativeLayout) findViewById(R.id.re_start);
        re_stop = (RelativeLayout) findViewById(R.id.re_stop);
        dialog_content = (LinearLayout) findViewById(R.id.dialog_content);
        dialog_content_1 = (LinearLayout) findViewById(R.id.dialog_content_1);
        lin_start = (LinearLayout) findViewById(R.id.lin_start);
        lin_stop = (LinearLayout) findViewById(R.id.lin_stop);
        imgage_right_start = (ImageView) findViewById(R.id.img_enter_start);
        imgage_right_stop = (ImageView) findViewById(R.id.img_enter_top);
        edit_reason = (EditText) findViewById(R.id.edit_resason);

        re_start.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                start();

            }
        });
        re_stop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                stop();

            }
        });

    }

    private void start() {
        if (bopen) {

            TranslateAnimation anim = new TranslateAnimation(0, 0, 0,
                    -dialog_content.getHeight());
            anim.setDuration(250);
            anim.setFillAfter(true);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    bopen = false;
                    lin_start.clearAnimation();
                    dialog_content.setVisibility(View.GONE);

                }
            });
            imgage_right_start.setBackgroundResource(R.drawable.enght_3);
            lin_start.startAnimation(anim);
        } else {

            TranslateAnimation anim = new TranslateAnimation(0, 0,
                    -dialog_content.getHeight(), 0);
            anim.setDuration(250);
            anim.setFillBefore(false);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    dialog_content.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    bopen = true;
                    lin_start.clearAnimation();

                }
            });
//			imgage_right_start.startAnimation(NetworkUtil.getRotateAnimation(
//					-90,0, 200));
            imgage_right_start.setBackgroundResource(R.drawable.enter_right);

            lin_start.startAnimation(anim);
        }
    }

    private void stop() {
        if (bopen_stop) {

            TranslateAnimation anim = new TranslateAnimation(0, 0, 0,
                    -dialog_content_1.getHeight());
            anim.setDuration(250);
            anim.setFillAfter(true);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    bopen_stop = false;
                    lin_stop.clearAnimation();
                    dialog_content_1.setVisibility(View.GONE);
                }
            });
//			 imgage_right_stop.startAnimation(NetworkUtil.getRotateAnimation(0,
//			 -90,
//			 200));
            imgage_right_stop.setBackgroundResource(R.drawable.enght_3);

            lin_stop.startAnimation(anim);
        } else {

            TranslateAnimation anim = new TranslateAnimation(0, 0,
                    -dialog_content_1.getHeight(), 0);
            anim.setDuration(250);
            anim.setFillBefore(false);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    dialog_content_1.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    bopen_stop = true;
                    lin_stop.clearAnimation();
                }
            });
//			 imgage_right_stop.startAnimation(NetworkUtil.getRotateAnimation(-90,
//			 0,
//			 200));
            imgage_right_stop.setBackgroundResource(R.drawable.enter_right);

            lin_stop.startAnimation(anim);
        }
    }

    public void np_stop() {

        np1_stop.setMaxValue(2100);
        np1_stop.setMinValue(2000);
        np1_stop.setValue(year);

        np1_stop.setFocusableInTouchMode(true);
        np2_stop.setFocusableInTouchMode(true);
        np3_stop.setFocusableInTouchMode(true);
        np1_stop.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
                // TODO Auto-generated method stub
                str1_stop = np1_stop.getValue() + "";
                tv_date_stop.setText(getData_stop());
                if (Integer.parseInt(str1_stop) % 4 == 0
                        && Integer.parseInt(str1_stop) % 100 != 0
                        || Integer.parseInt(str1_stop) % 400 == 0) {
                    if (str2_stop.equals("1") || str2_stop.equals("3")
                            || str2_stop.equals("5") || str2_stop.equals("7")
                            || str2_stop.equals("8") || str2_stop.equals("10")
                            || str2_stop.equals("12")) {
                        np3_stop.setMaxValue(31);
                        np3_stop.setMinValue(1);
                    } else if (str2_stop.equals("4") || str2_stop.equals("6")
                            || str2_stop.equals("9") || str2_stop.equals("11")) {
                        np3_stop.setMaxValue(30);
                        np3_stop.setMinValue(1);
                    } else {
                        np3_stop.setMaxValue(29);
                        np3_stop.setMinValue(1);
                    }

                } else {
                    if (str2_stop.equals("1") || str2_stop.equals("3")
                            || str2_stop.equals("5") || str2_stop.equals("7")
                            || str2_stop.equals("8") || str2_stop.equals("10")
                            || str2_stop.equals("12")) {
                        np3_stop.setMaxValue(31);
                        np3_stop.setMinValue(1);
                    } else if (str2_stop.equals("4") || str2_stop.equals("6")
                            || str2_stop.equals("9") || str2_stop.equals("11")) {
                        np3_stop.setMaxValue(30);
                        np3_stop.setMinValue(1);
                    } else {
                        np3_stop.setMaxValue(28);
                        np3_stop.setMinValue(1);
                    }
                }

            }
        });

        np2_stop.setMaxValue(12);
        np2_stop.setMinValue(1);
        np2_stop.setValue(month);
        np2_stop.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
                // TODO Auto-generated method stub
                str2_stop = np2_stop.getValue() + "";
                tv_date_stop.setText(getData_stop());
                if (str2_stop.equals("1") || str2_stop.equals("3")
                        || str2_stop.equals("5") || str2_stop.equals("7")
                        || str2_stop.equals("8") || str2_stop.equals("10")
                        || str2_stop.equals("12")) {
                    np3.setMaxValue(31);
                    np3.setMinValue(1);
                } else if (str2_stop.equals("4") || str2_stop.equals("6")
                        || str2_stop.equals("9") || str2_stop.equals("11")) {
                    np3_stop.setMaxValue(30);
                    np3_stop.setMinValue(1);
                } else {
                    if (Integer.parseInt(str1_stop) % 4 == 0
                            && Integer.parseInt(str1_stop) % 100 != 0
                            || Integer.parseInt(str1_stop) % 400 == 0) {
                        np3_stop.setMaxValue(29);
                        np3_stop.setMinValue(1);
                    } else {
                        np3_stop.setMaxValue(28);
                        np3_stop.setMinValue(1);
                    }
                }
            }
        });

        np3_stop.setMaxValue(31);
        np3_stop.setMinValue(1);
        np3_stop.setValue(day);
        np3_stop.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
                // TODO Auto-generated method stub
                str3_stop = np3_stop.getValue() + "";
                tv_date_stop.setText(getData_stop());
            }
        });
    }

    public void np_start() {
        np1.setMaxValue(2100);
        np1.setMinValue(2000);
        np1.setValue(year);
        np1.setFocusableInTouchMode(true);
        np2.setFocusableInTouchMode(true);
        np3.setFocusableInTouchMode(true);
        np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
                // TODO Auto-generated method stub
                str1_start = np1.getValue() + "";
                tv_date_start.setText(getData_start());
                if (Integer.parseInt(str1_start) % 4 == 0
                        && Integer.parseInt(str1_start) % 100 != 0
                        || Integer.parseInt(str1_start) % 400 == 0) {
                    if (str2_start.equals("1") || str2_start.equals("3")
                            || str2_start.equals("5") || str2_start.equals("7")
                            || str2_start.equals("8")
                            || str2_start.equals("10")
                            || str2_start.equals("12")) {
                        np3.setMaxValue(31);
                        np3.setMinValue(1);
                    } else if (str2_start.equals("4") || str2_start.equals("6")
                            || str2_start.equals("9")
                            || str2_start.equals("11")) {
                        np3.setMaxValue(30);
                        np3.setMinValue(1);
                    } else {
                        np3.setMaxValue(29);
                        np3.setMinValue(1);
                    }

                } else {
                    if (str2_start.equals("1") || str2_start.equals("3")
                            || str2_start.equals("5") || str2_start.equals("7")
                            || str2_start.equals("8")
                            || str2_start.equals("10")
                            || str2_start.equals("12")) {
                        np3.setMaxValue(31);
                        np3.setMinValue(1);
                    } else if (str2_start.equals("4") || str2_start.equals("6")
                            || str2_start.equals("9")
                            || str2_start.equals("11")) {
                        np3.setMaxValue(30);
                        np3.setMinValue(1);
                    } else {
                        np3.setMaxValue(28);
                        np3.setMinValue(1);
                    }
                }

            }
        });

        np2.setMaxValue(12);
        np2.setMinValue(1);
        np2.setValue(month);
        np2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
                // TODO Auto-generated method stub
                str2_start = np2.getValue() + "";
                tv_date_start.setText(getData_start());
                if (str2_start.equals("1") || str2_start.equals("3")
                        || str2_start.equals("5") || str2_start.equals("7")
                        || str2_start.equals("8") || str2_start.equals("10")
                        || str2_start.equals("12")) {
                    np3.setMaxValue(31);
                    np3.setMinValue(1);
                } else if (str2_start.equals("4") || str2_start.equals("6")
                        || str2_start.equals("9") || str2_start.equals("11")) {
                    np3.setMaxValue(30);
                    np3.setMinValue(1);
                } else {
                    if (Integer.parseInt(str1_start) % 4 == 0
                            && Integer.parseInt(str1_start) % 100 != 0
                            || Integer.parseInt(str1_start) % 400 == 0) {
                        np3.setMaxValue(29);
                        np3.setMinValue(1);
                    } else {
                        np3.setMaxValue(28);
                        np3.setMinValue(1);
                    }
                }
            }
        });

        np3.setMaxValue(31);
        np3.setMinValue(1);
        np3.setValue(day);
        np3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
                // TODO Auto-generated method stub
                str3_start = np3.getValue() + "";
                tv_date_start.setText(getData_start());
            }
        });
    }

    public static String getData_start() {
        return str1_start + "-" + str2_start + "-" + str3_start;
    }

    public static String getData_stop() {
        return str1_stop + "-" + str2_stop + "-" + str3_stop;
    }

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.next_btn:
                String reason = edit_reason.getText().toString().trim();
                if (reason != null & !"".equals(reason)) {
                    sendData(reason);
                } else {
                    ToastUtils.shortToast("停诊原因不能为空");
                }

                break;

            default:
                break;
        }
    }

    public void sendData(String reason) {
        String userid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(userid + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("userid", userid);
        params.put("command", "addstop");
        params.put("sign", sign);
        params.put("starttime", getData_start());
        params.put("endtime", getData_stop());
        params.put("reason", reason);

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.MyClinic_State_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        DLog.i(TAG, "停诊服务提交" + t.toString());
                        Map<String, String> map = ResolveJson.R_Action(t
                                .toString());
                        ToastUtils.shortToast(
                                map.get("msg"));
                        if (map.get("code").equals("0")) {
//							NumberStopTimeListActivity.refesh();
                            finish();
                        }
                        super.onSuccess(t);
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
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatisticalTools.onResume(NumberStopTimeActiviy.this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub

        StatisticalTools.onPause(NumberStopTimeActiviy.this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
    }
}
