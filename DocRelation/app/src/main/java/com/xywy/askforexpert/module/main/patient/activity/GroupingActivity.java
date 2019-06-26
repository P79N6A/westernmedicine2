package com.xywy.askforexpert.module.main.patient.activity;

import android.content.Intent;
import android.widget.ListView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.model.GroupingData;
import com.xywy.askforexpert.model.PatienTtitle;
import com.xywy.askforexpert.model.PatientGroupingData;
import com.xywy.askforexpert.module.consult.ServiceProvider;
import com.xywy.askforexpert.module.main.patient.adapter.GroupingAdapter;
import com.xywy.askforexpert.module.main.patient.adapter.PatientItemAdapter;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import rx.Subscriber;

/**
 * Created by jason on 2018/11/8.
 */

public class GroupingActivity extends YMBaseActivity {
    @Bind(R.id.grouping_list)
    ListView grouping_list;
    private GroupingAdapter groupingAdapter;
    private String doctorId = YMUserService.getCurUserId();
    private ArrayList<GroupingData> data = new ArrayList<>();

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("患者分组");
        titleBarBuilder.addItem("组管理",new ItemClickListener() {
            @Override
            public void onClick() {
                startActivity(new Intent(GroupingActivity.this,GroupManageActivity.class));
            }
        }).build();
        groupingAdapter = new GroupingAdapter(this,data,false, new PatientItemAdapter.SelectCallBack() {
            @Override
            public void select() {

            }
        }, new PatientItemAdapter.RemoveCallBack() {
            @Override
            public void remove(PatienTtitle patienTtitle) {
                data.get(0).getChild().add(patienTtitle);
                groupingAdapter.setData(data);
            }
        });
        grouping_list.setAdapter(groupingAdapter);
    }

    @Override
    protected void initData() {

    }

    private void loadData(){
        ServiceProvider.getPatientGroupingList(doctorId, new Subscriber<PatientGroupingData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(PatientGroupingData entry) {
                data.clear();
                data.addAll(entry.getData());
                groupingAdapter.setData(data);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_grouping;
    }
}
