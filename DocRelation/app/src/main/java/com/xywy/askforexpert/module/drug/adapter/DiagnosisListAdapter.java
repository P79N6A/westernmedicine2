package com.xywy.askforexpert.module.drug.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.drug.DiagnosisSearchActivity;
import com.xywy.askforexpert.module.drug.bean.BasisDoctorDiagnose;

import java.util.ArrayList;

/**
 * Created by jason on 2019/5/8.
 */

public class DiagnosisListAdapter extends BaseAdapter{
    private DiagnosisSearchActivity context;
    private ArrayList<BasisDoctorDiagnose> data = new ArrayList<>();
    private String keyword = "";
    public DiagnosisListAdapter(DiagnosisSearchActivity context, ArrayList<BasisDoctorDiagnose> data) {
        this.context = context;
        this.data.addAll(data);
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.diagnosis_list_item, null);
            holder.diagnosi_name = (TextView) convertView.findViewById(R.id.diagnosi_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (TextUtils.isEmpty(keyword)){
            holder.diagnosi_name.setText(data.get(position).getName());
        } else{

            if(-1 != data.get(position).getName().indexOf(keyword.charAt(0))){
                Spannable span = new SpannableString(data.get(position).getName());
                span.setSpan(new ForegroundColorSpan(Color.parseColor("#999999")), data.get(position).getName().indexOf(keyword.charAt(0)),
                        data.get(position).getName().indexOf(keyword.charAt(0)) + keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.diagnosi_name.setText(span);
            } else {
                holder.diagnosi_name.setText(data.get(position).getName());
            }




        }
        holder.diagnosi_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.setResult(context.RESULT_OK,new Intent().putExtra("DiagnosisData",data.get(position)));
                context.finish();
            }
        });



        return convertView;
    }

    public void setData(ArrayList<BasisDoctorDiagnose> data,String keyword) {
        this.data.clear();
        this.data.addAll(data);
        this.keyword = keyword;
        notifyDataSetChanged();
    }


    class ViewHolder {
        public TextView diagnosi_name;
    }
}
