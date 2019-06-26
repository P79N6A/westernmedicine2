package com.xywy.askforexpert.module.main.guangchang;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.ScreenUtils;

/**
 * <p>
 * 晋升级别图片列表适配器
 * </p>
 *
 * @author liuxuejiao
 * @2015-5-18 下午14:23:30
 */
public class GalleryImageAdapter extends BaseAdapter {
    Context mContext;
    private int selectItem;
    private String type;
    private int drawable1[] = new int[]{R.drawable.senior_psy,
            R.drawable.middle_psy, R.drawable.higher_psy};
    private int drawable2[] = new int[]{R.drawable.senior_yys,
            R.drawable.middle_yys, R.drawable.higher_yys};
    private int drawable3[] = new int[]{R.drawable.gold_medal,
            R.drawable.silver_medal, R.drawable.bronze_medal,
            R.drawable.doctor_medal};

    public GalleryImageAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Integer.MAX_VALUE;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public void setSelectItem(int selectItem) {

        if (this.selectItem != selectItem) {
            this.selectItem = selectItem;
            notifyDataSetChanged();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            holder.iv = new ImageView(mContext);
            convertView = holder.iv;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (type.equals("1")) {
            holder.iv.setImageResource(drawable3[position % drawable3.length]);
        } else if (type.equals("2")) {
            holder.iv.setImageResource(drawable2[position % drawable2.length]);
        } else {
            holder.iv.setImageResource(drawable1[position % drawable1.length]);
        }

        if (selectItem == position) {
            holder.iv.setLayoutParams(new Gallery.LayoutParams(ScreenUtils
                    .getScreenW() * 3 / 5,

                    ScreenUtils.getScreenW() * 3 / 5));
        } else {
            holder.iv.setLayoutParams(new Gallery.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            // 未选中
        }

        holder.iv.setScaleType(ScaleType.CENTER_CROP);
        return convertView;
    }

    class ViewHolder {
        ImageView iv;
    }
}
