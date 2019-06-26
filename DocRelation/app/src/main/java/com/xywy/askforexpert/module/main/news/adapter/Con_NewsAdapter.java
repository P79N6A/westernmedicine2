package com.xywy.askforexpert.module.main.news.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.ConsultingInfo;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

/**
 * 医学资讯 adpter
 *
 * @author 王鹏
 * @2015-5-10下午1:05:57
 */
@SuppressLint("ResourceAsColor")
public class Con_NewsAdapter extends BaseAdapter {

    public SparseBooleanArray selectionMap;
    public FinalBitmap fb;
    public boolean isScrollering;
    ImageLoader instance;
    DisplayImageOptions options;
    private List<ConsultingInfo> list;
    private Context context;
    private LayoutInflater inflater;

    public Con_NewsAdapter(Context context) {
        this.context = context;

        inflater = LayoutInflater.from(context);
        fb = FinalBitmap.create(context, false);
        options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .showStubImage(R.drawable.img_default_bg)
                .showImageForEmptyUri(R.drawable.img_default_bg)
                .showImageOnFail(R.drawable.img_default_bg).cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        instance = ImageLoader.getInstance();
    }

    public void setData(List<ConsultingInfo> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.con_news_item, null);
            holder.tv_title = (TextView) convertView
                    .findViewById(R.id.tv_news_title);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_watch = (TextView) convertView
                    .findViewById(R.id.tv_watch);
            holder.head_img = (ImageView) convertView
                    .findViewById(R.id.img_head);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (list != null) {
            ConsultingInfo info = list.get(position);
            holder.tv_title.setText(info.getTitle());
//			fb.display(holder.head_img, list.get(position).getImage());	
//			if(!isScrollering)
//			{
            instance.displayImage(info.getImage(), holder.head_img, options);
//			}

            holder.tv_date.setText(info.getCreatetime());
            holder.tv_watch.setText(info.getPraiseNum());
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tv_title;
        RelativeLayout re_item;
        ImageView head_img;
        TextView tv_date;
        TextView tv_watch;
    }

}
