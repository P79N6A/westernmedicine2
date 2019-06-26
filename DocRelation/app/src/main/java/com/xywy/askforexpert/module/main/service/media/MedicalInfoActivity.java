package com.xywy.askforexpert.module.main.service.media;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.widget.view.CategoryTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * 咨询类
 *
 * @author 王鹏
 * @2015-5-10上午10:15:55
 */
public class MedicalInfoActivity extends FragmentActivity {
    private CategoryTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.consultinginfo);

        tabs = (CategoryTabStrip) findViewById(R.id.category_strip);
        pager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);

        tabs.setViewPager(pager);

    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final List<String> catalogs = new ArrayList<String>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            catalogs.add("全部");
            catalogs.add("医学进展");
            catalogs.add("资讯头条");
            catalogs.add("最新会议");
            catalogs.add("医改政策");
            catalogs.add("医生故事");
            catalogs.add("生物医药");
            catalogs.add("考试科研");
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return catalogs.get(position);
        }

        @Override
        public int getCount() {
            return catalogs.size();
        }

        @Override
        public Fragment getItem(int position) {
            return NewsFragment.newInstance(position);
        }

    }

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.btn2:
                StatisticalTools.eventCount(MedicalInfoActivity.this, "zxsearch");
                Intent intent = new Intent(MedicalInfoActivity.this, CounsultSerchActivity.class);
                startActivity(intent);

                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatisticalTools.onResume(MedicalInfoActivity.this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        StatisticalTools.onPause(MedicalInfoActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
