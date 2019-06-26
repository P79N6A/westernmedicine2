package com.xywy.askforexpert.module.my.photo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.module.doctorcircle.DoctorCircleSendMessageActivty;
import com.xywy.askforexpert.module.main.diagnose.DiagnoselogAddEditActiviy;
import com.xywy.askforexpert.module.main.diagnose.TreatmentInfoActivity;
import com.xywy.askforexpert.module.main.patient.activity.AddTreatmentListLogInfoActivity;
import com.xywy.askforexpert.module.my.userinfo.ApplyForFamilyDoctorActivity;
import com.xywy.askforexpert.module.my.userinfo.IDCardUpStuActivity;
import com.xywy.askforexpert.module.my.userinfo.IDCardUplodActivity;
import com.xywy.askforexpert.module.my.userinfo.PersonalStyleActivity;
import com.xywy.askforexpert.widget.view.HackyViewPager;

import java.util.List;

/**
 * 图片展示 stone
 *
 * @author 王鹏
 * @2015-4-29下午1:49:52
 */
public class PhotoActivity extends FragmentActivity {

    private HackyViewPager mPager;
    private List<String> list;
    private List<String> list_url;
    private TextView indicator;
    /**
     * 页面滑动KEY
     */
    private static final String POSITION = "POSITION";
    ImagePagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.photoshow);
        mPager = (HackyViewPager) findViewById(R.id.viewpager);
        indicator = (TextView) findViewById(R.id.tv_title);
        View delete = findViewById(R.id.delete);
        /* 当前页数 默认为起始页数 */
        int pagerPosition;
        if (getIntent().getIntExtra("statPosition", -1) == -1) {
            pagerPosition = 0;
        } else {
            pagerPosition = getIntent().getIntExtra("statPosition", -1);
        }
        if (getIntent().getStringExtra("keytype").equals(Constants.HONOR_SHOW)) {
            list = ApplyForFamilyDoctorActivity.imagePathList_show;
            list_url = ApplyForFamilyDoctorActivity.list_show;
        } else if (getIntent().getStringExtra("keytype").equals(Constants.PERSONAL_STYLE)) {
            list = ApplyForFamilyDoctorActivity.imagePathList_style;
            list_url = ApplyForFamilyDoctorActivity.list_style;
        } else if (getIntent().getStringExtra("keytype").equals(IDCardUplodActivity.JOB_TITLE)) {
            list = IDCardUplodActivity.imagePahtList_jobTitle;
            list_url = IDCardUplodActivity.list_jobTitle;
        } else if (getIntent().getStringExtra("keytype").equals("job")) {
            list = IDCardUplodActivity.imagePathList;
            list_url = IDCardUplodActivity.list_job;
        } else if (getIntent().getStringExtra("keytype").equals("idcard")) {
            list = IDCardUplodActivity.imagePahtList_idcard;
            list_url = IDCardUplodActivity.list_idcard;
        } else if (getIntent().getStringExtra("keytype").equals("stu_job")) {
            list = IDCardUpStuActivity.imagePathList;
            list_url = IDCardUpStuActivity.stu_list;
        } else if (getIntent().getStringExtra("keytype").equals("perstyle")) {
            list = PersonalStyleActivity.pathlist;
            list_url = PersonalStyleActivity.list_save;
        } else if (getIntent().getStringExtra("keytype").equals("sendMessages")) {
            list = DoctorCircleSendMessageActivty.imagePahtList_idcard;
        } else if (getIntent().getStringExtra("keytype").equals("diagon_log")) {
            list = DiagnoselogAddEditActiviy.pathlist;
            list_url = DiagnoselogAddEditActiviy.stu_list;
        } else if (getIntent().getStringExtra("keytype").equals("add_treatment")) {
            list = AddTreatmentListLogInfoActivity.imagePathList;
            list_url = AddTreatmentListLogInfoActivity.stu_list;
        } else if (getIntent().getStringExtra("keytype").equals("treatment_info")) {
            list = TreatmentInfoActivity.imglist;
            delete.setVisibility(View.GONE);
        } else if (getIntent().getStringExtra("keytype").equals("circleBigImage")) {
            list = getIntent().getStringArrayListExtra("imgs");
            delete.setVisibility(View.GONE);
        }

        mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), list);
        mPager.setAdapter(mAdapter);

        CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
                .getAdapter().getCount());
        indicator.setText(text);

        // 更新下标
        mPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                CharSequence text = getString(R.string.viewpager_indicator,
                        arg0 + 1, mPager.getAdapter().getCount());
                indicator.setText(text);
            }

        });
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(POSITION);
        }

        mPager.setCurrentItem(pagerPosition);

        delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mPager.getAdapter().getCount() == 1) {
                    int postion = mPager.getCurrentItem();
                    if (list_url != null) {
                        list_url.remove(postion);
                    }
                    if (list != null) {
                        list.remove(postion);
                    }
                    mAdapter.notifyDataSetChanged();
                    finish();
                } else {
                    int postion = mPager.getCurrentItem();
                    if (list_url != null) {
                        list_url.remove(postion);
                    }
                    if (list != null) {
                        list.remove(postion);
                    }
                    CharSequence text = getString(R.string.viewpager_indicator,
                            postion + 1, mPager.getAdapter().getCount());
                    indicator.setText(text);
                    mAdapter.notifyDataSetChanged();
                }

            }
        });
        findViewById(R.id.btn1).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(POSITION, mPager.getCurrentItem());
    }

    /**
     * 滑动页面加载适配器
     *
     * @author Administrator
     */
    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public List<String> fileList;

        public ImagePagerAdapter(FragmentManager fragmentManager,
                                 List<String> fileList) {
            super(fragmentManager);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? -1 : fileList.size();
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList.get(position);
            return ImageDetailFragment.newInstance(url);
        }

        // public void destroyItem(ViewPager container, int position, V object)
        // {
        // // TODO Auto-generated method stub
        // super.destroyItem(container, position, object);
        // }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub

            super.destroyItem(container, position, object);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup arg0, int arg1) {
            // TODO Auto-generated method stub
            return super.instantiateItem(arg0, arg1);
        }
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
