package com.xywy.askforexpert.module.drug.bean;


import java.io.Serializable;
import java.util.List;

/**
 * 处方详情bean stone
 */
public class PrescriptionDetailBean implements Serializable {

    /**
     * prescription :
     * {"id":"207","pbn":"CF15288841413290","time":"2018-06-13 18:02:21","uname":"测试","usersex":"1","age":"23","dname":"张军红","paystate":"0","state":"2","expire":"2018-09-11 18:02:21","reason":"处方已过期","statusText":"已失效","diagnosis":"诊断信息","reviewer":"管理员","depart":"胸外科"}
     * drug :
     * [{"id":"266","gname":"修正 肺宁颗粒","specification":"10g*10/合","num":"1","price":"32.00","amount":"32.00","take_rate":"1","take_time":"饭后服用","take_num":"1","take_unit":"支","take_method":"口服","take_day":"0","drug_unit":"盒"},{"id":"267","gname":"通药制药 骨骼风痛片","specification":"0.31g*108/盒","num":"3","price":"36.00","amount":"108.00","take_rate":"1","take_time":"饭后服用","take_num":"1","take_unit":"支","take_method":"口服","take_day":"0","drug_unit":"盒"}]
     */

    public PrescriptionBean prescription;
    public List<DrugBean> drug;

    public PrescriptionBean getPrescription() {
        return prescription;
    }

    public void setPrescription(PrescriptionBean prescription) {
        this.prescription = prescription;
    }

    public List<DrugBean> getDrug() {
        return drug;
    }

    public void setDrug(List<DrugBean> drug) {
        this.drug = drug;
    }


}