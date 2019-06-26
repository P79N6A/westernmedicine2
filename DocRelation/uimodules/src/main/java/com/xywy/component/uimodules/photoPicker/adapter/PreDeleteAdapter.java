package com.xywy.component.uimodules.photoPicker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xywy.component.R;
import com.xywy.component.uimodules.PPImageLoader;
import com.xywy.component.uimodules.photoPicker.model.PhotoInfo;
import com.xywy.component.uimodules.photoPicker.view.PPZoomImageView;

import java.util.List;

/**
 * Created by DongJr on 2016/12/22.
 */

public class PreDeleteAdapter extends RecyclingPagerAdapter {

    private Context mContext;
    private List<PhotoInfo> mList;

    private LayoutInflater mInflater;

    public PreDeleteAdapter(Context context, List<PhotoInfo> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(List<PhotoInfo> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.pp_adapter_preview_viewpgaer_item, null);
            holder = new ViewHolder();
            holder.imageView = (PPZoomImageView) convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PhotoInfo photoInfo = mList.get(position);
        String path = "";
        if (photoInfo != null) {
            path = "file:///" + photoInfo.getPhotoPath();
        }

        PPImageLoader.getInstance().displayAlbumImage(path, holder.imageView);
        return convertView;
    }

    @Override
    public int getCount() {
        return mList.size();
    }


    static class ViewHolder {
        PPZoomImageView imageView;
    }
}
