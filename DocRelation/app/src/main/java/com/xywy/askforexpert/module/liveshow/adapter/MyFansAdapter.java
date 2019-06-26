package com.xywy.askforexpert.module.liveshow.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.adapter.better.YMRVSingleTypeBaseAdapter;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.net.rxretrofitoktools.service.WWSXYWYService;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.liveshow.FollowUNFollowResultBean;
import com.xywy.askforexpert.model.liveshow.MyFansBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * 我的粉丝Adapter
 * Created by bailiangjin on 2017/2/24.
 */

public class MyFansAdapter extends YMRVSingleTypeBaseAdapter<MyFansBean> {

    public MyFansAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutResId() {
        return R.layout.item_liveshow_my_fans;
    }

    @Override
    protected void convert(ViewHolder holder, final MyFansBean item, int position) {
        if(null==item){
          LogUtils.e("条目数据为空，服务端问题");
            return;
        }

        ImageView iv_icon= holder.getView(R.id.iv_icon);
        //  2017/3/2 服务端添加字段后 加载粉丝头像
        ImageLoadUtils.INSTANCE.loadCircleImageView(iv_icon, item.getUser().getPortrait(),R.drawable.icon_live_show_default_head);

        holder.setText(R.id.tv_name, TextUtils.isEmpty(item.getUser().getName())? "用户"+item.getUser().getUserid():item.getUser().getName());
        holder.setText(R.id.tv_level, "LV." + item.getUser().getLever());

        TextView tv_relation_state = holder.getView(R.id.tv_relation_state);
        TextView tv_follow = holder.getView(R.id.tv_follow);
        if (0 == item.getUser().getState()) {
            tv_follow.setVisibility(View.VISIBLE);
            tv_relation_state.setVisibility(View.GONE);

            tv_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //关注
                    WWSXYWYService.follow(YMUserService.getCurUserId(),""+ item.getUser().getUserid(), 1, new CommonResponse<FollowUNFollowResultBean>() {
                        @Override
                        public void onNext(FollowUNFollowResultBean followUNFollowResultBean) {
                            if(null!=followUNFollowResultBean){
                                ToastUtils.shortToast("关注成功");
                                LogUtils.e("关注成功："+GsonUtils.toJson(followUNFollowResultBean));
                                item.getUser().setState(1);
                                notifyDataSetChanged();
                            }else {
                                LogUtils.e("关注失败");
                            }

                        }
                    });
                }
            });
        } else {
            tv_follow.setVisibility(View.GONE);
            tv_relation_state.setVisibility(View.VISIBLE);
        }
    }


}
