package com.xywy.askforexpert.model;

import java.io.Serializable;

/**
 * Created by xugan on 2018/4/20.
 * 诊所的业务
 */

public class Service implements Serializable {
    public int work_id;  //业务的ID
    public String mark;  //业务审核的备注信息（包括审核不通过的原因）
    public int status;  //业务状态（-1未申请，0待审核，1审核通过开通，2审核不通过，3关闭）
    public String work_sign;  //业务名称
}
