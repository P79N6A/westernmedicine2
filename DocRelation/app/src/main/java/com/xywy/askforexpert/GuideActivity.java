package com.xywy.askforexpert;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.module.my.account.LoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页面
 *
 * @author LG
 */
public class GuideActivity extends Activity implements OnClickListener {

    public final static String isFistEnter = "isFistEnter";
    List<View> ls = new ArrayList<View>();
    List<View> allView = new ArrayList<View>();
    int oldItem = 0;
    private ViewPager vp_gui;
    private Editor edit;
    private LinearLayout ll_doc;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去除状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        setContentView(R.layout.activty_gui);
        initData();
        initDot();
        setListner();
    }

    private void initDot() {

        for (int i = 0; i < ls.size(); i++) {
            View view = new View(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DensityUtils.dp2px(10), DensityUtils.dp2px(10));
//            lp.leftMargin = DensityUtils.dp2px(10);
            if(0!=i){
                lp.leftMargin = DensityUtils.dp2px(10);
            }
            if (i > 0) {
                view.setBackgroundResource(R.drawable.icon_gui2);
            } else {
                view.setBackgroundResource(R.drawable.icon_gui1);
            }

            view.setLayoutParams(lp);
            ll_doc.addView(view);
        }
    }

    private void initData() {

        //page1
        View guiOne = View.inflate(this, R.layout.gui_one, null);

        // page2
//        View guiTwo = View.inflate(this, R.layout.gui_two, null);

//        //page3
//        View gui3 = View.inflate(getApplicationContext(), R.layout.gui3, null);
//
//        //page 4
        View view = getLayoutInflater().inflate(R.layout.gui_4, null);
        if(YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)){
            ImageView iv_guide_one = (ImageView) guiOne.findViewById(R.id.iv_guide_one);
            iv_guide_one.setImageResource(R.drawable.guide_01_yszs);
            ImageView iv_guide_four = (ImageView) view.findViewById(R.id.iv_guide_four);
            iv_guide_four.setImageResource(R.drawable.guide_02_yszs);
        }

        View startIn = view.findViewById(R.id.start_in);

        ls.add(guiOne);
//        ls.add(guiTwo);
//        ls.add(gui3);
        ls.add(view);
        vp_gui = (ViewPager) findViewById(R.id.vp_gui);
        ll_doc = (LinearLayout) findViewById(R.id.ll_doc);
        startIn.setOnClickListener(this);
    }

    @SuppressWarnings({"deprecation"})
    private void setListner() {
        vp_gui.setAdapter(new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return ls.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(ls.get(position));
                return ls.get(position);
            }
        });

        vp_gui.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageSelected(int arg0) {
                for (int i = 0; i < allView.size(); i++) {
                    allView.get(i).setVisibility(View.INVISIBLE);
                }
//                loadAnimation2.cancel();


                ll_doc.getChildAt(oldItem).setBackgroundResource(R.drawable.icon_gui2);
                ll_doc.getChildAt(arg0).setBackgroundResource(R.drawable.icon_gui1);
                oldItem = arg0;
            }

        });

    }


    @Override
    public void onClick(View v) {
        edit = sp.edit();
        String versonCode = "";
        try {
            versonCode = AppUtils.getAppVersionCodeStr();
        } catch (Exception e) {
            e.printStackTrace();
        }
        edit.putString(isFistEnter, isFistEnter).commit();
        edit.putString("versonCode", versonCode).commit();// 保存当前版本的versocode

//		System.out.println(sp.getString(isFistEnter, ""));;
//		System.out.println(sp.getString("versonCode", ""));;
        Intent intent = null;
        switch (v.getId()) {
            case R.id.start_in:
                intent = new Intent(this, LoginActivity.class);
                break;

            default:
                break;
        }
        startActivity(intent);

        this.finish();
    }

    @SuppressWarnings("static-access")
    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
    }

    @Override
    protected void onPause() {
        StatisticalTools.onPause(this);
        super.onPause();
    }
}
