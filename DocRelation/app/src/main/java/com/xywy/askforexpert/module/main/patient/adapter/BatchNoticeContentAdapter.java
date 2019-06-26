package com.xywy.askforexpert.module.main.patient.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.model.BatchNoticeData;
import com.xywy.askforexpert.model.DeleteBatchNotice;
import com.xywy.askforexpert.module.consult.ServiceProvider;
import com.xywy.askforexpert.module.discovery.medicine.util.ToastUtils;
import com.xywy.askforexpert.module.main.patient.activity.EditBatchNoticeContentActivity;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVSingleTypeBaseAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import rx.Subscriber;

/**
 * Created by jason on 2018/11/14.
 */

public class BatchNoticeContentAdapter extends XYWYRVSingleTypeBaseAdapter<BatchNoticeData> {
    private Context context;
    private String doctorId = YMUserService.getCurUserId();
    public BatchNoticeContentAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected int getItemLayoutResId() {
        return R.layout.batch_notice_content_item;
    }

    @Override
    protected void convert(final ViewHolder holder, final BatchNoticeData item, final int position) {
        holder.setOnClickListener(R.id.root_view, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.isSelectedFlag()){
                    holder.setImageResource(R.id.selected_img,R.drawable.un_selected_icon);
                    item.setSelectedFlag(false);
                }else{
                    holder.setImageResource(R.id.selected_img,R.drawable.selected_icon);
                    item.setSelectedFlag(true);
                }
            }
        });
        holder.setText(R.id.title_tv,item.getTitle());
        holder.setText(R.id.content_tv,item.getContent());
        holder.setOnClickListener(R.id.delete_ll, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceProvider.deleteBatchNoticeContent(item.getId(), new Subscriber<DeleteBatchNotice>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(DeleteBatchNotice data) {
                        if (data.getCode()==10000){
                            ToastUtils.longToast("删除成功");
                            getDatas().remove(position);
                            notifyDataSetChanged();
                        }else{
                            ToastUtils.longToast(data.getMsg());
                        }
                    }
                });
            }
        });
        holder.setOnClickListener(R.id.edit_ll, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,EditBatchNoticeContentActivity.class).putExtra("item",item));
            }
        });
    }
}
