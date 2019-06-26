package com.xywy.askforexpert.module.drug;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.module.drug.bean.DrugBean;
import com.xywy.askforexpert.module.drug.bean.RrescriptionBean;
import com.xywy.askforexpert.module.drug.bean.RrescriptionData;
import com.xywy.askforexpert.widget.view.DrugAmountView;
import com.xywy.askforexpert.widget.view.DrugAmountView1;
import com.xywy.askforexpert.widget.view.SelectBasePopupWindow;

import butterknife.Bind;

/**
 * Created by jason on 2018/7/10.
 */

public class DrugSettingActivity extends YMBaseActivity {

    private RelativeLayout dosage_format_rl;
    private TextView dosage_format_tv;
    private RelativeLayout single_dosage_format_rl;
    private RelativeLayout frequency_rl;
    private RelativeLayout instructions_rl;
    private RelativeLayout taking_time_rl;
    private TextView single_dosage_format_tv;
    private TextView frequency_tv;
    private TextView instructions_tv;
    private TextView taking_time_tv;

    //开药量单位
    private String[] open_unit_new;
    //单次服用单位
    private String[] take_unit_new;
    //用药频次
    private String[] take_rates;
    //用药方法
    private String[] take_method_new;
    //用药时间
    private String[] medicine_day;
    //商品ID
    private String id;
    //开药量单位对应名称
    private String doseUnit = "盒";
    //开药量单位对应数值
    private int drug_unit = 1;
    //单次服用量单位(int)
    private int take_unit = 1;
    //单次服用量单位名称
    private String doseName = "片";
    //服用频率（每天几次 string
    private String take_rate = "1次/日";
    //服用方法(int)
    private int take_method = 1;
    //服用方法名称
    private String methodName = "口服";
    //服用时间（饭前、饭后... string)
    private String take_time = "";
    private DrugBean entity;
    private DrugAmountView drug_dosage_tv;
    private DrugAmountView single_dosage_tv;
    private DrugAmountView1 days_tv;
    private EditText remark_et;
    private boolean mIsCommonDrug;


    @Override
    protected void initView() {

        id = getIntent().getStringExtra("id");
        mIsCommonDrug = getIntent().getBooleanExtra("mIsCommonDrug",false);
        if (TextUtils.isEmpty(id)){
            entity = (DrugBean) getIntent().getSerializableExtra("drugInfo");
            entity.setId(getIntent().getBooleanExtra("mIsCommonDrug",true)?entity.getPid():entity.getId());
        }
        open_unit_new = getResources().getStringArray(R.array.open_unit_new);
        take_unit_new = getResources().getStringArray(R.array.take_unit_new);
        take_rates = getResources().getStringArray(R.array.take_rate);
        take_method_new = getResources().getStringArray(R.array.take_method_new);
        medicine_day = getResources().getStringArray(R.array.medicine_day);
        titleBarBuilder.setTitleText("药品设置");
        //药品名称
        TextView drug_name = (TextView) findViewById(R.id.drug_name);
        //药品价格
        TextView price_tv = (TextView) findViewById(R.id.price_tv);
        //包装数量
        TextView quantity_tv = (TextView) findViewById(R.id.quantity_tv);

        //开药剂量
        drug_dosage_tv = (DrugAmountView) findViewById(R.id.drug_dosage_tv);
        //开药剂量规格布局
        dosage_format_rl = (RelativeLayout) findViewById(R.id.dosage_format_rl);
        dosage_format_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showPop(open_unit_new,1,dosage_format_rl);

            }
        });
        //开药剂量规格文字
        dosage_format_tv = (TextView) findViewById(R.id.dosage_format_tv);

        // 每次服用剂量数量
        single_dosage_tv = (DrugAmountView) findViewById(R.id.single_dosage_tv);
        //单次服用剂量规格布局
        single_dosage_format_rl = (RelativeLayout) findViewById(R.id.single_dosage_format_rl);
        single_dosage_format_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop(take_unit_new,2, single_dosage_format_rl);
            }
        });
        //单次服用剂量规格文字
        single_dosage_format_tv = (TextView) findViewById(R.id.single_dosage_format_tv);

        //用药频次布局
        frequency_rl = (RelativeLayout) findViewById(R.id.frequency_rl);
        frequency_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop(take_rates,3, frequency_rl);
            }
        });
        //每次用药频次文字
        frequency_tv = (TextView) findViewById(R.id.frequency_tv);

        //用法布局
        instructions_rl = (RelativeLayout) findViewById(R.id.instructions_rl);
        instructions_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop(take_method_new,4, instructions_rl);
            }
        });
        //用法文字
        instructions_tv = (TextView) findViewById(R.id.instructions_tv);
        //用药时间布局
        taking_time_rl = (RelativeLayout) findViewById(R.id.taking_time_rl);
        taking_time_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPop(medicine_day,5, taking_time_rl);
            }
        });
        //用药时间文字
        taking_time_tv = (TextView) findViewById(R.id.taking_time_tv);

        //用药天数
        days_tv = (DrugAmountView1) findViewById(R.id.days_tv);

        //备注
        remark_et = (EditText) findViewById(R.id.remark_et);

        //移出药品
        TextView delete_tv = (TextView) findViewById(R.id.delete_tv);
        delete_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RrescriptionData.getInstance().removeMedicine(id);
                finish();
            }
        });

        //确定
        TextView submit_tv = (TextView) findViewById(R.id.submit_tv);
        submit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RrescriptionBean  rescriptionBean;
                if (TextUtils.isEmpty(id)){
                    rescriptionBean = new RrescriptionBean();
                    rescriptionBean.setWksmj(entity.getWksmj());
                    rescriptionBean.setSpecification(entity.getSpecification());
                    rescriptionBean.setDrugName(entity.getName());
                    rescriptionBean.setId(mIsCommonDrug?entity.getPid():entity.getId());
                    rescriptionBean.setStock(entity.getStock()+"");
                    RrescriptionData.getInstance().addMedicine(setDrug(rescriptionBean));
                    RrescriptionData.getInstance().BACK_FLAG = true;
                    finish();
                }else{
                    rescriptionBean = RrescriptionData.getInstance().getMedicine(id);
                    RrescriptionData.getInstance().addMedicine(setDrug(rescriptionBean));
                    finish();
                }

            }
        });

        if (TextUtils.isEmpty(id)){
            delete_tv.setVisibility(View.GONE);
            drug_name.setText(entity.getName());
            price_tv.setText(entity.getWksmj());
            quantity_tv.setText(entity.getSpecification());
            drug_dosage_tv.setStock(entity.getStock());
        }else{
            delete_tv.setVisibility(View.VISIBLE);
            RrescriptionBean rescriptionBean = RrescriptionData.getInstance().getMedicine(id);
            drug_name.setText(rescriptionBean.getDrugName());
            price_tv.setText(rescriptionBean.getWksmj());
            quantity_tv.setText(rescriptionBean.getSpecification());

            drug_dosage_tv.setStock(Integer.valueOf(rescriptionBean.getStock().toString()));
            drug_dosage_tv.setCount(Integer.valueOf(rescriptionBean.getNum().toString()));
            dosage_format_tv.setText(rescriptionBean.getDoseUnit());
            //开药量单位数据初始化
            setUnit(open_unit_new,1,rescriptionBean.getDoseUnit());

            single_dosage_tv.setCount(Integer.valueOf(rescriptionBean.getTake_num().toString()));
            single_dosage_format_tv.setText(rescriptionBean.getDoseName());
            //单次用量单位数据初始化
            setUnit(take_unit_new,2,rescriptionBean.getDoseName());

            frequency_tv.setText(rescriptionBean.getTake_rate());
            //用药频次初始化
            setUnit(take_rates,3,rescriptionBean.getTake_rate());

            instructions_tv.setText(rescriptionBean.getMethodName());
            //用法单位初始化
            setUnit(take_method_new,4,rescriptionBean.getMethodName());

            taking_time_tv.setText(rescriptionBean.getTake_time());
            //用药时间初始化
            setUnit(medicine_day,5,rescriptionBean.getTake_time());

            days_tv.setCount(Integer.valueOf(rescriptionBean.getTake_day().toString()));
            String remake = !TextUtils.isEmpty(rescriptionBean.getRemark())?rescriptionBean.getRemark():"";
            remark_et.setText(remake);
        }
    }

    private RrescriptionBean setDrug(RrescriptionBean  rescriptionBean) {
        rescriptionBean.setNum(drug_dosage_tv.getText().toString());
        rescriptionBean.setDoseUnit(doseUnit);
        rescriptionBean.setDrug_unit(drug_unit+"");
        rescriptionBean.setTake_num(single_dosage_tv.getText());
        rescriptionBean.setTake_unit(take_unit+"");
        rescriptionBean.setDoseName(doseName);
        rescriptionBean.setTake_rate(take_rate);
        rescriptionBean.setMethodName(methodName);
        rescriptionBean.setTake_method(take_method+"");
        rescriptionBean.setTake_time(take_time);
        rescriptionBean.setTake_day(days_tv.getText());
        if (TextUtils.isEmpty(remark_et.getText().toString())) {
            rescriptionBean.setRemark("");
        }else{
            rescriptionBean.setRemark(remark_et.getText().toString());
        }
        return rescriptionBean;
    }



    private void setUnit(String[] data,int type,String name) {
        for (int i=0;i<data.length;i++){
            switch (type){
                case 1:
                    if (name.equals(data[i])) {
                        doseUnit = data[i];
                        drug_unit = i+1;
                        return;
                    }
                    break;
                case 2:
                    if (name.equals(data[i])) {
                        doseName = data[i];
                        take_unit = i+1;
                        return;
                    }
                    break;
                case 3:
                    if (name.equals(data[i])) {
                        take_rate = data[i];
                        return;
                    }
                    break;
                case 4:
                    if (name.equals(data[i])) {
                        take_method = i+1;
                        methodName = data[i];
                        return;
                    }
                    break;
                case 5:
                    if (name.equals(data[i])) {
                        take_time = data[i];
                        return;
                    }
                        break;
            }
        }
    }


    private void showPop(final String[] data, final int type,View view){
        final SelectBasePopupWindow mPopupWindow = new SelectBasePopupWindow(true, DrugSettingActivity.this);
        View popRoot = View.inflate(DrugSettingActivity.this, R.layout.pop_drug_setting_layout, null);
        TextView tv = (TextView) popRoot.findViewById(R.id.tv);
        LinearLayout select_ll = (LinearLayout) popRoot.findViewById(R.id.select_ll);
        select_ll.removeAllViews();
        for (int i=0;i<data.length;i++){
            View item = View.inflate(DrugSettingActivity.this, R.layout.pop_drug_setting_item, null);
            TextView tv_sort = (TextView) item.findViewById(R.id.tv_sort);
            tv_sort.setText(data[i]);
            final int finalI = i;
            tv_sort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (type){
                        case 1:
                            doseUnit = data[finalI];
                            drug_unit = finalI;
                            dosage_format_tv.setText(doseUnit);
                            mPopupWindow.dismiss();
                            break;
                        case 2:
                            doseName = data[finalI];
                            take_unit = finalI+1;
                            single_dosage_format_tv.setText(doseName);
                            mPopupWindow.dismiss();
                            break;
                        case 3:
                            take_rate = data[finalI];
                            frequency_tv.setText(take_rate);
                            mPopupWindow.dismiss();
                            break;
                        case 4:
                            methodName = data[finalI];
                            take_method = finalI+1;
                            instructions_tv.setText(methodName);
                            mPopupWindow.dismiss();
                            break;
                        case 5:
                            take_time = data[finalI];
                            taking_time_tv.setText(take_time);
                            mPopupWindow.dismiss();
                            break;
                    }

                }
            });
            select_ll.addView(item);
        }
        if (!mPopupWindow.isShowing()) {
            mPopupWindow.init(popRoot).showAsDropDown(view);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.drug_setting_layout;
    }
}
