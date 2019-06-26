package com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.discovery.medicine.common.MyBaseActivity;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.model.EditModel;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.model.EditType;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.model.PersonInfoModel;
import com.xywy.retrofit.demo.BaseRetrofitResponse;


public class InfoEditTextActivity extends MyBaseActivity {
    /**
     * 个性签名
     */
    private String type;
    /**
     * 介绍
     */
    private EditText etInfo;


    public static void start(Context context, @EditType String type) {
        Intent intent = new Intent(context,
                InfoEditTextActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_edittext_info;
    }

    @Override
    protected void initView() {
        etInfo = (EditText) findViewById(R.id.et_info);

    }

    @Override
    protected void initData() {
        type = getIntent().getStringExtra("type");
        etInfo.setHint(EditModel.getHintByType(type));
        titleBarBuilder.setTitleText(EditModel.getTitleByType(type));
        titleBarBuilder.setRignt("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = etInfo.getText().toString().trim();
                if(EditModel.checkInput(type,str)){
                    PersonInfoModel.updateLocalUser(type, str,new BaseRetrofitResponse(){
                        @Override
                        public void onNext(Object o) {
                            finish();
                        }
                    });
                }
            }
        });
    }
}
