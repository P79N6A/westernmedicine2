package com.xywy.askforexpert.module.message.msgchat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * @author 王鹏
 * @2015-6-18下午6:18:04
 */
@SuppressLint("NewApi")
public class AddNumActivity extends Activity {
    private static final String TAG = "AddNumActivity";
    private List<String> list = new ArrayList<String>();
    private List<Integer> position = new ArrayList<Integer>();
    private List<Integer> halfdays = new ArrayList<Integer>();

    private GridView gv_add_num;
    private GridAdapter adapter;
    private SparseBooleanArray selectionMap;
    private String str = "";
    private TextView tv_data;
    String mYear;
    String mMonth;
    String mDay;
    String mWay;
    String time;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_add_num_main);
        list = getIntent().getStringArrayListExtra("list");
        position = getIntent().getIntegerArrayListExtra("position");
        halfdays = getIntent().getIntegerArrayListExtra("halfdays");
        gv_add_num = (GridView) findViewById(R.id.gv_add_num);
        tv_data = (TextView) findViewById(R.id.tv_data);
        init();
        adapter = new GridAdapter();
        gv_add_num.setAdapter(adapter);

        c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        time = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        DLog.i(TAG, "时间。。" + time);
        // int week=t.
        gv_add_num.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
//				boolean ischeck = selectionMap.get(arg2);
                init();
                selectionMap.put(arg2, true);
                adapter.notifyDataSetChanged();

                int shwoweek = position.get(arg2) + 1;
                int nowWeek = Integer.valueOf(mWay);
                if (shwoweek == 8) {
                    shwoweek = 1;
                }
                if (nowWeek == shwoweek) {
                    int times = Integer.valueOf(time);
                    int str_time = 0;
                    if (0 <= times & times < 12) {
                        str_time = 1;
                    } else if (12 <= times & times < 18) {
                        str_time = 2;
                    } else if (18 <= times & times < 24) {
                        str_time = 3;
                    }
                    if (str_time - halfdays.get(arg2) > 0) {
                        Calendar calendar = Calendar.getInstance(); // 得到日历
                        calendar.add(Calendar.DAY_OF_YEAR,
                                7 - (nowWeek - shwoweek)); // 设置为前一天
                        String mYear = String.valueOf(calendar
                                .get(Calendar.YEAR)); // 获取当前年份
                        String mMonth = String.valueOf(calendar
                                .get(Calendar.MONTH) + 1);// 获取当前月份
                        String mDay = String.valueOf(calendar
                                .get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
                        String mWay = String.valueOf(calendar
                                .get(Calendar.DAY_OF_WEEK));
                        str = mMonth + "月" + mDay + "日" + list.get(arg2);
                    } else {
                        str = mMonth + "月" + mDay + "日" + list.get(arg2);
                    }
                } else if (nowWeek - shwoweek < 0) {
                    Calendar calendar = Calendar.getInstance(); // 得到日历
                    calendar.add(Calendar.DAY_OF_YEAR, shwoweek - nowWeek); // 设置为前一天
                    String mYear = String.valueOf(calendar.get(Calendar.YEAR)); // 获取当前年份
                    String mMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);// 获取当前月份
                    String mDay = String.valueOf(calendar
                            .get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
                    String mWay = String.valueOf(calendar
                            .get(Calendar.DAY_OF_WEEK));
                    str = mMonth + "月" + mDay + "日" + list.get(arg2);
                } else if (nowWeek - shwoweek > 0) {
                    Calendar calendar = Calendar.getInstance(); // 得到日历
                    calendar.add(Calendar.DAY_OF_YEAR, 7 - (nowWeek - shwoweek)); // 设置为前一天
                    String mYear = String.valueOf(calendar.get(Calendar.YEAR)); // 获取当前年份
                    String mMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);// 获取当前月份
                    String mDay = String.valueOf(calendar
                            .get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
                    String mWay = String.valueOf(calendar
                            .get(Calendar.DAY_OF_WEEK));
                    str = mMonth + "月" + mDay + "日" + list.get(arg2);
                }
                tv_data.setText(str);
            }
        });
    }

//	public void s()
//	{
//		for (int i = 0; i < list.size(); i++)
//		{
//
//		}
//	}

    class GridAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;

        public GridAdapter() {
            layoutInflater = LayoutInflater.from(AddNumActivity.this);
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
                        R.layout.chat_add_num_item, null);
                viewHolder.tv_text = (TextView) convertView
                        .findViewById(R.id.tv_text);
                viewHolder.re_background_image = (LinearLayout) convertView
                        .findViewById(R.id.lin_select);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            boolean ischeck = selectionMap.get(position);
            if (ischeck) {
                viewHolder.re_background_image
                        .setBackgroundResource(R.drawable.yuanjiao_huis_3);
            } else {
                viewHolder.re_background_image
                        .setBackgroundResource(R.drawable.yuanjiao_huis_2);

            }
            // convertView.setLayoutParams(param);
            viewHolder.tv_text.setText(list.get(position));
            return convertView;
        }

    }

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.btn2:
                if (!"".equals(str)) {
//				一叶草，您好！已经在2015年3月26日上午为您加号。请准时前往门诊，直接找到我并出示本条信息作为加号凭证
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("type", tv_data.getText().toString().trim() + "为您加号，请准时前往门诊，直接找到我并出示本条信息作为加号凭证");
                    intent.putExtras(bundle);
                    AddNumActivity.this.setResult(RESULT_OK, intent);
                    finish();
                } else {
                    ToastUtils.shortToast("请为患者选择加号时间");
                }
                break;

            default:
                break;
        }
    }

    public static int StringData() {
        String mYear;
        String mMonth;
        String mDay;
        String mWay;
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        // if ("1".equals(mWay))
        // {
        // mWay = "日";
        // } else if ("2".equals(mWay))
        // {
        // mWay = "一";
        // } else if ("3".equals(mWay))
        // {
        // mWay = "二";
        // } else if ("4".equals(mWay))
        // {
        // mWay = "三";
        // } else if ("5".equals(mWay))
        // {
        // mWay = "四";
        // } else if ("6".equals(mWay))
        // {
        // mWay = "五";
        // } else if ("7".equals(mWay))
        // {
        // mWay = "六";
        // }
        return Integer.valueOf(mWay);
    }

    public void init() {
        selectionMap = new SparseBooleanArray();
        for (int i = 0; i < list.size(); i++) {
            selectionMap.put(i, false);
        }
    }

    static class ViewHolder {
        LinearLayout re_background_image;
        ImageView image_bg;
        TextView tv_text;
        TextView tv_num;
    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
    }
}
