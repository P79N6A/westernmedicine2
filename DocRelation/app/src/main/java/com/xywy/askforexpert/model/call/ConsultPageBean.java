package com.xywy.askforexpert.model.call;

/**
 * Created by bailiangjin on 16/5/31.
 */
public class ConsultPageBean {


    public static final int CALL_TYPE_FROM_HELP = 1;
    public static final int CALL_TYPE_USER_CALL_DOCTOR = 2;
    public static final int CALL_TYPE_DOCTOR_CALL_USER = 3;

    /**
     * 用户头像
     */
    private String headUrl;

    /**
     * 呼叫提示
     */
    private String callingNotice;

    /**
     * 患者Id
     */
    private String patientId;

    /**
     * 医生id
     */
    private String doctorId;
    /**
     * 帮扶类型
     */
    private String helpType;
    /**
     * 呼叫类型 1 健康帮扶 2 从IM唤起 用户呼叫医生 3:医生呼叫用户
     */
    private int callType;

    public ConsultPageBean(int callType, String headUrl, String doctorId, String patientId, String helpType) {
        this.callType = callType;
        this.headUrl = headUrl;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.helpType = helpType;
        if (CALL_TYPE_FROM_HELP == callType) {
            this.callingNotice = "正在呼叫帮扶员...";
        } else if (CALL_TYPE_USER_CALL_DOCTOR == callType) {
            this.callingNotice = "正在呼叫家庭医生...";
        } else if (CALL_TYPE_DOCTOR_CALL_USER == callType) {
            this.callingNotice = "正在呼叫患者,请稍后...";
        } else {
            this.callingNotice = "未知呼叫类型 传参错误";
        }
    }

    public String getCallingNotice() {
        return callingNotice;
    }


    public String getHeadUrl() {
        return headUrl;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public int getCallType() {
        return callType;
    }

    public String getHelpType() {
        return helpType;
    }
}
