package com.xywy.askforexpert.module.my.photo;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.ScreenUtils;
import com.xywy.askforexpert.widget.SDCardImageLoader;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

/**
 * PhotoWall中GridView的适配器
 */

public class PhotoWallAdapter extends BaseAdapter {
    // 记录是否被选择
    public static SparseBooleanArray selectionMap;
    private Context context;
    private List<String> imagePathList = null;
    private SDCardImageLoader loader;
    private FinalBitmap fb;

    public PhotoWallAdapter(Context context, List<String> imagePathList) {
        this.context = context;
        this.imagePathList = imagePathList;
        ScreenUtils.initScreen((Activity) context);
        loader = new SDCardImageLoader(ScreenUtils.getScreenW(),
                ScreenUtils.getScreenH());
//		if(imagePathList_show!=null)
//		{
//			init();
//		}
        fb = FinalBitmap.create(context, false);

    }

    @Override
    public int getCount() {
        return imagePathList == null ? 0 : imagePathList.size();
    }

    @Override
    public Object getItem(int position) {
        return imagePathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String filePath = (String) getItem(position);

        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.photo_wall_item, null);
            holder = new ViewHolder();
            holder.selectImg = (ImageView) convertView
                    .findViewById(R.id.isselected);
            holder.imageView = (ImageView) convertView
                    .findViewById(R.id.photo_wall_item_photo);
            // holder.checkBox = (CheckBox)
            // convertView.findViewById(R.id.photo_wall_item_cb);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // tag的key必须使用id的方式定义以保证唯一，否则会出现IllegalArgumentException.
        // holder.checkBox.setTag(R.id.tag_first, position);
        // holder.checkBox.setTag(R.id.tag_second, holder.imageView);
        // holder.checkBox.setOnCheckedChangeListener(new
        // CompoundButton.OnCheckedChangeListener() {
        // @Override
        // public void onCheckedChanged(CompoundButton buttonView, boolean
        // isChecked) {
        // Integer position = (Integer) buttonView.getTag(R.id.tag_first);
        // ImageView image = (ImageView) buttonView.getTag(R.id.tag_second);
        //
        // selectionMap.put(position, isChecked);
        // if (isChecked) {
        // image.setColorFilter(context.getResources().getColor(R.color.image_checked_bg));
        // } else {
        // image.setColorFilter(null);
        // }
        // }
        // });

//		holder.checkBox.setChecked(selectionMap.get(position));

        if (selectionMap.get(position)) {
            holder.selectImg.setBackgroundResource(R.drawable.checkbox_checked);
        } else {
            holder.selectImg.setBackgroundResource(R.drawable.checkbox_normal);
        }

        holder.imageView.setTag(filePath);
        loader.loadImage(4, filePath, holder.imageView);
//		fb.display(holder.imageView, filePath);
        return convertView;
    }

    public SparseBooleanArray getSelectionMap() {
        return selectionMap;
    }

    public void clearSelectionMap() {
        selectionMap.clear();
    }

    public void init() {
        selectionMap = new SparseBooleanArray();
        for (int i = 0; i < imagePathList.size(); i++) {
            selectionMap.put(i, false);
        }
    }

    private class ViewHolder {
        ImageView imageView;
        CheckBox checkBox;
        ImageView selectImg;
    }
}
