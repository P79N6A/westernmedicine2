package com.xywy.askforexpert.module.main.patient.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.model.DeleteBatchNotice;
import com.xywy.askforexpert.model.PatienTtitle;
import com.xywy.askforexpert.module.consult.PatientEntity;
import com.xywy.askforexpert.module.consult.ServiceProvider;
import com.xywy.askforexpert.module.discovery.medicine.util.ToastUtils;
import com.xywy.askforexpert.module.main.patient.adapter.PatientManagerAdapter;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Subscriber;

/**
 * Created by jason on 2018/11/9.
 */

public class PatientListActivity extends YMBaseActivity {
    @Bind(R.id.patient_recycler)
    RecyclerView patient_recycler;
    private String doctorId = YMUserService.getCurUserId();
    private PatientManagerAdapter patientManagerAdapter;
    private String gid;

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("添加患者");
        titleBarBuilder.addItem("确定", new ItemClickListener() {
            @Override
            public void onClick() {
                List<PatienTtitle> list = patientManagerAdapter.getDatas();
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < list.size(); i++){
                    if (list.get(i).isSelected()){
                        stringBuffer.append(list.get(i).getId()+",");
                    }
                }
                if (stringBuffer.length()>0){
                    stringBuffer.deleteCharAt(stringBuffer.length()-1);
                }
//                ToastUtils.longToast(stringBuffer.toString());
                if (TextUtils.isEmpty(stringBuffer.toString())){
                    ToastUtils.longToast("请选择患者");
                } else{
                    ServiceProvider.addPatient(doctorId, gid, stringBuffer.toString(), new Subscriber<DeleteBatchNotice>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtils.longToast("添加患者失败");
                        }

                        @Override
                        public void onNext(DeleteBatchNotice questionMsgListRspEntity) {
                            if (questionMsgListRspEntity.getCode()==10000){
                                ToastUtils.longToast("添加患者成功");
                                setResult(RESULT_OK);
                                finish();
                            }else{
                                ToastUtils.longToast(questionMsgListRspEntity.getMsg());
                            }

                        }
                    });
                }
            }
        }).build();
        patient_recycler.setLayoutManager(new LinearLayoutManager(this));
        patientManagerAdapter = new PatientManagerAdapter(this,true);
        patient_recycler.setAdapter(patientManagerAdapter);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        gid = intent.getStringExtra("gid");
        ServiceProvider.getPatientList(doctorId, new Subscriber<PatientEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(PatientEntity patientEntity) {
                ArrayList<PatienTtitle> list = new ArrayList<PatienTtitle>();
                for (int i=0;i<patientEntity.getData().size();i++){
                    patientEntity.getData().get(i).setItemFlag(2);
                    patientEntity.getData().get(i).setSelected(true);
//                    list.add(patienTtitle);
                }
                patientManagerAdapter.setData(patientEntity.getData());
                patientManagerAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.patient_list_layout;
    }
}
