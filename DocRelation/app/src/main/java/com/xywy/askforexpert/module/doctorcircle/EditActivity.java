package com.xywy.askforexpert.module.doctorcircle;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
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
 * 项目名称：D_Platform
 * 类名称：EditActivity
 * 类描述：编辑问答帖子
 * 创建人：shihao
 * 创建时间：2015-6-23 上午9:33:13
 * 修改备注：
 */
public class EditActivity extends YMBaseActivity {

    private EditText editText;

    private String resonString;

    private InputMethodManager manager;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_edit;
    }

    @Override
    protected void beforeViewBind() {

    }

    @Override
    protected void initView() {
        hideCommonBaseTitle();
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        editText = (EditText) findViewById(R.id.edit_content);

    }

    @Override
    protected void initData() {

    }


    public void onEditClickListener(View v) {
        switch (v.getId()) {
            case R.id.btn_edit_back:
                finish();
                break;

            case R.id.btn_ok:
                resonString = editText.getText().toString().trim();
                reqHttp(resonString);
                break;

            case R.id.btn_chanel:
                finish();
                break;
        }
    }

    private void reqHttp(String content) {
        AjaxParams params = new AjaxParams();

        params.put("userid", YMApplication.getLoginInfo().getData().getPid());
        params.put("content", content);
        params.put("sign", MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid()
                + Constants.MD5_KEY));
        FinalHttp fh = new FinalHttp();

        fh.post(CommonUrl.QUE_EDIT_ADD, params, new AjaxCallBack() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                super.onFailure(t, errorNo, strMsg);
            }

            @Override
            public void onLoading(long count, long current) {
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
                        Intent intent = new Intent(EditActivity.this, FastReplyActivity.class);
                        setResult(3001, intent);
                        finish();
                    } else {
                        ToastUtils.shortToast(msg);
                    }
                } catch (JSONException e) {
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
