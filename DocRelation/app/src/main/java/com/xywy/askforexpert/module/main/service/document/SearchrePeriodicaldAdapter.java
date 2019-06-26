package com.xywy.askforexpert.module.main.service.document;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;

/**
 * 高级搜索全部期刊适配器是
 *
 * @author apple
 */
public class SearchrePeriodicaldAdapter extends BaseAdapter {

    String[] str;
    Context context;
    String curentStr;

    public SearchrePeriodicaldAdapter(String strCu, String[] str11, Context context) {
        this.str = str11;
        this.curentStr = strCu;
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
            arg1 = View.inflate(context, R.layout.pop_periodcal_item, null);
            hoder.tv = (TextView) arg1.findViewById(R.id.tv_periodcal);
            hoder.iv_wx_select = (ImageView) arg1.findViewById(R.id.iv_wx_select);
            arg1.setTag(hoder);
        } else {
            hoder = (ViewHoder) arg1.getTag();
        }

        String string = str[arg0];
        if (!TextUtils.isEmpty(curentStr)) {
            if (curentStr.trim().equals(string.trim())) {
                hoder.tv.setSelected(true);
                hoder.iv_wx_select.setSelected(true);
            } else {
                hoder.iv_wx_select.setSelected(false);

                hoder.tv.setSelected(false);
            }

        }

        hoder.tv.setText(string);
        return arg1;
    }

    static class ViewHoder {
        TextView tv;
        ImageView iv_wx_select;
    }
}
