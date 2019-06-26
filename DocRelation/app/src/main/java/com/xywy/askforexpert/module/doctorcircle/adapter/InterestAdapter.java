package com.xywy.askforexpert.module.doctorcircle.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
 * 类描述:
 * 创建人: shihao on 15/12/23 10:04.
 */
public class InterestAdapter extends BaseAdapter {

    private List<InterestePersonItemBean> data;
    private Context context;
    private int type;
    private FinalBitmap fb;

    public InterestAdapter(Context context, List<InterestePersonItemBean> data, String type) {
        this.context = context;
        this.data = data;
        this.type = Integer.parseInt(type);
        fb = FinalBitmap.create(context, false);
        fb.configLoadfailImage(R.drawable.icon_photo_def);
        fb.configLoadingImage(R.drawable.icon_photo_def);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_interes, null);

            mViewHolder.iv_usrer_pic = (ImageView) convertView.findViewById(R.id.iv_usrer_pic);
            mViewHolder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            mViewHolder.tv_job = (TextView) convertView.findViewById(R.id.tv_job);
            mViewHolder.bing = (TextView) convertView.findViewById(R.id.bing);
            mViewHolder.hostpitall = (TextView) convertView.findViewById(R.id.hostpitall);
            mViewHolder.iv_add = (RelativeLayout) convertView.findViewById(R.id.iv_add);
            mViewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.linearLayout);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        InterestePersonItemBean item = data.get(position);
        if (item != null) {
            fb.display(mViewHolder.iv_usrer_pic, item.getPhoto());// 头像


            if (!TextUtils.isEmpty(item.getNickname())) {// 姓名
                mViewHolder.tv_username.setVisibility(View.VISIBLE);
                mViewHolder.tv_username.setText(item.getNickname());
            } else {
                mViewHolder.tv_username.setVisibility(View.GONE);
            }


            if (!TextUtils.isEmpty(item.getJob())) {//医师
                mViewHolder.tv_job.setVisibility(View.VISIBLE);
                mViewHolder.tv_job.setText(item.getJob());
            } else {
                mViewHolder.tv_job.setVisibility(View.GONE);
            }

            if (type == 1) {
                mViewHolder.hostpitall.setVisibility(View.GONE);
            } else {
                if (!TextUtils.isEmpty(item.getHospital())) {//医院
                    mViewHolder.hostpitall.setVisibility(View.VISIBLE);
                    mViewHolder.hostpitall.setText(item.getHospital());
                } else {
                    mViewHolder.hostpitall.setVisibility(View.GONE);
                }
            }

            if (!TextUtils.isEmpty(item.getSubject())) {//妇科
                mViewHolder.bing.setVisibility(View.VISIBLE);
                mViewHolder.bing.setText(item.getSubject());
            } else {
                mViewHolder.bing.setVisibility(View.GONE);
            }


            // 0无关系1我关注的2我的粉丝 3互相关注4 自己
            if (!TextUtils.isEmpty(item.getRelation())) {

                if (item.getRelation().equals("2") || item.getRelation().equals("3") || item.getRelation().equals("4")) {

                    mViewHolder.iv_add.setVisibility(View.GONE);

                } else {

                    mViewHolder.iv_add.setVisibility(View.VISIBLE);

                }
            }
        }

        mViewHolder.tv_username.setOnClickListener(new View.OnClickListener() {//点击用户名

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

        mViewHolder.iv_usrer_pic.setOnClickListener(new View.OnClickListener() {//点击用户头像

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

        mViewHolder.iv_add.setOnClickListener(new View.OnClickListener() {//点击加好友

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
        mViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                enterToPersonCenter(position);
            }
        });
        return convertView;
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

    class ViewHolder {
        LinearLayout linearLayout;
        ImageView iv_usrer_pic;
        TextView tv_username, tv_job, bing, hostpitall;
        RelativeLayout iv_add;
    }
}
