package com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.CertUploadEntity;
import com.xywy.util.UriHelper;
import com.xywy.util.XYImageLoader;

import java.util.List;

/**
 * Created by zhangzheng on 2017/4/12.
 */

public class PersonCertUploadAdapter extends BaseAdapter {
    private Context context;
    private List<CertUploadEntity> data;

    public PersonCertUploadAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_upload_pic, null);
            holder = new ViewHolder();
            holder.rlInitContainer = (RelativeLayout) convertView.findViewById(R.id.rl_init);
            holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_img);
            holder.tvText = (TextView) convertView.findViewById(R.id.tv_text);
            holder.rlImgContainer = (RelativeLayout) convertView.findViewById(R.id.rl_img);
            holder.ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete);
            holder.ivSelect = (ImageView) convertView.findViewById(R.id.iv_select);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CertUploadEntity entity = data.get(position);
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entity.setImgUrl("");
                notifyDataSetChanged();
            }
        });
        if (entity.getImgUrl() != null && !entity.getImgUrl().equals("")) {
            holder.rlInitContainer.setVisibility(View.GONE);
            holder.rlImgContainer.setVisibility(View.VISIBLE);
            String path;
            if (entity.getImgUrl().startsWith("http")) {
                path = entity.getImgUrl();
            } else {
                path = UriHelper.getUriFromFile(entity.getImgUrl());
            }
            XYImageLoader.getInstance().displayImage(path, holder.ivPic);
        } else {
            holder.rlInitContainer.setVisibility(View.VISIBLE);
            holder.rlImgContainer.setVisibility(View.GONE);
        }

        if (entity.getText() == null || entity.getText().equals("")) {
            holder.tvText.setVisibility(View.GONE);
        } else {
            holder.tvText.setVisibility(View.VISIBLE);
            holder.tvText.setText(entity.getText());
        }

        return convertView;
    }

    private class ViewHolder {
        RelativeLayout rlInitContainer;
        RelativeLayout rlImgContainer;
        ImageView ivSelect;
        ImageView ivPic;
        ImageView ivDelete;
        TextView tvText;
    }

    public List<CertUploadEntity> getData() {
        return data;
    }

    public void setData(List<CertUploadEntity> data) {
        this.data = data;
    }


}
