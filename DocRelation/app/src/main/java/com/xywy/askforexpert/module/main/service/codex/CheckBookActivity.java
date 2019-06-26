package com.xywy.askforexpert.module.main.service.codex;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 检查手册 stone
 *
 * @author 王鹏
 * @2015-5-10下午2:29:03
 */
public class CheckBookActivity extends YMBaseActivity {

    private ViewPager viewpage;
    private MyPagerAdapter adaper;
    private TextView tv_phy_check;
    private TextView tv_che_check;

    private TextView tv_assay;


    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final List<String> list = new ArrayList<String>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            list.add("化学检查");
            list.add("物理检查");
            list.add("化验");
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Fragment getItem(int position) {
            return CheckBookFragment.newInstance(position);
        }

    }

    // viewsetOnPageChangeListener
    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.lin_medicine:
                viewpage.setCurrentItem(0);
                break;
            case R.id.lin_medicine_c:
                viewpage.setCurrentItem(1);
                break;
            case R.id.lin_assay:
                viewpage.setCurrentItem(2);
                break;
            case R.id.btn2:
                StatisticalTools.eventCount(CheckBookActivity.this, "scsearch");
                Intent intent = new Intent(CheckBookActivity.this, BookSerchActivity.class);
                intent.putExtra("type", 2 + "");
                startActivity(intent);
                break;
            default:
                break;
        }
    }



    @Override
    protected void initView() {

        //stone
        titleBarBuilder.setTitleText("检查手册").addItem("", R.drawable.more_search_icon, new ItemClickListener() {
            @Override
            public void onClick() {
                StatisticalTools.eventCount(CheckBookActivity.this, "scsearch");
                Intent intent = new Intent(CheckBookActivity.this, BookSerchActivity.class);
                intent.putExtra("type", 2 + "");
                startActivity(intent);
            }
        }).build();

        viewpage = (ViewPager) findViewById(R.id.view_pager);
        tv_phy_check = (TextView) findViewById(R.id.tv_medicine_c);
        tv_che_check = (TextView) findViewById(R.id.tv_medicine);
        tv_assay = (TextView) findViewById(R.id.tv_assay);
        adaper = new MyPagerAdapter(getSupportFragmentManager());
        viewpage.setAdapter(adaper);
        viewpage.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                switch (arg0) {
                    case 0:
                        tv_che_check.setTextColor(getResources().getColor(
                                R.color.color_00c8aa));
                        tv_phy_check.setTextColor(getResources().getColor(
                                R.color.my_textcolor));
                        tv_assay.setTextColor(getResources().getColor(
                                R.color.my_textcolor));
                        break;
                    case 1:
                        tv_che_check.setTextColor(getResources().getColor(
                                R.color.my_textcolor));
                        tv_phy_check.setTextColor(getResources().getColor(
                                R.color.color_00c8aa));
                        tv_assay.setTextColor(getResources().getColor(
                                R.color.my_textcolor));
                        break;
                    case 2:
                        tv_che_check.setTextColor(getResources().getColor(
                                R.color.my_textcolor));
                        tv_phy_check.setTextColor(getResources().getColor(
                                R.color.my_textcolor));
                        tv_assay.setTextColor(getResources().getColor(
                                R.color.color_00c8aa));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.checkbookfrag;
    }
}
