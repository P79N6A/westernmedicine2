package com.xywy.askforexpert.module.main.service.document;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xywy.askforexpert.R;

/**
 * 文献所有字段适配器
 *
 * @author apple
 */
public class Allfieldadapter extends BaseAdapter {

    String[] str;
    Context context;
    String curenttv_time;

    public Allfieldadapter(String curenttv_time, String[] str, Context context) {
        this.str = str;
        this.curenttv_time = curenttv_time;
        this.context = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return str.length;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        ViewHoder hoder = null;
        if (arg1 == null) {
            hoder = new ViewHoder();
            arg1 = View.inflate(context,
                    R.layout.pup_item_seach_all_filed, null);
            hoder.tv = (TextView) arg1.findViewById(R.id.tv_all_filed);
            arg1.setTag(hoder);
        } else {
            hoder = (ViewHoder) arg1.getTag();
        }
        String string = str[arg0];
        if (!TextUtils.isEmpty(curenttv_time)) {
            if (curenttv_time.equals(string)) {
                hoder.tv.setSelected(true);
            } else {

                hoder.tv.setSelected(false);
            }

        }

        hoder.tv.setText(str[arg0]);
        return arg1;
    }

    static class ViewHoder {
        TextView tv;
    }
}
