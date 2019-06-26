package com.xywy.askforexpert.module.my.clinic;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 预约转诊时间设置 stone
 *
 * @author 王鹏
 * @2015-5-20上午8:49:02
 */
public class NumberTimeSetActivity extends Activity {
    GridView gridView;
    List<String> list = new ArrayList<String>();
    LinearLayout week;
    public SparseBooleanArray selectionMap;
    private String isType;
    private GridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);


        setContentView(R.layout.number_time_set);
//		isType = YMApplication.getLoginInfo().getData().getXiaozhan().getYuyue();
        isType = getIntent().getStringExtra("istype");
        gridView = (GridView) findViewById(R.id.gv_time_set);

        if (isType.equals(Constants.FUWU_AUDIT_STATUS_1)) {
            list = YMApplication.addnuminfo.getList();
            findViewById(R.id.btn2).setVisibility(View.GONE);
        } else {
            if (YMApplication.addnuminfo.getSelectionMap() != null
                    & YMApplication.addnuminfo.getSelectionMap().size() > 0) {
                selectionMap = YMApplication.addnuminfo.getSelectionMap();
            }

            init();
            findViewById(R.id.btn2).setVisibility(View.VISIBLE);
        }

        adapter = new GridAdapter();
        gridView.setAdapter(adapter);

        week = (LinearLayout) findViewById(R.id.lin_week);
        ((TextView) findViewById(R.id.tv_title)).setText("预约门诊时间");
        if (isType.equals(Constants.FUWU_AUDIT_STATUS_1)) {

        } else {
            gridView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    boolean ischeck = selectionMap.get(arg2);
                    selectionMap.put(arg2, !ischeck);
                    adapter.notifyDataSetChanged();

                }
            });

        }
    }

    public List<String> getList_Choolse() {
        List<String> list2 = new ArrayList<String>();
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            if (selectionMap.get(i)) {
                int item = (i) * 10 % 3 + 1;
                int week = getNumColumms(i);
                str = week + "_" + item;
                list2.add(str);
            }
        }
        return list2;
    }

    /**
     * 拼接字符串
     *
     * @param arg2
     * @return
     */

    public String str() {
        String str = "";
        List<String> list = getList_Choolse();
        StringBuffer sb = new StringBuffer();

        for (int j = 0; j < list.size(); j++) {
            sb.append(list.get(j) + ",");
        }
        // StringUtils.join(stu_list, "");
        if (sb.length() > 0) {
            str = sb.substring(0, sb.lastIndexOf(","));
        }


        return str;
    }

    public int getNumColumms(int arg2) {
        int numcolu = 1;
        if (arg2 >= 0 && arg2 <= 2) {
            numcolu = 1;
        } else if (arg2 >= 3 && arg2 <= 5) {
            numcolu = 2;
        } else if (arg2 >= 6 && arg2 <= 8) {
            numcolu = 3;
        } else if (arg2 >= 9 && arg2 <= 11) {
            numcolu = 4;
        } else if (arg2 >= 12 && arg2 <= 14) {
            numcolu = 5;
        } else if (arg2 >= 15 && arg2 <= 17) {
            numcolu = 6;
        } else if (arg2 >= 18 && arg2 <= 20) {
            numcolu = 7;
        }
        return numcolu;
    }

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.btn2:
                if (!TextUtils.isEmpty(str()) & str() != null && !str().equals("")) {
                    YMApplication.addnuminfo.setDetail(str());
                    YMApplication.addnuminfo.setSelectionMap(selectionMap);
                    finish();
                } else {
                    ToastUtils.shortToast("请编辑预约门诊时间");
                }
                break;
            default:
                break;
        }
    }

    public void init() {
        for (int i = 0; i < 21; i++) {
            list.add("");
        }
        selectionMap = new SparseBooleanArray();
        for (int i = 0; i < list.size(); i++) {
            selectionMap.put(i, false);
        }

    }

    class GridAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;

        public GridAdapter() {
            layoutInflater = LayoutInflater.from(NumberTimeSetActivity.this);
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
                        R.layout.number_add_time_set_item, null);
                viewHolder.image_bg = (ImageView) convertView
                        .findViewById(R.id.img_ok);
                viewHolder.lin_item = (LinearLayout) convertView
                        .findViewById(R.id.lin_item);
                viewHolder.lin_item_1 = (LinearLayout) convertView
                        .findViewById(R.id.lin_item_1);
                viewHolder.tv_content = (TextView) convertView
                        .findViewById(R.id.tv_content);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (isType.equals(Constants.FUWU_AUDIT_STATUS_1)) {
                viewHolder.lin_item.setVisibility(View.GONE);
                viewHolder.lin_item_1.setVisibility(View.VISIBLE);
                viewHolder.tv_content.setText(list.get(position));
            } else {
                viewHolder.lin_item_1.setVisibility(View.GONE);
                viewHolder.lin_item.setVisibility(View.VISIBLE);
                boolean ischek = selectionMap.get(position);
                if (ischek) {
                    viewHolder.image_bg.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.image_bg.setVisibility(View.GONE);
                }

            }
            AbsListView.LayoutParams param = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    gridView.getHeight() / 7);
            convertView.setLayoutParams(param);

            return convertView;
        }

    }

    static class ViewHolder {
        LinearLayout lin_item;
        LinearLayout lin_item_1;
        ImageView image_bg;
        TextView tv_content;
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatisticalTools.onResume(NumberTimeSetActivity.this);

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub

        StatisticalTools.onPause(NumberTimeSetActivity.this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
