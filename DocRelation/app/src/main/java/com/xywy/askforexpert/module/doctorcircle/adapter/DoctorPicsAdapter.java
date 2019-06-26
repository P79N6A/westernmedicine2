package com.xywy.askforexpert.module.doctorcircle.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;

import java.util.List;

/**
 * 医圈动态评论图片适配器
 *
 * @author LG
 */
public class DoctorPicsAdapter extends BaseAdapter {
    DisplayImageOptions options;
    ImageLoader instance;
    private List<String> minimgs;
    private List<String> imgs;
    private Context context;

    public DoctorPicsAdapter(Context con, List<String> minimgs, List<String> imgs) {
        this.minimgs = minimgs;
        this.context = con;
        this.imgs = imgs;
        instance = ImageLoader.getInstance();// showStubImage(R.drawable.def_image)
        // .showImageForEmptyUri(R.drawable.def_image).showImageOnFail(R.drawable.def_image)
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .showImageOnFail(R.drawable.img_default_bg)
                .showImageOnLoading(R.drawable.img_default_bg)
                .showImageForEmptyUri(R.drawable.img_default_bg)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheOnDisk(true)
                .build();
    }

    @Override
    public int getCount() {
        return minimgs.size();
    }

    public List<String> getPics() {
        return imgs;
    }

    @Override
    public Object getItem(int position) {
        return minimgs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold mb;
        if (convertView == null) {
            mb = new ViewHold();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gradview_imgeviewpics, parent, false);
            mb.iv_item_pic = (ImageView) convertView
                    .findViewById(R.id.iv_gv_pic);
            convertView.setTag(mb);
        } else {
            mb = (ViewHold) convertView.getTag();
        }
        if (minimgs.size() == 1 || minimgs.size() == 4 || minimgs.size() == 2) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    DensityUtils.dp2px(130),
                    DensityUtils.dp2px(130));

            mb.iv_item_pic.setLayoutParams(lp);
        } else {
            int width = AppUtils.getScreenWidth((Activity) context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((width - DensityUtils.dp2px(80)) / 3,
                    (width - DensityUtils.dp2px(80)) / 3);
            mb.iv_item_pic.setLayoutParams(lp);
        }

        mb.iv_item_pic.setTag(minimgs.get(position));
        instance.displayImage(minimgs.get(position), mb.iv_item_pic, options);

        return convertView;
    }

    class ViewHold {
        ImageView iv_item_pic;
    }
}
