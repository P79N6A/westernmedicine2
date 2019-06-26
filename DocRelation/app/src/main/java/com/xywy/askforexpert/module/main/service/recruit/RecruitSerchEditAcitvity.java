package com.xywy.askforexpert.module.main.service.recruit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 招聘中心 高级搜索
 *
 * @author 王鹏
 * @2015-5-16下午2:20:37
 */
public class RecruitSerchEditAcitvity extends Activity {
    private TextView tv_area, tv_jobtype, tv_money, tv_xueli, tv_work_year,
            tv_work_type, tv_com_type;
    private List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);

        setContentView(R.layout.recruit_serch_edit);
        ((TextView) findViewById(R.id.tv_title)).setText("编辑搜索条件");
        tv_area = (TextView) findViewById(R.id.tv_area);
        tv_jobtype = (TextView) findViewById(R.id.tv_jobtype);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_xueli = (TextView) findViewById(R.id.tv_xueli);
        tv_work_year = (TextView) findViewById(R.id.tv_work_year);
        tv_work_type = (TextView) findViewById(R.id.tv_work_type);
        tv_com_type = (TextView) findViewById(R.id.tv_com_type);
        if (RecruitCenterMainActivity.serchinfo != null) {
            initview();
        }

    }

    public void initview() {
        String area1 = RecruitCenterMainActivity.serchinfo.getArea_1();
        String area2 = RecruitCenterMainActivity.serchinfo.getArea_2();
        String jobtype = RecruitCenterMainActivity.serchinfo.getJobtype();
        String money = RecruitCenterMainActivity.serchinfo.getMoney();
        String xueli = RecruitCenterMainActivity.serchinfo.getXueli();
        String workyear = RecruitCenterMainActivity.serchinfo.getWork_year();
        String worktype = RecruitCenterMainActivity.serchinfo.getWork_type();
        String workstat = RecruitCenterMainActivity.serchinfo.getCom_type();
        if (area2 != null & !"".equals(area2) || area1 != null & !"".equals(area1)) {
            if ("全部".equals(area2) || "".equals(area2)) {
                tv_area.setText(area1);
                RecruitCenterMainActivity.serchinfo.setArea_2_id("");
            } else {
                tv_area.setText(area1 + "," + area2);
            }
        } else {
            tv_area.setText("不限");
        }
        if (!TextUtils.isEmpty(jobtype)) {
            tv_jobtype.setText(jobtype);
        } else {
            tv_jobtype.setText("不限");
        }
        if (!TextUtils.isEmpty(money)) {
            tv_money.setText(money);
        } else {
            tv_money.setText("不限");
        }
        if (!TextUtils.isEmpty(xueli)) {
            tv_xueli.setText(xueli);
        } else {
            tv_xueli.setText("不限");
        }
        if (!TextUtils.isEmpty(workyear)) {
            tv_work_year.setText(workyear);
        } else {
            tv_work_year.setText("不限");
        }
        if (!TextUtils.isEmpty(worktype)) {
            tv_work_type.setText(worktype);
        } else {
            tv_work_type.setText("不限");
        }
        if (!TextUtils.isEmpty(workstat)) {
            tv_com_type.setText(workstat);
        } else {
            tv_com_type.setText("不限");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (RecruitCenterMainActivity.serchinfo != null) {
            initview();
        }
        StatisticalTools.onResume(RecruitSerchEditAcitvity.this);

    }

    public void onClick_back(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.btn1:
                finish();
                break;

            case R.id.re_area:// 工作地点
                intent = new Intent(RecruitSerchEditAcitvity.this,
                        R_AreaListActivity.class);
                intent.putExtra("type", "serch_area");
                startActivity(intent);
                break;
            case R.id.re_jobtype:// 职业类型

                intent = new Intent(RecruitSerchEditAcitvity.this,
                        R_SerchBaseJobTypeActivity.class);
                startActivity(intent);

                break;
            case R.id.re_money:// 薪资

                intent = new Intent(RecruitSerchEditAcitvity.this,
                        R_SerchBaseActivity.class);
                intent.putExtra("type", "money");

                startActivity(intent);
                break;
            case R.id.re_xueli:// 学历
                intent = new Intent(RecruitSerchEditAcitvity.this,
                        R_SerchBaseActivity.class);
                intent.putExtra("type", "xueli");

                startActivity(intent);
                break;
            case R.id.re_work_year:// 工作年限
                intent = new Intent(RecruitSerchEditAcitvity.this,
                        R_SerchBaseActivity.class);
                intent.putExtra("type", "workyear");

                startActivity(intent);
                break;
            case R.id.re_work_type:// 工作性质
                intent = new Intent(RecruitSerchEditAcitvity.this,
                        R_SerchBaseActivity.class);
                intent.putExtra("type", "worktype");

                startActivity(intent);
                break;
            case R.id.re_com_type:// 企业性质

                intent = new Intent(RecruitSerchEditAcitvity.this,
                        R_SerchBaseActivity.class);
                intent.putExtra("type", "workstat");
                startActivity(intent);
                break;
            case R.id.btn_add_newfriend:
                intent = new Intent(RecruitSerchEditAcitvity.this,
                        RecruitSerchMainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        StatisticalTools.onPause(RecruitSerchEditAcitvity.this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
