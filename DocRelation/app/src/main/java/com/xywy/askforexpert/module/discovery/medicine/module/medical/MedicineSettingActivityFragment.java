package com.xywy.askforexpert.module.discovery.medicine.module.medical;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.reginald.editspinner.EditSpinner;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.MedicineCartEntity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.MedicineEntity;
import com.xywy.askforexpert.module.discovery.medicine.view.AmountView;
import com.xywy.util.LogUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class MedicineSettingActivityFragment extends Fragment {

    private View rootView;
    private TextView mMedicineName;
    private TextView mIntroduction;
    private TextView mMedicineSpec;
    private AmountView mAmount_view_count,mDay_amount,mTime_amount;
    private EditSpinner mDaySpinner,mTimeSpinner,mTakeMethodSpinner;
    private EditText mEt_remark;
    private int mCount = 1,mDay_count=1,mTime_count=1;
    private Button mSubmit;

    private MedicineEntity mEntity;
    private final String BR = "<br />";
    private final String LT = "&lt;p&gt;";
    private final String LT_P = "&lt;/p&gt;";
    private final String LT_BR = "&lt;br /&gt;";
    public void setmEntity(MedicineEntity mEntity) {
        this.mEntity = mEntity;
    }

    public MedicineSettingActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_medicine_setting, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        rootView = view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initView(rootView);
    }

    private void initView(final View view) {
        mMedicineName = (TextView) view.findViewById(R.id.medicine_name);
        mMedicineName.setText(mEntity.getName()+mEntity.getSpecification());
        mIntroduction = (TextView) view.findViewById(R.id.introduction);
        mIntroduction.setMovementMethod(ScrollingMovementMethod.getInstance());
        //接口中暂时没有药品使用说明字段
        LogUtils.i("mEntity.getUseage()="+mEntity.getUseage());
        String temp = mEntity.getUseage().replace(BR,"\\n");
        temp = temp.replace(LT,"");
        temp = temp.replace(LT_P,"");
        temp = temp.replace(LT_BR,"");
        mIntroduction.setText("常规用法："+temp);
        mMedicineSpec = (TextView) view.findViewById(R.id.medicine_spec);
        mMedicineSpec.setText("规格 " + mEntity.getSpecification());
        mAmount_view_count = (AmountView) view.findViewById(R.id.amount_view_count);
        mAmount_view_count.setGoods_storage(mEntity.getStock());
        mAmount_view_count.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                mCount = amount;
                LogUtils.i("mCount="+mCount);
            }
        });
        mAmount_view_count.clearFocus();

        mDay_amount = (AmountView) view.findViewById(R.id.day_amount);
        mDay_amount.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
//                LogUtils.i("amount========="+amount);//被调用了两次
                mDay_count = amount;
            }
        });

        mTime_amount = (AmountView) view.findViewById(R.id.time_amount);
        mTime_amount.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                mTime_count = amount;

            }
        });

        mDaySpinner = (EditSpinner)view.findViewById(R.id.day_spinner);
        final String[] medicine_day = getResources().getStringArray(R.array.medicine_day);
        ArrayAdapter<String> adapter_day = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,
                medicine_day);
        mDaySpinner.setAdapter(adapter_day);
        mDaySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == medicine_day.length - 1) {
                    showSoftInputPanel(mDaySpinner);
                    mDaySpinner.setText("");
                    mDaySpinner.setHint("");
                }else {
                    LogUtils.i("medicine_day[position] = " + medicine_day[position]);
                    mDaySpinner.setText(medicine_day[position]);
                    mEntity.setDayCountDesc(medicine_day[position]);
                }

            }
        });
        mDaySpinner.setOnShowListener(new EditSpinner.OnShowListener() {
            @Override
            public void onShow() {
                hideSoftInputPanel(mDaySpinner);
            }
        });
        mDaySpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s != null && s.length()<=10){//手动输入的字母或数字个数为1到10
                    if(!medicine_day[medicine_day.length - 1].equals(s)){
                        mEntity.setDayCountDesc(s.toString());
                    }
                }else {
                    mDaySpinner.setText(mEntity.getDayCountDesc());
                    mDaySpinner.setSelection(mEntity.getDayCountDesc().length());
                }

            }
        });

        mTimeSpinner = (EditSpinner)view.findViewById(R.id.time_spinner);
        final String[] medicine_time = getResources().getStringArray(R.array.medicine_time);
        ArrayAdapter<String> adapter_time = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,
                medicine_time);
        mTimeSpinner.setAdapter(adapter_time);
        mTimeSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //stone +1
                mEntity.setTimeCountDesc(String.valueOf(position+1));
            }
        });

        mTakeMethodSpinner = (EditSpinner)view.findViewById(R.id.take_method);
        final String[] take_method = getResources().getStringArray(R.array.take_method);
        ArrayAdapter<String> adapter_method = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,
                take_method);
        mTakeMethodSpinner.setAdapter(adapter_method);
        mTakeMethodSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //stone +1
                mEntity.setTakeMethod(String.valueOf(position+1));
            }
        });

        mEt_remark = (EditText) view.findViewById(R.id.et_remark);

        mSubmit = (Button) view.findViewById(R.id.submit);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean checkResult = checkAmountView(mAmount_view_count,"请设置药品数量");
                if(!checkResult){
                    return;
                }
                checkResult = checkAmountView(mDay_amount,"请设置药品每天的使用次数");
                if(!checkResult){
                    return;
                }
                checkResult = checkAmountView(mTime_amount,"请设置药品每次的使用数量");
                if(!checkResult){
                    return;
                }
                if(TextUtils.isEmpty(mEntity.getDayCountDesc())){
                    showToast(getActivity(),"请选择服用时间");
                    return;
                }
                if(TextUtils.isEmpty(mEntity.getTimeCountDesc())){
                    showToast(getActivity(),"请选择服用单位");
                    return;
                }

                if(TextUtils.isEmpty(mEntity.getTakeMethod())){
                    showToast(getActivity(),"请选择用法");
                    return;
                }

//                LogUtils.e("mCount="+mCount+"---mDay_count="+mDay_count+"---mTime_count="+mTime_count);
                mEntity.setCount(mCount);//设置总数量
                //设置每天的次数
                mEntity.setDayCount(mDay_count);
                //设置每次的用量
                mEntity.setTimeCount(mTime_count);
                mEntity.setRemark(mEt_remark.getText().toString());
                MedicineCartEntity entity = new MedicineCartEntity(mEntity);
                MedicineCartCenter.getInstance().addMedicine(entity);
                getActivity().finish();
            }


        });

    }

    private boolean checkAmountView(AmountView mAmount_view,String msg) {
        if(TextUtils.isEmpty(mAmount_view.getText())){
            showToast(getActivity(),msg);
            return false;
        }
        if("0".equals(mAmount_view.getText())){
            showToast(getActivity(),msg);
            return false;
        }
        return true;
    }

    private void hideSoftInputPanel(EditSpinner editSpinner) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(editSpinner.getWindowToken(), 0);
        }
    }

    private void showSoftInputPanel(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }

    private void showToast(Context context, String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

}
