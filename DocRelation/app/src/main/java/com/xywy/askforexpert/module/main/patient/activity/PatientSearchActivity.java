package com.xywy.askforexpert.module.main.patient.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.model.PatienTtitle;
import com.xywy.askforexpert.module.main.patient.adapter.PatientManagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jason on 2018/11/13.
 */

public class PatientSearchActivity extends YMBaseActivity {
    @Bind(R.id.pharmacy_search)
    EditText pharmacy_search;
    @Bind(R.id.patient_recycler)
    RecyclerView patient_recycler;
    @Bind(R.id.next_btn)
    TextView next_btn;
    @OnClick(R.id.next_btn) void next(){
        List<PatienTtitle> list = patientManagerAdapter.getDatas();
        StringBuffer stringBuffer = new StringBuffer();
        for(PatienTtitle patienTtitle : list){
            if (patienTtitle.isSelected()){
                stringBuffer.append(patienTtitle.getUid()+",");
            }
        }
        if (stringBuffer.length()>0) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            startActivity(new Intent(this, BatchNoticeContentActivity.class)
                    .putExtra("uid", stringBuffer.toString()));
            finish();
        }
    }
    private boolean localFlag;
    private ArrayList<PatienTtitle> childList;
    private PatientManagerAdapter patientManagerAdapter;

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("搜索");
        pharmacy_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!TextUtils.isEmpty(s)) {
                        ArrayList<PatienTtitle> patienTtitleList = new ArrayList<PatienTtitle>();
                        for (PatienTtitle patienTtitle : childList) {
                            if (patienTtitle.getUsername().contains(s)){
                                patienTtitle.setItemFlag(2);
                                patienTtitleList.add(patienTtitle);
                            }
                        }
                        patientManagerAdapter.setData(patienTtitleList);
                        patientManagerAdapter.notifyDataSetChanged();
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        patient_recycler.setLayoutManager(new LinearLayoutManager(this));
        patientManagerAdapter = new PatientManagerAdapter(this,localFlag);
        patient_recycler.setAdapter(patientManagerAdapter);

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        childList = (ArrayList<PatienTtitle>) intent.getSerializableExtra("PatientList");
        localFlag = intent.getBooleanExtra("localFlag",false);
        if (localFlag){
            next_btn.setVisibility(View.VISIBLE);
        }else{
            next_btn.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.patient_search_layout;
    }
}
