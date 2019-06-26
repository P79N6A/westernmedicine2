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
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.AddNumPerInfo;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xywy.askforexpert.appcommon.old.Constants.FUWU_AUDIT_STATUS_3;

/**
 * 预约转诊 设置页面 stone
 *
 * @author 王鹏
 * @2015-5-20上午8:29:35
 */
public class AddnumberSettingActivity extends Activity {

    private static final String TAG = "AddnumberSettingActivity";
    // private LinearLayout next_btn;
    private Map<String, String> map = new HashMap<String, String>();
    private String str = YMApplication.getLoginInfo().getData().getXiaozhan().getYuyue();
    private AddNumPerInfo addperinfo;
    private String[] type = new String[]{"普通", "专家", "特需", "专科", "会诊", "夜诊"};

    private Handler hander = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 100:
                    if (map.get("code").equals("0")) {
                        // findViewById(R.id.next_btn).setVisibility(View.GONE);
                        ToastUtils.shortToast(
                                "您的服务申请已经提交，我们将在24小时内审核，请耐心等待！");
                        YMApplication.getLoginInfo().getData().getXiaozhan().setYuyue(Constants.FUWU_AUDIT_STATUS_0);
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();

                    } else {
                        ToastUtils.shortToast(
                                map.get("msg"));
                    }
                    break;
                case 200:
                    //stone 医生助手独自判断  数据返回没有 status ???
                    if (YMApplication.sIsYSZS) {
                        if (addperinfo != null && !TextUtils.isEmpty(addperinfo.getCode()) && addperinfo.getCode().equals("0")) {
                            fuzhi();
                        }
                    } else {
                        if (addperinfo != null && !TextUtils.isEmpty(addperinfo.getCode()) && addperinfo.getCode().equals("0")) {
                            fuzhi();
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
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.add_number_setting);
        // next_btn = (LinearLayout) findViewById(R.id.next_btn);
        if (Constants.FUWU_AUDIT_STATUS_1.equals(str) || FUWU_AUDIT_STATUS_3.equals(str)) {
            findViewById(R.id.next_btn).setVisibility(View.GONE);
            getDataInfo();
        }
        ((TextView) findViewById(R.id.tv_title)).setText("预约转诊");
    }

    public void fuzhi() {
        //stone 添加判空
        if (addperinfo != null && addperinfo.getData() != null && addperinfo.getData().getList() != null) {


            YMApplication.addnuminfo.setPlus_range(addperinfo.getData().getList()
                    .getPlus_range());// 治疗范围
            YMApplication.addnuminfo.setPlusline(addperinfo.getData().getList()
                    .getPlusline());
            if (!TextUtils.isEmpty(addperinfo.getData().getList().getIstrue())
                    && "2".equals(addperinfo.getData().getList().getIstrue())) {
                YMApplication.addnuminfo.setIsttrue_position(0);
            } else if (!TextUtils.isEmpty(addperinfo.getData().getList().getIstrue())
                    && "1".equals(addperinfo.getData().getList().getIstrue())) {
                YMApplication.addnuminfo.setIsttrue_position(1);
            }
            // YMApplication.addnuminfo.setIsttrue_position(Integer.parseInt(addperinfo.getData().getList().getIstrue()));
            if (!TextUtils.isEmpty(addperinfo.getData().getList().getTreatlimit())
                    && "2".equals(addperinfo.getData().getList().getTreatlimit())) {
                YMApplication.addnuminfo.setTreatlimt_position(0);
            } else if (!TextUtils.isEmpty(addperinfo.getData().getList().getTreatlimit())
                    && "1".equals(addperinfo.getData().getList().getTreatlimit())) {
                YMApplication.addnuminfo.setTreatlimt_position(1);
            }
            // Integer.parseInt()-1

            List<String> list = new ArrayList<String>();
            for (int i = 0; i < 21; i++) {
                list.add("");
            }
            List<AddNumPerInfo> week = addperinfo.getData().getList().getTime();
            if (week != null) {
                if (week.size() > 0) {
                    for (int i = 0; i < week.size(); i++) {
                        int position = 3
                                * (Integer.parseInt(week.get(i).getWeek()) - 1)
                                + (Integer.parseInt(week.get(i).getHalfday()) - 1);
                        list.remove(position);
                        list.add(position,
                                type[Integer.parseInt(week.get(i).getType())]);
                    }
                }


            }
            YMApplication.addnuminfo.setList(list);
        }
    }

    /**
     * 申请开通以后 获取基础信息
     */
    public void getDataInfo() {
        String uid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(uid + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("userid", uid);
        params.put("sign", sign);
        params.put("command", "yuyuedoc");

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.MyClinic_State_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        DLog.i(TAG, "预约专家返回数据" + t.toString());
                        Gson gson = new Gson();
                        addperinfo = gson.fromJson(t.toString(),
                                AddNumPerInfo.class);
                        hander.sendEmptyMessage(200);
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

    public void sendData() {
        String uid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(uid + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("userid", uid);
        params.put("istrue", YMApplication.addnuminfo.getIstrue());
        params.put("movephone", YMApplication.getLoginInfo().getData()
                .getPhone());
        params.put("treatlimit", YMApplication.addnuminfo.getTreatlimt());
        params.put("plus_range", YMApplication.addnuminfo.getPlus_range());
        params.put("detail", YMApplication.addnuminfo.getDetail());
        params.put("plusline", YMApplication.addnuminfo.getPlusline());
        params.put("arealimit", YMApplication.addnuminfo.getArealimit());
        params.put("command", "subscribe");
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.MyClinic_State_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        // TODO Auto-generated method stub
                        DLog.i(TAG, "预约转诊开通返回数据" + t.toString());
                        map = ResolveJson.R_Action(t.toString());
                        hander.sendEmptyMessage(100);
                        super.onSuccess(t);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        // TODO Auto-generated method stub
                        DLog.i(TAG, "预约转诊错误日子" + strMsg);
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
            case R.id.re_number_time:

                intent = new Intent(AddnumberSettingActivity.this,
                        NumberTimeSetActivity.class);
                intent.putExtra("istype", str);
                startActivity(intent);

                break;
            case R.id.re_name:// 治疗范围

                intent = new Intent(AddnumberSettingActivity.this,
                        AddNumberEditAcitvity.class);
                intent.putExtra("title", "治疗范围");
                intent.putExtra("type", "plus_range");
                intent.putExtra("istype", str);
                startActivity(intent);
                break;
            case R.id.re_get_number:// 领取挂号方式

                intent = new Intent(AddnumberSettingActivity.this,
                        AddNumberEditAcitvity.class);
                intent.putExtra("title", "领取预约单方式");
                intent.putExtra("type", "plusline");
                intent.putExtra("istype", str);
                startActivity(intent);

                break;
            case R.id.re_demand:// 就诊要
                intent = new Intent(AddnumberSettingActivity.this,
                        AddNumberChoose.class);
                intent.putExtra("title", "就诊要求");
                intent.putExtra("type", "demand");
                intent.putExtra("istype", str);
                startActivity(intent);

                break;
            case R.id.re_order_action:

                intent = new Intent(AddnumberSettingActivity.this,
                        AddNumberChoose.class);
                intent.putExtra("title", "确认预约方式");
                intent.putExtra("type", "order");
                intent.putExtra("istype", str);
                startActivity(intent);

                break;
            case R.id.next_btn:
                String plus_range = YMApplication.addnuminfo.getPlus_range();
                String detail = YMApplication.addnuminfo.getDetail();
                String plusline = YMApplication.addnuminfo.getPlusline();
                String treatlimit = YMApplication.addnuminfo.getTreatlimt();
                String istrue = YMApplication.addnuminfo.getIstrue();

                if (TextUtils.isEmpty(plus_range)) {
                    ToastUtils.shortToast("请编辑治疗范围");
                } else if (TextUtils.isEmpty(detail)) {
                    ToastUtils.shortToast("请编辑预约门诊时间");
                } else if (TextUtils.isEmpty(plusline)) {
                    ToastUtils.shortToast(
                            "请编辑领取挂单单号方式");
                } else if (TextUtils.isEmpty(treatlimit)) {
                    ToastUtils.shortToast("请编辑就诊要求");
                } else if (TextUtils.isEmpty(istrue)) {
                    ToastUtils.shortToast("请编辑预约方式");
                } else

                {
                    if (NetworkUtil
                            .isNetWorkConnected()) {
                        sendData();
                    } else {
                        ToastUtils.shortToast("网络连接失败");

                    }
                }

                break;
            case R.id.re_stop_time:// 停诊要求
                if (str.equals(Constants.FUWU_AUDIT_STATUS_1)) {
                    intent = new Intent(AddnumberSettingActivity.this,
                            NumberStopTimeListActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtils.shortToast("请先开通");
                }

                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatisticalTools.onResume(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub

        StatisticalTools.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
    }
}
