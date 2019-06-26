package com.xywy.askforexpert.module.main.patient.activity;

import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.model.GroupingData;
import com.xywy.askforexpert.model.PatienTtitle;
import com.xywy.askforexpert.model.PatientGroupingData;
import com.xywy.askforexpert.module.consult.ServiceProvider;
import com.xywy.askforexpert.module.main.patient.adapter.GroupingAdapter;
import com.xywy.askforexpert.module.main.patient.adapter.PatientItemAdapter;
import com.xywy.askforexpert.module.main.patient.service.BatchNoticeDataFlag;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by jason on 2018/11/13.
 */

public class BatchNoticeActivity extends YMBaseActivity{
    @Bind(R.id.grouping_list)
    ListView grouping_list;
    @Bind(R.id.next_btn)
    TextView next_btn;
    @OnClick(R.id.next_btn) void next(){
        StringBuffer stringBuffer = new StringBuffer();
        for (PatienTtitle patienTtitle : childList){
            if (patienTtitle.isSelected()){
                stringBuffer.append(patienTtitle.getUid()+",");
            }

        }
        if (stringBuffer.length()>0) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            startActivity(new Intent(this, BatchNoticeContentActivity.class)
                    .putExtra("uid", stringBuffer.toString()));
        }
    }
    @OnClick(R.id.pharmacy_search) void search(){
        startActivity(new Intent(this,PatientSearchActivity.class).
                putExtra("PatientList",childList).
                putExtra("localFlag",true));
    }
    private GroupingAdapter groupingAdapter;
    private String doctorId = YMUserService.getCurUserId();
    private ArrayList<GroupingData> data = new ArrayList<>();
    private ArrayList<PatienTtitle> childList = new ArrayList<>();
    private String uid;
    private boolean allSelected = false;
    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("批量通知");
        titleBarBuilder.addItem("全选", new ItemClickListener() {
            @Override
            public void onClick() {
                if (data!=null&data.size()!=0){
                    if (allSelected){
                        int nextNum = 0;
                        for (GroupingData groupingData : data){
                            for (PatienTtitle patienTtitle: groupingData.getChild()){
                                nextNum++;
                                next_btn.setText("下一步"+"("+nextNum+")");
                            }
                        }
                        allSelected = false;
                    }else{
                        for (GroupingData groupingData : data){
                            for (PatienTtitle patienTtitle: groupingData.getChild()){
                                next_btn.setText("下一步");
                            }
                        }
                        allSelected = true;
                    }
                }
                groupingAdapter.setAllSelected(allSelected);
            }
        }).build();
        groupingAdapter = new GroupingAdapter(this,data,true,new PatientItemAdapter.SelectCallBack() {
            @Override
            public void select() {
                int nextNum = 0;
                for (PatienTtitle patienTtitle : childList){
                    if (patienTtitle.isSelected()){
                        nextNum++;
                    }

                }
                if (nextNum==0) {
                    next_btn.setText("下一步");
                }else{
                    next_btn.setText("下一步"+"("+nextNum+")");
                }
            }
        });
        grouping_list.setAdapter(groupingAdapter);
    }

    @Override
    protected void initData() {
        ServiceProvider.getPatientGroupingList(doctorId, new Subscriber<PatientGroupingData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(PatientGroupingData entry) {
                data.addAll(entry.getData());
                groupingAdapter.setData(data);
                for (GroupingData groupingData : entry.getData()){
                    childList.addAll(groupingData.getChild());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (BatchNoticeDataFlag.getInstance().BACK_FLAG){
            BatchNoticeDataFlag.getInstance().BACK_FLAG = false;
            finish();
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.batch_notice_layout;
    }
}
