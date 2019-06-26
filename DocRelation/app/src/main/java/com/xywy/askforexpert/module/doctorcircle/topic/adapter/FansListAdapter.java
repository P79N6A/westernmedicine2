package com.xywy.askforexpert.module.doctorcircle.topic.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.adapter.CommonBaseAdapter;
import com.xywy.askforexpert.appcommon.base.adapter.CommonViewHolder;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.topics.FansData;
import com.xywy.askforexpert.module.main.service.person.PersonDetailActivity;
import com.xywy.askforexpert.module.message.friend.AddCardHoldVerifyActiviy;

import java.util.List;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/5/4 15:21
 */
public class FansListAdapter extends CommonBaseAdapter<FansData.DataBean> implements View.OnClickListener {
    private static final String TAG = "FansListAdapter";

    private final DisplayImageOptions mOptions;

    public FansListAdapter(Context mContext, List<FansData.DataBean> mDatas, int mItemLayoutId) {
        super(mContext, mDatas, mItemLayoutId);

        mOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.icon_photo_def)
                .showImageOnFail(R.drawable.icon_photo_def)
                .showImageOnLoading(R.drawable.icon_photo_def)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mItemLayoutId, parent, false);
        }

        ImageView userAvatar = CommonViewHolder.getView(convertView, R.id.fans_list_avatar);
        TextView userName = CommonViewHolder.getView(convertView, R.id.fans_list_name);
        TextView userDep = CommonViewHolder.getView(convertView, R.id.fans_list_dep);
        TextView userHos = CommonViewHolder.getView(convertView, R.id.fans_list_hos);
        TextView isFriend = CommonViewHolder.getView(convertView, R.id.fans_list_add_friend);

        FansData.DataBean dataBean = mDatas.get(position);
        if (dataBean != null) {
            mImageLoader.displayImage(dataBean.getPhoto(), userAvatar, mOptions);
            if (dataBean.getNickname() != null && !dataBean.getNickname().equals("")) {
                userName.setText(dataBean.getNickname());
            } else {
                userName.setText("用户" + dataBean.getUserid());
            }
            userDep.setText(dataBean.getJob());
            userHos.setText(dataBean.getHospital());
            // 0无关系   1我的粉丝     2我关注的   3互相关注   4 自己
            int relation = dataBean.getRelation();
            DLog.d(TAG, "fans relation = " + relation);
            if (relation == 2 || relation == 3 || relation == 4) {
                isFriend.setVisibility(View.GONE);
            } else {
                isFriend.setVisibility(View.VISIBLE);
            }
        }

        userAvatar.setTag(position);
        userName.setTag(position);
        isFriend.setTag(position);
        userAvatar.setOnClickListener(this);
        userName.setOnClickListener(this);
        isFriend.setOnClickListener(this);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        switch (v.getId()) {
            case R.id.fans_list_avatar:
            case R.id.fans_list_name:
                if (!NetworkUtil.isNetWorkConnected()) {
                    ToastUtils.shortToast( "网络异常，请检查网络连接");
                } else {
                    if (YMUserService.isGuest()) {
                        DialogUtil.LoginDialog(new YMOtherUtils(mContext).context);
                    } else {
                        Intent intent = new Intent(mContext, PersonDetailActivity.class);
                        DLog.d(TAG, "fans userId = " + mDatas.get(pos).getUserid() + ", relation = " + mDatas.get(pos).getRelation());
                        intent.putExtra("uuid", String.valueOf(mDatas.get(pos).getUserid()));
                        mContext.startActivity(intent);
                    }
                }
                break;
            case R.id.fans_list_add_friend:
                if (!NetworkUtil.isNetWorkConnected()) {
                    ToastUtils.shortToast( "网络异常，请检查网络连接");
                } else {
                    if (YMUserService.isGuest()) {// 登陆
                        DialogUtil.LoginDialog(new YMOtherUtils(mContext).context);
                    } else {
                        int touserid = mDatas.get(pos).getUserid();
                        Intent intenAdd = new Intent(mContext, AddCardHoldVerifyActiviy.class);
                        intenAdd.putExtra("toAddUsername", "did_" + touserid);
                        mContext.startActivity(intenAdd);
                    }
                }
                break;
        }
    }
}
