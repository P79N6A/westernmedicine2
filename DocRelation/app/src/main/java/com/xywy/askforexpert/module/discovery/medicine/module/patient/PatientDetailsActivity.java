package com.xywy.askforexpert.module.discovery.medicine.module.patient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.module.discovery.medicine.common.MyRxBus;
import com.xywy.askforexpert.module.discovery.medicine.module.patient.entity.Patient;
import com.xywy.askforexpert.module.main.diagnose.Patient_Group_ManagerActivity;
import com.xywy.retrofit.rxbus.Event;
import com.xywy.retrofit.rxbus.EventSubscriber;
import com.xywy.util.ImageLoaderUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import static com.xywy.askforexpert.R.id.tv_groupname;


/**
 * Created by xgxg on 2017/11/2.
 * 用药助手的患者详情
 */

public class PatientDetailsActivity extends YMBaseActivity {
    private final String PATIENT = "patient";
    private final String PATIENT_BUNDLE= "patient_bundle";
    private final String INEXID= "0";
    private final String MALE= "0";
    private final String FEMALE= "1";
    private final String AGE = "0";
    private ImageView mImageHead;
    private TextView mTvRealname,mTvSex,mTvAge,mArea,mTvGroupName;
    private Patient mPateint;
    @Override
    protected void initView() {
        //监听到系统消息，刷新患者列表的接口数据
        MyRxBus.registerPatientInfoChanged(new EventSubscriber() {
            @Override
            public void onNext(Object o) {
                LogUtils.i("收到患者信息改变");
                Event event = (Event) o;
                if(null != event){
                    String newGroupName = event.getData().toString();
                    if (mPateint != null) {
                        LogUtils.i(mPateint.getHx_user());
                        SharedPreferences sp = getSharedPreferences("save_gid", MODE_PRIVATE);
                        String mLastGid = sp.getString(mPateint.getHx_user(),"0");
                        //如果是从聊天页面更改了分组id,那么，再从患者详情页面直接跳转到分组管理
                        //页面，就需要从新设置当前患者的分组id,同理，如果从患者详情页面直接跳转到了
                        //分组管理页面，更改了分组id，回到患者详情页面，就需要从新设置当前患者的分组id
                        mPateint.getGroup().setGid(mLastGid);
                        LogUtils.i(mPateint.getGroup().getGid());
                    }
                    mTvGroupName.setText(newGroupName);
                }

            }
        },this);
        Intent intent = getIntent();
        if(null != intent){
            Bundle b = intent.getBundleExtra(PATIENT_BUNDLE);
            if(null != b){
                mPateint = (Patient) b.getSerializable(PATIENT);
            }
        }
        mImageHead = (ImageView) findViewById(R.id.img_head);
        mTvRealname = (TextView) findViewById(R.id.tv_realname);
        mTvSex = (TextView) findViewById(R.id.tv_sex);
        mTvAge = (TextView) findViewById(R.id.tv_age);
        mArea = (TextView) findViewById(R.id.area);
        mTvGroupName = (TextView) findViewById(tv_groupname);
    }

    @Override
    protected void initData() {
        dealWithData(mPateint);
    }

    private void dealWithData(Patient patient) {
        if(null !=patient){
            titleBarBuilder.setTitleText(mPateint.getRealName());
            ImageLoaderUtils.getInstance().displayImage(mPateint.getPhoto(),mImageHead);
            mTvRealname.setText(mPateint.getRealName());
            if(MALE.equals(mPateint.getSex())){
                mTvSex.setText("男");
            }else if(FEMALE.equals(mPateint.getSex())){
                mTvSex.setText("女");
            }else {
                mTvSex.setText("");
            }
            mTvAge.setText(AGE.equals(mPateint.getAge())?"":mPateint.getAge());
            if(null != mPateint.getGroup()){
                mTvGroupName.setText(mPateint.getGroup().getGname());
            }
            String province_index = mPateint.getProvince();
            String city_index = mPateint.getCity();
            if(!TextUtils.isEmpty(province_index)){
                if(!INEXID.equals(province_index)){
                    String area_province = getRowJson(R.raw.patient_area_province);
                    List list_province = ResolveJson.R_list_noid(area_province);
                    String province_name = list_province.get(Integer.parseInt(province_index)).toString();
                    String city_name="";
                    if(!INEXID.equals(city_index)){
                        String area_city = getRowJson(R.raw.patient_area_city);
                        List<List<HashMap<String, String>>> list_city = ResolveJson.R_List_List(area_city);
                        boolean findResult = false;
                        for (int i=0;i<list_city.size();i++){
                            List<HashMap<String, String>> list_inner = list_city.get(i);
                            for (int j=0;j<list_inner.size();j++){
                                HashMap<String, String> map = list_inner.get(j);
                                if(mPateint.getCity().equals(map.get("id").toString())){
                                    city_name = map.get("name");
                                    findResult = true;
                                    break;
                                }
                            }
                            if(findResult){
                                break;
                            }
                        }
                    }

                    mArea.setText(province_name+" "+city_name);
                }
            }
        }
    }

    /**
     * 获取 文件里面的json
     *
     * @param id
     * @return
     */
    public String getRowJson(int id) {
        String json = null;
        InputStream is = this.getResources().openRawResource(id);
        byte[] buffer;
        try {
            buffer = new byte[is.available()];
            is.read(buffer);
            json = new String(buffer, "utf-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_patient_details;
    }

    public void onClick_back(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn1:

                finish();
                break;
            case R.id.btn_send_message:
                if (mPateint != null) {
                    //跳转到聊天页面
                    YMOtherUtils.skip2ChatPage(this,mPateint);
                }
                break;
            case R.id.re_group_name:
                if (mPateint != null) {

                    intent = new Intent(PatientDetailsActivity.this,
                            Patient_Group_ManagerActivity.class);
                    intent.putExtra("type", "set_group");
                    intent.putExtra("uid", mPateint.getUId());
                    if(null != mPateint.getGroup()){
                        intent.putExtra("lastgid", mPateint.getGroup().getGid());
                    }
                    intent.putExtra("hx_userid", mPateint.getHx_user());
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyRxBus.unRegisterPatientInfoChanged(this);
    }
}
