package com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.model;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.xywy.askforexpert.module.discovery.medicine.module.account.UserManager;
import com.xywy.askforexpert.module.discovery.medicine.module.account.beans.UserBean;
import com.xywy.askforexpert.module.discovery.medicine.module.account.model.LoginModel;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.activity.IDProveActivity;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.activity.InfoEditTextActivity;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.ImageBean;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.KeyValueGroup;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.KeyValueNode;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.request.PersonCardRequest;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.util.DataParserUtil;
import com.xywy.askforexpert.module.discovery.medicine.view.SelectPicPopupWindow;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.util.CamaraUtil;
import com.xywy.util.ContextUtil;
import com.xywy.util.T;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Subscriber;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/24 16:52
 */

public class PersonInfoModel {
    private SelectPicPopupWindow menu;
    static UserBean user= UserManager.getInstance().getCurrentLoginUser();
    public static void updateLocalUser(String fieldName, String value, Subscriber commonResponse) {
        switch (fieldName){
            case EditType.userName:
                user.getLoginServerBean().setReal_name(value);
                break;
            case EditType.good:
                user.getLoginServerBean().setBe_good_at(value);
                break;
            case EditType.intro:
                user.getLoginServerBean().setIntroduce(value);
                break;
            case EditType.headImage:
                user.getLoginServerBean().setPhoto(value);
                break;
            case EditType.sex:
                user.getLoginServerBean().setGender(value);
                break;
            default:
                break;
        }
        if (commonResponse!=null){
            commonResponse.onNext(null);
        }
//        UserBean user = UserManager.getInstance().getCurrentLoginUser();
//        try {
//            Field f = user.getClass().getDeclaredField(fieldName);
//            f.set(user, value);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
    }

    public static void editName(Activity context) {
        if (UserManager.getInstance().isPerInfoUpdateable(context, true)) {
            InfoEditTextActivity.start(context, EditType.userName);
        }
    }

    public static void editGood(Activity context) {
        if (UserManager.getInstance().isPerInfoUpdateable(context, true)) {
            InfoEditTextActivity.start(context, EditType.good);
        }
    }

    public static void editIntro(Activity context) {
        if (UserManager.getInstance().isPerInfoUpdateable(context, true)) {
            InfoEditTextActivity.start(context, EditType.intro);
        }
    }

    public static void editIdentifyProve(Activity context) {
        if (UserManager.getInstance().isPerInfoUpdateable(context, true)) {
            IDProveActivity.start(context);
        }
    }

    public static void editBirthday(Activity context, final Subscriber subscriber) {
        if (UserManager.getInstance().isPerInfoUpdateable(context, true)) {
            //条件选择器
            TimePickerView pvTime = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {//选中事件回调
                    String birthday = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    updateLocalUser(EditType.birthday, birthday,subscriber);
                }
            })
                    .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                    .isCenterLabel(true)
                    .isCyclic(true)
                    .build();
            pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
            pvTime.show();
        }
    }

    public static void editJobTitle(final Activity context, final Subscriber resp) {
        if (UserManager.getInstance().isPerInfoUpdateable(context, true)) {
            //条件选择器
            DataParserUtil.parseJobTitle(new BaseRetrofitResponse<List<KeyValueGroup>>() {
                @Override
                public void onNext(final List<KeyValueGroup> majorItems) {
                    OptionsPickerView pvOptions = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
                        @Override
                        public void onOptionsSelect(int options1, int option2, int options3, View v) {
                            //返回的分别是三个级别的选中位置
                            user.setJobTitle(majorItems.get(options1));
                            resp.onNext(null);
                        }
                    }).build();
                    pvOptions.setPicker(majorItems);
                    pvOptions.show();
                }
            });
        }
    }

    public static void editDep(final Activity context, final Subscriber resp) {
        if (UserManager.getInstance().isPerInfoUpdateable(context, true)) {
            //条件选择器
            DataParserUtil.parseMajor(new BaseRetrofitResponse<List<KeyValueGroup>>() {
                                          @Override
                                          public void onNext(final List<KeyValueGroup> majorItems) {
                                              final List<List<KeyValueNode>> majorSecondItems = new ArrayList<>();
                                              for (int i = 0; i< majorItems.size(); i++){//遍历省份
                                                  majorSecondItems.add(majorItems.get(i).getNodes());
                                              }
                                              OptionsPickerView pvOptions = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
                                                  @Override
                                                  public void onOptionsSelect(int options1, int option2, int options3, View v) {
                                                      //返回的分别是三个级别的选中位置
                                                      user.setMajor(majorItems.get(options1));
                                                      user.setMajorSecond(majorSecondItems.get(options1).get(option2));
                                                      resp.onNext(null);
                                                  }
                                              }).build();
                                              pvOptions.setPicker(majorItems,majorSecondItems);
                                              pvOptions.show();
                                          }
                                      }
            );

//            String[] sex = {"耳鼻喉", "五官"};
//            final List<String> options1Items = Arrays.asList(sex);
//            OptionsPickerView pvOptions = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
//                @Override
//                public void onOptionsSelect(int options1, int option2, int options3, View v) {
//                    //返回的分别是三个级别的选中位置
//                    updateLocalUser(EditType.depart, options1Items.get(options1),resp);
//                }
//            }).build();
//            pvOptions.setPicker(options1Items);
//            pvOptions.show();
        }
    }

    public static void editHosp(Activity context, final Subscriber resp) {
        if (UserManager.getInstance().isPerInfoUpdateable(context, true)) {
            selectArea(context, resp);
        }
    }
    private static void selectHosp(final Activity context, String area_id, final Subscriber resp) {
        if (UserManager.getInstance().isPerInfoUpdateable(context, true)) {
            PersonCardRequest.getInstance().getHosp(area_id)
                    .subscribe(new BaseRetrofitResponse<BaseData<List<KeyValueNode>>>(resp){
                        @Override
                        public void onNext(BaseData<List<KeyValueNode>> listBaseData) {
                            super.onNext(listBaseData);
                            final List<KeyValueNode> options1Items = listBaseData.getData();
                            OptionsPickerView pvOptions = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
                                @Override
                                public void onOptionsSelect(int options1, int option2, int options3, View v) {
                                    //返回的分别是三个级别的选中位置
                                    user.setHosp(options1Items.get(options1));
                                    resp.onNext(options1Items.get(options1));
                                }
                            }).build();
                            pvOptions.setPicker(options1Items);
                            pvOptions.show();
                        }
                    });
        }
    }
    private static void selectArea(final Activity context, final Subscriber resp) {

//        String readFileByLines= FileUtils.readFileByLines(ContextUtil.getAppContext().getResources().openRawResource(R.raw.pro));
        DataParserUtil.parseArea(new BaseRetrofitResponse<List<KeyValueGroup>>()
        {
            @Override
            public void onNext(List<KeyValueGroup>  provinceItems) {
                super.onNext(provinceItems);
                final List<List<KeyValueNode>> cityItems = new ArrayList<>();
                for (int i=0;i<provinceItems.size();i++){//遍历省份
                    cityItems.add(provinceItems.get(i).getNodes());
                }
                OptionsPickerView pvOptions = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        selectHosp(context,cityItems.get(options1).get(option2).getId(),resp);
                    }
                }).build();
                pvOptions.setPicker(provinceItems,cityItems);
                pvOptions.show();
            }
        });
    }

    public static void editSex(final Activity context, final Subscriber resp) {
        if (UserManager.getInstance().isPerInfoUpdateable(context, true)) {
            //条件选择器
            DataParserUtil.parseSex(new BaseRetrofitResponse<List<KeyValueGroup>>() {
                @Override
                public void onNext(final List<KeyValueGroup> majorItems) {
                    OptionsPickerView pvOptions = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
                        @Override
                        public void onOptionsSelect(int options1, int option2, int options3, View v) {
                            //返回的分别是三个级别的选中位置
                            user.setGender(majorItems.get(options1));
                            resp.onNext(null);
                        }
                    }).build();
                    pvOptions.setPicker(majorItems);
                    pvOptions.show();
                }
            });
        }
    }

    public void selectHead(Activity context) {
        if (UserManager.getInstance().isPerInfoUpdateable(context, true)) {
            menu = new SelectPicPopupWindow(context);
            menu.showAtLocation(context.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }
    public void commitToServer(final Activity context, final Subscriber subscriber) {
        if (UserManager.getInstance().isPerInfoUpdateable(context, true)&&checkInfoComplete()) {
            subscriber.onStart();
            PersonCardRequest.getInstance().updatePersonalInfo().subscribe(new BaseRetrofitResponse<BaseData>(subscriber) {
                @Override
                public void onNext(BaseData o) {
                    user.getLoginServerBean().setWkys(UserBean.UserState.checking);
                    LoginModel.refreshPersonalInfo(null);
                    super.onNext(o);
                }
            });
        }
    }

    /**
     * 检测提交验证的信息是否完整
     */
    private static boolean checkInfoComplete(){
        UserBean user= UserManager.getInstance().getCurrentLoginUser();
        KeyValueNode noDefine=new KeyValueNode();
        if (user.getMajor().equals(noDefine)||
                user.getMajorSecond().equals(noDefine)){
            T.showShort("请选择科室");
            return false;
        }
        if (user.getGender().equals(noDefine)){
            T.showShort("请选择性别");
            return false;
        }
        if (TextUtils.isEmpty(user.getPhoto())){
            T.showShort("请选择头像");
            return false;
        }
        if (TextUtils.isEmpty(user.getLoginServerBean().getReal_name())){
            T.showShort("请填写名字");
            return false;
        }
        if (user.getHosp().equals(noDefine)){
            T.showShort("请选择医院");
            return false;
        }
        if (user.getJobTitle().equals(noDefine)){
            T.showShort("请选择职称");
            return false;
        }
//        if (TextUtils.isEmpty(user.getLoginServerBean().getBe_good_at())){
//            T.showShort("请填写擅长");
//            return false;
//        }
//        if (TextUtils.isEmpty(user.getLoginServerBean().getIntroduce())){
//            T.showShort("请填写简介");
//            return false;
//        }
        if (user.getIdImages().size()==0||user.getJobImages().size()==0){
            T.showShort("请上传资格图片");
            return false;
        }
        return true;
    }

    public void updateHead(int requestCode, int resultCode, Intent data, final Subscriber subscriber) {
        String path = CamaraUtil.handleResult(ContextUtil.currentActivity(), menu.getImageUri(), requestCode, resultCode, data);
        if (path==null||!new File(path).exists()){
            return;
        }
        subscriber.onStart();
        PersonCardRequest.getInstance().uploadImage(new File(path)).subscribe(new BaseRetrofitResponse<BaseData<ImageBean>>(subscriber) {
            @Override
            public void onNext(BaseData<ImageBean> o) {
                updateLocalUser(EditType.headImage, o.getData().getUrl(),subscriber);
            }
        });
    }

}
