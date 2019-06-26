package com.xywy.askforexpert.module.docotorcirclenew.activity;

import android.support.v4.app.FragmentTransaction;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.module.docotorcirclenew.fragment.tab.MyCircleVIsitHistoryTabFragment;

/**
 * 我的未读历史通知
 *
 * @author LG
 */
public class MyUnReadMessagesActivity extends YMBaseActivity {

    private final String TAG = "MyUnReadMessagesActivity";


    @Override
    protected int getLayoutResId() {
        return R.layout.ativity_common_fragment_view;
    }

    @Override
    protected void beforeViewBind() {

    }

    protected void initView() {
        CommonUtils.initSystemBar(this);
        titleBarBuilder.setTitleText("动态通知");
        initFragment();
    }

    @Override
    protected void initData() {

    }

    protected void initFragment() {
        MyCircleVIsitHistoryTabFragment fragment=new MyCircleVIsitHistoryTabFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragment).commit();
    }
}
