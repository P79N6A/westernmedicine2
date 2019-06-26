package com.xywy.askforexpert.module.consult.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.module.consult.fragment.HistoryAnswerPagerFragment;

import butterknife.Bind;

/**
 * 历史回复 stone  移植yimai 新版本新增tab切换
*/
public class ConsultAnsweredListActivity extends YMBaseActivity implements HistoryAnswerPagerFragment.ITip{

    @Bind(R.id.tv_activity_consult_tip)
    TextView tvTip;

    private String mStrTitle;
    private HistoryAnswerPagerFragment pagerFragment;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ConsultAnsweredListActivity.class));
    }

    @Override
    protected void initView() {
        mStrTitle =getString(R.string.question_history_reply);
        initTitleBar();
        if (pagerFragment == null) {
            pagerFragment = new HistoryAnswerPagerFragment();
            pagerFragment.setiTip(this);
            displayFragment(pagerFragment);
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_consult_history;
    }

    private void displayFragment(Fragment fragment) {
        if (!fragment.isAdded()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fl_container, fragment);
            transaction.commitAllowingStateLoss();
        }
    }

    private void initTitleBar() {
        titleBarBuilder.setTitleText(mStrTitle);
    }

    @Override
    public void showTip(String text) {
        tvTip.setText(text);
    }
}
