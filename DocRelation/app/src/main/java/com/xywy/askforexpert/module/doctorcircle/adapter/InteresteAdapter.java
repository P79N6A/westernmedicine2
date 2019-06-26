package com.xywy.askforexpert.module.doctorcircle.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.DoctorAPI;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.doctor.InterestePersonItemBean;
import com.xywy.askforexpert.module.main.service.person.PersonDetailActivity;
import com.xywy.askforexpert.module.message.friend.AddCardHoldVerifyActiviy;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

/**
 * 感兴趣的人适配器 stone
 *
 * @author apple
 */
public class InteresteAdapter extends RecyclerView.Adapter<InteresteAdapter.Holder> {

    private List<InterestePersonItemBean> data;
    private Activity context;
    private int type;
    private FinalBitmap fb;

    public InteresteAdapter(Activity context, List<InterestePersonItemBean> data, String type) {
        this.context = context;
        this.data = data;
        this.type = Integer.parseInt(type);
        fb = FinalBitmap.create(context, false);
        fb.configLoadfailImage(R.drawable.icon_photo_def);
        fb.configLoadingImage(R.drawable.icon_photo_def);
    }

    public void setUnVisitCount(int unVisitCount) {
        this.unVisitCount = unVisitCount;
    }

    private int unVisitCount=0;


    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup view, final int arg1) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_interest_layout, null);

        Holder holder = new Holder(v);
        return holder;
    }
    //type = 1  推荐好友 3 访问用户列表

    @Override
    public void onBindViewHolder(Holder holder, final int position) {

        InterestePersonItemBean item = data.get(position);
        if (item != null) {
            fb.display(holder.iv_usrer_pic, item.getPhoto());// 头像


            if (!TextUtils.isEmpty(item.getNickname())) {// 姓名
                holder.tv_username.setVisibility(View.VISIBLE);
                holder.tv_username.setText(item.getNickname());
            } else {
                holder.tv_username.setVisibility(View.GONE);
            }


            if (!TextUtils.isEmpty(item.getJob())) {//医师
                holder.tv_job.setVisibility(View.VISIBLE);
                holder.tv_job.setText(item.getJob());
            } else {
                holder.tv_job.setVisibility(View.GONE);
            }


            if (!TextUtils.isEmpty(item.getHospital())) {//医院
                holder.hostpitall.setVisibility(View.VISIBLE);
                holder.hostpitall.setText(item.getHospital());
            } else {
                holder.hostpitall.setVisibility(View.GONE);
            }


            if (!TextUtils.isEmpty(item.getSubject())) {//妇科
                holder.bing.setVisibility(View.VISIBLE);
                holder.bing.setText(item.getSubject());
            } else {
                holder.bing.setVisibility(View.GONE);
            }


            // 0无关系1我关注的2我的粉丝 3互相关注4 自己
            if (!TextUtils.isEmpty(item.getRelation())) {

                if (item.getRelation().equals("2") || item.getRelation().equals("3") || item.getRelation().equals("4")) {

                    holder.iv_add.setVisibility(View.GONE);

                } else {

                    holder.iv_add.setVisibility(View.VISIBLE);

                }
            }
        }

        holder.tv_username.setOnClickListener(new OnClickListener() {//点击用户名

            @Override
            public void onClick(View arg0) {
                enterToPersonCenter(position);

                if (type == 1) {
                    StatisticalTools.eventCount(context, "yqRecomListToHomepage");
                } else {
                    StatisticalTools.eventCount(context, "yqViewListToHomepage");
                }


            }
        });

        holder.iv_usrer_pic.setOnClickListener(new OnClickListener() {//点击用户头像

            @Override
            public void onClick(View arg0) {
                enterToPersonCenter(position);
                if (type == 1) {
                    StatisticalTools.eventCount(context, "yqRecomListToHomepage");
                } else {
                    StatisticalTools.eventCount(context, "yqViewListToHomepage");
                }

            }
        });

        holder.iv_add.setOnClickListener(new OnClickListener() {//点击加好友

            @Override
            public void onClick(View v) {

                if (!NetworkUtil.isNetWorkConnected()) {
                    ToastUtils.shortToast( context.getString(R.string.no_network));//"网络异常，请检查网络连接"
                    return;
                }

                if (YMUserService.isGuest()) {
                    DoctorAPI.startLogIn(context);
                    return;
                }

                InterestePersonItemBean realNameItem = data.get(position);
                String touserid = realNameItem.getUserid();
                Intent intenAdd = new Intent(context, AddCardHoldVerifyActiviy.class);
                intenAdd.putExtra("toAddUsername", "did_" + touserid);
                context.startActivity(intenAdd);
                if (type == 1) {
                    StatisticalTools.eventCount(context, "yqRecomListAddf");
                } else {
                    StatisticalTools.eventCount(context, "yqViewListAddf");
                }

            }
        });
        holder.linearLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                enterToPersonCenter(position);
            }
        });

        //需要区分前面的未访问和已经访问过的
        if (unVisitCount>0){
            if (position<unVisitCount){
                 holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        }
    }

    /**
     * 进入个人中心
     */
    private void enterToPersonCenter(int posison) {

        if (!NetworkUtil.isNetWorkConnected()) {
            ToastUtils.shortToast( context.getString(R.string.no_network));//"网络异常，请检查网络连接"
            return;
        }

        InterestePersonItemBean reaiposon = data.get(posison);
        Intent itemPoson = new Intent(context, PersonDetailActivity.class);
        itemPoson.putExtra("uuid", reaiposon.getUserid());
        itemPoson.putExtra("isDoctor", reaiposon.getRelation());//是否是好友或者关注
        context.startActivity(itemPoson);

    }


    public static class Holder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        ImageView iv_usrer_pic;
        TextView tv_username, tv_job, bing, hostpitall;
        RelativeLayout iv_add;

        public Holder(View v) {
            super(v);
            iv_usrer_pic = (ImageView) v.findViewById(R.id.iv_usrer_pic);
            tv_username = (TextView) v.findViewById(R.id.tv_username);
            tv_job = (TextView) v.findViewById(R.id.tv_job);
            bing = (TextView) v.findViewById(R.id.bing);
            hostpitall = (TextView) v.findViewById(R.id.hostpitall);
            iv_add = (RelativeLayout) v.findViewById(R.id.iv_add);
            linearLayout = (LinearLayout) v.findViewById(R.id.linearLayout);


        }
    }

}
