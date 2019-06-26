package com.xywy.askforexpert.module.discovery.medicine.module.medical;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.MedicineEntity;
import com.xywy.base.XywyBaseActivity;

public class MedicineSettingActivity extends XywyBaseActivity {

    public static String MEDICINE_ENTITY = "medicine_entity";

    private MedicineSettingActivityFragment mFragment;


    public static void startActivity(Context context, MedicineEntity entity) {
        Intent intent = new Intent(context, MedicineSettingActivity.class);
        intent.putExtra(MEDICINE_ENTITY, entity);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_setting);
        //初始化统一bar，将系统栏的背景设置成透明色
        CommonUtils.initSystemBar(this);
        titleBarBuilder.setBackGround(R.drawable.toolbar_bg_no_alpha);
        titleBarBuilder.setTitleText("药品设置");
        titleBarBuilder.setBackIcon("", R.drawable.base_back_btn_selector_new, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mFragment = (MedicineSettingActivityFragment) getFragmentManager().findFragmentById(R.id.fragment);
        MedicineEntity mEntity = (MedicineEntity) getIntent().getSerializableExtra(MEDICINE_ENTITY);
        if(mFragment!=null && mEntity!=null) {
            initView(mEntity);
            mFragment.setmEntity(mEntity);
        }
    }

    private void initView(MedicineEntity entity) {
        setTitle(entity.getGyssku());
    }

}
