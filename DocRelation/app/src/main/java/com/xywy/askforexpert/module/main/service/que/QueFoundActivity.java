package com.xywy.askforexpert.module.main.service.que;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.module.main.guangchang.PromotionActivity;

public class QueFoundActivity extends YMBaseActivity {

    private RelativeLayout jinJi, daTi;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_que_found;
    }

    @Override
    protected void beforeViewBind() {

    }

    @Override
    protected void initView() {
        jinJi = (RelativeLayout) findViewById(R.id.rl_promotion_application);
        daTi = (RelativeLayout) findViewById(R.id.rl_que_performance);
    }

    @Override
    protected void initData() {
        titleBarBuilder.setTitleText("发现");
        if (YMApplication.getLoginInfo().getData().getIsjob().equals("2")) {
            jinJi.setVisibility(View.GONE);
            daTi.setVisibility(View.GONE);
        } else {
            jinJi.setVisibility(View.VISIBLE);
            daTi.setVisibility(View.VISIBLE);
        }

    }

    public void onReplyClickListener(View v) {
        Intent intent = null;
        switch (v.getId()) {

            case R.id.rl_promotion_application:
                StatisticalTools.eventCount(QueFoundActivity.this, "Promotion");
                intent = new Intent(QueFoundActivity.this, PromotionActivity.class);
                startActivity(intent);
                //PromotionNewActivity.start(QueFoundActivity.this);
                break;
            case R.id.rl_que_performance:
                StatisticalTools.eventCount(QueFoundActivity.this, "performance");
                intent = new Intent(QueFoundActivity.this, QuePerActivity.class);
                intent.putExtra("isfrom", "答题绩效");
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
