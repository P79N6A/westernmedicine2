package com.xywy.askforexpert.module.my.userinfo;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.util.PathUtil;
import com.xywy.askforexpert.MainActivity;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ScreenUtils;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.IdNameBean;
import com.xywy.askforexpert.model.LoginInfo_New;
import com.xywy.askforexpert.model.certification.ApproveBean;
import com.xywy.askforexpert.module.consult.activity.AddOrEditTextInfoActivity;
import com.xywy.askforexpert.module.discovery.medicine.common.LimitInputTextWatcher;
import com.xywy.askforexpert.module.my.photo.PhotoWallActivity;
import com.xywy.askforexpert.widget.view.RoundAngleImageView;
import com.xywy.askforexpert.widget.view.SelectPicPopupWindow;

import net.tsz.afinal.FinalBitmap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * [认证步骤一] 认证信息完善 stone
 */
public class ApproveInfoActivity extends YMBaseActivity {

    private ApproveBean mApproveBean = new ApproveBean();

    private LoginInfo_New mLoginInfo;

//    private static final String TAG = "ApproveInfoActivity";
    /**
     * 图片地址
     */
    private File origUri;
    private SelectPicPopupWindow menuWindow;

    private Map<String, String> map = new HashMap<String, String>();
    private Map<String, String> map2 = new HashMap<String, String>();

    private FinalBitmap fb;

    private LinearLayout main;
    private RoundAngleImageView iv_head;
    private TextView tv_jobtitle, tv_hospital, tv_department, tv_goodat, tv_desc;
    private TextView tv_area;
    private TextView tv_girl, tv_boy;
    private EditText et_name, et_phone, et_phone2;

    private IdNameBean boy = new IdNameBean("1", "男");
    private IdNameBean girl = new IdNameBean("2", "女");
    //性别
    private IdNameBean mSex;
    private String mHeadIconPath;
    private String mHeadIconUploadingPath;//上传中图片
    private String mName;
    private IdNameBean mJobTitle;
    private IdNameBean mDepartment;
    private String mGoodAt;
    private String mArea;
    private IdNameBean mHospital;
    private String mDesc;
    private String mPhone;
    private String mP1;
    private String mP2;
    private IdNameBean mProvince;
    private IdNameBean mCity;

    @Override
    protected int getLayoutResId() {
        return R.layout.approveinfo;
    }

    @Override
    protected void beforeViewBind() {
        fb = FinalBitmap.create(ApproveInfoActivity.this, false);
        ScreenUtils.initScreen(ApproveInfoActivity.this);
    }

    @Override
    public void initView() {
        hideCommonBaseTitle();
        ((TextView) findViewById(R.id.tv_title)).setText("专业认证");

        main = (LinearLayout) findViewById(R.id.main);
        iv_head = (RoundAngleImageView) findViewById(R.id.iv_head);
        tv_jobtitle = (TextView) findViewById(R.id.tv_jobtitle);
        tv_hospital = (TextView) findViewById(R.id.tv_hospital);
        tv_department = (TextView) findViewById(R.id.tv_department);
        tv_goodat = (TextView) findViewById(R.id.tv_goodat);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        tv_area = (TextView) findViewById(R.id.tv_area);
        tv_girl = (TextView) findViewById(R.id.tv_girl);
        tv_boy = (TextView) findViewById(R.id.tv_boy);
        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_phone2 = (EditText) findViewById(R.id.et_phone2);

        et_name.addTextChangedListener(new LimitInputTextWatcher(et_name));

        handleInfo();

    }

    @Override
    protected void initData() {

    }


    @Override
    protected void handleMsg(Message msg) {
        super.handleMsg(msg);
        map = (HashMap<String, String>) msg.obj;
        switch (msg.what) {
            case 200:
                initView();
                break;
            case 400:
                if (map.get("code").equals("0")) {

                    fb.display(iv_head, mHeadIconUploadingPath);

                    mHeadIconPath = mHeadIconUploadingPath;
                }

                break;
            case 300:
                if (map2.get("code").equals("0")) {
                    YMApplication.getLoginInfo().getData().setIsdoctor("0");
                    YMApplication.getLoginInfo().getData().setApproveid("0");
                    YMApplication.getLoginInfo().getData().setIsjob("0");
                }
                ToastUtils.shortToast(map2.get("msg"));
                Intent intent = new Intent(ApproveInfoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }

    //点击事件
    public void onClick_back(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.submit:
                mName = et_name.getText().toString().trim();
                mArea = tv_area.getText().toString().trim();
                if (mHospital != null) {
                    mHospital.name = tv_hospital.getText().toString().trim();
                }
                if (mJobTitle != null) {
                    mJobTitle.name = tv_jobtitle.getText().toString().trim();
                }
                mGoodAt = tv_goodat.getText().toString().trim();
                mDesc = tv_desc.getText().toString().trim();


                mP1 = et_phone.getText().toString().trim();
                mP2 = et_phone2.getText().toString().trim();
                //科室电话需要 - 区号
                if (!TextUtils.isEmpty(mP1) && !TextUtils.isEmpty(mP2)) {
                    mPhone = mP1 + "-" + mP2;
                } else {
                    mPhone = "";
                }

                if (TextUtils.isEmpty(mHeadIconPath)) {
                    ToastUtils.shortToast("请上传头像");
                } else if (TextUtils.isEmpty(mName)) {
                    ToastUtils.shortToast("请输入姓名");
                } else if (mName.length() < 2 || mName.length() > 4) {
                    ToastUtils.shortToast("请输入姓名为2-4个汉字");
                } else if (mSex == null) {
                    ToastUtils.shortToast("请选择性别");
                } else if (TextUtils.isEmpty(mArea)) {
                    ToastUtils.shortToast("请选择医院地点");
                } else if (mHospital == null) {
                    ToastUtils.shortToast("请填写在职医院");
                } else if (mDepartment == null) {
                    ToastUtils.shortToast("请填写所属科室");
                } else if (TextUtils.isEmpty(mPhone) && (!TextUtils.isEmpty(mP1) || !TextUtils.isEmpty(mP2))) {
                    //没填全 其中有一个为空
                    ToastUtils.shortToast("请输入带区号的固定电话");
                } else if (!TextUtils.isEmpty(mPhone) && !YMOtherUtils.isPhone(mPhone)) {
                    //填全 匹配正则
                    ToastUtils.shortToast("请输入正确的的科室电话");
                } else if (mJobTitle == null) {
                    ToastUtils.shortToast("请选择临床职称");
                } else if (TextUtils.isEmpty(mGoodAt)) {
                    ToastUtils.shortToast("请填写擅长疾病");
                } else if (TextUtils.isEmpty(mDesc)) {
                    ToastUtils.shortToast("请填写个人简介");
                } else {
                    //上传认证图片
                    mApproveBean.be_good_at = mGoodAt;
                    mApproveBean.introduce = mDesc;
                    mApproveBean.city = mCity.id;
                    mApproveBean.province = mProvince.id;
                    mApproveBean.clinic = mJobTitle.id;
                    mApproveBean.hospital_id = mHospital.id;
                    mApproveBean.hospital_name = mHospital.name;
                    mApproveBean.doctor_id = String.valueOf(mLoginInfo.user_id);
                    mApproveBean.photo = mHeadIconPath;
                    mApproveBean.real_name = mName;
                    mApproveBean.sex = mSex.id;
                    mApproveBean.subject_first = mDepartment.id;
                    mApproveBean.subject_second = mDepartment.name;
                    mApproveBean.subject_phone = mPhone;
                    Bundle bundle
                            = new Bundle();
                    bundle.putSerializable(Constants.KEY_VALUE, mApproveBean);
                    YMOtherUtils.startActivityForResult(this, IDCardUplodActivity.class, bundle, Constants.REQUESTCODE_GO_UPLOAD_PHOTO);
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.open_exit);
                }
                break;
            case R.id.rl_area:
                //地区
                intent = new Intent(this, AreaListActivity.class);
                intent.putExtra(Constants.KEY_TYPE, AreaListActivity.HOSPITAL);
                intent.putExtra(Constants.KEY_CITY, mCity);
                intent.putExtra(Constants.KEY_PROVINCE, mProvince);
                startActivityForResult(intent, Constants.REQUESTCODE_GO_CHOOSE_PROVINCE_CITY);
                break;
            case R.id.rl_jobtitle:
                //临床职称
                intent = new Intent(ApproveInfoActivity.this, BaseJobListActivity.class);
                intent.putExtra(Constants.KEY_VALUE, mJobTitle);
                startActivityForResult(intent, Constants.REQUESTCODE_JOBTITLE);
                break;
            case R.id.rl_head:
                // 设置layout在PopupWindow中显示的位置
                menuWindow = new SelectPicPopupWindow(ApproveInfoActivity.this,
                        itemsOnClick);
                // 显示窗口
                menuWindow.showAtLocation(main, Gravity.BOTTOM
                        | Gravity.CENTER_HORIZONTAL, 0, 0);

                //添加遮罩 stone
                YMOtherUtils.addScreenBg(menuWindow, this);

                break;
            case R.id.rl_hospital:
                intent = new Intent(ApproveInfoActivity.this, SearchHosipitalActivity.class);
                intent.putExtra(Constants.KEY_VALUE, mHospital);
                startActivityForResult(intent, Constants.REQUESTCODE_HOSPITAL);
                break;
            case R.id.rl_department:
                //科室
                String depart = tv_department.getText().toString().trim();
                if (!TextUtils.isEmpty(depart)) {
                    String[] strs = depart.split(Constants.DIVIDERS);
                    if (strs.length == 2) {
                        FillDepartmentActivity.startActivityForResult(this, strs[0], strs[1]);
                    } else {
                        FillDepartmentActivity.startActivityForResult(this, "", "");
                    }
                } else {
                    FillDepartmentActivity.startActivityForResult(this, "", "");
                }
                break;
            case R.id.rl_goodat:
                // 擅长疾病
                AddOrEditTextInfoActivity.startGoodAtActivityForResult(ApproveInfoActivity.this, tv_goodat.getText().toString().trim(), true);
                break;
            case R.id.rl_desc:
                //个人简介
                AddOrEditTextInfoActivity.startPersonalDescActivityForResult(ApproveInfoActivity.this, tv_desc.getText().toString().trim(), true);
                break;
            //性别
            case R.id.tv_boy:
                mSex = boy;
                tv_boy.setSelected(true);
                tv_girl.setSelected(false);
                break;
            case R.id.tv_girl:
                mSex = girl;
                tv_boy.setSelected(false);
                tv_girl.setSelected(true);
                break;
            default:
                break;
        }
    }

    // 为弹出窗口实现监听类
    private OnClickListener itemsOnClick = new OnClickListener() {
        Intent intent;

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.item_popupwindows_camera:
                    // 照片命名
                    // String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                    // .format(new Date());
                    // String origFileName = "osc_3" + timeStamp + ".jpg";
                    // origUri = Uri.fromFile(new File(FILE_SAVEPATH,
                    // origFileName));
                    origUri = new File(PathUtil.getInstance().getImagePath(),
                            "osc_" + System.currentTimeMillis() + ".jpg");
                    origUri.getParentFile().mkdirs();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri mImageUri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果是7.0android系统
                        ContentValues contentValues = new ContentValues(1);
                        contentValues.put(MediaStore.Images.Media.DATA,origUri.getAbsolutePath());
                        mImageUri= getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
                    }else{
                        mImageUri = Uri.fromFile(origUri);
                    }

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                    startActivityForResult(intent, Constants.REQUESTCODE_CHOOSE_CAMERA);
                    // startActivity(intent);
                    break;
                case R.id.item_popupwindows_Photo:
                    intent = new Intent(ApproveInfoActivity.this, PhotoWallActivity.class);
                    startActivity(intent);
                    YMApplication.photoTag = "approve";
                    YMApplication.isSelectMore = false;
                    break;
                case R.id.item_popupwindows_cancel:
                    menuWindow.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        DLog.i("300", "裁剪" + getIntent().getIntExtra("code", 0));
        int code = intent.getIntExtra("code", -1);
        if (code != 100) {
            return;
        }

        ArrayList<String> list1 = new ArrayList<String>();
        list1.clear();
        ArrayList<String> paths = intent.getStringArrayListExtra("paths");
        if (paths != null && paths.size() > 0) {
            Intent intent2 = new Intent(ApproveInfoActivity.this, ClipPictureActivity.class);
            DLog.d("approve", paths.get(0));
            intent2.putExtra("path", paths.get(0));
            startActivityForResult(intent2, Constants.REQUESTCODE_MODIFY_FINISH);
        } else {
            ToastUtils.shortToast("图片选取失败");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DLog.i("300", "onActivityResult==" + requestCode);
        if (requestCode == Constants.REQUESTCODE_CHOOSE_CAMERA && resultCode == RESULT_OK) {
            Intent intent = new Intent(ApproveInfoActivity.this, ClipPictureActivity.class);
            DLog.i("300", "REQUESTCODE_CHOOSE_CAMERA" + origUri.getPath());
            intent.putExtra("path", origUri.getPath());
            startActivityForResult(intent, Constants.REQUESTCODE_MODIFY_FINISH);
        } else if (requestCode == Constants.REQUESTCODE_MODIFY_FINISH && resultCode == RESULT_OK) {
            DLog.i("300", " REQUESTCODE_MODIFY_FINISH resultCode==" + resultCode);
            if (data != null) {
                final String path = data.getStringExtra("path");
                // Bitmap b = BitmapFactory.decodeFile(path);
                // iv_head.setImageBitmap(b);
                if (path != null && !path.equals("")) {
                    if (NetworkUtil.isNetWorkConnected()) {
                        mHeadIconUploadingPath = path;
//                        YMUserService.getPerInfo().getData().setPhoto(path);
                        YMApplication.getLoginInfo().getData().setPhoto(path);

                        // TODO: 2018/3/20 去掉,此时不需要重新上传,上个页面已经上传过了  stone
//                        UpdatePersonInfo.update("photo", path, 400, uiHandler);

                        fb.display(iv_head, mHeadIconUploadingPath);

                        mHeadIconPath = mHeadIconUploadingPath;

                    } else {
                        ToastUtils.shortToast(
                                "网络连接失败,图片不能上传,请联网重试");
                    }
                }
            }
        } else if ((requestCode == Constants.REQUESTCODE_GOOD_AT && resultCode == RESULT_OK)) {
            if (data != null) {
                String str = data.getStringExtra(Constants.KEY_VALUE);
                if (!TextUtils.isEmpty(str.trim())) {
                    tv_goodat.setText(str);
                }
            }
        } else if ((requestCode == Constants.REQUESTCODE_PERSONAL_DESC && resultCode == RESULT_OK)) {
            if (data != null) {
                String str = data.getStringExtra(Constants.KEY_VALUE);
                if (!TextUtils.isEmpty(str.trim())) {
                    tv_desc.setText(str);
                }
            }
        } else if ((requestCode == Constants.REQUESTCODE_JOBTITLE && resultCode == RESULT_OK)) {
            if (data != null) {
                mJobTitle = (IdNameBean) data.getSerializableExtra(Constants.KEY_VALUE);
                if (mJobTitle != null) {
                    tv_jobtitle.setText(mJobTitle.name);
                }
            }
        } else if ((requestCode == Constants.REQUESTCODE_HOSPITAL && resultCode == RESULT_OK)) {
            if (data != null) {
                mHospital = (IdNameBean) data.getSerializableExtra(Constants.KEY_VALUE);
                if (!TextUtils.isEmpty(mHospital.name)) {
                    tv_hospital.setText(mHospital.name);
                }
            }
        } else if ((requestCode == Constants.REQUESTCODE_DEPARTMENT && resultCode == RESULT_OK)) {
            if (data != null) {
                String str = data.getStringExtra(Constants.KEY_VALUE);
                if (!TextUtils.isEmpty(str.trim())) {
                    String atr[] = str.split(Constants.DIVIDERS);
                    if (atr.length == 2) {
                        mDepartment = new IdNameBean(atr[0], atr[1]);
                        tv_department.setText(str);
                    }
                }
            }
        } else if ((requestCode == Constants.REQUESTCODE_GO_CHOOSE_PROVINCE_CITY && resultCode == RESULT_OK)) {
            if (data != null) {
                mProvince = (IdNameBean) data.getSerializableExtra(Constants.KEY_PROVINCE);
                mCity = (IdNameBean) data.getSerializableExtra(Constants.KEY_CITY);
                if (mProvince != null && mCity != null && !TextUtils.isEmpty(mProvince.name) && !TextUtils.isEmpty(mCity.name)) {
                    tv_area.setText(mProvince.name + Constants.DIVIDERS + mCity.name);
                }
            }
        } else if ((requestCode == Constants.REQUESTCODE_GO_UPLOAD_PHOTO && resultCode == RESULT_OK)) {
            finish();
        }

    }


    /**
     * 处理数据 stone
     */
    private void handleInfo() {
        if (YMApplication.getUserInfo() != null) {
            mLoginInfo = YMApplication.getUserInfo();
            mHeadIconPath = mLoginInfo.photo;
            mName = mLoginInfo.real_name;
            mGoodAt = mLoginInfo.be_good_at;
            mDesc = mLoginInfo.introduce;
            if (mLoginInfo.details != null) {
                mSex = (!TextUtils.isEmpty(mLoginInfo.details.gender) && mLoginInfo.details.gender.equals("男"))
                        ? boy : ((!TextUtils.isEmpty(mLoginInfo.details.gender) && mLoginInfo.details.gender.equals("女"))
                        ? girl : null);
                if (!TextUtils.isEmpty(mLoginInfo.details.clinic)) {
                    mJobTitle = new IdNameBean(String.valueOf(mLoginInfo.clinic), mLoginInfo.details.clinic);
                }
                if (!TextUtils.isEmpty(mLoginInfo.details.subject_first)) {
                    mDepartment = new IdNameBean(mLoginInfo.details.subject_first, mLoginInfo.details.subject_second);
                }
                if (!TextUtils.isEmpty(mLoginInfo.details.province) && !TextUtils.isEmpty(mLoginInfo.details.city)) {
                    mArea = mLoginInfo.details.province + Constants.DIVIDERS + mLoginInfo.details.city;
                } else {
                    mArea = "";
                }
                if (!TextUtils.isEmpty(mLoginInfo.details.hos_name)) {
                    mHospital = new IdNameBean(String.valueOf(mLoginInfo.hospitalid), mLoginInfo.details.hos_name);
                }
                mPhone = mLoginInfo.details.subject_phone;
                if (!TextUtils.isEmpty(mLoginInfo.details.province)) {
                    mProvince = new IdNameBean(String.valueOf(mLoginInfo.province), mLoginInfo.details.province);
                }
                if (!TextUtils.isEmpty(mLoginInfo.details.city)) {
                    mCity = new IdNameBean(String.valueOf(mLoginInfo.city), mLoginInfo.details.city);
                }

                if (mSex == null) {
                    tv_boy.setSelected(false);
                    tv_girl.setSelected(false);
                } else if (mSex.name.equals("男")) {
                    tv_boy.setSelected(true);
                    tv_girl.setSelected(false);
                } else {
                    tv_boy.setSelected(false);
                    tv_girl.setSelected(true);
                }
                tv_area.setText(mArea);
                if (mHospital != null) {
                    tv_hospital.setText(mHospital.name);
                }
                if (mDepartment != null) {
                    if (!TextUtils.isEmpty(mDepartment.id)
                            && !TextUtils.isEmpty(mDepartment.name)) {
                        tv_department.setText(mDepartment.id + Constants.DIVIDERS + mDepartment.name);
                    } else if (!TextUtils.isEmpty(mDepartment.id)) {
                        tv_department.setText(mDepartment.id);
                    } else {
                        tv_department.setText(mDepartment.name);
                    }
                }
                et_phone.setText(mPhone);
                if (mJobTitle != null) {
                    tv_jobtitle.setText(mJobTitle.name);
                }
            }

            fb.display(iv_head, mHeadIconPath);
            et_name.setText(mName);
            tv_goodat.setText(mGoodAt);
            tv_desc.setText(mDesc);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.setContentView(R.layout.empty_view);
    }

}
