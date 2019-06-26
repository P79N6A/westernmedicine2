package com.xywy.askforexpert.module.my.clinic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.widget.view.TimeNumberPickerPopup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 接电话时间 stone
 *
 * @author 王鹏
 * @2015-5-26下午7:42:11
 */
public class FamServerTimeActivity extends Activity {
    private TextView btn22;
    private GridView gridView;
    private GridView gv_day;
    private List<String> list = new ArrayList<String>();//存0-20的索引
    private List<String> list1 = new ArrayList<String>();//列表中每一项目中的数据 时间段集合
    private List<List<String>> choose_list = new ArrayList<List<String>>();//21个 一周七天 一天分上午中午下午

    private List<String> day = new ArrayList<String>();//周几的集合
    public SparseBooleanArray selectionMap;

    private TimeNumberPickerPopup timepick;
    private GridAdapter adapter;
    private GridAdapter2 adapter2;
    private LinearLayout main;
    private int position;

    private boolean fromApplyPage;//来自申请页面 stone

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FamServerTimeActivity.this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        CommonUtils.initSystemBar(this);

        fromApplyPage = getIntent().getBooleanExtra(Constants.KEY_VALUE, false);

        setContentView(R.layout.fam_tieme);
        btn22 = (TextView) findViewById(R.id.btn22);
        btn22.setVisibility(View.VISIBLE);
        btn22.setText(getString(R.string.save));

        gridView = (GridView) findViewById(R.id.gv_time_set);
        gv_day = (GridView) findViewById(R.id.gv_day);
        gridView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return MotionEvent.ACTION_MOVE == event.getAction() ? true
                        : false;
            }
        });

        gv_day.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return MotionEvent.ACTION_MOVE == event.getAction() ? true
                        : false;
            }
        });

        main = (LinearLayout) findViewById(R.id.main);
        if (YMApplication.famdocinfo.getDate_list() != null
                & YMApplication.famdocinfo.getDate_list().size() > 0) {

//            list = YMApplication.famdocinfo.getDate_list();
//            choose_list = YMApplication.famdocinfo.getChoolse_list();
//            day = YMApplication.famdocinfo.getDay();

            int len = YMApplication.famdocinfo.getDate_list().size();
            for (int i = 0; i < len; i++) {
                list.add(YMApplication.famdocinfo.getDate_list().get(i));
            }

            int len1 = YMApplication.famdocinfo.getChoolse_list().size();
            for (int i = 0; i < len1; i++) {
                choose_list.add(YMApplication.famdocinfo.getChoolse_list().get(i));
            }

            int len2 = YMApplication.famdocinfo.getDay().size();
            for (int i = 0; i < len2; i++) {
                day.add(YMApplication.famdocinfo.getDay().get(i));
            }

        } else {
            for (int i = 0; i < 21; i++) {
                list.add("");
                choose_list.add(list1);
            }
        }

        init();
        adapter = new GridAdapter();
        adapter2 = new GridAdapter2();
        gridView.setAdapter(adapter);
        gv_day.setAdapter(adapter2);
        ((TextView) findViewById(R.id.tv_title)).setText("服务时间");
        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                int numcode = (arg2 / 3) + 1;
                int min = 0;
                int max = 0;
                int item = (arg2 + 1) * 10 % 3;
                if (item == 1) {
                    min = 8;
                    max = 12;
                } else if (item == 2) {
                    min = 12;
                    max = 18;
                } else if (item == 0) {
                    min = 18;
                    max = 22;
                }
                timepick = new TimeNumberPickerPopup(
                        FamServerTimeActivity.this, saveOnclik, min, max,
                        numcode);
                position = arg2;
                timepick.showAtLocation(main, Gravity.BOTTOM
                        | Gravity.CENTER_HORIZONTAL, 0, 0);

                //添加遮罩 stone
                YMOtherUtils.addScreenBg(timepick, FamServerTimeActivity.this);
            }
        });
    }

    private OnClickListener saveOnclik = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_save:
                    list.remove(position);
                    list.add(position, timepick.getData());
                    choose_list.remove(position);
                    choose_list.add(position, timepick.getList());
                    int days = (position / 3) + 1;//展示 1 2 3 .. 相当于周一 周二 ..

                    if (!TextUtils.isEmpty(timepick.getData())) {
                        if (!day.contains(days + "")) {
                            day.add(days + "");
                        }
                    } else {
                        //选择了一个错误的时间 相当于取消这个选择
                        if (day != null && day.size() > 0 && day.contains(days + "")
                                && TextUtils.isEmpty(list.get(3 * (days - 1)))
                                && TextUtils.isEmpty(list.get(3 * (days - 1) + 1))
                                && TextUtils.isEmpty(list.get(3 * (days - 1) + 2))) {
                            day.remove(days + "");
                        }
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
            case R.id.btn22:
//			System.out.println(choose_list);
                if (list() != null & list().size() > 0 & day.size() >= 4) {
                    //升序排列
                    Collections.sort(day);

                    YMApplication.famdocinfo.setTime(str());
                    YMApplication.famdocinfo.setDate_list(list);
                    YMApplication.famdocinfo.setChoolse_list(choose_list);
                    YMApplication.famdocinfo.setDay(day);
                    if (fromApplyPage) {
                        setResult(RESULT_OK, new Intent());
                        finish();
                    } else {
                        finish();
                    }
                } else {
                    DialogUtil.showDialog(this, null, getString(R.string.familydoctor_notice3), "知道了", null, null);
                }
                break;
            default:
                break;
        }
    }

    public String str() {
        String str = "";
        List<String> list = list();
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

    public List<String> list() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < choose_list.size(); i++) {
            if (choose_list.get(i).size() > 0) {
                for (int j = 0; j < choose_list.get(i).size(); j++) {
                    list.add(choose_list.get(i).get(j));
                }
            }
        }
        return list;
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
            layoutInflater = LayoutInflater.from(FamServerTimeActivity.this);
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
                        R.layout.fam_time_child_item, null);
                viewHolder.image_bg = (ImageView) convertView
                        .findViewById(R.id.img_ok);
                viewHolder.tv_text = (TextView) convertView
                        .findViewById(R.id.tv_text);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            AbsListView.LayoutParams param = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ((gridView.getHeight()) / 7));
            convertView.setLayoutParams(param);
            viewHolder.tv_text.setText(list.get(position));

            return convertView;
        }

    }

    class GridAdapter2 extends BaseAdapter {

        private LayoutInflater layoutInflater;
        String[] days = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};

        public GridAdapter2() {
            layoutInflater = LayoutInflater.from(FamServerTimeActivity.this);
        }

        @Override
        public int getCount() {
            return days.length;
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
                        R.layout.fam_time_day_item, null);
                viewHolder.tv_text = (TextView) convertView
                        .findViewById(R.id.tv_text);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            AbsListView.LayoutParams param = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ((gv_day.getHeight()) / 7));
            convertView.setLayoutParams(param);
            viewHolder.tv_text.setText(days[position]);

            return convertView;
        }

    }

    static class ViewHolder {
        RelativeLayout re_background_image;
        ImageView image_bg;
        TextView tv_text;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatisticalTools.onResume(FamServerTimeActivity.this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub

        StatisticalTools.onPause(FamServerTimeActivity.this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
    }
}
