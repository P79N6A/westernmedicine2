package com.xywy.askforexpert.module.main.service.recruit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;

/**
 * 投递过的
 *
 * @author 王鹏
 * @2015-6-17下午8:46:17
 */
public class DeliverActivity extends FragmentActivity {
    private DeliverAllFragment all;
    private DeliverGoneFragment gone;
    private int index;
    private int currentTabIndex;
    private Fragment[] fragments;
    private TextView tv_gone;
    private TextView tv_all;
    private LinearLayout lin_downlist;
    private LinearLayout lin_downing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.mydownloads);

        gone = new DeliverGoneFragment();//简历被查看
        all = new DeliverAllFragment();//全部
        tv_gone = (TextView) findViewById(R.id.tv_online);
        tv_all = (TextView) findViewById(R.id.tv_offline);
        lin_downlist = (LinearLayout) findViewById(R.id.lin_downlist);
        lin_downing = (LinearLayout) findViewById(R.id.lin_downing);
        tv_all.setText("全部");
        tv_gone.setText("简历被查看");

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, all)
                .add(R.id.fragment_container, gone)
                .hide(gone).show(all).commit();

        fragments = new Fragment[]{all, gone};

    }

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.lin_downlist:
                index = 0;
                break;
            case R.id.lin_downing:
                index = 1;
                break;
            case R.id.tv_offline:
                index = 0;
                break;
            case R.id.tv_online:
                index = 1;
                break;
            default:
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
            if (index == 0) {
                tv_gone.setTextColor(getResources().getColor(
                        R.color.gray_text));
                tv_all.setTextColor(getResources().getColor(
                        R.color.doctor_cirlor_name));
                lin_downlist.setBackgroundColor(getResources().getColor(R.color.huise));
                lin_downing.setBackgroundColor(getResources().getColor(R.color.white));
            } else if (index == 1) {
                tv_all.setTextColor(getResources().getColor(
                        R.color.gray_text));
                tv_gone.setTextColor(getResources().getColor(
                        R.color.doctor_cirlor_name));
                lin_downing.setBackgroundColor(getResources().getColor(R.color.huise));

                lin_downlist.setBackgroundColor(getResources().getColor(R.color.white));
            }
            currentTabIndex = index;
        }

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        StatisticalTools.onPause(DeliverActivity.this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatisticalTools.onPause(DeliverActivity.this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
