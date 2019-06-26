package com.xywy.askforexpert.module.my.userinfo;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;

import butterknife.OnClick;

/**
 * 认证审核状态(审核中, 不通过 ,被驳回) stone
 */
public class CheckStateActivity extends YMBaseActivity {
    private String type;
    private String title;

    private ImageView iv_icon;
    private TextView tv_notice;
    private TextView tv_go;

    public static void startActivity(Context context, String type, String title) {
        Intent intent = new Intent(context, CheckStateActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @OnClick({R.id.btn1, R.id.tv_go})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.tv_go:
                //stone 重新认证
                intent = new Intent(CheckStateActivity.this, ApproveInfoActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.checkstate;
    }

    @Override
    protected void beforeViewBind() {
        type = getIntent().getStringExtra("type");
        title = getIntent().getStringExtra("title");
    }

    @Override
    protected void initView() {
        hideCommonBaseTitle();

        ((TextView) findViewById(R.id.tv_title)).setText(title);

        tv_notice = (TextView) findViewById(R.id.tv_notice);
        tv_go = (TextView) findViewById(R.id.tv_go);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);


        if (type.equals("checking")) {
            tv_notice.setText(getString(R.string.checking));
            iv_icon.setImageResource(R.drawable.renzhengzhong_icon);
            tv_go.setVisibility(View.GONE);
        } else if (type.equals("check_err")) {
            if (YMApplication.getUserInfo() != null
                    && YMApplication.getUserInfo().details != null
                    && !TextUtils.isEmpty(YMApplication.getUserInfo().details.remark)) {
                tv_notice.setText("很抱歉，您的专业审核未通过\n" + "未通过原因：" + YMApplication.getUserInfo().details.remark);
            } else {
                tv_notice.setText(type.equals("check_err") ? "很抱歉，您的专业审核未通过" : "很抱歉，您的专业审核被驳回");
            }
            iv_icon.setImageResource(R.drawable.butongguo_icon);
            tv_go.setVisibility(View.GONE);
        } else if (type.equals("check_reject")) {
            if (YMApplication.getUserInfo() != null
                    && YMApplication.getUserInfo().details != null
                    && !TextUtils.isEmpty(YMApplication.getUserInfo().details.remark)) {
                tv_notice.setText("很抱歉，您的专业审核被驳回\n" + "被驳回原因：" + YMApplication.getUserInfo().details.remark);
            } else {
                tv_notice.setText(type.equals("check_err") ? "很抱歉，您的专业审核未通过" : "很抱歉，您的专业审核被驳回");
            }
            iv_icon.setImageResource(R.drawable.bohui_icon);
            tv_go.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void initData() {

    }
}
