package com.xywy.askforexpert.module.discovery.medicine.module.medical;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.module.discovery.medicine.common.MyBaseActivity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.request.MedicineRequest;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.util.LogUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 *
 * 绑定医药代理 Activity
 * Created by bailiangjin on 2017/4/18.
 */

public class BindingDelegateActivity extends MyBaseActivity{
    private String mRep_id;
    @Bind(R.id.et_delegate_id)
    EditText et_delegate_id;

    @Bind(R.id.btn_binding)
    Button btn_binding;


    public static void start(Activity activity){
        activity.startActivity(new Intent(activity,BindingDelegateActivity.class));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_binding_delegate;
    }

    @Override
    protected void initView() {
        super.initView();
        hideCommonBaseTitle();
        //初始化统一bar，将系统栏的背景设置成透明色
        CommonUtils.initSystemBar(this);
        initTitle();
        et_delegate_id.setInputType(EditorInfo.TYPE_CLASS_PHONE);
    }

    private void initTitle() {
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
    }



    @OnClick({R.id.btn_binding})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_binding:
                mRep_id=et_delegate_id.getText().toString().trim();
                if(TextUtils.isEmpty(mRep_id)){
                    shortToast("邀请码不能为空 请重新输入");
                    return;
                }
                if(!isNumber(mRep_id)){
                    shortToast("请输入数字");
                    return;
                }
//                shortToast("点击了提交:id="+rep_id);
                // TODO: 2017/4/18 调用绑定接口
//                Pharmaceutical agents
                //由于使用医脉的用户信息，所以注销掉用药助手的用户信息
//                UserBean user = UserManager.getInstance().getCurrentLoginUser();
////                LogUtils.i("user="+user+"---rep_id="+rep_id);
//                if(null != user){
//                    long doctroId = Long.parseLong(user.getLoginServerBean().getUser_id());
//                    getData(doctroId,Long.parseLong(mRep_id));
//                }
                long doctroId = Long.parseLong(YMUserService.getCurUserId());
                getData(doctroId,Long.parseLong(mRep_id));
                break;

            default:
                break;
        }
    }

    private void getData(long doctor_id, long rep_id){
        MedicineRequest.getInstance().bindPharmaceuticalAgentsOffline(doctor_id,rep_id).subscribe(new BaseRetrofitResponse<BaseData>(){
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
            public void onNext(BaseData entry) {
                super.onNext(entry);
                if(entry!=null) {
                    dealwithEntry(entry);
                }
            }
        });
    }

    private void dealwithEntry(BaseData entry) {
        LogUtils.i("code="+entry.getCode());
        if(10000==entry.getCode()){
            shortToast("您已成功接受"+mRep_id+"的邀请");
            new Handler().postDelayed(new Runnable()
            {
                public void run()
                {
                    //execute the task
                    finish();
                }
            }, 500);

        }else {
            shortToast(entry.getMsg());
        }
    }

    /**
     * 只含数字
     *
     * @param data 可能只包含数字的字符串
     * @return 是否只包含数字
     */
    public static boolean isNumber(String data) {
        String expr = "^[0-9]+$";
        return data.matches(expr);
    }
}

