package com.xywy.askforexpert.model.liveshow;

import java.util.List;

/**
 * Created by bailiangjin on 2017/3/1.
 */

public class MyFansPageBean {

    /**
     * code : 10000
     * msg : 成功
     * data : [{"user":{"userid":12345,"name":"","sex":0,"synopsis":"","state":0,"lever":0},"userid":12345,"touserid":123123},{"user":{"userid":12345,"name":"","sex":0,"synopsis":"","state":0,"lever":0},"userid":12345,"touserid":123123},{"user":{"userid":12345,"name":"","sex":0,"synopsis":"","state":0,"lever":0},"userid":12345,"touserid":123123},{"user":{"userid":12345,"name":"","sex":0,"synopsis":"","state":0,"lever":0},"userid":12345,"touserid":123123},{"user":{"userid":12345,"name":"","sex":0,"synopsis":"","state":0,"lever":0},"userid":12345,"touserid":123123},{"user":{"userid":12345,"name":"","sex":0,"synopsis":"","state":0,"lever":0},"userid":12345,"touserid":123123},{"user":{"userid":12345,"name":"","sex":0,"synopsis":"","state":0,"lever":0},"userid":12345,"touserid":123123},{"user":{"userid":12345,"name":"","sex":0,"synopsis":"","state":0,"lever":0},"userid":12345,"touserid":123123},{"user":{"userid":12345,"name":"","sex":0,"synopsis":"","state":0,"lever":0},"userid":12345,"touserid":123123},{"user":{"userid":12345,"name":"","sex":0,"synopsis":"","state":0,"lever":0},"userid":12345,"touserid":123123},{"user":{"userid":12345,"name":"","sex":0,"synopsis":"","state":0,"lever":0},"userid":12345,"touserid":123123},{"user":{"userid":12345,"name":"","sex":0,"synopsis":"","state":0,"lever":0},"userid":12345,"touserid":123123},{"user":{"userid":12345,"name":"","sex":0,"synopsis":"","state":0,"lever":0},"userid":12345,"touserid":123123},{"user":{"userid":12345,"name":"","sex":0,"synopsis":"","state":0,"lever":0},"userid":12345,"touserid":123123},{"user":{"userid":12345,"name":"","sex":0,"synopsis":"","state":0,"lever":0},"userid":12345,"touserid":123123},{"user":{"userid":12345,"name":"","sex":0,"synopsis":"","state":0,"lever":0},"userid":12345,"touserid":123123}]
     */

    private int code;
    private String msg;
    private List<MyFansBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<MyFansBean> getData() {
        return data;
    }

    public void setData(List<MyFansBean> data) {
        this.data = data;
    }


}
