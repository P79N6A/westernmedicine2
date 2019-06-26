package com.xywy.askforexpert.module.my.clinic;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.widget.view.TimeNumberPickerPopup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 服务时间设置
 *
 * @author 王鹏
 * @2015-5-20下午5:57:23
 */
public class PhoneServerTimeActivity extends Activity {
    GridView gridView;
    List<String> list_choolse = new ArrayList<String>();
    List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    LinearLayout week;
    public SparseBooleanArray selectionMap;
    private LinearLayout main;
    private GridAdapter adapter;
    private TimeNumberPickerPopup timepick;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub'
        super.onCreate(savedInstanceState);
        PhoneServerTimeActivity.this
                .requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.phone_tieme);

        main = (LinearLayout) findViewById(R.id.main);
        gridView = (GridView) findViewById(R.id.gv_time_set);

        if (YMApplication.phosetinfo.getChoose_list() != null
                & YMApplication.phosetinfo.getChoose_list().size() > 0) {
            list_choolse = YMApplication.phosetinfo.getChoose_list();

            list = YMApplication.phosetinfo.getMap();
        } else {
            for (int i = 0; i < 28; i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("date", "");
                map.put("num", "");
                list_choolse.add("");
                list.add(map);
            }
        }

        init();
        adapter = new GridAdapter();
        gridView.setAdapter(adapter);
        week = (LinearLayout) findViewById(R.id.lin_week);
        ((TextView) findViewById(R.id.tv_title)).setText("服务时间开通");
        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                int min = 0;
                int max = 0;
                int y = arg2 % 4;
                if (y == 0) {
                    min = 8;
                    max = 12;
                } else if (y == 1) {
                    min = 12;
                    max = 14;
                } else if (y == 2) {
                    min = 14;
                    max = 18;
                } else if (y == 3) {
                    min = 18;
                    max = 23;
                }
                timepick = new TimeNumberPickerPopup(
                        PhoneServerTimeActivity.this, saveOnclik, min, max,
                        (arg2 / 4) + 1);
                position = arg2;
                timepick.getIsNumShow(true);
                timepick.showAtLocation(main, Gravity.BOTTOM
                        | Gravity.CENTER_HORIZONTAL, 0, 0);

            }
        });
    }

    private String str_time() {
        StringBuffer sb = new StringBuffer();
        String str = "";
        for (int i = 0; i < list_choolse.size(); i++) {
            if (!"".equals(list_choolse.get(i))) {
                sb.append(list_choolse.get(i) + ",");
            }
        }
        if (sb.length() > 0) {
            str = sb.substring(0, sb.lastIndexOf(","));
        }

        return str;
    }

    private OnClickListener saveOnclik = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_save:
                    HashMap<String, String> map = new HashMap<String, String>();
                    String date = timepick.getData();
                    String num_str = timepick.getEidtStr();
                    String str_phone = timepick.getStrPhone();
                    if (num_str != null && !"".equals(num_str)) {
                        if (!TextUtils.isEmpty(date)) {
                            map.put("date", date);
                            map.put("num", num_str);
                            list.remove(position);
                            list.add(position, map);
                            list_choolse.remove(position);
                            list_choolse.add(position, str_phone);
                        }

                    } else {
                        ToastUtils.shortToast("名额不能为空");
                    }
                    adapter.notifyDataSetChanged();
                    timepick.dismiss();
                    break;
                default:
                    break;
            }

        }

    };

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.btn2:

                if (!TextUtils.isEmpty(str_time())) {
                    YMApplication.phosetinfo.setChoose_list(list_choolse);
                    YMApplication.phosetinfo.setMap(list);
                    YMApplication.phosetinfo.setPhonetime(str_time());
                    finish();
                } else {
                    ToastUtils.shortToast("请编辑服务时间");
                }
                break;
            default:
                break;
        }
    }

    public void init() {
        selectionMap = new SparseBooleanArray();
        for (int i = 0; i < list.size(); i++) {
            selectionMap.put(i, false);
        }

    }

    class GridAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;

        public GridAdapter() {
            layoutInflater = LayoutInflater.from(PhoneServerTimeActivity.this);
        }

        @Override
        public int getCount() {
            return list.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = layoutInflater.inflate(
                        R.layout.phone_time_child_item, null);
                viewHolder.tv_text = (TextView) convertView
                        .findViewById(R.id.tv_text);
                viewHolder.tv_num = (TextView) convertView
                        .findViewById(R.id.tv_num);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            AbsListView.LayoutParams param = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    gridView.getHeight() / 7);
            convertView.setLayoutParams(param);
            viewHolder.tv_text.setText(list.get(position).get("date"));
            viewHolder.tv_num.setText(list.get(position).get("num"));
            return convertView;
        }

    }

    static class ViewHolder {
        RelativeLayout re_background_image;
        ImageView image_bg;
        TextView tv_text;
        TextView tv_num;
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatisticalTools.onResume(PhoneServerTimeActivity.this);

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        StatisticalTools.onPause(PhoneServerTimeActivity.this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
