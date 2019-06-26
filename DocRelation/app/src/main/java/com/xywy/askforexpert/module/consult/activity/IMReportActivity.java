package com.xywy.askforexpert.module.consult.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.consultentity.CommonRspEntity;
import com.xywy.askforexpert.module.consult.ConsultConstants;
import com.xywy.askforexpert.module.consult.ServiceProvider;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import rx.Subscriber;

/**
 * Created by jason on 2018/10/11.
 */

public class IMReportActivity extends YMBaseActivity{
    @Bind(R.id.report_reason_1)
    RelativeLayout report_reason_1;
    @Bind(R.id.report_reason_2)
    RelativeLayout report_reason_2;
    @Bind(R.id.report_reason_3)
    RelativeLayout report_reason_3;
    @Bind(R.id.report_reason_4)
    RelativeLayout report_reason_4;

    @Bind(R.id.report_check_box1)
    ImageView report_check_box1;
    @Bind(R.id.report_check_box2)
    ImageView report_check_box2;
    @Bind(R.id.report_check_box3)
    ImageView report_check_box3;
    @Bind(R.id.report_check_box4)
    ImageView report_check_box4;

    @Bind(R.id.report_reason)
    EditText report_reason;

    private int report_type = 1;
    private ArrayList<ImageView> views;
    private String questionId;

    @Override
    protected void initView() {
        initTitleBar();
        views = new ArrayList<>();
        views.add(report_check_box1);
        views.add(report_check_box2);
        views.add(report_check_box3);
        views.add(report_check_box4);
        report_reason_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report_type = 1;
                selectReportType(0);
            }
        });

        report_reason_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report_type = 2;
                selectReportType(1);
            }
        });

        report_reason_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report_type = 3;
                selectReportType(2);
            }
        });

        report_reason_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report_type = 4;
                selectReportType(3);
            }
        });
    }

    private void initTitleBar() {
        titleBarBuilder.setTitleText("举报");

        titleBarBuilder.addItem("确定",new ItemClickListener() {
            @Override
            public void onClick() {
                if (!TextUtils.isEmpty(report_reason.getText().toString())&&report_reason.getText().length()>=5) {
                    showProgressDialog("提交中");
                    ServiceProvider.tipOff(YMUserService.getCurUserId(), questionId,report_type+"" ,report_reason.getText().toString(), new Subscriber<CommonRspEntity>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtils.shortToast("举报失败");
                            LogUtils.e(e.getMessage());
                            hideProgressDialog();
                        }

                        @Override
                        public void onNext(CommonRspEntity commonRspEntity) {
                            if (commonRspEntity != null && commonRspEntity.getCode() == ConsultConstants.CODE_REQUEST_SUCCESS) {
                                ToastUtils.shortToast("举报成功");
                                setResult(Activity.RESULT_OK);
                                onBackPressed();
                            } else {
                                ToastUtils.shortToast("举报失败");
                            }
                            hideProgressDialog();
                        }
                    });
                }else{
                    ToastUtils.longToast("举报原因最少输入五个字");
                }
            }
        }).build();
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        questionId = intent.getStringExtra("questionId");
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout__im_report;
    }

    private void selectReportType(int type){

        for (int i = 0;i<views.size();i++){
            if (type==i){
                views.get(i).setVisibility(View.VISIBLE);
            }else{
                views.get(i).setVisibility(View.GONE);
            }
        }
    }
}
