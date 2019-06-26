package com.xywy.askforexpert.module.my.clinic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.PhoneDocPerInfo;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 电话医生设置 开通 stone
 *
 * @author 王鹏
 * @2015-5-20下午5:16:04
 */
public class PhoneDoctorSettingActivity extends Activity {

    private Map<String, String> map = new HashMap<String, String>();
    private PhoneDocPerInfo phonperinfo;
    String str;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 100:
                    if (map.get("code").equals("0")) {
//					findViewById(R.id.next_btn).setVisibility(View.GONE);
                        ToastUtils.shortToast("您的服务申请已经提交，我们将在24小时内审核，请耐心等待！");
                        YMApplication.getLoginInfo().getData().getXiaozhan().setPhone(Constants.FUWU_AUDIT_STATUS_0);
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    ToastUtils.shortToast(
                            map.get("msg"));
//				T.showNoRepeatShort(PhoneDoctorSettingActivity.this,
//						getResources().getString(R.string.err_add_num));
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CommonUtils.initSystemBar(this);
        setContentView(R.layout.phone_doctor_setting);
        ((TextView) findViewById(R.id.tv_title)).setText("电话医生");

        str = YMApplication.getLoginInfo().getData().getXiaozhan().getPhone();
        if (!TextUtils.isEmpty(str)) {
            if (str.equals(Constants.FUWU_AUDIT_STATUS_1)) {
                findViewById(R.id.next_btn).setVisibility(View.GONE);
                getDataInfo();
            }
        }

    }


    public void fuzhi() {

        //stone 判空
        if (phonperinfo.getData() != null && phonperinfo.getData().getList() != null) {


            YMApplication.phosetinfo.setPhone(phonperinfo.getData().getList().getPhone());
            YMApplication.phosetinfo.setPhone1(phonperinfo.getData().getList().getPhone1());
            YMApplication.phosetinfo.setPhone2(phonperinfo.getData().getList().getPhone2());
            YMApplication.phosetinfo.setExplanation(phonperinfo.getData().getList().getExplanation());

            //判空
            List<PhoneDocPerInfo> prices = phonperinfo.getData().getList().getPrice();
            if (prices != null) {
                int price_size = prices.size();
                List<String> time_list = new ArrayList<String>();
                for (int i = 0; i < price_size; i++) {
                    time_list.add(prices.get(i).getTime());
                }
                YMApplication.phosetinfo.setTime(time_list);
            }

            //判空
            if (phonperinfo.getData().getList().getTimes() != null) {


                List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                Map<String, String> map = new HashMap<String, String>();
                map.put("day_week", "星期一：");
                map.put("day_time", getWeek_map(phonperinfo.getData().getList().getTimes().getWeek1()));
                Map<String, String> map1 = new HashMap<String, String>();
                map1.put("day_week", "星期二：");
                map1.put("day_time", getWeek_map(phonperinfo.getData().getList().getTimes().getWeek2()));

                Map<String, String> map2 = new HashMap<String, String>();
                map2.put("day_week", "星期三：");
                map2.put("day_time", getWeek_map(phonperinfo.getData().getList().getTimes().getWeek3()));

                Map<String, String> map3 = new HashMap<String, String>();
                map3.put("day_week", "星期四：");
                map3.put("day_time", getWeek_map(phonperinfo.getData().getList().getTimes().getWeek4()));

                Map<String, String> map4 = new HashMap<String, String>();
                map4.put("day_week", "星期五：");
                map4.put("day_time", getWeek_map(phonperinfo.getData().getList().getTimes().getWeek5()));

                Map<String, String> map5 = new HashMap<String, String>();
                map5.put("day_week", "星期六：");
                map5.put("day_time", getWeek_map(phonperinfo.getData().getList().getTimes().getWeek6()));

                Map<String, String> map6 = new HashMap<String, String>();
                map6.put("day_week", "星期日：");
                map6.put("day_time", getWeek_map(phonperinfo.getData().getList().getTimes().getWeek7()));
                list.add(map);
                list.add(map1);
                list.add(map2);
                list.add(map3);
                list.add(map4);
                list.add(map5);
                list.add(map6);
                YMApplication.phosetinfo.setList_times(list);
            }
        }
    }

    public String getWeek_map(List<PhoneDocPerInfo> week_day) {
        String str = "";
        //stone 可能为空
        if (week_day != null) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < week_day.size(); i++) {

                sb.append(week_day.get(i).getStart_time() + "－" + week_day.get(i).getEnd_time() + "人数:" + week_day.get(i).getNum() + ",");
            }
            if (sb.length() > 0) {
                str = sb.substring(0, sb.lastIndexOf(","));
            }
        }
        return str;
    }

    public void getDataInfo() {
        String uid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(uid + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("userid", uid);
        params.put("command", "phonedoc");
        params.put("sign", sign);
        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.MyClinic_Phone_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                // TODO Auto-generated method stub
                Gson gson = new Gson();
                phonperinfo = gson.fromJson(t.toString(), PhoneDocPerInfo.class);
                //stone 医生助手独自判断  数据返回没有 status ???
                if (YMApplication.sIsYSZS) {
                    if (phonperinfo != null && !TextUtils.isEmpty(phonperinfo.getCode()) && phonperinfo.getCode().equals("0")) {
                        fuzhi();
                    }
                } else {
                    //stone
                    if (phonperinfo != null && !TextUtils.isEmpty(phonperinfo.getCode()) && phonperinfo.getCode().equals("0")) {
                        fuzhi();
                    }
                }
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    public void sendData() {
        String uid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(uid + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("userid", uid);
        params.put("phone", YMApplication.phosetinfo.getPhone());
        params.put("phone1", YMApplication.phosetinfo.getPhone1());
        params.put("phone2", YMApplication.phosetinfo.getPhone2());
        params.put("explanation",
                YMApplication.phosetinfo.getExplanation());
        params.put("fee", YMApplication.phosetinfo.getFee());
        params.put("phonetime", YMApplication.phosetinfo.getPhonetime());
        params.put("command", "phonedoc_save");
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.MyClinic_Phone_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        map = ResolveJson.R_Action(t.toString());
                        handler.sendEmptyMessage(100);
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

    public void onClick_back(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.re_server_phone:// 接受服务电话

                intent = new Intent(PhoneDoctorSettingActivity.this,
                        Pho_Doc_Ser_PhoneActiviy.class);
                startActivity(intent);

                break;
            case R.id.re_server_money:// 服务费用

                intent = new Intent(PhoneDoctorSettingActivity.this,
                        Phone_moneyActivity.class);
                intent.putExtra("str_type", str);
                startActivity(intent);
                break;
            case R.id.re_server_time:// 服务时间
                if (!TextUtils.isEmpty(str) && str.equals(Constants.FUWU_AUDIT_STATUS_1)) {
                    intent = new Intent(PhoneDoctorSettingActivity.this,
                            FamDoctorTimeShowActivity.class);
                    intent.putExtra("type", "phone");
                    startActivity(intent);
                } else {
                    intent = new Intent(PhoneDoctorSettingActivity.this,
                            PhoneServerTimeActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.re_server_expan:// 服务说明
                intent = new Intent(PhoneDoctorSettingActivity.this,
                        AddNumberEditAcitvity.class);
                intent.putExtra("title", "服务说明");
                intent.putExtra("type", "server_fee");
                //stone
//                if (YMApplication.sIsYSZS) {
                if (!TextUtils.isEmpty(str) && str.equals(Constants.FUWU_AUDIT_STATUS_1)) {
                    intent.putExtra("istype", Constants.FUWU_AUDIT_STATUS_1);
                }
//                }
                startActivity(intent);
                break;
            case R.id.next_btn:
                String phone = YMApplication.phosetinfo.getPhone();
                String expan = YMApplication.phosetinfo.getExplanation();
                String fee = YMApplication.phosetinfo.getFee();
                String phonetime = YMApplication.phosetinfo.getPhonetime();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.shortToast("请编辑服务电话");
                } else if (TextUtils.isEmpty(fee)) {
                    ToastUtils.shortToast("请编辑服务费用");
                } else if (TextUtils.isEmpty(phonetime)) {
                    ToastUtils.shortToast("请编辑服务时间");
                } else if (TextUtils.isEmpty(expan)) {
                    ToastUtils.shortToast("请编辑服务说明");
                } else {
                    if (NetworkUtil.isNetWorkConnected()) {
                        sendData();
                    } else {
                        ToastUtils.shortToast("网络连接失败");
                    }

                }


                break;
            default:
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatisticalTools.onResume(PhoneDoctorSettingActivity.this);

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub

        StatisticalTools.onPause(PhoneDoctorSettingActivity.this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);

        super.onDestroy();
    }
}
