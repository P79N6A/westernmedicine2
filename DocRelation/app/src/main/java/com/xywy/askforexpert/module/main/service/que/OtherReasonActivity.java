package com.xywy.askforexpert.module.main.service.que;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 投诉原因
 *
 * @author shihao
 *         2015-5-14
 */
public class OtherReasonActivity extends Activity {

    private EditText editText;

    private String resonString;

    private InputMethodManager manager;

    private String id;

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        id = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        setContentView(R.layout.activity_other_reason);

        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        editText = (EditText) findViewById(R.id.edit_reson);

    }

    public void onResonClickListener(View v) {
        switch (v.getId()) {
            case R.id.btn_other_back:
                finish();
                break;

            case R.id.btn_ok:
                resonString = editText.getText().toString();
                if (resonString.length() == 0) {
                    ToastUtils.shortToast("您输入的内容不能为空");
                } else {
                    if (resonString.length() < 5) {
                        ToastUtils.shortToast("投诉理由不得少于5个汉字");
                    } else {
                        reqHttp(resonString);
                    }
                }
                break;

            case R.id.btn_chanel:
                finish();
                break;
        }
    }

    private void reqHttp(String content) {
        AjaxParams params = new AjaxParams();

        params.put("userid", YMApplication.getLoginInfo().getData().getPid());
        params.put("id", id);
        params.put("reason", content);
        params.put("sign", MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid() + id
                + Constants.MD5_KEY));
        FinalHttp fh = new FinalHttp();

        fh.post(CommonUrl.QUE_OTHER_REASON, params, new AjaxCallBack() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                super.onFailure(t, errorNo, strMsg);
            }

            @Override
            public void onLoading(long count, long current) {
                // TODO Auto-generated method stub
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    String msg = jsonObject.getString("msg");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        ToastUtils.shortToast(msg);
                        SharedPreferences.Editor edit = getSharedPreferences(
                                "isskip", MODE_PRIVATE).edit();
                        edit.putBoolean("skip", true);
                        edit.putString("type", type);
                        edit.commit();
                        Intent intent = new Intent(
                                OtherReasonActivity.this,
                                QueDetailActivity.class);
                        setResult(2017, intent);
                        finish();
                    } else {
                        ToastUtils.shortToast(msg);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

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
