package com.xywy.askforexpert.module.my.clinic;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.model.HomeDoctorInfo;
import com.xywy.base.view.ProgressDialog;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 家庭医生 设置页面 stone
 *
 * @author 王鹏
 * @2015-5-20下午4:17:49
 */
public class FamDoctorSetingActivity extends YMBaseActivity {
    private HomeDoctorInfo homeoc;
    private Map<String, String> map = new HashMap<String, String>();
    private String fam_stat;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 100:
                    if (map.get("code").equals("0")) {
                       ToastUtils.shortToast(
                                "您的服务申请已经提交，我们将在24小时内审核，请耐心等待！");
                        YMApplication.getLoginInfo().getData().getXiaozhan().setFamily(Constants.FUWU_AUDIT_STATUS_0);
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        ToastUtils.shortToast(map.get("msg"));
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void beforeViewBind() {
        super.beforeViewBind();
        fam_stat = YMApplication.getLoginInfo().getData().getXiaozhan().getFamily();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fam_doctor_setting;
    }

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("家庭医生");
    }

    @Override
    protected void initData() {
        if (NetworkUtil.isNetWorkConnected()) {
            getData();
        } else {
            ToastUtils.shortToast("网络连接失败");
        }

        if (fam_stat.equals(Constants.FUWU_AUDIT_STATUS_1)) {
            findViewById(R.id.next_btn).setVisibility(View.GONE);
        }
    }


    public void fuzhi() {
        //stone 判空

        if (fam_stat.equals(Constants.FUWU_AUDIT_STATUS_1)
                && homeoc != null
                && homeoc.getData() != null) {
            YMApplication.famdocinfo.setWords(homeoc.getData().getWords());
            YMApplication.famdocinfo.setSpecial(homeoc.getData().getSpecial());

            if (homeoc.getData().getProductinfo() != null && homeoc.getData().getProductinfo().size() >= 2) {
                if (!TextUtils.isEmpty(homeoc.getData().getProductinfo().get(0).getCategory())
                        && homeoc.getData().getProductinfo().get(0).getCategory()
                        .equals("1")) {
                    YMApplication.famdocinfo.setWeek(homeoc.getData()
                            .getProductinfo().get(0).getPrice());
                    YMApplication.famdocinfo.setMonth(homeoc.getData()
                            .getProductinfo().get(1).getPrice());
                } else {
                    YMApplication.famdocinfo.setWeek(homeoc.getData()
                            .getProductinfo().get(1).getPrice());
                    YMApplication.famdocinfo.setMonth(homeoc.getData()
                            .getProductinfo().get(0).getPrice());
                }
            }

            if (homeoc.getData().getOnlinetime() != null) {

                List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                Map<String, String> map = new HashMap<String, String>();
                map.put("day_week", "星期一：");
                map.put("day_time", getWeek_map(homeoc.getData().getOnlinetime()
                        .getMonday()));
                Map<String, String> map1 = new HashMap<String, String>();
                map1.put("day_week", "星期二：");
                map1.put("day_time", getWeek_map(homeoc.getData().getOnlinetime()
                        .getTuesday()));

                Map<String, String> map2 = new HashMap<String, String>();
                map2.put("day_week", "星期三：");
                map2.put("day_time", getWeek_map(homeoc.getData().getOnlinetime()
                        .getWednesday()));

                Map<String, String> map3 = new HashMap<String, String>();
                map3.put("day_week", "星期四：");
                map3.put("day_time", getWeek_map(homeoc.getData().getOnlinetime()
                        .getThursday()));

                Map<String, String> map4 = new HashMap<String, String>();
                map4.put("day_week", "星期五：");
                map4.put("day_time", getWeek_map(homeoc.getData().getOnlinetime()
                        .getFriday()));

                Map<String, String> map5 = new HashMap<String, String>();
                map5.put("day_week", "星期六：");
                map5.put("day_time", getWeek_map(homeoc.getData().getOnlinetime()
                        .getSaturday()));

                Map<String, String> map6 = new HashMap<String, String>();
                map6.put("day_week", "星期日：");
                map6.put("day_time", getWeek_map(homeoc.getData().getOnlinetime()
                        .getSunday()));
                list.add(map);
                list.add(map1);
                list.add(map2);
                list.add(map3);
                list.add(map4);
                list.add(map5);
                list.add(map6);
                YMApplication.famdocinfo.setList(list);
            }
        }
    }

    public String getWeek_map(List<HomeDoctorInfo> week_day) {
        if (week_day == null) {
            return "";
        }
        String str = "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < week_day.size(); i++) {

            sb.append(week_day.get(i).getStart() + "时－"
                    + week_day.get(i).getEnd() + "时" + ",");
        }
        if (sb.length() > 0) {
            str = sb.substring(0, sb.lastIndexOf(","));
        }
        return str;
    }

    public void getData() {
        final ProgressDialog dialog = new ProgressDialog(this, "正在加载中...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.showProgersssDialog();
        String userid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(userid + Constants.MD5_KEY);

        AjaxParams params = new AjaxParams();
        params.put("uid", userid);
        params.put("func", "form_exec");
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();

        fh.post(CommonUrl.MyClinic_Fam_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                Gson gson = new Gson();
                homeoc = gson.fromJson(t.toString(), HomeDoctorInfo.class);
                if (fam_stat.equals(Constants.FUWU_AUDIT_STATUS_1)) {
                    fuzhi();
                }
                dialog.dismiss();
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                dialog.dismiss();
                ToastUtils.longToast("网络繁忙,请稍后重试");
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    /**
     * @param words   医生寄语
     * @param time    接电话时间
     * @param week    包周
     * @param special 擅长
     * @param month
     */
    public void sendData(String words, String time, String month, String week,
                         String special) {
        String userid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(userid + Constants.MD5_KEY);

        AjaxParams params = new AjaxParams();
        params.put("uid", userid);
        params.put("func", "form_exec");
        params.put("sign", sign);
        params.put("words", words);
        params.put("time", time);
        params.put("month", month);
        params.put("week", week);
        params.put("special", special);

        FinalHttp fh = new FinalHttp();

        fh.post(CommonUrl.MyClinic_Fam_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                map = ResolveJson.R_Action(t.toString());
                handler.sendEmptyMessage(100);
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    public void onClick_back(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.re_doc_words:
                intent = new Intent(FamDoctorSetingActivity.this,
                        FamDoctorWordsActivity.class);
                intent.putExtra("title", "医生寄语");
                intent.putExtra("hint", "愿尽我所学为广大患者提供医疗服务，让患者远离疾病的痛苦");
                intent.putExtra("type", "words");
                startActivity(intent);
                break;
            case R.id.re_doc_special:
                intent = new Intent(FamDoctorSetingActivity.this,
                        FamDoctorWordsActivity.class);
                intent.putExtra("title", "擅长疾病");
                intent.putExtra("hint", "心内科的各种常见病、多发病和疑难病的诊治。包括冠心病、心绞痛。");
                intent.putExtra("type", "special");
                startActivity(intent);
                break;
            case R.id.re_money:
                if (homeoc != null) {
                    intent = new Intent(FamDoctorSetingActivity.this,
                            FamDoctorMoneyActivity.class);
                    int week_min = 10;
                    int week_max = 5000;
                    int month_min = 30;
                    int month_max = 9999;
                    //stone 判断下 医脉的逻辑不变
                    if (YMApplication.sIsYSZS) {
                        week_min = (int) Float.parseFloat(homeoc.getData().getWeek_price().getMin());
                        week_max = (int) Float.parseFloat(homeoc.getData().getWeek_price().getMax());
                        month_min = (int) Float.parseFloat(homeoc.getData().getMonth_price().getMin());
                        month_max = (int) Float.parseFloat(homeoc.getData().getMonth_price().getMax());
                    } else {
                        if (TextUtils.isEmpty(YMApplication.famdocinfo.getWeek())) {
                            week_min = 0;
                            week_max = 0;
                        } else {
                            week_min = (int) Float.parseFloat(YMApplication.famdocinfo.getWeek());
                            week_max = (int) Float.parseFloat(YMApplication.famdocinfo.getWeek());
                        }

                        if (TextUtils.isEmpty(YMApplication.famdocinfo.getMonth())) {
                            month_min = 0;
                            month_max = 0;
                        } else {
                            month_min = (int) Float.parseFloat(YMApplication.famdocinfo.getMonth());
                            month_max = (int) Float.parseFloat(YMApplication.famdocinfo.getMonth());
                        }
                    }

                    intent.putExtra("week_min", week_min);
                    intent.putExtra("week_max", week_max);
                    intent.putExtra("month_min", month_min);
                    intent.putExtra("month_max", month_max);
                    startActivity(intent);
                }
                break;
            case R.id.re_askphone_time:
                if (fam_stat.equals(Constants.FUWU_AUDIT_STATUS_1)) {
                    intent = new Intent(FamDoctorSetingActivity.this,
                            FamDoctorTimeShowActivity.class);
                    intent.putExtra("type", "fam");
                    startActivity(intent);
                } else {
                    intent = new Intent(FamDoctorSetingActivity.this,
                            FamServerTimeActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.re_category_set:
                boolean is_discount = false;
                if (homeoc != null) {
                    is_discount = homeoc.getData().getIs_discount();
                }

                if (is_discount) {
                    intent = new Intent(FamDoctorSetingActivity.this,
                            FamCategroyActivity.class);
                    intent.putExtra("type", homeoc.getData().getIs_set());
                    startActivity(intent);
                } else {
                    ToastUtils.shortToast("权限不够");
                }

                break;
            case R.id.next_btn:
                String words = YMApplication.famdocinfo.getWords();
                String times = YMApplication.famdocinfo.getTime();
                String month = YMApplication.famdocinfo.getMonth();
                String week = YMApplication.famdocinfo.getWeek();
                String special = YMApplication.famdocinfo.getSpecial();
                if (TextUtils.isEmpty(words)) {
                    ToastUtils.shortToast("请编辑医生寄语");
                } else if (TextUtils.isEmpty(special)) {
                    ToastUtils.shortToast("请编辑擅长疾病");
                } else if (TextUtils.isEmpty(week)) {
                    ToastUtils.shortToast("请编辑包周价格");
                } else if (TextUtils.isEmpty(month)) {
                    ToastUtils.shortToast("请编辑包月价格");
                } else if (TextUtils.isEmpty(times)) {
                    ToastUtils.shortToast("请编辑接电话时间");
                } else {
                    if (NetworkUtil.isNetWorkConnected()) {
                        sendData(YMApplication.famdocinfo.getWords(),
                                YMApplication.famdocinfo.getTime(),
                                YMApplication.famdocinfo.getMonth(),
                                YMApplication.famdocinfo.getWeek(),
                                YMApplication.famdocinfo.getSpecial());
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
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

}
