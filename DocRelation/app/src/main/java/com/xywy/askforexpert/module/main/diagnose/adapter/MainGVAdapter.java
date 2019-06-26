package com.xywy.askforexpert.module.main.diagnose.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.ScreenUtils;
import com.xywy.askforexpert.widget.SDCardImageLoader;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

/**
 * 主页面中GridView的适配器 stone
 *
 * @author 王鹏
 * @2015-4-24上午10:53:04
 */
public class MainGVAdapter extends BaseAdapter {
    public boolean isAddShow = true;
    // public SparseBooleanArray selectionMap;
    public int maxsize;
    FinalBitmap fb;
    private Context context;
    private List<String> imagePathList = null;
    private SDCardImageLoader loader;

    public MainGVAdapter(Context context, int maxsize) {
        this.context = context;

        loader = new SDCardImageLoader(ScreenUtils.getScreenW(),
                ScreenUtils.getScreenH());
        this.maxsize = maxsize;
        fb = FinalBitmap.create(context, false);
        // init();
    }

    public void setData(List<String> imagePathList) {
        this.imagePathList = imagePathList;
    }

    @Override
    public int getCount() {
        return imagePathList.size() == maxsize ? imagePathList.size()
                : imagePathList.size() + 1;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.photo_grid_item, null);
            holder = new ViewHolder();

            holder.imageView = (ImageView) convertView
                    .findViewById(R.id.idcard_gridView_item_photo);
            holder.image_delete = (ImageView) convertView
                    .findViewById(R.id.item_check);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == imagePathList.size()) {
            if (isAddShow) {

                holder.imageView.setImageBitmap(BitmapFactory.decodeResource(
                        context.getResources(), R.drawable.card_add));
                if (position == maxsize) {
                    holder.imageView.setVisibility(View.GONE);
                }
            } else {
                holder.imageView.setVisibility(View.GONE);
            }

        } else {
            String paths = imagePathList.get(position);
            // holder.imageView.setTag(paths);
            // loader.loadImage(3, paths, holder.imageView);
            // Bitmap bm = loader.compressImageFromFile(paths);
            // holder.imageView.setImageBitmap(bm);
//			paths=paths.replace("100_100_", "");
            fb.display(holder.imageView, paths);

        }

        // if (selectionMap.get(position))
        // {
        // holder.image_delete.setVisibility(View.VISIBLE);
        // } else
        // {
        // holder.image_delete.setVisibility(View.GONE);
        // }
        holder.image_delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                imagePathList.remove(position);
                notifyDataSetChanged();
                init();

            }
        });
        return convertView;
    }

    public void init() {
        // selectionMap = new SparseBooleanArray();
        // for (int i = 0; i < imagePathList_show.size(); i++)
        // {
        // selectionMap.put(i, false);
        // }
    }

    private class ViewHolder {
        ImageView imageView;
        ImageView image_delete;
    }
}
