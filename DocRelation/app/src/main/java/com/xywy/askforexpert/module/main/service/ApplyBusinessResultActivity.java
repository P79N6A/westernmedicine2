package com.xywy.askforexpert.module.main.service;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.model.Service;
import com.xywy.askforexpert.module.main.home.request.ApplyClubOrImwdStateRequest;
import com.xywy.askforexpert.module.my.userinfo.ApplyForFamilyDoctorActivity;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;

import java.util.Map;

/**
 * Created by xugan on 2018/4/19.
 */

public class ApplyBusinessResultActivity extends YMBaseActivity{

    private String title;
    private int type;
    private Service service;
    private ImageView iv;
    private TextView tv_state;
    private TextView tv_apply;
    private TextView tv_refuse_reason;
    private int mRequestCode = 100;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_apply_business_result;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        title = intent.getStringExtra(Constants.INTENT_KEY_TITLE);
        titleBarBuilder.setTitleText(title+"服务申请");
        Map<String, Service> serviceMark = YMApplication.getServiceMark();
        service = serviceMark.get(this.title);
        iv = (ImageView) findViewById(R.id.iv);

        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_apply = (TextView) findViewById(R.id.tv_apply);
        tv_refuse_reason = (TextView) findViewById(R.id.tv_refuse_reason);
        if(Constants.FUWU_AUDIT_STATUS_0.equals(service.status+"")){
            showSuccessState();
        }else {
            iv.setImageResource(R.drawable.refuse);
            tv_state.setText("审核未通过");
            tv_apply.setVisibility(View.VISIBLE);
            tv_refuse_reason.setText(TextUtils.isEmpty(service.mark)?"未通过原因：":"未通过原因："+ service.mark);
        }

        tv_apply.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //审核未通过-重新申请 stone
                StatisticalTools.eventCount(ApplyBusinessResultActivity.this, Constants.REAPPLY);

                if(Constants.IMWD_REWARD_EN.equals(service.work_sign)){ //即时问答(悬赏) // 2悬赏 3指定
                    applyImwdState(Constants.TYPE_2);
                }else if(Constants.IMWD_ASSIGN_EN.equals(service.work_sign)){
                    applyImwdState( Constants.TYPE_3);
                }else if(Constants.FAMILYDOCTOR_EN.equals(service.work_sign)){  //重新申请开通家庭医生
                    Intent intent = new Intent();
                    intent.setClass(YMApplication.getAppContext(), ApplyForFamilyDoctorActivity.class);
                    startActivityForResult(intent,mRequestCode);
                }else if(Constants.CLUB_ASSIGN_EN.equals(service.work_sign)){
                    //问题广场指定
                    applyClubAssignState();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == mRequestCode && resultCode == RESULT_OK){
            showSuccessState();
        }
    }

    //医生即时问答(悬赏，指定)提交审核
    private void applyImwdState(final int type) {
        String curUserId = YMUserService.getCurUserId();
        ApplyClubOrImwdStateRequest.getInstance().getImwdState(curUserId,type).subscribe(new BaseRetrofitResponse<BaseData>(){
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onNext(BaseData baseData) {
                super.onNext(baseData);
                showSuccessState();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    private void showSuccessState() {
        iv.setImageResource(R.drawable.commit_success);
        tv_state.setText("提交成功");
        tv_apply.setVisibility(View.GONE);
        tv_refuse_reason.setText("服务审核中，审核通过后，服务将自动开通");
    }

    //医生问题广场指定付费提交审核
    private void applyClubAssignState() {
        String curUserId = YMUserService.getCurUserId();
        ApplyClubOrImwdStateRequest.getInstance().getClubAssignState(curUserId).subscribe(new BaseRetrofitResponse<BaseData>(){
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onNext(BaseData baseData) {
                super.onNext(baseData);
                showSuccessState();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    @Override
    protected void initData() {

    }
}
