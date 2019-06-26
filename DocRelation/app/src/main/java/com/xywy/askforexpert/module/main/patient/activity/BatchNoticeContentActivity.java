package com.xywy.askforexpert.module.main.patient.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.model.BatchNoticeContent;
import com.xywy.askforexpert.model.BatchNoticeData;
import com.xywy.askforexpert.model.consultentity.QuestionMsgListRspEntity;
import com.xywy.askforexpert.module.consult.ServiceProvider;
import com.xywy.askforexpert.module.discovery.medicine.util.ToastUtils;
import com.xywy.askforexpert.module.main.patient.adapter.BatchNoticeContentAdapter;
import com.xywy.askforexpert.module.main.patient.service.BatchNoticeDataFlag;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by jason on 2018/11/13.
 */

public class BatchNoticeContentActivity extends YMBaseActivity {
    @Bind(R.id.batch_notice_recycler)
    RecyclerView batch_notice_recycler;
    private String uid;


    @OnClick(R.id.send_btn) void send(){
        StringBuffer stringBuffer = new StringBuffer();
        for (BatchNoticeData batchNoticeData:batchNoticeContentAdapter.getDatas()){
            if (batchNoticeData.isSelectedFlag()){
                stringBuffer.append(batchNoticeData.getId()+",");
            }
        }
        if (!TextUtils.isEmpty(stringBuffer.toString())){
            stringBuffer.deleteCharAt(stringBuffer.length()-1);
            ServiceProvider.sendBatchNoticeContent(doctorId, uid, stringBuffer.toString(), new Subscriber<QuestionMsgListRspEntity>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(QuestionMsgListRspEntity questionMsgListRspEntity) {
                    if (questionMsgListRspEntity.getCode()==10000){
                        ToastUtils.longToast("发送成功");
                        BatchNoticeDataFlag.getInstance().BACK_FLAG = true;
                        finish();
                    }else{
                        ToastUtils.longToast(questionMsgListRspEntity.getMsg());
                    }
                }
            });
        }else{
            ToastUtils.longToast("选择通知内容");
        }

    }
    private String doctorId = YMUserService.getCurUserId();
    private BatchNoticeContentAdapter batchNoticeContentAdapter;

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("批量通知");
        titleBarBuilder.addItem("新增", new ItemClickListener() {
            @Override
            public void onClick() {
                startActivity(new Intent(BatchNoticeContentActivity.this,EditBatchNoticeContentActivity.class));
            }
        }).build();

        batch_notice_recycler.setLayoutManager(new LinearLayoutManager(this));
        batchNoticeContentAdapter = new BatchNoticeContentAdapter(this);
        batch_notice_recycler.setAdapter(batchNoticeContentAdapter);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData(){
        ServiceProvider.getBatchNoticeContentList(doctorId, new Subscriber<BatchNoticeContent>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BatchNoticeContent batchNoticeContent) {
                if (batchNoticeContent.getCode()==10000) {
                    if (batchNoticeContent.getData().size()!=0) {
                        batchNoticeContentAdapter.setData(batchNoticeContent.getData());
                        batchNoticeContentAdapter.notifyDataSetChanged();
                    }else{
                        ToastUtils.longToast("暂无消息通知列表");
                    }
                }
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.batch_notice_content_layout;
    }
}
