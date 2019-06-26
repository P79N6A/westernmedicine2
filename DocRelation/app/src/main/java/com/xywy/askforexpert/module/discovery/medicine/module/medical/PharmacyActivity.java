package com.xywy.askforexpert.module.discovery.medicine.module.medical;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.module.discovery.medicine.config.OffLineService;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.MedicalCategoryEntity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.PharmacyEntity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.request.MedicineRequest;
import com.xywy.askforexpert.module.discovery.medicine.view.GridSpacingItemDecoration;
import com.xywy.base.XywyBaseActivity;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.uilibrary.recyclerview.adapter.DividerItemDecoration;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;
/**
 * 药方页
 * stone
 * 2017/10/27 下午7:35
 */
public class PharmacyActivity extends XywyBaseActivity {

    private RecyclerView mPharmacyRecyclerView;
    private PharmacyAdapter mPharmacyAdapter;
    private RecyclerView mPharmacySecondRecyclerView;
    private PharmacySecondAdapter mPharmacySecondAdapter;
    private int mLastPosition = -1;

    List<PharmacyEntity> mPharmacyList = new ArrayList<>();

    private EditText mSearch;
    private View mBack;
    private boolean mIsCommonMedicine = false;//是否是常用药

    private static final String IS_FROM = "isFrom";
    private String mIsFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);
        //初始化统一bar，将系统栏的背景设置成透明色
        CommonUtils.initSystemBar(this);
        Intent intent = getIntent();
        if(null != intent){
            mIsFrom = intent.getStringExtra(IS_FROM);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initData();
        initView();
    }
    public static void start(Context context) {
        Intent startIntent = new Intent(context, PharmacyActivity.class);
        context.startActivity(startIntent);
    }
    private void initView() {
        hideCommonBaseTitle();

        mBack = findViewById(R.id.back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSearch = (EditText) findViewById(R.id.pharmacy_search);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PharmacyActivity.this, SearchActivity.class);
                intent.putExtra(IS_FROM,mIsFrom);
                startActivity(intent);
            }
        });

        mPharmacyRecyclerView = (RecyclerView) findViewById(R.id.pharmacy_main_category_view);

        mPharmacyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPharmacyRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        mPharmacyAdapter = new PharmacyAdapter(this);
        mPharmacyAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if(mLastPosition != -1 && mPharmacyList.size()>mLastPosition && mPharmacyList.get(mLastPosition)!=null) {
                    mPharmacyList.get(mLastPosition).setSelected(false);
                }

                mLastPosition = position;
                mPharmacyList.get(position).setSelected(true);
                mPharmacyAdapter.notifyDataSetChanged();
                if(mPharmacyList.get(position).getSon()!=null) {
                    mPharmacySecondAdapter.setData(mPharmacyList.get(position).getSon());
                    mPharmacySecondAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        mPharmacyAdapter.setData(mPharmacyList);
        mPharmacyRecyclerView.setAdapter(mPharmacyAdapter);

        mPharmacySecondRecyclerView = (RecyclerView) findViewById(R.id.pharmacy_second_category_view);

        mPharmacySecondRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

        mPharmacySecondRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2,4,false));

        mPharmacySecondAdapter = new PharmacySecondAdapter(this);

        mPharmacySecondAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if(view!=null && view.getTag()!=null && view.getTag() instanceof MedicalCategoryEntity) {
                    MedicalCategoryEntity entry = (MedicalCategoryEntity) view.getTag();
                    if(BuildConfig.service_id == 1){
                        if("我的常用药".equals(entry.getName())) {
                            //表明点击的是常用药
                            mIsCommonMedicine = true;
                        }else {
                            mIsCommonMedicine = false;
                        }
                    }else {
                        mIsCommonMedicine = false;
                    }
                    MedicineListActivity.startActivity(PharmacyActivity.this, entry.getId(),mIsCommonMedicine,mIsFrom);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        mPharmacySecondRecyclerView.setAdapter(mPharmacySecondAdapter);
    }

    private void initData() {
        MedicineRequest.getInstance().getMedicineCategory().subscribe(new BaseRetrofitResponse<BaseData<List<PharmacyEntity>>>(){
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
                if(entry!=null && entry.getData()!=null) {
                    if(OffLineService.isOffLine()){
                        mPharmacyList.clear();
                        mPharmacyList.add(0,entry.getData().get(0));//我的常用药
                        PharmacyEntity pharmacyEntity = new PharmacyEntity();
                        pharmacyEntity.setName("明星药品");
                        List<MedicalCategoryEntity> son = new ArrayList<MedicalCategoryEntity>();
                        MedicalCategoryEntity medicalCategoryEntity = new MedicalCategoryEntity();
                        medicalCategoryEntity.setName("重点推荐");
                        medicalCategoryEntity.setId(-2);//这里由于明星药品的数据都是本地添加的，所以id就随意的设定了一个值
                        son.add(0,medicalCategoryEntity);
                        pharmacyEntity.setSon(son);
                        mPharmacyList.add(pharmacyEntity);
                        for (int i = 1; i < entry.getData().size(); i++) {
                            mPharmacyList.add((i+1),entry.getData().get(i));
                        }
                    }else {
                        mPharmacyList = entry.getData();
                    }
                    mPharmacyAdapter.setData(mPharmacyList);
                    mPharmacyAdapter.notifyDataSetChanged();
                    if(mPharmacyList.size()>0){
                        if(mPharmacyList.get(0).getSon()!=null) {
                            mPharmacyList.get(0).setSelected(true);
                            mLastPosition = 0;
                            mPharmacySecondAdapter.setData(mPharmacyList.get(0).getSon());
                            mPharmacySecondAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

}
