package com.xywy.askforexpert.module.drug;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.module.drug.adapter.PrescriptionDetailAdapter;
import com.xywy.askforexpert.module.drug.bean.PrescriptionBean;
import com.xywy.askforexpert.module.drug.bean.PrescriptionDetailBean;
import com.xywy.askforexpert.module.drug.request.DrugAboutRequest;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.uilibrary.titlebar.ItemClickListener;
import com.xywy.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import cn.org.bjca.sdk.core.bean.ResultBean;
import cn.org.bjca.sdk.core.kit.BJCASDK;
import cn.org.bjca.sdk.core.values.ConstantParams;

/**
 * Created by xugan on 2018/7/9.
 */
public class PrescriptionDetailActivity extends YMBaseActivity implements View.OnClickListener {

    private TextView tv_status,tv_name,tv_age,tv_gender,tv_recipe_time,tv_section,tv_reslult;
    private RecyclerView recipe_medicine_list;
    private TextView tv_pay,tv_doctor_name,tv_chenck_doctor_name;
    private Button btn;

    private String pid;//处方id

    private List mList;
    private PrescriptionDetailAdapter adapter;
    private String mDoctorId = YMUserService.getCurUserId();


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_prescription_detail;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                //取消处方
                DrugAboutRequest.getInstance().getPrescriptionCancel(mDoctorId,pid).
                        subscribe(new BaseRetrofitResponse<BaseData>(){
                            @Override
                            public void onStart() {
                                showProgressDialog("");
                            }

                            @Override
                            public void onNext(BaseData baseData) {
                                ToastUtils.shortToast(baseData.getMsg());
                                if(10000==(baseData.getCode())){
                                    btn.setVisibility(View.GONE);
                                    tv_status.setText("已失效");
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                hideProgressDialog();
                            }

                            @Override
                            public void onCompleted() {
                                super.onCompleted();
                                hideProgressDialog();
                            }
                        });
                break;
           default:
               break;
        }
    }

    @Override
    protected void initView() {
        pid = getIntent().getStringExtra(Constants.KEY_ID);
        titleBarBuilder.setTitleText("处方详情");
        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_gender = (TextView) findViewById(R.id.tv_gender);
        tv_recipe_time = (TextView) findViewById(R.id.tv_recipe_time);
        tv_section = (TextView) findViewById(R.id.tv_section);
        tv_reslult = (TextView) findViewById(R.id.tv_reslult);
        recipe_medicine_list = (RecyclerView) findViewById(R.id.recipe_medicine_list);
        tv_pay = (TextView) findViewById(R.id.tv_pay);
        tv_doctor_name = (TextView) findViewById(R.id.tv_doctor_name);
        tv_chenck_doctor_name = (TextView) findViewById(R.id.tv_chenck_doctor_name);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(this);
        mList = new ArrayList<>();
        adapter = new PrescriptionDetailAdapter(PrescriptionDetailActivity.this);
        recipe_medicine_list.setLayoutManager(new GridLayoutManager(this, 1));
        adapter.setData(mList);
        recipe_medicine_list.setAdapter(adapter);
    }

    @Override
    public void initData() {
        getPrescriptionDetailData();
    }

    private void getPrescriptionDetailData() {
        DrugAboutRequest.getInstance().getPrescriptionDetail(mDoctorId,pid).subscribe(new BaseRetrofitResponse<BaseData<PrescriptionDetailBean>>(){
            @Override
            public void onStart() {
                showProgressDialog("");
            }

            @Override
            public void onNext(BaseData<PrescriptionDetailBean> prescriptionDetailBeanBaseData) {
                handleList(prescriptionDetailBeanBaseData);
            }

            @Override
            public void onError(Throwable e) {
                hideProgressDialog();
                super.onError(e);
            }

            @Override
            public void onCompleted() {
                hideProgressDialog();
            }
        });
    }

    /**
     * 处理列表请求结果
     * @param entry
     */
    private void handleList( BaseData<PrescriptionDetailBean> entry) {
        if (entry != null && null != entry.getData()) {
            PrescriptionDetailBean data = entry.getData();
            if(null !=data && null != data.prescription){
                final PrescriptionBean prescription = data.prescription;
                if (!TextUtils.isEmpty(prescription.sign_state)&&prescription.sign_state.equals("2")){
                    tv_doctor_name.setTextColor(Color.RED);
                    tv_doctor_name.setBackgroundResource(R.drawable.prescription_doctor_name_bg);
                }
                tv_status.setText(prescription.statusText);
                tv_name.setText("姓名:"+prescription.uname);
                tv_gender.setText("性别:"+(prescription.usersex.equals("1")?"男":"女"));
                tv_age.setText("年龄:"+prescription.age);
                tv_recipe_time.setText("时间:"+TimeUtils.getPhpStrDate(prescription.time, "yyyy-MM-dd"));
                tv_section.setText("科室:"+prescription.getDepart());
                tv_reslult.setText("初步诊断:"+prescription.getDiagnosis());
                tv_pay.setText("药费:"+prescription.prePrice+"元");
                tv_doctor_name.setText(prescription.getDname());
                if (!TextUtils.isEmpty(prescription.getReviewer())){
                    tv_chenck_doctor_name.setText(prescription.getReviewer());
                    tv_chenck_doctor_name.setTextColor(Color.RED);
                    tv_chenck_doctor_name.setBackgroundResource(R.drawable.prescription_doctor_name_bg);
                }
                if("3".equals(prescription.appstate)){
                    btn.setText("失效原因:"+prescription.getReason());
                    btn.setTextColor(getResources().getColor(R.color.color_ff3333));
                    btn.setBackgroundColor(getResources().getColor(R.color.white));
                    btn.setEnabled(false);
                }else if("2".equals(prescription.appstate)){
                    btn.setVisibility(View.GONE);
                    tv_status.setVisibility(View.GONE);
                    findViewById(R.id.iv_seal).setVisibility(View.VISIBLE);
                }
                if (!TextUtils.isEmpty(prescription.uniqueId)){
                    if (prescription.sign_state.equals("1")){
                        titleBarBuilder.addItem("签名", new ItemClickListener() {
                            @Override
                            public void onClick() {
                                showLoadDataDialog();
                                BJCASDK.getInstance().signRecipe(PrescriptionDetailActivity.this, Constants.YWQ_CLIENTID, prescription.uniqueId);
                            }
                        }).build();
                    }

                }
            }

            mList = data.drug;
            adapter.setData(mList);
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        hideProgressDialog();
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "用户取消操作", Toast.LENGTH_SHORT).show();
            return;
        } else if (requestCode == ConstantParams.ACTIVITY_SIGN_DATA) {// 签名返回码
            String result = data.getStringExtra(ConstantParams.KEY_SIGN_BACK);
            Gson gson = new Gson();
            ResultBean resultBean = gson.fromJson(result, ResultBean.class);
            if (resultBean != null && TextUtils.equals(resultBean.getStatus(), ConstantParams.SUCCESS)) {
                Toast.makeText(this, "重新签名成功", Toast.LENGTH_SHORT).show();
                tv_doctor_name.setTextColor(Color.RED);
                titleBarBuilder.addItem("",null).build();
            } else {
                Toast.makeText(this, "签名失败!\n错误码：" + resultBean.getStatus() + "\n错误提示：" + resultBean.getMessage()
                        , Toast.LENGTH_SHORT).show();
            }
        }
    }
}