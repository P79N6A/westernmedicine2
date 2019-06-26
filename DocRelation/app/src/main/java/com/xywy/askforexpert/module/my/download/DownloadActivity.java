package com.xywy.askforexpert.module.my.download;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;

/**
 * 我的下载
 *
 * @author 王鹏
 * @2015-4-23上午10:40:50
 */
public class DownloadActivity extends FragmentActivity {
    private DownListFragment listFragment;
    private DownloadingFragment loadingFragment;
    private int index;
    private int currentTabIndex;
    private Fragment[] fragments;
    private TextView tv_online;
    private TextView tv_offline;

    private LinearLayout lin_downlist;
    private LinearLayout lin_downing;
    String type_commed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);

        setContentView(R.layout.mydownloads);
        MobclickAgent.openActivityDurationTrack(false);
        listFragment = new DownListFragment();
        loadingFragment = new DownloadingFragment();
        type_commed = getIntent().getStringExtra("type_commed");
        tv_online = (TextView) findViewById(R.id.tv_online);// 正在下载
        tv_offline = (TextView) findViewById(R.id.tv_offline);
        lin_downlist = (LinearLayout) findViewById(R.id.lin_downlist);
        lin_downing = (LinearLayout) findViewById(R.id.lin_downing);// 正在下载
        Bundle bundle = new Bundle();
        bundle.putString("type_commed", type_commed);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, listFragment)
                .add(R.id.fragment_container, loadingFragment)
                .hide(loadingFragment).show(listFragment).commit();

        fragments = new Fragment[]{listFragment, loadingFragment};
        listFragment.setArguments(bundle);
        loadingFragment.setArguments(bundle);
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
                tv_online.setTextColor(getResources().getColor(
                        R.color.gray_text));
                tv_offline.setTextColor(getResources().getColor(
                        R.color.c_00c8aa));
                lin_downlist.setBackgroundColor(getResources().getColor(
                        R.color.huise));
                lin_downing.setBackgroundColor(getResources().getColor(
                        R.color.white));

            } else if (index == 1) {
                tv_offline.setTextColor(getResources().getColor(
                        R.color.gray_text));
                tv_online.setTextColor(getResources().getColor(
                        R.color.c_00c8aa));
                lin_downing.setBackgroundColor(getResources().getColor(
                        R.color.huise));

                lin_downlist.setBackgroundColor(getResources().getColor(
                        R.color.white));
            }
            currentTabIndex = index;
        }

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        finish();
    }

    public void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
    }

    public void onPause() {
        super.onPause();
        StatisticalTools.onPause(this);
    }
}
