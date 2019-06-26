package com.xywy.askforexpert.module.message.friend.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.SparseIntArray;
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
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.model.InviteNewFriendInfo;
import com.xywy.askforexpert.module.message.friend.AddCardHoldVerifyActiviy;

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
public class BaseInviteNewFriendAdapter extends BaseAdapter {

    //	public SparseBooleanArray selectionMap;
    public SparseIntArray selectionMap;
    List<InviteNewFriendInfo> list;
    private Context context;
    private LayoutInflater inflater;
    private FinalBitmap fb;

    public BaseInviteNewFriendAdapter(Context context) {
        this.context = context;

        inflater = LayoutInflater.from(context);
        fb = FinalBitmap.create(context, false);
    }

    public void setData(List<InviteNewFriendInfo> list) {
        this.list = list;
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
            convertView = inflater.inflate(R.layout.invite_item, null);
            holder.textView = (TextView) convertView.findViewById(R.id.tv_name);
            holder.btn_add = (TextView) convertView.findViewById(R.id.btn_add);
            holder.btn_invite = (TextView) convertView.findViewById(R.id.btn_invite);
            holder.btn_invite_gone = (TextView) convertView.findViewById(R.id.btn_invite_gone);
            holder.btn_add_gone = (TextView) convertView.findViewById(R.id.btn_add_gone);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (list != null) {
            if (!list.get(position).getName().equals("")) {
                holder.textView.setText(list.get(position).getName());
            } else {
                holder.textView.setText(list.get(position).getPhone());
            }
            String isFriend = list.get(position).getIsfriend();
            String xywyid = list.get(position).getXywyid();

            if (isFriend.equals("1")) {
//				holder.btn_add.setBackgroundResource(R.drawable.yuanjiao);
//				holder.btn_add.setTextColor(R.color.my_textcolor);
//				holder.btn_add.setText("已添加");
                holder.btn_add_gone.setVisibility(View.VISIBLE);
                holder.btn_add.setVisibility(View.GONE);
                holder.btn_invite.setVisibility(View.GONE);
                holder.btn_invite_gone.setVisibility(View.GONE);
            } else if (isFriend.equals("0") & xywyid.equals("0")) {
                String hassend = list.get(position).getHassend();
                if ("0".equals(hassend)) {

//					holder.btn_add
//							.setBackgroundResource(R.drawable.yuanjiao_green);
//					holder.btn_add.setText("邀请");
                    holder.btn_invite.setOnClickListener(new MyOnClick(position,
                            0, holder.btn_invite));
                    holder.btn_add_gone.setVisibility(View.GONE);
                    holder.btn_add.setVisibility(View.GONE);
                    holder.btn_invite.setVisibility(View.VISIBLE);
                    holder.btn_invite_gone.setVisibility(View.GONE);
                } else {
//					holder.btn_add.setBackgroundResource(R.drawable.yuanjiao);
//					holder.btn_add.setTextColor(R.color.my_textcolor);
//					holder.btn_add.setText("已邀请");

                    holder.btn_add_gone.setVisibility(View.GONE);
                    holder.btn_add.setVisibility(View.GONE);
                    holder.btn_invite_gone.setVisibility(View.VISIBLE);
                    holder.btn_invite.setVisibility(View.GONE);
                }

            } else if (isFriend.equals("0") & !xywyid.equals("0")) {
//				holder.btn_add
//						.setBackgroundResource(R.drawable.yuanjiao_yellew);
//				holder.btn_add.setText("添加");
                holder.btn_add.setOnClickListener(new MyOnClick(position, 1,
                        holder.btn_add));
                holder.btn_add_gone.setVisibility(View.GONE);
                holder.btn_add.setVisibility(View.VISIBLE);
                holder.btn_invite_gone.setVisibility(View.GONE);
                holder.btn_invite.setVisibility(View.GONE);
            }

        }
        return convertView;
    }

    public void sendData(String mobie) {
        String did = YMApplication.getLoginInfo().getData().getPid();
        String bind = did + mobie;
        Long st = System.currentTimeMillis();

        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();

        params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("a", "chat");
        params.put("m", "invitefriend");
        params.put("did", did);
        params.put("mobile", mobie);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.get(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        // TODO Auto-generated method stub
                        Map<String, String> map = ResolveJson.R_Action(t
                                .toString());
                        if (map.get("code").equals("0")) {
                            ToastUtils.shortToast( map.get("msg"));
                        }

                        super.onSuccess(t);
                    }
                });

    }

    public void init() {
        for (int i = 0; i < list.size(); i++) {
            selectionMap = new SparseIntArray();
            int tag = Integer.valueOf(list.get(i).getHassend());
            selectionMap.put(i, tag);
        }
    }

    class MyOnClick implements OnClickListener {
        int position;
        int type;
        TextView tv;

        public MyOnClick(int position, final int type, TextView tv) {
            this.position = position;
            this.type = type;
            this.tv = tv;
        }

        @Override
        public void onClick(View arg0) {
            if (type == 0) {
//				T.shortToast( "邀请");
                sendData(list.get(position).getPhone());
//				tv.setText("已邀请");
//
//				tv.setBackgroundResource(R.drawable.yuanjiao);
//				tv.setTextColor(R.color.my_textcolor);
                list.get(position).setHassend("1");
                setData(list);
                notifyDataSetChanged();
            } else if (type == 1) {
//				T.shortToast( "添加");
                Intent intent = new Intent(context,
                        AddCardHoldVerifyActiviy.class);
                intent.putExtra("toAddUsername", "did_"
                        + list.get(position).getXywyid());
                context.startActivity(intent);
                // tv.setText("已发送");
            }

        }

    }

    private class ViewHolder {
        TextView textView;
        RelativeLayout re_item;
        TextView btn_add;
        TextView btn_invite;
        TextView btn_invite_gone;
        TextView btn_add_gone;
        ImageView avatar;
    }
}
