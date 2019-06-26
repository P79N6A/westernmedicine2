package com.xywy.modifyjson;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.xywy.modifyjson.entity.DepartmentEntity;
import com.xywy.modifyjson.entity.HospitalEntity;
import com.xywy.modifyjson.entity.PromotionsMenuEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by DongJr on 2016/11/1.
 */

public class ModifyJsonUtil {


    //全部id后加的常量,全部的id = 父id+constant
    public static final String CONSTANT = "65535";


    private ModifyJsonUtil() {
    }

    /**
     * 专家网-医院添加全部
     *
     * @param fileName assets文件名称
     */
    public static void modifyHospital(String fileName, Context context) {
        boolean hasAll = false;

        String areaJson = getFromAssets(fileName, context);

        List<Object> list = new ArrayList<>();

        HospitalEntity hospitalEntity = GsonUtils.getInstance().getGson().fromJson(areaJson, HospitalEntity.class);
        List<HospitalEntity.DataEntity> data = hospitalEntity.getData();
        LinkedList<HospitalEntity.DataEntity> tempData = new LinkedList<>();

        for (HospitalEntity.DataEntity entity : data) {
            if (entity.getProvince().equals("全国")) {
                hasAll = true;
            }

            if ("全部医院".equals(entity.getHospital())) {
                list.add(entity.getProvince());
            } else {
                if (!list.contains(entity.getProvince())) {
                    HospitalEntity.DataEntity entity1 = new HospitalEntity.DataEntity();
                    setHospitalEmpty(entity1, entity.getProvince(), entity.getProvince_id());

                    list.add(entity.getProvince());
                    tempData.add(entity1);
                }
            }

            tempData.add(entity);
        }

        if (!hasAll) {
            HospitalEntity.DataEntity dataEntity = new HospitalEntity.DataEntity();
            setHospitalEmpty(dataEntity, "全国", "0");
            tempData.addFirst(dataEntity);
        }

        hospitalEntity.setData(tempData);

        String json = GsonUtils.getInstance().toJson(hospitalEntity);

        save(json, fileName);
    }

    /**
     * 专家网-科室
     *
     * @param fileName assets文件名称
     */
    public static void modifyDepartment(String fileName, Context context) {

        boolean hasAll = false;

        String areaJson = getFromAssets(fileName, context);

        List<Object> list = new ArrayList<>();

        DepartmentEntity departmentEntity = GsonUtils.getInstance().getGson().fromJson(areaJson, DepartmentEntity.class);
        List<DepartmentEntity.DataEntity> data = departmentEntity.getData();
        LinkedList<DepartmentEntity.DataEntity> tempData = new LinkedList<>();

        for (DepartmentEntity.DataEntity entity : data) {

            List<DepartmentEntity.DataEntity.SubsEntity> tempSubData = new ArrayList<>();

            if (entity.getPname().equals("全部科室")) {
                hasAll = true;
            }
            for (DepartmentEntity.DataEntity.SubsEntity subsEntity : entity.getSubs()) {

                if ("全部医生".equals(subsEntity.getSname())) {
                    list.add(entity.getPname());
                } else {
                    if (!list.contains(entity.getPname())) {

                        list.add(entity.getPname());
                        DepartmentEntity.DataEntity.SubsEntity subsEntity1 = new DepartmentEntity.DataEntity.SubsEntity();
                        subsEntity1.setSid(entity.getPid() + CONSTANT);
                        subsEntity1.setSname("全部医生");
                        tempSubData.add(subsEntity1);
                    }
                }
                tempSubData.add(subsEntity);
            }
            entity.setSubs(tempSubData);
            tempData.add(entity);
        }
        if (!hasAll) {
            DepartmentEntity.DataEntity entity = new DepartmentEntity.DataEntity();
            DepartmentEntity.DataEntity.SubsEntity subsEntity = new DepartmentEntity.DataEntity.SubsEntity();
            ArrayList<DepartmentEntity.DataEntity.SubsEntity> subsEntities = new ArrayList<>();
            subsEntity.setSname("全部医生");
            subsEntity.setSid("");
            subsEntities.add(subsEntity);
            entity.setPname("全部科室");
            entity.setPid("");
            entity.setSubs(subsEntities);

            tempData.addFirst(entity);
        }

        departmentEntity.setData(tempData);
        String json = GsonUtils.getInstance().toJson(departmentEntity);

        save(json, fileName);
    }

    /**
     * 限时促销 菜单项
     *
     * @param fileName   assets文件名称
     * @param outLevel   外层描述,比如全国,全部科室
     * @param innerLevel 内层描述,比如全部,全部医生
     */
    public static void modifyPromotionsMenu(String fileName, Context context, String outLevel, String innerLevel) {
        boolean hasAll = false;

        String areaJson = getFromAssets(fileName, context);

        List<Object> list = new ArrayList<>();

        PromotionsMenuEntity promotionsEntity = GsonUtils.getInstance().getGson().fromJson(areaJson, PromotionsMenuEntity.class);
        List<PromotionsMenuEntity.SubsEntity> data = promotionsEntity.getData();

        LinkedList<PromotionsMenuEntity.SubsEntity> tempData = new LinkedList<>();

        for (PromotionsMenuEntity.SubsEntity entity : data) {

            List<PromotionsMenuEntity.SubEntity> tempSubData = new ArrayList<>();

            if (entity.getName().equals(outLevel)) {
                hasAll = true;
            }
            for (PromotionsMenuEntity.SubEntity subsEntity : entity.getSubs()) {

                if (innerLevel.equals(subsEntity.getName())) {
                    list.add(entity.getName());
                } else {
                    if (!list.contains(entity.getName())) {

                        list.add(entity.getName());
                        PromotionsMenuEntity.SubEntity subsEntity1 = new PromotionsMenuEntity.SubEntity();
                        subsEntity1.setId(Integer.valueOf(entity.getId() + CONSTANT));
                        subsEntity1.setName(innerLevel);
                        tempSubData.add(subsEntity1);
                    }
                }
                tempSubData.add(subsEntity);
            }
            entity.setSubs(tempSubData);
            tempData.add(entity);
        }
        if (!hasAll) {
            PromotionsMenuEntity.SubsEntity entity = new PromotionsMenuEntity.SubsEntity();
            PromotionsMenuEntity.SubEntity subsEntity = new PromotionsMenuEntity.SubEntity();
            List<PromotionsMenuEntity.SubEntity> subsEntities = new ArrayList<>();
            subsEntity.setName(outLevel);
            subsEntity.setId(0);
            subsEntities.add(subsEntity);
            entity.setName(innerLevel);
            entity.setId(0);
            entity.setSubs(subsEntities);

            tempData.addFirst(entity);
        }

        promotionsEntity.setData(tempData);
        String json = GsonUtils.getInstance().toJson(promotionsEntity);

        save(json, fileName);
    }


    private static void setHospitalEmpty(HospitalEntity.DataEntity entity, String province, String id) {
        entity.setProvince(province);
        entity.setProvince_id(id);
        entity.setHospital("全部医院");
        entity.setCity("");
        entity.setCity_id("");
        entity.setId(id + CONSTANT);
        entity.setIs_yibao("");
        entity.setLevel("");
        entity.setType("");
    }

    /**
     * 得到assets文件夹中的文件
     *
     * @param fileName 文件名称
     * @return json
     */
    private static String getFromAssets(String fileName, Context context) {
        StringBuilder content = new StringBuilder();
        InputStream is = null;
        try {
            is = context.getAssets().open(fileName);

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String lineStr = "";
            while ((lineStr = reader.readLine()) != null) {
                content.append(lineStr);
            }

        } catch (IOException e) {
            Log.e("fileName err", "fileName 路径有问题");
            e.printStackTrace();

        } finally {

            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    /**
     * 存储到sdcard
     *
     * @param json     要存储的json
     * @param fileName 文件名称 需要加格式后缀.txt
     */
    private static void save(String json, String fileName) {

        FileOutputStream fout = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory().getCanonicalPath() + "/xywy");

            if (!file.exists()) {
                file.mkdirs();
            }

            File jsonFile = new File(file, fileName);
            fout = new FileOutputStream(jsonFile);
            byte[] bytes = json.getBytes();
            fout.write(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
