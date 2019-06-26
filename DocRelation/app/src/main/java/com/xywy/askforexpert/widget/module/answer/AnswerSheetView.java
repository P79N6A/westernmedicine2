package com.xywy.askforexpert.widget.module.answer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.artifex.mupdfdemo.AsyncTask;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.discovery.answer.adapter.AnswerSheetAdapter;
import com.xywy.askforexpert.widget.view.MyGridView;

import java.util.List;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wangpeng on 16/8/23.
 * describ：
 * revise：
 */
public class AnswerSheetView extends LinearLayout {
    @Bind(R.id.tv_sheet_type)
    TextView tvSheetType;
    @Bind(R.id.gridview_sheet)
    MyGridView gridviewSheet;
    AnswerSheetAdapter adapter;

    public AnswerSheetView(Context context) {
        this(context, null);
    }

    public AnswerSheetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public void initData(final Activity context, final List datalist) {
        adapter = new AnswerSheetAdapter(context, datalist);
        DataTask task = new DataTask();
        task.execute();
    }


    /**
     * 监听接口
     *
     * @param callback
     */
    public void setOnItemClickListenner(SheetOnitemClickCallbackListenner callback) {
        gridviewSheet.setOnItemClickListener(new SheetOnitemClickListenner(callback));
    }

    /**
     * 设置题型
     *
     * @param str
     */
    public void setTheetType(String str) {
        if (!TextUtils.isEmpty(str)) {
            tvSheetType.setText(str);
        }
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.answer_sheet_view, this);
        ButterKnife.bind(this, view);

    }

    private void setData() {

        gridviewSheet.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridviewSheet.setAdapter(adapter);
    }

    /**
     * 回调接口
     */
    public interface SheetOnitemClickCallbackListenner {
        void onItemClickCallBack(AdapterView<?> adapterView, View view, int i, long l, int orderInAll);
    }

    private class DataTask extends AsyncTask<Void, Objects, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setData();
        }
    }

    /**
     * 设置监听
     */
    private class SheetOnitemClickListenner implements AdapterView.OnItemClickListener {
        SheetOnitemClickCallbackListenner callback;

        public SheetOnitemClickListenner(SheetOnitemClickCallbackListenner callback) {
            this.callback = callback;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (adapter != null) {
                callback.onItemClickCallBack(adapterView, view, i, l, adapter.getItem(i).getOrderInAll());
            }
        }
    }


}
