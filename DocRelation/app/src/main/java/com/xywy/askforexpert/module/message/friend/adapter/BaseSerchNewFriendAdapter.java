package com.xywy.askforexpert.module.message.friend.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.model.CreateHXnameInfo;
import com.xywy.askforexpert.model.SerchNewCardInfo;
import com.xywy.askforexpert.module.message.friend.AddCardHoldVerifyActiviy;
import com.xywy.base.view.ProgressDialog;
import com.xywy.easeWrapper.db.InviteMessgeDao;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索好友
 *
 * @author 王鹏
 * @2015-6-2上午9:20:34
 */
@SuppressLint("ResourceAsColor")
public class BaseSerchNewFriendAdapter extends ArrayAdapter<SerchNewCardInfo> {

    private final DisplayImageOptions mOptions;
    public SparseBooleanArray selectionMap;
    List<SerchNewCardInfo> list;
    InviteMessgeDao inviteMessgeDao;
    private Context context;
    private LayoutInflater inflater;
    private ConversationFilter conversationFilter;

    public BaseSerchNewFriendAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
        inflater = LayoutInflater.from(context);
        mOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.icon_photo_def)
                .showImageOnFail(R.drawable.icon_photo_def)
                .showImageOnLoading(R.drawable.icon_photo_def)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    public void setData(List<SerchNewCardInfo> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
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
            convertView = inflater.inflate(R.layout.serch_newfriend_item, null);
            holder.textView = (TextView) convertView.findViewById(R.id.tv_name);
            // holder.re_item = (RelativeLayout) convertView
            // .findViewById(R.id.re_item);
            holder.tv_add = (TextView) convertView.findViewById(R.id.btn_add);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.tv_hospital = (TextView) convertView
                    .findViewById(R.id.tv_hospital);
            holder.tv_subject = (TextView) convertView
                    .findViewById(R.id.tv_subject);
            holder.tv_job = (TextView) convertView.findViewById(R.id.tv_job);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // holder.btn_add.setOnClickListener(new MyOnclick(position));
        if (list != null) {
            holder.textView.setText(list.get(position).getRealname());
//            fb.display(holder.avatar, list.get(position).getPhoto());
            ImageLoader.getInstance().displayImage(list.get(position).getPhoto() == null ? "" : list.get(position).getPhoto(),
                    holder.avatar, mOptions);
            holder.tv_job.setText(list.get(position).getJob());
            String str_hospital = list.get(position).getHospital();
//			if (str_hospital.length() > 12)
//			{
//				str_hospital = str_hospital.substring(1, 10);
//				holder.tv_hospital.setText(str_hospital + "...");
//			} else
//			{
            holder.tv_hospital.setText(str_hospital);
//			}

            holder.tv_subject.setText(list.get(position).getSubject2());
        }
        holder.tv_add.setOnClickListener(new MyOnclick(position));

        return convertView;
    }

    public void getData(String id) {
        final ProgressDialog dialog = new ProgressDialog(context, "正在加载中...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.showProgersssDialog();
        String bind = id;
        Long st = System.currentTimeMillis();
        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("m", "addDocUser");
        params.put("a", "chat");
        params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("id", id);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.get(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        dialog.dismiss();
                        CreateHXnameInfo hxinfo;
                        Gson gson = new Gson();
                        hxinfo = gson.fromJson(t.toString(),
                                CreateHXnameInfo.class);
                        if (hxinfo != null) {
                            if ("0".equals(hxinfo.getCode())) {
                                Intent intent = new Intent(context,
                                        AddCardHoldVerifyActiviy.class);
                                intent.putExtra("toAddUsername", hxinfo.getData().getUsername());
                                context.startActivity(intent);
                            }
                        }


                        super.onSuccess(t);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        dialog.dismiss();
                        super.onFailure(t, errorNo, strMsg);
                    }
                });
    }

    public void init() {
        for (int i = 0; i < list.size(); i++) {
            selectionMap = new SparseBooleanArray();
            selectionMap.put(i, false);
        }
    }

    @Override
    public Filter getFilter() {
        if (conversationFilter == null) {
            conversationFilter = new ConversationFilter(list);
        }
        return conversationFilter;
    }

    private class ViewHolder {
        TextView textView;
        RelativeLayout re_item;
        TextView tv_add;
        ImageView avatar;
        TextView tv_job;
        TextView tv_hospital;
        TextView tv_subject;
    }

    class MyOnclick implements OnClickListener {
        int position;

        public MyOnclick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View arg0) {
            if (list.get(position).getHx_username().equals("")) {
                if (NetworkUtil.isNetWorkConnected()) {
                    getData(list.get(position).getPid());
                } else {
                    ToastUtils.shortToast( "网络连接失败");
                }

            } else {
                Intent intent = new Intent(context,
                        AddCardHoldVerifyActiviy.class);
                intent.putExtra("toAddUsername", list.get(position)
                        .getHx_username());
                context.startActivity(intent);
            }
        }

    }

    private class ConversationFilter extends Filter {
        List<SerchNewCardInfo> mOriginalValues = null;

        public ConversationFilter(List<SerchNewCardInfo> mList) {
            mOriginalValues = mList;
        }

        @Override
        protected FilterResults performFiltering(CharSequence arg0) {
            FilterResults results = new FilterResults();
            List<SerchNewCardInfo> copy_list = new ArrayList<SerchNewCardInfo>();

            if (mOriginalValues == null) {
                mOriginalValues = new ArrayList<SerchNewCardInfo>();
            }
            if (arg0 == null || arg0.length() == 0) {
                results.count = mOriginalValues.size();
                results.values = mOriginalValues;
            } else {
                arg0 = arg0.toString().toLowerCase();
                for (int i = 0; i < mOriginalValues.size(); i++) {
                    SerchNewCardInfo sercard = mOriginalValues.get(i);
                    String realname = sercard.getRealname();
                    if (realname.toLowerCase().startsWith(arg0.toString())) {
                        copy_list.add(sercard);
                    }
                }
                results.count = copy_list.size();
                results.values = copy_list;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence arg0, FilterResults arg1) {
            // TODO Auto-generated method stub
            list = (List<SerchNewCardInfo>) arg1.values;
            notifyDataSetChanged();
        }
    }
}
