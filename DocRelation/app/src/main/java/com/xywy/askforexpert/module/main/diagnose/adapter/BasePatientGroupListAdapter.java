package com.xywy.askforexpert.module.main.diagnose.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.model.PatientGroupInfo;
import com.xywy.askforexpert.module.discovery.medicine.common.MyRxBus;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 学校城市 适配器
 *
 * @author 王鹏
 * @2015-5-7下午5:48:59
 */
@SuppressLint("ResourceAsColor")
public class BasePatientGroupListAdapter extends BaseAdapter {

    public SparseBooleanArray selectionMap;
    public boolean isDelete = false;
    public Map<String, String> map = new HashMap<String, String>();
    List<PatientGroupInfo> list;
    private Context context;
    private LayoutInflater inflater;
    private ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);

    public BasePatientGroupListAdapter(Context context) {
        this.context = context;

        inflater = LayoutInflater.from(context);
    }

    public void setData(List<PatientGroupInfo> list) {
        this.list = list;
        if (list != null) {
            init();
        }

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.patient_group_item, null);
            holder.textView = (TextView) convertView
                    .findViewById(R.id.tv_group_name);
            // holder.re_item = (RelativeLayout) convertView
            // .findViewById(R.id.re_item);
            holder.img_delete = (ImageView) convertView
                    .findViewById(R.id.img_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (isDelete) {
            holder.img_delete.setVisibility(View.VISIBLE);
        } else {
            holder.img_delete.setVisibility(View.GONE);

        }
        boolean ischeck = selectionMap.get(position);
        if (ischeck) {
            holder.textView.setTextColor(context.getResources().getColor(
                    R.color.color_00c8aa));
        } else {
            holder.textView.setTextColor(context.getResources().getColor(
                    R.color.my_textcolor));

        }
        if (NetworkUtil.isNetWorkConnected()) {
            holder.img_delete.setOnClickListener(new MyOnclick(position));

        } else {
            ToastUtils.shortToast( "网络连接失败");
        }
        if (list != null) {
            holder.textView.setText(list.get(position).getName());
        }
        return convertView;
    }

    public void init() {
        for (int i = 0; i < list.size(); i++) {
            selectionMap = new SparseBooleanArray();
            selectionMap.put(i, false);
        }
    }

    private class ViewHolder {
        TextView textView;
        RelativeLayout re_item;
        ImageView img_delete;
    }

    class MyOnclick implements OnClickListener {
        private int Postion;

        public MyOnclick(int Postion) {
            this.Postion = Postion;
        }

        @Override
        public void onClick(View arg0) {
            String id = list.get(Postion).getId();
            String did = YMApplication.getLoginInfo().getData().getPid();
            String bind = did + id;
            Long st = System.currentTimeMillis();

            String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
            AjaxParams params = new AjaxParams();

            params.put("timestamp", st + "");
            params.put("bind", bind);
            params.put("a", "chat");
            params.put("m", "groupDelete");
            params.put("did", did);
            params.put("id", id);
            params.put("sign", sign);

            FinalHttp fh = new FinalHttp();
            fh.get(CommonUrl.Patient_Manager_Url, params,
                    new AjaxCallBack() {
                        @Override
                        public void onSuccess(String t) {
                            map = ResolveJson.R_Action(t.toString());
                            if (map.get("code").equals("0")) {
                                list.remove(Postion);
                                notifyDataSetChanged();
                                //通知更新最近咨询患者的接口的数据
                                scheduledThreadPool.schedule(new Runnable() {
                                    @Override
                                    public void run() {
                                        LogUtils.i("delay 500 MILLISECONDS");
                                        //防止服务端还未将数据推送到最近咨询的接口，
                                        // 延时500毫秒通知MedicineAssistantActivity刷新数据
                                        //收到患者发过来的消息，表明有患者咨询了医生，这时，要通知MedicineAssistantActivity
                                        //页面从新请求最近咨询患者的接口了
                                        MyRxBus.notifyLatestPatientInfoChanged();
                                    }
                                }, 500, TimeUnit.MILLISECONDS);
                            } else {
                                ToastUtils.shortToast( "删除失败");
                            }
                            // handler.sendEmptyMessage(100);
                            super.onSuccess(t);
                        }
                    });

        }

    }
}
