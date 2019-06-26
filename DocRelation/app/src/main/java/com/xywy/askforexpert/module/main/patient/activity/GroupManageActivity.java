package com.xywy.askforexpert.module.main.patient.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.model.GrouManageData;
import com.xywy.askforexpert.model.GrouManageList;
import com.xywy.askforexpert.module.consult.ServiceProvider;
import com.xywy.askforexpert.module.main.patient.adapter.GroupManageAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by jason on 2018/11/8.
 */

public class GroupManageActivity extends YMBaseActivity {
    @Bind(R.id.group_recycler)
    RecyclerView group_recycler;
    @OnClick(R.id.add_group_bt) void addGroup(){
        startActivityForResult(new Intent(this,AddGroupActivity.class),1);
    }
    private String doctorId = YMUserService.getCurUserId();
    private GroupManageAdapter groupManageAdapter;
    private ArrayList<GrouManageList> data = new ArrayList<>();

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("组管理");
        group_recycler.setLayoutManager(new LinearLayoutManager(this));
        groupManageAdapter = new GroupManageAdapter(this);
        groupManageAdapter.setData(data);
        group_recycler.setAdapter(groupManageAdapter);
    }

    @Override
    protected void initData() {
        loadData();
    }

    private void loadData(){
        ServiceProvider.getPatientGrouManageList(doctorId, new Subscriber<GrouManageData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(GrouManageData entry) {
                groupManageAdapter.setData(entry.getData());
                groupManageAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (resultCode==RESULT_OK){
                finish();
            }
        }else if(requestCode==1){
            if (resultCode==RESULT_OK){
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.group_manage_layout;
    }
}
