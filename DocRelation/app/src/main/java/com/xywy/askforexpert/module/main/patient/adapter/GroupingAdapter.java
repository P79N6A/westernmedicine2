package com.xywy.askforexpert.module.main.patient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.GroupingData;
import com.xywy.askforexpert.widget.view.MyListView;

import java.util.ArrayList;

/**
 * Created by jason on 2018/11/8.
 */

public class GroupingAdapter extends BaseAdapter {
    private ArrayList<GroupingData> data;
    private Context context;
    private boolean selectFlag;
    private PatientItemAdapter.SelectCallBack selectCallBack;
    private boolean allSelected = false;
    private PatientItemAdapter.RemoveCallBack removeCallBack;

    public GroupingAdapter(Context context, ArrayList<GroupingData> data, boolean selectFlag
            , PatientItemAdapter.SelectCallBack selectCallBack, PatientItemAdapter.RemoveCallBack removeCallBack) {
        this.context = context;
        this.data = data;
        this.selectFlag = selectFlag;
        this.selectCallBack = selectCallBack;
        this.removeCallBack = removeCallBack;
    }

    public GroupingAdapter(Context context, ArrayList<GroupingData> data, boolean selectFlag
            , PatientItemAdapter.SelectCallBack selectCallBack) {
        this.context = context;
        this.data = data;
        this.selectFlag = selectFlag;
        this.selectCallBack = selectCallBack;
    }

    @Override
    public int getCount() {
        return data.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_grouping_patient, null);
            holder = new ViewHolder();
            holder.title_ll = (LinearLayout) convertView
                    .findViewById(R.id.title_ll);
            holder.title_img = (ImageView) convertView
                    .findViewById(R.id.title_img);
            holder.title_name = (TextView) convertView
                    .findViewById(R.id.title_name);
            holder.patient_num = (TextView) convertView
                    .findViewById(R.id.patient_num);
            holder.patient_list = (MyListView) convertView
                    .findViewById(R.id.patient_list);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.get(position).isItemFlag()){
                    holder.patient_list.setVisibility(View.GONE);
                    holder.title_img.setImageResource(R.drawable.arrow_right_small);
                    data.get(position).setItemFlag(false);
                } else {
                    holder.patient_list.setVisibility(View.VISIBLE);
                    holder.title_img.setImageResource(R.drawable.arrow_down);
                    data.get(position).setItemFlag(true);
                }
            }
        });
        if (allSelected){
            holder.title_img.setImageResource(R.drawable.arrow_down);
            data.get(position).setItemFlag(true);
            holder.patient_list.setVisibility(View.VISIBLE);
        }else{
            holder.patient_list.setVisibility(View.GONE);
            holder.title_img.setImageResource(R.drawable.arrow_right_small);
            data.get(position).setItemFlag(false);
        }
        PatientItemAdapter patientItemAdapter = new PatientItemAdapter(context,data.get(position).getChild()
                ,selectFlag,selectCallBack,allSelected,removeCallBack);
        holder.patient_list.setAdapter(patientItemAdapter);
        holder.title_name.setText(data.get(position).getG_name());
        holder.patient_num.setText(data.get(position).getChild().size()+"");
        return convertView;
    }

    public void setAllSelected(boolean allSelected) {
        this.allSelected = allSelected;
        notifyDataSetChanged();
    }

    class ViewHolder {
        LinearLayout title_ll;
        ImageView title_img;
        TextView title_name;
        TextView patient_num;
        MyListView patient_list;
    }

    public void setData(ArrayList<GroupingData> data) {
        this.data = data;
        notifyDataSetChanged();
    }


}
