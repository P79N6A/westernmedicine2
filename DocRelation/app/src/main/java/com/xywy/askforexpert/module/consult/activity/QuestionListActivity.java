package com.xywy.askforexpert.module.consult.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.model.consultentity.ConsultPagerTabBean;
import com.xywy.askforexpert.model.consultentity.ConsultPagerTabItemBean;
import com.xywy.askforexpert.module.consult.fragment.ConsultItemFragment;
import com.xywy.askforexpert.widget.view.SelectBasePopupWindow;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import java.util.List;

/**
 * Created by jason on 2018/10/9.
 */

public class QuestionListActivity extends YMBaseActivity implements ConsultItemFragment.IModifyMsgTip {
//    @Bind(R.id.fl_container)
//    FrameLayout fl_container;
    private SelectBasePopupWindow mPopupWindow;

    private View lay1;
    private View lay2;
    private View popRoot;
    private List<Fragment> fragments;
    private static final String STR_QUESTIONS = "问题库";
    private List<ConsultPagerTabBean> consultPagerTabBeanList;
    private ConsultItemFragment consultItemFragment;


    @Override
    protected void initView() {
        initTitleBar();
        ConsultPagerTabItemBean itemBean = new ConsultPagerTabItemBean(ConsultPagerTabItemBean.TYPE_QUESTIONS, STR_QUESTIONS);
        consultItemFragment = ConsultItemFragment.newInstance(new ConsultPagerTabBean(STR_QUESTIONS, itemBean), this, true,0);
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_container, consultItemFragment).show(consultItemFragment).commitAllowingStateLoss();

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_question_list;
    }

    private void initTitleBar() {
        titleBarBuilder.setTitleText(getString(R.string.online_consultation));

        titleBarBuilder.addItem("设置",new ItemClickListener() {
            @Override
            public void onClick() {
                showPop();
            }
        }).build();
    }

    private void showPop() {
        if (mPopupWindow == null) {
            mPopupWindow = new SelectBasePopupWindow(true, this);
            popRoot = View.inflate(getBaseContext(), R.layout.pop_layout_message_setting, null);
            lay1 = popRoot.findViewById(R.id.lay1);
            lay2 = popRoot.findViewById(R.id.lay2);
            lay1.setOnClickListener(mPopOnClickListener);
            lay2.setOnClickListener(mPopOnClickListener);
        }
        if (!mPopupWindow.isShowing()) {
//            mPopupWindow.init(popRoot).showAtLocation(this.getWindow().getDecorView(), Gravity.TOP | Gravity.RIGHT, (AppUtils.dpToPx(48, getResources()) - 24) / 2 + AppUtils.dpToPx(5, getResources()) - 30 - 27, AppUtils.dpToPx(48, getResources()) + YMApplication.getStatusBarHeight() - 30);
            mPopupWindow.init(popRoot).showAtLocation(this.getWindow().getDecorView(), Gravity.TOP | Gravity.RIGHT, AppUtils.dpToPx(5, getResources()), AppUtils.dpToPx(38, getResources()) + YMApplication.getStatusBarHeight());
        }
    }

    /**
     * pop监听器 种族
     */
    private View.OnClickListener mPopOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mPopupWindow.dismiss();
            switch (view.getId()) {
                case R.id.lay1:
                    ConsultAnsweredListActivity.startActivity(QuestionListActivity.this);
                    break;
                case R.id.lay2:
                    //留言设置 stone
                    StatisticalTools.eventCount(QuestionListActivity.this, Constants.MESSAGESETTING);

                    startActivity(new Intent(QuestionListActivity.this, MessageSettingActivity.class));
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public void updateMsgCount(int tab, String count) {

    }

    @Override
    public void loadProcessingData() {

    }

}
