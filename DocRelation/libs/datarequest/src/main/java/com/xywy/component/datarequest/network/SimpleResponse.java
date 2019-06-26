package com.xywy.component.datarequest.network;

/**
 * Created by shijiazi on 16/3/23.
 */
public class SimpleResponse {
    public String response;
    public boolean intermediate = false;//是否是中间数据

    public SimpleResponse(String data) {
        response = data;
    }

    @Override
    public String toString() {
        return response;
    }
}
