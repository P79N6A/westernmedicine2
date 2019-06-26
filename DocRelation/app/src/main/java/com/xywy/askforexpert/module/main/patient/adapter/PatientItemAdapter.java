package com.xywy.askforexpert.module.main.patient.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.model.PatienTtitle;
import com.xywy.askforexpert.model.consultentity.QuestionMsgListRspEntity;
import com.xywy.askforexpert.module.consult.ServiceProvider;
import com.xywy.askforexpert.module.discovery.medicine.util.ToastUtils;
import com.xywy.askforexpert.module.main.patient.activity.PatientDetailActivity;
import com.xywy.askforexpert.widget.CircleImageView;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by jason on 2018/11/8.
 */

public class PatientItemAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PatienTtitle> child;
    private boolean selectFlag;
    private SelectCallBack selectCallBack;
    private boolean allSelected;
    private RemoveCallBack removeCallBack;
    private String doctorId = YMUserService.getCurUserId();
    public PatientItemAdapter(Context context, ArrayList<PatienTtitle> child, boolean selectFlag,
                              SelectCallBack selectCallBack, boolean allSelected, RemoveCallBack removeCallBack) {
        this.context = context;
        this.child = child;
        this.selectFlag = selectFlag;
        this.selectCallBack = selectCallBack;
        this.allSelected = allSelected;
        this.removeCallBack = removeCallBack;
    }



    @Override
    public int getCount() {
        return child.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.patien_content_item, null);
            holder = new ViewHolder();
            holder.iv_head_icon = (CircleImageView) convertView.findViewById(R.id.iv_head_icon);
            holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
            holder.age_sex_tv = (TextView) convertView.findViewById(R.id.age_sex_tv);
            holder.advice_tv = (TextView) convertView.findViewById(R.id.advice_tv);
            holder.line1 = convertView.findViewById(R.id.line1);
            holder.line2 = convertView.findViewById(R.id.line2);
            holder.select_img = (ImageView) convertView.findViewById(R.id.select_img);
            holder.root_view = (LinearLayout) convertView.findViewById(R.id.root_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.line1.setVisibility(View.VISIBLE);
        holder.line2.setVisibility(View.GONE);
        Glide.with(context).load(child.get(position).getPhoto()).into(holder.iv_head_icon);
        holder.user_name.setText(child.get(position).getUsername());
        holder.age_sex_tv.setText(child.get(position).getSex()+" "+child.get(position).getAge()+"岁");
        holder.advice_tv.setText(child.get(position).getAdvice());
        if (allSelected){
            child.get(position).setSelected(true);
            holder.select_img.setImageResource(R.drawable.selected_icon);
        }
        if (selectFlag){
            holder.select_img.setVisibility(View.VISIBLE);
            holder.root_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (child.get(position).isSelected()){
                        holder.select_img.setImageResource(R.drawable.un_selected_icon);
                        child.get(position).setSelected(false);
                    }else{
                        holder.select_img.setImageResource(R.drawable.selected_icon);
                        child.get(position).setSelected(true);
                    }
                    selectCallBack.select();
                }
            });

        }else{
            holder.select_img.setVisibility(View.GONE);
            holder.root_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context,PatientDetailActivity.class).putExtra("patienData",child.get(position)));
                }
            });
            holder.root_view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showNormalDialog(position);
                    return false;
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        CircleImageView iv_head_icon;
        TextView user_name;
        TextView age_sex_tv;
        TextView advice_tv;
        View line1;
        View line2;
        ImageView select_img;
        LinearLayout root_view;
    }

    public interface SelectCallBack{
        void select();
    }

    public interface RemoveCallBack{
        void remove(PatienTtitle patienTtitle);
    }
    private void showNormalDialog(final int position){
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(context);
        normalDialog.setMessage("您确定要移除此患者吗？");
        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which)
            {
                if(null!=removeCallBack) {
                    ServiceProvider.deletePatient(doctorId, child.get(position).getId(), new Subscriber<QuestionMsgListRspEntity>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(QuestionMsgListRspEntity deleteBatchNotice) {
                            if (deleteBatchNotice.getCode()==10000){
                                PatienTtitle patienTtitle = child.get(position);
                                child.remove(position);
                                removeCallBack.remove(patienTtitle);
                                dialog.dismiss();
                            }else{
                                ToastUtils.longToast(deleteBatchNotice.getMsg());
                            }

                        }
                    });

                }

            }
        });
        normalDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        normalDialog.show();
    }
}
