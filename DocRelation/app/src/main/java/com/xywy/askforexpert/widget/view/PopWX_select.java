package com.xywy.askforexpert.widget.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.main.service.document.SearchrePeriodicaldAdapter;

/**
 * 文献全部论文排序pop
 *
 * @author apple
 */
public class PopWX_select extends PopupWindow {
    ListView mlistView = null;


    public PopWX_select(String str, Context context, String[] allStr, OnItemClickListener ontiem, OnClickListener onc) {


        View inflate = View.inflate(context, R.layout.pop_docment_lv, null);
        setContentView(inflate);

        mlistView = (ListView) inflate.findViewById(R.id.lv);
        mlistView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mlistView.setAdapter(new SearchrePeriodicaldAdapter(str, allStr, context));
        mlistView.setDividerHeight(0);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        setHeight(LayoutParams.MATCH_PARENT);
        setWidth(LayoutParams.MATCH_PARENT);

        mlistView.setOnItemClickListener(ontiem);
        inflate.findViewById(R.id.view).setOnClickListener(onc);
    }

}
