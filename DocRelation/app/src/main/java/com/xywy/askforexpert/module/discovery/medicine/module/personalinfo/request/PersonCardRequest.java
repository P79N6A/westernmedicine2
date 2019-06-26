package com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.request;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.module.discovery.medicine.common.MyRetrofitClient;
import com.xywy.askforexpert.module.discovery.medicine.common.RequestTool;
import com.xywy.askforexpert.module.discovery.medicine.module.account.UserManager;
import com.xywy.askforexpert.module.discovery.medicine.module.account.beans.UserBean;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.AppVersion;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.ImageBean;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.KeyValueNode;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.PersonCard;
import com.xywy.retrofit.RetrofitClient;
import com.xywy.retrofit.net.BaseData;
import com.xywy.util.CompressUtil;
import com.xywy.util.FilePathUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class PersonCardRequest {

    private static PersonCardRequest instance;
    private PersonCardApi api;

    private PersonCardRequest() {
//        api = RetrofitClient.getRetrofit().create(PersonCardApi.class);
        api = MyRetrofitClient.getMyRetrofit().create(PersonCardApi.class);
    }

    static public PersonCardRequest getInstance() {
        if (instance == null) {
            instance = new PersonCardRequest();
        }
        return instance;
    }

    public Observable<BaseData> getDoctorEWM(String doctor_id) {
        Map<String, String> getParams = RequestTool.getCommonParams("1760");
        getParams.put("doctor_id", doctor_id + "");
        getParams.put("sign",RequestTool.getSign(getParams));
        return api.getDoctorEWM(getParams);
    }

    public Observable<BaseData<PersonCard>> getDoctorCard(String doctor_id) {
        Map<String, String> getParams = RequestTool.getCommonParams("1601");
        getParams.put("doctor_id", doctor_id + "");
        getParams.put("sign",RequestTool.getSign(getParams));
        return api.getDoctorCardNew(getParams);
    }


    public Observable<BaseData<ImageBean>> uploadImage(final File file) {
        return Observable.create(new Observable.OnSubscribe<File>() {
            @Override
            public void call(final Subscriber<? super File> subscriber) {
                Bitmap bitmap = CompressUtil.createCompressBitmap(file.getPath(), 500, 500);
                File randomFile = FilePathUtil.getRandomFile(".png");
                CompressUtil.saveBitmap(bitmap, randomFile.getPath());
                subscriber.onNext(randomFile);
            }
        })
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<File, Observable<BaseData<ImageBean>>>() {
                    @Override
                    public Observable<BaseData<ImageBean>> call(File f) {
                        Map<String, String> getParamMap = RequestTool.getCommonParams("1248");
                        getParamMap.put("version", "1.1");
                        Map<String, String> postParams = new HashMap<>();
                        postParams.put("thumb", "2");
                        getParamMap.put("sign", RequestTool.getSign(getParamMap, postParams));

                        MultipartBody.Builder build = RetrofitClient.addFiles("mypic", f, MediaType.parse("application/octet-stream"));
                        RetrofitClient.addTextPart(build, postParams);
                        return api.uploadImg(getParamMap, build.build());
                    }
                });
    }

    public Observable<BaseData<List<KeyValueNode>>> getHosp(final String area_id) {
        Map<String, String> getParamMap = RequestTool.getCommonParams("1608");
        getParamMap.put("area_id", area_id);
        getParamMap.put("sign", RequestTool.getSign(getParamMap, null));
        return api.getHosp(getParamMap);
    }

    final static String spliter="|";
    public Observable<BaseData> updatePersonalInfo() {
        UserBean user= UserManager.getInstance().getCurrentLoginUser();
        Map<String, String> postParams = new HashMap<>();
        postParams.put("major_second", user.getMajorSecond().getId());
        postParams.put("major_first", user.getMajor().getId());
        String sex=user.getGender().getName();
        if (sex.equals("男")){
            sex="1";
        }else{
            sex="2";
        }
        postParams.put("sex", sex);

        postParams.put("photo", user.getPhoto());
        postParams.put("real_name", user.getLoginServerBean().getReal_name());
        postParams.put("hospital_id", user.getHosp().getId());
        postParams.put("clinic", user.getJobTitle().getId());
        postParams.put("be_good_at", user.getLoginServerBean().getBe_good_at());
        postParams.put("introduce", user.getLoginServerBean().getIntroduce());
        postParams.put("real_name", user.getLoginServerBean().getReal_name());

        String idImage = getImageString(user.getIdImages());
        String zgImage = getImageString(user.getJobImages());

        postParams.put("shf_image", idImage);
        postParams.put("zg_image", zgImage);
        postParams.put("doctor_id", user.getId());
        postParams.put("bus_source", "13");
        Map<String, String> getParamMap = RequestTool.getCommonParams("1617");
        getParamMap.put("sign", RequestTool.getSign(getParamMap, postParams));
        return api.updatePersonalInfo(getParamMap,postParams);
    }

    @NonNull
    private String getImageString(List<String> idImages) {
        String zgImage ="";
        for (int i=0;i<idImages.size();i++){
            String image =idImages.get(i);
            zgImage=zgImage+image;
            if (i+1<idImages.size()){
                zgImage+= spliter;
            }
        }
        return zgImage;
    }

    public Observable<BaseData<AppVersion>> checkVersion(final String version) {
//        getParamMap.put("sign", RequestTool.getSign(getParamMap, null));
        Map<String, String> getParams = new HashMap<>();
//        source传
//        yyzs_doctor_online 线上版
//        yyzs_doctor_offline_cq 线下重庆
//        yyzs_doctor_offline_xt 线下邢台

        getParams.put("source", BuildConfig.source);
        getParams.put("app_version", version);
        return api.checkVersion(getParams);
    }
}
