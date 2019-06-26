package com.xywy.askforexpert.module.message.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xywy.askforexpert.R;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

public class SendMesagesGridviewAdapter extends BaseAdapter {
    public static List<String> imagePahtList_idcard = null;
    public static boolean isAddShow = true;
    final int maxsize = 9;
    Context context;
    private ImageLoader instance;
    private DisplayImageOptions options;
    private FinalBitmap create;

    public SendMesagesGridviewAdapter(List<String> imagePahtList_idcard,
                                      Context context) {
        this.context = context;
        create = FinalBitmap.create(context, false);
        SendMesagesGridviewAdapter.imagePahtList_idcard = imagePahtList_idcard;
    }

    public SendMesagesGridviewAdapter(Context context) {
        this.context = context;
    }

    public void addData(List<String> imagePahtList_idcard) {
        SendMesagesGridviewAdapter.imagePahtList_idcard.addAll(0, imagePahtList_idcard);
    }

    @Override
    public int getCount() {
        return imagePahtList_idcard.size() == maxsize ? imagePahtList_idcard.size() : imagePahtList_idcard.size() + 1;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder mm;
        if (convertView == null) {
            mm = new ViewHolder();
            convertView = View.inflate(context, R.layout.doctor_photo_grid_item, null);
            mm.iv_pic = (ImageView) convertView.findViewById(R.id.idcard_gridView_item_photo);
            convertView.setTag(mm);

        } else {
            mm = (ViewHolder) convertView.getTag();
        }

        if (position == imagePahtList_idcard.size()) {
            if (isAddShow) {

                mm.iv_pic.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.card_add));

                if (position == maxsize) {
                    mm.iv_pic.setVisibility(View.GONE);
                }
            } else {
                mm.iv_pic.setVisibility(View.GONE);
            }

        } else {
            String paths = imagePahtList_idcard.get(position);

            create.display(mm.iv_pic, paths);

        }
        return convertView;
    }

    class ViewHolder {
        ImageView iv_delete, iv_pic;
    }

}
