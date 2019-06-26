package com.xywy.askforexpert.module.main.patient.activity;

import android.content.Intent;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.model.PatienTtitle;
import com.xywy.askforexpert.module.main.patient.adapter.PatientDetailImageAdpter;
import com.xywy.askforexpert.widget.view.MyGridView;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jason on 2018/11/1.
 */

public class PatientDetailActivity extends YMBaseActivity{
    @Bind(R.id.image_gv)
    MyGridView image_gv;
    @Bind(R.id.user_name_tv)
    TextView user_name_tv;
    @Bind(R.id.content_tv)
    TextView content_tv;
    @Bind(R.id.time_tv)
    TextView time_tv;
    @Bind(R.id.propose_tv)
    TextView propose_tv;
    @Bind(R.id.see_doc_time_tv)
    TextView see_doc_time_tv;
    private PatienTtitle patienData;

    @OnClick(R.id.im_details) void imDetail(){
        PatientChatDetailActivity.startActivity(this,patienData.getQid(),patienData.getUid(),patienData.getUsername(),false,0);
    }

    private PatientDetailImageAdpter patientDetailImageAdpter;

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("报道患者");
        Intent intent = getIntent();
        patienData = (PatienTtitle) intent.getSerializableExtra("patienData");
        if (null!= patienData){
            user_name_tv.setText(patienData.getUsername()+" "+ patienData.getSex()+" "+ patienData.getAge()+"岁");
            content_tv.setText(patienData.getSick());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            String time = simpleDateFormat.format(Long.valueOf(patienData.getSee_doc_time())*1000);
            time_tv.setText(time);
            propose_tv.setText(patienData.getAdvice());
//            String see_doc_time = simpleDateFormat.format(Long.valueOf(patienData.getSee_doc_time()));
            see_doc_time_tv.setText(patienData.getFz());
            patientDetailImageAdpter = new PatientDetailImageAdpter(this, patienData.getImgs());
            image_gv.setAdapter(patientDetailImageAdpter);
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.patient_detail_layout;
    }
}
