package com.xywy.askforexpert.module.drug;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.SearchActivity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.MedicalCategoryEntity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.PharmacyEntity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.request.MedicineRequest;
import com.xywy.askforexpert.module.discovery.medicine.view.GridSpacingItemDecoration;
import com.xywy.askforexpert.module.drug.adapter.MedicineTypeOneAdapter;
import com.xywy.askforexpert.module.drug.adapter.MedicineTypeTwoAdapter;
import com.xywy.askforexpert.module.drug.bean.RrescriptionData;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.uilibrary.recyclerview.adapter.HSItemDecoration;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的药房 stone
 */
public class MyPharmacyActivity extends YMBaseActivity implements MedicineTypeOneAdapter.OneItemClick, MedicineTypeTwoAdapter.TwoItemClick {

    private boolean mIsCommonOpt;

    private int mLastPosition = -1;

    private RecyclerView mRecyclerView1; //展示一级列表
    private RecyclerView mRecyclerView2; //展示二级列表
    private View no_data; //无药品时显示

    private List<PharmacyEntity> mPharmacyList = new ArrayList<>();
    private List<MedicalCategoryEntity> mSonList = new ArrayList<>();

    private MedicineTypeOneAdapter adapterOne; //一级列表适配器
    private MedicineTypeTwoAdapter adapterTwo; //二级列表适配器

    @Override
    protected int getLayoutResId() {
        return R.layout.my_pharmacy_activity;
    }


    @Override
    protected void initView() {
        mIsCommonOpt = getIntent().getBooleanExtra(Constants.KEY_VALUE, false);

        titleBarBuilder.setTitleText("药品分类").addItem("搜索", new ItemClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(MyPharmacyActivity.this, SearchActivity.class);
                intent.putExtra("isFrom", false)
                        .putExtra("mIsCommonOpt",mIsCommonOpt)
                        .putExtra("drug",true);
                startActivity(intent);
            }
        }).build();

        mRecyclerView1 = (RecyclerView) findViewById(R.id.lv_my_pharmacy_medicine_list_one);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView1.setLayoutManager(linearLayoutManager);
        mRecyclerView1.addItemDecoration(new HSItemDecoration(this, R.color.app_common_divider_color));

        mRecyclerView2 = (RecyclerView) findViewById(R.id.gv_my_pharmacy_medicine_list_two);
        //设置布局管理器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView2.setLayoutManager(gridLayoutManager);
        mRecyclerView2.addItemDecoration(new GridSpacingItemDecoration(2, 0, false));

        adapterOne = new MedicineTypeOneAdapter(this, mPharmacyList);
        adapterTwo = new MedicineTypeTwoAdapter(this, mSonList);

        no_data = findViewById(R.id.tv_my_pharmacy_no_data);

        adapterOne.setOneItemClick(this);
        adapterTwo.setTwoItemClick(this);

        mRecyclerView1.setAdapter(adapterOne);
        mRecyclerView2.setAdapter(adapterTwo);
    }

    @Override
    protected void initData() {
        MedicineRequest.getInstance().getMedicineCategory().subscribe(new BaseRetrofitResponse<BaseData<List<PharmacyEntity>>>() {
            @Override
            public void onStart() {
                super.onStart();
                showProgressDialog("");
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                hideProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                hideProgressDialog();
            }

            @Override
            public void onNext(BaseData<List<PharmacyEntity>> entry) {
                super.onNext(entry);
                if (entry != null && entry.getData() != null) {

                    mPharmacyList = entry.getData();
                    adapterOne.refreshList(mPharmacyList);

                    if (mPharmacyList.size() > 0) {
                        if (mPharmacyList.get(0).getSon() != null) {
                            mPharmacyList.get(0).setSelected(true);
                            mLastPosition = 0;
                            adapterTwo.refresh(mPharmacyList.get(0).getSon());
                        }
                    }
                }
            }
        });
    }

    /**
     * 一级列表条目点击的接口回调
     *
     * @param position 第几条
     * @param one      bean
     */
    @Override
    public void oneItemClick(final int position, PharmacyEntity one) {
        showData();
        adapterTwo.showNoDataPage();

        if (mLastPosition != -1 && mPharmacyList.size() > mLastPosition && mPharmacyList.get(mLastPosition) != null) {
            mPharmacyList.get(mLastPosition).setSelected(false);
        }

        mLastPosition = position;
        mPharmacyList.get(position).setSelected(true);
        adapterOne.notifyDataSetChanged();

        if (mPharmacyList.get(position).getSon() != null && mPharmacyList.get(position).getSon().size() > 0) {
            adapterTwo.refresh(mPharmacyList.get(mLastPosition).getSon());
        } else {
            adapterTwo.refresh(mSonList);
            showNoData();
        }

    }

    @Override
    public void twoItemClick(int drugType, MedicalCategoryEntity typeTwo, int position, boolean isLast) {
        Intent intent = new Intent(this, CommonDrugListActivity.class);

        intent.putExtra(Constants.KEY_VALUE, mIsCommonOpt);//是否是常用药操作 否则就是加入处方操作
        intent.putExtra(Constants.KEY_CITY, String.valueOf(typeTwo.getId()));//分类id
        intent.putExtra(Constants.KEY_MESSAGE, typeTwo.getName());//标题
//        intent.putExtra(Constants.KEY_PROVINCE,mOne);
//        intent.putExtra("drugFlag",getIntent().getBooleanExtra("drugFlag",false));
        startActivity(intent);

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode== Activity.RESULT_OK&&resultCode==2){
//            finish();
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        if (RrescriptionData.getInstance().BACK_FLAG){
            finish();
        }
    }

    /**
     * 显示无数据页面
     */
    private void showNoData() {
        mRecyclerView2.setVisibility(View.GONE);
        no_data.setVisibility(View.VISIBLE);
    }

    /**
     * 显示页面
     */
    private void showData() {
        no_data.setVisibility(View.GONE);
        mRecyclerView2.setVisibility(View.VISIBLE);
    }

}
