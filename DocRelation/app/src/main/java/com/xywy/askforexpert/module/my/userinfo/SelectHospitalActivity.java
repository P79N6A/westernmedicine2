package com.xywy.askforexpert.module.my.userinfo;

import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.uilibrary.titlebar.ItemClickListener;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.model.HospitalInfo;
import com.xywy.askforexpert.module.my.userinfo.adapter.BaseHospitalAdapter;
import com.xywy.askforexpert.module.my.userinfo.service.UpdatePersonInfo;
import com.xywy.base.view.ProgressDialog;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 医院选择
 *
 * @author 王鹏
 * @2015-5-25下午8:28:36
 */
@Deprecated
public class SelectHospitalActivity extends YMBaseActivity {

    private String type;
    private String province_id;
    private String city_id;
    private HospitalInfo hospitalInfo;
    private BaseHospitalAdapter adapter;
    private ListView lv_content;
    private String hospital;
    private Map<String, String> map = new HashMap<String, String>();



    @Override
    protected int getLayoutResId() {
        return R.layout.activity_common_simple_listview;
    }

    @Override
    protected void beforeViewBind() {

    }

    @Override
    protected void initView() {

        titleBarBuilder.setTitleText("选择医院");
        titleBarBuilder.addItem("确认", new ItemClickListener() {
            @Override
            public void onClick() {
                onSureClick();
            }
        }).build();
        lv_content = (ListView) findViewById(R.id.lv_content);
        type = getIntent().getStringExtra("type");
        province_id = getIntent().getStringExtra("province_id");
        city_id = getIntent().getStringExtra("city_id");
        if (NetworkUtil.isNetWorkConnected()) {
            Update(province_id, city_id);
        } else {
            shortToast( "网络连接失败");
        }

        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                adapter.init();
                adapter.selectionMap.put(arg2, true);
                adapter.notifyDataSetChanged();

            }
        });

    }

    @Override
    protected void initData() {

    }


    public String getHospital(List<String> list) {
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            if (adapter.selectionMap.get(i)) {
                str = list.get(i);
                break;
            }
        }
        return str;
    }

    public void Update(String province_id, String city_id) {
        final ProgressDialog dialog = new ProgressDialog(this, "正在加载中...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.showProgersssDialog();
        AjaxParams params = new AjaxParams();
        String userid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(userid + province_id + city_id
                + Constants.MD5_KEY);
        params.put("command", "getHospital");
        params.put("userid", userid);
        params.put("province_id", province_id);
        params.put("city_id", city_id);
        params.put("sign", sign);
        LogUtils.d("地址" + CommonUrl.DP_COMMON + params);
        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.DP_COMMON, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {

                LogUtils.i("返回数据。。。" + t.toString());
                hospitalInfo = ResolveJson.R_hospatil(t.toString());
                sendEmptyHandlerMsg(100);
                dialog.dismiss();
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                LogUtils.d("失败原因：" + strMsg);
                ToastUtils.shortToast("网络超时");
                super.onFailure(t, errorNo, strMsg);
            }
        });

    }

    public void onSureClick() {

        if (hospitalInfo != null) {
            hospital = getHospital(hospitalInfo.getData());
        }

        if (hospital != null && !"".equals(hospital)) {
            if (type.equals("per_hostpital")) {
                UpdatePersonInfo.updateHospital(province_id,city_id, "hospital",hospital, 200, uiHandler);
            } else if (type.equals("stu_per_hostpital")) {
                UpdatePersonInfo.updateHospital(province_id,city_id, "training_hospital",hospital, 200, uiHandler);

            }
        } else {
            ToastUtils.shortToast("请选择医院");
        }
    }

    @Override
    protected void handleMsg(Message msg) {
        super.handleMsg(msg);

        map = (HashMap<String, String>) msg.obj;
        switch (msg.what) {
            case 100:
                if (hospitalInfo.getData() != null) {
                    adapter = new BaseHospitalAdapter(SelectHospitalActivity.this,
                            hospitalInfo.getData());
                    adapter.init();
                    if (lv_content != null) {
                        lv_content.setAdapter(adapter);
                    }
                }

                break;
            case 200:
                if (map.get("code").equals("0")) {
                    if (type.equals("per_hostpital")) {
                        YMUserService.getPerInfo().getData().setHospital(hospital);

                    } else if (type.equals("stu_per_hostpital")) {
                        YMUserService.getPerInfo().getData().setTraining_hospital(
                                hospital);
                    }
                    YMApplication.getLoginInfo().getData()
                            .setHospital(hospital);
                    ToastUtils.shortToast(map.get("msg"));
                    finish();
                }
                break;
            default:
                break;
        }


    }
}
