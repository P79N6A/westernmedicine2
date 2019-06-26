package com.xywy.askforexpert.module.main.patient.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.model.BatchNoticeData;
import com.xywy.askforexpert.model.consultentity.QuestionMsgListRspEntity;
import com.xywy.askforexpert.module.consult.ServiceProvider;
import com.xywy.askforexpert.module.discovery.medicine.util.ToastUtils;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import butterknife.Bind;
import rx.Subscriber;

/**
 * Created by jason on 2018/11/14.
 */

public class EditBatchNoticeContentActivity extends YMBaseActivity {
    @Bind(R.id.title_edit)
    EditText title_edit;
    @Bind(R.id.content_edit)
    EditText content_edit;
    private BatchNoticeData batchNoticeData;
    private String act;
    private String doctorId = YMUserService.getCurUserId();
    private String title = "";
    private String id = "";
    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("编辑内容");
        titleBarBuilder.addItem("添加", new ItemClickListener() {
            @Override
            public void onClick() {
                if (TextUtils.isEmpty(title_edit.getText())){
                    ToastUtils.longToast("请输入通知标题");
                    return;
                }
                if (!TextUtils.isEmpty(content_edit.getText())) {
                    ServiceProvider.addBatchNoticeContent(doctorId
                            , title
                            , content_edit.getText().toString()
                            , id, act, new Subscriber<QuestionMsgListRspEntity>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(QuestionMsgListRspEntity questionMsgListRspEntity) {
                                    if (questionMsgListRspEntity.getCode()==10000){
                                        if (null!=batchNoticeData) {
                                            ToastUtils.longToast("修改成功");
                                        }else{
                                            ToastUtils.longToast("添加成功");
                                        }
                                        finish();
                                    }else{
                                        ToastUtils.longToast(questionMsgListRspEntity.getMsg());
                                    }
                                }
                            });
                } else{
                    ToastUtils.longToast("请输入通知内容");
                }
            }
        }).build();
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        batchNoticeData = (BatchNoticeData) intent.getSerializableExtra("item");
        if (null!=batchNoticeData){
            title_edit.setText(TextUtils.isEmpty(batchNoticeData.getTitle())?"":batchNoticeData.getTitle());
            content_edit.setText(batchNoticeData.getContent());
            title = title_edit.getText().toString();
            id = batchNoticeData.getId();
            act = "2";
        }else{
            act = "1";
        }

        title_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                title = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.edit_batch_notice_content_layout;
    }
}
