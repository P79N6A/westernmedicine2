package com.xywy.askforexpert.module.drug;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.model.LoginInfo;
import com.xywy.askforexpert.module.drug.adapter.DrugRecipeDetailAdapter;
import com.xywy.askforexpert.module.drug.bean.BasisDoctorDiagnose;
import com.xywy.askforexpert.module.drug.bean.DiagnoseData;
import com.xywy.askforexpert.module.drug.bean.MyRrescriptionBean;
import com.xywy.askforexpert.module.drug.bean.PrescriptionMsg;
import com.xywy.askforexpert.module.drug.bean.RrescriptionBean;
import com.xywy.askforexpert.module.drug.bean.RrescriptionData;
import com.xywy.askforexpert.module.drug.request.DrugAboutRequest;
import com.xywy.base.XywyBaseFragment;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.util.TimeUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cn.org.bjca.sdk.core.bean.ResultBean;
import cn.org.bjca.sdk.core.kit.BJCASDK;
import cn.org.bjca.sdk.core.kit.PageNum;
import cn.org.bjca.sdk.core.values.ConstantParams;

import static com.xywy.askforexpert.module.drug.OnlineChatDetailActivity.ADD_PRESCRIPTION_CODE;

/**
 * 处方详情/确认处方
 * Created by xugan on 2018/7/9.
 */
public class PrescriptionActivityFragment extends XywyBaseFragment {

    private final String CONSTATNS = "com.xywy.medicine_super_market_dialogueDecreaseActivity";
    private final String DIALOGUE_CONSTANTS = "com.xywy.medicine_super_market_dialogueActivity";
    private TextView mRecipe_time, tv_doctor_name, mGender;
//    private EditText mRecipe_sick;
    private TextView mName, mAge;
    private Button mBtn_send;
    private RecyclerView mMedicineListView;
    private DrugRecipeDetailAdapter mMedicineListAdapter;
    private int mPatientId, mPatientAge, mSex, mDoctroId;
    private String mPatientName, mDoctorName, mDiagnosis;
    private JSONArray mJsonArray;
    private MedicineDataStateChanageReceiver receiver;
    private String[] medicine_time, take_method;
//    private final String RECIPE_SICK = "recipe_sick";
//    private final String RECIPE_DAY = "recipe_day";
    private String uid;
    private TextView tv_pay;
    private double price = 0;
    private String patientName;
    private String usersex;
    private String year;
    private String questionId;
    private String did = YMUserService.getCurUserId();
    private String month;
    private String day;
    private Button addMedicine;
    public String uniqueId;
    private String id;
    private LoginInfo loginInfo;
    private List<DiagnoseData> diagnosisList = new ArrayList<>();
    private TextView recipe_sick;
    private int DIAGNOSIS_CODE = 1;
    private LinearLayout diagnosis_ll;

    public PrescriptionActivityFragment() {
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_prescription;
    }

    @Override
    protected void beforeViewBind() {
    }

    @Override
    protected void bindView(View view) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(CONSTATNS);
        filter.addAction(DIALOGUE_CONSTANTS);
        receiver = new MedicineDataStateChanageReceiver();
        getActivity().registerReceiver(receiver, filter);
    }


    @Override
    protected void initView() {
        String patientSex = getArguments().getString(Constants.INTENT_KEY_SEX);
        patientName = getArguments().getString(Constants.INTENT_KEY_NAME);
        questionId = getArguments().getString(Constants.INTENT_KEY_QID);
        usersex = patientSex.equals("男")?"1":"2";
        year = getArguments().getString(Constants.INTENT_KEY_YEAR);
        month = getArguments().getString(Constants.INTENT_KEY_MONTH);
        day = getArguments().getString(Constants.INTENT_KEY_DAY);
        StringBuffer age = new StringBuffer();
        if (!year.equals("0")){
            age.append(year+"岁");
        }
        if (!month.equals("0")){
            age.append(month+"月");
        }
        if (!day.equals("0")){
            age.append(day+"天");
        }
        String department = getArguments().getString(Constants.INTENT_KEY_DEPARTMENT);
        uid = getArguments().getString(Constants.INTENT_KEY_UID);
        String myTime = getArguments().getString(Constants.INTENT_KEY_TIME);
        mName = (TextView) rootView.findViewById(R.id.name);
        mName.setText(patientName);
        mRecipe_time = (TextView) rootView.findViewById(R.id.recipe_time);
        mAge = (TextView) rootView.findViewById(R.id.age);
        mAge.setText(age);
        mGender = (TextView) rootView.findViewById(R.id.gender);
        mGender.setText(patientSex);
        TextView tv_section = (TextView) rootView.findViewById(R.id.tv_section);
        tv_section.setText("科室:"+department);
        String time = TimeUtils.getPhpStrDate(myTime, "yyyy-MM-dd");
        mRecipe_time.setText(time);

//        mRecipe_sick = (EditText) rootView.findViewById(R.id.recipe_sick);
//        //设置EditText的显示方式为多行文本输入
//        mRecipe_sick.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//        //文本显示的位置在EditText的最上方
//        mRecipe_sick.setGravity(Gravity.TOP);
//        //改变默认的单行模式
//        mRecipe_sick.setSingleLine(false);
//        //水平滚动设置为False
//        mRecipe_sick.setHorizontallyScrolling(false);
//        //设置光标的位置
//        mRecipe_sick.setSelection(mRecipe_sick.getText().toString().length());
        recipe_sick = (TextView) rootView.findViewById(R.id.recipe_sick);
        recipe_sick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (diagnosisList.size()==2){
                    Toast.makeText(getActivity(),"最多可选两种诊断",Toast.LENGTH_SHORT).show();
                } else {
                    startActivityForResult(new Intent(getActivity(),DiagnosisSearchActivity.class),DIAGNOSIS_CODE);
                }

            }
        });

        //设置医师的名称
        tv_doctor_name = (TextView) rootView.findViewById(R.id.tv_doctor_name);

        loginInfo = YMApplication.getLoginInfo();
        if (null != loginInfo) {
            tv_doctor_name.setText("医师:"+ loginInfo.getData().getRealname());
            mDoctroId = Integer.parseInt(loginInfo.getData().getPid());
            mDoctorName = loginInfo.getData().getRealname();
        }

        mBtn_send = (Button) rootView.findViewById(R.id.btn_send);
        mBtn_send.setText("发送处方");
        mBtn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 发送处方

                showNormalDialog();
            }
        });

        tv_pay = (TextView) rootView.findViewById(R.id.tv_pay);
        tv_pay.setText("药费:"+price);

        //添加药品
        addMedicine = (Button) rootView.findViewById(R.id.add_medicine);
        addMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到药品分类,KEY_VALUE传false，表示从开处方这个页面进入药品分类页面
                Intent intent = new Intent(getActivity(), MyPharmacyActivity.class);
                intent.putExtra(Constants.KEY_VALUE, false);
                intent.putExtra("drugFlag",true);
                startActivity(intent);
            }
        });
        mMedicineListView = (RecyclerView) rootView.findViewById(R.id.recipe_medicine_list);
        mMedicineListAdapter = new DrugRecipeDetailAdapter(getActivity());

        mMedicineListView.setLayoutManager(new LinearLayoutManager(getContext()));
//        mMedicineListView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

        mMedicineListAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (view != null && view.getTag() != null && view.getTag() instanceof RrescriptionBean) {
                    RrescriptionBean rescriptionBean = (RrescriptionBean) view.getTag();
                    startActivity(new Intent(getActivity(),DrugSettingActivity.class).
                            putExtra("id",rescriptionBean.getId()));
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        diagnosis_ll = (LinearLayout) rootView.findViewById(R.id.diagnosis_ll);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMedicineListAdapter.setData(RrescriptionData.getInstance().getMedicineList());
        mMedicineListView.setAdapter(mMedicineListAdapter);
        List<RrescriptionBean> list = RrescriptionData.getInstance().getMedicineList();
        price = 0;
        for (RrescriptionBean rrescriptionBean : list){
            Integer num = Integer.valueOf(rrescriptionBean.getNum());
            for (int i =0;i<num;i++){
                price += Double.valueOf(rrescriptionBean.getWksmj());
                tv_pay.setText("药费:"+ price);
            }
        }
        addMedicine.setVisibility(list.size()==5?View.GONE:View.VISIBLE);
        if (RrescriptionData.getInstance().BACK_FLAG){
            RrescriptionData.getInstance().BACK_FLAG = false;
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void unBindView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            Toast.makeText(getActivity(), "用户取消操作", Toast.LENGTH_SHORT).show();
            return;
        } else if (requestCode == ConstantParams.ACTIVITY_SIGN_DATA) {// 签名返回码
            String result = data.getStringExtra(ConstantParams.KEY_SIGN_BACK);
            Gson gson = new Gson();
            ResultBean resultBean = gson.fromJson(result, ResultBean.class);
            if (resultBean != null && TextUtils.equals(resultBean.getStatus(), ConstantParams.SUCCESS)) {
                Toast.makeText(getActivity(), "处方发送成功", Toast.LENGTH_SHORT).show();
                getActivity().setResult(ADD_PRESCRIPTION_CODE, new Intent().putExtra("id", id));
                getActivity().finish();
            } else {
                Toast.makeText(getActivity(), "签名失败!\n错误码：" + resultBean.getStatus() + "\n错误提示：" + resultBean.getMessage()
                        , Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == DIAGNOSIS_CODE){
            BasisDoctorDiagnose diagnose = (BasisDoctorDiagnose) data.getSerializableExtra("DiagnosisData");
            DiagnoseData dData = new DiagnoseData();
            dData.setId(diagnose.getId());
            dData.setDiagnosename(diagnose.getName());
            if (diagnosisList.size()==0){
                diagnosisList.add(dData);
            } else {
                for (int i=0;i<diagnosisList.size();i++){
                    if (!dData.getId().equals(diagnosisList.get(i).getId())){
                        diagnosisList.add(dData);
                        break;
                    }
                }
            }

            setDiagnosiData();

        }
    }

    private void setDiagnosiData(){
        diagnosis_ll.removeAllViews();
        for (int i = 0; i<diagnosisList.size();i++){
            View itemView = View.inflate(getActivity(), R.layout.diagnosis_item, null);
            TextView diagnosi_name = (TextView) itemView.findViewById(R.id.diagnosi_name);
            diagnosi_name.setText(diagnosisList.get(i).getDiagnosename());
            final int finalI = i;
            itemView.findViewById(R.id.diagnosi_iv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    diagnosisList.remove(finalI);
                    setDiagnosiData();
                }
            });
            diagnosis_ll.addView(itemView);
        }
    }

    @Override
    public void onDestroy() {
        if (null != receiver) {
            getActivity().unregisterReceiver(receiver);
        }
        if (!RrescriptionData.getInstance().getMedicineList().isEmpty()){
            RrescriptionData.getInstance().removeAllMedicine();
        }
        super.onDestroy();
    }

    class MedicineDataStateChanageReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(CONSTATNS)) {
                Bundle b = intent.getExtras();
                if (null != b) {
                    int productId = b.getInt("productId");
                    if (-1 != productId) {
                        RrescriptionData.getInstance().removeMedicine(productId+"");
                        mMedicineListAdapter.setData(RrescriptionData.getInstance().getMedicineList());
                        mMedicineListView.setAdapter(mMedicineListAdapter);
                    }
                }
            } else if (intent.getAction().equals(DIALOGUE_CONSTANTS)) {
                //发送处方
            }
        }
    }


    private void sendPrescription(){
        if(BJCASDK.getInstance().existsCert(getActivity()) &&
                BJCASDK.getInstance().existsStamp(getActivity())) {
            if (RrescriptionData.getInstance().getMedicineList().size() != 0 &&
                    0 != diagnosisList.size()) {
                LoginInfo.UserData userData = loginInfo.getData();
                List<RrescriptionBean> data = RrescriptionData.getInstance().getMedicineList();
                List<MyRrescriptionBean> myData = new ArrayList<MyRrescriptionBean>();
                for (RrescriptionBean bean : data) {
                    MyRrescriptionBean myRrescriptionBean = new MyRrescriptionBean();
                    myRrescriptionBean.setDrug_unit(bean.getDrug_unit());
                    myRrescriptionBean.setId(bean.getId());
                    myRrescriptionBean.setNum(bean.getNum());
                    myRrescriptionBean.setRemark(bean.getRemark());
                    myRrescriptionBean.setTake_day(bean.getTake_day());
                    myRrescriptionBean.setTake_method(bean.getTake_method());
                    myRrescriptionBean.setTake_num(bean.getTake_num());
                    myRrescriptionBean.setTake_rate(bean.getTake_rate());
                    myRrescriptionBean.setTake_time(bean.getTake_time());
                    myRrescriptionBean.setTake_unit(bean.getTake_unit());
                    myData.add(myRrescriptionBean);
                }
//                    String drugs = new Gson().toJson(RrescriptionData.getInstance().getMedicineList());
                String drugs = new Gson().toJson(myData);
                String diagnosis = new Gson().toJson(diagnosisList);
                if (TextUtils.isEmpty(uniqueId)) {
                    DrugAboutRequest.getInstance().postPrescriptionAdd(did, userData.getRealname(), uid, patientName, usersex,
                            year, diagnosis, drugs, questionId, month, day).subscribe(new BaseRetrofitResponse<BaseData<PrescriptionMsg>>() {
                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(BaseData<PrescriptionMsg> entry) {
                            if (entry.getCode() == 10000) {
                                uniqueId = entry.getData().getUniqueId();
                                id = entry.getData().getId();
                                BJCASDK.getInstance().signRecipe(getActivity(), Constants.YWQ_CLIENTID, uniqueId);
                            } else {
                                Toast.makeText(getActivity(), entry.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    BJCASDK.getInstance().signRecipe(getActivity(), Constants.YWQ_CLIENTID, uniqueId);
                }
            } else {
                Toast.makeText(getActivity(), "请填写初步诊断/请添加药品", Toast.LENGTH_SHORT).show();
            }
        }else{
            if (!BJCASDK.getInstance().existsCert(getActivity())){
                BJCASDK.getInstance().startUrl(getActivity(), Constants.YWQ_CLIENTID, PageNum.CERT_DOWN);
                Toast.makeText(getActivity(), "请下载证书", Toast.LENGTH_SHORT).show();
            }
            if (!BJCASDK.getInstance().existsStamp(getActivity())){
                BJCASDK.getInstance().startUrl(getActivity(), Constants.YWQ_CLIENTID, PageNum.SET_STAMP);
                Toast.makeText(getActivity(), "请下设置签章", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void showNormalDialog(){
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(getActivity());
        normalDialog.setTitle("温馨提示");
        normalDialog.setMessage("如处方信息无误，点击\"确认\"后处方将签字生效");
        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                sendPrescription();
                dialog.dismiss();
            }
        });
        normalDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        normalDialog.show();
    }
}
