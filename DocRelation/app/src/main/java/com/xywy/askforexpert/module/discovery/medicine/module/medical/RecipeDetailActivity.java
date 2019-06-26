package com.xywy.askforexpert.module.discovery.medicine.module.medical;

import android.os.Bundle;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.base.XywyBaseActivity;
/**
 * 处方详情/确认处方
 * stone
 * 2017/11/1 下午4:01
 */
public class RecipeDetailActivity extends XywyBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        //初始化统一bar，将系统栏的背景设置成透明色
        CommonUtils.initSystemBar(this);
        RecipeDetailActivityFragment fragment = new RecipeDetailActivityFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
        initTitle();
    }
    private void initTitle() {
        titleBarBuilder.setBackGround(R.drawable.toolbar_bg_no_alpha);
        titleBarBuilder.setTitleText("确认处方")
                .setBackIcon("", R.drawable.base_back_btn_selector_new, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        showCommonBaseTitle();
    }

}
