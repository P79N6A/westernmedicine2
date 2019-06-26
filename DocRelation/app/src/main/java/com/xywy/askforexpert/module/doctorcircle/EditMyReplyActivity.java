package com.xywy.askforexpert.module.doctorcircle;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.module.main.service.que.QueDetailActivity;
import com.xywy.base.view.ProgressDialog;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名称：D_Platform
 * 类名称：EditMyReply
 * 类描述：编辑我的回复
 * 创建人：shihao
 * 创建时间：2015-6-23 上午9:33:54
 * 修改备注：
 */
@Deprecated
public class EditMyReplyActivity extends YMBaseActivity {

    private EditText content;
    private String cotentString;
    private String str1, str2, id, type, rid;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_edit_myreply;
    }

    @Override
    protected void beforeViewBind() {
        str1 = getIntent().getStringExtra("analyse");
        str2 = getIntent().getStringExtra("suggest");
        id = getIntent().getStringExtra("id");
        rid = getIntent().getStringExtra("rid");
        type = getIntent().getStringExtra("type");
    }

    @Override
    protected void initView() {
        hideCommonBaseTitle();
        content = (EditText) findViewById(R.id.edit_content);

    }

    @Override
    protected void initData() {


        content.setText(str2);

    }

    public void onEditReplyClickListener(View v) {
        switch (v.getId()) {
            case R.id.btn_edit_reply_back:
                finish();
                break;

            case R.id.btn_chanel:
                finish();
                break;
            case R.id.btn_ok:
                cotentString = content.getText().toString().trim();
                if (!"".equals(cotentString)) {
                    if (str2.equals(cotentString)) {
                        Toast.makeText(EditMyReplyActivity.this, "您还没有修改哦，修改后才能提交", Toast.LENGTH_SHORT).show();
                    } else {
                        submit(cotentString, "");
                    }
                } else {
                    ToastUtils.shortToast( "输入内容不能为空！");
                }

                break;
        }
    }

    private void submit(String str1, String str2) {
        final ProgressDialog dialog = new ProgressDialog(this, "发送中...");
        dialog.showProgersssDialog();
        AjaxParams params = new AjaxParams();
        params.put("userid", YMApplication.getLoginInfo().getData().getPid());
        params.put("id", id);
        params.put("rid", rid);
        params.put("tag", type);
        params.put("suggest", str1);
        params.put("analyse", "");
        params.put(
                "sign",
                MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid() + id + type
                        + Constants.MD5_KEY));
        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.QUE_SEND_REPLY, params, new AjaxCallBack() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }

            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                dialog.closeProgersssDialog();
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {

                        Intent intent = new Intent(
                                EditMyReplyActivity.this,
                                QueDetailActivity.class);
                        setResult(2016, intent);
                        ToastUtils.shortToast( "修改成功");
                        finish();
                    } else {
                        Toast.makeText(EditMyReplyActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
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
