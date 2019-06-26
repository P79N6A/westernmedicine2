package com.xywy.askforexpert.module.docotorcirclenew.activity;

import android.content.Context;
import android.content.Intent;

import com.xywy.askforexpert.appcommon.base.activity.CommonListBaseActivity;
import com.xywy.askforexpert.module.docotorcirclenew.adapter.BaseUltimateRecycleAdapter;
import com.xywy.askforexpert.module.docotorcirclenew.adapter.CircleVisitHistoryAdapter;
import com.xywy.askforexpert.module.docotorcirclenew.model.DoctorCircleModelFactory;
import com.xywy.askforexpert.module.docotorcirclenew.model.IRecycleViewModel;

/**
 * 医圈动态消息
 *
 * @author LG
 */
public class DoctorMessageNotify extends CommonListBaseActivity {
    private String type;
    public  static void startActivity(Context context, String type){
        Intent intent = new Intent(context, DoctorMessageNotify.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        titleBarBuilder.setTitleText("动态通知");
    }


    @Override
    protected IRecycleViewModel getRecycleViewModel() {
        return DoctorCircleModelFactory.newVisitHistoryModel(listFragment, this, type);
    }

    @Override
    protected BaseUltimateRecycleAdapter getRecycleViewAdapter() {
        return new CircleVisitHistoryAdapter(this);
    }

    @Override
    protected void beforeViewBind() {
        type = getIntent().getStringExtra("type");
    }

}
