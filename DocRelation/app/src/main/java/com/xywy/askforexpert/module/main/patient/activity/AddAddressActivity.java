package com.xywy.askforexpert.module.main.patient.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.model.AddressBean;
import com.xywy.askforexpert.model.AddressData;
import com.xywy.askforexpert.model.IdNameBean;
import com.xywy.askforexpert.module.consult.ServiceProvider;
import com.xywy.askforexpert.module.discovery.medicine.util.ToastUtils;
import com.xywy.askforexpert.module.my.userinfo.AreaListActivity;

import butterknife.Bind;
import rx.Subscriber;

/**
 * Created by jason on 2018/11/15.
 */

public class AddAddressActivity extends YMBaseActivity {
    @Bind(R.id.edit_user_name)
    EditText edit_user_name;
    @Bind(R.id.edit_mobile)
    EditText edit_mobile;
    @Bind(R.id.region_ll)
    LinearLayout region_ll;
    @Bind(R.id.addres_tv)
    TextView addres_tv;
    @Bind(R.id.edit_address_detail)
    EditText edit_address_detail;
    @Bind(R.id.save_btn)
    TextView save_btn;
    public int REQUESTCODE = 1;
    private IdNameBean mProvince;
    private IdNameBean mCity;
    private String act;
    private String id = "";
    private String doctorId = YMUserService.getCurUserId();

    @Override
    protected void initView() {
        Intent intent = getIntent();
        final AddressData data = (AddressData) intent.getSerializableExtra("addressData");
        if (null!=data){
            titleBarBuilder.setTitleText("修改收货地址");
            edit_user_name.setText(data.getUname());
            edit_mobile.setText(data.getMobile());
            addres_tv.setText(data.getProvince()+data.getCity()+data.getCounty());
            edit_address_detail.setText(data.getAddress());
            act = "2";
            id = data.getId();
        }else{
            titleBarBuilder.setTitleText("添加收货地址");
            act = "1";
        }
        region_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddAddressActivity.this, AreaListActivity.class),REQUESTCODE);
            }
        });
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String province;
                String city;
                if (null==mProvince&&null==mCity){
                    province = data.getProvince();
                    city = data.getCity();
                }else{
                    province = mProvince.name;
                    city = mCity.name;
                }
                if (edit_user_name.getText().length()<2){
                    ToastUtils.longToast("请输入2-10字收件人姓名");
                } else if (edit_mobile.getText().length()!=11){
                    ToastUtils.longToast("请输入11位手机号码");
                }else if (edit_address_detail.getText().length()<5){
                    ToastUtils.longToast("请输入5-100字详细收件地址");
                }else if (!TextUtils.isEmpty(addres_tv.getText())) {
                    ServiceProvider.editAddress(doctorId, edit_user_name.getText().toString(),
                            edit_mobile.getText().toString(), province, city,
                            edit_address_detail.getText().toString(), act, id,
                            new Subscriber<AddressBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(AddressBean addressBean) {
                                    if (addressBean.getCode()==10000){
                                        ToastUtils.longToast("编辑成功");
                                        finish();
                                    }else{
                                        ToastUtils.longToast(addressBean.getMsg());
                                    }
                                }
                            });
                }else {
                    ToastUtils.longToast("请填写完整数据");
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUESTCODE && resultCode == RESULT_OK)) {
            if (data != null) {
                mProvince = (IdNameBean) data.getSerializableExtra(Constants.KEY_PROVINCE);
                mCity = (IdNameBean) data.getSerializableExtra(Constants.KEY_CITY);
                if (mProvince != null && mCity != null && !TextUtils.isEmpty(mProvince.name) && !TextUtils.isEmpty(mCity.name)) {
                    addres_tv.setText(mProvince.name + Constants.DIVIDERS + mCity.name);
                }
            }
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.add_address_layout;
    }
}
