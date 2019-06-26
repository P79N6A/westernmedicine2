package com.xywy.askforexpert.module.main.patient.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.model.DeleteGroup;
import com.xywy.askforexpert.model.GrouManageList;
import com.xywy.askforexpert.module.consult.ServiceProvider;
import com.xywy.askforexpert.module.discovery.medicine.util.ToastUtils;
import com.xywy.askforexpert.module.main.patient.activity.PatientListActivity;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVSingleTypeBaseAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import rx.Subscriber;

/**
 * Created by jason on 2018/11/8.
 */

public class GroupManageAdapter extends XYWYRVSingleTypeBaseAdapter<GrouManageList> {
    private Activity context;
    private String doctorId = YMUserService.getCurUserId();
    public GroupManageAdapter(Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    protected int getItemLayoutResId() {
        return R.layout.group_manage_item;
    }

    @Override
    protected void convert(ViewHolder holder, final GrouManageList item, final int position) {
        holder.setText(R.id.title_name,item.getG_name());
        holder.setOnClickListener(R.id.add_patient_tv, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivityForResult(new Intent(context,PatientListActivity.class).putExtra("gid",item.getId()),2);
            }
        });
        holder.setOnClickListener(R.id.delete_patient_tv, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<GrouManageList> data = getDatas();
                ServiceProvider.deleteGroupManageList(doctorId, data.get(position).getId(), new Subscriber<DeleteGroup>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.longToast("删除失败");
                    }

                    @Override
                    public void onNext(DeleteGroup grouManageData) {
                        if (grouManageData.getCode()==10000){
                            data.remove(position);
                            ToastUtils.longToast("删除成功");
                            notifyDataSetChanged();
                        }else {
                            ToastUtils.longToast(grouManageData.getMsg());
                        }
                    }
                });



            }
        });
    }


}
