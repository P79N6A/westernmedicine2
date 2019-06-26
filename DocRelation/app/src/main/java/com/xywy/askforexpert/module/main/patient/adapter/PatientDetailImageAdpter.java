package com.xywy.askforexpert.module.main.patient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xywy.askforexpert.R;

import java.util.ArrayList;

/**
 * Created by jason on 2018/11/2.
 */

public class PatientDetailImageAdpter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> images;

    public PatientDetailImageAdpter(Context mContext, ArrayList<String> images) {
        this.mContext = mContext;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.patient_photo_grid_item, null);
            holder = new ViewHolder();


            holder.patient_iv = (ImageView) convertView
                    .findViewById(R.id.patient_iv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext).load(images.get(position)).into(holder.patient_iv);
        return convertView;
    }

    public void setData(ArrayList<String> images) {
        this.images.addAll(images);
        notifyDataSetChanged();
    }

    public void addData() {
    }

    class ViewHolder {
        ImageView patient_iv;
    }
}
