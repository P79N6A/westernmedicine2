package com.xywy.askforexpert.module.main.service.document;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.enctools.RSATools;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.base.view.ProgressDialog;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.Map;

/**
 * 积分充值
 *
 * @author apple
 */
public class IntegralPayActivity extends Activity implements OnClickListener {

    final String[] jifen = {"5000", "20000", "50000", "100000"};
    final String[] money = {"5", "20", "50", "100"};
    // final String[] money = { "1", "20", "50", "50", "100" };

    private ImageView back;
    private TextView tv_title, tv_jifen, tv_money, tv_nom_scrol, tv_my_money;

    private int posion;
    private String point, mymoney;
    private Button commet;
    private EditText edit_password;
    private Map<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);

        setContentView(R.layout.activity_integralpay);
        posion = getIntent().getIntExtra("posion", 0);
        point = getIntent().getStringExtra("point");
        mymoney = getIntent().getStringExtra("money");
        initView();
        initData();
        initLisener();
        init(point, mymoney);
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_jifen = (TextView) findViewById(R.id.tv_jifen);
        commet = (Button) findViewById(R.id.next_btn);
        tv_nom_scrol = (TextView) findViewById(R.id.tv_nom_scrol);
        tv_my_money = (TextView) findViewById(R.id.tv_my_money);
        edit_password = (EditText) findViewById(R.id.edit_password);

    }

    private void init(String pString, String mString) {
        if (!TextUtils.isEmpty(pString)) {
            tv_nom_scrol.setText(pString);
        }
        if (!TextUtils.isEmpty(mString)) {
            tv_my_money.setText(mString);
        }
    }

    private void initData() {
        tv_title.setText("充值" + jifen[posion] + "积分");
        tv_jifen.setText(jifen[posion] + "积分");
        tv_money.setText(money[posion] + "元");

    }

    private void initLisener() {
        back.setOnClickListener(this);
        commet.setOnClickListener(this);
    }

    public void sendData(final String money, final String points,
                         String password) {
        final ProgressDialog dialog = new ProgressDialog(this, "兑换中请稍后");
        dialog.setCanceledOnTouchOutside(false);
        dialog.showProgersssDialog();

        String userid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(userid + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("command", "addpoint");
        params.put("userid", userid);
        params.put("money", money);
        params.put("point", points);
        params.put("password", password);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();

        fh.post(CommonUrl.ScoresPointUrl, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                Gson gson = new Gson();
                map = ResolveJson.R_Action(t.toString());

                if (map != null) {
                    if ("0".equals(map.get("code"))) {
                        int dp = Integer.valueOf(point);
                        int ndp = Integer.valueOf(points);
                        dp = dp + ndp;
                        java.text.DecimalFormat df = new java.text.DecimalFormat(
                                "#.00");
                        Double mo = Double.valueOf(mymoney);
                        Double nmo = Double.valueOf(money);
                        mo = mo - nmo;
                        init("" + dp, "" + mo);
                        edit_password.setText("");
                        ToastUtils.shortToast("充值成功＋" + points + "积分");
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        ToastUtils.shortToast(map
                                .get("msg").toString());
                    }

                }

                dialog.dismiss();
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                ToastUtils.shortToast("网络链接超时");
                super.onFailure(t, errorNo, strMsg);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                this.finish();

                break;
            case R.id.next_btn:// 提交
                StatisticalTools.eventCount(this, "scoreadmit");
                String password = edit_password.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    ToastUtils.shortToast("请输入账号密码");
                } else {
                    NewDialog(password);
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatisticalTools.onResume(this);


    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        StatisticalTools.onPause(this);
        super.onPause();
    }

    public void NewDialog(final String password) {

        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(
                IntegralPayActivity.this);
        dialog.setTitle("温馨提示");

        dialog.setMessage("请确认要兑换积分，兑换成功之后,积分将不能兑换成金钱");

        dialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ((DialogInterface) dialogs).dismiss();
                        sendData(money[posion], jifen[posion],
                                RSATools.strRSA(password));
//						Intent intent = new Intent();
//						setResult(RESULT_OK, intent);
//						finish();

                    }
                });
        dialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        dialog.create().show();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
