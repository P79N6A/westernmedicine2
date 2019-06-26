package com.xywy.askforexpert.module.discovery.medicine.module.medical;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.model.LoginInfo;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.MedicineCartEntity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.MedicineEntity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.RecipeEntity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.request.MedicineRequest;
import com.xywy.askforexpert.module.discovery.medicine.module.patient.PatientManager;
import com.xywy.askforexpert.module.discovery.medicine.module.patient.entity.Patient;
import com.xywy.askforexpert.module.discovery.medicine.util.RegUtils;
import com.xywy.askforexpert.module.discovery.medicine.util.ToastUtils;
import com.xywy.askforexpert.module.discovery.medicine.view.AmountView;
import com.xywy.askforexpert.module.message.msgchat.ChatMainActivity;
import com.xywy.base.XywyBaseActivity;
import com.xywy.base.XywyBaseFragment;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.uilibrary.dialog.bottompopupdialog.BottomBtnPopupWindow;
import com.xywy.uilibrary.dialog.bottompopupdialog.listener.BtnClickListener;
import com.xywy.uilibrary.recyclerview.adapter.DividerItemDecoration;
import com.xywy.util.LogUtils;
import com.xywy.util.SharedPreferencesHelper;
import com.xywy.util.TimeUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 确认处方 stone
 * A placeholder fragment containing a simple view.
 */
public class RecipeDetailActivityFragment extends XywyBaseFragment {
    private final String CONSTATNS = "com.xywy.medicine_super_market_dialogueDecreaseActivity";
    private final String DIALOGUE_CONSTANTS = "com.xywy.medicine_super_market_dialogueActivity";
    private TextView mRecipe_time, mTv_doctor, mGender;
    private EditText mRecipe_sick, mName, mAge;
    private AmountView mAmountView;
    private Button mBtn_send;
    private RecyclerView mMedicineListView;
    private RecipeDetailAdapter mMedicineListAdapter;
    private int mPatientId, mPatientAge, mSex, mDoctroId, mPov = 90;
    private String mPatientName, mDoctorName, mDiagnosis;
    private JSONArray mJsonArray;
    private MedicineDataStateChanageReceiver receiver;
    private String[] medicine_time, take_method;
    private final String RECIPE_SICK = "recipe_sick";
    private final String RECIPE_DAY = "recipe_day";

    public RecipeDetailActivityFragment() {
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_recipe_detail;
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
        mName = (EditText) rootView.findViewById(R.id.name);
        mName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mName.setFocusableInTouchMode(true);
                return false;
            }
        });
        mName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkName(s.toString());
            }
        });
        mRecipe_time = (TextView) rootView.findViewById(R.id.recipe_time);
        mAge = (EditText) rootView.findViewById(R.id.age);
        mAge.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mAge.setFocusableInTouchMode(true);
                return false;
            }
        });
        mAge.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        mAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkAge(s.toString());
            }
        });
        mGender = (TextView) rootView.findViewById(R.id.gender);
        mRecipe_sick = (EditText) rootView.findViewById(R.id.recipe_sick);
        mRecipe_sick.setText(SharedPreferencesHelper.getIns(getActivity()).getString(RECIPE_SICK));
        //设置EditText的显示方式为多行文本输入
        mRecipe_sick.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        //文本显示的位置在EditText的最上方
        mRecipe_sick.setGravity(Gravity.TOP);
        //改变默认的单行模式
        mRecipe_sick.setSingleLine(false);
        //水平滚动设置为False
        mRecipe_sick.setHorizontallyScrolling(false);
        mRecipe_sick.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferencesHelper.getIns(getActivity()).putString(RECIPE_SICK, s.toString());
            }
        });
        //设置光标的位置
        mRecipe_sick.setSelection(mRecipe_sick.getText().toString().length());

        mAmountView = (AmountView) rootView.findViewById(R.id.amount_view);
        mAmountView.setGoods_storage(90);
        int oldMPov = SharedPreferencesHelper.getIns(getActivity()).getInt(RECIPE_DAY);
        if (-1 != oldMPov) {
            mPov = oldMPov;
        }
        mAmountView.setCount(mPov);
        mAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                mPov = amount;
                SharedPreferencesHelper.getIns(getActivity()).putInt(RECIPE_DAY, mPov);
            }
        });
        //设置医师的名称
        mTv_doctor = (TextView) rootView.findViewById(R.id.tv_doctor);

        LoginInfo loginInfo = YMApplication.getLoginInfo();
        if (null != loginInfo) {
            mTv_doctor.setText(loginInfo.getData().getRealname());
            mDoctroId = Integer.parseInt(loginInfo.getData().getPid());
            mDoctorName = loginInfo.getData().getRealname();
        }

        mBtn_send = (Button) rootView.findViewById(R.id.btn_send);
        mBtn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkName(mName.getText().toString()) && checkAge(mAge.getText().toString())) {
                    if (sendRecipeData()) {
                        //弹出一个确认是否发送处方的对话框
                        Intent intent = new Intent(getActivity(), DialogueActivity.class);
                        Bundle b = new Bundle();
                        b.putString("msg", "请仔细核对处方，发送后不可修改，确认发送?");
                        intent.putExtras(b);
                        getActivity().startActivity(intent);
                    }
                }
            }
        });

        //添加药品
        Button addMedicine = (Button) rootView.findViewById(R.id.add_medicine);
        addMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mMedicineListView = (RecyclerView) rootView.findViewById(R.id.recipe_medicine_list);
        mMedicineListAdapter = new RecipeDetailAdapter(getActivity());

        mMedicineListView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMedicineListView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

        mMedicineListAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (view != null && view.getTag() != null && view.getTag() instanceof MedicineCartEntity) {
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mMedicineListAdapter.setData(MedicineCartCenter.getInstance().getMedicineList());
        mMedicineListView.setAdapter(mMedicineListAdapter);
    }

    private boolean checkName(String name) {
        if (name.length() > 6 || name.length() < 2) {
            //stone 替换
            shortToast("请输入2~6位的姓名");
//            shortToast("请输入正确格式姓名");
            return false;
        }
        return true;
    }

    private boolean checkAge(String age) {
        if (RegUtils.isNumber(age)) {
            int len = age.length();
            if (len == 0 || len > 3) {
                shortToast("请输入正确的年龄");
                return false;
            } else {
                if (len > 1) {
                    String temp = age.substring(0, 1);
                    if ("0".equals(temp)) {
                        mAge.setText("0");
                        return false;
                    }
                    if (Integer.parseInt(age) > 199) {
                        shortToast("请输入正确的年龄");
                        return false;
                    }
                }
                return true;
            }
        } else {
            shortToast("请输入正确的年龄");
            return false;
        }
    }

    /**
     * 发送处方笺
     */
    private boolean sendRecipeData() {
        LogUtils.i("getMedicineList.size=" + MedicineCartCenter.getInstance().getMedicineList().size());
        Patient p = PatientManager.getInstance().getPatient();
        if (null != p) {
            mPatientId = Integer.parseInt(p.getUId());
            mPatientName = mName.getText().toString();
            if (TextUtils.isEmpty(mPatientName)) {
                shortToast("姓名不能为空");
                return false;
            }
            String tempAge = mAge.getText().toString();
            if (TextUtils.isEmpty(tempAge)) {
                shortToast("年龄不能为空");
                return false;
            }
            mPatientAge = Integer.parseInt(tempAge);
            //1.男 2女
            if ("男".equals(mGender.getText().toString().trim())) {
                mSex = 1;
            } else if ("女".equals(mGender.getText().toString().trim())) {
                mSex = 2;
            }
        }
        mDiagnosis = mRecipe_sick.getText().toString().trim();
        if (TextUtils.isEmpty(mDiagnosis)) {
            mDiagnosis = "";
        }

        final List<MedicineCartEntity> medicineList = MedicineCartCenter.getInstance().getMedicineList();
        mJsonArray = new JSONArray();
        medicine_time = getResources().getStringArray(R.array.medicine_time);
        take_method = getResources().getStringArray(R.array.take_method);
        MedicineEntity medicineEntity = null;
        RelativeLayout layout = null;
        AmountView amountView = null;
        EditText etAmount = null;

        for (int i = 0; i < medicineList.size(); i++) {
            final JSONObject jsonObject = new JSONObject();
            medicineEntity = medicineList.get(i).getEntity();
            layout = (RelativeLayout) mMedicineListView.getChildAt(i);
            amountView = (AmountView) layout.findViewById(R.id.amount_view_count);
            etAmount = (EditText) amountView.findViewById(R.id.etAmount);
            if(TextUtils.isEmpty(etAmount.getText())){
                ToastUtils.shortToast("药品数量不能为空");
                return false;
            }else {
                if(medicineEntity.getCount()==0){
                    ToastUtils.shortToast("药品数量不能为0");
                    return false;
                }
            }

            try {
                jsonObject.put("id", medicineEntity.getId());//商品id
                jsonObject.put("name", medicineEntity.getName());//商品名称
                jsonObject.put("num", medicineEntity.getCount());//数量
                jsonObject.put("take_rate", medicineEntity.getDayCount());//每日服用次数
                jsonObject.put("take_time", medicineEntity.getDayCountDesc());
                jsonObject.put("take_num", medicineEntity.getTimeCount());//每次服用量
                jsonObject.put("take_unit", Integer.parseInt(medicineEntity.getTimeCountDesc()));//服用单位
                jsonObject.put("take_method", Integer.parseInt(medicineEntity.getTakeMethod()));//服用方法
                jsonObject.put("remark", medicineEntity.getRemark());//备注
                mJsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        LogUtils.i(mJsonArray.toString());
        if (mJsonArray.length() <= 0) {
            shortToast("您还未添加药品");
            return false;
        }

        if (TextUtils.isEmpty(mDiagnosis)) {
            shortToast("您还未填写任何诊断信息");
            return false;
        }else {
            if(mDiagnosis.length()>50){
                shortToast("诊断字数不能超过50个字");
                return false;
            }
        }

        if (TextUtils.isEmpty(mGender.getText().toString().trim())) {
            shortToast("请选择性别");
            return false;
        }

        EditText editText = (EditText) mAmountView.findViewById(R.id.etAmount);
        if(TextUtils.isEmpty(editText.getText())){
            shortToast("处方有效期不能为空，请选择处方有效期");
            SharedPreferencesHelper.getIns(getActivity()).putInt(RECIPE_DAY, -1);
            return false;
        }else {
            mPov = Integer.parseInt(editText.getText().toString());
            SharedPreferencesHelper.getIns(getActivity()).putInt(RECIPE_DAY, mPov);
            if (mPov<=0) {
                shortToast("处方有效期不能为0，请选择处方有效期");
                return false;
            }
        }

        return true;
    }

    private void sendRecipe() {
        MedicineRequest.getInstance().sendRecipe(mPatientId, mPatientName, mPatientAge, mSex, mDoctroId, mDoctorName, mDiagnosis, mPov, mJsonArray.toString()).subscribe(new BaseRetrofitResponse<BaseData<RecipeEntity>>() {
            @Override
            public void onStart() {
                super.onStart();
                ((XywyBaseActivity) getActivity()).showProgressDialog("loading");
            }

            @Override
            public void onCompleted() {
                ((XywyBaseActivity) getActivity()).hideProgressDialog();
                super.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ((XywyBaseActivity) getActivity()).hideProgressDialog();
            }

            @Override
            public void onNext(final BaseData<RecipeEntity> recipe) {
                super.onNext(recipe);
                dealwithEntry(recipe.getData());

//                DataParserUtil.parseSex(new BaseRetrofitResponse<List<KeyValueGroup>>() {
//                    @Override
//                    public void onNext(List<KeyValueGroup> list) {
                EMMessage msg = EMMessage.createSendMessage(EMMessage.Type.TXT);
                msg.setFrom(YMApplication.getLoginInfo().getData().getHuanxin_username());
                Patient patient = PatientManager.getInstance().getPatient();
                msg.setTo(patient.getPatientHxid());
                msg.addBody(new EMTextMessageBody("[处方信息]"));
//                        KeyValueGroup sex = DataParserUtil.find(patient.getSex(), list);
                JSONObject prescription = new JSONObject();
                List<MedicineCartEntity> medicineList = MedicineCartCenter.getInstance().getMedicineList();
                try {
                    prescription.put("id", recipe.getData().getId());
                    prescription.put("name", mName.getText().toString());
                    prescription.put("sex", mGender.getText().toString());
                    MedicineEntity entity = medicineList.get(0).getEntity();
                    prescription.put("drugInformation", entity.getName() + " " + entity.getSpecification());
                    prescription.put("usage", getUsage(entity));
                    prescription.put("detail", "点击查看详情>>");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LogUtils.e("" + prescription.toString());
                //暂时先注销
                msg.setAttribute("prescription", prescription);
                //发消息 必加扩展字段
                msg.setAttribute("fromRealName", YMApplication.getLoginInfo().getData().getRealname());
                msg.setAttribute("fromAvatar", YMApplication.getLoginInfo().getData().getPhoto());

                msg.setAttribute("toRealName", patient.getRealName());
                msg.setAttribute("toAvatar", patient.getPhoto());
                //stone 新添加的扩展字段区别是医脉搏还是用药助手
                if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
                    msg.setAttribute("source", "selldrug");
                }

                MedicineCartCenter.getInstance().removeAllMedicine();//如果发送成功，将购物车中的信息全部清除
                EMClient.getInstance().chatManager().sendMessage(msg);

                Intent intent = new Intent(getActivity(), ChatMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("userId", patient.getPatientHxid());
                intent.putExtra("username", patient.getRealName());
                intent.putExtra("toHeadImge", patient.getPhoto());
                startActivity(intent);
//                    }
//                });

            }
        });
    }

    @NonNull
    private String getUsage(MedicineEntity medicineEntity) {
        StringBuffer sb = new StringBuffer();
        sb.append("每日" + medicineEntity.getDayCount() + "次");
        //stone 从1开始
        sb.append("，一次" + medicineEntity.getTimeCount() + medicine_time[Integer.parseInt(medicineEntity.getTimeCountDesc())-1]);

        if (!TextUtils.isEmpty(medicineEntity.getTakeMethod())) {
            //stone 从1开始
            sb.append("，" + take_method[Integer.parseInt(medicineEntity.getTakeMethod())-1]);
        }

        if (!TextUtils.isEmpty(medicineEntity.getRemark())) {
            sb.append("，" + medicineEntity.getRemark());
        }
        return sb.toString();
    }

    private void dealwithEntry(RecipeEntity recipeEntity) {
        //处方发送成功后，清空诊断信息
        SharedPreferencesHelper.getIns(getActivity()).putString(RECIPE_SICK, "");
        //重置处方的发送有效期
        SharedPreferencesHelper.getIns(getActivity()).putInt(RECIPE_DAY, 90);
        String id = recipeEntity.getId();
        LogUtils.i(id);
    }

    private void initData() {
        setPatientInfo();
    }

    private void setPatientInfo() {
//        if (BuildConfig.isTestServer) {
//            mGender.setText("男");//测试代码
//            mPatientId = 2;
//            mPatientName = "test";
//        }
        Patient p = PatientManager.getInstance().getPatient();
        if (null != p) {
            mName.setText(p.getRealName() == null ? "" : p.getRealName());
            String time = TimeUtils.getStrDate(TimeUtils.getTime(), "yyyy/MM/dd");
            mRecipe_time.setText(time);
            mAge.setText(p.getAge() == null ? "" : p.getAge());

            if (SEX.MALE.getSEXStatus().equals(p.getSex())) {
                mGender.setText("男");
            } else if (SEX.FEMALE.getSEXStatus().equals(p.getSex())) {
                mGender.setText("女");
            } else {
                mGender.setText("      ");
            }
            mGender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBottomPopuWindow();
                }
            });

        }
    }

    private void showBottomPopuWindow() {
        new BottomBtnPopupWindow.Builder()
                .addGroupItem("选择性别", null)//无点击事件传null
                //添加组Item 会自动分组 组第一个为上部圆角 组最后一个为底部圆角 中部item圆角
                .addGroupItem("男", new BtnClickListener() {
                    @Override
                    public void onItemClick() {
                        mGender.setText("男");
                        mSex = 1;
                    }
                })
                .addGroupItem("女", new BtnClickListener() {
                    @Override
                    public void onItemClick() {
                        mGender.setText("女");
                        mSex = 2;
                    }
                }).build(getActivity()).show(rootView);
    }

    public enum SEX {
        MALE("0"),
        FEMALE("1");
        private final String sex;

        SEX(String sex) {
            this.sex = sex;
        }

        public String getSEXStatus() {
            return sex;
        }

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        initData();
    }

    @Override
    protected void unBindView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onDestroy() {
        if (null != receiver) {
            getActivity().unregisterReceiver(receiver);
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
                        MedicineCartCenter.getInstance().removeMedicine(productId);
                        mMedicineListAdapter.setData(MedicineCartCenter.getInstance().getMedicineList());
                        mMedicineListView.setAdapter(mMedicineListAdapter);
                    }
                }
            } else if (intent.getAction().equals(DIALOGUE_CONSTANTS)) {
                //发送处方
                sendRecipe();
            }
        }

    }

}
