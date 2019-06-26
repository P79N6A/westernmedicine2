package com.xywy.askforexpert.module.my.clinic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.CateInfo;
import com.xywy.askforexpert.widget.view.NumberPickerPopupwinow2;
import com.xywy.base.view.ProgressDialog;
import com.xywy.uilibrary.dialog.pndialog.XywyPNDialog;
import com.xywy.uilibrary.dialog.pndialog.listener.PositiveDialogListener;
import com.xywy.util.KeyBoardUtils;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 医生开通折扣设置
 *
 * @author 王鹏
 * @2015-5-27上午8:53:24
 */
public class FamCategroyActivity extends Activity {
    private static final String TAG = "FamCategroyActivity";
    private List<String> list = new ArrayList<String>();
    private ListView listView;
    private EditText edit_free_num;
    private BaseCategroydapter adapter;
    private LinearLayout main;

    private NumberPickerPopupwinow2 start_time;
    private NumberPickerPopupwinow2 end_time;
    private TextView tv_start_time;
    private TextView tv_end_time;

    private String category;
    private boolean type;
    private Map<String, String> map = new HashMap<String, String>();

    CateInfo cateinfo;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 100:
                    if (map.get("code").equals("0")) {
                        finish();
                    } else {
                        ToastUtils.shortToast(map.get("msg"));
                    }
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FamCategroyActivity.this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        CommonUtils.initSystemBar(this);
        setContentView(R.layout.fam_category_list);
        // re_number = (RelativeLayout) findViewById(R.id.re_number);
        listView = (ListView) findViewById(R.id.list);
        main = (LinearLayout) findViewById(R.id.main);
        type = getIntent().getBooleanExtra("type", false);
        // re_stat_time = (RelativeLayout) findViewById(R.id.re_stat_time);
        // re_end_time = (RelativeLayout) findViewById(R.id.re_end_time);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        tv_end_time = (TextView) findViewById(R.id.tv_end_time);
        edit_free_num = (EditText) findViewById(R.id.edit_free_num);
        ((TextView) findViewById(R.id.tv_title)).setText("折扣设置");

        for (int i = 5; i < 10; i++) {
            if (i == 0) {
                list.add("免费");
            } else {
                list.add(i + "");
            }

        }
        adapter = new BaseCategroydapter(FamCategroyActivity.this, list);
        adapter.init();
        listView.setAdapter(adapter);
        if (type) {
            getData();
            findViewById(R.id.btn2).setVisibility(View.GONE);

        } else {

            listView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    adapter.init();
                    adapter.selectionMap.put(arg2, true);
                    adapter.notifyDataSetChanged();
                    category = "4";
//                    if (arg2 == 0) {
//                        category = "3";
//                    } else {
//                        category = "4";
//                    }

                }
            });
        }
        if (!isAbleToSetDiscount()) {
            //价格过低无法设置折扣
            new XywyPNDialog.Builder()
                    .setNoNegativeBtn(true)
                    .setNoTitle(true)
                    .setContent("您现在价格过低无法做折扣活动\n可折扣价格范围周>=20元且月>=60元")
                    .setCancelable(false)
                    .create(this, new PositiveDialogListener() {
                        @Override
                        public void onPositive() {
                            finish();
                        }
                    }).show();
            return;
        }
    }

    public void initview() {
        if (cateinfo != null) {
            tv_start_time.setText(cateinfo.getData().getTimebegin());
            tv_end_time.setText(cateinfo.getData().getTimeend());
            edit_free_num.setText(cateinfo.getData().getMaxnum());
            edit_free_num.setFocusable(false);


            for (int i = 0; i < list.size(); i++) {
                if (cateinfo.getData().getPrice().contains((i + 5) + "")) {
                    adapter.selectionMap.put(i, true);
                    break;
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void getData() {
        final ProgressDialog dialog = new ProgressDialog(this, "正在加载中...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.showProgersssDialog();
        String userid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(userid + Constants.MD5_KEY);

        AjaxParams params = new AjaxParams();
        params.put("uid", userid);
        params.put("func", "get_discount");
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();

        fh.post(CommonUrl.MyClinic_Fam_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                DLog.i(TAG, "家庭医生返回数据。" + t.toString());
                Gson gson = new Gson();
                cateinfo = gson.fromJson(t.toString(), CateInfo.class);
                initview();
                dialog.dismiss();
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                DLog.i(TAG, "家庭医生返回数据。" + strMsg);
                dialog.dismiss();
                ToastUtils.longToast("网络繁忙,请稍后重试");
                super.onFailure(t, errorNo, strMsg);
            }

        });

    }

    /**
     * @param timeend   结束时间
     * @param timebegin 开始时间
     * @param maxnum    名额数
     * @param discount  折扣数
     */
    public void sendData(String timeend, String timebegin, String maxnum,
                         String discount) {
        String uid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(uid + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("uid", uid);
        params.put("func", "open_free_or_discount");
        params.put("sign", sign);
        params.put("timeend", timeend);
        params.put("timebegin", timebegin);
        params.put("maxnum", maxnum);
        params.put("discount", discount);
        params.put("category", category);

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.MyClinic_Fam_Url, params, new AjaxCallBack() {

            @Override
            public void onSuccess(String t) {
                DLog.i(TAG, "折扣设置返回数据。。" + t.toString());
                map = ResolveJson.R_Action(t.toString());
                handler.sendEmptyMessage(100);
                super.onSuccess(t);
                type = true;
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                DLog.i(TAG, "错误日志.." + strMsg);
                ToastUtils.longToast("网络繁忙,请稍后重试");
                super.onFailure(t, errorNo, strMsg);
            }
        });

    }

    private OnClickListener saveOnclik_stat = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_save:
                    tv_start_time.setText(start_time.getData2());
                    start_time.dismiss();
                    break;

                default:
                    break;
            }

        }

    };

    public void onClick_back(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.re_stat_time:
                if (!type) {
                    KeyBoardUtils.closeKeyboard(FamCategroyActivity.this);
                    start_time = new NumberPickerPopupwinow2(
                            FamCategroyActivity.this, saveOnclik_stat);
                    // 显示窗口
                    start_time.showAtLocation(main, Gravity.BOTTOM
                            | Gravity.CENTER_HORIZONTAL, 0, 0);
                }

                break;
            case R.id.re_end_time:
                if (!type) {
                    KeyBoardUtils.closeKeyboard(FamCategroyActivity.this);
                    end_time = new NumberPickerPopupwinow2(FamCategroyActivity.this,
                            saveOnclik_end);
                    // 显示窗口
                    end_time.showAtLocation(main, Gravity.BOTTOM
                            | Gravity.CENTER_HORIZONTAL, 0, 0);
                }

                break;
            case R.id.btn1:
                finish();
                break;
            case R.id.btn2:

                String timebegin = tv_start_time.getText().toString().trim();
                String timeend = tv_end_time.getText().toString().trim();
                String maxnum = edit_free_num.getText().toString().trim();
                String discount = getDiscount();


                if (TextUtils.isEmpty(maxnum)) {
                    ToastUtils.shortToast("限免人数不能为空");

                } else if (TextUtils.isEmpty(timebegin)) {
                    ToastUtils.shortToast("开始时间不能为空");
                } else if (TextUtils.isEmpty(timeend)) {
                    ToastUtils.shortToast("结束时间不能为空");

                } else if (TextUtils.isEmpty(discount)) {
                    ToastUtils.shortToast("请选择折扣设置");
                } else {

                    if (NetworkUtil.isNetWorkConnected()) {
                        if (category.equals("3")) {
                            sendData(timeend, timebegin, maxnum, "");
                        } else {
                            sendData(timeend, timebegin, maxnum, discount);
                        }

                    } else {
                        ToastUtils.shortToast("网络连接失败");
                    }
                }

                break;
            default:
                break;
        }
    }

    private boolean isAbleToSetDiscount() {
        //return FamilyDoctorService.getWeekPrice() >= 20 && FamilyDoctorService.getMonthPrice() >= 60;
        return true;
    }

    public String getDiscount() {
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            if (adapter.selectionMap.get(i)) {
                str = list.get(i);
                break;
            }
        }
        return str;
    }

    private OnClickListener saveOnclik_end = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_save:
                    tv_end_time.setText(end_time.getData2());
                    end_time.dismiss();
                    break;

                default:
                    break;
            }

        }

    };

    public class BaseCategroydapter extends BaseAdapter {

        List<String> list;
        private Context context;
        private LayoutInflater inflater;
        public SparseBooleanArray selectionMap;

        public BaseCategroydapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
            inflater = LayoutInflater.from(context);
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
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.my_job_type_item, null);
                holder.textView = (TextView) convertView
                        .findViewById(R.id.tv_job_name);
                holder.imageView = (ImageView) convertView
                        .findViewById(R.id.img_sure);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            boolean ischeck = selectionMap.get(position);
            if (ischeck) {
                holder.imageView.setVisibility(View.VISIBLE);
                holder.textView.setTextColor(context.getResources().getColor(
                        R.color.purse_blue));
            } else {

                holder.imageView.setVisibility(View.GONE);
                holder.textView.setTextColor(context.getResources().getColor(
                        R.color.my_textcolor));
            }
            if (list != null) {
//                if (position == 0) {
//                    holder.textView.setText(list.get(position));
//                } else {
//                    holder.textView.setText(list.get(position) + "折");
//                }
                holder.textView.setText(list.get(position) + "折");

            }
            return convertView;
        }

        private class ViewHolder {
            TextView textView;
            ImageView imageView;
        }

        public void init() {
            for (int i = 0; i < list.size(); i++) {
                selectionMap = new SparseBooleanArray();
                selectionMap.put(i, false);
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
    }

    @Override
    protected void onPause() {
        StatisticalTools.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}