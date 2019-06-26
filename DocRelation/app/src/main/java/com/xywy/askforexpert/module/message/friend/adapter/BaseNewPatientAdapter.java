package com.xywy.askforexpert.module.message.friend.adapter;

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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.model.PatientListInfo;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.List;
import java.util.Map;

/**
 * 学校城市 适配器
 *
 * @author 王鹏
 * @2015-5-7下午5:48:59
 */
@SuppressLint("ResourceAsColor")
public class BaseNewPatientAdapter extends BaseAdapter {

    public SparseBooleanArray selectionMap;
    List<PatientListInfo> list;
    ImageLoader instance;
    DisplayImageOptions options;
    private Context context;
    private LayoutInflater inflater;
    private FinalBitmap fb;

    public BaseNewPatientAdapter(Context context) {
        this.context = context;

        inflater = LayoutInflater.from(context);
        fb = FinalBitmap.create(context, false);
        options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .showStubImage(R.drawable.icon_photo_def)
                .showImageForEmptyUri(R.drawable.icon_photo_def)
                .showImageOnFail(R.drawable.icon_photo_def).cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        instance = ImageLoader.getInstance();
    }

    public void setData(List<PatientListInfo> list) {
        this.list = list;
        if (list.size() > 0) {
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
            convertView = inflater.inflate(R.layout.patient_server_gone_item,
                    null);
            holder.textView = (TextView) convertView.findViewById(R.id.tv_name);
            // holder.re_item = (RelativeLayout) convertView
            // .findViewById(R.id.re_item);
            holder.btn_add = (TextView) convertView.findViewById(R.id.btn_add);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.btn_add_gone = (TextView) convertView
                    .findViewById(R.id.btn_add_gone);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (list != null) {
            holder.textView.setText(list.get(position).getRealname());
//			fb.display(holder.avatar, list.get(position).getPhoto());
//			fb.configLoadfailImage(R.drawable.icon_photo_def);
            instance.displayImage(list.get(position).getPhoto(), holder.avatar, options);

            String hassend = list.get(position).getHassend();
            boolean ischeck = selectionMap.get(position);
            if (ischeck)// 0
            {
                holder.btn_add.setVisibility(View.VISIBLE);
                holder.btn_add_gone.setVisibility(View.GONE);
            } else
            // 1
            {
                holder.btn_add_gone.setVisibility(View.VISIBLE);
                holder.btn_add.setVisibility(View.GONE);
            }
            holder.btn_add.setOnClickListener(new MyOnclick(position,
                    holder.btn_add, holder.btn_add_gone));
        }
        return convertView;
    }

    public void getData(String mobile) {
        String did = YMApplication.getLoginInfo().getData().getPid();
        String bind = did + mobile;
        Long st = System.currentTimeMillis();

        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();

        params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("a", "chat");
        params.put("m", "patientSendmsg");
        params.put("did", did);
        params.put("mobile", mobile);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        // "http://test.api.app.club.xywy.com/app/1.0/index.interface.php"
        fh.get(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        // TODO Auto-generated method stub
                        Map<String, String> map = ResolveJson.R_Action(t
                                .toString());
                        ToastUtils.shortToast( map.get("msg"));
                        super.onSuccess(t);
                    }
                });

    }

    public void init() {
        selectionMap = new SparseBooleanArray();
        for (int i = 0; i < list.size(); i++) {
            if ("0".equals(list.get(i).getHassend())) {
                selectionMap.put(i, true);
            } else if ("1".equals(list.get(i).getHassend())) {
                selectionMap.put(i, false);
            }

        }
    }

    private class ViewHolder {
        TextView textView;
        RelativeLayout re_item;
        TextView btn_add;
        ImageView avatar;
        TextView btn_add_gone;
    }

    class MyOnclick implements OnClickListener {
        int position;
        TextView tv;
        TextView tv_gone;

        public MyOnclick(int position, TextView tv, TextView tv_gone) {
            this.position = position;
            this.tv = tv;
            this.tv_gone = tv_gone;
        }

        @Override
        public void onClick(View arg0) {
            String mobile = list.get(position).getMobile();
            if (mobile != null & !"".equals(mobile)) {
                getData(mobile);
                // tv.setVisibility(View.GONE);
                // tv_gone.setVisibility(View.VISIBLE);
                //
                // tv_gone.setText("已发送");

                // list.get(position).setHassend("1");
                // setData(list);
                selectionMap.put(position, false);
                notifyDataSetChanged();
//				T.shortToast( "发了");
            } else {
                ToastUtils.shortToast( "没有手机号");
            }

        }

    }
}
