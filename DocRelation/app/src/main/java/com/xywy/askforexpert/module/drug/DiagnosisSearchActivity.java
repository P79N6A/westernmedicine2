package com.xywy.askforexpert.module.drug;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.module.drug.adapter.DiagnosisListAdapter;
import com.xywy.askforexpert.module.drug.bean.BasisDoctorDiagnose;
import com.xywy.askforexpert.module.drug.request.DrugAboutRequest;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.org.bjca.sdk.core.kit.BJCASDK;

/**
 * Created by jason on 2019/5/8.
 */

public class DiagnosisSearchActivity extends YMBaseActivity {

    private String doctor_id = YMUserService.getCurUserId();
    private ArrayList<BasisDoctorDiagnose> baseListData = new ArrayList<>();
    @Bind(R.id.search_et)
    EditText search_et;
    @Bind(R.id.diagnosis_list)
    ListView diagnosis_list;
    @Bind(R.id.diagnosis_cancel_ll)
    LinearLayout diagnosis_cancel_ll;
    private DiagnosisListAdapter diagnosisListAdapter;

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("疾病诊断");
        diagnosis_cancel_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diagnosisListAdapter.setData(baseListData,"");
                search_et.setText("");
            }
        });
        search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    if (TextUtils.isEmpty(search_et.getText())){
                        diagnosisListAdapter.setData(baseListData,"");
                    } else {
                        showLoadDataDialog();
                        DrugAboutRequest.getInstance().getDiagnoseSearchList(search_et.getText().toString()).subscribe(new BaseRetrofitResponse<BaseData<ArrayList<BasisDoctorDiagnose>>>() {
                            @Override
                            public void onError(Throwable e) {
                                hideProgressDialog();
                                Toast.makeText(DiagnosisSearchActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(BaseData<ArrayList<BasisDoctorDiagnose>> entry) {
                                hideProgressDialog();
                                if (entry.getCode() == 10000 &&
                                        null != entry.getData()) {
                                    if (entry.getData().size()==0){
                                        Toast.makeText(DiagnosisSearchActivity.this,"未搜索到相关疾病",Toast.LENGTH_SHORT).show();
                                    }
                                    diagnosisListAdapter.setData(entry.getData(),search_et.getText().toString());
                                }
                            }
                        });
                    }
                    return true;
                }
                return false;
            }
        });
        diagnosisListAdapter = new DiagnosisListAdapter(this,baseListData);
        diagnosis_list.setAdapter(diagnosisListAdapter);
    }

    @Override
    protected void initData() {
        showLoadDataDialog();
        DrugAboutRequest.getInstance().getDoctorDiagnose(doctor_id).subscribe(new BaseRetrofitResponse<BaseData<ArrayList<BasisDoctorDiagnose>>>() {
            @Override
            public void onError(Throwable e) {
                hideProgressDialog();
                Toast.makeText(DiagnosisSearchActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(BaseData<ArrayList<BasisDoctorDiagnose>> entry) {
                hideProgressDialog();
                if (entry.getCode() == 10000) {
                    baseListData.addAll(entry.getData());
                    diagnosisListAdapter.setData(baseListData,"");
                }
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_diagnosis_search;
    }
}
