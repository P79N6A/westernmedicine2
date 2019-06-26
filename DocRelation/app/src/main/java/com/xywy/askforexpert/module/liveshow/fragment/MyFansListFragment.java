package com.xywy.askforexpert.module.liveshow.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseFragment;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.net.rxretrofitoktools.service.WWSXYWYService;
import com.xywy.askforexpert.model.liveshow.MyFansBean;
import com.xywy.askforexpert.model.liveshow.MyFansPageBean;
import com.xywy.askforexpert.module.liveshow.adapter.MyFansAdapter;
import com.xywy.uilibrary.recyclerview.adapter.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 我的粉丝
 * Created by bailiangjin on 2017/2/23.
 */

public class MyFansListFragment extends YMBaseFragment {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    MyFansAdapter myFansAdapter;

    List<MyFansBean> fansList= new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_live_show_my_fans_list;
    }

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if(isResumed()){
            showLoadDataDialog();
        }

        WWSXYWYService.getMyFansPageInfo(YMUserService.getCurUserId(), new CommonResponse<MyFansPageBean>() {
//        WWSXYWYService.getMyFansPageInfo(YMUserService.getCurUserId(), new CommonResponse<MyFansPageBean>() {
            @Override
            public void onNext(MyFansPageBean myFansPageBean) {
                hideProgressDialog();
                fansList.clear();
                fansList.addAll(myFansPageBean.getData());
                if (null == myFansAdapter) {
                    myFansAdapter = new MyFansAdapter(getActivity());
                    myFansAdapter.setData(fansList);
                    recyclerView.setAdapter(myFansAdapter);
                } else {
                    myFansAdapter.setData(fansList);
                    myFansAdapter.notifyDataSetChanged();
                }


            }
        });

    }

    @Override
    public String getStatisticalPageName() {
        return null;
    }
}
