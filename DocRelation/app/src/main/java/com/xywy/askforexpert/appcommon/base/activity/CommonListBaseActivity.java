package com.xywy.askforexpert.appcommon.base.activity;

import android.support.v4.app.FragmentTransaction;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.base.fragment.CommonListFragment;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.module.docotorcirclenew.adapter.BaseUltimateRecycleAdapter;
import com.xywy.askforexpert.module.docotorcirclenew.model.IRecycleViewModel;

/**
 * 只有一个状态栏和列表的页面直接继承此基类
 */
public abstract class CommonListBaseActivity extends YMBaseActivity {
    protected CommonListFragment listFragment;
    @Override
    protected int getLayoutResId() {
        return R.layout.ativity_common_fragment_view;
    }

    @Override
    protected void beforeViewBind() {

    }
    @Override
    protected void initData() {

    }
    protected void initView() {
        CommonUtils.initSystemBar(this);
        initFragment();
    }


    protected abstract IRecycleViewModel getRecycleViewModel();
    protected abstract BaseUltimateRecycleAdapter getRecycleViewAdapter();

    protected void initFragment() {
        listFragment =  new CommonListFragment();
        listFragment.setRecycleViewModel(getRecycleViewModel());
        listFragment.setAdapter(getRecycleViewAdapter());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, listFragment).commit();
    }
}
