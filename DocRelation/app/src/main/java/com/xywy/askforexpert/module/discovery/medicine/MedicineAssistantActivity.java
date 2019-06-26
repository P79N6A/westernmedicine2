package com.xywy.askforexpert.module.discovery.medicine;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.module.discovery.medicine.adapter.MedicineBaseAdapter;
import com.xywy.askforexpert.module.discovery.medicine.adapter.entity.LastestMedicineRecordListItem;
import com.xywy.askforexpert.module.discovery.medicine.adapter.entity.LastestPatientListItem;
import com.xywy.askforexpert.module.discovery.medicine.adapter.entity.MedicineItemBean;
import com.xywy.askforexpert.module.discovery.medicine.common.MyRxBus;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.BindingDelegateActivity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.PharmacyActivity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.PharmacyRecordActivity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.PharmacyRecordEntity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.request.MedicineRequest;
import com.xywy.askforexpert.module.discovery.medicine.module.patient.PatientListActivity;
import com.xywy.askforexpert.module.discovery.medicine.module.patient.entity.Patient;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.activity.PersonalCardActivity;
import com.xywy.askforexpert.module.main.diagnose.Patient_Group_ManagerActivity;
import com.xywy.askforexpert.module.main.diagnose.Patient_ServerGoneActiviy;
import com.xywy.askforexpert.widget.ActionItem;
import com.xywy.askforexpert.widget.TitlePopup;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.retrofit.rxbus.EventSubscriber;
import com.xywy.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xgxg on 2017/10/18.
 * 药品助手
 */

public class MedicineAssistantActivity extends YMBaseActivity implements View.OnClickListener, TitlePopup.OnItemOnClickListener {
    private TitlePopup menuPopup;
    private static final String INVITE_PATIENT = "邀请患者";
    private static final String ADD_XY_PATIENT = "添加寻医患者";
    private static final String MANAGER_GROUP = "分组管理";
    private static final String BINDE_AGENT = "邀请码";
    private static final String IS_FROM = "isFrom";
    private static final String IS_FROM_VALUE = "MedicineAssistantActivity";
    private RecyclerView mRecyclerView;
    private MedicineBaseAdapter mMedicineItemAdapter;
    private List<MedicineItemBean> itemList = new ArrayList<>();
    private boolean mBackGround = true;//当前activity是否在前台

    @Override
    protected void initView() {
        //监听到系统消息刷新请求最近咨询患者信息
        MyRxBus.registerLatestPatientInfoChanged(new EventSubscriber() {
            @Override
            public void onNext(Object o) {
                LogUtils.i("收到新的最近咨询患者信息");
                mBackGround = true;
                getLastestPatientListItem(mBackGround);
            }
        },this);
        hideCommonBaseTitle();
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        LinearLayout ll_patient = (LinearLayout) findViewById(R.id.ll_patient);
        menuPopup = new TitlePopup(this, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        menuPopup.setItemOnClickListener(this);

        menuPopup.cleanAction();
        menuPopup.addAction(new ActionItem(this, INVITE_PATIENT));
        menuPopup.addAction(new ActionItem(this, ADD_XY_PATIENT));
        menuPopup.addAction(new ActionItem(this, MANAGER_GROUP));
        menuPopup.addAction(new ActionItem(this, BINDE_AGENT));
        ImageView patient_add = (ImageView) findViewById(R.id.patient_add);
        patient_add.setOnClickListener(this);
        LinearLayout ll_pharmacy = (LinearLayout) findViewById(R.id.ll_pharmacy);
        LinearLayout ll_record = (LinearLayout) findViewById(R.id.ll_record);
        ll_patient.setOnClickListener(this);
        ll_pharmacy.setOnClickListener(this);
        ll_record.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMedicineItemAdapter = new MedicineBaseAdapter(this);
        mRecyclerView.setAdapter(mMedicineItemAdapter);
    }

    @Override
    protected void initData() {
        mBackGround = false;
        getLastestPatientListItem(mBackGround);

//        mMedicineItemAdapter.setData(itemList);
//        mMedicineItemAdapter.notifyDataSetChanged();
    }

    private void getLastestRecordListItem(boolean backGround) {
        if (!TextUtils.isEmpty(YMApplication.getLoginInfo().getData().getPid())) {
            int doctorId = Integer.parseInt(YMApplication.getLoginInfo().getData().getPid());
            LogUtils.i("mDoctorId=" + doctorId);
            String keyword = "";
            int page = 1;
            int pageSize = 10;
            getData(doctorId, keyword, page, pageSize,backGround);
        }

//        List<LastestMedicineRecordInnerItem> innerItemList = new ArrayList<>();
//        LastestMedicineRecordInnerItem lastestMedicineRecordInnerItem = new LastestMedicineRecordInnerItem();
//        LastestMedicineRecordInnerItem.Drug drug = lastestMedicineRecordInnerItem.new Drug("1", "张三", R.drawable.ic_annot,
//                "海力生", "步步脑心通0.4g*36粒/盒", "每日一次",
//                "1", "饭后", 1,
//                0);
//        lastestMedicineRecordInnerItem.setDrug_list(drug);
//        innerItemList.add(lastestMedicineRecordInnerItem);
//
//        LastestMedicineRecordInnerItem lastestMedicineRecordInnerItem2 = new LastestMedicineRecordInnerItem();
//        LastestMedicineRecordInnerItem.Drug drug2 = lastestMedicineRecordInnerItem2.new Drug("2", "只能怪胡", R.drawable.add,
//                "海力生", "步步脑心通0.4g*36粒/盒", "每日一次",
//                "1", "饭后", 1,
//                0);
//        lastestMedicineRecordInnerItem2.setDrug_list(drug2);
//        innerItemList.add(lastestMedicineRecordInnerItem2);
//        return new LastestMedicineRecordListItem(innerItemList);
    }

    private void getData(int doctorId, final String keyword, int page, int pagesize,final boolean backGround) {
        MedicineRequest.getInstance().getPharmacyRecord(doctorId, keyword, page, pagesize).subscribe(new BaseRetrofitResponse<BaseData<List<PharmacyRecordEntity>>>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                if(!backGround){
                    hideProgressDialog();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if(!backGround){
                    hideProgressDialog();
                }
            }

            @Override
            public void onNext(BaseData<List<PharmacyRecordEntity>> entry) {
                super.onNext(entry);
                if (entry != null && entry.getData() != null) {
                    int size = entry.getData().size();
                    if(size>=2){
                        size = 2;
                    }

                    List<PharmacyRecordEntity> innerItemList = new ArrayList<PharmacyRecordEntity>();
                    for (int i = 0; i < size; i++) {
                        innerItemList.add(entry.getData().get(i));
                    }
                    LastestMedicineRecordListItem lastestMedicineRecordListItem = new LastestMedicineRecordListItem(innerItemList);
                    itemList.add(new MedicineItemBean(MedicineItemBean.TYPE_MEDICINE_RECORD, lastestMedicineRecordListItem));
                    mMedicineItemAdapter.setData(itemList);
                    mMedicineItemAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getLastestPatientListItem(final boolean backGround) {
        long doctroId = Long.parseLong(YMUserService.getCurUserId());
        int pageSize = 8;
        GetRecentPatientRequest.getInstance().getRecentPatient(doctroId,pageSize).subscribe(new BaseRetrofitResponse<BaseData<List<Patient>>>(){
            @Override
            public void onStart() {
                super.onStart();
                if(!backGround){
                    showProgressDialog("");
                }

            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if(!backGround){
                    hideProgressDialog();
                }

            }

            @Override
            public void onNext(BaseData<List<Patient>> entry) {
                super.onNext(entry);
                if(entry!=null) {
                    dealwithEntry(entry);
                }
            }
        });
    }

    private void dealwithEntry(BaseData<List<Patient>> entry) {
        if(null != entry && entry.getCode()==10000){
            itemList.clear();
            itemList.add(new MedicineItemBean(MedicineItemBean.TYPE_LIST_PATIENT, new LastestPatientListItem(entry.getData())));
            getLastestRecordListItem(mBackGround);
        }

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_medicine_assistant;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.patient_add:
                menuPopup.show(v);
                break;
            case R.id.ll_patient:
                StatisticalTools.eventCount(MedicineAssistantActivity.this, "YPMypatients");
                startActivity(new Intent(MedicineAssistantActivity.this, PatientListActivity.class));
                break;
            case R.id.ll_pharmacy:
                StatisticalTools.eventCount(MedicineAssistantActivity.this, "YPMypharmacy");
                Intent intent = new Intent(MedicineAssistantActivity.this, PharmacyActivity.class);
                intent.putExtra(IS_FROM,IS_FROM_VALUE);
                startActivity(intent);
                break;
            case R.id.ll_record:
                StatisticalTools.eventCount(MedicineAssistantActivity.this, "YPmedicationrecord");
                startActivity(new Intent(MedicineAssistantActivity.this, PharmacyRecordActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(ActionItem item, int position) {
        String mTitle = (String) item.mTitle;
        Intent intent;
        switch (mTitle) {
            // 邀请患者
            case INVITE_PATIENT:
                StatisticalTools.eventCount(this, "YPinvitepatients");
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(this).context);
                } else {
                    //跳转到医生二维码页面
                    intent = new Intent(this, PersonalCardActivity.class);
                    startActivity(intent);
//                    if (UserManager.getInstance().isVerifiedDoc(MedicineAssistantActivity.this,true)) {
//                        //跳转到医生二维码页面
//                        intent = new Intent(this, PersonalCardActivity.class);
//                        startActivity(intent);
//                    }

                }
                break;
            // 添加寻医患者
            case ADD_XY_PATIENT:
                StatisticalTools.eventCount(this, "YPaddxypatients");
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(this).context);
                } else {
                    intent = new Intent(this, Patient_ServerGoneActiviy.class);
                    startActivity(intent);
                }
                break;
            // 分组管理
            case MANAGER_GROUP:
                StatisticalTools.eventCount(this, "YPgroupingmanagement");
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(this).context);
                } else {
                    intent = new Intent(MedicineAssistantActivity.this,
                            Patient_Group_ManagerActivity.class);
                    intent.putExtra("type", "gruoup");
                    startActivity(intent);
                }
                break;

            // 绑定代理
            case BINDE_AGENT:
                StatisticalTools.eventCount(this, "YPBindingagent");
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(this).context);
                } else {
                    BindingDelegateActivity.start(MedicineAssistantActivity.this);
                }
                break;
            default:
                break;
        }
    }

    //stone
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyRxBus.unRegisterLatestPatientInfoChanged(this);
    }
}
