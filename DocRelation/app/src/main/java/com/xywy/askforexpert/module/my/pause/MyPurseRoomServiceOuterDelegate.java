package com.xywy.askforexpert.module.my.pause;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.MyPurse.MyPurseGVItemBean;
import com.xywy.askforexpert.model.MyPurse.MyPurseItemBean;
import com.xywy.askforexpert.widget.module.home.HomeRoomServiceItemView;
import com.xywy.util.LogUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xgxg on 2018/6/20.
 */

public class MyPurseRoomServiceOuterDelegate implements ItemViewDelegate<MyPurseItemBean>, View.OnTouchListener {
    Context context;
    private int currentMonth;
    private ArrayList<HomeRoomServiceItemView> viewList = new ArrayList();

    public MyPurseRoomServiceOuterDelegate(Context context){
        this.context = context;

    }
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_home_room_service_grid_outer;
    }

    @Override
    public boolean isForViewType(MyPurseItemBean item, int position) {
        return MyPurseItemBean.TYPE_GRID==item.getType();
    }

    @Override
    public void convert(ViewHolder holder, MyPurseItemBean myPurseItemBean, int position) {
        holder.getView(R.id.ll_root).setBackgroundColor(context.getResources().getColor(R.color.white));
        holder.getView(R.id.ll).setVisibility(View.GONE);
        holder.getView(R.id.ll_3).setVisibility(View.GONE);
        HomeRoomServiceItemView dsiv_wtgc = holder.getView(R.id.dsiv_wtgc);
        HomeRoomServiceItemView dsiv_imwd = holder.getView(R.id.dsiv_imwd);
        HomeRoomServiceItemView dsiv_jtys = holder.getView(R.id.dsiv_jtys);
        HomeRoomServiceItemView dsiv_dhys = holder.getView(R.id.dsiv_dhys);
        HomeRoomServiceItemView dsiv_yyzz = holder.getView(R.id.dsiv_yyzz);
        HomeRoomServiceItemView dsiv_consulting_room_online = holder.getView(R.id.dsiv_consulting_room_online);
        dsiv_consulting_room_online.setIconRes(R.drawable.blank);
        viewList.add(dsiv_wtgc);
        viewList.add(dsiv_imwd);
        viewList.add(dsiv_jtys);
        viewList.add(dsiv_dhys);
        viewList.add(dsiv_yyzz);
        viewList.add(dsiv_consulting_room_online);

        List<MyPurseGVItemBean> myPurseGVItemDatas = myPurseItemBean.getHomeGVItemDatas();
        for (int i = 0; i < myPurseGVItemDatas.size(); i++) {
            fillDataForServiceItemView(viewList.get(i), myPurseGVItemDatas.get(i));
        }
        currentMonth = myPurseItemBean.current_month;
        dsiv_wtgc.setOnTouchListener(this);
        dsiv_imwd.setOnTouchListener(this);
        dsiv_jtys.setOnTouchListener(this);
        dsiv_dhys.setOnTouchListener(this);
        dsiv_yyzz.setOnTouchListener(this);
    }

    private void fillDataForServiceItemView(final HomeRoomServiceItemView homeRoomServiceItemView, final MyPurseGVItemBean innerItem) {
        //提醒数
        homeRoomServiceItemView.setName(innerItem.getName(), context.getResources().getDimensionPixelSize(R.dimen.text_13sp));
        homeRoomServiceItemView.setState(innerItem.getDesc(),context.getResources().getDimensionPixelSize(R.dimen.text_14sp));
        homeRoomServiceItemView.setOpenTvTextColor(context.getResources().getColor(R.color.color_ff9901));

        if (innerItem.getUnreadMsgCount() <= 0) {
            homeRoomServiceItemView.setNumberTvVisibility(false);
        } else {
            homeRoomServiceItemView.setNumberTvVisibility(true);
            homeRoomServiceItemView.setNumberText(innerItem.getUnreadMsgCount());
        }
        homeRoomServiceItemView.setIconRes(innerItem.getIconResId());

        homeRoomServiceItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == innerItem) {
                    LogUtils.e("item为空");
                    return;
                }

                if ("问题广场".equals(innerItem.getName())) {
                    StatisticalTools.eventCount(context, "incomeWTGC");
                    goToBillDetailPage(innerItem.getName(),currentMonth,2);
                } else if (context.getString(R.string.online_consultation).equals(innerItem.getName())) {
                    StatisticalTools.eventCount(context, "incomeJSWD");
                    goToBillDetailPage(innerItem.getName(),currentMonth,1);
                } else if ("家庭医生".equals(innerItem.getName())) {
                    StatisticalTools.eventCount(context, "incomeJTYS");
                    goToBillDetailPage(innerItem.getName(),currentMonth,3);
                } else if ("电话医生".equals(innerItem.getName())) {
                    StatisticalTools.eventCount(context, "incomeDHYS");
                    goToBillDetailPage(innerItem.getName(),currentMonth,4);
                } else if ("预约转诊".equals(innerItem.getName())) {
                    //预约转诊的目前是免费的，所以，不用查看收入明细
                }  else if (Constants.OTHER.equals(innerItem.getName())) {
                    StatisticalTools.eventCount(context, "incomeQT");
                    goToBillDetailPage(innerItem.getName(),currentMonth,5);
                } else if (Constants.CONSULTING_ROOM_ONLINE.equals(innerItem.getName())) {
                    if (!TextUtils.isEmpty(YMApplication.getLoginInfo().getData().getZxzhsh())&&
                            "1".equals(YMApplication.getLoginInfo().getData().getZxzhsh())) {
                        goToBillDetailPage(innerItem.getName(), currentMonth, 6);
                    }else{
                        ToastUtils.longToast("您暂未开通"+innerItem.getName());
                    }
                }else {
                    ToastUtils.shortToast("无效跳转");
                }
            }
        });
    }

    private void goToBillDetailPage(String title,int month,int type) {
        Intent intent = new Intent(context,BillDetailActivity.class);
        intent.putExtra(Constants.INTENT_KEY_TITLE,title);
        intent.putExtra(Constants.INTENT_KEY_MONTH,month);
        intent.putExtra(Constants.INTENT_KEY_TYPE,type);
        context.startActivity(intent);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        HomeRoomServiceItemView serviceItemView = (HomeRoomServiceItemView) view;
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                serviceItemView.setBackgroundColor(context.getResources().getColor(R.color.color_fafafa));
                if ("问题广场".equals(serviceItemView.getName())) {
                    serviceItemView.setIconRes(R.drawable.wtgc_income);
                } else if (context.getString(R.string.online_consultation).equals(serviceItemView.getName())) {
                    serviceItemView.setIconRes(R.drawable.imwd_income);
                } else if ("家庭医生".equals(serviceItemView.getName())) {
                    serviceItemView.setIconRes(R.drawable.jtys_income);
                } else if ("电话医生".equals(serviceItemView.getName())) {
                    serviceItemView.setIconRes(R.drawable.dhys_income);
                } else if ("预约转诊".equals(serviceItemView.getName())) {
                    //预约转诊的目前是免费的，所以，不用查看收入明细
                }  else if(Constants.OTHER.equals(serviceItemView.getName())){
                    serviceItemView.setIconRes(R.drawable.other_income);
                }
                break;
            case MotionEvent.ACTION_UP:
                serviceItemView.setBackgroundColor(context.getResources().getColor(R.color.white));
                if ("问题广场".equals(serviceItemView.getName())) {
                    serviceItemView.setIconRes(R.drawable.wtgc_income_normal);
                } else if (context.getString(R.string.online_consultation).equals(serviceItemView.getName())) {
                    serviceItemView.setIconRes(R.drawable.imwd_income_normal);
                } else if ("家庭医生".equals(serviceItemView.getName())) {
                    serviceItemView.setIconRes(R.drawable.jtys_income_normal);
                } else if ("电话医生".equals(serviceItemView.getName())) {
                    serviceItemView.setIconRes(R.drawable.dhys_income_normal);
                } else if ("预约转诊".equals(serviceItemView.getName())) {
                    //预约转诊的目前是免费的，所以，不用查看收入明细
                }  else if(Constants.OTHER.equals(serviceItemView.getName())) {
                    serviceItemView.setIconRes(R.drawable.other_income_normal);
                }
                break;
            default:
                serviceItemView.setBackgroundColor(context.getResources().getColor(R.color.white));
                if ("问题广场".equals(serviceItemView.getName())) {
                    serviceItemView.setIconRes(R.drawable.wtgc_income_normal);
                } else if (context.getString(R.string.online_consultation).equals(serviceItemView.getName())) {
                    serviceItemView.setIconRes(R.drawable.imwd_income_normal);
                } else if ("家庭医生".equals(serviceItemView.getName())) {
                    serviceItemView.setIconRes(R.drawable.jtys_income_normal);
                } else if ("电话医生".equals(serviceItemView.getName())) {
                    serviceItemView.setIconRes(R.drawable.dhys_income_normal);
                } else if ("预约转诊".equals(serviceItemView.getName())) {
                    //预约转诊的目前是免费的，所以，不用查看收入明细
                }  else if(Constants.OTHER.equals(serviceItemView.getName())) {
                    serviceItemView.setIconRes(R.drawable.other_income_normal);
                }
                break;
        }
        return false;
    }

}
