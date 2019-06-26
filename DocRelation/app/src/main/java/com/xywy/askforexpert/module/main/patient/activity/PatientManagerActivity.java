package com.xywy.askforexpert.module.main.patient.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.model.PatienTtitle;
import com.xywy.askforexpert.model.PatientManagerData;
import com.xywy.askforexpert.module.consult.ServiceProvider;
import com.xywy.askforexpert.module.main.patient.adapter.PatientManagerAdapter;
import com.xywy.askforexpert.widget.view.SelectBasePopupWindow;
import com.xywy.uilibrary.recyclerview.wrapper.CustomRecyclerViewLoadMoreWrapper;
import com.xywy.uilibrary.titlebar.ItemClickListener;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by jason on 2018/8/20.
 */

public class PatientManagerActivity extends YMBaseActivity {

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.pharmacy_record_recycler)
    RecyclerView pharmacy_record_recycler;
    @OnClick(R.id.pharmacy_search) void search(){
        startActivity(new Intent(this,PatientSearchActivity.class).
                putExtra("PatientList",childList).
                putExtra("localFlag",false));
    }
    private PatientManagerAdapter patientManagerAdapter;
    private EmptyWrapper mEmptyWrapper;
    private CustomRecyclerViewLoadMoreWrapper mLoadMoreWrapper;
    private String doctorId = YMUserService.getCurUserId();
    private int page = 1;
    private ArrayList<PatienTtitle> listData = new ArrayList<>();
    private ArrayList<PatienTtitle> childList = new ArrayList<>();
    private SelectBasePopupWindow mPopupWindow;
    private View lay1;
    private View lay2;
    private View lay3;
    private View lay4;
    private View popRoot;

    @Override
    protected void initView() {
//        showLoadDataDialog();
        titleBarBuilder.setTitleText("报到患者");
        titleBarBuilder.addItem("更多功能",new ItemClickListener() {
            @Override
            public void onClick() {
                showPop();
            }
        }).build();
        mSwipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=1;
                loadData();

            }
        });
        pharmacy_record_recycler.setLayoutManager(new LinearLayoutManager(this));
        patientManagerAdapter = new PatientManagerAdapter(this,false);
        patientManagerAdapter.setData(listData);
        mEmptyWrapper = new EmptyWrapper(patientManagerAdapter);
        mLoadMoreWrapper = new CustomRecyclerViewLoadMoreWrapper(mEmptyWrapper, pharmacy_record_recycler);
        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                loadData();
            }
        });
        mLoadMoreWrapper.loadDataFailed();
        pharmacy_record_recycler.setAdapter(mLoadMoreWrapper);


    }

    @Override
    protected void initData() {
        loadData();
    }

    private void loadData(){
        ServiceProvider.getPatientManagerList(page,20,doctorId, new Subscriber<PatientManagerData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (page>1){
                    page--;
                }
                mSwipeRefreshLayout.setRefreshing(false);
                mLoadMoreWrapper.loadDataFailed();
                mLoadMoreWrapper.notifyDataSetChanged();
//                hideProgressDialog();
            }

            @Override
            public void onNext(PatientManagerData entry) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (0==entry.getData().getTotal()){
                    mLoadMoreWrapper.loadDataFailed();
                }else {
                    mLoadMoreWrapper.showLoadMore();
                }
                if (page==1){
//                    hideProgressDialog();
                    listData.clear();
                    childList.clear();
                }
                ArrayList<PatienTtitle> data = new ArrayList();
                for (int i=0;i<entry.getData().getData().size();i++){
                    PatienTtitle patienListTitle = entry.getData().getData().get(i);
                    patienListTitle.setItemFlag(1);
                    listData.add(patienListTitle);
                    data.add(patienListTitle);
                    for (int j=0;j<entry.getData().getData().get(i).getPatien_list().size();j++){

                        PatienTtitle patienListContent = entry.getData().getData().get(i).getPatien_list().get(j);
                        patienListContent.setItemFlag(2);
                        listData.add(patienListContent);
                        data.add(patienListTitle);
                        childList.add(patienListContent);
                    }
                }
                if (page==1){
                    patientManagerAdapter.setData(listData);
                }else{
                    patientManagerAdapter.addData(data);
                }
                mLoadMoreWrapper.notifyDataSetChanged();
            }
        });
    }

    private void showPop() {
        if (mPopupWindow == null) {
            mPopupWindow = new SelectBasePopupWindow(true, this);
            popRoot = View.inflate(getBaseContext(), R.layout.pop_layout_patient_manager, null);
            lay1 = popRoot.findViewById(R.id.lay1);
            lay2 = popRoot.findViewById(R.id.lay2);
            lay3 = popRoot.findViewById(R.id.lay3);
            lay4 = popRoot.findViewById(R.id.lay4);
            lay1.setOnClickListener(mPopOnClickListener);
            lay2.setOnClickListener(mPopOnClickListener);
            lay3.setOnClickListener(mPopOnClickListener);
            lay4.setOnClickListener(mPopOnClickListener);
        }
        if (!mPopupWindow.isShowing()) {
            mPopupWindow.init(popRoot).showAtLocation(this.getWindow().getDecorView(), Gravity.TOP | Gravity.RIGHT, AppUtils.dpToPx(5, getResources()), AppUtils.dpToPx(48, getResources()) + YMApplication.getStatusBarHeight());
        }
    }

    private View.OnClickListener mPopOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mPopupWindow.dismiss();
            switch (view.getId()) {
                case R.id.lay1:
                    startActivity(new Intent(PatientManagerActivity.this,InvitePatientQRCodeActivity.class));
                    break;
                case R.id.lay2:
                    startActivity(new Intent(PatientManagerActivity.this,GroupingActivity.class));
                    break;
                case R.id.lay3:
                    startActivity(new Intent(PatientManagerActivity.this,BatchNoticeActivity.class));
                    break;
                case R.id.lay4:
                    startActivity(new Intent(PatientManagerActivity.this,AddressActivity.class));
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected int getLayoutResId() {
        return R.layout.patient_manager_layout;
    }
}
