package com.xywy.askforexpert.module.main.service.document;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.MyPointInfo;
import com.xywy.askforexpert.module.my.pause.MoneyAdapter;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

/**
 * 充值积分列表
 *
 * @author apple
 */
public class IntegralRechargeactivity extends Activity implements
        OnClickListener {

    final String[] jifen = {"5000积分", "20000积分", "50000积分", "100000积分"};
    final String[] money = {"5", "20", "50", "100"};
    // final String[] money = { "1", "20", "50", "50", "100" };

    private ListView lv_money_list;
    private ImageView iv_back;
    private TextView tv_nom_scrol, tv_my_money;

    private MyPointInfo point;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activty_jifen);

        type = getIntent().getStringExtra("type");
        initView();
        initData();
        initLisener();

    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        lv_money_list = (ListView) findViewById(R.id.lv_money_list);
        tv_nom_scrol = (TextView) findViewById(R.id.tv_nom_scrol);
        tv_my_money = (TextView) findViewById(R.id.tv_my_money);
    }

    private void initData() {
        lv_money_list.setAdapter(new MoneyAdapter(this, jifen, money));

    }

    private void initLisener() {
        iv_back.setOnClickListener(this);
        lv_money_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int posion,
                                    long arg3) {
                switch (posion) {
                    case 0:
                        StatisticalTools.eventCount(IntegralRechargeactivity.this, "scorerecharge5");
                        break;
                    case 1:
                        StatisticalTools.eventCount(IntegralRechargeactivity.this, "scorerecharge20");
                        break;
                    case 2:
                        StatisticalTools.eventCount(IntegralRechargeactivity.this, "scorerecharge50");
                        break;
                    case 3:
                        StatisticalTools.eventCount(IntegralRechargeactivity.this, "scorerecharge100");
                        break;

                    default:
                        break;
                }

                if (point != null) {
                    if (!TextUtils.isEmpty(point.getData().getBalance())) {
                        String purse = point.getData().getBalance();

                        String newPurse = purse.replaceAll(",", "");

                        DLog.i("abc", newPurse);

                        if (Double.valueOf(newPurse) >= Double
                                .valueOf(money[posion])) {
                            Intent intent = new Intent(
                                    IntegralRechargeactivity.this,
                                    IntegralPayActivity.class);
                            intent.putExtra("posion", posion);
                            intent.putExtra("point", point.getData()
                                    .getTotal_score());
                            intent.putExtra("money", newPurse);
                            if (!TextUtils.isEmpty(type)) {
                                if ("0".equals(type)) {
                                    IntegralRechargeactivity.this
                                            .startActivityForResult(intent, 100);
                                } else {
                                    IntegralRechargeactivity.this
                                            .startActivityForResult(intent, 101);
                                }
                            }

                        } else {
                            ToastUtils.shortToast(
                                    "钱包余额不足");
                        }
                    } else {
                        ToastUtils.shortToast(
                                "获取余额失败");

                    }

                }

            }

        });
    }

    /**
     * 获取积分 和余额数据
     */
    public void getData() {
        String userid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(userid + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("command", "mypoint");
        params.put("userid", userid);
        params.put("ismoney", "1");
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();

        fh.post(CommonUrl.ScoresPointUrl, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                Gson gson = new Gson();
                try {
                    point = gson.fromJson(t.toString(), MyPointInfo.class);
                    if (point != null) {
                        if ("0".equals(point.getCode())) {
                            tv_nom_scrol.setText(point.getData().getTotal_score());
                            tv_my_money.setText(point.getData().getBalance());
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method
                super.onFailure(t, errorNo, strMsg);
                ToastUtils.shortToast("网络链接超时");

            }
        });

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.iv_back:
                this.finish();

                break;

            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
        // TODO Auto-generated method stub


        if (NetworkUtil.isNetWorkConnected()) {
            getData();
        } else {
            ToastUtils.shortToast("网络连接失败");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK & requestCode == 100) {
            finish();
        }

    }

    @Override
    protected void onPause() {
        StatisticalTools.onPause(this);
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}