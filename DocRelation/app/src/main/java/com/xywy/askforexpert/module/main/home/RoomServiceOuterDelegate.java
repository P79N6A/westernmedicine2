package com.xywy.askforexpert.module.main.home;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.home.HomeGVItemBean;
import com.xywy.askforexpert.model.home.HomeItemBean;
import com.xywy.askforexpert.module.my.smallstation.MySite;
import com.xywy.askforexpert.widget.module.home.HomeRoomServiceItemView;
import com.xywy.util.LogUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xgxg on 2017/10/18.
 */

public class RoomServiceOuterDelegate implements ItemViewDelegate<HomeItemBean> {
    Context context;
    public RoomServiceOuterDelegate(Context context){
        this.context = context;
    }
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_home_room_service_grid_outer;
    }

    @Override
    public boolean isForViewType(HomeItemBean item, int position) {
        return HomeItemBean.TYPE_GRID==item.getType();
    }

    @Override
    public void convert(ViewHolder holder, HomeItemBean homneItemBean, int position) {
        final TextView tv_more = holder.getView(R.id.tv_more);
        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, MySite.class));

                //stone 更多服务
                StatisticalTools.eventCount(context, Constants.MORESERVICE);
            }
        });


        HomeRoomServiceItemView dsiv_wtgc = holder.getView(R.id.dsiv_wtgc);
        HomeRoomServiceItemView dsiv_imwd = holder.getView(R.id.dsiv_imwd);
        HomeRoomServiceItemView dsiv_jtys = holder.getView(R.id.dsiv_jtys);
        HomeRoomServiceItemView dsiv_dhys = holder.getView(R.id.dsiv_dhys);
        HomeRoomServiceItemView dsiv_yyzz = holder.getView(R.id.dsiv_yyzz);
        HomeRoomServiceItemView dsiv_consulting_room_online = holder.getView(R.id.dsiv_consulting_room_online);


        HomeRoomServiceItemView more_servide = holder.getView(R.id.more_servide);

        HomeRoomServiceItemView more_servide1 = holder.getView(R.id.more_servide1);
//        more_servide1.setName("");
//        more_servide1.setState("");
//        more_servide1.setNumberTvVisibility(false);
//        more_servide1.setOpenTvVisibility(false);
//        more_servide1.setIconRes(R.drawable.blank);


        HomeRoomServiceItemView more_servide2 = holder.getView(R.id.more_servide2);
        more_servide2.setName("");
        more_servide2.setState("");
        more_servide2.setNumberTvVisibility(false);
        more_servide2.setOpenTvVisibility(false);
        more_servide2.setIconRes(R.drawable.blank);

        ArrayList<HomeRoomServiceItemView> views = new ArrayList<>();
        views.add(dsiv_wtgc);
        views.add(dsiv_imwd);
        views.add(dsiv_jtys);
        views.add(dsiv_dhys);
        views.add(dsiv_yyzz);
        views.add(dsiv_consulting_room_online);
        views.add(more_servide);
        views.add(more_servide1);

        List<HomeGVItemBean> homeGVItemDatas = homneItemBean.getHomeGVItemDatas();
        for(int i =0; i<homeGVItemDatas.size();i++){
            fillDataForServiceItemView(views.get(i), homeGVItemDatas.get(i));
        }
//        final HomeGVItemBean innerItem1 = homeGVItemDatas.get(0);
//        final HomeGVItemBean innerItem2 = homeGVItemDatas.get(1);
//        final HomeGVItemBean innerItem3 = homeGVItemDatas.get(2);
//        final HomeGVItemBean innerItem4 = homeGVItemDatas.get(3);
//        final HomeGVItemBean innerItem5 = homeGVItemDatas.get(4);
//        final HomeGVItemBean innerItem6 = homeGVItemDatas.get(5);
//        final HomeGVItemBean innerItem6 = homeGVItemDatas.get(5);
//        fillDataForServiceItemView(dsiv_wtgc, innerItem1);
//        fillDataForServiceItemView(dsiv_imwd, innerItem2);
//        fillDataForServiceItemView(dsiv_jtys, innerItem3);
//        fillDataForServiceItemView(dsiv_dhys, innerItem4);
//        fillDataForServiceItemView(dsiv_yyzz, innerItem5);
//        fillDataForServiceItemView(dsiv_consulting_room_online, innerItem6);
//        fillDataForServiceItemView(dsiv_consulting_room_online, innerItem6);
    }

    private void fillDataForServiceItemView(final HomeRoomServiceItemView homeRoomServiceItemView, final HomeGVItemBean innerItem) {
        if (TextUtils.isEmpty(innerItem.getName())) {
            //空条目 占位用
            homeRoomServiceItemView.setVisibility(View.GONE);
            return;
        } else {
            homeRoomServiceItemView.setVisibility(View.VISIBLE);
        }
        //提醒数
        homeRoomServiceItemView.setName(innerItem.getName());
        homeRoomServiceItemView.setState(innerItem.getDesc());

        if (innerItem.getUnreadMsgCount() <= 0) {
            homeRoomServiceItemView.setNumberTvVisibility(false);
        } else {
            homeRoomServiceItemView.setNumberTvVisibility(true);
            homeRoomServiceItemView.setNumberText(innerItem.getUnreadMsgCount());
        }
        homeRoomServiceItemView.setOpenTvVisibility(!innerItem.isOpened());
        homeRoomServiceItemView.setIconRes(innerItem.getIconResId());


        homeRoomServiceItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == innerItem) {
                    LogUtils.e("item为空");
                    return;
                }
                if ("问题广场".equals(innerItem.getName())) {
                    CommonUtils.gotoService(homeRoomServiceItemView.getContext(), 1);
                } else if (context.getString(R.string.online_consultation).equals(innerItem.getName())) {
                    CommonUtils.gotoService(homeRoomServiceItemView.getContext(), 888);
                } else if ("家庭医生".equals(innerItem.getName())) {
                    CommonUtils.gotoService(homeRoomServiceItemView.getContext(), 5);
                } else if ("电话医生".equals(innerItem.getName())) {
                    CommonUtils.gotoService(homeRoomServiceItemView.getContext(), 7);
                } else if ("预约转诊".equals(innerItem.getName())) {
                    CommonUtils.gotoService(homeRoomServiceItemView.getContext(), 10);
                } else if("问诊用药".equals(innerItem.getName())){
                    CommonUtils.gotoService(context, 12);
                }else if("极速电话".equals(innerItem.getName())){
                    if (innerItem.isOpened()){
                        CommonUtils.gotoService(context, 13);
                    }
                }else if("诊后患者".equals(innerItem.getName())){
                    if (innerItem.isOpened()){
                        CommonUtils.gotoService(context, 14);
                    }
                }else {
                    ToastUtils.shortToast("无效跳转");
                }
            }
        });
    }


}
